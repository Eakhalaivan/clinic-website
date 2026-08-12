import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import axios from 'axios';
import useAuthStore from '../../store/authStore';
import { Activity, FileText, AlertTriangle, List, CheckCircle, Save, Stethoscope, ArrowLeft, Lock, Pill, Send, Paperclip, MessageSquare, Plus, Video, Mic, MicOff } from 'lucide-react';

const ClinicalWorkspace = () => {
  const { id } = useParams(); // encounterId
  const navigate = useNavigate();
  const { token, user } = useAuthStore();
  const [encounter, setEncounter] = useState(null);
  const [soapNote, setSoapNote] = useState({ subjective: '', objective: '', assessment: '', plan: '' });
  const [diagnoses, setDiagnoses] = useState([]);
  const [allergies, setAllergies] = useState([]);
  const [prescriptions, setPrescriptions] = useState([]);
  const [safetyAlerts, setSafetyAlerts] = useState(null);
  const [newRx, setNewRx] = useState({ medicationName: '', dosage: '', frequency: '', duration: '' });
  const [activeTab, setActiveTab] = useState('soap');
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [error, setError] = useState(null);

  const [recordingField, setRecordingField] = useState(null);
  const [teleconsultation, setTeleconsultation] = useState(null);

  useEffect(() => {
    fetchWorkspaceData();
    fetchTeleconsultation();
  }, [id]);

  const fetchTeleconsultation = async () => {
    try {
      const headers = { Authorization: `Bearer ${token}` };
      const res = await axios.get(`/api/doctor/teleconsultations/encounter/${id}`, { headers });
      setTeleconsultation(res.data);
    } catch (err) {
      console.log('No teleconsultation found or error', err);
    }
  };

  const handleDictate = (field) => {
    setRecordingField(field);
    setTimeout(() => {
      setRecordingField(null);
      const simulatedText = "Patient presents with a 3-day history of productive cough, fever, and chills. Denies shortness of breath.";
      setSoapNote(prev => ({
        ...prev,
        [field]: prev[field] + (prev[field] ? ' ' : '') + simulatedText
      }));
    }, 2000);
  };

  const startTeleconsultation = async () => {
    if (!teleconsultation) return;
    try {
      const headers = { Authorization: `Bearer ${token}` };
      const res = await axios.put(`/api/doctor/teleconsultations/${teleconsultation.id}/status?status=IN_PROGRESS`, {}, { headers });
      setTeleconsultation(res.data);
      window.open(res.data.roomUrl, '_blank');
    } catch (err) {
      alert('Failed to start teleconsultation');
    }
  };

  const handleAddPrescription = async () => {
    if (!newRx.medicationName) return;
    try {
      const headers = { Authorization: `Bearer ${token}` };
      
      // Safety check first
      const safetyRes = await axios.post('/api/prescriptions/safety-check', {
        patientId: encounter.patientId,
        medicationNames: [newRx.medicationName]
      }, { headers });

      if (!safetyRes.data.safe) {
        setSafetyAlerts(safetyRes.data.messages);
        if (!window.confirm("Safety alerts detected. Do you want to proceed and override?")) {
           return;
        }
      } else {
        setSafetyAlerts(null);
      }

      // Add as draft
      const payload = {
        patientId: encounter.patientId,
        appointmentId: encounter.appointmentId,
        items: [newRx],
        encounterId: parseInt(id)
      };
      
      await axios.post('/api/prescriptions/draft', payload, { headers });
      setNewRx({ medicationName: '', dosage: '', frequency: '', duration: '' });
      fetchWorkspaceData();
    } catch (err) {
      alert('Failed to add prescription draft.');
    }
  };

  const handleSignPrescription = async (rxId) => {
    try {
      const headers = { Authorization: `Bearer ${token}` };
      await axios.post(`/api/prescriptions/${rxId}/sign`, {}, { headers });
      fetchWorkspaceData();
    } catch (err) {
      alert('Failed to sign prescription.');
    }
  };

  const fetchWorkspaceData = async () => {
    try {
      setLoading(true);
      const headers = { Authorization: `Bearer ${token}` };
      
      const encounterRes = await axios.get(`/api/v1/doctor/encounters/${id}`, { headers });
      setEncounter(encounterRes.data);
      
      const patientId = encounterRes.data.patientId;
      
      const [soapRes, diagRes, allergyRes, rxRes] = await Promise.all([
        axios.get(`/api/v1/doctor/encounters/${id}/soap-note`, { headers }).catch(() => ({ data: null })),
        axios.get(`/api/v1/doctor/patients/${patientId}/diagnoses`, { headers }),
        axios.get(`/api/v1/doctor/patients/${patientId}/allergies`, { headers }),
        axios.get(`/api/prescriptions/patient/${patientId}`, { headers })
      ]);
      
      if (soapRes.data) setSoapNote(soapRes.data);
      setDiagnoses(diagRes.data);
      setAllergies(allergyRes.data);
      setPrescriptions(rxRes.data.filter(rx => rx.encounterId === parseInt(id)));
    } catch (err) {
      setError('Failed to load clinical workspace. Ensure the encounter is assigned to you.');
    } finally {
      setLoading(false);
    }
  };

  const handleSaveSoapNote = async () => {
    setSaving(true);
    try {
      const headers = { Authorization: `Bearer ${token}` };
      const res = await axios.post(`/api/v1/doctor/encounters/${id}/soap-note`, soapNote, { headers });
      setSoapNote(res.data);
      alert('SOAP Note saved successfully');
    } catch (err) {
      alert('Failed to save SOAP note');
    } finally {
      setSaving(false);
    }
  };

  const handleFinalize = async () => {
    if (!window.confirm("Are you sure you want to finalize this encounter? No further changes can be made.")) return;
    try {
      const headers = { Authorization: `Bearer ${token}` };
      await axios.post(`/api/v1/doctor/encounters/${id}/finalize`, {}, { headers });
      fetchWorkspaceData();
    } catch (err) {
      alert(err.response?.data?.message || 'Failed to finalize encounter');
    }
  };

  if (loading) return <div className="p-8 text-center text-slate-500 font-medium">Loading Clinical Workspace...</div>;
  if (error) return <div className="p-8 text-center text-red-600 bg-red-50 rounded-xl m-4 border border-red-200">{error}</div>;

  const isFinalized = encounter?.status === 'Finalized' || encounter?.status === 'Signed';

  return (
    <div className="max-w-7xl mx-auto p-4 sm:p-6 lg:p-8 space-y-6 bg-slate-50 min-h-screen">
      
      {/* Header */}
      <div className="bg-white rounded-2xl shadow-xs border border-slate-200 p-4 sm:p-6 flex flex-col sm:flex-row justify-between items-start sm:items-center gap-4">
        <div>
          <button onClick={() => navigate(-1)} className="text-slate-400 hover:text-indigo-600 flex items-center gap-2 text-sm font-medium mb-3 transition">
            <ArrowLeft size={16} /> Back to Dashboard
          </button>
          <h1 className="text-2xl font-black text-slate-800 tracking-tight flex items-center gap-3">
            <Stethoscope className="text-indigo-600" />
            Clinical Consultation
            {isFinalized && (
              <span className="bg-emerald-100 text-emerald-800 text-xs px-2.5 py-1 rounded-full font-bold flex items-center gap-1">
                <Lock size={12} /> FINALIZED
              </span>
            )}
          </h1>
          <p className="text-sm text-slate-500 mt-1 font-medium">
            Encounter #{encounter.id} • Patient ID: {encounter.patientId} • Status: {encounter.status}
          </p>
        </div>
        
        <div className="flex items-center gap-3 w-full sm:w-auto">
          <button 
            onClick={handleSaveSoapNote}
            disabled={isFinalized || saving}
            className="flex-1 sm:flex-none px-4 py-2 bg-slate-100 text-slate-700 font-bold rounded-xl text-sm flex items-center justify-center gap-2 hover:bg-slate-200 transition disabled:opacity-50"
          >
            <Save size={16} /> {saving ? 'Saving...' : 'Save Draft'}
          </button>
          <button 
            onClick={handleFinalize}
            disabled={isFinalized}
            className="flex-1 sm:flex-none px-4 py-2 bg-indigo-600 text-white font-bold rounded-xl text-sm flex items-center justify-center gap-2 hover:bg-indigo-700 transition shadow-sm disabled:opacity-50"
          >
            <CheckCircle size={16} /> Finalize
          </button>
        </div>
      </div>

      {/* Safety Banner */}
      {allergies.length > 0 && (
        <div className="bg-red-50 border border-red-200 rounded-xl p-4 flex items-start gap-3">
          <AlertTriangle className="text-red-500 shrink-0 mt-0.5" />
          <div>
            <h3 className="text-red-800 font-bold text-sm">Critical Patient Allergies</h3>
            <p className="text-red-600 text-xs mt-1">
              {allergies.map(a => a.allergen).join(', ')}
            </p>
          </div>
        </div>
      )}

      {/* Workspace Grid */}
      <div className="grid grid-cols-1 lg:grid-cols-4 gap-6">
        
        {/* Left Sidebar: Patient 360 & Context */}
        <div className="lg:col-span-1 space-y-6">
          <div className="bg-white rounded-2xl shadow-xs border border-slate-200 p-5">
            <h3 className="font-bold text-slate-800 mb-4 flex items-center gap-2 text-sm border-b border-slate-100 pb-3">
              <Activity size={18} className="text-indigo-500" /> Patient 360
            </h3>
            <div className="space-y-4">
              <div>
                <p className="text-[10px] uppercase font-bold text-slate-400 tracking-wider">Chief Complaint</p>
                <p className="text-sm font-medium text-slate-700 mt-1 bg-slate-50 p-2 rounded-lg">{encounter.chiefComplaint || 'No complaint specified'}</p>
              </div>
              <div>
                <p className="text-[10px] uppercase font-bold text-slate-400 tracking-wider mb-2">Active Diagnoses</p>
                {diagnoses.length > 0 ? (
                  <ul className="space-y-2">
                    {diagnoses.map(d => (
                      <li key={d.id} className="text-xs font-semibold text-slate-700 bg-orange-50 border border-orange-100 px-2 py-1.5 rounded-md flex justify-between">
                        <span>{d.displayName}</span>
                        <span className="text-orange-600">{d.code}</span>
                      </li>
                    ))}
                  </ul>
                ) : (
                  <p className="text-xs text-slate-500 italic">No active diagnoses</p>
                )}
              </div>
            </div>
          </div>
          
          {/* Teleconsultation Card */}
          {teleconsultation && (
            <div className="bg-white rounded-2xl shadow-xs border border-slate-200 p-5 mt-6">
              <h3 className="font-bold text-slate-800 mb-4 flex items-center gap-2 text-sm border-b border-slate-100 pb-3">
                <Video size={18} className="text-indigo-500" /> Teleconsultation
              </h3>
              <div className="space-y-4">
                <div className="flex justify-between items-center bg-slate-50 p-3 rounded-xl border border-slate-100">
                  <span className="text-sm font-semibold text-slate-600">Status</span>
                  <span className={`text-xs font-bold px-2 py-1 rounded-full ${teleconsultation.status === 'IN_PROGRESS' ? 'bg-emerald-100 text-emerald-800' : 'bg-blue-100 text-blue-800'}`}>
                    {teleconsultation.status}
                  </span>
                </div>
                <button
                  onClick={startTeleconsultation}
                  className="w-full py-2.5 bg-indigo-600 hover:bg-indigo-700 text-white font-bold rounded-xl text-sm flex items-center justify-center gap-2 transition"
                >
                  <Video size={16} /> {teleconsultation.status === 'IN_PROGRESS' ? 'Rejoin Call' : 'Start Video Call'}
                </button>
              </div>
            </div>
          )}
        </div>

        {/* Main Content Area */}
        <div className="lg:col-span-3">
          <div className="bg-white rounded-2xl shadow-xs border border-slate-200 flex flex-col h-full min-h-[600px]">
            {/* Tabs */}
            <div className="flex border-b border-slate-200 overflow-x-auto hide-scrollbar p-1 bg-slate-50/50 rounded-t-2xl">
              <button 
                onClick={() => setActiveTab('soap')}
                className={`flex items-center gap-2 px-5 py-3 text-sm font-bold border-b-2 transition whitespace-nowrap ${activeTab === 'soap' ? 'border-indigo-600 text-indigo-700 bg-white shadow-xs rounded-xl' : 'border-transparent text-slate-500 hover:text-slate-700'}`}
              >
                <FileText size={16} /> SOAP Notes
              </button>
              <button 
                onClick={() => setActiveTab('dx')}
                className={`flex items-center gap-2 px-5 py-3 text-sm font-bold border-b-2 transition whitespace-nowrap ${activeTab === 'dx' ? 'border-indigo-600 text-indigo-700 bg-white shadow-xs rounded-xl' : 'border-transparent text-slate-500 hover:text-slate-700'}`}
              >
                <List size={16} /> Diagnoses
              </button>
              <button 
                onClick={() => setActiveTab('allergy')}
                className={`flex items-center gap-2 px-5 py-3 text-sm font-bold border-b-2 transition whitespace-nowrap ${activeTab === 'allergy' ? 'border-indigo-600 text-indigo-700 bg-white shadow-xs rounded-xl' : 'border-transparent text-slate-500 hover:text-slate-700'}`}
              >
                <AlertTriangle size={16} /> Allergies
              </button>
              <button 
                onClick={() => setActiveTab('prescriptions')}
                className={`flex items-center gap-2 px-5 py-3 text-sm font-bold border-b-2 transition whitespace-nowrap ${activeTab === 'prescriptions' ? 'border-indigo-600 text-indigo-700 bg-white shadow-xs rounded-xl' : 'border-transparent text-slate-500 hover:text-slate-700'}`}
              >
                <Pill size={16} /> Prescriptions
              </button>
              <button 
                onClick={() => setActiveTab('referrals')}
                className={`flex items-center gap-2 px-5 py-3 text-sm font-bold border-b-2 transition whitespace-nowrap ${activeTab === 'referrals' ? 'border-indigo-600 text-indigo-700 bg-white shadow-xs rounded-xl' : 'border-transparent text-slate-500 hover:text-slate-700'}`}
              >
                <Send size={16} /> Referrals
              </button>
              <button 
                onClick={() => setActiveTab('attachments')}
                className={`flex items-center gap-2 px-5 py-3 text-sm font-bold border-b-2 transition whitespace-nowrap ${activeTab === 'attachments' ? 'border-indigo-600 text-indigo-700 bg-white shadow-xs rounded-xl' : 'border-transparent text-slate-500 hover:text-slate-700'}`}
              >
                <Paperclip size={16} /> Attachments
              </button>
              <button 
                onClick={() => setActiveTab('messages')}
                className={`flex items-center gap-2 px-5 py-3 text-sm font-bold border-b-2 transition whitespace-nowrap ${activeTab === 'messages' ? 'border-indigo-600 text-indigo-700 bg-white shadow-xs rounded-xl' : 'border-transparent text-slate-500 hover:text-slate-700'}`}
              >
                <MessageSquare size={16} /> Messages
              </button>
            </div>

            {/* Tab Content */}
            <div className="p-6 flex-1 flex flex-col">
              
              {activeTab === 'soap' && (
                <div className="space-y-5 flex-1 flex flex-col">
                  {['subjective', 'objective', 'assessment', 'plan'].map((field) => (
                    <div key={field} className="flex-1 min-h-[120px] flex flex-col">
                      <div className="flex justify-between items-center mb-2">
                        <label className="text-xs font-bold text-slate-500 uppercase tracking-wider">{field}</label>
                        {!isFinalized && (
                          <button
                            onClick={() => handleDictate(field)}
                            disabled={recordingField !== null && recordingField !== field}
                            className={`flex items-center gap-1 text-xs font-bold px-2 py-1 rounded-md transition ${recordingField === field ? 'bg-red-100 text-red-600 animate-pulse' : 'bg-slate-100 text-slate-600 hover:bg-slate-200'}`}
                          >
                            {recordingField === field ? <MicOff size={14} /> : <Mic size={14} />}
                            {recordingField === field ? 'Recording...' : 'Dictate'}
                          </button>
                        )}
                      </div>
                      <textarea
                        disabled={isFinalized || recordingField === field}
                        className="flex-1 w-full bg-slate-50 border border-slate-200 rounded-xl p-3 text-sm text-slate-700 focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:bg-white transition disabled:bg-slate-100 disabled:text-slate-500 disabled:cursor-not-allowed resize-none"
                        value={soapNote[field] || ''}
                        onChange={(e) => setSoapNote({ ...soapNote, [field]: e.target.value })}
                        placeholder={`Enter ${field} details...`}
                      />
                    </div>
                  ))}
                </div>
              )}

              {activeTab === 'dx' && (
                <div className="space-y-4">
                  {!isFinalized && (
                     <div className="bg-slate-50 border border-slate-200 p-4 rounded-xl flex gap-3">
                        <input type="text" placeholder="Search ICD-10 or SNOMED code..." className="flex-1 px-4 py-2 border border-slate-300 rounded-lg text-sm" />
                        <button className="px-4 py-2 bg-indigo-600 text-white text-sm font-bold rounded-lg hover:bg-indigo-700">Add Diagnosis</button>
                     </div>
                  )}
                  <div className="bg-white border border-slate-200 rounded-xl overflow-hidden">
                    <table className="w-full text-left text-sm">
                      <thead className="bg-slate-50 text-slate-500 text-xs uppercase font-bold tracking-wider">
                        <tr>
                          <th className="px-4 py-3">Code</th>
                          <th className="px-4 py-3">Description</th>
                          <th className="px-4 py-3">Status</th>
                          <th className="px-4 py-3">Certainty</th>
                        </tr>
                      </thead>
                      <tbody className="divide-y divide-slate-100">
                        {diagnoses.length === 0 ? (
                          <tr><td colSpan="4" className="px-4 py-6 text-center text-slate-400">No diagnoses added yet.</td></tr>
                        ) : diagnoses.map(d => (
                          <tr key={d.id} className="hover:bg-slate-50">
                            <td className="px-4 py-3 font-semibold text-indigo-600">{d.code}</td>
                            <td className="px-4 py-3 font-medium text-slate-800">{d.displayName}</td>
                            <td className="px-4 py-3"><span className="bg-green-100 text-green-800 text-[10px] px-2 py-1 rounded-full font-bold uppercase">{d.status}</span></td>
                            <td className="px-4 py-3 text-slate-500">{d.certainty}</td>
                          </tr>
                        ))}
                      </tbody>
                    </table>
                  </div>
                </div>
              )}

              {activeTab === 'allergy' && (
                <div className="space-y-4">
                  {!isFinalized && (
                     <div className="bg-slate-50 border border-slate-200 p-4 rounded-xl flex gap-3">
                        <input type="text" placeholder="Search allergen..." className="flex-1 px-4 py-2 border border-slate-300 rounded-lg text-sm" />
                        <select className="px-4 py-2 border border-slate-300 rounded-lg text-sm bg-white"><option>Drug</option><option>Food</option><option>Environmental</option></select>
                        <select className="px-4 py-2 border border-slate-300 rounded-lg text-sm bg-white"><option>Mild</option><option>Moderate</option><option>Severe</option><option>Critical</option></select>
                        <button className="px-4 py-2 bg-indigo-600 text-white text-sm font-bold rounded-lg hover:bg-indigo-700">Add Allergy</button>
                     </div>
                  )}
                  <div className="bg-white border border-slate-200 rounded-xl overflow-hidden">
                    <table className="w-full text-left text-sm">
                      <thead className="bg-slate-50 text-slate-500 text-xs uppercase font-bold tracking-wider">
                        <tr>
                          <th className="px-4 py-3">Allergen</th>
                          <th className="px-4 py-3">Type</th>
                          <th className="px-4 py-3">Reaction</th>
                          <th className="px-4 py-3">Severity</th>
                        </tr>
                      </thead>
                      <tbody className="divide-y divide-slate-100">
                        {allergies.length === 0 ? (
                          <tr><td colSpan="4" className="px-4 py-6 text-center text-slate-400">No allergies reported.</td></tr>
                        ) : allergies.map(a => (
                          <tr key={a.id} className="hover:bg-slate-50">
                            <td className="px-4 py-3 font-semibold text-slate-800">{a.allergen}</td>
                            <td className="px-4 py-3 text-slate-600">{a.allergyType}</td>
                            <td className="px-4 py-3 text-slate-600">{a.reaction}</td>
                            <td className="px-4 py-3">
                               <span className={`text-[10px] px-2 py-1 rounded-full font-bold uppercase ${a.severity === 'Critical' ? 'bg-red-100 text-red-800' : 'bg-orange-100 text-orange-800'}`}>{a.severity}</span>
                            </td>
                          </tr>
                        ))}
                      </tbody>
                    </table>
                  </div>
                </div>
              )}

              {activeTab === 'prescriptions' && (
                <div className="space-y-4">
                  {!isFinalized && (
                     <div className="bg-slate-50 border border-slate-200 p-4 rounded-xl flex gap-3 flex-col sm:flex-row">
                        <input type="text" placeholder="Search medication (e.g. Amoxicillin)..." className="flex-1 px-4 py-2 border border-slate-300 rounded-lg text-sm" value={newRx.medicationName} onChange={e => setNewRx({...newRx, medicationName: e.target.value})} />
                        <div className="flex gap-2">
                           <input type="text" placeholder="Dose" className="w-20 px-3 py-2 border border-slate-300 rounded-lg text-sm" value={newRx.dosage} onChange={e => setNewRx({...newRx, dosage: e.target.value})} />
                           <input type="text" placeholder="Freq" className="w-20 px-3 py-2 border border-slate-300 rounded-lg text-sm" value={newRx.frequency} onChange={e => setNewRx({...newRx, frequency: e.target.value})} />
                           <input type="text" placeholder="Days" className="w-20 px-3 py-2 border border-slate-300 rounded-lg text-sm" value={newRx.duration} onChange={e => setNewRx({...newRx, duration: e.target.value})} />
                           <button onClick={handleAddPrescription} className="px-4 py-2 bg-indigo-600 text-white text-sm font-bold rounded-lg hover:bg-indigo-700">Add Rx</button>
                        </div>
                     </div>
                  )}

                  {safetyAlerts && safetyAlerts.length > 0 && (
                     <div className="bg-red-50 border border-red-200 rounded-xl p-4 mb-4 flex items-start gap-3">
                        <AlertTriangle className="text-red-500 shrink-0 mt-0.5" />
                        <div>
                           <h3 className="text-red-800 font-bold text-sm">Clinical Decision Support Alert</h3>
                           <ul className="text-red-600 text-xs mt-1 list-disc ml-4 space-y-1">
                              {safetyAlerts.map((msg, idx) => (
                                 <li key={idx}>{msg}</li>
                              ))}
                           </ul>
                        </div>
                     </div>
                  )}

                  <div className="bg-white border border-slate-200 rounded-xl overflow-hidden">
                    <table className="w-full text-left text-sm">
                      <thead className="bg-slate-50 text-slate-500 text-xs uppercase font-bold tracking-wider">
                        <tr>
                          <th className="px-4 py-3">Medication</th>
                          <th className="px-4 py-3">Dosage</th>
                          <th className="px-4 py-3">Sig</th>
                          <th className="px-4 py-3">Status</th>
                          <th className="px-4 py-3 text-right">Actions</th>
                        </tr>
                      </thead>
                      <tbody className="divide-y divide-slate-100">
                        {prescriptions.length === 0 ? (
                          <tr><td colSpan="5" className="px-4 py-6 text-center text-slate-400">No prescriptions drafted.</td></tr>
                        ) : prescriptions.map(rx => (
                          <React.Fragment key={rx.id}>
                             <tr className="bg-slate-50">
                                <td colSpan="5" className="px-4 py-2 text-xs font-bold text-slate-500 uppercase tracking-wider flex justify-between">
                                   <span>Prescription #{rx.id}</span>
                                   <span className={`px-2 py-0.5 rounded-md ${rx.status === 'Signed' ? 'bg-emerald-100 text-emerald-800' : rx.status === 'Void' ? 'bg-red-100 text-red-800' : 'bg-slate-200 text-slate-700'}`}>{rx.status}</span>
                                </td>
                             </tr>
                             {rx.items?.map(item => (
                                <tr key={item.id} className="hover:bg-slate-50">
                                  <td className="px-4 py-3 font-semibold text-indigo-600">{item.medicationName}</td>
                                  <td className="px-4 py-3 text-slate-800">{item.dosage}</td>
                                  <td className="px-4 py-3 text-slate-500">{item.frequency} x {item.duration}</td>
                                  <td className="px-4 py-3"></td>
                                  <td className="px-4 py-3 text-right">
                                     {rx.status === 'Draft' && !isFinalized && (
                                        <button className="text-red-500 hover:text-red-700 font-bold text-xs bg-red-50 px-2 py-1 rounded">Remove</button>
                                     )}
                                  </td>
                                </tr>
                             ))}
                             {rx.status === 'Draft' && !isFinalized && (
                                <tr>
                                   <td colSpan="5" className="px-4 py-3 text-right bg-slate-50/50">
                                      <button onClick={() => handleSignPrescription(rx.id)} className="bg-emerald-600 text-white px-4 py-1.5 rounded-lg text-xs font-bold hover:bg-emerald-700">Digital Sign Prescription</button>
                                   </td>
                                </tr>
                             )}
                          </React.Fragment>
                        ))}
                      </tbody>
                    </table>
                  </div>
                </div>
              )}

              {activeTab === 'referrals' && (
                <div className="flex-1 flex flex-col items-center justify-center text-slate-500 space-y-3 p-12">
                  <div className="h-16 w-16 bg-slate-100 rounded-full flex items-center justify-center mb-2">
                    <Send size={32} className="text-slate-400" />
                  </div>
                  <h3 className="text-lg font-bold text-slate-700">Referrals</h3>
                  <p className="text-sm text-center max-w-sm">Create internal and external referrals.</p>
                  <button className="mt-4 flex items-center justify-center px-4 py-2 bg-indigo-600 text-white text-sm font-bold rounded-lg hover:bg-indigo-700">
                    <Plus size={16} className="mr-2" /> New Referral
                  </button>
                </div>
              )}

              {activeTab === 'attachments' && (
                <div className="flex-1 flex flex-col items-center justify-center text-slate-500 space-y-3 p-12">
                  <div className="h-16 w-16 bg-slate-100 rounded-full flex items-center justify-center mb-2">
                    <Paperclip size={32} className="text-slate-400" />
                  </div>
                  <h3 className="text-lg font-bold text-slate-700">Clinical Attachments</h3>
                  <p className="text-sm text-center max-w-sm">Upload encrypted files linked to this encounter.</p>
                  <button className="mt-4 flex items-center justify-center px-4 py-2 bg-indigo-600 text-white text-sm font-bold rounded-lg hover:bg-indigo-700">
                    <Plus size={16} className="mr-2" /> Upload File
                  </button>
                </div>
              )}

              {activeTab === 'messages' && (
                <div className="flex-1 flex flex-col items-center justify-center text-slate-500 space-y-3 p-12">
                  <div className="h-16 w-16 bg-slate-100 rounded-full flex items-center justify-center mb-2">
                    <MessageSquare size={32} className="text-slate-400" />
                  </div>
                  <h3 className="text-lg font-bold text-slate-700">Clinical Messages</h3>
                  <p className="text-sm text-center max-w-sm">Doctor-to-doctor messaging regarding this patient.</p>
                  <button className="mt-4 flex items-center justify-center px-4 py-2 bg-indigo-600 text-white text-sm font-bold rounded-lg hover:bg-indigo-700">
                    <Plus size={16} className="mr-2" /> New Message
                  </button>
                </div>
              )}

            </div>
          </div>
        </div>
        
      </div>
    </div>
  );
};

export default ClinicalWorkspace;
