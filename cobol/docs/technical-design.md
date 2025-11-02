# Technical Design Document (TDD)

Date: 2025-11-02
Project: COBOL-AIRLINES — Legacy CICS/COBOL → Java/React Migration

This document provides the Technical Design required by the hackathon: architecture
diagram, ERD and database mapping guidance, backend class and API design, OpenAPI
3.0 contract (separate YAML file), frontend component diagram, and UI/UX state
management proposal.

IMPORTANT: Several low-level DB2 copybook-to-Postgres mappings were inferred from
table names and typical patterns. The canonical DB2 artifacts (`DB2/DER-COB-AIRLINES.pdf`,
`DB2/create-db`, and `DB2/DCLGEN/*`) must be used to finalize column types,
constraints, and indexes. TODOs in this doc mark items requiring that authoritative
mapping step.

----

## 1. System Architecture (C4-style, high level)

Context: Single-page React frontend communicates with a backend API gateway and
microservices implemented as Spring Boot apps. PostgreSQL is the canonical data
store.

Simplified C4-style diagram (textual):

React SPA (browser)
  └─> HTTPS requests
        └─> API Gateway (Spring Cloud Gateway)
              ├─> Auth Service (Spring Boot)
              │     └─> Employee / Passengers tables (Postgres)
              ├─> Flight Service (Spring Boot)
              │     └─> Flight table (Postgres)
              └─> Booking Service (Spring Boot)
                    └─> Passenger/Ticket/Booking tables (Postgres)

Sequence example (Login flow):
1. Browser sends POST /api/auth/login → API Gateway → Auth Service
2. Auth Service queries `emplo` (Postgres) and validates password (legacy crypto)
3. On success Auth Service returns JWT → Browser stores JWT
4. Browser calls GET /api/flights/search with JWT → Gateway forwards to Flight Service

Notes:
- API Gateway handles TLS, routing, rate-limiting and attaches auth context (JWT) to downstream requests.
- Each service exposes health and metrics endpoints for observability.

----

## 2. Database Design (ERD + suggested SQL)

High-level entities (inferred from DB2 artifacts and DCLGEN names):

- Employee (`emplo`) — store employee id, admission date, deptid, hashed password
- Flight (`flight`) — flightId, flightNum, flightDate, airportDep, airportArr, deptTime, arrTime
- Passenger (`passeng` / `passenger`) — clientId, firstName, lastName, passn (seq), other contact fields
- Ticket / Booking (`ticket` / `booking`) — mapping of passenger to flight and purchase data

Relationships:
- Employee -< Ticket (employee may sell tickets)  (optional)
- Flight 1 -< Ticket
- Passenger 1 -< Ticket

Textual ERD (tables and core columns):

Employee (emplo)
- empid VARCHAR PRIMARY KEY
- admidate DATE NOT NULL
- deptid INT NOT NULL
- stored_cryptpass VARCHAR NOT NULL  -- legacy hashed value

Flight (flight)
- flight_id BIGSERIAL PRIMARY KEY
- flight_num VARCHAR NOT NULL
- flight_date DATE NOT NULL
- airport_dep VARCHAR(10)
- airport_arr VARCHAR(10)
- dep_time TIME
- arr_time TIME
- seats_total INT
- seats_available INT

Passenger (passeng)
- client_id VARCHAR PRIMARY KEY
- first_name VARCHAR
- last_name VARCHAR
- dob DATE

Ticket / Booking (ticket)
- ticket_id BIGSERIAL PRIMARY KEY
- flight_id BIGINT REFERENCES flight(flight_id)
- client_id VARCHAR REFERENCES passeng(client_id)
- passenger_seq INT  -- passenger number within booking (passn)
- unit_price NUMERIC(10,2)
- total_price NUMERIC(12,2)
- created_by_empid VARCHAR REFERENCES emplo(empid)

TODO: Use `DB2/create-db` and `DB2/DCLGEN/*` to extract exact column names, sizes,
constraints and indexes; update this schema accordingly.

Suggested initial SQL (db/schema.sql) is included in the repo to bootstrap local dev.

----

## 3. Backend Design (Java / Spring Boot)

Overview: Three primary services (can be packaged as modules or separate Spring Boot apps):

- Auth Service
- Flight Service
- Booking Service

Each service exposes a REST API and uses Spring Data JPA to talk to the shared
Postgres database. Security is JWT-based (Auth Service issues tokens).

3.1 Class Diagrams (textual)

Auth Service
- AuthController
  - POST /api/auth/login
  - Injects AuthService
- AuthService
  - login(empid, password): AuthResponse
  - uses EmployeeRepository, CryptoService
- EmployeeRepository extends JpaRepository<EmployeeEntity, String>
- CryptoService
  - replicateLegacyHash(password, empid, admidate): String
  - compare(stored, computed): boolean
- Entities: EmployeeEntity (fields: empid, admidate, deptid, storedCryptpass)
- DTOs: AuthRequest {empid,password}, AuthResponse {token, empid, deptid}

Flight Service
- FlightController
  - GET /api/flights/search?flightNum=&date=&depAirport=&arrAirport=
  - Injects FlightService
- FlightService
  - search(Optional<String> flightNum, Optional<LocalDate> date, Optional<String> depAirport, Optional<String> arrAirport): List<FlightDTO>
  - builds dynamic query using Specification<FlightEntity>
- FlightRepository extends JpaRepository<FlightEntity, Long>, JpaSpecificationExecutor<FlightEntity>
- Entities: FlightEntity (match DB)
- DTOs: FlightDTO

Booking Service
- BookingController
  - POST /api/bookings/validate-step1
  - Injects BookingService
- BookingService
  - validateStep1(clientId, flightNum, date, passengerCount): BookingQuote
  - uses PassengerRepository and FlightRepository
- Repositories: PassengerRepository, TicketRepository
- DTOs: BookingQuote (client info, flight info, unitPrice, totalPrice)

3.2 Important design details
- CryptoService must implement the exact algorithm from `CICS/LOGIN/CRYPTO-VERIFICATION`.
  - Add unit tests with known vectors from `EMPLO-OUTPUT-PASS-CRYPT` or PASSDOC extracts.
- FlightService must use JPA Specifications or CriteriaBuilder to reproduce CURSOR
  selection behavior described in `SRCHFLY-COB` (COUNT1 evaluation). Unit tests
  must assert generated predicates for each scenario.
- All services must expose health (/actuator/health) and metrics (/actuator/prometheus).

----

## 4. API Design — OpenAPI 3.0 (summary)

Full OpenAPI file is included separately at `specs/openapi.yaml` with schemas for
AuthRequest/AuthResponse, FlightDTO, BookingQuote and endpoints:

- POST /api/auth/login
- GET /api/flights/search
- POST /api/bookings/validate-step1

Design notes:
- Use standard HTTP status codes: 200 OK, 400 Bad Request for malformed input,
  401 Unauthorized for invalid auth, 404 Not Found for resource-not-found semantics
  where appropriate.
- All endpoints require JWT except /api/auth/login.

----

## 5. Frontend Design (React)

5.1 Component Diagram (tree)

App.js
- Router
  - LoginPage (path: /login)
  - ProtectedRoute (wraps) -> Application Shell
    - SearchFlightPage (path: /flights/search)
    - BookingStep1 (path: /bookings/step1)
    - BookingStep2 (path: /bookings/step2)

5.2 Component responsibilities
- `LoginPage` — render terminal-style login (maps to `MAP1.png`), perform POST /api/auth/login, store JWT and user info into Auth context.
- `SearchFlightPage` — render SRCHFLY1 terminal UI, call GET /api/flights/search, show results in rows.
- `BookingStep1` — render sell1-1, call POST /api/bookings/validate-step1, show BookingQuote, then navigate to BookingStep2.
- `BookingStep2` — second step UI (SELLCOB2 equivalent) — not implemented in this TDD.

5.3 State management proposal

Primary state to manage:
- Authentication state (JWT, empid, deptid, expiry)
- BookingQuote transient state (BookingQuote passed between Step1 → Step2)

Recommendation (minimal but robust):
- Use React Context for Authentication (AuthContext) and a small local store (React Context or Zustand) for booking flow state.
  - AuthContext provides: token, user, login(), logout(), isAuthenticated()
  - BookingContext (or small Zustand store) holds current BookingQuote; BookingStep1 writes it, BookingStep2 consumes it.

Security & persistence:
- Store JWT in memory and optionally in secure cookie or localStorage with XSS mitigations; use HttpOnly cookies for production where possible.
- Use Axios interceptors to attach Authorization header to outbound requests.

Testing & Visual Fidelity:
- Use Playwright for E2E tests; use visual snapshot comparisons against provided screenshots (`MAP1.png`, `SRCHFLY2.png`, `sell1-1.png`) with a small pixel-diff tolerance.

----

## 6. Observability, CI/CD and Deployment Notes

- Provide Dockerfile for each service and a root `docker-compose.yml` to run the stack locally: gateway, auth-service, flight-service, booking-service, postgres, and a test runner service for Playwright.
- CI job should build and run unit tests, perform static analysis (SpotBugs/Checkstyle), start the stack via docker-compose and execute Playwright E2E tests.
- Add Prometheus scraping target for each Spring Boot app and a Grafana dashboard for basic metrics.

----

## 7. Assumptions & Open Items (TODOs)

1. Exact DB2-to-Postgres column names, types, and constraints must be extracted from `DB2/create-db` and `DB2/DCLGEN/*`. (TODO: run script / produce mapping table)
2. Need concrete test vectors from legacy PASSDOC or `EMPLO-OUTPUT-PASS-CRYPT` for CryptoService unit tests. (TODO)
3. Decide JWT signing key management and rotation policy (TODO: security ops).

----

Appendices:
- `specs/openapi.yaml` — OpenAPI 3.0 YAML for defined endpoints (created in repo).
- `db/schema.sql` — Suggested Postgres schema (bootstrap only; needs verification against DB2 artifacts).

End of TDD
