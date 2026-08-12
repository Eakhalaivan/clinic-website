import React, { useState } from 'react';
import { AlertTriangle, MapPin, Search } from 'lucide-react';

const DispatcherConsole = () => {
  const [requests, setRequests] = useState([
    { id: 'REQ-1001', priority: 'CRITICAL', status: 'REQUESTED', caller: 'John Doe', type: 'CARDIAC', eta: '--' }
  ]);

  return (
    <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
      <div className="md:col-span-1">
        <div className="bg-white rounded-xl shadow-sm border border-slate-200 h-full overflow-hidden">
          <div className="p-4 border-b border-slate-100">
            <h5 className="font-bold text-lg mb-0 text-slate-800">Emergency Triage Queue</h5>
          </div>
          <div className="p-4">
            <div className="flex gap-2 mb-4">
              <input type="text" placeholder="Search incidents..." className="flex-1 px-3 py-2 border border-slate-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500" />
              <button className="bg-blue-600 text-white px-3 py-2 rounded-lg hover:bg-blue-700 transition-colors"><Search size={18} /></button>
            </div>
            {requests.map(req => (
              <div key={req.id} className="mb-3 border-l-4 border-red-500 bg-red-50 p-4 rounded-r-lg shadow-sm">
                <div className="flex justify-between items-start mb-2">
                  <h6 className="font-bold text-slate-900">{req.id}</h6>
                  <span className="bg-red-500 text-white text-xs px-2 py-1 rounded font-semibold">{req.priority}</span>
                </div>
                <p className="mb-1 text-slate-600 text-sm flex items-center"><AlertTriangle size={14} className="mr-1 text-red-500" /> {req.type}</p>
                <p className="mb-3 text-slate-600 text-sm">Caller: {req.caller}</p>
                <button className="w-full bg-red-600 text-white py-2 rounded-lg text-sm font-semibold hover:bg-red-700 transition-colors">Assign Nearest Unit</button>
              </div>
            ))}
          </div>
        </div>
      </div>
      <div className="md:col-span-2">
        <div className="bg-white rounded-xl shadow-sm border border-slate-200 h-full overflow-hidden flex flex-col">
          <div className="p-4 border-b border-slate-100">
            <h5 className="font-bold text-lg mb-0 text-slate-800">Live Fleet Map (SSE Tracking)</h5>
          </div>
          <div className="flex-1 bg-slate-100 flex items-center justify-center min-h-[500px]">
            <div className="text-center text-slate-400">
              <MapPin size={48} className="mx-auto mb-2 text-blue-500 opacity-50" />
              <p>Live GPS telemetry connected.</p>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default DispatcherConsole;
