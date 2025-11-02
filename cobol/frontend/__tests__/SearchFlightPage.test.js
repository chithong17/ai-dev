import React from 'react';
import { render, screen } from '@testing-library/react';
import SearchFlightPage from '../src/pages/SearchFlightPage';
import { BrowserRouter } from 'react-router-dom';

test('renders search inputs', () => {
  render(<BrowserRouter><SearchFlightPage /></BrowserRouter>);
  expect(screen.getByPlaceholderText(/YYYY-MM-DD/i)).toBeInTheDocument();
});
