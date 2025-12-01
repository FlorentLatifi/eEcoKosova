import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import Layout from "./components/Layout";
import Dashboard from "./components/Dashboard";
import ContainersPage from "./pages/ContainersPage";
import ZonesPage from "./pages/ZonesPage";
import ReportsPage from "./pages/ReportsPage";
import RoutesPage from "./pages/RoutesPage";
import "./index.css";

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Layout />}>
          <Route index element={<Navigate to="/dashboard" replace />} />
          <Route path="dashboard" element={<Dashboard />} />
          <Route path="containers" element={<ContainersPage />} />
          <Route path="zones" element={<ZonesPage />} />
          <Route path="routes" element={<RoutesPage />} />
          <Route path="reports" element={<ReportsPage />} />
        </Route>
      </Routes>
    </BrowserRouter>
  );
}

export default App;
