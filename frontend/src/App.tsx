import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import { AuthProvider } from "./context/AuthContext";
import { NotificationProvider } from "./context/NotificationContext";
import { ToastProvider } from "./context/ToastContext";
import Layout from "./components/Layout";
import Dashboard from "./components/Dashboard";
import ContainersPage from "./pages/ContainersPage";
import ZonesPage from "./pages/ZonesPage";
import ReportsPage from "./pages/ReportsPage";
import RoutesPage from "./pages/RoutesPage";
import SettingsPage from "./pages/SettingsPage";
import KamioniPage from "./pages/KamioniPage";
import QytetariPage from "./pages/QytetariPage";
import CikliMbledhjesPage from "./pages/CikliMbledhjesPage";
import "./index.css";

function App() {
  return (
    <AuthProvider>
      <NotificationProvider>
        <ToastProvider>
          <BrowserRouter>
            <Routes>
              <Route path="/" element={<Layout />}>
                <Route index element={<Navigate to="/dashboard" replace />} />
                <Route path="dashboard" element={<Dashboard />} />
              <Route path="containers" element={<ContainersPage />} />
              <Route path="zones" element={<ZonesPage />} />
              <Route path="routes" element={<RoutesPage />} />
              <Route path="kamionet" element={<KamioniPage />} />
              <Route path="qytetaret" element={<QytetariPage />} />
              <Route path="ciklet" element={<CikliMbledhjesPage />} />
              <Route path="reports" element={<ReportsPage />} />
              <Route path="settings" element={<SettingsPage />} />
              </Route>
            </Routes>
          </BrowserRouter>
        </ToastProvider>
      </NotificationProvider>
    </AuthProvider>
  );
}

export default App;
