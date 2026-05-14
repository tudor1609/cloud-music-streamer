import axios from 'axios';

const api = axios.create({
    // Hardcodat direct către Railway Gateway, dă-l încolo de .env
    baseURL: 'https://gateway-production-529a.up.railway.app'
});

api.interceptors.request.use((config) => {
    const token = localStorage.getItem('dracify_token');
    if (token) {
        config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
});

export default api;