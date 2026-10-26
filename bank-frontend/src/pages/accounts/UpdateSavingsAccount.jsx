import { useEffect, useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import AlertMessage from '../../components/AlertMessage';
import { getAccountById, updateSavingsAccount } from '../../api/accountApi';

function UpdateSavingsAccount() {
  const { id } = useParams();
  const navigate = useNavigate();

  const [formData, setFormData] = useState({
    balance: '',
    interestRate: '',
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
          interestRate: account.interestRate || '',
        });
      } catch (err) {
        setError('Failed to load savings account details.');
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
      await updateSavingsAccount(id, {
        balance: Number(formData.balance),
        interestRate: Number(formData.interestRate),
      });

      setSuccess('Savings account updated successfully.');

      setTimeout(() => {
        navigate('/accounts');
      }, 1200);
    } catch (err) {
      setError('Failed to update savings account.');
    }
  };

  return (
    <div className="page-card">
      <h2>Update Savings Account</h2>

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
          step="0.01"
          name="interestRate"
          placeholder="Interest Rate"
          value={formData.interestRate}
          onChange={handleChange}
          className="form-control"
          required
        />

        <button type="submit" className="btn btn-warning">
          Update Savings Account
        </button>
      </form>
    </div>
  );
}

export default UpdateSavingsAccount;