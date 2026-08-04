import React from 'react';
import { Loader2 } from 'lucide-react';

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
 * Enterprise Button Primitive
 * @param {Object} props
 * @param {'primary'|'secondary'|'ghost'|'danger'|'outline'|'gold'} [props.variant='primary']
 * @param {'sm'|'md'|'lg'} [props.size='md']
 * @param {boolean} [props.isLoading=false]
 * @param {boolean} [props.fullWidth=false]
 * @param {React.ReactNode} [props.icon]
 * @param {React.ButtonHTMLAttributes<HTMLButtonElement>} props
 */
export default function Button({
  children,
  variant = 'primary',
  size = 'md',
  isLoading = false,
  fullWidth = false,
  icon: IconComponent,
  disabled,
  className = '',
  type = 'button',
  ...rest
}) {
  const baseStyles = "inline-flex items-center justify-center font-medium transition-all duration-200 rounded-sm focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-offset-2 focus-visible:ring-offset-[var(--color-surface)] disabled:opacity-50 disabled:cursor-not-allowed disabled:transform-none select-none";

  const variants = {
    // Gold gradient primary — the premium default CTA
    primary: [
      "bg-[var(--color-navy-800)] text-white",
      "hover:bg-[var(--color-navy-900)]",
      "dark:bg-[var(--color-navy-800)] dark:hover:bg-[var(--color-navy-900)]",
      "active:scale-[0.99] shadow-sm hover:shadow-card",
      "focus-visible:ring-[var(--color-navy-600)]",
    ].join(' '),
    // Explicit gold/luxury CTA variant
    gold: [
      "bg-[var(--color-gold)] text-white font-semibold",
      "hover:bg-[var(--color-gold-hover)]",
      "active:scale-[0.99] shadow-sm hover:shadow-card",
      "focus-visible:ring-[var(--color-gold)]",
    ].join(' '),
    secondary: [
      "bg-[var(--color-surface-alt)] text-[var(--color-navy-900)] border border-[var(--color-border)]",
      "hover:bg-[var(--color-border)]",
      "dark:border-white/15 dark:text-[var(--color-text)] dark:hover:bg-white/8",
      "active:scale-[0.99]",
      "focus-visible:ring-[var(--color-navy-600)]",
    ].join(' '),
    outline: [
      "bg-transparent text-[var(--color-navy-900)] border border-[var(--color-navy-800)]",
      "hover:bg-black/5",
      "dark:border-white/20 dark:text-[var(--color-text)] dark:hover:bg-white/8",
      "active:scale-[0.99]",
      "focus-visible:ring-[var(--color-navy-600)]",
    ].join(' '),
    ghost: [
      "bg-transparent text-[var(--color-navy-900)]",
      "hover:bg-black/5 dark:hover:bg-white/8",
      "dark:text-[var(--color-text)]",
      "active:scale-[0.99]",
      "focus-visible:ring-[var(--color-navy-600)]",
    ].join(' '),
    danger: [
      "bg-[var(--color-danger)] text-white",
      "hover:bg-red-700 dark:hover:bg-red-500",
      "active:scale-[0.99] shadow-sm",
      "focus-visible:ring-[var(--color-danger)]",
    ].join(' '),
  };

  const sizes = {
    sm: "px-3 py-1.5 text-xs gap-1.5 min-h-[32px]",
    md: "px-4 py-2 text-sm gap-2 min-h-[40px]",
    lg: "px-6 py-2.5 text-base gap-2.5 min-h-[48px]"
  };

  const widthStyle = fullWidth ? "w-full" : "";

  return (
    <button
      type={type}
      disabled={disabled || isLoading}
      className={`${baseStyles} ${variants[variant] || variants.primary} ${sizes[size] || sizes.md} ${widthStyle} ${className}`}
      {...rest}
    >
      {isLoading ? (
        <Loader2 className="w-4 h-4 animate-spin text-current shrink-0" />
      ) : (
        renderIcon(IconComponent, "w-4 h-4 text-current shrink-0")
      )}
      {children && <span>{children}</span>}
    </button>
  );
}
