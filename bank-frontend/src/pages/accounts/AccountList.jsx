
// import { useEffect, useState } from 'react';
// import { Link } from 'react-router-dom';
// import {
//   deleteCheckingAccount,
//   deleteSavingsAccount,
//   getAllAccounts
// } from '../../api/accountApi';
// import Loader from '../../components/Loader';
// import AlertMessage from '../../components/AlertMessage';
// import { useAuth } from '../../context/AuthContext';

// function AccountList() {
//   const { isAdmin } = useAuth();

//   const [accounts, setAccounts] = useState([]);
//   const [loading, setLoading] = useState(true);
//   const [message, setMessage] = useState('');
//   const [error, setError] = useState('');

//   const loadAccounts = async () => {
//     try {
//       setLoading(true);
//       const response = await getAllAccounts();
//       setAccounts(response.data);
//       setError('');
//     } catch (err) {
//       setError('Failed to load accounts.');
//     } finally {
//       setLoading(false);
//     }
//   };

//   useEffect(() => {
//     loadAccounts();
//   }, []);

//   const handleDelete = async (account) => {
//     const confirmDelete = window.confirm(
//       'Are you sure you want to delete this account?'
//     );

//     if (!confirmDelete) return;

//     try {
//       if (account.accountType === 'SAVINGS') {
//         await deleteSavingsAccount(account.accountId);
//       } else {
//         await deleteCheckingAccount(account.accountId);
//       }

//       setMessage('Account deleted successfully.');
//       loadAccounts();
//     } catch (err) {
//       setError('Failed to delete account.');
//     }
//   };

//   if (loading) return <Loader />;

//   return (
//     <div className="page-card">
//       <div className="page-header">
//         <h2>View All Accounts</h2>
//       </div>

//       <AlertMessage type="success" message={message} />
//       <AlertMessage type="danger" message={error} />

//       <table className="table table-bordered table-striped">
//         <thead className="table-dark">
//           <tr>
//             <th>Account Id</th>
//             <th>Balance</th>
//             <th>Type</th>
//             <th>Interest Rate</th>
//             <th>Next Check Number</th>
//             <th>Customer Name</th>
//             <th>Actions</th>
//           </tr>
//         </thead>

//         <tbody>
//           {accounts.map((account) => (
//             <tr key={account.accountId}>
//               <td>{account.accountId}</td>
//               <td>{account.balance}</td>
//               <td>{account.accountType}</td>
//               <td>{account.interestRate || '-'}</td>
//               <td>{account.nextCheckNumber || '-'}</td>
//               <td>{account.customerName}</td>

//               <td>
//                 <div className="action-buttons">

//                   {/* VIEW for everyone */}
//                   <Link
//                     to={`/accounts/${account.accountId}`}
//                     className="btn btn-primary btn-sm"
//                   >
//                     View
//                   </Link>

//                   {/* ADMIN only */}
//                   {isAdmin && (
//                     <>
//                       {account.accountType === 'SAVINGS' ? (
//                         <Link
//                           to={`/accounts/savings/update/${account.accountId}`}
//                           className="btn btn-warning btn-sm"
//                         >
//                           Update
//                         </Link>
//                       ) : (
//                         <Link
//                           to={`/accounts/checking/update/${account.accountId}`}
//                           className="btn btn-warning btn-sm"
//                         >
//                           Update
//                         </Link>
//                       )}

//                       <button
//                         className="btn btn-danger btn-sm"
//                         onClick={() => handleDelete(account)}
//                       >
//                         Delete
//                       </button>
//                     </>
//                   )}

//                 </div>
//               </td>
//             </tr>
//           ))}
//         </tbody>
//       </table>
//     </div>
//   );
// }

// export default AccountList;

import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import {
  deleteCheckingAccount,
  deleteSavingsAccount,
  getAllAccounts
} from '../../api/accountApi';
import Loader from '../../components/Loader';
import AlertMessage from '../../components/AlertMessage';
import { useAuth } from '../../context/AuthContext';

function AccountList() {
  const { isAdmin } = useAuth();

  const [accounts, setAccounts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [message, setMessage] = useState('');
  const [error, setError] = useState('');
  const [currentPage, setCurrentPage] = useState(1);

  const recordsPerPage = 5;

  const loadAccounts = async () => {
    try {
      setLoading(true);
      const response = await getAllAccounts();
      setAccounts(response.data);
      setError('');
    } catch (err) {
      setError('Failed to load accounts.');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadAccounts();
  }, []);

  const handleDelete = async (account) => {
    const confirmDelete = window.confirm('Are you sure you want to delete this account?');
    if (!confirmDelete) return;

    try {
      if (account.accountType === 'SAVINGS') {
        await deleteSavingsAccount(account.accountId);
      } else {
        await deleteCheckingAccount(account.accountId);
      }

      setMessage('Account deleted successfully.');

      const updatedAccounts = accounts.filter((a) => a.accountId !== account.accountId);
      setAccounts(updatedAccounts);

      const totalPagesAfterDelete = Math.ceil(updatedAccounts.length / recordsPerPage);
      if (currentPage > totalPagesAfterDelete && totalPagesAfterDelete > 0) {
        setCurrentPage(totalPagesAfterDelete);
      }
    } catch (err) {
      setError('Failed to delete account.');
    }
  };

  const totalPages = Math.ceil(accounts.length / recordsPerPage);
  const startIndex = (currentPage - 1) * recordsPerPage;
  const endIndex = startIndex + recordsPerPage;
  const currentAccounts = accounts.slice(startIndex, endIndex);

  if (loading) return <Loader />;

  return (
    <div className="page-card">
      <div className="page-header">
        <h2>View All Accounts</h2>
      </div>

      <AlertMessage type="success" message={message} />
      <AlertMessage type="danger" message={error} />

      <table className="table table-bordered table-striped">
        <thead className="table-dark">
          <tr>
            <th>Account Id</th>
            <th>Balance</th>
            <th>Type</th>
            <th>Interest Rate</th>
            <th>Next Check Number</th>
            <th>Customer Name</th>
            <th>Actions</th>
          </tr>
        </thead>

        <tbody>
          {currentAccounts.map((account) => (
            <tr key={account.accountId}>
              <td>{account.accountId}</td>
              <td>{account.balance}</td>
              <td>{account.accountType}</td>
              <td>{account.interestRate || '-'}</td>
              <td>{account.nextCheckNumber || '-'}</td>
              <td>{account.customerName}</td>

              <td>
                <div className="action-buttons">
                  <Link
                    to={`/accounts/${account.accountId}`}
                    className="btn btn-primary btn-sm"
                  >
                    View
                  </Link>

                  {isAdmin && (
                    <>
                      {account.accountType === 'SAVINGS' ? (
                        <Link
                          to={`/accounts/savings/update/${account.accountId}`}
                          className="btn btn-warning btn-sm"
                        >
                          Update
                        </Link>
                      ) : (
                        <Link
                          to={`/accounts/checking/update/${account.accountId}`}
                          className="btn btn-warning btn-sm"
                        >
                          Update
                        </Link>
                      )}

                      <button
                        className="btn btn-danger btn-sm"
                        onClick={() => handleDelete(account)}
                      >
                        Delete
                      </button>
                    </>
                  )}
                </div>
              </td>
            </tr>
          ))}

          {currentAccounts.length === 0 && (
            <tr>
              <td colSpan="7" className="text-center">
                No accounts found.
              </td>
            </tr>
          )}
        </tbody>
      </table>

      {accounts.length > recordsPerPage && (
        <div className="pagination-container">
          <button
            className="btn btn-secondary btn-sm"
            onClick={() => setCurrentPage(currentPage - 1)}
            disabled={currentPage === 1}
          >
            Previous
          </button>

          <span className="pagination-info">
            Page {currentPage} of {totalPages}
          </span>

          <button
            className="btn btn-secondary btn-sm"
            onClick={() => setCurrentPage(currentPage + 1)}
            disabled={currentPage === totalPages}
          >
            Next
          </button>
        </div>
      )}
    </div>
  );
}

export default AccountList;