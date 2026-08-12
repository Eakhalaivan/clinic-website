import React, { useState } from 'react';
import { useQuery, useQueryClient } from '@tanstack/react-query';
import { axiosPrivate } from '../../api/axios';
import { ConfigDrivenDashboard } from '../../components/dashboard/ConfigDrivenDashboard';
import { dashboardConfig } from '../../config/dashboardConfig';
import useAuthStore from '../../store/authStore';

const NurseDashboard = () => {
  const { user, token } = useAuthStore();
  const [activeTab, setActiveTab] = useState('Dashboard');
  const [selectedPatientId, setSelectedPatientId] = useState(null);
  const queryClient = useQueryClient();

  React.useEffect(() => {
      if (!token) return;
      const baseUrl = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api';
      const evtSource = new EventSource(`${baseUrl.replace('/api', '')}/api/sse/appointments?token=${token}`);
      
      const invalidate = () => {
          queryClient.invalidateQueries(['nurseAssignments']);
          queryClient.invalidateQueries(['nurse-assigned-patients']);
          queryClient.invalidateQueries(['nursingRecentActivity']);
      };
      
      evtSource.addEventListener('appointment-status-changed', invalidate);
      evtSource.addEventListener('queue-token-called', invalidate);
      
      return () => evtSource.close();
  }, [queryClient]);

  const { data: assignments, isLoading: isAssignmentsLoading } = useQuery({
    queryKey: ['nurseAssignments'],
    queryFn: async () => {
      const res = await axiosPrivate.get('/nursing/assignments/op');
      return res.data;
    }
  });

  const { data: recentActivity = [], isLoading: isActivityLoading } = useQuery({
    queryKey: ['nursingRecentActivity'],
    queryFn: async () => {
      const res = await axiosPrivate.get('/nursing/recent-activity');
      return res.data;
    },
    refetchInterval: 15000 // 15 seconds polling
  });

  const assignmentsList = assignments || [];
  const selectedPatient = assignmentsList.find(a => a.patientId === selectedPatientId);

  // Package all widget data
  const data = {
    assignmentsList,
    isAssignmentsLoading,
    selectedPatientId,
    setSelectedPatientId,
    selectedPatient,
    recentActivity,
    isActivityLoading
  };

  return (
    <ConfigDrivenDashboard 
      config={dashboardConfig.ROLE_NURSE}
      data={data}
      activeTab={activeTab}
      onTabChange={setActiveTab}
    />
  );
};

export default NurseDashboard;
