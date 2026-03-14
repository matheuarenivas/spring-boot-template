# Spring Boot REST API Template

A clean, opinionated starter template for building REST APIs with Spring Boot and JdbcTemplate.

## Project Structure

```
src/main/java/com/template/api/
├── Application.java              # Entry point
├── config/                       # CORS, web config, etc.
├── controller/                   # REST endpoints (HTTP layer)
├── service/                      # Business logic
├── repository/                   # Data access (JdbcTemplate)
├── model/                        # Internal domain objects (POJOs)
├── dto/
│   ├── request/                  # Incoming request bodies
│   └── response/                 # Outgoing response bodies
├── query/                        # SQL query constants
├── mapper/                       # RowMappers + DTO converters
├── exception/                    # Custom exceptions + global handler
└── util/                         # Shared helpers
```

## Tech Stack

- **Java 21** + **Spring Boot 3.3**
- **JdbcTemplate** for data access (no JPA/Hibernate)
- **HikariCP** connection pool (auto-configured)
- **H2** in-memory DB for local dev
- **PostgreSQL** driver included for production
- **Jakarta Validation** for request validation

## Quick Start

```bash
# Clone the template
git clone https://github.com/YOUR_USERNAME/spring-boot-api-template.git my-new-api
cd my-new-api

# Run with H2 (no external DB needed)
./mvnw spring-boot:run

# Test endpoints
curl http://localhost:8080/api/examples
curl http://localhost:8080/api/examples/1

# View H2 console
# Open http://localhost:8080/h2-console
# JDBC URL: jdbc:h2:mem:devdb | User: sa | Password: (blank)
```

## Customizing for Your Project

1. **Rename the base package**: Replace `com.template.api` with your own (e.g., `com.yourname.projectname`)
2. **Update `pom.xml`**: Change `groupId`, `artifactId`, and `description`
3. **Add your entities**: Copy the Example* files as a starting point for each new entity
4. **Switch databases**: Update `application-prod.properties` with your real DB credentials

## Adding a New Entity

For each new entity (e.g., `Product`), create:

1. `model/Product.java` — POJO
2. `dto/request/CreateProductRequest.java` — request body with validation
3. `dto/response/ProductResponse.java` — API response shape
4. `query/ProductQueries.java` — SQL constants
5. `mapper/ProductRowMapper.java` — ResultSet → POJO
6. `mapper/ProductMapper.java` — DTO ↔ model conversions
7. `repository/ProductRepository.java` — data access
8. `service/ProductService.java` — business logic
9. `controller/ProductController.java` — REST endpoints

## Running with a Real Database

```bash
# Set environment variables
export DB_USERNAME=myuser
export DB_PASSWORD=mypassword

# Run with production profile
./mvnw spring-boot:run -Dspring-boot.run.profiles=prod
```

## Request Flow

```
HTTP Request → Controller → Service → Repository → JdbcTemplate → Database
                  ↓             ↓           ↓
              Validates     Business     SQL from
              via @Valid    logic        query constants
```
