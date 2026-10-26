import { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { getAccountById } from '../../api/accountApi';
import Loader from '../../components/Loader';
import AlertMessage from '../../components/AlertMessage';

function AccountDetails() {

  const { id } = useParams();

  const [account, setAccount] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {

    const fetchAccount = async () => {
      try {
        const res = await getAccountById(id);
        setAccount(res.data);
      } catch (err) {
        setError('Failed to load account.');
      } finally {
        setLoading(false);
      }
    };

    fetchAccount();

  }, [id]);

  if (loading) return <Loader />;

  if (error) return <AlertMessage type="danger" message={error} />;

  return (
    <div className="page-card">

      <h2>Account Details</h2>

      <table className="table table-bordered">

        <tbody>

          <tr>
            <th>ID</th>
            <td>{account.accountId}</td>
          </tr>

          <tr>
            <th>Balance</th>
            <td>{account.balance}</td>
          </tr>

          <tr>
            <th>Type</th>
            <td>{account.accountType}</td>
          </tr>

          <tr>
            <th>Interest Rate</th>
            <td>{account.interestRate || '-'}</td>
          </tr>

          <tr>
            <th>Next Check Number</th>
            <td>{account.nextCheckNumber || '-'}</td>
          </tr>

          <tr>
            <th>Customer</th>
            <td>{account.customerName}</td>
          </tr>

        </tbody>

      </table>

    </div>
  );
}

export default AccountDetails;