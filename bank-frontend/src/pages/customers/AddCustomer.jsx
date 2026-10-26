

import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { createCustomer, createCustomerWithGeoJson } from '../../api/customerApi';
import AlertMessage from '../../components/AlertMessage';

function AddCustomer() {
  const navigate = useNavigate();

  const [formData, setFormData] = useState({
    customerType: 'PERSON',
    customerName: '',
    address: {
      streetNumber: '',
      postalCode: '',
      city: '',
      province: '',
    },
  });

  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');

  const handleChange = (e) => {
    const { name, value } = e.target;

    if (['streetNumber', 'postalCode', 'city', 'province'].includes(name)) {
      setFormData({
        ...formData,
        address: {
          ...formData.address,
          [name]: value,
        },
      });
    } else {
      setFormData({
        ...formData,
        [name]: value,
      });
    }
  };

  const handleNormalSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setSuccess('');

    try {
      await createCustomer(formData);
      setSuccess('Customer created successfully.');
      setTimeout(() => navigate('/customers'), 1000);
    } catch (err) {
      setError('Failed to create customer.');
    }
  };

  const handleGeoJsonSubmit = async () => {
    setError('');
    setSuccess('');

    try {
      const payload = {
        ...formData,
        address: {
          ...formData.address,
          city: '',
          province: '',
        },
      };

      await createCustomerWithGeoJson(payload);
      setSuccess('Customer created successfully using geocoder JSON.');
      setTimeout(() => navigate('/customers'), 1000);
    } catch (err) {
      setError('Failed to create customer using geocoder JSON.');
    }
  };

  return (
    <div className="page-card">
      <h2>Add Customer</h2>

      <AlertMessage type="danger" message={error} />
      <AlertMessage type="success" message={success} />

      <form onSubmit={handleNormalSubmit} className="form-grid">
        <select
          name="customerType"
          value={formData.customerType}
          onChange={handleChange}
          className="form-control"
        >
          <option value="PERSON">Person</option>
          <option value="COMPANY">Company</option>
        </select>

        <input
          type="text"
          name="customerName"
          placeholder="Customer Name"
          value={formData.customerName}
          onChange={handleChange}
          className="form-control"
          required
        />

        <input
          type="text"
          name="streetNumber"
          placeholder="Street Number"
          value={formData.address.streetNumber}
          onChange={handleChange}
          className="form-control"
          required
        />

        <input
          type="text"
          name="postalCode"
          placeholder="Postal Code"
          value={formData.address.postalCode}
          onChange={handleChange}
          className="form-control"
          required
        />

        <input
          type="text"
          name="city"
          placeholder="City (optional for Geo JSON)"
          value={formData.address.city}
          onChange={handleChange}
          className="form-control"
        />

        <input
          type="text"
          name="province"
          placeholder="Province (optional for Geo JSON)"
          value={formData.address.province}
          onChange={handleChange}
          className="form-control"
        />

        <div className="d-flex gap-2 flex-wrap">
          <button type="submit" className="btn btn-success">
            Save Customer
          </button>

          <button
            type="button"
            className="btn btn-primary"
            onClick={handleGeoJsonSubmit}
          >
            Save With Geo JSON
          </button>
        </div>
      </form>
    </div>
  );
}

export default AddCustomer;