import React from 'react';
// Giả định: npm install framer-motion
import { motion } from 'framer-motion'; 

// IMPORT DEFAULT (KHÔNG DẤU NGOẶC {}) cho tất cả các Component con
import SearchFlightForm from '../components/SearchFlightForm'; 
import Header from '../components/Header'; 
import Footer from '../components/Footer'; 

// --- Thiết lập Hiệu ứng Động Cơ bản (Giữ nguyên Framer Motion) ---
const containerVariants = {
    hidden: { opacity: 0 },
    visible: { 
        opacity: 1, 
        transition: { 
            staggerChildren: 0.15 
        }
    },
};

const itemVariants = {
    hidden: { opacity: 0, y: 50, scale: 0.95 }, 
    visible: { opacity: 1, y: 0, scale: 1, transition: { type: "spring", stiffness: 150, damping: 20 } },
};

const HomePage = () => {
    
    // Component Feature Card với CSS Class
    const FeatureCard = ({ icon, title, description }) => (
        <motion.div 
            variants={itemVariants} 
            className="feature-card-style" // Class CSS sẽ định nghĩa shadow, border, hover
        >
            <i className={`feature-icon ${icon}`}></i>
            <h3 className="feature-title">{title}</h3>
            <p className="feature-description">{description}</p>
        </motion.div>
    );

    return (
        <motion.div 
            className="homepage-wrapper"
            initial="hidden"
            animate="visible"
            variants={containerVariants}
            style={{ minHeight: '100vh', display: 'flex', flexDirection: 'column' }}
        >
            
            {/* 1. THANH ĐIỀU HƯỚNG */}
            <motion.header variants={itemVariants}>
                <Header /> 
            </motion.header>

            {/* 2. KHU VỰC CHÍNH (HERO SECTION) */}
            <section className="hero-section" style={{ 
                backgroundImage: 'url(/images/placeholder-flight.jpg)', // Tùy chọn: đặt hình nền qua class
                backgroundSize: 'cover', 
                backgroundPosition: 'center',
                flexGrow: 1, 
                display: 'flex', 
                alignItems: 'center', 
                justifyContent: 'center', 
                position: 'relative' 
            }}>
                <div className="hero-content-overlay" style={{ position: 'relative', zIndex: 10, padding: '20px', width: '100%', maxWidth: '960px', textAlign: 'center' }}>
                    
                    {/* Tiêu đề chính */}
                    <motion.h1 
                        initial={{ opacity: 0, y: -20 }}
                        animate={{ opacity: 1, y: 0 }}
                        transition={{ duration: 0.8, type: "spring", stiffness: 100 }}
                        className="main-title" 
                        style={{ fontSize: '3.5rem', fontWeight: 900, marginBottom: '15px', textShadow: '2px 2px 4px rgba(0,0,0,0.7)' }}
                    >
                        Khám phá Thế giới. Bắt đầu từ Cobol Airlines.
                    </motion.h1>
                    
                    {/* Mô tả phụ */}
                    <motion.p 
                        initial={{ opacity: 0 }}
                        animate={{ opacity: 1 }}
                        transition={{ delay: 0.5, duration: 0.6 }}
                        className="sub-title"
                        style={{ fontSize: '1.25rem', fontWeight: 300, marginBottom: '60px', textShadow: '1px 1px 3px rgba(0,0,0,0.5)' }}
                    >
                        Đặt vé máy bay đơn giản, an toàn và giá tốt nhất chỉ trong vài bước.
                    </motion.p>

                    {/* FORM TÌM KIẾM */}
                    <motion.div 
                        initial={{ scale: 0.8, opacity: 0 }}
                        animate={{ scale: 1, opacity: 1 }}
                        transition={{ delay: 0.7, duration: 0.6, type: "spring", stiffness: 120 }}
                        className="search-form-container"
                        style={{ margin: '0 auto', maxWidth: '900px', transform: 'translateY(80px)' }} // Kéo Form lên cao
                    >
                        <SearchFlightForm /> 
                    </motion.div>

                </div>
                
                {/* Lớp phủ Gradient mờ cho hình nền */}
                <div style={{ position: 'absolute', inset: 0, background: 'linear-gradient(to top, rgba(0, 0, 0, 0.6), transparent)' }}></div>
                
            </section>

            {/* 3. PHẦN ĐẶC TRƯNG & LÒNG TIN */}
            <motion.main 
                className="main-content-area"
                style={{ paddingTop: '100px', paddingBottom: '80px', backgroundColor: '#F9FAFB', flexGrow: 1, padding: '20px' }}
            >
                
                <h2 className="section-heading" style={{ fontSize: '2rem', fontWeight: 800, textAlign: 'center', marginBottom: '60px', color: '#1F2937' }}>
                    Tại sao Cobol Airlines là lựa chọn số 1?
                </h2>
                
                <div className="features-grid" style={{ display: 'grid', gridTemplateColumns: 'repeat(3, 1fr)', gap: '40px', maxWidth: '1280px', margin: '0 auto' }}>
                    
                    {/* Thẻ 1: Giá tốt nhất */}
                    <FeatureCard icon="icon-price" title="Giá tốt nhất" description="Đảm bảo giá vé cạnh tranh nhất trên thị trường mà không có phí ẩn." />

                    {/* Thẻ 2: Thanh toán An toàn */}
                    <FeatureCard icon="icon-security" title="Bảo mật Tối đa" description="Hệ thống bảo mật 256-bit chuẩn quốc tế, bảo vệ tuyệt đối thông tin cá nhân và giao dịch." />

                    {/* Thẻ 3: Hỗ trợ 24/7 */}
                    <FeatureCard icon="icon-support" title="Hỗ trợ Ưu tiên" description="Đội ngũ chuyên nghiệp luôn sẵn sàng hỗ trợ bạn 24/7 qua điện thoại, email và chat trực tuyến." />
                    
                </div>
                
            </motion.main>

            {/* 4. FOOTER */}
            <Footer /> 

        </motion.div>
    );
};

export default HomePage;