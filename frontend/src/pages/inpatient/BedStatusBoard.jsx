import React, { useState } from 'react';
import { useQuery } from '@tanstack/react-query';
import { axiosPrivate } from '../../api/axios';
import { BedDouble, CheckCircle, Clock, AlertTriangle, Filter, Search, Plus } from 'lucide-react';
import EmptyState from '../../components/common/EmptyState';

const STATUS_COLORS = {
  AVAILABLE: 'bg-green-100 text-green-700 border-green-200',
  OCCUPIED: 'bg-red-100 text-red-700 border-red-200',
  CLEANING: 'bg-yellow-100 text-yellow-700 border-yellow-200',
  MAINTENANCE: 'bg-slate-100 text-slate-700 border-slate-200',
  RESERVED: 'bg-purple-100 text-purple-700 border-purple-200'
};

const BedStatusBoard = () => {
  const [filterStatus, setFilterStatus] = useState('ALL');
  const [searchTerm, setSearchTerm] = useState('');

  const { data: wards, isLoading: wardsLoading } = useQuery({
    queryKey: ['wards'],
    queryFn: async () => {
      const res = await axiosPrivate.get('/inpatient/wards');
      return res.data;
    }
  });

  const { data: beds, isLoading: bedsLoading } = useQuery({
    queryKey: ['beds', filterStatus],
    queryFn: async () => {
      let url = '/inpatient/beds';
      if (filterStatus !== 'ALL') {
        url += `?status=${filterStatus}`;
      }
      const res = await axiosPrivate.get(url);
      return res.data;
    }
  });

  if (wardsLoading || bedsLoading) {
    return <div className="p-10 flex justify-center text-slate-400">Loading bed status...</div>;
  }

  // Group beds by Ward then by Room
  const bedsByWard = {};
  
  if (beds && wards) {
    // Initialize wards
    wards.forEach(w => {
      bedsByWard[w.id] = { ...w, rooms: {} };
    });

    beds.forEach(bed => {
      const roomId = bed.room.id;
      const wardId = bed.room.ward.id;
      
      if (bedsByWard[wardId]) {
        if (!bedsByWard[wardId].rooms[roomId]) {
          bedsByWard[wardId].rooms[roomId] = { ...bed.room, beds: [] };
        }
        
        // Search filter
        if (searchTerm) {
          const term = searchTerm.toLowerCase();
          if (bed.bedNumber.toLowerCase().includes(term) || 
              bed.room.roomNumber.toLowerCase().includes(term)) {
            bedsByWard[wardId].rooms[roomId].beds.push(bed);
          }
        } else {
          bedsByWard[wardId].rooms[roomId].beds.push(bed);
        }
      }
    });
  }

  return (
    <div className="p-6 max-w-7xl mx-auto space-y-6">
      
      {/* Header */}
      <div className="flex flex-col md:flex-row md:items-center justify-between gap-4">
        <div>
          <h1 className="text-2xl font-bold text-slate-800 flex items-center gap-2">
            <BedDouble className="text-blue-600" />
            Bed Status Board
          </h1>
          <p className="text-slate-500 mt-1">Live overview of ward and bed availability.</p>
        </div>
        
        <div className="flex items-center gap-3">
           {/* Filters */}
          <div className="relative">
            <Search className="absolute left-3 top-1/2 -translate-y-1/2 text-slate-400" size={18} />
            <input 
              type="text"
              placeholder="Search room or bed..."
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              className="pl-9 pr-4 py-2 border border-slate-200 rounded-lg text-sm focus:ring-2 focus:ring-blue-500 outline-none w-48"
            />
          </div>
          <div className="flex items-center gap-2 bg-white border border-slate-200 rounded-lg px-3 py-1.5">
            <Filter className="text-slate-400" size={18} />
            <select 
              value={filterStatus}
              onChange={(e) => setFilterStatus(e.target.value)}
              className="text-sm outline-none bg-transparent"
            >
              <option value="ALL">All Statuses</option>
              <option value="AVAILABLE">Available</option>
              <option value="OCCUPIED">Occupied</option>
              <option value="CLEANING">Cleaning</option>
              <option value="MAINTENANCE">Maintenance</option>
            </select>
          </div>
        </div>
      </div>

      {Object.values(bedsByWard).length === 0 ? (
        <EmptyState 
          icon={BedDouble}
          title="No Wards Configured"
          message="There are no wards or beds set up in the system yet."
        />
      ) : (
        <div className="space-y-8">
          {Object.values(bedsByWard).map(ward => {
            const rooms = Object.values(ward.rooms);
            // Hide empty wards if we are filtering
            if (rooms.length === 0 && (filterStatus !== 'ALL' || searchTerm)) return null;
            
            return (
              <div key={ward.id} className="bg-white rounded-xl shadow-sm border border-slate-200 overflow-hidden">
                <div className="bg-slate-50 px-6 py-4 border-b border-slate-200 flex justify-between items-center">
                  <div>
                    <h2 className="font-bold text-lg text-slate-800">{ward.name}</h2>
                    <p className="text-xs text-slate-500">{ward.wardType.replace(/_/g, ' ')} • Floor {ward.floor}</p>
                  </div>
                </div>
                
                <div className="p-6">
                  {rooms.length === 0 ? (
                    <p className="text-slate-400 text-sm italic">No beds match the current filters in this ward.</p>
                  ) : (
                    <div className="space-y-6">
                      {rooms.map(room => {
                        if (room.beds.length === 0) return null;
                        
                        return (
                          <div key={room.id}>
                            <h4 className="text-sm font-semibold text-slate-700 mb-3 flex items-center gap-2">
                              Room {room.roomNumber}
                              <span className="text-xs font-normal px-2 py-0.5 bg-slate-100 rounded-full text-slate-600">
                                {room.roomType.replace(/_/g, ' ')}
                              </span>
                            </h4>
                            
                            <div className="grid grid-cols-2 sm:grid-cols-3 md:grid-cols-4 lg:grid-cols-6 gap-3">
                              {room.beds.map(bed => (
                                <div 
                                  key={bed.id} 
                                  className={`p-3 rounded-lg border-2 flex flex-col items-center justify-center gap-2 transition-all hover:shadow-md cursor-pointer ${STATUS_COLORS[bed.status] || STATUS_COLORS.MAINTENANCE}`}
                                  title={`Bed: ${bed.bedNumber} - Status: ${bed.status}`}
                                >
                                  <BedDouble size={24} />
                                  <div className="text-center">
                                    <p className="font-bold text-sm">Bed {bed.bedNumber}</p>
                                    <p className="text-[10px] uppercase font-bold tracking-wider opacity-80">{bed.status}</p>
                                  </div>
                                </div>
                              ))}
                            </div>
                          </div>
                        );
                      })}
                    </div>
                  )}
                </div>
              </div>
            );
          })}
        </div>
      )}

    </div>
  );
};

export default BedStatusBoard;
