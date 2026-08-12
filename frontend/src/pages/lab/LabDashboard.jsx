import React from 'react';
import { useQuery } from '@tanstack/react-query';
import { axiosPrivate } from '../../api/axios';
import { useAuth } from '../../context/AuthContext';
import LabTopKpis from '../../components/lab/LabTopKpis';
import LabStatusDonut from '../../components/lab/LabStatusDonut';
import LabPriorityDonut from '../../components/lab/LabPriorityDonut';
import LabDailyTrend from '../../components/lab/LabDailyTrend';
import LabTurnaroundTime from '../../components/lab/LabTurnaroundTime';
import LabAlerts from '../../components/lab/LabAlerts';
import LabQuickActions from '../../components/lab/LabQuickActions';
import LabRecentRequests from '../../components/lab/LabRecentRequests';
import LabStatusSidebar from '../../components/lab/LabStatusSidebar';

const LabDashboard = () => {
  const { auth } = useAuth();
  const [filter, setFilter] = React.useState('ALL');

  const { data: summaryResponse, isLoading: summaryLoading } = useQuery({
    queryKey: ['lab-dashboard-summary'],
    queryFn: async () => {
      const res = await axiosPrivate.get('/lab/operations/dashboard', {
        params: { branchId: 1 }
      });
      return res.data;
    },
    refetchInterval: 30000 
  });

  const summary = summaryResponse || { totalRequests: 0, statusCounts: {}, priorityCounts: {}, requestsToday: 0 };

  if (summaryLoading) {
    return (
      <div className="flex justify-center items-center h-64">
        <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-indigo-600"></div>
      </div>
    );
  }

  return (
    <div className="space-y-6">
      <div className="flex justify-between items-end mb-6">
        <div>
          <h1 className="text-2xl font-bold text-gray-900 mb-1">Laboratory Dashboard</h1>
          <p className="text-sm font-medium text-gray-500">Welcome back, {auth?.user?.name || 'Lab Admin'}!</p>
        </div>
      </div>

      <LabTopKpis summary={summary} />
      <LabQuickActions setFilter={setFilter} />

      {/* Main Grid Layout */}
      <div className="grid grid-cols-1 lg:grid-cols-12 gap-6">
        {/* Left Column - Main Charts */}
        <div className="lg:col-span-8 xl:col-span-9 space-y-6">
          <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
            <LabStatusDonut summary={summary} />
            <LabPriorityDonut summary={summary} />
          </div>
          
          <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
             <LabDailyTrend />
             <LabTurnaroundTime />
          </div>
        </div>

        {/* Right Column - Sidebar style */}
        <div className="lg:col-span-4 xl:col-span-3 space-y-6 flex flex-col">
          <div className="h-64 flex-shrink-0">
             <LabStatusSidebar summary={summary} />
          </div>
          <div className="h-64 flex-shrink-0">
             <LabAlerts />
          </div>
          <div className="flex-1 min-h-[300px]">
             {/* Small sized table at bottom right */}
             <LabRecentRequests filter={filter} setFilter={setFilter} onViewDetails={(req) => console.log(req)} />
          </div>
        </div>
      </div>
    </div>
  );
};

export default LabDashboard;
