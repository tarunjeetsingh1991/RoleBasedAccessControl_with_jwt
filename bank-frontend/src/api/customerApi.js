import api from './axiosConfig';

export const getAllCustomers = () => api.get('/customers');

export const getCustomerById = (id) => api.get(`/customers/${id}`);

export const createCustomer = (customerData) => api.post('/customers', customerData);

export const updateCustomer = (id, customerData) => api.put(`/customers/${id}`, customerData);

export const deleteCustomer = (id) => api.delete(`/customers/${id}`);

export const getCustomersByCity = (city) => api.get(`/customers/city/${city}`);

export const getCustomersWithSavings = () => api.get('/customers/savings');

export const createCustomerWithGeoJson = (customerData) =>
  api.post('/customers/province/json', customerData);