import { api } from './api';
import { ApiError, handleApiError } from './api';

export interface LoginRequest {
  username: string;
  password: string;
}

export interface RegisterRequest {
  username: string;
  email: string;
  password: string;
  role?: string;
}

export interface AuthResponse {
  token: string;
  type: string;
  username: string;
  email: string;
  role: string;
  expiresIn: number;
}

export interface User {
  username: string;
  email: string;
  role: string;
}

// Auth API functions
export const login = async (credentials: LoginRequest): Promise<AuthResponse> => {
  try {
    const response = await api.post<AuthResponse>('/auth/login', credentials);
    const { token, username, email, role } = response.data;
    
    // Store token dhe user info
    localStorage.setItem('auth_token', token);
    localStorage.setItem('auth_user', JSON.stringify({ username, email, role }));
    
    return response.data;
  } catch (error) {
    handleApiError(error, 'login');
  }
};

export const register = async (data: RegisterRequest): Promise<AuthResponse> => {
  try {
    const response = await api.post<AuthResponse>('/auth/register', data);
    const { token, username, email, role } = response.data;
    
    // Store token dhe user info
    localStorage.setItem('auth_token', token);
    localStorage.setItem('auth_user', JSON.stringify({ username, email, role }));
    
    return response.data;
  } catch (error) {
    handleApiError(error, 'register');
  }
};

export const logout = (): void => {
  localStorage.removeItem('auth_token');
  localStorage.removeItem('auth_user');
};

export const getCurrentUser = (): User | null => {
  const userStr = localStorage.getItem('auth_user');
  if (userStr) {
    try {
      return JSON.parse(userStr);
    } catch {
      return null;
    }
  }
  return null;
};

export const isAuthenticated = (): boolean => {
  return !!localStorage.getItem('auth_token');
};

export const isAdmin = (): boolean => {
  const user = getCurrentUser();
  return user?.role === 'ADMIN';
};

