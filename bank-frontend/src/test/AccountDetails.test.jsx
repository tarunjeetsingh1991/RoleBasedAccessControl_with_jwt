import { render, screen } from '@testing-library/react';
import { vi } from 'vitest';
import { MemoryRouter } from 'react-router-dom';
import AccountDetails from '../pages/accounts/AccountDetails';

vi.mock('../api/accountApi', () => ({
  getAccountById: vi.fn(),
}));

vi.mock('react-router-dom', async () => {
  const actual = await vi.importActual('react-router-dom');
  return {
    ...actual,
    useParams: () => ({ id: '1' }),
  };
});

import { getAccountById } from '../api/accountApi';

describe('AccountDetails', () => {
  test('renders account details', async () => {
    getAccountById.mockResolvedValue({
      data: {
        accountId: 1,
        balance: 2500,
        accountType: 'SAVINGS',
        interestRate: 2.5,
        nextCheckNumber: null,
        customerName: 'Tarun',
      },
    });

    render(
      <MemoryRouter>
        <AccountDetails />
      </MemoryRouter>
    );

    expect(await screen.findByText(/account details/i)).toBeInTheDocument();
    expect(screen.getByText('2500')).toBeInTheDocument();
    expect(screen.getByText(/tarun/i)).toBeInTheDocument();
  });
});