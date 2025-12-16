import { useState, useCallback } from 'react';
import { ApiError } from '../services/api';

export interface UseApiState<T> {
  data: T | null;
  loading: boolean;
  error: string | null;
}

export interface UseApiReturn<T> extends UseApiState<T> {
  execute: (...args: any[]) => Promise<T | void>;
  reset: () => void;
}

/**
 * Custom hook për API calls me loading states dhe error handling
 */
export function useApi<T>(
  apiFunction: (...args: any[]) => Promise<T>,
  options?: {
    onSuccess?: (data: T) => void;
    onError?: (error: ApiError) => void;
    initialData?: T | null;
  }
): UseApiReturn<T> {
  const [state, setState] = useState<UseApiState<T>>({
    data: options?.initialData || null,
    loading: false,
    error: null,
  });

  const execute = useCallback(
    async (...args: any[]): Promise<T | void> => {
      setState((prev) => ({ ...prev, loading: true, error: null }));

      try {
        const data = await apiFunction(...args);
        setState({ data, loading: false, error: null });
        
        if (options?.onSuccess) {
          options.onSuccess(data);
        }
        
        return data;
      } catch (error) {
        const apiError = error instanceof ApiError 
          ? error 
          : new ApiError(error instanceof Error ? error.message : 'An unknown error occurred');
        
        setState({
          data: null,
          loading: false,
          error: apiError.message,
        });
        
        if (options?.onError) {
          options.onError(apiError);
        }
        
        throw apiError;
      }
    },
    [apiFunction, options]
  );

  const reset = useCallback(() => {
    setState({
      data: options?.initialData || null,
      loading: false,
      error: null,
    });
  }, [options?.initialData]);

  return {
    ...state,
    execute,
    reset,
  };
}

