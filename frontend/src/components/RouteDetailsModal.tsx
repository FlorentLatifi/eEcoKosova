import React from 'react';
import { X, MapPin, Clock, Navigation, TrendingUp, AlertTriangle } from 'lucide-react';
import type { Route } from '../services/api';

interface RouteDetailsModalProps {
  route: Route | null;
  isOpen: boolean;
  onClose: () => void;
}

const RouteDetailsModal: React.FC<RouteDetailsModalProps> = ({ route, isOpen, onClose }) => {
  if (!isOpen || !route) return null;

  const formatTime = (minutes: number): string => {
    const hours = Math.floor(minutes / 60);
    const mins = Math.floor(minutes % 60);
    if (hours > 0) {
      return `${hours}h ${mins}min`;
    }
    return `${mins}min`;
  };

  const getStatusColorClass = (fillLevel: number) => {
    const color = getStatusColor(fillLevel);
    if (color === 'red') return 'bg-red-100 text-red-800 border-red-300';
    if (color === 'amber') return 'bg-amber-100 text-amber-800 border-amber-300';
    return 'bg-green-100 text-green-800 border-green-300';
  };

  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center p-4 z-50">
      <div className="bg-white rounded-xl shadow-2xl max-w-4xl w-full max-h-[90vh] overflow-y-auto">
        {/* Header */}
        <div className="bg-gradient-to-r from-eco-blue to-blue-600 text-white p-6 rounded-t-xl">
          <div className="flex items-center justify-between">
            <div>
              <h2 className="text-2xl font-bold">{route.zoneName}</h2>
              <p className="text-blue-100 mt-1">{route.zoneId}</p>
              <span className="inline-block mt-2 px-3 py-1 text-sm font-semibold bg-white bg-opacity-20 rounded-full">
                {route.routeType === 'OPTIMAL' ? 'Optimale (Nearest Neighbor)' : 'Sipas Prioritetit'}
              </span>
            </div>
            <button
              onClick={onClose}
              className="hover:bg-white hover:bg-opacity-20 rounded-full p-2 transition-all"
            >
              <X className="w-6 h-6" />
            </button>
          </div>
        </div>

        {/* Content */}
        <div className="p-6 space-y-6">
          {/* Statistics Grid */}
          <div className="grid grid-cols-2 md:grid-cols-4 gap-4">
            <div className="bg-gray-50 rounded-lg p-4 border border-gray-200">
              <div className="flex items-center space-x-3">
                <div className="bg-gray-200 rounded-lg p-2">
                  <MapPin className="w-5 h-5 text-gray-700" />
                </div>
                <div>
                  <p className="text-xs text-gray-500 font-medium">Kontejnerë</p>
                  <p className="text-2xl font-bold text-gray-900">{route.containerCount}</p>
                </div>
              </div>
            </div>

            <div className="bg-blue-50 rounded-lg p-4 border border-blue-200">
              <div className="flex items-center space-x-3">
                <div className="bg-blue-200 rounded-lg p-2">
                  <Navigation className="w-5 h-5 text-blue-700" />
                </div>
                <div>
                  <p className="text-xs text-gray-500 font-medium">Distancë</p>
                  <p className="text-2xl font-bold text-blue-600">
                    {route.totalDistanceKm.toFixed(2)} km
                  </p>
                </div>
              </div>
            </div>

            <div className="bg-amber-50 rounded-lg p-4 border border-amber-200">
              <div className="flex items-center space-x-3">
                <div className="bg-amber-200 rounded-lg p-2">
                  <Clock className="w-5 h-5 text-amber-700" />
                </div>
                <div>
                  <p className="text-xs text-gray-500 font-medium">Kohë</p>
                  <p className="text-2xl font-bold text-amber-600">
                    {formatTime(route.estimatedTimeMinutes)}
                  </p>
                </div>
              </div>
            </div>

            <div className="bg-green-50 rounded-lg p-4 border border-green-200">
              <div className="flex items-center space-x-3">
                <div className="bg-green-200 rounded-lg p-2">
                  <TrendingUp className="w-5 h-5 text-green-700" />
                </div>
                <div>
                  <p className="text-xs text-gray-500 font-medium">Kapacitet</p>
                  <p className="text-2xl font-bold text-green-600">
                    {route.totalCapacityLiters}L
                  </p>
                </div>
              </div>
            </div>
          </div>

          {/* Containers List */}
          <div>
            <h3 className="text-lg font-semibold text-gray-900 mb-4">
              Kontejnerët në Rrugë ({route.containers.length})
            </h3>
            <div className="space-y-3">
              {route.containers.map((container, index) => (
                <div
                  key={container.id}
                  className="bg-white border border-gray-200 rounded-lg p-4 hover:shadow-md transition-shadow"
                >
                  <div className="flex items-center justify-between">
                    <div className="flex items-center space-x-4 flex-1">
                      <div className="flex-shrink-0 w-10 h-10 bg-eco-blue bg-opacity-10 rounded-full flex items-center justify-center">
                        <span className="text-eco-blue font-bold text-sm">#{index + 1}</span>
                      </div>
                      <div className="flex-1 min-w-0">
                        <div className="flex items-center space-x-2">
                          <MapPin className="w-4 h-4 text-gray-400 flex-shrink-0" />
                          <p className="text-sm font-semibold text-gray-900 truncate">
                            {container.id}
                          </p>
                        </div>
                        <p className="text-xs text-gray-500 mt-1 truncate">{container.address}</p>
                      </div>
                    </div>
                    <div className="flex items-center space-x-3">
                      <div className="text-right">
                        <p className="text-sm font-semibold text-gray-900">
                          {container.fillLevel}%
                        </p>
                        <div className="w-24 bg-gray-200 rounded-full h-2 mt-1">
                          <div
                            className={`h-2 rounded-full ${
                              isCritical(container.fillLevel)
                                ? 'bg-red-500'
                                : isWarning(container.fillLevel)
                                ? 'bg-amber-500'
                                : 'bg-green-500'
                            }`}
                            style={{ width: `${container.fillLevel}%` }}
                          />
                        </div>
                      </div>
                      <span
                        className={`px-3 py-1 text-xs font-semibold rounded-full border ${getStatusColorClass(
                          container.fillLevel
                        )}`}
                      >
                        {container.status}
                      </span>
                    </div>
                  </div>
                  {container.needsCollection && (
                    <div className="mt-3 flex items-center space-x-2 text-red-600 text-sm">
                      <AlertTriangle className="w-4 h-4" />
                      <span>Kërkon mbledhje urgjente</span>
                    </div>
                  )}
                </div>
              ))}
            </div>
          </div>

          {/* Route Summary */}
          <div className="bg-gray-50 rounded-lg p-4 border border-gray-200">
            <h4 className="font-semibold text-gray-900 mb-2">Përmbledhje e Rrugës</h4>
            <div className="grid grid-cols-2 gap-4 text-sm">
              <div>
                <span className="text-gray-600">Distancë totale:</span>
                <span className="font-semibold text-gray-900 ml-2">
                  {route.totalDistanceKm.toFixed(2)} km
                </span>
              </div>
              <div>
                <span className="text-gray-600">Kohë e parashikuar:</span>
                <span className="font-semibold text-gray-900 ml-2">
                  {formatTime(route.estimatedTimeMinutes)}
                </span>
              </div>
              <div>
                <span className="text-gray-600">Kontejnerë kritikë:</span>
                <span className="font-semibold text-red-600 ml-2">
                  {route.containers.filter((c) => isCritical(c.fillLevel)).length}
                </span>
              </div>
              <div>
                <span className="text-gray-600">Kapacitet total:</span>
                <span className="font-semibold text-gray-900 ml-2">
                  {route.totalCapacityLiters}L
                </span>
              </div>
            </div>
          </div>
        </div>

        {/* Footer */}
        <div className="p-6 border-t border-gray-200 bg-gray-50 rounded-b-xl">
          <button
            onClick={onClose}
            className="w-full bg-eco-blue text-white py-3 rounded-lg hover:bg-blue-600 transition-colors font-medium"
          >
            Mbyll
          </button>
        </div>
      </div>
    </div>
  );
};

export default RouteDetailsModal;

