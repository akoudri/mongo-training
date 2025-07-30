#!/bin/bash

# MongoDB sharded cluster connection string (no authentication)
MONGOS_URI="mongodb://localhost:27017"

echo "Installing data into MongoDB sharded cluster..."

# Test connection and cluster readiness
echo "Testing MongoDB connection..."
if ! mongosh "$MONGOS_URI" --eval "db.runCommand('ping')" >/dev/null 2>&1; then
    echo "ERROR: Cannot connect to MongoDB. Make sure the cluster is running and initialized."
    echo "Run: cd docker && ./setup-cluster.sh"
    exit 1
fi

echo "Testing cluster sharding status..."
if ! mongosh "$MONGOS_URI" --eval "sh.status()" >/dev/null 2>&1; then
    echo "ERROR: Cluster is not properly initialized for sharding."
    echo "Run: cd docker && ./setup-cluster.sh"
    exit 1
fi

echo "Connected successfully to sharded cluster!"
URI="$MONGOS_URI"

for db in sample*/; do
  dbname=$(basename "$db" | sed 's/^sample_//')
  
  echo "Processing database: $dbname"
  
  # Enable sharding for the database
  echo "Enabling sharding for database: $dbname"
  mongosh "$URI" --eval "sh.enableSharding('$dbname')" || {
    echo "Warning: Could not enable sharding for database $dbname"
  }
  
  for colfile in "$db"*.json; do
    colname=$(basename "$colfile" .json)
    
    echo "Importing collection: $dbname.$colname"
    
    # Import data through mongos router
    mongoimport --uri "$URI" --db "$dbname" --collection "$colname" --file "$colfile" --drop
    
    # Configure sharding for the collection (using hashed _id as shard key)
    # You may want to customize the shard key based on your data structure
    mongosh "$URI" --eval "
      use $dbname;
      try {
        sh.shardCollection('$dbname.$colname', { '_id': 'hashed' });
        print('Sharded collection: $dbname.$colname');
      } catch (e) {
        print('Warning: Could not shard collection $dbname.$colname - ' + e.message);
      }
    "
  done
done

echo "Data installation complete. Checking cluster status..."
mongosh "$URI" --eval "sh.status()"

