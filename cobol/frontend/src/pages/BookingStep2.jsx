import React from "react";
import { useLocation, useNavigate } from "react-router-dom";
import api from "../lib/api";

export default function BookingStep2() {
  const { state } = useLocation();
  const navigate = useNavigate();

  const quote = state?.bookingQuote;

  const confirmBooking = async () => {
    try {
      const req = {
        clientId: quote.clientId,
        flightNum: quote.flightNum,
        //   date: quote.flightDate ? quote.flightDate.slice(0, 10) : null,
        date: "2025-11-05",
        passengerCount: quote.passengerCount,
      };

      console.log("📤 Sending confirm payload:", req); // 👈 thêm dòng này
      const resp = await api.post("/api/bookings/confirm", req);
      alert(resp.data.message || "Booking confirmed!");
      navigate(`/booking-summary?clientId=${quote.clientId}`);
    } catch (err) {
      console.error("❌ Confirm failed:", err.response?.data); // 👈 thêm dòng này
      alert(err?.response?.data?.error || "Error confirming booking");
    }
  };

  if (!quote) return <div>No booking data found. Please go back.</div>;

  return (
    <div className="terminal-page">
      <h2>Booking — Step 2</h2>
      <div>
        <p>Client: {quote.clientId}</p>
        <p>Flight: {quote.flightNum}</p>
        <p>Date: {quote.flightDate}</p>
        <p>Passengers: {quote.passengerCount}</p>
        <p>Total Price: {quote.totalPrice}</p>
      </div>
      <button onClick={confirmBooking}>Confirm Booking</button>
    </div>
  );
}
