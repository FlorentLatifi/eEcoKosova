import { useEffect, useRef } from 'react';
import { MapContainer, TileLayer, Marker, Popup, useMap } from 'react-leaflet';
import L from 'leaflet';
import 'leaflet/dist/leaflet.css';
import type { Container } from '../services/api';
import { getStatusColor } from '../utils/thresholdUtils';

// Fix për default markers në Leaflet
delete (L.Icon.Default.prototype as any)._getIconUrl;
L.Icon.Default.mergeOptions({
  iconRetinaUrl: 'https://cdnjs.cloudflare.com/ajax/libs/leaflet/1.9.4/images/marker-icon-2x.png',
  iconUrl: 'https://cdnjs.cloudflare.com/ajax/libs/leaflet/1.9.4/images/marker-icon.png',
  shadowUrl: 'https://cdnjs.cloudflare.com/ajax/libs/leaflet/1.9.4/images/marker-shadow.png',
});

interface ContainerMapProps {
  containers: Container[];
  selectedContainer?: Container | null;
  onContainerClick?: (container: Container) => void;
  center?: [number, number];
  zoom?: number;
}

// Komponent për të përditësuar view kur selectedContainer ndryshon
const MapUpdater = ({ 
  selectedContainer, 
  center 
}: { 
  selectedContainer?: Container | null;
  center?: [number, number];
}) => {
  const map = useMap();

  useEffect(() => {
    if (selectedContainer) {
      map.setView(
        [selectedContainer.latitude, selectedContainer.longitude],
        map.getZoom()
      );
    } else if (center) {
      map.setView(center, map.getZoom());
    }
  }, [selectedContainer, center, map]);

  return null;
};

// Krijo custom icon bazuar në status
const createContainerIcon = (fillLevel: number) => {
  const color = getStatusColor(fillLevel);
  const iconColor = color === 'red' ? 'red' : color === 'amber' ? 'orange' : 'green';
  
  return L.divIcon({
    className: 'custom-marker',
    html: `
      <div style="
        background-color: ${iconColor};
        width: 24px;
        height: 24px;
        border-radius: 50%;
        border: 2px solid white;
        box-shadow: 0 2px 4px rgba(0,0,0,0.3);
        display: flex;
        align-items: center;
        justify-content: center;
        color: white;
        font-weight: bold;
        font-size: 10px;
      ">
        ${Math.round(fillLevel)}%
      </div>
    `,
    iconSize: [24, 24],
    iconAnchor: [12, 12],
  });
};

export const ContainerMap = ({
  containers,
  selectedContainer,
  onContainerClick,
  center = [42.6629, 21.1655], // Default: Prishtina
  zoom = 12,
}: ContainerMapProps) => {
  const mapRef = useRef<L.Map | null>(null);

  // Group containers by location për clustering (simplified)
  const groupedContainers = containers.reduce((acc, container) => {
    const key = `${Math.round(container.latitude * 100) / 100}_${Math.round(container.longitude * 100) / 100}`;
    if (!acc[key]) {
      acc[key] = [];
    }
    acc[key].push(container);
    return acc;
  }, {} as Record<string, Container[]>);

  return (
    <div className="w-full h-full rounded-lg overflow-hidden border border-gray-200">
      <MapContainer
        center={center}
        zoom={zoom}
        style={{ height: '100%', width: '100%' }}
        ref={mapRef}
      >
        <TileLayer
          attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
          url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
        />
        
        <MapUpdater selectedContainer={selectedContainer} center={center} />

        {/* Render markers */}
        {Object.entries(groupedContainers).map(([key, group]) => {
          const firstContainer = group[0];
          const avgFillLevel = group.reduce((sum, c) => sum + c.fillLevel, 0) / group.length;
          
          // Nëse ka më shumë se 1 kontejner në të njëjtin vend, shfaq cluster
          if (group.length > 1) {
            return (
              <Marker
                key={key}
                position={[firstContainer.latitude, firstContainer.longitude]}
                icon={L.divIcon({
                  className: 'cluster-marker',
                  html: `
                    <div style="
                      background-color: #3b82f6;
                      width: 32px;
                      height: 32px;
                      border-radius: 50%;
                      border: 3px solid white;
                      box-shadow: 0 2px 4px rgba(0,0,0,0.3);
                      display: flex;
                      align-items: center;
                      justify-content: center;
                      color: white;
                      font-weight: bold;
                      font-size: 12px;
                    ">
                      ${group.length}
                    </div>
                  `,
                  iconSize: [32, 32],
                  iconAnchor: [16, 16],
                })}
              >
                <Popup>
                  <div className="p-2">
                    <h3 className="font-semibold mb-2">{group.length} Kontejnerë</h3>
                    <div className="space-y-1 text-sm">
                      <p>Niveli mesatar: {Math.round(avgFillLevel)}%</p>
                      <p>Zona: {firstContainer.zoneId}</p>
                    </div>
                  </div>
                </Popup>
              </Marker>
            );
          }

          // Single container marker
          return (
            <Marker
              key={firstContainer.id}
              position={[firstContainer.latitude, firstContainer.longitude]}
              icon={createContainerIcon(firstContainer.fillLevel)}
              eventHandlers={{
                click: () => {
                  if (onContainerClick) {
                    onContainerClick(firstContainer);
                  }
                },
              }}
            >
              <Popup>
                <div className="p-2 min-w-[200px]">
                  <h3 className="font-semibold mb-2">{firstContainer.id}</h3>
                  <div className="space-y-1 text-sm">
                    <p><strong>Tipi:</strong> {firstContainer.type}</p>
                    <p><strong>Niveli:</strong> {firstContainer.fillLevel}%</p>
                    <p><strong>Statusi:</strong> {firstContainer.status}</p>
                    <p><strong>Adresa:</strong> {firstContainer.address}</p>
                    <p><strong>Zona:</strong> {firstContainer.zoneId}</p>
                  </div>
                </div>
              </Popup>
            </Marker>
          );
        })}
      </MapContainer>
    </div>
  );
};

