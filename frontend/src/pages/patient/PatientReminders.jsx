import React from 'react';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { axiosPrivate } from '../../api/axios';
import { Bell, CheckCircle, XCircle } from 'lucide-react';
import toast from 'react-hot-toast';

const PatientReminders = () => {
  const queryClient = useQueryClient();
  const patientId = 1; // Assuming patient 1 for demo purposes. In reality, get from context/auth.

  const { data: reminders = [], isLoading } = useQuery({
    queryKey: ['patient-reminders', patientId],
    queryFn: async () => (await axiosPrivate.get(`/engagement/reminders?patientId=${patientId}&status=PENDING`)).data,
  });

  const dismissMutation = useMutation({
    mutationFn: async (id) => await axiosPrivate.put(`/engagement/reminders/${id}/dismiss`),
    onSuccess: () => {
      toast.success('Reminder dismissed');
      queryClient.invalidateQueries(['patient-reminders', patientId]);
    }
  });

  const completeMutation = useMutation({
    mutationFn: async (id) => await axiosPrivate.put(`/engagement/reminders/${id}/complete`),
    onSuccess: () => {
      toast.success('Reminder completed');
      queryClient.invalidateQueries(['patient-reminders', patientId]);
    }
  });

  if (isLoading) return <div className="p-6">Loading reminders...</div>;

  return (
    <div className="p-4 sm:p-6" style={{ maxWidth: '800px', margin: '0 auto' }}>
      <div className="flex items-center gap-3 mb-6">
        <Bell size={28} style={{ color: 'var(--color-primary)' }} />
        <h1 className="text-xl sm:text-2xl font-bold" style={{ color: 'var(--color-text)' }}>Your Reminders</h1>
      </div>

      {reminders.length === 0 ? (
        <div style={{ background: 'var(--color-surface)', padding: '40px', borderRadius: '12px', border: '1px solid var(--color-border)', textAlign: 'center' }}>
          <Bell size={48} style={{ color: 'var(--color-text-muted)', margin: '0 auto 16px' }} />
          <h2 style={{ color: 'var(--color-text)', fontSize: '1.25rem', fontWeight: 600 }}>All caught up!</h2>
          <p style={{ color: 'var(--color-text-muted)', marginTop: '8px' }}>You have no pending reminders.</p>
        </div>
      ) : (
        <div style={{ display: 'flex', flexDirection: 'column', gap: '16px' }}>
          {reminders.map(reminder => (
            <div key={reminder.id} style={{ background: 'var(--color-surface)', padding: '20px', borderRadius: '12px', border: '1px solid var(--color-border)', display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
              <div>
                <div style={{ display: 'flex', alignItems: 'center', gap: '8px', marginBottom: '8px' }}>
                  <span style={{ fontSize: '0.75rem', fontWeight: 700, padding: '4px 8px', borderRadius: '4px', background: 'var(--color-primary-light)', color: 'var(--color-primary)' }}>
                    {reminder.reminderType}
                  </span>
                  <span style={{ fontSize: '0.85rem', color: 'var(--color-text-muted)' }}>
                    Due: {new Date(reminder.dueAt).toLocaleDateString()}
                  </span>
                </div>
                <h3 style={{ fontSize: '1.1rem', fontWeight: 600, color: 'var(--color-text)', marginBottom: '4px' }}>{reminder.title}</h3>
                <p style={{ color: 'var(--color-text-muted)', fontSize: '0.95rem' }}>{reminder.message}</p>
              </div>
              <div style={{ display: 'flex', gap: '12px' }}>
                <button 
                  onClick={() => dismissMutation.mutate(reminder.id)}
                  style={{ padding: '8px', borderRadius: '50%', background: 'var(--color-surface)', border: '1px solid var(--color-border)', color: 'var(--color-text-muted)', cursor: 'pointer' }}
                  title="Dismiss"
                >
                  <XCircle size={24} />
                </button>
                <button 
                  onClick={() => completeMutation.mutate(reminder.id)}
                  style={{ padding: '8px', borderRadius: '50%', background: 'var(--color-success)', border: 'none', color: '#fff', cursor: 'pointer' }}
                  title="Mark Complete"
                >
                  <CheckCircle size={24} />
                </button>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
};

export default PatientReminders;
