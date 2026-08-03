import React, { useState } from 'react';
import { useQuery } from '@tanstack/react-query';
import { axiosPrivate } from '../../api/axios';
import { ConfigDrivenDashboard } from '../../components/dashboard/ConfigDrivenDashboard';
import { dashboardConfig } from '../../config/dashboardConfig';

const RadiologistDashboard = () => {
  const [filterStatus, setFilterStatus] = useState('ALL');

  const { data: requests = [], isLoading } = useQuery({
    queryKey: ['radiology-requests', filterStatus],
    queryFn: async () => {
      const url = filterStatus === 'ALL' ? '/radiology/requests' : `/radiology/requests?status=${filterStatus}`;
      const res = await axiosPrivate.get(url);
      return res.data;
    },
  });

  const { data: procedures = [] } = useQuery({
    queryKey: ['radiology-procedures'],
    queryFn: async () => (await axiosPrivate.get('/radiology/procedures')).data,
  });

  const pendingCount = requests.filter(r => r.status === 'REQUESTED' || r.status === 'SCHEDULED').length;
  const completedCount = requests.filter(r => r.status === 'COMPLETED').length;

  const data = {
    requests,
    isLoading,
    proceduresCount: procedures.length,
    pendingCount,
    completedCount
  };

  return (
    <ConfigDrivenDashboard 
      config={dashboardConfig.ROLE_RADIOLOGIST}
      data={data}
      activeTab={filterStatus}
      onTabChange={setFilterStatus}
    />
  );
};

export default RadiologistDashboard;
