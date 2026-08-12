import React, { useState, useMemo } from 'react';
import { useQuery } from '@tanstack/react-query';
import { axiosPrivate } from '../../api/axios';
import useAuthStore from '../../store/authStore';
import { 
  Calendar, Clock, FileText, Pill, Users, Video, UploadCloud, Download, 
  Heart, Shield, Settings as SettingsIcon, LayoutGrid, Search, ChevronRight, 
  ChevronLeft, Check, CheckCircle2, Activity, Bot, Plus, ArrowUpRight, Zap,
  Loader2, Moon, Compass, Sparkles, Smile, Droplets, Footprints,
  CreditCard, ShoppingCart, Scan, FlaskConical, Stethoscope, Home
} from 'lucide-react';
import { useNavigate, useSearchParams } from 'react-router-dom';
import { Pill as Rx } from 'lucide-react';
import './PatientDashboard.css';

const TIMELINE_EVENTS = [];

const PatientDashboard = () => {
  const { user } = useAuthStore();
  const navigate = useNavigate();
  const [searchParams, setSearchParams] = useSearchParams();
  const [activeTab, setActiveTab] = useState('Dashboard');
  const [medicationTaken, setMedicationTaken] = useState(false);
  const [symptomQuery, setSymptomQuery] = useState('');
  const [waterCount, setWaterCount] = useState(8);
  const [walkingDone, setWalkingDone] = useState(true);

  // ─── API Queries ───
  const { data: profile, isLoading: profileLoading } = useQuery({
    queryKey: ['patientProfile', user?.id],
    queryFn: async () => {
      const res = await axiosPrivate.get(`/patients/profile/${user.id}`);
      return res.data;
    },
    enabled: !!user?.id
  });

  const { data: appointments = [], isLoading: appointmentsLoading } = useQuery({
    queryKey: ['patientAppointments', user?.id],
    queryFn: async () => {
      const res = await axiosPrivate.get(`/appointments/patient/${user.id}`);
      return res.data;
    },
    enabled: !!user?.id
  });

  const { data: labReports = [], isLoading: labLoading } = useQuery({
    queryKey: ['patientLabReports'],
    queryFn: async () => {
      const res = await axiosPrivate.get('/lab/patient/lab-reports');
      return res.data;
    },
    enabled: !!user?.id
  });

  const patientName = profile?.firstName ? `${profile.firstName} ${profile.lastName || ''}`.trim() : user?.email?.split('@')[0] || 'Patient';
  const patientId = profile?.id ? `AH-${9800 + Number(profile.id)}` : 'AH-9821';

  // Process upcoming consultations from real appointment list
  const upcomingConsultations = useMemo(() => {
    if (appointments && appointments.length > 0) {
      return appointments.slice(0, 3).map((apt) => {
        const timeStr = apt.startTime 
          ? new Date(apt.startTime).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })
          : '09:15 AM';
        const docName = apt.doctorFirstName 
          ? `Dr. ${apt.doctorFirstName} ${apt.doctorLastName || ''}`
          : 'Dr. Michael Lee';
        const isVideo = apt.reasonForVisit?.toLowerCase().includes('video') || false;
        const isFollowUp = apt.reasonForVisit?.toLowerCase().includes('follow') || false;
        const typeLabel = isVideo ? 'Video' : isFollowUp ? 'Follow-up' : 'Clinic';
        const typeStyle = isVideo 
          ? 'bg-orange-100 text-orange-700' 
          : isFollowUp 
          ? 'bg-blue-100 text-blue-700' 
          : 'bg-green-100 text-green-700';

        return {
          id: apt.id,
          doctor: docName,
          time: timeStr,
          type: typeLabel,
          typeStyle
        };
      });
    }
    // Fallback to empty array to ensure no mock data is shown
    return [];
  }, [appointments]);

  // Process recent lab results
  const recentLabResults = useMemo(() => {
    if (labReports && labReports.length > 0) {
      return labReports.slice(0, 2).map((lab, i) => {
        const testName = lab.testCatalog?.testName || (i === 0 ? 'Lipid Profile' : 'CBC (Complete Blood Count)');
        const dateStr = lab.requestedAt ? new Date(lab.requestedAt).toLocaleDateString([], { month: 'short', day: 'numeric' }) : (i === 0 ? 'Today' : 'Yesterday');
        const isNew = lab.status !== 'RELEASED';
        return {
          id: lab.id,
          testName,
          date: dateStr,
          details: lab.status === 'RELEASED' ? 'HDL: 52 mg/dL • Normal' : 'Hemoglobin: 14.2 g/dL',
          statusLabel: isNew ? 'New Report' : 'Reviewed',
          statusStyle: isNew ? 'text-blue-600' : 'text-green-600',
          iconBg: i === 0 ? 'bg-blue-50 text-blue-600' : 'bg-orange-50 text-orange-600'
        };
      });
    }
    // Fallback to empty array to ensure no mock data is shown
    return [];
  }, [labReports]);

  return (
    <div className="patient-dashboard-root">
      <main className="patient-dashboard-main">

        {/* BEGIN: QuickActionsBar */}
        <section className="patient-quick-actions">
          

          

          

          

          

          
        </section>
        {/* END: QuickActionsBar */}



        {/* BEGIN: MainContent Grid (Top 3 columns) */}
        <div className="patient-main-grid">

          {/* BEGIN: LeftColumn (Upcoming Consultations & Medications) */}
          <div className="col-left">
            {/* Upcoming Consultations Card */}
            <section className="bg-white rounded-2xl shadow-2xs border border-slate-100 p-4">
              <div className="flex items-center justify-between mb-3">
                <h2 className="text-xs font-bold text-slate-800">Upcoming Consultations</h2>
                <button onClick={() => navigate('/patient/appointments')} className="text-blue-600 text-[10px] font-bold uppercase tracking-wider hover:underline">
                  View All
                </button>
              </div>
              <div className="space-y-3">
                <table className="w-full text-[11px]">
                  <thead>
                    <tr className="text-slate-400 font-medium border-b border-slate-50">
                      <th className="text-left pb-1.5 font-semibold">Doctor</th>
                      <th className="text-left pb-1.5 font-semibold">Time</th>
                      <th className="text-right pb-1.5 font-semibold">Type</th>
                    </tr>
                  </thead>
                  <tbody className="text-slate-700">
                    {upcomingConsultations.map((item) => (
                      <tr key={item.id} className="border-t border-slate-50">
                        <td className="py-2.5 font-bold text-slate-800">{item.doctor}</td>
                        <td className="py-2.5 text-slate-500 font-medium">{item.time}</td>
                        <td className="py-2.5 text-right">
                          <span className={`px-2 py-0.5 rounded text-[9px] font-bold uppercase ${item.typeStyle}`}>
                            {item.type}
                          </span>
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
                <button 
                  onClick={() => navigate('/patient/appointments')}
                  className="w-full mt-2 py-2 bg-slate-50 rounded-lg brand-purple text-xs font-bold hover:bg-slate-100 transition"
                >
                  Go to Consultation Hub
                </button>
              </div>
            </section>

            {/* My Medications Card */}
            <section className="bg-white rounded-2xl shadow-2xs border border-slate-100 p-4">
              <div className="flex items-center justify-between mb-4">
                <h2 className="text-xs font-bold text-slate-800">My Medications</h2>
                <button onClick={() => navigate('/patient/prescriptions')} className="text-blue-600 text-[10px] font-bold uppercase tracking-wider hover:underline">
                  Prescription History
                </button>
              </div>
              <div className="flex flex-col items-center space-y-3 text-center">
                <div className="w-14 h-14 bg-blue-50 rounded-2xl flex items-center justify-center border border-blue-100 shadow-2xs">
                  <div className="text-center">
                    <p className="text-[9px] font-bold text-slate-400 uppercase">May</p>
                    <p className="text-lg font-black text-slate-800 leading-none">21</p>
                    <p className="text-[9px] font-bold text-slate-400 uppercase">Tue</p>
                  </div>
                </div>
                <div>
                  <p className="text-[10px] font-bold brand-purple uppercase tracking-wide">Next Dose: 10:30 AM</p>
                  <h3 className="text-sm font-bold text-slate-800 mt-0.5">Lipitor (Atorvastatin)</h3>
                  <p className="text-[10px] text-slate-400 font-medium">10mg Tablet • After Breakfast</p>
                </div>
                <button 
                  onClick={() => setMedicationTaken(!medicationTaken)}
                  className={`w-full py-2.5 rounded-xl text-xs font-bold flex items-center justify-center gap-2 shadow-xs transition ${
                    medicationTaken 
                      ? 'bg-green-600 text-white' 
                      : 'bg-brand-purple text-white hover:opacity-90'
                  }`}
                >
                  <Check className="w-4 h-4" />
                  <span>{medicationTaken ? 'Dose Taken' : 'Mark as Taken'}</span>
                </button>
              </div>
            </section>
          </div>
          {/* END: LeftColumn */}

          {/* BEGIN: CenterColumn (Health Journey Timeline) */}
          <div className="col-center">
            <div className="timeline-container">
              {/* Header controls */}
              <div className="p-4 border-b border-slate-100 flex items-center justify-between flex-shrink-0">
                <div className="flex items-center gap-2">
                  <div className="flex items-center gap-1">
                    <button className="p-1 hover:bg-slate-100 rounded-lg text-slate-400 hover:text-slate-700">
                      <ChevronLeft className="w-4 h-4" />
                    </button>
                    <h2 className="text-base font-bold text-slate-800 px-1">Health Journey: 2026</h2>
                    <button className="p-1 hover:bg-slate-100 rounded-lg text-slate-400 hover:text-slate-700">
                      <ChevronRight className="w-4 h-4" />
                    </button>
                  </div>
                  <button className="px-3 py-1 bg-blue-50 brand-purple text-xs font-bold rounded-lg ml-2">
                    Today
                  </button>
                </div>
                <div className="flex bg-slate-50 p-1 rounded-xl">
                  <button className="px-3 py-1 bg-white shadow-2xs rounded-lg text-[11px] font-bold text-slate-800">Week</button>
                  <button className="px-3 py-1 text-slate-500 text-[11px] font-bold hover:text-slate-800">Month</button>
                  <button className="px-3 py-1 text-slate-500 text-[11px] font-bold hover:text-slate-800">Quarter</button>
                </div>
              </div>

              {/* Scrollable Timeline Content */}
              <div className="timeline-scroll-body p-5">
                {/* Live Status Red Line Indicator */}
                <div className="sticky top-4 left-0 w-full flex items-center z-10 my-2">
                  <div className="bg-red-500 text-white text-[9px] font-bold px-2 py-0.5 rounded-full shadow-2xs uppercase tracking-wider">
                    LIVE STATUS
                  </div>
                  <div className="flex-1 h-[1px] bg-red-500 ml-2 relative">
                    <div className="absolute right-0 -top-1 w-2.5 h-2.5 bg-red-500 rounded-full shadow-xs"></div>
                  </div>
                </div>

                {/* Timeline Milestone Entries */}
                <div className="space-y-6 pl-14 py-2">
                  {TIMELINE_EVENTS.map((event, index) => (
                    <div key={index} className="relative">
                      <div className="absolute -left-14 top-1 text-[10px] font-bold text-slate-400 text-right w-11">
                        {event.time}
                      </div>
                      <div className={`p-3.5 rounded-r-xl ${event.width} shadow-2xs border-l-4 ${event.cardStyle}`}>
                        <h4 className="text-xs font-bold leading-tight">{event.title}</h4>
                        <p className={`text-[11px] mt-0.5 font-medium ${event.subStyle}`}>{event.subtitle}</p>
                        
                        {event.badge && (
                          <div className={`mt-2 text-[9px] font-bold inline-block px-2 py-0.5 rounded ${event.badgeStyle}`}>
                            {event.badge}
                          </div>
                        )}

                        {event.tags && (
                          <div className="mt-2 flex gap-1.5">
                            {event.tags.map((t, idx) => (
                              <span key={idx} className="text-[9px] font-bold text-orange-700 bg-white/60 px-2 py-0.5 rounded">
                                {t}
                              </span>
                            ))}
                          </div>
                        )}

                        {event.author && (
                          <p className="mt-1.5 text-[9px] font-bold text-slate-400 uppercase">{event.author}</p>
                        )}
                      </div>
                    </div>
                  ))}
                </div>
              </div>
            </div>
          </div>
          {/* END: CenterColumn */}

          {/* BEGIN: RightColumn */}
          <div className="col-right flex flex-col h-full">
            <div className="mt-auto">
            {/* Recent Prescriptions Card (New Design) */}
            <section className="bg-white rounded-[16px] shadow-sm p-4 mt-0">
              <div className="flex items-center justify-between mb-3">
                <h2 className="text-[13px] font-extrabold text-slate-900 font-serif">Recent Prescriptions</h2>
                <button onClick={() => navigate('/patient/prescriptions')} className="text-indigo-600 text-[10px] font-bold uppercase tracking-widest hover:underline">
                  VIEW ALL
                </button>
              </div>
              <div className="space-y-3">
                {/* Item 1 */}
                <div className="flex items-center justify-between pb-3 border-b border-slate-50 cursor-pointer hover:opacity-80 transition" onClick={() => navigate('/patient/prescriptions')}>
                  <div className="flex items-center gap-3">
                    <div className="w-9 h-9 rounded-xl bg-indigo-50/60 flex items-center justify-center shrink-0">
                      <Rx className="w-4 h-4 text-indigo-800" />
                    </div>
                    <div>
                      <h3 className="font-bold text-slate-800 text-[12px] leading-tight">Lipitor<br/>(Atorvastatin)</h3>
                      <p className="text-slate-500 text-[10px] font-medium mt-0.5">10mg Tablet</p>
                    </div>
                  </div>
                  <div className="flex items-center gap-2">
                    <span className="text-[10px] font-bold text-slate-500 text-right">May 21,<br/>2026</span>
                    <ChevronRight className="w-3 h-3 text-slate-300" />
                  </div>
                </div>
                {/* Item 2 */}
                <div className="flex items-center justify-between pt-1 cursor-pointer hover:opacity-80 transition" onClick={() => navigate('/patient/prescriptions')}>
                  <div className="flex items-center gap-3">
                    <div className="w-9 h-9 rounded-xl bg-indigo-50/60 flex items-center justify-center shrink-0">
                      <Rx className="w-4 h-4 text-indigo-800" />
                    </div>
                    <div>
                      <h3 className="font-bold text-slate-800 text-[12px]">Amlodipine</h3>
                      <p className="text-slate-500 text-[10px] font-medium mt-0.5">5mg Tablet</p>
                    </div>
                  </div>
                  <div className="flex items-center gap-2">
                    <span className="text-[10px] font-bold text-slate-500">May 10, 2026</span>
                    <ChevronRight className="w-3 h-3 text-slate-300" />
                  </div>
                </div>
              </div>
            </section>
            {/* Quick Link Buttons (New Design) */}
            <div className="grid grid-cols-2 md:grid-cols-4 gap-3 mt-3">
              
              <button onClick={() => navigate('/patient/assistant')} className="bg-white rounded-[14px] shadow-sm p-3 flex items-center justify-center gap-2 hover:shadow-md transition">
                <Bot size={18} className="text-indigo-600" />
                <span className="font-semibold text-slate-700 text-sm">AI Assistant</span>
              </button>

              <button onClick={() => navigate('/patient/documents')} className="bg-white rounded-[14px] shadow-sm p-3 flex items-center justify-center gap-2 hover:shadow-md transition">
                <FileIcon size={18} className="text-blue-600" />
                <span className="font-semibold text-slate-700 text-sm">Documents</span>
              </button>

              <button onClick={() => navigate('/patient/timeline')} className="bg-white rounded-[14px] shadow-sm p-3 flex items-center justify-center gap-2 hover:shadow-md transition">
                <Activity size={18} className="text-orange-500" />
                <span className="font-semibold text-slate-700 text-sm">Timeline</span>
              </button>

              <button onClick={() => navigate('/patient/lab-reports')} className="bg-white rounded-[14px] shadow-sm p-3 flex items-center justify-center gap-2 hover:shadow-md transition">
                <FlaskConical size={18} className="text-purple-500" />
                <span className="font-semibold text-slate-700 text-sm">Labs</span>
              </button>

              <button onClick={() => navigate('/patient/home-visits')} className="bg-white rounded-[14px] shadow-sm p-3 flex items-center justify-center gap-2 hover:shadow-md transition">
                <Home size={18} className="text-teal-500" />
                <span className="font-semibold text-slate-700 text-sm">Home Visit</span>
              </button>

              <button onClick={() => navigate('/patient/teleconsultations')} className="bg-white rounded-[14px] shadow-sm p-3 flex items-center justify-center gap-2 hover:shadow-md transition">
                <Video size={18} className="text-indigo-500" />
                <span className="font-semibold text-slate-700 text-sm">Video Call</span>
              </button>

              <button onClick={() => navigate('/patient/dependents')} className="bg-white rounded-[14px] shadow-sm p-3 flex items-center justify-center gap-2 hover:shadow-md transition">
                <Users size={18} className="text-blue-500" />
                <span className="font-semibold text-slate-700 text-sm">Family</span>
              </button>

              <button onClick={() => navigate('/patient/settings')} className="bg-white rounded-[14px] shadow-sm p-3 flex items-center justify-center gap-2 hover:shadow-md transition">
                <SettingsIcon size={18} className="text-slate-500" />
                <span className="font-semibold text-slate-700 text-sm">Settings</span>
              </button>
            </div>
            
            </div>
          </div>
          {/* END: RightColumn */}

        </div>
        {/* END: MainContent Grid */}
      </main>
    </div>
  );
};

export default PatientDashboard;
