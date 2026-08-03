import React from 'react';
import { motion } from 'framer-motion';

const renderIcon = (Icon, className) => {
  if (!Icon) return null;
  if (React.isValidElement(Icon)) return Icon;
  if (typeof Icon === 'function' || typeof Icon === 'object') {
    const IconComp = Icon;
    return <IconComp className={className} />;
  }
  return null;
};

/**
 * Enterprise Tabs Primitive
 * @param {Object} props
 * @param {Array<{ id: string, label: string, icon?: React.ReactNode, badge?: string|number }>} props.tabs
 * @param {string} props.activeTab
 * @param {Function} props.onChange
 */
export default function Tabs({
  tabs = [],
  activeTab,
  onChange,
  className = ''
}) {
  return (
    <div className={`flex items-center gap-1 border-b border-[var(--color-border)] overflow-x-auto no-scrollbar ${className}`}>
      {tabs.map((tab) => {
        const isActive = activeTab === tab.id;
        const Icon = tab.icon;

        return (
          <button
            key={tab.id}
            type="button"
            onClick={() => onChange(tab.id)}
            className={`relative px-4 py-3 text-sm font-medium transition-colors flex items-center gap-2 whitespace-nowrap focus-visible:outline-none ${
              isActive
                ? 'text-[var(--color-navy-800)] dark:text-[var(--color-navy-600)] font-semibold'
                : 'text-[var(--color-text-muted)] hover:text-[var(--color-text)]'
            }`}
          >
            {renderIcon(Icon, "w-4 h-4 shrink-0")}
            <span>{tab.label}</span>
            
            {tab.badge !== undefined && (
              <span className={`px-2 py-0.5 text-[11px] rounded-pill font-semibold ${
                isActive
                  ? 'bg-[var(--color-navy-800)]/10 text-[var(--color-navy-800)] dark:text-[var(--color-navy-600)]'
                  : 'bg-[var(--color-surface-alt)] text-[var(--color-text-muted)]'
              }`}>
                {tab.badge}
              </span>
            )}

            {isActive && (
              <motion.div
                layoutId="activeTabUnderline"
                className="absolute bottom-0 left-0 right-0 h-0.5 bg-[var(--color-navy-800)] dark:bg-[var(--color-navy-600)] rounded-full"
                transition={{ type: 'spring', stiffness: 500, damping: 35 }}
              />
            )}
          </button>
        );
      })}
    </div>
  );
}
