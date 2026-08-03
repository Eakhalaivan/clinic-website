import React, { useState } from 'react';
import { useQuery } from '@tanstack/react-query';
import { axiosPrivate } from '../../api/axios';
import { AlertTriangle } from 'lucide-react';
import { ConfigDrivenDashboard } from '../../components/dashboard/ConfigDrivenDashboard';
import { dashboardConfig } from '../../config/dashboardConfig';
import { AmbulanceNewRequestWidget } from '../../components/dashboard/widgets/AmbulanceWidgets';

const AmbulanceDashboard = () => {
  const [activeTab, setActiveTab] = useState('requests');
  const [showNewRequest, setShowNewRequest] = useState(false);

  const { data: fleet = [], isLoading: loadingFleet } = useQuery({ queryKey: ['ambulance-fleet'], queryFn: async () => (await axiosPrivate.get('/ambulance/fleet')).data, refetchInterval: 15000 });
  const { data: requests = [], isLoading: loadingRequests } = useQuery({ queryKey: ['ambulance-requests'], queryFn: async () => (await axiosPrivate.get('/ambulance/requests')).data, refetchInterval: 10000 });

  const availableAmbulances = fleet.filter(a => a.status === 'AVAILABLE');
  const activeCount = requests.filter(r => ['REQUESTED', 'DISPATCHED', 'EN_ROUTE'].includes(r.status)).length;

  const data = {
    activeTab,
    requests,
    fleet,
    loadingRequests,
    loadingFleet,
    activeCount,
    availableCount: availableAmbulances.length,
    fleetCount: fleet.length,
    requestsCount: requests.length,
    customQuickActions: [
      {
        label: 'New Emergency',
        icon: AlertTriangle,
        action: () => setShowNewRequest(true),
        color: 'text-rose-600',
        bg: 'bg-rose-50'
      }
    ]
  };

  return (
    <>
      <ConfigDrivenDashboard 
        config={dashboardConfig.ROLE_AMBULANCE}
        data={data}
        activeTab={activeTab}
        onTabChange={setActiveTab}
      />
      {showNewRequest && (
        <AmbulanceNewRequestWidget
          showNewRequest={showNewRequest}
          setShowNewRequest={setShowNewRequest}
        />
      )}
    </>
  );
};

export default AmbulanceDashboard;
