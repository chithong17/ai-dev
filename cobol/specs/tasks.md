---

description: "Implementation task list for COBOL-AIRLINES migration"
---

# Tasks: COBOL-AIRLINES Full Migration

**Input**: TDD (docs/technical-design.md), specs/*

## Phase 1: Setup (Shared Infrastructure)

- [ ] T001 Initialize backend Maven project (artifactId: cobol-airlines-backend) at `backend/` with `pom.xml` (Spring Boot, JPA, Postgres, Security, JJWT)
- [ ] T002 Initialize frontend project at `frontend/` with `package.json` and React dependencies (react, react-dom, react-router-dom, axios)
- [ ] T003 [P] Add Dockerfiles for backend and frontend: `backend/Dockerfile`, `frontend/Dockerfile`
- [ ] T004 Create root `docker-compose.yml` to run gateway, backend, frontend, and Postgres at repo root
- [ ] T005 [P] Add initial `db/schema.sql` and migration directory `db/migrations/` to store SQL migration files
- [ ] T006 [P] Configure basic CI workflow placeholder at `.github/workflows/ci.yml`

## Phase 2: Foundational (Blocking Prerequisites)

 - [X] T007 Create backend base package `com.cobolairlines` and Spring Boot starter class `backend/src/main/java/com/cobolairlines/Application.java`
 - [X] T008 Create `Employee`, `Flight`, `Passenger`, `Ticket` entity classes in `backend/src/main/java/com/cobolairlines/model/`
 - [X] T009 Create Spring Data repositories: `EmployeeRepository`, `FlightRepository`, `PassengerRepository`, `TicketRepository` in `backend/src/main/java/com/cobolairlines/repo/`
 - [X] T010 Create DTOs and mappers for `AuthRequest`, `AuthResponse`, `FlightDTO`, `BookingQuote` in `backend/src/main/java/com/cobolairlines/dto/`
 - [X] T011 [P] Create `CryptoService` implementation and unit tests in `backend/src/test/java/...` to validate deterministic hashing behavior
 - [X] T012 Setup application properties for Postgres in `backend/src/main/resources/application.yml` and test H2 config for tests
 - [X] T013 [P] Create base frontend App skeleton `frontend/src/App.js` and Router `frontend/src/router.js`

## Phase 3: User Story US1 - Login (Priority: P1)

**Goal**: Implement login flow matching legacy behavior and JWT issuance

 - [X] T014 [US1] Create `AuthController.java` with `POST /api/auth/login` at `backend/src/main/java/com/cobolairlines/controller/AuthController.java`
 - [X] T015 [US1] Create `AuthService.java` at `backend/src/main/java/com/cobolairlines/service/AuthService.java` with `authenticate(empid,password)` using `CryptoService` and `EmployeeRepository`
 - [X] T016 [US1] Unit test: `AuthServiceTest.java` in `backend/src/test/java/...` that mocks `EmployeeRepository` and asserts authenticate behavior
 - [X] T017 [US1] Frontend `LoginPage.js` at `frontend/src/pages/LoginPage.js` with terminal-style layout and POST to `/api/auth/login`
 - [X] T018 [US1] E2E Playwright placeholder test for login in `frontend/tests/login.spec.js`

## Phase 4: User Story US2 - Flight Search (Priority: P1)

**Goal**: Implement flight search with dynamic query predicates matching COUNT1 logic

 - [X] T019 [US2] Create `FlightController.java` with `GET /api/flights/search` at `backend/.../controller/FlightController.java`
 - [X] T020 [US2] Create `FlightService.java` implementing JPA Specifications logic for combinations (date-only, date+num, date+dep, date+arr, date+dep+arr) at `backend/.../service/FlightService.java`
 - [X] T021 [US2] Unit tests for `FlightServiceSpecificationTest.java` verifying built predicates and returned results using in-memory H2 and seeded data
 - [X] T022 [US2] Frontend `SearchFlightPage.js` at `frontend/src/pages/SearchFlightPage.js` matching `SRCHFLY1.png` and calling GET endpoint
 - [X] T023 [US2] Playwright E2E tests for search scenarios in `frontend/tests/search.spec.js`

## Phase 5: User Story US3 - Booking Step 1 (Priority: P1)

**Goal**: Implement booking validation and price calculation logic

 - [X] T024 [US3] Create `BookingController.java` with `POST /api/bookings/validate-step1` at `backend/.../controller/BookingController.java`
 - [X] T025 [US3] Create `BookingService.java` implementing passenger and flight validations and price computation (`120.99 * count`) at `backend/.../service/BookingService.java`
 - [X] T026 [US3] Unit tests `BookingServiceTest.java` covering passenger-missing, flight-missing, and successful quote scenarios
 - [X] T027 [US3] Frontend `BookingStep1.js` at `frontend/src/pages/BookingStep1.js` matching `sell1-1.png`, integrates with Booking API and navigates to BookingStep2 on success
 - [X] T028 [US3] Playwright E2E tests for booking step1 flows in `frontend/tests/booking-step1.spec.js`

## Phase 6: Polish & Cross-Cutting Concerns

- [ ] T029 Create integration tests that start the stack with docker-compose and run Playwright E2E tests
- [ ] T030 Add actuator and Prometheus metrics configuration to backend services
- [ ] T031 Add README.md with run instructions and dev quickstart
- [ ] T032 Final: Visual snapshot baselining and verification images under `frontend/tests/snapshots/`

## Dependencies & Execution Order

- Foundation (Phase 2) must be complete before US1/US2/US3 implementation.
- Within each user story: Tests (if included) → Models → Services → Controllers → Frontend → E2E

## Summary

- Total tasks listed: 32
- P1 stories: US1 (Login), US2 (Flight Search), US3 (Booking Step1)
- Suggested MVP: Complete Phase 1 + Phase 2 + US1 (Login) and US2 (Search) to demo login+search flow
