# EcoKosova: waste management system

[Shqip](README.sq.md)

A web system for managing waste collection in Kosovo. Operators see the fill level of every container, get alerted when containers are full, plan collection cycles, assign routes to trucks and generate reports.

Built as a team project for the Software Architecture & Design course at UBT (2024–2025), using domain-driven design and a layered architecture.

## Features

- Container monitoring by zone, with critical containers flagged
- Collection cycles and truck route assignment
- Collection routes per zone
- Operational reports
- Map view of containers
- JWT authentication

## Architecture

Five layers:

```
┌─────────────────────────┐
│   Startup               │  ← dependency injection, wiring
├─────────────────────────┤
│   Presentation          │  ← REST API
├─────────────────────────┤
│   Application           │  ← CQRS commands and queries
├─────────────────────────┤
│   Domain                │  ← business logic (DDD)
├─────────────────────────┤
│   Infrastructure        │  ← persistence and events
└─────────────────────────┘
```

**Domain-driven design**

- Bounded contexts: Monitoring, Collection, Reporting
- Aggregates: Container, Zone
- Value objects: FillLevel, Coordinates
- Domain events: ContainerFullEvent, CollectionScheduledEvent

**Patterns:** Repository, CQRS, Observer (domain events), Factory, Strategy (route optimisation), DTOs at the API boundary.

## Tech stack

| | |
|---|---|
| Backend | Java 17, Spring Boot 3.2 (Web, Data JPA, Security, Validation, Actuator), JWT, Flyway |
| Database | SQL Server; H2 for tests |
| API docs | springdoc OpenAPI (Swagger UI) |
| Frontend | React 18, TypeScript, Vite, Tailwind CSS, React Router, React Hook Form + Zod, Axios, Leaflet |
| Tooling | Maven, JUnit 5, Docker Compose, GitHub Actions |

## Getting started

### Everything with Docker

```bash
cp .env.example .env
```

Set your own passwords in `.env`, then:

```bash
docker compose up --build
```

| Service | URL |
|---|---|
| Frontend | http://localhost:3000 |
| Backend API | http://localhost:8080 |
| Swagger UI | http://localhost:8080/swagger-ui/index.html |
| SQL Server | localhost:1433 |

### Backend only

Requires Java 17+, Maven 3.8+ and a running SQL Server. The backend reads `SPRING_DATASOURCE_USERNAME` and `SPRING_DATASOURCE_PASSWORD` from the environment.

```bash
cd backend
mvn clean package
mvn spring-boot:run
```

### Frontend only

Requires Node.js 20.19+.

```bash
cd frontend
npm install
npm run dev
```

## API

| Base path | What it covers |
|---|---|
| `/api/auth` | Login and registration |
| `/api/monitoring/containers` | Fill levels, critical containers, containers by zone |
| `/api/containers` | Create, update and delete containers; schedule collection; mark emptied |
| `/api/zones` | Zones and zone statistics |
| `/api/routes` | Collection routes per zone |
| `/api/ciklet` | Collection cycles |
| `/api/kamionet` | Trucks and route assignment |
| `/api/reports` | Generate and read reports |
| `/api/qytetaret` | Citizens |
| `/api/kontroll-panel` | Citizen control panels |

Full, interactive documentation is in Swagger UI.

## Tests

```bash
cd backend
mvn test
```

CI builds and tests the backend and builds the frontend on every push to `main`.

## Team

- **Professor:** Greta Ahma
- **Students:** Diell Ajeti, Isa Bilalli, Florent Latifi, Shefket Dalipi

## Documentation

- Software Architecture Document: `docs/SAD/`
- Detailed design: `docs/DDD/`
- Conceptual model: `docs/Conceptual/`
- Demo and test scenarios: `docs/DEMO.md` (Albanian)

## License

Academic project developed at UBT – University for Business and Technology.
