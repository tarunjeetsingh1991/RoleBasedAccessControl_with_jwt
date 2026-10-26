import { render, screen, fireEvent } from '@testing-library/react';
import FindById from '../pages/customers/FindById';
import { MemoryRouter } from 'react-router-dom';

test('renders input', () => {
  render(
    <MemoryRouter>
      <FindById />
    </MemoryRouter>
  );

  expect(screen.getByPlaceholderText(/id/i)).toBeInTheDocument();
});