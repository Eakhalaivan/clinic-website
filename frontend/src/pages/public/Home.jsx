import React, { useEffect, useRef, useState, useCallback } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import {
  ShieldCheck, Users, Building2, Phone, PhoneCall, ChevronDown,
  Calendar, User, Mail, ArrowRight, HeartPulse, Stethoscope,
  Brain, Bone, FlaskConical, Activity, Menu, X, Star,
  CheckCircle2, Clock, Ambulance, Microscope,
  MapPin, ChevronRight, Quote, Loader2
} from 'lucide-react';
import { usePublicDoctors, usePublicDepartments, useBookAppointment, useClinicStats } from '../../api/publicApi';
import toast from 'react-hot-toast';

/* ════════════════════════════════════════════════════════════════════════════
   CONSTANTS & TOKENS
════════════════════════════════════════════════════════════════════════════ */
const BLUE     = '#2B4AFE';
const BLUE_D   = '#1A38E0';
const DARK     = '#0B1220';
const BG       = '#F4F6FF';
const BG_LIGHT = '#E8EDFF';
const MUTED    = '#667085';
const BORDER   = '#DCE3F5';
const WHITE    = '#FFFFFF';

/* ════════════════════════════════════════════════════════════════════════════
   SCROLL-REVEAL HOOK
════════════════════════════════════════════════════════════════════════════ */
function useScrollReveal() {
  useEffect(() => {
    const els = document.querySelectorAll('[data-reveal]');
    if (!els.length) return;
    const obs = new IntersectionObserver(
      (entries) => entries.forEach((e) => {
        if (e.isIntersecting) {
          e.target.classList.add('revealed');
          obs.unobserve(e.target);
        }
      }),
      { threshold: 0.1, rootMargin: '0px 0px -40px 0px' }
    );
    els.forEach((el) => obs.observe(el));
    return () => obs.disconnect();
  }, []);
}

/* ════════════════════════════════════════════════════════════════════════════
   COUNTER ANIMATION HOOK
════════════════════════════════════════════════════════════════════════════ */
function useCounter(target, duration = 1800) {
  const [count, setCount] = useState(0);
  const ref = useRef(null);
  const started = useRef(false);

  useEffect(() => {
    const el = ref.current;
    if (!el) return;
    const obs = new IntersectionObserver(([entry]) => {
      if (entry.isIntersecting && !started.current) {
        started.current = true;
        const start = performance.now();
        const step = (now) => {
          const progress = Math.min((now - start) / duration, 1);
          const eased = 1 - Math.pow(1 - progress, 3);
          setCount(Math.round(eased * target));
          if (progress < 1) requestAnimationFrame(step);
        };
        requestAnimationFrame(step);
      }
    }, { threshold: 0.5 });
    obs.observe(el);
    return () => obs.disconnect();
  }, [target, duration]);

  return { count, ref };
}

/* ════════════════════════════════════════════════════════════════════════════
   STATIC DATA
════════════════════════════════════════════════════════════════════════════ */
const NAV_LINKS = [
  { label: 'Home', href: '#hero' },
  { label: 'About Us', href: '#about' },
  { label: 'Services', href: '#services' },
  { label: 'Doctors', href: '#doctors' },
  { label: 'Blog', href: '#blog' },
  { label: 'Contact Us', href: '#contact' },
];

const SERVICES = [
  {
    icon: HeartPulse, title: 'Cardiology',
    desc: 'Comprehensive heart care with advanced diagnostic tools and experienced cardiologists.',
    color: '#FF6B6B',
  },
  {
    icon: Activity, title: 'Pulmonary',
    desc: 'Expert lung and respiratory disease management for optimal breathing health.',
    color: '#4ECDC4',
  },
  {
    icon: Brain, title: 'Neurology',
    desc: 'State-of-the-art neurological treatment for brain and nervous system disorders.',
    color: '#A855F7',
  },
  {
    icon: Bone, title: 'Orthopedics',
    desc: 'Advanced bone, joint, and muscle care with minimally invasive surgery options.',
    color: '#F59E0B',
  },
  {
    icon: Microscope, title: 'Laboratory',
    desc: 'Cutting-edge diagnostic laboratory services for accurate and timely results.',
    color: '#10B981',
  },
];

const WHY_US = [
  { icon: ShieldCheck, title: 'Expert Doctors', desc: '25+ board-certified specialists across all major medical disciplines.' },
  { icon: PhoneCall, title: 'Emergency Care', desc: '24/7 emergency care with rapid response teams always ready.' },
  { icon: Building2, title: 'Modern Facilities', desc: 'State-of-the-art equipment and internationally accredited facilities.' },
  { icon: Clock, title: '24/7 Support', desc: 'Round-the-clock patient support and telemedicine consultations.' },
];

const TESTIMONIALS = [
  {
    name: 'Sarah Johnson', role: 'Patient', rating: 5,
    text: 'The level of care I received was exceptional. The doctors were knowledgeable, compassionate, and took time to explain everything. Highly recommend!',
    avatar: 'https://i.pravatar.cc/80?img=47',
  },
  {
    name: 'Michael Chen', role: 'Patient', rating: 5,
    text: 'World-class facility with cutting-edge technology. My surgery was a complete success and recovery was smooth thanks to the amazing nursing staff.',
    avatar: 'https://i.pravatar.cc/80?img=33',
  },
  {
    name: 'Emily Rodriguez', role: 'Patient', rating: 5,
    text: 'After years of dealing with chronic pain, the orthopedics team here finally gave me my life back. I cannot thank them enough for their expertise.',
    avatar: 'https://i.pravatar.cc/80?img=32',
  },
];

const BLOG_POSTS = [
  {
    tag: 'Cardiology', date: 'August 15, 2026',
    title: '10 Warning Signs You Shouldn\'t Ignore About Your Heart Health',
    img: 'https://images.unsplash.com/photo-1576091160399-112ba8d25d1d?q=80&w=600&auto=format&fit=crop',
    excerpt: 'Early detection is key. Learn the critical warning signs that indicate you should see a cardiologist immediately.',
  },
  {
    tag: 'Wellness', date: 'August 10, 2026',
    title: 'How Modern Neurology Is Transforming Stroke Recovery',
    img: 'https://images.unsplash.com/photo-1559757175-5700dde675bc?q=80&w=600&auto=format&fit=crop',
    excerpt: 'Innovative rehabilitation techniques are helping stroke survivors regain function faster than ever before.',
  },
  {
    tag: 'Orthopedics', date: 'August 5, 2026',
    title: 'Minimally Invasive Surgery: Faster Recovery, Better Outcomes',
    img: 'https://images.unsplash.com/photo-1582750433449-648ed127bb54?q=80&w=600&auto=format&fit=crop',
    excerpt: 'Learn how our orthopedic surgeons use the latest arthroscopic techniques for better patient outcomes.',
  },
];

const DEPARTMENTS_FALLBACK = [
  'Cardiology', 'Neurology', 'Orthopedics', 'Pulmonary',
  'Pediatrics', 'Dermatology', 'Ophthalmology',
];

/* ════════════════════════════════════════════════════════════════════════════
   SVG LOGO MARK
════════════════════════════════════════════════════════════════════════════ */
const MedviceLogo = ({ size = 40 }) => (
  <div className="flex items-center gap-2.5 select-none">
    <div
      className="rounded-xl flex items-center justify-center shadow-md flex-shrink-0"
      style={{ width: size, height: size, background: BLUE }}
    >
      <HeartPulse className="text-white" style={{ width: size * 0.5, height: size * 0.5 }} strokeWidth={2.5}/>
    </div>
    <div>
      <p className="font-black text-[17px] leading-none tracking-tight" style={{ color: DARK }}>Medvice</p>
      <p className="text-[10px] font-medium leading-tight" style={{ color: MUTED }}>Medical Clinic</p>
    </div>
  </div>
);

/* ════════════════════════════════════════════════════════════════════════════
   STAT COUNTER COMPONENT
════════════════════════════════════════════════════════════════════════════ */
const StatCounter = ({ value, suffix, label, icon: Icon }) => {
  const num = parseInt(String(value).replace(/\D/g, '')) || 0;
  const { count, ref } = useCounter(num);

  const display = count >= 1000
    ? `${(count / 1000).toFixed(count % 1000 === 0 ? 0 : 1)}K`
    : String(count);

  return (
    <div ref={ref} className="text-center">
      <div
        className="w-12 h-12 mx-auto rounded-full flex items-center justify-center mb-3"
        style={{ background: BG_LIGHT }}
      >
        <Icon className="w-5 h-5" style={{ color: BLUE }}/>
      </div>
      <p className="text-2xl font-black leading-none mb-1" style={{ color: BLUE }}>
        {display}{suffix}
      </p>
      <p className="text-[11px] font-bold text-gray-600 uppercase tracking-wide">{label}</p>
    </div>
  );
};

/* ════════════════════════════════════════════════════════════════════════════
   DOCTOR CARD
════════════════════════════════════════════════════════════════════════════ */
const DoctorCard = ({ doc, getBookLink }) => {
  const name = doc.firstName && doc.lastName
    ? `Dr. ${doc.firstName} ${doc.lastName}`
    : `Dr. ${doc.name || 'Specialist'}`;

  const avatarUrl = doc.profilePictureUrl
    || doc.photoUrl
    || `https://ui-avatars.com/api/?name=${encodeURIComponent(name)}&background=E8EDFF&color=2B4AFE&size=200&bold=true&format=png`;

  return (
    <div
      className="bg-white rounded-2xl overflow-hidden shadow-md border group hover:-translate-y-2 transition-all duration-300 hover:shadow-xl"
      style={{ borderColor: BORDER }}
    >
      <div className="relative overflow-hidden" style={{ background: BG }}>
        <img
          src={avatarUrl}
          alt={name}
          loading="lazy"
          className="w-full h-56 object-cover object-top group-hover:scale-105 transition-transform duration-500"
          onError={(e) => {
            e.target.src = `https://ui-avatars.com/api/?name=${encodeURIComponent(name)}&background=E8EDFF&color=2B4AFE&size=200&bold=true`;
          }}
        />
        <div
          className="absolute bottom-0 left-0 right-0 h-16 bg-gradient-to-t from-white to-transparent"
        />
      </div>
      <div className="p-5">
        <h3 className="font-bold text-gray-900 text-[15px] leading-tight mb-1">{name}</h3>
        <p className="text-xs font-semibold mb-3" style={{ color: BLUE }}>
          {doc.specialty || doc.specialization || 'General Practitioner'}
        </p>
        <div className="flex items-center gap-1 mb-4">
          {[...Array(5)].map((_, i) => (
            <Star key={i} className="w-3 h-3 fill-amber-400 text-amber-400"/>
          ))}
          <span className="text-[11px] text-gray-500 ml-1">5.0</span>
        </div>
        <Link
          to={getBookLink(doc.userId || doc.id)}
          className="block w-full text-center py-2.5 rounded-xl text-[12px] font-bold transition-all hover:brightness-110 active:scale-[0.98]"
          style={{ background: BG_LIGHT, color: BLUE }}
        >
          Book Appointment
        </Link>
      </div>
    </div>
  );
};

/* ════════════════════════════════════════════════════════════════════════════
   APPOINTMENT FORM
════════════════════════════════════════════════════════════════════════════ */
const AppointmentForm = ({ departments }) => {
  const [form, setForm] = useState({
    name: '', phone: '', email: '', department: '', date: '',
  });
  const [errors, setErrors] = useState({});

  const { mutate, isPending, isSuccess } = useBookAppointment();

  const validate = () => {
    const errs = {};
    if (!form.name.trim())        errs.name       = 'Name is required';
    if (!form.phone.trim())       errs.phone      = 'Phone is required';
    if (!/\S+@\S+\.\S+/.test(form.email)) errs.email = 'Valid email required';
    if (!form.department)         errs.department = 'Select a department';
    if (!form.date)               errs.date       = 'Select a date';
    return errs;
  };

  const handleChange = (e) => {
    setForm((p) => ({ ...p, [e.target.name]: e.target.value }));
    if (errors[e.target.name]) setErrors((p) => ({ ...p, [e.target.name]: '' }));
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    const errs = validate();
    if (Object.keys(errs).length) { setErrors(errs); return; }

    mutate(form, {
      onSuccess: () => {
        toast.success('Appointment request submitted! We will confirm shortly.', { duration: 5000 });
        setForm({ name: '', phone: '', email: '', department: '', date: '' });
      },
      onError: (err) => {
        const msg = err?.response?.data?.message || 'Failed to book. Please try again.';
        toast.error(msg);
      },
    });
  };

  if (isSuccess) return (
    <div className="bg-white rounded-2xl p-8 shadow-xl border" style={{ borderColor: BORDER }}>
      <div className="text-center py-8">
        <CheckCircle2 className="w-16 h-16 mx-auto mb-4" style={{ color: BLUE }}/>
        <h3 className="text-xl font-bold text-gray-900 mb-2">Request Submitted!</h3>
        <p className="text-sm text-gray-500">Our team will contact you within 24 hours to confirm your appointment.</p>
        <button
          onClick={() => setForm({ name: '', phone: '', email: '', department: '', date: '' })}
          className="mt-6 text-sm font-semibold hover:underline"
          style={{ color: BLUE }}
        >Book Another →</button>
      </div>
    </div>
  );

  const Field = ({ name, placeholder, type = 'text', Icon }) => (
    <div className="relative">
      <input
        type={type}
        name={name}
        value={form[name]}
        onChange={handleChange}
        placeholder={placeholder}
        className={`w-full border-b py-3 pr-8 text-sm bg-transparent outline-none transition-colors placeholder-gray-400
          ${errors[name] ? 'border-red-400 text-red-600' : 'border-gray-200 focus:border-[#2B4AFE]'}`}
      />
      {Icon && <Icon className="w-4 h-4 absolute right-0 top-3.5" style={{ color: errors[name] ? '#ef4444' : BLUE }}/>}
      {errors[name] && <p className="text-[10px] text-red-500 mt-0.5">{errors[name]}</p>}
    </div>
  );

  return (
    <div className="bg-white rounded-2xl p-8 shadow-xl border" style={{ borderColor: BORDER }}>
      <h3 className="text-lg font-bold text-gray-900 mb-6">Book Appointment</h3>
      <form onSubmit={handleSubmit} className="space-y-4" noValidate>
        <Field name="name" placeholder="Your Name" Icon={User}/>
        <Field name="phone" placeholder="Phone Number" type="tel" Icon={Phone}/>
        <Field name="email" placeholder="Your Email" type="email" Icon={Mail}/>

        {/* Department select */}
        <div className="relative">
          <select
            name="department"
            value={form.department}
            onChange={handleChange}
            className={`w-full border-b py-3 pr-8 text-sm bg-transparent outline-none appearance-none transition-colors
              ${errors.department ? 'border-red-400 text-red-600' : 'border-gray-200 focus:border-[#2B4AFE]'}
              ${!form.department ? 'text-gray-400' : 'text-gray-900'}`}
          >
            <option value="">Select Department</option>
            {(departments?.length ? departments : DEPARTMENTS_FALLBACK).map((d) => (
              <option key={typeof d === 'string' ? d : d.id} value={typeof d === 'string' ? d : d.name}>
                {typeof d === 'string' ? d : d.name}
              </option>
            ))}
          </select>
          <ChevronDown className="w-4 h-4 absolute right-0 top-3.5 pointer-events-none" style={{ color: errors.department ? '#ef4444' : BLUE }}/>
          {errors.department && <p className="text-[10px] text-red-500 mt-0.5">{errors.department}</p>}
        </div>

        {/* Date */}
        <div className="relative">
          <input
            type="date"
            name="date"
            value={form.date}
            onChange={handleChange}
            min={new Date().toISOString().split('T')[0]}
            className={`w-full border-b py-3 pr-8 text-sm bg-transparent outline-none transition-colors
              ${errors.date ? 'border-red-400 text-red-600' : 'border-gray-200 focus:border-[#2B4AFE]'}
              ${!form.date ? 'text-gray-400' : 'text-gray-900'}`}
          />
          <Calendar className="w-4 h-4 absolute right-0 top-3.5 pointer-events-none" style={{ color: errors.date ? '#ef4444' : BLUE }}/>
          {errors.date && <p className="text-[10px] text-red-500 mt-0.5">{errors.date}</p>}
        </div>

        <button
          type="submit"
          disabled={isPending}
          className="w-full text-white py-3.5 rounded-xl text-[13px] font-bold mt-2 transition-all hover:brightness-110 active:scale-[0.98] flex items-center justify-center gap-2 shadow-lg"
          style={{ background: BLUE, boxShadow: `0 6px 24px ${BLUE}40` }}
        >
          {isPending ? <><Loader2 className="w-4 h-4 animate-spin"/> Submitting…</> : 'Book Appointment'}
        </button>
      </form>
    </div>
  );
};

/* ════════════════════════════════════════════════════════════════════════════
   MAIN HOME COMPONENT
════════════════════════════════════════════════════════════════════════════ */
const Home = () => {
  useScrollReveal();

  const [mobileMenuOpen,  setMobileMenuOpen]  = useState(false);
  const [activeSection,   setActiveSection]   = useState('hero');
  const [scrolled,        setScrolled]        = useState(false);
  const [activeTestimonial, setActiveTestimonial] = useState(0);

  const navigate = useNavigate();

  /* API data */
  const { data: doctors,     isLoading: loadingDoctors }  = usePublicDoctors();
  const { data: departments, isLoading: loadingDepts }    = usePublicDepartments();
  const { data: stats }                                   = useClinicStats();

  /* Scroll effects */
  useEffect(() => {
    const onScroll = () => {
      setScrolled(window.scrollY > 20);
      const sectionIds = ['hero', 'about', 'services', 'doctors', 'blog', 'contact'];
      for (const id of [...sectionIds].reverse()) {
        const el = document.getElementById(id);
        if (el && window.scrollY >= el.offsetTop - 120) {
          setActiveSection(id);
          break;
        }
      }
    };
    window.addEventListener('scroll', onScroll, { passive: true });
    return () => window.removeEventListener('scroll', onScroll);
  }, []);

  /* Testimonial auto-rotate */
  useEffect(() => {
    const t = setInterval(() => setActiveTestimonial((p) => (p + 1) % TESTIMONIALS.length), 5000);
    return () => clearInterval(t);
  }, []);

  const scrollTo = useCallback((id) => {
    const el = document.getElementById(id.replace('#', ''));
    if (el) el.scrollIntoView({ behavior: 'smooth', block: 'start' });
    setMobileMenuOpen(false);
  }, []);

  const getBookLink = (docId) => {
    const token = window.__CLINIC_TOKEN__;
    if (!token) return '/register';
    return `/patient/book/${docId}`;
  };

  const displayedDoctors = (doctors || []).slice(0, 4);
  const deptList         = departments || [];

  /* ── render ──────────────────────────────────────────────────────────── */
  return (
    <>
      {/* ── Scroll reveal styles ─────────────────────────────────────────── */}
      <style>{`
        @import url('https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700;800;900&display=swap');
        html { scroll-behavior: smooth; }
        [data-reveal] {
          opacity: 0;
          transform: translateY(32px);
          transition: opacity 0.7s cubic-bezier(.22,.61,.36,1), transform 0.7s cubic-bezier(.22,.61,.36,1);
        }
        [data-reveal].revealed { opacity: 1; transform: translateY(0); }
        [data-reveal-delay="1"] { transition-delay: 0.1s; }
        [data-reveal-delay="2"] { transition-delay: 0.2s; }
        [data-reveal-delay="3"] { transition-delay: 0.3s; }
        [data-reveal-delay="4"] { transition-delay: 0.4s; }
        [data-reveal-delay="5"] { transition-delay: 0.5s; }
        .btn-primary {
          display:inline-flex; align-items:center; gap:8px;
          background:${BLUE}; color:#fff; font-weight:700; font-size:14px;
          padding: 14px 28px; border-radius:10px;
          box-shadow: 0 6px 24px ${BLUE}40;
          transition: filter 0.2s, transform 0.15s;
          border: none; cursor: pointer; text-decoration:none;
        }
        .btn-primary:hover { filter:brightness(1.12); transform:translateY(-1px); }
        .btn-primary:active { transform:scale(0.97); }
        .btn-outline {
          display:inline-flex; align-items:center; gap:8px;
          background:transparent; color:${BLUE}; font-weight:700; font-size:14px;
          padding: 13px 28px; border-radius:10px; border: 2px solid ${BLUE};
          transition: background 0.2s, transform 0.15s;
          cursor:pointer; text-decoration:none;
        }
        .btn-outline:hover { background:${BG_LIGHT}; transform:translateY(-1px); }
        .dot-pattern {
          background-image: radial-gradient(circle, ${BLUE}30 1.5px, transparent 1.5px);
          background-size: 16px 16px;
        }
        .float-anim { animation: floatY 3.5s ease-in-out infinite; }
        .float-anim-slow { animation: floatY 5s ease-in-out infinite; }
        .float-anim-rev { animation: floatY 4s ease-in-out infinite reverse; }
        @keyframes floatY {
          0%,100% { transform: translateY(0); }
          50% { transform: translateY(-12px); }
        }
        .service-card:hover .service-icon { background: ${BLUE}; color: #fff; }
        .service-card:hover { box-shadow: 0 20px 60px ${BLUE}20; border-color: ${BLUE}30; }
        .hero-arch {
          border-radius: 50% 50% 0 0 / 60% 60% 0 0;
        }
      `}</style>

      <div className="min-h-screen overflow-x-hidden" style={{ background: BG, fontFamily: "'Inter', sans-serif", color: DARK }}>

        {/* ══════════════════════════════════════════════════════════════════
            NAVBAR
        ══════════════════════════════════════════════════════════════════ */}
        <header
          className="sticky top-0 z-50 transition-all duration-300"
          style={{
            background: scrolled ? 'rgba(244,246,255,0.92)' : BG,
            backdropFilter: scrolled ? 'blur(16px)' : 'none',
            boxShadow: scrolled ? '0 2px 32px rgba(43,74,254,0.10)' : 'none',
            borderBottom: scrolled ? `1px solid ${BORDER}` : 'none',
          }}
        >
          <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-16 h-[72px] flex items-center justify-between">

            {/* Logo */}
            <Link to="/" onClick={() => scrollTo('hero')}>
              <MedviceLogo/>
            </Link>

            {/* Desktop nav */}
            <nav className="hidden lg:flex items-center gap-7">
              {NAV_LINKS.map(({ label, href }) => (
                <button
                  key={label}
                  onClick={() => scrollTo(href)}
                  className="text-[13.5px] font-semibold transition-colors relative pb-1"
                  style={{
                    color: activeSection === href.replace('#', '') ? BLUE : MUTED,
                    background: 'none', border: 'none', cursor: 'pointer',
                  }}
                >
                  {label}
                  {activeSection === href.replace('#', '') && (
                    <span
                      className="absolute bottom-0 left-0 right-0 h-0.5 rounded-full"
                      style={{ background: BLUE }}
                    />
                  )}
                </button>
              ))}
            </nav>

            {/* Right actions */}
            <div className="hidden sm:flex items-center gap-5">
              <div className="flex items-center gap-2.5">
                <div
                  className="w-9 h-9 rounded-full flex items-center justify-center flex-shrink-0"
                  style={{ background: BG_LIGHT }}
                >
                  <PhoneCall className="w-4 h-4" style={{ color: BLUE }}/>
                </div>
                <div>
                  <p className="text-[10px] font-semibold" style={{ color: MUTED }}>Call Now</p>
                  <p className="text-[13px] font-black leading-tight" style={{ color: DARK }}>123 456 7890</p>
                </div>
              </div>

              <button
                className="btn-primary"
                onClick={() => scrollTo('about')}
                aria-label="Make Appointment"
              >
                Make Appointment
              </button>
            </div>

            {/* Mobile hamburger */}
            <button
              className="lg:hidden p-2 rounded-lg"
              onClick={() => setMobileMenuOpen(!mobileMenuOpen)}
              aria-label="Toggle menu"
              style={{ background: BG_LIGHT }}
            >
              {mobileMenuOpen ? <X className="w-5 h-5" style={{ color: BLUE }}/> : <Menu className="w-5 h-5" style={{ color: BLUE }}/>}
            </button>
          </div>

          {/* Mobile menu */}
          {mobileMenuOpen && (
            <div className="lg:hidden border-t px-4 py-4 flex flex-col gap-3" style={{ background: WHITE, borderColor: BORDER }}>
              {NAV_LINKS.map(({ label, href }) => (
                <button
                  key={label}
                  onClick={() => scrollTo(href)}
                  className="text-left text-[14px] font-semibold py-2 px-3 rounded-lg transition-colors"
                  style={{
                    background: activeSection === href.replace('#', '') ? BG_LIGHT : 'transparent',
                    color: activeSection === href.replace('#', '') ? BLUE : DARK,
                  }}
                >
                  {label}
                </button>
              ))}
              <button
                className="btn-primary justify-center mt-2"
                onClick={() => { scrollTo('about'); setMobileMenuOpen(false); }}
              >
                Make Appointment
              </button>
            </div>
          )}
        </header>

        {/* ══════════════════════════════════════════════════════════════════
            HERO
        ══════════════════════════════════════════════════════════════════ */}
        <section
          id="hero"
          className="relative overflow-hidden px-4 sm:px-6 lg:px-16 pt-12 lg:pt-20 pb-24 lg:pb-32"
          style={{ background: BG }}
        >
          {/* Decorative dot grid top-left */}
          <div
            className="absolute top-8 left-0 w-48 h-48 dot-pattern opacity-40 pointer-events-none"
            aria-hidden="true"
          />

          <div className="max-w-7xl mx-auto flex flex-col lg:flex-row items-center gap-12 lg:gap-0">

            {/* Left column */}
            <div className="lg:w-1/2 z-10" data-reveal>
              {/* Badge */}
              <div
                className="inline-flex items-center gap-2 px-4 py-2 rounded-full text-[12px] font-bold mb-6"
                style={{ background: BG_LIGHT, color: BLUE, border: `1px solid ${BORDER}` }}
              >
                <span className="w-2 h-2 rounded-full bg-green-400 animate-pulse"/>
                We Care For Your Health
              </div>

              <h1
                className="font-black leading-[1.08] mb-5"
                style={{ fontSize: 'clamp(38px, 5.5vw, 68px)', color: DARK }}
              >
                We Provide Best<br/>
                <span style={{ color: BLUE }}>Health Care</span>
              </h1>

              <p className="text-[15px] leading-relaxed mb-8 max-w-[440px]" style={{ color: MUTED }}>
                Delivering compassionate, world-class healthcare with advanced technology,
                experienced specialists, and a patient-first approach to every treatment.
              </p>

              <div className="flex flex-wrap gap-3 mb-12">
                <button className="btn-primary" onClick={() => scrollTo('services')}>
                  Our Services <ArrowRight className="w-4 h-4"/>
                </button>
                <button className="btn-outline" onClick={() => scrollTo('about')}>
                  Make Appointment
                </button>
              </div>

              {/* Mini feature highlights */}
              <div className="flex gap-8">
                {[
                  { icon: ShieldCheck, label: 'Advanced\nTechnology' },
                  { icon: Users,       label: 'Expert\nDoctors' },
                  { icon: Building2,   label: 'Modern\nFacility' },
                ].map(({ icon: Icon, label }) => (
                  <div key={label} className="flex flex-col items-center text-center">
                    <div
                      className="w-12 h-12 rounded-2xl flex items-center justify-center mb-2 shadow-sm"
                      style={{ background: WHITE }}
                    >
                      <Icon className="w-5 h-5" style={{ color: BLUE }}/>
                    </div>
                    <p className="text-[11px] font-bold whitespace-pre-line leading-tight" style={{ color: DARK }}>
                      {label}
                    </p>
                  </div>
                ))}
              </div>
            </div>

            {/* Right column – doctor + arch */}
            <div className="lg:w-1/2 relative flex justify-end items-end mt-8 lg:mt-0" data-reveal data-reveal-delay="2">
              {/* Blue arched background */}
              <div
                className="absolute bottom-0 right-4 lg:right-8 hero-arch z-0"
                style={{
                  width: '72%',
                  height: '88%',
                  background: `linear-gradient(160deg, ${BLUE} 0%, ${BLUE_D} 100%)`,
                }}
                aria-hidden="true"
              >
                {/* Dot decoration inside arch */}
                <div
                  className="absolute top-6 right-6 w-32 h-32 dot-pattern opacity-20"
                  style={{ backgroundImage: `radial-gradient(circle, rgba(255,255,255,0.4) 1.5px, transparent 1.5px)` }}
                  aria-hidden="true"
                />
              </div>

              {/* Doctor image */}
              <img
                src="https://images.unsplash.com/photo-1612349317150-e413f6a5b16d?q=80&w=720&auto=format&fit=crop&crop=top"
                alt="Expert doctor"
                loading="lazy"
                className="relative z-10 object-cover object-top"
                style={{
                  width: '68%',
                  aspectRatio: '3/4',
                  maskImage: 'linear-gradient(to top, transparent 0%, black 15%)',
                  WebkitMaskImage: 'linear-gradient(to top, transparent 0%, black 15%)',
                }}
              />

              {/* Floating Emergency Card */}
              <div
                className="absolute top-[28%] right-0 lg:-right-4 z-20 rounded-2xl p-5 text-center w-44 float-anim shadow-2xl"
                style={{ background: WHITE, boxShadow: '0 16px 60px rgba(43,74,254,0.18)' }}
              >
                <div
                  className="w-14 h-14 rounded-full flex items-center justify-center mx-auto mb-3 shadow-lg"
                  style={{ background: BLUE, boxShadow: `0 8px 24px ${BLUE}50` }}
                >
                  <PhoneCall className="w-6 h-6 text-white" fill="currentColor"/>
                </div>
                <p className="text-[11px] font-bold mb-1" style={{ color: MUTED }}>Emergency Case</p>
                <p className="text-3xl font-black leading-none mb-1" style={{ color: BLUE }}>24/7</p>
                <p className="text-[12px] font-bold text-gray-700">Service</p>
              </div>
            </div>
          </div>
        </section>

        {/* ══════════════════════════════════════════════════════════════════
            ABOUT US + APPOINTMENT FORM
        ══════════════════════════════════════════════════════════════════ */}
        <section id="about" className="py-20 lg:py-28 px-4 sm:px-6 lg:px-16" style={{ background: WHITE }}>
          <div className="max-w-7xl mx-auto flex flex-col lg:flex-row gap-10 xl:gap-16 items-start">

            {/* Left – image collage */}
            <div className="lg:w-[28%] flex-shrink-0" data-reveal>
              <div className="grid grid-cols-2 gap-3">
                <img
                  src="https://images.unsplash.com/photo-1576091160550-2173dba999ef?q=80&w=500&auto=format&fit=crop"
                  alt="Medical team"
                  loading="lazy"
                  className="rounded-2xl w-full h-48 object-cover"
                />
                <div
                  className="rounded-2xl flex flex-col items-center justify-center text-white h-48 p-4 text-center shadow-xl"
                  style={{ background: `linear-gradient(135deg, ${BLUE} 0%, ${BLUE_D} 100%)`, boxShadow: `0 12px 40px ${BLUE}40` }}
                >
                  <p className="text-4xl font-black leading-none mb-1">25+</p>
                  <p className="text-[11px] font-semibold opacity-90">Years of<br/>Experience</p>
                </div>
                <img
                  src="https://images.unsplash.com/photo-1519494026892-80bbd2d6fd0d?q=80&w=600&auto=format&fit=crop"
                  alt="Hospital building"
                  loading="lazy"
                  className="rounded-2xl w-full h-40 object-cover col-span-2"
                />
              </div>
            </div>

            {/* Middle – about text */}
            <div className="lg:w-[37%]" data-reveal data-reveal-delay="2">
              <p className="text-[12px] font-black uppercase tracking-widest mb-3" style={{ color: BLUE }}>About Us</p>
              <h2 className="text-[clamp(26px,3.2vw,38px)] font-black leading-tight mb-5" style={{ color: DARK }}>
                We Provide Total<br/>
                <span style={{ color: BLUE }}>Health Care</span> Solution
              </h2>
              <p className="text-[14px] leading-relaxed mb-8" style={{ color: MUTED }}>
                For over 25 years, Medvice Medical Clinic has been committed to delivering exceptional healthcare with
                compassion, precision, and cutting-edge technology. Our multidisciplinary team of specialists ensures
                every patient receives personalized, evidence-based treatment.
              </p>

              {/* Stats */}
              <div className="grid grid-cols-3 gap-4 mb-8">
                <StatCounter
                  value={stats?.happyPatients || 15000}
                  suffix="+"
                  label="Happy Patients"
                  icon={Users}
                />
                <StatCounter
                  value={stats?.expertDoctors || 25}
                  suffix="+"
                  label="Expert Doctors"
                  icon={Stethoscope}
                />
                <StatCounter
                  value={stats?.successfulSurgeries || 12000}
                  suffix="+"
                  label="Successful Surgeries"
                  icon={Building2}
                />
              </div>

              <button
                className="btn-primary"
                onClick={() => navigate('/doctors')}
              >
                Learn More <ArrowRight className="w-4 h-4"/>
              </button>
            </div>

            {/* Right – appointment form */}
            <div className="lg:w-[35%] w-full" data-reveal data-reveal-delay="3">
              <AppointmentForm departments={deptList}/>
            </div>
          </div>
        </section>

        {/* ══════════════════════════════════════════════════════════════════
            SERVICES
        ══════════════════════════════════════════════════════════════════ */}
        <section id="services" className="py-20 lg:py-28 px-4 sm:px-6 lg:px-16" style={{ background: BG }}>
          <div className="max-w-7xl mx-auto">

            {/* Header */}
            <div className="flex flex-col md:flex-row justify-between items-start md:items-end gap-6 mb-14" data-reveal>
              <div>
                <p className="text-[12px] font-black uppercase tracking-widest mb-3" style={{ color: BLUE }}>Our Services</p>
                <h2 className="text-[clamp(26px,3.2vw,38px)] font-black leading-tight" style={{ color: DARK }}>
                  We Provide Best<br/>
                  <span style={{ color: BLUE }}>Services</span> For You
                </h2>
              </div>
              <div className="flex flex-col md:flex-row items-start md:items-center gap-5 md:max-w-xs">
                <p className="text-[13px] leading-relaxed" style={{ color: MUTED }}>
                  Comprehensive medical services delivered with the highest standards of care across all specialties.
                </p>
                <button
                  className="btn-primary flex-shrink-0"
                  onClick={() => navigate('/doctors')}
                >
                  View All <ArrowRight className="w-4 h-4"/>
                </button>
              </div>
            </div>

            {/* Service cards */}
            <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-5 gap-5">
              {SERVICES.map((srv, i) => (
                <div
                  key={srv.title}
                  className="service-card bg-white rounded-2xl p-6 border flex flex-col cursor-pointer transition-all duration-300 group"
                  style={{ borderColor: BORDER }}
                  data-reveal
                  data-reveal-delay={String(i + 1)}
                  onClick={() => scrollTo('about')}
                  role="button"
                  tabIndex={0}
                  onKeyDown={(e) => e.key === 'Enter' && scrollTo('about')}
                >
                  <div
                    className="service-icon w-14 h-14 rounded-2xl flex items-center justify-center mb-4 transition-all duration-300"
                    style={{ background: `${srv.color}18`, color: srv.color }}
                  >
                    <srv.icon className="w-6 h-6"/>
                  </div>
                  <h3 className="font-black text-[15px] text-gray-900 mb-2">{srv.title}</h3>
                  <p className="text-[12px] leading-relaxed flex-1 mb-4" style={{ color: MUTED }}>{srv.desc}</p>
                  <button
                    className="flex items-center gap-1 text-[12px] font-bold group-hover:gap-2 transition-all"
                    style={{ color: BLUE, background: 'none', border: 'none', cursor: 'pointer', padding: 0 }}
                  >
                    Learn More <ArrowRight className="w-3 h-3"/>
                  </button>
                </div>
              ))}
            </div>
          </div>
        </section>

        {/* ══════════════════════════════════════════════════════════════════
            WHY CHOOSE US
        ══════════════════════════════════════════════════════════════════ */}
        <section className="py-20 lg:py-28 px-4 sm:px-6 lg:px-16" style={{ background: WHITE }}>
          <div className="max-w-7xl mx-auto">
            <div className="text-center mb-14" data-reveal>
              <p className="text-[12px] font-black uppercase tracking-widest mb-3" style={{ color: BLUE }}>Why Choose Us</p>
              <h2 className="text-[clamp(26px,3.2vw,38px)] font-black leading-tight" style={{ color: DARK }}>
                We Care for<br/>
                <span style={{ color: BLUE }}>Your Health</span>
              </h2>
            </div>

            <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-6">
              {WHY_US.map((item, i) => (
                <div
                  key={item.title}
                  className="text-center p-7 rounded-2xl border group hover:-translate-y-1 transition-all duration-300 hover:shadow-lg cursor-default"
                  style={{ borderColor: BORDER }}
                  data-reveal
                  data-reveal-delay={String(i + 1)}
                >
                  <div
                    className="w-16 h-16 rounded-2xl mx-auto flex items-center justify-center mb-5 group-hover:scale-110 transition-transform duration-300"
                    style={{ background: BG_LIGHT }}
                  >
                    <item.icon className="w-7 h-7" style={{ color: BLUE }}/>
                  </div>
                  <h3 className="font-black text-[15px] mb-2" style={{ color: DARK }}>{item.title}</h3>
                  <p className="text-[13px] leading-relaxed" style={{ color: MUTED }}>{item.desc}</p>
                </div>
              ))}
            </div>
          </div>
        </section>

        {/* ══════════════════════════════════════════════════════════════════
            EMERGENCY BANNER
        ══════════════════════════════════════════════════════════════════ */}
        <section
          className="py-14 px-4 sm:px-6 lg:px-16 relative overflow-hidden"
          style={{ background: `linear-gradient(120deg, ${BLUE} 0%, ${BLUE_D} 100%)` }}
        >
          <div
            className="absolute right-0 top-0 bottom-0 w-64 dot-pattern opacity-10"
            style={{ backgroundImage: 'radial-gradient(circle, rgba(255,255,255,0.5) 1.5px, transparent 1.5px)' }}
            aria-hidden="true"
          />
          <div className="max-w-7xl mx-auto flex flex-col md:flex-row items-center justify-between gap-6">
            <div className="flex items-center gap-5">
              <div className="w-16 h-16 rounded-2xl flex items-center justify-center flex-shrink-0 bg-white/20">
                <Ambulance className="w-8 h-8 text-white"/>
              </div>
              <div>
                <h2 className="text-[22px] font-black text-white leading-tight">
                  Do You Have Any Emergency?
                </h2>
                <p className="text-white/80 text-[13px] font-medium">We're Available 24/7</p>
              </div>
            </div>
            <div className="flex items-center gap-4">
              <div className="flex items-center gap-3">
                <div className="w-10 h-10 rounded-full bg-white/20 flex items-center justify-center">
                  <PhoneCall className="w-5 h-5 text-white"/>
                </div>
                <div>
                  <p className="text-white/70 text-[11px] font-semibold">Call Now</p>
                  <p className="text-white text-lg font-black">123 456 7890</p>
                </div>
              </div>
              <a
                href="tel:+11234567890"
                className="bg-white rounded-xl px-6 py-3 text-[13px] font-black transition-all hover:scale-105"
                style={{ color: BLUE }}
              >
                Call Now
              </a>
            </div>
          </div>
        </section>

        {/* ══════════════════════════════════════════════════════════════════
            DOCTORS
        ══════════════════════════════════════════════════════════════════ */}
        <section id="doctors" className="py-20 lg:py-28 px-4 sm:px-6 lg:px-16" style={{ background: BG }}>
          <div className="max-w-7xl mx-auto">
            <div className="flex flex-col sm:flex-row justify-between items-start sm:items-end gap-5 mb-14" data-reveal>
              <div>
                <p className="text-[12px] font-black uppercase tracking-widest mb-3" style={{ color: BLUE }}>Our Team</p>
                <h2 className="text-[clamp(26px,3.2vw,38px)] font-black leading-tight" style={{ color: DARK }}>
                  Meet Our Expert<br/>
                  <span style={{ color: BLUE }}>Doctors</span>
                </h2>
              </div>
              <button
                className="btn-outline flex-shrink-0"
                onClick={() => navigate('/doctors')}
              >
                View All Doctors <ArrowRight className="w-4 h-4"/>
              </button>
            </div>

            {loadingDoctors ? (
              <div className="flex items-center justify-center h-48">
                <Loader2 className="w-8 h-8 animate-spin" style={{ color: BLUE }}/>
              </div>
            ) : displayedDoctors.length > 0 ? (
              <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-6">
                {displayedDoctors.map((doc, i) => (
                  <div key={doc.userId || i} data-reveal data-reveal-delay={String(i + 1)}>
                    <DoctorCard doc={doc} getBookLink={getBookLink}/>
                  </div>
                ))}
              </div>
            ) : (
              /* Skeleton cards when no backend data */
              <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-6">
                {[
                  { name: 'Dr. James Wilson',   specialty: 'Cardiologist',      img: 'https://images.unsplash.com/photo-1537368910025-700350fe46c7?q=80&w=400&auto=format&fit=crop' },
                  { name: 'Dr. Sarah Mitchell', specialty: 'Neurologist',       img: 'https://images.unsplash.com/photo-1559839734-2b71ea197ec2?q=80&w=400&auto=format&fit=crop' },
                  { name: 'Dr. Michael Park',   specialty: 'Orthopedic Surgeon',img: 'https://images.unsplash.com/photo-1622253692010-333f2da6031d?q=80&w=400&auto=format&fit=crop' },
                  { name: 'Dr. Emily Torres',   specialty: 'Pulmonologist',     img: 'https://images.unsplash.com/photo-1594824476967-48c8b964273f?q=80&w=400&auto=format&fit=crop' },
                ].map((doc, i) => (
                  <div
                    key={doc.name}
                    className="bg-white rounded-2xl overflow-hidden shadow-md border group hover:-translate-y-2 transition-all duration-300 hover:shadow-xl"
                    style={{ borderColor: BORDER }}
                    data-reveal
                    data-reveal-delay={String(i + 1)}
                  >
                    <div className="relative overflow-hidden" style={{ background: BG }}>
                      <img
                        src={doc.img}
                        alt={doc.name}
                        loading="lazy"
                        className="w-full h-56 object-cover object-top group-hover:scale-105 transition-transform duration-500"
                      />
                    </div>
                    <div className="p-5">
                      <h3 className="font-bold text-gray-900 text-[15px] leading-tight mb-1">{doc.name}</h3>
                      <p className="text-xs font-semibold mb-3" style={{ color: BLUE }}>{doc.specialty}</p>
                      <div className="flex items-center gap-1 mb-4">
                        {[...Array(5)].map((_, j) => (
                          <Star key={j} className="w-3 h-3 fill-amber-400 text-amber-400"/>
                        ))}
                        <span className="text-[11px] text-gray-500 ml-1">5.0</span>
                      </div>
                      <button
                        className="block w-full text-center py-2.5 rounded-xl text-[12px] font-bold transition-all hover:brightness-110"
                        style={{ background: BG_LIGHT, color: BLUE }}
                        onClick={() => scrollTo('about')}
                      >
                        Book Appointment
                      </button>
                    </div>
                  </div>
                ))}
              </div>
            )}
          </div>
        </section>

        {/* ══════════════════════════════════════════════════════════════════
            TESTIMONIALS
        ══════════════════════════════════════════════════════════════════ */}
        <section className="py-20 lg:py-28 px-4 sm:px-6 lg:px-16" style={{ background: WHITE }}>
          <div className="max-w-7xl mx-auto">
            <div className="text-center mb-14" data-reveal>
              <p className="text-[12px] font-black uppercase tracking-widest mb-3" style={{ color: BLUE }}>Testimonials</p>
              <h2 className="text-[clamp(26px,3.2vw,38px)] font-black leading-tight" style={{ color: DARK }}>
                What Our Patients<br/>
                <span style={{ color: BLUE }}>Say About Us</span>
              </h2>
            </div>

            <div className="grid grid-cols-1 md:grid-cols-3 gap-6" data-reveal data-reveal-delay="1">
              {TESTIMONIALS.map((t, i) => (
                <div
                  key={t.name}
                  className={`rounded-2xl p-7 border transition-all duration-300 ${activeTestimonial === i ? 'shadow-xl scale-[1.02]' : 'shadow-sm'}`}
                  style={{
                    borderColor: activeTestimonial === i ? BLUE : BORDER,
                    background: activeTestimonial === i ? BG_LIGHT : WHITE,
                  }}
                >
                  <Quote className="w-8 h-8 mb-4 opacity-30" style={{ color: BLUE }}/>
                  <p className="text-[13px] leading-relaxed mb-6" style={{ color: MUTED }}>{t.text}</p>
                  <div className="flex items-center gap-3">
                    <img src={t.avatar} alt={t.name} className="w-10 h-10 rounded-full object-cover" loading="lazy"/>
                    <div>
                      <p className="font-bold text-[13px]" style={{ color: DARK }}>{t.name}</p>
                      <div className="flex items-center gap-1">
                        {[...Array(t.rating)].map((_, j) => (
                          <Star key={j} className="w-3 h-3 fill-amber-400 text-amber-400"/>
                        ))}
                      </div>
                    </div>
                  </div>
                </div>
              ))}
            </div>

            {/* Dots */}
            <div className="flex justify-center gap-2 mt-8">
              {TESTIMONIALS.map((_, i) => (
                <button
                  key={i}
                  onClick={() => setActiveTestimonial(i)}
                  className="rounded-full transition-all duration-300"
                  style={{
                    width: activeTestimonial === i ? 24 : 8,
                    height: 8,
                    background: activeTestimonial === i ? BLUE : BORDER,
                    border: 'none', cursor: 'pointer',
                  }}
                  aria-label={`Go to testimonial ${i + 1}`}
                />
              ))}
            </div>
          </div>
        </section>

        {/* ══════════════════════════════════════════════════════════════════
            BLOG
        ══════════════════════════════════════════════════════════════════ */}
        <section id="blog" className="py-20 lg:py-28 px-4 sm:px-6 lg:px-16" style={{ background: BG }}>
          <div className="max-w-7xl mx-auto">
            <div className="flex flex-col sm:flex-row justify-between items-start sm:items-end gap-5 mb-14" data-reveal>
              <div>
                <p className="text-[12px] font-black uppercase tracking-widest mb-3" style={{ color: BLUE }}>Latest News</p>
                <h2 className="text-[clamp(26px,3.2vw,38px)] font-black leading-tight" style={{ color: DARK }}>
                  Health Tips &amp;<br/>
                  <span style={{ color: BLUE }}>Medical News</span>
                </h2>
              </div>
              <button className="btn-outline flex-shrink-0">
                View All Posts <ArrowRight className="w-4 h-4"/>
              </button>
            </div>

            <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
              {BLOG_POSTS.map((post, i) => (
                <article
                  key={post.title}
                  className="bg-white rounded-2xl overflow-hidden border group hover:-translate-y-1 transition-all duration-300 hover:shadow-xl cursor-pointer"
                  style={{ borderColor: BORDER }}
                  data-reveal
                  data-reveal-delay={String(i + 1)}
                >
                  <div className="overflow-hidden">
                    <img
                      src={post.img}
                      alt={post.title}
                      loading="lazy"
                      className="w-full h-48 object-cover group-hover:scale-105 transition-transform duration-500"
                    />
                  </div>
                  <div className="p-6">
                    <div className="flex items-center gap-3 mb-3">
                      <span
                        className="px-3 py-1 rounded-full text-[11px] font-bold"
                        style={{ background: BG_LIGHT, color: BLUE }}
                      >
                        {post.tag}
                      </span>
                      <span className="text-[11px]" style={{ color: MUTED }}>{post.date}</span>
                    </div>
                    <h3 className="font-black text-[15px] leading-snug mb-3 group-hover:text-blue-600 transition-colors" style={{ color: DARK }}>
                      {post.title}
                    </h3>
                    <p className="text-[12px] leading-relaxed mb-4" style={{ color: MUTED }}>{post.excerpt}</p>
                    <button
                      className="flex items-center gap-1 text-[12px] font-bold group-hover:gap-2 transition-all"
                      style={{ color: BLUE, background: 'none', border: 'none', cursor: 'pointer', padding: 0 }}
                    >
                      Read More <ArrowRight className="w-3 h-3"/>
                    </button>
                  </div>
                </article>
              ))}
            </div>
          </div>
        </section>

        {/* ══════════════════════════════════════════════════════════════════
            CONTACT CTA
        ══════════════════════════════════════════════════════════════════ */}
        <section
          id="contact"
          className="py-20 lg:py-28 px-4 sm:px-6 lg:px-16 relative overflow-hidden"
          style={{ background: WHITE }}
        >
          <div className="max-w-7xl mx-auto">
            <div
              className="rounded-3xl p-10 lg:p-16 flex flex-col lg:flex-row items-center justify-between gap-10 relative overflow-hidden"
              style={{ background: `linear-gradient(120deg, ${BLUE} 0%, ${BLUE_D} 100%)` }}
            >
              <div
                className="absolute right-0 top-0 bottom-0 w-80 dot-pattern opacity-10"
                style={{ backgroundImage: 'radial-gradient(circle, rgba(255,255,255,0.5) 1.5px, transparent 1.5px)' }}
                aria-hidden="true"
              />

              <div className="text-white max-w-xl" data-reveal>
                <h2 className="text-[clamp(22px,3vw,36px)] font-black leading-tight mb-4">
                  Ready to Take Control of Your Health?
                </h2>
                <p className="text-white/80 text-[14px] leading-relaxed">
                  Schedule your appointment today and experience world-class medical care from our team of expert specialists.
                </p>
              </div>

              <div className="flex flex-col sm:flex-row gap-3 flex-shrink-0" data-reveal data-reveal-delay="2">
                <button
                  className="bg-white font-black text-[14px] px-8 py-4 rounded-xl transition-all hover:scale-105"
                  style={{ color: BLUE }}
                  onClick={() => scrollTo('about')}
                >
                  Book Appointment
                </button>
                <a
                  href="tel:+11234567890"
                  className="flex items-center gap-2 border-2 border-white/50 text-white font-black text-[14px] px-8 py-4 rounded-xl transition-all hover:bg-white/10"
                >
                  <PhoneCall className="w-4 h-4"/> Call Now
                </a>
              </div>
            </div>
          </div>
        </section>

        {/* ══════════════════════════════════════════════════════════════════
            FOOTER
        ══════════════════════════════════════════════════════════════════ */}
        <footer style={{ background: DARK }} className="text-white px-4 sm:px-6 lg:px-16 pt-16 pb-8">
          <div className="max-w-7xl mx-auto">
            <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-10 mb-12">

              {/* Brand column */}
              <div>
                <div className="flex items-center gap-2.5 mb-5">
                  <div
                    className="w-10 h-10 rounded-xl flex items-center justify-center"
                    style={{ background: BLUE }}
                  >
                    <HeartPulse className="w-5 h-5 text-white"/>
                  </div>
                  <div>
                    <p className="font-black text-[17px] leading-none">Medvice</p>
                    <p className="text-[10px] font-medium leading-tight text-white/50">Medical Clinic</p>
                  </div>
                </div>
                <p className="text-[13px] text-white/60 leading-relaxed mb-5">
                  Delivering compassionate, world-class healthcare for over 25 years with advanced technology and experienced specialists.
                </p>
                <div className="flex items-start gap-2 text-white/60 text-[12px]">
                  <MapPin className="w-4 h-4 flex-shrink-0 mt-0.5"/>
                  <span>121 Street, New York,<br/>USA 466791</span>
                </div>
                <div className="flex gap-3 mt-5">
  {[
                    /* Facebook */
                    <svg viewBox="0 0 24 24" fill="currentColor" className="w-4 h-4 text-white/70"><path d="M18 2h-3a5 5 0 0 0-5 5v3H7v4h3v8h4v-8h3l1-4h-4V7a1 1 0 0 1 1-1h3z"/></svg>,
                    /* Twitter/X */
                    <svg viewBox="0 0 24 24" fill="currentColor" className="w-4 h-4 text-white/70"><path d="M18.244 2.25h3.308l-7.227 8.26 8.502 11.24H16.17l-5.214-6.817L4.99 21.75H1.68l7.73-8.835L1.254 2.25H8.08l4.713 6.231zm-1.161 17.52h1.833L7.084 4.126H5.117z"/></svg>,
                    /* Instagram */
                    <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" className="w-4 h-4 text-white/70"><rect x="2" y="2" width="20" height="20" rx="5" ry="5"/><path d="M16 11.37A4 4 0 1 1 12.63 8 4 4 0 0 1 16 11.37z"/><line x1="17.5" y1="6.5" x2="17.51" y2="6.5"/></svg>,
                    /* LinkedIn */
                    <svg viewBox="0 0 24 24" fill="currentColor" className="w-4 h-4 text-white/70"><path d="M16 8a6 6 0 0 1 6 6v7h-4v-7a2 2 0 0 0-2-2 2 2 0 0 0-2 2v7h-4v-7a6 6 0 0 1 6-6z"/><rect x="2" y="9" width="4" height="12"/><circle cx="4" cy="4" r="2"/></svg>,
                  ].map((icon, i) => (
                    <button
                      key={i}
                      className="w-8 h-8 rounded-lg flex items-center justify-center transition-colors hover:bg-white/20"
                      style={{ background: 'rgba(255,255,255,0.08)' }}
                      aria-label="Social media"
                    >
                      {icon}
                    </button>
                  ))}
                </div>
              </div>

              {/* Quick Links */}
              <div>
                <h4 className="font-black text-[14px] mb-5 text-white">Quick Links</h4>
                <ul className="space-y-3">
                  {['About Us', 'Services', 'Doctors', 'Blog', 'Contact Us'].map((l) => (
                    <li key={l}>
                      <button
                        className="text-[13px] text-white/60 hover:text-white transition-colors flex items-center gap-2"
                        style={{ background: 'none', border: 'none', cursor: 'pointer', padding: 0 }}
                        onClick={() => scrollTo(`#${l.toLowerCase().replace(' ', '')}`)}
                      >
                        <ChevronRight className="w-3 h-3"/> {l}
                      </button>
                    </li>
                  ))}
                </ul>
              </div>

              {/* Services */}
              <div>
                <h4 className="font-black text-[14px] mb-5 text-white">Our Services</h4>
                <ul className="space-y-3">
                  {SERVICES.map((s) => (
                    <li key={s.title}>
                      <button
                        className="text-[13px] text-white/60 hover:text-white transition-colors flex items-center gap-2"
                        style={{ background: 'none', border: 'none', cursor: 'pointer', padding: 0 }}
                        onClick={() => scrollTo('services')}
                      >
                        <ChevronRight className="w-3 h-3"/> {s.title}
                      </button>
                    </li>
                  ))}
                </ul>
              </div>

              {/* Contact Info */}
              <div>
                <h4 className="font-black text-[14px] mb-5 text-white">Contact Info</h4>
                <div className="space-y-4">
                  <div className="flex items-center gap-3">
                    <div className="w-7 h-7 rounded-lg flex items-center justify-center flex-shrink-0" style={{ background: `${BLUE}30` }}>
                      <MapPin className="w-3.5 h-3.5" style={{ color: BLUE }}/>
                    </div>
                    <p className="text-[13px] text-white/60">121 Street, New York, USA 466791</p>
                  </div>
                  <div className="flex items-center gap-3">
                    <div className="w-7 h-7 rounded-lg flex items-center justify-center flex-shrink-0" style={{ background: `${BLUE}30` }}>
                      <Mail className="w-3.5 h-3.5" style={{ color: BLUE }}/>
                    </div>
                    <p className="text-[13px] text-white/60">info@medvice.com</p>
                  </div>
                  <div className="flex items-center gap-3">
                    <div className="w-7 h-7 rounded-lg flex items-center justify-center flex-shrink-0" style={{ background: `${BLUE}30` }}>
                      <PhoneCall className="w-3.5 h-3.5" style={{ color: BLUE }}/>
                    </div>
                    <p className="text-[13px] text-white/60">123 456 7890</p>
                  </div>
                </div>
              </div>
            </div>

            {/* Bottom bar */}
            <div
              className="flex flex-col sm:flex-row justify-between items-center gap-4 pt-8 border-t text-[12px] text-white/40"
              style={{ borderColor: 'rgba(255,255,255,0.08)' }}
            >
              <p>© {new Date().getFullYear()} Medvice. All Rights Reserved.</p>
              <div className="flex gap-6">
                <button style={{ background: 'none', border: 'none', cursor: 'pointer', color: 'inherit' }} className="hover:text-white transition-colors">Privacy Policy</button>
                <button style={{ background: 'none', border: 'none', cursor: 'pointer', color: 'inherit' }} className="hover:text-white transition-colors">Terms &amp; Conditions</button>
              </div>
            </div>
          </div>
        </footer>

      </div>
    </>
  );
};

export default Home;
