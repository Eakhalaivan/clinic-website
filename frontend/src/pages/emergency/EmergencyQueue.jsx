import React, { useState } from 'react';
import { useQuery } from '@tanstack/react-query';
import { axiosPrivate } from '../../api/axios';
import { AlertCircle, UserPlus, Clock, ArrowRight, Ambulance, Syringe } from 'lucide-react';
import EmptyState from '../../components/common/EmptyState';
import { formatDistanceToNow } from 'date-fns';

const TRIAGE_COLORS = {
  CRITICAL: 'bg-red-100 text-red-700 border-red-300',
  URGENT: 'bg-orange-100 text-orange-700 border-orange-300',
  SEMI_URGENT: 'bg-yellow-100 text-yellow-700 border-yellow-300',
  NON_URGENT: 'bg-green-100 text-green-700 border-green-300',
  PENDING: 'bg-slate-100 text-slate-700 border-slate-300',
};

const EmergencyQueue = () => {
  const [filter, setFilter] = useState('ALL');

  const { data: encounters, isLoading } = useQuery({
    queryKey: ['emergency-encounters', filter],
    queryFn: async () => {
      let url = '/emergency/encounters';
      if (filter !== 'ALL') {
        url += `?status=${filter}`;
      }
      const res = await axiosPrivate.get(url);
      
      // We need to fetch triage assessments for these if possible, 
      // but assuming the backend can include it or we just show status.
      return res.data;
    },
    refetchInterval: 30000 // auto refresh ED queue every 30s
  });

  if (isLoading) {
    return <div className="p-10 flex justify-center text-slate-400">Loading emergency queue...</div>;
  }

  return (
    <div className="p-6 max-w-7xl mx-auto space-y-6">
      
      {/* Header */}
      <div className="flex flex-col md:flex-row justify-between items-start md:items-center gap-4">
        <div>
          <h1 className="text-2xl font-bold text-slate-800 flex items-center gap-2">
            <AlertCircle className="text-red-600" />
            Emergency Department Queue
          </h1>
          <p className="text-slate-500 mt-1">Live triage and patient tracking for the ED.</p>
        </div>
        
        <div className="flex items-center gap-3">
          <button className="bg-red-600 text-white px-4 py-2 rounded-lg font-medium hover:bg-red-700 transition-colors flex items-center gap-2 shadow-sm">
            <UserPlus size={18} /> Register Patient
          </button>
        </div>
      </div>

      {/* Stats/Filters */}
      <div className="grid grid-cols-4 gap-4">
        {[
          { label: 'All Patients', value: 'ALL', count: encounters?.length || 0, color: 'bg-blue-50 text-blue-700' },
          { label: 'Waiting Triage', value: 'REGISTERED', count: encounters?.filter(e => e.status === 'REGISTERED').length || 0, color: 'bg-slate-50 text-slate-700' },
          { label: 'In Triage', value: 'IN_TRIAGE', count: encounters?.filter(e => e.status === 'IN_TRIAGE').length || 0, color: 'bg-orange-50 text-orange-700' },
          { label: 'In Treatment', value: 'IN_TREATMENT', count: encounters?.filter(e => e.status === 'IN_TREATMENT').length || 0, color: 'bg-green-50 text-green-700' },
        ].map(stat => (
          <div 
            key={stat.value}
            onClick={() => setFilter(stat.value)}
            className={`p-4 rounded-xl border cursor-pointer transition-all ${
              filter === stat.value ? 'ring-2 ring-blue-500 border-transparent shadow-sm' : 'border-slate-200 hover:border-slate-300'
            } ${filter === stat.value ? stat.color : 'bg-white'}`}
          >
            <p className="text-sm font-medium opacity-80">{stat.label}</p>
            <p className="text-2xl font-bold mt-1">{stat.count}</p>
          </div>
        ))}
      </div>

      {/* Queue List */}
      <div className="bg-white border border-slate-200 rounded-xl shadow-sm overflow-hidden">
        <div className="overflow-x-auto">
          <table className="w-full text-left border-collapse">
            <thead>
              <tr className="bg-slate-50 border-b border-slate-200 text-sm font-semibold text-slate-600">
                <th className="px-6 py-4">Wait Time</th>
                <th className="px-6 py-4">Patient</th>
                <th className="px-6 py-4">Arrival Mode</th>
                <th className="px-6 py-4">Acuity / Status</th>
                <th className="px-6 py-4">Provider</th>
                <th className="px-6 py-4 text-right">Action</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-slate-100">
              {(!encounters || encounters.length === 0) ? (
                <tr>
                  <td colSpan="6">
                    <EmptyState 
                      icon={AlertCircle}
                      title="Queue Empty"
                      message="No patients are currently in the emergency queue."
                    />
                  </td>
                </tr>
              ) : (
                encounters.map(encounter => (
                  <tr key={encounter.id} className="hover:bg-slate-50 transition-colors">
                    <td className="px-6 py-4 whitespace-nowrap text-sm">
                      <div className="flex items-center gap-1.5 text-slate-600 font-medium">
                        <Clock size={14} />
                        {formatDistanceToNow(new Date(encounter.arrivedAt))}
                      </div>
                      <div className="text-xs text-slate-400 mt-1">
                        {new Date(encounter.arrivedAt).toLocaleTimeString()}
                      </div>
                    </td>
                    
                    <td className="px-6 py-4 whitespace-nowrap">
                      {encounter.patient ? (
                        <>
                          <div className="font-bold text-slate-800">
                            {encounter.patient.firstName} {encounter.patient.lastName}
                          </div>
                          <div className="text-xs text-slate-500 mt-0.5">
                            {encounter.patient.gender} • {new Date().getFullYear() - new Date(encounter.patient.dateOfBirth).getFullYear()}y
                          </div>
                        </>
                      ) : (
                        <div className="font-bold text-slate-800 italic">Unidentified Patient</div>
                      )}
                    </td>
                    
                    <td className="px-6 py-4 whitespace-nowrap">
                      <div className="flex items-center gap-1.5 text-sm text-slate-600">
                        {encounter.arrivalMode === 'AMBULANCE' ? <Ambulance size={16} className="text-red-500" /> : <UserPlus size={16} />}
                        {encounter.arrivalMode}
                      </div>
                    </td>
                    
                    <td className="px-6 py-4 whitespace-nowrap">
                      {/* Placeholder triage logic since we don't have nested triage assessment in encounter yet */}
                      <span className={`px-2.5 py-1 rounded-full text-xs font-bold border ${
                        encounter.status === 'REGISTERED' ? TRIAGE_COLORS.PENDING : TRIAGE_COLORS.URGENT
                      }`}>
                        {encounter.status === 'REGISTERED' ? 'PENDING TRIAGE' : encounter.status}
                      </span>
                    </td>
                    
                    <td className="px-6 py-4 whitespace-nowrap text-sm text-slate-600">
                      {encounter.assignedDoctor ? `Dr. ${encounter.assignedDoctor.userId}` : 'Unassigned'}
                    </td>
                    
                    <td className="px-6 py-4 whitespace-nowrap text-right">
                      {encounter.status === 'REGISTERED' && (
                        <button className="text-sm font-medium text-orange-600 hover:text-orange-800 flex items-center justify-end gap-1 ml-auto">
                          Triage <ArrowRight size={14} />
                        </button>
                      )}
                      {encounter.status === 'IN_TRIAGE' && (
                        <button className="text-sm font-medium text-blue-600 hover:text-blue-800 flex items-center justify-end gap-1 ml-auto">
                          Assign MD <ArrowRight size={14} />
                        </button>
                      )}
                      {encounter.status === 'IN_TREATMENT' && (
                        <div className="flex gap-3 justify-end">
                          <button className="text-sm font-medium text-slate-600 hover:text-slate-800 flex items-center gap-1" title="Orders">
                            <Syringe size={16} />
                          </button>
                          <button className="text-sm font-medium text-green-600 hover:text-green-800 flex items-center gap-1">
                            Disposition <ArrowRight size={14} />
                          </button>
                        </div>
                      )}
                    </td>
                  </tr>
                ))
              )}
            </tbody>
          </table>
        </div>
      </div>

    </div>
  );
};

export default EmergencyQueue;
