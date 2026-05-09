import React from 'react';
import { Form, Input, Button, Card, Typography, message } from 'antd';
import { UserOutlined, LockOutlined, MailOutlined } from '@ant-design/icons';
import { observer } from 'mobx-react-lite';
import { useStore } from '../stores/RootStore';
import { useNavigate } from 'react-router-dom';

const { Title, Text } = Typography;

const Register = observer(() => {
  const { authStore } = useStore();
  const navigate = useNavigate();

  const onFinish = async (values) => {
    const success = await authStore.register(values.firstName, values.lastName, values.email, values.password);
    if (success) {
      message.success('Registration successful! You can now log in.');
      navigate('/login');
    } else {
      message.error(authStore.error || 'Registration failed');
    }
  };

  return (
    <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', minHeight: '70vh' }}>
      <Card 
        style={{ width: 450, boxShadow: '0 4px 12px rgba(0,0,0,0.08)', borderRadius: '12px' }}
        bordered={false}
      >
        <div style={{ textAlign: 'center', marginBottom: '2rem' }}>
          <Title level={2} style={{ margin: 0, color: '#1890ff' }}>Create an Account</Title>
          <Text type="secondary">Join NLPEcommerce today</Text>
        </div>

        <Form
          name="register_form"
          layout="vertical"
          onFinish={onFinish}
          size="large"
        >
          <div style={{ display: 'flex', gap: '10px' }}>
            <Form.Item
              name="firstName"
              style={{ flex: 1 }}
              rules={[{ required: true, message: 'First name is required!' }]}
            >
              <Input prefix={<UserOutlined className="site-form-item-icon" />} placeholder="First Name" />
            </Form.Item>

            <Form.Item
              name="lastName"
              style={{ flex: 1 }}
              rules={[{ required: true, message: 'Last name is required!' }]}
            >
              <Input prefix={<UserOutlined className="site-form-item-icon" />} placeholder="Last Name" />
            </Form.Item>
          </div>

          <Form.Item
            name="email"
            rules={[
              { required: true, message: 'Email is required!' },
              { type: 'email', message: 'Please enter a valid email!' }
            ]}
          >
            <Input prefix={<MailOutlined className="site-form-item-icon" />} placeholder="Email Address" />
          </Form.Item>

          <Form.Item
            name="password"
            rules={[
              { required: true, message: 'Password is required!' },
              { min: 6, message: 'Password must be at least 6 characters!' }
            ]}
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
              Register
            </Button>
          </Form.Item>
          
          <div style={{ textAlign: 'center' }}>
            <Text type="secondary">Already have an account? <a onClick={() => navigate('/login')} style={{cursor: 'pointer'}}>Log in!</a></Text>
          </div>
        </Form>
      </Card>
    </div>
  );
});

export default Register;
