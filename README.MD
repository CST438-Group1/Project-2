# Project-2: MotoRYX

## Table of Contents
1. [Overview](#overview)
2. [Introduction](#introduction)
3. [Product Spec](#product-spec)
4. [Documentation](#documentation)
5. [Conclusion](#conclusion)

---

## Overview

### Description

**MotoRYX** is a web application that lets users browse and save data related to racing tracks, vehicles, racing events, and lap performance.

---

## Introduction

MotoRYX is built around the **Lap Vault API** — a secure, RESTful track statistics service designed for motorsport enthusiasts and administrators. Users authenticate via OAuth2 and can explore lap data filtered by continent, country, track, vehicle make, and model year. Authenticated users can save favorites to their profile, while administrators manage core data through protected endpoints. The goal of this project is to provide structured, secure, and easily accessible track data through a clean interface backed by a remote database.

### Team

| Name | Role |
|---|---|
| Metztli Herrera | Developer |
| James Fisher | Developer |
| Rishabh Patel | Developer |
| Jordan Lagura | Developer |

---

## Product Spec

### Features

- **OAuth2 Authentication** — Secure login with role-based authorization (User / Admin)
- **Track and Lap Data** — Browse lap statistics filtered by continent, country, track, vehicle make, and model year
- **Vehicle Endpoints** — Query and manage vehicle data including make, model, and year
- **Favorites** — Authenticated users can save tracks and vehicles to their profile
- **Admin CRUD** — Administrators can create, update, and delete track and vehicle records
- **Dockerized Deployment** — Containerized for consistent, production-ready deployments
- **OpenAPI / Swagger Documentation** — Auto-generated and browsable API documentation

### Tech Stack

| Layer | Technology |
|---|---|
| Backend | Spring Boot (Java) |
| Authentication | Spring Security + OAuth2 |
| Database | Firebase / Supabase |
| Frontend | React |
| Containerization | Docker |
| API Testing | Postman |
| API Documentation | Swagger / OpenAPI |
| Design and Mockups | Figma |
| Version Control | GitHub |

### Project Roadmap

| Step | Phase | Description |
|---|---|---|
| 1 | Backend Foundation | Initialize Spring Boot project and configure project structure |
| 2 | Authentication and User Management | Implement OAuth login, create user records on first login, enforce role-based authorization |
| 3 | Core API Implementation | Build endpoints for tracks, vehicles, makes, routes, and favoriting; integrate remote database |
| 4 | Testing and Documentation | Finalize test coverage and Swagger documentation |
| 5 | Deployment and Frontend Integration | Dockerize, deploy, and connect React demo UI |

---

## Documentation

### Getting Started

**Prerequisites**
- Java 17+
- Docker and Docker Desktop
- Node.js (for React frontend)
- A Firebase or Supabase project configured

**Running Locally**

```bash
# Clone the repository
git clone https://github.com/<your-org>/motoryx.git
cd motoryx

# Build and run with Docker
docker-compose up --build
```

The API will be available at `http://localhost:8080`.  
Swagger UI: `http://localhost:8080/swagger-ui.html`

### API Endpoints

| Resource | Description |
|---|---|
| `/auth` | OAuth2 login and session management |
| `/tracks` | Browse and filter tracks by location |
| `/vehicles` | Query vehicles by make and model year |
| `/laps` | Retrieve lap performance statistics |
| `/favorites` | Save and manage user favorites |
| `/admin/tracks` | Admin CRUD for track data |
| `/admin/vehicles` | Admin CRUD for vehicle data |

### Foreseeable Challenges

**OAuth Configuration**
- Configuring OAuth2 correctly within Spring Boot
- Accurately handling login redirects
- Properly enforcing user roles across protected routes

**Deployment and Docker**
- Dockerizing the Spring Boot application correctly
- Debugging production-only issues that do not surface locally
- Ensuring secure handling of environment variables in containerized environments

**Database Design**
- Structuring Firestore collections to align with a relational ERD design
- Preventing inconsistent or duplicate data across collections

### Resources

**Technical**
- Spring Boot Documentation
- Firebase / Supabase Documentation
- Spring Security and OAuth2 Documentation
- Docker Documentation
- Swagger / OpenAPI Documentation

**Development Tools**
- IntelliJ IDEA
- GitHub (issues and version control)
- Figma (mockups and ERD design)
- Postman (API testing)
- Docker Desktop

---

## Conclusion

MotoRYX provides a structured and secure platform for accessing motorsport data through a modern web interface. By combining a Spring Boot backend with OAuth2 authentication, a NoSQL remote database, and a React frontend, the project demonstrates a full-stack architecture capable of handling real-world use cases in data browsing, user management, and administrative control. This project served as a foundation for the team to gain hands-on experience with RESTful API design, cloud database integration, containerized deployment, and collaborative software development.