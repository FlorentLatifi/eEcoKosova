# 🎬 Demo Guide - EcoKosova Waste Management System

Ky dokument përmban skenarët e testimit për sistemin EcoKosova, të organizuara sipas utility tree dhe kërkesave funksionale.

## 📋 Përgatitja

### Startimi i Sistemit

#### Opsioni 1: Docker Compose (Rekomanduar)
```bash
# Në root directory të projektit
docker-compose up -d

# Shiko logs
docker-compose logs -f

# Stop
docker-compose down
```

#### Opsioni 2: Manual Start
```bash
# Backend
cd backend
mvn clean install
mvn spring-boot:run

# Frontend (në terminal tjetër)
cd frontend
npm install
npm run dev
```

### Verifikimi i Startimit

1. **Backend Health Check:**
   ```bash
   curl http://localhost:8080/actuator/health
   ```
   Duhet të kthejë: `{"status":"UP"}`

2. **Frontend:**
   - Hap browser: `http://localhost:3000`
   - Duhet të shfaqet Dashboard

---

## 🎯 Skenarët e Testimit

### Skenari 1: Monitorimi i Nivelit të Mbushjes (FR-01)

**Qëllimi:** Verifikimi i monitorimit në kohë reale të kontejnerëve

**Hapat:**
1. Hap Dashboard (`http://localhost:3000/dashboard`)
2. Vëzhgo panelin e statistikave:
   - Total kontejnerë
   - Kontejnerë kritikë (≥90%)
   - Kontejnerë me paralajmërim (≥70%)
   - Kontejnerë normalë
3. Kliko në një kontejner për të parë detajet
4. Në modal, përditëso nivelin e mbushjes duke përdorur slider
5. Verifiko që ndryshimi reflektohet në dashboard

**Rezultati i pritur:**
- Statistikat përditësohen automatikisht
- Toast notification shfaqet për sukses/gabim
- Niveli i ri shfaqet në ContainerCard

**API Test:**
```bash
# Merr të gjitha kontejnerët
curl http://localhost:8080/api/monitoring/containers

# Përditëso nivelin
curl -X PUT http://localhost:8080/api/monitoring/containers/CONTAINER_ID/fill-level \
  -H "Content-Type: application/json" \
  -d '{"fillLevel": 95}'
```

---

### Skenari 2: Njoftime Automatike (FR-02)

**Qëllimi:** Verifikimi i sistemit të njoftimeve për kontejnerët e mbushur

**Hapat:**
1. Përditëso një kontejner në nivel ≥90% (kritik)
2. Verifiko që njoftimi shfaqet në:
   - AlertList në Dashboard
   - Notifications dropdown (bell icon)
3. Kliko në notification për të parë detajet
4. Mark notification si read

**Rezultati i pritur:**
- Njoftimi krijohet automatikisht kur fillLevel ≥90%
- Notification shfaqet në real-time
- Unread count përditësohet

**API Test:**
```bash
# Merr njoftime kritike
curl http://localhost:8080/api/monitoring/containers/critical
```

---

### Skenari 3: Optimizimi i Rrugëve (FR-03)

**Qëllimi:** Testimi i route optimization me Strategy Pattern

**Hapat:**
1. Shko te Routes page (`/routes`)
2. Zgjedh një zonë
3. Zgjedh strategji:
   - **NEAREST_NEIGHBOR** (default)
   - **PRIORITY_BASED**
4. Verifiko që rruga optimale shfaqet me:
   - Numër kontejnerësh
   - Distancë totale (km)
   - Kohë e parashikuar (minuta)
   - Listë kontejnerësh në rend
5. Kliko "Shiko Detajet" për të parë rrugën në modal

**Rezultati i pritur:**
- Rruga llogaritet bazuar në strategjinë e zgjedhur
- NEAREST_NEIGHBOR: kontejnerët në rend sipas distancës
- PRIORITY_BASED: kontejnerët më të mbushur së pari

**API Test:**
```bash
# Rrugë optimale për zonë
curl "http://localhost:8080/api/routes/zone/ZONE_ID?startLat=42.6629&startLon=21.1655&strategy=NEAREST_NEIGHBOR"

# Rrugë bazuar në prioritet
curl "http://localhost:8080/api/routes/zone/ZONE_ID?strategy=PRIORITY_BASED"

# Të gjitha rrugët
curl "http://localhost:8080/api/routes/all?strategy=OPTIMAL"
```

---

### Skenari 4: Gjenerimi i Raporteve (FR-04)

**Qëllimi:** Testimi i sistemit të raporteve

**Hapat:**
1. Shko te Reports page (`/reports`)
2. Zgjedh llojin e raportit:
   - Raport Operacional
   - Raport Statistikor
   - Raport i Zonave
3. Kliko "Gjenero Raport"
4. Verifiko që raporti shfaqet me:
   - Titull dhe përshkrim
   - Të dhëna të strukturuara
   - Datë gjenerimi

**Rezultati i pritur:**
- Raporti gjenerohet me sukses
- Të dhënat janë të sakta dhe të përditësuara

**API Test:**
```bash
# Merr të gjitha raportet
curl http://localhost:8080/api/reports

# Gjenero raport të ri
curl -X POST http://localhost:8080/api/reports/generate \
  -H "Content-Type: application/json" \
  -d '{"type": "OPERATIONAL"}'
```

---

### Skenari 5: Menaxhimi i Kontejnerëve (CRUD)

**Qëllimi:** Testimi i operacioneve CRUD për kontejnerë

**Hapat:**
1. Shko te Containers page (`/containers`)
2. **Create:**
   - Kliko "Shto Kontejner të Ri" (nëse ekziston)
   - Plotëso formën me validation
   - Verifiko që gabimet shfaqen nëse fushat janë bosh
3. **Read:**
   - Shiko listën e kontejnerëve
   - Kliko në një kontejner për detaje
4. **Update:**
   - Në modal, përditëso nivelin e mbushjes
   - Verifiko që ndryshimi ruhet
5. **Delete:**
   - Fshi një kontejner (nëse funksionaliteti ekziston)

**Rezultati i pritur:**
- Form validation funksionon (react-hook-form + zod)
- Toast notifications për sukses/gabim
- Të dhënat përditësohen në real-time

**API Test:**
```bash
# Krijo kontejner
curl -X POST http://localhost:8080/api/containers \
  -H "Content-Type: application/json" \
  -d '{
    "id": "CONTAINER_NEW",
    "zoneId": "ZONE_ID",
    "type": "ORGANIC",
    "street": "Rruga e Re",
    "city": "Prishtinë",
    "municipality": "Prishtinë",
    "latitude": 42.6629,
    "longitude": 21.1655
  }'

# Përditëso kontejner
curl -X PUT http://localhost:8080/api/containers/CONTAINER_ID \
  -H "Content-Type: application/json" \
  -d '{"fillLevel": 75}'

# Fshi kontejner
curl -X DELETE http://localhost:8080/api/containers/CONTAINER_ID
```

---

### Skenari 6: Vizualizimi në Hartë

**Qëllimi:** Testimi i Map view me Leaflet

**Hapat:**
1. Shko te Dashboard
2. Kliko toggle "Hartë" (në vend të "Listë")
3. Verifiko që:
   - Harta shfaqet me OpenStreetMap
   - Markers shfaqen për çdo kontejner
   - Markers kanë ngjyrë bazuar në fillLevel:
     - 🔴 E kuqe: ≥90% (kritik)
     - 🟠 Portokalli: ≥70% (paralajmërim)
     - 🟢 E gjelbër: <70% (normal)
4. Kliko në një marker për të parë popup me detaje
5. Nëse ka më shumë kontejnerë në të njëjtin vend, verifiko cluster marker

**Rezultati i pritur:**
- Harta ngarkohet me sukses
- Markers janë të sakta dhe me ngjyra të duhura
- Clustering funksionon për kontejnerë të afërt

---

### Skenari 7: Health Checks dhe Monitoring

**Qëllimi:** Verifikimi i Spring Boot Actuator

**Hapat:**
1. Testo health endpoint:
   ```bash
   curl http://localhost:8080/actuator/health
   ```
2. Shiko metrics:
   ```bash
   curl http://localhost:8080/actuator/metrics
   ```
3. Shiko info:
   ```bash
   curl http://localhost:8080/actuator/info
   ```

**Rezultati i pritur:**
- Health check kthen `{"status":"UP"}`
- Metrics dhe info janë të disponueshme

---

### Skenari 8: Error Handling dhe Toast Notifications

**Qëllimi:** Testimi i error handling dhe user feedback

**Hapat:**
1. Provoni të përditësoni një kontejner me ID që nuk ekziston
2. Provoni të bëni një request me të dhëna të pavlefshme
3. Verifiko që:
   - Toast notification shfaqet për gabim
   - Mesazhi është i qartë dhe i kuptueshëm
   - Toast zhduket automatikisht pas 5 sekondave

**Rezultati i pritur:**
- Error handling funksionon në të gjitha rastet
- User merr feedback të qartë
- Toast notifications janë të bukura dhe informative

---

## 🧪 Testim i Integrimit

### Testim i Plotë të Flow

1. **Starto sistemin** (Docker ose manual)
2. **Përditëso një kontejner në nivel kritik** (≥90%)
3. **Verifiko që njoftimi krijohet** automatikisht
4. **Shko te Routes page** dhe gjenero rrugë optimale
5. **Shko te Reports** dhe gjenero raport
6. **Shiko hartën** dhe verifiko markers
7. **Testo form validation** duke krijuar/përditësuar kontejner

---

## 📊 Checklist i Verifikimit

- [ ] Backend starton me sukses
- [ ] Frontend starton me sukses
- [ ] Health check funksionon
- [ ] Dashboard shfaq të dhëna
- [ ] Container details modal funksionon
- [ ] Form validation funksionon
- [ ] Toast notifications shfaqen
- [ ] Map view funksionon
- [ ] Route optimization funksionon me të dy strategjitë
- [ ] Reports gjenerohen me sukses
- [ ] Error handling funksionon
- [ ] API endpoints janë të aksesueshme

---

## 🐛 Troubleshooting

### Backend nuk starton
- Verifiko që porti 8080 është i lirë
- Shiko logs: `docker-compose logs backend`
- Kontrollo që Java 17 është i instaluar

### Frontend nuk starton
- Verifiko që porti 3000 është i lirë
- Shiko logs: `docker-compose logs frontend`
- Kontrollo që Node.js 18+ është i instaluar

### CORS errors
- Verifiko vite.config.ts proxy configuration
- Kontrollo backend CORS settings në AppConfig.java

### Map nuk shfaqet
- Verifiko që Leaflet CSS është importuar
- Kontrollo console për errors
- Verifiko që lat/lon janë të vlefshme

---

## 📝 Shënime

- Të gjitha endpoint-et përdorin prefix `/api`
- Backend përdor JSON files për storage (dev mode)
- Frontend përdor environment variables për API_BASE_URL
- Health checks janë të konfiguruara në Docker

---

**Data e krijuar:** 2024  
**Version:** 1.0.0

