import React, { useState } from 'react';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { Plus, Edit2, Trash2, Search, X } from 'lucide-react';
import toast from 'react-hot-toast';
import Modal from '../../components/ui/Modal';
import FileUpload from '../../components/ui/FileUpload';

export default function ManageMedicines() {
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [editingMedicine, setEditingMedicine] = useState(null);
  const [formData, setFormData] = useState({
    name: '',
    description: '',
    price: '',
    unit: '',
    stockQuantity: '',
    isActive: true
  });
  
  const queryClient = useQueryClient();
  const baseUrl = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api';

  const { data: medicines = [], isLoading } = useQuery({
    queryKey: ['doctorMedicines'],
    queryFn: async () => {
      const res = await fetch(`${baseUrl}/doctor/medicines`, {
        headers: { 'Authorization': `Bearer ${localStorage.getItem('token')}` }
      });
      if (!res.ok) throw new Error('Failed to fetch medicines');
      return res.json();
    }
  });

  const saveMutation = useMutation({
    mutationFn: async (data) => {
      const url = editingMedicine ? `${baseUrl}/doctor/medicines/${editingMedicine.id}` : `${baseUrl}/doctor/medicines`;
      const method = editingMedicine ? 'PUT' : 'POST';
      const res = await fetch(url, {
        method,
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${localStorage.getItem('token')}`
        },
        body: JSON.stringify(data)
      });
      if (!res.ok) throw new Error('Failed to save medicine');
      return res.json();
    },
    onSuccess: () => {
      toast.success(`Medicine ${editingMedicine ? 'updated' : 'added'} successfully`);
      queryClient.invalidateQueries(['doctorMedicines']);
      closeModal();
    },
    onError: (err) => toast.error(err.message)
  });

  const deleteMutation = useMutation({
    mutationFn: async (id) => {
      const res = await fetch(`${baseUrl}/doctor/medicines/${id}`, {
        method: 'DELETE',
        headers: { 'Authorization': `Bearer ${localStorage.getItem('token')}` }
      });
      if (!res.ok) throw new Error('Failed to delete medicine');
    },
    onSuccess: () => {
      toast.success('Medicine deleted successfully');
      queryClient.invalidateQueries(['doctorMedicines']);
    },
    onError: (err) => toast.error(err.message)
  });

  const openModal = (med = null) => {
    if (med) {
      setEditingMedicine(med);
      setFormData({
        name: med.name,
        description: med.description || '',
        price: med.price,
        unit: med.unit || '',
        stockQuantity: med.stockQuantity,
        isActive: med.isActive
      });
    } else {
      setEditingMedicine(null);
      setFormData({
        name: '', description: '', price: '', unit: '', stockQuantity: '', isActive: true
      });
    }
    setIsModalOpen(true);
  };

  const closeModal = () => {
    setIsModalOpen(false);
    setEditingMedicine(null);
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    saveMutation.mutate(formData);
  };

  return (
    <div className="max-w-6xl mx-auto p-4 sm:p-6 lg:p-8">
      <div className="flex justify-between items-center mb-6">
        <div>
          <h1 className="text-2xl font-bold text-gray-900">Manage My Medicines</h1>
          <p className="text-sm text-gray-500 mt-1">Add and update medicines available to your patients.</p>
        </div>
        <button
          onClick={() => openModal()}
          className="bg-indigo-600 hover:bg-indigo-700 text-white px-4 py-2 rounded-lg font-medium flex items-center gap-2"
        >
          <Plus size={18} /> Add Medicine
        </button>
      </div>

      <div className="bg-white rounded-xl shadow-sm border border-gray-200 overflow-hidden">
        {isLoading ? (
          <div className="p-8 text-center text-gray-500">Loading medicines...</div>
        ) : medicines.length === 0 ? (
          <div className="p-8 text-center text-gray-500">
            No medicines added yet. Click "Add Medicine" to get started.
          </div>
        ) : (
          <div className="overflow-x-auto">
            <table className="w-full text-left border-collapse">
              <thead>
                <tr className="bg-gray-50 text-gray-600 text-sm border-b border-gray-200">
                  <th className="p-4 font-semibold">Medicine</th>
                  <th className="p-4 font-semibold">Price</th>
                  <th className="p-4 font-semibold">Stock</th>
                  <th className="p-4 font-semibold text-right">Actions</th>
                </tr>
              </thead>
              <tbody className="divide-y divide-gray-100">
                {medicines.map((med) => (
                  <tr key={med.id} className="hover:bg-gray-50">
                    <td className="p-4">
                      <div className="font-medium text-gray-900">{med.name}</div>
                      <div className="text-xs text-gray-500">{med.unit} • {med.description}</div>
                    </td>
                    <td className="p-4 font-medium">₹{med.price.toFixed(2)}</td>
                    <td className="p-4">
                      <span className={`font-medium ${med.stockQuantity > 0 ? 'text-green-600' : 'text-red-500'}`}>
                        {med.stockQuantity}
                      </span>
                    </td>
                    <td className="p-4 flex items-center justify-end gap-2">
                      <button onClick={() => openModal(med)} className="p-1.5 text-gray-400 hover:text-indigo-600 transition">
                        <Edit2 size={16} />
                      </button>
                      <button 
                        onClick={() => {
                          if(window.confirm('Are you sure you want to delete this medicine?')) {
                            deleteMutation.mutate(med.id);
                          }
                        }} 
                        className="p-1.5 text-gray-400 hover:text-red-600 transition"
                      >
                        <Trash2 size={16} />
                      </button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
      </div>

      <Modal isOpen={isModalOpen} onClose={closeModal} title={editingMedicine ? "Edit Medicine" : "Add Medicine"}>
        <form onSubmit={handleSubmit} className="space-y-4">
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">Name</label>
            <input required type="text" className="w-full border-gray-300 rounded-lg shadow-sm focus:ring-indigo-500 focus:border-indigo-500" value={formData.name} onChange={e => setFormData({...formData, name: e.target.value})} />
          </div>
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">Description (Composition)</label>
            <input type="text" className="w-full border-gray-300 rounded-lg shadow-sm focus:ring-indigo-500 focus:border-indigo-500" value={formData.description} onChange={e => setFormData({...formData, description: e.target.value})} />
          </div>
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">Package Details (Unit)</label>
            <input type="text" placeholder="e.g. Strip of 10 tablets" className="w-full border-gray-300 rounded-lg shadow-sm focus:ring-indigo-500 focus:border-indigo-500" value={formData.unit} onChange={e => setFormData({...formData, unit: e.target.value})} />
          </div>
          <div className="grid grid-cols-2 gap-4">
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">Price (₹)</label>
              <input required type="number" step="0.01" className="w-full border-gray-300 rounded-lg shadow-sm focus:ring-indigo-500 focus:border-indigo-500" value={formData.price} onChange={e => setFormData({...formData, price: e.target.value})} />
            </div>
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">Stock Quantity</label>
              <input required type="number" className="w-full border-gray-300 rounded-lg shadow-sm focus:ring-indigo-500 focus:border-indigo-500" value={formData.stockQuantity} onChange={e => setFormData({...formData, stockQuantity: e.target.value})} />
            </div>
          </div>
          <div className="flex items-center mt-2">
            <input type="checkbox" id="isActive" checked={formData.isActive} onChange={e => setFormData({...formData, isActive: e.target.checked})} className="h-4 w-4 text-indigo-600 focus:ring-indigo-500 border-gray-300 rounded" />
            <label htmlFor="isActive" className="ml-2 block text-sm text-gray-900">
              Active (Visible to patients)
            </label>
          </div>
          
          <div className="pt-4 flex justify-end gap-3">
            <button type="button" onClick={closeModal} className="px-4 py-2 border border-gray-300 rounded-lg text-gray-700 hover:bg-gray-50 font-medium">Cancel</button>
            <button type="submit" disabled={saveMutation.isPending} className="px-4 py-2 bg-indigo-600 text-white rounded-lg hover:bg-indigo-700 font-medium disabled:bg-indigo-400">
              {saveMutation.isPending ? 'Saving...' : 'Save Medicine'}
            </button>
          </div>
        </form>
      </Modal>
    </div>
  );
}
