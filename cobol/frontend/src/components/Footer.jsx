import React from 'react';

const Footer = () => {
    
    // 1. ĐỊNH NGHĨA CÁC STYLE CHUNG (Inline Styles)
    const footerStyle = {
        width: '100%',
        backgroundColor: 'white',
        color: '#424242',
        paddingTop: '60px',
        paddingBottom: '30px',
        borderTop: '8px solid #F0F0F0', // Border tách biệt
    };

    const containerStyle = {
        maxWidth: '1200px',
        margin: '0 auto',
        padding: '0 20px',
    };

    const gridContainerStyle = {
        display: 'grid',
        gridTemplateColumns: 'repeat(5, 1fr)', // 5 cột cho màn hình lớn
        gap: '30px',
        paddingBottom: '40px',
        borderBottom: '1px solid #E0E0E0',
    };

    const headingStyle = {
        fontSize: '16px',
        fontWeight: '700',
        marginBottom: '15px',
        color: '#1565C0', // Màu xanh dương đậm cho tiêu đề
        textTransform: 'uppercase',
    };

    const linkStyle = {
        display: 'block',
        fontSize: '13px',
        color: '#616161',
        textDecoration: 'none',
        marginBottom: '8px',
        transition: 'color 0.2s',
    };

    const qrBlockStyle = {
        backgroundColor: '#F7F7F7',
        border: '1px solid #E0E0E0',
        borderRadius: '8px',
        padding: '12px',
        display: 'flex',
        alignItems: 'flex-start',
        marginBottom: '10px',
        boxShadow: '0 2px 5px rgba(0,0,0,0.05)',
    };
    
    const qrTextStyle = {
        marginLeft: '10px',
    };

    const qrTitleStyle = {
        fontWeight: '700',
        fontSize: '14px',
        color: '#1B5E20', // Màu xanh lá cho ứng dụng
    };

    // 2. TẠO COMPONENT LINK để dễ dàng thêm hiệu ứng
    const FooterLink = ({ href, children }) => (
        <a 
            href={href} 
            style={linkStyle}
            onMouseOver={e => e.currentTarget.style.color = '#1565C0'} // Hiệu ứng hover
            onMouseOut={e => e.currentTarget.style.color = '#616161'}
        >
            {children}
        </a>
    );

    return (
        <footer style={footerStyle}>
            <div style={containerStyle}>
                
                {/* PHẦN CHÍNH: 4 CỘT THÔNG TIN + 1 KHỐI QR CODE */}
                <div style={gridContainerStyle}>
                    
                    {/* Cột 1: Cobol Airlines */}
                    <div>
                        <h3 style={headingStyle}>Cobol Airlines</h3>
                        <FooterLink href="#">Thông Tin Về Cobol</FooterLink>
                        <FooterLink href="#">Thông Cáo Báo Chí</FooterLink>
                        <FooterLink href="#">Tin Tức</FooterLink>
                        <FooterLink href="#">Giải Thưởng</FooterLink>
                        <FooterLink href="#">Đội Bay</FooterLink>
                        <FooterLink href="#">Cơ Hội Nghề Nghiệp</FooterLink>
                        <FooterLink href="#">Dành Cho Đại Lý</FooterLink>
                    </div>

                    {/* Cột 2: Pháp Lý */}
                    <div>
                        <h3 style={headingStyle}>Pháp Lý</h3>
                        <FooterLink href="#">Bảo Mật Thông Tin</FooterLink>
                        <FooterLink href="#">Điều Lệ Vận Chuyển</FooterLink>
                        <FooterLink href="#">Điều Khoản Sử Dụng Website</FooterLink>
                        <FooterLink href="#">Điều Khoản Sử Dụng Chức Năng Đặt Vé</FooterLink>
                        <FooterLink href="#">Điều Khoản Sử Dụng Cookies</FooterLink>
                    </div>

                    {/* Cột 3: Thông Tin Liên Hệ */}
                    <div>
                        <h3 style={headingStyle}>Thông Tin Liên Hệ</h3>
                        <FooterLink href="#">Liên Hệ</FooterLink>
                        <FooterLink href="#">Hoá Đơn VAT</FooterLink>
                        <p style={{fontSize: '13px', color: '#616161', marginTop: '15px'}}>Hotline: 1900 1001</p>
                    </div>

                    {/* Cột 4: Trợ Giúp */}
                    <div>
                        <h3 style={headingStyle}>Trợ Giúp</h3>
                        <FooterLink href="#">Chính Sách Bảo Vệ Khách</FooterLink>
                        <FooterLink href="#">Câu Hỏi Thường Gặp</FooterLink>
                        <FooterLink href="#">Trung Tâm Trợ Giúp</FooterLink>
                    </div>
                    
                    {/* KHỐI 5: QR CODES */}
                    <div style={{gridColumn: 'span 1', display: 'flex', flexDirection: 'column', paddingTop: '15px'}}>
                        
                        {/* Khối Ứng dụng (Màu Xanh Lá) */}
                        <div style={{...qrBlockStyle, border: '1px solid #A5D6A7'}}>
                            <div style={{width: '60px', height: '60px', backgroundColor: '#E8F5E9', display: 'flex', alignItems: 'center', justifyContent: 'center'}}>
                                [QR]
                            </div>
                            <div style={qrTextStyle}>
                                <h4 style={qrTitleStyle}>Cài Đặt Ứng Dụng</h4>
                                <p style={{fontSize: '11px', color: '#4CAF50', marginTop: '3px'}}>Quét Mã Ngay để Cài Đặt Từ Các Cửa Hàng Ứng Dụng!</p>
                            </div>
                        </div>

                        {/* Khối Sticker/Nhãn dán (Màu Tím Nhạt) */}
                        <div style={{...qrBlockStyle, border: '1px solid #CE93D8'}}>
                            <div style={{width: '60px', height: '60px', backgroundColor: '#F3E5F5', display: 'flex', alignItems: 'center', justifyContent: 'center'}}>
                                [QR]
                            </div>
                            <div style={qrTextStyle}>
                                <h4 style={{...qrTitleStyle, color: '#8E24AA'}}>Cài Đặt Nhãn Dán</h4>
                                <p style={{fontSize: '11px', color: '#8E24AA', marginTop: '3px'}}>Quét mã ngay để sở hữu bộ nhãn dán Viber!</p>
                            </div>
                        </div>
                    </div>

                </div>

                {/* PHẦN ĐÁY (BOTTOM) - TÁI TẠO CÁC DANH MỤC DƯỚI CÙNG */}
                <div style={{paddingTop: '20px', display: 'flex', flexWrap: 'wrap', justifyContent: 'space-between', alignItems: 'center', fontSize: '13px', color: '#616161'}}>
                    <span style={{fontWeight: '700', color: '#1565C0', marginRight: '15px', marginBottom: '8px'}}>Cam Nang Du Lịch:</span>
                    <a href="#" style={{...linkStyle, marginBottom: '8px'}}>Hà Nội - TP. Hồ Chí Minh</a>
                    <a href="#" style={{...linkStyle, marginBottom: '8px'}}>Hà Nội - Đà Lạt</a>
                    <a href="#" style={{...linkStyle, marginBottom: '8px'}}>TP. Hồ Chí Minh - Đà Lạt</a>
                    <span style={{marginLeft: 'auto', fontSize: '12px'}}>© 2025 Cobol Airlines</span>
                </div>
            </div>
        </footer>
    );
};

export default Footer;