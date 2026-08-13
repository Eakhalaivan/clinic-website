import React, { useState } from 'react';
import { Send, MessageSquare, ArrowLeft, Mail, Smartphone } from 'lucide-react';
import { Link } from 'react-router-dom';
import { motion } from 'framer-motion';
import toast from 'react-hot-toast';
import Card from '../../components/ui/Card';
import Button from '../../components/ui/Button';
import FormField from '../../components/ui/FormField';
import { fadeIn } from '../../components/ui/motion';
import EmptyState from '../../components/ui/EmptyState';

const MarketingCommunications = () => {
  const [form, setForm] = useState({
    recipient: '',
    channel: 'EMAIL',
    subject: '',
    message: ''
  });

  const handleSend = (e) => {
    e.preventDefault();
    if (!form.recipient || !form.message) {
      toast.error('Recipient and message are required');
      return;
    }
    toast.success('Mock communication sent successfully!');
    setForm({ recipient: '', channel: 'EMAIL', subject: '', message: '' });
  };

  return (
    <motion.div 
      initial="hidden" 
      animate="visible" 
      variants={fadeIn}
      className="max-w-6xl mx-auto space-y-6"
    >
      <div className="flex items-center justify-between gap-4">
        <div>
          <Link to="/marketing" className="inline-flex items-center text-xs font-semibold text-[var(--color-navy-600)] hover:underline mb-2 gap-1">
            <ArrowLeft className="w-3.5 h-3.5" /> Back to Dashboard
          </Link>
          <h1 className="text-2xl sm:text-3xl font-bold font-display text-[var(--color-navy-900)] m-0 flex items-center gap-2">
            <MessageSquare className="w-7 h-7 text-indigo-600" />
            Patient Communications
          </h1>
          <p className="text-sm text-[var(--color-text-muted)] m-0 mt-1">
            Send direct messages, appointment reminders, and follow-ups. (Mocked UI)
          </p>
        </div>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        <div className="space-y-6">
          <Card>
            <Card.Header>
              <h2 className="text-lg font-bold text-[var(--color-navy-900)]">Send New Message</h2>
            </Card.Header>
            <Card.Body>
              <form onSubmit={handleSend} className="space-y-4">
                <FormField label="Recipient (Patient Name or ID)" required id="recipient">
                  <input 
                    id="recipient" type="text" value={form.recipient} 
                    onChange={e => setForm({...form, recipient: e.target.value})} 
                    className="input-field" placeholder="e.g. John Doe (ID: 1023)" required
                  />
                </FormField>
                
                <FormField label="Communication Channel" id="channel">
                  <div className="flex gap-4">
                    <label className="flex items-center gap-2 cursor-pointer">
                      <input 
                        type="radio" name="channel" value="EMAIL" 
                        checked={form.channel === 'EMAIL'} 
                        onChange={e => setForm({...form, channel: e.target.value})} 
                        className="text-indigo-600 focus:ring-indigo-500"
                      />
                      <Mail size={16} className="text-slate-500" /> Email
                    </label>
                    <label className="flex items-center gap-2 cursor-pointer">
                      <input 
                        type="radio" name="channel" value="SMS" 
                        checked={form.channel === 'SMS'} 
                        onChange={e => setForm({...form, channel: e.target.value})} 
                        className="text-indigo-600 focus:ring-indigo-500"
                      />
                      <Smartphone size={16} className="text-slate-500" /> SMS Text
                    </label>
                  </div>
                </FormField>

                {form.channel === 'EMAIL' && (
                  <FormField label="Subject" required id="subject">
                    <input 
                      id="subject" type="text" value={form.subject} 
                      onChange={e => setForm({...form, subject: e.target.value})} 
                      className="input-field" placeholder="Message Subject"
                    />
                  </FormField>
                )}

                <FormField label="Message Body" required id="message">
                  <textarea 
                    id="message" value={form.message} 
                    onChange={e => setForm({...form, message: e.target.value})} 
                    className="input-field min-h-[120px]" placeholder="Type your message here..." required
                  />
                </FormField>

                <div className="pt-4 flex justify-end">
                  <Button type="submit" variant="primary" icon={Send}>
                    Send Message
                  </Button>
                </div>
              </form>
            </Card.Body>
          </Card>
        </div>

        <div className="opacity-75 pointer-events-none">
          <Card className="h-full">
            <Card.Header>
              <h2 className="text-lg font-bold text-slate-400">Communication History</h2>
            </Card.Header>
            <Card.Body className="flex items-center justify-center min-h-[400px]">
              <EmptyState icon={MessageSquare} title="History Unavailable" description="This is a mocked view. Message history API is not yet implemented." />
            </Card.Body>
          </Card>
        </div>
      </div>
    </motion.div>
  );
};

export default MarketingCommunications;
