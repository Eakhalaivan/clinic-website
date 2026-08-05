import React, { useState } from 'react';
import { useMutation } from '@tanstack/react-query';
import { axiosPrivate } from '../../api/axios';
import useAuthStore from '../../store/authStore';

const PatientConsent = () => {
  const { user } = useAuthStore();
  const [signature, setSignature] = useState('');
  
  const consentMutation = useMutation({
    mutationFn: async (data) => {
      // In a real implementation this would hit a POST /api/v1/consent endpoint
      // const res = await axiosPrivate.post('/api/v1/consent', data);
      // return res.data;
      return new Promise(resolve => setTimeout(() => resolve({ success: true }), 1000));
    },
    onSuccess: () => {
      alert('Consent form signed successfully!');
      setSignature('');
    }
  });

  const handleSign = (e) => {
    e.preventDefault();
    if (!signature) {
      alert("Please type your signature to agree.");
      return;
    }
    consentMutation.mutate({
      patientId: user?.id,
      formType: 'GENERAL_CONSENT',
      signatureData: signature
    });
  };

  return (
    <div className="p-8 max-w-2xl mx-auto">
      <h2 className="text-2xl font-bold mb-4 text-slate-800">General Consent for Treatment</h2>
      
      <div className="bg-white p-6 rounded-lg shadow-sm border border-slate-200 mb-6">
        <p className="mb-4 text-slate-600 leading-relaxed">
          I hereby authorize the medical staff of ClinicApp to provide me with medical treatment and care. 
          I understand that I have the right to be informed about my condition and the recommended surgical, 
          medical, or diagnostic procedure to be used so that I may make an informed decision whether or not 
          to undergo the procedure after knowing the risks and hazards involved.
        </p>
        <p className="text-slate-600 leading-relaxed">
          By signing below, I acknowledge that I have read and understand this consent form, and I voluntarily 
          agree to the treatments and procedures as deemed necessary by my attending physicians.
        </p>
      </div>

      <form onSubmit={handleSign} className="bg-white p-6 rounded-lg shadow-sm border border-slate-200">
        <div className="mb-4">
          <label className="block text-sm font-medium text-slate-700 mb-2">
            Electronic Signature (Type your full name)
          </label>
          <input 
            type="text" 
            value={signature}
            onChange={(e) => setSignature(e.target.value)}
            placeholder="John Doe"
            className="w-full p-3 border border-slate-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
            required
          />
        </div>
        
        <button 
          type="submit" 
          disabled={consentMutation.isPending}
          className="w-full bg-blue-600 text-white font-medium py-3 px-4 rounded-md hover:bg-blue-700 transition-colors disabled:opacity-50"
        >
          {consentMutation.isPending ? 'Submitting...' : 'Sign and Agree'}
        </button>
      </form>
    </div>
  );
};

export default PatientConsent;
