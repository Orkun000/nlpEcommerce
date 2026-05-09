import { makeAutoObservable } from 'mobx';
import api from '../services/api';

class ProductStore {
  products = [];
  isLoading = false;
  error = null;

  constructor(rootStore) {
    this.rootStore = rootStore;
    makeAutoObservable(this);
  }

  async fetchProducts() {
    this.isLoading = true;
    this.error = null;
    try {
      const response = await api.get('/products');
      // The backend returns an ApiResponse wrapper containing a Page object in the data field
      // e.g. { success: true, data: { content: [...] } }
      this.products = response.data.data?.content || response.data.data || [];
    } catch (err) {
      this.error = 'Failed to fetch products.';
      console.error(err);
    } finally {
      this.isLoading = false;
    }
  }

  async searchProducts(query) {
    if (!query || query.trim() === '') {
      return this.fetchProducts();
    }
    
    this.isLoading = true;
    this.error = null;
    try {
      const response = await api.post('/search/nlp', { query });
      this.products = response.data.data?.content || response.data.data || [];
    } catch (err) {
      this.error = 'Search failed.';
      console.error(err);
    } finally {
      this.isLoading = false;
    }
  }
}

export default ProductStore;
