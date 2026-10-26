import { render, screen } from '@testing-library/react';
import FindByBalance from '../pages/accounts/FindByBalance';
import { MemoryRouter } from 'react-router-dom';

test('renders balance search', () => {
  render(
    <MemoryRouter>
      <FindByBalance />
    </MemoryRouter>
  );

  expect(screen.getByText(/balance/i)).toBeInTheDocument();
});