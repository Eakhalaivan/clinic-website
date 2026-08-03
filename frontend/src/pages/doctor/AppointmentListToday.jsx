import React, { useState } from 'react';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { useNavigate } from 'react-router-dom';
import { axiosPrivate } from '../../api/axios';
import {
  CheckCircle2, Clock, XCircle, Play, LogIn, Users,
  CalendarCheck, AlertCircle, ChevronRight, Loader2
} from 'lucide-react';

// ── helpers ──────────────────────────────────────────────────────────────────
const fmt = (iso) => {
  if (!iso) return '—';
  return new Date(iso).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });
};

const STATUS_META = {
  BOOKED:     { label: 'Booked',      bg: 'bg-blue-50 dark:bg-blue-500/10', color: 'text-blue-700 dark:text-blue-400' },
  CHECKED_IN: { label: 'Checked In',  bg: 'bg-orange-50 dark:bg-orange-500/10', color: 'text-orange-700 dark:text-orange-400' },
  IN_PROGRESS:{ label: 'In Progress', bg: 'bg-yellow-50 dark:bg-yellow-500/10', color: 'text-yellow-700 dark:text-yellow-400' },
  COMPLETED:  { label: 'Completed',   bg: 'bg-green-50 dark:bg-green-500/10', color: 'text-green-700 dark:text-green-400' },
  CANCELLED:  { label: 'Cancelled',   bg: 'bg-slate-100 dark:bg-slate-700', color: 'text-slate-600 dark:text-slate-300' },
  NO_SHOW:    { label: 'No Show',     bg: 'bg-red-50 dark:bg-red-500/10', color: 'text-red-700 dark:text-red-400' },
};

const StatusBadge = ({ status }) => {
  const meta = STATUS_META[status] || { label: status, bg: 'bg-slate-100 dark:bg-slate-700', color: 'text-slate-600 dark:text-slate-300' };
  return (
    <span className={`px-2.5 py-1 rounded-full text-[0.72rem] font-bold tracking-wide ${meta.bg} ${meta.color}`}>
      {meta.label}
    </span>
  );
};

// ── confirm dialog ────────────────────────────────────────────────────────────
const ConfirmDialog = ({ message, onConfirm, onCancel }) => (
  <div className="fixed inset-0 bg-black/40 flex items-center justify-center z-[1000]">
    <div className="bg-white dark:bg-slate-800 rounded-xl p-7 max-w-[380px] w-[90%] shadow-2xl">
      <div className="flex items-center gap-2.5 mb-3">
        <AlertCircle size={20} className="text-orange-600 dark:text-orange-500" />
        <h3 className="m-0 text-base font-bold text-slate-900 dark:text-white">Confirm Action</h3>
      </div>
      <p className="mt-0 mb-5 text-sm text-slate-600 dark:text-slate-300">{message}</p>
      <div className="flex justify-end gap-2.5">
        <button onClick={onCancel} className="px-4 py-1.5 rounded-md border border-slate-200 dark:border-slate-600 bg-white dark:bg-slate-700 text-slate-600 dark:text-slate-200 font-semibold text-sm hover:bg-slate-50 dark:hover:bg-slate-600 transition-colors cursor-pointer">
          Cancel
        </button>
        <button onClick={onConfirm} className="px-4 py-1.5 rounded-md border-none bg-orange-600 hover:bg-orange-700 text-white font-semibold text-sm transition-colors cursor-pointer">
          Confirm
        </button>
      </div>
    </div>
  </div>
);

// ── main component ────────────────────────────────────────────────────────────
const AppointmentListToday = () => {
  const queryClient = useQueryClient();
  const navigate = useNavigate();
  const [confirm, setConfirm] = useState(null); // { id, status, message, patientId }

  const { data: appointments = [], isLoading, error } = useQuery({
    queryKey: ['doctor-today-appointments'],
    queryFn: async () => {
      const start = new Date(); start.setHours(0,0,0,0);
      const end = new Date(); end.setHours(23,59,59,999);
      return (await axiosPrivate.get(`/appointments/today?start=${start.toISOString()}&end=${end.toISOString()}`)).data;
    },
    refetchInterval: 30_000,
  });

  const updateStatus = useMutation({
    mutationFn: async ({ id, status, patientId }) =>
      axiosPrivate.patch(`/appointments/${id}/status?status=${status}`),
    onSuccess: (data, variables) => {
      // Invalidate this list, the dashboard counts, and the calendar
      queryClient.invalidateQueries({ queryKey: ['doctor-today-appointments'] });
      queryClient.invalidateQueries({ queryKey: ['doctorAppointments'] });
      queryClient.invalidateQueries({ queryKey: ['doctor-appointments'] });

      if (variables.status === 'COMPLETED' && variables.patientId) {
        navigate(`/doctor/patients/${variables.patientId}`);
      }
    },
  });

  const doUpdate = (id, status, message, patientId = null) => {
    if (message) {
      setConfirm({ id, status, message, patientId });
    } else {
      updateStatus.mutate({ id, status, patientId });
    }
  };

  // Sort chronologically by startTime
  const sorted = [...appointments].sort(
    (a, b) => new Date(a.startTime) - new Date(b.startTime)
  );

  const counts = {
    total: sorted.length,
    checkedIn: sorted.filter(a => a.status === 'CHECKED_IN').length,
    completed: sorted.filter(a => a.status === 'COMPLETED').length,
  };

  return (
    <div className="p-6 max-w-[1100px] mx-auto">
      {confirm && (
        <ConfirmDialog
          message={confirm.message}
          onConfirm={() => {
            updateStatus.mutate({ id: confirm.id, status: confirm.status, patientId: confirm.patientId });
            setConfirm(null);
          }}
          onCancel={() => setConfirm(null)}
        />
      )}

      <h1 className="text-2xl font-bold text-slate-900 dark:text-white mb-5">
        Today's Appointments
      </h1>

      {/* Summary Cards — computed from the same live data as the table */}
      <div className="grid grid-cols-1 md:grid-cols-3 gap-4 mb-7">
        {[
          { label: 'Total Today',  value: counts.total,     icon: CalendarCheck, bg: 'bg-blue-50 dark:bg-blue-500/10', color: 'text-blue-700 dark:text-blue-400' },
          { label: 'Checked In',  value: counts.checkedIn,  icon: LogIn,         bg: 'bg-orange-50 dark:bg-orange-500/10', color: 'text-orange-700 dark:text-orange-400' },
          { label: 'Completed',   value: counts.completed,  icon: CheckCircle2,  bg: 'bg-green-50 dark:bg-green-500/10', color: 'text-green-700 dark:text-green-400' },
        ].map(({ label, value, icon: Icon, bg, color }) => (
          <div key={label} className="bg-white dark:bg-[#1A263E] p-4 sm:px-5 rounded-xl border border-slate-200 dark:border-slate-700 flex items-center gap-3.5 shadow-sm">
            <div className={`p-2.5 rounded-lg ${bg}`}>
              <Icon size={20} className={color} />
            </div>
            <div>
              <p className="m-0 text-[0.78rem] text-slate-500 dark:text-slate-400 font-semibold">{label}</p>
              <h3 className={`m-0 mt-0.5 text-3xl font-extrabold ${color}`}>{value}</h3>
            </div>
          </div>
        ))}
      </div>

      {/* Table */}
      <div className="bg-white dark:bg-[#1A263E] rounded-xl border border-slate-200 dark:border-slate-700 overflow-hidden">
        {isLoading ? (
          <div className="p-12 text-center text-slate-400 flex justify-center items-center gap-2">
            <Loader2 size={18} className="animate-spin" /> Loading appointments…
          </div>
        ) : error ? (
          <div className="p-10 text-center text-red-600 dark:text-red-400">
            Failed to load appointments. Please refresh.
          </div>
        ) : sorted.length === 0 ? (
          <div className="p-14 text-center flex flex-col items-center">
            <CalendarCheck size={40} className="text-slate-300 dark:text-slate-600 mb-3" />
            <p className="text-slate-400 dark:text-slate-500 font-medium m-0">No appointments scheduled for today</p>
          </div>
        ) : (
          <div className="overflow-x-auto">
            <table className="w-full text-left border-collapse">
              <thead className="bg-slate-50 dark:bg-slate-800/50 border-b border-slate-200 dark:border-slate-700">
                <tr>
                  {['#', 'Time', 'Patient', 'Reason', 'Status', 'Actions'].map(h => (
                    <th key={h} className="py-3 px-4 text-[0.78rem] font-bold text-slate-500 dark:text-slate-400 uppercase tracking-wide">
                      {h}
                    </th>
                  ))}
                </tr>
              </thead>
              <tbody className="divide-y divide-slate-100 dark:divide-slate-700/50">
                {sorted.map((a, i) => (
                  <tr key={a.id} className="hover:bg-slate-50 dark:hover:bg-slate-800/30 transition-colors">
                    <td className="py-3.5 px-4 text-[0.78rem] font-semibold text-slate-400">{i + 1}</td>
                    <td className="py-3.5 px-4 text-sm font-bold text-slate-900 dark:text-white whitespace-nowrap">
                      {fmt(a.startTime)}
                      {a.endTime && <span className="font-normal text-slate-400"> – {fmt(a.endTime)}</span>}
                    </td>
                    <td className="py-3.5 px-4">
                      <div className="flex items-center gap-2.5">
                        <div className="w-8 h-8 rounded-full bg-blue-50 dark:bg-blue-500/10 flex items-center justify-center shrink-0">
                          <Users size={14} className="text-blue-600 dark:text-blue-400" />
                        </div>
                        <div>
                          <div className="text-sm font-semibold text-slate-900 dark:text-white">
                            {a.patientFirstName && a.patientLastName
                              ? `${a.patientFirstName} ${a.patientLastName}`
                              : a.patientFirstName || a.patientLastName || 'Unknown Patient'}
                          </div>
                          {a.reasonForVisit && (
                            <div className="text-[0.72rem] text-slate-400 mt-px">
                              {a.reasonForVisit.length > 40 ? a.reasonForVisit.slice(0, 40) + '…' : a.reasonForVisit}
                            </div>
                          )}
                        </div>
                      </div>
                    </td>
                    <td className="py-3.5 px-4 text-[0.8rem] text-slate-500 dark:text-slate-400">Consultation</td>
                    <td className="py-3.5 px-4"><StatusBadge status={a.status} /></td>
                    <td className="py-3.5 px-4">
                      <div className="flex gap-1.5 flex-wrap">
                        {/* BOOKED → Check In */}
                        {a.status === 'BOOKED' && (
                          <>
                            <ActionBtn
                              icon={LogIn} label="Check In" color="text-blue-700 dark:text-blue-400" bg="bg-blue-50 dark:bg-blue-500/10"
                              loading={updateStatus.isPending}
                              onClick={() => doUpdate(a.id, 'CHECKED_IN', null)}
                            />
                            <ActionBtn
                              icon={XCircle} label="Cancel" color="text-slate-600 dark:text-slate-300" bg="bg-slate-100 dark:bg-slate-700"
                              loading={updateStatus.isPending}
                              onClick={() => doUpdate(a.id, 'CANCELLED', 'Cancel this appointment? This cannot be undone.')}
                            />
                          </>
                        )}

                        {/* CHECKED_IN → Start / Complete */}
                        {a.status === 'CHECKED_IN' && (
                          <ActionBtn
                            icon={Play} label="Start" color="text-yellow-700 dark:text-yellow-400" bg="bg-yellow-50 dark:bg-yellow-500/10"
                            loading={updateStatus.isPending}
                            onClick={() => doUpdate(a.id, 'IN_PROGRESS', null, a.patientId)}
                          />
                        )}

                        {/* IN_PROGRESS → Complete */}
                        {a.status === 'IN_PROGRESS' && (
                          <ActionBtn
                            icon={CheckCircle2} label="Complete" color="text-green-700 dark:text-green-400" bg="bg-green-50 dark:bg-green-500/10"
                            loading={updateStatus.isPending}
                            onClick={() => doUpdate(a.id, 'COMPLETED', null, a.patientId)}
                          />
                        )}

                        {/* COMPLETED / CANCELLED — no actions */}
                        {(a.status === 'COMPLETED' || a.status === 'CANCELLED') && (
                          <span className="text-slate-300 dark:text-slate-600 text-[0.78rem]">—</span>
                        )}
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
      </div>
    </div>
  );
};

// ── small reusable action button ──────────────────────────────────────────────
const ActionBtn = ({ icon: Icon, label, color, bg, onClick, loading }) => (
  <button
    onClick={onClick}
    disabled={loading}
    className={`flex items-center gap-1 px-2.5 py-1.5 rounded-md text-xs font-bold transition-opacity border-none ${bg} ${color} ${loading ? 'opacity-60 cursor-not-allowed' : 'hover:opacity-80 cursor-pointer'}`}
  >
    <Icon size={12} />
    {label}
  </button>
);

export default AppointmentListToday;
