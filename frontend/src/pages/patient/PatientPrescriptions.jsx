import React from 'react';
import logger from '../../utils/logger';
import { useQuery } from '@tanstack/react-query';
import { axiosPrivate } from '../../api/axios';
import useAuthStore from '../../store/authStore';
import { Pill, Activity, CalendarDays, Info, Eye, X, Printer } from 'lucide-react';
import PrescriptionDocument from '../../components/doctor/PrescriptionDocument';
import { useState } from 'react';
import './PatientPrescriptions.css';

const PatientPrescriptions = () => {
  const { user } = useAuthStore();
  const [viewPrescription, setViewPrescription] = useState(null);

  const { data: prescriptions, isLoading } = useQuery({
    queryKey: ['patientPrescriptions', user?.id],
    queryFn: async () => {
      const res = await axiosPrivate.get(`/prescriptions/patient/${user.id}`);
      return res.data;
    },
    enabled: !!user?.id
  });

  return (
    <div className="prescriptions-page">
      <header className="page-header">
        <h2 className="page-title">My Prescriptions</h2>
      </header>

      {isLoading ? (
        <div className="card">Loading prescriptions...</div>
      ) : prescriptions && prescriptions.length > 0 ? (
        <div className="prescriptions-list">
          {prescriptions.map((prescription, idx) => (
            <div key={prescription.id} className="card card-enter" style={{ animationDelay: `${idx * 100}ms` }}>
              <div style={{ display: 'flex', justifyContent: 'space-between', borderBottom: '1px solid var(--color-border)', paddingBottom: 'var(--space-4)', marginBottom: 'var(--space-4)' }}>
                <div>
                  <h3 style={{ margin: '0 0 var(--space-1) 0' }}>Prescription from Dr. {prescription.doctorName}</h3>
                  <span style={{ fontSize: 'var(--text-sm)', color: 'var(--color-navy-500)' }}>
                    {new Date(prescription.createdAt).toLocaleDateString()}
                  </span>
                </div>
                <div style={{ textAlign: 'right' }}>
                  {/* Pharmacy status pill */}
                  <span style={{
                    display: 'inline-block',
                    padding: '3px 10px',
                    borderRadius: '20px',
                    fontSize: '0.72rem',
                    fontWeight: 700,
                    background: prescription.pharmacyStatus === 'DISPENSED' ? '#dcfce7' : '#fff7ed',
                    color:      prescription.pharmacyStatus === 'DISPENSED' ? '#15803d' : '#c2410c',
                  }}>
                    {prescription.pharmacyStatus === 'DISPENSED' ? '✓ Dispensed' : '⏳ Pending Pharmacy'}
                  </span>
                  {prescription.dispensedAt && (
                    <div style={{ fontSize: '0.72rem', color: 'var(--color-navy-500)', marginTop: 3 }}>
                      by {prescription.dispensedBy} · {new Date(prescription.dispensedAt).toLocaleDateString()}
                    </div>
                  )}
                </div>
              </div>

              {prescription.notes && (
                <div style={{ marginBottom: 'var(--space-4)', padding: 'var(--space-3)', backgroundColor: 'var(--color-surface-hover)', borderRadius: 'var(--radius-md)' }}>
                  <strong>Doctor's Notes:</strong> {prescription.notes}
                </div>
              )}

              <div style={{ marginBottom: 'var(--space-4)', display: 'flex', gap: '10px' }}>

                <button
                  onClick={() => setViewPrescription(prescription)}
                  style={{
                    background: '#e0e7ff', color: '#4338ca', border: 'none', padding: '6px 12px',
                    borderRadius: '6px', fontSize: '0.8rem', fontWeight: 600, cursor: 'pointer',
                    display: 'flex', alignItems: 'center', gap: '6px'
                  }}
                >
                  <Eye size={14} /> View
                </button>
  
                <button
                  onClick={async () => {
                     try {
                       const response = await axiosPrivate.get(`/prescriptions/${prescription.id}/pdf`, { responseType: 'blob' });
                       const url = window.URL.createObjectURL(new Blob([response.data]));
                       const link = document.createElement('a');
                       link.href = url;
                       link.setAttribute('download', `prescription_${prescription.id}.pdf`);
                       document.body.appendChild(link);
                       link.click();
                       link.remove();
                     } catch (e) {
                       logger.error('Failed to download PDF', e);
                       alert('Failed to download PDF');
                     }
                  }}
                  style={{
                    background: '#2563eb', color: 'white', border: 'none', padding: '6px 12px',
                    borderRadius: '6px', fontSize: '0.8rem', fontWeight: 600, cursor: 'pointer'
                  }}
                >
                  Download PDF
                </button>
              </div>

              <div className="medication-grid" style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-3)' }}>
                {prescription.items.map(item => (
                  <div key={item.id} style={{ display: 'flex', gap: 'var(--space-4)', padding: 'var(--space-4)', border: '1px solid var(--color-border)', borderRadius: 'var(--radius-md)' }}>
                    <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'center', width: '48px', height: '48px', backgroundColor: 'var(--color-brand-50)', color: 'var(--color-brand-600)', borderRadius: 'var(--radius-md)', flexShrink: 0 }}>
                      <Pill size={24} />
                    </div>
                    <div style={{ flex: 1 }}>
                      <h4 style={{ margin: '0 0 var(--space-2) 0', fontSize: 'var(--text-lg)', color: 'var(--color-navy-900)' }}>
                        {item.medicationName} <span style={{ fontWeight: 'normal', color: 'var(--color-navy-600)', fontSize: 'var(--text-md)' }}>({item.type ? `${item.type}, ` : ''}{item.dosage})</span>
                      </h4>
                      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(150px, 1fr))', gap: 'var(--space-2)' }}>
                        <div style={{ display: 'flex', alignItems: 'center', gap: 'var(--space-2)', fontSize: 'var(--text-sm)', color: 'var(--color-navy-700)' }}>
                          <Activity size={16} /> {item.frequency}
                        </div>
                        <div style={{ display: 'flex', alignItems: 'center', gap: 'var(--space-2)', fontSize: 'var(--text-sm)', color: 'var(--color-navy-700)' }}>
                          <CalendarDays size={16} /> {item.duration}
                        </div>
                        {item.instructions && (
                          <div style={{ display: 'flex', alignItems: 'center', gap: 'var(--space-2)', fontSize: 'var(--text-sm)', color: 'var(--color-navy-700)' }}>
                            <Info size={16} /> {item.instructions}
                          </div>
                        )}
                      </div>
                    </div>
                  </div>
                ))}
              </div>
            </div>
          ))}
        </div>
      ) : (
        <div className="card empty-state">
          <Pill size={48} className="text-navy-300" />
          <h3>No prescriptions found</h3>
          <p>You don't have any active or past prescriptions.</p>
        </div>
      )}

      {viewPrescription && (
        <div style={{
          position: 'fixed', top: 0, left: 0, width: '100%', height: '100%', 
          backgroundColor: 'rgba(0,0,0,0.5)', zIndex: 9999,
          display: 'flex', justifyContent: 'center', alignItems: 'center', overflow: 'auto', padding: '20px'
        }}>
          <div style={{
            background: 'white', borderRadius: '12px', width: '100%', maxWidth: '900px', 
            maxHeight: '90vh', overflowY: 'auto', padding: '20px', position: 'relative'
          }}>
            <div style={{ display: 'flex', justifyContent: 'flex-end', gap: '10px', marginBottom: '20px' }} className="print:hidden">
              <button 
                onClick={() => window.print()}
                style={{ display: 'flex', alignItems: 'center', gap: '8px', padding: '8px 16px', background: '#4f46e5', color: 'white', border: 'none', borderRadius: '6px', cursor: 'pointer' }}
              >
                <Printer size={16} /> Print
              </button>
              <button 
                onClick={() => setViewPrescription(null)}
                style={{ display: 'flex', alignItems: 'center', gap: '8px', padding: '8px 16px', background: '#f1f5f9', color: '#334155', border: 'none', borderRadius: '6px', cursor: 'pointer' }}
              >
                <X size={16} /> Close
              </button>
            </div>
            <PrescriptionDocument data={{
              doctorName: 'Dr. ' + viewPrescription.doctorName,
              doctorSpecialty: viewPrescription.doctorSpecialty,
              doctorQualifications: viewPrescription.doctorQualifications,
              registrationNumber: viewPrescription.registrationNumber,
              clinicName: viewPrescription.clinicName,
              clinicAddress: viewPrescription.clinicAddress,
              clinicPhone: viewPrescription.clinicPhone,
              clinicEmail: viewPrescription.clinicEmail,
              patientName: viewPrescription.patientName,
              patientAge: viewPrescription.patientAge || 'N/A', 
              patientGender: viewPrescription.patientGender || 'N/A',
              patientId: viewPrescription.patientId,
              chiefComplaint: viewPrescription.chiefComplaint,
              diagnosis: viewPrescription.diagnosis,
              items: viewPrescription.items.map(i => ({
                medicationName: i.medicationName,
                dosage: i.dosage,
                frequency: i.frequency,
                duration: i.duration,
                instructions: i.instructions
              })),
              followUpDate: viewPrescription.followUpDate
            }} />
          </div>
        </div>
      )}
      </div>
  );
};

export default PatientPrescriptions;
