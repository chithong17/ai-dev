import axios from 'axios';

const baseURL = process.env.REACT_APP_API_URL || '';

const api = axios.create({ baseURL });

// THÊM INTERCEPTOR (BỘ CHẶN) NÀY
api.interceptors.request.use(
  (config) => {
    // Lấy token từ localStorage (nơi useAuth.js [cite: "chithong17/ai-dev/ai-dev-main/cobol/frontend/src/state/useAuth.js"] lưu nó)
    const token = localStorage.getItem('jwt'); 
    if (token) {
      // Đính kèm token vào header
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);
// KẾT THÚC INTERCEPTOR

api.interceptors.response.use(r => r, err => {
  // simple passthrough for error handling in UI
  return Promise.reject(err);
});

export default api;

