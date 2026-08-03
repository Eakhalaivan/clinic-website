import React, { useState } from 'react';
import { Bot, Send, User, X, MessageSquare } from 'lucide-react';

const AIAssistantWidget = () => {
  const [isOpen, setIsOpen] = useState(false);
  const [messages, setMessages] = useState([
    { sender: 'bot', text: 'Hello! I am your Aurelian Health AI Assistant. How can I help you today? You can ask about your symptoms, appointment booking, or clinic services.' }
  ]);
  const [input, setInput] = useState('');

  const send = () => {
    if (!input.trim()) return;
    const userMsg = { sender: 'user', text: input };
    setMessages(prev => [...prev, userMsg]);
    setInput('');

    setTimeout(() => {
      let reply = 'Thank you for your message. For acute medical symptoms, please book an appointment with our specialist doctor or contact emergency services.';
      if (input.toLowerCase().includes('fever') || input.toLowerCase().includes('headache')) {
        reply = 'Fever and headache can stem from viral infections or fatigue. Make sure to stay hydrated. If fever exceeds 101°F for > 2 days, please consult Dr. Ramesh Rao.';
      } else if (input.toLowerCase().includes('timing') || input.toLowerCase().includes('hour')) {
        reply = 'Aurelian Health Clinic is open Monday to Saturday from 08:00 AM to 08:00 PM. Emergency services operate 24/7.';
      }
      setMessages(prev => [...prev, { sender: 'bot', text: reply }]);
    }, 600);
  };

  return (
    <>
      {/* Floating Toggle Button */}
      {!isOpen && (
        <button
          onClick={() => setIsOpen(true)}
          className="fixed bottom-6 right-6 w-14 h-14 bg-[var(--color-primary)] text-white rounded-full shadow-lg flex items-center justify-center hover:bg-[#0369a1] transition-colors z-50 border-none cursor-pointer"
        >
          <MessageSquare size={24} />
        </button>
      )}

      {/* Chat Panel */}
      {isOpen && (
        <div className="fixed bottom-6 right-6 w-[350px] sm:w-[400px] h-[500px] bg-[var(--color-surface)] border border-[var(--color-border)] rounded-2xl shadow-xl flex flex-col z-50 overflow-hidden font-sans">
          
          {/* Header */}
          <div className="bg-[var(--color-primary)] p-4 flex items-center justify-between text-white shrink-0">
            <div className="flex items-center gap-2">
              <Bot size={20} />
              <h3 className="m-0 text-sm font-bold tracking-wide uppercase">AI Health Assistant</h3>
            </div>
            <button 
              onClick={() => setIsOpen(false)}
              className="bg-transparent border-none text-white cursor-pointer p-1 hover:bg-white/10 rounded"
            >
              <X size={18} />
            </button>
          </div>

          {/* Messages Area */}
          <div className="flex-1 overflow-y-auto p-4 bg-[var(--color-surface-alt)] flex flex-col gap-3">
            {messages.map((m, i) => (
              <div key={i} className={`flex ${m.sender === 'user' ? 'justify-end' : 'justify-start'}`}>
                <div 
                  className={`max-w-[80%] p-3 rounded-2xl text-sm leading-relaxed ${
                    m.sender === 'user' 
                      ? 'bg-[var(--color-primary)] text-white rounded-tr-sm' 
                      : 'bg-white text-[var(--color-text)] border border-[var(--color-border)] rounded-tl-sm shadow-sm'
                  }`}
                >
                  {m.text}
                </div>
              </div>
            ))}
          </div>

          {/* Input Area */}
          <div className="p-3 bg-[var(--color-surface)] border-t border-[var(--color-border)] flex items-center gap-2 shrink-0">
            <input
              type="text"
              value={input}
              onChange={e => setInput(e.target.value)}
              onKeyDown={e => e.key === 'Enter' && send()}
              placeholder="Type your question..."
              className="flex-1 input-field border-none bg-[var(--color-surface-alt)] rounded-full px-4 py-2.5 text-sm outline-none"
            />
            <button 
              onClick={send}
              className="bg-[var(--color-primary)] text-white border-none p-2.5 rounded-full cursor-pointer hover:opacity-90 flex items-center justify-center shrink-0"
            >
              <Send size={16} />
            </button>
          </div>

        </div>
      )}
    </>
  );
};

export default AIAssistantWidget;
