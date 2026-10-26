
import { useState } from 'react';
import { getAccountsByCustomerId } from '../../api/accountApi';
import AlertMessage from '../../components/AlertMessage';

function FindAccountsByCustomer() {

  const [customerId, setCustomerId] = useState('');
  const [accounts, setAccounts] = useState([]);

  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');

  const handleSearch = async () => {

    setError('');
    setSuccess('');
    setAccounts([]);

    if (!customerId) {
      setError('Please enter customer id');
      return;
    }

    try {

      const res = await getAccountsByCustomerId(customerId);

      if (!res.data || res.data.length === 0) {
        setError('Customer not found or no accounts for this customer');
        return;
      }

      setAccounts(res.data);
      setSuccess(`Found ${res.data.length} account(s)`);

    } catch (err) {

      setError('Customer not found');

    }
  };

  const handleReset = () => {
    setCustomerId('');
    setAccounts([]);
    setError('');
    setSuccess('');
  };

  return (
    <div className="page-card">

      <h2>Accounts By Customer</h2>

      <AlertMessage type="danger" message={error} />
      <AlertMessage type="success" message={success} />

      {/* input */}

      <input
        type="number"
        placeholder="Enter Customer ID"
        value={customerId}
        onChange={(e) => setCustomerId(e.target.value)}
        className="form-control mb-2"
      />

      {/* buttons */}

      <div className="d-flex gap-2 mb-3">

        <button
          className="btn btn-primary"
          onClick={handleSearch}
        >
          Search
        </button>

        <button
          className="btn btn-secondary"
          onClick={handleReset}
        >
          Reset
        </button>

      </div>

      {/* table */}

      {accounts.length > 0 && (

        <table className="table mt-3">

          <thead>
            <tr>
              <th>ID</th>
              <th>Balance</th>
              <th>Type</th>
              <th>Customer</th>
            </tr>
          </thead>

          <tbody>

            {accounts.map((a) => (
              <tr key={a.accountId}>
                <td>{a.accountId}</td>
                <td>{a.balance}</td>
                <td>{a.accountType}</td>
                <td>{a.customerName}</td>
              </tr>
            ))}

          </tbody>

        </table>

      )}

    </div>
  );
}

export default FindAccountsByCustomer;