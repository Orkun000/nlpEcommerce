import { makeAutoObservable } from 'mobx';
import api from '../services/api';

class AuthStore {
  user = null;
  isAuthenticated = false;
  isLoading = false;
  error = null;

  constructor(rootStore) {
    this.rootStore = rootStore;
    makeAutoObservable(this);
    // Check local storage for initial auth state if desired
  }

  async login(email, password) {
    this.isLoading = true;
    this.error = null;
    try {
      const response = await api.post('/users/login', { email, password });
      
      if (response.data.success) {
        // Backend UserResponse object
        const user = response.data.data;
        this.setAuthSuccess(user, 'mock-jwt-token-if-needed');
        return true;
      } else {
        this.error = response.data.message || 'Login failed.';
        return false;
      }
    } catch (err) {
      this.error = err.response?.data?.message || 'Login failed. Please check your credentials.';
      return false;
    } finally {
      this.isLoading = false;
    }
  }

  async register(firstName, lastName, email, password) {
    this.isLoading = true;
    this.error = null;
    try {
      const response = await api.post('/users/register', { firstName, lastName, email, password });
      if (response.data.success) {
        return true; // Optionally log them in immediately, but returning true is enough
      } else {
        this.error = response.data.message || 'Registration failed.';
        return false;
      }
    } catch (err) {
      this.error = err.response?.data?.message || 'Registration failed. Please check your details.';
      return false;
    } finally {
      this.isLoading = false;
    }
  }

  setAuthSuccess(user, token) {
    this.user = user;
    this.isAuthenticated = true;
    localStorage.setItem('token', token);
  }

  logout() {
    this.user = null;
    this.isAuthenticated = false;
    localStorage.removeItem('token');
  }
}

export default AuthStore;
