import { render, screen } from '@testing-library/react';
import Sidebar from '../components/Sidebar';
import { MemoryRouter } from 'react-router-dom';
import { vi } from 'vitest';

vi.mock('../context/AuthContext', () => ({
  useAuth: () => ({
    isLoggedIn: true,
    isAdmin: true,
  }),
}));

test('shows sidebar links', () => {
  render(
    <MemoryRouter>
      <Sidebar />
    </MemoryRouter>
  );

  expect(screen.getByText(/home/i)).toBeInTheDocument();
});