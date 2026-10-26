import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import { vi } from 'vitest';
import { MemoryRouter } from 'react-router-dom';
import FindAccountsByCustomer from '../pages/accounts/FindAccountsByCustomer';

vi.mock('../api/accountApi', () => ({
  getAccountsByCustomerId: vi.fn(),
}));

import { getAccountsByCustomerId } from '../api/accountApi';

describe('FindAccountsByCustomer', () => {
  test('renders search by customer id page', () => {
    render(
      <MemoryRouter>
        <FindAccountsByCustomer />
      </MemoryRouter>
    );

    expect(screen.getByText(/accounts by customer/i)).toBeInTheDocument();
    expect(screen.getByPlaceholderText(/customer id/i)).toBeInTheDocument();
  });

  test('searches accounts by customer id', async () => {
    getAccountsByCustomerId.mockResolvedValue({
      data: [
        {
          accountId: 1,
          balance: 2000,
          accountType: 'CHECKING',
          customerName: 'Tarun',
        },
      ],
    });

    render(
      <MemoryRouter>
        <FindAccountsByCustomer />
      </MemoryRouter>
    );

    fireEvent.change(screen.getByPlaceholderText(/customer id/i), {
      target: { value: '1' },
    });

    fireEvent.click(screen.getByRole('button', { name: /search/i }));

    await waitFor(() => {
      expect(getAccountsByCustomerId).toHaveBeenCalledWith('1');
    });

    expect(await screen.findByText(/checking/i)).toBeInTheDocument();
  });
});