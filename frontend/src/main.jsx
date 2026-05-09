import React from 'react';
import ReactDOM from 'react-dom/client';
import App from './App.jsx';
import './index.css';
import { StoreContext, rootStore } from './stores/RootStore.js';

ReactDOM.createRoot(document.getElementById('root')).render(
  <React.StrictMode>
    <StoreContext.Provider value={rootStore}>
      <App />
    </StoreContext.Provider>
  </React.StrictMode>,
);
