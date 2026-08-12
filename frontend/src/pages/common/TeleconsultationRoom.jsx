import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { Video, Mic, MicOff, VideoOff, PhoneOff, MonitorUp, FileText, CheckSquare } from 'lucide-react';
import Button from '../../components/ui/Button';
import { axiosPrivate } from '../../api/axios';

const TeleconsultationRoom = () => {
    const { id } = useParams();
    const navigate = useNavigate();
    const [status, setStatus] = useState('WAITING'); // WAITING, ADMITTED, DISCONNECTED
    const [micEnabled, setMicEnabled] = useState(true);
    const [cameraEnabled, setCameraEnabled] = useState(true);

    useEffect(() => {
        // Simulate waiting room SSE
        const timer = setTimeout(() => {
            setStatus('ADMITTED');
        }, 3000);
        return () => clearTimeout(timer);
    }, [id]);

    const handleEndCall = () => {
        setStatus('DISCONNECTED');
        setTimeout(() => navigate('/patient/dashboard'), 2000);
    };

    if (status === 'WAITING') {
        return (
            <div className="min-h-screen bg-[var(--color-background)] flex items-center justify-center p-4">
                <div className="bg-[var(--color-surface)] border border-[var(--color-border)] p-8 rounded-2xl max-w-md w-full text-center shadow-lg">
                    <Video size={48} className="mx-auto text-[var(--color-primary)] mb-4 animate-pulse" />
                    <h2 className="text-xl font-bold mb-2">Virtual Waiting Room</h2>
                    <p className="text-[var(--color-text-muted)] mb-6">Your doctor has been notified and will admit you shortly. Please do not close this window.</p>
                    <div className="flex justify-center gap-4">
                        <Button variant="secondary" onClick={() => setMicEnabled(!micEnabled)}>
                            {micEnabled ? <Mic size={20} /> : <MicOff size={20} className="text-red-500" />}
                        </Button>
                        <Button variant="secondary" onClick={() => setCameraEnabled(!cameraEnabled)}>
                            {cameraEnabled ? <Video size={20} /> : <VideoOff size={20} className="text-red-500" />}
                        </Button>
                    </div>
                </div>
            </div>
        );
    }

    if (status === 'DISCONNECTED') {
        return (
            <div className="min-h-screen bg-gray-900 flex items-center justify-center">
                <div className="text-center text-white">
                    <CheckSquare size={48} className="mx-auto text-green-500 mb-4" />
                    <h2 className="text-2xl font-bold">Consultation Ended</h2>
                    <p className="mt-2 text-gray-400">Returning to dashboard...</p>
                </div>
            </div>
        );
    }

    // Admitted state
    return (
        <div className="min-h-screen bg-gray-900 flex flex-col">
            <div className="flex-1 p-4 flex gap-4">
                {/* Video Grid */}
                <div className="flex-1 bg-black rounded-xl overflow-hidden relative">
                    {/* Doctor Video placeholder */}
                    <div className="absolute inset-0 flex items-center justify-center text-gray-500">
                        <Video size={64} opacity={0.3} />
                        <span className="absolute bottom-4 left-4 text-white font-semibold">Dr. Smith</span>
                    </div>
                    {/* Patient PIP */}
                    <div className="absolute top-4 right-4 w-48 h-32 bg-gray-800 rounded-lg overflow-hidden border-2 border-gray-700">
                         <div className="absolute inset-0 flex items-center justify-center text-gray-500">
                            {cameraEnabled ? <Video size={32} /> : <VideoOff size={32} className="text-red-500"/>}
                         </div>
                    </div>
                </div>
                
                {/* Clinical Tools Panel (Only visible to doctor typically, shown here for structural demo) */}
                <div className="w-80 bg-gray-800 rounded-xl p-4 flex flex-col hidden sm:flex">
                    <h3 className="text-white font-bold mb-4 flex items-center gap-2"><FileText size={18} /> Shared Context</h3>
                    <div className="flex-1 text-sm text-gray-400">
                        <p>Patient: John Doe</p>
                        <p>Reason: Follow-up on hypertension.</p>
                        <hr className="border-gray-700 my-4" />
                        <p>No files shared yet.</p>
                    </div>
                </div>
            </div>

            {/* Controls */}
            <div className="h-20 bg-gray-800 flex items-center justify-center gap-6 px-4">
                <button onClick={() => setMicEnabled(!micEnabled)} className={`p-4 rounded-full ${micEnabled ? 'bg-gray-700 text-white' : 'bg-red-500 text-white'}`}>
                    {micEnabled ? <Mic size={24} /> : <MicOff size={24} />}
                </button>
                <button onClick={() => setCameraEnabled(!cameraEnabled)} className={`p-4 rounded-full ${cameraEnabled ? 'bg-gray-700 text-white' : 'bg-red-500 text-white'}`}>
                    {cameraEnabled ? <Video size={24} /> : <VideoOff size={24} />}
                </button>
                <button className="p-4 rounded-full bg-gray-700 text-white hidden sm:block">
                    <MonitorUp size={24} />
                </button>
                <button onClick={handleEndCall} className="p-4 rounded-full bg-red-600 text-white hover:bg-red-700">
                    <PhoneOff size={24} />
                </button>
            </div>
        </div>
    );
};
export default TeleconsultationRoom;
