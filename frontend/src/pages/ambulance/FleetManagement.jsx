import React from 'react';
import { Plus } from 'lucide-react';

const FleetManagement = () => {
  return (
    <div className="bg-white rounded-xl shadow-sm border border-slate-200 overflow-hidden">
      <div className="p-5 border-b border-slate-100 flex justify-between items-center bg-slate-50">
        <h5 className="font-bold text-lg mb-0 text-slate-800">Fleet & Personnel Directory</h5>
        <button className="bg-blue-600 text-white px-4 py-2 rounded-lg text-sm font-semibold hover:bg-blue-700 transition-colors flex items-center"><Plus size={16} className="mr-2"/> Register Vehicle</button>
      </div>
      <div className="p-0 overflow-x-auto">
        <table className="w-full text-left border-collapse">
          <thead>
            <tr className="bg-slate-50 border-b border-slate-200 text-slate-600 text-sm">
              <th className="p-4 font-semibold">Vehicle No</th>
              <th className="p-4 font-semibold">Type</th>
              <th className="p-4 font-semibold">Status</th>
              <th className="p-4 font-semibold">Maintenance</th>
              <th className="p-4 font-semibold">Base Location</th>
              <th className="p-4 font-semibold">Actions</th>
            </tr>
          </thead>
          <tbody className="text-sm">
            <tr className="border-b border-slate-100 hover:bg-slate-50">
              <td className="p-4 font-medium text-slate-800">AMB-001</td>
              <td className="p-4">ALS</td>
              <td className="p-4"><span className="bg-green-100 text-green-800 px-2 py-1 rounded text-xs font-semibold">AVAILABLE</span></td>
              <td className="p-4"><span className="bg-blue-100 text-blue-800 px-2 py-1 rounded text-xs font-semibold">OK</span></td>
              <td className="p-4 text-slate-600">Main Hospital</td>
              <td className="p-4"><button className="text-blue-600 border border-blue-600 px-3 py-1 rounded hover:bg-blue-50 transition-colors">Manage</button></td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  );
};

export default FleetManagement;
