# Order Management System (Online Grocery)

A Spring Boot RESTful API service for managing customers, grocery items, and orders using Spring Data JPA and an H2 in-memory database.

## Features & Domain Model

- **Customer (`CustomerEntity`)**: Stores customer details (`id`, `name`, `email`, `address`, `phone`). One customer can place multiple orders.
- **Grocery Item (`GroceryItemEntity`)**: Stores inventory items (`id`, `name`, `category`, `price`, `quantity`). An item can appear in multiple orders.
- **Order (`OrderEntity`)**: Tracks orders placed by customers (`id`, `customer`, `items`, `orderDate`, `totalPrice`) via a many-to-many join table (`order_grocery_items`).
- **Global Error Handling (`GlobalExceptionHandler`)**: Returns structured JSON error responses (`404 Not Found` for missing resources, `400 Bad Request` for validation/input errors).

## Project Setup

### Prerequisites
- **Java 21**
- **Maven 3.9+**

### Run Locally
```bash
git clone <your-repo-url>
cd order-management-system
mvn spring-boot:run



# H2 In-Memory Database
spring.datasource.url=jdbc:h2:mem:cafedb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

# H2 Console
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console

# JPA / Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

