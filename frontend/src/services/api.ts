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

// Helper Functions
export const getStatusColor = (fillLevel: number): string => {
  if (fillLevel >= 90) return 'red';
  if (fillLevel >= 70) return 'amber';
  return 'green';
};

export const getStatusBadge = (fillLevel: number): string => {
  if (fillLevel >= 90) return 'badge-danger';
  if (fillLevel >= 70) return 'badge-warning';
  return 'badge-success';
};

export const getStatusText = (fillLevel: number): string => {
  if (fillLevel >= 90) return 'KRITIK';
  if (fillLevel >= 70) return 'PARALAJMËRIM';
  if (fillLevel <= 10) return 'BOS';
  return 'NORMAL';
};

export default api;

