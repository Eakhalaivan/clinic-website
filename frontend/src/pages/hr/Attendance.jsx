import React, { useState } from 'react';
import useAuthStore from '../../store/authStore';

const Attendance = () => {
  const { user } = useAuthStore();
  const [clockedIn, setClockedIn] = useState(false);
  const [logs, setLogs] = useState([
    { id: 1, date: new Date().toLocaleDateString(), in: '09:00 AM', out: '05:00 PM', status: 'Present' }
  ]);

  const handleClockInOut = () => {
    // Simulated API call for clocking in/out
    if (clockedIn) {
      alert("Clocked out successfully!");
      setClockedIn(false);
    } else {
      alert("Clocked in successfully!");
      setClockedIn(true);
    }
  };

  return (
    <div className="p-8 max-w-4xl mx-auto">
      <div className="flex justify-between items-center mb-6">
        <h2 className="text-2xl font-bold text-slate-800">HR Attendance &amp; Time Tracking</h2>
        
        <button 
          onClick={handleClockInOut}
          className={`px-6 py-2 rounded-full font-bold text-white shadow-md transition-colors ${clockedIn ? 'bg-orange-500 hover:bg-orange-600' : 'bg-emerald-500 hover:bg-emerald-600'}`}
        >
          {clockedIn ? 'Clock Out' : 'Clock In'}
        </button>
      </div>
      
      <div className="bg-white rounded-lg shadow-sm border border-slate-200 overflow-hidden">
        <table className="w-full text-left text-sm text-slate-600">
          <thead className="bg-slate-50 text-slate-700 font-medium border-b border-slate-200">
            <tr>
              <th className="px-6 py-3">Date</th>
              <th className="px-6 py-3">Clock In</th>
              <th className="px-6 py-3">Clock Out</th>
              <th className="px-6 py-3">Status</th>
            </tr>
          </thead>
          <tbody className="divide-y divide-slate-100">
            {logs.map(log => (
              <tr key={log.id} className="hover:bg-slate-50/50">
                <td className="px-6 py-4">{log.date}</td>
                <td className="px-6 py-4 text-emerald-600 font-medium">{log.in}</td>
                <td className="px-6 py-4 text-orange-600 font-medium">{log.out || '--'}</td>
                <td className="px-6 py-4">
                  <span className="bg-emerald-100 text-emerald-800 px-2 py-1 rounded-full text-xs font-semibold">
                    {log.status}
                  </span>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
};

export default Attendance;
