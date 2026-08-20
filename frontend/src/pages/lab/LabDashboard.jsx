import React from 'react';
import { 
  FileText, FlaskConical, Beaker, Settings, CheckSquare, ShieldCheck, 
  Upload, XCircle, Plus, Printer, Clock, AlertCircle, BookOpen,
  Calendar, Bell, ChevronDown, BarChart2, FolderOpen
} from 'lucide-react';

const LabDashboard = () => {
  return (
    <div className="w-full h-full min-h-screen overflow-y-auto bg-[#F8FAFC] p-4 lg:p-6" style={{ fontFamily: "'Inter', sans-serif" }}>
      
      {/* Header */}
      <div className="flex flex-col md:flex-row justify-between items-start md:items-center mb-6 gap-4">
        <div>
          <p className="text-[13px] font-medium text-gray-500 mb-0.5">Welcome back, Lab Admin! 👋</p>
          <h1 className="text-2xl font-bold text-gray-900 leading-tight">Laboratory Dashboard</h1>
        </div>
        <div className="flex items-center gap-3">
          <button className="flex items-center gap-2 px-3 py-2 bg-white border border-gray-200 rounded-lg text-[13px] font-semibold text-gray-700 shadow-sm hover:bg-gray-50">
            <Calendar className="w-4 h-4 text-gray-500" />
            May 21, 2025
            <ChevronDown className="w-4 h-4 text-gray-400" />
          </button>
          <button className="relative p-2.5 bg-white border border-gray-200 rounded-lg shadow-sm hover:bg-gray-50">
            <Bell className="w-4 h-4 text-gray-600" />
            <span className="absolute top-1.5 right-1.5 w-2 h-2 bg-red-500 rounded-full ring-2 ring-white"></span>
          </button>
        </div>
      </div>

      {/* KPI Cards Row */}
      <div className="grid grid-cols-2 md:grid-cols-4 xl:grid-cols-8 gap-3 mb-6">
        {[
          { title: 'TOTAL REQUESTS', val: '0', sub: 'Today', icon: FileText, color: 'text-blue-600', bg: 'bg-blue-50', hasGreenIcon: true },
          { title: 'REQUESTED', val: '0', sub: 'Awaiting samples', icon: FlaskConical, color: 'text-orange-500', bg: 'bg-orange-50' },
          { title: 'SAMPLES COLLECTED', val: '0', sub: 'Collected today', icon: Beaker, color: 'text-blue-600', bg: 'bg-blue-50' },
          { title: 'PROCESSING', val: '0', sub: 'In progress', icon: Settings, color: 'text-purple-600', bg: 'bg-purple-50' },
          { title: 'RESULT ENTERED', val: '0', sub: 'Awaiting verification', icon: CheckSquare, color: 'text-teal-600', bg: 'bg-teal-50' },
          { title: 'VERIFIED', val: '0', sub: 'Ready to release', icon: ShieldCheck, color: 'text-green-600', bg: 'bg-green-50' },
          { title: 'RELEASED', val: '0', sub: 'Released today', icon: Upload, color: 'text-indigo-600', bg: 'bg-indigo-50' },
          { title: 'REJECTED', val: '0', sub: 'Requires attention', icon: XCircle, color: 'text-red-500', bg: 'bg-red-50' },
        ].map((kpi, i) => (
          <div key={i} className="bg-white border border-gray-100 rounded-xl p-3 flex flex-col justify-between shadow-[0_2px_8px_rgba(0,0,0,0.02)] min-h-[100px]">
            <div className="flex items-start gap-3">
              <div className={`w-9 h-9 rounded-lg ${kpi.bg} flex items-center justify-center shrink-0`}>
                <kpi.icon className={`w-5 h-5 ${kpi.color}`} />
              </div>
              <div>
                <p className="text-[9px] font-bold text-gray-500 uppercase tracking-wider">{kpi.title}</p>
                <p className="text-2xl font-bold text-gray-900 leading-tight mt-0.5">{kpi.val}</p>
              </div>
            </div>
            <div className="flex items-center gap-1 mt-4">
              <p className="text-[11px] text-gray-500">{kpi.sub}</p>
              {kpi.hasGreenIcon && (
                <div className="w-3 h-3 rounded-full border border-green-500 flex items-center justify-center">
                  <div className="w-1.5 h-1.5 bg-green-500 rounded-full"></div>
                </div>
              )}
            </div>
          </div>
        ))}
      </div>

      {/* Quick Actions Row */}
      <div className="mb-6">
        <h2 className="text-sm font-bold text-gray-900 mb-3">Quick Actions</h2>
        <div className="grid grid-cols-2 sm:grid-cols-4 lg:grid-cols-8 gap-3">
          <button className="flex flex-col items-center justify-center gap-2 bg-gradient-to-br from-[#4F46E5] to-[#7C3AED] rounded-xl p-4 shadow-sm hover:shadow-md transition-shadow h-[84px]">
            <Plus className="w-5 h-5 text-white" />
            <span className="text-[11px] font-semibold text-white">New Request</span>
          </button>
          
          {[
            { label: 'Collect Sample', icon: Beaker, color: 'text-blue-500' },
            { label: 'Enter Results', icon: FlaskConical, color: 'text-purple-500' },
            { label: 'Verify Results', icon: CheckSquare, color: 'text-teal-500' },
            { label: 'Print Reports', icon: Printer, color: 'text-gray-500' },
            { label: 'Patient History', icon: Clock, color: 'text-green-500' },
            { label: 'Alerts', icon: AlertCircle, color: 'text-red-500' },
            { label: 'Catalog', icon: BookOpen, color: 'text-blue-500' },
          ].map((action, i) => (
            <button key={i} className="flex flex-col items-center justify-center gap-2 bg-white border border-gray-100 rounded-xl p-4 shadow-sm hover:bg-gray-50 transition-colors h-[84px]">
              <action.icon className={`w-5 h-5 ${action.color}`} />
              <span className="text-[11px] font-semibold text-gray-600">{action.label}</span>
            </button>
          ))}
        </div>
      </div>

      {/* Middle Row: Overview, Priority, Status */}
      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6 mb-6">
        {/* Requests Overview */}
        <div className="bg-white border border-gray-100 rounded-xl p-5 shadow-[0_2px_8px_rgba(0,0,0,0.02)] flex flex-col h-[300px]">
          <div className="flex justify-between items-center mb-6">
            <h3 className="text-[14px] font-bold text-gray-900">Requests Overview</h3>
            <button className="flex items-center gap-1.5 px-2.5 py-1 bg-white border border-gray-200 rounded text-[11px] font-semibold text-gray-600">
              <Calendar className="w-3 h-3" />
              May 21, 2025
            </button>
          </div>
          <div className="flex-1 flex flex-col items-center justify-center text-center">
            <div className="w-16 h-16 mb-4 relative flex items-center justify-center">
               <FolderOpen className="w-10 h-10 text-blue-100" strokeWidth={1.5} />
            </div>
            <h4 className="text-[13px] font-bold text-gray-900 mb-1">No requests yet</h4>
            <p className="text-[12px] text-gray-500">You're all caught up! New requests will appear here.</p>
          </div>
        </div>

        {/* Priority Breakdown */}
        <div className="bg-white border border-gray-100 rounded-xl p-5 shadow-[0_2px_8px_rgba(0,0,0,0.02)] flex flex-col h-[300px]">
          <h3 className="text-[14px] font-bold text-gray-900 mb-6">Priority Breakdown</h3>
          <div className="flex-1 flex items-center justify-center gap-8">
            <div className="relative flex items-center justify-center">
              <svg viewBox="0 0 100 100" className="w-36 h-36 transform -rotate-90">
                <circle cx="50" cy="50" r="35" fill="none" stroke="#EF4444" strokeWidth="12" strokeDasharray="45 175" strokeDashoffset="0" />
                <circle cx="50" cy="50" r="35" fill="none" stroke="#F59E0B" strokeWidth="12" strokeDasharray="45 175" strokeDashoffset="-55" />
                <circle cx="50" cy="50" r="35" fill="none" stroke="#3B82F6" strokeWidth="12" strokeDasharray="45 175" strokeDashoffset="-110" />
                <circle cx="50" cy="50" r="35" fill="none" stroke="#10B981" strokeWidth="12" strokeDasharray="45 175" strokeDashoffset="-165" />
              </svg>
              <div className="absolute inset-0 flex flex-col items-center justify-center">
                <span className="text-xl font-bold text-gray-900 leading-none">0</span>
                <span className="text-[11px] text-gray-500 mt-1">Total</span>
              </div>
            </div>
            <div className="flex flex-col gap-4">
              {[
                { label: 'High Priority', color: 'bg-red-500' },
                { label: 'Medium Priority', color: 'bg-yellow-500' },
                { label: 'Low Priority', color: 'bg-blue-500' },
                { label: 'Routine', color: 'bg-green-500' }
              ].map((item, i) => (
                <div key={i} className="flex items-center gap-2">
                  <div className={`w-2.5 h-2.5 rounded-full ${item.color}`}></div>
                  <span className="text-[12px] text-gray-600 w-24">{item.label}</span>
                  <span className="text-[12px] font-bold text-gray-900">0</span>
                </div>
              ))}
            </div>
          </div>
        </div>

        {/* Request Queue by Status */}
        <div className="bg-white border border-gray-100 rounded-xl p-5 shadow-[0_2px_8px_rgba(0,0,0,0.02)] flex flex-col h-[300px]">
          <h3 className="text-[14px] font-bold text-gray-900 mb-6">Request Queue by Status</h3>
          <div className="flex-1 flex flex-col justify-between">
            {[
              { label: 'Requested', val: '0', color: 'text-orange-500' },
              { label: 'Sample Collected', val: '0', color: 'text-blue-500' },
              { label: 'Processing', val: '0', color: 'text-purple-500' },
              { label: 'Result Entered', val: '0', color: 'text-teal-500' },
              { label: 'Verified', val: '0', color: 'text-green-500' },
            ].map((item, i) => (
              <div key={i} className="flex justify-between items-center pb-3 border-b border-gray-50 last:border-0 last:pb-0">
                <span className="text-[13px] text-gray-600">{item.label}</span>
                <span className={`text-[13px] font-bold ${item.color}`}>{item.val}</span>
              </div>
            ))}
          </div>
        </div>
      </div>

      {/* Bottom Row: Charts and Lists */}
      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        {/* Daily Trend */}
        <div className="bg-white border border-gray-100 rounded-xl p-5 shadow-[0_2px_8px_rgba(0,0,0,0.02)] flex flex-col h-[300px]">
          <div className="flex justify-between items-center mb-6">
            <h3 className="text-[14px] font-bold text-gray-900">Daily Trend</h3>
            <button className="flex items-center gap-1.5 px-2.5 py-1 bg-white border border-gray-200 rounded text-[11px] font-semibold text-gray-600">
              Requests
              <ChevronDown className="w-3 h-3" />
            </button>
          </div>
          <div className="flex-1 relative flex">
            {/* Y Axis */}
            <div className="flex flex-col justify-between h-full text-[10px] text-gray-400 pb-6 pr-4">
              <span>10</span><span>8</span><span>6</span><span>4</span><span>2</span><span>0</span>
            </div>
            {/* Chart Area */}
            <div className="flex-1 border-l border-b border-gray-100 relative flex flex-col items-center justify-center pb-6">
              <div className="flex flex-col items-center text-center absolute inset-0 pt-16">
                <BarChart2 className="w-8 h-8 text-blue-100 mb-2" />
                <h4 className="text-[13px] font-bold text-gray-900 mb-1">No trend data available</h4>
                <p className="text-[11px] text-gray-500">Data will appear once requests are received.</p>
              </div>
              {/* X Axis */}
              <div className="absolute bottom-[-24px] left-0 right-0 flex justify-between text-[10px] text-gray-400 pt-2">
                <span>May 15</span><span>May 16</span><span>May 17</span><span>May 18</span><span>May 19</span><span>May 20</span><span>May 21</span>
              </div>
            </div>
          </div>
        </div>

        {/* Turnaround Time */}
        <div className="bg-white border border-gray-100 rounded-xl p-5 shadow-[0_2px_8px_rgba(0,0,0,0.02)] flex flex-col h-[300px]">
          <div className="flex justify-between items-center mb-6">
            <h3 className="text-[14px] font-bold text-gray-900">Test Turnaround Time (TAT)</h3>
            <button className="text-[12px] font-semibold text-blue-600 hover:underline">
              View Report
            </button>
          </div>
          <div className="flex-1 relative flex">
             {/* Chart Area (no Y axis on this one according to design, just X axis and empty state) */}
             <div className="flex-1 border-b border-gray-100 relative flex flex-col items-center justify-center pb-6">
              <div className="flex flex-col items-center text-center absolute inset-0 pt-16">
                <BarChart2 className="w-8 h-8 text-blue-100 mb-2" />
                <h4 className="text-[13px] font-bold text-gray-900 mb-1">No TAT data available</h4>
                <p className="text-[11px] text-gray-500">TAT performance data will appear here.</p>
              </div>
              {/* X Axis */}
              <div className="absolute bottom-[-24px] left-0 right-0 flex justify-between text-[10px] text-gray-400 pt-2">
                <span>May 15</span><span>May 16</span><span>May 17</span><span>May 18</span><span>May 19</span><span>May 20</span><span>May 21</span>
              </div>
            </div>
          </div>
        </div>

        {/* Right Stack: Alerts & Recent */}
        <div className="flex flex-col gap-6 h-[300px]">
          {/* Alerts */}
          <div className="bg-white border border-gray-100 rounded-xl p-4 shadow-[0_2px_8px_rgba(0,0,0,0.02)] flex-1 flex flex-col">
            <div className="flex justify-between items-center mb-4">
              <h3 className="text-[14px] font-bold text-gray-900">Alerts & Notifications</h3>
              <button className="text-[12px] font-semibold text-blue-600 hover:underline">View All</button>
            </div>
            <div className="flex-1 flex flex-col items-center justify-center text-center">
              <div className="w-8 h-8 bg-blue-50 rounded-full flex items-center justify-center mb-2">
                <Bell className="w-4 h-4 text-blue-500" />
              </div>
              <h4 className="text-[12px] font-bold text-gray-900">No new alerts</h4>
              <p className="text-[11px] text-gray-500">You're all caught up!</p>
            </div>
          </div>

          {/* Recent */}
          <div className="bg-white border border-gray-100 rounded-xl p-4 shadow-[0_2px_8px_rgba(0,0,0,0.02)] flex-1 flex flex-col">
            <div className="flex justify-between items-center mb-4">
              <h3 className="text-[14px] font-bold text-gray-900">Recent Lab Requests</h3>
              <button className="flex items-center gap-1.5 px-2.5 py-1 bg-white border border-gray-200 rounded text-[11px] font-semibold text-gray-600">
                All Requests
                <ChevronDown className="w-3 h-3" />
              </button>
            </div>
            <div className="flex-1 flex flex-col items-center justify-center text-center">
              <FileText className="w-8 h-8 text-blue-100 mb-2" strokeWidth={1.5} />
              <h4 className="text-[12px] font-bold text-gray-900">No requests found</h4>
              <p className="text-[11px] text-gray-500">New requests will appear here.</p>
            </div>
          </div>
        </div>

      </div>

    </div>
  );
};

export default LabDashboard;
