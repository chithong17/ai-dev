---
description: Execute the implementation planning workflow using the plan template to generate design artifacts.
---

## User Input

```text
$ARGUMENTS
```

You **MUST** consider the user input before proceeding (if not empty).

## Outline

1. **Setup**: Run `.specify/scripts/powershell/setup-plan.ps1 -Json` from repo root and parse JSON for FEATURE_SPEC, IMPL_PLAN, SPECS_DIR, BRANCH. For single quotes in args like "I'm Groot", use escape syntax: e.g 'I'\''m Groot' (or double-quote if possible: "I'm Groot").

2. **Load context**: Read FEATURE_SPEC and `.specify/memory/constitution.md`. Load IMPL_PLAN template (already copied).

3. **Execute plan workflow**: Follow the structure in IMPL_PLAN template to:
   - Fill Technical Context (mark unknowns as "NEEDS CLARIFICATION")
   - Fill Constitution Check section from constitution
   - Evaluate gates (ERROR if violations unjustified)
   - Phase 0: Generate research.md (resolve all NEEDS CLARIFICATION)
   - Phase 1: Generate data-model.md, contracts/, quickstart.md
   - Phase 1: Update agent context by running the agent script
   - Re-evaluate Constitution Check post-design

4. **Stop and report**: Command ends after Phase 2 planning. Report branch, IMPL_PLAN path, and generated artifacts.

## Phases

### Phase 0: Outline & Research

1. **Extract unknowns from Technical Context** above:
   - For each NEEDS CLARIFICATION → research task
   - For each dependency → best practices task
   - For each integration → patterns task

2. **Generate and dispatch research agents**:

   ```text
   For each unknown in Technical Context:
     Task: "Research {unknown} for {feature context}"
   For each technology choice:
     Task: "Find best practices for {tech} in {domain}"
   ```

3. **Consolidate findings** in `research.md` using format:
   - Decision: [what was chosen]
   - Rationale: [why chosen]
   - Alternatives considered: [what else evaluated]

**Output**: research.md with all NEEDS CLARIFICATION resolved

### Phase 1: Design & Contracts

**Prerequisites:** `research.md` complete

1. **Extract entities from feature spec** → `data-model.md`:
   - Entity name, fields, relationships
   - Validation rules from requirements
   - State transitions if applicable

2. **Generate API contracts** from functional requirements:
   - For each user action → endpoint
   - Use standard REST/GraphQL patterns
   - Output OpenAPI/GraphQL schema to `/contracts/`

3. **Agent context update**:
   - Run `.specify/scripts/powershell/update-agent-context.ps1 -AgentType copilot`
   - These scripts detect which AI agent is in use
   - Update the appropriate agent-specific context file
   - Add only new technology from current plan
   - Preserve manual additions between markers

**Output**: data-model.md, /contracts/*, quickstart.md, agent-specific file

## Key rules

- Use absolute paths
- ERROR on gate failures or unresolved clarifications


# SpecKit Phase 2 – Implementation Prompt

## Goal
Continue project setup for the **Airline Management System**.  
Generate full backend and frontend implementation based on the planning artifacts:
- research.md
- data-model.md
- contracts/openapi.yaml
- quickstart.md

## Stack
- **Backend:** Java Spring Boot (JPA/Hibernate, REST, Lombok)
- **Frontend:** React + Tailwind CSS (TypeScript)
- **Database:** PostgreSQL
- **Testing:** JUnit (backend), Playwright (frontend)
- **Deployment:** Local (Docker optional)

---

## Tasks

### 🏗️ Backend Generation
Inside `/backend`:
1. Create full Spring Boot structure under `com.example.airline`:
   - `entity/` → based on `data-model.md`
   - `repository/` → extend `JpaRepository`
   - `service/` → CRUD and business logic
   - `controller/` → REST endpoints defined in `contracts/openapi.yaml`
   - `dto/` and `mapper/` → optional data transfer objects
2. Configure:
   - PostgreSQL connection in `application.properties`
   - Global exception handling and validation
3. Add sample JUnit tests for one entity (e.g., Flight).

---

### 💻 Frontend Generation
Inside `/frontend`:
1. Initialize React + Tailwind project (TypeScript template).
2. Create components/pages:
   - `pages/FlightList.tsx`
   - `pages/FlightForm.tsx`
   - `components/Navbar.tsx`
   - `services/api.ts` → handles API requests to backend
3. Implement CRUD UI for each major entity defined in `data-model.md`.
4. Ensure responsive design using Tailwind classes.
5. Add simple Playwright test for one page (e.g., checking flight list loads).

---

### ⚙️ Integration
1. Configure CORS in backend to allow frontend access.
2. Add a `docker-compose.yml` (optional) with:
   - `backend`
   - `frontend`
   - `postgres`
3. Update `quickstart.md` with new setup instructions:
   - Run database
   - Start backend
   - Start frontend
   - Access app at `http://localhost:3000`

---

### ✅ Deliverables
After running this phase, the project folder should contain:

```
/backend
  ├── src/main/java/com/example/airline/
  │     ├── entity/
  │     ├── repository/
  │     ├── service/
  │     ├── controller/
  │     └── dto/
  ├── src/test/java/com/example/airline/
  ├── pom.xml
/frontend
  ├── src/pages/
  ├── src/components/
  ├── src/services/
  ├── package.json
contracts/openapi.yaml
data-model.md
quickstart.md
research.md
```

Then proceed to testing and manual refinement.

---

## Command Hint
If CLI is available:
```bash
speckit implement
```
If blocked, execute this prompt manually as a system plan for Phase 2.
