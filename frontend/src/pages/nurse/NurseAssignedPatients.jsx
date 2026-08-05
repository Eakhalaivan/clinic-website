import React, { useState } from 'react';
import { useQuery } from '@tanstack/react-query';
import { axiosPrivate } from '../../api/axios';
import { useNavigate } from 'react-router-dom';
import { HeartPulse, FileText, UserRound } from 'lucide-react';
import EmptyState from '../../components/ui/EmptyState';

const NurseAssignedPatients = () => {
  const navigate = useNavigate();
  const [search, setSearch] = useState('');

  const { data: patients = [], isLoading } = useQuery({
    queryKey: ['nurse-assigned-patients'],
    queryFn: async () => (await axiosPrivate.get('/nursing/assignments/op')).data,
  });

  return (
    <div className="p-4 sm:p-6" style={{ maxWidth: '1000px', margin: '0 auto' }}>
      <div className="flex flex-col sm:flex-row sm:justify-between sm:items-center gap-2 mb-5">
        <div>
          <h1 className="text-xl sm:text-2xl font-bold" style={{ color: 'var(--color-text)', margin: 0 }}>Assigned Patients</h1>
          <p style={{ margin: 0, fontSize: '0.8rem', color: 'var(--color-text-muted)' }}>Patients currently assigned to your shift</p>
        </div>
        <span style={{ fontSize: '0.85rem', fontWeight: 600, color: '#0f766e', background: '#ccfbf1', padding: '4px 12px', borderRadius: '6px', whiteSpace: 'nowrap' }}>Shift: Morning (08:00 - 16:00)</span>
      </div>

      <div style={{ background: 'var(--color-surface)', borderRadius: '12px', border: '1px solid var(--color-border)', overflow: 'hidden' }}>
        {patients.length === 0 ? (
          <div style={{ padding: '40px 0' }}>
            <EmptyState 
              icon={UserRound}
              title="No Patients Assigned" 
              description="There are currently no OP patients assigned to your nursing queue." 
            />
          </div>
        ) : (
          patients.map(p => (
            <div key={p.id} className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-3 p-4 border-b" style={{ borderColor: 'var(--color-surface-alt)' }}>
              <div style={{ display: 'flex', alignItems: 'center', gap: '14px' }}>
                <div style={{ width: '40px', height: '40px', borderRadius: '50%', background: '#ccfbf1', display: 'flex', alignItems: 'center', justifyContent: 'center', color: '#0f766e', fontWeight: 700, flexShrink: 0 }}>
                  {p.patientName ? p.patientName[0] : '?'}
                </div>
                <div>
                  <h3 style={{ margin: 0, fontSize: '0.95rem', fontWeight: 700, color: 'var(--color-text)' }}>{p.patientName} ({p.age}y)</h3>
                  <p style={{ margin: '2px 0 0', fontSize: '0.78rem', color: 'var(--color-text-muted)' }}>
                    {p.tokenNumber ? `Token #${p.tokenNumber} · ` : ''}Reason: {p.appointmentReason} · Attending: {p.attendingDoctorName}
                  </p>
                  <p style={{ margin: '2px 0 0', fontSize: '0.75rem', color: '#0f766e', fontWeight: 500 }}>Vitals: {p.lastVitalsSummary}</p>
                </div>
              </div>
              <div className="flex gap-2 flex-wrap sm:flex-nowrap">
                <button onClick={() => navigate(`/nurse/vitals?patientId=${p.patientId}`)} style={{ background: '#0f766e', color: 'var(--color-surface)', border: 'none', padding: '6px 12px', borderRadius: '6px', fontSize: '0.78rem', cursor: 'pointer', fontWeight: 600, display: 'flex', alignItems: 'center', gap: '4px' }}>
                  <HeartPulse size={14} /> Record Vitals
                </button>
                <button onClick={() => navigate(`/nurse/notes?patientId=${p.patientId}`)} style={{ background: 'var(--color-surface-alt)', color: 'var(--color-text)', border: 'none', padding: '6px 12px', borderRadius: '6px', fontSize: '0.78rem', cursor: 'pointer', fontWeight: 500, display: 'flex', alignItems: 'center', gap: '4px' }}>
                  <FileText size={14} /> Notes
                </button>
              </div>
            </div>
          ))
        )}
      </div>
    </div>
  );
};

export default NurseAssignedPatients;
