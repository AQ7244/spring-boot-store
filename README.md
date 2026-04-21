# Spring Boot Store API

This repository contains a comprehensive REST API for an e-commerce platform. Originally adapted from a starter template, it has been refactored and expanded to include core business logic for handling users, products, and transactions.

## Features & Modules

The project is structured into several key modules:

*   **Authentication & Security**: Secure user login and registration using Spring Security.
*   **User Management**: APIs for managing user profiles and account details.
*   **Product Catalog**: Full CRUD operations for product listings, categories, and inventory.
*   **Orders**: Management of customer orders and history.
*   **Checkout**: Transactional logic for processing carts and finalizing purchases.

## Technologies Used

*   **Java 17**: Core programming language using the latest features.
*   **Spring Boot 3**: Framework for building the RESTful services.
*   **Spring Security & JWT**: Implementation for robust authentication and stateless authorization.
*   **Spring Data JPA**: For streamlined database interaction and ORM.
*   **MySQL**: Relational database for persistent storage.
*   **Maven**: Dependency management and build automation.
*   **Lombok**: To minimize boilerplate code like getters, setters, and constructors.

## Getting Started

To get started with this project, clone the repository to your local machine:

```sh
git clone https://github.com/AQ7244/spring-boot-store.git
cd spring-boot-store
