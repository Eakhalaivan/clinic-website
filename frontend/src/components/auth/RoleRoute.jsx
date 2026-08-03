import React from 'react';
import { Navigate, useLocation } from 'react-router-dom';
import useAuthStore, { isTokenValid } from '../../store/authStore';
import { getPortalConfig } from '../../config/portalConfig';

export default function RoleRoute({ portalSlug, allowedRoles, children }) {
    const { token, roles = [] } = useAuthStore();
    const location = useLocation();

    // Treat expired tokens as unauthenticated — redirect to login
    if (!isTokenValid(token)) {
        return <Navigate to={`/${portalSlug || 'patient'}/login`} state={{ from: location }} replace />;
    }

    const portalConfig = getPortalConfig(portalSlug);
    const targetRoles = allowedRoles || (portalConfig.role ? [portalConfig.role, 'ROLE_SUPER_ADMIN'] : []);

    const userRoles = roles || [];
    const hasPermission = userRoles.includes('ROLE_ADMIN') || 
                          userRoles.includes('ROLE_SUPER_ADMIN') ||
                          (targetRoles.length === 0) ||
                          targetRoles.some(r => userRoles.includes(r));

    if (!hasPermission) {
        return <Navigate to="/unauthorized" replace />;
    }

    return children;
}
