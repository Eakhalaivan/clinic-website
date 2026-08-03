import React, { useEffect } from 'react';
import { Link } from 'react-router-dom';

const Home = () => {
    useEffect(() => {
        const observerOptions = {
            root: null,
            threshold: 0.1,
            rootMargin: '0px'
        };

        const observer = new IntersectionObserver((entries) => {
            entries.forEach(entry => {
                if (entry.isIntersecting) {
                    entry.target.classList.add('opacity-100', 'translate-y-0');
                    entry.target.classList.remove('opacity-0', 'translate-y-10');
                }
            });
        }, observerOptions);

        document.querySelectorAll('section > div').forEach(section => {
            section.classList.add('transition-all', 'duration-1000', 'opacity-0', 'translate-y-10');
            observer.observe(section);
        });

        return () => observer.disconnect();
    }, []);

    return (
        <div className="bg-background text-on-surface font-body-md selection:bg-primary-fixed selection:text-on-primary-fixed">
            {/* TopNavBar */}
            <header className="bg-surface/80 dark:bg-surface-dim/80 docked full-width top-0 sticky backdrop-blur-md border-b border-outline-variant/30 z-50">
                <div className="flex justify-between items-center w-full px-10 py-4 max-w-container-max mx-auto">
                    <Link className="text-headline-sm font-headline-sm font-bold tracking-tight text-on-surface dark:text-inverse-on-surface" to="/">
                        Aurelian Health
                    </Link>
                    <nav className="hidden md:flex items-center gap-8">
                        <Link className="text-primary dark:text-primary-fixed-dim border-b-2 border-primary pb-1 font-label-md text-label-md" to="#">Consultations</Link>
                        <Link className="text-on-surface-variant dark:text-surface-variant hover:text-primary transition-colors font-label-md text-label-md" to="#">Specialists</Link>
                        <Link className="text-on-surface-variant dark:text-surface-variant hover:text-primary transition-colors font-label-md text-label-md" to="#">Health Records</Link>
                        <Link className="text-on-surface-variant dark:text-surface-variant hover:text-primary transition-colors font-label-md text-label-md" to="#">Concierge</Link>
                    </nav>
                    <div className="flex items-center gap-4">
                        <Link className="hidden sm:block text-primary font-label-md text-label-md hover:underline decoration-2 underline-offset-4" to="/patient/login">Sign In</Link>
                        <Link className="bg-primary text-on-primary px-6 py-3 font-label-md text-label-md rounded-lg hover:opacity-90 transition-all" to="/doctors">Book Visit</Link>
                    </div>
                </div>
            </header>

            {/* Hero Section */}
            <section className="relative h-[90vh] flex items-center overflow-hidden">
                <div className="absolute inset-0 z-0">
                    <img alt="Aurelian Health Clinic Interior" className="w-full h-full object-cover" src="https://lh3.googleusercontent.com/aida/AP1WRLv8OSlAcuPPlu_u0oM5FYH33ZIeCnun4D2wBzNdPpJrmDPBLDJz3p-x939_Mbq7V5X_dU0_90oSTIRhobH410qJAz3aw95bIl0YOJyRkLWr0Asb3_mbxeKFNa80tHaLpX_i_U9lpu_cvExeEs5K_ptdmpw-yvQoUthr_cG5HQaBerz4qfSEUu7IYldAj7JaPWW6SwJ9jIkgeI2Oi_Ks8IOOyjDNlG2awKmD0plxZwRuwA9OCPhZGZCdGY8g"/>
                    <div className="absolute inset-0 bg-gradient-to-r from-on-surface/20 to-transparent"></div>
                </div>
                <div className="container-max mx-auto px-margin-desktop relative z-10">
                    <div className="glass-card max-w-2xl p-12 rounded-xl border-l-4 border-l-gold shadow-2xl animate-fade-in-up">
                        <span className="text-label-md font-label-md text-primary tracking-[0.2em] uppercase mb-4 block">Ultra-Premium Care</span>
                        <h1 className="font-headline-lg text-headline-lg text-on-surface mb-6 leading-tight">Medical Excellence Redefined</h1>
                        <p className="text-body-lg font-body-lg text-on-surface-variant mb-10 max-w-lg">
                            Experience a sanctuary of precision medicine where clinical mastery meets unparalleled concierge service. Our private practice is designed for those who demand the absolute standard in health.
                        </p>
                        <div className="flex flex-col sm:flex-row gap-4">
                            <Link to="/patient/register" className="bg-primary text-on-primary px-8 py-4 font-label-md text-label-md rounded-lg hover:opacity-90 transition-all flex items-center justify-center gap-2">
                                Request Private Consultation
                                <span className="material-symbols-outlined text-sm">arrow_forward</span>
                            </Link>
                            <Link to="/doctors" className="border border-outline text-on-surface px-8 py-4 font-label-md text-label-md rounded-lg hover:bg-surface-container transition-all text-center">
                                Explore Our Specializations
                            </Link>
                        </div>
                    </div>
                </div>
            </section>

            {/* Stats Section */}
            <section className="bg-surface-container-lowest py-16 border-b border-outline-variant/20">
                <div className="max-w-container-max mx-auto px-margin-desktop">
                    <div className="grid grid-cols-2 md:grid-cols-4 gap-gutter text-center">
                        <div className="space-y-2">
                            <h3 className="text-headline-md font-headline-md text-primary">12+</h3>
                            <p className="text-label-md font-label-md text-on-surface-variant uppercase tracking-widest">Medical Disciplines</p>
                        </div>
                        <div className="space-y-2">
                            <h3 className="text-headline-md font-headline-md text-gold">4.9/5</h3>
                            <p className="text-label-md font-label-md text-on-surface-variant uppercase tracking-widest">Patient Excellence</p>
                        </div>
                        <div className="space-y-2">
                            <h3 className="text-headline-md font-headline-md text-primary">150+</h3>
                            <p className="text-label-md font-label-md text-on-surface-variant uppercase tracking-widest">Private Specialists</p>
                        </div>
                        <div className="space-y-2">
                            <h3 className="text-headline-md font-headline-md text-gold">24/7</h3>
                            <p className="text-label-md font-label-md text-on-surface-variant uppercase tracking-widest">Concierge Support</p>
                        </div>
                    </div>
                </div>
            </section>

            {/* Services Section */}
            <section className="section-padding bg-background">
                <div className="max-w-container-max mx-auto px-margin-desktop">
                    <div className="flex flex-col md:flex-row md:items-end justify-between mb-16 gap-4">
                        <div className="max-w-xl">
                            <h2 className="text-headline-md font-headline-md mb-4">Clinical Specializations</h2>
                            <p className="text-body-md font-body-md text-on-surface-variant">We provide a curated ecosystem of health services, each delivered by industry leaders utilizing cutting-edge diagnostic technology.</p>
                        </div>
                        <Link className="text-primary font-label-md text-label-md border-b-2 border-primary hover:opacity-70 transition-opacity" to="/doctors">View All Specializations</Link>
                    </div>
                    <div className="grid grid-cols-1 md:grid-cols-3 gap-10">
                        {/* Service 1 */}
                        <div className="group bg-surface p-10 rounded-xl border border-outline-variant/30 hover:border-gold/50 transition-all duration-500 hover:-translate-y-2">
                            <div className="w-14 h-14 bg-surface-container-high rounded-full flex items-center justify-center mb-8 group-hover:bg-primary-container transition-colors duration-500">
                                <span className="material-symbols-outlined text-primary group-hover:text-on-primary-container" style={{fontVariationSettings: "'opsz' 32"}}>cardiology</span>
                            </div>
                            <h4 className="text-headline-sm font-headline-sm mb-4">Executive Cardiology</h4>
                            <p className="text-body-md font-body-md text-on-surface-variant mb-6">Advanced cardiovascular screenings and personalized heart health strategies for peak performance.</p>
                            <ul className="space-y-3 mb-8">
                                <li className="flex items-center gap-3 text-label-md font-label-md text-on-surface-variant">
                                    <span className="material-symbols-outlined text-gold text-sm">check_circle</span> 3D Stress Testing
                                </li>
                                <li className="flex items-center gap-3 text-label-md font-label-md text-on-surface-variant">
                                    <span className="material-symbols-outlined text-gold text-sm">check_circle</span> Calcium Scoring
                                </li>
                            </ul>
                            <Link className="flex items-center gap-2 text-primary font-label-md text-label-md group-hover:gap-4 transition-all" to="/doctors">
                                Learn More <span className="material-symbols-outlined text-sm">arrow_forward</span>
                            </Link>
                        </div>
                        {/* Service 2 */}
                        <div className="group bg-surface p-10 rounded-xl border border-outline-variant/30 hover:border-gold/50 transition-all duration-500 hover:-translate-y-2">
                            <div className="w-14 h-14 bg-surface-container-high rounded-full flex items-center justify-center mb-8 group-hover:bg-primary-container transition-colors duration-500">
                                <span className="material-symbols-outlined text-primary group-hover:text-on-primary-container" style={{fontVariationSettings: "'opsz' 32"}}>neurology</span>
                            </div>
                            <h4 className="text-headline-sm font-headline-sm mb-4">Precision Neurology</h4>
                            <p className="text-body-md font-body-md text-on-surface-variant mb-6">Cutting-edge neuro-imaging and cognitive optimization protocols designed for professional mental stamina.</p>
                            <ul className="space-y-3 mb-8">
                                <li className="flex items-center gap-3 text-label-md font-label-md text-on-surface-variant">
                                    <span className="material-symbols-outlined text-gold text-sm">check_circle</span> Brain-Map Analysis
                                </li>
                                <li className="flex items-center gap-3 text-label-md font-label-md text-on-surface-variant">
                                    <span className="material-symbols-outlined text-gold text-sm">check_circle</span> Sleep Architecture
                                </li>
                            </ul>
                            <Link className="flex items-center gap-2 text-primary font-label-md text-label-md group-hover:gap-4 transition-all" to="/doctors">
                                Learn More <span className="material-symbols-outlined text-sm">arrow_forward</span>
                            </Link>
                        </div>
                        {/* Service 3 */}
                        <div className="group bg-surface p-10 rounded-xl border border-outline-variant/30 hover:border-gold/50 transition-all duration-500 hover:-translate-y-2">
                            <div className="w-14 h-14 bg-surface-container-high rounded-full flex items-center justify-center mb-8 group-hover:bg-primary-container transition-colors duration-500">
                                <span className="material-symbols-outlined text-primary group-hover:text-on-primary-container" style={{fontVariationSettings: "'opsz' 32"}}>genetics</span>
                            </div>
                            <h4 className="text-headline-sm font-headline-sm mb-4">Genomic Wellness</h4>
                            <p className="text-body-md font-body-md text-on-surface-variant mb-6">Comprehensive DNA sequencing to identify longevity pathways and preventative medical interventions.</p>
                            <ul className="space-y-3 mb-8">
                                <li className="flex items-center gap-3 text-label-md font-label-md text-on-surface-variant">
                                    <span className="material-symbols-outlined text-gold text-sm">check_circle</span> Pharmacogenomics
                                </li>
                                <li className="flex items-center gap-3 text-label-md font-label-md text-on-surface-variant">
                                    <span className="material-symbols-outlined text-gold text-sm">check_circle</span> Epigenetic Tracking
                                </li>
                            </ul>
                            <Link className="flex items-center gap-2 text-primary font-label-md text-label-md group-hover:gap-4 transition-all" to="/doctors">
                                Learn More <span className="material-symbols-outlined text-sm">arrow_forward</span>
                            </Link>
                        </div>
                    </div>
                </div>
            </section>

            {/* Philosophy: The Aurelian Standard */}
            <section className="section-padding bg-surface-container-low overflow-hidden">
                <div className="max-w-container-max mx-auto px-margin-desktop">
                    <div className="flex flex-col lg:flex-row items-center gap-16">
                        <div className="w-full lg:w-1/2 relative">
                            <div className="absolute -top-4 -left-4 w-32 h-32 bg-gold/10 rounded-full blur-3xl"></div>
                            <div className="relative z-10 rounded-2xl overflow-hidden shadow-2xl border border-outline-variant/30">
                                <img className="w-full h-[600px] object-cover" alt="A macro shot of a sophisticated medical device in a pristine white clinic setting, highlighted by clean architectural lighting and subtle gold metallic accents. The atmosphere is clinical yet artistic, focusing on precision and high-tech elegance." src="https://lh3.googleusercontent.com/aida-public/AB6AXuCLmpGCF9SspCF2UGlTqSbimRlgSGMQWU6zciSSGzJ6NlcA-d8ao64fiIGhHrXfLYrzmx2Avug0TDFVFnfmkyHFbCr9vGY2cZ08yKnp9HKazpTG9M6TCOzB05egl3pTLGkDnUqETRc3MnTlrVLb6DpeyRWJFg_LJL1GWfaroyxD4lSmS5ryRrzVgmX_hl2PKvge1cXuLtcEI5lRmFA3ml4t4Z6B7ZzXwZ6iFNgKHum_zzWJXgSIiGFHop4f_Pc-8tLxum5G_xzefS_-"/>
                            </div>
                        </div>
                        <div className="w-full lg:w-1/2 space-y-8">
                            <span className="text-label-md font-label-md text-gold tracking-widest uppercase">The Philosophy</span>
                            <h2 className="text-headline-lg font-headline-lg text-on-surface leading-tight">The Aurelian Standard</h2>
                            <p className="text-body-lg font-body-lg text-on-surface-variant">
                                Our approach transcends the traditional medical visit. We believe in "Quiet Authority"—a clinical environment that provides absolute clarity and peace of mind through meticulously managed data and hyper-personalized care pathways.
                            </p>
                            <div className="space-y-6">
                                <div className="flex gap-6">
                                    <div className="shrink-0 w-12 h-12 rounded-lg bg-surface border border-gold/30 flex items-center justify-center">
                                        <span className="material-symbols-outlined text-gold">verified</span>
                                    </div>
                                    <div>
                                        <h5 className="text-headline-sm font-headline-sm text-sm mb-1">Unrivaled Privacy</h5>
                                        <p className="text-body-md font-body-md text-on-surface-variant">Secured private entrances and encrypted health record systems ensuring complete discretion.</p>
                                    </div>
                                </div>
                                <div className="flex gap-6">
                                    <div className="shrink-0 w-12 h-12 rounded-lg bg-surface border border-gold/30 flex items-center justify-center">
                                        <span className="material-symbols-outlined text-gold">hourglass_empty</span>
                                    </div>
                                    <div>
                                        <h5 className="text-headline-sm font-headline-sm text-sm mb-1">Time Integrity</h5>
                                        <p className="text-body-md font-body-md text-on-surface-variant">Zero-wait policy. Every appointment is scheduled with generous buffers for deep consultation.</p>
                                    </div>
                                </div>
                            </div>
                            <Link to="/patient/register" className="bg-on-surface text-surface px-10 py-4 font-label-md text-label-md rounded-lg hover:bg-on-surface/90 transition-all mt-4 inline-block">
                                Discover Our Patient Journey
                            </Link>
                        </div>
                    </div>
                </div>
            </section>

            {/* Social Proof / Testimonials */}
            <section className="section-padding bg-surface">
                <div className="max-w-container-max mx-auto px-margin-desktop text-center">
                    <h2 className="text-headline-md font-headline-md mb-16">Trusted by Discerning Clients</h2>
                    <div className="grid grid-cols-1 md:grid-cols-2 gap-10">
                        <div className="p-12 bg-background border border-outline-variant/20 rounded-xl relative">
                            <span className="material-symbols-outlined text-gold/20 absolute top-8 right-12 text-6xl">format_quote</span>
                            <p className="text-headline-sm font-headline-sm italic text-on-surface mb-8 leading-relaxed">
                                "The level of clinical precision at Aurelian is matched only by their incredible attention to the patient experience. It is the only place I trust with my family's complex healthcare needs."
                            </p>
                            <div className="flex flex-col items-center">
                                <div className="w-16 h-16 rounded-full overflow-hidden mb-4 border-2 border-gold/20">
                                    <img className="w-full h-full object-cover" alt="Dr. Julian Vance" src="https://lh3.googleusercontent.com/aida-public/AB6AXuBuCP4fVe2Nis5vEDV5SLNyvKofM7zoZwYlewfNHn_2kMyxOdsl2eKOlHFO2z7b-XzfDwPZ8i4RH5lJJ3jWlyg-jT0pWOGAjrkeCmP2ac32h5MaCnlrq8ZoBRC1K4qNPzMgkBnooBcOzXENlrqQKfhMcLFyacoY_FUbRu9JlswjXWHetc2OpfNtbdol920po-iQ7F6L0NLhzex4kOUFbl1UNU9zyKApvDdmb9UU3y3Un2CiO6u0ZLy5hezdJR463uewurhxr87Yhsup"/>
                                </div>
                                <h6 className="font-label-md text-label-md text-on-surface">DR. JULIAN VANCE</h6>
                                <p className="font-label-sm text-label-sm text-on-surface-variant uppercase tracking-widest">Global Logistics Executive</p>
                            </div>
                        </div>
                        <div className="p-12 bg-background border border-outline-variant/20 rounded-xl relative">
                            <span className="material-symbols-outlined text-gold/20 absolute top-8 right-12 text-6xl">format_quote</span>
                            <p className="text-headline-sm font-headline-sm italic text-on-surface mb-8 leading-relaxed">
                                "Finally, a medical practice that understands the value of time and the power of preventive genomics. Aurelian is truly in a league of its own for high-performance medicine."
                            </p>
                            <div className="flex flex-col items-center">
                                <div className="w-16 h-16 rounded-full overflow-hidden mb-4 border-2 border-gold/20">
                                    <img className="w-full h-full object-cover" alt="Elena Rossi" src="https://lh3.googleusercontent.com/aida-public/AB6AXuCB0s_T-gdG4iMzGpuCErrudFhapNSSEV0NnnMeabVnzHtRrzyhKcf9iDyWegY5Qxu55wBOOfLIhpTyN7cMbBznH3GCeah7Rq6poCrzlB8WzJhBLm4363m0tGqqtEVTRFsDkeJJG-0zMStH16tjszBzCoOySlxpv6C3_7yomOIFFkPhNxdNOkyESO-nWx4BC2Vv68AoDTyjkoiwjIxn5wxL-r0Go_rvGZyqDNFf36zEFWmBKtCpTiOKlr-RNYT9myYmTN0Lnb58bc8F"/>
                                </div>
                                <h6 className="font-label-md text-label-md text-on-surface">ELENA ROSSI</h6>
                                <p className="font-label-sm text-label-sm text-on-surface-variant uppercase tracking-widest">Philanthropist &amp; Board Director</p>
                            </div>
                        </div>
                    </div>
                </div>
            </section>

            {/* Footer */}
            <footer className="bg-surface-container-low border-t border-outline-variant/20">
                <div className="flex flex-col md:flex-row justify-between items-center w-full px-10 py-16 max-w-container-max mx-auto gap-gutter">
                    <div className="flex flex-col items-center md:items-start gap-4">
                        <Link className="text-headline-sm font-headline-sm font-semibold text-on-surface" to="/">Aurelian Health</Link>
                        <p className="text-body-md font-body-md text-on-surface-variant max-w-xs text-center md:text-left">
                            Redefining medical excellence through precision, privacy, and performance.
                        </p>
                    </div>
                    <div className="flex flex-wrap justify-center gap-8">
                        <Link className="text-on-surface-variant hover:text-primary transition-colors duration-200 font-label-sm text-label-sm" to="#">Privacy Policy</Link>
                        <Link className="text-on-surface-variant hover:text-primary transition-colors duration-200 font-label-sm text-label-sm" to="#">Terms of Service</Link>
                        <Link className="text-on-surface-variant hover:text-primary transition-colors duration-200 font-label-sm text-label-sm" to="#">Security Compliance</Link>
                        <Link className="text-on-surface-variant hover:text-primary transition-colors duration-200 font-label-sm text-label-sm" to="#">Patient Rights</Link>
                        <Link className="text-on-surface-variant hover:text-primary transition-colors duration-200 font-label-sm text-label-sm" to="#">Careers</Link>
                    </div>
                </div>
                <div className="w-full py-8 border-t border-outline-variant/10 text-center">
                    <p className="text-label-sm font-label-sm text-on-secondary-container opacity-60">
                        © 2024 Aurelian Health. All rights reserved. HIPAA Compliant &amp; Secure.
                    </p>
                </div>
            </footer>
        </div>
    );
};

export default Home;
