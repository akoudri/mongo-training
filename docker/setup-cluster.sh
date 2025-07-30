#!/bin/bash

echo "Setting up MongoDB Sharded Cluster..."

# Wait for containers to be ready
echo "Waiting for containers to start..."
sleep 10

# Initialize Config Server Replica Set
echo "Initializing Config Server Replica Set..."
docker exec -it configsvr1 mongosh --eval "
rs.initiate({
  _id: 'configrs',
  configsvr: true,
  members: [
    { _id: 0, host: 'configsvr1:27017' },
    { _id: 1, host: 'configsvr2:27017' },
    { _id: 2, host: 'configsvr3:27017' }
  ]
})
"

# Wait for config server replica set to be ready
echo "Waiting for config server replica set to be ready..."
sleep 15

# Initialize Shard 1 Replica Set
echo "Initializing Shard 1 Replica Set..."
docker exec -it shard1svr1 mongosh --eval "
rs.initiate({
  _id: 'shard1rs',
  members: [
    { _id: 0, host: 'shard1svr1:27017' },
    { _id: 1, host: 'shard1svr2:27017' },
    { _id: 2, host: 'shard1svr3:27017' }
  ]
})
"

# Initialize Shard 2 Replica Set
echo "Initializing Shard 2 Replica Set..."
docker exec -it shard2svr1 mongosh --eval "
rs.initiate({
  _id: 'shard2rs',
  members: [
    { _id: 0, host: 'shard2svr1:27017' },
    { _id: 1, host: 'shard2svr2:27017' },
    { _id: 2, host: 'shard2svr3:27017' }
  ]
})
"

# Initialize Shard 3 Replica Set
echo "Initializing Shard 3 Replica Set..."
docker exec -it shard3svr1 mongosh --eval "
rs.initiate({
  _id: 'shard3rs',
  members: [
    { _id: 0, host: 'shard3svr1:27017' },
    { _id: 1, host: 'shard3svr2:27017' },
    { _id: 2, host: 'shard3svr3:27017' }
  ]
})
"

# Wait for shard replica sets to be ready
echo "Waiting for shard replica sets to be ready..."
sleep 20

# Add shards to the cluster
echo "Adding shards to the cluster..."
docker exec -it mongos1 mongosh --eval "
sh.addShard('shard1rs/shard1svr1:27017,shard1svr2:27017,shard1svr3:27017')
sh.addShard('shard2rs/shard2svr1:27017,shard2svr2:27017,shard2svr3:27017')
sh.addShard('shard3rs/shard3svr1:27017,shard3svr2:27017,shard3svr3:27017')
"

echo "Waiting for shards to be added..."
sleep 10

# Verify cluster is ready for sharding operations
echo "Verifying cluster is ready..."
for i in {1..5}; do
  if docker exec -it mongos1 mongosh --eval "sh.status()" >/dev/null 2>&1; then
    echo "Cluster is ready!"
    break
  else
    echo "Waiting for cluster to be ready... (attempt $i/5)"
    sleep 5
  fi
done

# Check cluster status
echo "Checking cluster status..."
docker exec -it mongos1 mongosh --eval "sh.status()"

echo "MongoDB Sharded Cluster setup complete!"
echo "You can connect to the cluster using:"
echo "mongosh mongodb://localhost:27017"
echo "or"
echo "mongosh mongodb://localhost:27018"