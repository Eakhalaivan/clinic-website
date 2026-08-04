import React from 'react';
import { useQuery } from '@tanstack/react-query';
import { axiosPrivate } from '../../api/axios';
import { Pill, Activity } from 'lucide-react';
import { DashboardShell, DashboardGrid } from '../../components/dashboard/shared/DashboardShell';
import Card from '../../components/ui/Card';

const PharmacistDashboard = () => {
  const { data: dispensed, isLoading } = useQuery({
    queryKey: ['dispensedPrescriptions'],
    queryFn: async () => {
      const res = await axiosPrivate.get('/pharmacy/dispensed');
      return res.data;
    }
  });

  return (
    <DashboardShell
      tabs={['Dashboard', 'My Dispensed', 'Inventory']}
      activeTab="Dashboard"
      quickActions={[
        { label: 'Dispense Prescription', icon: Pill, color: 'text-emerald-500', bg: 'bg-emerald-500/10', action: () => {} },
        { label: 'Check Stock', icon: Activity, color: 'text-orange-500', bg: 'bg-orange-500/10', action: () => {} }
      ]}
    >
      <div className="mb-6">
        <h1 className="text-2xl sm:text-3xl font-bold font-display text-[var(--color-navy-900)] m-0">
          Pharmacist Dashboard
        </h1>
        <p className="text-sm text-[var(--color-text-muted)] m-0 mt-1">
          Review and dispense patient prescriptions.
        </p>
      </div>

      <DashboardGrid
        center={
          <Card>
            <Card.Header>
              <h3 className="font-display font-bold text-lg text-[var(--color-navy-900)] m-0">My Dispensed Prescriptions</h3>
            </Card.Header>
            <Card.Body>
              {isLoading ? <p className="p-4 text-[var(--color-text-muted)]">Loading...</p> : (
                <div className="overflow-x-auto">
                  <table className="w-full text-left text-sm border-collapse">
                    <thead>
                      <tr className="border-b border-[var(--color-border)]">
                        <th className="py-3 px-4 font-semibold text-[var(--color-text-muted)]">Dispensed ID</th>
                        <th className="py-3 px-4 font-semibold text-[var(--color-text-muted)]">Prescription Ref</th>
                        <th className="py-3 px-4 font-semibold text-[var(--color-text-muted)]">Time</th>
                        <th className="py-3 px-4 font-semibold text-[var(--color-text-muted)]">Notes</th>
                      </tr>
                    </thead>
                    <tbody>
                      {(!dispensed || dispensed.length === 0) && (
                        <tr><td colSpan="4" className="py-4 text-center text-[var(--color-text-muted)]">No records found.</td></tr>
                      )}
                      {dispensed?.map(d => (
                        <tr key={d.id} className="border-b border-[var(--color-border)] last:border-0 hover:bg-[var(--color-surface-alt)]">
                          <td className="py-3 px-4 font-medium">#{d.id}</td>
                          <td className="py-3 px-4">{d.prescription.id}</td>
                          <td className="py-3 px-4 text-[var(--color-text-muted)]">{new Date(d.dispensedAt).toLocaleString()}</td>
                          <td className="py-3 px-4">{d.notes}</td>
                        </tr>
                      ))}
                    </tbody>
                  </table>
                </div>
              )}
            </Card.Body>
          </Card>
        }
      />
    </DashboardShell>
  );
};

export default PharmacistDashboard;
