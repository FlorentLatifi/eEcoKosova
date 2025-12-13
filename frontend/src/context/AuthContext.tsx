import React, { createContext, useContext, useState, useEffect } from 'react';

export interface User {
  id: string;
  name: string;
  email: string;
  role: 'admin' | 'operator' | 'viewer';
  avatar?: string;
}

interface AuthContextType {
  user: User | null;
  login: (email: string, password: string) => Promise<boolean>;
  logout: () => void;
  updateUser: (userData: Partial<User>) => void;
  isAuthenticated: boolean;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export const AuthProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const [user, setUser] = useState<User | null>(null);

  useEffect(() => {
    // Load user from localStorage on mount
    const savedUser = localStorage.getItem('ecokosova_user');
    if (savedUser) {
      try {
        setUser(JSON.parse(savedUser));
      } catch (e) {
        console.error('Error loading user:', e);
      }
    } else {
      // Default demo user
      const defaultUser: User = {
        id: '1',
        name: 'Admin User',
        email: 'admin@ecokosova.com',
        role: 'admin',
      };
      setUser(defaultUser);
      localStorage.setItem('ecokosova_user', JSON.stringify(defaultUser));
    }
  }, []);

  const login = async (email: string, password: string): Promise<boolean> => {
    // Demo authentication - në aplikacion real do të bëhet API call
    if (email && password) {
      const userData: User = {
        id: '1',
        name: email.split('@')[0] || 'User',
        email,
        role: email.includes('admin') ? 'admin' : 'operator',
      };
      setUser(userData);
      localStorage.setItem('ecokosova_user', JSON.stringify(userData));
      return true;
    }
    return false;
  };

  const logout = () => {
    setUser(null);
    localStorage.removeItem('ecokosova_user');
    window.location.href = '/';
  };

  const updateUser = (userData: Partial<User>) => {
    if (user) {
      const updatedUser = { ...user, ...userData };
      setUser(updatedUser);
      localStorage.setItem('ecokosova_user', JSON.stringify(updatedUser));
    }
  };

  return (
    <AuthContext.Provider
      value={{
        user,
        login,
        logout,
        updateUser,
        isAuthenticated: !!user,
      }}
    >
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (context === undefined) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
};

