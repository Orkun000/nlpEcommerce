import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import { ConfigProvider, Layout } from 'antd';
import Navbar from './components/Navbar';
import Home from './pages/Home';
import Login from './pages/Login';
import Register from './pages/Register';
import ProductDetail from './pages/ProductDetail';

const { Content, Footer } = Layout;

function App() {
  return (
    <ConfigProvider
      theme={{
        token: {
          colorPrimary: '#1890ff',
          borderRadius: 8,
          fontFamily: "'Inter', -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif",
        },
      }}
    >
      <Router>
        <Layout className="layout" style={{ minHeight: '100vh', background: '#f0f2f5' }}>
          <Navbar />
          <Content style={{ padding: '0 50px', marginTop: 24 }}>
            <div className="site-layout-content" style={{ minHeight: 'calc(100vh - 160px)' }}>
              <Routes>
                <Route path="/" element={<Home />} />
                <Route path="/login" element={<Login />} />
                <Route path="/register" element={<Register />} />
                <Route path="/products/:id" element={<ProductDetail />} />
                {/* Fallback route */}
                <Route path="*" element={<Home />} />
              </Routes>
            </div>
          </Content>
          <Footer style={{ textAlign: 'center', background: '#f0f2f5' }}>
            NLPEcommerce ©{new Date().getFullYear()} Created with React, Ant Design & MobX
          </Footer>
        </Layout>
      </Router>
    </ConfigProvider>
  );
}

export default App;
