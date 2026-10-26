// import axios from 'axios';

// const authApi = axios.create({
//   baseURL: 'http://localhost:9090/api/v1',
//   headers: {
//     'Content-Type': 'application/json',
//   },
// });

// export const loginUser = (credentials) =>
//   authApi.post('/auth/login', credentials);

// export default authApi;

import axios from 'axios';

const authApi = axios.create({
  baseURL: 'http://localhost:9090/api/v1',
  headers: {
    'Content-Type': 'application/json',
  },
  withCredentials: true,
});

export const loginUser = (credentials) =>
  authApi.post('/auth/login', credentials);

export const logoutUser = () =>
  authApi.post('/auth/logout');

export default authApi;