import React, { useState } from 'react';
import { useMutation } from '@tanstack/react-query';
import { axiosPrivate } from '../../api/axios';
import { Bot, User, AlertTriangle, Send } from 'lucide-react';

const PatientAiAssistant = () => {
    const [messages, setMessages] = useState([
        { sender: 'AI', content: 'Hello! I am your AI health assistant. I can help you understand your reports or find an appointment. I am NOT a doctor. If you are experiencing a medical emergency, please call 911 immediately.', isUrgent: false }
    ]);
    const [input, setInput] = useState('');
    
    // Hardcoded for demo
    const sessionId = 1;
    const userId = 1;
    const tenantId = 1;

    const sendMessage = useMutation({
        mutationFn: async (content) => {
            return (await axiosPrivate.post(`/api/ai/patient/session/${sessionId}/message?userId=${userId}&tenantId=${tenantId}&content=${encodeURIComponent(content)}`)).data;
        },
        onSuccess: (data) => {
            setMessages(prev => [...prev, { sender: 'AI', content: data.content, isUrgent: data.containsSafetyFlag }]);
        }
    });

    const handleSend = () => {
        if (!input.trim()) return;
        setMessages(prev => [...prev, { sender: 'USER', content: input, isUrgent: false }]);
        sendMessage.mutate(input);
        setInput('');
    };

    return (
        <div className="flex flex-col h-full bg-[var(--color-background)]">
            {/* Header */}
            <div className="bg-[var(--color-primary)] text-white p-4 flex items-center gap-2">
                <Bot size={24} />
                <div>
                    <h2 className="font-bold text-lg m-0">Healthcare AI Assistant</h2>
                    <p className="text-xs opacity-80 m-0">Not for emergencies. Answers are AI-generated.</p>
                </div>
            </div>

            {/* Chat Area */}
            <div className="flex-1 overflow-y-auto p-4 flex flex-col gap-4">
                {messages.map((msg, idx) => (
                    <div key={idx} className={`flex gap-3 ${msg.sender === 'USER' ? 'flex-row-reverse' : ''}`}>
                        <div className={`p-2 rounded-full h-fit ${msg.sender === 'USER' ? 'bg-[var(--color-primary-light)]' : 'bg-gray-200'}`}>
                            {msg.sender === 'USER' ? <User size={16} /> : <Bot size={16} />}
                        </div>
                        <div className={`max-w-[75%] p-3 rounded-2xl ${msg.sender === 'USER' ? 'bg-[var(--color-primary)] text-white rounded-tr-none' : 'bg-[var(--color-surface)] border border-[var(--color-border)] rounded-tl-none'}`}>
                            {msg.isUrgent && (
                                <div className="flex items-center gap-2 text-red-600 font-bold mb-2">
                                    <AlertTriangle size={16} /> EMERGENCY ALERT
                                </div>
                            )}
                            <p className="text-sm m-0">{msg.content}</p>
                        </div>
                    </div>
                ))}
                {sendMessage.isLoading && (
                    <div className="flex gap-3">
                         <div className="p-2 rounded-full h-fit bg-gray-200"><Bot size={16} /></div>
                         <div className="bg-[var(--color-surface)] border border-[var(--color-border)] p-3 rounded-2xl rounded-tl-none">
                             <span className="animate-pulse">Thinking...</span>
                         </div>
                    </div>
                )}
            </div>

            {/* Input Area */}
            <div className="p-4 bg-[var(--color-surface)] border-t border-[var(--color-border)] flex gap-2">
                <input 
                    type="text" 
                    value={input}
                    onChange={(e) => setInput(e.target.value)}
                    onKeyPress={(e) => e.key === 'Enter' && handleSend()}
                    placeholder="Ask about your health or appointments..."
                    className="flex-1 p-2 border border-[var(--color-border)] rounded-lg"
                />
                <button 
                    onClick={handleSend}
                    disabled={sendMessage.isLoading}
                    className="p-2 bg-[var(--color-primary)] text-white rounded-lg hover:opacity-90 disabled:opacity-50"
                >
                    <Send size={20} />
                </button>
            </div>
        </div>
    );
};
export default PatientAiAssistant;
