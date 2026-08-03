import React, { useState } from 'react';
import { useMutation, useQueryClient } from '@tanstack/react-query';
import { axiosPrivate } from '../../../api/axios';
import { HeartPulse, Users, Save, Activity, FileText } from 'lucide-react';
import toast from 'react-hot-toast';
import Card from '../../ui/Card';
import Button from '../../ui/Button';
import FormField from '../../ui/FormField';
import Badge from '../../ui/Badge';
import EmptyState from '../../ui/EmptyState';
import Skeleton from '../../ui/Skeleton';
import PatientProfileCard from '../../PatientProfileCard';

export const NurseAssignedPatientsWidget = ({ assignmentsList, isAssignmentsLoading, selectedPatientId, setSelectedPatientId }) => {
  const [showTokenForm, setShowTokenForm] = useState(false);
  const [walkInForm, setWalkInForm] = useState({ patientId: '', firstName: '', lastName: '', phone: '', reasonForVisit: '' });
  const queryClient = useQueryClient();

  const registerWalkIn = useMutation({
    mutationFn: async (data) => {
      const payload = { firstName: data.firstName, lastName: data.lastName, phone: data.phone, reasonForVisit: data.reasonForVisit };
      if (data.patientId) { payload.patient = { id: parseInt(data.patientId) }; }
      const res = await axiosPrivate.post(`/reception/branches/1/walk-ins`, payload);
      const walkIn = res.data;
      const tokenRes = await axiosPrivate.post(`/reception/branches/1/queue/generate?walkInId=${walkIn.id}`);
      return { walkIn, token: tokenRes.data };
    },
    onSuccess: (data) => {
      toast.success(`OP Registered! OP No: ${data.walkIn.opNumber} | Token No: ${data.token.tokenNumber}`);
      setWalkInForm({ patientId: '', firstName: '', lastName: '', phone: '', reasonForVisit: '' });
      setShowTokenForm(false);
      queryClient.invalidateQueries({ queryKey: ['nurseAssignments'] });
    },
    onError: (err) => toast.error(err.response?.data?.message || 'Failed to register walk-in')
  });

  return (
    <Card className="h-[600px] flex flex-col">
      <Card.Header className="flex justify-between items-center">
        <h2 className="font-display font-bold text-lg text-[var(--color-navy-900)] m-0 flex items-center gap-2">
          <Users className="w-5 h-5 text-[var(--color-navy-800)]" /> Assigned Patients
        </h2>
        <Button variant="outline" size="sm" onClick={() => setShowTokenForm(!showTokenForm)}>+ OP Token</Button>
      </Card.Header>
      <Card.Body className="p-2 flex-1 overflow-y-auto">
        {showTokenForm && (
          <div className="p-3 mb-2 bg-[var(--color-surface-alt)] rounded-md border border-[var(--color-border)] space-y-3">
            <p className="text-xs font-semibold text-[var(--color-text)] m-0">Register Walk-in / OP Token</p>
            <input type="text" placeholder="Existing Patient ID (optional)" className="input-field py-1 px-2 text-sm w-full" value={walkInForm.patientId} onChange={e => setWalkInForm({...walkInForm, patientId: e.target.value})} />
            {!walkInForm.patientId && (
              <div className="space-y-2">
                <div className="grid grid-cols-2 gap-2">
                  <input type="text" placeholder="First Name" className="input-field py-1 px-2 text-sm" value={walkInForm.firstName} onChange={e => setWalkInForm({...walkInForm, firstName: e.target.value})} />
                  <input type="text" placeholder="Last Name" className="input-field py-1 px-2 text-sm" value={walkInForm.lastName} onChange={e => setWalkInForm({...walkInForm, lastName: e.target.value})} />
                </div>
                <input type="text" placeholder="Phone Number" className="input-field py-1 px-2 text-sm w-full" value={walkInForm.phone} onChange={e => setWalkInForm({...walkInForm, phone: e.target.value})} />
              </div>
            )}
            <input type="text" placeholder="Reason for Visit" className="input-field py-1 px-2 text-sm w-full" value={walkInForm.reasonForVisit} onChange={e => setWalkInForm({...walkInForm, reasonForVisit: e.target.value})} />
            <div className="flex gap-2">
              <Button variant="primary" size="sm" className="w-full" onClick={() => registerWalkIn.mutate(walkInForm)} isLoading={registerWalkIn.isPending}>Register OP</Button>
              <Button variant="outline" size="sm" onClick={() => { setShowTokenForm(false); setWalkInForm({ patientId: '', firstName: '', lastName: '', phone: '', reasonForVisit: '' }); }}>Cancel</Button>
            </div>
          </div>
        )}
        {isAssignmentsLoading ? (
          <Skeleton count={4} variant="line" className="h-10 mb-2" />
        ) : assignmentsList?.length === 0 ? (
          <EmptyState title="No Patients Assigned" description="There are currently no OP patients assigned to your nursing queue." />
        ) : (
          <div className="space-y-1">
            {assignmentsList?.map((assignment) => {
              const isSelected = selectedPatientId === assignment.patientId;
              return (
                <div key={assignment.id} onClick={() => setSelectedPatientId(assignment.patientId)} className={`p-3 rounded-md border transition-all cursor-pointer flex items-center justify-between gap-2 ${isSelected ? 'border-[var(--color-navy-600)] bg-[var(--color-navy-800)]/10 shadow-sm' : 'border-transparent hover:bg-[var(--color-surface-alt)]'}`}>
                  <div>
                    <p className="font-semibold text-sm text-[var(--color-navy-900)] m-0">{assignment.patientName}</p>
                    <p className="text-xs text-[var(--color-text-muted)] m-0 mb-1">Dr. {assignment.attendingDoctorName}</p>
                    <div className="flex gap-1 flex-wrap mt-1">
                      {assignment.insuranceStatus && <span className="text-[10px] px-1.5 py-0.5 rounded-sm bg-blue-100 text-blue-800 border border-blue-200">Ins: {assignment.insuranceStatus}</span>}
                    </div>
                  </div>
                  <Badge variant={isSelected ? 'info' : 'neutral'} size="sm">{isSelected ? 'Active' : 'Select'}</Badge>
                </div>
              );
            })}
          </div>
        )}
      </Card.Body>
    </Card>
  );
};

export const VitalSignsFormWidget = ({ selectedPatientId, selectedPatient }) => {
  const queryClient = useQueryClient();
  const [vitalSign, setVitalSign] = useState({ temperature: '', bloodPressure: '', heartRate: '', respiratoryRate: '', oxygenSaturation: '', notes: '' });

  const recordVitals = useMutation({
    mutationFn: async (data) => {
      const res = await axiosPrivate.post(`/patients/${selectedPatientId}/vitals`, data);
      return res.data;
    },
    onSuccess: () => {
      toast.success('Vital signs recorded successfully!');
      setVitalSign({ temperature: '', bloodPressure: '', heartRate: '', respiratoryRate: '', oxygenSaturation: '', notes: '' });
      queryClient.invalidateQueries({ queryKey: ['nurseAssignments'] });
      queryClient.invalidateQueries({ queryKey: ['nursingRecentActivity'] });
    },
    onError: (err) => toast.error(err.response?.data?.message || 'Failed to record vital signs')
  });

  if (!selectedPatientId || !selectedPatient) {
    return (
      <Card className="min-h-[300px] flex items-center justify-center h-full">
        <EmptyState icon={Users} title="No Patient Selected" description="Select an assigned patient from the list on the left to view records and log triage vitals." />
      </Card>
    );
  }

  return (
    <div className="space-y-6 h-[600px] overflow-y-auto">
      <PatientProfileCard patient={{ firstName: selectedPatient.patientName.split(' ')[0], lastName: selectedPatient.patientName.split(' ')[1] || '', age: selectedPatient.age, id: selectedPatient.patientId }} />
      <Card>
        <Card.Header><h2 className="font-display font-bold text-lg text-[var(--color-navy-900)] m-0 flex items-center gap-2"><Activity className="w-5 h-5 text-[var(--color-navy-800)]" /> Record Vital Signs</h2></Card.Header>
        <Card.Body>
          <form onSubmit={e => { e.preventDefault(); recordVitals.mutate(vitalSign); }} className="space-y-4">
            <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
              <FormField label="Temperature (°C)" required id="temp"><input id="temp" type="number" step="0.1" className="input-field" value={vitalSign.temperature} onChange={e => setVitalSign({...vitalSign, temperature: e.target.value})} placeholder="e.g. 37.2" required /></FormField>
              <FormField label="Blood Pressure (mmHg)" required id="bp"><input id="bp" type="text" className="input-field" value={vitalSign.bloodPressure} onChange={e => setVitalSign({...vitalSign, bloodPressure: e.target.value})} placeholder="e.g. 120/80" required /></FormField>
              <FormField label="Heart Rate (bpm)" required id="hr"><input id="hr" type="number" className="input-field" value={vitalSign.heartRate} onChange={e => setVitalSign({...vitalSign, heartRate: e.target.value})} placeholder="e.g. 72" required /></FormField>
              <FormField label="Respiratory Rate (bpm)" required id="rr"><input id="rr" type="number" className="input-field" value={vitalSign.respiratoryRate} onChange={e => setVitalSign({...vitalSign, respiratoryRate: e.target.value})} placeholder="e.g. 16" required /></FormField>
              <FormField label="Oxygen Saturation SpO2 (%)" required id="spo2"><input id="spo2" type="number" step="0.1" className="input-field" value={vitalSign.oxygenSaturation} onChange={e => setVitalSign({...vitalSign, oxygenSaturation: e.target.value})} placeholder="e.g. 98" required /></FormField>
            </div>
            <FormField label="Clinical Nursing Notes" id="notes"><textarea id="notes" className="input-field" rows={3} value={vitalSign.notes} onChange={e => setVitalSign({...vitalSign, notes: e.target.value})} placeholder="Observe patient symptoms, medication tolerance, or triage notes..." /></FormField>
            <div className="pt-2 flex justify-end"><Button type="submit" variant="primary" icon={Save} isLoading={recordVitals.isPending}>Save Vitals</Button></div>
          </form>
        </Card.Body>
      </Card>
    </div>
  );
};

export const NurseRecentActivityWidget = ({ recentActivity, isActivityLoading }) => (
  <Card className="h-[600px] flex flex-col">
    <Card.Header><h2 className="font-display font-bold text-lg text-[var(--color-navy-900)] m-0 flex items-center gap-2"><Activity className="w-5 h-5 text-[var(--color-navy-800)]" /> Recent Activity</h2></Card.Header>
    <Card.Body className="p-5 flex-1 overflow-y-auto">
      {isActivityLoading ? (
        <Skeleton count={5} variant="line" className="h-12 mb-3" />
      ) : recentActivity?.length === 0 ? (
        <EmptyState icon={Activity} title="No Recent Activity" description="Quiet shift so far. No events to show." />
      ) : (
        <div className="space-y-5">
          {recentActivity?.map((act, i) => (
            <div key={i} className="flex gap-3 relative">
              {i !== recentActivity.length - 1 && <div className="absolute left-[13px] top-8 w-px h-10 bg-[var(--color-border)]"></div>}
              <div className="w-7 h-7 rounded-full flex items-center justify-center shrink-0 z-10 ring-4 ring-[var(--color-surface)]" style={{ backgroundColor: act.bg || '#f3f4f6', color: act.color || '#3b82f6' }}><Activity size={12} /></div>
              <div className="flex-1 min-w-0 pt-0.5">
                <p className="text-xs font-bold text-[var(--color-text)] truncate">{act.title}</p>
                <p className="text-[10px] text-[var(--color-text-muted)]">{act.sub}</p>
              </div>
              <div className="text-[10px] text-[var(--color-text-muted)] font-medium pt-0.5">{act.time ? new Date(act.time).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' }) : ''}</div>
            </div>
          ))}
        </div>
      )}
    </Card.Body>
  </Card>
);
