import React from 'react';
import { render, screen } from '@testing-library/react';
import BookingStep1 from '../src/pages/BookingStep1';
import { BrowserRouter } from 'react-router-dom';

test('renders booking fields', () => {
  render(<BrowserRouter><BookingStep1 /></BrowserRouter>);
  expect(screen.getByLabelText(/CLIIDI/i)).toBeInTheDocument();
  expect(screen.getByLabelText(/PASSNI/i)).toBeInTheDocument();
});
