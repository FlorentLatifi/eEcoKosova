# 🌿 EcoKosova - Waste Management System

[English](README.md)

Sistema inteligjente për menaxhimin e mbeturinave në Kosovë, e ndërtuar me Domain-Driven Design dhe Layered Architecture.

## 📋 Përmbledhje

EcoKosova është një sistem modern që mundëson:

- ♻️ Monitorim në kohë reale të kontejnerëve të mbeturinave
- 🔔 Njoftime automatike për kontejnerët e mbushur
- 🚛 Optimizim të rrugëve të mbledhjes
- 📊 Raporte dhe analiza operacionale
- 🗺️ Vizualizim interaktiv përmes web dashboard

## 🏗️ Arkitektura

### Layered Architecture (5 Shtresa)

```
┌─────────────────────────┐
│   Startup Layer         │
├─────────────────────────┤
│   Presentation Layer    │  ← REST API
├─────────────────────────┤
│   Application Layer     │  ← CQRS Commands/Queries
├─────────────────────────┤
│   Domain Layer          │  ← Business Logic (DDD)
├─────────────────────────┤
│   Infrastructure Layer  │  ← Persistence & Events
└─────────────────────────┘
```

### Domain-Driven Design

- **Bounded Contexts:** Monitoring, Collection, Reporting
- **Aggregates:** Kontenier, Zone
- **Value Objects:** FillLevel, Coordinates
- **Domain Events:** ContainerFullEvent, CollectionScheduledEvent

## 🎯 Design Patterns

✅ **Repository Pattern** - Data access abstraction  
✅ **Command Pattern (CQRS)** - Separation of reads and writes  
✅ **Observer Pattern** - Domain Events  
✅ **Factory Pattern** - Object creation  
✅ **Strategy Pattern** - Route optimization algorithms  
✅ **DTO Pattern** - API data transfer

## 🛠️ Teknologjitë

### Backend

- **Java 17** - Core language
- **Spring Boot 3.2** - REST API framework
- **Maven** - Build tool
- **Gson** - JSON serialization
- **JUnit 5** - Testing

### Frontend

- **React 18** - UI framework
- **Tailwind CSS** - Styling
- **Axios** - HTTP client
- **Lucide React** - Icons

## 📁 Struktura e Projektit

```
ecokosova/
├── backend/              # Java Spring Boot
│   ├── src/main/java/eco/kosova/
│   │   ├── domain/       # Domain Layer (DDD)
│   │   ├── application/  # Application Layer (CQRS)
│   │   ├── infrastructure/ # Infrastructure Layer
│   │   ├── presentation/ # REST API Controllers
│   │   └── startup/      # Dependency Injection
│   └── src/main/resources/
│       └── data/         # JSON data files
│
├── frontend/             # React App
│   └── src/
│       ├── components/   # UI Components
│       ├── services/     # API Services
│       └── hooks/        # Custom Hooks
│
└── README.md
```

## 🚀 Instalimi dhe Ekzekutimi

### Prerequisites

- Java 17+
- Maven 3.8+
- Node.js 18+
- npm 9+
- MSSQL Server (ose Docker me SQL Server)

### Backend Setup

```bash
# Navigate to backend
cd backend

# Full build me teste
mvn -DskipTests=false clean package

# Ose gjatë zhvillimit (pa teste)
mvn clean install

# Run application
mvn spring-boot:run
```

Backend do të startohet në: `http://localhost:8080`

#### Konfigurimi i databazës (MSSQL)

- `backend/src/main/resources/application.properties` përdor variabla mjedisi:
  - `SPRING_DATASOURCE_USERNAME` (default `sa`)
  - `SPRING_DATASOURCE_PASSWORD` (pa default – duhet vendosur)
- Për development, mund të krijosh një file `.env` (mos e commito) duke u bazuar në `docker-compose.yml`, p.sh.:

```bash
MSSQL_SA_PASSWORD=ChangeThisStrongPassword123!
SPRING_DATASOURCE_USERNAME=sa
SPRING_DATASOURCE_PASSWORD=ChangeThisStrongPassword123!
```

Spring Boot do të lexojë këto si environment variables kur starton në Docker.

### Frontend Setup

```bash
# Navigate to frontend
cd frontend

# Install dependencies
npm install

# Build për production
npm run build

# Run development server
npm run dev
```

Frontend do të startohet në: `http://localhost:3000`

## 🗄️ Startimi i plotë me Docker (backend + frontend + MSSQL)

```bash
docker-compose up --build
```

- MSSQL ruan fajllat në folderin `mssql-data/` në root të projektit.
- Backend lidhet me databazën `EcoKosova` në MSSQL duke përdorur variablat e mjedisit të konfiguruara.

## 📡 API Endpoints

### Containers

- `GET /api/containers` - Merr të gjitha kontejnerët
- `GET /api/containers/{id}` - Merr një kontejner specifik
- `PUT /api/containers/{id}/fill-level` - Përditëson nivelin e mbushjes

### Zones

- `GET /api/zones` - Merr të gjitha zonat
- `GET /api/zones/{id}/containers` - Merr kontejnerët e një zone

### Alerts

- `GET /api/alerts` - Merr njoftime aktive
- `GET /api/alerts/critical` - Merr vetëm njoftime kritike

### Reports

- `POST /api/reports/generate` - Gjeneron raport të ri
- `GET /api/reports/{id}` - Merr raport specifik

## 🧪 Testing

```bash
# Run all tests
mvn test

# Run tests with coverage
mvn test jacoco:report
```

## 👥 Ekipi Zhvillues

- **Prof:** Greta Ahma
- **Studentët:**
  - Diell Ajeti
  - Isa Bilalli
  - Florent Latifi
  - Shefket Dalipi

## 📚 Dokumentacioni

- **SAD (Software Architecture Document)**: `docs/SAD/SADEcoKosova.docx`
- **Detailed Design (DDD)**: `docs/DDD/DDD EcoKosova Waste Management System (1).docx`
- **Conceptual Model**: `docs/Conceptual/Dokumentimi i Modelit Konceptual.txt` dhe `docs/Conceptual/EcoKosova - Conceptual Model.drawio`
- **Demo & skenarë testimi**: `docs/DEMO.md`
- **Guides teknike** (në root):
  - `START_GUIDE.md` – si të startosh backend + frontend
  - `HOW_TO_START.md` – version i shkurtuar i komandave kryesore
  - `CRUD_IMPLEMENTIMI.md` – përmbledhje e CRUD endpoints + mapping backend/frontend
  - `INTEGRIMI_END_TO_END.md` – integrimi komplet frontend-backend-DB
  - `DEMONSTRIMI_I_PROJEKTIT.md` – udhëzime për demonstrim
  - `STATUS_IMPLEMENTIMI.md` – status i kërkesave dhe implementimeve

## 📄 Licenca

Ky projekt është zhvilluar për qëllime akademike në UBT - University for Business and Technology.

---

**Viti Akademik:** 2024-2025  
**Lënda:** Software Architecture & Design  
**Institucioni:** UBT - Kolegji për Shkenca Kompjuterike dhe Inxhinieri
