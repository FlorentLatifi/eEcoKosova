import React from 'react';
import { MapPin, Activity, AlertCircle } from 'lucide-react';
import { Container, getStatusBadge, getStatusText } from '../services/api';

interface ContainerCardProps {
  container: Container;
  onClick: () => void;
}

const ContainerCard: React.FC<ContainerCardProps> = ({ container, onClick }) => {
  const badgeClass = getStatusBadge(container.fillLevel);
  const statusText = getStatusText(container.fillLevel);
  
  return (
    <div
      onClick={onClick}
      className="bg-white rounded-lg shadow-md p-6 cursor-pointer hover:shadow-lg transition-all duration-200 border-l-4"
      style={{
        borderLeftColor:
          container.fillLevel >= 90
            ? '#EF4444'
            : container.fillLevel >= 70
            ? '#F59E0B'
            : '#10B981',
      }}
    >
      <div className="flex items-start justify-between mb-4">
        <div>
          <h3 className="text-lg font-bold text-gray-900">{container.id}</h3>
          <p className="text-sm text-gray-600">{container.type}</p>
        </div>
        <span className={`${badgeClass} whitespace-nowrap`}>{statusText}</span>
      </div>

      <div className="space-y-2 mb-4">
        <div className="flex items-center text-sm text-gray-600">
          <MapPin className="w-4 h-4 mr-2" />
          <span className="truncate">{container.address}</span>
        </div>
        <div className="flex items-center text-sm text-gray-600">
          <Activity className="w-4 h-4 mr-2" />
          <span>Kapacitet: {container.capacity}L</span>
        </div>
      </div>

      <div className="mb-2">
        <div className="flex justify-between text-sm mb-1">
          <span className="font-medium text-gray-700">Mbushje: {container.fillLevel}%</span>
          <span className="text-gray-500">Zona: {container.zoneId}</span>
        </div>
        <div className="w-full bg-gray-200 rounded-full h-3 overflow-hidden">
          <div
            className={`h-full transition-all ${
              container.fillLevel >= 90
                ? 'bg-red-500'
                : container.fillLevel >= 70
                ? 'bg-amber-500'
                : 'bg-green-500'
            }`}
            style={{ width: `${container.fillLevel}%` }}
          />
        </div>
      </div>

      {!container.operational && (
        <div className="flex items-center text-red-600 text-sm mt-2">
          <AlertCircle className="w-4 h-4 mr-1" />
          <span>Jo-Operativ</span>
        </div>
      )}
    </div>
  );
};

export default ContainerCard;

