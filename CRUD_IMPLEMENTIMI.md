# Implementimi i CRUD Operations - EcoKosova

## ✅ BACKEND - PLOTËSUAR

### 1. Commands për Kontejnerët ✅
- `CreateContainerCommand.java` - Krijon kontejner të ri
- `UpdateContainerCommand.java` - Përditëson kontejner ekzistues
- `DeleteContainerCommand.java` - Fshin kontejner

### 2. Handlers për Kontejnerët ✅
- `CreateContainerHandler.java` - Handlon krijimin e kontejnerit
- `UpdateContainerHandler.java` - Handlon përditësimin e kontejnerit
- `DeleteContainerHandler.java` - Handlon fshirjen e kontejnerit

### 3. Commands për Zonat ✅
- `CreateZoneCommand.java` - Ekziston tashmë
- `UpdateZoneCommand.java` - Përditëson zonë ekzistuese
- `DeleteZoneCommand.java` - Fshin zonë

### 4. Handlers për Zonat ✅
- `CreateZoneHandler.java` - Ekziston tashmë
- `UpdateZoneHandler.java` - Handlon përditësimin e zonës
- `DeleteZoneHandler.java` - Handlon fshirjen e zonës (me kontroll për kontejnerë)

### 5. Controllers ✅
- `ContainerManagementController.java` - Endpoints për CRUD të kontejnerëve:
  - `POST /api/containers` - Krijon kontejner të ri
  - `PUT /api/containers/{id}` - Përditëson kontejner
  - `DELETE /api/containers/{id}` - Fshin kontejner

- `ZoneController.java` - Endpoints për CRUD të zonave:
  - `GET /api/zones` - Merr të gjitha zonat
  - `POST /api/zones` - Krijon zonë të re
  - `PUT /api/zones/{id}` - Përditëson zonë
  - `DELETE /api/zones/{id}` - Fshin zonë
  - `GET /api/zones/statistics` - Statistika (ekzistonte tashmë)

### 6. HandlerConfig ✅
- Të gjitha handlers janë konfiguruar si Spring Beans

---

## ⏳ FRONTEND - DUHET TË KRIJOHET

### 1. API Functions në `api.ts`
Duhet të shtohen:
```typescript
- createContainer(container: CreateContainerRequest): Promise<Container>
- updateContainer(id: string, container: UpdateContainerRequest): Promise<Container>
- deleteContainer(id: string): Promise<void>
- getAllZones(): Promise<Zone[]>
- createZone(zone: CreateZoneRequest): Promise<Zone>
- updateZone(id: string, zone: UpdateZoneRequest): Promise<Zone>
- deleteZone(id: string): Promise<void>
```

### 2. Forms/Modals për Kontejnerët
- `CreateContainerModal.tsx` - Form për krijimin e kontejnerit
- `EditContainerModal.tsx` - Form për përditësimin e kontejnerit
- `DeleteContainerModal.tsx` - Konfirmim për fshirje

### 3. Forms/Modals për Zonat
- `CreateZoneModal.tsx` - Form për krijimin e zone
- `EditZoneModal.tsx` - Form për përditësimin e zone
- `DeleteZoneModal.tsx` - Konfirmim për fshirje

### 4. Butonat Edit/Delete
Duhet të shtohen në:
- `ContainersPage.tsx` - Butonat Edit dhe Delete për çdo kontejner
- `ZonesPage.tsx` - Butonat Edit dhe Delete për çdo zonë
- `Dashboard.tsx` - Butonat Edit në ContainerCard (optional)

### 5. Features Shtesë
- Search dhe Filter të përmirësuara
- Export CSV për kontejnerët dhe zonat
- Validation në forms
- Error handling i plotë
- Success notifications

---

## 📋 PLANI I IMPLEMENTIMIT

### Hapi 1: API Functions (api.ts)
1. Shto interfaces për Create/Update requests
2. Shto functions për kontejnerët
3. Shto functions për zonat

### Hapi 2: Create Container Modal
1. Krijo modal component
2. Shto form me të gjitha fields
3. Integro me API
4. Shto validation

### Hapi 3: Edit Container Modal
1. Krijo modal component
2. Load të dhënat ekzistuese
3. Shto form me fields
4. Integro me API

### Hapi 4: Delete Container
1. Shto buton delete në ContainersPage
2. Shto konfirmim modal
3. Integro me API

### Hapi 5: Zones CRUD (i njëjti proces)
1. Create Zone Modal
2. Edit Zone Modal
3. Delete Zone

### Hapi 6: Features Shtesë
1. Export CSV
2. Search/Filter të përmirësuara
3. Validation
4. Notifications

---

## 🔧 ENDPOINTS TË DISPONUESHME

### Containers
- `GET /api/monitoring/containers` - Merr të gjitha kontejnerët
- `GET /api/monitoring/containers/critical` - Merr kontejnerët kritikë
- `PUT /api/monitoring/containers/{id}/fill-level` - Përditëson fill level
- `POST /api/containers` - Krijon kontejner të ri ⭐ NOV
- `PUT /api/containers/{id}` - Përditëson kontejner ⭐ NOV
- `DELETE /api/containers/{id}` - Fshin kontejner ⭐ NOV

### Zones
- `GET /api/zones/statistics` - Merr statistika
- `GET /api/zones` - Merr të gjitha zonat ⭐ NOV
- `POST /api/zones` - Krijon zonë të re ⭐ NOV
- `PUT /api/zones/{id}` - Përditëson zonë ⭐ NOV
- `DELETE /api/zones/{id}` - Fshin zonë ⭐ NOV

⭐ NOV = Endpoint i ri

---

## 📝 SHËNIME

- Të gjitha backend endpoints janë gati dhe funksionale
- Frontend është i sinkronizuar me këto endpoints (naming + schema)
- Validation ekziston si në backend (Jakarta Validation) ashtu edhe në frontend (react-hook-form/zod)
- Global error handler kthen strukturë uniforme (`status`, `code`, `message`, `errors[]`)
- Success/error notifications në frontend përmirësojnë UX

---

## 🔄 Mapping Backend ↔ Frontend (fushat kryesore)

### Containers

| Backend DTO / Fusha                 | Frontend Model (`Container` nga `api.ts`) | Shënime                      |
|-------------------------------------|-------------------------------------------|------------------------------|
| `ContainerResponseDTO.id`           | `id`                                      | string                       |
| `ContainerResponseDTO.zoneId`       | `zoneId`                                  | string                       |
| `ContainerResponseDTO.type`         | `type`                                    | string (display name)        |
| `ContainerResponseDTO.fillLevel`    | `fillLevel`                               | number (0-100)               |
| `ContainerResponseDTO.status`       | `status`                                  | string (display name)        |
| `ContainerResponseDTO.capacity`     | `capacity`                                | number (liters)              |
| `ContainerResponseDTO.operational`  | `operational`                             | boolean                      |
| `ContainerResponseDTO.latitude`     | `latitude`                                | number                       |
| `ContainerResponseDTO.longitude`    | `longitude`                               | number                       |
| `ContainerResponseDTO.address`      | `address`                                 | string (full address)        |
| `ContainerResponseDTO.needsCollection` | `needsCollection`                       | boolean                      |

### Zones (lista e zonave)

| Backend DTO / Fusha          | Frontend Model (`Zone`)          | Shënime                      |
|------------------------------|----------------------------------|------------------------------|
| `ZoneDTO.id`                 | `id`                             | string                       |
| `ZoneDTO.name`               | `name`                           | string                       |
| `ZoneDTO.criticalThreshold`  | `criticalThreshold`              | number                       |
| `ZoneDTO.containerIds`       | `containerIds`                   | string[]                     |
| `ZoneDTO.status`             | `status`                         | string (display name)        |
| `ZoneDTO.centerPoint.latitude`  | `centerPoint.latitude`        | number                       |
| `ZoneDTO.centerPoint.longitude` | `centerPoint.longitude`       | number                       |

### Zone Statistics

| Backend DTO / Fusha                | Frontend Model (`ZoneStatistics`) | Shënime                      |
|------------------------------------|-----------------------------------|------------------------------|
| `ZoneStatisticsDTO.zoneId`        | `zoneId`                          | string                       |
| `ZoneStatisticsDTO.zoneName`      | `zoneName`                        | string                       |
| `ZoneStatisticsDTO.totalContainers` | `totalContainers`               | number                       |
| `ZoneStatisticsDTO.criticalContainers` | `criticalContainers`         | number                       |
| `ZoneStatisticsDTO.operationalContainers` | `operationalContainers`   | number                       |
| `ZoneStatisticsDTO.averageFillLevel` | `averageFillLevel`            | number (0-100)               |
| `ZoneStatisticsDTO.status`        | `status`                          | string (display name)        |

### Qytetarët (Users)

| Backend DTO / Fusha       | Frontend Model (`Qytetari`) | Shënime                            |
|---------------------------|-----------------------------|------------------------------------|
| `QytetariDTO.id`          | `id`                        | string                             |
| `QytetariDTO.name`        | `name`                      | string                             |
| `QytetariDTO.address`     | `address`                   | string (mund të jetë `null`)       |
| `QytetariDTO.createdAt`   | `createdAt`                 | string ISO-8601 (Instant)          |
| `QytetariDTO.lastUpdated` | `lastUpdated`               | string ISO-8601 (Instant)          |

### Reports

| Backend DTO / Fusha        | Frontend Model (`Report`) | Shënime                             |
|----------------------------|---------------------------|-------------------------------------|
| `ReportDTO.id`             | `id`                      | string                             |
| `ReportDTO.title`          | `title`                   | string                             |
| `ReportDTO.description`    | `description`             | string                             |
| `ReportDTO.type`           | `type`                    | string (`GENERAL`, `CRITICAL`, ...) |
| `ReportDTO.generatedAt`    | `generatedAt`             | string ISO-8601                     |
| `ReportDTO.data`           | `data`                    | `any` / `Map<String, Object>`       |

