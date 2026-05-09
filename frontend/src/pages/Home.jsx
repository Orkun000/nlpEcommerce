import React, { useEffect, useState } from 'react';
import { observer } from 'mobx-react-lite';
import { useStore } from '../stores/RootStore';
import { Card, Row, Col, Typography, Spin, Button, Result, Badge, Input } from 'antd';
import { ShoppingCartOutlined, FireOutlined, RobotOutlined } from '@ant-design/icons';
import { useNavigate } from 'react-router-dom'
const { Title, Paragraph, Text } = Typography;
const { Meta } = Card;
const { Search } = Input;

const getEmojiForProduct = (name) => {
  const lowerName = name.toLowerCase();
  if (lowerName.includes('laptop') || lowerName.includes('bilgisayar') || lowerName.includes('mac')) return '💻';
  if (lowerName.includes('headphone') || lowerName.includes('kulaklık')) return '🎧';
  if (lowerName.includes('keyboard') || lowerName.includes('klavye')) return '⌨️';
  if (lowerName.includes('mouse') || lowerName.includes('fare')) return '🖱️';
  if (lowerName.includes('phone') || lowerName.includes('telefon') || lowerName.includes('iphone')) return '📱';
  if (lowerName.includes('watch') || lowerName.includes('saat')) return '⌚';
  if (lowerName.includes('camera') || lowerName.includes('kamera')) return '📷';
  if (lowerName.includes('tv') || lowerName.includes('televizyon')) return '📺';
  if (lowerName.includes('game') || lowerName.includes('oyun')) return '🎮';
  return '📦';
};

const Home = observer(() => {
  const { productStore, cartStore } = useStore();
  const [searchQuery, setSearchQuery] = useState('');
  const navigate = useNavigate();

  useEffect(() => {
    productStore.fetchProducts();
  }, [productStore]);

  const handleSearch = (value) => {
    productStore.searchProducts(value);
  };

  if (productStore.error) {
    return (
      <Result
        status="500"
        title="500"
        subTitle={productStore.error}
        extra={<Button type="primary" onClick={() => productStore.fetchProducts()}>Try Again</Button>}
      />
    );
  }

  return (
    <div className="home-container" style={{ padding: '2rem 0' }}>
      <div style={{ textAlign: 'center', marginBottom: '2rem' }}>
        <Title level={1} style={{ marginBottom: '0.5rem' }}>
          Welcome to <span style={{ color: '#1890ff' }}>NLPEcommerce</span>
        </Title>
        <Paragraph type="secondary" style={{ fontSize: '1.1rem' }}>
          Discover the best premium tech products specially curated for you.
        </Paragraph>
      </div>

      <div style={{ maxWidth: '600px', margin: '0 auto 3rem auto' }}>
        <Search
          placeholder="Doğal dille arayın... (Örn: 500 TL altı kulaklıklar)"
          allowClear
          enterButton={
            <Button type="primary" icon={<RobotOutlined />}>
              Akıllı Ara
            </Button>
          }
          size="large"
          value={searchQuery}
          onChange={(e) => setSearchQuery(e.target.value)}
          onSearch={handleSearch}
          style={{ boxShadow: '0 4px 12px rgba(0,0,0,0.05)', borderRadius: '8px' }}
        />
      </div>

      <div className="featured-section" style={{ marginBottom: '2rem' }}>
        <Title level={3}>
          <FireOutlined style={{ color: '#fa8c16', marginRight: '8px' }} />
          {searchQuery ? 'Arama Sonuçları' : 'Trending Products'}
        </Title>

        {productStore.isLoading ? (
          <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', minHeight: '30vh' }}>
            <Spin size="large" tip="Yapay zeka ürünleri listeliyor..." />
          </div>
        ) : productStore.products.length === 0 ? (
          <Result title="Ürün bulunamadı" subTitle="Farklı bir arama yapmayı deneyin." />
        ) : (
          <Row gutter={[24, 24]} style={{ marginTop: '1.5rem' }}>
            {productStore.products.map(product => (
              <Col xs={24} sm={12} md={8} lg={6} key={product.id}>
                <Badge.Ribbon text="Hot" color="red" style={{ display: product.id % 2 === 0 ? 'block' : 'none' }}>
                  <Card
                    hoverable
                    className="product-card"
                    cover={
                      <div
                        onClick={() => navigate(`/products/${product.id}`)}
                        style={{
                          height: '200px',
                          display: 'flex',
                          alignItems: 'center',
                          justifyContent: 'center',
                          background: 'linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%)',
                          fontSize: '5rem',
                          borderTopLeftRadius: '8px',
                          borderTopRightRadius: '8px',
                          cursor: 'pointer'
                        }}
                      >
                        {getEmojiForProduct(product.name)}
                      </div>
                    }
                    actions={[
                      <Button
                        type="primary"
                        icon={<ShoppingCartOutlined />}
                        style={{ width: '80%' }}
                        onClick={() => cartStore.addToCart(product)}
                      >
                        Add to Cart
                      </Button>
                    ]}
                  >
                    <Meta
                      title={<span style={{ cursor: 'pointer' }} onClick={() => navigate(`/products/${product.id}`)}>{product.name}</span>}
                      description={
                        <div style={{ marginTop: '10px' }}>
                          <Text type="secondary" ellipsis={{ tooltip: product.description }}>
                            {product.description}
                          </Text>
                          <div style={{ marginTop: '10px', fontSize: '1.2rem', fontWeight: 'bold', color: '#1890ff' }}>
                            ${product.price ? product.price.toFixed(2) : '0.00'}
                          </div>
                        </div>
                      }
                    />
                  </Card>
                </Badge.Ribbon>
              </Col>
            ))}
          </Row>
        )}
      </div>
    </div>
  );
});

export default Home;
