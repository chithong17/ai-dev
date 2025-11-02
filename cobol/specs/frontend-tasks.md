---
description: "Frontend implementation tasks for COBOL-AIRLINES migration"
---

# Frontend Tasks: COBOL-AIRLINES

Phase: Frontend Setup & Feature Implementation (Login, Search, Booking)

## Phase A: Frontend Setup

- [X] T033 Initialize frontend project at `frontend/` with `package.json` and dependencies (react, react-dom, react-router-dom, axios, @playwright/test, @testing-library/react)
- [X] T034 Create `frontend/Dockerfile` to containerize the frontend app
- [X] T035 Create `frontend/src/index.js` and `frontend/src/index.css` (app entry)
- [X] T036 Create `frontend/src/lib/api.js` (axios instance configured with base URL and error handling)
- [X] T037 Create `frontend/src/styles/theme.css` (global monospace terminal theme and layout variables)

## Phase B: Shared Components & State

- [X] T038 Create `frontend/src/components/TerminalInput.js` — reusable keyboard-focusable input component used by terminal-style pages
- [X] T039 Create `frontend/src/components/FlightResults.js` — presentational component for rendering up to 10 result rows
- [X] T040 Create `frontend/src/components/BookingQuote.js` — presentational component for booking quote read-only fields
- [X] T041 Add `frontend/src/state/useAuth.js` — lightweight auth hook for storing JWT and user context

## Phase C: US1 — Login (Priority: P1)

- [X] T042 Implement `frontend/src/pages/LoginPage.js` (terminal-style layout, fields `USERIDI` and `PASSWI`, keyboard navigation, POST to `/api/auth/login` via `lib/api.js`)
- [X] T043 Add form validation and inline error UI matching legacy messages and layout
- [X] T044 Add unit tests `frontend/src/__tests__/LoginPage.test.js` using React Testing Library (happy path + invalid password)
- [X] T045 Add Playwright E2E test `frontend/tests/login.spec.js` that reproduces `MAP1.png` interactions and stores snapshot baseline in `frontend/tests/snapshots/login/`

## Phase D: US2 — Flight Search (Priority: P1)

- [X] T046 Implement `frontend/src/pages/SearchFlightPage.js` matching `SRCHFLY1.png` layout and calling GET `/api/flights/search`
- [X] T047 Integrate `FlightResults` into `SearchFlightPage.js` and ensure keyboard navigation between rows
- [X] T048 Add unit tests `frontend/src/__tests__/SearchFlightPage.test.js` (predicate mapping and UI behavior)
- [X] T049 Add Playwright E2E test `frontend/tests/search.spec.js` with snapshots in `frontend/tests/snapshots/search/`

## Phase E: US3 — Booking Step1 (Priority: P1)

- [X] T050 Implement `frontend/src/pages/BookingStep1.js` matching `sell1-1.png` and wired to POST `/api/bookings/validate-step1`
- [X] T051 Integrate `BookingQuote` component into `BookingStep1.js` and implement F11 (Research) and F12 (Insert Passengers) behaviors
- [X] T052 Add unit tests `frontend/src/__tests__/BookingStep1.test.js` (passenger missing, flight missing, successful quote)
- [X] T053 Add Playwright E2E test `frontend/tests/booking-step1.spec.js` with snapshots in `frontend/tests/snapshots/booking/`

## Phase F: Accessibility, Visual Regression & CI

- [X] T054 Add accessibility checks and keyboard navigation tests `frontend/tests/accessibility.spec.js`
- [X] T055 Add visual regression workflow: baseline snapshots under `frontend/tests/snapshots/` and a Playwright compare job
- [X] T056 Add CI workflow file `.github/workflows/playwright.yml` to run Playwright E2E and snapshot comparisons on pushes and PRs
- [X] T057 Create `frontend/Dockerfile` (if not already created in T034) and ensure build artifacts are produced for `docker-compose` integration

## Phase G: Polish

- [X] T058 Final: Visual snapshot baselining and verification images under `frontend/tests/snapshots/` (copy golden images here and commit)
- [X] T059 Update `README.md` with frontend run/build instructions and Playwright E2E developer guide

## Notes

- Tasks marked [P] are parallelizable where implementation does not conflict with other tasks.
- All tests must be written so they can be executed in CI (use `--ci` flags when appropriate) and snapshot images should be stored under `frontend/tests/snapshots/`.
