import React from 'react';
import { render, screen, fireEvent } from '@testing-library/react';
import LoginPage from '../src/pages/LoginPage';
import { BrowserRouter } from 'react-router-dom';

test('renders login fields', () => {
  render(<BrowserRouter><LoginPage /></BrowserRouter>);
  expect(screen.getByLabelText(/USERIDI/i)).toBeInTheDocument();
  expect(screen.getByLabelText(/PASSWI/i)).toBeInTheDocument();
});
