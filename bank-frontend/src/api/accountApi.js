import api from './axiosConfig';

export const createSavingsAccount = (data) => api.post('/accounts/savings', data);

export const createCheckingAccount = (data) => api.post('/accounts/checking', data);

export const updateSavingsAccount = (id, data) =>
  api.put(`/accounts/savings/${id}`, data);

export const updateCheckingAccount = (id, data) =>
  api.put(`/accounts/checking/${id}`, data);

export const deleteSavingsAccount = (id) =>
  api.delete(`/accounts/savings/${id}`);

export const deleteCheckingAccount = (id) =>
  api.delete(`/accounts/checking/${id}`);

export const getAccountsByCity = (city) =>
  api.get(`/accounts/city/${city}`);

export const getAccountsByCustomerId = (customerId) =>
  api.get(`/accounts/customer/${customerId}`);

export const getAllAccounts = () =>
  api.get('/accounts');

export const getAccountById = (id) =>
  api.get(`/accounts/${id}`);

export const getAccountsByBalance = (balance) =>
  api.get(`/accounts/balance/${balance}`);

export const getAccountsByCustomerName = (customerName) =>
  api.get(`/accounts/customer-name/${customerName}`);