import React from 'react';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { axiosPrivate } from '../../api/axios';
import useAuthStore from '../../store/authStore';
import { FileText, CheckCircle2, Clock, XCircle, CreditCard, Download, ChevronRight } from 'lucide-react';
import './PatientBilling.css';

const PatientBilling = () => {
  const { user } = useAuthStore();
  const queryClient = useQueryClient();

  const { data: invoices, isLoading } = useQuery({
    queryKey: ['patientInvoices', user?.id],
    queryFn: async () => {
      const res = await axiosPrivate.get(`/billing/patient/${user.id}`);
      return res.data;
    },
    enabled: !!user?.id
  });

  const payMutation = useMutation({
    mutationFn: async ({ invoiceId, amount }) => {
      const res = await axiosPrivate.post(`/api/v1/finance/payments/checkout/${invoiceId}`, { amount });
      return res.data;
    },
    onSuccess: (data) => {
      if (data && data.checkoutUrl) {
        // Redirect to the generated Stripe Checkout session URL
        window.location.href = data.checkoutUrl;
      }
    }
  });

  const handlePay = (invoice) => {
    payMutation.mutate({ 
      invoiceId: invoice.id, 
      amount: invoice.totalAmount || invoice.amount || 0 
    });
  };

  const handleDownloadPdf = async (id, invoiceNumber) => {
    const res = await axiosPrivate.get(`/billing/invoices/${id}/pdf`, { responseType: 'blob' });
    const url = URL.createObjectURL(new Blob([res.data], { type: 'application/pdf' }));
    const a = document.createElement('a');
    a.href = url;
    a.download = `invoice-${invoiceNumber || id}.pdf`;
    a.click();
    URL.revokeObjectURL(url);
  };

  const StatusBadge = ({ status }) => {
    switch (status?.toUpperCase()) {
      case 'PAID':    return <span className="badge badge-success"><CheckCircle2 size={12} style={{marginRight:'4px'}} aria-hidden="true"/> {status}</span>;
      case 'PENDING': return <span className="badge badge-warning"><Clock size={12} style={{marginRight:'4px'}} aria-hidden="true"/> {status}</span>;
      case 'OVERDUE': return <span className="badge badge-danger"><XCircle size={12} style={{marginRight:'4px'}} aria-hidden="true"/> {status}</span>;
      default:        return <span className="badge badge-neutral">{status}</span>;
    }
  };

  return (
    <div className="billing-page">
      <header className="page-header">
        <h2 className="page-title">Billing &amp; Invoices</h2>
      </header>

      {isLoading ? (
        <div className="card">Loading invoices...</div>
      ) : invoices && invoices.length > 0 ? (
        <div className="invoice-list">
          {invoices.map((invoice, idx) => (
            <div key={invoice.id} className="invoice-card card card-enter" style={{ animationDelay: `${idx * 80}ms` }}>
              <div className="invoice-header">
                <div className="invoice-icon"><FileText className="text-navy-600" size={24} /></div>
                <div className="invoice-meta">
                  <h3 style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
                    {invoice.invoiceNumber || `INV-${invoice.id}`}
                    <StatusBadge status={invoice.status} />
                  </h3>
                  <p style={{ margin: '2px 0', color: 'var(--color-text-muted)', fontSize: '0.875rem' }}>{invoice.description}</p>
                  <span className="invoice-date">Due: {invoice.dueDate ? new Date(invoice.dueDate).toLocaleDateString() : 'N/A'}</span>
                </div>
                <div className="invoice-amount-section">
                  <div className="invoice-amount">₹{(invoice.totalAmount || invoice.amount || 0).toFixed(2)}</div>
                  <div style={{ display: 'flex', gap: '8px', marginTop: '8px', flexWrap: 'wrap', justifyContent: 'flex-end' }}>
                    {invoice.status === 'PENDING' && (
                      <button className="btn-primary" onClick={() => handlePay(invoice)} disabled={payMutation.isPending}>
                        <CreditCard size={14} style={{ marginRight: '4px' }} />
                        {payMutation.isPending ? 'Processing...' : 'Pay Now'}
                      </button>
                    )}
                    {invoice.status === 'PAID' && (
                      <button
                        className="btn-secondary"
                        onClick={() => handleDownloadPdf(invoice.id, invoice.invoiceNumber)}
                        style={{ display: 'flex', alignItems: 'center', gap: '4px' }}
                      >
                        <Download size={14} /> Receipt
                      </button>
                    )}
                  </div>
                </div>
              </div>

              {/* Line-item breakdown */}
              {invoice.items && invoice.items.length > 0 && (
                <div style={{ marginTop: '16px', borderTop: '1px solid var(--color-surface-alt)', paddingTop: '12px' }}>
                  <p style={{ margin: '0 0 8px', fontSize: '0.8rem', fontWeight: 700, color: 'var(--color-text-muted)', textTransform: 'uppercase', letterSpacing: '0.05em' }}>
                    Line Items
                  </p>
                  <table style={{ width: '100%', borderCollapse: 'collapse', fontSize: '0.875rem' }}>
                    <thead>
                      <tr style={{ color: 'var(--color-text-muted)' }}>
                        <th style={{ textAlign: 'left', padding: '4px 0', fontWeight: 600 }}>Description</th>
                        <th style={{ textAlign: 'center', padding: '4px 0', fontWeight: 600 }}>Qty</th>
                        <th style={{ textAlign: 'right', padding: '4px 0', fontWeight: 600 }}>Unit</th>
                        <th style={{ textAlign: 'right', padding: '4px 0', fontWeight: 600 }}>Total</th>
                      </tr>
                    </thead>
                    <tbody>
                      {invoice.items.map(item => (
                        <tr key={item.id} style={{ borderTop: '1px solid var(--color-surface-alt)' }}>
                          <td style={{ padding: '6px 0', color: '#334155' }}>
                            <ChevronRight size={12} style={{ color: 'var(--color-text-muted)', marginRight: '4px' }} />
                            {item.description}
                          </td>
                          <td style={{ padding: '6px 0', textAlign: 'center', color: 'var(--color-text-muted)' }}>{item.quantity}</td>
                          <td style={{ padding: '6px 0', textAlign: 'right', color: 'var(--color-text-muted)' }}>₹{item.unitPrice?.toFixed(2)}</td>
                          <td style={{ padding: '6px 0', textAlign: 'right', fontWeight: 600, color: 'var(--color-text)' }}>₹{item.totalPrice?.toFixed(2)}</td>
                        </tr>
                      ))}
                    </tbody>
                    <tfoot>
                      {invoice.taxAmount > 0 && (
                        <tr>
                          <td colSpan="3" style={{ textAlign: 'right', padding: '6px 0', color: 'var(--color-text-muted)', fontSize: '0.8rem' }}>Tax:</td>
                          <td style={{ textAlign: 'right', padding: '6px 0', color: 'var(--color-text-muted)' }}>₹{invoice.taxAmount?.toFixed(2)}</td>
                        </tr>
                      )}
                      {invoice.discountAmount > 0 && (
                        <tr>
                          <td colSpan="3" style={{ textAlign: 'right', padding: '6px 0', color: 'var(--color-success)', fontSize: '0.8rem' }}>Discount:</td>
                          <td style={{ textAlign: 'right', padding: '6px 0', color: 'var(--color-success)' }}>-₹{invoice.discountAmount?.toFixed(2)}</td>
                        </tr>
                      )}
                      <tr style={{ borderTop: '2px solid var(--color-border)' }}>
                        <td colSpan="3" style={{ textAlign: 'right', padding: '8px 0', fontWeight: 700, color: 'var(--color-text)' }}>Total Due:</td>
                        <td style={{ textAlign: 'right', padding: '8px 0', fontWeight: 700, color: 'var(--color-text)', fontSize: '1rem' }}>
                          ₹{(invoice.totalAmount || invoice.amount || 0).toFixed(2)}
                        </td>
                      </tr>
                    </tfoot>
                  </table>
                </div>
              )}
            </div>
          ))}
        </div>
      ) : (
        <div className="card empty-state">
          <FileText size={48} className="text-navy-300" />
          <h3>No invoices found</h3>
          <p>You have no pending or paid invoices.</p>
        </div>
      )}
    </div>
  );
};

export default PatientBilling;
