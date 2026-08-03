import axios from 'axios';
import toast from 'react-hot-toast';
import useAuthStore from '../../store/authStore';

// Ensure baseURL always ends with '/api/pharmacy' to match the Spring Boot backend
let base = import.meta.env.VITE_API_URL || '/api/pharmacy';

// For local development with Vite proxy, strip out absolute URL to prevent cross-origin cookie rejection
if (base.startsWith('http://localhost') || base.startsWith('http://127.0.0.1')) {
  base = '/api/pharmacy';
}

if (base && !base.endsWith('/api/pharmacy')) {
  base = base.endsWith('/') ? `${base}api/pharmacy` : `${base}/api/pharmacy`;
}

const api = axios.create({
  baseURL: base,
  withCredentials: true,
});

// Request interceptor – attach token directly from shared auth store (useAuthStore)
api.interceptors.request.use(
  (config) => {
    const token = useAuthStore.getState().token;
    if (token) {
      config.headers['Authorization'] = `Bearer ${token}`;
    }

    const activeRole = localStorage.getItem('activeRole');
    if (activeRole) {
      config.headers['X-Active-Role'] = activeRole;
    }
    return config;
  },
  (error) => Promise.reject(error)
);

// Response interceptor – handle 401/403 globally
api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      // Ignore 401s from the login endpoint so the UI can display the error
      if (error.config && !error.config.url.includes('/auth/login')) {
        // Token expired — clear and redirect to login
        localStorage.clear();
        window.dispatchEvent(new Event('auth:expired'));
        if (window.location.pathname !== '/login') {
          window.location.href = '/login';
        }
      }
    }
    if (error.response?.status === 403) {
      console.error('403 Forbidden:', error.config?.url,
        '| Check: 1) token sent? 2) role allowed in SecurityConfig?');
    }
    return Promise.reject(error);
  }
);

export default api;

export const fetchWithRetry = async (url, options = {}, retries = 2) => {
  let attempt = 0;
  const delays = [500, 1000];

  while (attempt <= retries) {
    try {
      const response = await api({ url, ...options });
      return response;
    } catch (error) {
      if (attempt === retries || (error.response && error.response.status !== 429 && (error.response.status >= 400))) {
        throw error;
      }
      console.warn(`Fetch failed (attempt ${attempt + 1}/${retries + 1}). Retrying in ${delays[attempt]}ms...`);
      await new Promise(res => setTimeout(res, delays[attempt] || 1000));
      attempt++;
    }
  }
};
