# 🚀 Si ta Startosh Projektin EcoKosova

## 📋 Kërkesat Para Startimit

Para se të fillosh, sigurohu që ke instaluar:
- ✅ **Java 17** ose më të lartë
- ✅ **Maven** (për backend)
- ✅ **Node.js** dhe **npm** (për frontend)

---

## 🔧 HAPI 1: Starto Backend (Spring Boot)

### Terminal 1 - Backend

```powershell
# Navigo te backend folder
cd eEcoKosova\backend

# Starto Spring Boot aplikacionin
mvn spring-boot:run
```

**Ose nëse ke IDE (IntelliJ IDEA, Eclipse):**
1. Hap projektin backend në IDE
2. Gjej klasën `EcoKosovaApplication.java`
3. Kliko "Run" ose shtyp `Shift + F10`

### ✅ Backend është gati kur shikon:
```
Started EcoKosovaApplication in X.XXX seconds
```

**Backend do të jetë në:** `http://localhost:8080`

**API Endpoints:**
- `http://localhost:8080/api/monitoring/containers`
- `http://localhost:8080/api/zones/statistics`

---

## 🎨 HAPI 2: Starto Frontend (React + Vite)

### Terminal 2 - Frontend (HAP NË TERMINAL TË RE!)

```powershell
# Navigo te frontend folder
cd eEcoKosova\frontend

# Nëse është hera e parë, instaloni dependencies (vetëm një herë)
npm install

# Starto development server
npm run dev
```

### ✅ Frontend është gati kur shikon:
```
VITE v7.x.x  ready in XXX ms

➜  Local:   http://localhost:3000/
➜  Network: use --host to expose
```

**Frontend do të jetë në:** `http://localhost:3000`

---

## 📝 Udhëzime të Shkurtra (Quick Start)

### Për Backend:
```powershell
cd eEcoKosova\backend
mvn spring-boot:run
```

### Për Frontend:
```powershell
cd eEcoKosova\frontend
npm run dev
```

---

## 🔍 Si ta Testosh

1. **Hap browser:** `http://localhost:3000`
2. Duhet të shohësh **EcoKosova Dashboard**
3. Duhet të shfaqen kontejnerët (nëse ka të dhëna në backend)

---

## ⚠️ Problemet e Zakonshme

### Backend nuk starton:
- ✅ Sigurohu që porti 8080 nuk është i zënë
- ✅ Kontrollo që Java 17+ është instaluar: `java -version`
- ✅ Kontrollo që Maven është instaluar: `mvn -version`

### Frontend nuk starton:
- ✅ Sigurohu që backend-i është startuar më parë
- ✅ Kontrollo që Node.js është instaluar: `node -v`
- ✅ Nëse ka gabime, provo: `npm install` përsëri

### Frontend nuk mund të lidhet me backend:
- ✅ Sigurohu që backend-i po punon në `http://localhost:8080`
- ✅ Kontrollo nëse backend-i kthen përgjigje: `curl http://localhost:8080/api/monitoring/containers`

---

## 🛑 Si ta Ndalosh Projektin

### Për Backend:
- Në terminal, shtyp: `Ctrl + C`

### Për Frontend:
- Në terminal, shtyp: `Ctrl + C`

---

## 📊 Struktura e Projektit

```
EcoKosova/
├── eEcoKosova/
│   ├── backend/          ← Spring Boot (Port 8080)
│   │   ├── pom.xml
│   │   └── src/
│   └── frontend/         ← React + Vite (Port 3000)
│       ├── package.json
│       └── src/
└── START_GUIDE.md        ← Ky file
```

---

## 🎯 Rendi i Startimit (IMPORTANT!)

**GJITHMONË fillo me BACKEND, pastaj FRONTEND:**

1. ✅ Starto Backend (Terminal 1)
2. ✅ Prit derisa backend-i të fillojë plotësisht
3. ✅ Starto Frontend (Terminal 2)
4. ✅ Hap browser në `http://localhost:3000`

---

## 💡 Tips

- **Për build production:**
  ```powershell
  # Backend
  cd eEcoKosova\backend
  mvn clean package
  
  # Frontend
  cd eEcoKosova\frontend
  npm run build
  ```

- **Për të parë log-at e backend:**
  - Log-at shfaqen direkt në terminal ku ke startuar backend-in

- **Për të parë log-at e frontend:**
  - Log-at shfaqen në browser console (F12)

---

## ✅ Checklist Para Startimit

- [ ] Java 17+ instaluar
- [ ] Maven instaluar
- [ ] Node.js instaluar
- [ ] Backend dependencies të instaluara (Maven e bën automatikisht)
- [ ] Frontend dependencies të instaluara (`npm install`)

---

**Gëzuar coding! 🚀**

