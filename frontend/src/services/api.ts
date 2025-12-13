import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080/api';

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Types - Përputhet me ContainerResponseDTO nga backend
export interface Container {
  id: string;
  zoneId: string;
  type: string;
  fillLevel: number;
  status: string;
  capacity: number;
  operational: boolean;
  latitude: number;
  longitude: number;
  address: string;
  needsCollection: boolean;
}

export interface ZoneStatistics {
  zoneId: string;
  zoneName: string;
  totalContainers: number;
  criticalContainers: number;
  operationalContainers: number;
  averageFillLevel: number;
  status: string;
}

export interface Route {
  zoneId: string;
  zoneName: string;
  containerCount: number;
  totalDistanceKm: number;
  estimatedTimeMinutes: number;
  totalCapacityLiters: number;
  containers: Container[];
  routeType: string;
}

export interface Report {
  id: string;
  title: string;
  description: string;
  generatedAt: string;
  type: string;
  data: any;
}

// API Functions
export const getAllContainers = async (): Promise<Container[]> => {
  try {
    const response = await api.get<Container[]>('/monitoring/containers');
    return response.data;
  } catch (error) {
    console.error('Error fetching containers:', error);
    throw error;
  }
};

export const getCriticalContainers = async (): Promise<Container[]> => {
  try {
    const response = await api.get<Container[]>('/monitoring/containers/critical');
    return response.data;
  } catch (error) {
    console.error('Error fetching critical containers:', error);
    throw error;
  }
};

export const updateFillLevel = async (containerId: string, fillLevel: number): Promise<string> => {
  try {
    const response = await api.put<string>(
      `/monitoring/containers/${containerId}/fill-level`,
      { fillLevel }
    );
    return response.data;
  } catch (error) {
    console.error('Error updating fill level:', error);
    throw error;
  }
};

export const getZoneStatistics = async (): Promise<ZoneStatistics[]> => {
  try {
    const response = await api.get<ZoneStatistics[]>('/zones/statistics');
    return response.data;
  } catch (error) {
    console.error('Error fetching zone statistics:', error);
    throw error;
  }
};

export const getRouteForZone = async (
  zoneId: string,
  startLat: number = 42.6629,
  startLon: number = 21.1655,
  strategy: string = 'OPTIMAL'
): Promise<Route> => {
  try {
    const response = await api.get<Route>(`/routes/zone/${zoneId}`, {
      params: { startLat, startLon, strategy }
    });
    return response.data;
  } catch (error) {
    console.error('Error fetching route:', error);
    throw error;
  }
};

export const getAllRoutes = async (
  startLat: number = 42.6629,
  startLon: number = 21.1655,
  strategy: string = 'OPTIMAL'
): Promise<Route[]> => {
  try {
    const response = await api.get<Route[]>('/routes/all', {
      params: { startLat, startLon, strategy }
    });
    return response.data;
  } catch (error) {
    console.error('Error fetching all routes:', error);
    throw error;
  }
};

export const getReports = async (): Promise<Report[]> => {
  try {
    const response = await api.get<Report[]>('/reports');
    return response.data;
  } catch (error) {
    console.error('Error fetching reports:', error);
    throw error;
  }
};

export const generateReport = async (reportType: string): Promise<Report> => {
  try {
    const response = await api.post<Report>(`/reports/generate`, {
      type: reportType
    });
    return response.data;
  } catch (error) {
    console.error('Error generating report:', error);
    throw error;
  }
};

// Container Management Functions
export const getContainerById = async (containerId: string): Promise<Container> => {
  try {
    const response = await api.get<Container>(`/containers/${containerId}`);
    return response.data;
  } catch (error) {
    console.error('Error fetching container:', error);
    throw error;
  }
};

export const getContainersByZone = async (zoneId: string): Promise<Container[]> => {
  try {
    const response = await api.get<Container[]>(`/containers/zone/${zoneId}`);
    return response.data;
  } catch (error) {
    console.error('Error fetching containers by zone:', error);
    throw error;
  }
};

export const createContainer = async (containerData: {
  id: string;
  zoneId: string;
  type: string;
  capacity?: number;
  latitude?: number;
  longitude?: number;
  street: string;
  city: string;
  municipality: string;
  postalCode?: string;
  operational?: boolean;
}): Promise<Container> => {
  try {
    const response = await api.post<Container>('/containers', containerData);
    return response.data;
  } catch (error) {
    console.error('Error creating container:', error);
    throw error;
  }
};

export const updateContainer = async (
  containerId: string,
  containerData: {
    zoneId?: string;
    type?: string;
    capacity?: number;
    latitude?: number;
    longitude?: number;
    street?: string;
    city?: string;
    municipality?: string;
    postalCode?: string;
    operational?: boolean;
    fillLevel?: number;
  }
): Promise<string> => {
  try {
    const response = await api.put<string>(`/containers/${containerId}`, containerData);
    return response.data;
  } catch (error) {
    console.error('Error updating container:', error);
    throw error;
  }
};

export const deleteContainer = async (containerId: string): Promise<string> => {
  try {
    const response = await api.delete<string>(`/containers/${containerId}`);
    return response.data;
  } catch (error) {
    console.error('Error deleting container:', error);
    throw error;
  }
};

export const scheduleCollection = async (
  containerId: string,
  scheduledTime?: string
): Promise<string> => {
  try {
    const response = await api.post<string>(
      `/containers/${containerId}/schedule-collection`,
      { scheduledTime: scheduledTime || new Date(Date.now() + 3600000).toISOString() }
    );
    return response.data;
  } catch (error) {
    console.error('Error scheduling collection:', error);
    throw error;
  }
};

export const emptyContainer = async (containerId: string): Promise<string> => {
  try {
    const response = await api.post<string>(`/containers/${containerId}/empty`);
    return response.data;
  } catch (error) {
    console.error('Error emptying container:', error);
    throw error;
  }
};

// Zone Management Functions
export const getAllZones = async (): Promise<any[]> => {
  try {
    const response = await api.get<any[]>('/zones');
    return response.data;
  } catch (error) {
    console.error('Error fetching zones:', error);
    throw error;
  }
};

export const createZone = async (zoneData: {
  zoneId: string;
  name: string;
  latitude?: number;
  longitude?: number;
  municipality: string;
  description?: string;
}): Promise<string> => {
  try {
    const response = await api.post<string>('/zones', zoneData);
    return response.data;
  } catch (error) {
    console.error('Error creating zone:', error);
    throw error;
  }
};

export const updateZone = async (
  zoneId: string,
  zoneData: {
    name?: string;
    latitude?: number;
    longitude?: number;
    municipality?: string;
    description?: string;
  }
): Promise<string> => {
  try {
    const response = await api.put<string>(`/zones/${zoneId}`, zoneData);
    return response.data;
  } catch (error) {
    console.error('Error updating zone:', error);
    throw error;
  }
};

export const deleteZone = async (zoneId: string): Promise<string> => {
  try {
    const response = await api.delete<string>(`/zones/${zoneId}`);
    return response.data;
  } catch (error) {
    console.error('Error deleting zone:', error);
    throw error;
  }
};

// Helper Functions - DEPRECATED: Përdor thresholdUtils në vend
// Këto funksione janë mbajtur për backward compatibility
// Por rekomandohet përdorimi i thresholdUtils.ts

// Re-export nga thresholdUtils
export { 
  getStatusColor, 
  getStatusText 
} from '../utils/thresholdUtils';

// Legacy function për backward compatibility (badge class names)
export const getStatusBadge = (fillLevel: number): string => {
  // Lexo threshold nga localStorage
  const getCriticalThreshold = (): number => {
    const settings = localStorage.getItem('ecokosova_settings');
    if (settings) {
      try {
        const parsed = JSON.parse(settings);
        if (parsed.criticalThreshold) return parsed.criticalThreshold;
      } catch (e) {
        console.error('Error reading critical threshold:', e);
      }
    }
    return 90; // Default
  };

  const getWarningThreshold = (): number => {
    const settings = localStorage.getItem('ecokosova_settings');
    if (settings) {
      try {
        const parsed = JSON.parse(settings);
        if (parsed.warningThreshold) return parsed.warningThreshold;
      } catch (e) {
        console.error('Error reading warning threshold:', e);
      }
    }
    return 70; // Default
  };

  const criticalThreshold = getCriticalThreshold();
  const warningThreshold = getWarningThreshold();

  if (fillLevel >= criticalThreshold) return 'badge-danger';
  if (fillLevel >= warningThreshold) return 'badge-warning';
  return 'badge-success';
};

export default api;

