import axios from 'axios';

const baseURL = process.env.REACT_APP_API_URL || '';

const api = axios.create({ baseURL });

api.interceptors.response.use(r => r, err => {
  // simple passthrough for error handling in UI
  return Promise.reject(err);
});

export default api;
