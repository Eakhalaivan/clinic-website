import { useState, useMemo } from 'react';
import { useQuery } from '@tanstack/react-query';
import { Link } from 'react-router-dom';
import { AlertTriangle, Search, Stethoscope } from 'lucide-react';
import { axiosPublic } from '../../api/axios';
import useAuthStore from '../../store/authStore';
import './DoctorList.css';

const DoctorList = () => {
  const { token, roles } = useAuthStore();
  const [searchTerm, setSearchTerm] = useState('');
  const [selectedSpecialty, setSelectedSpecialty] = useState('');

  const { data: doctors, isLoading, error } = useQuery({
    queryKey: ['doctors'],
    queryFn: async () => {
      const response = await axiosPublic.get('/doctors');
      return response.data;
    }
  });

  const filteredDoctors = useMemo(() => {
    if (!doctors) return [];
    return doctors.filter(doc => {
      const matchSearch = doc.userId.toString().includes(searchTerm) || doc.specialty.toLowerCase().includes(searchTerm.toLowerCase());
      const matchSpecialty = selectedSpecialty ? doc.specialty === selectedSpecialty : true;
      return matchSearch && matchSpecialty;
    });
  }, [doctors, searchTerm, selectedSpecialty]);

  const uniqueSpecialties = useMemo(() => {
    if (!doctors) return [];
    return [...new Set(doctors.map(d => d.specialty))].sort();
  }, [doctors]);

  if (error) {
    return (
      <div className="empty-state">
        <div className="empty-state-icon"><AlertTriangle aria-hidden="true" size={48} /></div>
        <h3 className="empty-state-title">System Error</h3>
        <p className="empty-state-desc">Failed to load specialists at this time.</p>
      </div>
    );
  }

  const getBookLink = (doctorId) => {
    if (token && roles.includes('ROLE_PATIENT')) {
      return `/patient/book/${doctorId}`;
    }
    return '/register';
  };

  return (
    <div className="doctor-list-page">
      <div className="doctor-list-header">
        <span className="label-caps">Our Specialists</span>
        <h1 className="page-title">Meet Our Physicians</h1>
        <p>Expert care tailored to you. Browse our network of premium healthcare professionals.</p>
      </div>

      <div className="doctor-list-filters">
        <div className="input-icon-wrapper" style={{ width: '100%' }}>
          <span className="input-icon"><Search aria-hidden="true" size={20} /></span>
          <input 
            type="text" 
            className="input-field has-icon" 
            placeholder="Search by name or specialty..." 
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
          />
        </div>
        <select 
          className="input-field" 
          value={selectedSpecialty}
          onChange={(e) => setSelectedSpecialty(e.target.value)}
        >
          <option value="">All Specialties</option>
          {uniqueSpecialties.map(spec => (
            <option key={spec} value={spec}>{spec}</option>
          ))}
        </select>
      </div>

      {isLoading ? (
        <div className="doctor-grid">
          {[...Array(6)].map((_, i) => (
            <div key={i} className="card">
              <div className="skeleton card-shape" style={{ height: '250px' }}></div>
            </div>
          ))}
        </div>
      ) : filteredDoctors.length > 0 ? (
        <div className="doctor-grid">
          {filteredDoctors.map((doctor, index) => (
            <div key={doctor.id} className="card doctor-card card-enter" style={{ animationDelay: `${index * 50}ms` }}>
              <div className="doctor-avatar">
                {doctor.specialty.substring(0, 1).toUpperCase()}
              </div>
              <h3 className="doctor-name">Dr. {doctor.firstName} {doctor.lastName}</h3>
              <span className="label-caps">{doctor.specialty}</span>
              <p className="doctor-qualifications">{doctor.qualifications}</p>
              <Link to={getBookLink(doctor.userId)} className="btn-secondary doctor-card-action">
                Book Consultation
              </Link>
            </div>
          ))}
        </div>
      ) : (
        <div className="empty-state">
          <div className="empty-state-icon"><Stethoscope aria-hidden="true" size={48} /></div>
          <h3 className="empty-state-title">No specialists found</h3>
          <p className="empty-state-desc">Try adjusting your filters to see more results.</p>
          <button className="btn-primary" onClick={() => { setSearchTerm(''); setSelectedSpecialty(''); }}>Clear Filters</button>
        </div>
      )}
    </div>
  );
};

export default DoctorList;
