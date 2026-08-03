import React, { useState, useEffect } from 'react';
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { axiosPrivate } from '../../api/axios';
import useAuthStore from '../../store/authStore';
import { useNavigate } from 'react-router-dom';

const PatientProfileEdit = () => {
    const { user } = useAuthStore();
    const navigate = useNavigate();
    const queryClient = useQueryClient();
    
    const [formData, setFormData] = useState({
        dateOfBirth: '',
        gender: '',
        bloodGroup: '',
        emergencyContactName: '',
        emergencyContactPhone: '',
        address: '',
        medicalHistorySummary: '',
        branchId: 1 // default branch
    });
    const [error, setError] = useState('');

    const { data: profile, isLoading } = useQuery({
        queryKey: ['patientProfile', user?.id],
        queryFn: async () => {
            const res = await axiosPrivate.get(`/patients/profile/${user.id}`);
            return res.data;
        },
        enabled: !!user?.id,
        retry: false // If 404, it means no profile exists
    });

    useEffect(() => {
        if (profile) {
            setFormData({
                dateOfBirth: profile.dateOfBirth || '',
                gender: profile.gender || '',
                bloodGroup: profile.bloodGroup || '',
                emergencyContactName: profile.emergencyContactName || '',
                emergencyContactPhone: profile.emergencyContactPhone || '',
                address: profile.address || '',
                medicalHistorySummary: profile.medicalHistorySummary || '',
                branchId: profile.branchId || 1
            });
        }
    }, [profile]);

    const mutation = useMutation({
        mutationFn: async (data) => {
            const res = await axiosPrivate.post('/patients/profile', data);
            return res.data;
        },
        onSuccess: () => {
            queryClient.invalidateQueries(['patientProfile', user?.id]);
            navigate('/patient/dashboard');
        },
        onError: (err) => {
            setError(err.response?.data?.message || 'Failed to save profile');
        }
    });

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData(prev => ({ ...prev, [name]: value }));
    };

    const handleSubmit = (e) => {
        e.preventDefault();
        setError('');
        mutation.mutate(formData);
    };

    if (isLoading && !profile) {
        return (
            <div>
                <div className="skeleton card-shape" style={{ height: '400px' }}></div>
            </div>
        );
    }

    return (
        <div>
            <div style={{ marginBottom: 'var(--space-8)' }}>
                <h2 style={{ fontSize: '2rem', marginBottom: 'var(--space-2)' }}>Edit Profile</h2>
                <p style={{ color: 'var(--color-text-muted)' }}>Keep your medical information up to date.</p>
            </div>
            
            <form onSubmit={handleSubmit} className="card" style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-6)' }}>
                {error && <div className="error-message">{error}</div>}
                
                <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(250px, 1fr))', gap: 'var(--space-6)' }}>
                    <div>
                        <label className="label-caps" style={{ display: 'block', marginBottom: 'var(--space-2)' }}>Emergency Contact Name *</label>
                        <input 
                            type="text" 
                            name="emergencyContactName"
                            value={formData.emergencyContactName} 
                            onChange={handleChange} 
                            required
                            className="input-field"
                        />
                    </div>
                    <div>
                        <label className="label-caps" style={{ display: 'block', marginBottom: 'var(--space-2)' }}>Emergency Contact Phone *</label>
                        <input 
                            type="text" 
                            name="emergencyContactPhone"
                            value={formData.emergencyContactPhone} 
                            onChange={handleChange} 
                            required
                            placeholder="+1234567890"
                            className="input-field"
                        />
                    </div>
                    <div>
                        <label className="label-caps" style={{ display: 'block', marginBottom: 'var(--space-2)' }}>Date of Birth</label>
                        <input 
                            type="date" 
                            name="dateOfBirth"
                            value={formData.dateOfBirth} 
                            onChange={handleChange} 
                            className="input-field"
                        />
                    </div>
                    <div>
                        <label className="label-caps" style={{ display: 'block', marginBottom: 'var(--space-2)' }}>Gender</label>
                        <select 
                            name="gender"
                            value={formData.gender} 
                            onChange={handleChange} 
                            className="input-field"
                        >
                            <option value="">Select Gender</option>
                            <option value="Male">Male</option>
                            <option value="Female">Female</option>
                            <option value="Other">Other</option>
                        </select>
                    </div>
                    <div>
                        <label className="label-caps" style={{ display: 'block', marginBottom: 'var(--space-2)' }}>Blood Group</label>
                        <select 
                            name="bloodGroup"
                            value={formData.bloodGroup} 
                            onChange={handleChange} 
                            className="input-field"
                        >
                            <option value="">Select Blood Group</option>
                            <option value="A+">A+</option>
                            <option value="A-">A-</option>
                            <option value="B+">B+</option>
                            <option value="B-">B-</option>
                            <option value="O+">O+</option>
                            <option value="O-">O-</option>
                            <option value="AB+">AB+</option>
                            <option value="AB-">AB-</option>
                        </select>
                    </div>
                </div>

                <div>
                    <label className="label-caps" style={{ display: 'block', marginBottom: 'var(--space-2)' }}>Address</label>
                    <textarea 
                        name="address"
                        value={formData.address} 
                        onChange={handleChange} 
                        rows="3"
                        className="input-field"
                        style={{ resize: 'vertical' }}
                    ></textarea>
                </div>
                
                <div>
                    <label className="label-caps" style={{ display: 'block', marginBottom: 'var(--space-2)' }}>Medical History Summary</label>
                    <textarea 
                        name="medicalHistorySummary"
                        value={formData.medicalHistorySummary} 
                        onChange={handleChange} 
                        rows="3"
                        className="input-field"
                        style={{ resize: 'vertical' }}
                    ></textarea>
                </div>

                <div style={{ display: 'flex', justifyContent: 'flex-end', gap: 'var(--space-4)', paddingTop: 'var(--space-6)', borderTop: '1px solid var(--color-border)' }}>
                    <button 
                        type="button" 
                        onClick={() => navigate('/patient/dashboard')}
                        className="btn-ghost"
                    >
                        Cancel
                    </button>
                    <button 
                        type="submit" 
                        disabled={mutation.isPending}
                        className="btn-primary"
                    >
                        {mutation.isPending ? 'Saving...' : 'Save Profile'}
                    </button>
                </div>
            </form>
        </div>
    );
};

export default PatientProfileEdit;
