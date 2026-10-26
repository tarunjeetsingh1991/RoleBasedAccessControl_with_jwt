
// import { useState } from 'react';
// import { useNavigate } from 'react-router-dom';
// import { loginUser } from '../api/authApi';
// import { useAuth } from '../context/AuthContext';
// import AlertMessage from '../components/AlertMessage';

// function Login() {
//   const navigate = useNavigate();
//   const { login } = useAuth();

//   const [formData, setFormData] = useState({
//     username: '',
//     password: '',
//   });

//   const [error, setError] = useState('');
//   const [loading, setLoading] = useState(false);

//   const handleChange = (e) => {
//     const { name, value } = e.target;
//     setFormData({
//       ...formData,
//       [name]: value,
//     });
//   };

//   const handleSubmit = async (e) => {
//     e.preventDefault();
//     setError('');

//     if (!formData.username || !formData.password) {
//       setError('Please enter username and password.');
//       return;
//     }

//     try {
//       setLoading(true);

//       const response = await loginUser(formData);
//       console.log("Login response:", response.data);
//       console.log("Token:", response.data.token);

//       login(response.data);  

//       navigate('/');
//     } catch (err) {
//       setError('Invalid username or password.');
//     } finally {
//       setLoading(false);
//     }
//   };

//   return (
//     <div className="page-card login-card">
//       <h2 className="mb-3">Login</h2>

//       <AlertMessage type="danger" message={error} />

//       <form onSubmit={handleSubmit} className="form-grid">
//         <input
//           type="text"
//           name="username"
//           placeholder="Enter username"
//           value={formData.username}
//           onChange={handleChange}
//           className="form-control"
//         />

//         <input
//           type="password"
//           name="password"
//           placeholder="Enter password"
//           value={formData.password}
//           onChange={handleChange}
//           className="form-control"
//         />

//         <button type="submit" className="btn btn-primary" disabled={loading}>
//           {loading ? 'Logging in...' : 'Login'}
//         </button>
//       </form>
//     </div>
//   );
// }

// export default Login;

import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { loginUser } from '../api/authApi';
import { useAuth } from '../context/AuthContext';
import AlertMessage from '../components/AlertMessage';

function Login() {
  const navigate = useNavigate();
  const { login } = useAuth();

  const [formData, setFormData] = useState({
    username: '',
    password: '',
  });

  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({
      ...formData,
      [name]: value,
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');

    if (!formData.username || !formData.password) {
      setError('Please enter username and password.');
      return;
    }

    try {
      setLoading(true);

      const response = await loginUser(formData);

      console.log('Login response:', response.data);

      // cookie is automatically stored by browser
      // no need to manually save token
      login({
        username: response.data.username,
        role: response.data.role,
      });

      navigate('/');
    } catch (err) {
      setError('Invalid username or password.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="page-card login-card">
      <h2 className="mb-3">Login</h2>

      <AlertMessage type="danger" message={error} />

      <form onSubmit={handleSubmit} className="form-grid">
        <input
          type="text"
          name="username"
          placeholder="Enter username"
          value={formData.username}
          onChange={handleChange}
          className="form-control"
        />

        <input
          type="password"
          name="password"
          placeholder="Enter password"
          value={formData.password}
          onChange={handleChange}
          className="form-control"
        />

        <button type="submit" className="btn btn-primary" disabled={loading}>
          {loading ? 'Logging in...' : 'Login'}
        </button>
      </form>
    </div>
  );
}

export default Login;