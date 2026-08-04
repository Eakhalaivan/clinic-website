import React, { useState } from 'react';
import { useQuery } from '@tanstack/react-query';
import { axiosPrivate } from '../../api/axios';
import { useNavigate } from 'react-router-dom';
import { Search, FileHeart, ChevronRight, UserRound, Calendar, Pill } from 'lucide-react';

const PatientList = ({ onPatientClick }) => {
  const navigate = useNavigate();
  const [query, setQuery] = useState('');
  const [statusFilter, setStatusFilter] = useState('ALL');
  const [sortField, setSortField] = useState('name'); // name, lastVisit, upcoming

  const { data: patients = [], isLoading } = useQuery({
    queryKey: ['doctor-patients'],
    queryFn: async () => (await axiosPrivate.get('/doctor/patients/my')).data,
  });

  const filteredAndSorted = patients
    .filter(p => {
      const matchesQuery = !query || p.name?.toLowerCase().includes(query.toLowerCase()) ||
                           p.phone?.includes(query) || p.patientId?.toString().includes(query);
      const matchesStatus = statusFilter === 'ALL' || p.status === statusFilter;
      return matchesQuery && matchesStatus;
    })
    .sort((a, b) => {
      if (sortField === 'name') return (a.name || '').localeCompare(b.name || '');
      if (sortField === 'lastVisit') return new Date(b.lastVisitDate || 0) - new Date(a.lastVisitDate || 0);
      if (sortField === 'upcoming') {
        const dateA = a.upcomingAppointmentDate ? new Date(a.upcomingAppointmentDate) : new Date(8640000000000000);
        const dateB = b.upcomingAppointmentDate ? new Date(b.upcomingAppointmentDate) : new Date(8640000000000000);
        return dateA - dateB;
      }
      return 0;
    });

  const formatDate = (dateString) => {
    if (!dateString) return 'None';
    return new Date(dateString).toLocaleDateString('en-US', { month: 'short', day: 'numeric', year: 'numeric' });
  };

  return (
    <div className="p-4 sm:p-6" style={{ maxWidth: '1200px', margin: '0 auto' }}>
      <div className="flex flex-col sm:flex-row sm:justify-between sm:items-center gap-2 mb-5">
        <h1 className="text-xl sm:text-2xl font-bold" style={{ color: 'var(--color-text)' }}>My Patients</h1>
        <span className="text-sm" style={{ color: 'var(--color-text-muted)' }}>{filteredAndSorted.length} patients</span>
      </div>

      <div className="flex flex-col sm:flex-row gap-3 mb-5">
        <div style={{ position: 'relative', flex: 1 }}>
          <Search size={16} style={{ position: 'absolute', left: '12px', top: '50%', transform: 'translateY(-50%)', color: 'var(--color-text-muted)' }} />
          <input value={query} onChange={e => setQuery(e.target.value)} placeholder="Search by name, phone, or ID…"
            style={{ width: '100%', padding: '10px 12px 10px 36px', borderRadius: '8px', border: '1px solid var(--color-border)', fontSize: '0.875rem', outline: 'none', boxSizing: 'border-box' }} />
        </div>
        <div className="flex gap-3">
          <select value={statusFilter} onChange={e => setStatusFilter(e.target.value)} style={{ flex: 1, padding: '10px', borderRadius: '8px', border: '1px solid var(--color-border)', outline: 'none' }}>
            <option value="ALL">All Status</option>
            <option value="Active">Active</option>
            <option value="Inactive">Inactive</option>
          </select>
          <select value={sortField} onChange={e => setSortField(e.target.value)} style={{ flex: 1, padding: '10px', borderRadius: '8px', border: '1px solid var(--color-border)', outline: 'none' }}>
            <option value="name">Sort by Name</option>
            <option value="lastVisit">Sort by Last Visit</option>
            <option value="upcoming">Sort by Upcoming Appt</option>
          </select>
        </div>
      </div>

      <div style={{ background: 'var(--color-surface)', borderRadius: '12px', border: '1px solid var(--color-border)', overflow: 'hidden' }}>
        {/* Desktop header — hidden on mobile */}
        <div className="hidden sm:grid" style={{ gridTemplateColumns: '2fr 1fr 1fr 1fr 1fr 200px', padding: '12px 18px', background: 'var(--color-surface-alt)', borderBottom: '1px solid var(--color-border)', fontWeight: 600, fontSize: '0.85rem', color: 'var(--color-text-muted)' }}>
          <div>Patient Info</div>
          <div>Gender/Age</div>
          <div>Last Visit</div>
          <div>Upcoming Appt</div>
          <div>Status</div>
          <div style={{ textAlign: 'right' }}>Actions</div>
        </div>

        {isLoading ? <div style={{ padding: 40, textAlign: 'center', color: 'var(--color-text-muted)' }}>Loading patients…</div> :
          filteredAndSorted.length === 0 ? <div style={{ padding: 40, textAlign: 'center', color: 'var(--color-text-muted)' }}>No patients found</div> : (
          filteredAndSorted.map((p, i) => (
            <div key={p.id} onClick={() => onPatientClick ? onPatientClick(p.patientId) : navigate(`/doctor/patients/${p.patientId}`)}
              className="flex flex-col sm:grid gap-3 sm:gap-0 cursor-pointer p-4 sm:p-0 border-b"
              style={{
                // sm: restore 6-col grid via style (Tailwind grid-template is limited)
              }}
              onMouseEnter={e => e.currentTarget.style.background = 'var(--color-surface-alt)'}
              onMouseLeave={e => e.currentTarget.style.background = 'transparent'}
            >
              {/* Mobile card header */}
              <div className="flex items-center gap-3 sm:hidden">
                <div style={{ width: '36px', height: '36px', borderRadius: '50%', background: 'var(--color-info-bg)', display: 'flex', alignItems: 'center', justifyContent: 'center', flexShrink: 0 }}>
                  <UserRound size={18} color="var(--color-info)" />
                </div>
                <div className="flex-1 min-w-0">
                  <p className="font-semibold text-sm truncate" style={{ color: 'var(--color-text)' }}>{p.name}</p>
                  <p className="text-xs" style={{ color: 'var(--color-text-muted)' }}>{p.phone} · ID: {p.patientId}</p>
                </div>
                <span style={{ padding: '3px 8px', borderRadius: '4px', fontSize: '0.7rem', fontWeight: 600, background: p.status === 'Active' ? 'var(--color-success-bg)' : 'var(--color-surface-alt)', color: p.status === 'Active' ? '#166534' : 'var(--color-text-muted)', whiteSpace: 'nowrap' }}>
                  {p.status}
                </span>
              </div>
              <div className="flex gap-2 sm:hidden pl-12">
                <p className="text-xs" style={{ color: 'var(--color-text-muted)' }}>Last: {formatDate(p.lastVisitDate)}</p>
                <span style={{ color: 'var(--color-text-muted)' }}>·</span>
                <p className="text-xs" style={{ color: 'var(--color-text-muted)' }}>Next: {formatDate(p.upcomingAppointmentDate)}</p>
              </div>
              <div className="flex gap-2 sm:hidden pl-12" onClick={e => e.stopPropagation()}>
                <button onClick={e => { e.stopPropagation(); navigate(`/doctor/patients/${p.patientId}/prescriptions/new`); }}
                  style={{ display: 'flex', alignItems: 'center', gap: '4px', background: '#f0fdfa', color: '#0d9488', border: '1px solid #ccfbf1', padding: '6px 10px', borderRadius: '6px', fontSize: '0.75rem', cursor: 'pointer', fontWeight: 600 }}>
                  <Pill size={12} /> Rx
                </button>
                <button onClick={e => { e.stopPropagation(); navigate(`/doctor/patients/${p.patientId}/notes`); }}
                  style={{ display: 'flex', alignItems: 'center', gap: '4px', background: 'var(--color-info-bg)', color: 'var(--color-info)', border: '1px solid var(--color-info-bg)', padding: '6px 10px', borderRadius: '6px', fontSize: '0.75rem', cursor: 'pointer', fontWeight: 600 }}>
                  <FileHeart size={12} /> Notes
                </button>
              </div>

              {/* Desktop row — hidden on mobile */}
              <div className="hidden sm:grid items-center" style={{ gridTemplateColumns: '2fr 1fr 1fr 1fr 1fr 200px', padding: '14px 18px' }}>
                <div style={{ display: 'flex', alignItems: 'center', gap: '14px' }}>
                  <div style={{ width: '38px', height: '38px', borderRadius: '50%', background: 'var(--color-info-bg)', display: 'flex', alignItems: 'center', justifyContent: 'center', flexShrink: 0 }}>
                    <UserRound size={18} color="var(--color-info)" />
                  </div>
                  <div>
                    <p style={{ margin: 0, fontWeight: 600, fontSize: '0.9rem', color: 'var(--color-text)' }}>{p.name}</p>
                    <p style={{ margin: '2px 0 0', fontSize: '0.78rem', color: 'var(--color-text-muted)' }}>{p.phone} · ID: {p.patientId}</p>
                  </div>
                </div>
                <div style={{ fontSize: '0.85rem', color: 'var(--color-text-muted)' }}>{p.gender || '-'} {p.age ? `(${p.age}y)` : ''}</div>
                <div style={{ fontSize: '0.85rem', color: 'var(--color-text-muted)' }}>{formatDate(p.lastVisitDate)}</div>
                <div style={{ fontSize: '0.85rem', color: 'var(--color-text-muted)' }}>{formatDate(p.upcomingAppointmentDate)}</div>
                <div>
                  <span style={{ padding: '4px 8px', borderRadius: '4px', fontSize: '0.75rem', fontWeight: 600, background: p.status === 'Active' ? 'var(--color-success-bg)' : 'var(--color-surface-alt)', color: p.status === 'Active' ? '#166534' : 'var(--color-text-muted)' }}>
                    {p.status}
                  </span>
                </div>
                <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'flex-end', gap: '8px' }} onClick={e => e.stopPropagation()}>
                  <button onClick={e => { e.stopPropagation(); navigate(`/doctor/patients/${p.patientId}/prescriptions/new`); }}
                    style={{ display: 'flex', alignItems: 'center', gap: '4px', background: '#f0fdfa', color: '#0d9488', border: '1px solid #ccfbf1', padding: '6px 10px', borderRadius: '6px', fontSize: '0.75rem', cursor: 'pointer', fontWeight: 600 }}>
                    <Pill size={12} /> Rx
                  </button>
                  <button onClick={e => { e.stopPropagation(); navigate(`/doctor/patients/${p.patientId}/notes`); }}
                    style={{ display: 'flex', alignItems: 'center', gap: '4px', background: 'var(--color-info-bg)', color: 'var(--color-info)', border: '1px solid var(--color-info-bg)', padding: '6px 10px', borderRadius: '6px', fontSize: '0.75rem', cursor: 'pointer', fontWeight: 600 }}>
                    <FileHeart size={12} /> Notes
                  </button>
                  <ChevronRight size={16} color="var(--color-border)" />
                </div>
              </div>
            </div>
          ))
        )}
      </div>
    </div>
  );
};

export default PatientList;
