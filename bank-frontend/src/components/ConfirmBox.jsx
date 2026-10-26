function ConfirmBox({ message, onConfirm, onCancel }) {
  return (
    <div className="confirm-overlay">
      <div className="confirm-box">
        <h3>Confirm Action</h3>
        <p>{message}</p>

        <div className="confirm-actions">
          <button className="btn btn-danger" onClick={onConfirm}>
            Yes
          </button>
          <button className="btn btn-secondary" onClick={onCancel}>
            No
          </button>
        </div>
      </div>
    </div>
  );
}

export default ConfirmBox;