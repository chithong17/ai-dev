import React, {useState} from 'react';
import api from '../lib/api';
import useAuth from '../state/useAuth';
import {useNavigate} from 'react-router-dom';

export default function LoginPage(){
  const [empid, setEmpid] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState(null);
  const {setToken} = useAuth();
  const navigate = useNavigate();

  const submit = async (e) => {
    e.preventDefault();
    setError(null);
    try {
      const resp = await api.post('/api/auth/login', { empid, password });
      setToken(resp.data.token);
      // redirect to search for Sales dept (frontend routing handles)
      navigate('/booking-step1');

    } catch (err) {
      setError(err?.response?.data?.error || 'Login failed');
    }
  }

  return (
    <div className="terminal-page" style={{fontFamily: 'monospace', padding: 24}}>
      <h2>COBOL Airlines — Login</h2>
      <form onSubmit={submit}>
        <div>
          <label>USERIDI</label>
          <input name="empid" value={empid} onChange={e=>setEmpid(e.target.value)} />
        </div>
        <div>
          <label>PASSWI</label>
          <input name="password" type="password" value={password} onChange={e=>setPassword(e.target.value)} />
        </div>
        <button type="submit">Login</button>
        {error && <div className="error">{error}</div>}
      </form>
    </div>
  )
}
