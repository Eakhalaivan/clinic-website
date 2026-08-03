import React from 'react';
import { motion } from 'framer-motion';
import { Construction, ArrowLeft } from 'lucide-react';
import { useNavigate } from 'react-router-dom';
import { fadeIn, scaleIn } from '../../components/ui/motion';
import WipBanner from '../../components/ui/WipBanner';

/**
 * PlaceholderPage — renders for any portal sub-route that hasn't been built yet.
 * @param {Object} props
 * @param {string} [props.title]     - Custom page title override
 * @param {string} [props.subtitle]  - Custom subtitle override
 */
export default function PlaceholderPage({ title, subtitle }) {
  const navigate = useNavigate();
  // Derive title from URL if not passed
  const pageTitle = title || (() => {
    const seg = window.location.pathname.split('/').pop() ?? '';
    return seg
      .replace(/-/g, ' ')
      .replace(/\b\w/g, (c) => c.toUpperCase()) || 'Page';
  })();

  return (
    <motion.div
      className="min-h-[calc(100vh-64px)] flex flex-col items-center justify-center p-8"
      initial="hidden"
      animate="visible"
      variants={scaleIn}
    >
      <div className="w-full max-w-2xl mb-8">
        <WipBanner feature={pageTitle} note="This module is currently being built" />
      </div>
      <div className="max-w-md w-full text-center">
        <motion.div variants={fadeIn} className="flex justify-center mb-6">
          <div className="p-5 rounded-full bg-[var(--color-surface-alt)] border border-[var(--color-border)] shadow-sm">
            <Construction className="w-10 h-10 text-[var(--color-navy-600)]" />
          </div>
        </motion.div>

        <motion.h1
          variants={fadeIn}
          className="font-display font-bold text-2xl text-[var(--color-navy-900)] mb-2"
        >
          {pageTitle}
        </motion.h1>

        <motion.p
          variants={fadeIn}
          className="text-sm text-[var(--color-text-muted)] mb-8 leading-relaxed"
        >
          {subtitle || 'This module is currently being built. It will be available in the next release.'}
        </motion.p>

        <motion.div variants={fadeIn} className="flex items-center justify-center gap-3">
          <button
            onClick={() => navigate(-1)}
            className="inline-flex items-center gap-2 px-4 py-2 text-sm font-semibold
              bg-[var(--color-navy-800)] text-white rounded-sm hover:bg-[var(--color-navy-900)]
              transition-colors focus-visible:outline-none"
          >
            <ArrowLeft className="w-4 h-4" />
            Go Back
          </button>
        </motion.div>

        {/* Decorative dots */}
        <motion.div variants={fadeIn} className="flex justify-center gap-1.5 mt-10">
          {[0, 1, 2].map((i) => (
            <div
              key={i}
              className="w-1.5 h-1.5 rounded-full bg-[var(--color-border)]"
            />
          ))}
        </motion.div>
      </div>
    </motion.div>
  );
}
