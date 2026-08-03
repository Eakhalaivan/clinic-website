import React, { useState, useEffect } from 'react';
import { useParams, useNavigate, Link } from 'react-router-dom';
import useAuthStore, { isTokenValid } from '../../store/authStore';

import { getPortalConfig, PORTAL_CONFIGS } from '../../config/portalConfig';
import { motion } from 'framer-motion';
import { ShieldCheck, Lock, Mail, Key, ArrowRight, Activity, Headset } from 'lucide-react';
import Button from '../../components/ui/Button';
import FormField from '../../components/ui/FormField';
import { fadeIn, scaleIn } from '../../components/ui/motion';

export default function PortalLoginPage() {
    const { portalSlug = 'patient' } = useParams();
    const navigate = useNavigate();
    const config = getPortalConfig(portalSlug);
    const authConfig = config.authConfig || {};
    
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [showPassword, setShowPassword] = useState(false);
    const [rememberMe, setRememberMe] = useState(false);
    const [otp, setOtp] = useState('');
    const [forgotStep, setForgotStep] = useState(0); // 0: Login, 1: Request Reset, 2: Perform Reset
    const [newPassword, setNewPassword] = useState('');
    
    const { login: storeLogin, verifyMfa, mfaPending, error, isLoading, mfaEmail, clearError, token, roles, clearStaleToken, forgotPassword, resetPassword } = useAuthStore();

    // Resolve which dashboard route to navigate to after login
    const resolveTargetDashboard = (currentConfig) => {
        const userRoles = useAuthStore.getState().roles || [];
        const isSuperOrAdmin = userRoles.includes('ROLE_ADMIN') || userRoles.includes('ROLE_SUPER_ADMIN');
        
        if (isSuperOrAdmin) {
            return currentConfig.dashboardRoute || '/super-admin/dashboard';
        }
        
        if (currentConfig.role && userRoles.includes(currentConfig.role)) {
            return currentConfig.dashboardRoute;
        }

        const matchingPortal = PORTAL_CONFIGS.find(p => userRoles.includes(p.role));
        return matchingPortal ? matchingPortal.dashboardRoute : (currentConfig.dashboardRoute || '/');
    };

    // On mount: clear any expired token so the login form shows cleanly
    useEffect(() => {
        clearStaleToken?.();
    }, []); // eslint-disable-line react-hooks/exhaustive-deps

    // Redirect only if the token is still actually valid (not just present)
    useEffect(() => {
        if (isTokenValid(token) && roles?.length > 0) {
            const targetRoute = resolveTargetDashboard(config);
            navigate(targetRoute, { replace: true });
        }
    }, [token]); // eslint-disable-line react-hooks/exhaustive-deps

    useEffect(() => {
        clearError?.();
    }, [portalSlug, clearError]);

    const handleLogin = async (e) => {
        e.preventDefault();
        try {
            const success = await storeLogin(portalSlug, email, password);
            if (success) {
                const targetRoute = resolveTargetDashboard(config);
                navigate(targetRoute);
            }
        } catch (err) {
            console.error('Login failed:', err);
        }
    };

    const handleMfa = async (e) => {
        e.preventDefault();
        const success = await verifyMfa(portalSlug, mfaEmail, otp);
        if (success) {
            const targetRoute = resolveTargetDashboard(config);
            navigate(targetRoute);
        }
    };

    const handleForgotRequest = async (e) => {
        e.preventDefault();
        const success = await forgotPassword(email);
        if (success) {
            setForgotStep(2);
        }
    };

    const handleResetPassword = async (e) => {
        e.preventDefault();
        const success = await resetPassword(email, otp, newPassword);
        if (success) {
            setForgotStep(0);
            setPassword('');
            setOtp('');
            setNewPassword('');
            // You can optionally show a success toast here
        }
    };

    return (
        <div className="min-h-screen relative flex flex-col items-center justify-center p-4 font-sans text-on-surface">
            {/* Blurred Background */}
            <div className="absolute inset-0 z-0">
                <img 
                    src="/assets/clinic_hero.png" 
                    alt="Aurelian Health Clinic Interior" 
                    className="w-full h-full object-cover blur-sm scale-105"
                />
                <div className="absolute inset-0 bg-surface/30 backdrop-blur-md"></div>
            </div>

            <div className="relative z-10 w-full max-w-md flex flex-col items-center">
                {/* Top Branding */}
                <div className="mb-8 flex flex-col items-center text-center">
                    <span className="material-symbols-outlined text-primary text-4xl mb-2">medical_services</span>
                    <h2 className="font-headline-sm text-2xl font-bold text-primary tracking-tight">Aurelian Health</h2>
                    <p className="text-xs font-bold uppercase tracking-[0.2em] text-on-surface-variant mt-1">
                        Privé {portalSlug === 'patient' ? 'Patient' : 'Administrative'} Access
                    </p>
                </div>

                {/* Main Card */}
                <motion.div 
                    initial="hidden" 
                    animate="visible" 
                    variants={scaleIn}
                    className="w-full bg-surface/95 backdrop-blur-xl rounded-xl shadow-2xl overflow-hidden border border-white/40 p-8 sm:p-10"
                >
                    <div className="text-center mb-8">
                        <h1 className="font-headline-md text-3xl font-bold text-primary mb-3">
                            {config.displayName} Portal
                        </h1>
                        <p className="text-sm text-on-surface-variant leading-relaxed">
                            {authConfig.heroSubtitle || `Secure access for ${config.displayName.toLowerCase()} management.`}
                        </p>
                    </div>

                    {/* Error Alert */}
                    {error && (
                        <motion.div 
                            variants={fadeIn}
                            className="bg-error-container border-l-4 border-error p-4 mb-6 rounded-r" 
                            role="alert"
                        >
                            <p className="text-xs font-semibold text-error">Authentication Error</p>
                            <p className="text-sm text-on-error-container mt-0.5 m-0">
                                {typeof error === 'string' 
                                    ? (error === 'Access denied for this portal.' 
                                        ? `This account isn't registered for the ${config.displayName} portal — check you're on the right login page.` 
                                        : error) 
                                    : (error?.message || error?.error || JSON.stringify(error))}
                            </p>
                        </motion.div>
                    )}

                    {/* Form */}
                    {forgotStep === 1 ? (
                        <form className="space-y-6" onSubmit={handleResetPassword}>
                            <div>
                                <label className="block text-xs font-bold text-on-surface-variant uppercase tracking-wider mb-2">
                                    Email Address
                                </label>
                                <div className="relative w-full mb-4">
                                    <span className="material-symbols-outlined absolute left-3.5 top-1/2 -translate-y-1/2 text-outline text-xl">mail</span>
                                    <input 
                                        type="email" 
                                        required 
                                        value={email}
                                        onChange={e => setEmail(e.target.value)}
                                        placeholder="doctor@clinic.com"
                                        className="w-full bg-surface-container-lowest border border-outline-variant rounded-lg py-3 pl-12 pr-4 text-sm text-on-surface focus:border-primary focus:ring-1 focus:ring-primary transition-all outline-none" 
                                    />
                                </div>
                                <label className="block text-xs font-bold text-on-surface-variant uppercase tracking-wider mb-2">
                                    New Password
                                </label>
                                <div className="relative w-full">
                                    <span className="material-symbols-outlined absolute left-3.5 top-1/2 -translate-y-1/2 text-outline text-xl">lock_reset</span>
                                    <input 
                                        type={showPassword ? "text" : "password"}
                                        required 
                                        value={newPassword}
                                        onChange={e => setNewPassword(e.target.value)}
                                        placeholder="••••••••"
                                        className="w-full bg-surface-container-lowest border border-outline-variant rounded-lg py-3 pl-12 pr-10 text-sm text-on-surface focus:border-primary focus:ring-1 focus:ring-primary transition-all outline-none" 
                                    />
                                    <button 
                                        type="button" 
                                        onClick={() => setShowPassword(!showPassword)}
                                        className="absolute right-3.5 top-1/2 -translate-y-1/2 text-outline hover:text-on-surface transition-colors"
                                    >
                                        <span className="material-symbols-outlined text-xl">
                                            {showPassword ? "visibility_off" : "visibility"}
                                        </span>
                                    </button>
                                </div>
                            </div>

                            <div className="pt-2">
                                <button 
                                    type="submit"
                                    disabled={isLoading}
                                    className="w-full bg-primary text-on-primary py-3.5 rounded-lg font-label-md flex items-center justify-center gap-2 hover:opacity-90 transition-opacity disabled:opacity-50 mb-3"
                                >
                                    {isLoading ? 'Resetting...' : 'Reset Password'}
                                </button>
                                <button
                                    type="button"
                                    onClick={() => { setForgotStep(0); setNewPassword(''); }}
                                    className="w-full bg-transparent text-primary py-2 rounded-lg font-label-md hover:bg-surface-variant transition-colors"
                                >
                                    Cancel
                                </button>
                            </div>
                        </form>
                    ) : !mfaPending ? (
                        <form className="space-y-6" onSubmit={handleLogin}>
                            <div>
                                <label htmlFor="email" className="block text-xs font-bold text-on-surface-variant uppercase tracking-wider mb-2">
                                    Staff ID / Username
                                </label>
                                <div className="relative w-full">
                                    <span className="material-symbols-outlined absolute left-3.5 top-1/2 -translate-y-1/2 text-outline text-xl">badge</span>
                                    <input 
                                        id="email"
                                        type="email" 
                                        required 
                                        value={email}
                                        onChange={e => setEmail(e.target.value)}
                                        placeholder="Enter administrative ID"
                                        className="w-full bg-surface-container-lowest border border-outline-variant rounded-lg py-3 pl-12 pr-4 text-sm text-on-surface focus:border-primary focus:ring-1 focus:ring-primary transition-all outline-none placeholder:text-outline-variant" 
                                    />
                                </div>
                            </div>

                            <div>
                                <div className="flex justify-between items-center mb-2">
                                    <label htmlFor="password" className="block text-xs font-bold text-on-surface-variant uppercase tracking-wider">
                                        Password
                                    </label>
                                    <button type="button" onClick={() => setForgotStep(1)} className="text-xs font-semibold text-primary hover:underline">
                                        Forgot Password?
                                    </button>
                                </div>
                                <div className="relative w-full">
                                    <span className="material-symbols-outlined absolute left-3.5 top-1/2 -translate-y-1/2 text-outline text-xl">lock</span>
                                    <input 
                                        id="password"
                                        type={showPassword ? "text" : "password"}
                                        required 
                                        value={password}
                                        onChange={e => setPassword(e.target.value)}
                                        placeholder="••••••••"
                                        className="w-full bg-surface-container-lowest border border-outline-variant rounded-lg py-3 pl-12 pr-10 text-sm text-on-surface focus:border-primary focus:ring-1 focus:ring-primary transition-all outline-none placeholder:text-outline-variant" 
                                    />
                                    <button 
                                        type="button" 
                                        onClick={() => setShowPassword(!showPassword)}
                                        className="absolute right-3.5 top-1/2 -translate-y-1/2 text-outline hover:text-on-surface transition-colors"
                                    >
                                        <span className="material-symbols-outlined text-xl">
                                            {showPassword ? "visibility_off" : "visibility"}
                                        </span>
                                    </button>
                                </div>
                            </div>

                            <div className="pt-2">
                                <button 
                                    type="submit"
                                    disabled={isLoading}
                                    className="w-full bg-primary text-on-primary py-3.5 rounded-lg font-label-md flex items-center justify-center gap-2 hover:opacity-90 transition-opacity disabled:opacity-50"
                                >
                                    {isLoading ? 'Authenticating...' : 'Access Dashboard'}
                                    {!isLoading && <ArrowRight className="w-4 h-4" />}
                                </button>
                            </div>
                        </form>
                    ) : forgotStep === 1 ? (
                        <form className="space-y-6" onSubmit={handleForgotRequest}>
                            <div>
                                <label htmlFor="reset-email" className="block text-xs font-bold text-on-surface-variant uppercase tracking-wider mb-2">
                                    Email Address
                                </label>
                                <p className="text-xs text-outline mb-2">Enter your email to receive a password reset code.</p>
                                <div className="relative w-full">
                                    <span className="material-symbols-outlined absolute left-3.5 top-1/2 -translate-y-1/2 text-outline text-xl">mail</span>
                                    <input 
                                        id="reset-email"
                                        type="email" 
                                        required 
                                        value={email}
                                        onChange={e => setEmail(e.target.value)}
                                        placeholder="doctor@clinic.com"
                                        className="w-full bg-surface-container-lowest border border-outline-variant rounded-lg py-3 pl-12 pr-4 text-sm text-on-surface focus:border-primary focus:ring-1 focus:ring-primary transition-all outline-none" 
                                    />
                                </div>
                            </div>

                            <div className="pt-2">
                                <button 
                                    type="submit"
                                    disabled={isLoading}
                                    className="w-full bg-primary text-on-primary py-3.5 rounded-lg font-label-md flex items-center justify-center gap-2 hover:opacity-90 transition-opacity disabled:opacity-50 mb-3"
                                >
                                    {isLoading ? 'Sending...' : 'Send Reset Code'}
                                </button>
                                <button
                                    type="button"
                                    onClick={() => setForgotStep(0)}
                                    className="w-full bg-transparent text-primary py-2 rounded-lg font-label-md hover:bg-surface-variant transition-colors"
                                >
                                    Back to Login
                                </button>
                            </div>
                        </form>
                    ) : forgotStep === 2 ? (
                        <form className="space-y-6" onSubmit={handleResetPassword}>
                            <div>
                                <label className="block text-xs font-bold text-on-surface-variant uppercase tracking-wider mb-2">
                                    Reset Code
                                </label>
                                <p className="text-xs text-outline mb-2">Code sent to {email}</p>
                                <div className="relative w-full mb-4">
                                    <span className="material-symbols-outlined absolute left-3.5 top-1/2 -translate-y-1/2 text-outline text-xl">key</span>
                                    <input 
                                        type="text" 
                                        required 
                                        value={otp}
                                        onChange={e => setOtp(e.target.value)}
                                        placeholder="123456"
                                        className="w-full bg-surface-container-lowest border border-outline-variant rounded-lg py-3 pl-12 pr-4 text-center text-lg tracking-widest font-mono text-on-surface focus:border-primary focus:ring-1 focus:ring-primary transition-all outline-none" 
                                    />
                                </div>
                                <label className="block text-xs font-bold text-on-surface-variant uppercase tracking-wider mb-2">
                                    New Password
                                </label>
                                <div className="relative w-full">
                                    <span className="material-symbols-outlined absolute left-3.5 top-1/2 -translate-y-1/2 text-outline text-xl">lock_reset</span>
                                    <input 
                                        type={showPassword ? "text" : "password"}
                                        required 
                                        value={newPassword}
                                        onChange={e => setNewPassword(e.target.value)}
                                        placeholder="••••••••"
                                        className="w-full bg-surface-container-lowest border border-outline-variant rounded-lg py-3 pl-12 pr-10 text-sm text-on-surface focus:border-primary focus:ring-1 focus:ring-primary transition-all outline-none" 
                                    />
                                    <button 
                                        type="button" 
                                        onClick={() => setShowPassword(!showPassword)}
                                        className="absolute right-3.5 top-1/2 -translate-y-1/2 text-outline hover:text-on-surface transition-colors"
                                    >
                                        <span className="material-symbols-outlined text-xl">
                                            {showPassword ? "visibility_off" : "visibility"}
                                        </span>
                                    </button>
                                </div>
                            </div>

                            <div className="pt-2">
                                <button 
                                    type="submit"
                                    disabled={isLoading}
                                    className="w-full bg-primary text-on-primary py-3.5 rounded-lg font-label-md flex items-center justify-center gap-2 hover:opacity-90 transition-opacity disabled:opacity-50 mb-3"
                                >
                                    {isLoading ? 'Resetting...' : 'Reset Password'}
                                </button>
                                <button
                                    type="button"
                                    onClick={() => { setForgotStep(0); setOtp(''); setNewPassword(''); }}
                                    className="w-full bg-transparent text-primary py-2 rounded-lg font-label-md hover:bg-surface-variant transition-colors"
                                >
                                    Cancel
                                </button>
                            </div>
                        </form>
                    ) : (
                        <form className="space-y-6" onSubmit={handleMfa}>
                            <div>
                                <label className="block text-xs font-bold text-on-surface-variant uppercase tracking-wider mb-2">
                                    Security Verification Code
                                </label>
                                <p className="text-xs text-outline mb-2">One-Time Passcode sent to {mfaEmail}</p>
                                <div className="relative w-full">
                                    <span className="material-symbols-outlined absolute left-3.5 top-1/2 -translate-y-1/2 text-outline text-xl">key</span>
                                    <input 
                                        id="otp"
                                        type="text" 
                                        required 
                                        value={otp}
                                        onChange={e => setOtp(e.target.value)}
                                        placeholder="123456"
                                        className="w-full bg-surface-container-lowest border border-outline-variant rounded-lg py-3 pl-12 pr-4 text-center text-lg tracking-widest font-mono text-on-surface focus:border-primary focus:ring-1 focus:ring-primary transition-all outline-none" 
                                    />
                                </div>
                            </div>

                            <div className="pt-2">
                                <button 
                                    type="submit"
                                    disabled={isLoading}
                                    className="w-full bg-primary text-on-primary py-3.5 rounded-lg font-label-md flex items-center justify-center gap-2 hover:opacity-90 transition-opacity disabled:opacity-50 mb-3"
                                >
                                    {isLoading ? 'Verifying...' : 'Verify OTP Code'}
                                </button>
                                <button
                                    type="button"
                                    onClick={() => window.location.reload()}
                                    className="w-full bg-transparent text-primary py-2 rounded-lg font-label-md hover:bg-surface-variant transition-colors"
                                >
                                    Back to Login
                                </button>
                            </div>
                        </form>
                    )}

                    {/* Card Footer */}
                    <div className="mt-8 pt-6 border-t border-outline-variant/30 text-center">
                        <button className="flex items-center justify-center gap-2 mx-auto text-sm font-semibold text-on-surface-variant hover:text-primary transition-colors">
                            <Headset className="w-4 h-4" />
                            System Support
                        </button>
                    </div>
                </motion.div>

                {/* Page Footer */}
                <div className="mt-12 text-center w-full">
                    <div className="flex items-center justify-center gap-8 mb-6">
                        <div className="flex items-center gap-2 text-xs font-medium text-on-surface-variant">
                            <ShieldCheck className="w-4 h-4 text-primary" />
                            Enterprise Level Encryption
                        </div>
                        <div className="flex items-center gap-2 text-xs font-medium text-on-surface-variant">
                            <ShieldCheck className="w-4 h-4 text-primary" />
                            HIPAA Compliant
                        </div>
                    </div>
                    <p className="text-xs text-on-surface-variant/70">
                        © 2024 Aurelian Health. Internal Administrative Portal v4.2.1
                    </p>
                </div>
            </div>
        </div>
    );
}
