# Research: Airline Management System

**Feature Branch**: `1-airline-management`  
**Created**: 2025-11-02  

## Decisions and Rationale

### Backend: Java Spring Boot
- **Decision**: Use Java Spring Boot for the backend.
- **Rationale**: Spring Boot is a mature framework with extensive support for building RESTful APIs. It integrates well with JPA/Hibernate for ORM and provides robust tools for dependency injection and configuration.
- **Alternatives Considered**: Node.js (less suited for enterprise-grade applications), Python Django (less performant for high-concurrency scenarios).

### Frontend: React + Tailwind CSS
- **Decision**: Use React for the frontend with Tailwind CSS for styling.
- **Rationale**: React is widely adopted for building dynamic user interfaces, and Tailwind CSS simplifies styling with utility-first classes.
- **Alternatives Considered**: Angular (steeper learning curve), Vue.js (less community support).

### Database: PostgreSQL
- **Decision**: Use PostgreSQL as the relational database.
- **Rationale**: PostgreSQL is open-source, highly reliable, and supports advanced features like JSONB for semi-structured data.
- **Alternatives Considered**: MySQL (less feature-rich), MongoDB (not relational).

### ORM: JPA/Hibernate
- **Decision**: Use JPA with Hibernate for object-relational mapping.
- **Rationale**: JPA/Hibernate is the standard ORM for Java applications, providing a robust and flexible way to interact with relational databases.
- **Alternatives Considered**: MyBatis (less abstraction, more boilerplate).

### Testing: JUnit (backend), Playwright (frontend)
- **Decision**: Use JUnit for backend testing and Playwright for frontend testing.
- **Rationale**: JUnit is the de facto standard for Java testing, and Playwright provides powerful tools for end-to-end testing of web applications.
- **Alternatives Considered**: Selenium (less modern than Playwright).

### Deployment: Local
- **Decision**: Deploy the application locally using Docker.
- **Rationale**: Docker ensures consistent environments across development and testing.
- **Alternatives Considered**: Kubernetes (overkill for local deployment).

## Alternatives Summary

| Technology       | Chosen Option       | Alternatives Considered |
|------------------|---------------------|--------------------------|
| Backend          | Java Spring Boot    | Node.js, Python Django   |
| Frontend         | React + Tailwind CSS| Angular, Vue.js          |
| Database         | PostgreSQL          | MySQL, MongoDB           |
| ORM              | JPA/Hibernate       | MyBatis                  |
| Testing (Backend)| JUnit               | TestNG                   |
| Testing (Frontend)| Playwright         | Selenium                 |
| Deployment       | Docker (local)      | Kubernetes               |

---

## Notes

- This document is subject to review and updates as the project progresses.