import React, { useState } from 'react';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { axiosPrivate } from '../../api/axios';
import { ArrowLeftRight, Plus, Loader2, RefreshCw, ArrowRight, X, CheckCircle, Clock } from 'lucide-react';
import { toast } from 'react-hot-toast';

const STATUS_STYLES = {
  COMPLETED:  'bg-emerald-50 text-emerald-700 border border-emerald-200',
  IN_TRANSIT: 'bg-amber-50  text-amber-700  border border-amber-200',
  PENDING:    'bg-blue-50   text-blue-700   border border-blue-200',
};

const STATUS_ICON = {
  COMPLETED:  <CheckCircle className="w-3 h-3 inline mr-1" />,
  IN_TRANSIT: <Clock className="w-3 h-3 inline mr-1" />,
};

const EMPTY_FORM = { fromWarehouseId: '', toWarehouseId: '', stockItemId: '', quantity: '', notes: '', status: 'PENDING' };

const StockTransfers = () => {
  const queryClient = useQueryClient();
  const [showForm, setShowForm] = useState(false);
  const [form, setForm] = useState(EMPTY_FORM);

  const { data: transfers = [], isLoading, error, refetch } = useQuery({
    queryKey: ['stock-transfers'],
    queryFn: async () => (await axiosPrivate.get('/backoffice/inventory/transfers')).data,
  });

  const { data: warehouses = [] } = useQuery({
    queryKey: ['backoffice-warehouses'],
    queryFn: async () => (await axiosPrivate.get('/backoffice/inventory/warehouses')).data,
  });

  const { data: stockItems = [] } = useQuery({
    queryKey: ['backoffice-stock'],
    queryFn: async () => (await axiosPrivate.get('/backoffice/inventory/stock')).data,
  });

  const createMutation = useMutation({
    mutationFn: async (payload) => axiosPrivate.post('/backoffice/inventory/transfers', payload),
    onSuccess: () => {
      toast.success('Transfer request created');
      queryClient.invalidateQueries({ queryKey: ['stock-transfers'] });
      setShowForm(false);
      setForm(EMPTY_FORM);
    },
    onError: () => toast.error('Failed to create transfer'),
  });

  const handleSubmit = (e) => {
    e.preventDefault();
    if (!form.stockItemId || !form.quantity) {
      toast.error('Stock item and quantity are required');
      return;
    }
    const payload = {
      ...(form.fromWarehouseId ? { fromWarehouse: { id: Number(form.fromWarehouseId) } } : {}),
      ...(form.toWarehouseId ? { toWarehouse: { id: Number(form.toWarehouseId) } } : {}),
      stockItem: { id: Number(form.stockItemId) },
      quantity: Number(form.quantity),
      notes: form.notes || null,
    };
    createMutation.mutate(payload);
  };

  const formatDate = (val) => {
    if (!val) return '—';
    const d = new Date(val);
    return isNaN(d) ? val : d.toLocaleDateString('en-IN', { day: '2-digit', month: 'short', year: 'numeric' });
  };

  return (
    <div style={{ padding: '24px', maxWidth: '1100px', margin: '0 auto' }}>
      {/* Header */}
      <div className="flex justify-between items-center mb-6">
        <div>
          <h1 className="text-2xl font-bold text-slate-800 flex items-center gap-2">
            <ArrowLeftRight className="w-6 h-6 text-orange-600" /> Stock Transfers
          </h1>
          <p className="text-sm text-slate-500 mt-1">Inter-warehouse &amp; inter-branch stock movement</p>
        </div>
        <div className="flex gap-3">
          <button onClick={() => refetch()} className="px-3 py-2 text-sm font-semibold text-slate-600 border border-slate-200 rounded-lg hover:bg-slate-50 flex items-center gap-1.5">
            <RefreshCw className="w-4 h-4" /> Refresh
          </button>
          <button onClick={() => setShowForm(true)} className="flex items-center gap-2 px-4 py-2 bg-orange-600 text-white text-sm font-bold rounded-lg hover:bg-orange-700 transition-colors shadow-sm">
            <Plus className="w-4 h-4" /> New Transfer
          </button>
        </div>
      </div>

      {/* Inline create form */}
      {showForm && (
        <div className="mb-6 bg-white border border-slate-200 rounded-xl shadow-sm p-6">
          <div className="flex justify-between items-center mb-4">
            <h3 className="font-bold text-slate-800">New Transfer Request</h3>
            <button onClick={() => { setShowForm(false); setForm(EMPTY_FORM); }} className="text-slate-400 hover:text-slate-600">
              <X className="w-5 h-5" />
            </button>
          </div>
          <form onSubmit={handleSubmit} className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
            <div>
              <label className="text-xs font-semibold text-slate-500 uppercase tracking-wider block mb-1">From Warehouse</label>
              <select className="w-full text-sm border border-slate-200 rounded-lg px-3 py-2 focus:ring-2 focus:ring-orange-300 outline-none"
                value={form.fromWarehouseId} onChange={e => setForm(p => ({ ...p, fromWarehouseId: e.target.value }))}>
                <option value="">Select warehouse</option>
                {warehouses.map(w => <option key={w.id} value={w.id}>{w.name}</option>)}
              </select>
            </div>
            <div>
              <label className="text-xs font-semibold text-slate-500 uppercase tracking-wider block mb-1">To Warehouse</label>
              <select className="w-full text-sm border border-slate-200 rounded-lg px-3 py-2 focus:ring-2 focus:ring-orange-300 outline-none"
                value={form.toWarehouseId} onChange={e => setForm(p => ({ ...p, toWarehouseId: e.target.value }))}>
                <option value="">Select warehouse</option>
                {warehouses.map(w => <option key={w.id} value={w.id}>{w.name}</option>)}
              </select>
            </div>
            <div>
              <label className="text-xs font-semibold text-slate-500 uppercase tracking-wider block mb-1">Stock Item *</label>
              <select required className="w-full text-sm border border-slate-200 rounded-lg px-3 py-2 focus:ring-2 focus:ring-orange-300 outline-none"
                value={form.stockItemId} onChange={e => setForm(p => ({ ...p, stockItemId: e.target.value }))}>
                <option value="">Select item</option>
                {stockItems.map(s => <option key={s.id} value={s.id}>{s.itemName} ({s.quantity} available)</option>)}
              </select>
            </div>
            <div>
              <label className="text-xs font-semibold text-slate-500 uppercase tracking-wider block mb-1">Quantity *</label>
              <input required type="number" min="1"
                className="w-full text-sm border border-slate-200 rounded-lg px-3 py-2 focus:ring-2 focus:ring-orange-300 outline-none"
                value={form.quantity} onChange={e => setForm(p => ({ ...p, quantity: e.target.value }))} />
            </div>
            <div className="md:col-span-2">
              <label className="text-xs font-semibold text-slate-500 uppercase tracking-wider block mb-1">Notes</label>
              <input type="text"
                className="w-full text-sm border border-slate-200 rounded-lg px-3 py-2 focus:ring-2 focus:ring-orange-300 outline-none"
                value={form.notes} onChange={e => setForm(p => ({ ...p, notes: e.target.value }))}
                placeholder="Optional notes..." />
            </div>
            <div className="lg:col-span-3 flex justify-end gap-3 pt-2">
              <button type="button" onClick={() => { setShowForm(false); setForm(EMPTY_FORM); }}
                className="px-4 py-2 text-sm font-semibold text-slate-600 border border-slate-200 rounded-lg hover:bg-slate-50">
                Cancel
              </button>
              <button type="submit" disabled={createMutation.isPending}
                className="flex items-center gap-2 px-5 py-2 bg-orange-600 text-white text-sm font-bold rounded-lg hover:bg-orange-700 disabled:opacity-60">
                {createMutation.isPending && <Loader2 className="w-4 h-4 animate-spin" />}
                Create Transfer
              </button>
            </div>
          </form>
        </div>
      )}

      {/* Table */}
      <div className="bg-white rounded-xl border border-slate-200 shadow-sm overflow-hidden">
        {isLoading ? (
          <div className="flex justify-center items-center p-12">
            <Loader2 className="w-7 h-7 animate-spin text-orange-600" />
          </div>
        ) : error ? (
          <div className="p-8 text-center text-red-500 text-sm">Failed to load transfers</div>
        ) : (
          <div className="overflow-x-auto">
            <table className="min-w-full divide-y divide-slate-200">
              <thead className="bg-slate-50">
                <tr>
                  {['#', 'Item', 'From', '', 'To', 'Qty', 'Date', 'Notes', 'Status'].map(h => (
                    <th key={h} className="px-4 py-3 text-left text-xs font-semibold text-slate-500 uppercase tracking-wider whitespace-nowrap">{h}</th>
                  ))}
                </tr>
              </thead>
              <tbody className="bg-white divide-y divide-slate-100">
                {transfers.length === 0 ? (
                  <tr>
                    <td colSpan="9" className="px-4 py-10 text-center text-sm text-slate-500">
                      <ArrowLeftRight className="w-8 h-8 mx-auto text-slate-300 mb-3" />
                      No stock transfers found
                    </td>
                  </tr>
                ) : (
                  transfers.map(t => (
                    <tr key={t.id} className="hover:bg-slate-50 transition-colors">
                      <td className="px-4 py-3 text-xs font-mono text-slate-400">#{t.id}</td>
                      <td className="px-4 py-3 text-sm font-semibold text-slate-800">{t.stockItem?.itemName || '—'}</td>
                      <td className="px-4 py-3 text-sm text-slate-500 whitespace-nowrap">{t.fromWarehouse?.name || '—'}</td>
                      <td className="px-4 py-3 text-slate-300"><ArrowRight className="w-4 h-4" /></td>
                      <td className="px-4 py-3 text-sm text-slate-500 whitespace-nowrap">{t.toWarehouse?.name || '—'}</td>
                      <td className="px-4 py-3 text-sm font-bold text-orange-600">{t.quantity}</td>
                      <td className="px-4 py-3 text-xs text-slate-400 whitespace-nowrap">{formatDate(t.transferredAt)}</td>
                      <td className="px-4 py-3 text-xs text-slate-400 max-w-[180px] truncate">{t.notes || '—'}</td>
                      <td className="px-4 py-3">
                        <span className={`inline-flex items-center px-2.5 py-1 rounded-full text-xs font-semibold ${STATUS_STYLES[t.status] || 'bg-slate-100 text-slate-600'}`}>
                          {STATUS_ICON[t.status]}{t.status || 'PENDING'}
                        </span>
                      </td>
                    </tr>
                  ))
                )}
              </tbody>
            </table>
          </div>
        )}
      </div>
    </div>
  );
};

export default StockTransfers;
