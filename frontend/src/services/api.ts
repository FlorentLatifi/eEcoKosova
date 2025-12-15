import axios, { AxiosError } from 'axios';
import type { AxiosResponse } from 'axios';

// Lexo API_BASE_URL nga environment variable ose përdor default
const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api';
const API_TIMEOUT = parseInt(import.meta.env.VITE_API_TIMEOUT || '30000', 10);

// Krijo axios instance me konfigurim
const api = axios.create({
  baseURL: API_BASE_URL,
  timeout: API_TIMEOUT,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Request Interceptor - për të shtuar auth tokens, logging, etj.
api.interceptors.request.use(
  (config) => {
    // Mund të shtosh auth token këtu
    // const token = localStorage.getItem('auth_token');
    // if (token) {
    //   config.headers.Authorization = `Bearer ${token}`;
    // }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// Response Interceptor - për error handling global
api.interceptors.response.use(
  (response: AxiosResponse) => {
    return response;
  },
  (error: AxiosError) => {
    // Handle different error types
    if (error.response) {
      // Server responded with error status
      const status = error.response.status;
      const message = (error.response.data as any)?.message || error.message;
      
      switch (status) {
        case 400:
          console.error('Bad Request:', message);
          break;
        case 401:
          console.error('Unauthorized:', message);
          // Mund të redirect në login page
          break;
        case 403:
          console.error('Forbidden:', message);
          break;
        case 404:
          console.error('Not Found:', message);
          break;
        case 500:
          console.error('Server Error:', message);
          break;
        default:
          console.error(`Error ${status}:`, message);
      }
    } else if (error.request) {
      // Request was made but no response received
      console.error('Network Error: No response from server');
    } else {
      // Something else happened
      console.error('Error:', error.message);
    }
    
    return Promise.reject(error);
  }
);

// Error type për më mirë error handling
export class ApiError extends Error {
  constructor(
    message: string,
    public status?: number,
    public data?: any
  ) {
    super(message);
    this.name = 'ApiError';
  }
}

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

export interface Zone {
  id: string;
  name: string;
  criticalThreshold: number;
  containerIds: string[];
  status: string;
  centerPoint: {
    latitude: number;
    longitude: number;
  };
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

// Helper function për të handle errors në mënyrë konsistente
const handleApiError = (error: unknown, context: string): never => {
  if (axios.isAxiosError(error)) {
    const axiosError = error as AxiosError;

    // Provo të lexosh strukturën uniforme të error-it nga backend
    const data: any = axiosError.response?.data;
    let message = axiosError.message || `Error in ${context}`;

    if (data) {
      if (typeof data.message === 'string') {
        message = data.message;
      }

      // Në rast VALIDATION_ERROR, mbledh mesazhet e fushave
      if (Array.isArray(data.errors)) {
        const fieldMessages = data.errors
          .map((e: any) => (e && e.message ? String(e.message) : ''))
          .filter((m: string) => m.length > 0);
        if (fieldMessages.length > 0) {
          message = fieldMessages.join(' • ');
        }
      }
    }

    throw new ApiError(
      message,
      axiosError.response?.status,
      data
    );
  }
  throw new ApiError(`Unexpected error in ${context}`);
};

// API Functions
export const getAllContainers = async (): Promise<Container[]> => {
  try {
    const response = await api.get<Container[]>('/monitoring/containers');
    return response.data;
  } catch (error) {
    handleApiError(error, 'getAllContainers');
    // handleApiError always throws, so this is unreachable
    return [] as never;
  }
};

export const getCriticalContainers = async (): Promise<Container[]> => {
  try {
    const response = await api.get<Container[]>('/monitoring/containers/critical');
    return response.data;
  } catch (error) {
    handleApiError(error, 'getCriticalContainers');
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
    handleApiError(error, 'updateFillLevel');
  }
};

export const getZoneStatistics = async (): Promise<ZoneStatistics[]> => {
  try {
    const response = await api.get<ZoneStatistics[]>('/zones/statistics');
    return response.data;
  } catch (error) {
    handleApiError(error, 'getZoneStatistics');
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
    handleApiError(error, 'getRouteForZone');
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
    handleApiError(error, 'getAllRoutes');
  }
};

export const getReports = async (): Promise<Report[]> => {
  try {
    const response = await api.get<Report[]>('/reports');
    return response.data;
  } catch (error) {
    handleApiError(error, 'getReports');
  }
};

export const generateReport = async (reportType: string): Promise<Report> => {
  try {
    const response = await api.post<Report>(`/reports/generate`, {
      type: reportType
    });
    return response.data;
  } catch (error) {
    handleApiError(error, 'generateReport');
  }
};

// Container Management Functions
export const getContainerById = async (containerId: string): Promise<Container> => {
  try {
    const response = await api.get<Container>(`/containers/${containerId}`);
    return response.data;
  } catch (error) {
    handleApiError(error, 'getContainerById');
  }
};

export const getContainersByZone = async (zoneId: string): Promise<Container[]> => {
  try {
    const response = await api.get<Container[]>(`/containers/zone/${zoneId}`);
    return response.data;
  } catch (error) {
    handleApiError(error, 'getContainersByZone');
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
    handleApiError(error, 'createContainer');
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
    handleApiError(error, 'updateContainer');
  }
};

export const deleteContainer = async (containerId: string): Promise<string> => {
  try {
    const response = await api.delete<string>(`/containers/${containerId}`);
    return response.data;
  } catch (error) {
    handleApiError(error, 'deleteContainer');
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
    handleApiError(error, 'scheduleCollection');
  }
};

export const emptyContainer = async (containerId: string): Promise<string> => {
  try {
    const response = await api.post<string>(`/containers/${containerId}/empty`);
    return response.data;
  } catch (error) {
    handleApiError(error, 'emptyContainer');
  }
};

// Zone Management Functions
export const getAllZones = async (): Promise<Zone[]> => {
  try {
    const response = await api.get<Zone[]>('/zones');
    return response.data;
  } catch (error) {
    handleApiError(error, 'getAllZones');
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
    handleApiError(error, 'createZone');
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
    handleApiError(error, 'updateZone');
  }
};

export const deleteZone = async (zoneId: string): Promise<string> => {
  try {
    const response = await api.delete<string>(`/zones/${zoneId}`);
    return response.data;
  } catch (error) {
    handleApiError(error, 'deleteZone');
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
        // Silent fail - përdor default
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
        // Silent fail - përdor default
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

// ========== KAMIONI API ==========
export interface Kamioni {
  id: string;
  name: string;
  licensePlate: string;
  capacity: number;
  operatorId: string;
  status: string;
  latitude: number;
  longitude: number;
  currentRouteId?: string;
  assignedContainerCount: number;
  installationDate: string;
  lastUpdated: string;
  available: boolean;
}

export const getAllKamionet = async (): Promise<Kamioni[]> => {
  try {
    const response = await api.get<Kamioni[]>('/kamionet');
    return response.data;
  } catch (error) {
    handleApiError(error, 'getAllKamionet');
  }
};

export const getKamioniById = async (id: string): Promise<Kamioni> => {
  try {
    const response = await api.get<Kamioni>(`/kamionet/${id}`);
    return response.data;
  } catch (error) {
    handleApiError(error, 'getKamioniById');
  }
};

export const getAvailableKamionet = async (): Promise<Kamioni[]> => {
  try {
    const response = await api.get<Kamioni[]>('/kamionet/available');
    return response.data;
  } catch (error) {
    handleApiError(error, 'getAvailableKamionet');
  }
};

export const createKamioni = async (data: {
  id: string;
  name: string;
  licensePlate: string;
  capacity: number;
  operatorId: string;
  latitude: number;
  longitude: number;
}): Promise<Kamioni> => {
  try {
    const response = await api.post<Kamioni>('/kamionet', data);
    return response.data;
  } catch (error) {
    handleApiError(error, 'createKamioni');
  }
};

export const updateKamioni = async (id: string, data: Partial<Kamioni>): Promise<Kamioni> => {
  try {
    const response = await api.put<Kamioni>(`/kamionet/${id}`, data);
    return response.data;
  } catch (error) {
    handleApiError(error, 'updateKamioni');
  }
};

export const deleteKamioni = async (id: string): Promise<void> => {
  try {
    await api.delete(`/kamionet/${id}`);
  } catch (error) {
    handleApiError(error, 'deleteKamioni');
  }
};

export const assignRouteToKamioni = async (id: string, routeId: string, containerIds: string[]): Promise<Kamioni> => {
  try {
    const response = await api.post<Kamioni>(`/kamionet/${id}/assign-route`, { routeId, containerIds });
    return response.data;
  } catch (error) {
    handleApiError(error, 'assignRouteToKamioni');
  }
};

export const releaseRouteFromKamioni = async (id: string): Promise<Kamioni> => {
  try {
    const response = await api.post<Kamioni>(`/kamionet/${id}/release-route`);
    return response.data;
  } catch (error) {
    handleApiError(error, 'releaseRouteFromKamioni');
  }
};

// ========== QYTETARI API ==========
export interface Qytetari {
  id: string;
  name: string;
  address?: string;
  createdAt: string;
  lastUpdated: string;
}

export const getAllQytetaret = async (): Promise<Qytetari[]> => {
  try {
    const response = await api.get<Qytetari[]>('/qytetaret');
    return response.data;
  } catch (error) {
    handleApiError(error, 'getAllQytetaret');
  }
};

export const getQytetariById = async (id: string): Promise<Qytetari> => {
  try {
    const response = await api.get<Qytetari>(`/qytetaret/${id}`);
    return response.data;
  } catch (error) {
    handleApiError(error, 'getQytetariById');
  }
};

export const createQytetari = async (data: {
  id: string;
  name: string;
  address?: string;
}): Promise<Qytetari> => {
  try {
    const response = await api.post<Qytetari>('/qytetaret', data);
    return response.data;
  } catch (error) {
    handleApiError(error, 'createQytetari');
  }
};

export const updateQytetari = async (id: string, data: Partial<Qytetari>): Promise<Qytetari> => {
  try {
    const response = await api.put<Qytetari>(`/qytetaret/${id}`, data);
    return response.data;
  } catch (error) {
    handleApiError(error, 'updateQytetari');
  }
};

export const deleteQytetari = async (id: string): Promise<void> => {
  try {
    await api.delete(`/qytetaret/${id}`);
  } catch (error) {
    handleApiError(error, 'deleteQytetari');
  }
};

// ========== KONTROLL PANEL API ==========
export interface KontrollPanel {
  id: string;
  language: string;
  theme: string;
  screenState: string;
  qytetariId: string;
  createdAt: string;
  lastUpdated: string;
}

export const getAllPanels = async (): Promise<KontrollPanel[]> => {
  try {
    const response = await api.get<KontrollPanel[]>('/kontroll-panel');
    return response.data;
  } catch (error) {
    handleApiError(error, 'getAllPanels');
  }
};

export const getPanelById = async (id: string): Promise<KontrollPanel> => {
  try {
    const response = await api.get<KontrollPanel>(`/kontroll-panel/${id}`);
    return response.data;
  } catch (error) {
    handleApiError(error, 'getPanelById');
  }
};

export const getPanelByQytetariId = async (qytetariId: string): Promise<KontrollPanel> => {
  try {
    const response = await api.get<KontrollPanel>(`/kontroll-panel/qytetari/${qytetariId}`);
    return response.data;
  } catch (error) {
    handleApiError(error, 'getPanelByQytetariId');
  }
};

export const createPanel = async (data: {
  id: string;
  qytetariId: string;
}): Promise<KontrollPanel> => {
  try {
    const response = await api.post<KontrollPanel>('/kontroll-panel', data);
    return response.data;
  } catch (error) {
    handleApiError(error, 'createPanel');
  }
};

export const updatePanel = async (id: string, data: Partial<KontrollPanel>): Promise<KontrollPanel> => {
  try {
    const response = await api.put<KontrollPanel>(`/kontroll-panel/${id}`, data);
    return response.data;
  } catch (error) {
    handleApiError(error, 'updatePanel');
  }
};

export const deletePanel = async (id: string): Promise<void> => {
  try {
    await api.delete(`/kontroll-panel/${id}`);
  } catch (error) {
    handleApiError(error, 'deletePanel');
  }
};

// ========== CIKLI MBLEDHJES API ==========
export interface CikliMbledhjes {
  id: string;
  scheduleTime: string;
  maxCapacity: number;
  collectionDays: string[];
  zoneId: string;
  kamioniId?: string;
  status: string;
  createdAt: string;
  lastUpdated: string;
}

export const getAllCiklet = async (): Promise<CikliMbledhjes[]> => {
  try {
    const response = await api.get<CikliMbledhjes[]>('/ciklet');
    return response.data;
  } catch (error) {
    handleApiError(error, 'getAllCiklet');
  }
};

export const getCikliById = async (id: string): Promise<CikliMbledhjes> => {
  try {
    const response = await api.get<CikliMbledhjes>(`/ciklet/${id}`);
    return response.data;
  } catch (error) {
    handleApiError(error, 'getCikliById');
  }
};

export const getCikletByZone = async (zoneId: string): Promise<CikliMbledhjes[]> => {
  try {
    const response = await api.get<CikliMbledhjes[]>(`/ciklet/zone/${zoneId}`);
    return response.data;
  } catch (error) {
    handleApiError(error, 'getCikletByZone');
  }
};

export const getActiveCiklet = async (): Promise<CikliMbledhjes[]> => {
  try {
    const response = await api.get<CikliMbledhjes[]>('/ciklet/active');
    return response.data;
  } catch (error) {
    handleApiError(error, 'getActiveCiklet');
  }
};

export const createCikli = async (data: {
  id: string;
  scheduleTime: string;
  maxCapacity: number;
  collectionDays: string[];
  zoneId: string;
}): Promise<CikliMbledhjes> => {
  try {
    const response = await api.post<CikliMbledhjes>('/ciklet', data);
    return response.data;
  } catch (error) {
    handleApiError(error, 'createCikli');
  }
};

export const updateCikli = async (id: string, data: Partial<CikliMbledhjes>): Promise<CikliMbledhjes> => {
  try {
    const response = await api.put<CikliMbledhjes>(`/ciklet/${id}`, data);
    return response.data;
  } catch (error) {
    handleApiError(error, 'updateCikli');
  }
};

export const activateCikli = async (id: string): Promise<CikliMbledhjes> => {
  try {
    const response = await api.post<CikliMbledhjes>(`/ciklet/${id}/activate`);
    return response.data;
  } catch (error) {
    handleApiError(error, 'activateCikli');
  }
};

export const completeCikli = async (id: string): Promise<CikliMbledhjes> => {
  try {
    const response = await api.post<CikliMbledhjes>(`/ciklet/${id}/complete`);
    return response.data;
  } catch (error) {
    handleApiError(error, 'completeCikli');
  }
};

export const cancelCikli = async (id: string): Promise<CikliMbledhjes> => {
  try {
    const response = await api.post<CikliMbledhjes>(`/ciklet/${id}/cancel`);
    return response.data;
  } catch (error) {
    handleApiError(error, 'cancelCikli');
  }
};

export const deleteCikli = async (id: string): Promise<void> => {
  try {
    await api.delete(`/ciklet/${id}`);
  } catch (error) {
    handleApiError(error, 'deleteCikli');
  }
};

export default api;

