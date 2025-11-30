import { useState, useEffect } from 'react';
import { getAllContainers, updateFillLevel } from '../services/api';
import type { Container } from '../services/api';

interface Statistics {
  total: number;
  critical: number;
  warning: number;
  normal: number;
  offline: number;
}

interface UseContainersReturn {
  containers: Container[];
  loading: boolean;
  error: string | null;
  statistics: Statistics;
  refresh: () => Promise<void>;
  updateContainer: (containerId: string, newFillLevel: number) => Promise<{ success: boolean; error?: string }>;
}

export const useContainers = (refreshInterval: number = 30000): UseContainersReturn => {
  const [containers, setContainers] = useState<Container[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  const fetchContainers = async () => {
    try {
      setLoading(true);
      const data = await getAllContainers();
      setContainers(data);
      setError(null);
    } catch (err) {
      const errorMessage = err instanceof Error ? err.message : 'Unknown error';
      setError(errorMessage);
      console.error('Failed to fetch containers:', err);
    } finally {
      setLoading(false);
    }
  };

  const updateContainer = async (containerId: string, newFillLevel: number) => {
    try {
      await updateFillLevel(containerId, newFillLevel);
      await fetchContainers();
      return { success: true };
    } catch (err) {
      const errorMessage = err instanceof Error ? err.message : 'Unknown error';
      console.error('Failed to update container:', err);
      return { success: false, error: errorMessage };
    }
  };

  useEffect(() => {
    fetchContainers();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  useEffect(() => {
    if (refreshInterval > 0) {
      const interval = setInterval(() => {
        fetchContainers();
      }, refreshInterval);
      return () => clearInterval(interval);
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [refreshInterval]);

  const statistics: Statistics = {
    total: containers.length,
    critical: containers.filter(c => c.fillLevel >= 90).length,
    warning: containers.filter(c => c.fillLevel >= 70 && c.fillLevel < 90).length,
    normal: containers.filter(c => c.fillLevel < 70).length,
    offline: containers.filter(c => !c.operational).length,
  };

  return {
    containers,
    loading,
    error,
    statistics,
    refresh: fetchContainers,
    updateContainer,
  };
};

