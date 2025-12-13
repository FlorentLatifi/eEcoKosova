import React, { createContext, useContext, useState, useEffect, useCallback } from 'react';
import { getAllContainers, type Container } from '../services/api';
import { isCritical } from '../utils/thresholdUtils';

export interface Notification {
  id: string;
  type: 'critical' | 'warning' | 'info' | 'success';
  title: string;
  message: string;
  timestamp: Date;
  read: boolean;
  containerId?: string;
}

interface NotificationContextType {
  notifications: Notification[];
  unreadCount: number;
  markAsRead: (id: string) => void;
  markAllAsRead: () => void;
  clearAll: () => void;
  addNotification: (notification: Omit<Notification, 'id' | 'timestamp' | 'read'>) => void;
}

const NotificationContext = createContext<NotificationContextType | undefined>(undefined);

export const NotificationProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const [notifications, setNotifications] = useState<Notification[]>([]);

  // Load notifications from localStorage
  useEffect(() => {
    const saved = localStorage.getItem('ecokosova_notifications');
    if (saved) {
      try {
        const parsed = JSON.parse(saved).map((n: any) => ({
          ...n,
          timestamp: new Date(n.timestamp),
        }));
        setNotifications(parsed);
      } catch (e) {
        console.error('Error loading notifications:', e);
      }
    }
  }, []);

  // Save notifications to localStorage
  useEffect(() => {
    if (notifications.length > 0) {
      localStorage.setItem('ecokosova_notifications', JSON.stringify(notifications));
    }
  }, [notifications]);

  // Check for critical containers periodically
  useEffect(() => {
    const checkCriticalContainers = async () => {
      try {
        const allContainers = await getAllContainers();
        const critical = allContainers.filter(c => isCritical(c.fillLevel));
        
        if (critical.length > 0) {
          critical.forEach((container) => {
            const exists = notifications.some(
              (n) => n.containerId === container.id && !n.read
            );
            if (!exists) {
              addNotification({
                type: 'critical',
                title: 'Kontejner Kritik',
                message: `Kontejneri ${container.id} ka mbushje ${container.fillLevel}%`,
                containerId: container.id,
              });
            }
          });
        }
      } catch (error) {
        console.error('Error checking critical containers:', error);
      }
    };

    // Check immediately
    checkCriticalContainers();

    // Check every 30 seconds
    const interval = setInterval(checkCriticalContainers, 30000);
    return () => clearInterval(interval);
  }, [notifications, addNotification]);

  const addNotification = useCallback(
    (notification: Omit<Notification, 'id' | 'timestamp' | 'read'>) => {
      const newNotification: Notification = {
        ...notification,
        id: Date.now().toString(),
        timestamp: new Date(),
        read: false,
      };
      setNotifications((prev) => [newNotification, ...prev].slice(0, 50)); // Keep last 50
    },
    []
  );

  const markAsRead = useCallback((id: string) => {
    setNotifications((prev) =>
      prev.map((n) => (n.id === id ? { ...n, read: true } : n))
    );
  }, []);

  const markAllAsRead = useCallback(() => {
    setNotifications((prev) => prev.map((n) => ({ ...n, read: true })));
  }, []);

  const clearAll = useCallback(() => {
    setNotifications([]);
    localStorage.removeItem('ecokosova_notifications');
  }, []);

  const unreadCount = notifications.filter((n) => !n.read).length;

  return (
    <NotificationContext.Provider
      value={{
        notifications,
        unreadCount,
        markAsRead,
        markAllAsRead,
        clearAll,
        addNotification,
      }}
    >
      {children}
    </NotificationContext.Provider>
  );
};

export const useNotifications = () => {
  const context = useContext(NotificationContext);
  if (context === undefined) {
    throw new Error('useNotifications must be used within a NotificationProvider');
  }
  return context;
};

