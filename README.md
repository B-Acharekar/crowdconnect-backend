
# CrowdConnect Backend

CrowdConnect is a collaborative problem-solving platform designed to facilitate community engagement and effective solutions through voting, comments, and user authentication. This repository contains the backend implementation of the CrowdConnect application using Spring Boot and MySQL.

## Table of Contents

- [Features](#features)
- [Technologies Used](#technologies-used)
- [Setup Instructions](#setup-instructions)
- [API Endpoints](#api-endpoints)
- [Authentication](#authentication)
- [Database Schema](#database-schema)
- [Contributing](#contributing)
- [License](#license)

## Features

- **User Authentication**: Secure user registration and login using JSON Web Tokens (JWT).
- **Solution Management**: Create, read, update, and delete solutions.
- **Voting System**: Users can vote on solutions, promoting community involvement.
- **Comments Section**: Users can leave comments on solutions for discussions.
- **CORS Support**: Cross-Origin Resource Sharing support for frontend integration.

## Technologies Used

- **Java**
- **Spring Boot**
  - Spring Boot Starter
  - Spring Boot Starter Data JPA
  - Spring Boot Starter Security
  - Spring Boot DevTools
  - Spring Boot Starter Web
- **Hibernate JPA**
- **MySQL**
- **JSON Web Token (JWT)**
- **BCrypt for Password Encryption**
- **RESTful API Principles**

## Setup Instructions

1. **Clone the Repository**:
   ```bash
   git clone https://github.com/yourusername/crowdconnect-backend.git
   cd crowdconnect-backend
   ```

2. **Install Dependencies**:
   Make sure you have Maven installed. Run:
   ```bash
   mvn clean install
   ```

3. **Configure Database**:
   Update the `application.properties` file with your MySQL database credentials:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/crowdconnect
   spring.datasource.username=your_username
   spring.datasource.password=your_password
   spring.jpa.hibernate.ddl-auto=update
   ```

4. **Run the Application**:
   You can run the application using the following command:
   ```bash
   mvn spring-boot:run
   ```

5. **Access the API**:
   The API will be available at `http://localhost:8080`.

## API Endpoints

### Authentication

- **POST** `/api/auth/register`: Register a new user.
- **POST** `/api/auth/login`: Authenticate a user and return a JWT token.

### Solutions

- **GET** `/api/solutions`: Retrieve a list of solutions.
- **POST** `/api/solutions`: Create a new solution.
- **GET** `/api/solutions/{solutionId}`: Retrieve a specific solution.
- **PUT** `/api/solutions/{solutionId}`: Update an existing solution.
- **DELETE** `/api/solutions/{solutionId}`: Delete a specific solution.

### Voting

- **POST** `/api/solutions/{solutionId}/vote`: Vote on a solution.
- **GET** `/api/solutions/{solutionId}/votes`: Retrieve votes for a specific solution.

### Comments

- **POST** `/api/solutions/{solutionId}/comments`: Add a comment to a solution.
- **GET** `/api/solutions/{solutionId}/comments`: Retrieve comments for a specific solution.

## Authentication

- All API requests (except for registration and login) require a valid JWT token in the `Authorization` header. Use the format:
  ```
  Authorization: Bearer <token>
  ```

## Database Schema

The database schema includes tables for users, solutions, votes, and comments. Please refer to the database migration files for more detailed schema information.

## Contributing

Contributions are welcome! Please follow these steps:

1. Fork the repository.
2. Create a new branch (`git checkout -b feature/YourFeature`).
3. Make your changes and commit them (`git commit -m 'Add some feature'`).
4. Push to the branch (`git push origin feature/YourFeature`).
5. Create a pull request.
