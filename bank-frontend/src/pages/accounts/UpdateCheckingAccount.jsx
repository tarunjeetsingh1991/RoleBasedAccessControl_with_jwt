import { useEffect, useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import AlertMessage from '../../components/AlertMessage';
import { getAccountById, updateCheckingAccount } from '../../api/accountApi';

function UpdateCheckingAccount() {
  const { id } = useParams();
  const navigate = useNavigate();

  const [formData, setFormData] = useState({
    balance: '',
    nextCheckNumber: '',
  });

  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');

  useEffect(() => {
    const fetchAccount = async () => {
      try {
        const response = await getAccountById(id);
        const account = response.data;

        setFormData({
          balance: account.balance || '',
          nextCheckNumber: account.nextCheckNumber || '',
        });
      } catch (err) {
        setError('Failed to load checking account details.');
      }
    };

    fetchAccount();
  }, [id]);

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
    setSuccess('');

    try {
      await updateCheckingAccount(id, {
        balance: Number(formData.balance),
        nextCheckNumber: Number(formData.nextCheckNumber),
      });

      setSuccess('Checking account updated successfully.');

      setTimeout(() => {
        navigate('/accounts');
      }, 1200);
    } catch (err) {
      setError('Failed to update checking account.');
    }
  };

  return (
    <div className="page-card">
      <h2>Update Checking Account</h2>

      <AlertMessage type="success" message={success} />
      <AlertMessage type="danger" message={error} />

      <form onSubmit={handleSubmit} className="form-grid">
        <input
          type="number"
          step="0.01"
          name="balance"
          placeholder="Balance"
          value={formData.balance}
          onChange={handleChange}
          className="form-control"
          required
        />

        <input
          type="number"
          name="nextCheckNumber"
          placeholder="Next Check Number"
          value={formData.nextCheckNumber}
          onChange={handleChange}
          className="form-control"
          required
        />

        <button type="submit" className="btn btn-warning">
          Update Checking Account
        </button>
      </form>
    </div>
  );
}

export default UpdateCheckingAccount;