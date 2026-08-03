import React, { useState } from 'react';
import { IndianRupee, FileText, FileSpreadsheet, CreditCard, Eye, Printer, ArrowRight, RotateCcw } from 'lucide-react';
import {
  AreaChart, Area, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer, Legend
} from 'recharts';
import { useQuery } from '@tanstack/react-query';
import { useNavigate } from 'react-router-dom';
import KPICard from '../../components/pharmacy/ui/KPICard';
import DataTable from '../../components/pharmacy/ui/DataTable';
import AppModal from '../../components/pharmacy/ui/AppModal';
import Badge from '../../components/pharmacy/ui/Badge';
import PharmacyInvoice from '../../components/pharmacy/pharmacy/PharmacyInvoice';
import { DashboardShell, DashboardGrid } from '../../components/dashboard/shared/DashboardShell';
import Card from '../../components/ui/Card';
import Button from '../../components/ui/Button';
import { useSystem } from '../../context/pharmacy/SystemContext';
import { useConfig } from '../../context/pharmacy/ConfigContext';
import api from '../../utils/pharmacy/api';

const DashboardSkeleton = () => (
  <div className="space-y-8 animate-pulse">
    <div className="h-8 w-64 bg-slate-200 rounded"></div>
    <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
      {[...Array(3)].map((_, i) => <div key={i} className="h-32 bg-slate-200 rounded-2xl"></div>)}
    </div>
    <div className="grid grid-cols-1 lg:grid-cols-2 gap-8">
      <div className="h-[400px] bg-slate-200 rounded-2xl"></div>
      <div className="h-[400px] bg-slate-200 rounded-2xl"></div>
    </div>
  </div>
);

export default function BillingDashboard() {
  const [isInvoiceModalOpen, setIsInvoiceModalOpen] = useState(false);
  const [selectedInvoice, setSelectedInvoice] = useState(null);
  const navigate = useNavigate();

  const { systemData } = useSystem();
  const refreshIntervalSeconds = useConfig('dashboard_refresh_interval_seconds');
  const refreshInterval = (refreshIntervalSeconds ? Number(refreshIntervalSeconds) * 1000 : 60000);
  const currencySymbol = useConfig('currency_symbol') || '₹';

  const { data: kpis, isLoading: kpisLoading } = useQuery({
    queryKey: ['billing-kpis'],
    queryFn: () => api.get('/dashboard').then(r => r.data?.data || {}),
  });

  const { data: revSummary, isLoading: revenueLoading } = useQuery({
    queryKey: ['billing-revenue-summary'],
    queryFn: () => api.get('/dashboard/revenue-strip').then(r => r.data?.data || {}),
  });

  const { data: revenueChart = [] } = useQuery({
    queryKey: ['billing-revenue-trend'],
    queryFn: () => api.get('/dashboard/chart-data?days=7').then(r => r.data?.data || []),
    refetchInterval: refreshInterval,
  });

  const { data: recentBillsData, isLoading: billsLoading } = useQuery({
    queryKey: ['dashboard-recent-bills'],
    queryFn: () => api.get('/pharmacy/dashboard/recent-activities').then(r => r.data?.data || []),
    refetchInterval: refreshInterval,
  });

  const chartSales = Array.isArray(revenueChart)
    ? revenueChart.map(d => ({
        day: d.day_of_week || d.day || d.sale_date,
        sales: Number(d.daily_revenue || 0),
      }))
    : [];

  const isLoading = kpisLoading || revenueLoading || billsLoading;

  if (isLoading) return <DashboardSkeleton />;

  const recentBills = recentBillsData || [];
  const fmt = (val) => val != null ? Number(val).toLocaleString('en-IN') : '0';

  const quickActions = [
    { label: 'New Sale', icon: FileText, action: () => navigate('/sales'), color: 'text-primary', bg: 'bg-primary/10' },
    { label: 'Credit Bills', icon: CreditCard, action: () => navigate('/credit-bills'), color: 'text-amber-500', bg: 'bg-amber-500/10' },
    { label: 'Process Return', icon: RotateCcw, action: () => navigate('/returns'), color: 'text-rose-500', bg: 'bg-rose-500/10' },
  ];

  return (
    <DashboardShell quickActions={quickActions}>
      <div className="flex flex-col gap-1 mb-6">
        <h2 className="text-2xl sm:text-3xl font-bold font-display text-[var(--color-navy-900)] m-0">
          {systemData?.greeting || 'Welcome'}, here's your Billing Dashboard
        </h2>
        <p className="text-sm text-[var(--color-text-muted)] font-medium m-0 mt-1">
          Financial overview and billing operations as of {new Date().toLocaleString('en-IN')}
        </p>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
        <KPICard
          title="Today's Revenue"
          value={`${currencySymbol} ${fmt(kpis?.todays_sales_revenue)}`}
          icon={IndianRupee}
          colorToken="success"
        />
        <KPICard
          title="Total Bills Today"
          value={`${fmt(kpis?.bills_today)}`}
          icon={FileSpreadsheet}
          colorToken="primary"
        />
        <KPICard
          title="This Week's Revenue"
          value={`${currencySymbol} ${fmt(revSummary?.this_weeks_total)}`}
          icon={IndianRupee}
          colorToken="warning"
        />
      </div>

      <DashboardGrid
        left={
          <Card className="h-full">
            <Card.Header>
              <h3 className="font-bold text-lg text-[var(--color-navy-900)] m-0">Sales Trend (Last 7 Days)</h3>
            </Card.Header>
            <Card.Body className="p-4">
              <div className="w-full min-h-[300px]">
                <ResponsiveContainer width="100%" height={300}>
                  <AreaChart data={chartSales}>
                    <defs>
                      <linearGradient id="colorSales" x1="0" y1="0" x2="0" y2="1">
                        <stop offset="5%" stopColor="#5244F2" stopOpacity={0.1}/>
                        <stop offset="95%" stopColor="#5244F2" stopOpacity={0}/>
                      </linearGradient>
                    </defs>
                    <CartesianGrid strokeDasharray="3 3" vertical={false} stroke="#f1f5f9" />
                    <XAxis dataKey="day" axisLine={false} tickLine={false} tick={{fill: '#64748b', fontSize: 12}} dy={10} />
                    <YAxis axisLine={false} tickLine={false} tick={{fill: '#64748b', fontSize: 12}} tickFormatter={(val) => `₹${(val/1000).toFixed(0)}k`} />
                    <Tooltip contentStyle={{ borderRadius: '12px', border: 'none', boxShadow: '0 10px 15px -3px rgb(0 0 0 / 0.1)' }} />
                    <Area type="monotone" dataKey="sales" stroke="#5244F2" strokeWidth={3} fillOpacity={1} fill="url(#colorSales)" name="Revenue" />
                  </AreaChart>
                </ResponsiveContainer>
              </div>
            </Card.Body>
          </Card>
        }
        center={
          <Card className="h-full">
            <Card.Header className="flex items-center justify-between">
              <h3 className="font-bold text-lg text-[var(--color-navy-900)] m-0">Recent Transactions</h3>
              <button onClick={() => navigate('/sales')} className="text-[var(--color-primary)] hover:text-[var(--color-primary-dark)] text-sm font-medium flex items-center gap-1">
                View All <ArrowRight className="w-4 h-4" />
              </button>
            </Card.Header>
            <Card.Body className="p-0">
              <DataTable
                data={recentBills.slice(0, 6)}
                columns={[
                  { header: 'Bill No', accessor: 'billNumber' },
                  { header: 'Patient', accessor: 'patientName' },
                  { header: 'Amount', render: (row) => `₹ ${fmt(row.netAmount)}` },
                  { header: 'Status', render: (row) => (
                    <Badge variant={row.status === 'PAID' ? 'success' : 'warning'}>{row.status}</Badge>
                  )},
                  { header: 'Action', render: (row) => (
                    <div className="flex items-center gap-2">
                      <button onClick={() => { setSelectedInvoice(row); setIsInvoiceModalOpen(true); }}
                        className="p-1.5 bg-blue-50 text-blue-600 rounded-lg hover:bg-blue-100 transition-colors">
                        <Eye className="w-4 h-4" />
                      </button>
                      <button onClick={() => { setSelectedInvoice(row); setIsInvoiceModalOpen(true); }}
                        className="p-1.5 bg-gray-50 text-gray-600 rounded-lg hover:bg-gray-100 transition-colors">
                        <Printer className="w-4 h-4" />
                      </button>
                    </div>
                  )}
                ]}
              />
            </Card.Body>
          </Card>
        }
      />

      <AppModal isOpen={isInvoiceModalOpen} onClose={() => setIsInvoiceModalOpen(false)} maxWidth="sm:max-w-4xl" padding={false}>
        <PharmacyInvoice bill={selectedInvoice} onClose={() => setIsInvoiceModalOpen(false)} />
      </AppModal>
    </DashboardShell>
  );
}
