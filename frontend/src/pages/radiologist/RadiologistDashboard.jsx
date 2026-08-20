import React, { useState } from 'react';
import { useQuery } from '@tanstack/react-query';
import { axiosPrivate } from '../../api/axios';
import { Search, Filter, AlertCircle, CheckCircle, Image as ImageIcon, Plus, Maximize, ChevronDown } from 'lucide-react';

const EmptyStateIcon = () => (
  <div className="relative w-24 h-24 flex items-center justify-center">
    <svg width="100" height="100" viewBox="0 0 140 140" fill="none" xmlns="http://www.w3.org/2000/svg">
      <circle cx="70" cy="70" r="55" fill="#EEF2FF" opacity="0.6"/>
      <circle cx="105" cy="45" r="4" fill="#C7D2FE"/>
      <circle cx="115" cy="65" r="2.5" fill="#C7D2FE"/>
      <circle cx="35" cy="90" r="3" fill="#C7D2FE"/>
      <circle cx="40" cy="40" r="2" fill="#E0E7FF"/>
      
      {/* 3D Tray Back */}
      <path d="M40 75 L55 55 H85 L100 75 V85 C100 90.5 95.5 95 90 95 H50 C44.5 95 40 90.5 40 85 V75 Z" fill="#C7D2FE"/>
      {/* 3D Tray Front */}
      <path d="M40 75 H55 C57.7614 75 60 77.2386 60 80 V80 C60 82.7614 62.2386 85 65 85 H75 C77.7614 85 80 82.7614 80 80 V80 C80 77.2386 82.2386 75 85 75 H100 V85 C100 90.5228 95.5228 95 90 95 H50 C44.4772 95 40 90.5228 40 85 V75 Z" fill="url(#paint0_linear)"/>
      <path d="M40 75 H55 C57.7614 75 60 77.2386 60 80 C60 82.7614 62.2386 85 65 85 H75 C77.7614 85 80 82.7614 80 80 C80 77.2386 82.2386 75 85 75 H100" stroke="#818CF8" strokeWidth="3" strokeLinecap="round" strokeLinejoin="round"/>
      
      <defs>
        <linearGradient id="paint0_linear" x1="70" y1="75" x2="70" y2="95" gradientUnits="userSpaceOnUse">
          <stop stopColor="#A5B4FC"/>
          <stop offset="1" stopColor="#E0E7FF"/>
        </linearGradient>
      </defs>
    </svg>
  </div>
);

const DotsPattern = ({ className }) => (
  <svg width="100" height="100" viewBox="0 0 120 120" fill="none" xmlns="http://www.w3.org/2000/svg" className={className}>
    <pattern id="dots" x="0" y="0" width="16" height="16" patternUnits="userSpaceOnUse">
      <circle cx="2" cy="2" r="2" fill="#CBD5E1" opacity="0.4" />
    </pattern>
    <rect width="120" height="120" fill="url(#dots)" />
  </svg>
);

const RadiologistDashboard = () => {
  const [activeTab, setActiveTab] = useState('ALL');

  const { data: requests = [] } = useQuery({
    queryKey: ['radiology-requests-dashboard'],
    queryFn: async () => {
      const res = await axiosPrivate.get('/radiology/requests');
      return res.data;
    },
    refetchInterval: 30000
  });

  const pendingCount = requests.filter(r => r.status === 'REQUESTED' || r.status === 'SCHEDULED').length;
  const completedCount = requests.filter(r => r.status === 'COMPLETED').length;
  // Fallback for archived (mocking to 0 if none exist in standard data)
  const archivedCount = requests.filter(r => r.status === 'ARCHIVED').length;

  return (
    <div className="w-full h-full min-h-screen overflow-y-auto bg-[#F8FAFC] p-4 lg:p-6 flex flex-col font-sans" style={{ fontFamily: "'Inter', sans-serif" }}>
      
      {/* Tabs */}
      <div className="flex gap-2 mb-6">
        {['ALL', 'REQUESTED', 'SCHEDULED', 'COMPLETED'].map(tab => (
          <button 
            key={tab} 
            onClick={() => setActiveTab(tab)}
            className={`px-4 py-1.5 rounded-lg text-[11px] font-bold tracking-wide transition-colors border ${
              activeTab === tab 
                ? 'bg-[#3B52D9] text-white border-[#3B52D9] shadow-sm shadow-blue-500/20' 
                : 'bg-white text-gray-500 border-gray-200 hover:bg-gray-50'
            }`}
          >
            {tab}
          </button>
        ))}
      </div>

      {/* Header */}
      <div className="flex items-center gap-4 mb-6">
        <div className="w-12 h-12 bg-white rounded-xl border border-gray-100 shadow-[0_2px_8px_rgba(0,0,0,0.04)] flex items-center justify-center shrink-0">
          <Maximize className="w-6 h-6 text-[#3B52D9]" strokeWidth={2.5} />
        </div>
        <div>
          <h1 className="text-[22px] font-bold text-[#1E293B] leading-tight">Radiology & PACS Workstation</h1>
          <p className="text-[13px] text-gray-500 mt-0.5 font-medium">Imaging procedure management, DICOM study review, and diagnostic report generation.</p>
        </div>
      </div>

      {/* Content Grid */}
      <div className="flex flex-col lg:flex-row gap-5 flex-1">
        
        {/* Left Column: KPIs */}
        <div className="w-full lg:w-[220px] flex flex-col gap-4 shrink-0">
          
          {/* PENDING */}
          <div className="bg-white rounded-2xl p-4 shadow-[0_2px_12px_rgba(0,0,0,0.02)] border border-gray-50 flex items-center gap-4">
             <div className="w-10 h-10 bg-[#FFF7ED] rounded-xl flex items-center justify-center shrink-0">
               <AlertCircle className="w-5 h-5 text-[#EA580C]" strokeWidth={2} />
             </div>
             <div>
               <p className="text-[10px] font-bold text-gray-500 tracking-wider mb-0.5">PENDING</p>
               <p className="text-[24px] font-bold text-[#EA580C] leading-none">{pendingCount}</p>
             </div>
          </div>
          
          {/* COMPLETED */}
          <div className="bg-white rounded-2xl p-4 shadow-[0_2px_12px_rgba(0,0,0,0.02)] border border-gray-50 flex items-center gap-4">
             <div className="w-10 h-10 bg-[#ECFDF5] rounded-xl flex items-center justify-center shrink-0">
               <CheckCircle className="w-5 h-5 text-[#10B981]" strokeWidth={2} />
             </div>
             <div>
               <p className="text-[10px] font-bold text-gray-500 tracking-wider mb-0.5">COMPLETED</p>
               <p className="text-[24px] font-bold text-[#10B981] leading-none">{completedCount}</p>
             </div>
          </div>
          
          {/* ARCHIVED */}
          <div className="bg-white rounded-2xl p-4 shadow-[0_2px_12px_rgba(0,0,0,0.02)] border border-gray-50 flex items-center gap-4">
             <div className="w-10 h-10 bg-[#EEF2FF] rounded-xl flex items-center justify-center shrink-0">
               <ImageIcon className="w-5 h-5 text-[#3B52D9]" strokeWidth={2} />
             </div>
             <div>
               <p className="text-[10px] font-bold text-gray-500 tracking-wider mb-0.5">ARCHIVED</p>
               <p className="text-[24px] font-bold text-[#3B52D9] leading-none">{archivedCount}</p>
             </div>
          </div>

        </div>

        {/* Right Column: Main Area */}
        <div className="flex-1 bg-white rounded-[20px] shadow-[0_2px_16px_rgba(0,0,0,0.02)] border border-gray-100 p-5 flex flex-col relative overflow-hidden min-h-[400px]">
          
          {/* Decorative Dot Patterns */}
          <DotsPattern className="absolute top-12 right-6 pointer-events-none" />
          <DotsPattern className="absolute bottom-12 left-6 pointer-events-none" />

          {/* Toolbar */}
          <div className="flex gap-3 mb-12 relative z-10">
            <div className="flex-1 relative">
              <Search className="w-4 h-4 text-gray-400 absolute left-3 top-1/2 -translate-y-1/2" strokeWidth={2} />
              <input 
                type="text" 
                placeholder="Search imaging studies..." 
                className="w-full pl-9 pr-3 py-2.5 bg-[#F8FAFC] border border-transparent rounded-lg text-[13px] font-medium text-gray-700 placeholder-gray-400 focus:bg-white focus:border-[#3B52D9] focus:ring-2 focus:ring-blue-50 focus:outline-none transition-all" 
              />
            </div>
            <button className="flex items-center gap-2 px-4 py-2.5 bg-white border border-gray-200 rounded-lg text-[13px] font-semibold text-gray-700 hover:bg-gray-50 hover:border-gray-300 transition-colors shadow-sm">
              <Filter className="w-3.5 h-3.5 text-gray-500" strokeWidth={2.5} /> 
              Filter 
              <ChevronDown className="w-3.5 h-3.5 text-gray-400" strokeWidth={2.5} />
            </button>
          </div>

          {/* Empty State */}
          <div className="flex-1 flex flex-col items-center justify-center text-center relative z-10 pb-6">
            <EmptyStateIcon />
            <h3 className="text-lg font-bold text-[#1E293B] mb-1 mt-3">No radiology requests found</h3>
            <p className="text-[13px] text-gray-500 mb-6 font-medium">There are no entries to display at this time.</p>
            <button className="flex items-center gap-1.5 px-5 py-2.5 bg-[#3B52D9] text-white rounded-lg text-[13px] font-semibold hover:bg-[#2e42b8] transition-colors shadow-lg shadow-blue-500/20">
              <Plus className="w-4 h-4" strokeWidth={2.5} /> New Radiology Request
            </button>
          </div>

        </div>
      </div>
    </div>
  );
};

export default RadiologistDashboard;
