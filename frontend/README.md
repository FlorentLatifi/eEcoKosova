# EcoKosova Frontend

Dashboard për monitorimin në kohë reale të kontejnerëve të mbeturinave.

## 🚀 Teknologjitë

- **React 19** me **TypeScript**
- **Vite** për build tool
- **Tailwind CSS** për styling
- **Axios** për API calls
- **Lucide React** për ikona

## 📦 Instalimi

```bash
npm install
```

## 🏃 Startimi i Projektit

```bash
# Development mode
npm run dev

# Build për production
npm run build

# Preview production build
npm run preview
```

Frontend do të startohet në: `http://localhost:3000`

## 🔌 Backend Connection

Backend duhet të jetë i startuar në `http://localhost:8080`

Nëse backend është në port tjetër, ndrysho `API_BASE_URL` në `src/services/api.ts`

## 📁 Struktura e Projektit

```
frontend/
├── src/
│   ├── components/
│   │   ├── Dashboard.tsx          # Dashboard kryesor
│   │   ├── StatisticsPanel.tsx    # Panel me statistika
│   │   ├── ContainerCard.tsx      # Card për çdo kontejner
│   │   ├── AlertList.tsx          # Lista e alarmeve kritike
│   │   └── ContainerDetails.tsx   # Modal për detaje
│   ├── hooks/
│   │   └── useContainers.ts       # Custom hook për containers
│   ├── services/
│   │   └── api.ts                 # API service layer
│   ├── App.tsx
│   ├── main.tsx
│   └── index.css
├── tailwind.config.js
├── postcss.config.js
├── vite.config.ts
└── package.json
```

## 🎨 Features

- ✅ Real-time monitoring - Auto-refresh çdo 30 sekonda
- ✅ Statistics cards - Overview i shpejtë
- ✅ Color-coded status - Kuptim vizual
- ✅ Filter tabs - Filtrimi i kontejnerëve
- ✅ Critical alerts - Njoftime të prioritizuara
- ✅ Container cards - Informacion i kompletuar
- ✅ Details modal - Shfaqje e detajeve + përditësim
- ✅ Responsive design - Funksionon në çdo pajisje
- ✅ Beautiful UI - Modern, clean, professional

## 🔄 API Endpoints

Projekti përdor këto endpoints:

- `GET /api/monitoring/containers` - Merr të gjitha kontejnerët
- `GET /api/monitoring/containers/critical` - Merr kontejnerët kritikë
- `PUT /api/monitoring/containers/{id}/fill-level` - Përditëson nivelin
- `GET /api/zones/statistics` - Merr statistikat e zonave

## 🎯 Status Colors

- 🟢 **Green** (#10B981) - Normal/Operational (< 70%)
- 🟡 **Amber** (#F59E0B) - Warning (70-89%)
- 🔴 **Red** (#EF4444) - Critical (90%+)
- ⚫ **Gray** (#6B7280) - Inactive/Offline

## 📝 Notes

Projekti është i konfiguruar për TypeScript. Të gjitha komponentët dhe hooks janë type-safe.
