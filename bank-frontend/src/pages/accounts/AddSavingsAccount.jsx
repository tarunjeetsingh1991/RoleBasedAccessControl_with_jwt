import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { createSavingsAccount } from '../../api/accountApi';
import AlertMessage from '../../components/AlertMessage';

function AddSavingsAccount() {
  const navigate = useNavigate();

  const [formData, setFormData] = useState({
    balance: '',
    interestRate: '',
    customer: {
      customerId: '',
      customerType: 'PERSON',
    },
  });

  const [error, setError] = useState('');

  const handleChange = (e) => {
    const { name, value } = e.target;

    if (name === 'customerId') {
      setFormData({
        ...formData,
        customer: {
          ...formData.customer,
          customerId: value,
        },
      });
    } else if (name === 'customerType') {
      setFormData({
        ...formData,
        customer: {
          ...formData.customer,
          customerType: value,
        },
      });
    } else {
      setFormData({
        ...formData,
        [name]: value,
      });
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');

    try {
      await createSavingsAccount({
        balance: Number(formData.balance),
        interestRate: Number(formData.interestRate),
        customer: {
          customerId: Number(formData.customer.customerId),
          customerType: formData.customer.customerType,
        },
      });

      navigate('/accounts');
    } catch (err) {
      setError('Failed to create savings account.');
    }
  };

  return (
    <div className="page-card">
      <h2>Add Savings Account</h2>
      <AlertMessage type="danger" message={error} />

      <form onSubmit={handleSubmit} className="form-grid">
        <input
          type="number"
          name="customerId"
          placeholder="Customer Id"
          value={formData.customer.customerId}
          onChange={handleChange}
          className="form-control"
          required
        />

        <select
          name="customerType"
          value={formData.customer.customerType}
          onChange={handleChange}
          className="form-control"
        >
          <option value="PERSON">Person</option>
          <option value="COMPANY">Company</option>
        </select>

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

        <button type="submit" className="btn btn-success">
          Create Savings Account
        </button>
      </form>
    </div>
  );
}

export default AddSavingsAccount;