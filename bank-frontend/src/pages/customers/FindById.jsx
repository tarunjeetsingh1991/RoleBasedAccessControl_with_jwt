import { useState } from "react";
import { getCustomerById } from "../../api/customerApi";
import AlertMessage from "../../components/AlertMessage";

function FindById() {
  const [id, setId] = useState("");
  const [customer, setCustomer] = useState(null);
  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");
  const [loading, setLoading] = useState(false);

  const handleSearch = async (e) => {
    e.preventDefault();

    setError("");
    setSuccess("");
    setCustomer(null);

    if (!id) {
      setError("Please enter customer id");
      return;
    }

    try {
      setLoading(true);

      const response = await getCustomerById(id);

      setCustomer(response.data);
      setSuccess("Customer found");
    } catch (err) {
      setError("Customer not found");
    } finally {
      setLoading(false);
    }
  };

  const handleReset = () => {
    setId("");
    setCustomer(null);
    setError("");
    setSuccess("");
  };

  return (
    <div className="page-card">
      <h2>Find Customer By Id</h2>

      <AlertMessage type="danger" message={error} />
      <AlertMessage type="success" message={success} />

      <form onSubmit={handleSearch} className="form-grid mb-4">
        <input
          type="number"
          placeholder="Enter customer id"
          value={id}
          onChange={(e) => setId(e.target.value)}
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

      {customer && (
        <div className="detail-box">
          <p><strong>Customer Id:</strong> {customer.customerId}</p>
          <p><strong>Name:</strong> {customer.customerName}</p>
          <p><strong>Type:</strong> {customer.customerType}</p>

          {customer.address && (
            <>
              <p><strong>Street:</strong> {customer.address.streetNumber}</p>
              <p><strong>City:</strong> {customer.address.city}</p>
              <p><strong>Province:</strong> {customer.address.province}</p>
              <p><strong>Postal Code:</strong> {customer.address.postalCode}</p>
            </>
          )}
        </div>
      )}
    </div>
  );
}

export default FindById;