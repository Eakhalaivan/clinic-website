import React, { useState } from 'react';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { axiosPrivate } from '../../api/axios';
import { Building2 } from 'lucide-react';

const BranchManagement = () => {
    const queryClient = useQueryClient();
    const [isEditing, setIsEditing] = useState(false);
    const [currentBranch, setCurrentBranch] = useState(null);
    const [formData, setFormData] = useState({
        name: '',
        address: '',
        city: '',
        country: '',
        postalCode: '',
        phoneNumber: '',
        email: '',
        timezone: 'UTC',
        isActive: true
    });

    const { data: branches, isLoading } = useQuery({
        queryKey: ['branches'],
        queryFn: async () => {
            const res = await axiosPrivate.get('/branches');
            return res.data;
        }
    });

    const mutation = useMutation({
        mutationFn: async (branchData) => {
            if (branchData.id) {
                const res = await axiosPrivate.put(`/branches/${branchData.id}`, branchData);
                return res.data;
            } else {
                const res = await axiosPrivate.post('/branches', branchData);
                return res.data;
            }
        },
        onSuccess: () => {
            queryClient.invalidateQueries(['branches']);
            setIsEditing(false);
            setCurrentBranch(null);
        }
    });

    const handleEdit = (branch) => {
        setCurrentBranch(branch);
        setFormData(branch);
        setIsEditing(true);
    };

    const handleCreateNew = () => {
        setCurrentBranch(null);
        setFormData({
            name: '', address: '', city: '', country: '', postalCode: '', phoneNumber: '', email: '', timezone: 'UTC', isActive: true
        });
        setIsEditing(true);
    };

    const handleChange = (e) => {
        const { name, value, type, checked } = e.target;
        setFormData(prev => ({
            ...prev,
            [name]: type === 'checkbox' ? checked : value
        }));
    };

    const handleSubmit = (e) => {
        e.preventDefault();
        mutation.mutate(formData);
    };

    if (isEditing) {
        return (
            <div className="card">
                <div className="admin-section-header">
                    <h3>{currentBranch ? 'Edit Branch' : 'Create Branch'}</h3>
                </div>
                <form onSubmit={handleSubmit} style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-4)' }}>
                    <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(250px, 1fr))', gap: 'var(--space-4)' }}>
                        <div>
                            <label className="label-caps" style={{ display: 'block', marginBottom: 'var(--space-1)' }}>Name</label>
                            <input type="text" name="name" value={formData.name} onChange={handleChange} required className="input-field" />
                        </div>
                        <div>
                            <label className="label-caps" style={{ display: 'block', marginBottom: 'var(--space-1)' }}>Email</label>
                            <input type="email" name="email" value={formData.email} onChange={handleChange} required className="input-field" />
                        </div>
                        <div>
                            <label className="label-caps" style={{ display: 'block', marginBottom: 'var(--space-1)' }}>Phone Number</label>
                            <input type="text" name="phoneNumber" value={formData.phoneNumber} onChange={handleChange} required className="input-field" />
                        </div>
                        <div>
                            <label className="label-caps" style={{ display: 'block', marginBottom: 'var(--space-1)' }}>Timezone</label>
                            <input type="text" name="timezone" value={formData.timezone} onChange={handleChange} required className="input-field" />
                        </div>
                        <div style={{ gridColumn: '1 / -1' }}>
                            <label className="label-caps" style={{ display: 'block', marginBottom: 'var(--space-1)' }}>Address</label>
                            <input type="text" name="address" value={formData.address} onChange={handleChange} required className="input-field" />
                        </div>
                        <div>
                            <label className="label-caps" style={{ display: 'block', marginBottom: 'var(--space-1)' }}>City</label>
                            <input type="text" name="city" value={formData.city} onChange={handleChange} required className="input-field" />
                        </div>
                        <div>
                            <label className="label-caps" style={{ display: 'block', marginBottom: 'var(--space-1)' }}>Country</label>
                            <input type="text" name="country" value={formData.country} onChange={handleChange} required className="input-field" />
                        </div>
                        <div>
                            <label className="label-caps" style={{ display: 'block', marginBottom: 'var(--space-1)' }}>Postal Code</label>
                            <input type="text" name="postalCode" value={formData.postalCode} onChange={handleChange} className="input-field" />
                        </div>
                        <div style={{ display: 'flex', alignItems: 'center', marginTop: 'var(--space-6)' }}>
                            <input type="checkbox" id="isActive" name="isActive" checked={formData.isActive} onChange={handleChange} />
                            <label htmlFor="isActive" style={{ marginLeft: 'var(--space-2)', fontSize: '0.875rem' }}>Active Branch</label>
                        </div>
                    </div>
                    <div style={{ display: 'flex', justifyContent: 'flex-end', gap: 'var(--space-4)', paddingTop: 'var(--space-4)', borderTop: '1px solid var(--color-border)' }}>
                        <button type="button" onClick={() => setIsEditing(false)} className="btn-ghost">Cancel</button>
                        <button type="submit" disabled={mutation.isPending} className="btn-primary">{mutation.isPending ? 'Saving...' : 'Save Branch'}</button>
                    </div>
                </form>
            </div>
        );
    }

    return (
        <section className="card">
            <div className="admin-section-header">
                <h3>Active Branches</h3>
                <button onClick={handleCreateNew} className="btn-primary">Add Branch</button>
            </div>
            
            {isLoading ? (
                <div>
                    <div className="skeleton line-shape" style={{ height: 'var(--space-10)' }}></div>
                    <div className="skeleton line-shape" style={{ height: 'var(--space-10)' }}></div>
                    <div className="skeleton line-shape" style={{ height: 'var(--space-10)' }}></div>
                </div>
            ) : branches && branches.length > 0 ? (
                <div style={{ overflowX: 'auto' }}>
                    <table className="table">
                        <thead>
                            <tr>
                                <th>Name</th>
                                <th>City, Country</th>
                                <th>Phone</th>
                                <th>Timezone</th>
                                <th style={{ textAlign: 'right' }}>Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            {branches.map(branch => (
                                <tr key={branch.id}>
                                    <td style={{ fontWeight: 500 }}>{branch.name}</td>
                                    <td>{branch.city}, {branch.country}</td>
                                    <td>{branch.phoneNumber}</td>
                                    <td>{branch.timezone}</td>
                                    <td style={{ textAlign: 'right' }}>
                                        <button onClick={() => handleEdit(branch)} className="btn-secondary" style={{ padding: 'var(--space-1) var(--space-3)', fontSize: '0.75rem' }}>Edit</button>
                                    </td>
                                </tr>
                            ))}
                        </tbody>
                    </table>
                </div>
            ) : (
                <div className="empty-state">
                    <div className="empty-state-icon"><Building2 size={48} aria-hidden="true" /></div>
                    <h3 className="empty-state-title">No branches found.</h3>
                </div>
            )}
        </section>
    );
};

export default BranchManagement;
