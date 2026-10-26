// import { createContext, useContext, useEffect, useState } from 'react';

// const AuthContext = createContext();

// export function AuthProvider({ children }) {
//   const [auth, setAuth] = useState({
//     token: localStorage.getItem('token'),
//     username: localStorage.getItem('username'),
//     role: localStorage.getItem('role'),
//   });

//   useEffect(() => {
//     const syncAuth = () => {
//       setAuth({
//         token: localStorage.getItem('token'),
//         username: localStorage.getItem('username'),
//         role: localStorage.getItem('role'),
//       });
//     };

//     window.addEventListener('storage', syncAuth);
//     return () => window.removeEventListener('storage', syncAuth);
//   }, []);

//   const login = (data) => {
//     localStorage.setItem('token', data.token);
//     localStorage.setItem('username', data.username);
//     localStorage.setItem('role', data.role);

//     setAuth({
//       token: data.token,
//       username: data.username,
//       role: data.role,
//     });
//   };

//   const logout = () => {
//     localStorage.removeItem('token');
//     localStorage.removeItem('username');
//     localStorage.removeItem('role');

//     setAuth({
//       token: null,
//       username: null,
//       role: null,
//     });
//   };

//   const isLoggedIn = !!auth.token;
//   const isAdmin = auth.role === 'ROLE_ADMIN';
//   const isUser = auth.role === 'ROLE_USER';

//   return (
//     <AuthContext.Provider
//       value={{
//         auth,
//         login,
//         logout,
//         isLoggedIn,
//         isAdmin,
//         isUser,
//       }}
//     >
//       {children}
//     </AuthContext.Provider>
//   );
// }

// export function useAuth() {
//   return useContext(AuthContext);
// }

import { createContext, useContext, useEffect, useState } from 'react';
import { logoutUser } from '../api/authApi';

const AuthContext = createContext();

export function AuthProvider({ children }) {
  const [auth, setAuth] = useState({
    username: localStorage.getItem('username'),
    role: localStorage.getItem('role'),
    isLoggedIn: localStorage.getItem('isLoggedIn') === 'true',
  });

  useEffect(() => {
    const syncAuth = () => {
      setAuth({
        username: localStorage.getItem('username'),
        role: localStorage.getItem('role'),
        isLoggedIn: localStorage.getItem('isLoggedIn') === 'true',
      });
    };

    window.addEventListener('storage', syncAuth);
    return () => window.removeEventListener('storage', syncAuth);
  }, []);

  const login = (data) => {
    localStorage.setItem('username', data.username);
    localStorage.setItem('role', data.role);
    localStorage.setItem('isLoggedIn', 'true');

    setAuth({
      username: data.username,
      role: data.role,
      isLoggedIn: true,
    });
  };

  const logout = async () => {
    try {
      await logoutUser();
    } catch (error) {
      console.error('Logout request failed:', error);
    } finally {
      localStorage.removeItem('username');
      localStorage.removeItem('role');
      localStorage.removeItem('isLoggedIn');

      setAuth({
        username: null,
        role: null,
        isLoggedIn: false,
      });
    }
  };

  const isLoggedIn = auth.isLoggedIn;
  const isAdmin = auth.role === 'ROLE_ADMIN';
  const isUser = auth.role === 'ROLE_USER';

  return (
    <AuthContext.Provider
      value={{
        auth,
        login,
        logout,
        isLoggedIn,
        isAdmin,
        isUser,
      }}
    >
      {children}
    </AuthContext.Provider>
  );
}

export function useAuth() {
  return useContext(AuthContext);
}