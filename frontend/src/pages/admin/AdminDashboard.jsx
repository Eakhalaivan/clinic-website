import React, { useState } from 'react';
import { 
  Building2, BarChart2, Users, UserCircle, Stethoscope, LayoutGrid, ShieldCheck,
  UserPlus, Building, ClipboardList, Settings, Database, Download, Search, Filter, List, Grid, Plus, ChevronDown
} from 'lucide-react';
import UserManagement from './UserManagement';
import PatientManagement from './PatientManagement';
import DoctorManagement from './DoctorManagement';
import DepartmentManagement from './DepartmentManagement';
import AuditDashboard from './AuditDashboard';
import { DashboardShell } from '../../components/dashboard/shared/DashboardShell';

const TABS = [
  { id: 'branches', label: 'Manage Branches', icon: Building2 },
  { id: 'analytics', label: 'Analytics & Reports', icon: BarChart2 },
  { id: 'users', label: 'Manage Users', icon: Users },
  { id: 'patients', label: 'Manage Patients', icon: UserCircle },
  { id: 'doctors', label: 'Manage Doctors', icon: Stethoscope },
  { id: 'departments', label: 'Manage Departments', icon: LayoutGrid },
  { id: 'audit', label: 'Audit & Compliance', icon: ShieldCheck },
];

const QUICK_ACTIONS = [
  { title: 'Create User', desc: 'Add new system user', icon: UserPlus, color: 'text-purple-600', bg: 'bg-purple-50', id: 'users' },
  { title: 'Manage Users', desc: 'View and manage users', icon: Users, color: 'text-blue-600', bg: 'bg-blue-50', id: 'users' },
  { title: 'Roles & Permissions', desc: 'Manage access control', icon: ShieldCheck, color: 'text-orange-500', bg: 'bg-orange-50', id: 'users' },
  { title: 'Manage Departments', desc: 'Organize departments', icon: Building, color: 'text-blue-600', bg: 'bg-blue-50', id: 'departments' },
  { title: 'Manage Doctors', desc: 'Manage doctor profiles', icon: Stethoscope, color: 'text-purple-600', bg: 'bg-purple-50', id: 'doctors' },
  { title: 'Manage Patients', desc: 'Manage patient records', icon: UserCircle, color: 'text-blue-600', bg: 'bg-blue-50', id: 'patients' },
  
  { title: 'Manage Branches', desc: 'Manage all branches', icon: Building2, color: 'text-indigo-600', bg: 'bg-indigo-50', id: 'branches' },
  { title: 'Audit Logs', desc: 'View system activity', icon: ClipboardList, color: 'text-blue-600', bg: 'bg-blue-50', id: 'audit' },
  { title: 'Analytics Dashboard', desc: 'View platform analytics', icon: BarChart2, color: 'text-indigo-600', bg: 'bg-indigo-50', id: 'analytics' },
  { title: 'System Settings', desc: 'Configure system settings', icon: Settings, color: 'text-green-600', bg: 'bg-green-50', id: 'analytics' },
  { title: 'Backup & Restore', desc: 'Manage data backups', icon: Database, color: 'text-blue-600', bg: 'bg-blue-50', id: 'branches' },
  { title: 'Export Data', desc: 'Export system data', icon: Download, color: 'text-teal-500', bg: 'bg-teal-50', id: 'analytics' },
];

const AbstractGraphic = () => (
  <svg width="240" height="160" viewBox="0 0 240 160" fill="none" xmlns="http://www.w3.org/2000/svg" className="absolute right-4 top-2 pointer-events-none hidden sm:block">
    <rect x="60" y="40" width="140" height="90" rx="12" fill="#F8FAFC" stroke="#F1F5F9" strokeWidth="2"/>
    <rect x="75" y="55" width="40" height="8" rx="4" fill="#E2E8F0"/>
    <rect x="75" y="75" width="80" height="8" rx="4" fill="#E2E8F0"/>
    <rect x="75" y="95" width="60" height="8" rx="4" fill="#E2E8F0"/>
    
    <circle cx="210" cy="50" r="2" fill="#CBD5E1"/>
    <circle cx="220" cy="50" r="2" fill="#CBD5E1"/>
    <circle cx="210" cy="60" r="2" fill="#CBD5E1"/>
    <circle cx="220" cy="60" r="2" fill="#CBD5E1"/>
    <circle cx="210" cy="70" r="2" fill="#CBD5E1"/>
    <circle cx="220" cy="70" r="2" fill="#CBD5E1"/>

    <circle cx="200" cy="130" r="14" fill="#818CF8"/>
    <path d="M200 120 V124 M200 136 V140 M190 130 H194 M206 130 H210 M193 123 L196 126 M204 137 L207 140 M193 137 L196 134 M204 123 L207 126" stroke="#EEF2FF" strokeWidth="2" strokeLinecap="round"/>
    <circle cx="200" cy="130" r="5" fill="#EEF2FF"/>

    <g transform="translate(130, 85) rotate(-12) translate(-130, -85)">
      <rect x="90" y="30" width="80" height="90" rx="16" fill="url(#paint0_linear)"/>
      <path d="M130 45 C130 45 115 45 115 45 C115 45 115 65 115 78 C115 90 130 102 130 102 C130 102 145 90 145 78 C145 65 145 45 145 45 C145 45 130 45 130 45 Z" stroke="white" strokeWidth="3" strokeLinecap="round" strokeLinejoin="round"/>
      <path d="M122 72 L128 78 L138 62" stroke="white" strokeWidth="3" strokeLinecap="round" strokeLinejoin="round"/>
    </g>
    <defs>
      <linearGradient id="paint0_linear" x1="90" y1="30" x2="170" y2="120" gradientUnits="userSpaceOnUse">
        <stop stopColor="#6366F1"/>
        <stop offset="1" stopColor="#3730A3"/>
      </linearGradient>
    </defs>
  </svg>
);

const AdminDashboard = () => {
  const [activeTab, setActiveTab] = useState('branches');

  return (
    <div className="w-full h-full flex flex-col space-y-4 px-6 lg:px-8 py-6 bg-[#F8FAFC] overflow-y-auto" style={{ fontFamily: "'Inter', sans-serif" }}>
      
      {/* Top Tabs */}
      <div className="flex items-center gap-3 overflow-x-auto pb-1 no-scrollbar shrink-0">
        {TABS.map(tab => (
          <button
            key={tab.id}
            onClick={() => setActiveTab(tab.id)}
            className={`flex items-center gap-2 px-4 py-2 rounded-lg text-[13px] font-semibold whitespace-nowrap transition-colors border ${
              activeTab === tab.id 
                ? 'bg-[#3B52D9] text-white border-[#3B52D9] shadow-sm' 
                : 'bg-white text-gray-600 border-gray-200 hover:bg-gray-50'
            }`}
          >
            <tab.icon className="w-4 h-4" />
            {tab.label}
          </button>
        ))}
      </div>

      {/* Hero Section */}
      <div className="bg-white rounded-2xl border border-gray-100 shadow-[0_2px_12px_rgba(0,0,0,0.02)] p-6 relative overflow-hidden shrink-0">
        <div className="mb-8 max-w-xl relative z-10">
          <h1 className="text-2xl font-bold text-gray-900 leading-tight">System Administration</h1>
          <p className="text-[13px] text-gray-500 mt-2 mb-4">
            Manage system configurations, branches, users, and<br/>view platform analytics.
          </p>
          <div className="w-8 h-0.5 bg-[#3B52D9] rounded-full"></div>
        </div>
        
        <AbstractGraphic />

        {/* Action Grid */}
        <div className="grid grid-cols-2 sm:grid-cols-3 md:grid-cols-4 xl:grid-cols-6 gap-3 relative z-10">
          {QUICK_ACTIONS.map((action, idx) => (
            <button 
              key={idx} 
              onClick={() => setActiveTab(action.id)}
              className="flex items-center gap-3 p-3.5 bg-white border border-gray-100 rounded-xl hover:shadow-[0_4px_12px_rgba(0,0,0,0.04)] hover:border-gray-200 transition-all text-left group"
            >
              <div className={`w-10 h-10 rounded-lg ${action.bg} flex items-center justify-center shrink-0 group-hover:scale-105 transition-transform`}>
                <action.icon className={`w-4 h-4 ${action.color}`} />
              </div>
              <div className="min-w-0">
                <p className="text-[12px] font-bold text-gray-900 truncate">{action.title}</p>
                <p className="text-[10px] text-gray-500 truncate mt-0.5">{action.desc}</p>
              </div>
            </button>
          ))}
        </div>
      </div>

      {/* Dynamic Content Area based on Tab */}
      <div className="flex-1 bg-white rounded-2xl border border-gray-100 shadow-[0_2px_12px_rgba(0,0,0,0.02)] p-6 flex flex-col min-h-[300px]">
        {activeTab === 'branches' && (
          <>
            <div className="flex flex-col lg:flex-row justify-between items-start lg:items-center gap-4 mb-8">
              <div>
                <h2 className="text-[17px] font-bold text-gray-900">Active Branches</h2>
                <p className="text-[13px] text-gray-500 mt-0.5">Manage and monitor all system branches</p>
                <button className="mt-4 flex items-center gap-1.5 px-4 py-2 bg-[#3B52D9] text-white rounded-lg text-[13px] font-semibold hover:bg-[#2e42b8] transition-colors shadow-sm">
                  <Plus className="w-4 h-4" /> Add New Branch
                </button>
              </div>
              
              <div className="flex flex-wrap items-center gap-3">
                <div className="relative">
                  <Search className="w-4 h-4 text-gray-400 absolute left-3 top-1/2 -translate-y-1/2" />
                  <input 
                    type="text" 
                    placeholder="Search branches..." 
                    className="pl-9 pr-4 py-2 bg-white border border-gray-200 rounded-lg text-[13px] w-64 focus:outline-none focus:border-[#3B52D9] focus:ring-1 focus:ring-[#3B52D9]"
                  />
                </div>
                
                <button className="flex items-center gap-2 px-3 py-2 bg-white border border-gray-200 rounded-lg text-[13px] font-semibold text-gray-600 hover:bg-gray-50">
                  <Filter className="w-3.5 h-3.5 text-gray-400" />
                  All Status
                  <ChevronDown className="w-3.5 h-3.5 text-gray-400" />
                </button>
                
                <div className="flex items-center bg-gray-50 p-1 rounded-lg border border-gray-200">
                  <button className="p-1.5 bg-white text-gray-700 rounded shadow-sm">
                    <List className="w-4 h-4" />
                  </button>
                  <button className="p-1.5 text-gray-400 hover:text-gray-600">
                    <Grid className="w-4 h-4" />
                  </button>
                </div>
              </div>
            </div>

            {/* Empty State */}
            <div className="flex flex-1 flex-col items-center justify-center py-10">
              <div className="w-16 h-16 bg-[#EEF2FF] rounded-full flex items-center justify-center mb-4 relative">
                <Building2 className="w-8 h-8 text-[#3B52D9]" />
              </div>
              <h3 className="text-[15px] font-bold text-gray-900 mb-1">No branches found</h3>
              <p className="text-[13px] text-gray-500 mb-6 text-center">
                Get started by adding your first branch to the system.
              </p>
              <button className="flex items-center gap-1.5 px-5 py-2.5 bg-white border border-gray-200 rounded-lg text-[13px] font-semibold text-[#3B52D9] hover:bg-gray-50 transition-colors shadow-sm">
                <Plus className="w-4 h-4" /> Add Your First Branch
              </button>
            </div>
          </>
        )}
        {activeTab === 'users' && <UserManagement />}
        {activeTab === 'patients' && <PatientManagement />}
        {activeTab === 'doctors' && <DoctorManagement />}
        {activeTab === 'departments' && <DepartmentManagement />}
        {activeTab === 'audit' && <AuditDashboard />}
        {activeTab === 'analytics' && (
          <div className="flex flex-1 flex-col items-center justify-center py-10">
            <h3 className="text-[15px] font-bold text-gray-900 mb-1">Analytics coming soon</h3>
            <p className="text-[13px] text-gray-500 mb-6 text-center">Detailed charts will be integrated here.</p>
          </div>
        )}
      </div>

    </div>
  );
};

export default AdminDashboard;
