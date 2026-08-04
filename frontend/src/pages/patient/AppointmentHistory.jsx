import React from 'react';
import { useQuery } from '@tanstack/react-query';
import { axiosPrivate } from '../../api/axios';
import { CalendarDays } from 'lucide-react';
import useAuthStore from '../../store/authStore';

const AppointmentHistory = () => {
  const { user } = useAuthStore();

  const { data: appointments = [], isLoading } = useQuery({
    queryKey: ['patientAppointments', user?.id],
    queryFn: async () => (await axiosPrivate.get(`/appointments/patient/${user?.id}`)).data,
    enabled: !!user?.id,
  });

  const statusColor = (status) => {
    if (status === 'COMPLETED') return { bg: 'var(--color-success-bg)', color: 'var(--color-success)' };
    if (status === 'SCHEDULED') return { bg: '#e0f2fe', color: '#0369a1' };
    return { bg: 'var(--color-danger-bg)', color: 'var(--color-danger)' };
  };

  return (
    <div className="p-4 sm:p-6 max-w-3xl mx-auto">
      <h1 className="text-xl sm:text-2xl font-bold mb-5 flex items-center gap-2" style={{ color: 'var(--color-text)' }}>
        <CalendarDays size={24} color="#0369a1" aria-hidden="true" /> Appointment History
      </h1>

      <div style={{ background: 'var(--color-surface)', borderRadius: '12px', border: '1px solid var(--color-border)', overflow: 'hidden' }}>
        {isLoading && (
          <div className="p-8 text-center" style={{ color: 'var(--color-text-muted)' }}>Loading...</div>
        )}
        {!isLoading && appointments.length === 0 && (
          <div className="p-8 text-center" style={{ color: 'var(--color-text-muted)' }}>No appointments found.</div>
        )}
        {appointments.map(a => {
          const { bg, color } = statusColor(a.status);
          return (
            <div
              key={a.id}
              className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-2 p-4 border-b"
              style={{ borderColor: 'var(--color-surface-alt)' }}
            >
              <div>
                <h3 className="text-sm font-bold" style={{ color: 'var(--color-text)' }}>{a.doctorName}</h3>
                <p className="text-xs mt-0.5" style={{ color: 'var(--color-text-muted)' }}>
                  {a.specialty || 'General'} · {a.type || 'Consultation'}
                </p>
                <p className="text-xs mt-1 font-semibold" style={{ color: '#0369a1' }}>
                  {a.date || new Date(a.startTime).toLocaleDateString()} at{' '}
                  {a.time || new Date(a.startTime).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })}
                </p>
              </div>
              <span
                className="self-start sm:self-center text-xs font-bold px-2.5 py-1 rounded-md whitespace-nowrap"
                style={{ background: bg, color }}
              >
                {a.status}
              </span>
            </div>
          );
        })}
      </div>
    </div>
  );
};

export default AppointmentHistory;
