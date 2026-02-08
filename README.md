# Book Store Service

[![Build Status](https://github.com/ric4r2/book-store-service/workflows/CI/badge.svg)](https://github.com/ric4r2/book-store-service/actions)
[![codecov](https://codecov.io/gh/ric4r2/book-store-service/branch/main/graph/badge.svg)](https://codecov.io/gh/ric4r2/book-store-service)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.0-brightgreen.svg)](https://spring.io/projects/spring-boot)

A professional, production-ready RESTful API for book store management built with Spring Boot 3, featuring JWT authentication, comprehensive API documentation, and modern DevOps practices.

## Features

### Core Functionality
- **Book Management** - CRUD operations for books with search and pagination
- **User Management** - Separate client and employee roles with role-based access control
- **Order Management** - Complete order processing workflow
- **JWT Authentication** - Secure token-based authentication with refresh tokens

### Technical Highlights
- **RESTful API** - Clean, versioned API design (`/api/v1/`)
- **OpenAPI/Swagger** - Interactive API documentation
- **Database Migration** - Flyway for version-controlled schema management
- **Caching** - Caffeine cache for improved performance
- **Soft Delete** - Non-destructive data removal
- **Audit Fields** - Automatic tracking of created/updated timestamps
- **Exception Handling** - Global exception handling with standardized error responses
- **Validation** - Comprehensive input validation with Bean Validation
- **Security** - Spring Security with JWT, CORS, and role-based authorization
- **Observability** - Spring Boot Actuator with Prometheus metrics
- **Docker Support** - Multi-stage Dockerfile and docker-compose for easy deployment
- **CI/CD** - GitHub Actions pipeline for automated testing and quality checks

## Quick Start

### Prerequisites
- Java 17 or higher
- Maven 3.6+
- PostgreSQL 12+ (or use Docker)
- Docker & Docker Compose (optional, for containerized deployment)

### Running with Docker (Recommended)

```bash
# Clone the repository
git clone https://github.com/ric4r2/book-store-service.git
cd book-store-service

# Start the application with Docker Compose
docker-compose up -d

# The API will be available at http://localhost:8080
# Swagger UI: http://localhost:8080/swagger-ui.html
```

### Running Locally

1. **Set up PostgreSQL database**
```bash
createdb bookstore
```

2. **Configure environment variables** (optional)
```bash
export DB_USERNAME=postgres
export DB_PASSWORD=postgres
export JWT_SECRET=your-secret-key-here
```

3. **Build and run**
```bash
mvn clean install
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

## API Documentation

### Swagger UI
Access the interactive API documentation at: **http://localhost:8080/swagger-ui.html**

### Sample Endpoints

#### Authentication
```bash
# Register a new client
curl -X POST http://localhost:8080/api/v1/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Doe",
    "email": "john@example.com",
    "password": "password123",
    "phone": "+1234567890",
    "address": "123 Main St"
  }'

# Login
curl -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "admin@bookstore.com",
    "password": "password123"
  }'
```

#### Books
```bash
# Get all books (requires authentication)
curl -X GET http://localhost:8080/api/v1/books \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"

# Search books with pagination
curl -X GET "http://localhost:8080/api/v1/books/search?search=gatsby&page=0&size=10" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"

# Create a new book (requires EMPLOYEE role)
curl -X POST http://localhost:8080/api/v1/books \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "New Book",
    "author": "Author Name",
    "price": 19.99,
    "genre": "Fiction",
    "ageGroup": "ADULT",
    "language": "ENGLISH",
    "pages": 300
  }'
```

### Database Initialization

After starting the application for the first time, you need to initialize the database with default users and sample books:

```bash
# Initialize database with sample data
Get-Content src/main/resources/db/init-data.sql | docker exec -i bookstore-postgres psql -U postgres -d bookstore
```

This creates:
- **1 Admin User** (EMPLOYEE role - can manage books)
- **2 Client Users** (CLIENT role - can browse and order)
- **6 Sample Books** (classics and popular titles)

### Default Users

After running the initialization script, you can login with these accounts:

| Email | Password | Role | Permissions |
|-------|----------|------|-------------|
| admin@bookstore.com | password123 | EMPLOYEE | Create/Update/Delete books, View orders |
| john@example.com | password123 | CLIENT | Browse books, Create orders |
| jane@example.com | password123 | CLIENT | Browse books, Create orders |

> **Note**: Change these passwords in production! The BCrypt hashes in `init-data.sql` are for development only.

### Authentication Flow

This API uses **JWT (JSON Web Token)** authentication:

1. **Register** or **Login** to get an access token
2. **Include the token** in the `Authorization` header for protected endpoints
3. **Token expires** in 15 minutes (use refresh token to get a new one)

**Example**:
```bash
# 1. Login
curl -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"admin@bookstore.com","password":"password123"}'

# Response includes accessToken
# {"success":true,"data":{"accessToken":"eyJhbGc...","role":"EMPLOYEE"}}

# 2. Use token to access protected endpoints
curl -X GET http://localhost:8080/api/v1/books \
  -H "Authorization: Bearer YOUR_ACCESS_TOKEN"
```

## Architecture

### Technology Stack
- **Framework**: Spring Boot 3.3.0
- **Language**: Java 17
- **Database**: PostgreSQL 16
- **ORM**: Spring Data JPA with Hibernate
- **Security**: Spring Security with JWT
- **Schema Management**: JPA (Hibernate DDL)
- **Caching**: Caffeine
- **API Docs**: SpringDoc OpenAPI 3
- **Build Tool**: Maven
- **Containerization**: Docker

### Project Structure
```
src/main/java/com/bookstore/
├── config/              # Configuration classes
│   ├── SecurityConfig.java
│   ├── OpenApiConfig.java
│   ├── CacheConfig.java
│   └── JpaConfig.java
├── controller/          # REST controllers
│   ├── AuthController.java
│   └── BookController.java
├── dto/                 # Data Transfer Objects
│   ├── request/
│   └── response/
├── exception/           # Exception handling
│   ├── GlobalExceptionHandler.java
│   └── custom exceptions
├── model/               # JPA entities
│   ├── base/
│   └── enums/
├── repository/          # Spring Data repositories
├── security/            # Security components
│   └── jwt/
└── service/             # Business logic
```

## Testing

```bash
# Run all tests
mvn test

# Run tests with coverage
mvn clean test jacoco:report

# View coverage report
open target/site/jacoco/index.html

# Run integration tests
mvn verify -P integration-tests
```

## Monitoring

### Actuator Endpoints
- Health: `http://localhost:8080/actuator/health`
- Metrics: `http://localhost:8080/actuator/metrics`
- Prometheus: `http://localhost:8080/actuator/prometheus`

## Configuration

### Application Profiles
- **dev** - Development with PostgreSQL
- **test** - Testing with H2 in-memory database
- **prod** - Production configuration

### Environment Variables
| Variable | Description | Default |
|----------|-------------|---------|
| `SPRING_PROFILES_ACTIVE` | Active profile | dev |
| `DATABASE_URL` | Database connection URL | jdbc:postgresql://localhost:5432/bookstore |
| `DB_USERNAME` | Database username | postgres |
| `DB_PASSWORD` | Database password | postgres |
| `JWT_SECRET` | JWT signing secret | (see application.yml) |

## Deployment

### Docker Deployment
```bash
# Build image
docker build -t bookstore-service:1.0.0 .

# Run container
docker run -p 8080:8080 \
  -e SPRING_PROFILES_ACTIVE=prod \
  -e DATABASE_URL=jdbc:postgresql://host:5432/bookstore \
  -e DB_USERNAME=postgres \
  -e DB_PASSWORD=password \
  -e JWT_SECRET=your-secret \
  bookstore-service:1.0.0
```

### Production Checklist
- [ ] Update JWT secret to a strong, random value
- [ ] Configure production database credentials
- [ ] Enable HTTPS/TLS
- [ ] Set up database backups
- [ ] Configure logging aggregation
- [ ] Set up monitoring and alerting
- [ ] Review and adjust rate limiting
- [ ] Update CORS allowed origins

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Author

**Ricardo Cruz**
- GitHub: [@ric4r2](https://github.com/ric4r2)
- LinkedIn: [José Ricardo Cruz Tapia](https://www.linkedin.com/in/jos%C3%A9-ricardo-cruz-tapia/)
- Email: ric4r2.0201@gmail.com

## Acknowledgments

- Spring Boot team for the excellent framework
- All contributors who help improve this project

---

If you find this project useful, please consider giving it a star!
