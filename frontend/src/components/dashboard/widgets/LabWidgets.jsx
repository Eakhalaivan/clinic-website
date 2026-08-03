import React, { useState } from 'react';
import { useMutation, useQueryClient } from '@tanstack/react-query';
import { axiosPrivate } from '../../../api/axios';
import { FlaskConical, CheckCircle2, Activity, ArrowRight } from 'lucide-react';
import toast from 'react-hot-toast';
import KPICard from '../../ui/KPICard';
import DataTable from '../../ui/DataTable';
import Badge from '../../ui/Badge';
import Button from '../../ui/Button';

export const LabHeaderWidget = () => (
  <div className="mb-6">
    <h1 className="text-2xl sm:text-3xl font-bold font-display text-[var(--color-navy-900)] m-0 flex items-center gap-2">
      <FlaskConical className="w-7 h-7 text-[var(--color-navy-800)]" />
      Laboratory Diagnostics
    </h1>
    <p className="text-sm text-[var(--color-text-muted)] m-0 mt-1">
      Sample collection tracking, test processing pipeline, and lab result verification.
    </p>
  </div>
);

export const LabKPIWidget = ({ isLoading, requestsList, filter }) => (
  <div className="grid grid-cols-1 sm:grid-cols-3 gap-4 mb-6">
    <KPICard icon={FlaskConical} label="Active Lab Requests" value={isLoading ? '...' : requestsList?.length || 0} colorToken="navy" />
    <KPICard icon={Activity} label="In Pipeline" value={filter} colorToken="warning" />
    <KPICard icon={CheckCircle2} label="Lab Status" value="Operational" colorToken="success" />
  </div>
);

export const LabRequestsWidget = ({ requestsList, isLoading, filter }) => {
  const queryClient = useQueryClient();

  const updateStatus = useMutation({
    mutationFn: async ({ id, newStatus }) => {
      const res = await axiosPrivate.put(`/lab/requests/${id}/status?status=${newStatus}`);
      return res.data;
    },
    onSuccess: () => {
      toast.success('Lab request status updated');
      queryClient.invalidateQueries(['labRequests']);
    },
    onError: () => toast.error('Failed to update status')
  });

  const nextStatusMap = {
    'REQUESTED': 'SAMPLE_COLLECTED',
    'SAMPLE_COLLECTED': 'PROCESSING',
    'PROCESSING': 'RESULT_ENTERED',
    'RESULT_ENTERED': 'VERIFIED',
    'VERIFIED': 'RELEASED'
  };

  const columns = [
    {
      key: 'testName', title: 'Test Name',
      render: (_, row) => (
        <div>
          <span className="font-semibold text-sm text-[var(--color-navy-900)] block">
            {row.testCatalog?.testName || 'Laboratory Test'}
          </span>
          <Badge variant="neutral" size="sm" className="mt-1">{row.testCatalog?.testCode || 'LAB-01'}</Badge>
        </div>
      )
    },
    { key: 'patient', title: 'Patient', render: (p) => p ? `${p.firstName} ${p.lastName}` : 'N/A' },
    { key: 'priority', title: 'Priority', render: (val) => <Badge variant={val === 'URGENT' || val === 'STAT' ? 'danger' : 'info'}>{val || 'ROUTINE'}</Badge> },
    { key: 'status', title: 'Current Status', render: (val) => <Badge variant="warning">{val}</Badge> },
    {
      key: 'actions', title: 'Action', align: 'right',
      render: (_, row) => {
        const nextState = nextStatusMap[filter];
        if (filter === 'PROCESSING') {
          return <Button variant="primary" size="sm" onClick={() => toast.success('Opening result entry window...')}>Enter Results</Button>;
        }
        if (nextState) {
          return (
            <Button variant="secondary" size="sm" icon={ArrowRight} isLoading={updateStatus.isPending} onClick={() => updateStatus.mutate({ id: row.id, newStatus: nextState })}>
              Mark {nextState.replace('_', ' ')}
            </Button>
          );
        }
        return <Badge variant="success">Completed</Badge>;
      }
    }
  ];

  return (
    <div className="bg-[var(--color-surface)] border border-[var(--color-border)] rounded-2xl overflow-hidden shadow-sm">
      <DataTable
        columns={columns}
        data={requestsList || []}
        isLoading={isLoading}
        searchPlaceholder="Search lab tests or patient..."
        emptyTitle="No lab requests in this stage"
        emptyDescription={`There are currently no laboratory requests with status '${filter}'.`}
      />
    </div>
  );
};
