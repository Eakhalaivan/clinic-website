import React from 'react';
import { RefreshCcw, ArrowLeft, Clock } from 'lucide-react';
import { Link } from 'react-router-dom';
import { motion } from 'framer-motion';
import toast from 'react-hot-toast';
import Card from '../../components/ui/Card';
import Button from '../../components/ui/Button';
import { fadeIn } from '../../components/ui/motion';
import EmptyState from '../../components/ui/EmptyState';

const FinanceRefunds = () => {
  const handleMockRefund = () => {
    toast.error('Refunds API pending. This feature is not yet available in the backend.', {
      icon: '⏳',
    });
  };

  return (
    <motion.div 
      initial="hidden" 
      animate="visible" 
      variants={fadeIn}
      className="max-w-4xl mx-auto space-y-6"
    >
      <div className="flex items-center justify-between gap-4">
        <div>
          <Link to="/finance" className="inline-flex items-center text-xs font-semibold text-[var(--color-navy-600)] hover:underline mb-2 gap-1">
            <ArrowLeft className="w-3.5 h-3.5" /> Back to Dashboard
          </Link>
          <h1 className="text-2xl sm:text-3xl font-bold font-display text-[var(--color-navy-900)] m-0 flex items-center gap-2">
            <RefreshCcw className="w-7 h-7 text-amber-500" />
            Refunds Management
          </h1>
          <p className="text-sm text-[var(--color-text-muted)] m-0 mt-1">
            Process patient refunds for overpayments or cancelled services.
          </p>
        </div>
      </div>

      <Card>
        <Card.Body className="p-12 text-center flex flex-col items-center justify-center">
          <div className="w-16 h-16 bg-amber-100 rounded-full flex items-center justify-center mb-6">
            <Clock className="w-8 h-8 text-amber-500" />
          </div>
          <h2 className="text-xl font-bold text-[var(--color-navy-900)] mb-3">Feature Coming Soon</h2>
          <p className="text-sm text-slate-500 max-w-md mb-8">
            The backend API for processing and tracking refunds is currently under development. This module will be enabled in a future update.
          </p>
          <Button variant="primary" onClick={handleMockRefund} icon={RefreshCcw}>
            Simulate Refund Request
          </Button>
        </Card.Body>
      </Card>
      
      <div className="opacity-50 pointer-events-none">
        <Card>
          <Card.Header>
            <h2 className="text-lg font-bold text-slate-400">Recent Refund History</h2>
          </Card.Header>
          <Card.Body className="p-8">
            <EmptyState icon={RefreshCcw} title="No Data" description="Refund history is currently unavailable." />
          </Card.Body>
        </Card>
      </div>
    </motion.div>
  );
};

export default FinanceRefunds;
