import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import { describe, it, expect, vi } from 'vitest';
import PortalLoginPage from '../../auth/PortalLoginPage';
import { MemoryRouter, Route, Routes } from 'react-router-dom';
import useAuthStore, { isTokenValid } from '../../../store/authStore';

vi.mock('../../../store/authStore', () => {
  const mockState = {
    login: vi.fn().mockResolvedValue(true),
    verifyMfa: vi.fn(),
    mfaPending: false,
    error: null,
    isLoading: false,
    mfaEmail: null
  };
  return {
    default: () => mockState,
    isTokenValid: vi.fn().mockReturnValue(false)
  };
});

describe('PortalLoginPage', () => {
  const renderWithProviders = (component) => {
    return render(
      <MemoryRouter initialEntries={['/doctor/login']}>
        <Routes>
          <Route path="/:portalSlug/login" element={component} />
        </Routes>
      </MemoryRouter>
    );
  };

  it('renders login form with portal title', () => {
    isTokenValid.mockReturnValue(false);
    renderWithProviders(<PortalLoginPage />);
    expect(screen.getByText(/Doctor Portal/i)).toBeInTheDocument();
    expect(screen.getByLabelText(/Staff ID \/ Username/i)).toBeInTheDocument();
    expect(screen.getByLabelText(/password/i)).toBeInTheDocument();
    expect(screen.getByRole('button', { name: /access dashboard/i })).toBeInTheDocument();
  });
});
