import { useState } from 'react';
import { useQuery } from '@tanstack/react-query';
import { axiosPrivate } from '../../api/axios';
import { dashboardConfig } from '../../config/dashboardConfig';
import PageTransition from '../../components/ui/PageTransition';


const VendorDashboard = () => {
  const [activeTab, setActiveTab] = useState('orders');

  const { data: purchaseOrders = [], isLoading: loadingOrders } = useQuery({ queryKey: ['vendor-purchase-orders'], queryFn: async () => (await axiosPrivate.get('/vendor/purchase-orders')).data });
  const { data: deliveries = [], isLoading: loadingDeliveries } = useQuery({ queryKey: ['vendor-deliveries'], queryFn: async () => (await axiosPrivate.get('/vendor/deliveries')).data });

  const pendingPosCount = purchaseOrders.filter(po => po.status === 'SENT' || po.status === 'DRAFT').length;
  const activeDeliveriesCount = deliveries.filter(d => d.status === 'DISPATCHED' || d.status === 'IN_TRANSIT').length;

  const data = {
    activeTab,
    purchaseOrders,
    deliveries,
    loadingOrders,
    loadingDeliveries,
    pendingPosCount,
    activeDeliveriesCount,
    purchaseOrdersCount: purchaseOrders.length
  };

  return (
    <PageTransition>
    <ConfigDrivenDashboard 
      config={dashboardConfig.ROLE_VENDOR}
      data={data}
      activeTab={activeTab}
      onTabChange={setActiveTab}
    />
    </PageTransition>
  );
};

export default VendorDashboard;
