import React, { useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { useQuery } from '@tanstack/react-query';
import { axiosPrivate } from '../../api/axios';
import { ArrowLeft, Calendar, FileText, Activity, Phone, Mail, Droplet, MapPin, User, ChevronRight } from 'lucide-react';

const PatientDetail = ({ patientIdOverride }) => {
  const { patientId: paramPatientId } = useParams();
  const patientId = patientIdOverride || paramPatientId;
  const navigate = useNavigate();
  const [activeTab, setActiveTab] = useState('Overview');

  const { data: patient, isLoading } = useQuery({
    queryKey: ['patient-detail', patientId],
    queryFn: async () => (await axiosPrivate.get(`/doctor/patients/${patientId}`)).data,
  });

  if (isLoading) return <div className="p-10 text-center text-slate-500 font-medium">Loading patient details...</div>;
  if (!patient) return <div className="p-10 text-center text-red-500 font-medium">Patient not found</div>;

  const generateEmail = (name) => {
    if (!name) return '-';
    return `${name.replace(/\s+/g, '.').toLowerCase()}@example.com`;
  };

  const generateDisplayId = (id) => {
    if (!id) return '-';
    return `PAT-${String(id).padStart(5, '0')}`;
  };

  const tabs = ['Overview', 'Appointments', 'Prescriptions', 'Lab Reports', 'Medical History', 'Documents', 'Billing & Payments'];

  return (
    <div className="p-6 bg-[#F8FAFC] min-h-full font-sans">
      <div className="max-w-[1400px] mx-auto">
        
        {/* Breadcrumb Header */}
        <div className="flex items-center justify-between mb-6">
          <div className="flex items-center gap-2 text-sm font-semibold text-slate-500">
            <span className="cursor-pointer hover:text-slate-800" onClick={() => navigate('/doctor/patients')}>Patients</span>
            <ChevronRight size={14} className="text-slate-400" />
            <span className="text-slate-800">Patient Details</span>
          </div>
          
          <button 
            onClick={() => navigate('/doctor/patients')}
            className="flex items-center gap-2 text-sm font-bold text-slate-700 bg-white border border-slate-200 px-4 py-2 rounded-lg shadow-sm hover:bg-slate-50 transition-colors"
          >
            <ArrowLeft size={16} strokeWidth={2.5} /> Back to Patients
          </button>
        </div>

        {/* Main Grid */}
        <div className="flex flex-col lg:flex-row gap-6">
          
          {/* LEFT SIDEBAR: Profile Card */}
          <div className="w-full lg:w-[340px] flex-shrink-0 flex flex-col gap-6">
            
            {/* Profile Info */}
            <div className="bg-white rounded-xl border border-slate-200 shadow-sm p-6 flex flex-col items-center">
              <div className="flex items-center gap-4 w-full mb-6">
                <img 
                  src={`https://i.pravatar.cc/150?u=${patient.patientId || 1}`} 
                  alt={patient.name}
                  className="w-16 h-16 rounded-full object-cover border-2 border-white shadow-sm"
                />
                <div>
                  <h2 className="text-lg font-bold text-slate-800 leading-tight">{patient.name || 'Robert Williams'}</h2>
                  <p className="text-xs font-semibold text-slate-500 mt-0.5 mb-1.5">ID: {generateDisplayId(patient.patientId)}</p>
                  <span className="inline-block px-2.5 py-0.5 bg-indigo-50 text-[#5B21B6] text-[10px] font-bold rounded-md uppercase tracking-wide">
                    Active Patient
                  </span>
                </div>
              </div>

              {/* Detail List */}
              <div className="w-full flex flex-col gap-4 text-xs font-semibold text-slate-500 border-t border-slate-100 pt-5">
                <div className="grid grid-cols-[32px_90px_1fr] items-center">
                  <User size={15} className="text-[#5B21B6] justify-self-center" />
                  <span className="text-slate-400">Age / Gender</span>
                  <span className="text-slate-800">{patient.age || '45'} Years / {patient.gender || 'Male'}</span>
                </div>
                <div className="grid grid-cols-[32px_90px_1fr] items-center">
                  <Phone size={15} className="text-[#5B21B6] justify-self-center" />
                  <span className="text-slate-400">Phone</span>
                  <span className="text-slate-800">{patient.phone || '+1 586 123 4567'}</span>
                </div>
                <div className="grid grid-cols-[32px_90px_1fr] items-center">
                  <Mail size={15} className="text-[#5B21B6] justify-self-center" />
                  <span className="text-slate-400">Email</span>
                  <span className="text-slate-800 break-all">{patient.email || generateEmail(patient.name)}</span>
                </div>
                <div className="grid grid-cols-[32px_90px_1fr] items-center">
                  <Droplet size={15} className="text-[#5B21B6] justify-self-center" />
                  <span className="text-slate-400">Blood Group</span>
                  <span className="text-slate-800">{patient.bloodGroup || 'O+'}</span>
                </div>
                <div className="grid grid-cols-[32px_90px_1fr] items-center">
                  <Calendar size={15} className="text-[#5B21B6] justify-self-center" />
                  <span className="text-slate-400">Date of Birth</span>
                  <span className="text-slate-800">15 May 1979</span>
                </div>
                <div className="grid grid-cols-[32px_90px_1fr] items-start mt-1">
                  <MapPin size={15} className="text-[#5B21B6] justify-self-center mt-0.5" />
                  <span className="text-slate-400">Address</span>
                  <span className="text-slate-800 leading-tight">123 Main Street, New York, USA</span>
                </div>
              </div>

              {/* Stats Boxes */}
              <div className="w-full grid grid-cols-3 gap-3 mt-6 border-t border-slate-100 pt-6">
                <div className="bg-[#F0FDF4] border border-[#DCFCE7] rounded-lg p-2.5 flex flex-col items-center justify-center text-center">
                  <span className="text-lg font-bold text-[#16A34A]">12</span>
                  <span className="text-[10px] font-bold text-[#15803D] mt-0.5">Appointments</span>
                </div>
                <div className="bg-[#EFF6FF] border border-[#DBEAFE] rounded-lg p-2.5 flex flex-col items-center justify-center text-center">
                  <span className="text-lg font-bold text-[#2563EB]">08</span>
                  <span className="text-[10px] font-bold text-[#1D4ED8] mt-0.5">Prescriptions</span>
                </div>
                <div className="bg-[#FFF7ED] border border-[#FFEDD5] rounded-lg p-2.5 flex flex-col items-center justify-center text-center">
                  <span className="text-lg font-bold text-[#EA580C]">15</span>
                  <span className="text-[10px] font-bold text-[#C2410C] mt-0.5">Lab Reports</span>
                </div>
              </div>
            </div>

            {/* Emergency Contact */}
            <div className="bg-white rounded-xl border border-slate-200 shadow-sm p-6 relative">
              <h3 className="text-[13px] font-bold text-slate-800 mb-4 flex justify-between items-center">
                Emergency Contact
                <button className="text-[#5B21B6] text-[11px] hover:underline">Edit</button>
              </h3>
              <div className="flex items-center justify-between">
                <div>
                  <p className="text-sm font-semibold text-slate-800">{patient.emergencyContactName || 'Linda Williams (Wife)'}</p>
                  <p className="text-xs font-medium text-slate-500 mt-1">{patient.emergencyContactPhone || '+1 586 234 5678'}</p>
                </div>
                <button className="w-8 h-8 rounded-full bg-indigo-50 text-[#5B21B6] flex items-center justify-center">
                  <Phone size={14} />
                </button>
              </div>
            </div>

            {/* Allergies */}
            <div className="bg-white rounded-xl border border-slate-200 shadow-sm p-6 relative">
              <h3 className="text-[13px] font-bold text-slate-800 mb-4 flex justify-between items-center">
                Allergies
                <button className="text-[#5B21B6] text-[11px] hover:underline">Edit</button>
              </h3>
              <div className="flex flex-wrap gap-2">
                <span className="px-3 py-1 bg-orange-50 text-[#EA580C] text-[11px] font-bold rounded-md">Penicillin</span>
                <span className="px-3 py-1 bg-orange-50 text-[#EA580C] text-[11px] font-bold rounded-md">Peanuts</span>
              </div>
            </div>

          </div>

          {/* RIGHT CONTENT */}
          <div className="flex-1 flex flex-col min-w-0">
            
            {/* Tabs */}
            <div className="bg-white rounded-t-xl border border-b-0 border-slate-200 px-6 pt-4 flex gap-6 overflow-x-auto scrollbar-hide">
              {tabs.map(tab => (
                <button
                  key={tab}
                  onClick={() => setActiveTab(tab)}
                  className={`pb-3 text-sm font-bold whitespace-nowrap transition-colors relative ${
                    activeTab === tab ? 'text-[#5B21B6]' : 'text-slate-500 hover:text-slate-700'
                  }`}
                >
                  {tab}
                  {activeTab === tab && (
                    <div className="absolute bottom-0 left-0 right-0 h-[3px] bg-[#5B21B6] rounded-t-md"></div>
                  )}
                </button>
              ))}
            </div>

            {/* Tab Content Area */}
            <div className="bg-white rounded-b-xl border border-slate-200 shadow-sm p-6 flex flex-col gap-6">
              
              {activeTab === 'Overview' && (
                <>
                  {/* Summary Cards Row */}
                  <div className="grid grid-cols-1 md:grid-cols-2 xl:grid-cols-4 gap-4">
                    {/* Card 1 */}
                    <div className="bg-[#F8FAFC] border border-slate-100 rounded-xl p-5 flex items-start gap-4">
                      <div className="w-10 h-10 rounded-lg bg-indigo-50 text-[#5B21B6] flex items-center justify-center shrink-0">
                        <Calendar size={20} />
                      </div>
                      <div>
                        <p className="text-[11px] font-bold text-slate-500 mb-1">Last Appointment</p>
                        <p className="text-[15px] font-bold text-slate-800">21 May 2024</p>
                        <p className="text-[11px] font-semibold text-slate-500 mt-1">10:30 AM</p>
                        <p className="text-[11px] font-semibold text-slate-500">with Dr. Michael Lee</p>
                      </div>
                    </div>
                    {/* Card 2 */}
                    <div className="bg-[#F0FDF4] border border-[#DCFCE7] rounded-xl p-5 flex items-start gap-4">
                      <div className="w-10 h-10 rounded-lg bg-green-100 text-[#16A34A] flex items-center justify-center shrink-0">
                        <Calendar size={20} />
                      </div>
                      <div>
                        <p className="text-[11px] font-bold text-slate-500 mb-1">Next Appointment</p>
                        <p className="text-[15px] font-bold text-slate-800">28 May 2024</p>
                        <p className="text-[11px] font-semibold text-slate-500 mt-1">11:00 AM</p>
                        <p className="text-[11px] font-semibold text-slate-500">with Dr. Sophia Reynolds</p>
                      </div>
                    </div>
                    {/* Card 3 */}
                    <div className="bg-[#FFF7ED] border border-[#FFEDD5] rounded-xl p-5 flex items-start gap-4">
                      <div className="w-10 h-10 rounded-lg bg-orange-100 text-[#EA580C] flex items-center justify-center shrink-0">
                        <FileText size={20} />
                      </div>
                      <div>
                        <p className="text-[11px] font-bold text-slate-500 mb-1">Total Prescriptions</p>
                        <p className="text-[17px] font-bold text-slate-800 mb-1">08</p>
                        <a href="#" className="text-[11px] font-bold text-slate-500 hover:text-slate-800">View all prescriptions</a>
                      </div>
                    </div>
                    {/* Card 4 */}
                    <div className="bg-[#EFF6FF] border border-[#DBEAFE] rounded-xl p-5 flex items-start gap-4">
                      <div className="w-10 h-10 rounded-lg bg-blue-100 text-[#2563EB] flex items-center justify-center shrink-0">
                        <Activity size={20} />
                      </div>
                      <div>
                        <p className="text-[11px] font-bold text-slate-500 mb-1">Total Lab Reports</p>
                        <p className="text-[17px] font-bold text-slate-800 mb-1">15</p>
                        <a href="#" className="text-[11px] font-bold text-slate-500 hover:text-slate-800">View all reports</a>
                      </div>
                    </div>
                  </div>

                  {/* Medical Summary Details */}
                  <div className="w-full">
                    <h3 className="text-[15px] font-bold text-slate-800 mb-4">Medical Summary</h3>
                    <div className="grid grid-cols-2 md:grid-cols-4 gap-y-6 gap-x-4">
                      <div>
                        <p className="text-[11px] font-semibold text-slate-400 mb-1">Primary Physician</p>
                        <p className="text-[13px] font-bold text-slate-800">Dr. Michael Lee</p>
                        <p className="text-[11px] font-medium text-slate-500">Cardiologist</p>
                      </div>
                      <div>
                        <p className="text-[11px] font-semibold text-slate-400 mb-1">Contact Number</p>
                        <p className="text-[13px] font-bold text-slate-800">+1 586 123 4567</p>
                      </div>
                      <div>
                        <p className="text-[11px] font-semibold text-slate-400 mb-1">Last Visit</p>
                        <p className="text-[13px] font-bold text-slate-800">21 May 2024</p>
                      </div>
                      <div>
                        <p className="text-[11px] font-semibold text-slate-400 mb-1">Chronic Conditions</p>
                        <p className="text-[13px] font-bold text-slate-800">Hypertension, Asthma</p>
                      </div>
                      
                      <div>
                        <p className="text-[11px] font-semibold text-slate-400 mb-1">Blood Pressure</p>
                        <p className="text-[13px] font-bold text-slate-800">120/80 mmHg</p>
                      </div>
                      <div>
                        <p className="text-[11px] font-semibold text-slate-400 mb-1">Heart Rate</p>
                        <p className="text-[13px] font-bold text-slate-800">72 bpm</p>
                      </div>
                      <div>
                        <p className="text-[11px] font-semibold text-slate-400 mb-1">Weight</p>
                        <p className="text-[13px] font-bold text-slate-800">72 kg</p>
                      </div>
                      <div>
                        <p className="text-[11px] font-semibold text-slate-400 mb-1">Height</p>
                        <p className="text-[13px] font-bold text-slate-800">178 cm</p>
                      </div>
                      <div>
                        <p className="text-[11px] font-semibold text-slate-400 mb-1">BMI</p>
                        <p className="text-[13px] font-bold text-slate-800">22.7</p>
                      </div>
                      <div className="col-span-1 md:col-span-3">
                        <p className="text-[11px] font-semibold text-slate-400 mb-1">Last Updated</p>
                        <p className="text-[13px] font-bold text-slate-800">21 May 2024</p>
                      </div>
                    </div>
                  </div>

                  <hr className="border-slate-100" />

                  {/* Two Column Bottom Section */}
                  <div className="grid grid-cols-1 lg:grid-cols-2 gap-8">
                    
                    {/* Recent Medical History */}
                    <div>
                      <div className="flex items-center justify-between mb-5">
                        <h3 className="text-[14px] font-bold text-slate-800">Recent Medical History</h3>
                        <button className="text-[#5B21B6] text-[11px] font-bold hover:underline">View All</button>
                      </div>
                      
                      <div className="flex flex-col gap-6 relative before:absolute before:left-[45px] before:top-2 before:bottom-2 before:w-px before:bg-slate-200">
                        {/* Event 1 */}
                        <div className="flex gap-4 relative z-10">
                          <div className="w-[45px] pt-0.5 shrink-0 flex flex-col items-center">
                            <span className="text-[15px] font-bold text-slate-800 leading-none">21</span>
                            <span className="text-[9px] font-bold text-slate-500 uppercase mt-0.5">MAY 2024</span>
                          </div>
                          <div className="w-2 h-2 rounded-full bg-[#5B21B6] absolute left-[41.5px] top-1.5 ring-4 ring-white"></div>
                          <div className="flex-1 pl-3 pb-1">
                            <div className="flex items-center gap-2 mb-1">
                              <p className="text-[12px] font-bold text-slate-800">Consultation with Dr. Michael Lee</p>
                              <span className="px-1.5 py-0.5 bg-indigo-50 text-[#5B21B6] text-[9px] font-bold rounded">Cardiology</span>
                            </div>
                            <p className="text-[11px] font-medium text-slate-500 leading-relaxed">Routine heart checkup. Blood pressure normal. Continue current medication.</p>
                          </div>
                        </div>

                        {/* Event 2 */}
                        <div className="flex gap-4 relative z-10">
                          <div className="w-[45px] pt-0.5 shrink-0 flex flex-col items-center">
                            <span className="text-[15px] font-bold text-slate-800 leading-none">15</span>
                            <span className="text-[9px] font-bold text-slate-500 uppercase mt-0.5">APR 2024</span>
                          </div>
                          <div className="w-2 h-2 rounded-full bg-[#5B21B6] absolute left-[41.5px] top-1.5 ring-4 ring-white"></div>
                          <div className="flex-1 pl-3 pb-1">
                            <div className="flex items-center gap-2 mb-1">
                              <p className="text-[12px] font-bold text-slate-800">Lab Test - Complete Blood Count</p>
                              <span className="px-1.5 py-0.5 bg-blue-50 text-blue-600 text-[9px] font-bold rounded">Lab Report</span>
                            </div>
                            <p className="text-[11px] font-medium text-slate-500 leading-relaxed mb-2">All parameters within normal range.</p>
                          </div>
                        </div>

                        {/* Event 3 */}
                        <div className="flex gap-4 relative z-10">
                          <div className="w-[45px] pt-0.5 shrink-0 flex flex-col items-center">
                            <span className="text-[15px] font-bold text-slate-800 leading-none">10</span>
                            <span className="text-[9px] font-bold text-slate-500 uppercase mt-0.5">MAR 2024</span>
                          </div>
                          <div className="w-2 h-2 rounded-full bg-[#5B21B6] absolute left-[41.5px] top-1.5 ring-4 ring-white"></div>
                          <div className="flex-1 pl-3 pb-1">
                            <div className="flex items-center gap-2 mb-1">
                              <p className="text-[12px] font-bold text-slate-800">Prescription by Dr. Michael Lee</p>
                              <span className="px-1.5 py-0.5 bg-orange-50 text-orange-600 text-[9px] font-bold rounded">Prescription</span>
                            </div>
                            <p className="text-[11px] font-medium text-slate-500 leading-relaxed mb-2">Amlodipine 5mg, Aspirin 75mg</p>
                            <button className="text-[10px] font-bold text-[#5B21B6] bg-indigo-50 px-3 py-1.5 rounded-md hover:bg-indigo-100 transition-colors">View Details</button>
                          </div>
                        </div>
                      </div>
                      
                      <div className="mt-4 text-center">
                        <button className="text-[#5B21B6] text-[11px] font-bold hover:underline flex items-center justify-center gap-1 mx-auto">
                          View All History <ChevronRight size={14} />
                        </button>
                      </div>
                    </div>

                    {/* Current Medications */}
                    <div>
                      <div className="flex items-center justify-between mb-5">
                        <h3 className="text-[14px] font-bold text-slate-800">Current Medications</h3>
                        <button className="text-[#5B21B6] text-[11px] font-bold hover:underline">View All</button>
                      </div>
                      
                      <div className="flex flex-col gap-3">
                        <div className="flex items-center justify-between py-2 border-b border-slate-50 last:border-0">
                          <div className="flex items-center gap-3">
                            <div className="w-8 h-8 rounded-full bg-orange-50 text-orange-600 flex items-center justify-center font-serif italic font-bold">Rx</div>
                            <span className="text-[12px] font-bold text-slate-800">Amlodipine 5mg</span>
                          </div>
                          <span className="text-[11px] font-medium text-slate-500">1 tablet daily after food</span>
                          <span className="px-2 py-0.5 bg-green-50 text-green-700 text-[9px] font-bold rounded-md">Active</span>
                        </div>

                        <div className="flex items-center justify-between py-2 border-b border-slate-50 last:border-0">
                          <div className="flex items-center gap-3">
                            <div className="w-8 h-8 rounded-full bg-orange-50 text-orange-600 flex items-center justify-center font-serif italic font-bold">Rx</div>
                            <span className="text-[12px] font-bold text-slate-800">Aspirin 75mg</span>
                          </div>
                          <span className="text-[11px] font-medium text-slate-500">1 tablet daily after food</span>
                          <span className="px-2 py-0.5 bg-green-50 text-green-700 text-[9px] font-bold rounded-md">Active</span>
                        </div>

                        <div className="flex items-center justify-between py-2 border-b border-slate-50 last:border-0">
                          <div className="flex items-center gap-3">
                            <div className="w-8 h-8 rounded-full bg-orange-50 text-orange-600 flex items-center justify-center font-serif italic font-bold">Rx</div>
                            <span className="text-[12px] font-bold text-slate-800">Atorvastatin 10mg</span>
                          </div>
                          <span className="text-[11px] font-medium text-slate-500">1 tablet at bedtime</span>
                          <span className="px-2 py-0.5 bg-green-50 text-green-700 text-[9px] font-bold rounded-md">Active</span>
                        </div>

                        <div className="flex items-center justify-between py-2 border-b border-slate-50 last:border-0">
                          <div className="flex items-center gap-3">
                            <div className="w-8 h-8 rounded-full bg-orange-50 text-orange-600 flex items-center justify-center font-serif italic font-bold">Rx</div>
                            <span className="text-[12px] font-bold text-slate-800">Vitamin D3 60000 IU</span>
                          </div>
                          <span className="text-[11px] font-medium text-slate-500">1 capsule weekly</span>
                          <span className="px-2 py-0.5 bg-slate-100 text-slate-500 text-[9px] font-bold rounded-md">Completed</span>
                        </div>
                      </div>

                      <div className="mt-5 text-center">
                        <button className="text-[#5B21B6] text-[11px] font-bold hover:underline flex items-center justify-center gap-1 mx-auto">
                          View All Medications <ChevronRight size={14} />
                        </button>
                      </div>
                    </div>

                  </div>
                </>
              )}

              {activeTab !== 'Overview' && (
                <div className="py-20 text-center flex flex-col items-center">
                  <div className="w-16 h-16 bg-slate-50 rounded-full flex items-center justify-center mb-4">
                    <Calendar className="text-slate-300" size={24} />
                  </div>
                  <h3 className="text-lg font-bold text-slate-700 mb-2">{activeTab}</h3>
                  <p className="text-sm font-medium text-slate-500">This section is currently under development.</p>
                </div>
              )}

            </div>
          </div>
        </div>

      </div>
    </div>
  );
};

export default PatientDetail;
