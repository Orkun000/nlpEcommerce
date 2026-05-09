import { createContext, useContext } from 'react';
import AuthStore from './AuthStore';
import ProductStore from './ProductStore';
import CartStore from './CartStore';

class RootStore {
  constructor() {
    this.authStore = new AuthStore(this);
    this.productStore = new ProductStore(this);
    this.cartStore = new CartStore(this);
  }
}

export const rootStore = new RootStore();

// Context API for dependency injection
export const StoreContext = createContext(rootStore);

export const useStore = () => {
  return useContext(StoreContext);
};
