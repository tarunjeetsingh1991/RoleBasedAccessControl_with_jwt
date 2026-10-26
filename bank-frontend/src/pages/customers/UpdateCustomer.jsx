import { useEffect, useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { getCustomerById, updateCustomer } from '../../api/customerApi';
import AlertMessage from '../../components/AlertMessage';

function UpdateCustomer() {
  const { id } = useParams();
  const navigate = useNavigate();

  const [formData, setFormData] = useState({
    customerType: '',
    customerName: '',
    address: {
      streetNumber: '',
      postalCode: '',
      city: '',
      province: '',
    },
  });

  const [error, setError] = useState('');

  useEffect(() => {
    const fetchCustomer = async () => {
      try {
        const response = await getCustomerById(id);
        const customer = response.data;

        setFormData({
          customerType: customer.customerType || '',
          customerName: customer.customerName || '',
          address: {
            streetNumber: customer.address?.streetNumber || '',
            postalCode: customer.address?.postalCode || '',
            city: customer.address?.city || '',
            province: customer.address?.province || '',
          },
        });
      } catch (err) {
        setError('Failed to load customer.');
      }
    };

    fetchCustomer();
  }, [id]);

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

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');

    try {
      const payload = {
        customerType: formData.customerType,
        address: {
          postalCode: formData.address.postalCode,
          city: formData.address.city,
          province: formData.address.province,
        },
      };

      await updateCustomer(id, payload);
      navigate('/customers');
    } catch (err) {
      setError('Failed to update customer.');
    }
  };

  return (
    <div className="page-card">
      <h2>Update Customer</h2>
      <AlertMessage type="danger" message={error} />

      <form onSubmit={handleSubmit} className="form-grid">
        <input
          type="text"
          value={formData.customerType}
          disabled
          className="form-control"
        />

        <input
          type="text"
          value={formData.customerName}
          disabled
          className="form-control"
        />

        <input
          type="text"
          name="streetNumber"
          placeholder="Street Number"
          value={formData.address.streetNumber}
          onChange={handleChange}
          className="form-control"
          disabled
        />

        <input
          type="text"
          name="postalCode"
          placeholder="Postal Code"
          value={formData.address.postalCode}
          onChange={handleChange}
          className="form-control"
        />

        <input
          type="text"
          name="city"
          placeholder="City"
          value={formData.address.city}
          onChange={handleChange}
          className="form-control"
        />

        <input
          type="text"
          name="province"
          placeholder="Province"
          value={formData.address.province}
          onChange={handleChange}
          className="form-control"
        />

        <button className="btn btn-warning" type="submit">
          Update Customer
        </button>
      </form>
    </div>
  );
}

export default UpdateCustomer;