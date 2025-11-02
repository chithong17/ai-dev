import React, { useState } from 'react';

const SearchFlightForm = () => {
    // State cho tab chức năng (Đặt Vé, Làm Thủ Tục, Đặt Chỗ Của Tôi)
    const [mainTab, setMainTab] = useState('book'); 
    
    // State cho loại vé (Một chiều, Khứ hồi, Đa chặng)
    const [tripType, setTripType] = useState('roundTrip'); // roundTrip, oneWay, multi

    // === 1. ĐỊNH NGHĨA CÁC INLINE STYLES ===
    const colors = {
        bluePrimary: '#1565C0',
        blueDark: '#36475A',
        greenSuccess: '#4CAF50',
        grayLight: '#F5F5F5',
        grayMedium: '#616161',
        border: '#E0E0E0'
    };

    const tabActiveStyle = { 
        backgroundColor: 'white', 
        color: colors.bluePrimary, 
        boxShadow: '0 -2px 4px rgba(0,0,0,0.1)' 
    };
    
    const mainTabInactiveStyle = { 
        backgroundColor: colors.blueDark, 
        color: 'white'
    }; 
    
    const mainFormStyle = { 
        backgroundColor: 'white', 
        padding: '25px', 
        borderRadius: '0 4px 4px 4px', 
        boxShadow: '0 4px 10px rgba(0,0,0,0.2)' 
    };
    
    // Style cho các ô input liền mạch
    const inputWrapperStyle = { 
        padding: '8px 12px', 
        flex: 1, 
        borderRight: `1px solid ${colors.border}`, 
        display: 'flex', 
        flexDirection: 'column', 
        justifyContent: 'center',
        minHeight: '75px',
        backgroundColor: 'white',
        transition: 'background-color 0.2s',
        cursor: 'pointer'
    };
    
    const inputStyleBase = {
        border: 'none', 
        padding: 0, 
        fontSize: '16px', 
        fontWeight: 'bold', 
        width: '100%', 
        backgroundColor: 'transparent',
        outline: 'none'
    };
    
    const smallTextStyle = { 
        fontSize: '11px', 
        color: colors.grayMedium, 
        fontWeight: '500' 
    };

    const searchButtonStyle = { 
        backgroundColor: colors.greenSuccess, 
        color: 'white', 
        fontWeight: 'bold', 
        border: 'none', 
        cursor: 'pointer',
        transition: 'background-color 0.2s',
        flexBasis: '150px', 
        alignSelf: 'center',
        height: '55px', 
        borderRadius: '4px',
        fontSize: '16px'
    };
    
    // --- Tab Nội dung "Đặt Vé" ---
    const BookingContent = (
        <div>
            {/* 1. KHU VỰC LỰA CHỌN LOẠI VÉ VÀ BẢNG */}
            <div style={{ display: 'flex', gap: '20px', marginBottom: '15px', fontSize: '14px', color: '#424242' }}>
                <div style={{ color: colors.grayMedium, marginRight: '15px' }}>Bảng Tiền | Bảng Điểm | Bảng Tiền & Điểm</div>
                
                {/* Radio Buttons */}
                <label style={{ display: 'flex', alignItems: 'center', cursor: 'pointer' }}>
                    <input type="radio" name="trip" checked={tripType === 'oneWay'} onChange={() => setTripType('oneWay')} style={{ marginRight: '5px' }} />
                    Một chiều
                </label>
                <label style={{ display: 'flex', alignItems: 'center', cursor: 'pointer' }}>
                    <input type="radio" name="trip" checked={tripType === 'roundTrip'} onChange={() => setTripType('roundTrip')} style={{ marginRight: '5px' }} />
                    Khứ hồi
                </label>
                <label style={{ display: 'flex', alignItems: 'center', cursor: 'pointer' }}>
                    <input type="radio" name="trip" checked={tripType === 'multi'} onChange={() => setTripType('multi')} style={{ marginRight: '5px' }} />
                    Đa chặng
                </label>
            </div>

            {/* 2. KHU VỰC INPUT CHÍNH (Liền mạch) */}
            <div style={{ display: 'flex', alignItems: 'stretch' }}>
                
                <div style={{ display: 'flex', flex: 2.5, border: `1px solid ${colors.border}`, borderRadius: '4px', overflow: 'hidden', position: 'relative' }}>
                    {/* Từ */}
                    <div style={{ ...inputWrapperStyle, borderRight: `1px solid ${colors.border}`, flex: 1, position: 'relative' }}>
                        <div style={smallTextStyle}>Từ</div>
                        <input type="text" value="Hà Nội" style={{ ...inputStyleBase, fontWeight: 'bold' }} readOnly />
                        <span style={{ fontSize: '11px', color: colors.greenSuccess, fontWeight: '600' }}>HAN</span>
                    </div>
                    
                    {/* Biểu tượng Hoán đổi */}
                    <div style={{ position: 'absolute', left: '50%', transform: 'translateX(-50%)', top: '50%', backgroundColor: 'white', borderRadius: '50%', border: '2px solid white', zIndex: 10, cursor: 'pointer' }}>
                        <span style={{ color: colors.bluePrimary, fontSize: '20px', display: 'block', transform: 'rotate(90deg)' }}>⇄</span>
                    </div>
                    
                    {/* Tới */}
                    <div style={{ ...inputWrapperStyle, borderLeft: `1px solid ${colors.border}`, flex: 1, borderRight: 'none' }}>
                        <div style={smallTextStyle}>Tới</div>
                        <input type="text" placeholder="Điểm đến" style={inputStyleBase} />
                        <span style={{ fontSize: '11px', color: colors.grayMedium }}>Điểm đến</span>
                    </div>
                </div>

                {/* 2.2. Ngày đi / Ngày về / Hành khách */}
                <div style={{ display: 'flex', flex: 2, marginLeft: '10px', border: `1px solid ${colors.border}`, borderRadius: '4px', overflow: 'hidden' }}>
                    
                    {/* Ngày đi */}
                    <div style={{ ...inputWrapperStyle, borderRight: `1px solid ${colors.border}`, flex: 1 }}>
                        <div style={smallTextStyle}>Ngày đi</div>
                        <input type="text" placeholder="Chọn ngày" style={inputStyleBase} />
                    </div>

                    {/* Ngày về (Chỉ hiện khi Khứ hồi) */}
                    {tripType === 'roundTrip' && (
                        <div style={{ ...inputWrapperStyle, borderRight: `1px solid ${colors.border}`, flex: 1 }}>
                            <div style={smallTextStyle}>Ngày về</div>
                            <input type="text" placeholder="Chọn ngày" style={inputStyleBase} />
                        </div>
                    )}
                    
                    {/* Hành khách */}
                    <div style={{ ...inputWrapperStyle, borderRight: 'none', flex: 0.8, minWidth: '90px' }}>
                        <div style={smallTextStyle}>Hành khách</div>
                        <input type="number" defaultValue="1" min="1" style={inputStyleBase} />
                    </div>
                </div>

                {/* 2.3. NÚT TÌM KIẾM */}
                <button type="submit" style={{ ...searchButtonStyle, marginLeft: '10px', width: '150px' }}>
                    Tìm Chuyến Bay
                </button>
            </div>
            
            
        </div>
    );
    // --- Kết thúc Booking Content ---


    return (
        <div style={{ maxWidth: '1000px', margin: '0 auto', transform: 'translateY(50px)' }}> 
            {/* KHU VỰC TAB CHỨC NĂNG CHÍNH */}
            <div style={{ display: 'flex', fontSize: '16px', fontWeight: '600' }}>
                <button 
                    onClick={() => setMainTab('book')}
                    style={{ ...mainTabInactiveStyle, ... (mainTab === 'book' ? tabActiveStyle : {}), padding: '10px 20px', border: 'none', borderTopLeftRadius: '6px', borderTopRightRadius: '6px', cursor: 'pointer', transition: 'background-color 0.2s' }}
                >
                     Đặt Vé
                </button>
                <button 
                    onClick={() => setMainTab('checkin')}
                    style={{ ...mainTabInactiveStyle, ... (mainTab === 'checkin' ? tabActiveStyle : {}), padding: '10px 20px', border: 'none', borderTopLeftRadius: '6px', borderTopRightRadius: '6px', cursor: 'pointer', transition: 'background-color 0.2s', marginLeft: '2px' }}
                >
                     Làm Thủ Tục
                </button>
                <button 
                    onClick={() => setMainTab('mybooking')}
                    style={{ ...mainTabInactiveStyle, ... (mainTab === 'mybooking' ? tabActiveStyle : {}), padding: '10px 20px', border: 'none', borderTopLeftRadius: '6px', borderTopRightRadius: '6px', cursor: 'pointer', transition: 'background-color 0.2s', marginLeft: '2px' }}
                >
                     Đặt Chỗ Của Tôi
                </button>
                {/* Thanh dài lấp đầy */}
                <div style={{ flexGrow: 1, backgroundColor: colors.blueDark, borderTopRightRadius: '6px' }}></div>
            </div>

            {/* NỘI DUNG FORM */}
            <form onSubmit={(e) => e.preventDefault()} style={mainFormStyle}>
                {mainTab === 'book' && BookingContent}
                {/* Thêm nội dung cho 'checkin' và 'mybooking' nếu cần */}
            </form>
        </div>
    );
};

export default SearchFlightForm;