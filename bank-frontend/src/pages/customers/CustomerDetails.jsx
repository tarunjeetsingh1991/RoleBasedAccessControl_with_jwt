import { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { getCustomerById } from '../../api/customerApi';
import { getAccountsByCustomerId } from '../../api/accountApi';
import Loader from '../../components/Loader';
import AlertMessage from '../../components/AlertMessage';

function CustomerDetails() {
  const { id } = useParams();

  const [customer, setCustomer] = useState(null);
  const [accounts, setAccounts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    const fetchDetails = async () => {
      try {
        const customerResponse = await getCustomerById(id);
        setCustomer(customerResponse.data);

        try {
          const accountsResponse = await getAccountsByCustomerId(id);
          setAccounts(accountsResponse.data);
        } catch {
          setAccounts([]);
        }
      } catch (err) {
        setError('Customer not found.');
      } finally {
        setLoading(false);
      }
    };

    fetchDetails();
  }, [id]);

  if (loading) return <Loader />;

  if (error) return <AlertMessage type="danger" message={error} />;

  return (
    <div className="page-card">
      <h2>Customer Details</h2>

      <div className="detail-box">
        <p><strong>Id:</strong> {customer.customerId}</p>
        <p><strong>Name:</strong> {customer.customerName}</p>
        <p><strong>Type:</strong> {customer.customerType}</p>
        <p><strong>Street:</strong> {customer.address?.streetNumber}</p>
        <p><strong>City:</strong> {customer.address?.city}</p>
        <p><strong>Province:</strong> {customer.address?.province}</p>
        <p><strong>Postal Code:</strong> {customer.address?.postalCode}</p>
      </div>

      <h3 className="mt-4">Accounts</h3>
      <table className="table table-bordered">
        <thead className="table-dark">
          <tr>
            <th>Account Id</th>
            <th>Type</th>
            <th>Balance</th>
            <th>Interest Rate</th>
            <th>Next Check Number</th>
          </tr>
        </thead>
        <tbody>
          {accounts.length > 0 ? (
            accounts.map((account) => (
              <tr key={account.accountId}>
                <td>{account.accountId}</td>
                <td>{account.accountType}</td>
                <td>{account.balance}</td>
                <td>{account.interestRate || '-'}</td>
                <td>{account.nextCheckNumber || '-'}</td>
              </tr>
            ))
          ) : (
            <tr>
              <td colSpan="5">No accounts found for this customer.</td>
            </tr>
          )}
        </tbody>
      </table>
    </div>
  );
}

export default CustomerDetails;