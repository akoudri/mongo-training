# MongoDB Replica Set Setup

This configuration provides a simple MongoDB replica set with 3 nodes for development and testing purposes.

## Architecture

- **Replica Set Name**: `rs0`
- **Primary Node**: `mongo-primary` (port 27017)
- **Secondary Node 1**: `mongo-secondary1` (port 27018)
- **Secondary Node 2**: `mongo-secondary2` (port 27019)

## Quick Start

### 1. Start the Cluster
```bash
cd docker
docker-compose -f docker-compose.rs.yaml up -d
```

### 2. Initialize the Replica Set
```bash
./init-replica-set.sh
```

### 3. Connect to the Cluster
```bash
# Connect to primary
mongosh mongodb://admin:password123@localhost:27017/admin

# Connect with full replica set connection string
mongosh 'mongodb://admin:password123@localhost:27017,localhost:27018,localhost:27019/admin?replicaSet=rs0'
```

## Management Commands

### Check Replica Set Status
```javascript
// Connect to any node and run:
rs.status()
rs.conf()
```

### Check Current Primary
```javascript
db.hello()
```

### Force Election (if needed)
```javascript
// Connect to a secondary and run:
rs.stepDown()
```

### Add/Remove Members (Advanced)
```javascript
// Add a new member
rs.add("new-mongo-node:27017")

// Remove a member
rs.remove("mongo-secondary2:27017")
```

## Testing Replica Set Functionality

### 1. Test Write Concern
```javascript
use testdb
db.testcollection.insertOne(
  { name: "test", timestamp: new Date() },
  { writeConcern: { w: "majority", j: true } }
)
```

### 2. Test Read Preference
```javascript
// Read from secondary (connect to secondary node first)
db.getMongo().setReadPref("secondary")
db.testcollection.find()
```

### 3. Test Failover
```bash
# Stop primary container
docker stop mongo-primary

# Check which node becomes primary
docker exec -it mongo-secondary1 mongosh --eval "db.hello()"
```

## Connection Strings

| Purpose | Connection String |
|---------|------------------|
| Primary Only | `mongodb://admin:password123@localhost:27017/admin` |
| Full Replica Set | `mongodb://admin:password123@localhost:27017,localhost:27018,localhost:27019/admin?replicaSet=rs0` |
| Read from Secondary | `mongodb://admin:password123@localhost:27017,localhost:27018,localhost:27019/admin?replicaSet=rs0&readPreference=secondary` |

## Cleanup

### Stop and Remove Containers
```bash
docker-compose -f docker-compose.rs.yaml down
```

### Remove All Data (⚠️ Destructive)
```bash
docker-compose -f docker-compose.rs.yaml down -v
```

## Troubleshooting

### Check Container Logs
```bash
docker-compose -f docker-compose.rs.yaml logs mongo-primary
docker-compose -f docker-compose.rs.yaml logs mongo-secondary1
docker-compose -f docker-compose.rs.yaml logs mongo-secondary2
```

### Replica Set Not Initializing
1. Ensure all containers are running: `docker ps`
2. Check network connectivity between containers
3. Verify the initialization script ran successfully
4. Manually run replica set commands if needed

### Connection Issues
1. Verify ports are not in use: `netstat -tulpn | grep 2701`
2. Check firewall settings
3. Ensure Docker network is properly configured

## Security Notes

- **Default Credentials**: `admin/password123` (change in production)
- **Network**: Containers communicate on `mongodb-network`
- **Data Persistence**: All data is stored in Docker volumes
- **Production**: Enable SSL/TLS and use proper authentication mechanisms