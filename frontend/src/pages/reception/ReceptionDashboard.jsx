import React, { useState } from 'react';
import { useQuery } from '@tanstack/react-query';
import { axiosPrivate } from '../../api/axios';
import { Link } from 'react-router-dom';
import { motion } from 'framer-motion';
import {
  Users, Monitor, FileText, DollarSign, Shield, Ticket,
  UserCheck, UserPlus, ClipboardList, ArrowRight, RefreshCw,
  Loader2, TrendingUp, AlertCircle
} from 'lucide-react';
import Card from '../../components/ui/Card';
import Badge from '../../components/ui/Badge';
import { staggerChildren, fadeIn } from '../../components/ui/motion';
import useAuthStore from '../../store/authStore';

const BRANCH_ID = 1;

const QUICK_ACTIONS = [
  { label: 'Register Patient', icon: UserPlus, path: '/reception/register', color: 'from-indigo-500 to-indigo-600' },
  { label: 'Walk-In Check-In', icon: UserCheck, path: '/reception/walk-in', color: 'from-emerald-500 to-emerald-600' },
  { label: 'Queue Management', icon: Users, path: '/reception/queue', color: 'from-blue-500 to-blue-600' },
  { label: 'Token Generation', icon: Ticket, path: '/reception/tokens', color: 'from-orange-500 to-orange-600' },
  { label: 'Billing & Payments', icon: DollarSign, path: '/reception/billing', color: 'from-green-500 to-green-600' },
  { label: 'Insurance Verify', icon: Shield, path: '/reception/insurance', color: 'from-purple-500 to-purple-600' },
  { label: 'Document Scanning', icon: FileText, path: '/reception/documents', color: 'from-rose-500 to-rose-600' },
  { label: 'Kiosk Check-In', icon: Monitor, path: '/reception/kiosk', color: 'from-teal-500 to-teal-600' },
];

const StatCard = ({ label, value, icon: Icon, color, isLoading }) => (
  <motion.div
    variants={fadeIn}
    className="rounded-xl border border-[var(--color-border)] bg-[var(--color-surface)] p-4 flex items-center gap-4 shadow-sm hover:shadow-md transition-shadow"
  >
    <div className={`w-11 h-11 rounded-xl bg-gradient-to-br ${color} flex items-center justify-center flex-shrink-0`}>
      <Icon className="w-5 h-5 text-white" />
    </div>
    <div className="min-w-0">
      <p className="text-xs font-medium text-[var(--color-text-muted)] truncate">{label}</p>
      {isLoading ? (
        <Loader2 className="w-5 h-5 animate-spin text-[var(--color-navy-600)] mt-1" />
      ) : (
        <p className="text-2xl font-bold text-[var(--color-navy-900)] font-display">{value ?? '—'}</p>
      )}
    </div>
  </motion.div>
);

const ReceptionDashboard = () => {
  const user = useAuthStore(s => s.user);

  const { data: stats, isLoading: statsLoading, refetch, isFetching } = useQuery({
    queryKey: ['receptionDashboardStats', BRANCH_ID],
    queryFn: async () => {
      const res = await axiosPrivate.get(`/reception/kiosk/branch/${BRANCH_ID}/stats`);
      return res.data;
    },
    refetchInterval: 30000 // refresh every 30s
  });

  const { data: kioskToday = [], isLoading: kioskLoading } = useQuery({
    queryKey: ['kioskToday', BRANCH_ID],
    queryFn: async () => {
      const res = await axiosPrivate.get(`/reception/kiosk/branch/${BRANCH_ID}/today`);
      return res.data;
    },
    refetchInterval: 15000
  });

  const statusVariant = (status) => {
    if (status === 'CHECKED_IN') return 'success';
    if (status === 'NO_SHOW') return 'danger';
    if (status === 'VERIFIED') return 'info';
    return 'warning';
  };

  return (
    <motion.div
      initial="hidden"
      animate="visible"
      variants={staggerChildren}
      className="space-y-6"
    >
      {/* Header */}
      <motion.div variants={fadeIn} className="flex flex-col sm:flex-row justify-between items-start sm:items-center gap-4">
        <div>
          <h1 className="text-2xl sm:text-3xl font-bold font-display text-[var(--color-navy-900)]">
            Reception Desk
          </h1>
          <p className="text-sm text-[var(--color-text-muted)] mt-1">
            Good {new Date().getHours() < 12 ? 'morning' : 'afternoon'}, {user?.firstName || 'Staff'}! Here's your live overview.
          </p>
        </div>
        <button
          onClick={() => refetch()}
          className="flex items-center gap-1.5 text-sm font-semibold text-[var(--color-navy-600)] hover:text-[var(--color-navy-900)] transition-colors"
        >
          <RefreshCw className={`w-4 h-4 ${isFetching ? 'animate-spin' : ''}`} />
          Refresh
        </button>
      </motion.div>

      {/* Live Stats */}
      <motion.div variants={fadeIn} className="grid grid-cols-2 sm:grid-cols-3 lg:grid-cols-5 gap-3">
        <StatCard
          label="Queue Waiting"
          value={stats?.queueWaiting}
          icon={ClipboardList}
          color="from-blue-500 to-blue-600"
          isLoading={statsLoading}
        />
        <StatCard
          label="Walk-ins Today"
          value={stats?.walkInsToday}
          icon={UserCheck}
          color="from-emerald-500 to-emerald-600"
          isLoading={statsLoading}
        />
        <StatCard
          label="Kiosk Pending"
          value={stats?.kioskPending}
          icon={AlertCircle}
          color="from-orange-500 to-orange-600"
          isLoading={statsLoading}
        />
        <StatCard
          label="Verified Today"
          value={stats?.kioskVerified}
          icon={UserPlus}
          color="from-purple-500 to-purple-600"
          isLoading={statsLoading}
        />
        <StatCard
          label="Checked In"
          value={stats?.kioskCheckedIn}
          icon={TrendingUp}
          color="from-teal-500 to-teal-600"
          isLoading={statsLoading}
        />
      </motion.div>

      {/* Quick Actions */}
      <motion.div variants={fadeIn}>
        <h2 className="text-sm font-bold uppercase tracking-wider text-[var(--color-text-muted)] mb-3">Quick Actions</h2>
        <div className="grid grid-cols-2 sm:grid-cols-4 gap-3">
          {QUICK_ACTIONS.map(action => (
            <Link
              key={action.path}
              to={action.path}
              className="group flex flex-col items-center gap-2 p-4 rounded-xl border border-[var(--color-border)] bg-[var(--color-surface)] hover:border-[var(--color-primary)]/40 hover:shadow-md transition-all text-center"
            >
              <div className={`w-12 h-12 rounded-xl bg-gradient-to-br ${action.color} flex items-center justify-center shadow-sm group-hover:scale-105 transition-transform`}>
                <action.icon className="w-6 h-6 text-white" />
              </div>
              <span className="text-xs font-semibold text-[var(--color-navy-900)] leading-tight">{action.label}</span>
            </Link>
          ))}
        </div>
      </motion.div>

      {/* Today's Kiosk Check-ins */}
      <motion.div variants={fadeIn}>
        <Card>
          <Card.Header>
            <div className="flex items-center justify-between w-full">
              <h2 className="font-display font-bold text-lg text-[var(--color-navy-900)] flex items-center gap-2">
                <Monitor className="w-5 h-5" />
                Today's Kiosk Check-ins
              </h2>
              <Link
                to="/reception/queue"
                className="text-xs font-semibold text-[var(--color-primary)] hover:underline flex items-center gap-1"
              >
                Full Queue <ArrowRight className="w-3.5 h-3.5" />
              </Link>
            </div>
          </Card.Header>
          <Card.Body>
            {kioskLoading ? (
              <div className="flex justify-center py-6">
                <Loader2 className="w-6 h-6 animate-spin text-[var(--color-navy-600)]" />
              </div>
            ) : kioskToday.length === 0 ? (
              <div className="text-center py-6 text-sm text-[var(--color-text-muted)]">
                No kiosk check-ins yet today.
              </div>
            ) : (
              <div className="space-y-2">
                {kioskToday.slice(0, 8).map(k => (
                  <div
                    key={k.id}
                    className="flex items-center justify-between p-3 rounded-md border border-[var(--color-border)] bg-[var(--color-surface-alt)]/40"
                  >
                    <div className="flex items-center gap-3">
                      <div className="w-7 h-7 rounded-full bg-[var(--color-primary-bg)]/20 flex items-center justify-center">
                        <Monitor className="w-3.5 h-3.5 text-[var(--color-primary)]" />
                      </div>
                      <div>
                        <p className="text-sm font-semibold text-[var(--color-navy-900)]">
                          Check-In #{k.id}
                          {k.kioskStation && <span className="text-xs text-[var(--color-text-muted)] ml-1">— {k.kioskStation}</span>}
                        </p>
                        <p className="text-xs text-[var(--color-text-muted)]">
                          {k.createdAt ? new Date(k.createdAt).toLocaleTimeString() : ''}
                          {k.appointmentId ? ` · Appointment #${k.appointmentId}` : ' · Walk-In'}
                        </p>
                      </div>
                    </div>
                    <Badge variant={statusVariant(k.status)} size="sm">{k.status}</Badge>
                  </div>
                ))}
                {kioskToday.length > 8 && (
                  <p className="text-xs text-center text-[var(--color-text-muted)] pt-1">
                    +{kioskToday.length - 8} more check-ins today
                  </p>
                )}
              </div>
            )}
          </Card.Body>
        </Card>
      </motion.div>
    </motion.div>
  );
};

export default ReceptionDashboard;
