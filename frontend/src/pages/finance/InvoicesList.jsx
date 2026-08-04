import React, { useState } from 'react';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { axiosPrivate } from '../../api/axios';
import { FileText, Plus, Download, X } from 'lucide-react';
import { Dialog, Transition } from '@headlessui/react';
import toast, { Toaster } from 'react-hot-toast';

function useDebouncedValue(value, delay = 300) {
  const [debounced, setDebounced] = React.useState(value);
  React.useEffect(() => {
    const t = setTimeout(() => setDebounced(value), delay);
    return () => clearTimeout(t);
  }, [value, delay]);
  return debounced;
}

const InvoicesList = () => {
  const [isModalOpen, setIsModalOpen] = useState(false);
  const queryClient = useQueryClient();

  const [formData, setFormData] = useState({ amount: 0, tax: 0, status: 'UNPAID' });

  // Real patient search state
  const [patientSearch, setPatientSearch] = useState('');
  const [selectedPatient, setSelectedPatient] = useState(null);   // { id, fullName }
  const [patientDropdownOpen, setPatientDropdownOpen] = useState(false);
  const debouncedPatientSearch = useDebouncedValue(patientSearch, 300);

  const { data: patientResults = [], isFetching: patientSearchLoading } = useQuery({
    queryKey: ['patient-search-invoice', debouncedPatientSearch],
    queryFn: async () => {
      if (!debouncedPatientSearch.trim()) return [];
      const res = await axiosPrivate.get('/patients/search', { params: { query: debouncedPatientSearch } });
      return res.data;
    },
    enabled: debouncedPatientSearch.length >= 2,
    staleTime: 30_000,
  });

  const mutation = useMutation({
    mutationFn: async (data) => axiosPrivate.post('/billing/invoices', data),
    onSuccess: () => {
      toast.success('Invoice created successfully');
      queryClient.invalidateQueries(['billing-invoices']);
      setIsModalOpen(false);
      resetForm();
    },
    onError: (err) => toast.error(err?.response?.data?.message || 'Failed to create invoice')
  });

  const resetForm = () => {
    setFormData({ amount: 0, tax: 0, status: 'UNPAID' });
    setSelectedPatient(null);
    setPatientSearch('');
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    if (!selectedPatient) {
      toast.error('Please search for and select a patient.');
      return;
    }
    mutation.mutate({
      patientId: selectedPatient.id,                // ← real patient ID from DB
      description: `${selectedPatient.fullName} - Consultation`,
      dueDate: new Date().toISOString(),
      amount: formData.amount,
      taxAmount: formData.tax
    });
  };

  const { data: invoices = [], isLoading } = useQuery({
    queryKey: ['billing-invoices'],
    queryFn: async () => (await axiosPrivate.get('/billing/invoices')).data,
    staleTime: 60_000,
  });

  return (
    <>
    <div className="p-4 sm:p-6" style={{ maxWidth: '1000px', margin: '0 auto' }}>
      <Toaster position="top-right" />
      <div className="flex flex-col sm:flex-row sm:justify-between sm:items-center gap-3 mb-5">
        <div>
          <h1 className="text-xl sm:text-2xl font-bold" style={{ color: 'var(--color-text)', margin: 0 }}>Invoices &amp; Billing</h1>
          <p style={{ margin: 0, fontSize: '0.8rem', color: 'var(--color-text-muted)' }}>Manage patient &amp; clinic invoices</p>
        </div>
        <button onClick={() => setIsModalOpen(true)} style={{ background: '#3f6212', color: 'var(--color-surface)', border: 'none', padding: '8px 16px', borderRadius: '8px', fontWeight: 600, fontSize: '0.85rem', cursor: 'pointer', display: 'flex', alignItems: 'center', gap: '6px', alignSelf: 'flex-start' }}>
          <Plus size={16} /> Create Invoice
        </button>
      </div>

      <div style={{ background: 'var(--color-surface)', borderRadius: '12px', border: '1px solid var(--color-border)', overflow: 'hidden' }}>
        <div className="overflow-x-auto">
          <table style={{ width: '100%', borderCollapse: 'collapse', textAlign: 'left', minWidth: '560px' }}>
          <thead style={{ background: 'var(--color-surface-alt)', borderBottom: '1px solid var(--color-border)' }}>
            <tr>
              <th style={{ padding: '12px 16px', color: 'var(--color-text-muted)', fontSize: '0.8rem' }}>Invoice #</th>
              <th style={{ padding: '12px 16px', color: 'var(--color-text-muted)', fontSize: '0.8rem' }}>Patient</th>
              <th style={{ padding: '12px 16px', color: 'var(--color-text-muted)', fontSize: '0.8rem' }}>Date</th>
              <th style={{ padding: '12px 16px', color: 'var(--color-text-muted)', fontSize: '0.8rem' }}>Amount</th>
              <th style={{ padding: '12px 16px', color: 'var(--color-text-muted)', fontSize: '0.8rem' }}>Status</th>
              <th style={{ padding: '12px 16px', color: 'var(--color-text-muted)', fontSize: '0.8rem' }}>Action</th>
            </tr>
          </thead>
          <tbody>
            {isLoading ? (
              <tr><td colSpan={6} style={{ padding: '24px', textAlign: 'center', color: 'var(--color-text-muted)' }}>Loading invoices…</td></tr>
            ) : invoices.length === 0 ? (
              <tr><td colSpan={6} style={{ padding: '24px', textAlign: 'center', color: 'var(--color-text-muted)' }}>No invoices found.</td></tr>
            ) : invoices.map(inv => (
              <tr key={inv.id} style={{ borderBottom: '1px solid var(--color-surface-alt)' }}>
                <td style={{ padding: '12px 16px', fontWeight: 700, fontSize: '0.85rem', color: '#3f6212' }}>{inv.id}</td>
                <td style={{ padding: '12px 16px', fontSize: '0.85rem', color: 'var(--color-text)', fontWeight: 600 }}>{inv.patient || inv.patientName}</td>
                <td style={{ padding: '12px 16px', fontSize: '0.8rem', color: 'var(--color-text-muted)' }}>{inv.date || inv.dueDate?.split('T')[0]}</td>
                <td style={{ padding: '12px 16px', fontWeight: 700, fontSize: '0.85rem', color: 'var(--color-text)' }}>₹{Number(inv.amount || 0).toLocaleString()}</td>
                <td style={{ padding: '12px 16px' }}>
                  <span style={{
                    padding: '3px 8px', borderRadius: '4px', fontSize: '0.75rem', fontWeight: 600,
                    background: inv.status === 'PAID' ? 'var(--color-success-bg)' : inv.status === 'PARTIAL' ? '#fef9c3' : 'var(--color-danger-bg)',
                    color: inv.status === 'PAID' ? 'var(--color-success)' : inv.status === 'PARTIAL' ? '#854d0e' : 'var(--color-danger)'
                  }}>
                    {inv.status}
                  </span>
                </td>
                <td style={{ padding: '12px 16px' }}>
                  <button onClick={async () => {
                    try {
                      const res = await axiosPrivate.get(`/billing/invoices/${inv.id}/pdf`, { responseType: 'blob' });
                      const url = window.URL.createObjectURL(new Blob([res.data]));
                      const link = document.createElement('a');
                      link.href = url;
                      link.setAttribute('download', `invoice_${inv.id}.pdf`);
                      document.body.appendChild(link);
                      link.click();
                      link.remove();
                    } catch (e) {
                      toast.error('Failed to download PDF');
                    }
                  }} style={{ background: 'var(--color-surface-alt)', color: 'var(--color-text)', border: 'none', padding: '5px 10px', borderRadius: '5px', fontSize: '0.75rem', cursor: 'pointer', display: 'flex', alignItems: 'center', gap: '4px' }}>
                    <Download size={13} /> PDF
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
        </div>{/* overflow-x-auto */}
      </div>
    </div>

      <Transition show={isModalOpen} as={React.Fragment}>
        <Dialog as="div" className="relative z-50" onClose={() => { setIsModalOpen(false); resetForm(); }}>
          <Transition.Child as={React.Fragment} enter="ease-out duration-300" enterFrom="opacity-0" enterTo="opacity-100" leave="ease-in duration-200" leaveFrom="opacity-100" leaveTo="opacity-0">
            <div className="fixed inset-0 bg-slate-900/50 backdrop-blur-sm" />
          </Transition.Child>

          <div className="fixed inset-0 overflow-y-auto">
            <div className="flex min-h-full items-center justify-center p-4">
              <Transition.Child as={React.Fragment} enter="ease-out duration-300" enterFrom="opacity-0 scale-95" enterTo="opacity-100 scale-100" leave="ease-in duration-200" leaveFrom="opacity-100 scale-100" leaveTo="opacity-0 scale-95">
                <Dialog.Panel className="w-full max-w-md transform overflow-hidden rounded-2xl bg-white p-6 shadow-xl transition-all">
                  <div className="flex items-center justify-between mb-5">
                    <Dialog.Title as="h3" className="text-lg font-bold leading-6 text-slate-900">Create Invoice</Dialog.Title>
                    <button onClick={() => { setIsModalOpen(false); resetForm(); }} className="text-slate-400 hover:text-slate-500"><X className="w-5 h-5" /></button>
                  </div>
                  <form onSubmit={handleSubmit} className="flex flex-col gap-4">

                    {/* ── Patient Search (real autocomplete) ── */}
                    <div>
                      <label className="block text-[11px] font-semibold text-slate-500 mb-1">Patient <span className="text-red-500">*</span></label>
                      <div className="relative">
                        {selectedPatient ? (
                          <div className="flex items-center justify-between px-3 py-2 border rounded-lg text-sm bg-green-50 border-green-300">
                            <span className="font-medium text-green-800">{selectedPatient.fullName}</span>
                            <button type="button" onClick={() => setSelectedPatient(null)} className="text-green-600 hover:text-red-500 ml-2"><X size={14} /></button>
                          </div>
                        ) : (
                          <>
                            <input
                              value={patientSearch}
                              onChange={e => { setPatientSearch(e.target.value); setPatientDropdownOpen(true); }}
                              onFocus={() => setPatientDropdownOpen(true)}
                              placeholder="Search patient by name or UHID…"
                              className="w-full px-3 py-2 border rounded-lg text-sm"
                            />
                            {patientDropdownOpen && (patientResults.length > 0 || patientSearchLoading) && (
                              <div className="absolute z-10 top-full mt-1 left-0 right-0 bg-white border rounded-lg shadow-lg max-h-48 overflow-y-auto">
                                {patientSearchLoading && <div className="px-3 py-2 text-xs text-slate-400">Searching…</div>}
                                {patientResults.map(p => (
                                  <button key={p.id} type="button"
                                    className="w-full text-left px-3 py-2 text-sm hover:bg-slate-100 flex flex-col"
                                    onClick={() => { setSelectedPatient(p); setPatientDropdownOpen(false); setPatientSearch(''); }}>
                                    <span className="font-medium">{p.fullName}</span>
                                    <span className="text-xs text-slate-400">{p.uhid}</span>
                                  </button>
                                ))}
                                {!patientSearchLoading && patientResults.length === 0 && debouncedPatientSearch.length >= 2 && (
                                  <div className="px-3 py-2 text-xs text-slate-400">No patients found.</div>
                                )}
                              </div>
                            )}
                          </>
                        )}
                      </div>
                    </div>

                    <div>
                      <label className="block text-[11px] font-semibold text-slate-500 mb-1">Amount</label>
                      <input required type="number" value={formData.amount} onChange={e => setFormData({...formData, amount: Number(e.target.value)})} className="w-full px-3 py-2 border rounded-lg text-sm" />
                    </div>
                    <div>
                      <label className="block text-[11px] font-semibold text-slate-500 mb-1">Tax</label>
                      <input required type="number" value={formData.tax} onChange={e => setFormData({...formData, tax: Number(e.target.value)})} className="w-full px-3 py-2 border rounded-lg text-sm" />
                    </div>
                    <div className="pt-4 flex justify-end gap-3">
                      <button type="button" onClick={() => { setIsModalOpen(false); resetForm(); }} className="px-4 py-2 text-sm font-semibold text-slate-600 bg-slate-100 rounded-lg hover:bg-slate-200">Cancel</button>
                      <button type="submit" disabled={mutation.isPending || !selectedPatient} className="px-4 py-2 text-sm font-semibold text-white bg-[#3f6212] rounded-lg hover:bg-lime-800 disabled:opacity-50">
                        {mutation.isPending ? 'Creating...' : 'Create Invoice'}
                      </button>
                    </div>
                  </form>
                </Dialog.Panel>
              </Transition.Child>
            </div>
          </div>
        </Dialog>
      </Transition>
    </>
  );
};

export default InvoicesList;
