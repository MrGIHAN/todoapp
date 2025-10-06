# Todo App - Full Stack Application

A full-stack todo application built with React frontend, Spring Boot backend, and MySQL database.

## Features
- Create tasks with title and description
- View latest 5 incomplete tasks
- Mark tasks as completed
- Clean, responsive UI
- Docker containerization

## Tech Stack
- Frontend: React 18, Webpack, CSS3
- Backend: Spring Boot 3, Java 17, JPA/Hibernate
- Database: MySQL 9.1
- Containerization: Docker, Docker Compose

## Quick Start

1. **Prerequisites**
   - Docker
   - Docker Compose

2. **Clone and Run**
   ```bash
   # Build and start all services
   docker-compose up --build
   
   # Access the application
   Frontend: http://localhost:3000
   Backend API: http://localhost:8080/api/tasks
   MySQL: localhost:3308
