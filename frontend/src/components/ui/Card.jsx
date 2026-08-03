import React from 'react';

/**
 * Enterprise Card Primitive
 * @param {Object} props
 * @param {'flat'|'card'|'elevated'|'glass'} [props.variant='card']
 * @param {boolean} [props.hoverable=false]
 */
export default function Card({
  children,
  variant = 'card',
  hoverable = false,
  className = '',
  ...rest
}) {
  const baseStyles = "bg-[var(--color-surface)] border border-[var(--color-border)] rounded-md transition-all duration-200";

  const variants = {
    flat: "shadow-none bg-[var(--color-surface-alt)]",
    card: "shadow-card",
    elevated: "shadow-elevated border-opacity-60",
    glass: "backdrop-blur-glass bg-[var(--glass-bg)] border-[var(--glass-border)] shadow-md"
  };

  const hoverStyle = hoverable ? "hover:-translate-y-0.5 hover:shadow-elevated cursor-pointer" : "";

  return (
    <div className={`${baseStyles} ${variants[variant] || variants.card} ${hoverStyle} ${className}`} {...rest}>
      {children}
    </div>
  );
}

Card.Header = function CardHeader({ children, className = '', ...rest }) {
  return (
    <div className={`p-5 pb-3 border-b border-[var(--color-border)] flex items-center justify-between gap-4 ${className}`} {...rest}>
      {children}
    </div>
  );
};

Card.Body = function CardBody({ children, className = '', ...rest }) {
  return (
    <div className={`p-5 ${className}`} {...rest}>
      {children}
    </div>
  );
};

Card.Footer = function CardFooter({ children, className = '', ...rest }) {
  return (
    <div className={`p-5 pt-3 border-t border-[var(--color-border)] bg-[var(--color-surface-alt)]/50 rounded-b-md flex items-center justify-between gap-4 ${className}`} {...rest}>
      {children}
    </div>
  );
};
