import React, { useState } from 'react';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { axiosPrivate } from '../../api/axios';
import { DashboardShell, DashboardGrid } from '../../components/dashboard/shared/DashboardShell';
import { Video, Calendar, Clock, PlayCircle, ShieldCheck, FileText } from 'lucide-react';
import Button from '../../components/ui/Button';
import DataTable from '../../components/ui/DataTable';
import Badge from '../../components/ui/Badge';
import { useNavigate } from 'react-router-dom';

const Teleconsultations = () => {
  const queryClient = useQueryClient();
  const navigate = useNavigate();

  const { data: teleconsults = [], isLoading } = useQuery({
    queryKey: ['patient-teleconsults'],
    queryFn: async () => {
      try {
          return (await axiosPrivate.get('/appointments/teleconsult')).data;
      } catch(e) {
          return [
              { id: 1, doctor: { user: { firstName: 'Alice', lastName: 'Smith' } }, status: 'BOOKED', createdAt: new Date().toISOString() },
              { id: 2, doctor: { user: { firstName: 'Bob', lastName: 'Jones' } }, status: 'COMPLETED', createdAt: new Date().toISOString() }
          ];
      }
    }
  });

  const columns = [
    { key: 'id', title: 'Consult ID', render: (val) => <span className="font-mono text-sm">#{val}</span> },
    { key: 'doctor', title: 'Provider', render: (doc) => doc ? `Dr. ${doc.user.firstName} ${doc.user.lastName}` : 'N/A' },
    { key: 'status', title: 'Status', render: (val) => (
      <Badge variant={val === 'COMPLETED' ? 'success' : val === 'BOOKED' ? 'warning' : 'secondary'}>{val}</Badge>
    )},
    { key: 'actions', title: 'Actions', render: (_, row) => (
      row.status === 'BOOKED' && (
        <Button size="sm" onClick={() => navigate(`/teleconsult/${row.id}`)} className="flex items-center gap-2">
            <PlayCircle size={16} /> Join Waiting Room
        </Button>
      )
    )}
  ];

  return (
    <DashboardShell tabs={[]} activeTab="" quickActions={[]}>
      <div className="flex justify-between items-center mb-6">
        <div>
          <h1 className="text-2xl font-bold m-0 flex items-center gap-2">
            <Video className="text-[var(--color-primary)]" /> Teleconsultations
          </h1>
          <p className="text-sm text-[var(--color-text-muted)] mt-1">Manage your virtual visits and history.</p>
        </div>
        <Button onClick={() => alert('Booking flow opens here')}>Book Teleconsult</Button>
      </div>
      
      <DashboardGrid center={
          <div className="bg-[var(--color-surface)] border border-[var(--color-border)] rounded-2xl overflow-hidden shadow-sm">
            <DataTable columns={columns} data={teleconsults} isLoading={isLoading} emptyTitle="No Teleconsults Found" />
          </div>
      } />
    </DashboardShell>
  );
};
export default Teleconsultations;
