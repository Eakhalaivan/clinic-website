import React, { useState } from 'react';
import { useQuery } from '@tanstack/react-query';
import { axiosPrivate } from '../../api/axios';
import { Search, Filter, Headset, HelpCircle, Clock, CheckCircle, Plus, ChevronDown } from 'lucide-react';

const EmptyStateIcon = () => (
  <div className="relative w-24 h-24 flex items-center justify-center">
    <svg width="100" height="100" viewBox="0 0 140 140" fill="none" xmlns="http://www.w3.org/2000/svg">
      <circle cx="70" cy="70" r="55" fill="#EEF2FF" opacity="0.6"/>
      <circle cx="105" cy="45" r="4" fill="#C7D2FE"/>
      <circle cx="115" cy="65" r="2.5" fill="#C7D2FE"/>
      <circle cx="35" cy="90" r="3" fill="#C7D2FE"/>
      <circle cx="40" cy="40" r="2" fill="#E0E7FF"/>
      
      {/* Tray Graphic */}
      <path d="M45 75 H55 C57.7614 75 60 77.2386 60 80 C60 82.7614 62.2386 85 65 85 H75 C77.7614 85 80 82.7614 80 80 C80 77.2386 82.2386 75 85 75 H95 V85 C95 90.5228 90.5228 95 85 95 H55 C49.4772 95 45 90.5228 45 85 V75 Z" fill="#C7D2FE"/>
      <path d="M50 65 H90 L95 75 H85 C82.2386 75 80 77.2386 80 80 C80 82.7614 77.7614 85 75 85 H65 C62.2386 85 60 82.7614 60 80 C60 77.2386 57.7614 75 55 75 H45 L50 65 Z" fill="#EEF2FF"/>
      <path d="M45 75 H55 C57.7614 75 60 77.2386 60 80 C60 82.7614 62.2386 85 65 85 H75 C77.7614 85 80 82.7614 80 80 C80 77.2386 82.2386 75 85 75 H95 M45 75 V85 C45 90.5228 49.4772 95 55 95 H85 C90.5228 95 95 90.5228 95 85 V75 M45 75 L50 65 H90 L95 75" stroke="#3B52D9" strokeWidth="4" strokeLinecap="round" strokeLinejoin="round"/>
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

const HeadsetIllustration = () => (
  <svg width="100%" height="130" viewBox="0 0 180 120" fill="none" xmlns="http://www.w3.org/2000/svg" className="max-w-[200px]">
    <circle cx="90" cy="75" r="50" fill="#EEF2FF" opacity="0.6"/>
    <circle cx="40" cy="35" r="4" fill="#E0E7FF" opacity="0.6"/>
    <circle cx="35" cy="70" r="2.5" fill="#C7D2FE" opacity="0.5"/>
    <circle cx="115" cy="15" r="5" fill="#E0E7FF" opacity="0.4"/>
    
    {/* Headset Band */}
    <path d="M60 75 V55 C60 38 73 25 90 25 C107 25 120 38 120 55 V75" stroke="#818CF8" strokeWidth="6" strokeLinecap="round" />
    
    {/* Earpieces */}
    <rect x="52" y="60" width="16" height="30" rx="8" fill="#3B52D9" />
    <rect x="112" y="60" width="16" height="30" rx="8" fill="#3B52D9" />
    <rect x="55" y="63" width="5" height="24" rx="2.5" fill="#6366F1" />
    <rect x="120" y="63" width="5" height="24" rx="2.5" fill="#6366F1" />
    
    {/* Mic Boom */}
    <path d="M52 80 C40 90 55 105 70 100 H80" stroke="#818CF8" strokeWidth="4" strokeLinecap="round" />
    <circle cx="80" cy="100" r="4" fill="#3B52D9" />
    
    {/* Chat Bubble */}
    <g filter="drop-shadow(0px 4px 6px rgba(0, 0, 0, 0.05))">
      <path d="M115 50 C115 35 125 25 145 25 C165 25 175 35 175 50 C175 65 165 75 145 75 C135 75 125 70 120 80 L115 80 C115 70 115 60 115 50 Z" fill="white" />
      <circle cx="135" cy="50" r="3" fill="#6366F1" />
      <circle cx="145" cy="50" r="3" fill="#6366F1" />
      <circle cx="155" cy="50" r="3" fill="#6366F1" />
    </g>
  </svg>
);

const SupportDashboard = () => {
  const [activeTab, setActiveTab] = useState('ALL');

  const { data: tickets = [] } = useQuery({ queryKey: ['support-tickets'], queryFn: async () => (await axiosPrivate.get('/support/tickets')).data });

  const openCount = tickets.filter(t => t.status === 'OPEN').length;
  const inProgressCount = tickets.filter(t => t.status === 'IN_PROGRESS').length;
  const resolvedCount = tickets.filter(t => t.status === 'RESOLVED').length;

  const tabs = ['ALL', 'OPEN', 'IN_PROGRESS', 'RESOLVED'];

  return (
    <div className="w-full h-full min-h-screen overflow-y-auto bg-[#F8FAFC] p-4 lg:p-8 flex flex-col font-sans" style={{ fontFamily: "'Inter', sans-serif" }}>
      
      {/* Tabs Toolbar */}
      <div className="flex gap-3 mb-10">
        {tabs.map(tab => (
          <button 
            key={tab}
            onClick={() => setActiveTab(tab)}
            className={`px-5 py-2.5 rounded-xl text-[12px] font-bold tracking-wide transition-all border ${
              activeTab === tab 
                ? 'bg-[#3B52D9] text-white border-[#3B52D9] shadow-lg shadow-blue-500/20' 
                : 'bg-white text-gray-700 border-gray-200 hover:bg-gray-50'
            }`}
          >
            {tab}
          </button>
        ))}
      </div>

      {/* Header */}
      <div className="flex items-center gap-5 mb-8">
        <div className="w-16 h-16 bg-[#EEF2FF] rounded-2xl shadow-sm flex items-center justify-center shrink-0">
          <Headset className="w-8 h-8 text-[#3B52D9]" strokeWidth={2.5} />
        </div>
        <div>
          <h1 className="text-[26px] font-bold text-[#1E293B] leading-tight mb-1">Customer Support & Ticket Desk</h1>
          <p className="text-[14px] text-gray-500 font-medium">Manage patient inquiries, support tickets, and chat threads.</p>
        </div>
      </div>

      {/* Main Content Grid */}
      <div className="flex flex-col lg:flex-row gap-6 flex-1">
        
        {/* Left Column (KPIs & Info) */}
        <div className="w-full lg:w-[400px] flex flex-col gap-6 shrink-0">
          
          {/* KPI Cards Row */}
          <div className="grid grid-cols-3 gap-4">
            
            {/* OPEN TICKETS */}
            <div className="bg-white rounded-2xl p-4 shadow-[0_2px_12px_rgba(0,0,0,0.02)] border border-gray-50 flex flex-col items-center justify-center text-center relative overflow-hidden">
              <div className="absolute bottom-5 left-1/2 -translate-x-1/2 w-[70%] h-0.5 bg-blue-500"></div>
              <div className="w-10 h-10 bg-blue-50/50 rounded-full flex items-center justify-center mb-3">
                <HelpCircle className="w-5 h-5 text-blue-600" strokeWidth={2} />
              </div>
              <p className="text-[8px] font-extrabold text-gray-500 uppercase tracking-widest mb-1">OPEN TICKETS</p>
              <p className="text-[24px] font-bold text-blue-600 leading-none mb-3">{openCount}</p>
            </div>
            
            {/* IN PROGRESS */}
            <div className="bg-white rounded-2xl p-4 shadow-[0_2px_12px_rgba(0,0,0,0.02)] border border-gray-50 flex flex-col items-center justify-center text-center relative overflow-hidden">
              <div className="absolute bottom-5 left-1/2 -translate-x-1/2 w-[70%] h-0.5 bg-orange-400"></div>
              <div className="w-10 h-10 bg-orange-50/50 rounded-full flex items-center justify-center mb-3">
                <Clock className="w-5 h-5 text-orange-500" strokeWidth={2} />
              </div>
              <p className="text-[8px] font-extrabold text-gray-500 uppercase tracking-widest mb-1">IN_PROGRESS</p>
              <p className="text-[24px] font-bold text-orange-500 leading-none mb-3">{inProgressCount}</p>
            </div>
            
            {/* RESOLVED */}
            <div className="bg-white rounded-2xl p-4 shadow-[0_2px_12px_rgba(0,0,0,0.02)] border border-gray-50 flex flex-col items-center justify-center text-center relative overflow-hidden">
              <div className="absolute bottom-5 left-1/2 -translate-x-1/2 w-[70%] h-0.5 bg-green-400"></div>
              <div className="w-10 h-10 bg-green-50/50 rounded-full flex items-center justify-center mb-3">
                <CheckCircle className="w-5 h-5 text-green-500" strokeWidth={2} />
              </div>
              <p className="text-[8px] font-extrabold text-gray-500 uppercase tracking-widest mb-1">RESOLVED</p>
              <p className="text-[24px] font-bold text-green-500 leading-none mb-3">{resolvedCount}</p>
            </div>

          </div>

          {/* Info Card */}
          <div className="bg-[#F8FAFC] rounded-2xl p-8 shadow-[0_2px_16px_rgba(0,0,0,0.02)] border border-[#F1F5F9] flex flex-col items-center text-center">
            <HeadsetIllustration />
            <h4 className="text-[15px] font-bold text-[#1E293B] mt-4 mb-2">We're here to help</h4>
            <p className="text-[13px] text-gray-500 leading-relaxed max-w-[250px] mb-6">Create a new ticket, track updates, and get quick support.</p>
            <button className="flex items-center gap-2 px-5 py-2.5 bg-white border border-[#E2E8F0] text-[#3B52D9] rounded-xl text-[13px] font-bold hover:bg-gray-50 transition-all shadow-sm">
              <Plus className="w-4 h-4" strokeWidth={2.5} /> New Support Ticket
            </button>
          </div>

        </div>

        {/* Right Column (Main Workspace) */}
        <div className="flex-1 bg-white rounded-[24px] shadow-[0_2px_24px_rgba(0,0,0,0.02)] border border-gray-100 p-6 flex flex-col relative overflow-hidden min-h-[500px]">
          
          <DotsPattern className="absolute top-20 right-8 pointer-events-none" />
          <DotsPattern className="absolute bottom-20 left-8 pointer-events-none" />

          {/* Toolbar */}
          <div className="flex gap-4 mb-16 relative z-10">
            <div className="flex-1 relative">
              <Search className="w-4 h-4 text-gray-400 absolute left-4 top-1/2 -translate-y-1/2" strokeWidth={2.5} />
              <input 
                type="text" 
                placeholder="Search tickets..." 
                className="w-full pl-11 pr-4 py-3 bg-[#F8FAFC] border border-gray-100 rounded-xl text-[13px] font-medium text-gray-700 placeholder-gray-400 focus:bg-white focus:border-[#3B52D9] focus:ring-4 focus:ring-blue-50 focus:outline-none transition-all" 
              />
            </div>
            <button className="flex items-center gap-2 px-5 py-3 bg-white border border-gray-200 rounded-xl text-[13px] font-semibold text-gray-700 hover:bg-gray-50 hover:border-gray-300 transition-colors shadow-sm">
              <Filter className="w-4 h-4 text-gray-500" strokeWidth={2.5} /> 
              Filter 
              <ChevronDown className="w-4 h-4 text-gray-400" strokeWidth={2.5} />
            </button>
          </div>

          {/* Empty State */}
          <div className="flex-1 flex flex-col items-center justify-center text-center relative z-10 pb-10">
            <EmptyStateIcon />
            <h3 className="text-[17px] font-bold text-[#1E293B] mb-2 mt-4">No tickets found</h3>
            <p className="text-[13px] text-gray-500 mb-8 font-medium">There are no entries to display at this time.</p>
            <button className="flex items-center gap-2 px-6 py-3 bg-[#3B52D9] text-white rounded-xl text-[13px] font-semibold hover:bg-[#2e42b8] transition-all shadow-lg shadow-blue-500/30">
              <Plus className="w-4 h-4" strokeWidth={2.5} /> Create New Ticket
            </button>
          </div>

        </div>

      </div>
    </div>
  );
};

export default SupportDashboard;
