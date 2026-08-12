import React from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { ArrowLeft, ChevronRight } from 'lucide-react';
import Patient360View from '../../components/patient/Patient360View';

const PatientDetail = ({ patientIdOverride }) => {
  const { patientId: paramPatientId } = useParams();
  const patientId = patientIdOverride || paramPatientId;
  const navigate = useNavigate();

  return (
    <div className="p-6 bg-[#F8FAFC] min-h-full font-sans">
      <div className="max-w-[1400px] mx-auto">
        
        {/* Breadcrumb Header */}
        <div className="flex items-center justify-between mb-6">
          <div className="flex items-center gap-2 text-sm font-semibold text-slate-500">
            <span className="cursor-pointer hover:text-slate-800" onClick={() => navigate('/doctor/patients')}>Patients</span>
            <ChevronRight size={14} className="text-slate-400" />
            <span className="text-slate-800">Patient 360</span>
          </div>
          
          <button 
            onClick={() => navigate('/doctor/patients')}
            className="flex items-center gap-2 text-sm font-bold text-slate-700 bg-white border border-slate-200 px-4 py-2 rounded-lg shadow-sm hover:bg-slate-50 transition-colors"
          >
            <ArrowLeft size={16} strokeWidth={2.5} /> Back to Patients
          </button>
        </div>

        {/* Main Grid: Delegate to Patient360View */}
        <Patient360View 
            patientId={patientId} 
            onNavigateToPrescription={(pid) => navigate(`/doctor/patients/${pid}/prescriptions/new`)}
        />

      </div>
    </div>
  );
};

export default PatientDetail;
