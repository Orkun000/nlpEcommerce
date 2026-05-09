import React from 'react';
import { Form, Input, Button, Card, Typography, message } from 'antd';
import { UserOutlined, LockOutlined } from '@ant-design/icons';
import { observer } from 'mobx-react-lite';
import { useStore } from '../stores/RootStore';
import { useNavigate } from 'react-router-dom';

const { Title, Text } = Typography;

const Login = observer(() => {
  const { authStore } = useStore();
  const navigate = useNavigate();

  const onFinish = async (values) => {
    const success = await authStore.login(values.email, values.password);
    if (success) {
      message.success('Successfully logged in!');
      navigate('/');
    } else {
      message.error(authStore.error || 'Login failed');
    }
  };

  return (
    <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', minHeight: '70vh' }}>
      <Card 
        style={{ width: 400, boxShadow: '0 4px 12px rgba(0,0,0,0.08)', borderRadius: '12px' }}
        bordered={false}
      >
        <div style={{ textAlign: 'center', marginBottom: '2rem' }}>
          <Title level={2} style={{ margin: 0, color: '#1890ff' }}>Welcome Back</Title>
          <Text type="secondary">Sign in to your account</Text>
        </div>

        <Form
          name="login_form"
          layout="vertical"
          initialValues={{ remember: true }}
          onFinish={onFinish}
          size="large"
        >
          <Form.Item
            name="email"
            rules={[
              { required: true, message: 'Please input your Email!' },
              { type: 'email', message: 'Please enter a valid email!' }
            ]}
          >
            <Input prefix={<UserOutlined className="site-form-item-icon" />} placeholder="Email" />
          </Form.Item>

          <Form.Item
            name="password"
            rules={[{ required: true, message: 'Please input your Password!' }]}
          >
            <Input.Password
              prefix={<LockOutlined className="site-form-item-icon" />}
              placeholder="Password"
            />
          </Form.Item>

          <Form.Item>
            <Button 
              type="primary" 
              htmlType="submit" 
              style={{ width: '100%' }}
              loading={authStore.isLoading}
            >
              Log in
            </Button>
          </Form.Item>
          
          <div style={{ textAlign: 'center' }}>
            <Text type="secondary">Don't have an account? <a onClick={() => navigate('/register')} style={{cursor: 'pointer'}}>Register now!</a></Text>
          </div>
        </Form>
      </Card>
    </div>
  );
});

export default Login;
