import React, { useState, useCallback } from 'react';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { axiosPrivate } from '../../api/axios';
import { Users, UserPlus, Search, Mail, Phone, Building2, X } from 'lucide-react';
import { Dialog, Transition } from '@headlessui/react';
import toast, { Toaster } from 'react-hot-toast';

/**
 * Debounce helper – avoids a dependency on an external hook
 */
function useDebouncedValue(value, delay = 300) {
  const [debounced, setDebounced] = React.useState(value);
  React.useEffect(() => {
    const t = setTimeout(() => setDebounced(value), delay);
    return () => clearTimeout(t);
  }, [value, delay]);
  return debounced;
}

const Employees = () => {
  const [search, setSearch] = useState('');
  const [isModalOpen, setIsModalOpen] = useState(false);
  const queryClient = useQueryClient();

  // ── Form state ──────────────────────────────────────────────────────────────
  const [formData, setFormData] = useState({
    designation: '',
    department: '',
    status: 'ACTIVE'
  });

  // Real user-select state
  const [userSearch, setUserSearch] = useState('');
  const [selectedUser, setSelectedUser] = useState(null);     // { id, firstName, lastName, email }
  const [userDropdownOpen, setUserDropdownOpen] = useState(false);

  const debouncedUserSearch = useDebouncedValue(userSearch, 300);

  // Fetch matching users from backend
  const { data: userResults = [], isFetching: userSearchLoading } = useQuery({
    queryKey: ['user-search', debouncedUserSearch],
    queryFn: async () => {
      if (!debouncedUserSearch.trim()) return [];
      const res = await axiosPrivate.get('/users/search', { params: { q: debouncedUserSearch } });
      return res.data;
    },
    enabled: debouncedUserSearch.length >= 1,
    staleTime: 30_000,
  });

  // ── Mutations ────────────────────────────────────────────────────────────────
  const mutation = useMutation({
    mutationFn: async (data) => axiosPrivate.post('/hr/employees', data),
    onSuccess: () => {
      toast.success('Employee added successfully');
      queryClient.invalidateQueries(['hr-employees-list']);
      setIsModalOpen(false);
      resetForm();
    },
    onError: (err) => toast.error(err?.response?.data?.message || 'Failed to add employee')
  });

  const resetForm = () => {
    setFormData({ designation: '', department: '', status: 'ACTIVE' });
    setSelectedUser(null);
    setUserSearch('');
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    if (!selectedUser) {
      toast.error('Please select a user account to link this employee to.');
      return;
    }
    mutation.mutate({
      userId: selectedUser.id,                       // ← real user ID from DB
      department: formData.department,
      designation: formData.designation,
      employmentType: 'FULL_TIME',
      dateOfJoining: new Date().toISOString().split('T')[0],
      salary: 0,
      isActive: formData.status === 'ACTIVE'
    });
  };

  // ── Employee list query ──────────────────────────────────────────────────────
  const { data: employees = [], isLoading } = useQuery({
    queryKey: ['hr-employees-list'],
    queryFn: async () => (await axiosPrivate.get('/hr/employees')).data,
    staleTime: 60_000,
  });

  const filtered = employees.filter(e =>
    !search ||
    (e.name || '').toLowerCase().includes(search.toLowerCase()) ||
    (e.department || '').toLowerCase().includes(search.toLowerCase())
  );

  return (
    <>
    <div className="p-4 sm:p-6" style={{ maxWidth: '1000px', margin: '0 auto' }}>
      <Toaster position="top-right" />
      <div className="flex flex-col sm:flex-row sm:justify-between sm:items-center gap-3 mb-5">
        <div>
          <h1 className="text-xl sm:text-2xl font-bold" style={{ color: 'var(--color-text)', margin: 0 }}>Employee Directory</h1>
          <p style={{ margin: 0, fontSize: '0.8rem', color: 'var(--color-text-muted)' }}>Staff records linked with system user accounts</p>
        </div>
        <button onClick={() => setIsModalOpen(true)} style={{ background: '#be185d', color: 'var(--color-surface)', border: 'none', padding: '8px 16px', borderRadius: '8px', fontWeight: 600, fontSize: '0.85rem', cursor: 'pointer', display: 'flex', alignItems: 'center', gap: '6px', alignSelf: 'flex-start' }}>
          <UserPlus size={16} /> Add Employee
        </button>
      </div>

      {/* Search bar */}
      <div style={{ marginBottom: '16px' }}>
        <input
          value={search}
          onChange={e => setSearch(e.target.value)}
          placeholder="Filter by name or department…"
          className="w-full sm:w-64"
          style={{ padding: '7px 12px', borderRadius: '8px', border: '1px solid var(--color-border)', fontSize: '0.85rem' }}
        />
      </div>

      <div style={{ background: 'var(--color-surface)', borderRadius: '12px', border: '1px solid var(--color-border)', overflow: 'hidden' }}>
        <div className="overflow-x-auto">
          <table style={{ width: '100%', borderCollapse: 'collapse', textAlign: 'left', minWidth: '520px' }}>
          <thead style={{ background: 'var(--color-surface-alt)', borderBottom: '1px solid var(--color-border)' }}>
            <tr>
              <th style={{ padding: '12px 16px', color: 'var(--color-text-muted)', fontSize: '0.8rem' }}>Name &amp; Designation</th>
              <th style={{ padding: '12px 16px', color: 'var(--color-text-muted)', fontSize: '0.8rem' }}>Department</th>
              <th style={{ padding: '12px 16px', color: 'var(--color-text-muted)', fontSize: '0.8rem' }}>Email</th>
              <th style={{ padding: '12px 16px', color: 'var(--color-text-muted)', fontSize: '0.8rem' }}>Phone</th>
              <th style={{ padding: '12px 16px', color: 'var(--color-text-muted)', fontSize: '0.8rem' }}>Status</th>
            </tr>
          </thead>
          <tbody>
            {isLoading ? (
              <tr><td colSpan={5} style={{ padding: '24px', textAlign: 'center', color: 'var(--color-text-muted)' }}>Loading employees…</td></tr>
            ) : filtered.length === 0 ? (
              <tr><td colSpan={5} style={{ padding: '24px', textAlign: 'center', color: 'var(--color-text-muted)' }}>No employees found.</td></tr>
            ) : filtered.map(e => (
              <tr key={e.id} style={{ borderBottom: '1px solid var(--color-surface-alt)' }}>
                <td style={{ padding: '12px 16px' }}>
                  <span style={{ fontWeight: 600, fontSize: '0.875rem', color: 'var(--color-text)', display: 'block' }}>{e.name}</span>
                  <span style={{ fontSize: '0.75rem', color: 'var(--color-text-muted)' }}>{e.designation}</span>
                </td>
                <td style={{ padding: '12px 16px', fontSize: '0.8rem', color: 'var(--color-text)' }}>{e.department}</td>
                <td style={{ padding: '12px 16px', fontSize: '0.8rem', color: 'var(--color-text-muted)' }}>{e.email}</td>
                <td style={{ padding: '12px 16px', fontSize: '0.8rem', color: 'var(--color-text-muted)' }}>{e.phone}</td>
                <td style={{ padding: '12px 16px' }}>
                  <span style={{ background: 'var(--color-success-bg)', color: 'var(--color-success)', padding: '3px 8px', borderRadius: '4px', fontSize: '0.75rem', fontWeight: 600 }}>{e.status || 'ACTIVE'}</span>
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
            <div className="fixed inset-0 bg-black bg-opacity-25" />
          </Transition.Child>

          <div className="fixed inset-0 overflow-y-auto">
            <div className="flex min-h-full items-center justify-center p-4 text-center">
              <Transition.Child as={React.Fragment} enter="ease-out duration-300" enterFrom="opacity-0 scale-95" enterTo="opacity-100 scale-100" leave="ease-in duration-200" leaveFrom="opacity-100 scale-100" leaveTo="opacity-0 scale-95">
                <Dialog.Panel className="w-full max-w-md transform overflow-hidden rounded-2xl bg-white p-6 shadow-xl transition-all">
                  <div className="flex items-center justify-between mb-5">
                    <Dialog.Title as="h3" className="text-lg font-bold leading-6 text-slate-900">Add Employee</Dialog.Title>
                    <button onClick={() => { setIsModalOpen(false); resetForm(); }} className="text-slate-400 hover:text-slate-500"><span className="w-5 h-5 text-xl">&times;</span></button>
                  </div>
                  <form onSubmit={handleSubmit} className="flex flex-col gap-4">

                    {/* ── User Account Selector (real search) ── */}
                    <div>
                      <label className="block text-[11px] font-semibold text-slate-500 mb-1">Link System User Account <span className="text-red-500">*</span></label>
                      <div className="relative">
                        {selectedUser ? (
                          <div className="flex items-center justify-between px-3 py-2 border rounded-lg text-sm bg-green-50 border-green-300">
                            <span className="font-medium text-green-800">{selectedUser.firstName} {selectedUser.lastName} — {selectedUser.email}</span>
                            <button type="button" onClick={() => setSelectedUser(null)} className="text-green-600 hover:text-red-500 ml-2"><X size={14} /></button>
                          </div>
                        ) : (
                          <>
                            <input
                              value={userSearch}
                              onChange={e => { setUserSearch(e.target.value); setUserDropdownOpen(true); }}
                              onFocus={() => setUserDropdownOpen(true)}
                              placeholder="Search by name or email…"
                              className="w-full px-3 py-2 border rounded-lg text-sm"
                            />
                            {userDropdownOpen && (userResults.length > 0 || userSearchLoading) && (
                              <div className="absolute z-10 top-full mt-1 left-0 right-0 bg-white border rounded-lg shadow-lg max-h-48 overflow-y-auto">
                                {userSearchLoading && <div className="px-3 py-2 text-xs text-slate-400">Searching…</div>}
                                {userResults.map(u => (
                                  <button key={u.id} type="button"
                                    className="w-full text-left px-3 py-2 text-sm hover:bg-slate-100 flex flex-col"
                                    onClick={() => { setSelectedUser(u); setUserDropdownOpen(false); setUserSearch(''); }}>
                                    <span className="font-medium">{u.firstName} {u.lastName}</span>
                                    <span className="text-xs text-slate-400">{u.email}</span>
                                  </button>
                                ))}
                                {!userSearchLoading && userResults.length === 0 && debouncedUserSearch.length >= 1 && (
                                  <div className="px-3 py-2 text-xs text-slate-400">No matching users found.</div>
                                )}
                              </div>
                            )}
                          </>
                        )}
                      </div>
                    </div>

                    <div>
                      <label className="block text-[11px] font-semibold text-slate-500 mb-1">Designation</label>
                      <input required value={formData.designation} onChange={e => setFormData({...formData, designation: e.target.value})} className="w-full px-3 py-2 border rounded-lg text-sm" />
                    </div>
                    <div>
                      <label className="block text-[11px] font-semibold text-slate-500 mb-1">Department</label>
                      <input required value={formData.department} onChange={e => setFormData({...formData, department: e.target.value})} className="w-full px-3 py-2 border rounded-lg text-sm" />
                    </div>
                    <div className="pt-4 flex justify-end gap-3">
                      <button type="button" onClick={() => { setIsModalOpen(false); resetForm(); }} className="px-4 py-2 text-sm font-semibold text-slate-600 bg-slate-100 rounded-lg hover:bg-slate-200">Cancel</button>
                      <button type="submit" disabled={mutation.isPending || !selectedUser} className="px-4 py-2 text-sm font-semibold text-white bg-blue-600 rounded-lg hover:bg-blue-700 disabled:opacity-50">
                        {mutation.isPending ? 'Saving...' : 'Add Employee'}
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

export default Employees;
