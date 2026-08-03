import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { axiosPrivate } from '../../api/axios';
import { LineChart, Line, XAxis, YAxis, CartesianGrid, Tooltip, Legend, ResponsiveContainer } from 'recharts';
import { Pill, Calendar as CalendarIcon, ArrowLeft, UserRound, Phone, Mail, Activity, ClipboardList, ShieldPlus, Save } from 'lucide-react';
import toast from 'react-hot-toast';

const PatientDetail = ({ patientIdOverride }) => {
  const { patientId: paramPatientId } = useParams();
  const patientId = patientIdOverride || paramPatientId;
  const navigate = useNavigate();
  const queryClient = useQueryClient();

  const { data: patient, isLoading } = useQuery({
    queryKey: ['patient-detail', patientId],
    queryFn: async () => (await axiosPrivate.get(`/doctor/patients/${patientId}`)).data,
  });

  const [flags, setFlags] = useState({ insuranceStatus: '', injuryStatus: '' });

  useEffect(() => {
    if (patient) {
      setFlags({
        insuranceStatus: patient.insuranceStatus || '',
        injuryStatus: patient.injuryStatus || ''
      });
    }
  }, [patient]);

  const updateFlags = useMutation({
    mutationFn: async (data) => {
      const res = await axiosPrivate.put(`/patients/${patientId}`, data);
      return res.data;
    },
    onSuccess: () => {
      toast.success('Patient flags updated successfully');
      queryClient.invalidateQueries({ queryKey: ['patient-detail', patientId] });
    },
    onError: (err) => {
      toast.error(err.response?.data?.message || 'Failed to update flags');
    }
  });

  if (isLoading) return <div style={{ padding: 40, textAlign: 'center', color: 'var(--color-text-muted)' }}>Loading patient details...</div>;
  if (!patient) return <div style={{ padding: 40, textAlign: 'center', color: 'var(--color-danger)' }}>Patient not found</div>;

  const parseJsonStr = (str) => {
    try {
      return JSON.parse(str || '[]');
    } catch {
      return [];
    }
  };

  const renderList = (jsonStr) => {
    const list = parseJsonStr(jsonStr);
    if (!list.length) return <span style={{ color: 'var(--color-text-muted)', fontStyle: 'italic' }}>None recorded</span>;
    return <ul style={{ margin: 0, paddingLeft: '20px' }}>{list.map((item, i) => <li key={i}>{item}</li>)}</ul>;
  };

  return (
    <div style={{ padding: '24px', maxWidth: '1200px', margin: '0 auto', fontFamily: 'Inter, sans-serif' }}>
      {!patientIdOverride && (
        <button onClick={() => navigate('/doctor/patients')} style={{ display: 'flex', alignItems: 'center', gap: '8px', background: 'none', border: 'none', color: 'var(--color-text-muted)', cursor: 'pointer', marginBottom: '20px', padding: 0 }}>
          <ArrowLeft size={16} /> Back to Patients
        </button>
      )}

      {/* Header Profile */}
      <div style={{ background: 'var(--color-surface)', borderRadius: '12px', border: '1px solid var(--color-border)', padding: '24px', display: 'flex', gap: '24px', alignItems: 'center', marginBottom: '24px' }}>
        <div style={{ width: '80px', height: '80px', borderRadius: '50%', background: 'var(--color-info-bg)', display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
          <UserRound size={40} color="var(--color-info)" />
        </div>
        <div style={{ flex: 1 }}>
          <h1 style={{ margin: '0 0 8px 0', fontSize: '1.75rem', fontWeight: 700, color: 'var(--color-text)' }}>{patient.name}</h1>
          <div style={{ display: 'flex', gap: '24px', color: 'var(--color-text-muted)', fontSize: '0.9rem' }}>
            <span style={{ display: 'flex', gap: '6px', alignItems: 'center' }}><Phone size={14}/> {patient.phone}</span>
            <span style={{ display: 'flex', gap: '6px', alignItems: 'center' }}><Mail size={14}/> {patient.email}</span>
            <span><strong>Age:</strong> {patient.age}</span>
            <span><strong>Gender:</strong> {patient.gender}</span>
            <span><strong>Blood:</strong> {patient.bloodGroup}</span>
            <span><strong>ID:</strong> {patient.patientId}</span>
          </div>
          <div style={{ marginTop: '12px', fontSize: '0.85rem', color: 'var(--color-text-muted)' }}>
            <strong>Emergency Contact:</strong> {patient.emergencyContactName || 'N/A'} ({patient.emergencyContactPhone || 'N/A'})
          </div>
        </div>
        <div style={{ display: 'flex', gap: '12px' }}>
          <button onClick={() => navigate(`/doctor/patients/${patient.patientId}/notes`)}
            style={{ background: 'var(--color-surface-alt)', color: '#334155', border: '1px solid var(--color-border)', padding: '10px 16px', borderRadius: '8px', fontWeight: 600, display: 'flex', gap: '8px', alignItems: 'center', cursor: 'pointer' }}>
            <ClipboardList size={16} /> View Clinical Notes
          </button>
          <button onClick={() => navigate(`/doctor/patients/${patient.patientId}/prescriptions/new`)}
            style={{ background: 'var(--color-info)', color: 'white', border: 'none', padding: '10px 16px', borderRadius: '8px', fontWeight: 600, display: 'flex', gap: '8px', alignItems: 'center', cursor: 'pointer' }}>
            <Pill size={16} /> Send Prescription
          </button>
        </div>
      </div>

      <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '24px', marginBottom: '24px' }}>
        {/* Medical Info */}
        <div style={{ background: 'var(--color-surface)', borderRadius: '12px', border: '1px solid var(--color-border)', padding: '24px' }}>
          <h2 style={{ fontSize: '1.2rem', margin: '0 0 16px 0', display: 'flex', alignItems: 'center', gap: '8px' }}><ClipboardList size={18}/> Medical History</h2>
          <div style={{ display: 'grid', gap: '16px', fontSize: '0.9rem' }}>
            <div><strong style={{ color: '#334155' }}>Allergies:</strong> {renderList(patient.allergies)}</div>
            <div><strong style={{ color: '#334155' }}>Chronic Conditions:</strong> {renderList(patient.chronicConditions)}</div>
            <div><strong style={{ color: '#334155' }}>Current Medications:</strong> {renderList(patient.currentMedications)}</div>
            <div><strong style={{ color: '#334155' }}>Past Surgeries:</strong> {renderList(patient.pastSurgeries)}</div>
            <div><strong style={{ color: '#334155' }}>Family History:</strong> {renderList(patient.familyHistory)}</div>
          </div>
        </div>

        {/* Quick Flags (Insurance / Injury) */}
        <div style={{ background: 'var(--color-surface)', borderRadius: '12px', border: '1px solid var(--color-border)', padding: '24px' }}>
          <h2 style={{ fontSize: '1.2rem', margin: '0 0 16px 0', display: 'flex', alignItems: 'center', gap: '8px' }}><ShieldPlus size={18}/> Medical Flags</h2>
          <div style={{ display: 'grid', gap: '16px' }}>
            <div>
              <label style={{ display: 'block', fontSize: '0.9rem', fontWeight: 600, color: 'var(--color-navy-900)', marginBottom: '4px' }}>Insurance Status</label>
              <input 
                type="text" 
                value={flags.insuranceStatus} 
                onChange={(e) => setFlags({...flags, insuranceStatus: e.target.value})}
                placeholder="e.g. Covered, Pending, Self-pay"
                style={{ width: '100%', padding: '8px', borderRadius: '6px', border: '1px solid var(--color-border)' }}
              />
            </div>
            <div>
              <label style={{ display: 'block', fontSize: '0.9rem', fontWeight: 600, color: 'var(--color-navy-900)', marginBottom: '4px' }}>Injury / Emergency Status</label>
              <input 
                type="text" 
                value={flags.injuryStatus} 
                onChange={(e) => setFlags({...flags, injuryStatus: e.target.value})}
                placeholder="e.g. Traumatic, Non-urgent"
                style={{ width: '100%', padding: '8px', borderRadius: '6px', border: '1px solid var(--color-border)' }}
              />
            </div>
            <button onClick={() => updateFlags.mutate(flags)} disabled={updateFlags.isPending} style={{ background: 'var(--color-navy-700)', color: 'white', border: 'none', padding: '10px 16px', borderRadius: '8px', fontWeight: 600, display: 'flex', gap: '8px', alignItems: 'center', cursor: 'pointer', justifyContent: 'center' }}>
              <Save size={16} /> Save Flags
            </button>
          </div>
        </div>

        {/* Vitals Chart */}
        <div style={{ background: 'var(--color-surface)', borderRadius: '12px', border: '1px solid var(--color-border)', padding: '24px' }}>
          <h2 style={{ fontSize: '1.2rem', margin: '0 0 16px 0', display: 'flex', alignItems: 'center', gap: '8px' }}><Activity size={18}/> Vitals History</h2>
          {patient.vitalsHistory?.length > 0 ? (
             <ResponsiveContainer width="100%" height={250}>
               <LineChart data={patient.vitalsHistory}>
                 <CartesianGrid strokeDasharray="3 3" vertical={false} />
                 <XAxis dataKey="date" />
                 <YAxis yAxisId="left" />
                 <YAxis yAxisId="right" orientation="right" />
                 <Tooltip />
                 <Legend />
                 <Line yAxisId="left" type="monotone" dataKey="systolic" stroke="var(--color-danger)" name="Systolic BP" />
                 <Line yAxisId="left" type="monotone" dataKey="diastolic" stroke="#f97316" name="Diastolic BP" />
                 <Line yAxisId="right" type="monotone" dataKey="hr" stroke="var(--color-info)" name="Heart Rate" />
               </LineChart>
             </ResponsiveContainer>
          ) : (
            <div style={{ height: '250px', display: 'flex', alignItems: 'center', justifyContent: 'center', color: 'var(--color-text-muted)', fontStyle: 'italic', flexDirection: 'column' }}>
              No vitals data available.
              <span style={{ fontSize: '0.8rem', marginTop: '8px' }}>(Note: Vitals entity schema addition pending)</span>
            </div>
          )}
        </div>
      </div>

      {/* Appointment History & Prescriptions */}
      <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '24px' }}>
        {/* Appointment History */}
        <div style={{ background: 'var(--color-surface)', borderRadius: '12px', border: '1px solid var(--color-border)', padding: '24px' }}>
          <h2 style={{ fontSize: '1.2rem', margin: '0 0 16px 0', display: 'flex', alignItems: 'center', gap: '8px' }}><CalendarIcon size={18}/> Appointment History</h2>
          {(!patient.appointmentHistory || patient.appointmentHistory.length === 0) ? <p style={{ color: 'var(--color-text-muted)' }}>No past appointments with this doctor.</p> : (
            <div style={{ display: 'grid', gap: '12px' }}>
              {patient.appointmentHistory.map((appt) => (
                <div key={appt.appointmentId} style={{ display: 'flex', justifyContent: 'space-between', padding: '16px', border: '1px solid var(--color-surface-alt)', borderRadius: '8px', background: 'var(--color-surface-alt)' }}>
                  <div>
                    <div style={{ fontWeight: 600, color: 'var(--color-text)', marginBottom: '4px' }}>{appt.date !== 'N/A' ? new Date(appt.date).toLocaleString() : 'N/A'}</div>
                    <div style={{ color: 'var(--color-text-muted)', fontSize: '0.9rem' }}><strong>Reason:</strong> {appt.reason || 'None provided'}</div>
                    {appt.notes && <div style={{ color: 'var(--color-text-muted)', fontSize: '0.9rem' }}><strong>Notes:</strong> {appt.notes}</div>}
                  </div>
                  <div>
                    <span style={{ padding: '4px 8px', borderRadius: '4px', fontSize: '0.75rem', fontWeight: 600, background: appt.status === 'COMPLETED' ? 'var(--color-success-bg)' : 'var(--color-surface-alt)', color: appt.status === 'COMPLETED' ? '#166534' : 'var(--color-text-muted)' }}>
                      {appt.status}
                    </span>
                  </div>

                  <div style={{ display: 'flex', alignItems: 'center' }}>
                    <button
                      onClick={async () => {
                         try {
                           const response = await axiosPrivate.get(`/prescriptions/${rx.id}/pdf`, { responseType: 'blob' });
                           const url = window.URL.createObjectURL(new Blob([response.data]));
                           const link = document.createElement('a');
                           link.href = url;
                           link.setAttribute('download', `prescription_${rx.id}.pdf`);
                           document.body.appendChild(link);
                           link.click();
                           link.remove();
                         } catch (e) {
                           console.error('Failed to download PDF', e);
                           alert('Failed to download PDF');
                         }
                      }}
                      style={{ padding: '6px 12px', background: 'var(--color-brand-100)', color: 'var(--color-brand-700)', border: 'none', borderRadius: '4px', cursor: 'pointer', fontSize: '0.85rem', fontWeight: 600 }}
                    >
                      Download PDF
                    </button>
                  </div>
                    </div>
              ))}
            </div>
          )}
        </div>

        {/* Previous Prescriptions */}
        <div style={{ background: 'var(--color-surface)', borderRadius: '12px', border: '1px solid var(--color-border)', padding: '24px' }}>
          <h2 style={{ fontSize: '1.2rem', margin: '0 0 16px 0', display: 'flex', alignItems: 'center', gap: '8px' }}><Pill size={18}/> Previous Prescriptions</h2>
          {(!patient.previousPrescriptions || patient.previousPrescriptions.length === 0) ? <p style={{ color: 'var(--color-text-muted)' }}>No previous prescriptions found.</p> : (
            <div style={{ display: 'grid', gap: '12px' }}>
              {patient.previousPrescriptions.map((rx) => (
                <div key={rx.id} style={{ display: 'flex', justifyContent: 'space-between', padding: '16px', border: '1px solid var(--color-surface-alt)', borderRadius: '8px', background: 'var(--color-surface-alt)' }}>
                  <div>
                    <div style={{ fontWeight: 600, color: 'var(--color-text)', marginBottom: '4px' }}>{rx.date !== 'N/A' ? rx.date : 'N/A'}</div>
                    <div style={{ color: 'var(--color-text-muted)', fontSize: '0.9rem' }}><strong>By:</strong> {rx.doctorName}</div>
                    <div style={{ color: 'var(--color-text-muted)', fontSize: '0.9rem' }}><strong>Items:</strong> {rx.summary} ({rx.itemCount})</div>
                  </div>
                </div>
              ))}
            </div>
          )}
        </div>
      </div>

    </div>
  );
};

export default PatientDetail;
