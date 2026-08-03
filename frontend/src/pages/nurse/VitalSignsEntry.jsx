import React, { useState } from 'react';
import { useSearchParams } from 'react-router-dom';
import { useMutation, useQueryClient } from '@tanstack/react-query';
import { axiosPrivate } from '../../api/axios';
import { HeartPulse, Save, ChevronLeft } from 'lucide-react';

const VitalSignsEntry = () => {
  const queryClient = useQueryClient();
  const [searchParams] = useSearchParams();
  const patientIdFromUrl = searchParams.get('patientId') || '101';
  const [vitals, setVitals] = useState({
    patientId: patientIdFromUrl,
    systolicBp: 120,
    diastolicBp: 80,
    heartRate: 72,
    temperatureF: 98.6,
    spo2Percentage: 98,
    respiratoryRate: 16,
    notes: '',
  });

  const recordVitals = useMutation({
    mutationFn: async () => axiosPrivate.post(`/patients/${vitals.patientId}/vitals`, vitals),
    onSuccess: () => {
      alert('Vital signs recorded successfully!');
    },
  });

  return (
    <div style={{ padding: '24px', maxWidth: '800px', margin: '0 auto' }}>
      <h1 style={{ fontSize: '1.5rem', fontWeight: 700, color: 'var(--color-text)', marginBottom: '20px', display: 'flex', alignItems: 'center', gap: '8px' }}>
        <HeartPulse size={24} color="#0f766e" /> Record Vital Signs
      </h1>

      <div style={{ background: 'var(--color-surface)', borderRadius: '12px', border: '1px solid var(--color-border)', padding: '24px' }}>
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(2, 1fr)', gap: '16px', marginBottom: '20px' }}>
          <div>
            <label style={{ display: 'block', fontSize: '0.8rem', fontWeight: 600, color: 'var(--color-text)', marginBottom: '4px' }}>Systolic BP (mmHg)</label>
            <input type="number" value={vitals.systolicBp} onChange={e => setVitals({ ...vitals, systolicBp: +e.target.value })} style={{ width: '100%', padding: '8px', borderRadius: '6px', border: '1px solid var(--color-border)' }} />
          </div>
          <div>
            <label style={{ display: 'block', fontSize: '0.8rem', fontWeight: 600, color: 'var(--color-text)', marginBottom: '4px' }}>Diastolic BP (mmHg)</label>
            <input type="number" value={vitals.diastolicBp} onChange={e => setVitals({ ...vitals, diastolicBp: +e.target.value })} style={{ width: '100%', padding: '8px', borderRadius: '6px', border: '1px solid var(--color-border)' }} />
          </div>
          <div>
            <label style={{ display: 'block', fontSize: '0.8rem', fontWeight: 600, color: 'var(--color-text)', marginBottom: '4px' }}>Heart Rate (bpm)</label>
            <input type="number" value={vitals.heartRate} onChange={e => setVitals({ ...vitals, heartRate: +e.target.value })} style={{ width: '100%', padding: '8px', borderRadius: '6px', border: '1px solid var(--color-border)' }} />
          </div>
          <div>
            <label style={{ display: 'block', fontSize: '0.8rem', fontWeight: 600, color: 'var(--color-text)', marginBottom: '4px' }}>Temperature (°F)</label>
            <input type="number" step="0.1" value={vitals.temperatureF} onChange={e => setVitals({ ...vitals, temperatureF: +e.target.value })} style={{ width: '100%', padding: '8px', borderRadius: '6px', border: '1px solid var(--color-border)' }} />
          </div>
          <div>
            <label style={{ display: 'block', fontSize: '0.8rem', fontWeight: 600, color: 'var(--color-text)', marginBottom: '4px' }}>SpO2 (%)</label>
            <input type="number" value={vitals.spo2Percentage} onChange={e => setVitals({ ...vitals, spo2Percentage: +e.target.value })} style={{ width: '100%', padding: '8px', borderRadius: '6px', border: '1px solid var(--color-border)' }} />
          </div>
          <div>
            <label style={{ display: 'block', fontSize: '0.8rem', fontWeight: 600, color: 'var(--color-text)', marginBottom: '4px' }}>Resp. Rate (bpm)</label>
            <input type="number" value={vitals.respiratoryRate} onChange={e => setVitals({ ...vitals, respiratoryRate: +e.target.value })} style={{ width: '100%', padding: '8px', borderRadius: '6px', border: '1px solid var(--color-border)' }} />
          </div>
        </div>

        <div style={{ marginBottom: '20px' }}>
          <label style={{ display: 'block', fontSize: '0.8rem', fontWeight: 600, color: 'var(--color-text)', marginBottom: '4px' }}>Nurse Observations / Clinical Remarks</label>
          <textarea rows={3} value={vitals.notes} onChange={e => setVitals({ ...vitals, notes: e.target.value })} style={{ width: '100%', padding: '8px', borderRadius: '6px', border: '1px solid var(--color-border)', resize: 'vertical' }} />
        </div>

        <button onClick={() => recordVitals.mutate()} style={{ background: '#0f766e', color: 'var(--color-surface)', border: 'none', padding: '10px 24px', borderRadius: '8px', fontWeight: 700, cursor: 'pointer', display: 'flex', alignItems: 'center', gap: '8px' }}>
          <Save size={16} /> Save Vitals
        </button>
      </div>
    </div>
  );
};

export default VitalSignsEntry;
