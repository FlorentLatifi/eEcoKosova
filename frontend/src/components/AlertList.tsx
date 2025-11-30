import React from 'react';
import { AlertTriangle, MapPin, Navigation } from 'lucide-react';
import { Container } from '../services/api';

interface AlertListProps {
  containers: Container[];
}

const AlertList: React.FC<AlertListProps> = ({ containers }) => {
  const criticalContainers = containers.filter(c => c.fillLevel >= 90);

  if (criticalContainers.length === 0) return null;

  return (
    <div className="bg-red-50 border-l-4 border-red-500 rounded-lg p-6 mb-6">
      <div className="flex items-center mb-4">
        <AlertTriangle className="w-6 h-6 text-red-600 mr-2" />
        <h2 className="text-xl font-bold text-red-900">
          Alarm Kritik - {criticalContainers.length} Kontejnerë
        </h2>
      </div>

      <div className="space-y-3">
        {criticalContainers.map((container) => (
          <div
            key={container.id}
            className="bg-white rounded-lg p-4 shadow-sm border border-red-200"
          >
            <div className="flex items-start justify-between">
              <div className="flex-1">
                <div className="flex items-center mb-2">
                  <Navigation className="w-5 h-5 text-red-600 mr-2" />
                  <h3 className="font-semibold text-gray-900">{container.id}</h3>
                  <span className="ml-3 px-2 py-1 bg-red-100 text-red-800 text-xs font-semibold rounded">
                    {container.fillLevel}%
                  </span>
                </div>
                <div className="flex items-center text-sm text-gray-600">
                  <MapPin className="w-4 h-4 mr-2" />
                  <span>{container.address}</span>
                </div>
                <p className="text-sm text-gray-500 mt-1">Zona: {container.zoneId}</p>
              </div>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
};

export default AlertList;

