import React, { useState, useEffect } from 'react';
import { useQuery, useQueryClient } from '@tanstack/react-query';
import { axiosPrivate } from '../../api/axios';
import { ConfigDrivenDashboard } from '../../components/dashboard/ConfigDrivenDashboard';
import { dashboardConfig } from '../../config/dashboardConfig';
import useAuthStore from '../../store/authStore';
import { useSearchParams } from 'react-router-dom';

// Import the actual page components for the tabs
import NurseAssignedPatients from './NurseAssignedPatients';
import WardManagement from './WardManagement';

const NurseDashboard = () => {
  const { user, token } = useAuthStore();
  const [searchParams] = useSearchParams();
  const [activeTab, setActiveTab] = useState('Dashboard');
  const [selectedPatientId, setSelectedPatientId] = useState(null);
  const queryClient = useQueryClient();

  useEffect(() => {
    // If the URL has ?panel=supplies, we can switch to the Inventory tab
    if (searchParams.get('panel') === 'supplies') {
      setActiveTab('Inventory');
    }
  }, [searchParams]);

  useEffect(() => {
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
  }, [queryClient, token]);

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
    >
      {activeTab === 'OP Queue' && <NurseAssignedPatients />}
      {activeTab === 'IP Wards' && <WardManagement />}
    </ConfigDrivenDashboard>
  );
};

export default NurseDashboard;
