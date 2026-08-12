import React, { useState } from 'react';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { axiosPrivate } from '../../api/axios';
import { IndianRupee, Play, FileText, ChevronDown, ChevronRight, CheckCircle, Clock } from 'lucide-react';
import toast, { Toaster } from 'react-hot-toast';
import dayjs from 'dayjs';

const PayrollManagement = () => {
  const [selectedRun, setSelectedRun] = useState(null);
  const queryClient = useQueryClient();

  // Fetch all payroll runs
  const { data: runs = [], isLoading } = useQuery({
    queryKey: ['payrollRuns'],
    queryFn: async () => {
      const res = await axiosPrivate.get('/hr/payroll/runs');
      return res.data;
    }
  });

  // Create a new run
  const createRunMutation = useMutation({
    mutationFn: async () => {
      const currentMonth = dayjs().startOf('month').format('YYYY-MM-DD');
      const res = await axiosPrivate.post('/hr/payroll/runs', {
        periodStart: currentMonth,
        periodEnd: dayjs().endOf('month').format('YYYY-MM-DD'),
        status: 'DRAFT',
        runDate: dayjs().format('YYYY-MM-DD')
      });
      return res.data;
    },
    onSuccess: () => {
      toast.success("Payroll run created");
      queryClient.invalidateQueries(['payrollRuns']);
    },
    onError: (err) => {
      toast.error("Failed to create run: " + err.message);
    }
  });

  // Process a run
  const processRunMutation = useMutation({
    mutationFn: async (runId) => {
      const res = await axiosPrivate.post(`/hr/payroll/runs/${runId}/process`);
      return res.data;
    },
    onSuccess: () => {
      toast.success("Payroll run processed successfully");
      queryClient.invalidateQueries(['payrollRuns']);
      if (selectedRun) {
        queryClient.invalidateQueries(['payslips', selectedRun.id]);
      }
    }
  });

  // Fetch payslips for selected run
  const { data: payslips = [], isLoading: isLoadingPayslips } = useQuery({
    queryKey: ['payslips', selectedRun?.id],
    queryFn: async () => {
      if (!selectedRun) return [];
      const res = await axiosPrivate.get(`/hr/payroll/runs/${selectedRun.id}/payslips`);
      return res.data;
    },
    enabled: !!selectedRun
  });

  return (
    <div className="p-6 max-w-7xl mx-auto font-sans text-slate-800">
      <Toaster position="top-right" />
      
      <div className="flex justify-between items-center mb-6">
        <div>
          <h1 className="text-2xl font-bold">Payroll Management</h1>
          <p className="text-sm text-slate-500">Manage payroll runs, payslips, and salary components.</p>
        </div>
        <button 
          onClick={() => createRunMutation.mutate()}
          className="bg-indigo-600 hover:bg-indigo-700 text-white px-4 py-2 rounded shadow-sm text-sm font-semibold flex items-center gap-2 transition"
          disabled={createRunMutation.isLoading}
        >
          <IndianRupee size={16} /> {createRunMutation.isLoading ? 'Creating...' : 'New Payroll Run'}
        </button>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
        
        {/* Left Col: Payroll Runs List */}
        <div className="md:col-span-1 bg-white border border-slate-200 rounded-lg shadow-sm overflow-hidden flex flex-col">
          <div className="p-4 border-b border-slate-100 bg-slate-50 font-semibold text-slate-700 flex justify-between items-center">
            Recent Runs
          </div>
          <div className="flex-1 overflow-y-auto max-h-[600px] p-2 space-y-2">
            {isLoading ? (
              <div className="p-4 text-center text-slate-400 text-sm">Loading runs...</div>
            ) : runs.length === 0 ? (
              <div className="p-4 text-center text-slate-400 text-sm">No payroll runs found.</div>
            ) : (
              runs.sort((a,b) => b.id - a.id).map(run => (
                <div 
                  key={run.id}
                  onClick={() => setSelectedRun(run)}
                  className={`p-3 rounded border cursor-pointer transition ${
                    selectedRun?.id === run.id ? 'border-indigo-500 bg-indigo-50/50' : 'border-slate-100 hover:border-indigo-300 hover:bg-slate-50'
                  }`}
                >
                  <div className="flex justify-between items-start mb-1">
                    <div className="font-bold text-sm text-slate-800">#{run.id} - {dayjs(run.periodStart).format('MMM YYYY')}</div>
                    {run.status === 'DRAFT' && <span className="px-2 py-0.5 rounded text-[10px] font-bold bg-slate-100 text-slate-600 border border-slate-200">DRAFT</span>}
                    {run.status === 'PROCESSING' && <span className="px-2 py-0.5 rounded text-[10px] font-bold bg-amber-100 text-amber-700 border border-amber-200">PROCESSING</span>}
                    {run.status === 'REVIEW' && <span className="px-2 py-0.5 rounded text-[10px] font-bold bg-blue-100 text-blue-700 border border-blue-200">REVIEW</span>}
                    {run.status === 'COMPLETED' && <span className="px-2 py-0.5 rounded text-[10px] font-bold bg-emerald-100 text-emerald-700 border border-emerald-200">COMPLETED</span>}
                  </div>
                  <div className="text-xs text-slate-500 flex items-center gap-1">
                    <Clock size={12} /> {dayjs(run.periodStart).format('DD MMM')} - {dayjs(run.periodEnd).format('DD MMM YYYY')}
                  </div>
                </div>
              ))
            )}
          </div>
        </div>

        {/* Right Col: Details & Payslips */}
        <div className="md:col-span-2 bg-white border border-slate-200 rounded-lg shadow-sm flex flex-col overflow-hidden">
          {selectedRun ? (
            <>
              <div className="p-4 border-b border-slate-100 bg-slate-50 flex justify-between items-center">
                <div>
                  <h2 className="font-bold text-lg text-slate-800">Run #{selectedRun.id} Details</h2>
                  <p className="text-xs text-slate-500">Period: {dayjs(selectedRun.periodStart).format('DD MMM YYYY')} to {dayjs(selectedRun.periodEnd).format('DD MMM YYYY')}</p>
                </div>
                <div>
                  {selectedRun.status === 'DRAFT' && (
                    <button 
                      onClick={() => processRunMutation.mutate(selectedRun.id)}
                      disabled={processRunMutation.isLoading}
                      className="bg-indigo-600 hover:bg-indigo-700 text-white px-3 py-1.5 rounded text-sm font-semibold flex items-center gap-1 transition"
                    >
                      <Play size={14} /> {processRunMutation.isLoading ? 'Processing...' : 'Process Run'}
                    </button>
                  )}
                  {selectedRun.status === 'REVIEW' && (
                    <button className="bg-emerald-600 hover:bg-emerald-700 text-white px-3 py-1.5 rounded text-sm font-semibold flex items-center gap-1 transition">
                      <CheckCircle size={14} /> Approve & Finalize
                    </button>
                  )}
                </div>
              </div>
              
              <div className="flex-1 p-4 overflow-y-auto bg-slate-50/50">
                <h3 className="font-semibold text-slate-700 mb-3 flex items-center gap-2">
                  <FileText size={16} className="text-indigo-600" /> 
                  Generated Payslips
                </h3>
                
                {isLoadingPayslips ? (
                  <div className="text-center py-8 text-slate-400 text-sm">Loading payslips...</div>
                ) : payslips.length === 0 ? (
                  <div className="text-center py-12 text-slate-400 text-sm bg-white rounded border border-dashed border-slate-200">
                    {selectedRun.status === 'DRAFT' ? 'Process this run to generate payslips.' : 'No payslips generated for this run.'}
                  </div>
                ) : (
                  <div className="space-y-3">
                    {payslips.map(ps => (
                      <div key={ps.id} className="bg-white p-3 rounded-lg border border-slate-200 shadow-sm flex items-center justify-between">
                        <div>
                          <div className="font-bold text-slate-800 text-sm">{ps.employee?.user?.firstName} {ps.employee?.user?.lastName}</div>
                          <div className="text-xs text-slate-500">{ps.employee?.designation} • {ps.employee?.department}</div>
                        </div>
                        <div className="text-right">
                          <div className="font-bold text-emerald-600">₹{ps.netPay?.toLocaleString()}</div>
                          <div className="text-[10px] text-slate-400 uppercase font-semibold mt-0.5 cursor-pointer hover:text-indigo-600 transition">
                            View Breakdown
                          </div>
                        </div>
                      </div>
                    ))}
                  </div>
                )}
              </div>
            </>
          ) : (
            <div className="flex-1 flex flex-col items-center justify-center text-slate-400 p-8">
              <IndianRupee size={48} className="mb-4 opacity-20" />
              <p className="text-sm font-medium">Select a payroll run from the left panel to view details.</p>
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

export default PayrollManagement;
