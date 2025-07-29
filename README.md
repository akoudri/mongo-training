# MongoDB Airbnb Listings Management System

A comprehensive Spring Boot application for managing Airbnb listings data using MongoDB. This project demonstrates both **MongoRepository** and **MongoTemplate** approaches for data access with a complete REST API.

## 🏗️ Architecture

- **Spring Boot 3.5.4** with Java 17
- **MongoDB** for data persistence
- **Lombok** for reducing boilerplate code
- **Maven** for dependency management
- **Spring Data MongoDB** for data access
- **Validation** using Jakarta Validation API

## 📁 Project Structure

```
src/main/java/com/akfc/training/mongodb/
├── MongodbApplication.java          # Main application class
├── model/                          # Entity classes
│   ├── ListingAndReview.java      # Main listing entity
│   ├── Host.java                  # Host information
│   ├── Address.java               # Location data
│   ├── Images.java                # Image URLs
│   ├── Availability.java          # Availability calendar
│   ├── ReviewScores.java          # Review ratings
│   └── Review.java                # Individual reviews
├── repository/                     # Spring Data repositories
│   └── ListingAndReviewRepository.java
├── service/                       # Business logic
│   └── ListingAndReviewService.java
├── controller/                    # REST endpoints
│   └── ListingAndReviewController.java
└── config/                        # Configuration
    └── DataLoader.java            # Sample data loader
```

## 🚀 Features

### MongoDB Repository Features
- Standard CRUD operations
- Query by property type, room type, host name
- Price range filtering
- Superhost listings
- Pagination support

### MongoTemplate Features
- Complex custom queries with multiple criteria
- Geospatial queries (find nearby listings)
- Aggregation pipelines for statistics
- Bulk updates and operations
- Text search functionality

## 📚 API Endpoints

### Basic CRUD Operations
```
GET    /api/listings              # Get all listings
GET    /api/listings/paginated    # Get paginated listings
GET    /api/listings/{id}         # Get listing by ID
POST   /api/listings              # Create new listing
PUT    /api/listings/{id}         # Update listing
DELETE /api/listings/{id}         # Delete listing
```

### Search Operations
```
GET /api/listings/search/property-type/{type}    # By property type
GET /api/listings/search/room-type/{type}        # By room type
GET /api/listings/search/host/{hostName}         # By host name
GET /api/listings/search/price-range             # By price range
GET /api/listings/search/superhosts              # Superhost listings
```

### Advanced Search (MongoTemplate)
```
GET /api/listings/search/custom                  # Multi-criteria search
GET /api/listings/search/near                    # Geospatial search
GET /api/listings/search/text                    # Text search
```

### Bulk Operations
```
POST   /api/listings/bulk                        # Create multiple listings
DELETE /api/listings/property-type/{type}        # Delete by property type
```

### Update Operations
```
PATCH /api/listings/price/property-type/{type}   # Update price by type
PATCH /api/listings/host/{hostId}/response-time  # Update host response time
```

### Analytics & Statistics
```
GET /api/listings/stats/property-types           # Property type statistics
GET /api/listings/stats/top-hosts               # Top hosts by listings count
```

## 🛠️ Setup and Running

### Prerequisites
- Java 17+
- Maven 3.6+
- MongoDB instance (or use the provided Docker setup)

### Configuration
Update `src/main/resources/application.properties`:
```properties
spring.application.name=mongodb
spring.data.mongodb.uri=mongodb://localhost:27017/airbnb
```

### Running the Application
```bash
# Compile the project
mvn clean compile

# Run the application
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

### Sample Data
The application includes a `DataLoader` that automatically creates sample listings on startup if the database is empty.

## 🔍 Key Implementation Details

### Lombok Usage
All model classes use Lombok annotations:
- `@Data` - generates getters, setters, toString, equals, hashCode
- `@Builder` - provides builder pattern
- `@NoArgsConstructor`, `@AllArgsConstructor` - constructors
- `@RequiredArgsConstructor` - for dependency injection

### MongoDB Mapping
- `@Document(collection = "listingsAndReviews")` - maps to MongoDB collection
- `@Field("field_name")` - maps to MongoDB field names
- `@Id` - marks the primary key field

### Repository vs Template
**MongoRepository** - Used for:
- Simple queries and CRUD operations
- Standard Spring Data query methods
- Method name-based queries

**MongoTemplate** - Used for:
- Complex queries with multiple criteria
- Aggregation pipelines
- Geospatial queries
- Custom update operations
- Bulk operations

### Example Usage

#### Search by Custom Criteria
```bash
curl "http://localhost:8080/api/listings/search/custom?propertyType=Apartment&minAccommodates=4&maxPrice=200&country=United States"
```

#### Find Nearby Listings
```bash
curl "http://localhost:8080/api/listings/search/near?longitude=-73.9857&latitude=40.7484&maxDistance=1000"
```

#### Get Property Type Statistics
```bash
curl "http://localhost:8080/api/listings/stats/property-types"
```

## 🧪 Testing
The project includes comprehensive integration testing using Spring Boot Test framework.

## 📈 Performance Considerations
- Indexes should be created on frequently queried fields
- Use pagination for large result sets
- Consider geospatial indexes for location-based queries
- Aggregation pipelines are optimized for complex analytics

## 🔧 Extending the Application
- Add more sophisticated search filters
- Implement caching with Redis
- Add user authentication and authorization
- Create additional aggregation pipelines for business intelligence
- Add real-time notifications using WebSockets

This application serves as a comprehensive example of MongoDB integration with Spring Boot, showcasing both traditional repository patterns and advanced template-based operations.