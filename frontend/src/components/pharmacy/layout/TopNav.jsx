import React, { useState, useEffect, useRef } from 'react';
import { Bell, Search, Menu, X, AlertTriangle, ShieldAlert, Activity, LogOut, ChevronDown } from 'lucide-react';
import { useAuth } from '../../../context/pharmacy/AuthContext';
import { ROLES, ROLE_LABELS, DASHBOARD_ROUTES } from '../../../config/pharmacy/roles.config';
import api from '../../../utils/pharmacy/api';
import { useQuery } from '@tanstack/react-query';
import NotificationBell from '../../NotificationBell';
import { useNavigate } from 'react-router-dom';
import { AnimatePresence, motion } from 'framer-motion';

export default function TopNav({ onMenuClick }) {
  const { user, activeRole, roles, switchRole, logout } = useAuth();
  const [isAlertsOpen, setIsAlertsOpen] = useState(false);
  const [isRoleDropdownOpen, setIsRoleDropdownOpen] = useState(false);
  const dropdownRef = useRef(null);
  const navigate = useNavigate();

  useEffect(() => {
    function handleClickOutside(event) {
      if (dropdownRef.current && !dropdownRef.current.contains(event.target)) {
        setIsRoleDropdownOpen(false);
      }
    }
    document.addEventListener("mousedown", handleClickOutside);
    return () => document.removeEventListener("mousedown", handleClickOutside);
  }, []);

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  const { data: alerts } = useQuery({
    queryKey: ['top-nav-alerts'],
    queryFn: () => api.get('/pharmacy/dashboard/alerts').then(r => r.data?.data ?? []),
    staleTime: 30000,
    refetchInterval: 60000,
    enabled: !!activeRole
  });

  const getInitials = (name) => {
    if (!name) return 'HG';
    const parts = name.split(' ');
    if (parts.length >= 2) return (parts[0][0] + parts[1][0]).toUpperCase();
    return name.substring(0, 2).toUpperCase();
  };

  const SEVERITY_STYLES = {
    INFO:     { badge: 'bg-blue-100 text-blue-700',   icon: Activity,       iconColor: 'text-blue-500',   bg: 'bg-blue-50' },
    WARNING:  { badge: 'bg-amber-100 text-amber-700', icon: AlertTriangle,  iconColor: 'text-amber-500',  bg: 'bg-amber-50' },
    CRITICAL: { badge: 'bg-red-100 text-red-700',     icon: ShieldAlert,    iconColor: 'text-red-500',    bg: 'bg-red-50' },
  };

  const getRelativeTime = (isoStr) => {
    const diff = Date.now() - new Date(isoStr).getTime();
    const mins = Math.floor(diff / 60000);
    if (mins < 1) return 'Just now';
    if (mins < 60) return `${mins} mins ago`;
    const hrs = Math.floor(mins / 60);
    if (hrs < 24) return `${hrs} hour${hrs > 1 ? 's' : ''} ago`;
    return `${Math.floor(hrs / 24)} days ago`;
  };

  return (
    <>
      <header className="h-20 bg-white border-b border-gray-200 flex items-center justify-between px-4 sm:px-8 sticky top-0 z-10 shadow-sm">
        <div className="flex items-center gap-4">
          <div className="hidden sm:flex items-center gap-3">
            <div className="bg-blue-600 p-2 rounded-lg shadow-md shrink-0">
              <Activity className="w-5 h-5 text-white" />
            </div>
            <div>
              <h1 className="font-bold text-base leading-tight tracking-tight">PharmaDesk</h1>
            </div>
          </div>
        </div>

        <div className="flex items-center gap-4 sm:gap-6">
          {roles?.length > 1 && (
            <div className="relative" ref={dropdownRef}>
              <button 
                onClick={() => setIsRoleDropdownOpen(!isRoleDropdownOpen)}
                className="flex items-center gap-2 px-3 py-1.5 bg-gray-50 border border-gray-200 hover:bg-gray-100 rounded-lg transition-colors"
              >
                <div className="flex flex-col items-start min-w-0 hidden sm:flex">
                  <span className="text-[10px] text-gray-500 uppercase tracking-widest font-bold">Role</span>
                  <span className="text-sm font-medium text-gray-900 truncate">{ROLE_LABELS[activeRole] || activeRole || 'Staff'}</span>
                </div>
                <span className="sm:hidden text-xs font-semibold">{ROLE_LABELS[activeRole]?.split(' ')[0] || 'Role'}</span>
                <ChevronDown className={`w-4 h-4 text-gray-400 transition-transform ${isRoleDropdownOpen ? "rotate-180" : ""}`} />
              </button>
              
              <AnimatePresence>
                {isRoleDropdownOpen && (
                  <motion.div 
                    initial={{ opacity: 0, y: 10 }}
                    animate={{ opacity: 1, y: 0 }}
                    exit={{ opacity: 0, y: 10 }}
                    transition={{ duration: 0.2 }}
                    className="absolute top-full right-0 mt-2 w-48 bg-white border border-gray-100 rounded-lg shadow-xl z-50 py-1 overflow-hidden"
                  >
                    {roles.map(role => (
                      <button
                        key={role}
                        onClick={() => {
                          switchRole(role);
                          setIsRoleDropdownOpen(false);
                          navigate(DASHBOARD_ROUTES[role] || '/');
                        }}
                        className={`w-full px-3 py-2 text-left text-sm transition-colors ${activeRole === role ? "bg-blue-50 text-blue-700 font-medium" : "text-gray-700 hover:bg-gray-50"}`}
                      >
                        {ROLE_LABELS[role] || role}
                      </button>
                    ))}
                  </motion.div>
                )}
              </AnimatePresence>
            </div>
          )}

          <NotificationBell />

          <button 
            onClick={() => setIsAlertsOpen(true)}
            className="relative p-2 text-gray-500 hover:text-primary transition-colors"
          >
            <Bell className="w-5 h-5" />
            {alerts && alerts.length > 0 && (
              <span className="absolute top-0 right-0 min-w-4 min-h-4 px-1 bg-red-500 rounded-full text-[10px] font-bold text-white flex items-center justify-center border-2 border-white translate-x-1/4 -translate-y-1/4">
                {alerts.length > 99 ? '99+' : alerts.length}
              </span>
            )}
          </button>
          
          <div className="flex items-center gap-3 pl-4 sm:pl-6 border-l border-gray-200">
            <div className="text-right hidden sm:block">
              <p className="text-sm font-semibold text-gray-900">{user?.name || 'Hospital Staff'}</p>
              <p className="text-xs text-gray-500">{user?.branch || 'Main Branch'}</p>
            </div>
            <div className="w-9 h-9 rounded-full bg-primary/10 flex items-center justify-center text-primary font-bold">
              {getInitials(user?.name)}
            </div>
            <button
              onClick={handleLogout}
              className="p-1.5 ml-1 text-gray-400 hover:text-red-500 transition-colors"
              title="Logout"
            >
              <LogOut size={18} />
            </button>
          </div>
        </div>
      </header>

      {/* Alerts Drawer */}
      {isAlertsOpen && (
        <div className="fixed inset-0 z-50 flex justify-end bg-black/20">
          <div className="w-full max-w-md bg-white h-full shadow-2xl flex flex-col animate-in slide-in-from-right duration-300">
            <div className="px-6 py-4 border-b border-gray-100 flex items-center justify-between">
              <div>
                <h2 className="text-lg font-bold text-gray-900">System Alerts</h2>
                <p className="text-sm text-gray-500">{alerts?.length || 0} active alerts</p>
              </div>
              <button 
                onClick={() => setIsAlertsOpen(false)}
                className="p-2 hover:bg-gray-100 rounded-lg transition-colors text-gray-500"
              >
                <X className="w-5 h-5" />
              </button>
            </div>
            
            <div className="flex-1 overflow-y-auto p-6 space-y-4">
              {alerts && alerts.length > 0 ? (
                alerts.map(alert => {
                  const style = SEVERITY_STYLES[alert.severity] || SEVERITY_STYLES.INFO;
                  const Icon = style.icon;
                  return (
                    <div key={alert.id} className="flex gap-3 py-3 border-b border-gray-50 last:border-0">
                      <div className={`w-8 h-8 rounded-lg flex items-center justify-center flex-shrink-0 ${style.bg}`}>
                        <Icon className={`w-4 h-4 ${style.iconColor}`} />
                      </div>
                      <div className="flex-1 min-w-0">
                        <div className="flex items-center justify-between gap-2 mb-0.5">
                          <span className="text-sm font-semibold text-gray-800 truncate">{alert.title}</span>
                          <div className="flex items-center gap-2 flex-shrink-0">
                            <span className="text-xs text-gray-400">{getRelativeTime(alert.createdAt)}</span>
                            <span className={`text-[10px] font-bold px-2 py-0.5 rounded-full ${style.badge}`}>
                              {alert.severity}
                            </span>
                          </div>
                        </div>
                        <p className="text-xs text-gray-500 leading-relaxed line-clamp-2">{alert.description}</p>
                      </div>
                    </div>
                  );
                })
              ) : (
                <p className="text-center text-sm text-gray-500 py-10">No active alerts at this time.</p>
              )}
            </div>
          </div>
        </div>
      )}
    </>
  );
}
