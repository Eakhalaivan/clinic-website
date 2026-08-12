import React from 'react';

const TripHistory = () => {
  return (
    <div className="bg-white rounded-xl shadow-sm border border-slate-200 overflow-hidden">
      <div className="p-5 border-b border-slate-100 bg-slate-50">
        <h5 className="font-bold text-lg mb-0 text-slate-800">Completed Trips & Billing</h5>
      </div>
      <div className="p-0 overflow-x-auto">
        <table className="w-full text-left border-collapse">
          <thead>
            <tr className="bg-slate-50 border-b border-slate-200 text-slate-600 text-sm">
              <th className="p-4 font-semibold">Trip ID</th>
              <th className="p-4 font-semibold">Date</th>
              <th className="p-4 font-semibold">Ambulance</th>
              <th className="p-4 font-semibold">Outcome</th>
              <th className="p-4 font-semibold">Distance (km)</th>
              <th className="p-4 font-semibold">Billed Amount</th>
              <th className="p-4 font-semibold">Billing Status</th>
            </tr>
          </thead>
          <tbody className="text-sm">
            <tr className="border-b border-slate-100 hover:bg-slate-50">
              <td className="p-4 font-medium text-slate-800">TRP-9982</td>
              <td className="p-4 text-slate-600">2026-08-12</td>
              <td className="p-4 font-medium text-blue-600">AMB-001</td>
              <td className="p-4 text-slate-600">Handed Over</td>
              <td className="p-4 text-slate-600">14.2</td>
              <td className="p-4 text-slate-800 font-medium">$250.00</td>
              <td className="p-4"><span className="bg-blue-100 text-blue-800 px-2 py-1 rounded text-xs font-semibold">INVOICED</span></td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  );
};

export default TripHistory;
