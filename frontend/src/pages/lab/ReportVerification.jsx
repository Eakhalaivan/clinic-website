import React, { useState } from 'react';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { axiosPrivate } from '../../api/axios';

const ReportVerification = () => {
  const queryClient = useQueryClient();
  const [selectedRequest, setSelectedRequest] = useState(null);
  const [comments, setComments] = useState('');

  // Fetch only requests pending verification
  const { data: requests, isLoading } = useQuery({
    queryKey: ['lab-requests-verification'],
    queryFn: async () => {
      const res = await axiosPrivate.get('/lab/requests/status/PENDING_VERIFICATION');
      return res.data;
    }
  });

  const verifyMutation = useMutation({
    mutationFn: async ({ requestId, payload }) => {
      const res = await axiosPrivate.post(`/lab/requests/${requestId}/verify`, payload);
      return res.data;
    },
    onSuccess: () => {
      queryClient.invalidateQueries(['lab-requests-verification']);
      setSelectedRequest(null);
      setComments('');
      alert('Report verified successfully!');
    },
    onError: (error) => {
      alert('Error verifying report: ' + error.message);
    }
  });

  const handleVerify = () => {
    if (!selectedRequest) return;
    verifyMutation.mutate({
      requestId: selectedRequest.id,
      payload: { comments }
    });
  };

  const handleDownloadPdf = async (requestId) => {
    try {
      const res = await axiosPrivate.get(`/lab/requests/${requestId}/report/pdf`, {
        responseType: 'blob'
      });
      const url = window.URL.createObjectURL(new Blob([res.data]));
      const link = document.createElement('a');
      link.href = url;
      link.setAttribute('download', `LabReport_${requestId}.pdf`);
      document.body.appendChild(link);
      link.click();
    } catch (error) {
      console.error('Failed to download PDF', error);
      alert('Failed to download PDF');
    }
  };

  if (isLoading) return <div>Loading verification worklist...</div>;

  return (
    <div className="p-6">
      <h1 className="text-2xl font-bold mb-6">Report Verification</h1>

      <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
        <div className="md:col-span-1 border rounded shadow bg-white p-4 h-screen overflow-y-auto">
          <h2 className="text-lg font-semibold mb-4">Pending Verification</h2>
          {(!requests || requests.length === 0) ? (
            <p className="text-gray-500">No reports pending verification.</p>
          ) : (
            <ul className="space-y-4">
              {requests.map(req => (
                <li 
                  key={req.id}
                  className={`p-3 rounded border cursor-pointer ${selectedRequest?.id === req.id ? 'bg-indigo-50 border-indigo-500' : 'bg-gray-50 hover:bg-gray-100'}`}
                  onClick={() => setSelectedRequest(req)}
                >
                  <div className="font-semibold text-indigo-600">{req.testCatalog?.testName}</div>
                  <div className="text-sm">Patient ID: {req.patient?.id}</div>
                  <div className="text-sm text-gray-500">Req #: {req.labRequestNumber}</div>
                </li>
              ))}
            </ul>
          )}
        </div>

        <div className="md:col-span-2 border rounded shadow bg-white p-6">
          {selectedRequest ? (
            <div>
              <h2 className="text-xl font-bold mb-4">Verify Report: {selectedRequest.testCatalog?.testName}</h2>
              <div className="grid grid-cols-2 gap-4 mb-6">
                <div><strong>Patient ID:</strong> {selectedRequest.patient?.id}</div>
                <div><strong>Request Number:</strong> {selectedRequest.labRequestNumber}</div>
                <div><strong>Status:</strong> {selectedRequest.status}</div>
                <div><strong>Priority:</strong> {selectedRequest.priority}</div>
              </div>

              <div className="mb-6">
                <button
                  onClick={() => handleDownloadPdf(selectedRequest.id)}
                  className="bg-gray-600 text-white px-4 py-2 rounded hover:bg-gray-700"
                >
                  Preview PDF
                </button>
              </div>

              <div className="mb-6">
                <label className="block text-sm font-medium text-gray-700 mb-2">Pathologist Comments (Optional)</label>
                <textarea
                  className="w-full border-gray-300 rounded shadow-sm focus:ring-indigo-500 focus:border-indigo-500"
                  rows="4"
                  value={comments}
                  onChange={(e) => setComments(e.target.value)}
                  placeholder="Enter any comments for the final report..."
                ></textarea>
              </div>

              <div className="flex justify-end">
                <button
                  onClick={() => setSelectedRequest(null)}
                  className="bg-white border border-gray-300 text-gray-700 px-4 py-2 rounded mr-4 hover:bg-gray-50"
                >
                  Cancel
                </button>
                <button
                  onClick={handleVerify}
                  disabled={verifyMutation.isLoading}
                  className="bg-indigo-600 text-white px-6 py-2 rounded hover:bg-indigo-700"
                >
                  {verifyMutation.isLoading ? 'Verifying...' : 'Sign & Verify Report'}
                </button>
              </div>
            </div>
          ) : (
            <div className="flex h-full items-center justify-center text-gray-400">
              Select a report from the list to review and verify.
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

export default ReportVerification;
