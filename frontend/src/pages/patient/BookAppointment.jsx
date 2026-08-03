import React, { useState, useMemo } from 'react';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { useParams, useNavigate } from 'react-router-dom';
import { axiosPrivate } from '../../api/axios';
import useAuthStore from '../../store/authStore';
import { Calendar, Clock, ArrowRight } from 'lucide-react';
import PageHeader from '../doctor/PageHeader';
import Card from '../../components/ui/Card';
import Button from '../../components/ui/Button';

const DAYS = ['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat'];

const BookAppointment = () => {
    const { doctorId } = useParams();
    const navigate = useNavigate();
    const queryClient = useQueryClient();
    const { user } = useAuthStore();
    
    const [selectedDoctorId, setSelectedDoctorId] = useState(doctorId || '');
    const [selectedDate, setSelectedDate] = useState(() => {
        const today = new Date();
        return new Date(today.getFullYear(), today.getMonth(), today.getDate());
    });
    const [selectedSlotId, setSelectedSlotId] = useState('');
    const [reason, setReason] = useState('');
    const [error, setError] = useState('');

    // Fetch list of doctors
    const { data: doctors = [], isLoading: doctorsLoading } = useQuery({
        queryKey: ['allDoctors'],
        queryFn: async () => {
            const res = await axiosPrivate.get('/doctors');
            return res.data;
        }
    });

    // Fetch slots for the selected date only
    const { data: slots = [], isLoading: slotsLoading, isFetching: slotsFetching } = useQuery({
        queryKey: ['availableSlots', selectedDoctorId, selectedDate.toISOString()],
        queryFn: async () => {
            const start = new Date(selectedDate);
            start.setHours(0, 0, 0, 0);
            const end = new Date(selectedDate);
            end.setHours(23, 59, 59, 999);
            const res = await axiosPrivate.get(`/appointments/slots?doctorId=${selectedDoctorId}&start=${start.toISOString()}&end=${end.toISOString()}`);
            return res.data;
        },
        enabled: !!selectedDoctorId
    });

    // Fetch doctor's working hours for the header and availability check
    const { data: workingHours = [] } = useQuery({
        queryKey: ['doctorWorkingHours', selectedDoctorId],
        queryFn: async () => {
            const res = await axiosPrivate.get(`/doctors/${selectedDoctorId}/working-hours`);
            return res.data;
        },
        enabled: !!selectedDoctorId
    });

    const mutation = useMutation({
        mutationFn: async (data) => {
            const res = await axiosPrivate.post('/appointments/book', data);
            return res.data;
        },
        onSuccess: () => {
            queryClient.invalidateQueries(['patientAppointments', user?.id]);
            queryClient.invalidateQueries(['doctor-today-appointments']);
            navigate('/patient/dashboard');
        },
        onError: (err) => {
            setError(err.response?.data?.message || 'Failed to book appointment');
        }
    });

    // Generate upcoming 14 days array
    const upcomingDays = useMemo(() => {
        const days = [];
        for (let i = 0; i < 14; i++) {
            const d = new Date();
            d.setDate(d.getDate() + i);
            d.setHours(0, 0, 0, 0);
            days.push(d);
        }
        return days;
    }, []);

    // Group slots into Morning, Afternoon, Evening
    const groupedSlots = useMemo(() => {
        const groups = { Morning: [], Afternoon: [], Evening: [] };
        slots.forEach(slot => {
            const hour = new Date(slot.startTime).getHours();
            if (hour < 12) groups.Morning.push(slot);
            else if (hour < 17) groups.Afternoon.push(slot);
            else groups.Evening.push(slot);
        });
        return groups;
    }, [slots]);

    const handleSubmit = (e) => {
        e.preventDefault();
        setError('');
        if (!selectedSlotId) {
            setError('Please select an appointment slot.');
            return;
        }
        mutation.mutate({ slotId: selectedSlotId, reasonForVisit: reason });
    };

    return (
        <div className="p-4 md:p-8 space-y-6 max-w-4xl mx-auto">
            <PageHeader 
                title="Book Appointment" 
                subtitle="Select an available time slot and provide a reason for your visit."
                icon={<Calendar className="w-8 h-8 text-primary" />}
            />
            
            {workingHours.length > 0 && (
                <div className="bg-primary/5 p-4 rounded-xl border border-primary/20 flex flex-wrap gap-x-6 gap-y-2 items-center text-sm text-text-secondary">
                    <span className="font-semibold text-primary flex items-center gap-1"><Clock size={16}/> Doctor's Standard Hours:</span>
                    {DAYS.map((dayName, idx) => {
                        const dayHours = workingHours.filter(wh => wh.dayOfWeek === idx && wh.isActive);
                        if (dayHours.length === 0) {
                            return (
                                <span key={idx} className="bg-surface px-2 py-1 rounded-md border shadow-sm">
                                    <span className="font-medium text-text-primary">{dayName.substring(0,3)}</span>: Off
                                </span>
                            );
                        }
                        const timeString = dayHours.map(wh => `${wh.startTime.substring(0,5)}-${wh.endTime.substring(0,5)}`).join(', ');
                        return (
                            <span key={idx} className="bg-surface px-2 py-1 rounded-md border shadow-sm">
                                <span className="font-medium text-text-primary">{dayName.substring(0,3)}</span>: {timeString}
                            </span>
                        );
                    })}
                </div>
            )}

            <Card>
                <Card.Header>
                    <h3 className="text-lg font-semibold">1. Select a Doctor</h3>
                </Card.Header>
                <Card.Body>
                    {doctorsLoading ? (
                        <div className="text-text-secondary text-sm">Loading doctors...</div>
                    ) : (
                        <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                            {doctors.map(doc => {
                                const isSelected = selectedDoctorId === String(doc.userId);
                                return (
                                    <button
                                        key={doc.id}
                                        type="button"
                                        onClick={() => {
                                            setSelectedDoctorId(String(doc.userId));
                                            setSelectedSlotId(''); // Reset slot on doctor change
                                        }}
                                        className={`
                                            flex items-start text-left p-4 rounded-xl border transition-all duration-200
                                            ${isSelected 
                                                ? 'bg-primary/5 border-primary ring-1 ring-primary shadow-sm' 
                                                : 'bg-surface border-surface-border hover:border-primary/50 hover:shadow-sm'}
                                        `}
                                    >
                                        <div className="flex-1">
                                            <h4 className={`font-bold ${isSelected ? 'text-primary' : 'text-text-primary'}`}>Dr. {doc.firstName} {doc.lastName}</h4>
                                            <p className="text-sm text-text-secondary">{doc.specialty}</p>
                                        </div>
                                    </button>
                                );
                            })}
                        </div>
                    )}
                </Card.Body>
            </Card>

            <div className={`transition-opacity duration-300 ${!selectedDoctorId ? 'opacity-50 pointer-events-none' : ''}`}>
                <Card>
                    <Card.Header>
                        <h3 className="text-lg font-semibold">2. Select a Date</h3>
                    </Card.Header>
                    <Card.Body>
                        <div className="flex overflow-x-auto gap-3 pb-2 -mx-2 px-2 snap-x">
                            {upcomingDays.map((date, idx) => {
                                const isSelected = selectedDate.getTime() === date.getTime();
                                const isWorkingDay = workingHours.some(wh => wh.dayOfWeek === date.getDay() && wh.isActive);
                                
                                return (
                                    <button
                                        key={idx}
                                        type="button"
                                        onClick={() => { setSelectedDate(date); setSelectedSlotId(''); }}
                                        disabled={!isWorkingDay}
                                        className={`
                                            snap-start min-w-[100px] flex flex-col items-center justify-center p-3 rounded-xl border transition-all duration-200
                                            ${isSelected ? 'bg-primary border-primary text-primary-foreground shadow-md scale-105' : 'bg-surface border-surface-border text-text-primary hover:border-primary/50'}
                                            ${!isWorkingDay && !isSelected ? 'opacity-50 grayscale bg-surface-hover cursor-not-allowed' : ''}
                                        `}
                                    >
                                        <span className="text-xs font-semibold uppercase tracking-wider mb-1 opacity-80">{DAYS[date.getDay()]}</span>
                                        <span className="text-2xl font-bold">{date.getDate()}</span>
                                        <span className="text-[10px] mt-1">{date.toLocaleDateString('en-US', { month: 'short' })}</span>
                                        {!isWorkingDay && <span className="text-[9px] mt-1 text-red-500 font-bold bg-white/20 px-1 rounded">UNAVAILABLE</span>}
                                    </button>
                                );
                            })}
                        </div>
                    </Card.Body>
                </Card>
            </div>

            <div className={`transition-opacity duration-300 ${!selectedDoctorId ? 'opacity-50 pointer-events-none' : ''}`}>
                <Card>
                    <Card.Header>
                        <h3 className="text-lg font-semibold">3. Select a Time Slot</h3>
                    </Card.Header>
                    <Card.Body>
                        {slotsLoading || slotsFetching ? (
                            <div className="py-8 text-center text-text-secondary flex flex-col items-center">
                                <Clock className="w-8 h-8 animate-spin opacity-20 mb-2" />
                                Loading available slots...
                            </div>
                        ) : slots.length > 0 ? (
                            <div className="space-y-6">
                                {Object.entries(groupedSlots).map(([period, periodSlots]) => {
                                    if (periodSlots.length === 0) return null;
                                    return (
                                        <div key={period}>
                                            <h3 className="text-sm font-semibold text-text-secondary uppercase tracking-wider mb-3 flex items-center gap-2">
                                                {period === 'Morning' && '🌅'}
                                                {period === 'Afternoon' && '☀️'}
                                                {period === 'Evening' && '🌙'}
                                                {period}
                                            </h3>
                                            <div className="flex flex-wrap gap-3">
                                                {periodSlots.map(slot => {
                                                    const timeStr = new Date(slot.startTime).toLocaleTimeString([], { hour: 'numeric', minute: '2-digit' });
                                                    const isSelected = selectedSlotId === slot.id;
                                                    return (
                                                        <button
                                                            key={slot.id}
                                                            type="button"
                                                            onClick={() => setSelectedSlotId(slot.id)}
                                                            className={`
                                                                px-4 py-2 rounded-lg text-sm font-medium transition-all duration-200 border
                                                                ${isSelected 
                                                                    ? 'bg-primary border-primary text-primary-foreground shadow-md ring-2 ring-primary/20 ring-offset-1' 
                                                                    : 'bg-surface border-surface-border text-text-primary hover:border-primary hover:text-primary hover:bg-primary/5'}
                                                            `}
                                                        >
                                                            {timeStr}
                                                        </button>
                                                    );
                                                })}
                                            </div>
                                        </div>
                                    );
                                })}
                            </div>
                        ) : (
                            <div className="py-12 text-center bg-surface-hover rounded-xl border border-dashed border-surface-border">
                                <Calendar className="w-12 h-12 text-text-muted mx-auto mb-3 opacity-20" />
                                <p className="text-text-primary font-medium">No slots available on this date.</p>
                                <p className="text-text-secondary text-sm mt-1">Please select another date from the calendar above.</p>
                            </div>
                        )}
                    </Card.Body>
                </Card>
            </div>

            <div className={`transition-opacity duration-300 ${!selectedDoctorId ? 'opacity-50 pointer-events-none' : ''}`}>
                <Card>
                    <Card.Header>
                        <h3 className="text-lg font-semibold">4. Confirm Details</h3>
                    </Card.Header>
                    <Card.Body>
                        <form onSubmit={handleSubmit} className="space-y-6">
                            {error && <div className="bg-destructive/10 text-destructive border border-destructive/20 p-3 rounded-lg text-sm">{error}</div>}
                            
                            <div>
                                <label className="block text-sm font-medium text-text-primary mb-2">Reason for Visit <span className="text-destructive">*</span></label>
                                <textarea 
                                    value={reason} 
                                    onChange={(e) => setReason(e.target.value)} 
                                    required
                                    rows="3"
                                    placeholder="Briefly describe your symptoms or reason for visit..."
                                    className="w-full form-input bg-surface border-input rounded-xl focus:ring-2 focus:ring-primary/20 resize-y"
                                ></textarea>
                            </div>

                            <div className="flex justify-end gap-3 pt-4 border-t border-surface-border">
                                <Button type="button" variant="ghost" onClick={() => navigate('/doctors')}>
                                    Cancel
                                </Button>
                                <Button type="submit" disabled={mutation.isPending || !selectedSlotId} isLoading={mutation.isPending}>
                                    Confirm Booking <ArrowRight className="w-4 h-4 ml-2" />
                                </Button>
                            </div>
                        </form>
                    </Card.Body>
                </Card>
            </div>
        </div>
    );
};

export default BookAppointment;
