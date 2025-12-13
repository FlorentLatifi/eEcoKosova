/**
 * Utility functions për threshold management
 * Lexon settings nga localStorage dhe kthen threshold-et aktuale
 */

export const getCriticalThreshold = (): number => {
  const settings = localStorage.getItem('ecokosova_settings');
  if (settings) {
    try {
      const parsed = JSON.parse(settings);
      if (parsed.criticalThreshold) {
        return parsed.criticalThreshold;
      }
    } catch (e) {
      console.error('Error reading critical threshold:', e);
    }
  }
  return 90; // Default
};

export const getWarningThreshold = (): number => {
  const settings = localStorage.getItem('ecokosova_settings');
  if (settings) {
    try {
      const parsed = JSON.parse(settings);
      if (parsed.warningThreshold) {
        return parsed.warningThreshold;
      }
    } catch (e) {
      console.error('Error reading warning threshold:', e);
    }
  }
  return 70; // Default
};

export const isCritical = (fillLevel: number): boolean => {
  return fillLevel >= getCriticalThreshold();
};

export const isWarning = (fillLevel: number): boolean => {
  const warningThreshold = getWarningThreshold();
  const criticalThreshold = getCriticalThreshold();
  return fillLevel >= warningThreshold && fillLevel < criticalThreshold;
};

export const isNormal = (fillLevel: number): boolean => {
  return fillLevel < getWarningThreshold();
};

export const getStatusColor = (fillLevel: number): string => {
  if (isCritical(fillLevel)) return 'red';
  if (isWarning(fillLevel)) return 'amber';
  return 'green';
};

export const getStatusBadge = (fillLevel: number): string => {
  if (isCritical(fillLevel)) return 'bg-red-100 text-red-800';
  if (isWarning(fillLevel)) return 'bg-amber-100 text-amber-800';
  return 'bg-green-100 text-green-800';
};

export const getStatusText = (fillLevel: number): string => {
  if (isCritical(fillLevel)) return 'KRITIK';
  if (isWarning(fillLevel)) return 'PARALAJMËRIM';
  if (fillLevel <= 10) return 'BOS';
  return 'NORMAL';
};

