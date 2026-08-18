import React, { useState } from 'react';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { axiosPrivate } from '../../api/axios';
import { UserPlus, Trash2, Edit2, AlertCircle } from 'lucide-react';

const PatientDependents = () => {
  const queryClient = useQueryClient();
  const [isAdding, setIsAdding] = useState(false);
  const [formData, setFormData] = useState({
    firstName: '',
    lastName: '',
    dateOfBirth: '',
    gender: 'Male',
    relationship: 'Child',
    medicalHistorySummary: ''
  });

  const { data: dependents, isLoading, error } = useQuery({
    queryKey: ['dependents'],
    queryFn: async () => {
      const res = await axiosPrivate.get('/v1/patient/settings/dependents');
      return res.data;
    }
  });

  const addMutation = useMutation({
    mutationFn: async (newDependent) => {
      const res = await axiosPrivate.post('/v1/patient/settings/dependents', newDependent);
      return res.data;
    },
    onSuccess: () => {
      queryClient.invalidateQueries(['dependents']);
      setIsAdding(false);
      setFormData({
        firstName: '', lastName: '', dateOfBirth: '', gender: 'Male', relationship: 'Child', medicalHistorySummary: ''
      });
    }
  });

  const deleteMutation = useMutation({
    mutationFn: async (id) => {
      await axiosPrivate.delete(`/v1/patient/settings/dependents/${id}`);
    },
    onSuccess: () => queryClient.invalidateQueries(['dependents'])
  });

  const handleSubmit = (e) => {
    e.preventDefault();
    addMutation.mutate(formData);
  };

  if (isLoading) return <div className="p-8 text-center text-slate-500">Loading dependents...</div>;
  if (error) return <div className="p-8 text-red-500">Error loading dependents: {error.message}</div>;

  return (
    <div className="p-8 max-w-4xl mx-auto">
      <div className="flex justify-between items-center mb-6">
        <h2 className="text-2xl font-bold text-slate-800">Family & Dependents</h2>
        {!isAdding && (
          <button 
            onClick={() => setIsAdding(true)}
            className="flex items-center gap-2 bg-blue-600 text-white px-4 py-2 rounded-lg hover:bg-blue-700 transition-colors"
          >
            <UserPlus size={18} />
            Add Dependent
          </button>
        )}
      </div>

      {isAdding && (
        <form onSubmit={handleSubmit} className="bg-white p-6 rounded-xl shadow-sm border border-slate-200 mb-8 animate-in fade-in slide-in-from-top-4">
          <h3 className="text-lg font-semibold mb-4 border-b pb-2">Add New Dependent</h3>
          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div>
              <label className="block text-sm font-medium text-slate-700 mb-1">First Name</label>
              <input type="text" required value={formData.firstName} onChange={e => setFormData({...formData, firstName: e.target.value})} className="w-full p-2 border rounded-md" />
            </div>
            <div>
              <label className="block text-sm font-medium text-slate-700 mb-1">Last Name</label>
              <input type="text" required value={formData.lastName} onChange={e => setFormData({...formData, lastName: e.target.value})} className="w-full p-2 border rounded-md" />
            </div>
            <div>
              <label className="block text-sm font-medium text-slate-700 mb-1">Date of Birth</label>
              <input type="date" required value={formData.dateOfBirth} onChange={e => setFormData({...formData, dateOfBirth: e.target.value})} className="w-full p-2 border rounded-md" />
            </div>
            <div>
              <label className="block text-sm font-medium text-slate-700 mb-1">Relationship</label>
              <select value={formData.relationship} onChange={e => setFormData({...formData, relationship: e.target.value})} className="w-full p-2 border rounded-md">
                <option>Child</option>
                <option>Spouse</option>
                <option>Parent</option>
                <option>Sibling</option>
                <option>Other</option>
              </select>
            </div>
            <div className="md:col-span-2">
              <label className="block text-sm font-medium text-slate-700 mb-1">Medical History Summary</label>
              <textarea value={formData.medicalHistorySummary} onChange={e => setFormData({...formData, medicalHistorySummary: e.target.value})} className="w-full p-2 border rounded-md" rows={3}></textarea>
            </div>
          </div>
          <div className="mt-4 flex justify-end gap-3">
            <button type="button" onClick={() => setIsAdding(false)} className="px-4 py-2 border rounded-lg text-slate-600 hover:bg-slate-50">Cancel</button>
            <button type="submit" disabled={addMutation.isPending} className="px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 disabled:opacity-50">
              {addMutation.isPending ? 'Saving...' : 'Save Dependent'}
            </button>
          </div>
        </form>
      )}

      <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
        {dependents?.length === 0 ? (
          <div className="col-span-full p-8 text-center bg-slate-50 rounded-xl border border-dashed border-slate-300">
            <UserPlus className="mx-auto h-12 w-12 text-slate-400 mb-3" />
            <h3 className="text-lg font-medium text-slate-900">No dependents found</h3>
            <p className="text-slate-500 mt-1">Add your family members to manage their healthcare together.</p>
          </div>
        ) : (
          dependents?.map(dep => (
            <div key={dep.id} className="bg-white p-5 rounded-xl border border-slate-200 shadow-sm hover:shadow-md transition-shadow relative overflow-hidden group">
              <div className="absolute top-4 right-4 flex gap-2 opacity-0 group-hover:opacity-100 transition-opacity">
                <button onClick={() => { if(window.confirm('Delete dependent?')) deleteMutation.mutate(dep.id) }} className="text-red-500 hover:bg-red-50 p-1.5 rounded">
                  <Trash2 size={16} />
                </button>
              </div>
              <div className="flex items-start gap-4">
                <div className="h-12 w-12 bg-blue-100 text-blue-600 rounded-full flex items-center justify-center font-bold text-lg">
                  {dep.firstName[0]}{dep.lastName[0]}
                </div>
                <div>
                  <h3 className="font-semibold text-lg text-slate-800">{dep.firstName} {dep.lastName}</h3>
                  <p className="text-sm text-slate-500">{dep.relationship} • {new Date(dep.dateOfBirth).toLocaleDateString()}</p>
                </div>
              </div>
              {dep.medicalHistorySummary && (
                <div className="mt-4 p-3 bg-slate-50 rounded-lg text-sm text-slate-600 flex items-start gap-2">
                  <AlertCircle size={16} className="text-slate-400 mt-0.5 shrink-0" />
                  <p className="line-clamp-2">{dep.medicalHistorySummary}</p>
                </div>
              )}
            </div>
          ))
        )}
      </div>
    </div>
  );
};

export default PatientDependents;
