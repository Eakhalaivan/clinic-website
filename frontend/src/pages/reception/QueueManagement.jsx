import React from 'react';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { axiosPrivate } from '../../api/axios';
import { Ticket, Plus, Clock, ArrowLeft } from 'lucide-react';
import { Link } from 'react-router-dom';
import { motion } from 'framer-motion';
import toast from 'react-hot-toast';
import Card from '../../components/ui/Card';
import Button from '../../components/ui/Button';
import Badge from '../../components/ui/Badge';
import EmptyState from '../../components/ui/EmptyState';
import Skeleton from '../../components/ui/Skeleton';
import { fadeIn, staggerChildren } from '../../components/ui/motion';

const QueueManagement = () => {
  const queryClient = useQueryClient();
  const branchId = 1;

  const { data: queue, isLoading } = useQuery({
    queryKey: ['queueTokens', branchId],
    queryFn: async () => {
      const res = await axiosPrivate.get(`/reception/branches/${branchId}/queue`);
      return res.data;
    }
  });

  const generateToken = useMutation({
    mutationFn: async () => {
      const res = await axiosPrivate.post(`/reception/branches/${branchId}/queue/generate`);
      return res.data;
    },
    onSuccess: (data) => {
      toast.success(`Token ${data?.tokenNumber || 'generated'} created successfully!`);
      queryClient.invalidateQueries(['queueTokens', branchId]);
    },
    onError: () => {
      toast.error('Failed to generate token');
    }
  });

  const queueList = queue || [];

  return (
    <motion.div 
      initial="hidden" 
      animate="visible" 
      variants={staggerChildren}
      className="space-y-6"
    >
      <div>
        <Link to="/reception" className="inline-flex items-center text-xs font-semibold text-[var(--color-navy-600)] hover:underline mb-2 gap-1">
          <ArrowLeft className="w-3.5 h-3.5" /> Back to Reception Desk
        </Link>
        <h1 className="text-2xl sm:text-3xl font-bold font-display text-[var(--color-navy-900)] m-0 flex items-center gap-2">
          <Ticket className="w-7 h-7 text-[var(--color-navy-800)]" />
          Queue Management
        </h1>
        <p className="text-sm text-[var(--color-text-muted)] m-0 mt-1">
          Issue sequential walk-in tokens and monitor live patient waiting status.
        </p>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        {/* Token Generator Panel */}
        <Card className="lg:col-span-1">
          <Card.Header>
            <h2 className="font-display font-bold text-lg text-[var(--color-navy-900)] m-0 flex items-center gap-2">
              <Plus className="w-5 h-5 text-[var(--color-navy-800)]" />
              Generate Token
            </h2>
          </Card.Header>
          <Card.Body className="space-y-6 text-center">
            <p className="text-sm text-[var(--color-text-muted)] m-0 leading-relaxed">
              Generate a new sequential token for walk-in consultation or general inquiry.
            </p>
            <Button
              variant="primary"
              size="lg"
              fullWidth
              icon={Ticket}
              isLoading={generateToken.isPending}
              onClick={() => generateToken.mutate()}
            >
              Generate Next Token
            </Button>
          </Card.Body>
        </Card>

        {/* Active Queue Status */}
        <Card className="lg:col-span-2">
          <Card.Header>
            <div className="flex items-center justify-between w-full">
              <h2 className="font-display font-bold text-lg text-[var(--color-navy-900)] m-0 flex items-center gap-2">
                <Clock className="w-5 h-5 text-[var(--color-navy-800)]" />
                Active Waiting Queue
              </h2>
              <Badge variant="warning">{queueList.length} Waiting</Badge>
            </div>
          </Card.Header>
          <Card.Body>
            {isLoading ? (
              <div className="grid grid-cols-2 sm:grid-cols-3 gap-4">
                <Skeleton count={3} variant="card" className="h-28" />
              </div>
            ) : queueList.length === 0 ? (
              <EmptyState
                icon={Ticket}
                title="Queue is empty"
                description="There are currently no waiting tokens in the reception queue."
              />
            ) : (
              <div className="grid grid-cols-2 sm:grid-cols-3 md:grid-cols-4 gap-4">
                {queueList.map((q) => (
                  <motion.div
                    key={q.id}
                    variants={fadeIn}
                    className="p-4 rounded-md border border-[var(--color-info)]/30 bg-[var(--color-info-bg)]/40 text-center flex flex-col items-center justify-center gap-1 shadow-sm hover:shadow-card transition-shadow"
                  >
                    <span className="text-[11px] font-bold uppercase tracking-wider text-[var(--color-info)]">
                      TOKEN
                    </span>
                    <span className="text-3xl font-extrabold font-display text-[var(--color-navy-900)]">
                      {q.tokenNumber}
                    </span>
                  </motion.div>
                ))}
              </div>
            )}
          </Card.Body>
        </Card>
      </div>
    </motion.div>
  );
};

export default QueueManagement;
