# Course Management Backend Application

This is a Spring Boot backend application for managing online courses. The application provides a comprehensive REST API for course management, user authentication, search, enrollment, ordering, and payment processing.

## Table of Contents
- [Prerequisites](#prerequisites)
- [Technologies Used](#technologies-used)
- [Project Setup](#project-setup)
- [Database Configuration](#database-configuration)
- [Environment Variables](#environment-variables)
- [Running the Application](#running-the-application)
- [API Documentation](#api-documentation)
- [Authentication](#authentication)
- [Features](#features)
- [Project Structure](#project-structure)
- [Troubleshooting](#troubleshooting)

## Prerequisites

To run this application, you need to have the following installed:

- Java Development Kit (JDK) 21
- Docker and Docker Compose (for running the MySQL database)
- Git (for version control)

You do not need to install Maven separately as the project includes a Maven Wrapper (mvnw).

## Technologies Used

- **Spring Boot 3.4.2**: Core framework
- **Spring Security**: For authentication and authorization
- **Spring Data JPA**: For database access
- **MySQL**: Database
- **JWT**: For stateless authentication
- **Lombok**: To reduce boilerplate code
- **MapStruct**: For object mapping
- **Springdoc OpenAPI**: For API documentation (Swagger)
- **Thymeleaf**: For email templates and PDF generation
- **Flying Saucer and iText PDF**: For PDF generation
- **Spring Mail**: For email functionality
- **Spring Boot Docker Compose**: For integrating with Docker Compose

## Project Setup

1. Clone the repository:
   ```
   git clone <repository-url>
   ```

2. Install dependencies:
   ```
   ./mvnw clean install -DskipTests
   ```

## Database Configuration

The application uses MySQL as the database. The configuration is defined in `compose.yaml`:

```yaml
services:
  mysql:
    image: 'mysql:latest'
    environment:
      - 'MYSQL_DATABASE=courses-db'
      - 'MYSQL_PASSWORD=secret'
      - 'MYSQL_ROOT_PASSWORD=verysecret'
      - 'MYSQL_USER=myuser'
    ports:
      - '3307:3306'
```

This will create a MySQL container with:
- Database name: `courses-db`
- User: `myuser`
- Password: `secret`
- Root password: `verysecret`
- Port: `3307` (mapped to internal port `3306`)

## Environment Variables

The application requires several environment variables to be set. Create a `.env` file in the root directory with the following variables:

```
DATABASE_URL=localhost:3307
DATABASE_USERNAME=myuser
DATABASE_PASSWORD=secret
JWTSECRET=your-jwt-secret
EMAIL_USERNAME=your-email@gmail.com
EMAIL_PASSWORD=your-email-password
```

The application uses the `spring-dotenv` library to load these variables.

## Running the Application

### Using Maven Wrapper:

```
./mvnw spring-boot:run
```

Or with explicit profiles:

```
./mvnw spring-boot:run -Dspring.profiles.active=dev
```

### Using Docker Compose:

1. Start the MySQL database:
   ```
   docker compose up -d
   ```

2. Run the application:
   ```
   ./mvnw spring-boot:run
   ```

### Using the JAR file:

1. Build the JAR:
   ```
   ./mvnw clean package -DskipTests
   ```

2. Run the JAR:
   ```
   java -jar target/idata2306.jar
   ```

## API Documentation

Once the application is running, you can access the Swagger UI documentation at:

```
http://localhost:8080/swagger-ui/index.html
```

This provides interactive documentation for all available endpoints.

## Authentication

The application uses JWT (JSON Web Token) for authentication. To access protected endpoints:

1. Authenticate using the `/api/auth/login` endpoint with valid credentials
2. Use the returned JWT token in the Authorization header for subsequent requests:
   ```
   Authorization: Bearer <your_jwt_token>
   ```

### Default Users

The application initializes with the following users:

- **Admin User**:
    - Email: `dave`
    - Password: `Dangerous2024`
    - Role: `ADMIN`

- **Regular User**:
    - Email: `chuck`
    - Password: `Nunchucks2024`
    - Role: `USER`

- **Additional Admin**:
    - Email: `admin@example.com`
    - Password: `adminpass`
    - Role: `ADMIN`

- **Additional User**:
    - Email: `user@example.com`
    - Password: `userpass`
    - Role: `USER`

- **Provider User**:
    - Email: `provider@example.com`
    - Password: `providerpass`
    - Role: `PROVIDER`

- **Additional Provider User**:
    - Email: `provider2@example.com`
    - Password: `providerpass`
    - Role: `PROVIDER`

## Features

The application provides the following main features:

- **User Management**: Registration, authentication, and profile management
- **Course Management**: Create, read, update, and delete courses
- **Provider Management**: Manage course providers
- **Enrollment Management**: Enroll and track course participation
- **Order Processing**: Process orders for paid courses
- **Payment Processing**: Handle payment for orders
- **PDF Receipt Generation**: Generate PDF receipts for completed orders
- **Email Notifications**: Send email confirmations for orders and enrollments
- **Search Functionality**: Advanced generic search can search for topics, users, categories, and courses by name, description and other criteria
- **Review System**: Allow users to leave reviews for courses

## Project Structure

The application follows a standard Spring Boot application structure:

- **Controllers**: Handle HTTP requests and responses (`no.ntnu.idata2306.controller`)
- **Services**: Implement business logic (`no.ntnu.idata2306.service`)
- **Repositories**: Interface with the database (`no.ntnu.idata2306.repository`)
- **Models**: Define data structures (`no.ntnu.idata2306.model`)
- **DTOs**: Data Transfer Objects for request/response (`no.ntnu.idata2306.dto`)
- **Mappers**: Convert between DTOs and entities (`no.ntnu.idata2306.mapper`)
- **Config**: Configuration classes (`no.ntnu.idata2306.config`)
- **Security**: Authentication and authorization (`no.ntnu.idata2306.security`)
- **Exceptions**: Exception handler and custom exception classes (`no.ntnu.idata2306.exception`)
- **Enums**: Enumeration types (`no.ntnu.idata2306.enums`)
- **Utils**: Utility classes (`no.ntnu.idata2306.util`)

## Troubleshooting

### Database Connection Issues

- Ensure Docker is running
- Verify the MySQL container is up: `docker ps`
- Check the database connection parameters in `.env`
- Try connecting to the database using a client like MySQL Workbench or DBeaver:
    - Host: localhost
    - Port: 3307
    - User: myuser
    - Password: secret
    - Database: courses-db

### Email Configuration

If emails are not being sent:
- Make sure you've set up valid `EMAIL_USERNAME` and `EMAIL_PASSWORD`
- For Gmail accounts, you might need to enable "Less secure app access" or use an App Password

### Other Issues

- Check the application logs for detailed error information
- Verify all required environment variables are set correctly
- Ensure you have sufficient permissions for file operations
- Try clearing Maven cache: `./mvnw dependency:purge-local-repository clean install`
