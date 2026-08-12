import React, { useState } from 'react';
import { axiosPrivate } from '../../api/axios';
import { useQuery, useQueryClient } from '@tanstack/react-query';

const StatusBadge = ({ status }) => {
  const colors = {
    DRAFT: 'bg-gray-100 text-gray-700', REVIEW: 'bg-yellow-100 text-yellow-700',
    APPROVED: 'bg-blue-100 text-blue-700', SCHEDULED: 'bg-purple-100 text-purple-700',
    ACTIVE: 'bg-green-100 text-green-700', PAUSED: 'bg-orange-100 text-orange-700',
    COMPLETED: 'bg-teal-100 text-teal-700', ARCHIVED: 'bg-gray-100 text-gray-500',
    CANCELLED: 'bg-red-100 text-red-700',
  };
  return <span className={`px-2 py-0.5 rounded-full text-xs font-medium ${colors[status] || 'bg-gray-100'}`}>{status}</span>;
};

export default function MarketingCampaigns() {
  const qc = useQueryClient();
  const [filter, setFilter] = useState('ALL');

  const { data: campaigns = [], isLoading } = useQuery({
    queryKey: ['campaigns'],
    queryFn: async () => (await axiosPrivate.get('/api/marketing/campaigns')).data,
  });

  const [selectedId, setSelectedId] = useState(null);
  const { data: analytics } = useQuery({
    queryKey: ['campaign-analytics', selectedId],
    queryFn: async () => (await axiosPrivate.get(`/api/marketing/campaigns/${selectedId}/analytics`)).data,
    enabled: !!selectedId,
  });

  const handleAction = async (id, action, params = {}) => {
    try {
      await axiosPrivate.post(`/api/marketing/campaigns/${id}/${action}`, null, { params });
      qc.invalidateQueries({ queryKey: ['campaigns'] });
    } catch (e) {
      alert(e?.response?.data?.message || `Failed: ${action}`);
    }
  };

  const statuses = ['ALL', 'DRAFT', 'REVIEW', 'APPROVED', 'ACTIVE', 'PAUSED', 'COMPLETED', 'ARCHIVED'];
  const filtered = filter === 'ALL' ? campaigns : campaigns.filter(c => c.status === filter);

  return (
    <div className="p-6 space-y-4">
      <div className="flex items-center justify-between">
        <h1 className="text-xl font-bold text-gray-900">Campaigns</h1>
        <button className="px-4 py-2 bg-indigo-600 text-white text-sm rounded-lg hover:bg-indigo-700">
          + New Campaign
        </button>
      </div>

      {/* Status filter */}
      <div className="flex gap-2 overflow-x-auto pb-1">
        {statuses.map(s => (
          <button key={s} onClick={() => setFilter(s)}
            className={`px-3 py-1 rounded-full text-xs font-medium whitespace-nowrap ${
              filter === s ? 'bg-indigo-600 text-white' : 'bg-gray-100 text-gray-600 hover:bg-gray-200'
            }`}>
            {s}
          </button>
        ))}
      </div>

      {/* Campaign table */}
      <div className="bg-white rounded-xl border border-gray-100 shadow-sm overflow-hidden">
        {isLoading ? (
          <div className="p-12 text-center text-gray-400">Loading campaigns…</div>
        ) : filtered.length === 0 ? (
          <div className="p-12 text-center text-gray-400">No campaigns matching filter</div>
        ) : (
          <div className="overflow-x-auto">
            <table className="w-full text-sm">
              <thead className="bg-gray-50 text-xs text-gray-500">
                <tr>
                  <th className="px-4 py-3 text-left">Title</th>
                  <th className="px-4 py-3 text-left">Type</th>
                  <th className="px-4 py-3 text-left">Channels</th>
                  <th className="px-4 py-3 text-left">Status</th>
                  <th className="px-4 py-3 text-right">Sent</th>
                  <th className="px-4 py-3 text-left">Actions</th>
                </tr>
              </thead>
              <tbody className="divide-y divide-gray-50">
                {filtered.map(c => (
                  <tr key={c.id} className="hover:bg-gray-50">
                    <td className="px-4 py-3">
                      <button onClick={() => setSelectedId(c.id === selectedId ? null : c.id)}
                        className="font-medium text-indigo-700 hover:underline text-left">
                        {c.title}
                      </button>
                    </td>
                    <td className="px-4 py-3 text-gray-500">{c.campaignType}</td>
                    <td className="px-4 py-3">
                      <div className="flex flex-wrap gap-1">
                        {(c.channels || []).map(ch => (
                          <span key={ch} className="px-1.5 py-0.5 bg-blue-50 text-blue-700 text-xs rounded">{ch}</span>
                        ))}
                      </div>
                    </td>
                    <td className="px-4 py-3"><StatusBadge status={c.status} /></td>
                    <td className="px-4 py-3 text-right text-gray-600">{(c.sentCount || 0).toLocaleString()}</td>
                    <td className="px-4 py-3">
                      <div className="flex gap-1.5">
                        {c.status === 'DRAFT' && <button onClick={() => handleAction(c.id, 'submit')} className="text-xs px-2 py-1 bg-indigo-50 text-indigo-700 rounded">Submit</button>}
                        {c.status === 'REVIEW' && <button onClick={() => handleAction(c.id, 'approve', { approvedBy: 1 })} className="text-xs px-2 py-1 bg-green-50 text-green-700 rounded">Approve</button>}
                        {c.status === 'APPROVED' && <button onClick={() => handleAction(c.id, 'activate')} className="text-xs px-2 py-1 bg-teal-50 text-teal-700 rounded">Activate</button>}
                        {c.status === 'ACTIVE' && <button onClick={() => handleAction(c.id, 'pause')} className="text-xs px-2 py-1 bg-orange-50 text-orange-700 rounded">Pause</button>}
                        {c.status === 'PAUSED' && <button onClick={() => handleAction(c.id, 'resume')} className="text-xs px-2 py-1 bg-teal-50 text-teal-700 rounded">Resume</button>}
                        {(c.status === 'ACTIVE' || c.status === 'PAUSED') && <button onClick={() => handleAction(c.id, 'complete')} className="text-xs px-2 py-1 bg-gray-50 text-gray-700 rounded">Complete</button>}
                        <button onClick={() => handleAction(c.id, 'clone', { clonedBy: 1 })} className="text-xs px-2 py-1 bg-gray-50 text-gray-600 rounded">Clone</button>
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
      </div>

      {/* Analytics panel */}
      {selectedId && analytics && (
        <div className="bg-white rounded-xl border border-gray-100 shadow-sm p-5">
          <h3 className="font-semibold text-gray-800 mb-4 text-sm">Analytics — Campaign #{selectedId}</h3>
          <div className="grid grid-cols-4 md:grid-cols-7 gap-3">
            {['sent', 'delivered', 'opened', 'clicked', 'bounced', 'unsubscribed', 'failed'].map(k => (
              <div key={k} className="text-center">
                <p className="text-lg font-bold text-gray-800">{analytics[k] ?? 0}</p>
                <p className="text-xs text-gray-400 capitalize">{k}</p>
              </div>
            ))}
          </div>
        </div>
      )}
    </div>
  );
}
