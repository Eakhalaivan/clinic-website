import React from 'react';
import { useQuery } from '@tanstack/react-query';
import { axiosPrivate } from '../../api/axios';
import { Link } from 'react-router-dom';
import { motion } from 'framer-motion';
import {
  Users, Monitor, FileText, DollarSign, Shield, Ticket,
  UserCheck, UserPlus, ClipboardList, RefreshCw,
  Loader2, TrendingUp, Clock, ChevronRight, Briefcase
} from 'lucide-react';
import useAuthStore from '../../store/authStore';

const BRANCH_ID = 1;

// Colors mapping for SVG waves and icons
const colors = {
  blue: '#2B4AFE',
  green: '#10B981',
  orange: '#F97316',
  purple: '#8B5CF6',
  teal: '#14B8A6',
  red: '#F43F5E',
};

// SVG Wave for bottom right of metric cards
const WaveBackground = ({ color }) => (
  <div className="absolute right-0 bottom-0 pointer-events-none opacity-[0.04]">
    <svg width="80" height="48" viewBox="0 0 100 60" fill="none" xmlns="http://www.w3.org/2000/svg">
      <path d="M0 60 C 30 60, 50 20, 100 0 L 100 60 Z" fill={color}/>
    </svg>
  </div>
);

const QUICK_ACTIONS = [
  { label: 'Register Patient', desc: 'Add a new patient', icon: UserPlus, path: '/reception/register', bg: 'bg-[#8B5CF6]' },
  { label: 'Walk-in Check-In', desc: 'Check in a walk-in', icon: UserCheck, path: '/reception/walk-in', bg: 'bg-[#10B981]' },
  { label: 'Queue Management', desc: 'Manage the queue', icon: Users, path: '/reception/queue', bg: 'bg-[#2B4AFE]' },
  { label: 'Token Generation', desc: 'Generate new token', icon: Ticket, path: '/reception/tokens', bg: 'bg-[#F97316]' },
  { label: 'Billing & Payments', desc: 'Process payments', icon: DollarSign, path: '/reception/billing', bg: 'bg-[#10B981]' },
  { label: 'Insurance Verify', desc: 'Verify insurance details', icon: Shield, path: '/reception/insurance', bg: 'bg-[#8B5CF6]' },
  { label: 'Document Scanning', desc: 'Scan and upload docs', icon: FileText, path: '/reception/documents', bg: 'bg-[#F43F5E]' },
  { label: 'Kiosk Check-In', desc: 'Check-in via kiosk', icon: Monitor, path: '/reception/kiosk', bg: 'bg-[#14B8A6]' },
];

const StatCard = ({ label, value, icon: Icon, hexColor, isLoading }) => (
  <motion.div
    initial={{ opacity: 0, y: 5 }}
    animate={{ opacity: 1, y: 0 }}
    className="relative overflow-hidden rounded-xl bg-white p-3 shadow-[0_2px_8px_rgba(0,0,0,0.02)] flex items-center gap-3 border border-gray-100/50"
  >
    <WaveBackground color={hexColor} />
    <div className={`w-10 h-10 rounded-xl flex items-center justify-center flex-shrink-0 z-10`} style={{ backgroundColor: hexColor }}>
      <Icon className="w-4 h-4 text-white" />
    </div>
    <div className="z-10 flex-1 min-w-0">
      <p className="text-[10px] font-semibold text-gray-500 uppercase tracking-wider truncate mb-0.5">{label}</p>
      {isLoading ? (
        <Loader2 className="w-4 h-4 animate-spin text-gray-400" />
      ) : (
        <div className="flex items-baseline gap-1">
          <p className="text-xl font-bold text-gray-900 leading-none">{value < 10 ? `0${value}` : value}</p>
        </div>
      )}
      <p className="text-[10px] text-gray-400 mt-0.5">Patients</p>
    </div>
  </motion.div>
);

const EmptyState = () => (
  <div className="flex flex-col items-center justify-center py-6">
    <div className="relative mb-3">
      <div className="w-12 h-12 bg-[#EEF2FB] rounded-full flex items-center justify-center">
        <ClipboardList className="w-5 h-5 text-[#8B9CFA]" />
      </div>
      <div className="absolute top-1 -right-2 w-1.5 h-1.5 rounded-full bg-[#C2CCFF]" />
      <div className="absolute bottom-2 -left-3 w-1 h-1 rounded-full bg-[#C2CCFF]" />
      <div className="absolute top-1/2 -right-4 w-1 h-1 rounded-full bg-[#E0E7FF]" />
    </div>
    <h3 className="text-[13px] font-bold text-gray-900 mb-0.5">No kiosk check-ins yet today.</h3>
    <p className="text-[11px] text-gray-500">Check-ins from the kiosk will appear here.</p>
  </div>
);

const ReceptionDashboard = () => {
  const user = useAuthStore(s => s.user);

  const { data: stats, isLoading: statsLoading, refetch, isFetching } = useQuery({
    queryKey: ['receptionDashboardStats', BRANCH_ID],
    queryFn: async () => {
      const res = await axiosPrivate.get(`/reception/kiosk/branch/${BRANCH_ID}/stats`);
      return res.data;
    },
    refetchInterval: 30000
  });

  const { data: kioskToday = [], isLoading: kioskLoading } = useQuery({
    queryKey: ['kioskToday', BRANCH_ID],
    queryFn: async () => {
      const res = await axiosPrivate.get(`/reception/kiosk/branch/${BRANCH_ID}/today`);
      return res.data;
    },
    refetchInterval: 15000
  });

  const getStatusBadge = (status) => {
    switch (status) {
      case 'CHECKED_IN': return <span className="px-2 py-0.5 bg-green-50 text-green-700 text-[9px] font-bold rounded-md">CHECKED IN</span>;
      case 'NO_SHOW': return <span className="px-2 py-0.5 bg-red-50 text-red-700 text-[9px] font-bold rounded-md">NO SHOW</span>;
      case 'VERIFIED': return <span className="px-2 py-0.5 bg-blue-50 text-blue-700 text-[9px] font-bold rounded-md">VERIFIED</span>;
      default: return <span className="px-2 py-0.5 bg-orange-50 text-orange-700 text-[9px] font-bold rounded-md">{status}</span>;
    }
  };

  return (
    <div className="w-full h-full flex flex-col space-y-4 px-6 lg:px-8 py-6" style={{ fontFamily: "'Inter', sans-serif" }}>
      
      {/* Header */}
      <div className="flex flex-col sm:flex-row justify-between items-start sm:items-center gap-2">
        <div className="flex items-center gap-3">
          <div className="w-10 h-10 rounded-xl bg-[#2B4AFE] flex items-center justify-center shadow-md shadow-[#2B4AFE]/20">
            <Briefcase className="w-5 h-5 text-white" />
          </div>
          <div>
            <h1 className="text-xl font-bold text-gray-900 leading-tight">Reception Desk</h1>
            <p className="text-xs text-gray-500 mt-0.5">
              Good {new Date().getHours() < 12 ? 'morning' : 'afternoon'}, {user?.firstName || 'Staff'}! Here's your live overview.
            </p>
          </div>
        </div>
        <button
          onClick={() => refetch()}
          className="flex items-center gap-1.5 px-3 py-1.5 bg-white border border-gray-200 rounded-lg text-xs font-semibold text-[#2B4AFE] hover:bg-gray-50 transition-colors shadow-sm"
        >
          <RefreshCw className={`w-3.5 h-3.5 ${isFetching ? 'animate-spin' : ''}`} />
          Refresh
        </button>
      </div>

      {/* Metrics Row */}
      <div className="grid grid-cols-2 lg:grid-cols-5 gap-3">
        <StatCard label="Queue Waiting" value={stats?.queueWaiting ?? 0} icon={ClipboardList} hexColor={colors.blue} isLoading={statsLoading} />
        <StatCard label="Walk-ins Today" value={stats?.walkInsToday ?? 0} icon={UserCheck} hexColor={colors.green} isLoading={statsLoading} />
        <StatCard label="Kiosk Pending" value={stats?.kioskPending ?? 0} icon={Clock} hexColor={colors.orange} isLoading={statsLoading} />
        <StatCard label="Verified Today" value={stats?.kioskVerified ?? 0} icon={UserPlus} hexColor={colors.purple} isLoading={statsLoading} />
        <StatCard label="Checked In" value={stats?.kioskCheckedIn ?? 0} icon={TrendingUp} hexColor={colors.teal} isLoading={statsLoading} />
      </div>

      {/* Quick Actions */}
      <div>
        <div className="flex items-center gap-2 mb-2">
          <div className="w-1 h-3.5 bg-[#2B4AFE] rounded-full" />
          <h2 className="text-[13px] font-bold text-gray-900">Quick Actions</h2>
        </div>
        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-3">
          {QUICK_ACTIONS.map((action, idx) => (
            <Link
              key={idx}
              to={action.path}
              className="group flex items-center justify-between p-3 bg-white rounded-xl border border-gray-100 shadow-[0_2px_6px_rgba(0,0,0,0.02)] hover:shadow-[0_4px_10px_rgba(0,0,0,0.04)] transition-all"
            >
              <div className="flex items-center gap-3">
                <div className={`w-9 h-9 rounded-lg ${action.bg} flex items-center justify-center flex-shrink-0 group-hover:scale-105 transition-transform`}>
                  <action.icon className="w-4 h-4 text-white" />
                </div>
                <div>
                  <h3 className="text-[12px] font-bold text-gray-900">{action.label}</h3>
                  <p className="text-[10px] text-gray-500 mt-0.5">{action.desc}</p>
                </div>
              </div>
              <ChevronRight className="w-3.5 h-3.5 text-gray-300 group-hover:text-gray-400 transition-colors" />
            </Link>
          ))}
        </div>
      </div>

      {/* Today's Kiosk Check-ins */}
      <div className="flex-1 bg-white rounded-xl border border-gray-100 shadow-[0_2px_8px_rgba(0,0,0,0.02)] overflow-hidden flex flex-col min-h-[150px]">
        <div className="p-3 border-b border-gray-100 flex items-center justify-between">
          <div className="flex items-center gap-2">
            <Monitor className="w-4 h-4 text-[#2B4AFE]" />
            <h2 className="text-[13px] font-bold text-gray-900">Today's Kiosk Check-ins</h2>
          </div>
          <Link to="/reception/queue" className="text-[11px] font-semibold text-[#2B4AFE] hover:underline flex items-center gap-1">
            View Full Queue <ChevronRight className="w-3 h-3" />
          </Link>
        </div>
        
        {kioskLoading ? (
          <div className="flex flex-1 items-center justify-center py-6">
            <Loader2 className="w-5 h-5 animate-spin text-gray-400" />
          </div>
        ) : kioskToday.length === 0 ? (
          <div className="flex flex-1 items-center justify-center">
            <EmptyState />
          </div>
        ) : (
          <div className="overflow-x-auto flex-1">
            <table className="w-full text-left border-collapse">
              <thead>
                <tr className="bg-gray-50/50">
                  <th className="px-4 py-2 text-[9px] font-bold uppercase tracking-wider text-gray-500 border-b border-gray-100 w-[15%]">Token #</th>
                  <th className="px-4 py-2 text-[9px] font-bold uppercase tracking-wider text-gray-500 border-b border-gray-100">Patient Name</th>
                  <th className="px-4 py-2 text-[9px] font-bold uppercase tracking-wider text-gray-500 border-b border-gray-100">Check-in Time</th>
                  <th className="px-4 py-2 text-[9px] font-bold uppercase tracking-wider text-gray-500 border-b border-gray-100">Status</th>
                </tr>
              </thead>
              <tbody>
                {kioskToday.map((k, idx) => (
                  <tr key={k.id} className="hover:bg-gray-50/50 transition-colors border-b border-gray-50 last:border-none">
                    <td className="px-4 py-2.5 align-middle">
                      <span className="text-[12px] font-semibold text-gray-900">#{k.id}</span>
                    </td>
                    <td className="px-4 py-2.5 align-middle">
                      <div className="flex flex-col">
                        <span className="text-[12px] font-semibold text-gray-900">Patient {k.id}</span>
                        {k.kioskStation && <span className="text-[10px] text-gray-500">Station: {k.kioskStation}</span>}
                      </div>
                    </td>
                    <td className="px-4 py-2.5 align-middle">
                      <span className="text-[11px] text-gray-600">
                        {k.createdAt ? new Date(k.createdAt).toLocaleTimeString([], {hour: '2-digit', minute:'2-digit'}) : 'N/A'}
                      </span>
                    </td>
                    <td className="px-4 py-2.5 align-middle">
                      {getStatusBadge(k.status)}
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
      </div>

    </div>
  );
};

export default ReceptionDashboard;
