import React from 'react';
import { AlertCircle } from 'lucide-react';

/**
 * Enterprise FormField Primitive
 * @param {Object} props
 * @param {string} [props.label]
 * @param {string} [props.error]
 * @param {string} [props.helpText]
 * @param {boolean} [props.required=false]
 */
export default function FormField({
  label,
  error,
  helpText,
  required = false,
  children,
  className = '',
  id
}) {
  return (
    <div className={`flex flex-col gap-1.5 w-full ${className}`}>
      {label && (
        <label 
          htmlFor={id} 
          className="text-xs font-semibold uppercase tracking-wider text-[var(--color-navy-900)] dark:text-[var(--color-text-muted)] flex items-center justify-between"
        >
          <span>
            {label}
            {required && <span className="text-[var(--color-danger)] ml-1" aria-hidden="true">*</span>}
          </span>
        </label>
      )}

      <div className="relative flex items-center">
        {children}
      </div>

      {error ? (
        <div className="flex items-center gap-1 text-xs font-medium text-[var(--color-danger)] mt-0.5 animate-fadeIn">
          <AlertCircle className="w-3.5 h-3.5 shrink-0" />
          <span>{error}</span>
        </div>
      ) : helpText ? (
        <p className="text-xs text-[var(--color-text-muted)] mt-0.5 m-0">
          {helpText}
        </p>
      ) : null}
    </div>
  );
}
