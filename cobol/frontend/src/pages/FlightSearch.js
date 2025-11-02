import React, { useState } from 'react';
import axios from 'axios';

const FlightSearch = () => {
    const [origin, setOrigin] = useState('');
    const [destination, setDestination] = useState('');
    const [date, setDate] = useState('');
    const [flights, setFlights] = useState([]);
    const [message, setMessage] = useState('');

    const handleSearch = async (e) => {
        e.preventDefault();
        try {
            const response = await axios.get('/flights', {
                params: { origin, destination, date },
            });
            setFlights(response.data);
            setMessage(response.data.length ? '' : 'No flights found');
        } catch (error) {
            setMessage('Error searching for flights');
        }
    };

    return (
        <div>
            <h1>Flight Search</h1>
            <form onSubmit={handleSearch}>
                <div>
                    <label>Origin:</label>
                    <input
                        type="text"
                        value={origin}
                        onChange={(e) => setOrigin(e.target.value)}
                    />
                </div>
                <div>
                    <label>Destination:</label>
                    <input
                        type="text"
                        value={destination}
                        onChange={(e) => setDestination(e.target.value)}
                    />
                </div>
                <div>
                    <label>Date:</label>
                    <input
                        type="date"
                        value={date}
                        onChange={(e) => setDate(e.target.value)}
                    />
                </div>
                <button type="submit">Search</button>
            </form>
            {message && <p>{message}</p>}
            <ul>
                {flights.map((flight, index) => (
                    <li key={index}>
                        {flight.flightNumber} - {flight.origin} to {flight.destination} on {flight.date}
                    </li>
                ))}
            </ul>
        </div>
    );
};

export default FlightSearch;