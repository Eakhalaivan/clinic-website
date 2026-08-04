import React, { useEffect, useRef } from 'react';
import { Link } from 'react-router-dom';

/* ─── Utility: tiny fade-in-up on scroll observer ─────────────────────── */
function useScrollReveal(selector = '[data-reveal]') {
  useEffect(() => {
    const els = document.querySelectorAll(selector);
    if (!els.length) return;
    const obs = new IntersectionObserver(
      (entries) => {
        entries.forEach((e) => {
          if (e.isIntersecting) {
            e.target.style.opacity = '1';
            e.target.style.transform = 'translateY(0)';
            obs.unobserve(e.target);
          }
        });
      },
      { threshold: 0.12 }
    );
    els.forEach((el) => {
      el.style.opacity = '0';
      el.style.transform = 'translateY(28px)';
      el.style.transition = 'opacity 0.65s ease, transform 0.65s ease';
      obs.observe(el);
    });
    return () => obs.disconnect();
  }, []);
}

/* ─── Service card data ─────────────────────────────────────────────────── */
const SERVICES = [
  {
    icon: (
      <svg className="w-6 h-6" fill="none" stroke="currentColor" strokeWidth="2" viewBox="0 0 24 24" aria-hidden="true">
        <path strokeLinecap="round" strokeLinejoin="round" d="M12 4.354a4 4 0 110 5.292M15 21H3v-1a6 6 0 0112 0v1zm0 0h6v-1a6 6 0 00-9-5.197M13 7a4 4 0 11-8 0 4 4 0 018 0z" />
      </svg>
    ),
    title: 'Expert Doctors',
    desc: 'Connect with our team of 150+ board-certified specialists across every major medical discipline.',
  },
  {
    icon: (
      <svg className="w-6 h-6" fill="none" stroke="currentColor" strokeWidth="2" viewBox="0 0 24 24" aria-hidden="true">
        <path strokeLinecap="round" strokeLinejoin="round" d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z" />
      </svg>
    ),
    title: 'Easy Scheduling',
    desc: 'Book same-day or advance appointments in seconds with zero waiting rooms and instant confirmation.',
  },
  {
    icon: (
      <svg className="w-6 h-6" fill="none" stroke="currentColor" strokeWidth="2" viewBox="0 0 24 24" aria-hidden="true">
        <path strokeLinecap="round" strokeLinejoin="round" d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z" />
      </svg>
    ),
    title: 'Digital Prescriptions',
    desc: 'Receive, manage, and refill prescriptions digitally — directly connected to our in-house pharmacy.',
  },
  {
    icon: (
      <svg className="w-6 h-6" fill="none" stroke="currentColor" strokeWidth="2" viewBox="0 0 24 24" aria-hidden="true">
        <path strokeLinecap="round" strokeLinejoin="round" d="M9 19v-6a2 2 0 00-2-2H5a2 2 0 00-2 2v6a2 2 0 002 2h2a2 2 0 002-2zm0 0V9a2 2 0 012-2h2a2 2 0 012 2v10m-6 0a2 2 0 002 2h2a2 2 0 002-2m0 0V5a2 2 0 012-2h2a2 2 0 012 2v14a2 2 0 01-2 2h-2a2 2 0 01-2-2z" />
      </svg>
    ),
    title: 'Health Analytics',
    desc: "Track vitals, lab results, and long-term trends all in one unified, beautifully designed dashboard.",
  },
  {
    icon: (
      <svg className="w-6 h-6" fill="none" stroke="currentColor" strokeWidth="2" viewBox="0 0 24 24" aria-hidden="true">
        <path strokeLinecap="round" strokeLinejoin="round" d="M12 15v2m-6 4h12a2 2 0 002-2v-6a2 2 0 00-2-2H6a2 2 0 00-2 2v6a2 2 0 002 2zm10-10V7a4 4 0 00-8 0v4h8z" />
      </svg>
    ),
    title: 'HIPAA Secured',
    desc: 'Military-grade 256-bit encryption protects every record, message, and transaction on our platform.',
  },
  {
    icon: (
      <svg className="w-6 h-6" fill="none" stroke="currentColor" strokeWidth="2" viewBox="0 0 24 24" aria-hidden="true">
        <path strokeLinecap="round" strokeLinejoin="round" d="M3 5a2 2 0 012-2h3.28a1 1 0 01.948.684l1.498 4.493a1 1 0 01-.502 1.21l-2.257 1.13a11.042 11.042 0 005.516 5.516l1.13-2.257a1 1 0 011.21-.502l4.493 1.498a1 1 0 01.684.949V19a2 2 0 01-2 2h-1C9.716 21 3 14.284 3 6V5z" />
      </svg>
    ),
    title: '24/7 Concierge',
    desc: 'Our dedicated concierge team is always reachable — by phone, chat, or in person — for any concern.',
  },
];

/* ─── Testimonial data ──────────────────────────────────────────────────── */
const TESTIMONIALS = [
  {
    text: "Aurelian Health transformed how I manage my family's healthcare. The digital prescriptions and lab tracking alone saved us hours every month.",
    name: 'Sarah M.',
    role: 'Patient since 2022',
    initials: 'SM',
    color: 'from-indigo-500 to-purple-600',
  },
  {
    text: 'The appointment system is flawless. I booked, saw my doctor, and had my prescription ready at the pharmacy — all within the same afternoon.',
    name: 'James K.',
    role: 'Patient since 2023',
    initials: 'JK',
    color: 'from-purple-500 to-pink-500',
  },
  {
    text: "Finally a clinic that respects my time. Zero wait, exceptional doctors, and the health dashboard keeps me informed between visits. Truly premium.",
    name: 'Priya L.',
    role: 'Patient since 2021',
    initials: 'PL',
    color: 'from-blue-500 to-indigo-600',
  },
];

/* ═══════════════════════════════════════════════════════════════════════════
   Home Component
   ═══════════════════════════════════════════════════════════════════════════ */
const Home = () => {
  useScrollReveal('[data-reveal]');

  return (
    <div style={{ fontFamily: "'Inter', sans-serif" }} className="bg-gray-50 text-gray-800 antialiased overflow-x-hidden">

      {/* ── Navbar ──────────────────────────────────────────────────────── */}
      <header className="bg-white/80 backdrop-blur-md border-b border-gray-100 sticky top-0 z-50 shadow-sm">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex justify-between items-center h-16">
            {/* Brand */}
            <Link to="/" className="flex items-center gap-2.5">
              <div className="w-8 h-8 rounded-lg bg-gradient-to-br from-indigo-600 to-purple-600 flex items-center justify-center shadow-sm">
                <svg className="w-4 h-4 text-white" fill="none" stroke="currentColor" strokeWidth="2.5" viewBox="0 0 24 24" aria-hidden="true">
                  <path strokeLinecap="round" strokeLinejoin="round" d="M4.318 6.318a4.5 4.5 0 000 6.364L12 20.364l7.682-7.682a4.5 4.5 0 00-6.364-6.364L12 7.636l-1.318-1.318a4.5 4.5 0 00-6.364 0z" />
                </svg>
              </div>
              <span
                className="font-bold text-xl"
                style={{
                  background: 'linear-gradient(90deg, #4F46E5, #9333EA)',
                  WebkitBackgroundClip: 'text',
                  WebkitTextFillColor: 'transparent',
                }}
              >
                Aurelian Health
              </span>
            </Link>

            {/* Desktop Nav */}
            <nav className="hidden md:flex items-center gap-8" aria-label="Primary navigation">
              {['Services', 'Specialists', 'Health Records', 'About'].map((item) => (
                <Link
                  key={item}
                  to="#"
                  className="text-sm font-medium text-gray-600 hover:text-indigo-600 transition-colors duration-200"
                >
                  {item}
                </Link>
              ))}
            </nav>

            {/* CTA */}
            <div className="flex items-center gap-3">
              <Link
                to="/patient/login"
                className="hidden sm:block text-sm font-semibold text-indigo-600 hover:text-indigo-700 transition-colors"
              >
                Sign In
              </Link>
              <Link
                to="/patient/register"
                className="text-sm font-semibold text-white px-4 py-2 rounded-lg shadow-sm hover:opacity-90 transition-opacity"
                style={{ background: 'linear-gradient(135deg, #4F46E5, #9333EA)' }}
              >
                Get Started
              </Link>
            </div>
          </div>
        </div>
      </header>

      {/* ── Hero ────────────────────────────────────────────────────────── */}
      <section
        className="relative min-h-[92vh] flex items-center"
        style={{
          background: 'radial-gradient(circle at 70% 50%, rgba(238, 242, 255, 0.7) 0%, transparent 55%), #f9fafb',
        }}
        aria-label="Hero section"
      >
        {/* Decorative blobs */}
        <div
          className="absolute top-0 right-0 w-96 h-96 opacity-20 pointer-events-none"
          style={{
            background: 'radial-gradient(circle, #818cf8 0%, transparent 70%)',
            filter: 'blur(40px)',
          }}
          aria-hidden="true"
        />
        <div
          className="absolute bottom-20 left-0 w-72 h-72 opacity-15 pointer-events-none"
          style={{
            background: 'radial-gradient(circle, #a78bfa 0%, transparent 70%)',
            filter: 'blur(50px)',
          }}
          aria-hidden="true"
        />

        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-24 w-full">
          <div className="grid lg:grid-cols-2 gap-12 items-center">
            {/* Left copy */}
            <div data-reveal>
              <span className="inline-flex items-center gap-2 bg-indigo-50 text-indigo-700 text-xs font-bold px-3 py-1.5 rounded-full mb-6 border border-indigo-100">
                <span className="w-2 h-2 rounded-full bg-indigo-500 animate-pulse" aria-hidden="true" />
                Ultra-Premium Medical Care
              </span>

              <h1 className="text-4xl sm:text-5xl lg:text-6xl font-bold leading-tight mb-6 text-gray-900">
                Healthcare That{' '}
                <span
                  style={{
                    background: 'linear-gradient(90deg, #4F46E5, #9333EA)',
                    WebkitBackgroundClip: 'text',
                    WebkitTextFillColor: 'transparent',
                  }}
                >
                  Puts You First
                </span>
              </h1>

              <p className="text-lg text-gray-600 mb-8 max-w-lg leading-relaxed">
                Experience precision medicine where clinical mastery meets concierge service. Book appointments, access records, and manage your health — all in one place.
              </p>

              <div className="flex flex-col sm:flex-row gap-4 mb-12">
                <Link
                  to="/patient/register"
                  className="inline-flex items-center justify-center gap-2 px-6 py-3.5 rounded-xl text-white font-semibold shadow-lg hover:shadow-xl hover:-translate-y-0.5 transition-all duration-200 text-sm"
                  style={{ background: 'linear-gradient(135deg, #4F46E5, #9333EA)' }}
                >
                  Book a Consultation
                  <svg className="w-4 h-4" fill="none" stroke="currentColor" strokeWidth="2" viewBox="0 0 24 24" aria-hidden="true">
                    <path strokeLinecap="round" strokeLinejoin="round" d="M17 8l4 4m0 0l-4 4m4-4H3" />
                  </svg>
                </Link>
                <Link
                  to="/doctors"
                  className="inline-flex items-center justify-center gap-2 px-6 py-3.5 rounded-xl text-gray-700 font-semibold bg-white border border-gray-200 shadow-sm hover:border-indigo-300 hover:text-indigo-600 transition-all duration-200 text-sm"
                >
                  Meet Our Doctors
                </Link>
              </div>

              {/* Trust badges */}
              <div className="flex flex-wrap gap-4">
                {[
                  { icon: '🏥', label: '150+ Specialists' },
                  { icon: '⭐', label: '4.9 / 5 Rating' },
                  { icon: '🔒', label: 'HIPAA Compliant' },
                ].map(({ icon, label }) => (
                  <div key={label} className="flex items-center gap-2 bg-white border border-gray-100 rounded-lg px-3 py-2 shadow-sm text-xs font-medium text-gray-700">
                    <span aria-hidden="true">{icon}</span> {label}
                  </div>
                ))}
              </div>
            </div>

            {/* Right — dashboard preview card */}
            <div data-reveal className="relative hidden lg:block" style={{ transitionDelay: '0.15s' }}>
              <div
                className="rounded-2xl p-6 shadow-2xl border border-white/80"
                style={{
                  background: 'linear-gradient(135deg, #ffffff 0%, #f0f4ff 100%)',
                  boxShadow: '0 25px 60px rgba(79, 70, 229, 0.12)',
                }}
              >
                {/* Mock mini dashboard */}
                <div className="flex items-center justify-between mb-5">
                  <div>
                    <p className="text-xs text-gray-500 font-medium">Good morning,</p>
                    <p className="text-base font-bold text-gray-900">Julian Aurelius</p>
                  </div>
                  <div className="w-10 h-10 rounded-full bg-gradient-to-br from-indigo-400 to-purple-500 flex items-center justify-center text-white font-bold text-sm" aria-hidden="true">JA</div>
                </div>

                <div className="grid grid-cols-2 gap-3 mb-5">
                  {[
                    { label: 'Next Appointment', value: 'Today, 3:00 PM', color: 'bg-indigo-50 text-indigo-700', icon: '📅' },
                    { label: 'Active Prescriptions', value: '3 Medicines', color: 'bg-purple-50 text-purple-700', icon: '💊' },
                    { label: 'Lab Results', value: '2 New Reports', color: 'bg-green-50 text-green-700', icon: '🧪' },
                    { label: 'Health Score', value: '92 / 100 ✓', color: 'bg-amber-50 text-amber-700', icon: '❤️' },
                  ].map(({ label, value, color, icon }) => (
                    <div key={label} className={`rounded-xl p-3 ${color} border border-current/10`}>
                      <p className="text-xs opacity-70 mb-1 font-medium">{icon} {label}</p>
                      <p className="text-sm font-bold">{value}</p>
                    </div>
                  ))}
                </div>

                <div className="bg-gradient-to-r from-indigo-600 to-purple-600 rounded-xl p-4 text-white">
                  <p className="text-xs opacity-80 mb-1">Upcoming: Cardiology Review</p>
                  <p className="font-bold text-sm">Dr. Sophia Reynolds</p>
                  <p className="text-xs opacity-70 mt-1">Tomorrow · 10:30 AM · Room 204</p>
                </div>
              </div>
            </div>
          </div>
        </div>
      </section>

      {/* ── Stats Bar ───────────────────────────────────────────────────── */}
      <section className="bg-white border-y border-gray-100 py-10" aria-label="Key metrics">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="grid grid-cols-2 md:grid-cols-4 gap-8 text-center">
            {[
              { value: '50,000+', label: 'Patients Served' },
              { value: '150+', label: 'Specialist Doctors' },
              { value: '12+', label: 'Medical Disciplines' },
              { value: '4.9/5', label: 'Patient Satisfaction' },
            ].map(({ value, label }) => (
              <div key={label} data-reveal>
                <p
                  className="text-3xl font-bold mb-1"
                  style={{
                    background: 'linear-gradient(90deg, #4F46E5, #9333EA)',
                    WebkitBackgroundClip: 'text',
                    WebkitTextFillColor: 'transparent',
                  }}
                >
                  {value}
                </p>
                <p className="text-sm text-gray-500 font-medium">{label}</p>
              </div>
            ))}
          </div>
        </div>
      </section>

      {/* ── Services Grid ───────────────────────────────────────────────── */}
      <section className="py-24 bg-gray-50" aria-label="Our services">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="text-center mb-16" data-reveal>
            <span
              className="text-sm font-bold uppercase tracking-widest"
              style={{
                background: 'linear-gradient(90deg, #4F46E5, #9333EA)',
                WebkitBackgroundClip: 'text',
                WebkitTextFillColor: 'transparent',
              }}
            >
              What We Offer
            </span>
            <h2 className="text-3xl sm:text-4xl font-bold text-gray-900 mt-3 mb-4">
              Everything You Need,{' '}
              <span
                style={{
                  background: 'linear-gradient(90deg, #4F46E5, #9333EA)',
                  WebkitBackgroundClip: 'text',
                  WebkitTextFillColor: 'transparent',
                }}
              >
                In One Place
              </span>
            </h2>
            <p className="text-gray-500 max-w-2xl mx-auto text-base">
              From booking to billing, prescriptions to lab reports — Aurelian Health is your single, trusted healthcare companion.
            </p>
          </div>

          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
            {SERVICES.map(({ icon, title, desc }, i) => (
              <article
                key={title}
                data-reveal
                style={{ transitionDelay: `${i * 0.07}s` }}
                className="group bg-white rounded-2xl p-7 border border-gray-100 shadow-sm hover:shadow-lg hover:-translate-y-1.5 transition-all duration-300"
              >
                <div
                  className="w-12 h-12 rounded-xl flex items-center justify-center mb-5 text-indigo-600 group-hover:scale-110 transition-transform duration-300"
                  style={{ background: 'linear-gradient(135deg, #EEF2FF, #F5F3FF)' }}
                  aria-hidden="true"
                >
                  {icon}
                </div>
                <h3 className="text-lg font-bold text-gray-900 mb-2">{title}</h3>
                <p className="text-sm text-gray-500 leading-relaxed">{desc}</p>
              </article>
            ))}
          </div>
        </div>
      </section>

      {/* ── Dual CTA ────────────────────────────────────────────────────── */}
      <section className="py-24 bg-white" aria-label="Call to action">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="grid md:grid-cols-2 gap-8">
            {/* Patient CTA */}
            <div
              data-reveal
              className="relative overflow-hidden rounded-2xl p-10 text-white"
              style={{ background: 'linear-gradient(135deg, #4F46E5, #7C3AED)' }}
            >
              <div
                className="absolute -top-10 -right-10 w-40 h-40 rounded-full opacity-20"
                style={{ background: 'radial-gradient(circle, #fff 0%, transparent 70%)' }}
                aria-hidden="true"
              />
              <svg className="w-10 h-10 mb-5 opacity-90" fill="none" stroke="currentColor" strokeWidth="1.5" viewBox="0 0 24 24" aria-hidden="true">
                <path strokeLinecap="round" strokeLinejoin="round" d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z" />
              </svg>
              <h3 className="text-2xl font-bold mb-3">New Patient?</h3>
              <p className="text-indigo-100 text-sm mb-6 leading-relaxed">
                Create your free account in under 2 minutes and book your first consultation today.
              </p>
              <Link
                to="/patient/register"
                className="inline-flex items-center gap-2 bg-white text-indigo-700 font-bold text-sm px-5 py-2.5 rounded-xl hover:bg-indigo-50 transition-colors shadow-sm"
              >
                Create Account
                <svg className="w-4 h-4" fill="none" stroke="currentColor" strokeWidth="2" viewBox="0 0 24 24" aria-hidden="true">
                  <path strokeLinecap="round" strokeLinejoin="round" d="M17 8l4 4m0 0l-4 4m4-4H3" />
                </svg>
              </Link>
            </div>

            {/* Doctor CTA */}
            <div
              data-reveal
              className="relative overflow-hidden rounded-2xl p-10 text-white"
              style={{ background: 'linear-gradient(135deg, #7C3AED, #A855F7)' }}
            >
              <div
                className="absolute -top-10 -right-10 w-40 h-40 rounded-full opacity-20"
                style={{ background: 'radial-gradient(circle, #fff 0%, transparent 70%)' }}
                aria-hidden="true"
              />
              <svg className="w-10 h-10 mb-5 opacity-90" fill="none" stroke="currentColor" strokeWidth="1.5" viewBox="0 0 24 24" aria-hidden="true">
                <path strokeLinecap="round" strokeLinejoin="round" d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z" />
              </svg>
              <h3 className="text-2xl font-bold mb-3">Staff Portal</h3>
              <p className="text-purple-100 text-sm mb-6 leading-relaxed">
                Doctors, nurses, and admin staff — sign in to your dedicated portal to manage schedules, records, and more.
              </p>
              <Link
                to="/doctor/login"
                className="inline-flex items-center gap-2 bg-white text-purple-700 font-bold text-sm px-5 py-2.5 rounded-xl hover:bg-purple-50 transition-colors shadow-sm"
              >
                Staff Sign In
                <svg className="w-4 h-4" fill="none" stroke="currentColor" strokeWidth="2" viewBox="0 0 24 24" aria-hidden="true">
                  <path strokeLinecap="round" strokeLinejoin="round" d="M17 8l4 4m0 0l-4 4m4-4H3" />
                </svg>
              </Link>
            </div>
          </div>
        </div>
      </section>

      {/* ── App Download Banner ─────────────────────────────────────────── */}
      <section
        className="py-20"
        style={{ background: 'linear-gradient(135deg, #a78bfa, #818cf8)' }}
        aria-label="Mobile app download"
      >
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex flex-col md:flex-row items-center justify-between gap-10">
            <div data-reveal className="text-white max-w-xl">
              <h2 className="text-3xl sm:text-4xl font-bold mb-4 leading-tight">
                Your Health, In Your Pocket
              </h2>
              <p className="text-indigo-100 text-base mb-8 leading-relaxed">
                Download the Aurelian Health app for instant access to appointments, prescriptions, lab results, and 24/7 concierge support — wherever you are.
              </p>
              <div className="flex flex-wrap gap-4">
                <a
                  href="#"
                  className="flex items-center gap-3 bg-black text-white px-5 py-3 rounded-xl hover:bg-gray-900 transition-colors shadow-lg"
                  aria-label="Download on the App Store"
                >
                  <svg className="w-6 h-6" viewBox="0 0 24 24" fill="currentColor" aria-hidden="true">
                    <path d="M18.71 19.5c-.83 1.24-1.71 2.45-3.05 2.47-1.34.03-1.77-.79-3.29-.79-1.53 0-2 .77-3.27.82-1.31.05-2.3-1.32-3.14-2.53C4.25 17 2.94 12.45 4.7 9.39c.87-1.52 2.43-2.48 4.12-2.51 1.28-.02 2.5.87 3.29.87.78 0 2.26-1.07 3.8-.91.65.03 2.47.26 3.64 1.98-.09.06-2.17 1.28-2.15 3.81.03 3.02 2.65 4.03 2.68 4.04-.03.07-.42 1.44-1.38 2.83M13 3.5c.73-.83 1.94-1.46 2.94-1.5.13 1.17-.34 2.35-1.04 3.19-.69.85-1.83 1.51-2.95 1.42-.15-1.15.41-2.35 1.05-3.11z" />
                  </svg>
                  <div className="text-left">
                    <p className="text-[10px] text-gray-300 leading-none">Download on the</p>
                    <p className="text-sm font-bold leading-tight">App Store</p>
                  </div>
                </a>
                <a
                  href="#"
                  className="flex items-center gap-3 bg-black text-white px-5 py-3 rounded-xl hover:bg-gray-900 transition-colors shadow-lg"
                  aria-label="Get it on Google Play"
                >
                  <svg className="w-6 h-6" viewBox="0 0 24 24" fill="currentColor" aria-hidden="true">
                    <path d="M3 20.5v-17c0-.83.94-1.3 1.6-.8l14 8.5c.6.36.6 1.24 0 1.6l-14 8.5c-.66.5-1.6.03-1.6-.8z" />
                  </svg>
                  <div className="text-left">
                    <p className="text-[10px] text-gray-300 leading-none">Get it on</p>
                    <p className="text-sm font-bold leading-tight">Google Play</p>
                  </div>
                </a>
              </div>
            </div>

            {/* Phone mockup */}
            <div data-reveal className="relative flex-shrink-0" style={{ transitionDelay: '0.1s' }}>
              <div
                className="w-48 h-80 rounded-3xl shadow-2xl flex flex-col overflow-hidden border-4 border-white/30"
                style={{ background: 'linear-gradient(160deg, #312e81, #6d28d9)' }}
                aria-hidden="true"
              >
                <div className="p-4 flex-1">
                  <div className="flex justify-between items-center mb-4">
                    <p className="text-[10px] text-purple-200 font-bold">AURELIAN HEALTH</p>
                    <div className="w-2 h-2 rounded-full bg-green-400" />
                  </div>
                  <div className="space-y-2 mb-4">
                    <div className="bg-white/10 rounded-lg p-2.5">
                      <p className="text-[9px] text-purple-200 mb-1">Next Appointment</p>
                      <p className="text-xs text-white font-bold">Today, 3:00 PM</p>
                    </div>
                    <div className="bg-white/10 rounded-lg p-2.5">
                      <p className="text-[9px] text-purple-200 mb-1">Prescriptions</p>
                      <p className="text-xs text-white font-bold">3 Active</p>
                    </div>
                  </div>
                  <div className="bg-indigo-400/30 rounded-lg p-2.5">
                    <p className="text-[9px] text-purple-200 mb-1">Health Score</p>
                    <div className="flex items-center gap-2">
                      <div className="flex-1 h-1.5 bg-white/20 rounded-full overflow-hidden">
                        <div className="h-full bg-green-400 rounded-full" style={{ width: '92%' }} />
                      </div>
                      <p className="text-[10px] text-white font-bold">92%</p>
                    </div>
                  </div>
                </div>
                {/* Bottom bar */}
                <div className="border-t border-white/10 p-3 flex justify-around">
                  {['🏠', '📅', '💊', '👤'].map((e) => (
                    <span key={e} className="text-sm">{e}</span>
                  ))}
                </div>
              </div>
              {/* Glow */}
              <div
                className="absolute inset-0 rounded-3xl pointer-events-none"
                style={{ boxShadow: '0 0 80px rgba(167, 139, 250, 0.5)' }}
              />
            </div>
          </div>
        </div>
      </section>

      {/* ── Testimonials ────────────────────────────────────────────────── */}
      <section className="py-24 bg-gray-50" aria-label="Patient testimonials">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="text-center mb-16" data-reveal>
            <h2 className="text-3xl sm:text-4xl font-bold text-gray-900 mb-4">
              Trusted by{' '}
              <span
                style={{
                  background: 'linear-gradient(90deg, #4F46E5, #9333EA)',
                  WebkitBackgroundClip: 'text',
                  WebkitTextFillColor: 'transparent',
                }}
              >
                Thousands
              </span>
            </h2>
            <p className="text-gray-500 max-w-xl mx-auto text-base">
              Real patients. Real outcomes. See why Aurelian Health is the highest-rated clinic in the region.
            </p>
          </div>

          <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
            {TESTIMONIALS.map(({ text, name, role, initials, color }, i) => (
              <blockquote
                key={name}
                data-reveal
                style={{ transitionDelay: `${i * 0.1}s` }}
                className="bg-white rounded-2xl p-7 border border-gray-100 shadow-sm hover:shadow-md transition-shadow duration-300"
              >
                <div className="flex mb-4" aria-label="5 star rating">
                  {[...Array(5)].map((_, s) => (
                    <svg key={s} className="w-4 h-4 text-amber-400" fill="currentColor" viewBox="0 0 20 20" aria-hidden="true">
                      <path d="M9.049 2.927c.3-.921 1.603-.921 1.902 0l1.07 3.292a1 1 0 00.95.69h3.462c.969 0 1.371 1.24.588 1.81l-2.8 2.034a1 1 0 00-.364 1.118l1.07 3.292c.3.921-.755 1.688-1.54 1.118l-2.8-2.034a1 1 0 00-1.175 0l-2.8 2.034c-.784.57-1.838-.197-1.539-1.118l1.07-3.292a1 1 0 00-.364-1.118L2.98 8.72c-.783-.57-.38-1.81.588-1.81h3.461a1 1 0 00.951-.69l1.07-3.292z" />
                    </svg>
                  ))}
                </div>
                <p className="text-gray-600 text-sm leading-relaxed mb-6 italic">"{text}"</p>
                <footer className="flex items-center gap-3">
                  <div
                    className={`w-10 h-10 rounded-full bg-gradient-to-br ${color} flex items-center justify-center text-white text-xs font-bold flex-shrink-0`}
                    aria-hidden="true"
                  >
                    {initials}
                  </div>
                  <div>
                    <p className="text-sm font-bold text-gray-900">{name}</p>
                    <p className="text-xs text-gray-400">{role}</p>
                  </div>
                </footer>
              </blockquote>
            ))}
          </div>
        </div>
      </section>

      {/* ── Footer ──────────────────────────────────────────────────────── */}
      <footer className="bg-white border-t border-gray-100">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-12">
          <div className="grid grid-cols-1 md:grid-cols-4 gap-8 mb-12">
            {/* Brand */}
            <div className="md:col-span-1">
              <Link to="/" className="flex items-center gap-2 mb-4">
                <div className="w-7 h-7 rounded-lg bg-gradient-to-br from-indigo-600 to-purple-600 flex items-center justify-center" aria-hidden="true">
                  <svg className="w-3.5 h-3.5 text-white" fill="none" stroke="currentColor" strokeWidth="2.5" viewBox="0 0 24 24" aria-hidden="true">
                    <path strokeLinecap="round" strokeLinejoin="round" d="M4.318 6.318a4.5 4.5 0 000 6.364L12 20.364l7.682-7.682a4.5 4.5 0 00-6.364-6.364L12 7.636l-1.318-1.318a4.5 4.5 0 00-6.364 0z" />
                  </svg>
                </div>
                <span
                  className="font-bold text-base"
                  style={{
                    background: 'linear-gradient(90deg, #4F46E5, #9333EA)',
                    WebkitBackgroundClip: 'text',
                    WebkitTextFillColor: 'transparent',
                  }}
                >
                  Aurelian Health
                </span>
              </Link>
              <p className="text-sm text-gray-500 leading-relaxed">
                Precision medicine. Unparalleled care. Your health, our priority.
              </p>
            </div>

            {/* Links */}
            {[
              {
                heading: 'Services',
                links: ['Book Appointment', 'Find a Doctor', 'Lab Reports', 'Prescriptions'],
              },
              {
                heading: 'Portals',
                links: ['Patient Login', 'Doctor Portal', 'Admin Console', 'Pharmacy'],
              },
              {
                heading: 'Company',
                links: ['About Us', 'Privacy Policy', 'Terms of Service', 'HIPAA Compliance'],
              },
            ].map(({ heading, links }) => (
              <div key={heading}>
                <h3 className="text-xs font-bold uppercase tracking-widest text-gray-900 mb-4">{heading}</h3>
                <ul className="space-y-2">
                  {links.map((l) => (
                    <li key={l}>
                      <Link to="#" className="text-sm text-gray-500 hover:text-indigo-600 transition-colors">
                        {l}
                      </Link>
                    </li>
                  ))}
                </ul>
              </div>
            ))}
          </div>

          <div className="border-t border-gray-100 pt-6 flex flex-col sm:flex-row justify-between items-center gap-4">
            <p className="text-xs text-gray-400">
              © {new Date().getFullYear()} Aurelian Health. All rights reserved. HIPAA Compliant &amp; Secure.
            </p>
            <div className="flex items-center gap-2 text-xs text-gray-400">
              <span className="w-2 h-2 rounded-full bg-green-500" aria-hidden="true" />
              All systems operational
            </div>
          </div>
        </div>
      </footer>

    </div>
  );
};

export default Home;
