import { MemoryRouter } from 'react-router-dom';
import { AuthProvider } from '../context/AuthContext';
import { render } from '@testing-library/react';

export function renderWithProviders(ui) {
  return render(
    <MemoryRouter>
      <AuthProvider>
        {ui}
      </AuthProvider>
    </MemoryRouter>
  );
}