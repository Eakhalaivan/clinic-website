import React from 'react';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { axiosPrivate } from '../../api/axios';
import { Users, Clock, Bell, ChevronRight, Loader } from 'lucide-react';

import useAuthStore from '../../store/authStore';

const ConsultationQueue = () => {
  const queryClient = useQueryClient();
  const token = useAuthStore(state => state.token);

  const { data: queue = [], isLoading } = useQuery({
    queryKey: ['doctor-queue'],
    queryFn: async () => (await axiosPrivate.get('/appointments/queue')).data,
    staleTime: 60000,
  });

  React.useEffect(() => {
    if (!token) return;
    const baseUrl = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api';
    const evtSource = new EventSource(`${baseUrl.replace('/api', '')}/api/sse/appointments?token=${token}`);
    
    evtSource.onmessage = () => {
      queryClient.invalidateQueries(['doctor-queue']);
    };
    
    return () => evtSource.close();
  }, [token, queryClient]);

  const callNext = useMutation({
    mutationFn: async (appointmentId) => axiosPrivate.patch(`/appointments/${appointmentId}/status?status=IN_PROGRESS`),
    onSuccess: () => queryClient.invalidateQueries(['doctor-queue']),
  });

  const waiting = queue.filter(q => q.status === 'CHECKED_IN');
  const inProgress = queue.find(q => q.status === 'IN_PROGRESS');

  return (
    <div className="p-4 sm:p-6" style={{ maxWidth: '900px', margin: '0 auto' }}>
      <h1 className="text-xl sm:text-2xl font-bold mb-5" style={{ color: 'var(--color-text)' }}>Consultation Queue</h1>

      {/* Currently in room */}
      {inProgress && (
        <div style={{ background: 'linear-gradient(135deg, var(--color-info), var(--color-info))', borderRadius: '14px', padding: '20px 24px', marginBottom: '24px', color: 'var(--color-surface)' }}>
          <p style={{ margin: 0, fontSize: '0.8rem', opacity: 0.85 }}>Currently in consultation</p>
          <h2 style={{ margin: '4px 0 0', fontSize: '1.25rem', fontWeight: 700 }}>{inProgress.patientName || `Patient #${inProgress.patientId}`}</h2>
          <p style={{ margin: '4px 0 0', fontSize: '0.8rem', opacity: 0.75 }}>{inProgress.appointmentType || 'General Consultation'} · {inProgress.opNumber ? `${inProgress.opNumber} · ` : ''}Token #{inProgress.tokenNumber || inProgress.id}</p>
          <div style={{ display: 'flex', alignItems: 'center', gap: '6px', marginTop: '8px' }}>
            <div style={{ width: '8px', height: '8px', borderRadius: '50%', background: '#86efac', animation: 'pulse 1.5s infinite' }} />
            <span style={{ fontSize: '0.8rem' }}>In Progress</span>
          </div>
        </div>
      )}

      {/* Stats */}
      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(2, 1fr)', gap: '14px', marginBottom: '24px' }}>
        <div style={{ background: 'var(--color-surface)', padding: '16px 20px', borderRadius: '10px', border: '1px solid var(--color-border)', display: 'flex', alignItems: 'center', gap: '12px' }}>
          <div style={{ padding: '10px', background: 'var(--color-warning-bg)', borderRadius: '8px' }}><Users size={20} color="#c2410c" /></div>
          <div><p style={{ margin: 0, fontSize: '0.8rem', color: 'var(--color-text-muted)' }}>Waiting</p><h3 style={{ margin: 0, fontSize: '1.5rem', fontWeight: 800, color: '#c2410c' }}>{waiting.length}</h3></div>
        </div>
        <div style={{ background: 'var(--color-surface)', padding: '16px 20px', borderRadius: '10px', border: '1px solid var(--color-border)', display: 'flex', alignItems: 'center', gap: '12px' }}>
          <div style={{ padding: '10px', background: 'var(--color-success-bg)', borderRadius: '8px' }}><Clock size={20} color="var(--color-success)" /></div>
          <div><p style={{ margin: 0, fontSize: '0.8rem', color: 'var(--color-text-muted)' }}>Est. Wait (min)</p><h3 style={{ margin: 0, fontSize: '1.5rem', fontWeight: 800, color: 'var(--color-success)' }}>{waiting.length * 15}</h3></div>
        </div>
      </div>

      {/* Call next */}
      {waiting.length > 0 && !inProgress && (
        <button
          onClick={() => callNext.mutate(waiting[0].id)}
          disabled={callNext.isPending}
          style={{ background: 'var(--color-success)', color: 'var(--color-surface)', border: 'none', padding: '12px 24px', borderRadius: '8px', fontWeight: 700, fontSize: '0.95rem', cursor: 'pointer', display: 'flex', alignItems: 'center', gap: '8px', marginBottom: '20px' }}
        >
          {callNext.isPending ? <Loader size={16} /> : <Bell size={16} />}
          Call Next Patient
        </button>
      )}

      {/* Queue list */}
      <div style={{ background: 'var(--color-surface)', borderRadius: '12px', border: '1px solid var(--color-border)', overflow: 'hidden' }}>
        <div style={{ padding: '14px 18px', borderBottom: '1px solid var(--color-border)', fontWeight: 600, fontSize: '0.875rem', color: 'var(--color-text)' }}>Waiting Queue ({waiting.length})</div>
        {isLoading ? <div style={{ padding: 30, textAlign: 'center', color: 'var(--color-text-muted)' }}>Loading queue…</div> :
          waiting.length === 0 ? <div style={{ padding: 30, textAlign: 'center', color: 'var(--color-text-muted)' }}>Queue is empty</div> : (
          waiting.map((p, i) => (
            <div key={p.id} style={{ display: 'flex', alignItems: 'center', padding: '14px 18px', borderBottom: '1px solid var(--color-surface-alt)', gap: '14px' }}>
              <div style={{ width: '36px', height: '36px', borderRadius: '50%', background: i === 0 ? 'var(--color-info)' : 'var(--color-surface-alt)', color: i === 0 ? 'var(--color-surface)' : 'var(--color-text-muted)', display: 'flex', alignItems: 'center', justifyContent: 'center', fontWeight: 700, fontSize: '0.9rem', flexShrink: 0 }}>{i + 1}</div>
              <div style={{ flex: 1 }}>
                <p style={{ margin: 0, fontWeight: 600, fontSize: '0.875rem', color: 'var(--color-text)' }}>{p.patientName || `Patient #${p.patientId}`}</p>
                <p style={{ margin: 0, fontSize: '0.75rem', color: 'var(--color-text-muted)' }}>{p.opNumber ? `${p.opNumber} · ` : ''}Token #{p.tokenNumber || p.id} · {p.appointmentType || 'Consultation'}</p>
              </div>
              <span style={{ fontSize: '0.75rem', color: 'var(--color-text-muted)' }}>~{(i + 1) * 15} min</span>
              <ChevronRight size={16} color="var(--color-border)" />
            </div>
          ))
        )}
      </div>
    </div>
  );
};

export default ConsultationQueue;
