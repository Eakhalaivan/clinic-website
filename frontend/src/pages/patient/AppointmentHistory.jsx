import React from 'react';
import { useQuery } from '@tanstack/react-query';
import { axiosPrivate } from '../../api/axios';
import { CalendarDays, Clock, User, CheckCircle, XCircle } from 'lucide-react';
import useAuthStore from '../../store/authStore';

const AppointmentHistory = () => {
  const { user } = useAuthStore();
  
  const { data: appointments = [], isLoading } = useQuery({
    queryKey: ['patientAppointments', user?.id],
    queryFn: async () => (await axiosPrivate.get(`/appointments/patient/${user?.id}`)).data,
    enabled: !!user?.id,
  });



  return (
    <div style={{ padding: '24px', maxWidth: '900px', margin: '0 auto' }}>
      <h1 style={{ fontSize: '1.5rem', fontWeight: 700, color: 'var(--color-text)', marginBottom: '20px', display: 'flex', alignItems: 'center', gap: '8px' }}>
        <CalendarDays size={24} color="#0369a1" /> Appointment History
      </h1>

      <div style={{ background: 'var(--color-surface)', borderRadius: '12px', border: '1px solid var(--color-border)', overflow: 'hidden' }}>
        {appointments.length === 0 && !isLoading && (
          <div style={{ padding: '32px', textAlign: 'center', color: 'var(--color-text-muted)' }}>
            No appointments found.
          </div>
        )}
        {appointments.map(a => (
          <div key={a.id} style={{ padding: '16px 20px', borderBottom: '1px solid var(--color-surface-alt)', display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
            <div>
              <h3 style={{ margin: 0, fontSize: '0.95rem', fontWeight: 700, color: 'var(--color-text)' }}>{a.doctorName}</h3>
              <p style={{ margin: '2px 0 0', fontSize: '0.78rem', color: 'var(--color-text-muted)' }}>{a.specialty || 'General'} · {a.type || 'Consultation'}</p>
              <p style={{ margin: '4px 0 0', fontSize: '0.8rem', color: '#0369a1', fontWeight: 600 }}>
                {a.date || new Date(a.startTime).toLocaleDateString()} at {a.time || new Date(a.startTime).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })}
              </p>
            </div>
            <span style={{
              padding: '4px 10px', borderRadius: '6px', fontSize: '0.75rem', fontWeight: 700,
              background: a.status === 'COMPLETED' ? 'var(--color-success-bg)' : a.status === 'SCHEDULED' ? '#e0f2fe' : 'var(--color-danger-bg)',
              color: a.status === 'COMPLETED' ? 'var(--color-success)' : a.status === 'SCHEDULED' ? '#0369a1' : 'var(--color-danger)'
            }}>
              {a.status}
            </span>
          </div>
        ))}
      </div>
    </div>
  );
};

export default AppointmentHistory;
