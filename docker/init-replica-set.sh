#!/bin/bash

echo "🚀 Starting MongoDB Replica Set initialization..."

# Wait for MongoDB containers to be ready
echo "⏳ Waiting for MongoDB containers to start..."
sleep 10

# Initialize the replica set
echo "🔧 Initializing replica set 'rs0'..."
docker exec -it mongo-primary mongosh --eval "
rs.initiate({
  _id: 'rs0',
  members: [
    { _id: 0, host: 'mongo-primary:27017', priority: 2 },
    { _id: 1, host: 'mongo-secondary1:27017', priority: 1 },
    { _id: 2, host: 'mongo-secondary2:27017', priority: 1 }
  ]
})
"

# Wait for replica set to stabilize
echo "⏳ Waiting for replica set to stabilize..."
sleep 15

# Create admin user
echo "👤 Creating admin user..."
docker exec -it mongo-primary mongosh --eval "
use admin
db.createUser({
  user: 'admin',
  pwd: 'password123',
  roles: [
    { role: 'root', db: 'admin' }
  ]
})
"

# Check replica set status
echo "📊 Checking replica set status..."
docker exec -it mongo-primary mongosh --eval "rs.status()"

echo "✅ MongoDB Replica Set initialization completed!"
echo ""
echo "🔗 Connection strings:"
echo "   Primary:    mongodb://admin:password123@localhost:27017/admin"
echo "   Secondary1: mongodb://admin:password123@localhost:27018/admin"
echo "   Secondary2: mongodb://admin:password123@localhost:27019/admin"
echo "   Full RS:    mongodb://admin:password123@localhost:27017,localhost:27018,localhost:27019/admin?replicaSet=rs0"
echo ""
echo "📝 Usage:"
echo "   Start cluster: docker-compose -f docker-compose.rs.yaml up -d"
echo "   Stop cluster:  docker-compose -f docker-compose.rs.yaml down"
echo "   Connect:       mongosh 'mongodb://admin:password123@localhost:27017,localhost:27018,localhost:27019/admin?replicaSet=rs0'"