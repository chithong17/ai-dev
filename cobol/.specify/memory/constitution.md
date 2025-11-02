<<<<<<< HEAD
=======
<!--
Sync Impact Report

- Version change: unspecified → 1.0.0
- Modified principles: (new)set →
	- I. Spec-Accurate Reverse Engineering
	- II. UI Fidelity
	- III. Data Model Fidelity
	- IV. Test-First & E2E Validation
	- V. Modern, Containerized Architecture
- Added sections: Development Workflow, Additional Constraints
- Removed sections: none
- Templates requiring updates:
	- .specify/templates/plan-template.md: ✅ updated
	- .specify/templates/spec-template.md: ✅ updated
	- .specify/templates/tasks-template.md: ✅ updated
	- .specify/templates/commands/*.md: ⚠ pending (verify command docs reference generic agents)
- Follow-up TODOs: RATIFICATION_DATE (TODO)
-->

>>>>>>> b2f76f7502e7b5c07312b0f51c887fd4e7a2882d
# COBOL-AIRLINES Constitution

## Core Principles

<<<<<<< HEAD
### I. Accurate Translation of Business Logic
All business logic from COBOL must be accurately translated to Java, ensuring functional parity and adherence to original requirements.

### II. Data Integrity and Business Rules
Data integrity must be preserved across all operations. Business rules must be implemented consistently and validated rigorously.

### III. Modern React UI
The user interface must be developed using React, closely resembling the original COBOL interface while incorporating modern design principles.

### IV. Testing Standards
Each module must have at least three automated test cases to ensure functionality, reliability, and maintainability.

### V. Deployability
The system must support deployment via Docker containers or localhost setups, ensuring ease of deployment and testing.

## Additional Constraints

### Technology Stack Requirements
- COBOL for legacy systems.
- Java for modernized backend logic.
- React for frontend development.
- Docker for containerized deployment.

## Governance

- Amendments to this constitution require documentation, approval, and a migration plan.
- Compliance with principles must be verified during code reviews.
- Versioning follows semantic rules: MAJOR for incompatible changes, MINOR for new principles, PATCH for clarifications.

**Version**: 1.1.0 | **Ratified**: TODO(RATIFICATION_DATE) | **Last Amended**: 2025-11-02

<!-- Sync Impact Report:
Version change: 1.0.0 → 1.1.0
Modified principles: None (new additions)
Added sections: Core Principles, Additional Constraints
Templates requiring updates: ✅ plan-template.md, ✅ spec-template.md, ✅ tasks-template.md
Follow-up TODOs: Replace TODO(RATIFICATION_DATE) with actual date -->
=======
### I. Spec-Accurate Reverse Engineering (NON-NEGOTIABLE)
All business logic implemented in the modernized system MUST be directly reverse-engineered
from the legacy COBOL sources. No new or invented business rules are permitted unless
explicitly documented and approved by the project maintainers. Implementations MUST
reference the primary logic sources and include a mapping document (COBOL source →
modern implementation) and automated tests that demonstrate behavioral parity.

- Primary logic sources (MUST be used for authoritative behavior):
	- `CICS/LOGIN/LOGIN-COB`
	- `CICS/LOGIN/CRYPTO-VERIFICATION`
	- `CICS/SALES-MAP/SRCHFLY-COB`
	- `CICS/SALES-MAP/SELL1-COB`

Rationale: The hackathon goal is a faithful migration; correctness depends on literal
translation of legacy business behavior.

### II. UI Fidelity (NON-NEGOTIABLE)
The React frontend MUST reproduce the original terminal screens' layout and interaction
semantics. Visual fidelity is judged against the provided map files and screenshots.
Implementations MUST use CSS or CSS-in-JS to recreate the terminal look (black background,
colored text, fixed-width grid) and map field positions to the original maps.

- Primary UI sources (MUST be used):
	- `CICS/LOGIN/MAP1.png`
	- `CICS/SALES-MAP/SRCHFLY1.png`
	- `CICS/SALES-MAP/sell1-1.png`
	- `CICS/SALES-MAP/SRCHTKT2.png`
	- Map definitions: `CICS/LOGIN/LOGINMAP`, `CICS/SALES-MAP/SRCHFLI-MAP`, `CICS/SALES-MAP/SELL1-MAP`

Rationale: Judges require pixel-accurate terminal UX to validate the migration.

### III. Data Model Fidelity (NON-NEGOTIABLE)
The relational schema and data semantics MUST be extracted from the DB2 artifacts and
copybooks, then converted to PostgreSQL with equivalent constraints and types. The
deliverable MUST include: ERD, SQL migrations, and a documented mapping from DB2
definitions/copybooks to Postgres schema.

- Primary data sources (MUST be used):
	- `DB2/DER-COB-AIRLINES.pdf`
	- `DB2/create-db`
	- `DB2/DCLGEN/EMPLO-DCLGEN`, `DB2/DCLGEN/FLIGHT-DCLGEN`, `DB2/DCLGEN/PASSENG-DCLGEN`, `DB2/DCLGEN/TICKET-DCLGEN`

Rationale: Data integrity and reportability require faithful schema translation.

### IV. Test-First & E2E Validation (NON-NEGOTIABLE)
All code MUST be testable. Unit and integration tests MUST accompany implementations.
End-to-end tests using Playwright MUST be included and authored from the reverse-
engineered user journeys and the COBOL input/output behavior. Tests should be
written before implementation and validated as part of CI.

Rationale: Tests are the measurable contract confirming parity with legacy behavior.

### V. Modern, Containerized Architecture (REQUIRED)
The modernized system MUST use the approved stack and be deliverable as containers.
Minimum requirements: Java 17 + Spring Boot 3 for backend (Spring Data JPA), React 18
for frontend (functional components + Hooks), PostgreSQL for persistence, and
complete container artifacts (`Dockerfile`, `docker-compose.yml`) for local/dev
deployment.

Rationale: Ensures reproducible builds, CI/CD readiness, and cloud portability.

## Additional Constraints

- Security: Cryptographic verification implemented in `CICS/LOGIN/CRYPTO-VERIFICATION`
	MUST be preserved; where modern libraries are used, include a compatibility test
	demonstrating identical outputs for sample inputs from legacy tests.
- Performance: No explicit performance SLOs required for the hackathon, but heavy
	deviations from legacy response characteristics should be documented.

## Development Workflow

- All feature work MUST be driven by a spec in `specs/[feature]/spec.md` and an
	implementation plan. Each plan MUST include a "Constitution Check" demonstrating
	how the feature satisfies Principles I–V. PRs that change behavior MUST include
	mapping documents and updated Playwright tests.

- Intentionally retained template placeholders: `specs/[feature]/spec.md` is a
	deliberate path-template used across plans and templates; it indicates the
	expected spec folder for each feature and is not an unresolved governance token.

- Code reviews MUST verify:
	1. Source mapping to COBOL artifacts for behavioral changes.
	2. UI layout fidelity screenshots and diffs for relevant screens.
	3. Database migration correctness and sample data migration scripts.

## Governance

- Amendments: Changes to this constitution MUST be proposed through a PR that:
	1. Explains the rationale and scope of the amendment.
	2. Lists impacted principles, templates, and tests.
	3. Includes a migration plan for affected artifacts.
	Approval requires at least one maintainer review and a majority of active
	contributors on the PR (or explicit maintainers' approval if contributor list
	is small).
- Versioning: Semantic versioning for the constitution (MAJOR.MINOR.PATCH):
	- MAJOR: Backward-incompatible governance or principle removal/renaming.
	- MINOR: Addition of new principles or material expansions.
	- PATCH: Wording clarifications and non-semantic fixes.

**Version**: 1.0.0 | **Ratified**: TODO(RATIFICATION_DATE): provide original adoption date | **Last Amended**: 2025-11-02
>>>>>>> b2f76f7502e7b5c07312b0f51c887fd4e7a2882d
