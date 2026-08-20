import React, { useState } from 'react';
import { useQuery } from '@tanstack/react-query';
import { axiosPrivate } from '../../api/axios';
import { Search, Filter, Package, AlertTriangle, Home, ShoppingCart, Plus, ChevronDown } from 'lucide-react';

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

const Illustration = () => (
  <svg width="100%" height="130" viewBox="0 0 180 120" fill="none" xmlns="http://www.w3.org/2000/svg" className="max-w-[200px]">
    {/* Base shadow/glow */}
    <ellipse cx="90" cy="110" rx="70" ry="10" fill="#F1F5F9" />
    
    {/* Box */}
    <path d="M30 75 L60 85 L60 115 L30 105 Z" fill="#6366F1" />
    <path d="M60 85 L100 75 L100 105 L60 115 Z" fill="#4F46E5" />
    <path d="M30 75 L70 65 L100 75 L60 85 Z" fill="#818CF8" />
    <path d="M60 85 L80 80 L80 110 L60 115 Z" fill="#A5B4FC" opacity="0.4"/>
    
    {/* Clipboard */}
    <rect x="95" y="35" width="40" height="55" rx="4" fill="white" stroke="#E2E8F0" strokeWidth="2.5"/>
    {/* Clip */}
    <path d="M105 35 V30 C105 27 107 25 110 25 H120 C123 25 125 27 125 30 V35" fill="#CBD5E1" />
    {/* Check marks & lines */}
    <path d="M101 48 L105 52 L112 45" stroke="#10B981" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round" />
    <line x1="115" y1="48" x2="128" y2="48" stroke="#E2E8F0" strokeWidth="2" strokeLinecap="round" />
    
    <path d="M101 62 L105 66 L112 59" stroke="#10B981" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round" />
    <line x1="115" y1="62" x2="128" y2="62" stroke="#E2E8F0" strokeWidth="2" strokeLinecap="round" />
    
    <path d="M101 76 L105 80 L112 73" stroke="#CBD5E1" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round" />
    <line x1="115" y1="76" x2="128" y2="76" stroke="#E2E8F0" strokeWidth="2" strokeLinecap="round" />
    
    {/* Floating elements */}
    <rect x="135" y="65" width="25" height="15" rx="3" fill="#F8FAFC" stroke="#E2E8F0" strokeWidth="1.5" />
    <line x1="140" y1="72.5" x2="155" y2="72.5" stroke="#CBD5E1" strokeWidth="1.5" strokeLinecap="round" />
    
    <rect x="15" y="50" width="25" height="15" rx="3" fill="#F8FAFC" stroke="#E2E8F0" strokeWidth="1.5" />
    <line x1="20" y1="57.5" x2="35" y2="57.5" stroke="#CBD5E1" strokeWidth="1.5" strokeLinecap="round" />
    
    {/* Green success badge */}
    <circle cx="130" cy="30" r="10" fill="#10B981" />
    <path d="M126 30 L129 33 L134 27" stroke="white" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round" />
  </svg>
);

const InventoryDashboard = () => {
  const [activeTab, setActiveTab] = useState('stock');

  const { data: stockItems = [] } = useQuery({ queryKey: ['backoffice-stock'], queryFn: async () => (await axiosPrivate.get('/backoffice/inventory/stock')).data });
  const { data: warehouses = [] } = useQuery({ queryKey: ['backoffice-warehouses'], queryFn: async () => (await axiosPrivate.get('/backoffice/inventory/warehouses')).data });
  const { data: purchaseOrders = [] } = useQuery({ queryKey: ['backoffice-po'], queryFn: async () => (await axiosPrivate.get('/backoffice/inventory/purchase-orders')).data });

  const stockCount = stockItems.length;
  const lowStockCount = stockItems.filter(item => item.quantity <= item.reorderLevel).length;
  const availableCount = stockItems.filter(item => item.quantity > item.reorderLevel).length;

  const tabs = [
    { id: 'stock', label: 'Stock', icon: Package },
    { id: 'warehouses', label: 'Warehouses', icon: Home },
    { id: 'purchase-orders', label: 'Purchase Orders', icon: ShoppingCart }
  ];

  return (
    <div className="w-full h-full min-h-screen overflow-y-auto bg-[#F8FAFC] p-4 lg:p-8 flex flex-col font-sans" style={{ fontFamily: "'Inter', sans-serif" }}>
      
      {/* Tabs Toolbar */}
      <div className="flex gap-3 mb-10">
        {tabs.map(tab => (
          <button 
            key={tab.id}
            onClick={() => setActiveTab(tab.id)}
            className={`flex items-center gap-2 px-5 py-2.5 rounded-xl text-[13px] font-semibold transition-all border ${
              activeTab === tab.id 
                ? 'bg-[#3B52D9] text-white border-[#3B52D9] shadow-lg shadow-blue-500/20' 
                : 'bg-white text-gray-600 border-gray-200 hover:bg-gray-50'
            }`}
          >
            <tab.icon className={`w-4 h-4 ${activeTab === tab.id ? 'text-white' : 'text-gray-400'}`} strokeWidth={2.5} />
            {tab.label}
          </button>
        ))}
      </div>

      {/* Header */}
      <div className="flex items-center gap-5 mb-8">
        <div className="w-16 h-16 bg-white rounded-[20px] border border-gray-100 shadow-[0_2px_12px_rgba(0,0,0,0.03)] flex items-center justify-center shrink-0">
          <Package className="w-8 h-8 text-[#3B52D9]" strokeWidth={2.5} />
        </div>
        <div>
          <h1 className="text-[26px] font-bold text-[#1E293B] leading-tight mb-1">Back-Office Inventory Management</h1>
          <p className="text-[14px] text-gray-500 font-medium">Central medical supplies stock, warehouse management, and vendor purchase orders.</p>
        </div>
      </div>

      {/* Main Content Grid */}
      <div className="flex flex-col lg:flex-row gap-6 flex-1">
        
        {/* Left Column (KPIs & Info) */}
        <div className="w-full lg:w-[400px] flex flex-col gap-6 shrink-0">
          
          {/* KPI Cards Row */}
          <div className="grid grid-cols-3 gap-4">
            
            {/* Total Items */}
            <div className="bg-white rounded-2xl p-4 shadow-[0_2px_12px_rgba(0,0,0,0.02)] border border-gray-50 flex flex-col items-center justify-center text-center relative overflow-hidden">
              <div className="absolute bottom-0 left-1/2 -translate-x-1/2 w-8 h-1 bg-blue-600 rounded-t-full"></div>
              <div className="w-12 h-12 bg-blue-50/50 rounded-xl flex items-center justify-center mb-3 border border-blue-50">
                <Package className="w-6 h-6 text-blue-700" strokeWidth={2} />
              </div>
              <p className="text-[9px] font-bold text-gray-500 uppercase tracking-wider mb-1">TOTAL ITEMS</p>
              <p className="text-[26px] font-bold text-blue-700 leading-none">{stockCount}</p>
            </div>
            
            {/* Low Stock */}
            <div className="bg-white rounded-2xl p-4 shadow-[0_2px_12px_rgba(0,0,0,0.02)] border border-gray-50 flex flex-col items-center justify-center text-center relative overflow-hidden">
              <div className="absolute bottom-0 left-1/2 -translate-x-1/2 w-8 h-1 bg-red-500 rounded-t-full"></div>
              <div className="w-12 h-12 bg-red-50/50 rounded-xl flex items-center justify-center mb-3 border border-red-50">
                <AlertTriangle className="w-6 h-6 text-red-500" strokeWidth={2} />
              </div>
              <p className="text-[9px] font-bold text-gray-500 uppercase tracking-wider mb-1">LOW STOCK</p>
              <p className="text-[26px] font-bold text-red-500 leading-none">{lowStockCount}</p>
            </div>
            
            {/* Available */}
            <div className="bg-white rounded-2xl p-4 shadow-[0_2px_12px_rgba(0,0,0,0.02)] border border-gray-50 flex flex-col items-center justify-center text-center relative overflow-hidden">
              <div className="absolute bottom-0 left-1/2 -translate-x-1/2 w-8 h-1 bg-green-500 rounded-t-full"></div>
              <div className="w-12 h-12 bg-green-50/50 rounded-xl flex items-center justify-center mb-3 border border-green-50">
                <Home className="w-6 h-6 text-green-500" strokeWidth={2} />
              </div>
              <p className="text-[9px] font-bold text-gray-500 uppercase tracking-wider mb-1">AVAILABLE</p>
              <p className="text-[26px] font-bold text-green-500 leading-none">{availableCount}</p>
            </div>

          </div>

          {/* Info Card */}
          <div className="bg-white rounded-2xl p-8 shadow-[0_2px_16px_rgba(0,0,0,0.02)] border border-gray-50 flex flex-col items-center text-center">
            <Illustration />
            <h4 className="text-[15px] font-bold text-[#1E293B] mt-6 mb-2">Keep inventory in check</h4>
            <p className="text-[13px] text-gray-500 leading-relaxed max-w-[250px]">Add stock items, manage quantities, and track low stock alerts.</p>
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
                placeholder="Search stock catalog..." 
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
            <h3 className="text-[17px] font-bold text-[#1E293B] mb-2 mt-4">No stock items found</h3>
            <p className="text-[13px] text-gray-500 mb-8 font-medium">There are no entries to display at this time.</p>
            <button className="flex items-center gap-2 px-6 py-3 bg-[#3B52D9] text-white rounded-xl text-[13px] font-semibold hover:bg-[#2e42b8] transition-all shadow-lg shadow-blue-500/30">
              <Plus className="w-4 h-4" strokeWidth={2.5} /> Add Stock Item
            </button>
          </div>

        </div>

      </div>
    </div>
  );
};

export default InventoryDashboard;
