import React, { useState } from 'react';
import { Outlet, useLocation, Navigate, useNavigate, Link } from 'react-router-dom';
import { useQuery } from '@tanstack/react-query';
import { axiosPrivate } from '../api/axios';
import { 
  LogOut, ChevronDown, Search, Bell, Moon, MessageSquare, 
  Stethoscope, ShieldPlus, Plus, Zap, Activity, ArrowLeft
} from 'lucide-react';
import { getPortalConfig } from '../config/portalConfig';
import useAuthStore, { isTokenValid } from '../store/authStore';
import DashboardGrid from '../components/dashboard/DashboardGrid';
import ThemeToggle from '../components/ui/ThemeToggle';
import ErrorBoundary from '../components/ui/ErrorBoundary';
import CommandPalette from '../components/ui/CommandPalette';

const DashboardLayout = ({ portalSlug, allowedRoles }) => {
  const [isQuickActionOpen, setIsQuickActionOpen] = useState(false);
  const [isNotificationsOpen, setIsNotificationsOpen] = useState(false);
  const [isSearchOpen, setIsSearchOpen] = useState(false);
  const { token, user, roles = [], logout } = useAuthStore();
  const location = useLocation();
  const navigate = useNavigate();

  React.useEffect(() => {
    const handleKeyDown = (e) => {
      if ((e.metaKey || e.ctrlKey) && e.key === 'k') {
        e.preventDefault();
        setIsSearchOpen(true);
      }
    };
    window.addEventListener('keydown', handleKeyDown);
    return () => window.removeEventListener('keydown', handleKeyDown);
  }, []);

  if (!isTokenValid(token)) return <Navigate to={`/${portalSlug || 'patient'}/login`} replace />;
  const userRoles = roles || [];
  const hasPermission = userRoles.includes('ROLE_ADMIN') || 
                        userRoles.includes('ROLE_SUPER_ADMIN') ||
                        !allowedRoles ||
                        allowedRoles.some((r) => userRoles.includes(r));

  if (!hasPermission) {
    return <Navigate to="/unauthorized" replace />;
  }

  const portalConfig = getPortalConfig(portalSlug);
  const { displayName, dashboardTiles = [] } = portalConfig;

  const { data: unreadCountData } = useQuery({
    queryKey: ['unreadNotifications', user?.id],
    queryFn: async () => {
      try {
        const res = await axiosPrivate.get('/notifications/unread-count');
        return res.data;
      } catch (err) {
        return { count: 0 };
      }
    },
    enabled: !!user?.id,
    refetchInterval: 30000 // Poll every 30s
  });
  const unreadCount = unreadCountData?.count || 0;

  const userName = user?.email?.split('@')[0] || 'John Smith';
  const displayTitle = displayName || 'Doctor';

  return (
    <ErrorBoundary>
    <div className="h-screen overflow-hidden bg-[var(--color-bg-app)] flex flex-col font-sans transition-colors duration-200">
      {/* Top Header */}
      <header className="bg-white border-b border-slate-200 sticky top-0 z-50 shrink-0">
        <div className="px-6 py-3 flex items-center justify-between">
          {/* Logo and Brand */}
          <div className="flex items-center gap-3">
            {location.pathname !== `/${portalSlug}/dashboard` && location.pathname !== `/${portalSlug}/dashboard/` && (
              <button 
                onClick={() => navigate(-1)}
                className="p-2 mr-1 hover:bg-slate-100 rounded-full text-slate-600 transition flex items-center justify-center cursor-pointer"
                title="Go Back"
              >
                <ArrowLeft size={20} />
              </button>
            )}
            <Link to={`/${portalSlug}/dashboard`} className="flex items-center gap-3">
              <div className="w-10 h-10 bg-indigo-600 rounded-xl flex items-center justify-center shadow-xs cursor-pointer hover:bg-indigo-700 transition">
                <svg className="w-6 h-6 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path d="M4.318 6.318a4.5 4.5 0 000 6.364L12 20.364l7.682-7.682a4.5 4.5 0 00-6.364-6.364L12 7.636l-1.318-1.318a4.5 4.5 0 00-6.364 0z" strokeLinecap="round" strokeLinejoin="round" strokeWidth="2"></path>
                </svg>
              </div>
              <div>
                <h1 className="text-xl font-bold tracking-tight text-indigo-900 leading-none cursor-pointer hover:text-indigo-700 transition">AURELIAN HEALTH</h1>
                <p className="text-[10px] font-semibold text-slate-400 uppercase tracking-widest mt-1">
                  {portalSlug ? `${portalSlug.charAt(0).toUpperCase() + portalSlug.slice(1)} Portal` : 'Portal'}
                </p>
              </div>
            </Link>
          </div>


          {/* Search Bar */}
          <div className="flex-1 max-w-2xl mx-12 hidden md:block">
            <div className="relative">
              <span className="absolute inset-y-0 left-0 pl-3 flex items-center text-slate-400">
                <Search className="h-4 w-4 text-slate-400" />
              </span>
              <input 
                type="text"
                readOnly
                onClick={() => setIsSearchOpen(true)} 
                placeholder={portalSlug === 'patient' ? "Search health records, vitals, appointments..." : "Search patients, appointments, reports..."}
                className="block w-full pl-10 pr-12 py-2 border border-slate-200 rounded-lg bg-slate-50 text-sm focus:ring-indigo-500 focus:border-indigo-500 text-slate-800 placeholder-slate-400 cursor-pointer hover:bg-slate-100 transition-colors"
              />
              <span className="absolute inset-y-0 right-0 pr-3 flex items-center text-slate-400 text-xs font-medium">
                ⌘ K
              </span>
            </div>
          </div>

          {/* Right Actions */}
          <div className="flex items-center gap-6">
            <div className="relative">
              <button 
                onClick={() => setIsQuickActionOpen(!isQuickActionOpen)}
                className="bg-indigo-600 text-white px-4 py-2 rounded-lg flex items-center gap-2 text-sm font-semibold hover:bg-indigo-700 transition shadow-xs"
              >
                <Zap size={16} className="fill-current" />
                Quick Action
                <ChevronDown size={14} className={isQuickActionOpen ? "rotate-180 transition-transform" : "transition-transform"} />
              </button>

              {isQuickActionOpen && (
                <div 
                  className="absolute right-0 mt-2 w-56 bg-white border border-slate-200 rounded-xl shadow-xl z-50 py-2 text-xs font-medium text-slate-700"
                  onMouseLeave={() => setIsQuickActionOpen(false)}
                >
                  <div className="px-3 py-1.5 text-[10px] font-bold text-slate-400 uppercase tracking-wider border-b border-slate-100 mb-1">
                    {portalSlug === 'patient' ? 'Patient Actions' : 'Clinical Actions'}
                  </div>
                  {portalSlug === 'patient' ? (
                    <>
                      <button onClick={() => { setIsQuickActionOpen(false); window.location.pathname = '/patient/book'; }} className="w-full text-left px-3 py-2 hover:bg-slate-50 flex items-center gap-2.5">
                        <span className="p-1 rounded bg-indigo-50 text-indigo-600"><Stethoscope size={13} /></span> Book Appointment
                      </button>
                      <button onClick={() => { setIsQuickActionOpen(false); window.location.pathname = '/patient/order-medicine'; }} className="w-full text-left px-3 py-2 hover:bg-slate-50 flex items-center gap-2.5">
                        <span className="p-1 rounded bg-orange-50 text-orange-600"><ShieldPlus size={13} /></span> Order Medicine
                      </button>
                      <button onClick={() => { setIsQuickActionOpen(false); window.location.pathname = '/patient/lab-reports'; }} className="w-full text-left px-3 py-2 hover:bg-slate-50 flex items-center gap-2.5">
                        <span className="p-1 rounded bg-blue-50 text-blue-600"><Bell size={13} /></span> View Lab Reports
                      </button>
                    </>
                  ) : (
                    <>
                      <button onClick={() => { setIsQuickActionOpen(false); window.location.hash = '?panel=calendar'; }} className="w-full text-left px-3 py-2 hover:bg-slate-50 flex items-center gap-2.5">
                        <span className="p-1 rounded bg-indigo-50 text-indigo-600"><Stethoscope size={13} /></span> New Appointment
                      </button>
                      <button onClick={() => { setIsQuickActionOpen(false); window.location.hash = '?panel=patients'; }} className="w-full text-left px-3 py-2 hover:bg-slate-50 flex items-center gap-2.5">
                        <span className="p-1 rounded bg-green-50 text-green-600"><Search size={13} /></span> Add Patient
                      </button>
                      <button onClick={() => { setIsQuickActionOpen(false); window.location.pathname = '/doctor/prescription-templates'; }} className="w-full text-left px-3 py-2 hover:bg-slate-50 flex items-center gap-2.5">
                        <span className="p-1 rounded bg-orange-50 text-orange-600"><ShieldPlus size={13} /></span> New Prescription
                      </button>
                      <button onClick={() => { setIsQuickActionOpen(false); window.location.hash = '?panel=patients'; }} className="w-full text-left px-3 py-2 hover:bg-slate-50 flex items-center gap-2.5">
                        <span className="p-1 rounded bg-blue-50 text-blue-600"><Bell size={13} /></span> Lab Request
                      </button>
                      <button onClick={() => { setIsQuickActionOpen(false); window.location.hash = '?panel=queue'; }} className="w-full text-left px-3 py-2 hover:bg-slate-50 flex items-center gap-2.5 border-t border-slate-100 mt-1 pt-2">
                        <span className="p-1 rounded bg-emerald-50 text-emerald-600"><Stethoscope size={13} /></span> Start Consultation
                      </button>
                    </>
                  )}
                </div>
              )}
            </div>

            <div className="flex items-center gap-4 text-slate-500">
              
              <div className="relative" onMouseLeave={() => setIsNotificationsOpen(false)}>
                <div className="cursor-pointer relative" onClick={() => setIsNotificationsOpen(!isNotificationsOpen)}>
                  <Bell size={22} className="text-slate-600 hover:text-slate-800 transition" />
                  {unreadCount > 0 && (
                    <span className="absolute -top-1 -right-1 bg-red-500 text-white text-[10px] w-4 h-4 rounded-full flex items-center justify-center border-2 border-white font-bold">
                      {unreadCount}
                    </span>
                  )}
                </div>

                {isNotificationsOpen && (
                  <div className="absolute right-0 mt-3 w-80 bg-white border border-slate-200 rounded-xl shadow-xl z-50 overflow-hidden">
                    <div className="px-4 py-3 bg-slate-50 border-b border-slate-100 flex justify-between items-center">
                      <span className="font-bold text-slate-800 text-sm">Notifications</span>
                      <span className="text-xs text-indigo-600 font-semibold cursor-pointer hover:underline" onClick={() => window.location.pathname = '/patient/timeline'}>View All</span>
                    </div>
                    <div className="max-h-72 overflow-y-auto">
                      {unreadCount === 0 ? (
                         <div className="p-4 text-center text-sm text-slate-500">No new notifications.</div>
                      ) : (
                         <div className="p-4 text-center text-sm text-slate-500">You have {unreadCount} unread notifications. Check your timeline.</div>
                      )}
                    </div>
                  </div>
                )}
              </div>

              <div className="relative cursor-pointer" onClick={() => window.location.hash = '?panel=messages'}>
                <MessageSquare size={22} className="text-slate-600 hover:text-slate-800 transition" />
                <span className="absolute -top-1 -right-1 bg-red-500 text-white text-[10px] w-4 h-4 rounded-full flex items-center justify-center border-2 border-white font-bold">
                  6
                </span>
              </div>
              <Activity size={22} className="text-slate-600 hover:text-slate-800 transition cursor-pointer" />
            </div>

            <div className="flex items-center gap-3 pl-6 border-l border-slate-200">
              <img loading="lazy" 
                alt="User Profile" 
                className="w-10 h-10 rounded-full object-cover border-2 border-white shadow-xs" 
                src={portalSlug === 'patient' 
                  ? "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?auto=format&fit=crop&q=80&w=150"
                  : "https://images.unsplash.com/photo-1622253692010-333f2da6031d?auto=format&fit=crop&q=80&w=150"} 
              />
              <div className="text-left hidden sm:block">
                <p className="text-sm font-bold text-slate-800 leading-tight">
                  {portalSlug === 'patient' ? `Mr. ${userName}` : (userRoles.includes('ROLE_DOCTOR') ? `Dr. ${userName}` : userName)}
                </p>
                <p className="text-xs text-slate-400">
                  {portalSlug === 'patient' ? 'Patient ID: AH-9821' : displayTitle}
                </p>
              </div>
              <ChevronDown size={14} className="text-slate-400 hidden sm:block" />
              <button 
                onClick={() => {
                  logout();
                  window.location.href = `/${portalSlug || 'patient'}/login`;
                }}
                className="p-1 text-slate-400 hover:text-red-600 transition ml-1"
                title="Logout"
              >
                <LogOut size={16} />
              </button>
            </div>
          </div>
        </div>
      </header>

      {/* Horizontal Nav Bar Removed as requested */}
      
      <CommandPalette isOpen={isSearchOpen} onClose={() => setIsSearchOpen(false)} />
      
      {/* Main Content Area
          - Dashboard routes: overflow-hidden → tiles fill the viewport, no scrollbars.
          - All other routes:  overflow-y-auto  → page content scrolls naturally inside
            the fixed shell (header + nav bar remain sticky at top). */}
      <main
        className={`flex-1 w-full flex flex-col ${
          location.pathname.endsWith('/dashboard')
            ? 'overflow-hidden'
            : 'overflow-y-auto'
        }`}
      >
        <Outlet />
      </main>
    </div>
    </ErrorBoundary>
  );
};

export default DashboardLayout;
