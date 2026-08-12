import React from 'react';
import { Navigation, FileText, CheckCircle } from 'lucide-react';

const CrewView = () => {
  return (
    <div className="flex justify-center items-start pt-8 min-h-[600px]">
      <div className="bg-white rounded-xl shadow-lg border border-slate-200 overflow-hidden w-full max-w-md">
        <div className="bg-red-600 p-4 text-center text-white">
          <h4 className="font-bold text-xl mb-1">ACTIVE DISPATCH</h4>
          <p className="text-sm text-red-100 mb-0">REQ-1001 • CRITICAL</p>
        </div>
        <div className="p-6">
          <div className="text-center mb-6">
            <h5 className="font-bold text-slate-900 text-lg mb-1">Cardiac Arrest (Suspected)</h5>
            <p className="text-slate-500 mb-3">123 Emergency Lane, Suite 400</p>
            <span className="bg-amber-100 text-amber-800 px-4 py-2 rounded-full font-bold text-sm">ETA: 4 mins</span>
          </div>
          
          <div className="flex flex-col gap-3 mb-6">
            <button className="bg-blue-600 text-white w-full py-4 rounded-xl font-bold text-lg flex justify-center items-center hover:bg-blue-700 transition-colors shadow-sm">
              <Navigation size={22} className="mr-2" /> Start Navigation
            </button>
            <button className="bg-amber-500 text-white w-full py-4 rounded-xl font-bold text-lg flex justify-center items-center hover:bg-amber-600 transition-colors shadow-sm">
              <FileText size={22} className="mr-2" /> Pre-Hospital Care Record
            </button>
          </div>

          <div className="bg-slate-50 p-4 rounded-xl border border-slate-200">
            <h6 className="font-bold text-slate-700 mb-3 text-sm uppercase">Next Action Required:</h6>
            <button className="bg-emerald-500 text-white w-full py-4 rounded-xl font-bold text-lg flex justify-center items-center hover:bg-emerald-600 transition-colors shadow-sm">
              <CheckCircle size={22} className="mr-2" /> MARK AT SCENE
            </button>
          </div>
        </div>
      </div>
    </div>
  );
};

export default CrewView;
