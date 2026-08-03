import React from 'react';
import { useQuery } from '@tanstack/react-query';
import { axiosPrivate } from '../../api/axios';
import useAuthStore from '../../store/authStore';
import { FileText, ClipboardList, Stethoscope } from 'lucide-react';
import './MedicalRecords.css';

const MedicalRecords = () => {
  const { user } = useAuthStore();

  const { data: records, isLoading } = useQuery({
    queryKey: ['patientRecords', user?.id],
    queryFn: async () => {
      const res = await axiosPrivate.get(`/medical-records/patient/${user.id}`);
      return res.data;
    },
    enabled: !!user?.id
  });

  const getIcon = (type) => {
    switch(type) {
      case 'LAB_RESULT': return <ClipboardList className="text-navy-600" size={24} />;
      case 'PRESCRIPTION': return <FileText className="text-navy-600" size={24} />;
      default: return <Stethoscope className="text-navy-600" size={24} />;
    }
  };

  const formatType = (type) => {
    return type.split('_').map(w => w.charAt(0) + w.slice(1).toLowerCase()).join(' ');
  };

  return (
    <div className="medical-records-page">
      <header className="page-header">
        <h2 className="page-title">My Medical Records</h2>
      </header>

      {isLoading ? (
        <div className="card">Loading records...</div>
      ) : records && records.length > 0 ? (
        <div className="records-timeline">
          {records.map((record, idx) => (
            <div key={record.id} className="record-card card card-enter" style={{ animationDelay: `${idx * 100}ms` }}>
              <div className="record-header">
                <div className="record-icon">{getIcon(record.recordType)}</div>
                <div className="record-meta">
                  <h3>{record.title}</h3>
                  <span className="record-date">{new Date(record.createdAt).toLocaleDateString()}</span>
                  <span className={`badge badge-neutral`}>{formatType(record.recordType)}</span>
                </div>
              </div>
              <div className="record-body">
                <p><strong>Doctor:</strong> Dr. {record.doctorName}</p>
                {record.notes && (
                  <div className="record-notes">
                    <h4>Notes:</h4>
                    <p>{record.notes}</p>
                  </div>
                )}
              </div>
            </div>
          ))}
        </div>
      ) : (
        <div className="card empty-state">
          <FileText size={48} className="text-navy-300" />
          <h3>No records found</h3>
          <p>You don't have any medical records available yet.</p>
        </div>
      )}
    </div>
  );
};

export default MedicalRecords;
