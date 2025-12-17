import { useState, useEffect } from 'react';
import { getAllContainers, updateFillLevel, ApiError } from '../services/api';
import type { Container } from '../services/api';
import { isCritical, isWarning, isNormal } from '../utils/thresholdUtils';
import { useToast } from '../context/ToastContext';

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

export const useContainers = (refreshInterval?: number): UseContainersReturn => {
  const [containers, setContainers] = useState<Container[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const { showError, showSuccess } = useToast();

  // Get refresh interval from settings or use default
  const getRefreshInterval = () => {
    if (refreshInterval !== undefined) return refreshInterval;
    const settings = localStorage.getItem('ecokosova_settings');
    if (settings) {
      try {
        const parsed = JSON.parse(settings);
        if (parsed.autoRefresh && parsed.refreshInterval) {
          return parsed.refreshInterval * 1000; // Convert seconds to milliseconds
        }
      } catch (e) {
        console.error('Error reading settings:', e);
      }
    }
    return 30000; // Default 30 seconds
  };

  const fetchContainers = async () => {
    try {
      setLoading(true);
      const data = await getAllContainers();
      setContainers(data.content);
      setError(null);
    } catch (err) {
      const errorMessage = err instanceof ApiError 
        ? err.message 
        : err instanceof Error 
        ? err.message 
        : 'Dështoi marrja e kontejnerëve';
      setError(errorMessage);
      showError(errorMessage);
    } finally {
      setLoading(false);
    }
  };

  const updateContainer = async (containerId: string, newFillLevel: number) => {
    try {
      await updateFillLevel(containerId, newFillLevel);
      await fetchContainers();
      showSuccess('Niveli i mbushjes u përditësua me sukses');
      return { success: true };
    } catch (err) {
      const errorMessage = err instanceof ApiError 
        ? err.message 
        : err instanceof Error 
        ? err.message 
        : 'Dështoi përditësimi i nivelit të mbushjes';
      showError(errorMessage);
      return { success: false, error: errorMessage };
    }
  };

  useEffect(() => {
    fetchContainers();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  useEffect(() => {
    const interval = getRefreshInterval();
    if (interval > 0) {
      const timer = setInterval(() => {
        fetchContainers();
      }, interval);
      return () => clearInterval(timer);
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  // Listen for settings updates
  useEffect(() => {
    const handleSettingsUpdate = () => {
      fetchContainers();
    };
    window.addEventListener('settingsUpdated', handleSettingsUpdate);
    return () => window.removeEventListener('settingsUpdated', handleSettingsUpdate);
  }, []);

  const statistics: Statistics = {
    total: containers.length,
    critical: containers.filter(c => isCritical(c.fillLevel)).length,
    warning: containers.filter(c => isWarning(c.fillLevel)).length,
    normal: containers.filter(c => isNormal(c.fillLevel)).length,
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

