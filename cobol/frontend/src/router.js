import React from 'react';
import { Routes, Route } from 'react-router-dom';
import LoginPage from './pages/LoginPage';
// THÊM CÁC IMPORT NÀY
import SearchFlightPage from './pages/SearchFlightPage';
import BookingStep1 from './pages/BookingStep1';
// (Chúng ta cũng nên thêm một trang cho BookingStep2, dựa trên sell2-1.png)

export default function Router(){
  return (
    <Routes>
      <Route path="/" element={<LoginPage/>} />
      {/* THÊM CÁC ROUTE NÀY */}
      <Route path="/search" element={<SearchFlightPage />} />
      <Route path="/booking-step1" element={<BookingStep1 />} />
      {/* <Route path="/booking-step2" element={<BookingStep2 />} /> */}
    </Routes>
  )
}
