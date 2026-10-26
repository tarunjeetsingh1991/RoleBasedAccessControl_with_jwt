

import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { deleteCustomer, getAllCustomers } from '../../api/customerApi';
import AlertMessage from '../../components/AlertMessage';
import Loader from '../../components/Loader';
import { useAuth } from '../../context/AuthContext';

function CustomerList() {
  const { isAdmin } = useAuth();

  const [customers, setCustomers] = useState([]);
  const [loading, setLoading] = useState(true);
  const [message, setMessage] = useState('');
  const [error, setError] = useState('');
  const [currentPage, setCurrentPage] = useState(1);

  const recordsPerPage = 5;

  const loadCustomers = async () => {
    try {
      setLoading(true);
      const response = await getAllCustomers();
      setCustomers(response.data);
      setError('');
    } catch (err) {
      setError('Failed to load customers.');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadCustomers();
  }, []);

  const handleDelete = async (id) => {
    const confirmDelete = window.confirm('Are you sure you want to delete this customer?');
    if (!confirmDelete) return;

    try {
      await deleteCustomer(id);
      setMessage('Customer deleted successfully.');

      const updatedCustomers = customers.filter((c) => c.customerId !== id);
      setCustomers(updatedCustomers);

      const totalPagesAfterDelete = Math.ceil(updatedCustomers.length / recordsPerPage);
      if (currentPage > totalPagesAfterDelete && totalPagesAfterDelete > 0) {
        setCurrentPage(totalPagesAfterDelete);
      }
    } catch (err) {
      setError('Unable to delete customer.');
    }
  };

  const totalPages = Math.ceil(customers.length / recordsPerPage);
  const startIndex = (currentPage - 1) * recordsPerPage;
  const endIndex = startIndex + recordsPerPage;
  const currentCustomers = customers.slice(startIndex, endIndex);

  if (loading) return <Loader />;

  return (
    <div className="page-card">
      <div className="page-header d-flex justify-content-between align-items-center">
        <h2>View All Customers</h2>

        {isAdmin && (
          <Link to="/customers/add" className="btn btn-success">
            Add New Customer
          </Link>
        )}
      </div>

      <AlertMessage type="success" message={message} />
      <AlertMessage type="danger" message={error} />

      <table className="table table-bordered table-striped">
        <thead className="table-dark">
          <tr>
            <th>Customer Id</th>
            <th>Name</th>
            <th>Type</th>
            <th>City</th>
            <th>Province</th>
            <th>Postal Code</th>
            <th>Actions</th>
          </tr>
        </thead>

        <tbody>
          {currentCustomers.map((customer) => (
            <tr key={customer.customerId}>
              <td>{customer.customerId}</td>
              <td>{customer.customerName}</td>
              <td>{customer.customerType}</td>
              <td>{customer.address?.city}</td>
              <td>{customer.address?.province}</td>
              <td>{customer.address?.postalCode}</td>

              <td>
                <div className="action-buttons">
                  <Link
                    to={`/customers/${customer.customerId}`}
                    className="btn btn-primary btn-sm"
                  >
                    View
                  </Link>

                  {isAdmin && (
                    <>
                      <Link
                        to={`/customers/update/${customer.customerId}`}
                        className="btn btn-warning btn-sm"
                      >
                        Update
                      </Link>

                      <button
                        className="btn btn-danger btn-sm"
                        onClick={() => handleDelete(customer.customerId)}
                      >
                        Delete
                      </button>
                    </>
                  )}
                </div>
              </td>
            </tr>
          ))}

          {currentCustomers.length === 0 && (
            <tr>
              <td colSpan="7" className="text-center">
                No customers found.
              </td>
            </tr>
          )}
        </tbody>
      </table>

      {customers.length > recordsPerPage && (
        <div className="pagination-container">
          <button
            className="btn btn-secondary btn-sm"
            onClick={() => setCurrentPage(currentPage - 1)}
            disabled={currentPage === 1}
          >
            Previous
          </button>

          <span className="pagination-info">
            Page {currentPage} of {totalPages}
          </span>

          <button
            className="btn btn-secondary btn-sm"
            onClick={() => setCurrentPage(currentPage + 1)}
            disabled={currentPage === totalPages}
          >
            Next
          </button>
        </div>
      )}
    </div>
  );
}

export default CustomerList;
