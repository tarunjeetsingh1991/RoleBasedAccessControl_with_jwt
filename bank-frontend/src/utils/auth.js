export const saveAuthData = (data) => {
  localStorage.setItem('token', data.token);
  localStorage.setItem('username', data.username);
  localStorage.setItem('role', data.role);
};

export const clearAuthData = () => {
  localStorage.removeItem('token');
  localStorage.removeItem('username');
  localStorage.removeItem('role');
};

export const getToken = () => localStorage.getItem('token');
export const getUsername = () => localStorage.getItem('username');
export const getRole = () => localStorage.getItem('role');

export const isLoggedIn = () => !!localStorage.getItem('token');

export const isAdmin = () => localStorage.getItem('role') === 'ROLE_ADMIN';
export const isUser = () => localStorage.getItem('role') === 'ROLE_USER';