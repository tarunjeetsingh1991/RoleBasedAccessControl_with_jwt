import { Routes, Route } from "react-router-dom";

import Home from "./pages/Home";
import Login from "./pages/Login";

import CustomerList from "./pages/customers/CustomerList";
import CustomerDetails from "./pages/customers/CustomerDetails";
import AddCustomer from "./pages/customers/AddCustomer";
import UpdateCustomer from "./pages/customers/UpdateCustomer";
import FindById from "./pages/customers/FindById";

import AccountList from "./pages/accounts/AccountList";
import AddSavingsAccount from "./pages/accounts/AddSavingsAccount";
import AddCheckingAccount from "./pages/accounts/AddCheckingAccount";
import UpdateSavingsAccount from "./pages/accounts/UpdateSavingsAccount";
import UpdateCheckingAccount from "./pages/accounts/UpdateCheckingAccount";
import FindByBalance from "./pages/accounts/FindByBalance";

import ProtectedRoute from "./components/ProtectedRoute";
import AdminRoute from "./components/AdminRoute";
import FindAccountsByCustomer from './pages/accounts/FindAccountsByCustomer';
import FindAccountsByCustomerName from "./pages/accounts/FindAccountsByCustomerName";
import AccountDetails from "./pages/accounts/AccountDetails";

function AppRoutes() {
  return (
    <Routes>
      <Route path="/login" element={<Login />} />

      <Route
        path="/"
        element={
          <ProtectedRoute>
            <Home />
          </ProtectedRoute>
        }
      />

      <Route
        path="/customers"
        element={
          <ProtectedRoute>
            <CustomerList />
          </ProtectedRoute>
        }
      />

      <Route
        path="/customers/:id"
        element={
          <ProtectedRoute>
            <CustomerDetails />
          </ProtectedRoute>
        }
      />

      <Route
        path="/customers/find-by-id"
        element={
          <ProtectedRoute>
            <FindById />
          </ProtectedRoute>
        }
      />

      <Route
        path="/customers/add"
        element={
          <AdminRoute>
            <AddCustomer />
          </AdminRoute>
        }
      />

      <Route
        path="/customers/update/:id"
        element={
          <AdminRoute>
            <UpdateCustomer />
          </AdminRoute>
        }
      />

      <Route
        path="/accounts"
        element={
          <ProtectedRoute>
            <AccountList />
          </ProtectedRoute>
        }
      />

      <Route
        path="/accounts/find-by-balance"
        element={
          <ProtectedRoute>
            <FindByBalance />
          </ProtectedRoute>
        }
      />

      <Route
        path="/accounts/savings/add"
        element={
          <AdminRoute>
            <AddSavingsAccount />
          </AdminRoute>
        }
      />

      <Route
        path="/accounts/checking/add"
        element={
          <AdminRoute>
            <AddCheckingAccount />
          </AdminRoute>
        }
      />

      <Route
        path="/accounts/savings/update/:id"
        element={
          <AdminRoute>
            <UpdateSavingsAccount />
          </AdminRoute>
        }
      />

      <Route
        path="/accounts/checking/update/:id"
        element={
          <AdminRoute>
            <UpdateCheckingAccount />
          </AdminRoute>
        }
      />

      <Route
        path="/accounts/by-customer-id"
        element={
          <ProtectedRoute>
            <FindAccountsByCustomer />
          </ProtectedRoute>
        }
      />

      <Route
        path="/accounts/by-customer-name"
        element={
          <ProtectedRoute>
            <FindAccountsByCustomerName />
          </ProtectedRoute>
        }
      />
      <Route
        path="/accounts/:id"
        element={
          <ProtectedRoute>
            <AccountDetails />
          </ProtectedRoute>
        }
      />
    </Routes>
  );
}

export default AppRoutes;