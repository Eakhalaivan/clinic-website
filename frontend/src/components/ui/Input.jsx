import React from 'react';

/**
 * Simple styled Input component.
 */
const Input = React.forwardRef(function Input(
  { label, className = '', ...props },
  ref
) {
  return (
    <div className={`flex flex-col gap-1 w-full ${className}`}>
      {label && (
        <label className="text-sm font-semibold text-[var(--color-navy-900)]">
          {label}
        </label>
      )}
      <input
        ref={ref}
        className="w-full h-10 px-3 py-2 bg-transparent border border-[var(--color-border)] rounded-md focus:outline-none focus:ring-2 focus:ring-[var(--color-primary)] text-sm text-[var(--color-navy-900)] placeholder:text-[var(--color-text-muted)] transition-shadow"
        {...props}
      />
    </div>
  );
});

export default Input;
