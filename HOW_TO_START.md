# 🚀 Si ta Startosh Projektin EcoKosova

## 📋 Komanda për Startim

### 🔧 BACKEND (Spring Boot)

**Hapi 1:** Hap terminal dhe navigo te backend:

```powershell
cd eEcoKosova\backend
```

**Hapi 2:** Starto backend:

```powershell
mvn spring-boot:run
```

**✅ Backend është gati kur shikon:**
```
Started EcoKosovaApplication in X.XXX seconds
```

**📍 Backend do të jetë në:** `http://localhost:8080`

---

### 🎨 FRONTEND (React + Vite)

**Hapi 1:** Hap NJË TERMINAL TË RE dhe navigo te frontend:

```powershell
cd eEcoKosova\frontend
```

**Hapi 2:** Starto frontend:

```powershell
npm run dev
```

**✅ Frontend është gati kur shikon:**
```
➜  Local:   http://localhost:3000/
```

**📍 Frontend do të jetë në:** `http://localhost:3000`

---

## ⚠️ RENDI I RËNDËSISHËM!

1. ✅ **Fillimisht starto BACKEND** (Terminal 1)
2. ✅ **Prit derisa backend-i të startojë plotësisht**
3. ✅ **Pastaj starto FRONTEND** (Terminal 2)
4. ✅ **Hap browser në** `http://localhost:3000`

---

## 🛑 Si ta Ndalosh

**Për Backend:** Shtyp `Ctrl + C` në terminal
**Për Frontend:** Shtyp `Ctrl + C` në terminal

---

## 📝 Quick Reference

### Backend:
```powershell
cd eEcoKosova\backend
mvn spring-boot:run
```

### Frontend:
```powershell
cd eEcoKosova\frontend
npm run dev
```

---

## 🔍 Si ta Testosh

1. Hap browser: `http://localhost:3000`
2. Duhet të shohësh **EcoKosova Dashboard**
3. Duhet të shfaqen kontejnerët

---

## ⚠️ Nëse ka Probleme

### Backend nuk starton - SQL Server Connection Error:

Nëse shihni gabimin: `The TCP/IP connection to the host localhost, port 1433 has failed`

**Zgjidhje 1: Përdor Development Profile (Recomanduar)**

Përdorni H2 in-memory database që nuk kërkon SQL Server:

```powershell
cd eEcoKosova\backend
mvn spring-boot:run "-Dspring-boot.run.profiles=dev"
```

**Ose përdorni environment variable (më e lehtë në PowerShell):**
```powershell
cd eEcoKosova\backend
$env:SPRING_PROFILES_ACTIVE="dev"
mvn spring-boot:run
```

Ky profile përdor H2 database dhe nuk kërkon SQL Server të ekzekutuar.

**Zgjidhje 2: Starto SQL Server me Docker**

```powershell
# Në root të projektit
cd eEcoKosova
docker-compose up -d mssql

# Prisni 10-20 sekonda, pastaj startoni backend
cd backend
mvn spring-boot:run
```

Për më shumë detaje, shihni `README_DATABASE_SETUP.md`.

### Problema të tjera:

### Backend nuk starton:
- Sigurohu që porti 8080 nuk është i zënë
- Kontrollo: `java -version` (duhet të jetë Java 17+)
- Kontrollo: `mvn -version`

### Frontend nuk starton:
- Sigurohu që backend-i është startuar më parë
- Kontrollo: `node -v`
- Nëse ka gabime, provo: `npm install` përsëri

---

**Gëzuar coding! 🎉**

