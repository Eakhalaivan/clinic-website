import React, { useState } from 'react';
import { useQuery } from '@tanstack/react-query';
import { useSystem } from '../../context/pharmacy/SystemContext';
import { useConfig } from '../../context/pharmacy/ConfigContext';
import api from '../../utils/pharmacy/api';
import { ConfigDrivenDashboard } from '../../components/dashboard/ConfigDrivenDashboard';
import { dashboardConfig } from '../../config/dashboardConfig';

const DashboardSkeleton = () => (
  <div className="space-y-8 animate-pulse p-8">
    <div className="h-8 w-64 bg-slate-200 rounded"></div>
    <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
      {[...Array(4)].map((_, i) => <div key={i} className="h-32 bg-slate-200 rounded-2xl"></div>)}
    </div>
    <div className="grid grid-cols-1 lg:grid-cols-10 gap-8">
      <div className="lg:col-span-6 h-[400px] bg-slate-200 rounded-2xl"></div>
      <div className="lg:col-span-4 h-[400px] bg-slate-200 rounded-2xl"></div>
    </div>
  </div>
);

export default function PharmacyDashboard() {
  const [activeTab, setActiveTab] = useState('Overview');
  const { systemData } = useSystem();
  const refreshIntervalSeconds = useConfig('dashboard_refresh_interval_seconds');
  const refreshInterval = (refreshIntervalSeconds ? Number(refreshIntervalSeconds) * 1000 : 60000);
  const currencySymbol = useConfig('currency_symbol') || '₹';

  const { data: stats, isLoading: statsLoading } = useQuery({
    queryKey: ['pharmacy-kpis'],
    queryFn: () => api.get('/dashboard').then(r => r.data?.data || {}),
  });

  const { data: trendRaw = [], isLoading: trendLoading } = useQuery({
    queryKey: ['pharmacy-revenue-trend'],
    queryFn: () => api.get('/dashboard/chart-data?days=7').then(r => r.data?.data || []),
  });

  const { data: revSummary, isLoading: revLoading } = useQuery({
    queryKey: ['dashboard-revenue-summary'],
    queryFn: () => api.get('/dashboard/revenue-summary').then(r => r.data),
    refetchInterval: refreshInterval,
    staleTime: 30000,
    retry: 2,
  });

  const { data: recentBillsData, isLoading: billsLoading } = useQuery({
    queryKey: ['dashboard-recent-bills'],
    queryFn: () => api.get('/pharmacy/dashboard/recent-activities').then(r => r.data?.data || []),
    refetchInterval: refreshInterval,
    staleTime: 30000,
    retry: 2,
  });

  const { data: lowStockData, isLoading: lowStockLoading } = useQuery({
    queryKey: ['dashboard-low-stock'],
    queryFn: () => api.get('/pharmacy/stocks/low-stock').then(r => r.data?.data || []),
    refetchInterval: refreshInterval,
    staleTime: 30000,
    retry: 2,
  });

  const chartSalesReturns = Array.isArray(trendRaw)
    ? trendRaw.map(d => ({
        day: d.day_of_week || d.day || d.sale_date,
        sales: Number(d.daily_revenue || 0),
        returns: 0,
      }))
    : [];

  const isLoading = statsLoading || trendLoading || revLoading;

  if (isLoading) return <DashboardSkeleton />;

  const data = {
    stats,
    currencySymbol,
    chartSalesReturns,
    revSummary,
    recentBills: recentBillsData || [],
    lowStockMedicines: lowStockData || []
  };

  return (
    <div>
      <div className="flex flex-col gap-1 mb-6 px-4 sm:px-6 lg:px-8 mt-6">
        <h2 className="text-2xl font-bold tracking-tight text-gray-900">
          {systemData?.greeting || 'Welcome'}, here's your Pharmacy Dashboard
        </h2>
        <p className="text-sm text-gray-500 font-medium">
          Daily operations and financial overview as of {new Date().toLocaleString('en-IN')}
        </p>
      </div>
      <ConfigDrivenDashboard 
        config={dashboardConfig.ROLE_PHARMACIST}
        data={data}
        activeTab={activeTab}
        onTabChange={setActiveTab}
      />
    </div>
  );
}
