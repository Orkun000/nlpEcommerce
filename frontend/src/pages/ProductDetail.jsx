import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { observer } from 'mobx-react-lite';
import { useStore } from '../stores/RootStore';
import { Typography, Row, Col, Button, Spin, Tag, Card, Divider, Result } from 'antd';
import { ShoppingCartOutlined, ArrowLeftOutlined, StarFilled, ThunderboltFilled } from '@ant-design/icons';
import api from '../services/api';

const { Title, Text, Paragraph } = Typography;

const getEmojiForProduct = (name) => {
  const lowerName = name?.toLowerCase() || '';
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

const ProductDetail = observer(() => {
  const { id } = useParams();
  const navigate = useNavigate();
  const { cartStore } = useStore();
  const [product, setProduct] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchProduct = async () => {
      try {
        const response = await api.get(`/products/${id}`);
        setProduct(response.data.data);
      } catch (err) {
        setError("Ürün bulunamadı veya bir hata oluştu.");
      } finally {
        setLoading(false);
      }
    };
    fetchProduct();
  }, [id]);

  if (loading) {
    return (
      <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', minHeight: '60vh' }}>
        <Spin size="large" tip="Ürün detayları yükleniyor..." />
      </div>
    );
  }

  if (error || !product) {
    return (
      <Result
        status="404"
        title="404"
        subTitle={error}
        extra={<Button type="primary" onClick={() => navigate('/')}>Ana Sayfaya Dön</Button>}
      />
    );
  }

  return (
    <div style={{ padding: '2rem 0' }}>
      <Button 
        type="link" 
        icon={<ArrowLeftOutlined />} 
        onClick={() => navigate(-1)}
        style={{ marginBottom: '1rem', paddingLeft: 0 }}
      >
        Geri Dön
      </Button>

      <Card bordered={false} style={{ boxShadow: '0 8px 24px rgba(0,0,0,0.05)', borderRadius: '12px' }}>
        <Row gutter={[48, 24]}>
          <Col xs={24} md={10}>
            <div style={{ 
              height: '400px', 
              display: 'flex', 
              alignItems: 'center', 
              justifyContent: 'center', 
              background: 'linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%)',
              fontSize: '10rem',
              borderRadius: '12px'
            }}>
              {getEmojiForProduct(product.name)}
            </div>
          </Col>
          <Col xs={24} md={14}>
            <div style={{ display: 'flex', flexDirection: 'column', height: '100%' }}>
              <div>
                <div style={{ display: 'flex', alignItems: 'center', gap: '10px', marginBottom: '10px' }}>
                  {product.categoryName && <Tag color="blue">{product.categoryName}</Tag>}
                  {product.freeShipping && <Tag color="green" icon={<ThunderboltFilled />}>Ücretsiz Kargo</Tag>}
                </div>
                
                <Title level={2} style={{ marginTop: 0, marginBottom: '0.5rem' }}>{product.name}</Title>
                
                <div style={{ display: 'flex', alignItems: 'center', gap: '8px', marginBottom: '1rem' }}>
                  <StarFilled style={{ color: '#fa8c16' }} />
                  <Text strong>{product.rating || '5.0'}</Text>
                  <Text type="secondary">({product.reviewCount || Math.floor(Math.random() * 100) + 10} değerlendirme)</Text>
                </div>
                
                <Title level={2} style={{ color: '#1890ff', margin: '0 0 1.5rem 0' }}>
                  ${product.price ? product.price.toFixed(2) : '0.00'}
                </Title>
                
                <Paragraph style={{ fontSize: '1.1rem', lineHeight: '1.8' }}>
                  {product.description || 'Bu ürün için detaylı bir açıklama bulunmuyor.'}
                </Paragraph>
              </div>

              <Divider />
              
              <div style={{ marginTop: 'auto' }}>
                <div style={{ display: 'flex', alignItems: 'center', gap: '20px', marginBottom: '20px' }}>
                  <Text>Stok Durumu: </Text>
                  <Tag color={product.stock > 0 ? "success" : "error"}>
                    {product.stock > 0 ? `${product.stock} adet stokta` : 'Stokta Yok'}
                  </Tag>
                </div>
                
                <Button 
                  type="primary" 
                  size="large" 
                  icon={<ShoppingCartOutlined />} 
                  style={{ width: '100%', height: '50px', fontSize: '1.1rem' }}
                  onClick={() => cartStore.addToCart(product)}
                  disabled={product.stock === 0}
                >
                  Sepete Ekle
                </Button>
              </div>
            </div>
          </Col>
        </Row>
      </Card>
    </div>
  );
});

export default ProductDetail;
