# MongoDB Sharded Cluster with Docker Compose

This setup creates a MongoDB sharded cluster with the following components:

## Architecture

- **3 Config Servers** (configsvr1, configsvr2, configsvr3) - Replica Set: `configrs`
- **3 Shards** with 3 replica set members each:
  - Shard 1: `shard1rs` (shard1svr1, shard1svr2, shard1svr3)
  - Shard 2: `shard2rs` (shard2svr1, shard2svr2, shard2svr3)
  - Shard 3: `shard3rs` (shard3svr1, shard3svr2, shard3svr3)
- **2 Mongos Routers** (mongos1, mongos2)
- **MongoDB Compass** (Web-based GUI) - adminmongo interface

## Authentication

- **Username**: `admin`
- **Password**: `password123`

## Port Mapping

- **Mongos Routers**: 
  - mongos1: `localhost:27017`
  - mongos2: `localhost:27018`
- **MongoDB Compass Web UI**: `localhost:8081`
- **Config Servers**: `localhost:27019-27021`
- **Shard Servers**: `localhost:27022-27030`

## Quick Start

1. **Start the cluster**:
   ```bash
   docker-compose up -d
   ```

2. **Initialize the cluster** (run after containers are up):
   ```bash
   ./setup-cluster.sh
   ```

3. **Connect to the cluster**:
   ```bash
   mongosh mongodb://admin:password123@localhost:27017/admin
   ```

4. **Access MongoDB Compass Web UI**:
   - Open your browser and go to: `http://localhost:8081`
   - You'll need to manually add a connection to your mongos router
   - Connection details: `mongodb://mongos1:27017` (or use `mongos2:27017` for the secondary router)

## Useful Commands

### Check cluster status:
```bash
docker exec -it mongos1 mongosh mongodb://admin:password123@localhost:27017/admin --eval 'sh.status()'
```

### Enable sharding for a database:
```javascript
use admin
sh.enableSharding("myDatabase")
```

### Shard a collection:
```javascript
use myDatabase
sh.shardCollection("myDatabase.myCollection", { "_id": "hashed" })
```

### Check shard distribution:
```javascript
use myDatabase
db.myCollection.getShardDistribution()
```

## Data Persistence

All data is persisted using Docker volumes:
- Config servers: `configsvr1_data`, `configsvr2_data`, `configsvr3_data`
- Shard servers: `shard1svr1_data` through `shard3svr3_data`

## Stopping the Cluster

```bash
docker-compose down
```

To remove all data volumes as well:
```bash
docker-compose down -v
```

## Installing client on ubuntu

```bash
curl -fsSL https://www.mongodb.org/static/pgp/server-8.0.asc | sudo gpg -o /usr/share/keyrings/mongodb-server-8.0.gpg --dearmor
echo "deb [ arch=amd64,arm64 signed-by=/usr/share/keyrings/mongodb-server-8.0.gpg ] https://repo.mongodb.org/apt/ubuntu noble/mongodb-org/8.0 multiverse" | sudo tee /etc/apt/sources.list.d/mongodb-org-8.0.list
sudo apt update
sudo apt install -y mongodb-mongosh mongodb-org-tools
```

## Connecting

### Command Line Access
```bash
# Connect to primary mongos
mongosh mongodb://localhost:27017

# Connect to secondary mongos
mongosh mongodb://localhost:27018

# Check cluster status
mongosh mongodb://localhost:27017 --eval "sh.status()"
```

### Web-based Access
- **MongoDB Compass Web UI**: Access `http://localhost:8081` in your browser
- **Initial Setup**: On first access, add a new connection with:
  - Connection Name: `MongoDB Sharded Cluster`
  - Connection String: `mongodb://mongos1:27017`
  - Leave authentication fields empty (no auth configured)
- The web interface provides:
  - Visual database exploration
  - Collection browsing and editing
  - Query building and execution
  - Index management
  - Real-time performance metrics

## Scaling

To add more mongos routers, simply add more services to the docker-compose.yml file following the same pattern as mongos1 and mongos2.

## Security Notes

- Change the default password in production
- Consider using Docker secrets for sensitive data
- Enable SSL/TLS for production deployments
- Configure proper firewall rules

## Troubleshooting

1. **Containers not starting**: Check Docker logs with `docker-compose logs [service-name]`
2. **Replica set issues**: Ensure all containers can communicate on the mongodb-network
3. **Authentication issues**: Verify username/password and ensure admin user was created properly
4. **Connection issues**: Check that ports are not already in use on your host system