import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import { vi } from 'vitest';
import { MemoryRouter } from 'react-router-dom';
import UpdateCustomer from '../pages/customers/UpdateCustomer';

vi.mock('../api/customerApi', () => ({
  getCustomerById: vi.fn(),
  updateCustomer: vi.fn(),
}));

const mockNavigate = vi.fn();

vi.mock('react-router-dom', async () => {
  const actual = await vi.importActual('react-router-dom');
  return {
    ...actual,
    useNavigate: () => mockNavigate,
    useParams: () => ({ id: '1' }),
  };
});

import { getCustomerById, updateCustomer } from '../api/customerApi';

describe('UpdateCustomer', () => {
  test('loads customer details', async () => {
    getCustomerById.mockResolvedValue({
      data: {
        customerType: 'PERSON',
        customerName: 'Tarun',
        address: {
          streetNumber: '123 Main',
          postalCode: 'L6Y3N8',
          city: 'Brampton',
          province: 'ON',
        },
      },
    });

    render(
      <MemoryRouter>
        <UpdateCustomer />
      </MemoryRouter>
    );

    expect(await screen.findByDisplayValue('Tarun')).toBeInTheDocument();
    expect(screen.getByDisplayValue('L6Y3N8')).toBeInTheDocument();
  });

  test('submits update payload', async () => {
    getCustomerById.mockResolvedValue({
      data: {
        customerType: 'PERSON',
        customerName: 'Tarun',
        address: {
          streetNumber: '123 Main',
          postalCode: 'L6Y3N8',
          city: 'Brampton',
          province: 'ON',
        },
      },
    });

    updateCustomer.mockResolvedValue({ data: {} });

    render(
      <MemoryRouter>
        <UpdateCustomer />
      </MemoryRouter>
    );

    await screen.findByDisplayValue('Tarun');

    fireEvent.change(screen.getByPlaceholderText(/postal code/i), {
      target: { value: 'L6Y4N9' },
    });

    fireEvent.click(screen.getByRole('button', { name: /update customer/i }));

    await waitFor(() => {
      expect(updateCustomer).toHaveBeenCalled();
    });
  });
});