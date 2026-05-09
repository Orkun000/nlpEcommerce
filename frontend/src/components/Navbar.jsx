import React from 'react';
import { Layout, Menu, Button, Space, Badge, Drawer, List, Typography, Divider, InputNumber, message } from 'antd';
import { ShoppingCartOutlined, UserOutlined, LoginOutlined, LogoutOutlined, DeleteOutlined } from '@ant-design/icons';
import { useNavigate, useLocation } from 'react-router-dom';
import { observer } from 'mobx-react-lite';
import { useStore } from '../stores/RootStore';

const { Header } = Layout;
const { Text, Title } = Typography;

const Navbar = observer(() => {
  const { authStore, cartStore } = useStore();
  const navigate = useNavigate();
  const location = useLocation();

  const handleLogout = () => {
    authStore.logout();
    navigate('/login');
  };

  const menuItems = [
    {
      key: '/',
      label: 'Home',
    },
    {
      key: '/products',
      label: 'Products',
    }
  ];

  return (
    <>
      <Header style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', background: '#fff', padding: '0 50px', boxShadow: '0 2px 8px rgba(0,0,0,0.06)' }}>
        <div 
          className="logo" 
          style={{ fontSize: '1.5rem', fontWeight: 'bold', cursor: 'pointer', color: '#1890ff' }}
          onClick={() => navigate('/')}
        >
          NLPEcommerce
        </div>
        
        <Menu 
          mode="horizontal" 
          selectedKeys={[location.pathname]} 
          items={menuItems} 
          style={{ flex: 1, minWidth: 0, borderBottom: 'none', marginLeft: '40px' }} 
          onClick={({ key }) => navigate(key)}
        />

        <Space>
          <Badge count={cartStore.totalItems} offset={[-5, 5]} size="small">
            <Button 
              type="text" 
              icon={<ShoppingCartOutlined style={{ fontSize: '20px' }} />} 
              size="large" 
              onClick={() => cartStore.toggleDrawer()}
            />
          </Badge>
          {authStore.isAuthenticated ? (
            <>
              <span style={{ marginRight: '10px', marginLeft: '10px' }}>
                <UserOutlined /> {authStore.user?.firstName || authStore.user?.email}
              </span>
              <Button type="primary" danger icon={<LogoutOutlined />} onClick={handleLogout}>
                Logout
              </Button>
            </>
          ) : (
            <Button type="primary" icon={<LoginOutlined />} onClick={() => navigate('/login')} style={{ marginLeft: '10px' }}>
              Login
            </Button>
          )}
        </Space>
      </Header>

      <Drawer
        title={<div style={{display: 'flex', alignItems: 'center'}}><ShoppingCartOutlined style={{marginRight: '8px'}} /> Sepetiniz</div>}
        placement="right"
        onClose={() => cartStore.closeDrawer()}
        open={cartStore.isDrawerVisible}
        width={400}
      >
        {cartStore.items.length === 0 ? (
          <div style={{ textAlign: 'center', marginTop: '50px' }}>
            <ShoppingCartOutlined style={{ fontSize: '48px', color: '#ccc', marginBottom: '16px' }} />
            <Text type="secondary" style={{ display: 'block' }}>Sepetiniz şu an boş.</Text>
            <Button type="primary" style={{ marginTop: '16px' }} onClick={() => cartStore.closeDrawer()}>
              Alışverişe Başla
            </Button>
          </div>
        ) : (
          <div style={{ display: 'flex', flexDirection: 'column', height: '100%' }}>
            <List
              itemLayout="horizontal"
              dataSource={cartStore.items}
              renderItem={(item) => (
                <List.Item
                  actions={[
                    <Button 
                      type="text" 
                      danger 
                      icon={<DeleteOutlined />} 
                      onClick={() => cartStore.removeFromCart(item.product.id)} 
                    />
                  ]}
                >
                  <List.Item.Meta
                    title={<a onClick={() => { cartStore.closeDrawer(); navigate(`/products/${item.product.id}`); }}>{item.product.name}</a>}
                    description={`$${item.product.price.toFixed(2)}`}
                  />
                  <div style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
                    <InputNumber 
                      min={1} 
                      max={99} 
                      value={item.quantity} 
                      onChange={(val) => cartStore.updateQuantity(item.product.id, val)}
                      size="small"
                    />
                  </div>
                </List.Item>
              )}
            />
            <div style={{ marginTop: 'auto', paddingTop: '20px' }}>
              <Divider style={{ margin: '10px 0' }} />
              <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: '20px' }}>
                <Title level={4} style={{ margin: 0 }}>Toplam:</Title>
                <Title level={4} style={{ margin: 0, color: '#1890ff' }}>${cartStore.totalPrice.toFixed(2)}</Title>
              </div>
              <Button type="primary" size="large" block onClick={() => message.info('Satın alma işlemi entegre edilecek!')}>
                Siparişi Tamamla
              </Button>
            </div>
          </div>
        )}
      </Drawer>
    </>
  );
});

export default Navbar;
