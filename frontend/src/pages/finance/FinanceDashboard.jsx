import React, { useState } from 'react';
import { useQuery } from '@tanstack/react-query';
import { axiosPrivate } from '../../api/axios';
import { ConfigDrivenDashboard } from '../../components/dashboard/ConfigDrivenDashboard';
import { dashboardConfig } from '../../config/dashboardConfig';

const FinanceDashboard = () => {
  const [activeTab, setActiveTab] = useState('payments');

  const { data: payments = [] } = useQuery({ queryKey: ['finance-payments'], queryFn: async () => (await axiosPrivate.get('/finance/payments')).data });
  const { data: expenses = [] } = useQuery({ queryKey: ['finance-expenses'], queryFn: async () => (await axiosPrivate.get('/finance/expenses')).data });
  const { data: claims = [] } = useQuery({ queryKey: ['finance-claims'], queryFn: async () => (await axiosPrivate.get('/finance/claims')).data });

  const totalPayments = payments.reduce((acc, p) => acc + (Number(p.amount) || 0), 0);
  const totalExpenses = expenses.reduce((acc, e) => acc + (Number(e.amount) || 0), 0);
  const pendingClaims = claims.filter(c => c.status === 'SUBMITTED' || c.status === 'UNDER_REVIEW').length;

  const data = {
    payments,
    expenses,
    claims,
    totalPayments,
    totalExpenses,
    pendingClaims,
    activeTab
  };

  return (
    <ConfigDrivenDashboard 
      config={dashboardConfig.ROLE_FINANCE}
      data={data}
      activeTab={activeTab}
      onTabChange={setActiveTab}
    />
  );
};

export default FinanceDashboard;
