import axios from 'axios';

// API instance
const api = axios.create({
  baseURL: 'http://localhost:8080/api/v1', // Adjusted for Spring Boot v1 endpoint
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Request interceptor to add auth token if needed
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// Response interceptor for generic error handling
api.interceptors.response.use(
  (response) => {
    return response;
  },
  (error) => {
    // Handle global errors (e.g., 401 Unauthorized)
    if (error.response && error.response.status === 401) {
      // Logic to logout or refresh token could go here
      console.error('Unauthorized access. Please login again.');
    }
    return Promise.reject(error);
  }
);

export default api;
