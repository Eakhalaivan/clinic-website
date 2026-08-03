import React, { useState } from 'react';
import { useQuery } from '@tanstack/react-query';
import { axiosPrivate } from '../../api/axios';
import { ConfigDrivenDashboard } from '../../components/dashboard/ConfigDrivenDashboard';
import { dashboardConfig } from '../../config/dashboardConfig';

const LabDashboard = () => {
  const [filter, setFilter] = useState('REQUESTED');

  const { data: requests, isLoading } = useQuery({
    queryKey: ['labRequests', filter],
    queryFn: async () => {
      const res = await axiosPrivate.get(`/lab/requests/status/${filter}`);
      return res.data;
    }
  });

  const data = {
    filter,
    requestsList: requests || [],
    isLoading
  };

  return (
    <ConfigDrivenDashboard 
      config={dashboardConfig.ROLE_LAB_TECH}
      data={data}
      activeTab={filter}
      onTabChange={setFilter}
    />
  );
};

export default LabDashboard;
