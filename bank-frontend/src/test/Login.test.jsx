import { screen, fireEvent, waitFor } from '@testing-library/react';
import { vi } from 'vitest';
import Login from '../pages/Login';
import { renderWithProviders } from './testUtils';

vi.mock('../api/authApi', () => ({
  loginUser: vi.fn(),
}));

import { loginUser } from '../api/authApi';

describe('Login', () => {
  test('shows validation error', async () => {
    renderWithProviders(<Login />);

    fireEvent.click(screen.getByRole('button', { name: /login/i }));

    expect(
      await screen.findByText(/please enter username/i)
    ).toBeInTheDocument();
  });

  test('calls login api', async () => {
    loginUser.mockResolvedValue({
      data: {
        token: 'fake',
        username: 'tarun',
        role: 'ROLE_ADMIN',
      },
    });

    renderWithProviders(<Login />);

    fireEvent.change(
      screen.getByPlaceholderText(/username/i),
      { target: { value: 'tarun' } }
    );

    fireEvent.change(
      screen.getByPlaceholderText(/password/i),
      { target: { value: '123' } }
    );

    fireEvent.click(screen.getByRole('button', { name: /login/i }));

    await waitFor(() => {
      expect(loginUser).toHaveBeenCalled();
    });
  });
});