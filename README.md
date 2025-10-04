# Lost & Found Backend System

A Spring Boot application for managing lost and found items.

## Project Structure

```
src/main/java/com/myorg/lostfound/
├── LostFoundApplication.java          # Main application class
├── controller/                        # REST controllers
├── service/                          # Business logic services
├── repository/                       # Data access layer
├── model/                           # Entity classes
├── dto/                             # Data transfer objects
├── config/                          # Configuration classes
└── util/                            # Utility classes
```

## Prerequisites

- Java 11 or higher
- Maven 3.6 or higher
- MySQL 8.0 or higher

## Setup Instructions

1. **Clone the repository** (if applicable)

2. **Configure Database**
   - Create a MySQL database named `lostfound_db`
   - Update `src/main/resources/application.properties` with your database credentials

3. **Build the project**
   ```bash
   mvn clean install
   ```

4. **Run the application**
   ```bash
   mvn spring-boot:run
   ```

   Or run the JAR file:
   ```bash
   java -jar target/lostfound-backend-1.0.0.jar
   ```

## Default Configuration

The application will start on port 8080. You can access:
- Health check: `http://localhost:8080/actuator/health`
- API documentation: `http://localhost:8080/swagger-ui.html` (if Swagger is added)

## Dependencies Included

- **Spring Boot Starter Web**: REST API development
- **Spring Boot Starter Data JPA**: Database operations
- **Spring Boot Starter Validation**: Input validation
- **MySQL Connector**: MySQL database connectivity
- **Lombok**: Reduces boilerplate code

## Development Notes

- The project uses Java 11
- Package structure follows Spring Boot best practices
- Ready for adding REST controllers, services, and repositories
- Database configuration can be added in `application.properties`

## Next Steps

1. Add database configuration in `src/main/resources/application.properties`
2. Create entity models in the `model` package
3. Implement repositories in the `repository` package
4. Add business logic in the `service` package
5. Create REST endpoints in the `controller` package
