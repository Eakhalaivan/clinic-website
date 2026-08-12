import React, { useState } from 'react';
import { Search, ChevronDown, Filter, Plus, Eye, Edit2, Download, MoreVertical, FileText, ArrowUp, Activity, HeartPulse } from 'lucide-react';
import { useNavigate } from 'react-router-dom';

const DoctorPrescriptions = () => {
  const navigate = useNavigate();
  const [activeTab, setActiveTab] = useState('All Prescriptions');

  const prescriptions = [
    {
      id: 'RX-0001',
      subId: '#101',
      patient: { name: 'Robert Williams', details: '45 Years, Male', id: 1 },
      date: '21 May 2024',
      time: '10:30 AM',
      diagnosis: 'Hypertension',
      medicines: '3 Medicines',
      status: 'Active',
      statusColor: 'green'
    },
    {
      id: 'RX-0002',
      subId: '#102',
      patient: { name: 'Emily Davis', details: '36 Years, Female', id: 2 },
      date: '20 May 2024',
      time: '09:15 AM',
      diagnosis: 'Diabetes Type 2',
      medicines: '4 Medicines',
      status: 'Active',
      statusColor: 'green'
    },
    {
      id: 'RX-0003',
      subId: '#103',
      patient: { name: 'Michael Johnson', details: '50 Years, Male', id: 3 },
      date: '19 May 2024',
      time: '11:00 AM',
      diagnosis: 'Acute Bronchitis',
      medicines: '5 Medicines',
      status: 'Completed',
      statusColor: 'blue'
    },
    {
      id: 'RX-0004',
      subId: '#104',
      patient: { name: 'Sarah Wilson', details: '34 Years, Female', id: 4 },
      date: '18 May 2024',
      time: '02:30 PM',
      diagnosis: 'Thyroid Disorder',
      medicines: '2 Medicines',
      status: 'Active',
      statusColor: 'green'
    },
    {
      id: 'RX-0005',
      subId: '#105',
      patient: { name: 'David Brown', details: '47 Years, Male', id: 5 },
      date: '17 May 2024',
      time: '03:45 PM',
      diagnosis: 'Gastritis',
      medicines: '3 Medicines',
      status: 'Completed',
      statusColor: 'blue'
    },
    {
      id: 'RX-0006',
      subId: '#106',
      patient: { name: 'Linda Taylor', details: '40 Years, Female', id: 6 },
      date: '16 May 2024',
      time: '10:20 AM',
      diagnosis: 'Migraine',
      medicines: '2 Medicines',
      status: 'Discontinued',
      statusColor: 'red'
    },
    {
      id: 'RX-0007',
      subId: '#107',
      patient: { name: 'Daniel Martinez', details: '41 Years, Male', id: 7 },
      date: '15 May 2024',
      time: '01:10 PM',
      diagnosis: 'Allergic Rhinitis',
      medicines: '3 Medicines',
      status: 'Active',
      statusColor: 'green'
    },
    {
      id: 'RX-0008',
      subId: '#108',
      patient: { name: 'Patricia Harris', details: '38 Years, Female', id: 8 },
      date: '14 May 2024',
      time: '09:05 AM',
      diagnosis: 'Vitamin D Deficiency',
      medicines: '1 Medicine',
      status: 'Completed',
      statusColor: 'blue'
    }
  ];

  const getStatusBadgeClasses = (color) => {
    switch (color) {
      case 'green': return 'bg-[#F0FDF4] text-[#16A34A]';
      case 'blue': return 'bg-[#EFF6FF] text-[#2563EB]';
      case 'red': return 'bg-[#FEF2F2] text-[#DC2626]';
      default: return 'bg-gray-100 text-gray-700';
    }
  };

  const tabs = ['All Prescriptions', 'Draft', 'Active', 'Completed', 'Discontinued'];

  return (
    <div className="p-6 md:p-8 bg-white min-h-full font-sans">
      <div className="max-w-[1500px] mx-auto">
        
        {/* Header */}
        <div className="flex flex-col md:flex-row md:items-center justify-between gap-4 mb-6">
          <div>
            <h1 className="text-2xl font-bold text-slate-800">Prescriptions</h1>
            <p className="text-sm font-medium text-slate-500 mt-1">Create, manage and view all patient prescriptions</p>
          </div>
          <button 
            onClick={() => navigate('/doctor/prescription-templates')}
            className="flex items-center gap-2 bg-[#5B21B6] hover:bg-indigo-800 text-white px-5 py-2.5 rounded-lg text-sm font-bold shadow-sm transition-colors"
          >
            <Plus size={16} strokeWidth={2.5} /> New Prescription
          </button>
        </div>

        {/* Tabs and Filters Row */}
        <div className="flex flex-col xl:flex-row xl:items-center justify-between gap-4 mb-6 border-b border-slate-200">
          <div className="flex gap-6 overflow-x-auto scrollbar-hide">
            {tabs.map(tab => (
              <button
                key={tab}
                onClick={() => setActiveTab(tab)}
                className={`pb-3 text-sm font-bold whitespace-nowrap transition-colors relative ${
                  activeTab === tab ? 'text-[#5B21B6]' : 'text-slate-500 hover:text-slate-700'
                }`}
              >
                {tab}
                {activeTab === tab && (
                  <div className="absolute bottom-0 left-0 right-0 h-[3px] bg-[#5B21B6] rounded-t-md"></div>
                )}
              </button>
            ))}
          </div>

          <div className="flex flex-wrap items-center gap-3 pb-3">
            <div className="relative">
              <Search className="absolute left-3 top-1/2 -translate-y-1/2 text-slate-400" size={16} />
              <input 
                type="text" 
                placeholder="Search prescriptions..." 
                className="pl-9 pr-4 py-2 border border-slate-200 rounded-lg text-[13px] font-medium text-slate-700 focus:outline-none focus:border-[#5B21B6] w-[220px]"
              />
            </div>
            
            <button className="flex items-center gap-2 px-3 py-2 border border-slate-200 rounded-lg text-[13px] font-bold text-slate-700 hover:bg-slate-50">
              All Patients <ChevronDown size={14} className="text-slate-400" />
            </button>
            
            <button className="flex items-center gap-2 px-3 py-2 border border-slate-200 rounded-lg text-[13px] font-bold text-slate-700 hover:bg-slate-50">
              All Status <ChevronDown size={14} className="text-slate-400" />
            </button>
            
            <button className="flex items-center gap-2 px-4 py-2 border border-slate-200 rounded-lg text-[13px] font-bold text-slate-700 hover:bg-slate-50">
              <Filter size={14} className="text-slate-500" /> Filter
            </button>
          </div>
        </div>

        {/* Main Content Grid */}
        <div className="flex flex-col xl:flex-row gap-6">
          
          {/* Table Area */}
          <div className="flex-1 overflow-x-auto border border-slate-200 rounded-xl shadow-sm">
            <table className="w-full text-left border-collapse min-w-[900px]">
              <thead>
                <tr className="bg-[#F8FAFC] border-b border-slate-200">
                  <th className="py-4 px-6 text-[12px] font-bold text-slate-500 whitespace-nowrap">Prescription ID</th>
                  <th className="py-4 px-6 text-[12px] font-bold text-slate-500 whitespace-nowrap">Patient</th>
                  <th className="py-4 px-6 text-[12px] font-bold text-slate-500 whitespace-nowrap">Date</th>
                  <th className="py-4 px-6 text-[12px] font-bold text-slate-500 whitespace-nowrap">Diagnosis</th>
                  <th className="py-4 px-6 text-[12px] font-bold text-slate-500 whitespace-nowrap">Medicines</th>
                  <th className="py-4 px-6 text-[12px] font-bold text-slate-500 whitespace-nowrap">Status</th>
                  <th className="py-4 px-6 text-[12px] font-bold text-slate-500 whitespace-nowrap text-center">Actions</th>
                </tr>
              </thead>
              <tbody>
                {prescriptions.map((rx, idx) => (
                  <tr key={idx} className="border-b border-slate-100 hover:bg-slate-50 transition-colors bg-white">
                    <td className="py-4 px-6">
                      <p className="text-[13px] font-bold text-slate-800 leading-tight">{rx.id}</p>
                      <p className="text-[11px] font-medium text-slate-400 mt-0.5">{rx.subId}</p>
                    </td>
                    <td className="py-4 px-6">
                      <div className="flex items-center gap-3">
                        <img loading="lazy" 
                          src={`https://i.pravatar.cc/150?u=${rx.patient.id}`} 
                          alt={rx.patient.name}
                          className="w-8 h-8 rounded-full object-cover shadow-sm"
                        />
                        <div>
                          <p className="text-[13px] font-bold text-slate-800 leading-tight">{rx.patient.name}</p>
                          <p className="text-[11px] font-medium text-slate-500 mt-0.5">{rx.patient.details}</p>
                        </div>
                      </div>
                    </td>
                    <td className="py-4 px-6">
                      <p className="text-[13px] font-bold text-slate-800 leading-tight">{rx.date}</p>
                      <p className="text-[11px] font-medium text-slate-500 mt-0.5">{rx.time}</p>
                    </td>
                    <td className="py-4 px-6 text-[13px] font-bold text-slate-700">
                      {rx.diagnosis}
                    </td>
                    <td className="py-4 px-6 text-[13px] font-bold text-slate-800">
                      {rx.medicines}
                    </td>
                    <td className="py-4 px-6">
                      <span className={`px-2.5 py-1 text-[10px] font-bold rounded-md ${getStatusBadgeClasses(rx.statusColor)}`}>
                        {rx.status}
                      </span>
                    </td>
                    <td className="py-4 px-6">
                      <div className="flex items-center justify-center gap-3 text-[#5B21B6]">
                        <button className="w-7 h-7 flex items-center justify-center rounded bg-indigo-50 hover:bg-indigo-100 transition-colors">
                          <Eye size={14} strokeWidth={2.5} />
                        </button>
                        <button className="w-7 h-7 flex items-center justify-center rounded bg-indigo-50 hover:bg-indigo-100 transition-colors">
                          <Edit2 size={14} strokeWidth={2.5} />
                        </button>
                        <button className="w-7 h-7 flex items-center justify-center rounded bg-indigo-50 hover:bg-indigo-100 transition-colors">
                          <Download size={14} strokeWidth={2.5} />
                        </button>
                        <button className="text-slate-400 hover:text-slate-600">
                          <MoreVertical size={16} strokeWidth={2.5} />
                        </button>
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
            
            {/* Pagination Footer */}
            <div className="flex items-center justify-between p-4 bg-white border-t border-slate-200">
              <span className="text-[12px] font-medium text-slate-500">Showing 1 to 8 of 24 prescriptions</span>
              <div className="flex items-center gap-1">
                <button className="w-8 h-8 flex items-center justify-center rounded border border-slate-200 text-slate-500 hover:bg-slate-50">&lt;</button>
                <button className="w-8 h-8 flex items-center justify-center rounded bg-[#5B21B6] text-white font-bold text-[13px]">1</button>
                <button className="w-8 h-8 flex items-center justify-center rounded border border-slate-200 text-slate-600 font-bold text-[13px] hover:bg-slate-50">2</button>
                <button className="w-8 h-8 flex items-center justify-center rounded border border-slate-200 text-slate-600 font-bold text-[13px] hover:bg-slate-50">3</button>
                <button className="w-8 h-8 flex items-center justify-center rounded border border-slate-200 text-slate-500 hover:bg-slate-50">&gt;</button>
              </div>
            </div>
          </div>

          {/* Right Sidebar */}
          <div className="w-full xl:w-[320px] flex-shrink-0 flex flex-col gap-6">
            
            {/* Prescription Summary */}
            <div className="bg-white rounded-xl border border-slate-200 shadow-sm p-5">
              <div className="flex items-center justify-between mb-4">
                <h3 className="text-[14px] font-bold text-slate-800">Prescription Summary</h3>
                <button className="flex items-center gap-1 text-[11px] font-bold text-slate-500 hover:text-slate-700">
                  This Month <ChevronDown size={14} />
                </button>
              </div>
              
              <div className="grid grid-cols-2 gap-3">
                <div className="bg-[#F0FDF4] border border-[#DCFCE7] rounded-xl p-4 flex flex-col justify-center">
                  <span className="text-[24px] font-bold text-[#16A34A] leading-none mb-2">24</span>
                  <span className="text-[11px] font-bold text-slate-800 mb-1">Total Prescriptions</span>
                  <span className="text-[9px] font-bold text-[#16A34A] flex items-center gap-0.5"><ArrowUp size={10} strokeWidth={3}/> 12% from last month</span>
                </div>
                
                <div className="bg-[#EFF6FF] border border-[#DBEAFE] rounded-xl p-4 flex flex-col justify-center">
                  <span className="text-[24px] font-bold text-[#2563EB] leading-none mb-2">14</span>
                  <span className="text-[11px] font-bold text-[#1D4ED8] mb-1">Active Prescriptions</span>
                  <span className="text-[9px] font-semibold text-slate-500">Currently in progress</span>
                </div>
                
                <div className="bg-[#FFF7ED] border border-[#FFEDD5] rounded-xl p-4 flex flex-col justify-center">
                  <span className="text-[18px] font-bold text-[#EA580C] leading-none mb-2">8</span>
                  <span className="text-[11px] font-bold text-[#C2410C] mb-1">Completed</span>
                  <span className="text-[9px] font-semibold text-slate-500">This month</span>
                </div>
                
                <div className="bg-[#FEF2F2] border border-[#FEE2E2] rounded-xl p-4 flex flex-col justify-center">
                  <span className="text-[18px] font-bold text-[#DC2626] leading-none mb-2">2</span>
                  <span className="text-[11px] font-bold text-[#B91C1C] mb-1">Discontinued</span>
                  <span className="text-[9px] font-semibold text-slate-500">This month</span>
                </div>
              </div>
            </div>

            {/* Top Diagnoses */}
            <div className="bg-white rounded-xl border border-slate-200 shadow-sm p-5">
              <div className="flex items-center justify-between mb-5">
                <h3 className="text-[14px] font-bold text-slate-800">Top Diagnoses</h3>
                <button className="flex items-center gap-1 text-[11px] font-bold text-slate-500 hover:text-slate-700">
                  This Month <ChevronDown size={14} />
                </button>
              </div>

              <div className="flex flex-col gap-4">
                {[
                  { name: 'Hypertension', icon: <HeartPulse size={14} />, count: '6', pct: '25%', fill: 'w-[25%]' },
                  { name: 'Diabetes Type 2', icon: <Activity size={14} />, count: '4', pct: '16.7%', fill: 'w-[16.7%]' },
                  { name: 'Acute Bronchitis', icon: <FileText size={14} />, count: '3', pct: '12.5%', fill: 'w-[12.5%]' },
                  { name: 'Thyroid Disorder', icon: <Activity size={14} />, count: '2', pct: '8.3%', fill: 'w-[8.3%]' },
                  { name: 'Migraine', icon: <Activity size={14} />, count: '2', pct: '8.3%', fill: 'w-[8.3%]' }
                ].map((item, idx) => (
                  <div key={idx} className="flex flex-col gap-1.5">
                    <div className="flex items-center justify-between text-[11px] font-bold text-slate-700">
                      <div className="flex items-center gap-2">
                        <div className="text-slate-400">{item.icon}</div>
                        <span>{item.name}</span>
                      </div>
                      <span className="text-slate-500">{item.count} ({item.pct})</span>
                    </div>
                    <div className="w-full bg-slate-100 rounded-full h-1.5">
                      <div className={`bg-[#5B21B6] h-1.5 rounded-full ${item.fill}`}></div>
                    </div>
                  </div>
                ))}
              </div>
            </div>

            {/* Recent Drafts */}
            <div className="bg-white rounded-xl border border-slate-200 shadow-sm p-5">
              <div className="flex items-center justify-between mb-4">
                <h3 className="text-[14px] font-bold text-slate-800">Recent Drafts</h3>
                <button className="text-[11px] font-bold text-[#5B21B6] hover:underline">View All</button>
              </div>

              <div className="flex items-center justify-between p-3 border border-slate-100 rounded-lg">
                <div className="flex items-center gap-3">
                  <div className="w-9 h-9 rounded bg-indigo-50 text-[#5B21B6] flex items-center justify-center">
                    <FileText size={16} />
                  </div>
                  <div>
                    <p className="text-[12px] font-bold text-slate-800">Emily Davis</p>
                    <p className="text-[10px] font-medium text-slate-500">Draft created, 10 min ago</p>
                  </div>
                </div>
                <span className="px-2 py-0.5 bg-orange-50 text-[#EA580C] text-[10px] font-bold rounded">Draft</span>
              </div>
            </div>
            
          </div>
        </div>

      </div>
    </div>
  );
};

export default DoctorPrescriptions;
