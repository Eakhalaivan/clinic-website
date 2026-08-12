import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { axiosPrivate } from '../../api/axios';
import { Dialog, Transition } from '@headlessui/react';
import { 
  ArrowLeft, Search, Bell, PlusSquare, FilePlus, 
  ChevronRight, MoreVertical, Activity, Heart, 
  Wind, CircleDot, Bone, X, Plus, Trash2, FileText
} from 'lucide-react';
import clsx from 'clsx';
import toast, { Toaster } from 'react-hot-toast';
import useDebounce from '../../hooks/pharmacy/useDebounce';
import { Menu } from '@headlessui/react';

const CATEGORIES = [
  { name: 'Respiratory', icon: <Wind size={24} className="text-blue-500" />, desc: 'Cough, Cold, Asthma, COPD' },
  { name: 'Cardiology', icon: <Heart size={24} className="text-red-500" />, desc: 'Hypertension, CHF, Arrhythmia' },
  { name: 'Gastroenterology', icon: <Activity size={24} className="text-emerald-500" />, desc: 'Gastritis, GERD, Diarrhea' },
  { name: 'Endocrinology', icon: <CircleDot size={24} className="text-purple-500" />, desc: 'Diabetes, Thyroid, Obesity' },
  { name: 'Orthopedics', icon: <Bone size={24} className="text-amber-500" />, desc: 'Pain, Arthritis, Injury' },
];

const PrescriptionTemplates = () => {
  const navigate = useNavigate();
  const queryClient = useQueryClient();

  const [activeCategory, setActiveCategory] = useState(null);
  
  // Modals state
  const [isTemplateModalOpen, setIsTemplateModalOpen] = useState(false);
  const [isPatientModalOpen, setIsPatientModalOpen] = useState(false);

  // Search
  const [searchQuery, setSearchQuery] = useState('');
  const debouncedSearch = useDebounce(searchQuery, 300);

  const [patientSearchQuery, setPatientSearchQuery] = useState('');
  const debouncedPatientSearch = useDebounce(patientSearchQuery, 300);

  // Queries
  const { data: templates = [], isLoading: templatesLoading } = useQuery({
    queryKey: ['prescription-templates', activeCategory],
    queryFn: async () => {
      const url = activeCategory 
        ? `/prescriptions/templates?category=${encodeURIComponent(activeCategory)}`
        : `/prescriptions/templates`;
      return (await axiosPrivate.get(url)).data;
    }
  });

  const { data: patients = [] } = useQuery({
    queryKey: ['patients-search', debouncedPatientSearch],
    queryFn: async () => {
      if (!debouncedPatientSearch) return [];
      return (await axiosPrivate.get(`/patients/search?query=${encodeURIComponent(debouncedPatientSearch)}`)).data;
    },
    enabled: isPatientModalOpen && debouncedPatientSearch.length > 0
  });

  // Calculate counts per category based on fetched templates
  // Wait, if activeCategory is selected, templates only contains that category.
  // We need to fetch ALL templates to get accurate counts, or just do it client-side.
  const { data: allTemplates = [] } = useQuery({
    queryKey: ['prescription-templates', null],
    queryFn: async () => (await axiosPrivate.get(`/prescriptions/templates`)).data
  });

  const categoryCounts = CATEGORIES.map(cat => ({
    ...cat,
    count: allTemplates.filter(t => t.category === cat.name).length
  }));

  const filteredTemplates = templates.filter(t => 
    !debouncedSearch || t.name.toLowerCase().includes(debouncedSearch.toLowerCase()) || 
    (t.diagnosis && t.diagnosis.toLowerCase().includes(debouncedSearch.toLowerCase()))
  );

  return (
    <div className="min-h-screen bg-slate-50 font-sans p-6 max-w-[1400px] mx-auto">
      <Toaster position="top-right" />
      {/* Top Bar inside content area */}
      <div className="flex justify-between items-center mb-8">
        <button 
          onClick={() => navigate(-1)} 
          className="flex items-center gap-2 px-4 py-2 bg-white border border-slate-200 rounded-lg text-sm font-semibold text-slate-700 shadow-sm hover:bg-slate-50 transition-colors"
        >
          <ArrowLeft className="w-4 h-4" /> Back
        </button>
        <div className="flex items-center gap-5">
          <div className="relative hidden md:block">
            <Search className="w-4 h-4 absolute left-3 top-1/2 -translate-y-1/2 text-slate-400" />
            <input 
              placeholder="Search..." 
              className="pl-9 pr-4 py-2.5 rounded-lg border border-slate-200 focus:outline-none focus:ring-2 focus:ring-blue-100 focus:border-blue-500 w-72 text-sm transition-colors shadow-sm"
            />
          </div>
          <div className="relative cursor-pointer">
            <Bell className="w-5 h-5 text-slate-500" />
            <div className="absolute -top-1 -right-1 bg-red-500 text-white text-[10px] w-3.5 h-3.5 rounded-full flex items-center justify-center font-bold">
              5
            </div>
          </div>
          <div className="flex items-center gap-3">
            <img loading="lazy" src="https://i.pravatar.cc/150?img=11" alt="Dr. John Doe" className="w-9 h-9 rounded-full object-cover" />
            <div className="hidden sm:block">
              <div className="text-sm font-bold text-slate-900 leading-tight">Dr. John Doe</div>
              <div className="text-xs text-slate-500">Cardiologist</div>
            </div>
          </div>
        </div>
      </div>

      {/* Header Section */}
      <div className="flex flex-col md:flex-row md:justify-between md:items-start mb-6 gap-4">
        <div>
          <h1 className="text-3xl font-bold text-slate-900 mb-1 font-serif tracking-tight">Prescription Templates</h1>
          <p className="text-sm text-slate-500">Create and manage reusable prescription templates for quick prescribing</p>
        </div>
        <div className="flex gap-3">
          <button 
            onClick={() => setIsTemplateModalOpen(true)}
            className="flex items-center gap-2 px-5 py-2.5 bg-white text-blue-600 border border-blue-100 rounded-lg font-semibold text-sm shadow-sm hover:bg-blue-50 transition-colors"
          >
            <PlusSquare className="w-4 h-4" /> Create Template
          </button>
          <button 
            onClick={() => setIsPatientModalOpen(true)}
            className="flex items-center gap-2 px-5 py-2.5 bg-blue-600 text-white rounded-lg font-semibold text-sm shadow-[0_4px_6px_-1px_rgba(37,99,235,0.2)] hover:bg-blue-700 transition-colors"
          >
            <FilePlus className="w-4 h-4" /> Create Prescription
          </button>
        </div>
      </div>

      {/* Category Cards */}
      <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-5 gap-4 mb-8">
        {categoryCounts.map((cat, idx) => (
          <div 
            key={idx} 
            className={clsx(
              "rounded-2xl border p-6 flex flex-col items-center text-center shadow-sm transition-all",
              activeCategory === cat.name ? "bg-blue-50 border-blue-300 shadow-md ring-2 ring-blue-100" : "bg-white border-slate-200 hover:shadow-md cursor-pointer"
            )}
            onClick={() => setActiveCategory(activeCategory === cat.name ? null : cat.name)}
          >
            <div className="bg-white shadow-sm border border-slate-100 w-14 h-14 rounded-full flex items-center justify-center mb-4">
              {cat.icon}
            </div>
            <h3 className="font-bold text-slate-900 text-lg mb-2 font-serif">{cat.name}</h3>
            <div className="bg-blue-100 text-blue-700 px-3 py-1 rounded-full text-xs font-semibold mb-3">
              {cat.count} Templates
            </div>
            <p className="text-xs text-slate-500 leading-relaxed h-9 mb-4">
              {cat.desc}
            </p>
            <button 
              className={clsx(
                "font-semibold text-xs flex items-center gap-1 transition-colors",
                activeCategory === cat.name ? "text-blue-700" : "text-blue-600 hover:text-blue-700"
              )}
            >
              {activeCategory === cat.name ? "Clear Filter" : "View Templates"} <ChevronRight className="w-3 h-3" />
            </button>
          </div>
        ))}
      </div>

      {/* Two Columns Section */}
      <div className="grid grid-cols-1 lg:grid-cols-12 gap-6 pb-12">
        
        {/* Left Column: Templates List */}
        <div className="lg:col-span-12 bg-white rounded-2xl border border-slate-200 p-6 shadow-sm">
          <div className="flex justify-between items-center mb-6">
            <h2 className="text-xl font-bold text-slate-900 font-serif tracking-tight">
              {activeCategory ? `${activeCategory} Templates` : 'All Templates'}
            </h2>
          </div>
          
          <div className="flex gap-3 mb-6">
            <div className="relative w-full max-w-md">
              <Search className="w-4 h-4 absolute left-3 top-1/2 -translate-y-1/2 text-slate-400" />
              <input 
                value={searchQuery}
                onChange={e => setSearchQuery(e.target.value)}
                placeholder="Search templates..." 
                className="w-full pl-9 pr-4 py-2.5 rounded-lg border border-slate-200 focus:outline-none focus:ring-2 focus:ring-blue-100 focus:border-blue-500 text-sm bg-slate-50 transition-colors"
              />
            </div>
          </div>

          <div className="flex flex-col">
            {templatesLoading ? (
              <div className="py-8 text-center text-sm text-slate-500">Loading templates...</div>
            ) : filteredTemplates.length === 0 ? (
              <div className="py-8 text-center text-sm text-slate-500">No templates found.</div>
            ) : (
              filteredTemplates.map((template, idx) => (
                <div key={template.id} className={clsx("flex items-center justify-between py-4", idx < filteredTemplates.length - 1 ? 'border-b border-slate-100' : '')}>
                  <div className="flex items-center gap-3 w-2/5">
                    <div className="w-10 h-10 rounded-xl bg-blue-50 flex items-center justify-center font-bold text-sm shrink-0 text-blue-600 border border-blue-100">
                      <FileText className="w-5 h-5" />
                    </div>
                    <div className="truncate">
                      <div className="text-sm font-bold text-slate-900 truncate">{template.name}</div>
                      <div className="text-xs text-slate-500">{template.category}</div>
                    </div>
                  </div>
                  <div className="w-1/4 text-xs text-slate-500 hidden sm:block truncate">
                    {template.items?.length || 0} Medicines
                  </div>
                  <div className="w-1/4 text-sm text-slate-600 truncate">{template.diagnosis}</div>
                  <TemplateActions 
                    template={template} 
                    onDelete={async (id) => {
                      if (window.confirm('Are you sure you want to delete this template?')) {
                        try {
                          await axiosPrivate.delete(`/prescriptions/templates/${id}`);
                          toast.success('Template deleted');
                          queryClient.invalidateQueries(['prescription-templates']);
                        } catch (e) {
                          toast.error('Failed to delete template');
                        }
                      }
                    }} 
                  />
                </div>
              ))
            )}
          </div>
        </div>
      </div>

      {/* Patient Picker Modal */}
      <Transition show={isPatientModalOpen} as={React.Fragment}>
        <Dialog as="div" className="relative z-50" onClose={() => setIsPatientModalOpen(false)}>
          <Transition.Child
            as={React.Fragment}
            enter="ease-out duration-300"
            enterFrom="opacity-0"
            enterTo="opacity-100"
            leave="ease-in duration-200"
            leaveFrom="opacity-100"
            leaveTo="opacity-0"
          >
            <div className="fixed inset-0 bg-slate-900/50 backdrop-blur-sm" />
          </Transition.Child>

          <div className="fixed inset-0 overflow-y-auto">
            <div className="flex min-h-full items-center justify-center p-4">
              <Transition.Child
                as={React.Fragment}
                enter="ease-out duration-300"
                enterFrom="opacity-0 scale-95"
                enterTo="opacity-100 scale-100"
                leave="ease-in duration-200"
                leaveFrom="opacity-100 scale-100"
                leaveTo="opacity-0 scale-95"
              >
                <Dialog.Panel className="w-full max-w-md transform overflow-hidden rounded-2xl bg-white p-6 shadow-xl transition-all">
                  <div className="flex items-center justify-between mb-5">
                    <Dialog.Title as="h3" className="text-lg font-bold leading-6 text-slate-900">
                      Select Patient
                    </Dialog.Title>
                    <button onClick={() => setIsPatientModalOpen(false)} className="text-slate-400 hover:text-slate-500">
                      <X className="w-5 h-5" />
                    </button>
                  </div>
                  
                  <div className="relative mb-6">
                    <Search className="w-5 h-5 absolute left-3 top-1/2 -translate-y-1/2 text-slate-400" />
                    <input 
                      autoFocus
                      value={patientSearchQuery}
                      onChange={e => setPatientSearchQuery(e.target.value)}
                      placeholder="Search patient by name or phone..." 
                      className="w-full pl-10 pr-4 py-3 rounded-xl border border-slate-200 focus:outline-none focus:ring-2 focus:ring-blue-100 focus:border-blue-500 text-sm"
                    />
                  </div>

                  <div className="flex flex-col gap-2 max-h-80 overflow-y-auto">
                    {patients.length === 0 && debouncedPatientSearch ? (
                      <p className="text-center text-sm text-slate-500 py-4">No patients found matching "{debouncedPatientSearch}"</p>
                    ) : patients.map(p => (
                      <div 
                        key={p.id}
                        onClick={() => navigate(`/doctor/patients/${p.patientId || p.id}/prescriptions/new`)}
                        className="flex items-center gap-4 p-3 rounded-xl hover:bg-slate-50 cursor-pointer border border-transparent hover:border-slate-200 transition-colors"
                      >
                        <div className="w-10 h-10 rounded-full bg-slate-200 flex-shrink-0 overflow-hidden">
                           <img loading="lazy" src={`https://ui-avatars.com/api/?name=${p.firstName}+${p.lastName}&background=cbd5e1&color=334155`} alt="avatar" className="w-full h-full object-cover" />
                        </div>
                        <div>
                          <div className="text-sm font-bold text-slate-900">{p.firstName} {p.lastName}</div>
                          <div className="text-xs font-medium text-slate-500">{p.phone} • {p.gender}</div>
                        </div>
                      </div>
                    ))}
                  </div>
                </Dialog.Panel>
              </Transition.Child>
            </div>
          </div>
        </Dialog>
      </Transition>

      {/* Create Template Modal */}
      <CreateTemplateModal 
        isOpen={isTemplateModalOpen} 
        onClose={() => setIsTemplateModalOpen(false)} 
        onSuccess={() => queryClient.invalidateQueries(['prescription-templates'])}
      />
    </div>
  );
};

const TemplateActions = ({ template, onDelete }) => {
  return (
    <Menu as="div" className="relative inline-block text-left ml-2">
      <Menu.Button className="text-slate-400 hover:text-slate-600 p-1 rounded-full hover:bg-slate-100 transition-colors">
        <MoreVertical className="w-4 h-4" />
      </Menu.Button>
      <Transition
        as={React.Fragment}
        enter="transition ease-out duration-100"
        enterFrom="transform opacity-0 scale-95"
        enterTo="transform opacity-100 scale-100"
        leave="transition ease-in duration-75"
        leaveFrom="transform opacity-100 scale-100"
        leaveTo="transform opacity-0 scale-95"
      >
        <Menu.Items className="absolute right-0 mt-2 w-36 origin-top-right divide-y divide-slate-100 rounded-lg bg-white shadow-lg ring-1 ring-black ring-opacity-5 focus:outline-none z-10">
          <div className="px-1 py-1">
            <Menu.Item>
              {({ active }) => (
                <button
                  onClick={() => onDelete(template.id)}
                  className={`${
                    active ? 'bg-red-50 text-red-600' : 'text-slate-700'
                  } group flex w-full items-center rounded-md px-2 py-2 text-sm font-semibold`}
                >
                  <Trash2 className="mr-2 h-4 w-4 text-red-400" aria-hidden="true" />
                  Delete
                </button>
              )}
            </Menu.Item>
          </div>
        </Menu.Items>
      </Transition>
    </Menu>
  );
};

const CreateTemplateModal = ({ isOpen, onClose, onSuccess }) => {
  const [formData, setFormData] = useState({
    name: '',
    category: 'General',
    chiefComplaint: '',
    diagnosis: '',
    items: [{ medicationName: '', type: 'Tablet', strength: '', dosage: '', frequency: '1-0-1', duration: '5 Days', timing: 'After Food', instructions: '' }]
  });

  const updateItem = (idx, field, value) => {
    const newItems = [...formData.items];
    newItems[idx][field] = value;
    setFormData({ ...formData, items: newItems });
  };

  const addItem = () => {
    setFormData({ ...formData, items: [...formData.items, { medicationName: '', type: 'Tablet', strength: '', dosage: '', frequency: '1-0-1', duration: '5 Days', timing: 'After Food', instructions: '' }] });
  };

  const removeItem = (idx) => {
    const newItems = formData.items.filter((_, i) => i !== idx);
    setFormData({ ...formData, items: newItems });
  };

  const mutation = useMutation({
    mutationFn: async (data) => axiosPrivate.post(`/prescriptions/templates`, data),
    onSuccess: () => {
      toast.success('Template created successfully');
      onSuccess();
      onClose();
      // reset form
      setFormData({
        name: '', category: 'General', chiefComplaint: '', diagnosis: '', items: [{ medicationName: '', type: 'Tablet', strength: '', dosage: '', frequency: '1-0-1', duration: '5 Days', timing: 'After Food', instructions: '' }]
      });
    }
  });

  const handleSubmit = (e) => {
    e.preventDefault();
    if (!formData.name) return toast.error('Template name is required');
    mutation.mutate(formData);
  };

  return (
    <Transition show={isOpen} as={React.Fragment}>
      <Dialog as="div" className="relative z-50" onClose={onClose}>
        <Transition.Child
          as={React.Fragment}
          enter="ease-out duration-300"
          enterFrom="opacity-0"
          enterTo="opacity-100"
          leave="ease-in duration-200"
          leaveFrom="opacity-100"
          leaveTo="opacity-0"
        >
          <div className="fixed inset-0 bg-slate-900/50 backdrop-blur-sm" />
        </Transition.Child>

        <div className="fixed inset-0 overflow-y-auto">
          <div className="flex min-h-full items-center justify-center p-4">
            <Transition.Child
              as={React.Fragment}
              enter="ease-out duration-300"
              enterFrom="opacity-0 scale-95"
              enterTo="opacity-100 scale-100"
              leave="ease-in duration-200"
              leaveFrom="opacity-100 scale-100"
              leaveTo="opacity-0 scale-95"
            >
              <Dialog.Panel className="w-full max-w-4xl transform overflow-hidden rounded-2xl bg-[#F8FAFC] shadow-2xl transition-all flex flex-col max-h-[90vh]">
                <div className="flex items-center justify-between p-6 border-b border-slate-200 bg-white sticky top-0 z-10">
                  <Dialog.Title as="h3" className="text-xl font-bold leading-6 text-slate-900">
                    Create Prescription Template
                  </Dialog.Title>
                  <button onClick={onClose} className="text-slate-400 hover:text-slate-500">
                    <X className="w-6 h-6" />
                  </button>
                </div>
                
                <div className="p-6 overflow-y-auto flex-1">
                  <div className="grid grid-cols-2 gap-5 mb-6">
                    <div>
                        <label className="block text-[11px] font-semibold text-slate-500 mb-1.5">Template Name</label>
                        <input value={formData.name} onChange={e => setFormData({...formData, name: e.target.value})} className="w-full px-3 py-2 bg-white border border-slate-200 rounded-lg text-sm focus:outline-none focus:border-blue-500" placeholder="e.g. Standard HTN Protocol" />
                    </div>
                    <div>
                        <label className="block text-[11px] font-semibold text-slate-500 mb-1.5">Category</label>
                        <select value={formData.category} onChange={e => setFormData({...formData, category: e.target.value})} className="w-full px-3 py-2 bg-white border border-slate-200 rounded-lg text-sm focus:outline-none focus:border-blue-500">
                            {CATEGORIES.map(c => <option key={c.name} value={c.name}>{c.name}</option>)}
                            <option value="General">General</option>
                        </select>
                    </div>
                    <div>
                        <label className="block text-[11px] font-semibold text-slate-500 mb-1.5">Chief Complaint</label>
                        <input value={formData.chiefComplaint} onChange={e => setFormData({...formData, chiefComplaint: e.target.value})} className="w-full px-3 py-2 bg-white border border-slate-200 rounded-lg text-sm focus:outline-none focus:border-blue-500" />
                    </div>
                    <div>
                        <label className="block text-[11px] font-semibold text-slate-500 mb-1.5">Diagnosis</label>
                        <input value={formData.diagnosis} onChange={e => setFormData({...formData, diagnosis: e.target.value})} className="w-full px-3 py-2 bg-white border border-slate-200 rounded-lg text-sm focus:outline-none focus:border-blue-500" />
                    </div>
                  </div>

                  <div className="bg-white rounded-xl border border-slate-200 shadow-sm overflow-hidden mb-6">
                    <div className="overflow-x-auto">
                        <table className="w-full text-left">
                            <thead className="bg-slate-50 border-b border-slate-200">
                                <tr>
                                    <th className="px-3 py-2.5 text-[11px] font-semibold text-slate-600">Medicine Name</th>
                                    <th className="px-3 py-2.5 text-[11px] font-semibold text-slate-600">Type</th>
                                    <th className="px-3 py-2.5 text-[11px] font-semibold text-slate-600">Strength</th>
                                    <th className="px-3 py-2.5 text-[11px] font-semibold text-slate-600 w-24">Dosage</th>
                                    <th className="px-3 py-2.5 text-[11px] font-semibold text-slate-600 w-32">Frequency</th>
                                    <th className="px-3 py-2.5 text-[11px] font-semibold text-slate-600 w-28">Duration</th>
                                    <th className="px-3 py-2.5 text-[11px] font-semibold text-slate-600 w-32">Timing</th>
                                    <th className="px-3 py-2.5 text-[11px] font-semibold text-slate-600 w-10"></th>
                                </tr>
                            </thead>
                            <tbody>
                                {formData.items.map((item, idx) => (
                                    <tr key={idx} className="border-b border-slate-50">
                                        <td className="p-1.5"><input value={item.medicationName} onChange={e => updateItem(idx, 'medicationName', e.target.value)} className="w-full px-2 py-1.5 text-xs border border-slate-200 rounded" placeholder="Medicine" /></td>
                                        <td className="p-1.5"><input value={item.type} onChange={e => updateItem(idx, 'type', e.target.value)} className="w-full px-2 py-1.5 text-xs border border-slate-200 rounded" /></td>
                                        <td className="p-1.5"><input value={item.strength} onChange={e => updateItem(idx, 'strength', e.target.value)} className="w-full px-2 py-1.5 text-xs border border-slate-200 rounded" placeholder="e.g. 500mg" /></td>
                                        <td className="p-1.5"><input value={item.dosage} onChange={e => updateItem(idx, 'dosage', e.target.value)} className="w-full px-2 py-1.5 text-xs border border-slate-200 rounded" placeholder="e.g. 1" /></td>
                                        <td className="p-1.5"><input value={item.frequency} onChange={e => updateItem(idx, 'frequency', e.target.value)} className="w-full px-2 py-1.5 text-xs border border-slate-200 rounded" placeholder="1-0-1" /></td>
                                        <td className="p-1.5"><input value={item.duration} onChange={e => updateItem(idx, 'duration', e.target.value)} className="w-full px-2 py-1.5 text-xs border border-slate-200 rounded" placeholder="5 Days" /></td>
                                        <td className="p-1.5"><select value={item.timing} onChange={e => updateItem(idx, 'timing', e.target.value)} className="w-full px-2 py-1.5 text-xs border border-slate-200 rounded bg-white"><option>After Food</option><option>Before Food</option></select></td>
                                        <td className="p-1.5 text-center"><button onClick={() => removeItem(idx)} className="text-slate-400 hover:text-red-500"><Trash2 className="w-4 h-4"/></button></td>
                                    </tr>
                                ))}
                            </tbody>
                        </table>
                    </div>
                    <div className="p-3 bg-white border-t border-slate-100">
                        <button onClick={addItem} className="flex items-center gap-1.5 px-3 py-1.5 text-xs font-bold text-blue-600 bg-white hover:bg-slate-50 rounded-md transition-colors border border-blue-200">
                            <Plus className="w-3.5 h-3.5" /> Add Medicine
                        </button>
                    </div>
                  </div>
                </div>

                <div className="p-5 border-t border-slate-200 bg-white flex justify-end gap-3 sticky bottom-0 z-10">
                    <button onClick={onClose} className="px-5 py-2.5 text-sm font-semibold text-slate-700 bg-white border border-slate-300 rounded-lg hover:bg-slate-50">Cancel</button>
                    <button onClick={handleSubmit} disabled={mutation.isPending} className="px-5 py-2.5 text-sm font-semibold text-white bg-blue-600 rounded-lg hover:bg-blue-700 disabled:opacity-50">
                        {mutation.isPending ? 'Saving...' : 'Save Template'}
                    </button>
                </div>
              </Dialog.Panel>
            </Transition.Child>
          </div>
        </div>
      </Dialog>
    </Transition>
  );
};

export default PrescriptionTemplates;
