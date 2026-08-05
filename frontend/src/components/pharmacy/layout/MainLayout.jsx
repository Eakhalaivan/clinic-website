import React from 'react';
import { Outlet, useLocation } from 'react-router-dom';
import TopNav from './TopNav';
import { useAuth } from '../../../context/pharmacy/AuthContext';
import { NAV_BY_ROLE, getBaseRoleForUI, ROLE_LABELS } from '../../../config/pharmacy/roles.config';
import DashboardGrid from '../../dashboard/DashboardGrid';
import { ChevronRight } from 'lucide-react';

import Sidebar from './Sidebar';

export default function MainLayout() {
  const { activeRole, roles } = useAuth();
  const location = useLocation();
  const currentRole = activeRole || roles?.[0] || 'SYSTEM_ADMIN';
  const baseRole = getBaseRoleForUI(currentRole);
  
  // NAV_BY_ROLE[baseRole] gives the array of tiles.
  const dashboardTiles = NAV_BY_ROLE[baseRole] || NAV_BY_ROLE.PHARMACY_STAFF;

  // Derive theme color from role colors (which are tailwind classes). We'll map a few or just provide a default hex.
  const getRoleHexColor = (role) => {
    switch(role) {
      case 'SYSTEM_ADMIN': return '#1e293b'; // slate-800
      case 'SUPERVISOR': return '#7e22ce'; // purple-700
      case 'SENIOR_MEDICAL_STAFF': return '#0f766e'; // teal-700
      case 'MEDICAL_STAFF': return '#047857'; // emerald-700
      case 'BILLING_STAFF': return '#b45309'; // amber-700
      case 'PHARMACY_STAFF': return '#1d4ed8'; // blue-700
      case 'RECEPTIONIST': return '#be123c'; // rose-700
      case 'AUDIT_COMPLIANCE': return '#c2410c'; // orange-700
      case 'LAB_TECHNICIAN': return '#0e7490'; // cyan-700
      case 'STOREKEEPER': return '#44403c'; // stone-700
      default: return '#1d4ed8'; // blue-700
    }
  };

  const themeColor = getRoleHexColor(baseRole);

  const dashboardPath = dashboardTiles.length > 0 ? dashboardTiles[0].path : '/';
  const isDashboardHome = location.pathname === dashboardPath || location.pathname === dashboardPath + '/';

  const activeTile = dashboardTiles.find(t => {
    const path = t.path || '/';
    return location.pathname === path || 
      (path !== '/' && location.pathname.startsWith(`${path}/`)) ||
      (path === '/' && location.pathname.startsWith('/dashboard'));
  });

  const currentPageName = activeTile ? (activeTile.label || activeTile.name) : 'Overview';
  const portalName = ROLE_LABELS[currentRole] || 'Pharmacy';

  return (
    <div className="h-screen bg-gray-50 flex flex-col font-sans overflow-hidden">
      <TopNav />
      <div className="flex flex-1 overflow-hidden">
        {dashboardTiles && dashboardTiles.length > 0 && (
          <Sidebar items={dashboardTiles} themeColor={themeColor} />
        )}
        <main className="flex-1 p-4 sm:p-6 lg:p-8 w-full flex flex-col overflow-y-auto">
          
          {/* Breadcrumb */}
          <div className="flex items-center space-x-2 text-sm font-medium mb-4 shrink-0">
            <span className="text-gray-500">{portalName}</span>
            <ChevronRight size={14} className="text-gray-400" />
            <span className="text-gray-900">{currentPageName}</span>
          </div>

          <div className="flex-1 bg-white rounded-xl shadow-sm border border-gray-200 p-4 sm:p-6 flex flex-col">
            <Outlet />
          </div>
        </main>
      </div>
    </div>
  );
}
