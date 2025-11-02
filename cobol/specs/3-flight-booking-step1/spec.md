```markdown
# Feature Specification: Flight Booking - Step 1 (Sell Screen 1)

**Feature Branch**: `3-flight-booking-step1`  
**Created**: 2025-11-02  
**Status**: Draft  
**Input**: FlightBookingStep1

**Constitution Gates**: This spec documents how the feature satisfies the
project constitution: reverse-engineering source mapping (`CICS/SALES-MAP/SELL1-COB`),
UI fidelity to `CICS/SALES-MAP/sell1-1.png`, DB2→Postgres mapping via
`DB2/DCLGEN/PASSENG-DCLGEN` and `DB2/DCLGEN/FLIGHT-DCLGEN`, and Playwright E2E
scenarios derived from legacy flows.

## User Scenarios & Testing *(mandatory)*

### User Story 1 - Research a booking (Priority: P1)

Sales staff enter client id, flight number, flight date, and passenger count on the
Sell Screen 1. Pressing Research (F11) triggers server-side validation of client
and flight existence. If both exist, the server returns flight details, client
name, unit price 120.99, and computed total price (unit price * passengerCount)
which the UI displays in read-only fields. If a lookup fails, the UI shows
the exact legacy error message.

**Why this priority**: This step validates critical booking pre-conditions and
drives the next step in the sales flow.

**Independent Test**: Playwright E2E test reproducing `sell1-1.png` interactions:
enter inputs, press Research, assert returned BookingQuote populates read-only
fields and computed total price; assert error messages match legacy text when
lookups return no rows.

**Acceptance Scenarios**:

1. **Given** valid `clientId` and existing passenger record, **When** Research is pressed,
   **Then** server returns client `firstName` and `lastName`.
2. **Given** valid `flightNum` and `date` matching a flight, **When** Research is pressed,
   **Then** server returns flight details (flightId, departTime, depAirport, arrAirport).
3. **Given** both validations pass, **When** Research is pressed, **Then** server returns
   a `BookingQuote` with `unitPrice = 120.99` and `totalPrice = 120.99 * passengerCount`.
4. **Given** client missing, **When** Research is pressed, **Then** UI shows message
   "THIS PASSENGER DOES NOT EXIST" exactly and prevents proceeding.
5. **Given** flight missing, **When** Research is pressed, **Then** UI shows message
   "THIS FLIGHT DOES NOT EXIST" exactly and prevents proceeding.

---

## Functional Requirements *(mandatory & testable)*

- **FR-001**: Frontend MUST render `BookingStep1.js` that visually matches
  `sell1-1.png`. Fields: `CLIIDI` (clientId), `FNUMI` (flightNum), `FDATEI` (date),
  `PASSNI` (passengerCount), plus Research (F11) and Insert Passengers (F12)
  controls.
- **FR-002**: Backend MUST expose `POST /api/bookings/validate-step1` accepting
  JSON payload: { clientId, flightNum, date, passengerCount } and returning:
  - Success (200): { bookingQuote: { client: {firstName,lastName,clientId}, flight: {...}, unitPrice, totalPrice } }
  - Failure (400/404): { error: "THIS PASSENGER DOES NOT EXIST" } or
    { error: "THIS FLIGHT DOES NOT EXIST" } matching exact legacy text.
- **FR-003**: `BookingService.validateStep1` MUST run the equivalent queries:
  1. Passenger lookup: `SELECT FIRSTNAME, LASTNAME FROM PASSENGERS WHERE CLIENTID = :clientId` — if no rows, return passenger-nonexist error.
  2. Flight lookup: `SELECT FLIGHTID, DEPTIME, AIRPORTDEP, AIRPORTARR FROM FLIGHT WHERE FLIGHTDATE = :date AND FLIGHTNUM = :flightNum` — if no rows, return flight-nonexist error.
- **FR-004**: On success, `BookingService` MUST set `unitPrice = 120.99` (hardcoded) and compute `totalPrice = unitPrice * passengerCount` using decimal-accurate arithmetic to match COBOL numeric behavior.
- **FR-005**: The `POST /api/bookings/validate-step1` response MUST include all fields necessary for the UI read-only outputs (flightId, flightNum, date, depAirport, arrAirport, departTime, client names, unitPrice, totalPrice).
- **FR-006**: On Insert Passengers (F12) the frontend MUST navigate to `BookingStep2.js`, passing the `BookingQuote` in navigation state; the backend will not perform the insert at this endpoint (matching legacy XCTL to SELLCOB2 behavior).
- **FR-007**: All behavior must include mapping documentation linking each validation/lookup to the corresponding COBOL source lines in `SELL1-COB`.

## Key Entities *(data involved)*

- **Passenger (passeng)**: from `PASSENG-DCLGEN` — clientId, firstName, lastName, passn (passenger sequence) etc.
- **Flight (flight)**: from `FLIGHT-DCLGEN` — flightId, flightNum, flightDate, deptTime, airportDep, airportArr, etc.
- **BookingQuote**: DTO returned by validate endpoint: { client: {...}, flight: {...}, unitPrice: decimal, totalPrice: decimal, passengerCount }

## Success Criteria *(mandatory)*

- **SC-001**: Playwright E2E test shows the UI populated with client and flight details
  and a `totalPrice` equal to `120.99 * passengerCount` for valid inputs.
- **SC-002**: Unit tests cover passenger-missing and flight-missing cases and assert
  the API returns the exact legacy error messages.
- **SC-003**: Decimal arithmetic unit tests show `totalPrice` matches COBOL-like decimal
  computations for representative inputs (e.g., passengerCount = 1, 2, 3).

## Edge Cases

- Non-numeric `passengerCount` or zero/negative — return 400 Bad Request and show
  client-side validation message.
- Missing admidate or flightDate format mismatch — return 400 with validation error.
- Multiple flight rows (duplicates) — define selection policy in implementation plan (default: take first matching row ordered by flightId unless legacy code indicates otherwise).

## Assumptions

- Unit price is hardcoded at 120.99 in the legacy program and must be preserved exactly.
- The legacy XCTL to `SELLCOB2` corresponds to the frontend navigating to a second
  booking screen carrying the validated booking data; actual passenger insertions
  will be handled by a separate endpoint/program.

## Implementation Notes (for planning)

- Backend: Java 17 + Spring Boot 3 (Spring Data JPA). Implement `BookingService.validateStep1` with two repository lookups and decimal arithmetic using BigDecimal.
- Frontend: React 18 functional components; `BookingStep1.js` must match `sell1-1.png` visually and capture F11/F12 actions as buttons.
- Testing: Playwright tests to validate UI behavior and exact legacy messages; unit tests for service logic.

---

Next steps: create implementation plan, tasks, and scaffold controller (`BookingController`), service (`BookingService`), DTOs, `BookingStep1.js` and Playwright tests. Document mapping from `SELL1-COB` to implementation.

```
