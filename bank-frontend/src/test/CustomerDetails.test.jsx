import { render, screen, waitFor } from '@testing-library/react';
import { vi } from 'vitest';
import { MemoryRouter } from 'react-router-dom';
import CustomerDetails from '../pages/customers/CustomerDetails';

vi.mock('../api/customerApi', () => ({
  getCustomerById: vi.fn(),
}));

vi.mock('../api/accountApi', () => ({
  getAccountsByCustomerId: vi.fn(),
}));

vi.mock('react-router-dom', async () => {
  const actual = await vi.importActual('react-router-dom');
  return {
    ...actual,
    useParams: () => ({ id: '1' }),
  };
});

import { getCustomerById } from '../api/customerApi';
import { getAccountsByCustomerId } from '../api/accountApi';

describe('CustomerDetails', () => {
  test('renders customer details and accounts', async () => {
    getCustomerById.mockResolvedValue({
      data: {
        customerId: 1,
        customerName: 'Tarun',
        customerType: 'PERSON',
        address: {
          streetNumber: '123 Main',
          city: 'Brampton',
          province: 'ON',
          postalCode: 'L6Y3N8',
        },
      },
    });

    getAccountsByCustomerId.mockResolvedValue({
      data: [
        {
          accountId: 10,
          accountType: 'SAVINGS',
          balance: 1000,
          interestRate: 2.5,
        },
      ],
    });

    render(
      <MemoryRouter>
        <CustomerDetails />
      </MemoryRouter>
    );

    expect(await screen.findByText(/tarun/i)).toBeInTheDocument();

    await waitFor(() => {
      expect(screen.getByText(/savings/i)).toBeInTheDocument();
    });
  });
});