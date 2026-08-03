import React, { useState, useEffect } from 'react';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { axiosPrivate } from '../../api/axios';
import {
  Shield, Settings, CreditCard, FileText,
  CheckCircle, XCircle, AlertTriangle, Save,
  Server, Database, Mail, Bell, ToggleLeft, ToggleRight
} from 'lucide-react';
import { DashboardShell, DashboardGrid } from '../../components/dashboard/shared/DashboardShell';
import KPICard from '../../components/ui/KPICard';
import DataTable from '../../components/ui/DataTable';
import Badge from '../../components/ui/Badge';
import Button from '../../components/ui/Button';

// ─────────────────────────────────────────────────────────────────────────────
// Helpers
// ─────────────────────────────────────────────────────────────────────────────
const ServiceStatusCard = ({ name, icon: Icon, colorToken }) => (
  <div className="bg-[var(--color-surface)] p-4 rounded-xl border border-[var(--color-border)] flex items-center gap-3">
    <div className={`p-2.5 rounded-lg`} style={{ backgroundColor: `var(--color-${colorToken}-bg)`, color: `var(--color-${colorToken})` }}>
      <Icon size={20} />
    </div>
    <div>
      <p className="m-0 text-sm text-[var(--color-text-muted)]">{name}</p>
      <div className="flex items-center gap-1.5 mt-0.5">
        <div className="w-2 h-2 rounded-full bg-[var(--color-success)]" />
        <span className="text-xs font-semibold text-[var(--color-success)]">Operational</span>
      </div>
    </div>
  </div>
);

// ─────────────────────────────────────────────────────────────────────────────
// Main Component
// ─────────────────────────────────────────────────────────────────────────────
const SuperAdminConsole = ({ defaultTab = 'health' }) => {
  const queryClient = useQueryClient();
  const [activeTab, setActiveTab] = useState(defaultTab);
  const [editingConfig, setEditingConfig] = useState({}); // { [id]: newValue }
  const [auditPage, setAuditPage] = useState(0);

  useEffect(() => {
    if (defaultTab) {
      setActiveTab(defaultTab);
    }
  }, [defaultTab]);

  // ── Data Fetching ────────────────────────────────────────────────────────
  const { data: stats = {}, isLoading: loadingStats } = useQuery({
    queryKey: ['super-admin-stats'],
    queryFn: async () => (await axiosPrivate.get('/super-admin/stats')).data,
    refetchInterval: 30000,
  });

  const { data: configs = [], isLoading: loadingConfigs } = useQuery({
    queryKey: ['super-admin-configs'],
    queryFn: async () => (await axiosPrivate.get('/super-admin/configs')).data,
    enabled: activeTab === 'config',
  });

  const { data: plans = [], isLoading: loadingPlans } = useQuery({
    queryKey: ['super-admin-plans'],
    queryFn: async () => (await axiosPrivate.get('/super-admin/subscription-plans')).data,
    enabled: activeTab === 'plans',
  });

  const { data: auditData, isLoading: loadingAudit } = useQuery({
    queryKey: ['super-admin-audit', auditPage],
    queryFn: async () => (await axiosPrivate.get(`/super-admin/audit-logs?page=${auditPage}&size=20`)).data,
    enabled: activeTab === 'audit',
  });
  const auditLogs = auditData?.content || [];
  const auditTotalPages = auditData?.totalPages || 1;

  // ── Mutations ────────────────────────────────────────────────────────────
  const saveConfig = useMutation({
    mutationFn: async ({ id, value }) =>
      axiosPrivate.put(`/super-admin/configs/${id}?value=${encodeURIComponent(value)}`),
    onSuccess: () => {
      queryClient.invalidateQueries(['super-admin-configs']);
      setEditingConfig({});
    },
  });

  const togglePlan = useMutation({
    mutationFn: async (id) => axiosPrivate.patch(`/super-admin/subscription-plans/${id}/toggle`),
    onSuccess: () => queryClient.invalidateQueries(['super-admin-plans']),
  });

  const tabs = [
    { id: 'health', label: 'System Health' },
    { id: 'config', label: 'Configuration' },
    { id: 'plans', label: 'Subscription Plans' },
    { id: 'audit', label: 'Audit Logs' },
    { id: 'security', label: 'Security & RBAC' },
    { id: 'notifications', label: 'Notifications' },
  ];

  const configColumns = [
    { key: 'configKey', title: 'Config Key', render: (val) => <span className="font-mono text-sm font-semibold text-blue-800">{val}</span> },
    {
      key: 'configVal',
      title: 'Current Value',
      render: (_, cfg) => {
        const isEditing = editingConfig[cfg.id] !== undefined;
        const currentVal = isEditing ? editingConfig[cfg.id] : cfg.configVal;
        const isBool = cfg.configVal === 'true' || cfg.configVal === 'false';
        
        if (isBool) {
          return (
            <div className="flex items-center gap-2">
              <button
                onClick={() => saveConfig.mutate({ id: cfg.id, value: cfg.configVal === 'true' ? 'false' : 'true' })}
                className="bg-transparent border-none cursor-pointer p-0"
              >
                {cfg.configVal === 'true' 
                  ? <ToggleRight size={28} className="text-[var(--color-success)]" /> 
                  : <ToggleLeft size={28} className="text-[var(--color-text-muted)]" />}
              </button>
              <span className={`text-sm font-semibold ${cfg.configVal === 'true' ? 'text-[var(--color-success)]' : 'text-[var(--color-text-muted)]'}`}>
                {cfg.configVal === 'true' ? 'Enabled' : 'Disabled'}
              </span>
            </div>
          );
        }
        return (
          <input
            value={currentVal}
            onChange={e => setEditingConfig(prev => ({ ...prev, [cfg.id]: e.target.value }))}
            className="input-field py-1 px-2 text-sm w-full"
          />
        );
      }
    },
    { key: 'description', title: 'Description', render: (val) => <span className="text-sm text-[var(--color-text-muted)]">{val}</span> },
    {
      key: 'actions',
      title: 'Action',
      render: (_, cfg) => {
        const isEditing = editingConfig[cfg.id] !== undefined;
        const isBool = cfg.configVal === 'true' || cfg.configVal === 'false';
        if (!isBool && isEditing) {
          return (
            <Button size="sm" variant="info" icon={Save} onClick={() => saveConfig.mutate({ id: cfg.id, value: editingConfig[cfg.id] })}>
              Save
            </Button>
          );
        }
        if (!isBool && !isEditing) {
          return <span className="text-xs text-[var(--color-text-muted)]">{cfg.updatedBy || 'system'}</span>;
        }
        return null;
      }
    }
  ];

  const auditColumns = [
    { key: 'createdAt', title: 'Timestamp', render: (val) => <span className="text-xs text-[var(--color-text-muted)] whitespace-nowrap">{new Date(val).toLocaleString()}</span> },
    { key: 'actor', title: 'Actor', render: (_, log) => <span className="text-xs font-semibold text-blue-800">{log.actorEmail || `User #${log.actorId}`}</span> },
    { key: 'action', title: 'Action', render: (val) => <Badge variant="warning">{val}</Badge> },
    { key: 'entity', title: 'Entity', render: (_, log) => <span className="text-xs text-[var(--color-text-muted)]">{log.entityType && `${log.entityType} #${log.entityId}`}</span> },
    { key: 'details', title: 'Details', render: (val) => <div className="text-xs text-[var(--color-text-muted)] max-w-xs truncate">{val || '—'}</div> }
  ];

  return (
    <DashboardShell
      tabs={tabs}
      activeTab={activeTab}
      onTabChange={setActiveTab}
      quickActions={[]}
    >
      <div className="flex items-center gap-3">
        <div className="p-2.5 bg-[#1e1b4b] rounded-xl">
          <Shield size={24} color="#a5b4fc" />
        </div>
        <div>
          <h1 className="text-2xl sm:text-3xl font-bold font-display text-[var(--color-navy-900)] m-0">
            Super Admin Console
          </h1>
          <p className="text-sm text-[var(--color-text-muted)] m-0 mt-1">
            Platform-wide control panel — restricted access
          </p>
        </div>
      </div>

      <DashboardGrid
        center={
          <div className="flex flex-col gap-6">
            {/* ── System Health Tab ──────────────────────────────────────────────── */}
            {activeTab === 'health' && (
              <>
                <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4">
                  <KPICard label="Active Subscription Plans" value={loadingStats ? '...' : (stats.activePlans ?? '—')} colorToken="info" />
                  <KPICard label="Total Plans Configured" value={loadingStats ? '...' : (stats.totalPlans ?? '—')} colorToken="success" />
                  <KPICard label="System Config Keys" value={loadingStats ? '...' : (stats.totalConfigs ?? '—')} colorToken="warning" />
                  <KPICard label="Total Audit Events" value={loadingStats ? '...' : (stats.totalAuditLogs ?? '—')} colorToken="primary" />
                </div>

                <div>
                  <h2 className="text-lg font-bold text-[var(--color-text)] mb-3 m-0">API Service Status</h2>
                  <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-3">
                    <ServiceStatusCard name="Database (PostgreSQL)" icon={Database} colorToken="success" />
                    <ServiceStatusCard name="Spring Boot API" icon={Server} colorToken="info" />
                    <ServiceStatusCard name="Email Service (SMTP)" icon={Mail} colorToken="primary" />
                    <ServiceStatusCard name="Notification Service" icon={Bell} colorToken="warning" />
                  </div>
                </div>
              </>
            )}

            {/* ── Configuration Tab ──────────────────────────────────────────────── */}
            {activeTab === 'config' && (
              <div className="bg-[var(--color-surface)] border border-[var(--color-border)] rounded-2xl overflow-hidden shadow-sm">
                <DataTable
                  columns={configColumns}
                  data={configs}
                  isLoading={loadingConfigs}
                  searchPlaceholder="Search configs..."
                  emptyTitle="No configurations found"
                />
              </div>
            )}

            {/* ── Subscription Plans Tab ─────────────────────────────────────────── */}
            {activeTab === 'plans' && (
              <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-5">
                {plans.map(plan => (
                  <div key={plan.id} className={`bg-[var(--color-surface)] rounded-2xl p-6 relative transition-opacity border-2 ${plan.isActive ? 'border-blue-100 opacity-100' : 'border-[var(--color-surface-alt)] opacity-65'}`}>
                    {!plan.isActive && (
                      <span className="absolute top-4 right-4 bg-[var(--color-surface-alt)] text-[var(--color-text-muted)] px-2 py-1 rounded text-[10px] font-bold">
                        INACTIVE
                      </span>
                    )}
                    <h3 className="m-0 mb-1 text-xl font-extrabold text-[var(--color-text)]">{plan.planName}</h3>
                    <div className="flex items-baseline gap-1 my-3">
                      <span className="text-3xl font-extrabold text-[var(--color-info)]">₹{Number(plan.priceMonthly).toLocaleString()}</span>
                      <span className="text-sm text-[var(--color-text-muted)]">/mo</span>
                    </div>
                    {plan.priceAnnually && (
                      <p className="m-0 mb-3 text-xs font-semibold text-[var(--color-success)]">
                        ₹{Number(plan.priceAnnually).toLocaleString()}/year (save {Math.round((1 - plan.priceAnnually / (plan.priceMonthly * 12)) * 100)}%)
                      </p>
                    )}
                    <div className="text-sm text-[var(--color-text-muted)] leading-relaxed mb-4">
                      <p className="m-0">👤 Up to <strong>{plan.maxUsers}</strong> users</p>
                      <p className="m-0">🏥 Up to <strong>{plan.maxBranches}</strong> branches</p>
                    </div>
                    {plan.features && (
                      <div className="flex flex-wrap gap-2 mb-4">
                        {JSON.parse(plan.features).map(f => (
                          <span key={f} className="bg-[var(--color-info-bg)] text-[var(--color-info)] px-2 py-1 rounded text-[10px] font-bold">
                            {f}
                          </span>
                        ))}
                      </div>
                    )}
                    <Button
                      variant={plan.isActive ? 'danger' : 'success'}
                      className="w-full justify-center"
                      onClick={() => togglePlan.mutate(plan.id)}
                    >
                      {plan.isActive ? 'Deactivate Plan' : 'Reactivate Plan'}
                    </Button>
                  </div>
                ))}
              </div>
            )}

            {/* ── Audit Logs Tab ────────────────────────────────────────────────── */}
            {activeTab === 'audit' && (
              <div className="bg-[var(--color-surface)] border border-[var(--color-border)] rounded-2xl overflow-hidden shadow-sm flex flex-col">
                <DataTable
                  columns={auditColumns}
                  data={auditLogs}
                  isLoading={loadingAudit}
                  searchPlaceholder="Search audit logs..."
                  emptyTitle="No audit events recorded yet"
                />
                <div className="p-3 border-t border-[var(--color-border)] flex gap-2 justify-end bg-[var(--color-surface)]">
                  <Button variant="secondary" size="sm" disabled={auditPage === 0} onClick={() => setAuditPage(p => p - 1)}>
                    &larr; Prev
                  </Button>
                  <Button variant="secondary" size="sm" disabled={auditPage >= auditTotalPages - 1} onClick={() => setAuditPage(p => p + 1)}>
                    Next &rarr;
                  </Button>
                </div>
              </div>
            )}

            {/* Dummy state for security & notifications tabs */}
            {(activeTab === 'security' || activeTab === 'notifications') && (
              <div className="p-8 text-center bg-[var(--color-surface)] rounded-2xl border border-[var(--color-border)]">
                <p className="text-[var(--color-text-muted)]">This module is under construction.</p>
              </div>
            )}
          </div>
        }
      />
    </DashboardShell>
  );
};

export default SuperAdminConsole;
