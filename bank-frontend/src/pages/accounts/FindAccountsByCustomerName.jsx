import { useState } from 'react';
import { getAccountsByCustomerName } from '../../api/accountApi';
import AlertMessage from '../../components/AlertMessage';

function FindAccountsByCustomerName() {

  const [customerName, setCustomerName] = useState('');
  const [accounts, setAccounts] = useState([]);

  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');

  const handleSearch = async () => {

    setError('');
    setSuccess('');
    setAccounts([]);

    if (!customerName.trim()) {
      setError('Please enter customer name.');
      return;
    }

    try {

      const res = await getAccountsByCustomerName(customerName.trim());

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
    setCustomerName('');
    setAccounts([]);
    setError('');
    setSuccess('');
  };

  return (
    <div className="page-card">

      <h2>Accounts By Customer Name</h2>

      <AlertMessage type="danger" message={error} />
      <AlertMessage type="success" message={success} />

      {/* input */}

      <input
        type="text"
        className="form-control mb-3"
        placeholder="Enter customer name"
        value={customerName}
        onChange={(e) => setCustomerName(e.target.value)}
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

      {/* table only if results */}

      {accounts.length > 0 && (

        <table className="table table-striped">

          <thead>
            <tr>
              <th>ID</th>
              <th>Balance</th>
              <th>Type</th>
              <th>Interest</th>
              <th>Check #</th>
              <th>Customer</th>
            </tr>
          </thead>

          <tbody>

            {accounts.map((a) => (
              <tr key={a.accountId}>

                <td>{a.accountId}</td>

                <td>{a.balance}</td>

                <td>{a.accountType}</td>

                <td>{a.interestRate ?? '-'}</td>

                <td>{a.nextCheckNumber ?? '-'}</td>

                <td>{a.customerName}</td>

              </tr>
            ))}

          </tbody>

        </table>

      )}

    </div>
  );
}

export default FindAccountsByCustomerName;