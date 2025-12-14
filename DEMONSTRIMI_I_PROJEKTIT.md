# 🎬 Udhëzues për Demonstrimin e Projektit - EcoKosova

Ky dokument përmban udhëzuesin e plotë për demonstrimin e të gjitha funksionaliteteve gjatë mbrojtjes së projektit.

## 📋 Përgatitja

### 1. Startimi i Sistemit

```bash
# Terminal 1: Backend
cd backend
mvn clean install
mvn spring-boot:run

# Terminal 2: Frontend
cd frontend
npm install
npm run dev
```

### 2. Verifikimi i Startimit

- Backend: `http://localhost:8080/actuator/health` → `{"status":"UP"}`
- Frontend: `http://localhost:3000` → Duhet të shfaqet Dashboard

---

## 🎯 Skenarët e Demonstrimit

### Skenari 1: Monitorimi i Kontejnerëve (FR-01)

**Hapi 1:** Hap Dashboard (`/dashboard`)
- ✅ Verifiko që kontejnerët shfaqen
- ✅ Verifiko statistikat (Total, Kritikë, Paralajmërim, Normal)
- ✅ Verifiko AlertList për kontejnerët kritikë

**Hapi 2:** Kliko në një kontejner
- ✅ Modal shfaqet me detaje
- ✅ Slider për përditësimin e nivelit të mbushjes
- ✅ Form validation funksionon (0-100%)

**Hapi 3:** Përditëso nivelin e mbushjes
- ✅ Zgjidh nivel ≥90% për të testuar njoftimet
- ✅ Toast notification shfaqet për sukses
- ✅ Dashboard përditësohet automatikisht
- ✅ Njoftimi krijohet në AlertList

**Hapi 4:** Shiko hartën
- ✅ Kliko toggle "Hartë" në Dashboard
- ✅ Verifiko që markers shfaqen me ngjyra të duhura
- ✅ Kliko në marker për popup me detaje

**Endpoints të testuara:**
- `GET /api/monitoring/containers`
- `PUT /api/monitoring/containers/{id}/fill-level`

---

### Skenari 2: Menaxhimi i Kontejnerëve (CRUD)

**Hapi 1:** Shko te Containers Page (`/containers`)
- ✅ Shfaq listën e të gjitha kontejnerëve
- ✅ Verifiko që të dhënat janë të sakta

**Hapi 2:** Krijoni kontejner të ri
- ✅ Kliko "Shto Kontejner"
- ✅ Plotëso formën me validation
- ✅ Verifiko që gabimet shfaqen nëse fushat janë bosh
- ✅ Krijoni kontejnerin
- ✅ Verifiko që shfaqet në listë

**Hapi 3:** Përditësoni kontejner
- ✅ Kliko në një kontejner
- ✅ Përditëso informacionin
- ✅ Verifiko që ndryshimet ruhen

**Hapi 4:** Fshini kontejner
- ✅ Kliko "Fshi"
- ✅ Konfirmo fshirjen
- ✅ Verifiko që kontejneri hiqet nga lista

**Endpoints të testuara:**
- `GET /api/containers`
- `POST /api/containers`
- `PUT /api/containers/{id}`
- `DELETE /api/containers/{id}`

---

### Skenari 3: Menaxhimi i Zonave

**Hapi 1:** Shko te Zones Page (`/zones`)
- ✅ Shfaq statistikat për të gjitha zonat
- ✅ Verifiko që të dhënat janë të sakta

**Hapi 2:** Krijoni zonë të re
- ✅ Plotëso formën
- ✅ Krijoni zonën
- ✅ Verifiko që shfaqet në listë

**Endpoints të testuara:**
- `GET /api/zones/statistics`
- `POST /api/zones`

---

### Skenari 4: Optimizimi i Rrugëve (FR-03)

**Hapi 1:** Shko te Routes Page (`/routes`)
- ✅ Shfaq listën e rrugëve për të gjitha zonat

**Hapi 2:** Zgjedh strategji
- ✅ Zgjedh "NEAREST_NEIGHBOR" (default)
- ✅ Verifiko që rruga llogaritet
- ✅ Zgjedh "PRIORITY_BASED"
- ✅ Verifiko që rruga ndryshon

**Hapi 3:** Shiko detajet e rrugës
- ✅ Kliko "Shiko Detajet"
- ✅ Verifiko që modal shfaqet me:
  - Numër kontejnerësh
  - Distancë totale
  - Kohë e parashikuar
  - Listë kontejnerësh në rend

**Endpoints të testuara:**
- `GET /api/routes/all?strategy=NEAREST_NEIGHBOR`
- `GET /api/routes/zone/{zoneId}?strategy=PRIORITY_BASED`

---

### Skenari 5: Gjenerimi i Raporteve (FR-04)

**Hapi 1:** Shko te Reports Page (`/reports`)
- ✅ Shfaq listën e raporteve ekzistuese

**Hapi 2:** Gjenero raport të ri
- ✅ Zgjedh llojin e raportit
- ✅ Kliko "Gjenero Raport"
- ✅ Verifiko që raporti shfaqet me të dhëna

**Endpoints të testuara:**
- `GET /api/reports`
- `POST /api/reports/generate`

---

### Skenari 6: Menaxhimi i Kamionëve (I RI)

**Hapi 1:** Shko te Kamionët Page (`/kamionet`)
- ✅ Shfaq listën e të gjitha kamionëve
- ✅ Verifiko që ka kamionë demo

**Hapi 2:** Filtro kamionët e disponueshëm
- ✅ Kliko "Të Disponueshëm"
- ✅ Verifiko që vetëm kamionët e disponueshëm shfaqen

**Hapi 3:** Krijo kamion të ri
- ✅ Kliko "Shto Kamion"
- ✅ Plotëso formën:
  - ID: KAM-003
  - Emri: Kamion Mbledhje 3
  - Targat: KS-789-EF
  - Kapaciteti: 6000
  - Operatori: OP-003
  - Koordinata: 42.6829, 21.1855
- ✅ Krijoni kamionin
- ✅ Verifiko që shfaqet në listë

**Hapi 4:** Cakto kamionin në rrugë
- ✅ Kliko "Cakto Rrugë" (nëse funksionaliteti ekziston)
- ✅ Ose përditëso kamionin për të caktuar rrugë

**Hapi 5:** Përditëso kamion
- ✅ Kliko "Përditëso"
- ✅ Ndrysho statusin ose informacionin
- ✅ Verifiko që ndryshimet ruhen

**Endpoints të testuara:**
- `GET /api/kamionet`
- `GET /api/kamionet/available`
- `POST /api/kamionet`
- `PUT /api/kamionet/{id}`
- `POST /api/kamionet/{id}/assign-route`
- `POST /api/kamionet/{id}/release-route`

---

### Skenari 7: Menaxhimi i Qytetarëve (I RI)

**Hapi 1:** Shko te Qytetarët Page (`/qytetaret`)
- ✅ Shfaq listën e të gjithë qytetarëve
- ✅ Verifiko që ka qytetarë demo

**Hapi 2:** Krijo qytetar të ri
- ✅ Kliko "Shto Qytetar"
- ✅ Plotëso formën:
  - ID: QYT-003
  - Emri: Arben Gashi
  - Adresa: Rruga e Dukagjinit, Prishtinë
- ✅ Krijoni qytetarin
- ✅ Verifiko që shfaqet në listë

**Hapi 3:** Përditëso qytetar
- ✅ Kliko "Përditëso"
- ✅ Ndrysho emrin ose adresën
- ✅ Verifiko që ndryshimet ruhen

**Hapi 4:** Fshi qytetar
- ✅ Kliko "Fshi"
- ✅ Konfirmo fshirjen
- ✅ Verifiko që qytetari hiqet

**Endpoints të testuara:**
- `GET /api/qytetaret`
- `POST /api/qytetaret`
- `PUT /api/qytetaret/{id}`
- `DELETE /api/qytetaret/{id}`

---

### Skenari 8: Menaxhimi i Cikleve të Mbledhjes (I RI)

**Hapi 1:** Shko te Ciklet Page (`/ciklet`)
- ✅ Shfaq listën e të gjitha cikleve
- ✅ Verifiko që ka cikël demo

**Hapi 2:** Filtro ciklet aktive
- ✅ Kliko "Aktive"
- ✅ Verifiko që vetëm ciklet aktive shfaqen

**Hapi 3:** Krijo cikël të ri
- ✅ Kliko "Krijo Cikël"
- ✅ Plotëso formën:
  - ID: CIKLI-002
  - Koha: Zgjedh datë dhe orë në të ardhmen
  - Kapaciteti: 12000
  - Zona: ZONE-001
  - Ditët: Zgjedh MONDAY, WEDNESDAY, FRIDAY
- ✅ Krijoni ciklin
- ✅ Verifiko që shfaqet në listë me status "SCHEDULED"

**Hapi 4:** Aktivizo cikël
- ✅ Kliko "Aktivizo" në një cikël të planifikuar
- ✅ Verifiko që statusi ndryshon në "ACTIVE"
- ✅ Verifiko që kamioni duhet të jetë i caktuar (nëse kërkohet)

**Hapi 5:** Kompleto cikël
- ✅ Kliko "Kompleto" në një cikël aktiv
- ✅ Verifiko që statusi ndryshon në "COMPLETED"

**Hapi 6:** Anulo cikël
- ✅ Kliko "Anulo" në një cikël të planifikuar ose aktiv
- ✅ Verifiko që statusi ndryshon në "CANCELLED"

**Endpoints të testuara:**
- `GET /api/ciklet`
- `GET /api/ciklet/active`
- `POST /api/ciklet`
- `PUT /api/ciklet/{id}`
- `POST /api/ciklet/{id}/activate`
- `POST /api/ciklet/{id}/complete`
- `POST /api/ciklet/{id}/cancel`

---

### Skenari 9: Integrimi i Plotë - Flow i Plotë

**Hapi 1:** Krijo qytetar të ri
- ✅ Shko te Qytetarët
- ✅ Krijo qytetar: "Test User", Adresa: "Test Address"

**Hapi 2:** Krijo kamion të ri
- ✅ Shko te Kamionët
- ✅ Krijo kamion: "Test Kamion", Targat: "KS-TEST-01"

**Hapi 3:** Krijo cikël mbledhjeje
- ✅ Shko te Ciklet
- ✅ Krijo cikël për zonën ekzistuese
- ✅ Cakto kamionin e krijuar
- ✅ Aktivizo ciklin

**Hapi 4:** Përditëso kontejner në nivel kritik
- ✅ Shko te Dashboard
- ✅ Përditëso një kontejner në ≥90%
- ✅ Verifiko që njoftimi krijohet

**Hapi 5:** Gjenero rrugë optimale
- ✅ Shko te Rrugët
- ✅ Zgjedh zonën
- ✅ Gjenero rrugë me strategji "NEAREST_NEIGHBOR"
- ✅ Verifiko që rruga përfshin kontejnerin kritik

**Hapi 6:** Cakto kamionin në rrugë
- ✅ Shko te Kamionët
- ✅ Cakto kamionin e krijuar në rrugën e gjeneruar

**Hapi 7:** Gjenero raport
- ✅ Shko te Raporte
- ✅ Gjenero raport operacional
- ✅ Verifiko që raporti përfshin të gjitha aktivitetet

---

## 📊 Checklist i Demonstrimit

### Entitetet e Demonstrueshme:
- [x] Kontenierët - CRUD, Update Fill Level, Map View
- [x] Zonat - Statistics, CRUD
- [x] Rrugët - Route Optimization me Strategy Pattern
- [x] Raporte - Generate, View
- [x] Kamionët - CRUD, Assign Route, Release Route
- [x] Qytetarët - CRUD
- [x] Ciklet e Mbledhjes - CRUD, Activate, Complete, Cancel

### Funksionalitetet e Demonstrueshme:
- [x] Real-time monitoring
- [x] Automatic notifications
- [x] Route optimization
- [x] Report generation
- [x] Form validation
- [x] Error handling
- [x] Toast notifications
- [x] Map visualization

---

## 🎯 Pikat Kryesore për Demonstrim

1. **Të gjitha entitetet janë të aksesueshme nga UI**
2. **Të gjitha operacionet prodhojnë rezultat të dukshëm**
3. **Error handling dhe validation funksionojnë**
4. **Toast notifications sigurojnë feedback**
5. **Asnjë funksionalitet nuk mbetet i pa-demonstruar**

---

**Data:** 2024  
**Version:** 1.0.0  
**Status:** ✅ GATI PËR DEMONSTRIM

