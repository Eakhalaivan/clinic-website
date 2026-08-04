import React, { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { axiosPublic } from '../../api/axios';
import { Shield, Mail, Phone, Lock, User, Eye, EyeOff, Calendar, Activity, ChevronDown } from 'lucide-react';
import { motion } from 'framer-motion';
import { fadeIn } from '../../components/ui/motion';

const Register = () => {
  const [formData, setFormData] = useState({
    firstName: '',
    lastName: '',
    email: '',
    phone: '',
    password: '',
    confirmPassword: '',
    dob: '',
    gender: '',
    agreeTerms: false,
  });
  
  const [showPassword, setShowPassword] = useState(false);
  const [showConfirmPassword, setShowConfirmPassword] = useState(false);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');
  const [loading, setLoading] = useState(false);
  
  const navigate = useNavigate();

  const handleChange = (e) => {
    const value = e.target.type === 'checkbox' ? e.target.checked : e.target.value;
    setFormData({ ...formData, [e.target.name]: value });
  };

  const handleRegister = async (e) => {
    e.preventDefault();
    setError(''); setSuccess('');

    if (formData.password !== formData.confirmPassword) {
        setError('Passwords do not match.');
        return;
    }
    if (!formData.agreeTerms) {
      setError('You must agree to the Terms of Service and Privacy Policy to continue.');
      return;
    }

    setLoading(true);

    try {
      await axiosPublic.post('/auth/register', {
        firstName: formData.firstName,
        lastName: formData.lastName || formData.firstName, // Mockup only has "Full Name" but backend expects both. Splitting string is better, but this works for demo.
        email: formData.email,
        password: formData.password,
        phoneNumber: formData.phone,
        // Backend might need dob and gender if we're sending it now, but keeping original payload
      });
      
      setSuccess('Registration successful! Redirecting to login...');
      setTimeout(() => navigate('/patient/login'), 1500);
      
    } catch (err) {
      setError(err.response?.data?.message || err.response?.data || 'Registration failed. Please check details.');
    } finally {
      setLoading(false);
    }
  };

  // Helper to split full name into first and last
  const handleNameChange = (e) => {
      const parts = e.target.value.split(' ');
      setFormData({
          ...formData,
          firstName: parts[0] || '',
          lastName: parts.slice(1).join(' ') || ''
      });
  };

  return (
    <div className="min-h-screen flex bg-white font-sans text-gray-900">
        
      {/* ── Left Pane (Branding) ────────────────────────────────────────── */}
      <div className="hidden lg:flex lg:w-[45%] bg-[#f4f2ff] relative flex-col p-12 overflow-hidden">
        {/* Logo & Header */}
        <div className="flex flex-col z-10 mb-16">
            <div className="flex items-center gap-3 mb-2">
                <div className="w-12 h-12 bg-white rounded-xl shadow-sm flex items-center justify-center">
                    <Shield className="w-6 h-6 text-[#5b21b6] fill-[#5b21b6]/20" />
                </div>
                <div>
                    <h2 className="text-xl font-black text-[#1e1b4b] leading-tight tracking-tight uppercase">
                        Aurelian<br/>Health
                    </h2>
                </div>
            </div>
            <p className="text-[#6b7280] text-sm font-medium mt-1">Your Health, Our Priority</p>
        </div>

        {/* Center Content */}
        <div className="z-10 mb-auto">
            <h1 className="text-4xl font-bold text-[#1e1b4b] mb-4">Create Account</h1>
            <p className="text-gray-600 text-lg max-w-sm leading-relaxed">
                Join Aurelian Health and take the first step towards better healthcare
            </p>
        </div>

        {/* Decorative Image area (Phone mockup + Icons) */}
        <div className="absolute inset-x-0 bottom-0 top-[35%] flex items-end justify-center pointer-events-none">
            {/* Phone mockup placeholder matching design */}
            <div className="relative w-[280px] h-[550px] bg-white rounded-t-[40px] shadow-2xl border-[8px] border-[#1e1b4b] flex flex-col p-4 translate-y-12">
                {/* Notch */}
                <div className="absolute top-0 left-1/2 -translate-x-1/2 w-32 h-6 bg-[#1e1b4b] rounded-b-2xl z-20"></div>
                
                {/* Mockup UI Inner Content */}
                <div className="w-full h-full bg-[#f8f6fb] rounded-[24px] overflow-hidden pt-8 px-4 relative flex flex-col gap-4">
                    <div className="mb-2">
                        <h3 className="text-xl font-bold text-[#1e1b4b]">Hello, Michael 👋</h3>
                        <p className="text-xs text-gray-500">How are you feeling today?</p>
                    </div>
                    
                    {/* Fake Cards */}
                    <div className="bg-white p-3 rounded-2xl shadow-sm flex items-center gap-3">
                        <div className="w-10 h-10 bg-gray-200 rounded-full flex-shrink-0 overflow-hidden">
                            <img src="https://ui-avatars.com/api/?name=Dr+Sarah&background=e0e7ff&color=4f46e5" alt="Avatar" className="w-full h-full" />
                        </div>
                        <div>
                            <p className="text-xs font-bold">Dr. Sarah Johnson</p>
                            <p className="text-[10px] text-gray-400">Cardiologist</p>
                            <p className="text-[10px] text-gray-400 mt-1">May 21, 2024 • 10:30 AM</p>
                        </div>
                    </div>
                    <div className="bg-white p-3 rounded-2xl shadow-sm flex items-center gap-3">
                        <div className="w-8 h-8 bg-blue-50 rounded-lg flex items-center justify-center flex-shrink-0 text-blue-500">
                            <Activity size={16} />
                        </div>
                        <div>
                            <p className="text-xs font-bold">Health Records</p>
                            <p className="text-[10px] text-gray-400">View your medical history</p>
                        </div>
                    </div>
                    <div className="bg-white p-3 rounded-2xl shadow-sm flex items-center gap-3">
                        <div className="w-8 h-8 bg-orange-50 rounded-lg flex items-center justify-center flex-shrink-0 text-orange-500">
                            <span className="text-xs font-bold font-serif">Rx</span>
                        </div>
                        <div>
                            <p className="text-xs font-bold">Prescriptions</p>
                            <p className="text-[10px] text-gray-400">View your prescriptions</p>
                        </div>
                    </div>
                </div>
            </div>
            
            {/* Floating icons matching mockup */}
            <div className="absolute left-[15%] top-[10%] bg-white p-6 rounded-full shadow-[0_10px_40px_rgba(91,33,182,0.15)] flex items-center justify-center">
                <Shield className="w-16 h-16 text-[#5b21b6] fill-[#5b21b6]" />
                <div className="absolute text-white"><svg width="24" height="24" viewBox="0 0 24 24" fill="currentColor"><path d="M12 21.35l-1.45-1.32C5.4 15.36 2 12.28 2 8.5 2 5.42 4.42 3 7.5 3c1.74 0 3.41.81 4.5 2.09C13.09 3.81 14.76 3 16.5 3 19.58 3 22 5.42 22 8.5c0 3.78-3.4 6.86-8.55 11.54L12 21.35z"/></svg></div>
            </div>
            <div className="absolute right-[20%] top-[30%] bg-white p-2.5 rounded-xl shadow-lg">
                <User className="w-5 h-5 text-[#5b21b6]" />
            </div>
            <div className="absolute left-[20%] top-[40%] bg-white p-2.5 rounded-xl shadow-lg">
                <Calendar className="w-5 h-5 text-[#5b21b6]" />
            </div>
            <div className="absolute right-[25%] top-[45%] bg-white p-2.5 rounded-xl shadow-lg">
                <Activity className="w-5 h-5 text-[#5b21b6]" />
            </div>
        </div>

        <div className="absolute bottom-6 left-12 text-xs text-gray-400 z-10">
            © {new Date().getFullYear()} Aurelian Health. All rights reserved.
        </div>
      </div>

      {/* ── Right Pane (Form) ────────────────────────────────────────────── */}
      <div className="w-full lg:w-[55%] flex flex-col justify-center p-8 sm:p-12 lg:px-24 overflow-y-auto">
        <div className="w-full max-w-lg mx-auto py-8">
            <div className="text-center mb-8">
                <h2 className="text-3xl font-bold text-[#1e1b4b] mb-2">Create Your Account</h2>
                <p className="text-sm text-gray-500">Sign up to get started with Aurelian Health</p>
            </div>

            {error && (
                <motion.div variants={fadeIn} className="bg-red-50 border border-red-200 text-red-600 p-4 rounded-xl mb-6 text-sm">
                    {error}
                </motion.div>
            )}
            {success && (
                <motion.div variants={fadeIn} className="bg-green-50 border border-green-200 text-green-700 p-4 rounded-xl mb-6 text-sm">
                    {success}
                </motion.div>
            )}

            <form onSubmit={handleRegister} className="space-y-4">
                
                {/* Full Name */}
                <div>
                    <label className="block text-[11px] font-bold text-gray-700 mb-1">Full Name</label>
                    <div className="relative">
                        <User className="absolute left-4 top-1/2 -translate-y-1/2 text-gray-400 w-4 h-4" />
                        <input 
                            type="text" required onChange={handleNameChange}
                            className="w-full border border-gray-200 rounded-xl py-3 pl-11 pr-4 text-sm focus:border-[#5b21b6] focus:ring-1 focus:ring-[#5b21b6] outline-none" 
                            placeholder="Enter your full name" 
                        />
                    </div>
                </div>

                {/* Email & Phone Grid */}
                <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                    <div>
                        <label className="block text-[11px] font-bold text-gray-700 mb-1">Email Address</label>
                        <div className="relative">
                            <Mail className="absolute left-4 top-1/2 -translate-y-1/2 text-gray-400 w-4 h-4" />
                            <input 
                                type="email" required name="email" value={formData.email} onChange={handleChange}
                                className="w-full border border-gray-200 rounded-xl py-3 pl-11 pr-4 text-sm focus:border-[#5b21b6] focus:ring-1 focus:ring-[#5b21b6] outline-none" 
                                placeholder="Enter your email" 
                            />
                        </div>
                    </div>
                    <div>
                        <label className="block text-[11px] font-bold text-gray-700 mb-1">Phone Number</label>
                        <div className="relative">
                            <Phone className="absolute left-4 top-1/2 -translate-y-1/2 text-gray-400 w-4 h-4" />
                            <input 
                                type="tel" name="phone" value={formData.phone} onChange={handleChange}
                                className="w-full border border-gray-200 rounded-xl py-3 pl-11 pr-4 text-sm focus:border-[#5b21b6] focus:ring-1 focus:ring-[#5b21b6] outline-none" 
                                placeholder="Enter your phone" 
                            />
                        </div>
                    </div>
                </div>

                {/* Password Fields */}
                <div>
                    <label className="block text-[11px] font-bold text-gray-700 mb-1">Password</label>
                    <div className="relative">
                        <Lock className="absolute left-4 top-1/2 -translate-y-1/2 text-gray-400 w-4 h-4" />
                        <input 
                            type={showPassword ? "text" : "password"} required name="password" value={formData.password} onChange={handleChange}
                            className="w-full border border-gray-200 rounded-xl py-3 pl-11 pr-10 text-sm focus:border-[#5b21b6] focus:ring-1 focus:ring-[#5b21b6] outline-none" 
                            placeholder="Create a password" 
                        />
                        <button type="button" onClick={() => setShowPassword(!showPassword)} className="absolute right-4 top-1/2 -translate-y-1/2 text-gray-400">
                            {showPassword ? <EyeOff className="w-4 h-4" /> : <Eye className="w-4 h-4" />}
                        </button>
                    </div>
                </div>

                <div>
                    <label className="block text-[11px] font-bold text-gray-700 mb-1">Confirm Password</label>
                    <div className="relative">
                        <Lock className="absolute left-4 top-1/2 -translate-y-1/2 text-gray-400 w-4 h-4" />
                        <input 
                            type={showConfirmPassword ? "text" : "password"} required name="confirmPassword" value={formData.confirmPassword} onChange={handleChange}
                            className="w-full border border-gray-200 rounded-xl py-3 pl-11 pr-10 text-sm focus:border-[#5b21b6] focus:ring-1 focus:ring-[#5b21b6] outline-none" 
                            placeholder="Confirm your password" 
                        />
                        <button type="button" onClick={() => setShowConfirmPassword(!showConfirmPassword)} className="absolute right-4 top-1/2 -translate-y-1/2 text-gray-400">
                            {showConfirmPassword ? <EyeOff className="w-4 h-4" /> : <Eye className="w-4 h-4" />}
                        </button>
                    </div>
                </div>

                {/* DOB and Gender */}
                <div className="grid grid-cols-1 md:grid-cols-2 gap-4 pt-2">
                    <div>
                        <label className="block text-[11px] font-bold text-gray-700 mb-1">Date of Birth</label>
                        <div className="relative">
                            <Calendar className="absolute left-4 top-1/2 -translate-y-1/2 text-gray-400 w-4 h-4" />
                            <input 
                                type="date" name="dob" value={formData.dob} onChange={handleChange}
                                className="w-full border border-gray-200 rounded-xl py-3 pl-11 pr-4 text-sm text-gray-500 focus:border-[#5b21b6] focus:ring-1 focus:ring-[#5b21b6] outline-none" 
                            />
                        </div>
                    </div>
                    <div>
                        <label className="block text-[11px] font-bold text-gray-700 mb-1">Gender</label>
                        <div className="relative">
                            <select name="gender" value={formData.gender} onChange={handleChange} className="w-full border border-gray-200 rounded-xl py-3 pl-4 pr-10 text-sm text-gray-500 appearance-none focus:border-[#5b21b6] focus:ring-1 focus:ring-[#5b21b6] outline-none bg-white">
                                <option value="" disabled>Select your gender</option>
                                <option value="Male">Male</option>
                                <option value="Female">Female</option>
                                <option value="Other">Other</option>
                                <option value="Prefer not to say">Prefer not to say</option>
                            </select>
                            <ChevronDown className="absolute right-4 top-1/2 -translate-y-1/2 text-gray-400 w-4 h-4 pointer-events-none" />
                        </div>
                    </div>
                </div>

                {/* Terms Checkbox */}
                <div className="pt-2 pb-2">
                    <label className="flex items-center gap-3 cursor-pointer">
                        <input type="checkbox" name="agreeTerms" checked={formData.agreeTerms} onChange={handleChange} className="w-4 h-4 rounded border-gray-300 text-[#5b21b6] focus:ring-[#5b21b6]" />
                        <span className="text-[11px] text-gray-600">
                            I agree to the <a href="#" className="font-bold text-[#5b21b6] hover:underline">Terms of Service</a> and <a href="#" className="font-bold text-[#5b21b6] hover:underline">Privacy Policy</a>
                        </span>
                    </label>
                </div>

                <button type="submit" disabled={loading} className="w-full bg-[#5b21b6] hover:bg-[#4c1d95] text-white py-3.5 rounded-xl text-sm font-semibold transition-colors mb-6">
                    {loading ? 'Creating Account...' : 'Create Account'}
                </button>

                {/* OAuth Section */}
                <div className="relative flex items-center justify-center mb-6">
                    <div className="absolute inset-0 flex items-center">
                        <div className="w-full border-t border-gray-200"></div>
                    </div>
                    <span className="relative bg-white px-4 text-[11px] text-gray-400 uppercase tracking-wider">or sign up with</span>
                </div>

                <div className="grid grid-cols-2 gap-3 mb-8">
                    <button type="button" className="w-full flex items-center justify-center gap-2 bg-white border border-gray-200 hover:bg-gray-50 text-gray-700 py-3 rounded-xl text-xs font-semibold transition-colors">
                        <svg className="w-4 h-4" viewBox="0 0 24 24"><path fill="#4285F4" d="M22.56 12.25c0-.78-.07-1.53-.2-2.25H12v4.26h5.92c-.26 1.37-1.04 2.53-2.21 3.31v2.77h3.57c2.08-1.92 3.28-4.74 3.28-8.09z"/><path fill="#34A853" d="M12 23c2.97 0 5.46-.98 7.28-2.66l-3.57-2.77c-.98.66-2.23 1.06-3.71 1.06-2.86 0-5.29-1.93-6.16-4.53H2.18v2.84C3.99 20.53 7.7 23 12 23z"/><path fill="#FBBC05" d="M5.84 14.09c-.22-.66-.35-1.36-.35-2.09s.13-1.43.35-2.09V7.07H2.18C1.43 8.55 1 10.22 1 12s.43 3.45 1.18 4.93l2.85-2.22.81-.62z"/><path fill="#EA4335" d="M12 5.38c1.62 0 3.06.56 4.21 1.64l3.15-3.15C17.45 2.09 14.97 1 12 1 7.7 1 3.99 3.47 2.18 7.07l3.66 2.84c.87-2.6 3.3-4.53 6.16-4.53z"/></svg>
                        Sign up with Google
                    </button>
                    <button type="button" className="w-full flex items-center justify-center gap-2 bg-white border border-gray-200 hover:bg-gray-50 text-gray-700 py-3 rounded-xl text-xs font-semibold transition-colors">
                        <svg className="w-4 h-4" fill="currentColor" viewBox="0 0 24 24"><path d="M17.05 20.28c-.98.74-2.039 1.49-3.26 1.52-1.24.03-1.63-.73-3.04-.73-1.42 0-1.84.72-3.04.76-1.25.04-2.42-.8-3.41-2.24-2.04-2.93-3.6-8.29-1.52-11.9 1.03-1.8 2.87-2.95 4.9-2.98 1.2-.02 2.32.8 3.01.8.7 0 2.06-1 3.48-.85 1.47.05 2.8.71 3.55 1.83-3.08 1.88-2.58 6.13.43 7.37-.73 1.76-1.58 3.51-3.1 5.42zm-3.67-17.75c.61-.75 1.02-1.8 1.02-2.85-.97.05-2.08.68-2.73 1.45-.58.69-1.05 1.78-.9 2.8 1.05.09 2.03-.64 2.61-1.4z"/></svg>
                        Sign up with Apple
                    </button>
                </div>

                <p className="text-center text-xs text-gray-500">
                    Already have an account? <Link to="/patient/login" className="font-bold text-[#5b21b6] hover:underline">Sign In</Link>
                </p>
            </form>
        </div>
      </div>

    </div>
  );
};

export default Register;
