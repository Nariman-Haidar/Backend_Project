# Client Server Project:

## Overview

This project is a security module for a web application. It includes authentication and authorization functionalities, implementing JWT (JSON Web Token) for secure access control.

## Features

- JWT-based authentication
- Role-based access control
- Custom authentication manager
- CORS configuration

## Components

### JwtAuthenticationConverter

A custom server authentication converter that extracts the JWT token from the request headers or query parameters.

### JwtUtil

Utility class for JWT operations such as token generation, username extraction, and token validation.

### SecurityConfig

Configuration class for setting up security filters, authentication, and authorization rules.

### AuthenticationManager

Custom authentication manager for processing JWT authentication.

### CourseAccessManager

Custom authorization manager for handling course access permissions based on user roles.

## Project Structure

### Server-Side Packages

- **service**: Contains service classes such as `AccessService`, `AdministratorService`, `CourseService`, `QueueItemService`, and `UserService`.
- **model**: Contains entity classes such as `User`, `Access`, `Administrator`, `Course`, `QueueItem`, `Response`, and `Role`.
- **repository**: Contains repository interfaces for data access.
- **mapper**: Contains mapper classes for converting between entities and DTOs.
- **controller**: Contains REST controllers for handling API requests.
- **dto**: Contains Data Transfer Objects (DTOs) used for transferring data.
- **config**: Contains configuration classes.

### Client-Side Packages

- **view**: Contains view components such as `AdminQueueItemView`, `CourseDetailsView`, `CourseView`, `HeaderView`, `LoginView`, `UserQueueItemView`, `UserView`, and `AccessView`.
- **presenter**: Contains presenter components for handling business logic and updating the views.
- **assets**: Contains static assets.
- **datasource**: Contains components for data fetching and handling real-time updates.

## Configuration

### Properties

- `jwt.secret`: Secret key used for signing JWT tokens.
- `jwt.expirationMs`: JWT token expiration time in milliseconds.

## Usage

1. Add your security configurations in the `SecurityConfig` class.
2. Implement your user service to integrate with the `AuthenticationManager` and `JwtUtil`.
3. Ensure proper role-based access control with `CourseAccessManager`.

## Dependencies

- Spring Boot
- Spring Security
- JWT (Java Web Tokens)
- React
