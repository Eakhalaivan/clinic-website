import React from 'react';
import { ArrowLeft, UploadCloud, Info, FileText, X, ShieldCheck, Calendar, ChevronDown } from 'lucide-react';
import { useNavigate } from 'react-router-dom';

const UploadLabReport = () => {
  const navigate = useNavigate();

  return (
    <div className="p-6 md:p-8 bg-white min-h-full font-sans">
      <div className="max-w-[1500px] mx-auto">
        
        {/* Breadcrumb & Header */}
        <div className="mb-8">
          <div className="flex items-center justify-between mb-4">
            <div className="flex items-center gap-2 text-[13px] font-bold text-slate-500">
              <button onClick={() => navigate('/doctor/lab-reports')} className="hover:text-[#5B21B6]">Lab Reports</button>
              <span>&gt;</span>
              <span className="text-slate-800">Upload Report</span>
            </div>
            <button 
              onClick={() => navigate('/doctor/lab-reports')}
              className="flex items-center gap-2 px-4 py-2 border border-slate-200 rounded-lg text-[13px] font-bold text-slate-700 hover:bg-slate-50 transition-colors"
            >
              <ArrowLeft size={16} /> Back to Lab Reports
            </button>
          </div>
          <div>
            <h1 className="text-2xl font-bold text-slate-800">Upload Lab Report</h1>
            <p className="text-sm font-medium text-slate-500 mt-1">Upload and manage patient lab reports</p>
          </div>
        </div>

        {/* Main 3-Column Layout */}
        <div className="flex flex-col lg:flex-row gap-6">
          
          {/* LEFT COLUMN: Upload & Guidelines */}
          <div className="w-full lg:w-[320px] flex-shrink-0 flex flex-col gap-6">
            <div className="bg-white rounded-xl border border-slate-200 shadow-sm p-5">
              <h3 className="text-[15px] font-bold text-slate-800 mb-4">Upload Files</h3>
              
              <div className="border-2 border-dashed border-indigo-300 rounded-xl bg-indigo-50/30 p-8 flex flex-col items-center justify-center text-center">
                <div className="w-14 h-14 rounded-full bg-indigo-100 text-[#5B21B6] flex items-center justify-center mb-4">
                  <UploadCloud size={28} strokeWidth={2.5} />
                </div>
                <p className="text-[14px] font-bold text-slate-800 mb-2">Drag and drop files here</p>
                <p className="text-[12px] font-medium text-slate-500 mb-4">or</p>
                <button className="bg-[#5B21B6] hover:bg-indigo-800 text-white px-6 py-2 rounded-lg text-[13px] font-bold shadow-sm transition-colors mb-4">
                  Browse Files
                </button>
                <p className="text-[11px] font-medium text-slate-500 leading-relaxed">
                  Supports PDF, JPG, PNG, DICOM<br/>Max 20MB per file
                </p>
              </div>
            </div>

            <div className="bg-[#FAF5FF] border border-[#F3E8FF] rounded-xl p-5">
              <div className="flex items-center gap-2 mb-3">
                <Info size={16} className="text-[#9333EA]" strokeWidth={2.5} />
                <h3 className="text-[14px] font-bold text-[#5B21B6]">Guidelines</h3>
              </div>
              <ul className="text-[12px] font-medium text-slate-700 space-y-2 list-disc pl-4 marker:text-[#9333EA]">
                <li>Upload clear and readable reports</li>
                <li>Supported formats: PDF, JPG, PNG, DICOM</li>
                <li>Maximum file size: 20MB per file</li>
                <li>You can upload multiple files at once</li>
              </ul>
            </div>
          </div>

          {/* MIDDLE COLUMN: Report Details Form */}
          <div className="flex-1 bg-white rounded-xl border border-slate-200 shadow-sm p-6 lg:p-8">
            <h3 className="text-[16px] font-bold text-slate-800 mb-6">Report Details</h3>
            
            <div className="grid grid-cols-1 md:grid-cols-2 gap-x-6 gap-y-5">
              
              {/* Patient */}
              <div>
                <label className="block text-[13px] font-bold text-slate-700 mb-2">Patient <span className="text-red-500">*</span></label>
                <div className="relative">
                  <input type="text" placeholder="Search and select patient" className="w-full pl-4 pr-10 py-2.5 border border-slate-200 rounded-lg text-[13px] font-medium text-slate-700 focus:outline-none focus:border-[#5B21B6]" />
                  <ChevronDown size={16} className="absolute right-3 top-1/2 -translate-y-1/2 text-slate-400" />
                </div>
              </div>

              {/* Report Type */}
              <div>
                <label className="block text-[13px] font-bold text-slate-700 mb-2">Report Type <span className="text-red-500">*</span></label>
                <div className="relative">
                  <input type="text" placeholder="Select report type" className="w-full pl-4 pr-10 py-2.5 border border-slate-200 rounded-lg text-[13px] font-medium text-slate-700 focus:outline-none focus:border-[#5B21B6]" />
                  <ChevronDown size={16} className="absolute right-3 top-1/2 -translate-y-1/2 text-slate-400" />
                </div>
              </div>

              {/* Test/Report Name */}
              <div>
                <label className="block text-[13px] font-bold text-slate-700 mb-2">Test/Report Name <span className="text-red-500">*</span></label>
                <input type="text" placeholder="Enter test or report name" className="w-full px-4 py-2.5 border border-slate-200 rounded-lg text-[13px] font-medium text-slate-700 focus:outline-none focus:border-[#5B21B6]" />
              </div>

              {/* Test Date */}
              <div>
                <label className="block text-[13px] font-bold text-slate-700 mb-2">Test Date <span className="text-red-500">*</span></label>
                <div className="relative">
                  <Calendar size={16} className="absolute left-3 top-1/2 -translate-y-1/2 text-slate-400" />
                  <input type="text" placeholder="Select test date" className="w-full pl-10 pr-10 py-2.5 border border-slate-200 rounded-lg text-[13px] font-medium text-slate-700 focus:outline-none focus:border-[#5B21B6]" />
                  <Calendar size={16} className="absolute right-3 top-1/2 -translate-y-1/2 text-slate-400" />
                </div>
              </div>

              {/* Report Date */}
              <div>
                <label className="block text-[13px] font-bold text-slate-700 mb-2">Report Date <span className="text-red-500">*</span></label>
                <div className="relative">
                  <Calendar size={16} className="absolute left-3 top-1/2 -translate-y-1/2 text-slate-400" />
                  <input type="text" placeholder="Select report date" className="w-full pl-10 pr-10 py-2.5 border border-slate-200 rounded-lg text-[13px] font-medium text-slate-700 focus:outline-none focus:border-[#5B21B6]" />
                </div>
              </div>

              {/* Lab/Center Name */}
              <div>
                <label className="block text-[13px] font-bold text-slate-700 mb-2">Lab/Center Name <span className="text-red-500">*</span></label>
                <input type="text" placeholder="Enter lab or center name" className="w-full px-4 py-2.5 border border-slate-200 rounded-lg text-[13px] font-medium text-slate-700 focus:outline-none focus:border-[#5B21B6]" />
              </div>

            </div>

            {/* Ref. Doctor */}
            <div className="mt-5">
              <label className="block text-[13px] font-bold text-slate-700 mb-2">Ref. Doctor (Optional)</label>
              <div className="relative">
                <input type="text" placeholder="Select referring doctor" className="w-full pl-4 pr-10 py-2.5 border border-slate-200 rounded-lg text-[13px] font-medium text-slate-700 focus:outline-none focus:border-[#5B21B6]" />
                <ChevronDown size={16} className="absolute right-3 top-1/2 -translate-y-1/2 text-slate-400" />
              </div>
            </div>

            {/* Notes */}
            <div className="mt-5">
              <label className="block text-[13px] font-bold text-slate-700 mb-2">Notes (Optional)</label>
              <textarea 
                placeholder="Enter additional notes about the report" 
                rows={4}
                className="w-full px-4 py-3 border border-slate-200 rounded-lg text-[13px] font-medium text-slate-700 focus:outline-none focus:border-[#5B21B6] resize-none"
              ></textarea>
            </div>
            
          </div>

          {/* RIGHT COLUMN: Selected Files & Actions */}
          <div className="w-full lg:w-[350px] flex-shrink-0 flex flex-col justify-between">
            
            <div className="flex flex-col gap-5">
              <div className="flex items-center justify-between px-1">
                <h3 className="text-[14px] font-bold text-slate-800">Selected Files (2)</h3>
                <span className="text-[12px] font-medium text-slate-500">Total Size: 3.4 MB</span>
              </div>

              <div className="flex flex-col gap-3">
                {/* File 1 */}
                <div className="flex items-center justify-between p-3 border border-slate-200 rounded-xl bg-white shadow-sm">
                  <div className="flex items-center gap-3 overflow-hidden pr-2">
                    <div className="w-10 h-10 rounded bg-red-50 text-red-600 flex items-center justify-center flex-shrink-0">
                      <FileText size={18} strokeWidth={2.5} />
                    </div>
                    <div className="min-w-0">
                      <p className="text-[13px] font-bold text-slate-800 truncate">CBC_Report_Robert_Williams.pdf</p>
                      <p className="text-[11px] font-medium text-slate-500">1.2 MB</p>
                    </div>
                  </div>
                  <button className="flex-shrink-0 w-7 h-7 flex items-center justify-center text-slate-400 hover:bg-slate-100 hover:text-slate-600 rounded">
                    <X size={16} strokeWidth={2.5} />
                  </button>
                </div>

                {/* File 2 */}
                <div className="flex items-center justify-between p-3 border border-slate-200 rounded-xl bg-white shadow-sm">
                  <div className="flex items-center gap-3 overflow-hidden pr-2">
                    <div className="w-10 h-10 rounded bg-red-50 text-red-600 flex items-center justify-center flex-shrink-0">
                      <FileText size={18} strokeWidth={2.5} />
                    </div>
                    <div className="min-w-0">
                      <p className="text-[13px] font-bold text-slate-800 truncate">Thyroid_Profile_Report.pdf</p>
                      <p className="text-[11px] font-medium text-slate-500">2.2 MB</p>
                    </div>
                  </div>
                  <button className="flex-shrink-0 w-7 h-7 flex items-center justify-center text-slate-400 hover:bg-slate-100 hover:text-slate-600 rounded">
                    <X size={16} strokeWidth={2.5} />
                  </button>
                </div>
              </div>

              {/* Security Note */}
              <div className="bg-[#FAF5FF] border border-[#F3E8FF] rounded-xl p-4 flex gap-3">
                <ShieldCheck size={20} className="text-[#9333EA] flex-shrink-0" strokeWidth={2} />
                <div>
                  <h4 className="text-[13px] font-bold text-slate-800 mb-1">Files are secure and encrypted</h4>
                  <p className="text-[11px] font-medium text-slate-600 leading-relaxed">
                    Your uploaded reports are safely stored and managed in compliance with privacy standards.
                  </p>
                </div>
              </div>
            </div>

            {/* Bottom Actions */}
            <div className="flex items-center gap-3 mt-8">
              <button 
                onClick={() => navigate('/doctor/lab-reports')}
                className="flex-1 py-3 border border-slate-200 rounded-lg text-[14px] font-bold text-slate-700 hover:bg-slate-50 transition-colors bg-white"
              >
                Cancel
              </button>
              <button className="flex-1 py-3 bg-[#5B21B6] hover:bg-indigo-800 text-white rounded-lg text-[14px] font-bold shadow-sm transition-colors flex items-center justify-center gap-2">
                <UploadCloud size={18} strokeWidth={2.5} /> Upload Report
              </button>
            </div>
            
          </div>
        </div>

      </div>
    </div>
  );
};

export default UploadLabReport;
