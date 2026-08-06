import { useState, useEffect } from 'react';
import { useAuth } from '../../context/AuthContext';
import { axiosPrivate as axios } from '../../api/axios';
import { Shield, FileText, CheckCircle, XCircle, Clock, AlertCircle } from 'lucide-react';
import { format } from 'date-fns';

export default function Insurance() {
  const [preAuths, setPreAuths] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const { user } = useAuth();

  useEffect(() => {
    const fetchInsuranceData = async () => {
      try {
        const response = await axios.get('/api/patient/insurance/pre-auths');
        setPreAuths(response.data);
      } catch (err) {
        console.error('Error fetching insurance data:', err);
        setError('Failed to load insurance data.');
      } finally {
        setLoading(false);
      }
    };
    fetchInsuranceData();
  }, []);

  const getStatusIcon = (status) => {
    switch (status) {
      case 'APPROVED': return <CheckCircle className="w-5 h-5 text-green-500" />;
      case 'REJECTED': return <XCircle className="w-5 h-5 text-red-500" />;
      case 'SUBMITTED': 
      case 'PENDING_INFO': return <Clock className="w-5 h-5 text-yellow-500" />;
      default: return <FileText className="w-5 h-5 text-gray-500" />;
    }
  };

  const getStatusClass = (status) => {
    switch (status) {
      case 'APPROVED': return 'bg-green-50 text-green-700 border-green-200';
      case 'REJECTED': return 'bg-red-50 text-red-700 border-red-200';
      case 'SUBMITTED': 
      case 'PENDING_INFO': return 'bg-yellow-50 text-yellow-700 border-yellow-200';
      default: return 'bg-gray-50 text-gray-700 border-gray-200';
    }
  };

  if (loading) {
    return (
      <div className="flex justify-center items-center h-64">
        <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-indigo-600"></div>
      </div>
    );
  }

  if (error) {
    return (
      <div className="bg-red-50 border-l-4 border-red-500 p-4 rounded-md">
        <div className="flex">
          <div className="flex-shrink-0">
            <AlertCircle className="h-5 w-5 text-red-400" />
          </div>
          <div className="ml-3">
            <p className="text-sm text-red-700">{error}</p>
          </div>
        </div>
      </div>
    );
  }

  return (
    <div className="space-y-6">
      <div className="flex justify-between items-center">
        <h2 className="text-2xl font-semibold text-gray-900">Insurance & Coverage</h2>
      </div>

      <div className="bg-white rounded-xl shadow-sm border border-gray-100 overflow-hidden">
        <div className="p-6 border-b border-gray-100 bg-gradient-to-r from-blue-50 to-indigo-50 flex items-center">
          <div className="p-3 bg-white rounded-lg shadow-sm mr-4 text-indigo-600">
            <Shield className="w-6 h-6" />
          </div>
          <div>
            <h3 className="text-lg font-bold text-gray-900">Coverage Status</h3>
            <p className="text-sm text-gray-600">Manage your active policies and pre-authorization requests.</p>
          </div>
        </div>
      </div>

      <div>
        <h3 className="text-lg font-medium text-gray-900 mb-4">Pre-Authorization Requests</h3>
        
        {preAuths.length === 0 ? (
          <div className="bg-white rounded-xl shadow-sm border border-gray-100 p-12 text-center">
            <div className="w-16 h-16 bg-gray-50 text-gray-400 rounded-full flex items-center justify-center mx-auto mb-4">
              <FileText className="w-8 h-8" />
            </div>
            <h3 className="text-lg font-medium text-gray-900 mb-2">No Requests Found</h3>
            <p className="text-gray-500 max-w-sm mx-auto">
              You do not have any insurance pre-authorization requests or claims on file.
            </p>
          </div>
        ) : (
          <div className="space-y-4">
            {preAuths.map((auth) => (
              <div key={auth.id} className="bg-white rounded-xl shadow-sm border border-gray-100 p-5 hover:shadow-md transition-shadow">
                <div className="flex flex-col sm:flex-row sm:justify-between sm:items-start gap-4">
                  <div>
                    <div className="flex items-center gap-2 mb-1">
                      <h4 className="text-lg font-semibold text-gray-900">{auth.procedureName}</h4>
                      <span className={`inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium border ${getStatusClass(auth.status)}`}>
                        {auth.status}
                      </span>
                    </div>
                    <p className="text-sm text-gray-500 mb-2">
                      Provider: <span className="font-medium text-gray-700">{auth.providerName}</span> | Policy: <span className="font-medium text-gray-700">{auth.policyNumber || 'N/A'}</span>
                    </p>
                    {auth.denialReason && (
                      <p className="text-sm text-red-600 bg-red-50 p-2 rounded border border-red-100 mt-2">
                        Reason: {auth.denialReason}
                      </p>
                    )}
                  </div>
                  <div className="flex flex-col sm:items-end text-sm">
                    <div className="flex items-center text-gray-500 mb-1">
                      <span className="mr-2">Submitted:</span>
                      <span className="font-medium text-gray-900">
                        {auth.submittedAt ? format(new Date(auth.submittedAt), 'MMM d, yyyy') : 'Unknown'}
                      </span>
                    </div>
                    <div className="flex items-center text-gray-500 mb-2">
                      <span className="mr-2">Est. Cost:</span>
                      <span className="font-medium text-gray-900">${auth.estimatedCost?.toFixed(2)}</span>
                    </div>
                    {auth.approvedAmount > 0 && (
                      <div className="text-green-600 font-medium">
                        Approved: ${auth.approvedAmount?.toFixed(2)}
                      </div>
                    )}
                  </div>
                </div>
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  );
}
