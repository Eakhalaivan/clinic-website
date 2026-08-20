import React, { useState } from 'react';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { axiosPrivate } from '../../api/axios';
import FinancialReports from '../analytics/FinancialReports';
import { 
    DollarSign, FileText, TrendingUp, TrendingDown, 
    ArrowUp, ArrowDown, Minus, Plus, LayoutGrid, 
    IndianRupee, Calendar, ShieldCheck, Settings, Receipt
} from 'lucide-react';

export default function FinanceDashboard() {
    const queryClient = useQueryClient();
    const [activeTab, setActiveTab] = useState('overview');

    const { data: dashboard = {} } = useQuery({
        queryKey: ['finance-dashboard'],
        queryFn: async () => {
            const now = new Date();
            const startDate = new Date(now.getFullYear(), now.getMonth(), 1).toISOString().split('T')[0];
            const endDate = now.toISOString().split('T')[0];
            return (await axiosPrivate.get('/finance/dashboard', { params: { startDate, endDate } })).data;
        }
    });
    const { data: expenses = [] } = useQuery({ queryKey: ['finance-expenses'], queryFn: async () => (await axiosPrivate.get('/finance/expenses')).data });

    const totalRevenue = dashboard.totalRevenue || 0;
    const totalExpenses = dashboard.totalExpenses || 0;
    const netIncome = dashboard.netProfit || 0;

    const approveMutation = useMutation({
        mutationFn: async (id) => {
            const userStr = localStorage.getItem('user');
            const userId = userStr ? JSON.parse(userStr).id : 1;
            return await axiosPrivate.post(`/finance/expenses/${id}/approve?approverId=${userId}`);
        },
        onSuccess: () => queryClient.invalidateQueries(['finance-expenses'])
    });

    const payMutation = useMutation({
        mutationFn: async (id) => {
            const userStr = localStorage.getItem('user');
            const userId = userStr ? JSON.parse(userStr).id : 1;
            return await axiosPrivate.post(`/finance/expenses/${id}/pay?payerId=${userId}`);
        },
        onSuccess: () => queryClient.invalidateQueries(['finance-expenses'])
    });

    const WaveSVG = ({ colorClass, gradientId, stops }) => (
        <svg className={`absolute bottom-0 right-0 w-36 h-16 opacity-40 ${colorClass}`} viewBox="0 0 100 50" preserveAspectRatio="none">
            <defs>
                <linearGradient id={gradientId} x1="0%" y1="0%" x2="0%" y2="100%">
                    {stops.map((stop, i) => <stop key={i} offset={stop.offset} stopColor={stop.color} stopOpacity={stop.opacity} />)}
                </linearGradient>
            </defs>
            <path d="M0 50 C 30 20, 60 60, 100 10 L 100 50 Z" fill={`url(#${gradientId})`} />
            <path d="M0 50 C 30 20, 60 60, 100 10" fill="none" stroke="currentColor" strokeWidth="1.5" />
        </svg>
    );

    return (
        <div className="p-4 md:p-6 text-slate-800 bg-[#f8f9fb] min-h-full w-full max-w-[1600px] mx-auto">
            <div className="mb-5">
              <h2 className="text-2xl font-bold text-[#111827] mb-1 tracking-tight">Finance Dashboard</h2>
              <p className="text-slate-500 font-medium text-sm">Manage Revenue, Expenses, and Accounting</p>
            </div>

            {/* Top Stats */}
            <div className="grid grid-cols-1 md:grid-cols-3 gap-5">
                {/* Revenue Card */}
                <div className="relative bg-white p-5 rounded-xl shadow-[0_2px_10px_-4px_rgba(0,0,0,0.05)] border border-slate-100 border-l-[3px] border-l-emerald-400 overflow-hidden flex flex-col justify-between h-[130px]">
                    <WaveSVG 
                        colorClass="text-emerald-300" 
                        gradientId="grad-green" 
                        stops={[
                            { offset: "0%", color: "#6ee7b7", opacity: 0.6 },
                            { offset: "100%", color: "#ffffff", opacity: 0 }
                        ]} 
                    />
                    <div className="flex items-start gap-3 relative z-10">
                        <div className="w-11 h-11 rounded-full bg-emerald-50 flex items-center justify-center text-emerald-500 shrink-0">
                            <TrendingUp size={20} strokeWidth={2.5} />
                        </div>
                        <div className="flex flex-col">
                            <h3 className="text-slate-500 font-semibold text-xs mb-1">Total Revenue</h3>
                            <p className="text-2xl font-bold text-slate-800 leading-none mb-3">₹{totalRevenue.toLocaleString()}</p>
                            
                            <div className="flex items-center gap-2">
                                <span className="px-1.5 py-0.5 bg-emerald-50 text-emerald-600 text-[10px] font-bold rounded flex items-center gap-0.5">
                                    <ArrowUp size={10} strokeWidth={3} /> 0%
                                </span>
                                <span className="text-[11px] text-slate-400 font-medium">vs last 30 days</span>
                            </div>
                        </div>
                    </div>
                </div>

                {/* Expenses Card */}
                <div className="relative bg-white p-5 rounded-xl shadow-[0_2px_10px_-4px_rgba(0,0,0,0.05)] border border-slate-100 border-l-[3px] border-l-red-400 overflow-hidden flex flex-col justify-between h-[130px]">
                    <WaveSVG 
                        colorClass="text-red-300" 
                        gradientId="grad-red" 
                        stops={[
                            { offset: "0%", color: "#fca5a5", opacity: 0.6 },
                            { offset: "100%", color: "#ffffff", opacity: 0 }
                        ]} 
                    />
                    <div className="flex items-start gap-3 relative z-10">
                        <div className="w-11 h-11 rounded-full bg-red-50 flex items-center justify-center text-red-400 shrink-0">
                            <TrendingDown size={20} strokeWidth={2.5} />
                        </div>
                        <div className="flex flex-col">
                            <h3 className="text-slate-500 font-semibold text-xs mb-1">Total Expenses</h3>
                            <p className="text-2xl font-bold text-slate-800 leading-none mb-3">₹{totalExpenses.toLocaleString()}</p>
                            
                            <div className="flex items-center gap-2">
                                <span className="px-1.5 py-0.5 bg-red-50 text-red-500 text-[10px] font-bold rounded flex items-center gap-0.5">
                                    <ArrowDown size={10} strokeWidth={3} /> 0%
                                </span>
                                <span className="text-[11px] text-slate-400 font-medium">vs last 30 days</span>
                            </div>
                        </div>
                    </div>
                </div>

                {/* Net Income Card */}
                <div className="relative bg-white p-5 rounded-xl shadow-[0_2px_10px_-4px_rgba(0,0,0,0.05)] border border-slate-100 border-l-[3px] border-l-indigo-400 overflow-hidden flex flex-col justify-between h-[130px]">
                    <WaveSVG 
                        colorClass="text-indigo-300" 
                        gradientId="grad-indigo" 
                        stops={[
                            { offset: "0%", color: "#a5b4fc", opacity: 0.6 },
                            { offset: "100%", color: "#ffffff", opacity: 0 }
                        ]} 
                    />
                    <div className="flex items-start gap-3 relative z-10">
                        <div className="w-11 h-11 rounded-full bg-indigo-50 flex items-center justify-center text-indigo-500 shrink-0">
                            <DollarSign size={20} strokeWidth={2.5} />
                        </div>
                        <div className="flex flex-col">
                            <h3 className="text-slate-500 font-semibold text-xs mb-1">Net Income</h3>
                            <p className="text-2xl font-bold text-slate-800 leading-none mb-3">₹{netIncome.toLocaleString()}</p>
                            
                            <div className="flex items-center gap-2">
                                <span className="px-1.5 py-0.5 bg-indigo-50 text-indigo-600 text-[10px] font-bold rounded flex items-center gap-0.5">
                                    <Minus size={10} strokeWidth={3} /> 0%
                                </span>
                                <span className="text-[11px] text-slate-400 font-medium">vs last 30 days</span>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            {/* Expenses Workflow */}
            <div className="mt-6 bg-white rounded-xl shadow-[0_2px_10px_-4px_rgba(0,0,0,0.05)] border border-slate-100 overflow-hidden flex flex-col min-h-[300px]">
                <div className="p-4 flex items-center justify-between bg-white border-b border-slate-100">
                    <h3 className="text-[15px] font-bold text-slate-900">Expense Management & Approvals</h3>
                    <button className="flex items-center gap-1.5 bg-[#3f51b5] hover:bg-[#303f9f] text-white px-3 py-1.5 rounded text-xs font-semibold transition-colors shadow-sm">
                        <Plus size={14} strokeWidth={2.5} /> Add Expense
                    </button>
                </div>
                <div className="p-0 overflow-x-auto flex-1 flex flex-col">
                    <table className="w-full text-left border-collapse min-w-[700px] h-full">
                        <thead>
                            <tr className="bg-[#f8f9fa] border-b border-slate-100">
                                <th className="p-3 px-5 font-semibold text-slate-500 text-[10px] uppercase tracking-wider whitespace-nowrap">
                                    <div className="flex items-center gap-1.5"><LayoutGrid size={12} className="text-slate-400" /> CATEGORY</div>
                                </th>
                                <th className="p-3 font-semibold text-slate-500 text-[10px] uppercase tracking-wider whitespace-nowrap">
                                    <div className="flex items-center gap-1.5"><FileText size={12} className="text-slate-400" /> DESCRIPTION</div>
                                </th>
                                <th className="p-3 font-semibold text-slate-500 text-[10px] uppercase tracking-wider whitespace-nowrap">
                                    <div className="flex items-center gap-1.5"><IndianRupee size={12} className="text-slate-400" /> AMOUNT</div>
                                </th>
                                <th className="p-3 font-semibold text-slate-500 text-[10px] uppercase tracking-wider whitespace-nowrap">
                                    <div className="flex items-center gap-1.5"><Calendar size={12} className="text-slate-400" /> DATE</div>
                                </th>
                                <th className="p-3 font-semibold text-slate-500 text-[10px] uppercase tracking-wider whitespace-nowrap">
                                    <div className="flex items-center gap-1.5"><ShieldCheck size={12} className="text-slate-400" /> STATUS</div>
                                </th>
                                <th className="p-3 pr-5 font-semibold text-slate-500 text-[10px] uppercase tracking-wider whitespace-nowrap">
                                    <div className="flex items-center gap-1.5"><Settings size={12} className="text-slate-400" /> ACTION</div>
                                </th>
                            </tr>
                        </thead>
                        <tbody className="divide-y divide-slate-50 text-xs">
                            {expenses.slice(0, 10).map(expense => (
                                <tr key={expense.id} className="hover:bg-slate-50/50 transition">
                                    <td className="p-3 px-5 font-medium text-slate-800">{expense.category}</td>
                                    <td className="p-3 text-slate-600">{expense.description}</td>
                                    <td className="p-3 text-slate-800 font-semibold">₹{expense.amount}</td>
                                    <td className="p-3 text-slate-500">{new Date(expense.incurredOn).toLocaleDateString()}</td>
                                    <td className="p-3">
                                        <span className={`px-2 py-0.5 rounded-full text-[10px] font-semibold ${
                                            expense.status === 'PENDING_APPROVAL' ? 'bg-orange-50 text-orange-600' :
                                            expense.status === 'APPROVED' ? 'bg-blue-50 text-blue-600' :
                                            expense.status === 'PAID' ? 'bg-emerald-50 text-emerald-600' :
                                            'bg-red-50 text-red-600'
                                        }`}>
                                            {expense.status}
                                        </span>
                                    </td>
                                    <td className="p-3 pr-5">
                                        {expense.status === 'PENDING_APPROVAL' && (
                                            <button 
                                                onClick={() => approveMutation.mutate(expense.id)}
                                                className="px-2 py-1 bg-white border border-slate-200 text-slate-700 hover:bg-slate-50 hover:text-[#3f51b5] hover:border-indigo-200 rounded text-[10px] font-medium transition-colors shadow-sm"
                                            >
                                                Approve
                                            </button>
                                        )}
                                        {expense.status === 'APPROVED' && (
                                            <button 
                                                onClick={() => payMutation.mutate(expense.id)}
                                                className="px-2 py-1 bg-white border border-slate-200 text-slate-700 hover:bg-slate-50 hover:text-emerald-600 hover:border-emerald-200 rounded text-[10px] font-medium transition-colors shadow-sm"
                                            >
                                                Mark Paid
                                            </button>
                                        )}
                                    </td>
                                </tr>
                            ))}
                            {expenses.length === 0 && (
                                <tr className="h-full">
                                    <td colSpan="6" className="p-8 pb-12 align-middle">
                                        <div className="flex flex-col items-center justify-center text-center">
                                            <div className="relative w-[70px] h-[70px] mb-4 flex items-center justify-center">
                                                <div className="absolute inset-0 bg-[#f4f6ff] rounded-full scale-[1.1]"></div>
                                                <div className="absolute top-0 left-1 w-1.5 h-1.5 bg-indigo-100 rounded-full"></div>
                                                <div className="absolute top-4 -right-1 w-2 h-2 bg-indigo-200 rounded-full opacity-60"></div>
                                                <div className="absolute bottom-2 -left-1 w-1 h-1 bg-indigo-200 rounded-full"></div>
                                                
                                                <div className="relative z-10 w-9 h-12 bg-[#e0e7ff] rounded flex flex-col py-2 px-1.5 shadow-sm border border-[#c7d2fe]">
                                                    <div className="w-full h-[2px] bg-[#a5b4fc] rounded-full mb-1.5"></div>
                                                    <div className="w-full h-[2px] bg-[#a5b4fc] rounded-full mb-1.5"></div>
                                                    <div className="w-2/3 h-[2px] bg-[#a5b4fc] rounded-full mb-1.5"></div>
                                                    
                                                    <div className="absolute -bottom-1 left-0 w-full overflow-hidden text-[#e0e7ff]">
                                                        <svg width="100%" height="4" viewBox="0 0 100 10" preserveAspectRatio="none">
                                                            <path d="M0,0 L0,10 L10,0 L20,10 L30,0 L40,10 L50,0 L60,10 L70,0 L80,10 L90,0 L100,10 L100,0 Z" fill="currentColor" stroke="#c7d2fe" strokeWidth="1" />
                                                        </svg>
                                                    </div>
                                                </div>
                                            </div>
                                            <h4 className="text-[15px] font-bold text-slate-800 mb-1">No expenses recorded yet.</h4>
                                            <p className="text-slate-500 text-xs mb-4">Add your first expense to get started.</p>
                                            <button className="flex items-center gap-1.5 bg-white border border-[#c7d2fe] text-[#3f51b5] hover:bg-[#f4f6ff] px-4 py-1.5 rounded text-xs font-semibold transition-colors shadow-sm">
                                                <Plus size={14} strokeWidth={2.5} /> Add Expense
                                            </button>
                                        </div>
                                    </td>
                                </tr>
                            )}
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    );
}
