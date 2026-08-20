import React, { useState } from 'react';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { axiosPrivate } from '../../api/axios';
import FinancialReports from '../analytics/FinancialReports';
import { DollarSign, FileText, CheckCircle, AlertTriangle, TrendingUp, TrendingDown } from 'lucide-react';

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

    return (
        <div className="p-6">
            <div className="mb-4">
              <h2>Finance Dashboard</h2>
              <p className="text-muted">Manage Revenue, Expenses, and Accounting</p>
            </div>

            {/* Top Stats */}
            <div className="grid grid-cols-1 md:grid-cols-3 gap-6 mt-6">
                <div className="bg-white p-6 rounded-2xl shadow-sm border border-slate-200">
                    <div className="flex items-center gap-3 mb-2">
                        <div className="w-10 h-10 rounded-full bg-emerald-100 flex items-center justify-center text-emerald-600">
                            <TrendingUp size={20} />
                        </div>
                        <h3 className="text-slate-500 font-medium">Total Revenue</h3>
                    </div>
                    <p className="text-3xl font-bold text-slate-800">₹{totalRevenue.toLocaleString()}</p>
                </div>
                <div className="bg-white p-6 rounded-2xl shadow-sm border border-slate-200">
                    <div className="flex items-center gap-3 mb-2">
                        <div className="w-10 h-10 rounded-full bg-red-100 flex items-center justify-center text-red-600">
                            <TrendingDown size={20} />
                        </div>
                        <h3 className="text-slate-500 font-medium">Total Expenses</h3>
                    </div>
                    <p className="text-3xl font-bold text-slate-800">₹{totalExpenses.toLocaleString()}</p>
                </div>
                <div className="bg-white p-6 rounded-2xl shadow-sm border border-slate-200">
                    <div className="flex items-center gap-3 mb-2">
                        <div className="w-10 h-10 rounded-full bg-indigo-100 flex items-center justify-center text-indigo-600">
                            <DollarSign size={20} />
                        </div>
                        <h3 className="text-slate-500 font-medium">Net Income</h3>
                    </div>
                    <p className="text-3xl font-bold text-slate-800">₹{netIncome.toLocaleString()}</p>
                </div>
            </div>

            {/* Expenses Workflow */}
            <div className="mt-8 bg-white rounded-2xl shadow-sm border border-slate-200 overflow-hidden">
                <div className="p-5 border-b border-slate-200 bg-slate-50">
                    <h3 className="text-lg font-semibold text-slate-800">Expense Management & Approvals</h3>
                </div>
                <div className="p-0">
                    <table className="w-full text-left border-collapse">
                        <thead>
                            <tr className="bg-slate-50 text-slate-500 text-xs uppercase tracking-wider border-b border-slate-200">
                                <th className="p-4 font-semibold">Category</th>
                                <th className="p-4 font-semibold">Description</th>
                                <th className="p-4 font-semibold">Amount</th>
                                <th className="p-4 font-semibold">Date</th>
                                <th className="p-4 font-semibold">Status</th>
                                <th className="p-4 font-semibold">Action</th>
                            </tr>
                        </thead>
                        <tbody className="divide-y divide-slate-100 text-sm">
                            {expenses.slice(0, 10).map(expense => (
                                <tr key={expense.id} className="hover:bg-slate-50 transition">
                                    <td className="p-4 font-medium text-slate-800">{expense.category}</td>
                                    <td className="p-4 text-slate-600">{expense.description}</td>
                                    <td className="p-4 text-slate-800 font-medium">₹{expense.amount}</td>
                                    <td className="p-4 text-slate-500">{new Date(expense.incurredOn).toLocaleDateString()}</td>
                                    <td className="p-4">
                                        <span className={`px-2.5 py-1 rounded-full text-xs font-medium ${
                                            expense.status === 'PENDING_APPROVAL' ? 'bg-orange-100 text-orange-700' :
                                            expense.status === 'APPROVED' ? 'bg-blue-100 text-blue-700' :
                                            expense.status === 'PAID' ? 'bg-emerald-100 text-emerald-700' :
                                            'bg-red-100 text-red-700'
                                        }`}>
                                            {expense.status}
                                        </span>
                                    </td>
                                    <td className="p-4">
                                        {expense.status === 'PENDING_APPROVAL' && (
                                            <button 
                                                onClick={() => approveMutation.mutate(expense.id)}
                                                className="px-3 py-1 bg-blue-50 text-blue-600 hover:bg-blue-100 rounded text-xs font-medium"
                                            >
                                                Approve
                                            </button>
                                        )}
                                        {expense.status === 'APPROVED' && (
                                            <button 
                                                onClick={() => payMutation.mutate(expense.id)}
                                                className="px-3 py-1 bg-emerald-50 text-emerald-600 hover:bg-emerald-100 rounded text-xs font-medium"
                                            >
                                                Mark Paid & Post to GL
                                            </button>
                                        )}
                                    </td>
                                </tr>
                            ))}
                            {expenses.length === 0 && (
                                <tr>
                                    <td colSpan="6" className="p-8 text-center text-slate-500">
                                        No expenses recorded yet.
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
