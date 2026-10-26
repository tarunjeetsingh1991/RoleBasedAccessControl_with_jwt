import { render, screen } from '@testing-library/react';
import AdminRoute from '../components/AdminRoute';
import { MemoryRouter } from 'react-router-dom';
import { vi } from 'vitest';

vi.mock('../context/AuthContext', () => ({
  useAuth: () => ({
    isLoggedIn: true,
    isAdmin: true,
  }),
}));

test('admin allowed', () => {
  render(
    <MemoryRouter>
      <AdminRoute>
        <div>ADMIN</div>
      </AdminRoute>
    </MemoryRouter>
  );

  expect(screen.getByText('ADMIN')).toBeInTheDocument();
});