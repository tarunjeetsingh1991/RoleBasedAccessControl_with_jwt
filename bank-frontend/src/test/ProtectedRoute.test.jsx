import { render, screen } from '@testing-library/react';
import ProtectedRoute from '../components/ProtectedRoute';
import { MemoryRouter } from 'react-router-dom';
import { vi } from 'vitest';

vi.mock('../context/AuthContext', () => ({
  useAuth: () => ({
    isLoggedIn: true,
  }),
}));

test('renders child when logged in', () => {
  render(
    <MemoryRouter>
      <ProtectedRoute>
        <div>OK</div>
      </ProtectedRoute>
    </MemoryRouter>
  );

  expect(screen.getByText('OK')).toBeInTheDocument();
});