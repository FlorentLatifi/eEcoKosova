# Database Setup Guide

## Problemi: SQL Server Connection Error

Nëse shihni gabimin:
```
The TCP/IP connection to the host localhost, port 1433 has failed
```

Kjo do të thotë që SQL Server nuk është duke ekzekutuar ose nuk mund të lidhet.

## Zgjidhje 1: Përdor Development Profile me H2 (Recomanduar për Development)

Përdorni H2 in-memory database për development pa nevojë për SQL Server:

**Windows PowerShell:**
```powershell
cd backend
mvn spring-boot:run "-Dspring-boot.run.profiles=dev"
```

**Ose përdorni environment variable:**
```powershell
cd backend
$env:SPRING_PROFILES_ACTIVE="dev"; mvn spring-boot:run
```

**Linux/Mac/Git Bash:**
```bash
cd backend
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

Ky profile përdor H2 in-memory database dhe nuk kërkon SQL Server.

**H2 Console:** Nëse dëshironi të shikoni databazën, shkoni në: http://localhost:8080/h2-console
- JDBC URL: `jdbc:h2:mem:ecokosova`
- Username: `sa`
- Password: (bosh)

## Zgjidhje 2: Startoni SQL Server me Docker Compose

### Hapi 1: Krijo .env file

Kopjo `.env.example` në `.env` dhe përditëso password:

```bash
cd eEcoKosova
copy .env.example .env
# Ose në Linux/Mac: cp .env.example .env
```

Pastaj edito `.env` dhe vendos password të fortë për SQL Server.

### Hapi 2: Startoni SQL Server

```bash
docker-compose up -d mssql
```

Kjo do të startojë vetëm SQL Server. Prisni 10-20 sekonda që të startojë plotësisht.

### Hapi 3: Verifikoni që SQL Server është duke ekzekutuar

```bash
docker ps
```

Duhet të shihni `ecokosova-mssql` container duke ekzekutuar.

### Hapi 4: Startoni Backend

```bash
cd backend
mvn spring-boot:run
```

Ose nëse përdorni Docker Compose për të gjithë stack-in:

```bash
cd eEcoKosova
docker-compose up
```

## Zgjidhje 3: Instaloni SQL Server Lokal

Nëse keni SQL Server të instaluar lokal, sigurohuni që:
1. SQL Server është duke ekzekutuar
2. TCP/IP është i aktivizuar
3. Port 1433 është i hapur
4. Databaza `EcoKosova` ekziston ose lejoni që aplikacioni ta krijojë

## Profile të Disponueshme

- **default** - Përdor SQL Server (localhost:1433)
- **dev** - Përdor H2 in-memory database
- **docker** - Përdor SQL Server nëpërmjet docker network
- **test** - Përdor H2 për testing

## Troubleshooting

### SQL Server nuk starton në Docker

```bash
# Shikoni logs
docker logs ecokosova-mssql

# Nëse ka probleme, provoni të restartoni
docker-compose restart mssql
```

### Password nuk funksionon

SQL Server në Docker kërkon password të fortë. Përdorni:
- Të paktën 8 karaktere
- Të paktën një shkronjë të madhe
- Të paktën një numër
- Të paktën një karakter special (!@#$%^&*)

### Port 1433 është i zënë

Nëse porti 1433 është i zënë nga një instancë tjetër SQL Server, ndryshoni portin në `docker-compose.yml`:

```yaml
ports:
  - "1434:1433"  # Përdor port 1434 në host
```

Dhe përditëso `application.properties`:

```properties
spring.datasource.url=jdbc:sqlserver://localhost:1434;databaseName=EcoKosova;...
```

