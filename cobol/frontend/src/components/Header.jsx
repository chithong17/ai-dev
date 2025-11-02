import React from 'react';
import { Link } from 'react-router-dom';

const Header = () => {
    
    // ĐỊNH NGHĨA STYLES INLINE ĐỂ KHẮC PHỤC LỖI BỐ CỤC
    const headerStyle = {
        backgroundColor: 'white',
        borderBottom: '1px solid #E0E0E0',
        padding: '12px 20px',
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'space-between',
        width: '100%',
        boxShadow: '0 2px 4px rgba(0,0,0,0.1)',
        position: 'sticky',
        top: 0,
        zIndex: 100
    };

    const linkStyle = {
        color: '#424242', 
        textDecoration: 'none',
        fontSize: '16px',
        fontWeight: '500', 
        padding: '0 10px',
        transition: 'color 0.2s'
    };
    
    const utilityLinkStyle = {
        color: '#0D47A1', 
        textDecoration: 'none',
        fontSize: '14px',
        fontWeight: '600',
        marginLeft: '15px'
    };

    return (
        <header style={headerStyle}>
            
            {/* Cột 1: LOGO */}
            <div style={{ flex: 1, minWidth: '200px' }}>
                <Link to="/" style={{ textDecoration: 'none', fontWeight: '900', fontSize: '24px' }}>
                    <span style={{ color: '#1565C0' }}>COBOL</span>
                    <span style={{ color: '#4CAF50' }}> Airways</span>
                </Link>
            </div>
            
            {/* Cột 2: THANH ĐIỀU HƯỚNG CHÍNH */}
            <nav style={{ display: 'flex', justifyContent: 'center', flex: 2 }}>
                <Link to="/" style={linkStyle}>Khám phá</Link>
                <Link to="/booking-step1" style={linkStyle}>Đặt vé</Link>
                <Link to="#" style={linkStyle}>Thông tin hành trình</Link>
                <Link to="#" style={linkStyle}>Cobol Club</Link>
            </nav>
            
            {/* Cột 3: TIỆN ÍCH & AUTH */}
            <div style={{ flex: 1, display: 'flex', justifyContent: 'flex-end', alignItems: 'center' }}>
                
                {/* NHÓM TIỆN ÍCH PHÍA TRÊN (Giống Bamboo) */}
                <div style={{ display: 'flex', alignItems: 'center', fontSize: '12px', marginRight: '15px', color: '#616161' }}>
                    
                    <span style={{ color: '#D32F2F', display: 'flex', alignItems: 'center', cursor: 'pointer' }}>
                         Việt Nam - Tiếng Việt
                    </span>
                </div>

                {/* Dấu phân cách dọc */}
                <div style={{ height: '16px', width: '1px', backgroundColor: '#E0E0E0', marginRight: '15px' }}></div>
                
                {/* NHÓM AUTH (ĐĂNG NHẬP/ĐĂNG KÝ) */}
                <div style={{ display: 'flex', alignItems: 'center', fontWeight: '600' }}>
                    <Link to="/login" style={utilityLinkStyle}>
                        Đăng nhập
                    </Link>
                    
                    {/* ✅ ĐÃ THÊM: NÚT ĐĂNG KÝ */}
                    <Link 
                        to="/register" 
                        style={{ ...utilityLinkStyle, marginLeft: '10px', fontSize: '16px' }}
                    >
                        Đăng ký
                    </Link>
                    
                    {/* ✅ ĐÃ THÊM: BIỂU TƯỢNG USER */}
                    <Link 
                        to="/register" 
                        style={{ ...utilityLinkStyle, marginLeft: '10px', fontSize: '20px', color: '#1565C0' }}
                    >
                    </Link>
                </div>

            </div>
        </header>
    );
};

export default Header;