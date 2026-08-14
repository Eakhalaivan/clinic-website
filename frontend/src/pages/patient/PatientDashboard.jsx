import React, { useState } from 'react';
import { useQuery } from '@tanstack/react-query';
import { axiosPrivate } from '../../api/axios';
import useAuthStore from '../../store/authStore';
import { 
  Calendar, Pill, Users, FileText, Heart, Shield, ChevronRight, 
  ChevronLeft, Check, Plus, MessageCircle, FlaskConical, Bell
} from 'lucide-react';
import { useNavigate } from 'react-router-dom';
import './PatientDashboard.css';

const PatientDashboard = () => {
  const { user } = useAuthStore();
  const navigate = useNavigate();
  const [medicationTaken, setMedicationTaken] = useState(false);

  // ─── API Queries ───
  useQuery({
    queryKey: ['patientProfile', user?.id],
    queryFn: async () => {
      const res = await axiosPrivate.get(`/patients/profile/${user.id}`);
      return res.data;
    },
    enabled: !!user?.id
  });

  useQuery({
    queryKey: ['patientAppointments', user?.id],
    queryFn: async () => {
      const res = await axiosPrivate.get(`/appointments/patient/${user.id}`);
      return res.data;
    },
    enabled: !!user?.id
  });

  useQuery({
    queryKey: ['patientLabReports'],
    queryFn: async () => {
      const res = await axiosPrivate.get('/lab/patient/lab-reports');
      return res.data;
    },
    enabled: !!user?.id
  });

  return (
    <div className="patient-dashboard-root bg-[#F3F6FF]">
      <main className="patient-dashboard-main pt-6">
        <div className="patient-main-grid">

          {/* LEFT COLUMN */}
          <div className="col-left space-y-5">
            {/* Upcoming Consultation */}
            <section className="bg-white rounded-[20px] shadow-sm border border-slate-100 p-5">
              <div className="flex items-center justify-between mb-5">
                <div className="flex items-center gap-2">
                  <div className="w-8 h-8 rounded-lg bg-indigo-50 flex items-center justify-center">
                    <Calendar className="w-4 h-4 text-indigo-600" />
                  </div>
                  <h2 className="text-[14px] font-bold text-slate-800">Upcoming Consultation</h2>
                </div>
                <button onClick={() => navigate('/patient/appointments')} className="text-indigo-600 text-[12px] font-semibold hover:underline">
                  View All
                </button>
              </div>
              <div className="space-y-4">
                <div className="flex items-center justify-between border-b border-slate-100 pb-3">
                  <span className="font-bold text-slate-800 text-[13px]">11:30 AM</span>
                  <span className="text-[12px] font-bold text-indigo-600">Today</span>
                </div>
                <div>
                  <p className="font-bold text-slate-800 text-[14px]">Dr. Sarah Johnson</p>
                  <p className="text-[12px] text-slate-500 mt-1">General Physician</p>
                </div>
                <button className="w-full mt-4 py-2.5 rounded-xl border border-indigo-100 text-indigo-600 text-[13px] font-bold flex items-center justify-center gap-2 hover:bg-indigo-50 transition-colors">
                  <span>Go to Consultation</span>
                  <ChevronRight className="w-4 h-4" />
                </button>
              </div>
            </section>

            {/* My Next Medication */}
            <section className="bg-white rounded-[20px] shadow-sm border border-slate-100 p-5">
              <div className="flex items-center justify-between mb-5">
                <h2 className="text-[14px] font-bold text-slate-800">My Next Medication</h2>
                <button onClick={() => navigate('/patient/prescriptions')} className="text-indigo-600 text-[12px] font-semibold hover:underline">
                  View Medication Details
                </button>
              </div>
              <div className="flex flex-col items-center">
                <div className="w-16 h-16 bg-slate-50 rounded-2xl flex flex-col items-center justify-center mb-4">
                  <span className="text-[10px] font-bold text-slate-500 uppercase tracking-widest">May</span>
                  <span className="text-2xl font-black text-slate-800 leading-none mt-1">21</span>
                </div>
                <p className="text-[11px] font-bold text-indigo-600 tracking-wide uppercase mb-2">Wednesday, 10:00 AM</p>
                <h3 className="text-[15px] font-bold text-slate-800 text-center">Lisinopril (10mg Tablet)</h3>
                <p className="text-[13px] text-slate-500 mt-1 mb-6">1 tablet after breakfast</p>
                
                <button 
                  onClick={() => setMedicationTaken(!medicationTaken)}
                  className={`w-full py-3 rounded-xl text-[14px] font-bold flex items-center justify-center gap-2 transition-colors ${
                    medicationTaken 
                      ? 'bg-green-600 text-white hover:bg-green-700' 
                      : 'bg-indigo-600 text-white hover:bg-indigo-700'
                  }`}
                >
                  <Check className="w-5 h-5" />
                  <span>{medicationTaken ? 'Dose Taken' : 'Mark as Taken'}</span>
                </button>
              </div>
            </section>
          </div>

          {/* CENTER COLUMN (Health Journey) */}
          <div className="col-center bg-white rounded-[24px] shadow-sm border border-slate-100 overflow-hidden flex flex-col h-[800px]">
            {/* Header */}
            <div className="px-6 py-5 border-b border-slate-100 flex items-center justify-between">
              <div className="flex items-center gap-4">
                <button className="w-8 h-8 rounded-full bg-slate-50 flex items-center justify-center text-slate-600 hover:bg-slate-100 transition-colors">
                  <ChevronLeft className="w-5 h-5" />
                </button>
                <h1 className="text-[18px] font-bold text-slate-800">Health Journey 2026</h1>
                <span className="px-3 py-1 bg-indigo-50 text-indigo-600 text-[12px] font-bold rounded-full">Today</span>
              </div>
              <div className="flex bg-slate-50 p-1 rounded-full">
                <button className="px-4 py-1.5 bg-white shadow-sm rounded-full text-[13px] font-bold text-indigo-600">Week</button>
                <button className="px-4 py-1.5 text-slate-500 text-[13px] font-bold hover:text-slate-800 transition-colors">Month</button>
                <button className="px-4 py-1.5 text-slate-500 text-[13px] font-bold hover:text-slate-800 transition-colors">Year</button>
              </div>
            </div>

            {/* Timeline Area */}
            <div className="flex-1 relative p-8 flex flex-col">
              {/* Timeline Indicator Line */}
              <div className="flex items-center mb-16">
                <div className="bg-red-500 text-white text-[12px] font-bold px-3 py-1 rounded-full relative z-10">
                  Today
                </div>
                <div className="flex-1 h-[2px] bg-red-500 -ml-1 relative">
                  <div className="absolute right-0 top-1/2 -translate-y-1/2 w-3 h-3 bg-red-500 rounded-full border-2 border-white shadow-sm"></div>
                </div>
              </div>

              {/* Empty State */}
              <div className="flex-1 flex flex-col items-center justify-center -mt-16">
                <div className="relative w-28 h-28 mb-6">
                  <div className="absolute inset-0 bg-indigo-50 rounded-[24px] flex items-center justify-center transform rotate-6">
                  </div>
                  <div className="absolute inset-0 bg-white rounded-[24px] border-2 border-indigo-50 flex items-center justify-center shadow-lg transform -rotate-3 overflow-hidden">
                    <div className="w-full h-full flex flex-col">
                      <div className="h-8 bg-indigo-100/50 w-full flex items-center justify-center gap-2">
                        <div className="w-2 h-4 bg-indigo-200 rounded-full"></div>
                        <div className="w-2 h-4 bg-indigo-200 rounded-full"></div>
                      </div>
                      <div className="flex-1 bg-white grid grid-cols-4 grid-rows-3 gap-1 p-2">
                        {[...Array(12)].map((_, i) => (
                           <div key={i} className="bg-indigo-50/50 rounded-sm"></div>
                        ))}
                      </div>
                    </div>
                  </div>
                  <div className="absolute -bottom-2 -right-2 w-10 h-10 bg-indigo-600 rounded-full flex items-center justify-center border-[3px] border-white shadow-sm">
                    <Check className="w-6 h-6 text-white stroke-[3]" />
                  </div>
                </div>
                <h2 className="text-[20px] font-bold text-slate-800 mb-2">No activities for today</h2>
                <p className="text-[15px] text-slate-500 text-center">
                  You're all caught up!<br/>
                  Check back later for updates on your health journey.
                </p>
              </div>
            </div>
          </div>

          {/* RIGHT COLUMN */}
          <div className="col-right space-y-5">
            {/* Recent Prescriptions */}
            <section className="bg-white rounded-[20px] shadow-sm border border-slate-100 p-5">
              <div className="flex items-center justify-between mb-5">
                <h2 className="text-[14px] font-bold text-slate-800">Recent Prescriptions</h2>
                <button onClick={() => navigate('/patient/prescriptions')} className="text-indigo-600 text-[12px] font-semibold hover:underline">
                  View All
                </button>
              </div>
              <div className="space-y-4">
                <div className="flex items-center justify-between pb-4 border-b border-slate-50">
                  <div className="flex items-center gap-3">
                    <div className="w-10 h-10 rounded-full bg-indigo-50 flex items-center justify-center shrink-0 border border-indigo-100/50">
                      <Pill className="w-5 h-5 text-indigo-600" />
                    </div>
                    <div>
                      <h3 className="font-bold text-slate-800 text-[13px]">Lisinopril (10mg)</h3>
                      <p className="text-slate-500 text-[12px] mt-0.5">1 tablet daily</p>
                    </div>
                  </div>
                  <span className="text-[12px] text-slate-500 font-medium">May 12, 2026</span>
                </div>
                <div className="flex items-center justify-between">
                  <div className="flex items-center gap-3">
                    <div className="w-10 h-10 rounded-full bg-indigo-50 flex items-center justify-center shrink-0 border border-indigo-100/50">
                      <Pill className="w-5 h-5 text-indigo-600" />
                    </div>
                    <div>
                      <h3 className="font-bold text-slate-800 text-[13px]">Atorvastatin (20mg)</h3>
                      <p className="text-slate-500 text-[12px] mt-0.5">1 tablet daily</p>
                    </div>
                  </div>
                  <span className="text-[12px] text-slate-500 font-medium">May 02, 2026</span>
                </div>
              </div>
            </section>

            {/* Quick Links Grid */}
            <div className="grid grid-cols-3 gap-3">
              <button onClick={() => navigate('/patient/appointments')} className="bg-white border border-slate-100 rounded-[16px] p-4 flex flex-col items-center justify-center gap-2 hover:shadow-md transition-shadow">
                <Calendar className="w-6 h-6 text-indigo-600" />
                <span className="text-[11px] font-bold text-slate-700 text-center leading-tight">Book<br/>Appointment</span>
              </button>
              <button onClick={() => navigate('/patient/doctors')} className="bg-white border border-slate-100 rounded-[16px] p-4 flex flex-col items-center justify-center gap-2 hover:shadow-md transition-shadow">
                <Users className="w-6 h-6 text-indigo-600" />
                <span className="text-[11px] font-bold text-slate-700 text-center leading-tight">Find a<br/>Doctor</span>
              </button>
              <button onClick={() => navigate('/patient/documents')} className="bg-white border border-slate-100 rounded-[16px] p-4 flex flex-col items-center justify-center gap-2 hover:shadow-md transition-shadow">
                <FileText className="w-6 h-6 text-indigo-600" />
                <span className="text-[11px] font-bold text-slate-700 text-center leading-tight">Health<br/>Records</span>
              </button>
              <button onClick={() => navigate('/patient/lab-reports')} className="bg-white border border-slate-100 rounded-[16px] p-4 flex flex-col items-center justify-center gap-2 hover:shadow-md transition-shadow">
                <FlaskConical className="w-6 h-6 text-indigo-600" />
                <span className="text-[11px] font-bold text-slate-700 text-center leading-tight">Lab<br/>Reports</span>
              </button>
              <button onClick={() => navigate('/patient/vitals')} className="bg-white border border-slate-100 rounded-[16px] p-4 flex flex-col items-center justify-center gap-2 hover:shadow-md transition-shadow">
                <Heart className="w-6 h-6 text-indigo-600" />
                <span className="text-[11px] font-bold text-slate-700 text-center leading-tight">Vitals<br/>Log</span>
              </button>
              <button onClick={() => navigate('/patient/reminders')} className="bg-white border border-slate-100 rounded-[16px] p-4 flex flex-col items-center justify-center gap-2 hover:shadow-md transition-shadow">
                <Bell className="w-6 h-6 text-indigo-600" />
                <span className="text-[11px] font-bold text-slate-700 text-center leading-tight">Reminders<br/>&nbsp;</span>
              </button>
            </div>

            {/* Promotional Banner */}
            <section className="bg-white border border-slate-100 rounded-[20px] p-5 relative overflow-hidden flex items-center justify-between">
              <div className="relative z-10 w-2/3 space-y-2">
                <div className="flex items-center gap-2">
                  <Shield className="w-5 h-5 text-emerald-500" />
                  <h3 className="text-[14px] font-bold text-slate-800 leading-tight">Stay on top of your health</h3>
                </div>
                <p className="text-[12px] text-slate-500 leading-relaxed">
                  Keep your records updated and never miss your medications.
                </p>
              </div>
              <div className="w-1/3 flex items-center justify-center relative">
                <div className="w-16 h-20 bg-indigo-50 rounded-lg flex flex-col items-center justify-center border border-indigo-100 relative">
                   <div className="w-8 h-8 rounded-full bg-white border border-indigo-100 flex items-center justify-center absolute -top-3">
                     <Plus className="w-4 h-4 text-indigo-400" />
                   </div>
                   <div className="w-10 h-2 bg-indigo-200/50 rounded-full mt-4"></div>
                   <div className="w-8 h-2 bg-indigo-200/50 rounded-full mt-2"></div>
                   <div className="w-10 h-2 bg-indigo-200/50 rounded-full mt-2"></div>
                </div>
                <div className="absolute -bottom-2 -right-4 w-10 h-10 bg-emerald-100 rounded-full flex items-center justify-center opacity-70 transform rotate-12">
                   {/* Leaf shape abstraction */}
                   <div className="w-6 h-6 bg-emerald-400 rounded-t-full rounded-br-full"></div>
                </div>
              </div>
            </section>

          </div>

        </div>

        {/* Floating Action Button for Chat */}
        <button className="fixed bottom-8 right-8 w-14 h-14 bg-indigo-600 hover:bg-indigo-700 text-white rounded-full shadow-[0_8px_30px_rgb(79,70,229,0.4)] flex items-center justify-center transition-transform hover:scale-105 z-50">
          <MessageCircle className="w-6 h-6" />
        </button>
      </main>
    </div>
  );
};

export default PatientDashboard;
