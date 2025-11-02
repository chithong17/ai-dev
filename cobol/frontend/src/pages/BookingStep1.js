import React, {useState} from 'react';
import api from '../lib/api';
import BookingQuote from '../components/BookingQuote';
import {useNavigate} from 'react-router-dom';

export default function BookingStep1(){
  const [clientId, setClientId] = useState('');
  const [flightNum, setFlightNum] = useState('');
  const [date, setDate] = useState('');
  const [passengerCount, setPassengerCount] = useState(1);
  const [quote, setQuote] = useState(null);
  const navigate = useNavigate();

  const research = async () => {
    try {
      const resp = await api.post('/api/bookings/validate-step1', { clientId, flightNum, date, passengerCount });
      setQuote(resp.data.bookingQuote);
    } catch (err) {
      alert(err?.response?.data?.error || 'Error');
    }
  }

  const insertPassengers = () => {
    // Navigate to step 2 carrying bookingQuote
    navigate('/booking-step2', { state: { bookingQuote: quote } });
  }

  return (
    <div className="terminal-page">
      <h2>Booking — Step 1</h2>
      <div>
        <label>CLIIDI</label>
        <input value={clientId} onChange={e=>setClientId(e.target.value)} />
        <label>FNUMI</label>
        <input value={flightNum} onChange={e=>setFlightNum(e.target.value)} />
        <label>FDATEI</label>
        <input value={date} onChange={e=>setDate(e.target.value)} />
        <label>PASSNI</label>
        <input type="number" value={passengerCount} onChange={e=>setPassengerCount(Number(e.target.value))} />
      </div>
      <div style={{marginTop:12}}>
        <button onClick={research}>Research (F11)</button>
        <button onClick={insertPassengers}>Insert Passengers (F12)</button>
      </div>
      <BookingQuote quote={quote} />
    </div>
  )
}
