import React, { useState, useEffect } from 'react';
import logger from '../../utils/logger';
import { useParams, useNavigate, Link } from 'react-router-dom';
import useAuthStore, { isTokenValid } from '../../store/authStore';
import { getPortalConfig, PORTAL_CONFIGS } from '../../config/portalConfig';
import { motion } from 'framer-motion';
import { ShieldCheck, Lock, Mail, Key, ArrowRight, Eye, EyeOff, Activity, Clock, Shield, Award } from 'lucide-react';
import { fadeIn, scaleIn } from '../../components/ui/motion';

export default function PortalLoginPage() {
    const { portalSlug = 'patient' } = useParams();
    const navigate = useNavigate();
    const config = getPortalConfig(portalSlug);
    
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [showPassword, setShowPassword] = useState(false);
    const [rememberMe, setRememberMe] = useState(false);
    const [otp, setOtp] = useState('');
    const [forgotStep, setForgotStep] = useState(0); 
    const [newPassword, setNewPassword] = useState('');
    
    const { login: storeLogin, verifyMfa, mfaPending, error, isLoading, mfaEmail, clearError, token, roles, clearStaleToken, forgotPassword, resetPassword } = useAuthStore();

    const resolveTargetDashboard = (currentConfig) => {
        const userRoles = useAuthStore.getState().roles || [];
        const isSuperOrAdmin = userRoles.includes('ROLE_ADMIN') || userRoles.includes('ROLE_SUPER_ADMIN');
        
        if (isSuperOrAdmin) return currentConfig.dashboardRoute || '/super-admin/dashboard';
        if (currentConfig.role && userRoles.includes(currentConfig.role)) return currentConfig.dashboardRoute;
        const matchingPortal = PORTAL_CONFIGS.find(p => userRoles.includes(p.role));
        return matchingPortal ? matchingPortal.dashboardRoute : (currentConfig.dashboardRoute || '/');
    };

    useEffect(() => { clearStaleToken?.(); }, []); // eslint-disable-line react-hooks/exhaustive-deps

    useEffect(() => {
        if (isTokenValid(token) && roles?.length > 0) {
            const targetRoute = resolveTargetDashboard(config);
            navigate(targetRoute, { replace: true });
        }
    }, [token]); // eslint-disable-line react-hooks/exhaustive-deps

    useEffect(() => { clearError?.(); }, [portalSlug, clearError]);

    const handleLogin = async (e) => {
        e.preventDefault();
        try {
            const success = await storeLogin(portalSlug, email, password);
            if (success) navigate(resolveTargetDashboard(config));
        } catch (err) { logger.error('Login failed:', err); }
    };

    const handleMfa = async (e) => {
        e.preventDefault();
        const success = await verifyMfa(portalSlug, mfaEmail, otp);
        if (success) navigate(resolveTargetDashboard(config));
    };

    const handleForgotRequest = async (e) => {
        e.preventDefault();
        const success = await forgotPassword(email);
        if (success) setForgotStep(2);
    };

    const handleResetPassword = async (e) => {
        e.preventDefault();
        const success = await resetPassword(email, otp, newPassword);
        if (success) {
            setForgotStep(0);
            setPassword('');
            setOtp('');
            setNewPassword('');
        }
    };

    return (
        <div className="min-h-screen flex bg-white font-sans text-gray-900">
            {/* ── Left Pane (Branding & Features) ────────────────────────── */}
            <div className="hidden lg:flex lg:w-1/2 bg-[#f4f2ff] relative flex-col justify-between p-12">
                {/* Logo & Header */}
                <div className="flex flex-col z-10">
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
                <div className="z-10 mt-12 mb-auto">
                    <h1 className="text-4xl font-bold text-[#1e1b4b] mb-4">Welcome Back!</h1>
                    <p className="text-gray-600 text-lg max-w-sm leading-relaxed">
                        {portalSlug === 'patient' 
                            ? 'Sign in to continue to your Aurelian Health account'
                            : `Sign in to access the ${config.displayName}`
                        }
                    </p>
                </div>

                {/* Floating Elements & Image Placeholder */}
                <div className="absolute inset-0 flex items-center justify-center pointer-events-none overflow-hidden">
                    {/* Placeholder for doctors image from mockup */}
                    <div className="absolute bottom-32 w-full h-[400px] flex items-end justify-center opacity-80">
                        {/* If /assets/doctors.png exists, this will load it, otherwise shows generic gradient */}
                        <img loading="lazy" src="/assets/clinic_hero.png" alt="Doctors" className="object-cover h-full max-w-full opacity-20 mix-blend-multiply rounded-t-[100px]" onError={(e) => e.target.style.display = 'none'} />
                    </div>
                    
                    {/* Floating icons matching mockup */}
                    <div className="absolute left-[20%] top-[40%] bg-white p-3 rounded-2xl shadow-xl">
                        <Shield className="w-8 h-8 text-[#5b21b6]" />
                    </div>
                    <div className="absolute right-[25%] top-[55%] bg-white p-3 rounded-2xl shadow-xl">
                        <Activity className="w-6 h-6 text-[#5b21b6]" />
                    </div>
                </div>

                {/* Bottom Features */}
                <div className="flex items-center gap-6 z-10 bg-white/40 backdrop-blur-md py-4 px-6 rounded-2xl">
                    <div className="flex items-center gap-3">
                        <ShieldCheck className="w-5 h-5 text-[#5b21b6]" />
                        <div>
                            <p className="text-xs font-bold text-gray-900 leading-none">Secure & Trusted</p>
                            <p className="text-[10px] text-gray-500 mt-1">Your data is safe with us</p>
                        </div>
                    </div>
                    <div className="flex items-center gap-3">
                        <Award className="w-5 h-5 text-[#5b21b6]" />
                        <div>
                            <p className="text-xs font-bold text-gray-900 leading-none">Expert Doctors</p>
                            <p className="text-[10px] text-gray-500 mt-1">Experienced specialists</p>
                        </div>
                    </div>
                    <div className="flex items-center gap-3">
                        <Clock className="w-5 h-5 text-[#5b21b6]" />
                        <div>
                            <p className="text-xs font-bold text-gray-900 leading-none">24/7 Support</p>
                            <p className="text-[10px] text-gray-500 mt-1">We're here for you</p>
                        </div>
                    </div>
                </div>
                
                <div className="absolute bottom-6 left-12 text-xs text-gray-400">
                    © {new Date().getFullYear()} Aurelian Health. {portalSlug !== 'patient' ? `${config.displayName} Access.` : 'All rights reserved.'}
                </div>
            </div>

            {/* ── Right Pane (Form) ────────────────────────────────────────── */}
            <div className="w-full lg:w-1/2 flex flex-col justify-center items-center p-8 sm:p-12 relative bg-white">
                <div className="w-full max-w-md">
                    {/* Error Handling */}
                    <div aria-live="assertive" aria-atomic="true" id="login-error-region">
                        {error && (
                            <motion.div variants={fadeIn} className="bg-red-50 border border-red-200 text-red-600 p-4 rounded-xl mb-6 text-sm">
                                {typeof error === 'string' ? error : error?.message || 'Authentication failed.'}
                            </motion.div>
                        )}
                    </div>

                    {forgotStep === 1 ? (
                        <form onSubmit={handleForgotRequest} className="w-full">
                            <div className="text-center mb-10">
                                <h2 className="text-3xl font-bold text-gray-900 mb-2">Reset Password</h2>
                                <p className="text-sm text-gray-500">Enter your email to receive a reset code.</p>
                            </div>
                            <div className="space-y-4 mb-6">
                                <div>
                                    <label className="block text-xs font-semibold text-gray-700 mb-1.5">Email Address</label>
                                    <div className="relative">
                                        <Mail className="absolute left-4 top-1/2 -translate-y-1/2 text-gray-400 w-4 h-4" />
                                        <input 
                                            type="email" required value={email} onChange={e => setEmail(e.target.value)}
                                            className="w-full border border-gray-200 rounded-xl py-3 pl-11 pr-4 text-sm focus:border-[#5b21b6] focus:ring-1 focus:ring-[#5b21b6] outline-none" 
                                            placeholder="Enter your email" 
                                        />
                                    </div>
                                </div>
                            </div>
                            <button type="submit" disabled={isLoading} className="w-full bg-[#5b21b6] hover:bg-[#4c1d95] text-white py-3.5 rounded-xl text-sm font-semibold transition-colors mb-3">
                                {isLoading ? 'Sending...' : 'Send Reset Code'}
                            </button>
                            <button type="button" onClick={() => setForgotStep(0)} className="w-full text-[#5b21b6] text-sm font-semibold hover:underline">
                                Back to Sign In
                            </button>
                        </form>
                    ) : forgotStep === 2 ? (
                        <form onSubmit={handleResetPassword} className="w-full">
                            <div className="text-center mb-10">
                                <h2 className="text-3xl font-bold text-gray-900 mb-2">Create New Password</h2>
                                <p className="text-sm text-gray-500">Enter the code sent to {email}</p>
                            </div>
                            <div className="space-y-4 mb-6">
                                <div>
                                    <label className="block text-xs font-semibold text-gray-700 mb-1.5">Reset Code</label>
                                    <input type="text" required value={otp} onChange={e => setOtp(e.target.value)} className="w-full border border-gray-200 rounded-xl py-3 px-4 text-center tracking-widest font-mono text-sm focus:border-[#5b21b6] focus:ring-1 focus:ring-[#5b21b6] outline-none" placeholder="123456" />
                                </div>
                                <div>
                                    <label className="block text-xs font-semibold text-gray-700 mb-1.5">New Password</label>
                                    <div className="relative">
                                        <Lock className="absolute left-4 top-1/2 -translate-y-1/2 text-gray-400 w-4 h-4" />
                                        <input type={showPassword ? "text" : "password"} required value={newPassword} onChange={e => setNewPassword(e.target.value)} className="w-full border border-gray-200 rounded-xl py-3 pl-11 pr-10 text-sm focus:border-[#5b21b6] focus:ring-1 focus:ring-[#5b21b6] outline-none" placeholder="Create a password" />
                                        <button type="button" onClick={() => setShowPassword(!showPassword)} className="absolute right-4 top-1/2 -translate-y-1/2 text-gray-400 hover:text-gray-600">
                                            {showPassword ? <EyeOff className="w-4 h-4" /> : <Eye className="w-4 h-4" />}
                                        </button>
                                    </div>
                                </div>
                            </div>
                            <button type="submit" disabled={isLoading} className="w-full bg-[#5b21b6] hover:bg-[#4c1d95] text-white py-3.5 rounded-xl text-sm font-semibold transition-colors mb-3">
                                {isLoading ? 'Resetting...' : 'Reset Password'}
                            </button>
                            <button type="button" onClick={() => { setForgotStep(0); setOtp(''); setNewPassword(''); }} className="w-full text-[#5b21b6] text-sm font-semibold hover:underline">
                                Cancel
                            </button>
                        </form>
                    ) : !mfaPending ? (
                        <form onSubmit={handleLogin} className="w-full">
                            <div className="text-center mb-10">
                                <h2 className="text-3xl font-bold text-gray-900 mb-2">Hello Again! 👋</h2>
                                <p className="text-sm text-gray-500">Sign in to your account to continue</p>
                            </div>

                            <div className="space-y-4 mb-6">
                                <div>
                                    <label className="block text-xs font-semibold text-gray-700 mb-1.5">
                                        {portalSlug === 'patient' ? 'Email or Phone' : 'Staff Email'}
                                    </label>
                                    <div className="relative">
                                        <div className="absolute left-4 top-1/2 -translate-y-1/2">
                                            <svg className="w-4 h-4 text-[#5b21b6]/60" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z"></path></svg>
                                        </div>
                                        <input 
                                            type="email" required value={email} onChange={e => setEmail(e.target.value)}
                                            className="w-full border border-gray-200 rounded-xl py-3 pl-11 pr-4 text-sm focus:border-[#5b21b6] focus:ring-1 focus:ring-[#5b21b6] outline-none transition-shadow" 
                                            placeholder="Enter your email or phone number" 
                                        />
                                    </div>
                                </div>

                                <div>
                                    <label className="block text-xs font-semibold text-gray-700 mb-1.5">Password</label>
                                    <div className="relative">
                                        <Lock className="absolute left-4 top-1/2 -translate-y-1/2 text-[#5b21b6]/60 w-4 h-4" />
                                        <input 
                                            type={showPassword ? "text" : "password"} required value={password} onChange={e => setPassword(e.target.value)}
                                            className="w-full border border-gray-200 rounded-xl py-3 pl-11 pr-10 text-sm focus:border-[#5b21b6] focus:ring-1 focus:ring-[#5b21b6] outline-none transition-shadow" 
                                            placeholder="Enter your password" 
                                        />
                                        <button type="button" onClick={() => setShowPassword(!showPassword)} className="absolute right-4 top-1/2 -translate-y-1/2 text-gray-400 hover:text-gray-600">
                                            {showPassword ? <EyeOff className="w-4 h-4" /> : <Eye className="w-4 h-4" />}
                                        </button>
                                    </div>
                                </div>
                            </div>

                            <div className="flex items-center justify-between mb-8">
                                <label className="flex items-center gap-2 cursor-pointer">
                                    <input type="checkbox" checked={rememberMe} onChange={e => setRememberMe(e.target.checked)} className="w-4 h-4 rounded border-gray-300 text-[#5b21b6] focus:ring-[#5b21b6]" />
                                    <span className="text-sm text-gray-600 font-medium">Remember me</span>
                                </label>
                                <button type="button" onClick={() => setForgotStep(1)} className="text-sm font-bold text-[#5b21b6] hover:underline">
                                    Forgot Password?
                                </button>
                            </div>

                            <button type="submit" disabled={isLoading} className="w-full bg-[#5b21b6] hover:bg-[#4c1d95] text-white py-3.5 rounded-xl text-sm font-semibold transition-colors flex items-center justify-center gap-2 mb-8">
                                <Lock className="w-4 h-4" />
                                {isLoading ? 'Authenticating...' : 'Sign In'}
                            </button>

                            {/* OAuth Section (Patient Only) */}
                            {portalSlug === 'patient' && (
                                <>
                                    <div className="relative flex items-center justify-center mb-8">
                                        <div className="absolute inset-0 flex items-center">
                                            <div className="w-full border-t border-gray-200"></div>
                                        </div>
                                        <span className="relative bg-white px-4 text-xs text-gray-400">or continue with</span>
                                    </div>

                                    <div className="space-y-3">
                                        <button type="button" className="w-full flex items-center justify-center gap-3 bg-white border border-gray-200 hover:bg-gray-50 text-gray-700 py-3 rounded-xl text-sm font-semibold transition-colors">
                                            <svg className="w-5 h-5" viewBox="0 0 24 24"><path fill="#4285F4" d="M22.56 12.25c0-.78-.07-1.53-.2-2.25H12v4.26h5.92c-.26 1.37-1.04 2.53-2.21 3.31v2.77h3.57c2.08-1.92 3.28-4.74 3.28-8.09z"/><path fill="#34A853" d="M12 23c2.97 0 5.46-.98 7.28-2.66l-3.57-2.77c-.98.66-2.23 1.06-3.71 1.06-2.86 0-5.29-1.93-6.16-4.53H2.18v2.84C3.99 20.53 7.7 23 12 23z"/><path fill="#FBBC05" d="M5.84 14.09c-.22-.66-.35-1.36-.35-2.09s.13-1.43.35-2.09V7.07H2.18C1.43 8.55 1 10.22 1 12s.43 3.45 1.18 4.93l2.85-2.22.81-.62z"/><path fill="#EA4335" d="M12 5.38c1.62 0 3.06.56 4.21 1.64l3.15-3.15C17.45 2.09 14.97 1 12 1 7.7 1 3.99 3.47 2.18 7.07l3.66 2.84c.87-2.6 3.3-4.53 6.16-4.53z"/></svg>
                                            Continue with Google
                                        </button>
                                        <button type="button" className="w-full flex items-center justify-center gap-3 bg-white border border-gray-200 hover:bg-gray-50 text-gray-700 py-3 rounded-xl text-sm font-semibold transition-colors">
                                            <svg className="w-5 h-5" fill="currentColor" viewBox="0 0 24 24"><path d="M12 2C6.477 2 2 6.477 2 12c0 4.991 3.657 9.128 8.438 9.879V14.89h-2.54V12h2.54V9.797c0-2.506 1.492-3.89 3.777-3.89 1.094 0 2.238.195 2.238.195v2.46h-1.26c-1.243 0-1.63.771-1.63 1.562V12h2.773l-.443 2.89h-2.33v6.989C18.343 21.129 22 16.99 22 12c0-5.523-4.477-10-10-10z"/></svg>
                                            Continue with Facebook
                                        </button>
                                        <button type="button" className="w-full flex items-center justify-center gap-3 bg-white border border-gray-200 hover:bg-gray-50 text-gray-700 py-3 rounded-xl text-sm font-semibold transition-colors">
                                            <svg className="w-5 h-5" fill="currentColor" viewBox="0 0 24 24"><path d="M17.05 20.28c-.98.74-2.039 1.49-3.26 1.52-1.24.03-1.63-.73-3.04-.73-1.42 0-1.84.72-3.04.76-1.25.04-2.42-.8-3.41-2.24-2.04-2.93-3.6-8.29-1.52-11.9 1.03-1.8 2.87-2.95 4.9-2.98 1.2-.02 2.32.8 3.01.8.7 0 2.06-1 3.48-.85 1.47.05 2.8.71 3.55 1.83-3.08 1.88-2.58 6.13.43 7.37-.73 1.76-1.58 3.51-3.1 5.42zm-3.67-17.75c.61-.75 1.02-1.8 1.02-2.85-.97.05-2.08.68-2.73 1.45-.58.69-1.05 1.78-.9 2.8 1.05.09 2.03-.64 2.61-1.4z"/></svg>
                                            Continue with Apple
                                        </button>
                                    </div>
                                </>
                            )}

                            {portalSlug === 'patient' && (
                                <p className="mt-8 text-center text-sm text-gray-500">
                                    Don't have an account? <Link to="/register" className="font-bold text-[#5b21b6] hover:underline">Sign Up</Link>
                                </p>
                            )}
                        </form>
                    ) : (
                        <form onSubmit={handleMfa} className="w-full">
                            <div className="text-center mb-10">
                                <h2 className="text-3xl font-bold text-gray-900 mb-2">Security Verification</h2>
                                <p className="text-sm text-gray-500">Enter the code sent to {mfaEmail}</p>
                            </div>
                            <div className="space-y-4 mb-6">
                                <div>
                                    <label className="block text-xs font-semibold text-gray-700 mb-1.5">Verification Code</label>
                                    <input type="text" required value={otp} onChange={e => setOtp(e.target.value)} className="w-full border border-gray-200 rounded-xl py-3 px-4 text-center tracking-widest font-mono text-lg focus:border-[#5b21b6] focus:ring-1 focus:ring-[#5b21b6] outline-none" placeholder="123456" />
                                </div>
                            </div>
                            <button type="submit" disabled={isLoading} className="w-full bg-[#5b21b6] hover:bg-[#4c1d95] text-white py-3.5 rounded-xl text-sm font-semibold transition-colors mb-3">
                                {isLoading ? 'Verifying...' : 'Verify Code'}
                            </button>
                            <button type="button" onClick={() => window.location.reload()} className="w-full text-[#5b21b6] text-sm font-semibold hover:underline">
                                Back to Login
                            </button>
                        </form>
                    )}
                </div>
            </div>
        </div>
    );
}
