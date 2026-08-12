import React, { useState } from 'react';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { axiosPrivate } from '../../api/axios';
import { FilePlus, FileText, Download, Loader2, File, Activity, Pill } from 'lucide-react';
import useAuthStore from '../../store/authStore';

const getDocumentIcon = (type) => {
    const t = type.toLowerCase();
    if (t.includes('lab')) return <Activity size={20} className="text-rose-500" />;
    if (t.includes('prescription')) return <Pill size={20} className="text-emerald-500" />;
    return <FileText size={20} className="text-blue-500" />;
};

const PatientDocuments = () => {
    const { user } = useAuthStore();
    const queryClient = useQueryClient();
    
    const [title, setTitle] = useState('');
    const [documentType, setDocumentType] = useState('Medical Record');
    const [isUploading, setIsUploading] = useState(false);

    const { data: documents, isLoading } = useQuery({
        queryKey: ['patientDocuments'],
        queryFn: async () => {
            const res = await axiosPrivate.get('/api/v1/patient/documents');
            return res.data;
        }
    });

    const uploadMutation = useMutation({
        mutationFn: async (newDoc) => {
            const res = await axiosPrivate.post('/api/v1/patient/documents', newDoc);
            return res.data;
        },
        onSuccess: () => {
            queryClient.invalidateQueries(['patientDocuments']);
            setTitle('');
            setDocumentType('Medical Record');
            setIsUploading(false);
            alert("Document uploaded successfully!");
        }
    });

    const handleUpload = (e) => {
        e.preventDefault();
        if (!title.trim()) return;
        
        setIsUploading(true);
        // Simulate file upload delay
        setTimeout(() => {
            uploadMutation.mutate({
                title,
                documentType
            });
        }, 1000);
    };

    if (isLoading) {
        return <div className="flex justify-center items-center h-64"><Loader2 className="animate-spin text-indigo-600 w-8 h-8" /></div>;
    }

    return (
        <div className="p-4 sm:p-6 lg:p-8 max-w-5xl mx-auto">
            <div className="mb-8 flex flex-col sm:flex-row sm:items-center justify-between gap-4">
                <div>
                    <h2 className="text-2xl font-bold text-slate-800">My Documents</h2>
                    <p className="text-slate-500 mt-1">Manage your lab reports, prescriptions, and medical records.</p>
                </div>
            </div>

            <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
                
                {/* Upload Form */}
                <div className="lg:col-span-1">
                    <div className="bg-white rounded-3xl p-6 border border-slate-200 shadow-sm sticky top-6">
                        <div className="flex items-center gap-3 mb-6">
                            <div className="w-10 h-10 rounded-xl bg-indigo-50 text-indigo-600 flex items-center justify-center">
                                <FilePlus size={20} />
                            </div>
                            <h3 className="text-lg font-semibold text-slate-800">Upload Document</h3>
                        </div>
                        
                        <form onSubmit={handleUpload} className="space-y-4">
                            <div>
                                <label className="block text-sm font-medium text-slate-700 mb-1">Document Title</label>
                                <input 
                                    type="text" 
                                    required
                                    value={title}
                                    onChange={(e) => setTitle(e.target.value)}
                                    placeholder="e.g. Blood Test Results"
                                    className="w-full px-4 py-2.5 bg-slate-50 border border-slate-200 rounded-xl focus:ring-2 focus:ring-indigo-500 outline-none transition"
                                />
                            </div>
                            
                            <div>
                                <label className="block text-sm font-medium text-slate-700 mb-1">Document Type</label>
                                <select 
                                    value={documentType}
                                    onChange={(e) => setDocumentType(e.target.value)}
                                    className="w-full px-4 py-2.5 bg-slate-50 border border-slate-200 rounded-xl focus:ring-2 focus:ring-indigo-500 outline-none transition"
                                >
                                    <option value="Medical Record">Medical Record</option>
                                    <option value="Lab Report">Lab Report</option>
                                    <option value="Prescription">Prescription</option>
                                    <option value="Other">Other</option>
                                </select>
                            </div>
                            
                            <div>
                                <label className="block text-sm font-medium text-slate-700 mb-1">File</label>
                                <div className="border-2 border-dashed border-slate-300 rounded-xl p-6 text-center hover:bg-slate-50 transition cursor-pointer">
                                    <File size={24} className="mx-auto text-slate-400 mb-2" />
                                    <span className="text-sm text-indigo-600 font-medium">Click to upload</span>
                                    <p className="text-xs text-slate-500 mt-1">PDF, JPG, PNG (Max 5MB)</p>
                                </div>
                            </div>
                            
                            <button 
                                type="submit" 
                                disabled={isUploading || !title.trim()}
                                className="w-full mt-2 bg-indigo-600 text-white font-medium py-3 rounded-xl hover:bg-indigo-700 disabled:opacity-70 transition flex items-center justify-center gap-2"
                            >
                                {isUploading ? <><Loader2 size={18} className="animate-spin" /> Uploading...</> : 'Save Document'}
                            </button>
                        </form>
                    </div>
                </div>

                {/* Documents List */}
                <div className="lg:col-span-2">
                    {documents?.length === 0 ? (
                        <div className="bg-white rounded-3xl p-10 text-center border border-slate-200 shadow-sm h-full flex flex-col items-center justify-center">
                            <div className="w-20 h-20 bg-slate-50 rounded-full flex items-center justify-center mb-4">
                                <FileText size={32} className="text-slate-300" />
                            </div>
                            <h3 className="text-lg font-semibold text-slate-800 mb-2">No Documents Yet</h3>
                            <p className="text-slate-500 max-w-sm">Upload your first medical document using the form to keep your records organized.</p>
                        </div>
                    ) : (
                        <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
                            {documents?.map((doc) => (
                                <div key={doc.id} className="bg-white p-5 rounded-2xl border border-slate-200 shadow-sm hover:shadow-md transition-shadow group">
                                    <div className="flex items-start justify-between mb-4">
                                        <div className="w-12 h-12 rounded-xl bg-slate-50 flex items-center justify-center border border-slate-100 group-hover:bg-indigo-50 group-hover:border-indigo-100 transition-colors">
                                            {getDocumentIcon(doc.documentType)}
                                        </div>
                                        <span className="text-[10px] font-bold tracking-wider uppercase text-slate-400 bg-slate-100 px-2.5 py-1 rounded-md">
                                            {doc.documentType}
                                        </span>
                                    </div>
                                    <h3 className="font-semibold text-slate-800 line-clamp-1 mb-1" title={doc.title}>{doc.title}</h3>
                                    <p className="text-xs text-slate-500 mb-4">
                                        Uploaded on {new Date(doc.uploadedAt).toLocaleDateString()}
                                    </p>
                                    
                                    <a 
                                        href={doc.fileUrl} 
                                        target="_blank" 
                                        rel="noopener noreferrer"
                                        className="flex items-center justify-center gap-2 w-full py-2.5 bg-slate-50 hover:bg-indigo-50 text-slate-700 hover:text-indigo-700 text-sm font-medium rounded-xl transition-colors"
                                    >
                                        <Download size={16} /> Download
                                    </a>
                                </div>
                            ))}
                        </div>
                    )}
                </div>
            </div>
        </div>
    );
};

export default PatientDocuments;
