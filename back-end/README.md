
# bookstore back-end

> Book Store back-end for BNP Paribas's KATA assessment 

## Table of Contents
- [About the Project](#about-the-project)
- [Technologies Used](#technologies-used)
- [Getting Started](#getting-started)
  - [Prerequisites](#prerequisites)
  - [Installation](#installation)
  - [Running the Application](#running-the-application)
- [Configuration](#configuration)
  - [Application Properties](#application-properties)
  - [Usage](#usage)
  - [Predefined Users for local usage](#predefined-users-for-local-usage)
  - [Login](#login)
  - [Example Endpoints](#example-endpoints)
- [Testing](#testing)
- [Usage](#usage)
- [Testing](#testing)
  - [Testing Strategy](#testing-strategy)
- [Building the Project](#building-the-project)

---

## About the Project

This application contains a REST API. It deals with Users, Books and Shopping Cart.

We have 4 controllers: 
- One for books where we can perform CRUD operations on top of them;
- One for users where they can auto-register and login;
- One for carts where we can add books into the cart, remove them from the cart, checkout and fetch the cart;
- One for technical purposes called ping.

This application contains unit tests for the repository, service and controller layers. As well as integration tests.

Contains, also, endpoints for monitoring and maintaining (actuator), as well, as swagger UI endpoint.

I decided, for the sake of the challenge, follow the class-first approach for the database. So the database is created from the entities definitions. However, we can use Liquibase or Flyway to maintain and migrate database.

### Key Features
- Unit testing
- Integration testing
- Actuator endpoints
- Swagger documentation and swagger UI
- Authentication and Authorization

## Technologies Used

- **Java**: Version 17
- **Spring Boot**: Version 3.4.0
- **Database**: H2 (for testing and local)
- **Build Tool**: Maven
- **Other Libraries**: Spring Data JPA, Spring Validation, Spring Actuator, Spring Security, Lombok, Mapstruct and springdoc-openapi.

## Getting Started

Follow these steps to set up the project locally:

### Prerequisites

Make sure you have the following installed:
- **Java Development Kit (JDK)** 17
- **Maven**
- **Git** (for cloning the repository)

### Installation

1. Clone the repository:

   ```bash
   git clone https://github.com/toxiba/book-store.git
   cd book-store/back-end
   ```

2. Install dependencies:

   ```bash
   mvn clean install
   ```

### Running the Application

To start the application, use the following command:

```bash
mvn spring-boot:run
```

Alternatively, you can create an executable JAR:

```bash
mvn clean package
java -jar target/bookstore-0.0.1-SNAPSHOT.jar
```

## Configuration

### Application Properties

The application has only two profiles `local` and `tests`. The first one is the default and runs locally with a h2 in memory. Te second one is only used for unit tests and integration tests.

The default configuration for `local` environment is
```bash
spring.application.name=bookstore (local)

###
# Database
###
spring.datasource.url=${DATASOURCE_URL:jdbc:h2:mem:bookstore}
spring.datasource.driverClassName=${DATASOURCE_CLASS:org.h2.Driver}
spring.datasource.username=${DATASOURCE_USERNAME:sa}
spring.datasource.password=${DATASOURCE_PASSWORD:password}
spring.jpa.database-platform=${DATASOURCE_PLATFORM:org.hibernate.dialect.H2Dialect}

spring.jpa.hibernate.ddl-auto=${JPA_DDL_AUTO:create}
spring.jpa.show-sql=${JPA_SHOW_SQL:true}

spring.h2.console.enabled=${H2_CONSOLE:false}

###
# Security
###
users.user.username=${USER_USERNAME:user}
users.user.password=${USER_PASSWORD:password}
users.user.role=${USER_ROLE:ROLE_USER}
users.admin.username=${ADMIN_USERNAME:admin}
users.admin.password=${ADMIN_PASSWORD:password}
users.admin.role=${ADMIN_ROLE:ROLE_ADMIN}

logging.level.org.springframework.security=DEBUG


###
# Actuator
###
management.endpoints.web.exposure.include=${ACTUATOR_ENDPOINTS_EXPOSURE_INCLUDE:*}
management.endpoint.shutdown.enabled=${ACTUATOR_SHUTDOWN_ENDPOINT_ENABLED:true}
endpoints.shutdown.enabled=${ACTUATOR_SHUTDOWN_ENDPOINT_ENABLED:true}

###
# Swagger
###
springdoc.show-actuator=${SWAGGER_SHOW_ACTUATOR:true}
```

## Usage

After the application has started, we are able to call its endpoints directly via, for example, Postman or we can make use of the Swagger UI page.

```bash
localhost:8080/swagger-ui/index.html
```

With this page we can leverage the swagger documentation and execute the api endpoints, as well as the endpoints of actuator.

```bash
http://localhost:8080/swagger-ui/index.html?urls.primaryName=99.+Actuator+%28ADMIN%29
```
### Predefined Users for local usage

- **USER**:
  - username: **user**
  - password: **password**
  - role: **ROLE_USER**
- **ADMIN**:
    - username: **admin**
    - password: **password**
    - role: **ROLE_ADMIN**

### Login

With the help of the **(POST)/api/v1/users/login** endpoint we can log using the users above-mentioned or register new users (with the ROLE_USER) using the **(POST) /api/v1/users/register** endpoint.
Since the application uses SESSION all the other endpoints called after that will be made using the last successful user session.

### Example Endpoints

For a REST API application, you could document a few example endpoints here:

- **POST /api/v1/users/login** - Public and used to perform the authentication.
- **POST /api/v1/users/register** - Public and used to register a new user (with the ROLE_USER role).
- **GET /api/v1/books** - Public and Retrieves the list of books.
- **GET /api/v1/books/{id}** - Public and Retrieves a specific book by ID.
- **POST /api/v1/books/{id}** - Only for ADMIN users (ROLE_AMIN) and inserts a new book.
- **PUT /api/v1/books/{id}** - Only for ADMIN users (ROLE_AMIN) and updates a book by id.
- **DELETE /api/v1/books/{id}** - Only for ADMIN users (ROLE_AMIN) and removes a book by id.
- **POST/api/v1/cart/item/{bookId}/quantity/{quantity}** - Only for USERS users (ROLE_USER) and adds a specific quantity of a book into the shopping cart.
- **POST /api/v1/cart/checkout** - Only for USERS users (ROLE_USER) and performs a checkout of the user shopping cart.
- **GET /api/v1/cart** - Only for USERS users (ROLE_USER) and retrieves the user shopping cart.
- **DELETE /api/v1/cart/item/{bookId}}** - Only for USERS users (ROLE_USER) and removes a book from the user shopping cart.
- **DELETE /actuator/**** - Only for ADMIN users (ROLE_AMIN) and used to monitor and manage the application.

## Testing

To run tests, use:

  ```bash
  mvn test
  ```

### Testing Strategy

The application was developed using TDD, and all the tests follow a pattern of Given-When-Then.
Given Something, When Something, Then Something.

Unit Tests for the following layers:
- Repository
- Service
- Controller

Also, contains Integration tests for Books, Users, Cart and Security features. So all the layers are tested together. 

## Building the Project

- Build an executable JAR:
  ```bash
  mvn clean package
  ```
