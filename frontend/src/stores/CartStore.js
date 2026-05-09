import { makeAutoObservable } from 'mobx';
import { message } from 'antd';

class CartStore {
  items = []; // Array of { product, quantity }
  isDrawerVisible = false;

  constructor(rootStore) {
    this.rootStore = rootStore;
    makeAutoObservable(this);
    this.loadFromLocalStorage();
  }

  loadFromLocalStorage() {
    const savedCart = localStorage.getItem('cart');
    if (savedCart) {
      try {
        this.items = JSON.parse(savedCart);
      } catch (e) {
        console.error('Failed to parse cart from local storage', e);
      }
    }
  }

  saveToLocalStorage() {
    localStorage.setItem('cart', JSON.stringify(this.items));
  }

  addToCart(product, quantity = 1) {
    const existingItem = this.items.find(item => item.product.id === product.id);
    if (existingItem) {
      existingItem.quantity += quantity;
    } else {
      this.items.push({ product, quantity });
    }
    this.saveToLocalStorage();
    message.success(`${product.name} sepete eklendi!`);
  }

  removeFromCart(productId) {
    this.items = this.items.filter(item => item.product.id !== productId);
    this.saveToLocalStorage();
  }

  updateQuantity(productId, quantity) {
    if (quantity <= 0) {
      this.removeFromCart(productId);
      return;
    }
    const existingItem = this.items.find(item => item.product.id === productId);
    if (existingItem) {
      existingItem.quantity = quantity;
      this.saveToLocalStorage();
    }
  }

  clearCart() {
    this.items = [];
    this.saveToLocalStorage();
  }

  toggleDrawer() {
    this.isDrawerVisible = !this.isDrawerVisible;
  }

  closeDrawer() {
    this.isDrawerVisible = false;
  }

  get totalItems() {
    return this.items.reduce((total, item) => total + item.quantity, 0);
  }

  get totalPrice() {
    return this.items.reduce((total, item) => total + (item.product.price * item.quantity), 0);
  }
}

export default CartStore;
