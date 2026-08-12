import React, { useState } from 'react';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { axiosPrivate } from '../../api/axios';
import { Phone, Trash2, Plus, Star } from 'lucide-react';

const EmergencyContactsList = () => {
  const queryClient = useQueryClient();
  const [isAdding, setIsAdding] = useState(false);
  const [formData, setFormData] = useState({
    name: '', relationship: '', primaryPhone: '', alternatePhone: '', isPrimary: false, address: ''
  });

  const { data: contacts, isLoading } = useQuery({
    queryKey: ['emergency-contacts'],
    queryFn: async () => {
      const res = await axiosPrivate.get('/api/v1/patient/settings/emergency-contacts');
      return res.data;
    }
  });

  const addMutation = useMutation({
    mutationFn: async (data) => {
      await axiosPrivate.post('/api/v1/patient/settings/emergency-contacts', data);
    },
    onSuccess: () => {
      queryClient.invalidateQueries(['emergency-contacts']);
      setIsAdding(false);
      setFormData({ name: '', relationship: '', primaryPhone: '', alternatePhone: '', isPrimary: false, address: '' });
    }
  });

  const deleteMutation = useMutation({
    mutationFn: async (id) => {
      await axiosPrivate.delete(`/api/v1/patient/settings/emergency-contacts/${id}`);
    },
    onSuccess: () => queryClient.invalidateQueries(['emergency-contacts'])
  });

  const handleSubmit = (e) => {
    e.preventDefault();
    addMutation.mutate(formData);
  };

  if (isLoading) return <div className="p-4 text-center text-slate-500">Loading contacts...</div>;

  return (
    <div className="mt-8 border-t border-slate-200 pt-8">
      <div className="flex justify-between items-center mb-6">
        <div>
          <h3 className="text-xl font-bold text-slate-800">Emergency Contacts</h3>
          <p className="text-sm text-slate-500">People to contact in case of a medical emergency.</p>
        </div>
        {!isAdding && (
          <button 
            type="button"
            onClick={() => setIsAdding(true)}
            className="flex items-center gap-2 bg-slate-100 text-slate-700 hover:bg-slate-200 px-4 py-2 rounded-lg text-sm font-medium transition-colors"
          >
            <Plus size={16} /> Add Contact
          </button>
        )}
      </div>

      {isAdding && (
        <form onSubmit={handleSubmit} className="bg-slate-50 p-6 rounded-xl border border-slate-200 mb-6">
          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div>
              <label className="block text-sm font-medium text-slate-700 mb-1">Name *</label>
              <input type="text" required value={formData.name} onChange={e => setFormData({...formData, name: e.target.value})} className="w-full p-2 border rounded-md" />
            </div>
            <div>
              <label className="block text-sm font-medium text-slate-700 mb-1">Relationship</label>
              <input type="text" value={formData.relationship} onChange={e => setFormData({...formData, relationship: e.target.value})} className="w-full p-2 border rounded-md" placeholder="e.g. Spouse, Sibling" />
            </div>
            <div>
              <label className="block text-sm font-medium text-slate-700 mb-1">Primary Phone *</label>
              <input type="tel" required value={formData.primaryPhone} onChange={e => setFormData({...formData, primaryPhone: e.target.value})} className="w-full p-2 border rounded-md" placeholder="+1234567890" />
            </div>
            <div>
              <label className="block text-sm font-medium text-slate-700 mb-1">Alternate Phone</label>
              <input type="tel" value={formData.alternatePhone} onChange={e => setFormData({...formData, alternatePhone: e.target.value})} className="w-full p-2 border rounded-md" />
            </div>
            <div className="md:col-span-2">
              <label className="flex items-center gap-2 text-sm font-medium text-slate-700 cursor-pointer">
                <input type="checkbox" checked={formData.isPrimary} onChange={e => setFormData({...formData, isPrimary: e.target.checked})} className="w-4 h-4 rounded text-blue-600" />
                Set as Primary Emergency Contact
              </label>
            </div>
          </div>
          <div className="mt-4 flex justify-end gap-3">
            <button type="button" onClick={() => setIsAdding(false)} className="px-4 py-2 text-sm font-medium text-slate-600 hover:bg-slate-200 rounded-lg">Cancel</button>
            <button type="submit" disabled={addMutation.isPending} className="px-4 py-2 text-sm font-medium bg-blue-600 text-white rounded-lg hover:bg-blue-700 disabled:opacity-50">
              Save Contact
            </button>
          </div>
        </form>
      )}

      <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
        {contacts?.length === 0 ? (
          <div className="col-span-full p-6 text-center text-slate-500 bg-slate-50 rounded-lg border border-dashed border-slate-300">
            No emergency contacts added yet.
          </div>
        ) : (
          contacts?.map(contact => (
            <div key={contact.id} className="bg-white p-4 rounded-xl border border-slate-200 shadow-sm flex flex-col relative group">
              <div className="flex justify-between items-start mb-2">
                <div className="flex items-center gap-2">
                  <h4 className="font-semibold text-slate-800">{contact.name}</h4>
                  {contact.isPrimary && <Star size={14} className="text-yellow-500 fill-yellow-500" title="Primary Contact" />}
                </div>
                <button 
                  type="button"
                  onClick={() => { if(window.confirm('Remove contact?')) deleteMutation.mutate(contact.id) }} 
                  className="text-slate-400 hover:text-red-500 transition-colors opacity-0 group-hover:opacity-100"
                >
                  <Trash2 size={16} />
                </button>
              </div>
              <p className="text-sm text-slate-500 mb-3">{contact.relationship}</p>
              <div className="flex items-center gap-2 text-sm font-medium text-slate-700 bg-slate-50 p-2 rounded w-fit">
                <Phone size={14} className="text-blue-500" />
                {contact.primaryPhone}
              </div>
            </div>
          ))
        )}
      </div>
    </div>
  );
};

export default EmergencyContactsList;
