import axios from 'axios';

const api = axios.create({
  baseURL: 'http://localhost:8080/api'
});

export const fetchStocks = (params = {}) => api.get('/stocks', { params }).then((response) => response.data);
export const updateMarket = () => api.post('/market/update').then((response) => response.data);
export const fetchPortfolio = (params = {}) => api.get('/portfolio', { params }).then((response) => response.data);
export const fetchTransactions = () => api.get('/transactions').then((response) => response.data);
export const buyStock = (payload) => api.post('/buy', payload).then((response) => response.data);
export const sellStock = (payload) => api.post('/sell', payload).then((response) => response.data);

export const getApiError = (error) => {
  if (error.response?.data?.message) {
    return error.response.data.message;
  }

  return 'Could not connect to the backend. Please make sure Spring Boot is running on port 8080.';
};
