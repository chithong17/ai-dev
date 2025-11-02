import React from "react";
import { Routes, Route, Navigate } from "react-router-dom";
import LoginPage from "./pages/LoginPage";
import RegisterPage from "./pages/RegisterPage";
import BookingStep1 from "./pages/BookingStep1"; // ⬅ thêm dòng này
import BookingStep2 from "./pages/BookingStep2";
import BookingSummary from "./pages/BookingSummary";
export default function Router() {
  return (
    <Routes>
      <Route path="/login" element={<LoginPage />} />
      <Route path="/register" element={<RegisterPage />} />
      <Route path="/booking-step1" element={<BookingStep1 />} /> {/* ⬅ thêm dòng này */}
      <Route path="/booking-step2" element={<BookingStep2 />} />
      <Route path="/booking-summary" element={<BookingSummary />} />
      <Route path="*" element={<Navigate to="/login" replace />} />
    </Routes>
  );
}
