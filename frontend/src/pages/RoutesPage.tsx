import React, { useState, useEffect } from "react";
import {
  Route,
  MapPin,
  Clock,
  Navigation,
  Activity,
  TrendingUp,
  Map,
  RefreshCw,
  AlertCircle,
} from "lucide-react";
import { getAllRoutes, getRouteForZone, getZoneStatistics, type Route as RouteType, type ZoneStatistics } from "../services/api";

const RoutesPage: React.FC = () => {
  const [routes, setRoutes] = useState<RouteType[]>([]);
  const [zones, setZones] = useState<ZoneStatistics[]>([]);
  const [loading, setLoading] = useState(true);
  const [selectedRoute, setSelectedRoute] = useState<RouteType | null>(null);
  const [selectedZoneId, setSelectedZoneId] = useState<string>("");
  const [strategy, setStrategy] = useState<string>("OPTIMAL");

  useEffect(() => {
    fetchData();
  }, []);

  const fetchData = async () => {
    try {
      setLoading(true);
      const [routesData, zonesData] = await Promise.all([
        getAllRoutes(),
        getZoneStatistics(),
      ]);
      setRoutes(routesData);
      setZones(zonesData);
    } catch (error) {
      console.error("Error fetching routes:", error);
    } finally {
      setLoading(false);
    }
  };

  const handleZoneChange = async (zoneId: string) => {
    if (!zoneId) {
      setSelectedRoute(null);
      return;
    }

    try {
      setLoading(true);
      const route = await getRouteForZone(zoneId, 42.6629, 21.1655, strategy);
      setSelectedRoute(route);
    } catch (error) {
      console.error("Error fetching route for zone:", error);
    } finally {
      setLoading(false);
    }
  };

  const formatTime = (minutes: number): string => {
    const hours = Math.floor(minutes / 60);
    const mins = Math.floor(minutes % 60);
    if (hours > 0) {
      return `${hours}h ${mins}min`;
    }
    return `${mins}min`;
  };

  if (loading && routes.length === 0) {
    return (
      <div className="flex items-center justify-center h-96">
        <div className="text-center">
          <Activity className="w-12 h-12 text-eco-blue animate-spin mx-auto mb-4" />
          <p className="text-gray-600">Duke ngarkuar rrugët...</p>
        </div>
      </div>
    );
  }

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="bg-white rounded-lg shadow-md p-6">
        <div className="flex items-center justify-between mb-4">
          <div>
            <h2 className="text-2xl font-bold text-gray-900">Optimizimi i Rrugëve</h2>
            <p className="text-gray-600 mt-1">
              Rrugët e optimizuara për mbledhjen e mbeturinave
            </p>
          </div>
          <button
            onClick={fetchData}
            className="flex items-center space-x-2 bg-eco-blue text-white px-4 py-2 rounded-lg hover:bg-blue-600 transition-colors"
          >
            <RefreshCw className="w-5 h-5" />
            <span>Rifresko</span>
          </button>
        </div>

        {/* Zone Selector */}
        <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-2">
              Zgjidh Zonë
            </label>
            <select
              value={selectedZoneId}
              onChange={(e) => {
                setSelectedZoneId(e.target.value);
                handleZoneChange(e.target.value);
              }}
              className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-eco-blue focus:border-transparent"
            >
              <option value="">Të gjitha zonat</option>
              {zones.map((zone) => (
                <option key={zone.zoneId} value={zone.zoneId}>
                  {zone.zoneName} ({zone.zoneId})
                </option>
              ))}
            </select>
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-2">
              Strategjia
            </label>
            <select
              value={strategy}
              onChange={(e) => {
                setStrategy(e.target.value);
                if (selectedZoneId) {
                  handleZoneChange(selectedZoneId);
                }
              }}
              className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-eco-blue focus:border-transparent"
            >
              <option value="OPTIMAL">Optimale (Nearest Neighbor)</option>
              <option value="PRIORITY">Sipas Prioritetit</option>
            </select>
          </div>
        </div>
      </div>

      {/* Statistics Cards */}
      <div className="grid grid-cols-1 md:grid-cols-4 gap-4">
        <div className="bg-white rounded-lg shadow p-6">
          <div className="flex items-center justify-between">
            <div>
              <p className="text-sm text-gray-600">Total Rrugë</p>
              <p className="text-3xl font-bold text-gray-900 mt-2">
                {routes.length}
              </p>
            </div>
            <div className="bg-eco-blue bg-opacity-10 rounded-full p-4">
              <Route className="w-8 h-8 text-eco-blue" />
            </div>
          </div>
        </div>

        <div className="bg-white rounded-lg shadow p-6">
          <div className="flex items-center justify-between">
            <div>
              <p className="text-sm text-gray-600">Kontejnerë Total</p>
              <p className="text-3xl font-bold text-gray-900 mt-2">
                {routes.reduce((sum, r) => sum + r.containerCount, 0)}
              </p>
            </div>
            <div className="bg-green-100 rounded-full p-4">
              <MapPin className="w-8 h-8 text-green-600" />
            </div>
          </div>
        </div>

        <div className="bg-white rounded-lg shadow p-6">
          <div className="flex items-center justify-between">
            <div>
              <p className="text-sm text-gray-600">Distancë Total</p>
              <p className="text-3xl font-bold text-gray-900 mt-2">
                {routes.reduce((sum, r) => sum + r.totalDistanceKm, 0).toFixed(1)} km
              </p>
            </div>
            <div className="bg-amber-100 rounded-full p-4">
              <Navigation className="w-8 h-8 text-amber-600" />
            </div>
          </div>
        </div>

        <div className="bg-white rounded-lg shadow p-6">
          <div className="flex items-center justify-between">
            <div>
              <p className="text-sm text-gray-600">Kohë Mesatare</p>
              <p className="text-3xl font-bold text-gray-900 mt-2">
                {routes.length > 0
                  ? formatTime(
                      routes.reduce((sum, r) => sum + r.estimatedTimeMinutes, 0) /
                        routes.length
                    )
                  : "0min"}
              </p>
            </div>
            <div className="bg-red-100 rounded-full p-4">
              <Clock className="w-8 h-8 text-red-600" />
            </div>
          </div>
        </div>
      </div>

      {/* Routes List */}
      {(selectedRoute ? [selectedRoute] : routes).length === 0 ? (
        <div className="bg-white rounded-lg shadow-md p-12 text-center">
          <AlertCircle className="w-16 h-16 text-gray-400 mx-auto mb-4" />
          <h3 className="text-xl font-semibold text-gray-700 mb-2">
            Nuk ka rrugë
          </h3>
          <p className="text-gray-500">
            Nuk u gjetën rrugë për mbledhjen e mbeturinave.
          </p>
        </div>
      ) : (
        <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
          {(selectedRoute ? [selectedRoute] : routes).map((route) => (
            <div
              key={route.zoneId}
              className="bg-white rounded-lg shadow-md hover:shadow-lg transition-all"
            >
              <div className="p-6">
                {/* Header */}
                <div className="flex items-start justify-between mb-4">
                  <div>
                    <h3 className="text-lg font-bold text-gray-900">
                      {route.zoneName}
                    </h3>
                    <p className="text-sm text-gray-500">{route.zoneId}</p>
                  </div>
                  <span className="px-3 py-1 text-xs font-semibold bg-blue-100 text-blue-800 rounded-full">
                    {route.routeType === "OPTIMAL" ? "Optimale" : "Sipas Prioritetit"}
                  </span>
                </div>

                {/* Statistics */}
                <div className="grid grid-cols-2 gap-4 mb-4">
                  <div className="bg-gray-50 rounded-lg p-3">
                    <div className="flex items-center space-x-2">
                      <MapPin className="w-5 h-5 text-gray-600" />
                      <div>
                        <p className="text-xs text-gray-500">Kontejnerë</p>
                        <p className="text-lg font-bold text-gray-900">
                          {route.containerCount}
                        </p>
                      </div>
                    </div>
                  </div>

                  <div className="bg-gray-50 rounded-lg p-3">
                    <div className="flex items-center space-x-2">
                      <Navigation className="w-5 h-5 text-gray-600" />
                      <div>
                        <p className="text-xs text-gray-500">Distancë</p>
                        <p className="text-lg font-bold text-gray-900">
                          {route.totalDistanceKm.toFixed(2)} km
                        </p>
                      </div>
                    </div>
                  </div>

                  <div className="bg-gray-50 rounded-lg p-3">
                    <div className="flex items-center space-x-2">
                      <Clock className="w-5 h-5 text-gray-600" />
                      <div>
                        <p className="text-xs text-gray-500">Kohë</p>
                        <p className="text-lg font-bold text-gray-900">
                          {formatTime(route.estimatedTimeMinutes)}
                        </p>
                      </div>
                    </div>
                  </div>

                  <div className="bg-gray-50 rounded-lg p-3">
                    <div className="flex items-center space-x-2">
                      <TrendingUp className="w-5 h-5 text-gray-600" />
                      <div>
                        <p className="text-xs text-gray-500">Kapacitet</p>
                        <p className="text-lg font-bold text-gray-900">
                          {route.totalCapacityLiters}L
                        </p>
                      </div>
                    </div>
                  </div>
                </div>

                {/* Containers List */}
                {route.containers.length > 0 && (
                  <div className="mt-4">
                    <h4 className="text-sm font-semibold text-gray-700 mb-2">
                      Kontejnerët në rrugë ({route.containers.length})
                    </h4>
                    <div className="max-h-40 overflow-y-auto space-y-2">
                      {route.containers.map((container, index) => (
                        <div
                          key={container.id}
                          className="flex items-center justify-between bg-gray-50 rounded p-2"
                        >
                          <div className="flex items-center space-x-2">
                            <span className="text-xs font-semibold text-gray-500">
                              #{index + 1}
                            </span>
                            <span className="text-sm font-medium text-gray-900">
                              {container.id}
                            </span>
                            <span className="text-xs text-gray-500">
                              ({container.fillLevel}%)
                            </span>
                          </div>
                          <span
                            className={`text-xs px-2 py-1 rounded ${
                              container.fillLevel >= 90
                                ? "bg-red-100 text-red-800"
                                : container.fillLevel >= 70
                                ? "bg-amber-100 text-amber-800"
                                : "bg-green-100 text-green-800"
                            }`}
                          >
                            {container.status}
                          </span>
                        </div>
                      ))}
                    </div>
                  </div>
                )}

                {/* Action Button */}
                <button
                  onClick={() => setSelectedRoute(route)}
                  className="mt-4 w-full bg-eco-blue text-white py-2 rounded-lg hover:bg-blue-600 transition-colors"
                >
                  Shiko Detaje
                </button>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
};

export default RoutesPage;
