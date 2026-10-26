import { render, screen } from '@testing-library/react';
import { vi } from 'vitest';
import AccountList from '../pages/accounts/AccountList';
import { MemoryRouter } from 'react-router-dom';

vi.mock('../api/accountApi', () => ({
  getAllAccounts: vi.fn(),
}));

vi.mock('../context/AuthContext', () => ({
  useAuth: () => ({ isAdmin: true }),
}));

import { getAllAccounts } from '../api/accountApi';

test('shows accounts', async () => {
  getAllAccounts.mockResolvedValue({
    data: [
      {
        accountId: 1,
        balance: 100,
        accountType: 'SAVINGS',
        customerName: 'Tarun',
      },
    ],
  });

  render(
    <MemoryRouter>
      <AccountList />
    </MemoryRouter>
  );

  expect(await screen.findByText(/tarun/i)).toBeInTheDocument();
});