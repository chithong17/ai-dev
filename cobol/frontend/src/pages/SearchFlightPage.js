import React, {useState} from 'react';
import api from '../lib/api';
import FlightResults from '../components/FlightResults';

export default function SearchFlightPage(){
  const [date, setDate] = useState('');
  const [flightNum, setFlightNum] = useState('');
  const [dep, setDep] = useState('');
  const [arr, setArr] = useState('');
  const [rows, setRows] = useState([]);

  const search = async (e) => {
    e && e.preventDefault();
    try {
      const resp = await api.get('/api/flights/search', { params: { date, flightNum, depAirport: dep, arrAirport: arr } });
      setRows(resp.data);
    } catch (err) { console.error(err); }
  }

  return (
    <div className="terminal-page">
      <h2>Search Flights</h2>
      <form onSubmit={search}>
        <label>FDATEI</label>
        <input value={date} onChange={e=>setDate(e.target.value)} placeholder="YYYY-MM-DD" />
        <label>FNUMI</label>
        <input value={flightNum} onChange={e=>setFlightNum(e.target.value)} />
        <label>DAIRI</label>
        <input value={dep} onChange={e=>setDep(e.target.value)} />
        <label>LAIRI</label>
        <input value={arr} onChange={e=>setArr(e.target.value)} />
        <button type="submit">Search</button>
      </form>
      <FlightResults rows={rows} />
    </div>
  )
}
