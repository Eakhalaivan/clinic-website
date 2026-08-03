import { useState } from 'react';
import { useQuery } from '@tanstack/react-query';
import { axiosPrivate } from '../../api/axios';
import BranchManagement from './BranchManagement';
import UserManagement from './UserManagement';
import { BarChart3, CheckCircle2, DollarSign, CalendarCheck, CheckSquare, Download } from 'lucide-react';
import { DashboardShell, DashboardGrid } from '../../components/dashboard/shared/DashboardShell';
import KPICard from '../../components/ui/KPICard';
import Card from '../../components/ui/Card';
import DataTable from '../../components/ui/DataTable';
import Button from '../../components/ui/Button';
import EmptyState from '../../components/ui/EmptyState';

const AdminDashboard = () => {
  const [activeTab, setActiveTab] = useState('branches');

  const { data: branches, isLoading: branchesLoading } = useQuery({
    queryKey: ['branches'],
    queryFn: async () => {
      const res = await axiosPrivate.get('/branches');
      return res.data;
    },
    enabled: activeTab === 'branches'
  });

  const { data: metrics, isLoading: metricsLoading } = useQuery({
    queryKey: ['analytics-metrics'],
    queryFn: async () => {
      const res = await axiosPrivate.get('/analytics/daily-metrics');
      return res.data;
    },
    enabled: activeTab === 'analytics'
  });

  const totalRevenue = metrics?.reduce((sum, m) => sum + (m.totalRevenue || 0), 0) || 0;
  const totalAppointments = metrics?.reduce((sum, m) => sum + (m.totalAppointments || 0), 0) || 0;
  const totalCompleted = metrics?.reduce((sum, m) => sum + (m.completedAppointments || 0), 0) || 0;

  const tabs = [
    { id: 'branches', label: 'Manage Branches' },
    { id: 'analytics', label: 'Analytics & Reports' },
    { id: 'users', label: 'Manage Users' },
  ];

  const columns = [
    { key: 'date', title: 'Date' },
    { key: 'totalRevenue', title: 'Revenue', render: (val) => `$${val}` },
    { key: 'totalAppointments', title: 'Total Appts' },
    { key: 'completedAppointments', title: 'Completed' },
    { key: 'cancelledAppointments', title: 'Cancelled' },
  ];

  return (
    <DashboardShell
      tabs={tabs}
      activeTab={activeTab}
      onTabChange={setActiveTab}
      quickActions={[]}
    >
      <div className="mb-6">
        <h1 className="text-2xl sm:text-3xl font-bold font-display text-[var(--color-navy-900)] m-0">
          System Administration
        </h1>
        <p className="text-sm text-[var(--color-text-muted)] m-0 mt-1">
          Manage system configurations, branches, users, and view platform analytics.
        </p>
      </div>

      <div>
        {activeTab === 'branches' && <BranchManagement />}
        {activeTab === 'users' && <UserManagement />}

        {activeTab === 'analytics' && (
          <div className="space-y-6">
            <div className="flex items-center justify-end">
              <Button variant="secondary" icon={Download}>Export PDF</Button>
            </div>
            
            <div className="grid grid-cols-1 sm:grid-cols-3 gap-4">
              <KPICard
                icon={DollarSign}
                label="Total Revenue"
                value={`$${totalRevenue.toLocaleString()}`}
                colorToken="success"
              />
              <KPICard
                icon={CalendarCheck}
                label="Total Appointments"
                value={totalAppointments}
                colorToken="navy"
              />
              <KPICard
                icon={CheckSquare}
                label="Completed Appointments"
                value={totalCompleted}
                colorToken="info"
              />
            </div>

            <DashboardGrid
              center={
                <Card>
                  <Card.Header>
                    <h3 className="font-display font-bold text-lg text-[var(--color-navy-900)] m-0">
                      Recent Activity
                    </h3>
                  </Card.Header>
                  <Card.Body className="p-0">
                    <DataTable
                      columns={columns}
                      data={metrics || []}
                      isLoading={metricsLoading}
                      emptyTitle="No analytics data available"
                      emptyDescription="Platform metrics have not been generated yet."
                    />
                  </Card.Body>
                </Card>
              }
            />
          </div>
        )}
      </div>
    </DashboardShell>
  );
};

export default AdminDashboard;
