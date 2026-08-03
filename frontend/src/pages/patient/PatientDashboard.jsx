import React, { useState, useMemo } from 'react';
import { useQuery } from '@tanstack/react-query';
import { axiosPrivate } from '../../api/axios';
import useAuthStore from '../../store/authStore';
import { 
  Calendar, Clock, FileText, Pill, Users, Video, UploadCloud, Download, 
  Heart, Shield, Settings as SettingsIcon, LayoutGrid, Search, ChevronRight, 
  ChevronLeft, Check, CheckCircle2, Activity, Bot, Plus, ArrowUpRight, Zap,
  Loader2, Moon, Compass, Sparkles, Smile, Droplets, Footprints
} from 'lucide-react';
import { useNavigate, useSearchParams } from 'react-router-dom';
import './PatientDashboard.css';

const TIMELINE_EVENTS = [
  {
    time: '08:00 AM',
    title: 'Blood Pressure Stabilized',
    subtitle: 'Average: 120/80 mmHg',
    badge: 'Daily Goal Achieved',
    badgeStyle: 'bg-green-100/60 text-green-700',
    cardStyle: 'bg-green-50/80 border-l-4 border-green-500 text-green-900',
    subStyle: 'text-green-700',
    width: 'w-3/4'
  },
  {
    time: '10:30 AM',
    title: 'Annual Cardiac Screening',
    subtitle: 'Dr. Michael Lee • Cardiology Unit 4',
    tags: ['In-person', 'Confirmed'],
    cardStyle: 'bg-orange-50/80 border-l-4 border-orange-400 text-orange-900',
    subStyle: 'text-orange-700',
    width: 'w-2/3 ml-12'
  },
  {
    time: '01:00 PM',
    title: 'Physical Therapy Session',
    subtitle: 'Lower Back Recovery • Day 12',
    author: 'COACH EMILY DAVIS',
    cardStyle: 'bg-blue-50/80 border-l-4 border-blue-500 text-blue-900',
    subStyle: 'text-blue-700',
    width: 'w-1/2 ml-auto'
  },
  {
    time: '03:30 PM',
    title: 'Mental Wellness Check',
    subtitle: 'Guided Meditation • Focus: Stress Relief',
    badge: 'Personal Milestone',
    badgeStyle: 'bg-purple-100/60 text-purple-700',
    cardStyle: 'bg-purple-50/80 border-l-4 border-purple-500 text-purple-900',
    subStyle: 'text-purple-700',
    width: 'w-2/3 ml-4'
  }
];

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
          <button 
            onClick={() => navigate('/patient/book')}
            className="bg-white border border-slate-100 rounded-xl p-3 flex flex-col items-center justify-center min-w-[104px] h-[72px] shadow-2xs hover:shadow-xs transition"
          >
            <div className="p-1 bg-blue-50 text-blue-600 rounded-md mb-1">
              <Calendar className="w-4 h-4" />
            </div>
            <span className="text-[10px] font-bold text-center text-slate-800 leading-tight">Book<br/>Appointment</span>
          </button>

          <button 
            onClick={() => navigate('/patient/profile')}
            className="bg-white border border-slate-100 rounded-xl p-3 flex flex-col items-center justify-center min-w-[104px] h-[72px] shadow-2xs hover:shadow-xs transition"
          >
            <div className="p-1 bg-green-50 text-green-600 rounded-md mb-1">
              <Users className="w-4 h-4" />
            </div>
            <span className="text-[10px] font-bold text-center text-slate-800 leading-tight">Add<br/>Family Member</span>
          </button>

          <button 
            onClick={() => navigate('/patient/prescriptions')}
            className="bg-white border border-slate-100 rounded-xl p-3 flex flex-col items-center justify-center min-w-[104px] h-[72px] shadow-2xs hover:shadow-xs transition"
          >
            <div className="p-1 bg-orange-50 text-orange-600 rounded-md mb-1">
              <Pill className="w-4 h-4" />
            </div>
            <span className="text-[10px] font-bold text-center text-slate-800 leading-tight">Order<br/>Medicine</span>
          </button>

          <button 
            onClick={() => navigate('/patient/book')}
            className="bg-white border border-slate-100 rounded-xl p-3 flex flex-col items-center justify-center min-w-[104px] h-[72px] shadow-2xs hover:shadow-xs transition"
          >
            <div className="p-1 bg-blue-50 text-blue-600 rounded-md mb-1">
              <Video className="w-4 h-4" />
            </div>
            <span className="text-[10px] font-bold text-center text-slate-800 leading-tight">Request<br/>Home Visit</span>
          </button>

          <button 
            onClick={() => navigate('/patient/records')}
            className="bg-white border border-slate-100 rounded-xl p-3 flex flex-col items-center justify-center min-w-[104px] h-[72px] shadow-2xs hover:shadow-xs transition"
          >
            <div className="p-1 bg-purple-50 text-purple-600 rounded-md mb-1">
              <UploadCloud className="w-4 h-4" />
            </div>
            <span className="text-[10px] font-bold text-center text-slate-800 leading-tight">Upload<br/>Vitals</span>
          </button>

          <button 
            onClick={() => navigate('/patient/records')}
            className="bg-white border border-slate-100 rounded-xl p-3 flex flex-col items-center justify-center min-w-[104px] h-[72px] shadow-2xs hover:shadow-xs transition"
          >
            <div className="p-1 bg-orange-50 text-orange-600 rounded-md mb-1">
              <Download className="w-4 h-4" />
            </div>
            <span className="text-[10px] font-bold text-center text-slate-800 leading-tight">Download<br/>Records</span>
          </button>

          <button 
            onClick={() => navigate('/patient/book')}
            className="bg-white border border-slate-100 rounded-xl p-3 flex flex-col items-center justify-center min-w-[104px] h-[72px] shadow-2xs hover:shadow-xs transition"
          >
            <div className="p-1 bg-green-50 text-green-600 rounded-md mb-1">
              <Video className="w-4 h-4" />
            </div>
            <span className="text-[10px] font-bold text-center text-slate-800 leading-tight">Tele<br/>Consult</span>
          </button>

          <button 
            onClick={() => navigate('/patient/appointments')}
            className="bg-white border border-slate-100 rounded-xl p-3 flex flex-col items-center justify-center min-w-[104px] h-[72px] shadow-2xs hover:shadow-xs transition"
          >
            <div className="p-1 bg-purple-50 text-purple-600 rounded-md mb-1">
              <Clock className="w-4 h-4" />
            </div>
            <span className="text-[10px] font-bold text-center text-slate-800 leading-tight">Health<br/>History</span>
          </button>
        </section>
        {/* END: QuickActionsBar */}

        {/* BEGIN: NavigationTabs */}
        <nav className="patient-nav-tabs shadow-2xs">
          <button onClick={() => { setActiveTab('Dashboard'); navigate('/patient/dashboard'); }} className={`nav-tab ${activeTab === 'Dashboard' ? 'active' : ''}`}>
            <LayoutGrid className="w-3.5 h-3.5" />
            <span>Dashboard</span>
          </button>
          <button onClick={() => { setActiveTab('Appointments'); navigate('/patient/appointments'); }} className={`nav-tab ${activeTab === 'Appointments' ? 'active' : ''}`}>
            <Calendar className="w-3.5 h-3.5" />
            <span>Appointments</span>
          </button>
          <button onClick={() => { setActiveTab('Prescriptions'); navigate('/patient/prescriptions'); }} className={`nav-tab ${activeTab === 'Prescriptions' ? 'active' : ''}`}>
            <Pill className="w-3.5 h-3.5" />
            <span>Prescriptions</span>
          </button>
          <button onClick={() => { setActiveTab('Lab Results'); navigate('/patient/lab-reports'); }} className={`nav-tab ${activeTab === 'Lab Results' ? 'active' : ''}`}>
            <FileText className="w-3.5 h-3.5" />
            <span>Lab Results</span>
          </button>
          <button onClick={() => { setActiveTab('Health Insights'); navigate('/patient/records'); }} className={`nav-tab ${activeTab === 'Health Insights' ? 'active' : ''}`}>
            <Heart className="w-3.5 h-3.5" />
            <span>Health Insights</span>
          </button>
          <button onClick={() => { setActiveTab('Insurance'); navigate('/patient/insurance'); }} className={`nav-tab ${activeTab === 'Insurance' ? 'active' : ''}`}>
            <Shield className="w-3.5 h-3.5" />
            <span>Insurance</span>
          </button>
          <button onClick={() => { setActiveTab('Settings'); navigate('/patient/profile'); }} className="nav-tab ml-auto">
            <SettingsIcon className="w-3.5 h-3.5" />
            <span>Settings</span>
          </button>
        </nav>
        {/* END: NavigationTabs */}

        {/* BEGIN: MainContent Grid (Top 3 columns) */}
        <div className="patient-main-grid">

          {/* BEGIN: LeftColumn (Upcoming Consultations & Medications) */}
          <div className="col-left">
            {/* Upcoming Consultations Card */}
            <section className="bg-white rounded-2xl shadow-2xs border border-slate-100 p-4">
              <div className="flex items-center justify-between mb-3">
                <h2 className="text-xs font-bold text-slate-800">Upcoming Consultations</h2>
                <button onClick={() => navigate('/patient/appointments')} className="brand-purple text-[10px] font-bold uppercase tracking-wider hover:underline">
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
                <button onClick={() => navigate('/patient/prescriptions')} className="brand-purple text-[10px] font-bold uppercase tracking-wider hover:underline">
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

          {/* BEGIN: RightColumn (Health Score & Lab Results) */}
          <div className="col-right">
            {/* Health Score Metrics Card */}
            <section className="bg-white rounded-2xl shadow-2xs border border-slate-100 p-4">
              <div className="flex items-center justify-between mb-3">
                <h2 className="text-xs font-bold text-slate-800">Health Score Metrics</h2>
                <button onClick={() => navigate('/patient/records')} className="brand-purple text-[10px] font-bold uppercase tracking-wider hover:underline">
                  Analysis
                </button>
              </div>
              <div className="space-y-3">
                <div className="flex items-center gap-3">
                  <div className="w-11 h-11 rounded-full border-4 border-green-500 flex items-center justify-center shrink-0">
                    <span className="text-xs font-black text-slate-800">88</span>
                  </div>
                  <div>
                    <p className="text-[11px] font-bold text-slate-800 leading-tight">Overall Wellness</p>
                    <p className="text-[10px] text-green-600 font-bold mt-0.5">↑ 4% from last month</p>
                  </div>
                </div>
                <div className="space-y-2.5">
                  <div>
                    <div className="flex justify-between text-[10px] font-bold text-slate-600 mb-1">
                      <span className="uppercase">Heart Rate</span>
                      <span>72 BPM</span>
                    </div>
                    <div className="h-1.5 bg-slate-100 rounded-full overflow-hidden">
                      <div className="h-full bg-blue-500 w-[72%] rounded-full"></div>
                    </div>
                  </div>
                  <div>
                    <div className="flex justify-between text-[10px] font-bold text-slate-600 mb-1">
                      <span className="uppercase">Sleep Quality</span>
                      <span>7.5 Hrs</span>
                    </div>
                    <div className="h-1.5 bg-slate-100 rounded-full overflow-hidden">
                      <div className="h-full bg-purple-500 w-[85%] rounded-full"></div>
                    </div>
                  </div>
                </div>
                <button 
                  onClick={() => navigate('/patient/records')}
                  className="w-full mt-2 py-2 bg-slate-50 rounded-lg brand-purple text-xs font-bold hover:bg-slate-100 transition"
                >
                  Detailed Metrics Report
                </button>
              </div>
            </section>

            {/* Recent Lab Results Card */}
            <section className="bg-white rounded-2xl shadow-2xs border border-slate-100 p-4">
              <div className="flex items-center justify-between mb-3">
                <h2 className="text-xs font-bold text-slate-800">Recent Lab Results</h2>
                <button onClick={() => navigate('/patient/lab-reports')} className="brand-purple text-[10px] font-bold uppercase tracking-wider hover:underline">
                  All Tests
                </button>
              </div>
              <div className="space-y-2.5">
                {recentLabResults.map((result) => (
                  <div 
                    key={result.id} 
                    onClick={() => navigate('/patient/lab-reports')}
                    className="flex items-start gap-2.5 p-2 rounded-xl border border-transparent hover:border-slate-100 hover:bg-slate-50 transition cursor-pointer"
                  >
                    <div className={`p-2 rounded-lg shrink-0 ${result.iconBg}`}>
                      <FileText className="w-4 h-4" />
                    </div>
                    <div className="flex-1 min-w-0">
                      <div className="flex justify-between items-start">
                        <p className="text-xs font-bold text-slate-800 truncate">{result.testName}</p>
                        <span className="text-[9px] text-slate-400 font-bold uppercase shrink-0">{result.date}</span>
                      </div>
                      <p className="text-[10px] text-slate-500 truncate mt-0.5">{result.details}</p>
                      <p className={`text-[9px] font-bold uppercase mt-1 ${result.statusStyle}`}>{result.statusLabel}</p>
                    </div>
                  </div>
                ))}
                <button 
                  onClick={() => navigate('/patient/lab-reports')}
                  className="w-full mt-2 py-2 bg-slate-50 rounded-lg brand-purple text-xs font-bold hover:bg-slate-100 transition"
                >
                  View Lab Archive
                </button>
              </div>
            </section>
          </div>
          {/* END: RightColumn */}

        </div>
        {/* END: MainContent Grid */}

      </main>
    </div>
  );
};

export default PatientDashboard;
