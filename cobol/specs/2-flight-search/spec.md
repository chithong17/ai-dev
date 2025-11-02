```markdown
# Feature Specification: Flight Search (Sales)

**Feature Branch**: `2-flight-search`  
**Created**: 2025-11-02  
**Status**: Draft  
**Input**: FlightSearch

**Constitution Gates**: This spec documents how the feature satisfies the
project constitution: reverse-engineering source mapping (`CICS/SALES-MAP/SRCHFLY-COB`),
UI fidelity to `CICS/SALES-MAP/SRCHFLY1.png`, DB2→Postgres mapping via
`DB2/DCLGEN/FLIGHT-DCLGEN`, and Playwright E2E scenarios derived from legacy flows.

## User Scenarios & Testing *(mandatory)*

### User Story 1 - Flight search (Priority: P1)

Sales staff use the terminal-style flight search screen to find up to 10 flights
matching the entered criteria (date, optional flight number, optional departure
airport, optional arrival airport). The UI must render results in rows exactly as
the legacy map and enable selection/navigation consistent with the original.

**Why this priority**: Flight search is core to the sales workflow and must match
legacy selection and result ordering rules.

**Independent Test**: Playwright E2E test reproducing `SRCHFLY1.png` interaction:
enter combinations of inputs (date only, date+flight, date+dep, date+arr, date+dep+arr),
submit, and assert the returned results and visual layout match `SRCHFLY2.png`/`SRCHFLY3.png`
within approved pixel-diff tolerances and that up to 10 rows are shown.

**Acceptance Scenarios**:

1. **Given** only `FDATEI` is provided, **When** user searches, **Then** backend runs
   the query equivalent to `WHERE FLIGHTDATE = :FDATEI` and returns up to 10 rows.
2. **Given** `FDATEI` and `FNUMI` provided, **When** user searches, **Then** backend runs
   `WHERE FLIGHTDATE = :FDATEI AND FLIGHTNUM = :FNUMI` and returns up to 10 rows.
3. **Given** `FDATEI` and `DAIRI` provided, **When** user searches, **Then** backend runs
   `WHERE FLIGHTDATE = :FDATEI AND AIRPORTDEP = :DAIRI` and returns up to 10 rows.
4. **Given** `FDATEI` and `LAIRI` provided, **When** user searches, **Then** backend runs
   `WHERE FLIGHTDATE = :FDATEI AND AIRPORTARR = :LAIRI` and returns up to 10 rows.
5. **Given** `FDATEI`, `DAIRI`, and `LAIRI` provided, **When** user searches, **Then** backend
   runs `WHERE FLIGHTDATE = :FDATEI AND AIRPORTDEP = :DAIRI AND AIRPORTARR = :LAIRI`.

---

## Functional Requirements *(mandatory & testable)*

- **FR-001**: Frontend MUST render a flight search page matching `SRCHFLY1.png` with
  four input fields: `FNUMI` (flight number), `FDATEI` (flight date), `DAIRI` (departure
  airport), `LAIRI` (arrival airport), and a search control.
- **FR-002**: Backend MUST expose `GET /api/flights/search` accepting optional query
  params: `flightNum`, `date`, `depAirport`, `arrAirport`. The endpoint returns a JSON
  array of up to 10 `FlightDTO` objects representing the fetched rows.
- **FR-003**: `FlightService` MUST build a dynamic query (Spring Data JPA Specifications
  or CriteriaBuilder) that reproduces the exact cursor selection logic from
  `SRCHFLY-COB` (COUNT1 evaluation → open corresponding cursor). The service must
  implement the same predicate combinations and result ordering as the COBOL program.
- **FR-004**: The service MUST fetch at most 10 rows per request and map DB columns
  to `FlightDTO` fields in the same positions as legacy map output rows `FLIID0O`..`FLIID9O`.
- **FR-005**: The API must validate input formats (date format) and return 400 Bad Request
  for malformed inputs; otherwise return 200 with results (possibly empty array).
- **FR-006**: The implementation MUST include unit tests for each search scenario and
  Playwright E2E tests to validate UI behavior and visual layout of results.

## Key Entities *(include if feature involves data)*

- **Flight (flight)**: converted from DB2 `FLIGHT` (see `DB2/DCLGEN/FLIGHT-DCLGEN`);
  - flightNum — string
  - flightDate — date
  - airportDep — string
  - airportArr — string
  - other columns mapped into DTO used for display

## Success Criteria *(mandatory)*

- **SC-001**: For each scenario (date-only, date+flight, date+dep, date+arr, date+dep+arr),
  automated tests (unit + integration) validate the constructed query contains the same
  predicates as the legacy cursor mapping and returns up to 10 rows.
- **SC-002**: Playwright E2E visual snapshots for an empty search and a populated
  search match `SRCHFLY1.png` and `SRCHFLY2.png`/`SRCHFLY3.png` within the agreed tolerance.
- **SC-003**: API contract returns up to 10 `FlightDTO` items and uses appropriate HTTP
  statuses for errors and malformed input.

## Edge Cases

- No results found — UI shows an empty result area consistent with legacy empty screen.
- Partial/malformed date — return 400 and show inline validation on UI.
- More than 10 matching rows — only the first 10 (as per legacy loop) are returned.

## Assumptions

- Date handling uses ISO date or another agreed format; plan will specify exact date format
  for API and UI (default: YYYY-MM-DD) and mapping from legacy date format.
- Legacy DB ordering rules (if present) will be implemented; where unclear, default to
  ORDER BY flightNum then departure time unless legacy code indicates otherwise.

## Implementation Notes (for planning)

- Backend stack: Java 17 + Spring Boot 3 (Spring Data JPA) — use Specifications or
  CriteriaBuilder to build dynamic predicates matching legacy COUNT1 evaluation.
- Frontend: React 18 functional components — UI must match `SRCHFLY1.png` visually and
  support keyboard navigation consistent with terminal UX.
- Testing: Playwright E2E tests must validate both functionality and visual fidelity.

---

Next steps: produce an implementation plan with data-model mapping (DB2→Postgres),
create `Flight` JPA entity and `FlightDTO`, implement `FlightService` dynamic query,
scaffold `SearchFlightPage.js`, and add Playwright tests.

```
