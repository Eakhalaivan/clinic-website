import React, { useState } from 'react';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { axiosPrivate } from '../../api/axios';
import {
  DollarSign, CreditCard, Plus, Receipt, ArrowLeft, CheckCircle2,
  Loader2, FileText
} from 'lucide-react';
import { Link } from 'react-router-dom';
import { motion } from 'framer-motion';
import toast from 'react-hot-toast';
import Card from '../../components/ui/Card';
import Button from '../../components/ui/Button';
import Input from '../../components/ui/Input';
import Badge from '../../components/ui/Badge';
import EmptyState from '../../components/ui/EmptyState';
import { staggerChildren, fadeIn } from '../../components/ui/motion';

const PAYMENT_METHODS = ['CASH', 'CARD', 'INSURANCE', 'ONLINE'];

const ReceptionBilling = () => {
  const queryClient = useQueryClient();
  const [patientId, setPatientId] = useState('');
  const [searchedPatientId, setSearchedPatientId] = useState(null);
  const [selectedBill, setSelectedBill] = useState(null);
  const [paymentForm, setPaymentForm] = useState({
    amount: '',
    paymentMethod: 'CASH',
    referenceNumber: ''
  });
  const [billItems, setBillItems] = useState([{ description: '', amount: '', department: 'GENERAL' }]);
  const [showCreateBill, setShowCreateBill] = useState(false);

  const { data: bills = [], isLoading: billsLoading } = useQuery({
    queryKey: ['patientBills', searchedPatientId],
    queryFn: async () => {
      const res = await axiosPrivate.get(`/reception/billing/patient/${searchedPatientId}/bills`);
      return res.data;
    },
    enabled: !!searchedPatientId
  });

  const createBill = useMutation({
    mutationFn: async () => {
      const items = billItems
        .filter(i => i.description && i.amount)
        .map(i => ({ ...i, amount: parseFloat(i.amount) }));
      const res = await axiosPrivate.post('/reception/billing/bills', {
        patientId: searchedPatientId,
        items
      });
      return res.data;
    },
    onSuccess: () => {
      toast.success('Bill created successfully');
      queryClient.invalidateQueries(['patientBills', searchedPatientId]);
      setShowCreateBill(false);
      setBillItems([{ description: '', amount: '', department: 'GENERAL' }]);
    },
    onError: () => toast.error('Failed to create bill')
  });

  const recordPayment = useMutation({
    mutationFn: async (billId) => {
      const res = await axiosPrivate.post(`/reception/billing/bills/${billId}/payments`, {
        amount: parseFloat(paymentForm.amount),
        paymentMethod: paymentForm.paymentMethod,
        referenceNumber: paymentForm.referenceNumber || null
      });
      return res.data;
    },
    onSuccess: () => {
      toast.success('Payment recorded successfully');
      queryClient.invalidateQueries(['patientBills', searchedPatientId]);
      setSelectedBill(null);
      setPaymentForm({ amount: '', paymentMethod: 'CASH', referenceNumber: '' });
    },
    onError: () => toast.error('Failed to record payment')
  });

  const addBillItem = () => setBillItems(prev => [...prev, { description: '', amount: '', department: 'GENERAL' }]);
  const removeBillItem = (idx) => setBillItems(prev => prev.filter((_, i) => i !== idx));
  const updateBillItem = (idx, field, value) => {
    setBillItems(prev => prev.map((item, i) => i === idx ? { ...item, [field]: value } : item));
  };

  const statusVariant = (status) => {
    if (status === 'PAID') return 'success';
    if (status === 'CANCELLED') return 'danger';
    return 'warning';
  };

  return (
    <motion.div
      initial="hidden"
      animate="visible"
      variants={staggerChildren}
      className="max-w-5xl mx-auto space-y-6"
    >
      {/* Header */}
      <div className="flex flex-col sm:flex-row justify-between items-start sm:items-center gap-4">
        <div>
          <Link to="/reception" className="inline-flex items-center text-xs font-semibold text-[var(--color-navy-600)] hover:underline mb-2 gap-1">
            <ArrowLeft className="w-3.5 h-3.5" /> Back to Reception Desk
          </Link>
          <h1 className="text-2xl sm:text-3xl font-bold font-display text-[var(--color-navy-900)] flex items-center gap-2">
            <DollarSign className="w-7 h-7 text-[var(--color-navy-800)]" />
            Billing & Payments
          </h1>
          <p className="text-sm text-[var(--color-text-muted)] mt-1">
            Create bills, collect payments, and issue receipts for patients.
          </p>
        </div>
      </div>

      {/* Patient Search */}
      <Card>
        <Card.Header>
          <h2 className="font-display font-bold text-base text-[var(--color-navy-900)]">Search Patient</h2>
        </Card.Header>
        <Card.Body>
          <div className="flex gap-3">
            <Input
              placeholder="Enter Patient ID..."
              value={patientId}
              onChange={e => setPatientId(e.target.value)}
              className="flex-1"
            />
            <Button
              variant="primary"
              onClick={() => setSearchedPatientId(patientId || null)}
              disabled={!patientId}
            >
              Search
            </Button>
          </div>
        </Card.Body>
      </Card>

      {searchedPatientId && (
        <>
          {/* Bills List */}
          <Card>
            <Card.Header>
              <div className="flex items-center justify-between w-full">
                <h2 className="font-display font-bold text-lg text-[var(--color-navy-900)] flex items-center gap-2">
                  <FileText className="w-5 h-5" />
                  Bills for Patient #{searchedPatientId}
                </h2>
                <Button variant="primary" size="sm" icon={Plus} onClick={() => setShowCreateBill(true)}>
                  New Bill
                </Button>
              </div>
            </Card.Header>
            <Card.Body>
              {billsLoading ? (
                <div className="flex justify-center py-6">
                  <Loader2 className="w-6 h-6 animate-spin text-[var(--color-navy-600)]" />
                </div>
              ) : bills.length === 0 ? (
                <EmptyState
                  icon={Receipt}
                  title="No Bills Found"
                  description="No bills exist for this patient. Create one to get started."
                />
              ) : (
                <div className="space-y-3">
                  {bills.map(bill => (
                    <motion.div
                      key={bill.id}
                      variants={fadeIn}
                      className="flex items-center justify-between p-4 rounded-md border border-[var(--color-border)] bg-[var(--color-surface-alt)]/40 hover:bg-[var(--color-surface-alt)] transition-colors"
                    >
                      <div>
                        <div className="flex items-center gap-2">
                          <span className="font-semibold text-sm text-[var(--color-navy-900)]">
                            Bill #{bill.id}
                          </span>
                          <Badge variant={statusVariant(bill.status)} size="sm">{bill.status}</Badge>
                        </div>
                        <p className="text-xs text-[var(--color-text-muted)] mt-0.5">
                          Net: ₹{bill.netAmount?.toFixed(2)} | Items: {bill.items?.length || 0}
                        </p>
                      </div>
                      <div className="flex items-center gap-2">
                        {bill.status === 'PENDING' && (
                          <Button
                            variant="primary"
                            size="sm"
                            icon={CreditCard}
                            onClick={() => {
                              setSelectedBill(bill);
                              setPaymentForm(f => ({ ...f, amount: bill.netAmount?.toString() || '' }));
                            }}
                          >
                            Pay
                          </Button>
                        )}
                        {bill.status === 'PAID' && (
                          <Button variant="outline" size="sm" icon={Receipt} onClick={() => window.print()}>
                            Receipt
                          </Button>
                        )}
                      </div>
                    </motion.div>
                  ))}
                </div>
              )}
            </Card.Body>
          </Card>

          {/* Create Bill Inline Form */}
          {showCreateBill && (
            <motion.div variants={fadeIn}>
              <Card>
                <Card.Header>
                  <h2 className="font-display font-bold text-lg text-[var(--color-navy-900)]">Create New Bill</h2>
                </Card.Header>
                <Card.Body className="space-y-4">
                  {billItems.map((item, idx) => (
                    <div key={idx} className="grid grid-cols-12 gap-3 items-end">
                      <div className="col-span-5">
                        <Input
                          label={idx === 0 ? 'Description' : ''}
                          placeholder="e.g. Consultation fee"
                          value={item.description}
                          onChange={e => updateBillItem(idx, 'description', e.target.value)}
                        />
                      </div>
                      <div className="col-span-3">
                        <Input
                          label={idx === 0 ? 'Amount (₹)' : ''}
                          type="number"
                          placeholder="0.00"
                          value={item.amount}
                          onChange={e => updateBillItem(idx, 'amount', e.target.value)}
                        />
                      </div>
                      <div className="col-span-3">
                        {idx === 0 && (
                          <label className="block text-sm font-semibold text-[var(--color-navy-900)] mb-1">
                            Department
                          </label>
                        )}
                        <select
                          className="w-full h-10 px-3 py-2 bg-transparent border border-[var(--color-border)] rounded-md focus:outline-none focus:ring-2 focus:ring-[var(--color-primary)] text-sm"
                          value={item.department}
                          onChange={e => updateBillItem(idx, 'department', e.target.value)}
                        >
                          {['GENERAL', 'LAB', 'RADIOLOGY', 'PHARMACY', 'NURSING', 'CONSULTATION'].map(d => (
                            <option key={d} value={d}>{d}</option>
                          ))}
                        </select>
                      </div>
                      <div className="col-span-1 flex items-end pb-0.5">
                        {billItems.length > 1 && (
                          <button
                            type="button"
                            onClick={() => removeBillItem(idx)}
                            className="w-9 h-9 flex items-center justify-center rounded-md text-red-500 hover:bg-red-50 border border-red-200 text-xs font-bold"
                          >
                            ✕
                          </button>
                        )}
                      </div>
                    </div>
                  ))}
                  <div className="flex items-center justify-between pt-2 border-t border-[var(--color-border)]">
                    <Button variant="outline" size="sm" icon={Plus} onClick={addBillItem}>Add Item</Button>
                    <div className="flex gap-2">
                      <Button variant="ghost" size="sm" onClick={() => setShowCreateBill(false)}>Cancel</Button>
                      <Button
                        variant="primary"
                        size="sm"
                        icon={CheckCircle2}
                        isLoading={createBill.isPending}
                        onClick={() => createBill.mutate()}
                        disabled={billItems.every(i => !i.description || !i.amount)}
                      >
                        Create Bill
                      </Button>
                    </div>
                  </div>
                </Card.Body>
              </Card>
            </motion.div>
          )}

          {/* Payment Modal/Card */}
          {selectedBill && (
            <motion.div variants={fadeIn}>
              <Card>
                <Card.Header>
                  <h2 className="font-display font-bold text-lg text-[var(--color-navy-900)] flex items-center gap-2">
                    <CreditCard className="w-5 h-5" />
                    Collect Payment — Bill #{selectedBill.id}
                  </h2>
                </Card.Header>
                <Card.Body className="space-y-4">
                  <div className="p-3 rounded-md bg-[var(--color-surface-alt)] border border-[var(--color-border)]">
                    <p className="text-sm font-semibold text-[var(--color-navy-900)]">
                      Net Amount Due: <span className="text-[var(--color-primary)] text-lg">₹{selectedBill.netAmount?.toFixed(2)}</span>
                    </p>
                  </div>
                  <div className="grid grid-cols-1 sm:grid-cols-3 gap-4">
                    <Input
                      label="Amount (₹)"
                      type="number"
                      value={paymentForm.amount}
                      onChange={e => setPaymentForm(f => ({ ...f, amount: e.target.value }))}
                    />
                    <div className="space-y-1">
                      <label className="text-sm font-semibold text-[var(--color-navy-900)]">Payment Method</label>
                      <select
                        className="w-full h-10 px-3 py-2 bg-transparent border border-[var(--color-border)] rounded-md focus:outline-none focus:ring-2 focus:ring-[var(--color-primary)] text-sm"
                        value={paymentForm.paymentMethod}
                        onChange={e => setPaymentForm(f => ({ ...f, paymentMethod: e.target.value }))}
                      >
                        {PAYMENT_METHODS.map(m => <option key={m} value={m}>{m}</option>)}
                      </select>
                    </div>
                    <Input
                      label="Reference No. (optional)"
                      placeholder="TXN / Cheque No."
                      value={paymentForm.referenceNumber}
                      onChange={e => setPaymentForm(f => ({ ...f, referenceNumber: e.target.value }))}
                    />
                  </div>
                  <div className="flex gap-3 pt-2">
                    <Button variant="ghost" onClick={() => setSelectedBill(null)}>Cancel</Button>
                    <Button
                      variant="success"
                      icon={CheckCircle2}
                      isLoading={recordPayment.isPending}
                      onClick={() => recordPayment.mutate(selectedBill.id)}
                      disabled={!paymentForm.amount}
                    >
                      Confirm Payment
                    </Button>
                  </div>
                </Card.Body>
              </Card>
            </motion.div>
          )}
        </>
      )}
    </motion.div>
  );
};

export default ReceptionBilling;
