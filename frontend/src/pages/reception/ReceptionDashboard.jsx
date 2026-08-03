import React, { useState } from 'react';
import { useQuery } from '@tanstack/react-query';
import { axiosPrivate } from '../../api/axios';
import { ConfigDrivenDashboard } from '../../components/dashboard/ConfigDrivenDashboard';
import { dashboardConfig } from '../../config/dashboardConfig';

const ReceptionDashboard = () => {
  const branchId = 1;
  const [activeTab, setActiveTab] = useState('Dashboard');

  const { data: walkIns, isLoading } = useQuery({
    queryKey: ['walkIns', branchId],
    queryFn: async () => {
      const res = await axiosPrivate.get(`/reception/branches/${branchId}/walk-ins`);
      return res.data;
    }
  });

  const data = {
    walkInsData: walkIns || [],
    isLoading
  };

  return (
    <ConfigDrivenDashboard 
      config={dashboardConfig.ROLE_RECEPTION}
      data={data}
      activeTab={activeTab}
      onTabChange={setActiveTab}
    />
  );
};

export default ReceptionDashboard;
