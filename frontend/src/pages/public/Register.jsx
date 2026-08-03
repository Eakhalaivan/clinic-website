import React, { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { axiosPublic } from '../../api/axios';
import { motion } from 'framer-motion';
import { ShieldCheck, Lock, Mail, Phone, User, CheckCircle2, UserPlus } from 'lucide-react';
import Button from '../../components/ui/Button';
import FormField from '../../components/ui/FormField';
import ThemeToggle from '../../components/ui/ThemeToggle';
import { fadeIn, scaleIn } from '../../components/ui/motion';

const Register = () => {
  const [formData, setFormData] = useState({
    firstName: '',
    lastName: '',
    email: '',
    phone: '',
    password: '',
    agreeTerms: false,
  });
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');
  const [loading, setLoading] = useState(false);
  
  const navigate = useNavigate();

  const handleChange = (e) => {
    const value = e.target.type === 'checkbox' ? e.target.checked : e.target.value;
    setFormData({
      ...formData,
      [e.target.name]: value
    });
  };

  const getPasswordStrength = (pwd) => {
    if (!pwd) return 0;
    let score = 0;
    if (pwd.length >= 8) score++;
    if (/[A-Z]/.test(pwd)) score++;
    if (/[0-9]/.test(pwd)) score++;
    if (/[^A-Za-z0-9]/.test(pwd)) score++;
    return score;
  };

  const strength = getPasswordStrength(formData.password);

  const handleRegister = async (e) => {
    e.preventDefault();
    setError('');
    setSuccess('');

    if (!formData.agreeTerms) {
      setError('You must agree to the Terms of Service and Privacy Policy to continue.');
      return;
    }

    setLoading(true);

    try {
      await axiosPublic.post('/auth/register', {
        firstName: formData.firstName,
        lastName: formData.lastName,
        email: formData.email,
        password: formData.password,
        phoneNumber: formData.phone,
      });
      
      setSuccess('Registration successful! Redirecting to login...');
      setTimeout(() => navigate('/patient/login'), 1500);
      
    } catch (err) {
      setError(err.response?.data?.message || err.response?.data || 'Registration failed. Please check details.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen bg-[var(--color-bg-app)] flex items-center justify-center p-4 sm:p-6 lg:p-10 font-sans transition-colors duration-200">
      <motion.div 
        initial="hidden" 
        animate="visible" 
        variants={scaleIn}
        className="w-full max-w-5xl bg-[var(--color-surface)] rounded-lg shadow-elevated overflow-hidden grid grid-cols-1 lg:grid-cols-2 border border-[var(--color-border)] min-h-[680px]"
      >
        
        {/* ── Left Form Panel ────────────────────────────────────────────── */}
        <div className="p-8 sm:p-10 lg:p-12 flex flex-col justify-between bg-[var(--color-surface)] overflow-y-auto max-h-[90vh] lg:max-h-none">
          <div>
            {/* Header with Theme Toggle */}
            <div className="flex items-center justify-between mb-6">
              <div>
                <h2 className="font-display font-bold text-2xl text-[var(--color-navy-900)] tracking-tight m-0">
                  Aurelian Health
                </h2>
                <p className="text-xs text-[var(--color-text-muted)] mt-1 m-0">
                  Begin your journey towards precision medical care.
                </p>
              </div>
              <ThemeToggle />
            </div>

            {/* Banners */}
            {error && (
              <motion.div 
                variants={fadeIn}
                className="bg-[var(--color-danger-bg)] border-l-4 border-[var(--color-danger)] p-3.5 mb-5 rounded-r" 
                role="alert"
              >
                <p className="text-xs font-semibold text-[var(--color-danger)]">Registration Issue</p>
                <p className="text-xs text-[var(--color-text)] mt-0.5 m-0">{error}</p>
              </motion.div>
            )}
            {success && (
              <motion.div 
                variants={fadeIn}
                className="bg-[var(--color-success-bg)] border-l-4 border-[var(--color-success)] p-3.5 mb-5 rounded-r flex items-center space-x-2"
              >
                <CheckCircle2 size={16} className="text-[var(--color-success)] shrink-0" />
                <p className="text-xs font-medium text-[var(--color-text)] m-0">{success}</p>
              </motion.div>
            )}

            {/* Registration Form */}
            <form onSubmit={handleRegister} className="space-y-4">
              
              <div className="grid grid-cols-2 gap-3">
                <FormField label="First Name" required id="fn">
                  <input 
                    id="fn"
                    type="text"
                    name="firstName"
                    value={formData.firstName}
                    onChange={handleChange}
                    placeholder="Julian"
                    required 
                    className="input-field text-xs py-2.5"
                  />
                </FormField>
                <FormField label="Last Name" required id="ln">
                  <input 
                    id="ln"
                    type="text"
                    name="lastName"
                    value={formData.lastName}
                    onChange={handleChange}
                    placeholder="Aurelius"
                    required 
                    className="input-field text-xs py-2.5"
                  />
                </FormField>
              </div>

              <FormField label="Email Address" required id="email">
                <div className="relative w-full">
                  <Mail size={14} className="absolute left-3.5 top-3 text-[var(--color-text-muted)]" />
                  <input 
                    id="email"
                    type="email"
                    name="email"
                    value={formData.email}
                    onChange={handleChange}
                    placeholder="julian@example.com"
                    required 
                    className="input-field text-xs py-2.5 pl-9"
                  />
                </div>
              </FormField>

              <FormField label="Phone Number" id="phone">
                <div className="relative w-full">
                  <Phone size={14} className="absolute left-3.5 top-3 text-[var(--color-text-muted)]" />
                  <input 
                    id="phone"
                    type="tel"
                    name="phone"
                    value={formData.phone}
                    onChange={handleChange}
                    placeholder="+1 (555) 000-0000"
                    className="input-field text-xs py-2.5 pl-9"
                  />
                </div>
              </FormField>

              <FormField label="Password" required id="pwd">
                <div className="relative w-full">
                  <Lock size={14} className="absolute left-3.5 top-3 text-[var(--color-text-muted)]" />
                  <input 
                    id="pwd"
                    type="password"
                    name="password"
                    value={formData.password}
                    onChange={handleChange}
                    placeholder="••••••••"
                    required 
                    className="input-field text-xs py-2.5 pl-9"
                  />
                </div>
              </FormField>

              {/* Password Strength Meter */}
              <div className="flex items-center justify-between space-x-1.5 pt-0.5">
                <div className="flex-1 grid grid-cols-4 gap-1 h-1">
                  <div className={`rounded-full transition-all ${strength >= 1 ? 'bg-[var(--color-navy-600)]' : 'bg-[var(--color-border)]'}`} />
                  <div className={`rounded-full transition-all ${strength >= 2 ? 'bg-[var(--color-navy-600)]' : 'bg-[var(--color-border)]'}`} />
                  <div className={`rounded-full transition-all ${strength >= 3 ? 'bg-[var(--color-navy-600)]' : 'bg-[var(--color-border)]'}`} />
                  <div className={`rounded-full transition-all ${strength >= 4 ? 'bg-[var(--color-navy-600)]' : 'bg-[var(--color-border)]'}`} />
                </div>
                <span className="text-[10px] font-semibold text-[var(--color-navy-600)] shrink-0">
                  {strength === 0 ? 'Weak' : strength === 1 ? 'Fair' : strength === 2 ? 'Moderate' : 'Strong'}
                </span>
              </div>

              {/* Terms Checkbox */}
              <div className="pt-1">
                <label className="flex items-start space-x-2 cursor-pointer select-none text-xs text-[var(--color-text-muted)]">
                  <input 
                    type="checkbox"
                    name="agreeTerms"
                    checked={formData.agreeTerms}
                    onChange={handleChange}
                    className="w-4 h-4 mt-0.5 rounded border-[var(--color-border)] text-[var(--color-navy-600)]"
                  />
                  <span>
                    I agree to the{' '}
                    <a href="#terms" onClick={(e) => { e.preventDefault(); alert('Terms of Service: Aurelian Health patient privacy agreement.'); }} className="font-semibold text-[var(--color-navy-900)] hover:underline">
                      Terms of Service
                    </a>{' '}
                    and{' '}
                    <a href="#privacy" onClick={(e) => { e.preventDefault(); alert('Privacy Policy: Full HIPAA encrypted data protection policy.'); }} className="font-semibold text-[var(--color-navy-900)] hover:underline">
                      Privacy Policy
                    </a>.
                  </span>
                </label>
              </div>

              {/* Submit Button */}
              <div className="pt-2">
                <Button 
                  type="submit"
                  variant="primary"
                  size="md"
                  fullWidth
                  isLoading={loading}
                  icon={UserPlus}
                >
                  Create Account
                </Button>
              </div>
            </form>

            <div className="mt-4 text-center text-xs text-[var(--color-text-muted)]">
              Already have an account?{' '}
              <Link to="/patient/login" className="font-bold text-[var(--color-navy-600)] hover:underline">
                Sign In
              </Link>
            </div>
          </div>

          <div className="mt-6 pt-4 border-t border-[var(--color-border)] flex items-center justify-between text-[11px] text-[var(--color-text-muted)]">
            <span>Secure 256-bit HIPAA Encryption</span>
            <div className="flex items-center space-x-2 text-[var(--color-navy-600)]">
              <ShieldCheck size={14} />
              <Lock size={14} />
            </div>
          </div>
        </div>

        {/* ── Right Hero Image Panel ────────────────────────────────────── */}
        <div className="relative hidden lg:block overflow-hidden bg-[var(--color-navy-900)]">
          <img 
            src="/assets/clinic_hero.png" 
            alt="Aurelian Health Medical Care" 
            className="absolute inset-0 w-full h-full object-cover"
          />
          <div className="absolute inset-0 bg-gradient-to-t from-[var(--color-navy-900)]/95 via-[var(--color-navy-900)]/60 to-[var(--color-navy-900)]/30" />

          <div className="absolute top-8 right-8">
            <div className="bg-white/20 backdrop-blur-glass border border-white/30 text-white text-xs px-4 py-2 rounded-md font-medium flex items-center space-x-2 shadow-sm">
              <ShieldCheck size={16} className="text-amber-300" />
              <div>
                <p className="font-bold leading-tight m-0">Accredited Platinum Care</p>
                <p className="text-[10px] text-blue-100/80 m-0">Global Health Excellence 2024</p>
              </div>
            </div>
          </div>

          <div className="absolute bottom-10 left-10 right-10 text-white">
            <span className="font-display text-5xl text-blue-200/50 leading-none block mb-1">
              “
            </span>
            <blockquote className="font-display italic font-semibold text-xl sm:text-2xl leading-relaxed mb-4 text-white">
              "The art of medicine consists of amusing the patient while nature cures the disease. We provide the clarity nature requires."
            </blockquote>
            <p className="text-xs font-bold uppercase tracking-widest text-blue-200/90 m-0">
              — THE AURELIAN STANDARD
            </p>
          </div>
        </div>

      </motion.div>
    </div>
  );
};

export default Register;
