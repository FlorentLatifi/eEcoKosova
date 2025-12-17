# 🚀 SI TA STARTOJ - UDHËZIM I THJESHTË

## ⚡ STARTIMI (3 HAPA)

**Hapi 1:** Hap PowerShell dhe shko në backend folder:
```powershell
cd C:\Users\HP\Desktop\ecokosova\eEcoKosova\backend
```

**Hapi 2:** Vendos profile dev:
```powershell
$env:SPRING_PROFILES_ACTIVE="dev"
```

**Hapi 3:** Starto aplikacionin:
```powershell
mvn spring-boot:run
```

**GOTOVA!** Aplikacioni do të startojë në http://localhost:8080

---

## 📍 LIDHJET E RËNDËSISHME:
- **API:** http://localhost:8080/api
- **Swagger UI:** http://localhost:8080/swagger-ui/index.html
- **H2 Console:** http://localhost:8080/h2-console
  - JDBC URL: `jdbc:h2:mem:ecokosova`
  - Username: `sa`
  - Password: (lë bosh)

---

## 🛑 SI TA NDALOSH:
Shtyp `Ctrl + C` në terminal

---

## ⚠️ NËSE KA PROBLEME:

**Nëse shfaqet error për SQL Server:**
- Sigurohu që ke ekzekutuar: `$env:SPRING_PROFILES_ACTIVE="dev"` PARA `mvn spring-boot:run`

**Nëse porti 8080 është i zënë:**
- Mbyll aplikacionin tjetër që përdor portin 8080

**Nëse ka probleme me dependencies:**
- Ekzekuto: `mvn clean` pastaj `mvn spring-boot:run` përsëri
