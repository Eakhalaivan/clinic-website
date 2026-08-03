import React, { useState } from 'react';
import { useQuery } from '@tanstack/react-query';
import { axiosPrivate } from '../../api/axios';
import { ConfigDrivenDashboard } from '../../components/dashboard/ConfigDrivenDashboard';
import { dashboardConfig } from '../../config/dashboardConfig';

const MarketingDashboard = () => {
  const [activeTab, setActiveTab] = useState('campaigns');

  const { data: campaigns = [], isLoading: loadingCampaigns } = useQuery({ queryKey: ['marketing-campaigns'], queryFn: async () => (await axiosPrivate.get('/marketing/campaigns')).data });
  const { data: coupons = [], isLoading: loadingCoupons } = useQuery({ queryKey: ['marketing-coupons'], queryFn: async () => (await axiosPrivate.get('/marketing/coupons')).data });
  const { data: referrals = [], isLoading: loadingReferrals } = useQuery({ queryKey: ['marketing-referrals'], queryFn: async () => (await axiosPrivate.get('/marketing/referrals')).data });

  const sentCampaignsCount = campaigns.filter(c => c.status === 'SENT').length;

  const data = {
    activeTab,
    campaigns,
    coupons,
    referrals,
    loadingCampaigns,
    loadingCoupons,
    loadingReferrals,
    sentCampaignsCount,
    couponsCount: coupons.length,
    referralsCount: referrals.length
  };

  return (
    <ConfigDrivenDashboard 
      config={dashboardConfig.ROLE_MARKETING}
      data={data}
      activeTab={activeTab}
      onTabChange={setActiveTab}
    />
  );
};

export default MarketingDashboard;
