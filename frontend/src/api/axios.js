import axios from 'axios';
import useAuthStore from '../store/authStore';

const BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api'; // API Gateway

export const axiosPrivate = axios.create({
    baseURL: BASE_URL,
    headers: { 'Content-Type': 'application/json' },
    withCredentials: true
});

export const axiosPublic = axios.create({
    baseURL: BASE_URL,
    headers: { 'Content-Type': 'application/json' },
});

axiosPrivate.interceptors.request.use(
    config => {
        const token = useAuthStore.getState().token;
        if (token) {
            config.headers['Authorization'] = `Bearer ${token}`;
        }
        return config;
    },
    error => Promise.reject(error)
);

// Track if a refresh is already in progress to avoid parallel refresh attempts
let isRefreshing = false;
let failedQueue = [];

const processQueue = (error, token = null) => {
    failedQueue.forEach(({ resolve, reject }) => {
        if (error) {
            reject(error);
        } else {
            resolve(token);
        }
    });
    failedQueue = [];
};

axiosPrivate.interceptors.response.use(
    response => response,
    async error => {
        const originalRequest = error?.config;

        if (error?.response?.status === 401 && !originalRequest?._retry) {
            // If this is a login request failing, don't try to refresh — surface the error
            if (originalRequest?.url?.includes('/auth/') && originalRequest?.url?.includes('/login')) {
                return Promise.reject(error);
            }

            if (isRefreshing) {
                // Queue the failed request — resolve/reject it after the refresh completes
                return new Promise((resolve, reject) => {
                    failedQueue.push({ resolve, reject });
                }).then(token => {
                    originalRequest.headers['Authorization'] = `Bearer ${token}`;
                    return axiosPrivate(originalRequest);
                }).catch(err => Promise.reject(err));
            }

            originalRequest._retry = true;
            isRefreshing = true;

            try {
                const newAccessToken = await useAuthStore.getState().refresh();
                if (newAccessToken) {
                    processQueue(null, newAccessToken);
                    originalRequest.headers['Authorization'] = `Bearer ${newAccessToken}`;
                    return axiosPrivate(originalRequest);
                } else {
                    // Refresh failed — clear auth state and let the RoleRoute redirect to login
                    processQueue(new Error('Session expired'));
                    useAuthStore.getState().logout();
                    return Promise.reject(error);
                }
            } catch (refreshError) {
                processQueue(refreshError);
                useAuthStore.getState().logout();
                return Promise.reject(refreshError);
            } finally {
                isRefreshing = false;
            }
        }

        return Promise.reject(error);
    }
);
