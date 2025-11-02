import {useState, useEffect} from 'react';

export default function useAuth(){
  // very small in-memory hook; for broader use wrap with context
  const [token, setTokenState] = useState(()=> localStorage.getItem('jwt'));
  useEffect(()=>{ if(token) localStorage.setItem('jwt', token); else localStorage.removeItem('jwt'); },[token]);
  return {
    token,
    setToken: setTokenState,
    clear: ()=> setTokenState(null)
  }
}
