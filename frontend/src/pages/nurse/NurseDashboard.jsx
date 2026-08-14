import React, { useState, useEffect } from 'react';
import { useQuery, useQueryClient } from '@tanstack/react-query';
import { axiosPrivate } from '../../api/axios';
import useAuthStore from '../../store/authStore';
import { HeartPulse, Calendar as CalendarIcon, Activity, MessageCircle } from 'lucide-react';
import {
  NurseAssignedPatientsWidget,
  VitalSignsFormWidget,
  NurseRecentActivityWidget
} from '../../components/dashboard/widgets/NurseWidgets';

const NurseDashboard = () => {
  const { user, token } = useAuthStore();
  const [activeTab, setActiveTab] = useState('Dashboard');
  const [selectedPatientId, setSelectedPatientId] = useState(null);
  const queryClient = useQueryClient();

  useEffect(() => {
      if (!token) return;
      const baseUrl = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api';
      const evtSource = new EventSource(`${baseUrl.replace('/api', '')}/api/sse/appointments?token=${token}`);
      
      const invalidate = () => {
          queryClient.invalidateQueries(['nurseAssignments']);
          queryClient.invalidateQueries(['nurse-assigned-patients']);
          queryClient.invalidateQueries(['nursingRecentActivity']);
      };
      
      evtSource.addEventListener('appointment-status-changed', invalidate);
      evtSource.addEventListener('queue-token-called', invalidate);
      
      return () => evtSource.close();
  }, [queryClient, token]);

  const { data: assignments, isLoading: isAssignmentsLoading } = useQuery({
    queryKey: ['nurseAssignments'],
    queryFn: async () => {
      const res = await axiosPrivate.get('/nursing/assignments/op');
      return res.data;
    }
  });

  const { data: recentActivity = [], isLoading: isActivityLoading } = useQuery({
    queryKey: ['nursingRecentActivity'],
    queryFn: async () => {
      const res = await axiosPrivate.get('/nursing/recent-activity');
      return res.data;
    },
    refetchInterval: 15000 // 15 seconds polling
  });

  const assignmentsList = assignments || [];
  const selectedPatient = assignmentsList.find(a => a.patientId === selectedPatientId);

  // Tabs list
  const tabs = ['Dashboard', 'OP Queue', 'IP Wards', 'Shift Log', 'Inventory'];

  return (
    <div className="h-full bg-white flex flex-col font-sans overflow-hidden">
      
      {/* Top Action Cards */}
      <div className="flex items-center gap-6 p-6 shrink-0 bg-[#fafafa]">
        <button className="flex items-center gap-5 bg-white border border-slate-100 rounded-2xl px-5 py-4 w-[280px] hover:shadow-md transition-shadow shadow-[0_2px_12px_rgba(0,0,0,0.02)]">
          <div className="w-[50px] h-[50px] rounded-full bg-emerald-100 flex items-center justify-center shrink-0">
            <HeartPulse className="w-[22px] h-[22px] text-emerald-500" strokeWidth={2.5} />
          </div>
          <div className="text-left flex-1">
            <h3 className="font-bold text-slate-900 text-[15px] m-0 leading-tight">Register Walk-in</h3>
            <p className="text-slate-500 text-[13px] m-0 mt-1 leading-tight">Add new walk-in patient</p>
          </div>
        </button>

        <button className="flex items-center gap-5 bg-white border border-slate-100 rounded-2xl px-5 py-4 w-[280px] hover:shadow-md transition-shadow shadow-[0_2px_12px_rgba(0,0,0,0.02)]">
          <div className="w-[50px] h-[50px] rounded-full bg-indigo-100 flex items-center justify-center shrink-0">
            <CalendarIcon className="w-[22px] h-[22px] text-indigo-500" strokeWidth={2.5} />
          </div>
          <div className="text-left flex-1">
            <h3 className="font-bold text-slate-900 text-[15px] m-0 leading-tight">View Schedule</h3>
            <p className="text-slate-500 text-[13px] m-0 mt-1 leading-tight">Check your shifts</p>
          </div>
        </button>

        <button className="flex items-center gap-5 bg-white border border-slate-100 rounded-2xl px-5 py-4 w-[280px] hover:shadow-md transition-shadow shadow-[0_2px_12px_rgba(0,0,0,0.02)]">
          <div className="w-[50px] h-[50px] rounded-full bg-orange-50 flex items-center justify-center shrink-0">
            <Activity className="w-[22px] h-[22px] text-orange-500" strokeWidth={2.5} />
          </div>
          <div className="text-left flex-1">
            <h3 className="font-bold text-slate-900 text-[15px] m-0 leading-tight">Request Supplies</h3>
            <p className="text-slate-500 text-[13px] m-0 mt-1 leading-tight">Raise a supply request</p>
          </div>
        </button>
      </div>

      {/* Tabs */}
      <div className="px-6 flex items-center gap-8 border-b border-slate-100 bg-[#fafafa] shrink-0 pt-2">
        {tabs.map((tab) => {
          const isActive = activeTab === tab;
          return (
            <button
              key={tab}
              onClick={() => setActiveTab(tab)}
              className={`pb-3 font-semibold text-[14px] transition-colors relative ${
                isActive ? 'text-[#3627d3]' : 'text-slate-500 hover:text-slate-800'
              }`}
            >
              {tab}
              {isActive && (
                <div className="absolute bottom-[-1px] left-0 right-0 h-[2px] bg-[#3627d3]" />
              )}
            </button>
          );
        })}
      </div>

      {/* Main Content Area - 3 Column Layout */}
      <div className="flex-1 overflow-hidden p-6 relative bg-[#fafafa]">
        {activeTab === 'Dashboard' ? (
          <div className="grid grid-cols-12 gap-6 h-full max-w-full">
            {/* Left Column (Approx 3.5/12) */}
            <div className="col-span-12 lg:col-span-3 xl:col-span-3 h-full">
              <NurseAssignedPatientsWidget
                assignmentsList={assignmentsList}
                isAssignmentsLoading={isAssignmentsLoading}
                selectedPatientId={selectedPatientId}
                setSelectedPatientId={setSelectedPatientId}
              />
            </div>

            {/* Center Column (Approx 5/12) */}
            <div className="col-span-12 lg:col-span-6 xl:col-span-6 h-full">
              <VitalSignsFormWidget
                selectedPatientId={selectedPatientId}
                selectedPatient={selectedPatient}
              />
            </div>

            {/* Right Column (Approx 3.5/12) */}
            <div className="col-span-12 lg:col-span-3 xl:col-span-3 h-full">
              <NurseRecentActivityWidget
                recentActivity={recentActivity}
                isActivityLoading={isActivityLoading}
              />
            </div>
          </div>
        ) : (
          <div className="h-full flex items-center justify-center text-slate-400">
            {activeTab} Content Not Implemented
          </div>
        )}

        {/* Floating Chat Button */}
        <button className="absolute bottom-6 right-6 w-[60px] h-[60px] rounded-full bg-[#3627d3] text-white flex items-center justify-center shadow-lg hover:bg-[#2c1eb8] transition-colors z-50">
          <MessageCircle className="w-[28px] h-[28px]" fill="currentColor" stroke="currentColor" strokeWidth={1} />
        </button>
      </div>
    </div>
  );
};

export default NurseDashboard;
