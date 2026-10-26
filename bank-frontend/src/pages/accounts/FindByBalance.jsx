import { useState } from 'react';
import { getAccountsByBalance } from '../../api/accountApi';
import AlertMessage from '../../components/AlertMessage';

function FindByBalance() {
  const [balance, setBalance] = useState('');
  const [accounts, setAccounts] = useState([]);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');
  const [searched, setSearched] = useState(false);
  const [loading, setLoading] = useState(false);

  const handleSearch = async (e) => {
    e.preventDefault();

    setError('');
    setSuccess('');
    setAccounts([]);
    setSearched(false);

    if (balance === '') {
      setError('Please enter a balance.');
      return;
    }

    try {
      setLoading(true);

      const response = await getAccountsByBalance(balance);
      setAccounts(response.data);
      setSearched(true);

      if (response.data.length === 0) {
        setSuccess('No accounts found for the given balance.');
      } else {
        setSuccess(`Found ${response.data.length} account(s).`);
      }
    } catch (err) {
      setError('Failed to fetch accounts by balance.');
    } finally {
      setLoading(false);
    }
  };

  const handleReset = () => {
    setBalance('');
    setAccounts([]);
    setError('');
    setSuccess('');
    setSearched(false);
  };

  return (
    <div className="page-card">
      <h2>Find Accounts By Balance</h2>

      <AlertMessage type="danger" message={error} />
      <AlertMessage type="success" message={success} />

      <form onSubmit={handleSearch} className="form-grid mb-4">
        <input
          type="number"
          step="0.01"
          placeholder="Enter exact balance"
          value={balance}
          onChange={(e) => setBalance(e.target.value)}
          className="form-control"
        />

        <div className="d-flex gap-2">
          <button type="submit" className="btn btn-primary">
            Search
          </button>

          <button
            type="button"
            className="btn btn-secondary"
            onClick={handleReset}
          >
            Reset
          </button>
        </div>
      </form>

      {loading && <p>Loading...</p>}

      {searched && accounts.length > 0 && (
        <table className="table table-bordered table-striped">
          <thead className="table-dark">
            <tr>
              <th>Account Id</th>
              <th>Balance</th>
              <th>Account Type</th>
              <th>Interest Rate</th>
              <th>Next Check Number</th>
              <th>Customer Id</th>
              <th>Customer Name</th>
              <th>City</th>
              <th>Province</th>
              <th>Postal Code</th>
              <th>Street Number</th>
            </tr>
          </thead>
          <tbody>
            {accounts.map((account) => (
              <tr key={account.accountId}>
                <td>{account.accountId}</td>
                <td>{account.balance}</td>
                <td>{account.accountType}</td>
                <td>{account.interestRate ?? '-'}</td>
                <td>{account.nextCheckNumber ?? '-'}</td>
                <td>{account.customerId}</td>
                <td>{account.customerName}</td>
                <td>{account.city}</td>
                <td>{account.province}</td>
                <td>{account.postalCode}</td>
                <td>{account.streetNumber}</td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
}

export default FindByBalance;