import { render, screen, waitFor } from '@testing-library/react';
import { vi } from 'vitest';
import CustomerList from '../pages/customers/CustomerList';
import { MemoryRouter } from 'react-router-dom';

vi.mock('../api/customerApi', () => ({
  getAllCustomers: vi.fn(),
  deleteCustomer: vi.fn(),
}));

vi.mock('../context/AuthContext', () => ({
  useAuth: () => ({ isAdmin: true }),
}));

import { getAllCustomers } from '../api/customerApi';

describe('CustomerList', () => {
  test('renders customers', async () => {
    getAllCustomers.mockResolvedValue({
      data: [
        {
          customerId: 1,
          customerName: 'Tarun',
          customerType: 'PERSON',
          address: { city: 'Toronto', province: 'ON', postalCode: 'M1' },
        },
      ],
    });

    render(
      <MemoryRouter>
        <CustomerList />
      </MemoryRouter>
    );

    expect(await screen.findByText(/tarun/i)).toBeInTheDocument();
  });
});