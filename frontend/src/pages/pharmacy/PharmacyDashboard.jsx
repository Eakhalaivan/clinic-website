import React from 'react';
import { Link } from 'react-router-dom';
import { useQuery } from '@tanstack/react-query';
import api from '../../utils/pharmacy/api';
import { 
  Calendar, ChevronDown, Package, Database, ShoppingCart, FileText, AlertOctagon,
  PlusCircle, ArrowRightLeft, FileOutput, ArrowDownToLine, Settings2
} from 'lucide-react';
import { AreaChart, Area, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer, PieChart, Pie, Cell } from 'recharts';

export default function PharmacyDashboard() {
  const { data: stats } = useQuery({
    queryKey: ['pharmacy-kpis'],
    queryFn: () => api.get('/dashboard').then(r => r.data?.data || {}).catch(() => ({})),
    retry: false
  });

  // Mock data for charts and tables to perfectly match the design
  const stockOverviewData = [
    { name: '1 May', value: 12000 },
    { name: '5 May', value: 19000 },
    { name: '9 May', value: 24000 },
    { name: '13 May', value: 20000 },
    { name: '17 May', value: 27000 },
    { name: '21 May', value: 24560 },
  ];

  const stockSummaryData = [
    { name: 'In Stock', value: 876, color: '#10b981' },
    { name: 'Low Stock', value: 32, color: '#f59e0b' },
    { name: 'Out of Stock', value: 24, color: '#ef4444' },
    { name: 'Expired', value: 7, color: '#8b5cf6' },
  ];

  const lowStockAlerts = [
    { name: 'Paracetamol 650mg', type: 'Tablet', stock: 15, min: 50 },
    { name: 'Amoxicillin 500mg', type: 'Capsule', stock: 8, min: 30 },
    { name: 'Cetirizine 10mg', type: 'Tablet', stock: 12, min: 25 },
    { name: 'Salbutamol 100mcg', type: 'Inhaler', stock: 5, min: 20 },
    { name: 'Pantoprazole 40mg', type: 'Tablet', stock: 10, min: 20 },
  ];

  const recentlyAdded = [
    { name: 'Azithromycin 500mg', type: 'Tablet', category: 'Antibiotic', mfg: 'Cipla Ltd.', batch: 'AZ50023', exp: '31 Dec 2025', stock: 120, price: '$2.50', status: 'In Stock' },
    { name: 'Vitamin D3 60000 IU', type: 'Capsule', category: 'Vitamins', mfg: 'Sun Pharma', batch: 'VD360023', exp: '30 Nov 2025', stock: 85, price: '$3.20', status: 'In Stock' },
    { name: 'Metformin 500mg', type: 'Tablet', category: 'Diabetes', mfg: 'Mankind Pharma', batch: 'MF50023', exp: '30 Sep 2025', stock: 200, price: '$1.10', status: 'In Stock' },
    { name: 'Losartan 50mg', type: 'Tablet', category: 'Cardiovascular', mfg: 'Zydus Cadila', batch: 'LS50023', exp: '31 Aug 2025', stock: 60, price: '$1.80', status: 'Low Stock' },
    { name: 'Omeprazole 20mg', type: 'Capsule', category: 'Gastric', mfg: 'Dr. Reddy\'s', batch: 'OM20023', exp: '30 Jul 2025', stock: 25, price: '$1.40', status: 'Low Stock' },
  ];

  const expiryAlerts = [
    { name: 'Doxycycline 100mg', exp: '15 Jun 2024', days: 25 },
    { name: 'Ranitidine 150mg', exp: '22 Jun 2024', days: 32 },
    { name: 'Albendazole 400mg', exp: '05 Jul 2024', days: 45 },
  ];

  const getStatusColor = (status) => {
    switch (status) {
      case 'In Stock': return 'bg-emerald-100 text-emerald-700';
      case 'Low Stock': return 'bg-amber-100 text-amber-700';
      default: return 'bg-slate-100 text-slate-700';
    }
  };

  const getCategoryColor = (cat) => {
    switch(cat) {
      case 'Antibiotic': return 'bg-indigo-50 text-indigo-700';
      case 'Vitamins': return 'bg-emerald-50 text-emerald-700';
      case 'Diabetes': return 'bg-blue-50 text-blue-700';
      case 'Cardiovascular': return 'bg-rose-50 text-rose-700';
      case 'Gastric': return 'bg-amber-50 text-amber-700';
      default: return 'bg-slate-50 text-slate-700';
    }
  };

  return (
    <div className="font-sans">
      {/* Header */}
      <div className="flex justify-between items-center mb-8">
        <div>
          <h1 className="text-2xl font-bold text-slate-900 tracking-tight">Dashboard</h1>
          <p className="text-sm text-slate-500 mt-1">Overview of your pharmacy inventory and sales</p>
        </div>
        <button className="flex items-center gap-2 px-4 py-2.5 bg-white border border-slate-200 rounded-xl text-sm font-medium text-slate-700 shadow-sm hover:bg-slate-50 transition-colors">
          <Calendar className="w-4 h-4 text-indigo-600" />
          Today, 21 May 2024
          <ChevronDown className="w-4 h-4 text-slate-400 ml-2" />
        </button>
      </div>

      {/* 5 KPI Cards */}
      <div className="grid grid-cols-2 md:grid-cols-3 xl:grid-cols-5 gap-5 mb-6">
        <div className="bg-white rounded-2xl p-5 border border-slate-100 shadow-sm">
          <div className="flex items-center gap-4 mb-3">
            <div className="w-12 h-12 bg-indigo-50 rounded-xl flex items-center justify-center text-indigo-600">
              <Package className="w-6 h-6" />
            </div>
            <div>
              <p className="text-[11px] font-semibold text-slate-500 mb-1">Total Medicines</p>
              <h3 className="text-xl font-bold text-slate-900">1,248</h3>
            </div>
          </div>
          <p className="text-xs font-medium text-emerald-600 flex items-center gap-1">
            ↑ 8.5% <span className="text-slate-400 font-normal">from last month</span>
          </p>
        </div>

        <div className="bg-white rounded-2xl p-5 border border-slate-100 shadow-sm">
          <div className="flex items-center gap-4 mb-3">
            <div className="w-12 h-12 bg-blue-50 rounded-xl flex items-center justify-center text-blue-500">
              <Database className="w-6 h-6" />
            </div>
            <div>
              <p className="text-[11px] font-semibold text-slate-500 mb-1">Total Stock Value</p>
              <h3 className="text-xl font-bold text-slate-900">₹24,560.80</h3>
            </div>
          </div>
          <p className="text-xs font-medium text-emerald-600 flex items-center gap-1">
            ↑ 12.3% <span className="text-slate-400 font-normal">from last month</span>
          </p>
        </div>

        <div className="bg-white rounded-2xl p-5 border border-slate-100 shadow-sm">
          <div className="flex items-center gap-4 mb-3">
            <div className="w-12 h-12 bg-emerald-50 rounded-xl flex items-center justify-center text-emerald-500">
              <ShoppingCart className="w-6 h-6" />
            </div>
            <div>
              <p className="text-[11px] font-semibold text-slate-500 mb-1">Total Sales (Today)</p>
              <h3 className="text-xl font-bold text-slate-900">₹1,245.30</h3>
            </div>
          </div>
          <p className="text-xs font-medium text-emerald-600 flex items-center gap-1">
            ↑ 6.7% <span className="text-slate-400 font-normal">from yesterday</span>
          </p>
        </div>

        <div className="bg-white rounded-2xl p-5 border border-slate-100 shadow-sm">
          <div className="flex items-center gap-4 mb-3">
            <div className="w-12 h-12 bg-amber-50 rounded-xl flex items-center justify-center text-amber-500">
              <FileText className="w-6 h-6" />
            </div>
            <div>
              <p className="text-[11px] font-semibold text-slate-500 mb-1">Low Stock Items</p>
              <h3 className="text-xl font-bold text-slate-900">32</h3>
            </div>
          </div>
          <p className="text-xs font-medium text-rose-500 flex items-center gap-1">
            ↓ 5 <span className="text-slate-400 font-normal">from yesterday</span>
          </p>
        </div>

        <div className="bg-white rounded-2xl p-5 border border-slate-100 shadow-sm">
          <div className="flex items-center gap-4 mb-3">
            <div className="w-12 h-12 bg-rose-50 rounded-xl flex items-center justify-center text-rose-500">
              <AlertOctagon className="w-6 h-6" />
            </div>
            <div>
              <p className="text-[11px] font-semibold text-slate-500 mb-1">Expired Items</p>
              <h3 className="text-xl font-bold text-slate-900">7</h3>
            </div>
          </div>
          <p className="text-xs font-medium text-rose-500 flex items-center gap-1">
            ↓ 2 <span className="text-slate-400 font-normal">from yesterday</span>
          </p>
        </div>
      </div>

      {/* Middle Grid */}
      <div className="grid grid-cols-1 xl:grid-cols-12 gap-6 mb-6">
        
        {/* Stock Overview Chart */}
        <div className="xl:col-span-5 bg-white rounded-2xl p-6 border border-slate-100 shadow-sm">
          <div className="flex justify-between items-center mb-6">
            <h3 className="font-bold text-slate-900 text-base">Stock Overview</h3>
            <button className="flex items-center gap-1 text-xs font-medium text-slate-600 bg-slate-50 px-3 py-1.5 rounded-lg border border-slate-200">
              This Month <ChevronDown className="w-3 h-3" />
            </button>
          </div>
          <div className="h-[250px] w-full">
            <ResponsiveContainer width="100%" height="100%">
              <AreaChart data={stockOverviewData} margin={{ top: 10, right: 10, left: -20, bottom: 0 }}>
                <defs>
                  <linearGradient id="colorStock" x1="0" y1="0" x2="0" y2="1">
                    <stop offset="5%" stopColor="#8b5cf6" stopOpacity={0.3}/>
                    <stop offset="95%" stopColor="#8b5cf6" stopOpacity={0}/>
                  </linearGradient>
                </defs>
                <CartesianGrid strokeDasharray="3 3" vertical={false} stroke="#f1f5f9" />
                <XAxis dataKey="name" axisLine={false} tickLine={false} tick={{fill: '#94a3b8', fontSize: 11}} dy={10} />
                <YAxis axisLine={false} tickLine={false} tick={{fill: '#94a3b8', fontSize: 11}} tickFormatter={(val) => val >= 1000 ? `${val/1000}K` : val} />
                <Tooltip />
                <Area type="monotone" dataKey="value" stroke="#8b5cf6" strokeWidth={3} fillOpacity={1} fill="url(#colorStock)" />
              </AreaChart>
            </ResponsiveContainer>
          </div>
        </div>

        {/* Stock Summary Donut */}
        <div className="xl:col-span-3 bg-white rounded-2xl p-6 border border-slate-100 shadow-sm">
          <h3 className="font-bold text-slate-900 text-base mb-2">Stock Summary</h3>
          <div className="relative h-[200px] flex items-center justify-center">
            <ResponsiveContainer width="100%" height="100%">
              <PieChart>
                <Pie
                  data={stockSummaryData}
                  innerRadius={65}
                  outerRadius={85}
                  paddingAngle={2}
                  dataKey="value"
                  stroke="none"
                >
                  {stockSummaryData.map((entry, index) => (
                    <Cell key={`cell-${index}`} fill={entry.color} />
                  ))}
                </Pie>
              </PieChart>
            </ResponsiveContainer>
            <div className="absolute inset-0 flex flex-col items-center justify-center">
              <span className="text-2xl font-bold text-slate-900">1,248</span>
              <span className="text-xs text-slate-500 font-medium">Total Items</span>
            </div>
          </div>
          <div className="mt-2 space-y-2.5">
            {stockSummaryData.map(item => (
              <div key={item.name} className="flex items-center justify-between text-xs">
                <div className="flex items-center gap-2">
                  <div className="w-2.5 h-2.5 rounded-full" style={{ backgroundColor: item.color }} />
                  <span className="text-slate-600 font-medium">{item.name}</span>
                </div>
                <div className="flex items-center gap-2">
                  <span className="font-semibold text-slate-900">{item.value}</span>
                  <span className="text-slate-400 w-10 text-right">({(item.value / 1248 * 100).toFixed(1)}%)</span>
                </div>
              </div>
            ))}
          </div>
        </div>

        {/* Low Stock Alert */}
        <div className="xl:col-span-4 bg-white rounded-2xl p-6 border border-slate-100 shadow-sm flex flex-col">
          <div className="flex justify-between items-center mb-4">
            <h3 className="font-bold text-slate-900 text-base">Low Stock Alert</h3>
            <button className="text-indigo-600 text-xs font-semibold hover:underline">View All</button>
          </div>
          <div className="flex-1 space-y-4">
            {lowStockAlerts.map((item, idx) => (
              <div key={idx} className="flex items-center justify-between group">
                <div className="flex items-center gap-3 overflow-hidden">
                  <div className="w-9 h-9 bg-slate-100 rounded flex items-center justify-center text-slate-400 shrink-0">
                    <Package className="w-4 h-4" />
                  </div>
                  <div className="min-w-0">
                    <h4 className="text-xs font-semibold text-slate-900 group-hover:text-indigo-600 transition-colors truncate" title={item.name}>{item.name}</h4>
                    <p className="text-[10px] text-slate-500 mt-0.5 truncate">{item.type}</p>
                  </div>
                </div>
                <div className="text-right shrink-0 ml-2">
                  <p className="text-[11px] font-bold text-rose-600">Stock: {item.stock}</p>
                  <p className="text-[10px] text-slate-400 font-medium">Min: {item.min}</p>
                </div>
              </div>
            ))}
          </div>
        </div>
      </div>

      {/* Bottom Grid */}
      <div className="grid grid-cols-1 xl:grid-cols-12 gap-6">
        
        {/* Recently Added Medicines */}
        <div className="xl:col-span-6 bg-white rounded-2xl p-6 border border-slate-100 shadow-sm overflow-hidden">
          <div className="flex justify-between items-center mb-6">
            <h3 className="font-bold text-slate-900 text-base">Recently Added Medicines</h3>
            <button className="text-indigo-600 text-xs font-semibold hover:underline">View All</button>
          </div>
          <div className="overflow-x-auto">
            <table className="w-full text-left text-sm whitespace-nowrap">
              <thead>
                <tr className="text-[11px] font-semibold text-slate-500 border-b border-slate-100">
                  <th className="pb-3 font-medium px-2">Medicine Name</th>
                  <th className="pb-3 font-medium px-2">Category</th>
                  <th className="pb-3 font-medium px-2">Manufacturer</th>
                  <th className="pb-3 font-medium px-2">Batch No.</th>
                  <th className="pb-3 font-medium px-2">Expiry Date</th>
                  <th className="pb-3 font-medium px-2 text-right">Stock</th>
                  <th className="pb-3 font-medium px-2 text-right">Price</th>
                  <th className="pb-3 font-medium px-2 text-center">Status</th>
                </tr>
              </thead>
              <tbody className="divide-y divide-slate-50">
                {recentlyAdded.map((item, idx) => (
                  <tr key={idx} className="hover:bg-slate-50/50 transition-colors">
                    <td className="py-2.5 px-2">
                      <div className="flex items-center gap-3">
                        <div className="w-8 h-8 bg-slate-100 rounded flex items-center justify-center text-slate-400 shrink-0">
                          <Package className="w-4 h-4" />
                        </div>
                        <div>
                          <p className="font-semibold text-slate-900 text-xs">{item.name}</p>
                          <p className="text-[10px] text-slate-500">{item.type}</p>
                        </div>
                      </div>
                    </td>
                    <td className="py-2.5 px-2">
                      <span className={`px-2 py-1.5 rounded text-[10px] font-semibold tracking-wide ${getCategoryColor(item.category)}`}>
                        {item.category}
                      </span>
                    </td>
                    <td className="py-2.5 px-2 text-xs text-slate-700 font-medium">{item.mfg}</td>
                    <td className="py-2.5 px-2 text-[11px] text-slate-600 font-medium">{item.batch}</td>
                    <td className="py-2.5 px-2 text-[11px] text-slate-600 font-medium">{item.exp}</td>
                    <td className="py-2.5 px-2 text-xs text-slate-900 font-bold text-right">{item.stock}</td>
                    <td className="py-2.5 px-2 text-xs text-slate-900 font-bold text-right">{item.price}</td>
                    <td className="py-2.5 px-2 text-center">
                      <span className={`px-2 py-1 rounded text-[10px] font-bold ${getStatusColor(item.status)}`}>
                        {item.status}
                      </span>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>

        {/* Quick Actions */}
        <div className="xl:col-span-3 bg-white rounded-2xl p-6 border border-slate-100 shadow-sm">
          <h3 className="font-bold text-slate-900 text-base mb-6">Quick Actions</h3>
          <div className="grid grid-cols-2 gap-3">
            <Link to="/pharmacy/medicine-master" className="flex flex-col items-center justify-center gap-2.5 p-4 rounded-xl bg-indigo-50 hover:bg-indigo-100 transition-colors border border-transparent hover:border-indigo-200 group">
              <div className="w-10 h-10 rounded-full bg-white text-indigo-600 flex items-center justify-center group-hover:scale-110 transition-transform shadow-sm">
                <PlusCircle className="w-5 h-5" />
              </div>
              <span className="text-[10px] font-bold text-indigo-900 text-center">Add Medicine</span>
            </Link>
            <Link to="/pharmacy/purchase-orders" className="flex flex-col items-center justify-center gap-2.5 p-4 rounded-xl bg-sky-50 hover:bg-sky-100 transition-colors border border-transparent hover:border-sky-200 group">
              <div className="w-10 h-10 rounded-full bg-white text-sky-600 flex items-center justify-center group-hover:scale-110 transition-transform shadow-sm">
                <ShoppingCart className="w-5 h-5" />
              </div>
              <span className="text-[10px] font-bold text-sky-900 text-center">Purchase Order</span>
            </Link>
            <Link to="/pharmacy/medicine-stock" className="flex flex-col items-center justify-center gap-2.5 p-4 rounded-xl bg-emerald-50 hover:bg-emerald-100 transition-colors border border-transparent hover:border-emerald-200 group">
              <div className="w-10 h-10 rounded-full bg-white text-emerald-600 flex items-center justify-center group-hover:scale-110 transition-transform shadow-sm">
                <ArrowRightLeft className="w-5 h-5" />
              </div>
              <span className="text-[10px] font-bold text-emerald-900 text-center">Stock Transfer</span>
            </Link>
            <Link to="/pharmacy/direct-pharmacy-sales" className="flex flex-col items-center justify-center gap-2.5 p-4 rounded-xl bg-orange-50 hover:bg-orange-100 transition-colors border border-transparent hover:border-orange-200 group">
              <div className="w-10 h-10 rounded-full bg-white text-orange-600 flex items-center justify-center group-hover:scale-110 transition-transform shadow-sm">
                <FileOutput className="w-5 h-5" />
              </div>
              <span className="text-[10px] font-bold text-orange-900 text-center">Sales Invoice</span>
            </Link>
            <Link to="/pharmacy/grnentry" className="flex flex-col items-center justify-center gap-2.5 p-4 rounded-xl bg-rose-50 hover:bg-rose-100 transition-colors border border-transparent hover:border-rose-200 group">
              <div className="w-10 h-10 rounded-full bg-white text-rose-600 flex items-center justify-center group-hover:scale-110 transition-transform shadow-sm">
                <ArrowDownToLine className="w-5 h-5" />
              </div>
              <span className="text-[10px] font-bold text-rose-900 text-center">GRN Entry</span>
            </Link>
            <Link to="/pharmacy/medicine-stock" className="flex flex-col items-center justify-center gap-2.5 p-4 rounded-xl bg-purple-50 hover:bg-purple-100 transition-colors border border-transparent hover:border-purple-200 group">
              <div className="w-10 h-10 rounded-full bg-white text-purple-600 flex items-center justify-center group-hover:scale-110 transition-transform shadow-sm">
                <Settings2 className="w-5 h-5" />
              </div>
              <span className="text-[10px] font-bold text-purple-900 text-center">Stock Adjust</span>
            </Link>
          </div>
        </div>

        {/* Expiry Alert */}
        <div className="xl:col-span-3 bg-white rounded-2xl p-6 border border-slate-100 shadow-sm flex flex-col">
          <div className="flex justify-between items-center mb-6">
            <h3 className="font-bold text-slate-900 text-base">Expiry Alert</h3>
            <button className="text-indigo-600 text-xs font-semibold hover:underline">View All</button>
          </div>
          <div className="flex-1 space-y-5">
            {expiryAlerts.map((item, idx) => (
              <div key={idx} className="flex items-center justify-between group">
                <div className="flex items-center gap-2.5 overflow-hidden">
                  <div className="w-8 h-8 bg-slate-100 rounded flex items-center justify-center text-slate-400 shrink-0">
                    <Package className="w-4 h-4" />
                  </div>
                  <div className="min-w-0">
                    <h4 className="text-[11px] font-bold text-slate-900 group-hover:text-indigo-600 transition-colors truncate" title={item.name}>{item.name}</h4>
                    <p className="text-[9px] text-slate-500 mt-0.5 truncate">Expiry: {item.exp}</p>
                  </div>
                </div>
                <div className="text-right shrink-0 ml-2">
                  <p className={`text-sm font-bold ${item.days < 30 ? 'text-rose-600' : 'text-amber-500'}`}>{item.days}</p>
                  <p className={`text-[8px] font-bold uppercase tracking-wider mt-0.5 ${item.days < 30 ? 'text-rose-500' : 'text-amber-400'}`}>Days Left</p>
                </div>
              </div>
            ))}
          </div>
        </div>

      </div>
    </div>
  );
}
