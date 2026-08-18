import React, { useState } from 'react';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { axiosPrivate } from '../../api/axios';
import { format, formatDistanceToNow, isPast } from 'date-fns';

const ResultEntryModal = ({ request, onClose, onSuccess }) => {
  const [resultValue, setResultValue] = useState('');
  const [isCritical, setIsCritical] = useState(false);
  const [isAbnormal, setIsAbnormal] = useState(false);
  const [referenceRange, setReferenceRange] = useState(request.testCatalog?.referenceRange || '');
  const [unit, setUnit] = useState(request.testCatalog?.unit || '');
  const [file, setFile] = useState(null);
  
  // Live validation calculation
  let liveAbnormal = false;
  let liveCritical = false;
  
  if (resultValue && referenceRange) {
    const val = parseFloat(resultValue);
    if (!isNaN(val)) {
      const rangeMatch = referenceRange.match(/^([0-9.]+)\s*-\s*([0-9.]+)$/);
      if (rangeMatch) {
        const min = parseFloat(rangeMatch[1]);
        const max = parseFloat(rangeMatch[2]);
        if (val < min || val > max) {
          liveAbnormal = true;
          const critDev = (max - min) * 0.2;
          if (val < (min - critDev) || val > (max + critDev)) {
            liveCritical = true;
          }
        }
      } else if (referenceRange.trim().startsWith('<')) {
        const max = parseFloat(referenceRange.replace('<', '').trim());
        if (val >= max) {
          liveAbnormal = true;
          if (val >= max * 1.2) liveCritical = true;
        }
      } else if (referenceRange.trim().startsWith('>')) {
        const min = parseFloat(referenceRange.replace('>', '').trim());
        if (val <= min) {
          liveAbnormal = true;
          if (val <= min * 0.8) liveCritical = true;
        }
      }
    }
  }

  const queryClient = useQueryClient();

  const submitMutation = useMutation({
    mutationFn: async (formData) => {
      const res = await axiosPrivate.post(`/lab/requests/${request.id}/result`, formData, {
        headers: { 'Content-Type': 'multipart/form-data' }
      });
      return res.data;
    },
    onSuccess: () => {
      queryClient.invalidateQueries(['lab-requests']);
      onSuccess();
    }
  });

  const handleSubmit = (e, isDraft = false) => {
    e.preventDefault();
    const formData = new FormData();
    const resultObj = {
      resultValue,
      isCritical: liveCritical,
      isAbnormal: liveAbnormal,
      referenceRange,
      unit,
      isDraft
    };
    formData.append('result', new Blob([JSON.stringify(resultObj)], { type: 'application/json' }));
    if (file) {
      formData.append('file', file);
    }
    submitMutation.mutate(formData);
  };

  return (
    <div className="fixed inset-0 bg-gray-500 bg-opacity-75 flex items-center justify-center z-50">
      <div className="bg-white rounded-lg p-6 max-w-lg w-full">
        <h2 className="text-xl font-bold mb-4">Result Entry - {request.testCatalog?.testName}</h2>
        <form onSubmit={handleSubmit} className="space-y-4">
          <div>
            <label className="block text-sm font-medium text-gray-700">Result Value</label>
            <input type="text" value={resultValue} onChange={e => setResultValue(e.target.value)} required className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-indigo-500" />
          </div>
          <div className="grid grid-cols-2 gap-4">
            <div>
              <label className="block text-sm font-medium text-gray-700">Reference Range</label>
              <input type="text" value={referenceRange} onChange={e => setReferenceRange(e.target.value)} className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-indigo-500" />
            </div>
            <div>
              <label className="block text-sm font-medium text-gray-700">Unit</label>
              <input type="text" value={unit} onChange={e => setUnit(e.target.value)} className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-indigo-500" />
            </div>
          </div>
          <div className="flex items-center space-x-4 p-3 bg-slate-50 rounded-lg border border-slate-200">
            <div className="flex-1 flex items-center space-x-2">
              <span className="text-sm font-medium text-slate-700">Live Status:</span>
              {liveCritical ? (
                <span className="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium bg-red-100 text-red-800">
                  CRITICAL
                </span>
              ) : liveAbnormal ? (
                <span className="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium bg-orange-100 text-orange-800">
                  ABNORMAL
                </span>
              ) : resultValue ? (
                <span className="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium bg-green-100 text-green-800">
                  NORMAL
                </span>
              ) : (
                <span className="text-xs text-slate-500">Enter result to evaluate</span>
              )}
            </div>
            {(liveAbnormal || liveCritical) && (
              <p className="text-xs text-red-600 font-medium">
                Values outside reference range. A critical result will trigger an immediate alert.
              </p>
            )}
          </div>
          <div>
             <label className="block text-sm font-medium text-gray-700">Upload PDF Report (Optional)</label>
             <input type="file" accept="application/pdf" onChange={e => setFile(e.target.files[0])} className="mt-1 block w-full text-sm text-gray-500 file:mr-4 file:py-2 file:px-4 file:rounded-md file:border-0 file:text-sm file:font-semibold file:bg-indigo-50 file:text-indigo-700 hover:file:bg-indigo-100"/>
          </div>
          <div className="flex justify-end space-x-3 mt-6">
            <button type="button" onClick={onClose} className="px-4 py-2 border border-gray-300 rounded-md shadow-sm text-sm font-medium text-gray-700 bg-white hover:bg-gray-50">Cancel</button>
            <button type="button" onClick={(e) => handleSubmit(e, true)} disabled={submitMutation.isLoading} className="px-4 py-2 border border-gray-300 rounded-md shadow-sm text-sm font-medium text-gray-700 bg-white hover:bg-gray-50">Save Draft</button>
            <button type="button" onClick={(e) => handleSubmit(e, false)} disabled={submitMutation.isLoading} className="px-4 py-2 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-indigo-600 hover:bg-indigo-700">Submit Result</button>
          </div>
        </form>
      </div>
    </div>
  );
};

const LabWorklist = () => {
  const [filterStatus, setFilterStatus] = useState('ALL');
  const [search, setSearch] = useState('');
  const queryClient = useQueryClient();
  const [selectedRequest, setSelectedRequest] = useState(null);

  const { data = {}, isLoading, isError } = useQuery({
    queryKey: ['lab-requests', filterStatus, search],
    queryFn: async () => {
      const params = {};
      if (filterStatus !== 'ALL') params.status = filterStatus;
      if (search) params.search = search;
      params.size = 100; // Fetch up to 100 for now to keep it simple
      const res = await axiosPrivate.get('/lab/worklist', { params });
      return res.data;
    },
    refetchInterval: 30000
  });

  const requests = data.content || [];

  const updateStatusMutation = useMutation({
    mutationFn: async ({ id, status }) => {
      const res = await axiosPrivate.put(`/lab/requests/${id}/status?status=${status}`);
      return res.data;
    },
    onSuccess: () => {
      queryClient.invalidateQueries(['lab-requests']);
    }
  });

  const generateBarcodeMutation = useMutation({
    mutationFn: async (requestId) => {
      const res = await axiosPrivate.post(`/lab/requests/generate-barcodes`, [requestId]);
      return res.data;
    },
    onSuccess: () => {
      queryClient.invalidateQueries(['lab-requests']);
      alert('Barcode generated successfully!');
    }
  });

  // The backend already filters based on status and search via Specification
  const filteredRequests = requests;

  if (isLoading) {
    return (
      <div className="flex justify-center items-center h-64">
        <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-indigo-600"></div>
      </div>
    );
  }

  if (isError) {
    return <div className="p-4 bg-red-50 text-red-600 rounded-md">Error loading worklist.</div>;
  }

  return (
    <div className="space-y-6">
      <div className="flex justify-between items-end mb-6">
        <div>
          <h1 className="text-2xl font-bold text-gray-900 mb-1">Lab Worklist</h1>
          <p className="text-sm font-medium text-gray-500">Manage lab test queue</p>
        </div>
      </div>

      <div className="bg-white p-4 rounded-lg shadow-sm border border-gray-200 flex flex-col sm:flex-row justify-between items-center gap-4">
        <div className="flex space-x-2 w-full sm:w-auto overflow-x-auto">
          {['ALL', 'ORDERED', 'COLLECTED', 'RECEIVED', 'IN_PROGRESS', 'PENDING_VERIFICATION'].map(s => (
             <button key={s} onClick={() => setFilterStatus(s)} className={`px-4 py-2 rounded-md text-sm font-medium whitespace-nowrap ${filterStatus === s ? 'bg-indigo-100 text-indigo-700' : 'text-gray-600 hover:bg-gray-100'}`}>
               {s.replace('_', ' ')}
             </button>
          ))}
        </div>
        <input type="text" placeholder="Search patient or ID..." value={search} onChange={e => setSearch(e.target.value)} className="w-full sm:w-64 rounded-md border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-indigo-500" />
      </div>

      <div className="bg-white shadow overflow-hidden sm:rounded-md">
        {filteredRequests.length === 0 ? (
           <div className="p-6 text-center text-gray-500">No requests found.</div>
        ) : (
          <ul className="divide-y divide-gray-200">
            {filteredRequests.map(req => {
              // Calculate overdue
              const targetHours = req.testCatalog?.turnaroundTargetHours || 24;
              const dueTime = new Date(req.requestedAt).getTime() + (targetHours * 60 * 60 * 1000);
              const isOverdue = isPast(new Date(dueTime)) && req.status !== 'RELEASED' && req.status !== 'VERIFIED';
              
              return (
                <li key={req.id} className="p-4 hover:bg-gray-50 flex items-center justify-between">
                  <div className="flex-1">
                    <div className="flex items-center space-x-3">
                      <span className={`px-2 inline-flex text-xs leading-5 font-semibold rounded-full ${req.priority === 'STAT' ? 'bg-red-100 text-red-800' : req.priority === 'URGENT' ? 'bg-yellow-100 text-yellow-800' : 'bg-green-100 text-green-800'}`}>
                        {req.priority}
                      </span>
                      <p className="text-sm font-medium text-indigo-600 truncate">{req.testCatalog?.testName}</p>
                      <span className="text-sm text-gray-500">{req.labRequestNumber}</span>
                      {isOverdue && <span className="text-xs font-bold text-red-600 animate-pulse">OVERDUE</span>}
                    </div>
                    <div className="mt-2 flex items-center text-sm text-gray-500 space-x-4">
                      <span>Patient: {req.patient?.user?.firstName} {req.patient?.user?.lastName}</span>
                      <span>Requested: {format(new Date(req.requestedAt), 'PPp')}</span>
                      <span>Status: <strong className="uppercase">{req.status.replace('_', ' ')}</strong></span>
                    </div>
                  </div>
                  <div className="flex items-center space-x-2">
                     {req.status === 'ORDERED' && (
                       <div className="flex space-x-2">
                         {!req.sampleBarcodeId && (
                           <button onClick={() => generateBarcodeMutation.mutate(req.id)} className="px-3 py-1 bg-indigo-100 text-indigo-700 text-sm rounded hover:bg-indigo-200">Generate Barcode</button>
                         )}
                         <button onClick={() => updateStatusMutation.mutate({ id: req.id, status: 'COLLECTED' })} className="px-3 py-1 bg-blue-600 text-white text-sm rounded hover:bg-blue-700">Mark Collected</button>
                       </div>
                     )}
                     {req.status === 'COLLECTED' && (
                       <div className="flex space-x-2">
                         {req.sampleBarcodeId && (
                           <button onClick={() => window.print()} className="px-3 py-1 border border-slate-300 text-slate-700 text-sm rounded hover:bg-slate-50">Print {req.sampleBarcodeId}</button>
                         )}
                         <button onClick={() => updateStatusMutation.mutate({ id: req.id, status: 'RECEIVED' })} className="px-3 py-1 bg-blue-600 text-white text-sm rounded hover:bg-blue-700">Receive Sample</button>
                       </div>
                     )}
                     {req.status === 'RECEIVED' && (
                       <button onClick={() => updateStatusMutation.mutate({ id: req.id, status: 'IN_PROGRESS' })} className="px-3 py-1 bg-yellow-600 text-white text-sm rounded hover:bg-yellow-700">Start Processing</button>
                     )}
                     {req.status === 'IN_PROGRESS' && (
                       <button onClick={() => setSelectedRequest(req)} className="px-3 py-1 bg-indigo-600 text-white text-sm rounded hover:bg-indigo-700">Enter Results</button>
                     )}
                  </div>
                </li>
              );
            })}
          </ul>
        )}
      </div>
      
      {selectedRequest && (
        <ResultEntryModal request={selectedRequest} onClose={() => setSelectedRequest(null)} onSuccess={() => setSelectedRequest(null)} />
      )}
    </div>
  );
};

export default LabWorklist;
