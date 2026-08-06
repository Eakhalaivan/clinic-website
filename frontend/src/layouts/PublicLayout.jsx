import { useState } from 'react';
import { Link, Outlet, useLocation } from 'react-router-dom';
import { Menu, X } from 'lucide-react';
import './PublicLayout.css';
import ErrorBoundary from '../components/ui/ErrorBoundary';

const PublicLayout = () => {
  const [mobileMenuOpen, setMobileMenuOpen] = useState(false);
  const location = useLocation();

  const toggleMobileMenu = () => setMobileMenuOpen(!mobileMenuOpen);
  const closeMobileMenu = () => setMobileMenuOpen(false);

  const NavLinkItem = ({ to, label }) => {
    const isActive = location.pathname === to || (to !== '/' && location.pathname.startsWith(to + '/'));
    return (
        <Link 
            to={to} 
            className={`public-nav-link ${isActive ? 'active' : ''}`}
            onClick={closeMobileMenu}
        >
            {label}
        </Link>
    );
  };

  return (
    <ErrorBoundary>
    <div className="public-layout">
      {location.pathname !== '/' && (
        <header className="public-header">
        <Link to="/" className="public-brand">
          <div className="public-brand-dot"></div>
          <h1 className="public-brand-text">Aurelian Health</h1>
        </Link>
        <nav className="public-nav">
          <NavLinkItem to="/" label="Home" />
          <NavLinkItem to="/doctors" label="Our Doctors" />
          <Link to="/patient/login" className="btn-primary" style={{ marginLeft: 'var(--space-4)' }}>Patient Login</Link>
        </nav>
        <button className="mobile-menu-btn btn-ghost" onClick={toggleMobileMenu} aria-label="Toggle menu">
          {mobileMenuOpen ? <X size={24} /> : <Menu size={24} />}
        </button>
      </header>
      )}

      {/* Mobile Nav Menu */}
      <div className={`public-nav-mobile-overlay ${mobileMenuOpen ? 'is-open' : ''}`}>
        <NavLinkItem to="/" label="Home" />
        <NavLinkItem to="/doctors" label="Our Doctors" />
        <Link to="/patient/login" className="btn-primary" style={{ marginTop: 'var(--space-6)', textAlign: 'center' }} onClick={closeMobileMenu}>Patient Login</Link>
      </div>

      <main className="public-main">
        <Outlet />
      </main>
      {location.pathname !== '/' && (
        <footer className="public-footer">
          <p>&copy; 2026 Aurelian Health</p>
        </footer>
      )}
    </div>
    </ErrorBoundary>
  );
};

export default PublicLayout;
