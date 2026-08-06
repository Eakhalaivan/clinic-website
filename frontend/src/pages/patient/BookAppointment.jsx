import React, { useState, useMemo, useEffect } from 'react';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { useParams, useNavigate } from 'react-router-dom';
import { axiosPrivate } from '../../api/axios';
import useAuthStore from '../../store/authStore';
import { Calendar as CalendarIcon, Clock, ArrowRight, Search, MapPin, ChevronLeft, ChevronRight, Check } from 'lucide-react';
import { toast } from 'react-hot-toast';
import { format, addMonths, subMonths, startOfMonth, endOfMonth, eachDayOfInterval, isSameMonth, isSameDay, isToday, addDays } from 'date-fns';

const DAYS = ['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat'];

export default function BookAppointment() {
    const { doctorId } = useParams();
    const navigate = useNavigate();
    const queryClient = useQueryClient();
    const { user } = useAuthStore();
    
    // State
    const [currentStep, setCurrentStep] = useState(1); // 1: Doctor, 2: Date/Time, 3: Details, 4: Confirm, 5: Complete
    const [selectedDoctorId, setSelectedDoctorId] = useState(doctorId || '');
    
    const [currentMonth, setCurrentMonth] = useState(new Date());
    const [selectedDate, setSelectedDate] = useState(null);
    const [selectedSlotId, setSelectedSlotId] = useState('');
    const [reason, setReason] = useState('');
    const [error, setError] = useState('');
    
    const [searchQuery, setSearchQuery] = useState('');

    // Fetch list of doctors
    const { data: doctors = [], isLoading: doctorsLoading } = useQuery({
        queryKey: ['allDoctors'],
        queryFn: async () => {
            const res = await axiosPrivate.get('/doctors');
            return res.data;
        }
    });

    const selectedDoctor = useMemo(() => doctors.find(d => String(d.userId) === String(selectedDoctorId)), [doctors, selectedDoctorId]);

    // Fetch doctor's working hours
    const { data: workingHours = [] } = useQuery({
        queryKey: ['doctorWorkingHours', selectedDoctorId],
        queryFn: async () => {
            const res = await axiosPrivate.get(`/doctors/${selectedDoctorId}/working-hours`);
            return res.data;
        },
        enabled: !!selectedDoctorId
    });

    // Fetch slots for the selected date only
    const { data: slots = [], isLoading: slotsLoading } = useQuery({
        queryKey: ['availableSlots', selectedDoctorId, selectedDate?.toISOString()],
        queryFn: async () => {
            if (!selectedDate) return [];
            const start = new Date(selectedDate);
            start.setHours(0, 0, 0, 0);
            const end = new Date(selectedDate);
            end.setHours(23, 59, 59, 999);
            const res = await axiosPrivate.get(`/appointments/slots?doctorId=${selectedDoctorId}&start=${start.toISOString()}&end=${end.toISOString()}`);
            return res.data;
        },
        enabled: !!selectedDoctorId && !!selectedDate
    });

    // Calendar logic
    const monthStart = startOfMonth(currentMonth);
    const monthEnd = endOfMonth(currentMonth);
    const startDate = monthStart;
    const endDate = monthEnd;
    const dateFormat = "MMMM yyyy";
    const daysInMonth = eachDayOfInterval({ start: startDate, end: endDate });

    // Pad beginning of month with empty slots
    const startDayOfWeek = monthStart.getDay();
    const emptyDaysBefore = Array.from({ length: startDayOfWeek }).map((_, i) => i);

    const nextMonth = () => setCurrentMonth(addMonths(currentMonth, 1));
    const prevMonth = () => setCurrentMonth(subMonths(currentMonth, 1));

    // Handle slot real-time updates
    useEffect(() => {
        const token = localStorage.getItem('access_token');
        const baseUrl = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api';
        const evtSource = new EventSource(`${baseUrl.replace('/api', '')}/api/sse/appointments?token=${token}`);
        
        evtSource.addEventListener('appointment-booked', (event) => {
            try {
                const data = JSON.parse(event.data);
                if (String(data.doctorId) === String(selectedDoctorId) && selectedDate) {
                    queryClient.invalidateQueries(['availableSlots', selectedDoctorId, selectedDate.toISOString()]);
                }
            } catch (err) {}
        });

        return () => evtSource.close();
    }, [selectedDoctorId, selectedDate, queryClient]);

    const mutation = useMutation({
        mutationFn: async (data) => {
            const res = await axiosPrivate.post('/appointments/book', data);
            return res.data;
        },
        onSuccess: () => {
            queryClient.invalidateQueries(['patientAppointments']);
            queryClient.invalidateQueries(['doctor-today-appointments']);
            setCurrentStep(5);
        },
        onError: (err) => {
            setError(err.response?.data?.message || 'Failed to book appointment');
            if (err.response?.status === 409 && selectedDate) {
                queryClient.invalidateQueries(['availableSlots', selectedDoctorId, selectedDate.toISOString()]);
                setSelectedSlotId('');
            }
        }
    });

    const handleConfirm = () => {
        setError('');
        if (!selectedSlotId) return setError('Please select a time slot.');
        if (!reason) return setError('Please provide a reason for the visit.');
        mutation.mutate({ slotId: selectedSlotId, reasonForVisit: reason });
    };

    // Filtered Doctors
    const filteredDoctors = doctors.filter(doc => {
        const fullName = `${doc.firstName} ${doc.lastName}`.toLowerCase();
        return fullName.includes(searchQuery.toLowerCase()) || (doc.specialty || '').toLowerCase().includes(searchQuery.toLowerCase());
    });

    const selectedSlot = slots.find(s => s.id === selectedSlotId);

    return (
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8 space-y-8 bg-gray-50 min-h-screen">
            
            {/* Stepper */}
            <div className="hidden md:flex justify-between items-center relative mb-12 px-12">
                <div className="absolute top-1/2 left-16 right-16 h-0.5 bg-gray-200 -z-10 -translate-y-1/2"></div>
                
                {[
                    { num: 1, label: 'Select Doctor' },
                    { num: 2, label: 'Select Date & Time' },
                    { num: 3, label: 'Appointment Details' },
                    { num: 4, label: 'Review & Confirm' },
                    { num: 5, label: 'Booking Complete' }
                ].map(step => (
                    <div key={step.num} className="flex flex-col items-center relative z-10 bg-gray-50 px-2">
                        <div className={`w-8 h-8 rounded-full flex items-center justify-center font-bold text-sm border-2 
                            ${currentStep > step.num ? 'bg-indigo-600 border-indigo-600 text-white' : 
                              currentStep === step.num ? 'bg-indigo-600 border-indigo-600 text-white ring-4 ring-indigo-100' : 
                              'bg-white border-gray-300 text-gray-400'}`}>
                            {currentStep > step.num ? <Check className="w-4 h-4" /> : step.num}
                        </div>
                        <span className={`text-xs mt-2 font-medium ${currentStep === step.num ? 'text-indigo-600' : 'text-gray-500'}`}>
                            {step.label}
                        </span>
                    </div>
                ))}
            </div>

            {currentStep === 5 ? (
                <div className="bg-white rounded-2xl shadow-sm border border-gray-100 p-12 text-center max-w-2xl mx-auto">
                    <div className="w-20 h-20 bg-green-100 rounded-full flex items-center justify-center mx-auto mb-6">
                        <Check className="w-10 h-10 text-green-600" />
                    </div>
                    <h2 className="text-3xl font-bold text-gray-900 mb-2">Booking Confirmed!</h2>
                    <p className="text-gray-600 mb-8">
                        Your appointment with Dr. {selectedDoctor?.lastName} is scheduled for {selectedDate && format(selectedDate, 'MMMM d, yyyy')} at {selectedSlot && format(new Date(selectedSlot.startTime), 'h:mm a')}.
                    </p>
                    <button 
                        onClick={() => navigate('/patient/dashboard')}
                        className="bg-indigo-600 text-white px-8 py-3 rounded-lg font-medium hover:bg-indigo-700 transition-colors"
                    >
                        Go to Dashboard
                    </button>
                </div>
            ) : (
                <div className="grid grid-cols-1 lg:grid-cols-12 gap-8">
                    
                    {/* LEFT COLUMN: Main Content area (Steps 1, 3, 4) */}
                    <div className="lg:col-span-8 space-y-6">
                        
                        <div>
                            <h1 className="text-3xl font-bold text-gray-900 mb-2">Book an Appointment</h1>
                            <p className="text-gray-500">Find the right doctor and book your appointment in a few simple steps</p>
                        </div>

                        {currentStep === 1 || currentStep === 2 ? (
                            <>
                                {/* Filters */}
                                <div className="flex flex-col md:flex-row gap-4 mb-6">
                                    <div className="relative flex-1">
                                        <Search className="absolute left-3 top-1/2 -translate-y-1/2 text-gray-400 w-5 h-5" />
                                        <input 
                                            type="text" 
                                            placeholder="Search doctors by name or specialization" 
                                            className="w-full pl-10 pr-4 py-3 rounded-xl border border-gray-200 focus:border-indigo-500 focus:ring-1 focus:ring-indigo-500 outline-none transition-all"
                                            value={searchQuery}
                                            onChange={e => setSearchQuery(e.target.value)}
                                        />
                                    </div>
                                    <select className="px-4 py-3 rounded-xl border border-gray-200 bg-white text-gray-700 focus:border-indigo-500 focus:ring-1 focus:ring-indigo-500 outline-none">
                                        <option>All Specialties</option>
                                        <option>Cardiologist</option>
                                        <option>General Physician</option>
                                    </select>
                                    <select className="px-4 py-3 rounded-xl border border-gray-200 bg-white text-gray-700 focus:border-indigo-500 focus:ring-1 focus:ring-indigo-500 outline-none">
                                        <option>All Locations</option>
                                    </select>
                                </div>

                                {/* Doctor List */}
                                <div className="space-y-4">
                                    {doctorsLoading ? (
                                        <div className="text-center py-12 text-gray-500">Loading doctors...</div>
                                    ) : filteredDoctors.map(doc => {
                                        const isSelected = selectedDoctorId === String(doc.userId);
                                        // Mock specialties and fee since backend doesn't explicitly return an array or fee
                                        const specialties = (doc.specialty || '').split(',').map(s => s.trim()).filter(Boolean);
                                        const fee = doc.consultationFee || 60; // Mock fee if not available
                                        
                                        return (
                                            <div 
                                                key={doc.id} 
                                                className={`bg-white rounded-2xl p-6 border transition-all ${isSelected ? 'border-indigo-500 shadow-md ring-1 ring-indigo-500' : 'border-gray-200 shadow-sm hover:border-indigo-300'}`}
                                            >
                                                <div className="flex flex-col md:flex-row gap-6 items-start md:items-center">
                                                    <div className="w-20 h-20 rounded-full bg-gray-200 overflow-hidden flex-shrink-0">
                                                        {doc.profileImageUrl ? (
                                                            <img src={doc.profileImageUrl} alt={`Dr. ${doc.lastName}`} className="w-full h-full object-cover" />
                                                        ) : (
                                                            <div className="w-full h-full flex items-center justify-center text-gray-500 font-bold text-xl bg-indigo-50 text-indigo-700">
                                                                {doc.firstName?.[0]}{doc.lastName?.[0]}
                                                            </div>
                                                        )}
                                                    </div>
                                                    
                                                    <div className="flex-1">
                                                        <h3 className="text-xl font-bold text-gray-900">Dr. {doc.firstName} {doc.lastName}</h3>
                                                        <p className="text-gray-500 text-sm mb-1">{doc.specialty}</p>
                                                        <div className="flex items-center text-yellow-400 text-sm mb-3">
                                                            {'★'.repeat(5)} <span className="text-gray-500 ml-2">4.9 (120 Reviews)</span>
                                                        </div>
                                                        
                                                        {specialties.length > 0 && (
                                                            <div className="flex flex-wrap gap-2">
                                                                {specialties.map((spec, i) => (
                                                                    <span key={i} className="bg-indigo-50 text-indigo-700 px-3 py-1 rounded-full text-xs font-medium">
                                                                        {spec}
                                                                    </span>
                                                                ))}
                                                            </div>
                                                        )}
                                                    </div>

                                                    <div className="flex flex-col items-end gap-3 mt-4 md:mt-0 border-t md:border-t-0 md:border-l border-gray-100 pt-4 md:pt-0 md:pl-6 min-w-[140px]">
                                                        <div className="text-center">
                                                            <p className="text-sm text-gray-500">Consultation Fee</p>
                                                            <p className="text-2xl font-bold text-indigo-600">${fee}</p>
                                                        </div>
                                                        <button 
                                                            onClick={() => {
                                                                setSelectedDoctorId(String(doc.userId));
                                                                if (currentStep === 1) setCurrentStep(2);
                                                            }}
                                                            className={`w-full py-2 px-4 rounded-lg font-medium transition-colors ${isSelected ? 'bg-indigo-100 text-indigo-700' : 'bg-indigo-600 text-white hover:bg-indigo-700'}`}
                                                        >
                                                            {isSelected ? 'Selected' : 'Book Appointment'}
                                                        </button>
                                                    </div>
                                                </div>
                                            </div>
                                        );
                                    })}
                                </div>
                            </>
                        ) : currentStep === 3 || currentStep === 4 ? (
                            <div className="bg-white rounded-2xl shadow-sm border border-gray-200 p-8">
                                <h3 className="text-2xl font-bold text-gray-900 mb-6">Appointment Details</h3>
                                
                                <div className="space-y-6">
                                    <div>
                                        <label className="block text-sm font-medium text-gray-700 mb-2">Reason for Visit *</label>
                                        <textarea
                                            value={reason}
                                            onChange={e => setReason(e.target.value)}
                                            rows="4"
                                            className="w-full rounded-xl border border-gray-300 p-4 focus:border-indigo-500 focus:ring-1 focus:ring-indigo-500 outline-none resize-y"
                                            placeholder="Please describe your symptoms or reason for visit..."
                                        />
                                    </div>
                                    
                                    {error && (
                                        <div className="bg-red-50 text-red-700 p-4 rounded-lg border border-red-200">
                                            {error}
                                        </div>
                                    )}

                                    <div className="flex justify-between items-center pt-6 border-t border-gray-100">
                                        <button 
                                            onClick={() => setCurrentStep(2)}
                                            className="text-gray-500 hover:text-gray-900 font-medium"
                                        >
                                            Back to Date & Time
                                        </button>
                                        <button 
                                            onClick={handleConfirm}
                                            disabled={mutation.isPending || !reason.trim()}
                                            className="bg-indigo-600 text-white px-8 py-3 rounded-lg font-medium hover:bg-indigo-700 disabled:opacity-50 flex items-center"
                                        >
                                            {mutation.isPending ? 'Confirming...' : 'Review & Confirm'} <ArrowRight className="w-4 h-4 ml-2" />
                                        </button>
                                    </div>
                                </div>
                            </div>
                        ) : null}
                    </div>

                    {/* RIGHT COLUMN: Selection Summary and Date/Time Picker */}
                    <div className="lg:col-span-4 space-y-6">
                        
                        {/* Summary Card */}
                        <div className="bg-white rounded-2xl shadow-sm border border-gray-200 p-6">
                            <div className="flex justify-between items-center mb-4">
                                <h3 className="font-bold text-gray-900">Appointment Summary</h3>
                                <button onClick={() => { setSelectedDoctorId(''); setSelectedDate(null); setSelectedSlotId(''); setCurrentStep(1); }} className="text-indigo-600 text-sm hover:underline">Clear All</button>
                            </div>
                            
                            <div className="space-y-4 text-sm">
                                <div className="flex justify-between border-b border-gray-50 pb-3">
                                    <span className="text-gray-500 flex items-center"><span className="w-5 mr-2">👤</span> Doctor</span>
                                    <span className={selectedDoctor ? 'font-medium text-gray-900' : 'text-gray-400'}>
                                        {selectedDoctor ? `Dr. ${selectedDoctor.lastName}` : 'Not selected'}
                                    </span>
                                </div>
                                <div className="flex justify-between border-b border-gray-50 pb-3">
                                    <span className="text-gray-500 flex items-center"><span className="w-5 mr-2">📅</span> Date</span>
                                    <span className={selectedDate ? 'font-medium text-gray-900' : 'text-gray-400'}>
                                        {selectedDate ? format(selectedDate, 'MMM d, yyyy') : 'Not selected'}
                                    </span>
                                </div>
                                <div className="flex justify-between border-b border-gray-50 pb-3">
                                    <span className="text-gray-500 flex items-center"><span className="w-5 mr-2">🕒</span> Time</span>
                                    <span className={selectedSlot ? 'font-medium text-gray-900' : 'text-gray-400'}>
                                        {selectedSlot ? format(new Date(selectedSlot.startTime), 'h:mm a') : 'Not selected'}
                                    </span>
                                </div>
                                <div className="flex justify-between pb-1">
                                    <span className="text-gray-500 flex items-center"><span className="w-5 mr-2">🏥</span> Consultation Type</span>
                                    <span className="font-medium text-gray-900">In-person</span>
                                </div>
                            </div>
                        </div>

                        {/* Calendar Card (Only visible/active if doctor is selected and we are on step 1 or 2) */}
                        {(currentStep === 1 || currentStep === 2) && (
                            <div className={`bg-white rounded-2xl shadow-sm border border-gray-200 p-6 transition-opacity ${!selectedDoctorId ? 'opacity-50 pointer-events-none' : ''}`}>
                                <h3 className="font-bold text-gray-900 mb-4">Select Date</h3>
                                
                                <div className="mb-4">
                                    <div className="flex justify-between items-center mb-4">
                                        <button onClick={prevMonth} className="p-1 hover:bg-gray-100 rounded-md text-gray-600"><ChevronLeft className="w-5 h-5"/></button>
                                        <span className="font-bold text-sm text-gray-900">{format(currentMonth, dateFormat)}</span>
                                        <button onClick={nextMonth} className="p-1 hover:bg-gray-100 rounded-md text-gray-600"><ChevronRight className="w-5 h-5"/></button>
                                    </div>
                                    
                                    <div className="grid grid-cols-7 text-center text-xs text-gray-500 font-medium mb-2">
                                        {DAYS.map(d => <div key={d}>{d}</div>)}
                                    </div>
                                    
                                    <div className="grid grid-cols-7 text-center gap-y-2">
                                        {emptyDaysBefore.map(i => <div key={`empty-${i}`} />)}
                                        
                                        {daysInMonth.map(day => {
                                            const isPast = day < new Date(new Date().setHours(0,0,0,0));
                                            const isSelectedDay = selectedDate && isSameDay(day, selectedDate);
                                            const isWorkingDay = workingHours.some(wh => wh.dayOfWeek === day.getDay() && wh.isActive);
                                            const isDisabled = isPast || !isWorkingDay;
                                            
                                            return (
                                                <div key={day.toString()} className="flex justify-center">
                                                    <button
                                                        onClick={() => { setSelectedDate(day); setSelectedSlotId(''); setCurrentStep(2); }}
                                                        disabled={isDisabled}
                                                        className={`w-8 h-8 flex items-center justify-center rounded-full text-sm font-medium transition-colors
                                                            ${isSelectedDay ? 'bg-indigo-600 text-white' : 
                                                              isDisabled ? 'text-gray-300 cursor-not-allowed' : 'text-gray-700 hover:bg-indigo-50'}`}
                                                    >
                                                        {format(day, 'd')}
                                                    </button>
                                                </div>
                                            );
                                        })}
                                    </div>
                                </div>
                                
                                {/* Time Slots */}
                                {selectedDate && (
                                    <div className="mt-6 border-t border-gray-100 pt-6">
                                        <h3 className="font-bold text-gray-900 mb-4">Select Time</h3>
                                        
                                        {slotsLoading ? (
                                            <div className="text-center text-sm text-gray-500 py-4">Loading slots...</div>
                                        ) : slots.length === 0 ? (
                                            <div className="text-center text-sm text-gray-500 py-4">No available slots for this date.</div>
                                        ) : (
                                            <div className="grid grid-cols-2 gap-2">
                                                {slots.slice(0, 6).map(slot => {
                                                    const isSelected = selectedSlotId === slot.id;
                                                    return (
                                                        <button 
                                                            key={slot.id}
                                                            onClick={() => setSelectedSlotId(slot.id)}
                                                            className={`py-2 px-1 text-sm rounded-lg border font-medium transition-colors
                                                                ${isSelected ? 'bg-indigo-50 border-indigo-600 text-indigo-700 ring-1 ring-indigo-600' : 'border-gray-200 text-gray-700 hover:border-indigo-300'}`}
                                                        >
                                                            {format(new Date(slot.startTime), 'hh:mm a')}
                                                        </button>
                                                    );
                                                })}
                                            </div>
                                        )}
                                        {slots.length > 6 && (
                                            <button className="w-full mt-3 text-sm text-indigo-600 font-medium hover:underline">Show More</button>
                                        )}
                                    </div>
                                )}
                                
                                {selectedSlotId && (
                                    <button 
                                        onClick={() => setCurrentStep(3)}
                                        className="w-full mt-6 bg-indigo-600 text-white py-3 rounded-lg font-medium hover:bg-indigo-700 transition-colors flex items-center justify-center"
                                    >
                                        Next: Appointment Details <ArrowRight className="w-4 h-4 ml-2" />
                                    </button>
                                )}
                            </div>
                        )}
                    </div>
                </div>
            )}
        </div>
    );
}
