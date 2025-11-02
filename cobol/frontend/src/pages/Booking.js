import React, { useState } from 'react';
import axios from 'axios';

const Booking = () => {
    const [flightId, setFlightId] = useState('');
    const [customerId, setCustomerId] = useState('');
    const [seatNumber, setSeatNumber] = useState('');
    const [message, setMessage] = useState('');

    const handleBooking = async (e) => {
        e.preventDefault();
        try {
            const response = await axios.post('/bookings', { flightId, customerId, seatNumber });
            setMessage(response.data.message);
        } catch (error) {
            setMessage(error.response?.data?.message || 'Booking failed');
        }
    };

    return (
        <div>
            <h1>Booking</h1>
            <form onSubmit={handleBooking}>
                <div>
                    <label>Flight ID:</label>
                    <input
                        type="text"
                        value={flightId}
                        onChange={(e) => setFlightId(e.target.value)}
                    />
                </div>
                <div>
                    <label>Customer ID:</label>
                    <input
                        type="text"
                        value={customerId}
                        onChange={(e) => setCustomerId(e.target.value)}
                    />
                </div>
                <div>
                    <label>Seat Number:</label>
                    <input
                        type="text"
                        value={seatNumber}
                        onChange={(e) => setSeatNumber(e.target.value)}
                    />
                </div>
                <button type="submit">Book</button>
            </form>
            {message && <p>{message}</p>}
        </div>
    );
};

export default Booking;