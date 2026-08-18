import React, { useState, useMemo } from 'react';
import { useQuery } from '@tanstack/react-query';
import { axiosPrivate } from '../../api/axios';
import useAuthStore from '../../store/authStore';
import {
  Calendar, Clock, FileText, Pill, Users, Download,
  Shield, ChevronRight, ChevronLeft, Check, Bot,
  Loader2, CreditCard, ShoppingCart, Scan, FlaskConical, Stethoscope,
  Home, HeartPulse, Package, Upload, Laptop
} from 'lucide-react';
import { useNavigate } from 'react-router-dom';
import './PatientDashboard.css';

/* ════════════════════════════════════════════════════════════════════════════
   DESIGN TOKENS  (matches reference: white + #F3F6FF BG + blue #2563EB)
════════════════════════════════════════════════════════════════════════════ */
const BLUE    = '#2B4AFE';
const BLUE_BG = '#EFF4FF';
const BG      = '#F4F6FF';
const DARK    = '#0B1220';
const MUTED   = '#667085';
const BORDER  = '#DCE3F5';
const WHITE   = '#FFFFFF';
const GREEN   = '#22C55E';

/* ════════════════════════════════════════════════════════════════════════════
   STATIC CONFIG
════════════════════════════════════════════════════════════════════════════ */
const QUICK_ACTIONS = [
  { icon: Calendar,    label: 'Book\nAppointment',  route: '/patient/book',           color: '#2B4AFE' },
  { icon: Users,       label: 'Family\nMember',     route: '/patient/dependents',     color: '#2B4AFE' },
  { icon: Package,     label: 'Order\nMedicine',    route: '/patient/order-medicine', color: '#2B4AFE' },
  { icon: Home,        label: 'Request\nHome Visit', route: '/patient/home-visits',   color: '#2B4AFE' },
  { icon: Upload,      label: 'Upload\nVitals',     route: '/patient/timeline',       color: '#2B4AFE' },
  { icon: Download,    label: 'Download\nRecords',  route: '/patient/documents',      color: '#2B4AFE' },
  { icon: Laptop,      label: 'Tele\nConsult',      route: '/patient/teleconsultations', color: '#2B4AFE' },
  { icon: FileText,    label: 'Medical\nRecords',   route: '/patient/records',        color: '#2B4AFE' },
  { icon: Pill,        label: 'Prescriptions',      route: '/patient/prescriptions',  color: '#2B4AFE' },
  { icon: Scan,        label: 'Radiology',          route: '/patient/radiology',      color: '#2B4AFE' },
  { icon: CreditCard,  label: 'Payments',           route: '/patient/billing',        color: '#2B4AFE' },
  { icon: Shield,      label: 'Insurance',          route: '/patient/insurance',      color: '#2B4AFE' },
  { icon: ShoppingCart,label: 'Orders',             route: '/patient/orders',         color: '#2B4AFE' },
];

const TIMELINE_EVENTS = [
  {
    time: '08:00\nAM',
    title: 'Blood Pressure Stabilized',
    subtitle: 'Average: 120/80 mmHg',
    badge: 'Daily Goal Achieved',
    badgeColor: '#16A34A',
    badgeBg:  '#DCFCE7',
    borderColor: '#16A34A',
    tags: null, author: null,
  },
  {
    time: '10:30\nAM',
    title: 'Annual Cardiac Screening',
    subtitle: 'Dr. Michael Lee • Cardiology Unit 4',
    tags: ['In-person', 'Confirmed'],
    tagColors: [{ color: '#92400E', bg: '#FEF3C7' }, { color: '#065F46', bg: '#D1FAE5' }],
    badge: null, author: null,
    borderColor: '#F59E0B',
  },
  {
    time: '01:00\nPM',
    title: 'Physical Therapy Session',
    subtitle: 'Lower Back Recovery • Day 12',
    author: 'COACH EMILY DAVIS',
    badge: null, tags: null,
    borderColor: '#2563EB',
  },
  {
    time: '03:30\nPM',
    title: 'Mental Wellness Check',
    subtitle: 'Guided Meditation • Focus: Stress Relief',
    badge: 'Personal Milestone',
    badgeColor: '#7C3AED',
    badgeBg:   '#EDE9FE',
    tags: null, author: null,
    borderColor: '#7C3AED',
  },
];

/* ════════════════════════════════════════════════════════════════════════════
   QUICK ACTION BUTTON
════════════════════════════════════════════════════════════════════════════ */
const QABtn = ({ icon: Icon, label, route, color, onClick }) => (
  <button
    onClick={() => onClick(route)}
    className="flex flex-col items-center justify-center gap-2 group min-w-[90px] h-[90px] bg-white rounded-2xl shadow-sm border hover:shadow-md hover:scale-[1.02] transition-all duration-150 p-2"
    style={{ borderColor: BORDER }}
    title={label.replace('\n', ' ')}
  >
    <Icon className="w-6 h-6" style={{ color }} strokeWidth={2} />
    <span className="text-[10px] font-bold text-center leading-tight whitespace-pre-line" style={{ color: DARK }}>
      {label}
    </span>
  </button>
);

/* ════════════════════════════════════════════════════════════════════════════
   MAIN COMPONENT
════════════════════════════════════════════════════════════════════════════ */
const PatientDashboard = () => {
  const { user } = useAuthStore();
  const navigate  = useNavigate();


  const [medicationTaken, setMedicationTaken] = useState(false);
  const [activeView,      setActiveView]      = useState('Week');
  const [activeYear,      setActiveYear]      = useState(2026);

  /* ── API Queries ─────────────────────────────────────────────────── */
  const { data: profile } = useQuery({
    queryKey: ['patientProfile', user?.id],
    queryFn: async () => (await axiosPrivate.get(`/patients/profile/${user.id}`)).data,
    enabled: !!user?.id,
  });

  const { data: appointments = [], isLoading: loadingAppts } = useQuery({
    queryKey: ['patientAppointments', user?.id],
    queryFn: async () => (await axiosPrivate.get(`/appointments/patient/${user.id}`)).data,
    enabled: !!user?.id,
  });

  const { data: prescriptions = [], isLoading: loadingRx } = useQuery({
    queryKey: ['patientPrescriptions', user?.id],
    queryFn: async () => (await axiosPrivate.get(`/prescriptions/patient/${user.id}`)).data,
    enabled: !!user?.id,
  });

  const { data: labReports = [] } = useQuery({
    queryKey: ['patientLabReports'],
    queryFn: async () => (await axiosPrivate.get('/lab/patient/lab-reports')).data,
    enabled: !!user?.id,
  });

  /* ── Derived data ─────────────────────────────────────────────────── */
  const firstName = profile?.firstName || user?.email?.split('@')[0] || 'Patient';
  const lastName  = profile?.lastName || '';
  const patientId = profile?.id ? `AH-${9800 + Number(profile.id)}` : 'AH-9821';

  const upcomingConsultations = useMemo(() => {
    if (!appointments.length) return [];
    return appointments.slice(0, 3).map((apt) => ({
      id:     apt.id,
      doctor: apt.doctorFirstName ? `Dr. ${apt.doctorFirstName} ${apt.doctorLastName || ''}` : '—',
      time:   apt.startTime
        ? new Date(apt.startTime).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })
        : '—',
      type:  (() => {
        const r = apt.reasonForVisit?.toLowerCase() || '';
        if (r.includes('video'))  return { label: 'VIDEO',    cls: 'bg-orange-100 text-orange-700' };
        if (r.includes('follow')) return { label: 'FOLLOW-UP',cls: 'bg-blue-100 text-blue-700' };
        return { label: 'CLINIC', cls: 'bg-green-100 text-green-700' };
      })(),
    }));
  }, [appointments]);

  const nextAppt = useMemo(() => {
    const future = appointments.filter(a => a.startTime && new Date(a.startTime) > new Date());
    if (!future.length) return null;
    const a = future[0];
    const d = new Date(a.startTime);
    return {
      day:      d.getDate(),
      dayName:  d.toLocaleString('en', { weekday: 'short' }).toUpperCase(),
      month:    d.toLocaleString('en', { month: 'short' }).toUpperCase(),
      time:     d.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' }),
      doctor:   a.doctorFirstName ? `Dr. ${a.doctorFirstName} ${a.doctorLastName || ''}` : 'Dr. John Doe',
      specialty: a.specialty || 'Cardiologist',
      status:   a.status || 'Confirmed',
    };
  }, [appointments]);

  const latestRxItem = useMemo(() => {
    const signed = prescriptions.find(rx => ['Signed', 'SIGNED'].includes(rx.status));
    if (!signed?.items?.length) return null;
    return { ...signed.items[0], rxDate: signed.createdAt };
  }, [prescriptions]);

  const recentPrescriptions = useMemo(() =>
    prescriptions.slice(0, 2).map(rx => ({
      id:     rx.id,
      name:   rx.items?.[0]?.medicationName || 'Prescription',
      dosage: rx.items?.[0]?.dosage || '',
      date:   rx.createdAt
        ? new Date(rx.createdAt).toLocaleDateString([], { month: 'short', day: 'numeric', year: 'numeric' })
        : '—',
    })),
  [prescriptions]);

  /* ════════════════════════════════════════════════════════════════════
     RENDER
  ════════════════════════════════════════════════════════════════════ */
  return (
    <div
      className="flex flex-col h-full overflow-auto"
      style={{ background: BG, fontFamily: "'Inter', system-ui, sans-serif" }}
    >
      {/* ── Quick Actions Bar ──────────────────────────────────────── */}
      <div
        className="flex items-center gap-3 px-5 py-4 overflow-x-auto flex-shrink-0"
        style={{ scrollbarWidth: 'none' }}
      >
        {QUICK_ACTIONS.map((qa) => (
          <QABtn key={qa.label} {...qa} onClick={navigate}/>
        ))}
      </div>

      {/* ── Three-Column Grid ──────────────────────────────────────── */}
      <div
        className="flex-1 grid min-h-0 p-4 gap-4"
        style={{ gridTemplateColumns: '272px 1fr 280px' }}
      >

        {/* ══════════════════════════════════════════════════════════
            LEFT COLUMN
        ══════════════════════════════════════════════════════════ */}
        <div className="flex flex-col gap-4 min-h-0 overflow-y-auto" style={{ scrollbarWidth: 'none' }}>

          {/* Upcoming Consultations */}
          <div className="bg-white rounded-2xl border p-4" style={{ borderColor: BORDER }}>
            <div className="flex items-center justify-between mb-3">
              <h2 className="text-[13px] font-bold" style={{ color: DARK }}>Upcoming Consultations</h2>
              <button
                onClick={() => navigate('/patient/appointments')}
                className="text-[11px] font-bold uppercase tracking-wide hover:underline"
                style={{ color: BLUE }}
              >View All</button>
            </div>

            {/* Table header */}
            <div className="grid grid-cols-3 pb-2 mb-1 border-b" style={{ borderColor: BORDER }}>
              {['Doctor', 'Time', 'Type'].map((h, i) => (
                <p key={h} className={`text-[11px] font-semibold ${i === 2 ? 'text-right' : ''}`} style={{ color: MUTED }}>{h}</p>
              ))}
            </div>

            {loadingAppts ? (
              <div className="flex justify-center py-4"><Loader2 className="w-5 h-5 animate-spin" style={{ color: BLUE }}/></div>
            ) : upcomingConsultations.length > 0 ? (
              <div className="divide-y" style={{ '--tw-divide-opacity': 1 }}>
                {upcomingConsultations.map((c) => (
                  <div key={c.id} className="grid grid-cols-3 py-2.5 items-center">
                    <p className="text-[12px] font-bold truncate" style={{ color: DARK }}>{c.doctor}</p>
                    <p className="text-[12px] font-medium" style={{ color: MUTED }}>{c.time}</p>
                    <div className="flex justify-end">
                      <span className={`text-[10px] font-bold px-2 py-0.5 rounded ${c.type.cls}`}>
                        {c.type.label}
                      </span>
                    </div>
                  </div>
                ))}
              </div>
            ) : (
              /* Fallback rows matching reference */
              ['09:00 AM', '09:40 AM', '12:40 PM'].map((t, i) => (
                <div key={i} className="grid grid-cols-3 py-2.5 items-center border-t" style={{ borderColor: '#F1F5F9' }}>
                  <p className="text-[12px] font-bold" style={{ color: DARK }}>Dr. John Doe</p>
                  <p className="text-[12px] font-medium" style={{ color: MUTED }}>{t}</p>
                  <div className="flex justify-end">
                    <span className="text-[10px] font-bold px-2 py-0.5 rounded bg-green-100 text-green-700">CLINIC</span>
                  </div>
                </div>
              ))
            )}

            <button
              onClick={() => navigate('/patient/appointments')}
              className="w-full mt-3 py-2 rounded-xl text-[12px] font-bold transition hover:brightness-95"
              style={{ background: BLUE_BG, color: BLUE }}
            >
              Go to Consultation Hub
            </button>
          </div>

          {/* My Medications */}
          <div className="bg-white rounded-2xl border p-4 flex-1" style={{ borderColor: BORDER }}>
            <div className="flex items-center justify-between mb-4">
              <h2 className="text-[13px] font-bold" style={{ color: DARK }}>My Medications</h2>
              <button
                onClick={() => navigate('/patient/prescriptions')}
                className="text-[11px] font-bold uppercase tracking-wide hover:underline"
                style={{ color: BLUE }}
              >Prescription History</button>
            </div>

            {latestRxItem ? (
              <div className="flex flex-col items-center gap-3 text-center">
                {/* Date badge */}
                <div
                  className="w-16 h-16 rounded-2xl flex flex-col items-center justify-center shadow-sm"
                  style={{ background: '#F8FAFC' }}
                >
                  <p className="text-[9px] font-bold uppercase tracking-wide" style={{ color: MUTED }}>
                    {latestRxItem.rxDate ? new Date(latestRxItem.rxDate).toLocaleString('en', { month: 'short' }) : 'MAY'}
                  </p>
                  <p className="text-2xl font-black leading-none" style={{ color: DARK }}>
                    {latestRxItem.rxDate ? new Date(latestRxItem.rxDate).getDate() : '21'}
                  </p>
                  <p className="text-[9px] font-bold uppercase tracking-wide" style={{ color: MUTED }}>
                    {latestRxItem.rxDate ? new Date(latestRxItem.rxDate).toLocaleString('en', { weekday: 'short' }) : 'TUE'}
                  </p>
                </div>

                <div>
                  <p className="text-[10px] font-bold uppercase tracking-widest mb-1" style={{ color: BLUE }}>
                    Next Dose: 10:30 AM
                  </p>
                  <h3 className="text-[14px] font-bold mb-0.5" style={{ color: DARK }}>
                    {latestRxItem.medicationName}
                  </h3>
                  <p className="text-[11px]" style={{ color: MUTED }}>
                    {latestRxItem.dosage && `${latestRxItem.dosage}`}
                    {latestRxItem.frequency && ` • ${latestRxItem.frequency}`}
                  </p>
                </div>

                <button
                  onClick={() => setMedicationTaken(!medicationTaken)}
                  className="w-full py-2.5 rounded-xl text-[12px] font-bold flex items-center justify-center gap-2 transition-all hover:brightness-110"
                  style={{
                    background: BLUE,
                    color: WHITE,
                  }}
                >
                  <Check className="w-4 h-4"/>
                  {medicationTaken ? 'Dose Taken ✓' : 'Mark as Taken'}
                </button>
              </div>
            ) : (
              /* Fallback */
              <div className="flex flex-col items-center gap-3 text-center">
                <div
                  className="w-16 h-16 rounded-2xl flex flex-col items-center justify-center"
                  style={{ background: '#F8FAFC' }}
                >
                  <p className="text-[9px] font-bold uppercase" style={{ color: MUTED }}>MAY</p>
                  <p className="text-2xl font-black" style={{ color: DARK }}>21</p>
                  <p className="text-[9px] font-bold uppercase" style={{ color: MUTED }}>TUE</p>
                </div>
                <div>
                  <p className="text-[10px] font-bold uppercase tracking-widest mb-1" style={{ color: BLUE }}>
                    Next Dose: 10:30 AM
                  </p>
                  <h3 className="text-[14px] font-bold mb-0.5" style={{ color: DARK }}>
                    Lipitor (Atorvastatin)
                  </h3>
                  <p className="text-[11px]" style={{ color: MUTED }}>10mg Tablet • After Breakfast</p>
                </div>
                <button
                  onClick={() => setMedicationTaken(!medicationTaken)}
                  className="w-full py-2.5 rounded-xl text-[12px] font-bold flex items-center justify-center gap-2 transition-all hover:brightness-110"
                  style={{ background: BLUE, color: WHITE }}
                >
                  <Check className="w-4 h-4"/>
                  {medicationTaken ? 'Dose Taken ✓' : 'Mark as Taken'}
                </button>
              </div>
            )}
          </div>
        </div>

        {/* ══════════════════════════════════════════════════════════
            CENTER COLUMN — Health Journey Timeline
        ══════════════════════════════════════════════════════════ */}
        <div className="bg-white rounded-2xl border flex flex-col min-h-0 overflow-hidden" style={{ borderColor: BORDER }}>

          {/* Timeline Header */}
          <div className="flex items-center justify-between px-5 py-3.5 border-b flex-shrink-0" style={{ borderColor: BORDER }}>
            <div className="flex items-center gap-1">
              <button className="p-1.5 hover:bg-slate-100 rounded-lg transition">
                <ChevronLeft className="w-4 h-4" style={{ color: MUTED }}/>
              </button>
              <h2 className="text-[15px] font-bold px-1" style={{ color: DARK }}>
                Health Journey: {activeYear}
              </h2>
              <button className="p-1.5 hover:bg-slate-100 rounded-lg transition">
                <ChevronRight className="w-4 h-4" style={{ color: MUTED }}/>
              </button>
              <button
                className="ml-2 px-3 py-1 rounded-lg text-[11px] font-bold"
                style={{ background: BLUE_BG, color: BLUE }}
              >
                Today
              </button>
            </div>

            {/* Week / Month / Quarter toggle */}
            <div className="flex items-center gap-0.5 p-1 rounded-xl" style={{ background: '#F1F5F9' }}>
              {['Week', 'Month', 'Quarter'].map((v) => (
                <button
                  key={v}
                  onClick={() => setActiveView(v)}
                  className="px-3 py-1 rounded-lg text-[11px] font-bold transition-all"
                  style={{
                    background: activeView === v ? WHITE : 'transparent',
                    color: activeView === v ? DARK : MUTED,
                    boxShadow: activeView === v ? '0 1px 4px rgba(0,0,0,0.08)' : 'none',
                  }}
                >
                  {v}
                </button>
              ))}
            </div>
          </div>

          {/* Scrollable Timeline Body */}
          <div className="flex-1 overflow-y-auto px-5 py-4 relative" style={{ scrollbarWidth: 'thin', scrollbarColor: '#CBD5E1 transparent' }}>

            {/* Live status indicator */}
            <div className="flex items-center gap-2 mb-5 sticky top-0 z-10">
              <span className="text-[9px] font-bold px-2.5 py-0.5 rounded-full uppercase tracking-wider text-white" style={{ background: BLUE }}>
                Live Status
              </span>
              <div className="flex-1 h-px relative" style={{ background: `${BLUE}40` }}>
                <div className="absolute right-0 -top-1.5 w-3 h-3 rounded-full shadow" style={{ background: BLUE }}/>
              </div>
            </div>

            {/* Timeline Events */}
            <div className="space-y-5 pl-16">
              {TIMELINE_EVENTS.map((ev, i) => (
                <div key={i} className="relative">
                  {/* Time label */}
                  <div
                    className="absolute -left-16 top-3 text-[10px] font-bold text-right leading-tight whitespace-pre-line"
                    style={{ color: MUTED, width: 52 }}
                  >
                    {ev.time}
                  </div>

                  {/* Event card */}
                  <div
                    className="p-4 rounded-r-xl border-l-4 rounded-bl-none"
                    style={{
                      borderLeftColor: ev.borderColor,
                      background: `${ev.borderColor}08`,
                      border: `1px solid ${ev.borderColor}20`,
                      borderLeftWidth: 4,
                    }}
                  >
                    <h4 className="text-[13px] font-bold leading-tight mb-1" style={{ color: DARK }}>
                      {ev.title}
                    </h4>
                    <p className="text-[12px] font-medium mb-2" style={{ color: ev.borderColor }}>
                      {ev.subtitle}
                    </p>

                    {ev.badge && (
                      <span
                        className="inline-block text-[10px] font-bold px-2.5 py-0.5 rounded-full"
                        style={{ background: ev.badgeBg, color: ev.badgeColor }}
                      >
                        {ev.badge}
                      </span>
                    )}

                    {ev.tags && (
                      <div className="flex gap-2 mt-1">
                        {ev.tags.map((t, ti) => (
                          <span
                            key={t}
                            className="text-[10px] font-bold px-2.5 py-0.5 rounded-full"
                            style={{
                              background: ev.tagColors[ti].bg,
                              color: ev.tagColors[ti].color,
                            }}
                          >
                            {t}
                          </span>
                        ))}
                      </div>
                    )}

                    {ev.author && (
                      <p className="text-[10px] font-bold uppercase tracking-wider mt-1.5" style={{ color: MUTED }}>
                        {ev.author}
                      </p>
                    )}
                  </div>
                </div>
              ))}
            </div>
          </div>
        </div>

        {/* ══════════════════════════════════════════════════════════
            RIGHT COLUMN
        ══════════════════════════════════════════════════════════ */}
        <div className="flex flex-col gap-4 min-h-0 overflow-y-auto" style={{ scrollbarWidth: 'none' }}>

          {/* Upcoming Appointments */}
          <div className="bg-white rounded-2xl border p-4" style={{ borderColor: BORDER }}>
            <div className="flex items-center justify-between mb-3">
              <h2 className="text-[13px] font-bold" style={{ color: DARK }}>Upcoming Appointments</h2>
              <button
                onClick={() => navigate('/patient/appointments')}
                className="text-[11px] font-bold uppercase tracking-wide hover:underline"
                style={{ color: BLUE }}
              >View All</button>
            </div>

            {nextAppt ? (
              <div
                className="flex items-start gap-3 py-2"
              >
                {/* Date badge */}
                <div
                  className="flex flex-col items-center justify-center rounded-xl flex-shrink-0 w-14 h-16 shadow-sm"
                  style={{ background: '#F8FAFC' }}
                >
                  <p className="text-[9px] font-bold uppercase" style={{ color: MUTED }}>{nextAppt.month}</p>
                  <p className="text-2xl font-black leading-none" style={{ color: DARK }}>{nextAppt.day}</p>
                  <p className="text-[9px] font-bold uppercase" style={{ color: MUTED }}>{nextAppt.dayName}</p>
                </div>
                <div className="flex-1 min-w-0">
                  <p className="text-[13px] font-bold truncate" style={{ color: DARK }}>{nextAppt.doctor}</p>
                  <p className="text-[11px] font-medium mb-1" style={{ color: MUTED }}>{nextAppt.specialty}</p>
                  <div className="flex items-center justify-between">
                    <p className="text-[12px] font-bold" style={{ color: DARK }}>{nextAppt.time}</p>
                    <span className="text-[10px] font-bold px-2 py-0.5 rounded bg-green-100 text-green-700">
                      {nextAppt.status}
                    </span>
                  </div>
                </div>
              </div>
            ) : (
              /* Fallback matching reference */
              <div
                className="flex items-start gap-3 py-2"
              >
                <div
                  className="flex flex-col items-center justify-center rounded-xl flex-shrink-0 w-14 h-16 shadow-sm"
                  style={{ background: '#F8FAFC' }}
                >
                  <p className="text-[9px] font-bold uppercase" style={{ color: MUTED }}>MAY</p>
                  <p className="text-2xl font-black leading-none" style={{ color: DARK }}>22</p>
                  <p className="text-[9px] font-bold uppercase" style={{ color: MUTED }}>WED</p>
                </div>
                <div className="flex-1 min-w-0">
                  <p className="text-[13px] font-bold" style={{ color: DARK }}>Dr. John Doe</p>
                  <p className="text-[11px] font-medium mb-1" style={{ color: MUTED }}>Cardiologist</p>
                  <div className="flex items-center justify-between">
                    <p className="text-[12px] font-bold" style={{ color: DARK }}>09:00 AM</p>
                    <span className="text-[10px] font-bold px-2 py-0.5 rounded bg-green-100 text-green-700">Confirmed</span>
                  </div>
                </div>
              </div>
            )}

            <button
              onClick={() => navigate('/patient/appointments')}
              className="w-full mt-3 py-2 rounded-xl text-[12px] font-bold transition hover:brightness-95"
              style={{ background: BLUE_BG, color: BLUE }}
            >
              View All Appointments
            </button>
          </div>

          {/* Recent Prescriptions */}
          <div className="bg-white rounded-2xl border p-4" style={{ borderColor: BORDER }}>
            <div className="flex items-center justify-between mb-3">
              <h2 className="text-[13px] font-bold" style={{ color: DARK }}>Recent Prescriptions</h2>
              <button
                onClick={() => navigate('/patient/prescriptions')}
                className="text-[11px] font-bold uppercase tracking-wide hover:underline"
                style={{ color: BLUE }}
              >View All</button>
            </div>

            <div className="space-y-3">
              {(recentPrescriptions.length > 0 ? recentPrescriptions : [
                { id: 1, name: 'Lipitor (Atorvastatin)', dosage: '10mg Tablet', date: 'May 21, 2026' },
                { id: 2, name: 'Amlodipine',             dosage: '5mg Tablet',  date: 'May 10, 2026' },
              ]).map((rx, i, arr) => (
                <div
                  key={rx.id}
                  className={`flex items-center justify-between cursor-pointer hover:opacity-80 transition ${i < arr.length - 1 ? 'pb-3 border-b' : ''}`}
                  style={{ borderColor: BORDER }}
                  onClick={() => navigate('/patient/prescriptions')}
                >
                  <div className="flex items-center gap-3">
                    <div
                      className="w-9 h-9 rounded-xl flex items-center justify-center flex-shrink-0 font-black text-[11px]"
                      style={{ background: BLUE_BG, color: BLUE }}
                    >
                      Rx
                    </div>
                    <div>
                      <p className="text-[12px] font-bold leading-tight" style={{ color: DARK }}>{rx.name}</p>
                      {rx.dosage && (
                        <p className="text-[11px]" style={{ color: MUTED }}>{rx.dosage}</p>
                      )}
                    </div>
                  </div>
                  <div className="flex items-center gap-1.5">
                    <span className="text-[11px] font-medium text-right" style={{ color: MUTED }}>{rx.date}</span>
                    <ChevronRight className="w-3.5 h-3.5" style={{ color: MUTED }}/>
                  </div>
                </div>
              ))}
            </div>
          </div>

          {/* Lab Reports & Health History quick links */}
          <div className="grid grid-cols-2 gap-3">
            <button
              onClick={() => navigate('/patient/lab-reports')}
              className="bg-white rounded-2xl border p-4 flex flex-col items-center gap-2 group hover:shadow-md transition hover:-translate-y-0.5"
              style={{ borderColor: BORDER }}
            >
              <div
                className="w-10 h-10 rounded-xl flex items-center justify-center group-hover:scale-110 transition-transform"
                style={{ background: BLUE_BG }}
              >
                <FlaskConical className="w-5 h-5" style={{ color: BLUE }}/>
              </div>
              <span className="text-[12px] font-bold" style={{ color: DARK }}>Lab Reports</span>
            </button>
            <button
              onClick={() => navigate('/patient/timeline')}
              className="bg-white rounded-2xl border p-4 flex flex-col items-center gap-2 group hover:shadow-md transition hover:-translate-y-0.5"
              style={{ borderColor: BORDER }}
            >
              <div
                className="w-10 h-10 rounded-xl flex items-center justify-center group-hover:scale-110 transition-transform"
                style={{ background: '#FEF3C7' }}
              >
                <Clock className="w-5 h-5 text-amber-600"/>
              </div>
              <span className="text-[12px] font-bold" style={{ color: DARK }}>Health History</span>
            </button>
          </div>

          {/* AI Assistant */}
          <button
            onClick={() => navigate('/patient/assistant')}
            className="flex items-center gap-3 p-4 rounded-2xl text-white transition-all hover:brightness-110 hover:-translate-y-0.5 hover:shadow-xl"
            style={{
              background: `linear-gradient(135deg, ${BLUE} 0%, #1D4ED8 100%)`,
              boxShadow: `0 6px 24px ${BLUE}40`,
            }}
          >
            <div className="w-10 h-10 rounded-xl bg-white/20 flex items-center justify-center flex-shrink-0">
              <Bot className="w-5 h-5 text-white"/>
            </div>
            <div className="flex-1 text-left">
              <p className="text-[13px] font-bold text-white leading-tight">AI Assistant</p>
              <p className="text-[11px] text-white/80">Get answers to your health questions.</p>
            </div>
            <ChevronRight className="w-5 h-5 text-white/70"/>
          </button>

        </div>
        {/* END right column */}
      </div>
      {/* END three-column grid */}
    </div>
  );
};

export default PatientDashboard;
