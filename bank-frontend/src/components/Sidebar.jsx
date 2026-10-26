import { Link } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

function Sidebar() {
  const { isLoggedIn, isAdmin } = useAuth();

  if (!isLoggedIn) return null;

  return (
    <aside className="sidebar">
      <nav className="sidebar-nav">
        <Link to="/" className="sidebar-link">
          <i className="bi bi-house-door-fill me-2"></i>
          Home
        </Link>

        <Link to="/customers" className="sidebar-link">
          <i className="bi bi-people-fill me-2"></i>
          View Customers
        </Link>

        {isAdmin && (
          <Link to="/customers/add" className="sidebar-link">
            <i className="bi bi-person-plus-fill me-2"></i>
            Add Customer
          </Link>
        )}

        <Link to="/customers/find-by-id" className="sidebar-link">
          <i className="bi bi-search me-2"></i>
          Find By Id
        </Link>

        <Link to="/accounts" className="sidebar-link">
          <i className="bi bi-credit-card-2-front-fill me-2"></i>
          View Accounts
        </Link>

        <Link to="/accounts/by-customer-id" className="sidebar-link">
          <i className="bi bi-person-badge-fill me-2"></i>
          Accounts By Customer ID
        </Link>

        <Link to="/accounts/by-customer-name" className="sidebar-link">
          <i className="bi bi-person-lines-fill me-2"></i>
          Accounts By Customer Name
        </Link>

        {isAdmin && (
          <Link to="/accounts/savings/add" className="sidebar-link">
            <i className="bi bi-cash-coin me-2"></i>
            Add Savings
          </Link>
        )}

        {isAdmin && (
          <Link to="/accounts/checking/add" className="sidebar-link">
            <i className="bi bi-bank me-2"></i>
            Add Checking
          </Link>
        )}

        <Link to="/accounts/find-by-balance" className="sidebar-link">
          <i className="bi bi-bar-chart-fill me-2"></i>
          Find By Balance
        </Link>
      </nav>
    </aside>
  );
}

export default Sidebar;