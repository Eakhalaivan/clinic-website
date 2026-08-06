import { useState, useEffect } from 'react';
import { useAuth } from '../../context/AuthContext';
import { axiosPrivate as axios } from '../../api/axios';
import { Activity, FileText, Stethoscope, Image as ImageIcon, CheckCircle, Clock, AlertCircle } from 'lucide-react';
import { format } from 'date-fns';

export default function HealthTimeline() {
  const [events, setEvents] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const { user } = useAuth();

  useEffect(() => {
    const fetchTimeline = async () => {
      try {
        const response = await axios.get('/api/patient/timeline');
        setEvents(response.data);
      } catch (err) {
        console.error('Error fetching timeline:', err);
        setError('Failed to load health timeline.');
      } finally {
        setLoading(false);
      }
    };
    fetchTimeline();
  }, []);

  const getEventIcon = (type) => {
    switch (type) {
      case 'RADIOLOGY': return <ImageIcon className="w-5 h-5 text-purple-600" />;
      case 'PRESCRIPTION': return <Activity className="w-5 h-5 text-blue-600" />;
      case 'CLINICAL_NOTE': return <Stethoscope className="w-5 h-5 text-emerald-600" />;
      case 'INVOICE': return <FileText className="w-5 h-5 text-amber-600" />;
      default: return <CheckCircle className="w-5 h-5 text-gray-600" />;
    }
  };

  const getEventBgClass = (type) => {
    switch (type) {
      case 'RADIOLOGY': return 'bg-purple-100 ring-purple-50';
      case 'PRESCRIPTION': return 'bg-blue-100 ring-blue-50';
      case 'CLINICAL_NOTE': return 'bg-emerald-100 ring-emerald-50';
      case 'INVOICE': return 'bg-amber-100 ring-amber-50';
      default: return 'bg-gray-100 ring-gray-50';
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
        <h2 className="text-2xl font-semibold text-gray-900">Health Timeline</h2>
      </div>

      <div className="bg-white rounded-xl shadow-sm border border-gray-100 overflow-hidden">
        <div className="p-6 border-b border-gray-100 bg-gradient-to-r from-indigo-50 to-purple-50 flex items-center">
          <div className="p-3 bg-white rounded-lg shadow-sm mr-4 text-indigo-600">
            <Clock className="w-6 h-6" />
          </div>
          <div>
            <h3 className="text-lg font-bold text-gray-900">Your Medical History</h3>
            <p className="text-sm text-gray-600">A chronological view of all your medical events, visits, and reports.</p>
          </div>
        </div>
      </div>

      <div>
        {events.length === 0 ? (
          <div className="bg-white rounded-xl shadow-sm border border-gray-100 p-12 text-center">
            <div className="w-16 h-16 bg-gray-50 text-gray-400 rounded-full flex items-center justify-center mx-auto mb-4">
              <Clock className="w-8 h-8" />
            </div>
            <h3 className="text-lg font-medium text-gray-900 mb-2">No Events Found</h3>
            <p className="text-gray-500 max-w-sm mx-auto">
              Your health timeline is currently empty. Medical records, prescriptions, and reports will appear here automatically.
            </p>
          </div>
        ) : (
          <div className="flow-root mt-8">
            <ul className="-mb-8">
              {events.map((event, eventIdx) => (
                <li key={event.id}>
                  <div className="relative pb-8">
                    {eventIdx !== events.length - 1 ? (
                      <span className="absolute top-4 left-6 -ml-px h-full w-0.5 bg-gray-200" aria-hidden="true" />
                    ) : null}
                    <div className="relative flex space-x-4">
                      <div>
                        <span className={`h-12 w-12 rounded-full flex items-center justify-center ring-8 ${getEventBgClass(event.type)}`}>
                          {getEventIcon(event.type)}
                        </span>
                      </div>
                      <div className="min-w-0 flex-1 pt-1.5 flex justify-between space-x-4">
                        <div className="bg-white border border-gray-100 p-4 rounded-lg shadow-sm flex-1 ml-2 hover:shadow-md transition-shadow">
                          <div className="flex justify-between items-center mb-1">
                            <h4 className="text-sm font-medium text-gray-900">{event.title}</h4>
                            <span className="text-xs text-gray-500">
                              {event.eventDate ? format(new Date(event.eventDate), 'MMM d, yyyy h:mm a') : 'Unknown Date'}
                            </span>
                          </div>
                          <p className="text-sm text-gray-600 mb-2">{event.description}</p>
                          <div className="flex justify-between items-center mt-3">
                            <span className="inline-flex items-center px-2 py-0.5 rounded text-xs font-medium bg-gray-100 text-gray-800">
                              Status: {event.status}
                            </span>
                            <span className="text-xs font-mono text-gray-400 text-right w-full block">
                              Ref: {event.id}
                            </span>
                          </div>
                        </div>
                      </div>
                    </div>
                  </div>
                </li>
              ))}
            </ul>
          </div>
        )}
      </div>
    </div>
  );
}
