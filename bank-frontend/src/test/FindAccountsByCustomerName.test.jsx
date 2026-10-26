import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import { vi } from 'vitest';
import { MemoryRouter } from 'react-router-dom';
import FindAccountsByCustomerName from '../pages/accounts/FindAccountsByCustomerName';

vi.mock('../api/accountApi', () => ({
  getAccountsByCustomerName: vi.fn(),
}));

import { getAccountsByCustomerName } from '../api/accountApi';

describe('FindAccountsByCustomerName', () => {
  test('renders customer name search page', () => {
    render(
      <MemoryRouter>
        <FindAccountsByCustomerName />
      </MemoryRouter>
    );

    expect(screen.getByText(/accounts by customer name/i)).toBeInTheDocument();
    expect(screen.getByPlaceholderText(/customer name/i)).toBeInTheDocument();
  });

  test('shows validation if input is empty', async () => {
    render(
      <MemoryRouter>
        <FindAccountsByCustomerName />
      </MemoryRouter>
    );

    fireEvent.click(screen.getByRole('button', { name: /search/i }));

    expect(await screen.findByText(/please enter customer name/i)).toBeInTheDocument();
  });

  test('searches accounts by customer name', async () => {
    getAccountsByCustomerName.mockResolvedValue({
      data: [
        {
          accountId: 1,
          balance: 5000,
          accountType: 'SAVINGS',
          customerName: 'Tarun',
          interestRate: 2.5,
        },
      ],
    });

    render(
      <MemoryRouter>
        <FindAccountsByCustomerName />
      </MemoryRouter>
    );

    fireEvent.change(screen.getByPlaceholderText(/customer name/i), {
      target: { value: 'Tarun' },
    });

    fireEvent.click(screen.getByRole('button', { name: /search/i }));

    await waitFor(() => {
      expect(getAccountsByCustomerName).toHaveBeenCalledWith('Tarun');
    });

    expect(await screen.findByText(/found 1 account/i)).toBeInTheDocument();
  });
});