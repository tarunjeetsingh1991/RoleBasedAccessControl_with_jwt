import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import { vi } from 'vitest';
import { MemoryRouter } from 'react-router-dom';
import AddCustomer from '../pages/customers/AddCustomer';

vi.mock('../api/customerApi', () => ({
  createCustomer: vi.fn(),
  createCustomerWithGeoJson: vi.fn(),
}));

const mockNavigate = vi.fn();

vi.mock('react-router-dom', async () => {
  const actual = await vi.importActual('react-router-dom');
  return {
    ...actual,
    useNavigate: () => mockNavigate,
  };
});

import {
  createCustomer,
  createCustomerWithGeoJson,
} from '../api/customerApi';

describe('AddCustomer', () => {
  test('renders add customer form', () => {
    render(
      <MemoryRouter>
        <AddCustomer />
      </MemoryRouter>
    );

    expect(screen.getByText(/add customer/i)).toBeInTheDocument();
    expect(screen.getByPlaceholderText(/customer name/i)).toBeInTheDocument();
    expect(screen.getByPlaceholderText(/street number/i)).toBeInTheDocument();
    expect(screen.getByPlaceholderText(/postal code/i)).toBeInTheDocument();
  });

  test('submits normal create customer', async () => {
    createCustomer.mockResolvedValue({ data: {} });

    render(
      <MemoryRouter>
        <AddCustomer />
      </MemoryRouter>
    );

    fireEvent.change(screen.getByPlaceholderText(/customer name/i), {
      target: { value: 'Tarun' },
    });
    fireEvent.change(screen.getByPlaceholderText(/street number/i), {
      target: { value: '123 Main St' },
    });
    fireEvent.change(screen.getByPlaceholderText(/postal code/i), {
      target: { value: 'L6Y3N8' },
    });
    fireEvent.change(screen.getByPlaceholderText(/city/i), {
      target: { value: 'Brampton' },
    });
    fireEvent.change(screen.getByPlaceholderText(/province/i), {
      target: { value: 'ON' },
    });

    fireEvent.click(screen.getByRole('button', { name: /save customer/i }));

    await waitFor(() => {
      expect(createCustomer).toHaveBeenCalled();
    });
  });

  test('submits geo json customer create', async () => {
    createCustomerWithGeoJson.mockResolvedValue({ data: {} });

    render(
      <MemoryRouter>
        <AddCustomer />
      </MemoryRouter>
    );

    fireEvent.change(screen.getByPlaceholderText(/customer name/i), {
      target: { value: 'Tarun' },
    });
    fireEvent.change(screen.getByPlaceholderText(/street number/i), {
      target: { value: '123 Main St' },
    });
    fireEvent.change(screen.getByPlaceholderText(/postal code/i), {
      target: { value: 'L6Y3N8' },
    });

    fireEvent.click(
      screen.getByRole('button', { name: /save with geo json/i })
    );

    await waitFor(() => {
      expect(createCustomerWithGeoJson).toHaveBeenCalled();
    });
  });
});