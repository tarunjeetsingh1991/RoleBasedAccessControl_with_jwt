
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

function Navbar() {
  const navigate = useNavigate();
  const { auth, isLoggedIn, logout } = useAuth();

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  return (
    <nav className="top-navbar navbar navbar-expand-lg navbar-light bg-white shadow-sm">
      <div className="container-fluid">
        <span className="navbar-brand fw-bold text-primary ms-4">
          Banking Application
        </span>

        <button
          className="navbar-toggler"
          type="button"
          data-bs-toggle="collapse"
          data-bs-target="#topNavbar"
          aria-controls="topNavbar"
          aria-expanded="false"
          aria-label="Toggle navigation"
        >
          <span className="navbar-toggler-icon"></span>
        </button>

        <div className="collapse navbar-collapse" id="topNavbar">
          <ul className="navbar-nav ms-auto align-items-lg-center">
            {isLoggedIn ? (
              <>
                <li className="nav-item">
                  <Link to="/" className="nav-link fw-bold">Home</Link>
                </li>
                <li className="nav-item">
                  <Link to="/customers" className="nav-link fw-bold">Customers</Link>
                </li>
                <li className="nav-item">
                  <Link to="/accounts" className="nav-link fw-bold">Accounts</Link>
                </li>
                <li className="nav-item">
                  <span className="nav-link text-muted fst-italic">{auth.username} </span>
                </li>
                <li className="nav-item">
                  <button className="btn btn-outline-danger btn-sm ms-lg-2" onClick={handleLogout}>
                    Logout
                  </button>
                </li>
              </>
            ) : (
              <li className="nav-item">
                <Link to="/login" className="nav-link">Login</Link>
              </li>
            )}
          </ul>
        </div>
      </div>
    </nav>
  );
}

export default Navbar;