import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { axiosPrivate } from '../../api/axios';
import { Video, PhoneOff, Mic, MicOff, VideoOff } from 'lucide-react';

const TeleconsultationRoom = () => {
  const { appointmentId } = useParams();
  const navigate = useNavigate();
  const [meetingUrl, setMeetingUrl] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [audioMuted, setAudioMuted] = useState(false);
  const [videoMuted, setVideoMuted] = useState(false);

  useEffect(() => {
    const fetchRoom = async () => {
      try {
        const res = await axiosPrivate.get(`/api/v1/telemedicine/room/${appointmentId}`);
        setMeetingUrl(res.data.meetingUrl);
      } catch (err) {
        setError('Failed to fetch meeting URL. Please try again later.');
      } finally {
        setLoading(false);
      }
    };
    if (appointmentId) fetchRoom();
  }, [appointmentId]);

  const handleLeave = () => {
    if (window.confirm('Are you sure you want to leave the consultation?')) {
      navigate(-1);
    }
  };

  if (loading) return <div className="p-8 text-center text-slate-500">Initializing secure connection...</div>;
  if (error) return <div className="p-8 text-center text-red-500 bg-red-50">{error}</div>;

  return (
    <div className="flex flex-col h-[calc(100vh-4rem)] bg-slate-900 text-white rounded-xl overflow-hidden shadow-2xl m-4">
      <div className="flex justify-between items-center px-6 py-4 bg-slate-800 border-b border-slate-700">
        <h2 className="text-xl font-bold flex items-center gap-2">
          <Video className="text-blue-400" /> 
          Teleconsultation Room
        </h2>
        <span className="text-sm text-slate-400 font-medium tracking-wide">Appointment #{appointmentId}</span>
      </div>
      
      <div className="flex-1 bg-black relative">
        {meetingUrl ? (
          <iframe 
            src={meetingUrl} 
            allow="camera; microphone; fullscreen; display-capture"
            className="w-full h-full border-0"
            title="Teleconsultation Video"
          />
        ) : (
          <div className="flex items-center justify-center h-full text-slate-500">
            Connecting to video stream...
          </div>
        )}
      </div>

      <div className="flex justify-center items-center gap-6 py-4 bg-slate-800 border-t border-slate-700">
        <button 
          onClick={() => setAudioMuted(!audioMuted)}
          className={`p-4 rounded-full transition-all ${audioMuted ? 'bg-red-500/20 text-red-500' : 'bg-slate-700 text-slate-300 hover:bg-slate-600'}`}
          title={audioMuted ? "Unmute Audio" : "Mute Audio"}
        >
          {audioMuted ? <MicOff size={24} /> : <Mic size={24} />}
        </button>
        
        <button 
          onClick={handleLeave}
          className="p-4 bg-red-600 text-white rounded-full hover:bg-red-700 shadow-lg shadow-red-900/50 transition-all"
          title="Leave Consultation"
        >
          <PhoneOff size={24} />
        </button>

        <button 
          onClick={() => setVideoMuted(!videoMuted)}
          className={`p-4 rounded-full transition-all ${videoMuted ? 'bg-red-500/20 text-red-500' : 'bg-slate-700 text-slate-300 hover:bg-slate-600'}`}
          title={videoMuted ? "Enable Video" : "Disable Video"}
        >
          {videoMuted ? <VideoOff size={24} /> : <Video size={24} />}
        </button>
      </div>
    </div>
  );
};

export default TeleconsultationRoom;
