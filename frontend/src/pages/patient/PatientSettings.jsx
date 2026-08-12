import React, { useState } from 'react';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { axiosPrivate } from '../../api/axios';
import { Bell, Mail, Smartphone, MessageSquare } from 'lucide-react';

const CATEGORIES = [
  { id: 'APPOINTMENTS', label: 'Appointments & Reminders', desc: 'Updates about your bookings and visit reminders.' },
  { id: 'LAB_REPORTS', label: 'Lab & Radiology Reports', desc: 'Alerts when your diagnostic test results are ready.' },
  { id: 'BILLING', label: 'Billing & Payments', desc: 'Invoices, receipts, and payment due reminders.' },
  { id: 'GENERAL', label: 'General Updates', desc: 'Clinic news, health tips, and policy changes.' }
];

const PatientSettings = () => {
  const queryClient = useQueryClient();

  const { data: preferences, isLoading } = useQuery({
    queryKey: ['notification-preferences'],
    queryFn: async () => {
      const res = await axiosPrivate.get('/api/v1/patient/settings/notifications');
      return res.data;
    }
  });

  const updateMutation = useMutation({
    mutationFn: async ({ category, pref }) => {
      const res = await axiosPrivate.put(`/api/v1/patient/settings/notifications/${category}`, pref);
      return res.data;
    },
    onSuccess: () => queryClient.invalidateQueries(['notification-preferences'])
  });

  const handleToggle = (category, type, currentValue) => {
    const existing = preferences?.find(p => p.category === category) || {
      emailEnabled: true, smsEnabled: true, pushEnabled: true, inAppEnabled: true
    };
    updateMutation.mutate({
      category,
      pref: { ...existing, [type]: !currentValue }
    });
  };

  const getPref = (category, type) => {
    const existing = preferences?.find(p => p.category === category);
    if (!existing) return true; // Default true
    return existing[type];
  };

  if (isLoading) return <div className="p-8 text-center text-slate-500">Loading settings...</div>;

  return (
    <div className="p-8 max-w-4xl mx-auto animate-in fade-in">
      <h2 className="text-2xl font-bold text-slate-800 mb-6">Account Settings</h2>

      <div className="bg-white rounded-xl shadow-sm border border-slate-200 overflow-hidden">
        <div className="p-6 border-b border-slate-100 bg-slate-50/50">
          <h3 className="text-lg font-semibold flex items-center gap-2">
            <Bell className="text-blue-500" size={20} />
            Notification Preferences
          </h3>
          <p className="text-sm text-slate-500 mt-1">Control how and when you want to be notified.</p>
        </div>

        <div className="p-0">
          {CATEGORIES.map((cat, idx) => (
            <div key={cat.id} className={`p-6 flex flex-col md:flex-row md:items-center justify-between gap-6 ${idx !== CATEGORIES.length - 1 ? 'border-b border-slate-100' : ''}`}>
              <div className="flex-1">
                <h4 className="font-medium text-slate-800">{cat.label}</h4>
                <p className="text-sm text-slate-500 mt-1">{cat.desc}</p>
              </div>
              
              <div className="flex gap-4">
                <ToggleBtn 
                  icon={<Mail size={16} />} label="Email" 
                  active={getPref(cat.id, 'emailEnabled')} 
                  onClick={() => handleToggle(cat.id, 'emailEnabled', getPref(cat.id, 'emailEnabled'))} 
                />
                <ToggleBtn 
                  icon={<MessageSquare size={16} />} label="SMS" 
                  active={getPref(cat.id, 'smsEnabled')} 
                  onClick={() => handleToggle(cat.id, 'smsEnabled', getPref(cat.id, 'smsEnabled'))} 
                />
                <ToggleBtn 
                  icon={<Smartphone size={16} />} label="Push" 
                  active={getPref(cat.id, 'pushEnabled')} 
                  onClick={() => handleToggle(cat.id, 'pushEnabled', getPref(cat.id, 'pushEnabled'))} 
                />
              </div>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
};

const ToggleBtn = ({ icon, label, active, onClick }) => (
  <button
    onClick={onClick}
    className={`flex flex-col items-center justify-center p-2 rounded-lg border w-16 h-16 transition-colors ${
      active 
        ? 'border-blue-500 bg-blue-50 text-blue-700' 
        : 'border-slate-200 bg-white text-slate-400 hover:bg-slate-50'
    }`}
  >
    {icon}
    <span className="text-[10px] mt-1 font-medium uppercase tracking-wider">{label}</span>
  </button>
);

export default PatientSettings;
