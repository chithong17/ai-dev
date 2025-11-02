```markdown
# Feature Specification: User Authentication Service (Login)

**Feature Branch**: `1-user-auth`  
**Created**: 2025-11-02  
**Status**: Draft  
**Input**: User Authentication

**Constitution Gates**: This spec documents how the feature satisfies the
project constitution: reverse-engineering source mapping (`CICS/LOGIN/LOGIN-COB`,
`CICS/LOGIN/CRYPTO-VERIFICATION`), UI fidelity to `CICS/LOGIN/MAP1.png`, DB2→Postgres
mapping via `DB2/DCLGEN/EMPLO-DCLGEN`, and Playwright E2E scenarios derived from
legacy flows.

## User Scenarios & Testing *(mandatory)*

### User Story 1 - Employee login (Priority: P1)

An employee uses the terminal-style login screen to authenticate with their
employee identifier and password. On success the system issues a JWT and routes
the user to the appropriate post-login flow (Sales dept → flight search). On
failure the original error screen is shown.

**Why this priority**: Login is the gateway to all application flows and must match
legacy behavior exactly.

**Independent Test**: Playwright E2E test reproducing `MAP1.png` interaction:
fill `USERIDI` and `PASSWI`, submit, assert either a JWT is returned and the
post-login redirect is triggered, or the exact error message/screen from `MAP3.png`
is displayed.

**Acceptance Scenarios**:

1. **Given** employee exists and password matches, **When** user submits valid
   credentials on the login screen, **Then** server returns HTTP 200 with a JWT
   containing `empid` and `deptid`, and the frontend navigates to the post-login
   flow (Sales dept → SRCHFLI).
2. **Given** employee does not exist or password mismatch, **When** user submits
   credentials, **Then** the UI displays the exact message "PASSWORD OR USERID
   INCORRECT." and shows the legacy error screen layout (see `MAP3.png`).

---

## Functional Requirements *(mandatory & testable)*

- **FR-001**: The frontend MUST render a login page that visually matches
  `CICS/LOGIN/MAP1.png` and provides two fields: `USERIDI` (employee id) and
  `PASSWI` (password). Submission control must be keyboard-focusable to match
  terminal UX.
- **FR-002**: Backend MUST expose `POST /api/auth/login` that accepts JSON
  payload { "empid": "...", "password": "..." } and returns:
  - On success: 200 OK + { "token": "<JWT>", "empid": "...", "deptid": N }
  - On failure: 401 Unauthorized + { "error": "PASSWORD OR USERID INCORRECT." }
- **FR-003**: `AuthService` MUST perform a DB lookup equivalent to
  `SELECT ADMIDATE, DEPTID FROM EMPLO WHERE EMPID = :WS-USERID` using the
  converted Postgres `emplo` table (mapping from `DB2/DCLGEN/EMPLO-DCLGEN`).
- **FR-004**: `AuthService` MUST implement the hashing algorithm from
  `CICS/LOGIN/CRYPTO-VERIFICATION` exactly. Given inputs (password, empid,
  admidate) it must produce a `cryptpass` value and compare it to the persisted
  stored password. The comparison semantics (success flag) MUST match the COBOL
  program (`LS-FLAG-RETURN = 0` on match).
- **FR-005**: On successful authentication, the service MUST emit a signed JWT
  (using project key material) containing `empid` and `deptid` and an expiry
  (standard short TTL). The JWT format and signing algorithm will be documented
  in the implementation plan (not in this spec).
- **FR-006**: If `deptid == 7` (Sales), the service response or frontend MUST
  trigger the SRCHFLI flow. For the hackathon this can be a frontend redirect to
  the flight-search route; backend should include deptid in JWT for authorization.
- **FR-007**: All behavior changes MUST include a mapping document that links
  the implementation code back to the COBOL source lines (file and region).

## Key Entities *(include if feature involves data)*

- **Employee (emplo)**: represents an employee record converted from DB2;
  - empid (PK) — string
  - admidate — date (used as seed)
  - deptid — integer
  - stored_cryptpass — string (legacy hashed password migrated from PASSDOC or
    equivalent)

## Success Criteria *(mandatory)*

- **SC-001**: Playwright E2E test reproducing MAP1 login with a known good
  credential returns a JWT and verifies the JWT contains `empid` and `deptid`.
- **SC-002**: Playwright E2E test reproducing MAP1 with wrong credentials shows
  the exact legacy error message and matches `MAP3.png` visual snapshot within
  approved pixel-diff tolerance (documented in plan).
- **SC-003**: A DB2→Postgres mapping document exists for `EMPLO` (from
  `DB2/DCLGEN/EMPLO-DCLGEN`) and migration scripts produce a working Postgres
  `emplo` table used by `AuthService`.
- **SC-004**: Unit tests for `AuthService` demonstrate the Java implementation
  of `CRYPTO-VERIFICATION` produces identical `cryptpass` outputs for test
  vectors taken from legacy COBOL or `EMPLO-OUTPUT-PASS-CRYPT` samples.

## Edge Cases

- Employee missing admidate or malformed admidate — service must fail safely and
  surface a helpful error to ops (not to end-user) and treat as authentication
  failure for the UI.
- Concurrent logins — stateless JWT issuance must remain idempotent.
- Legacy PASSDOC file formats — a migration plan must normalize and populate
  `stored_cryptpass` in the `emplo` table.

## Assumptions

- The PASSDOC legacy storage (file) will be migrated into a Postgres column
  `stored_cryptpass` prior to enabling authentication for migrated users.
- JWT signing keys and expiry policies will be decided in the implementation
  plan and kept out of this spec (security ops task).
- The hashing algorithm in `CRYPTO-VERIFICATION` can be reproduced deterministically
  in Java using standard Java APIs; any platform-specific random seed behavior
  will be documented and matched exactly.

## Implementation Notes (for planning)

- Backend stack: Java 17 + Spring Boot 3 (Spring Data JPA) — used for planning
  and implementation tasks only; the spec remains technology-agnostic for tests.
- Frontend: React 18 functional components — UI must match `MAP1.png` visually.
- Testing: Playwright E2E tests must be created to cover acceptance scenarios.

---

This specification was produced by reverse-engineering the legacy artifacts
identified in the feature request (see Constitution Gates above). The next step
is to create an implementation plan and tasks that produce the DB migration,
backend endpoint and service, frontend login screen, Playwright tests, and
mapping documents.

```
