import React, { useState, useEffect } from "react";
import {
  Map,
  MapPin,
  Activity,
  AlertTriangle,
  CheckCircle,
  X,
} from "lucide-react";
import { getZoneStatistics, type ZoneStatistics } from "../services/api";

const ZonesPage: React.FC = () => {
  const [zones, setZones] = useState<ZoneStatistics[]>([]);
  const [loading, setLoading] = useState(true);
  const [selectedZone, setSelectedZone] = useState<ZoneStatistics | null>(null);

  useEffect(() => {
    fetchZones();
  }, []);

  const fetchZones = async () => {
    try {
      setLoading(true);
      const data = await getZoneStatistics();
      setZones(data);
    } catch (error) {
      console.error("Error fetching zones:", error);
    } finally {
      setLoading(false);
    }
  };

  const getStatusColor = (status: string) => {
    if (status === "Kritike") return "bg-red-100 text-red-800 border-red-300";
    if (status === "Aktive")
      return "bg-green-100 text-green-800 border-green-300";
    if (status === "Në Mirëmbajtje")
      return "bg-amber-100 text-amber-800 border-amber-300";
    return "bg-gray-100 text-gray-800 border-gray-300";
  };

  const getStatusBorderColor = (status: string) => {
    if (status === "Kritike") return "#EF4444";
    if (status === "Aktive") return "#10B981";
    if (status === "Në Mirëmbajtje") return "#F59E0B";
    return "#6B7280";
  };

  if (loading) {
    return (
      <div className="flex items-center justify-center h-96">
        <div className="text-center">
          <Activity className="w-12 h-12 text-eco-blue animate-spin mx-auto mb-4" />
          <p className="text-gray-600">Duke ngarkuar zonat...</p>
        </div>
      </div>
    );
  }

  return (
    <div className="space-y-6">
      {/* Summary Cards */}
      <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
        <div className="bg-white rounded-lg shadow p-6">
          <div className="flex items-center justify-between">
            <div>
              <p className="text-sm text-gray-600">Total Zona</p>
              <p className="text-3xl font-bold text-gray-900 mt-2">
                {zones.length}
              </p>
            </div>
            <div className="bg-eco-blue bg-opacity-10 rounded-full p-4">
              <Map className="w-8 h-8 text-eco-blue" />
            </div>
          </div>
        </div>

        <div className="bg-white rounded-lg shadow p-6">
          <div className="flex items-center justify-between">
            <div>
              <p className="text-sm text-gray-600">Zona Kritike</p>
              <p className="text-3xl font-bold text-red-600 mt-2">
                {zones.filter((z) => z.status === "Kritike").length}
              </p>
            </div>
            <div className="bg-red-100 rounded-full p-4">
              <AlertTriangle className="w-8 h-8 text-red-600" />
            </div>
          </div>
        </div>

        <div className="bg-white rounded-lg shadow p-6">
          <div className="flex items-center justify-between">
            <div>
              <p className="text-sm text-gray-600">Total Kontejnerë</p>
              <p className="text-3xl font-bold text-gray-900 mt-2">
                {zones.reduce((sum, z) => sum + z.totalContainers, 0)}
              </p>
            </div>
            <div className="bg-green-100 rounded-full p-4">
              <CheckCircle className="w-8 h-8 text-green-600" />
            </div>
          </div>
        </div>
      </div>

      {/* Zones Grid */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        {zones.map((zone) => (
          <div
            key={zone.zoneId}
            onClick={() => setSelectedZone(zone)}
            className="bg-white rounded-lg shadow-md hover:shadow-lg transition-all cursor-pointer border-l-4"
            style={{
              borderLeftColor: getStatusBorderColor(zone.status),
            }}
          >
            <div className="p-6">
              {/* Header */}
              <div className="flex items-start justify-between mb-4">
                <div className="flex-1">
                  <h3 className="text-lg font-bold text-gray-900">
                    {zone.zoneName}
                  </h3>
                  <p className="text-sm text-gray-500">{zone.zoneId}</p>
                </div>
                <span
                  className={`px-3 py-1 text-xs font-semibold rounded-full border ${getStatusColor(
                    zone.status
                  )}`}
                >
                  {zone.status}
                </span>
              </div>

              {/* Stats */}
              <div className="space-y-3">
                <div className="flex items-center justify-between">
                  <span className="text-sm text-gray-600">
                    Total Kontejnerë
                  </span>
                  <span className="text-sm font-semibold text-gray-900">
                    {zone.totalContainers}
                  </span>
                </div>

                <div className="flex items-center justify-between">
                  <span className="text-sm text-gray-600">Kritikë</span>
                  <span className="text-sm font-semibold text-red-600">
                    {zone.criticalContainers}
                  </span>
                </div>

                <div className="flex items-center justify-between">
                  <span className="text-sm text-gray-600">Operativë</span>
                  <span className="text-sm font-semibold text-green-600">
                    {zone.operationalContainers}
                  </span>
                </div>

                {/* Average Fill Level */}
                <div className="pt-3 border-t">
                  <div className="flex items-center justify-between mb-2">
                    <span className="text-sm text-gray-600">
                      Mbushje Mesatare
                    </span>
                    <span className="text-sm font-semibold text-gray-900">
                      {Math.round(zone.averageFillLevel)}%
                    </span>
                  </div>
                  <div className="w-full bg-gray-200 rounded-full h-2">
                    <div
                      className={`h-2 rounded-full transition-all ${
                        zone.averageFillLevel >= 90
                          ? "bg-red-500"
                          : zone.averageFillLevel >= 70
                          ? "bg-amber-500"
                          : "bg-green-500"
                      }`}
                      style={{ width: `${zone.averageFillLevel}%` }}
                    />
                  </div>
                </div>
              </div>
            </div>
          </div>
        ))}
      </div>

      {/* Zone Details Modal */}
      {selectedZone && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center p-4 z-50">
          <div className="bg-white rounded-xl shadow-2xl max-w-3xl w-full max-h-[90vh] overflow-y-auto">
            {/* Modal Header */}
            <div className="bg-gradient-to-r from-eco-blue to-blue-600 text-white p-6 rounded-t-xl">
              <div className="flex items-center justify-between">
                <div>
                  <h2 className="text-2xl font-bold">
                    {selectedZone.zoneName}
                  </h2>
                  <p className="text-blue-100 mt-1">{selectedZone.zoneId}</p>
                </div>
                <button
                  onClick={() => setSelectedZone(null)}
                  className="hover:bg-white hover:bg-opacity-20 rounded-full p-2 transition-all"
                >
                  <X className="w-6 h-6" />
                </button>
              </div>
            </div>

            {/* Modal Content */}
            <div className="p-6 space-y-6">
              {/* Status Badge */}
              <div className="flex items-center justify-center">
                <span
                  className={`px-4 py-2 text-sm font-semibold rounded-full border ${getStatusColor(
                    selectedZone.status
                  )}`}
                >
                  {selectedZone.status}
                </span>
              </div>

              {/* Statistics Grid */}
              <div className="grid grid-cols-2 gap-4">
                <div className="bg-gray-50 rounded-lg p-4 border border-gray-200">
                  <div className="flex items-center space-x-3">
                    <div className="bg-gray-200 rounded-lg p-2">
                      <MapPin className="w-5 h-5 text-gray-700" />
                    </div>
                    <div>
                      <p className="text-xs text-gray-500 font-medium">
                        Total Kontejnerë
                      </p>
                      <p className="text-2xl font-bold text-gray-900">
                        {selectedZone.totalContainers}
                      </p>
                    </div>
                  </div>
                </div>

                <div className="bg-red-50 rounded-lg p-4 border border-red-200">
                  <div className="flex items-center space-x-3">
                    <div className="bg-red-200 rounded-lg p-2">
                      <AlertTriangle className="w-5 h-5 text-red-700" />
                    </div>
                    <div>
                      <p className="text-xs text-gray-500 font-medium">
                        Kritikë
                      </p>
                      <p className="text-2xl font-bold text-red-600">
                        {selectedZone.criticalContainers}
                      </p>
                    </div>
                  </div>
                </div>

                <div className="bg-green-50 rounded-lg p-4 border border-green-200">
                  <div className="flex items-center space-x-3">
                    <div className="bg-green-200 rounded-lg p-2">
                      <CheckCircle className="w-5 h-5 text-green-700" />
                    </div>
                    <div>
                      <p className="text-xs text-gray-500 font-medium">
                        Operativë
                      </p>
                      <p className="text-2xl font-bold text-green-600">
                        {selectedZone.operationalContainers}
                      </p>
                    </div>
                  </div>
                </div>

                <div className="bg-blue-50 rounded-lg p-4 border border-blue-200">
                  <div className="flex items-center space-x-3">
                    <div className="bg-blue-200 rounded-lg p-2">
                      <Activity className="w-5 h-5 text-blue-700" />
                    </div>
                    <div>
                      <p className="text-xs text-gray-500 font-medium">
                        Mbushje Mesatare
                      </p>
                      <p className="text-2xl font-bold text-blue-600">
                        {Math.round(selectedZone.averageFillLevel)}%
                      </p>
                    </div>
                  </div>
                </div>
              </div>

              {/* Fill Level Visualization */}
              <div className="bg-gray-50 rounded-lg p-4 border border-gray-200">
                <h3 className="text-sm font-semibold text-gray-700 mb-3">
                  Niveli i Mbushjes Mesatare
                </h3>
                <div className="space-y-2">
                  <div className="flex items-center justify-between text-sm">
                    <span className="text-gray-600">Progresi</span>
                    <span className="font-semibold text-gray-900">
                      {Math.round(selectedZone.averageFillLevel)}%
                    </span>
                  </div>
                  <div className="w-full bg-gray-200 rounded-full h-4">
                    <div
                      className={`h-4 rounded-full transition-all ${
                        selectedZone.averageFillLevel >= 90
                          ? "bg-red-500"
                          : selectedZone.averageFillLevel >= 70
                          ? "bg-amber-500"
                          : "bg-green-500"
                      }`}
                      style={{ width: `${selectedZone.averageFillLevel}%` }}
                    />
                  </div>
                  <div className="flex justify-between text-xs text-gray-500">
                    <span>0%</span>
                    <span>50%</span>
                    <span>100%</span>
                  </div>
                </div>
              </div>

              {/* Additional Info */}
              <div className="bg-blue-50 rounded-lg p-4 border border-blue-200">
                <div className="flex items-start space-x-3">
                  <div className="bg-blue-200 rounded-lg p-2 flex-shrink-0">
                    <Activity className="w-5 h-5 text-blue-700" />
                  </div>
                  <div>
                    <h4 className="text-sm font-semibold text-blue-900 mb-1">
                      Informacion
                    </h4>
                    <p className="text-sm text-blue-800">
                      Kjo zonë ka {selectedZone.totalContainers} kontejnerë,
                      prej të cilëve {selectedZone.criticalContainers} janë në
                      gjendje kritike dhe kërkojnë mbledhje të menjëhershme.
                    </p>
                  </div>
                </div>
              </div>

              {/* Action Buttons */}
              <div className="flex space-x-3">
                <button
                  onClick={() => setSelectedZone(null)}
                  className="flex-1 bg-gray-100 text-gray-700 py-3 rounded-lg hover:bg-gray-200 transition-colors font-medium"
                >
                  Mbyll
                </button>
                <button className="flex-1 bg-eco-blue text-white py-3 rounded-lg hover:bg-blue-600 transition-colors font-medium">
                  Shiko Kontejnerët
                </button>
              </div>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default ZonesPage;
