# Quickstart: Airline Management System

**Feature Branch**: `1-airline-management`  
**Created**: 2025-11-02  

## Prerequisites

- Java 17 or higher
- Node.js 18 or higher
- PostgreSQL 14 or higher
- Docker

## Backend Setup

1. Navigate to the backend directory:
   ```bash
   cd backend
   ```
2. Build the project:
   ```bash
   ./mvnw clean install
   ```
3. Run the application:
   ```bash
   ./mvnw spring-boot:run
   ```

## Frontend Setup

1. Navigate to the frontend directory:
   ```bash
   cd frontend
   ```
2. Install dependencies:
   ```bash
   npm install
   ```
3. Start the development server:
   ```bash
   npm start
   ```

## Database Setup

1. Start PostgreSQL using Docker:
   ```bash
   docker run --name airline-db -e POSTGRES_USER=admin -e POSTGRES_PASSWORD=admin -e POSTGRES_DB=airline -p 5432:5432 -d postgres:14
   ```
2. Apply database migrations (from the backend directory):
   ```bash
   ./mvnw flyway:migrate
   ```

## Testing

### Backend

1. Run backend tests:
   ```bash
   ./mvnw test
   ```

### Frontend

1. Run frontend tests:
   ```bash
   npx playwright test
   ```

## Notes

- Ensure Docker is running before starting the database.
- Update environment variables as needed in `.env` files.