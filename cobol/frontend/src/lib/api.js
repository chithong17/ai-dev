import axios from 'axios';

const api = axios.create({
  baseURL: 'http://localhost:8080', // ✅ backend URL thật
});

api.interceptors.response.use(r => r, err => Promise.reject(err));

export default api;
