import React from 'react';
import { useQuery } from '@tanstack/react-query';
import { axiosPrivate } from '../../api/axios';
import { motion } from 'framer-motion';
import { FlaskConical, Download, CheckCircle2, Clock, AlertCircle } from 'lucide-react';
import { staggerChildren, fadeIn } from '../../components/ui/motion';
import Card from '../../components/ui/Card';
import Badge from '../../components/ui/Badge';
import EmptyState from '../../components/ui/EmptyState';
import Skeleton from '../../components/ui/Skeleton';

const statusConfig = {
  RELEASED:        { variant: 'success', label: 'Released',         icon: CheckCircle2 },
  RESULT_ENTERED:  { variant: 'info',    label: 'Result Entered',   icon: CheckCircle2 },
  IN_PROGRESS:     { variant: 'warning', label: 'In Progress',      icon: Clock },
  SAMPLE_COLLECTED:{ variant: 'warning', label: 'Sample Collected', icon: Clock },
  REQUESTED:       { variant: 'neutral', label: 'Requested',        icon: Clock },
};

const LabReports = () => {
  const { data: reports = [], isLoading, isError } = useQuery({
    queryKey: ['patient-lab-reports'],
    queryFn: async () => (await axiosPrivate.get('/lab/patient/lab-reports')).data,
    retry: 1,
  });

  return (
    <motion.div
      className="p-4 sm:p-6 max-w-4xl mx-auto"
      initial="hidden"
      animate="visible"
      variants={staggerChildren}
    >
      {/* Header */}
      <motion.div variants={fadeIn} className="flex items-center gap-3 mb-6">
        <div className="p-2.5 rounded-sm bg-[var(--color-info-bg)] text-[var(--color-info)]">
          <FlaskConical className="w-5 h-5" />
        </div>
        <div>
          <h1 className="font-display font-bold text-xl text-[var(--color-navy-900)] m-0">
            My Lab Reports
          </h1>
          <p className="text-xs text-[var(--color-text-muted)] m-0 mt-0.5">
            View all laboratory test requests and results
          </p>
        </div>
      </motion.div>

      {/* Loading */}
      {isLoading && (
        <Card>
          <Card.Body className="space-y-3">
            <Skeleton variant="line" lines={4} />
          </Card.Body>
        </Card>
      )}

      {/* Error */}
      {isError && !isLoading && (
        <EmptyState
          icon={AlertCircle}
          title="Could not load lab reports"
          description="Please try refreshing the page. If the problem persists, contact support."
        />
      )}

      {/* Empty */}
      {!isLoading && !isError && reports.length === 0 && (
        <EmptyState
          icon={FlaskConical}
          title="No lab reports yet"
          description="Your lab test requests will appear here once your doctor orders them."
        />
      )}

      {/* Report List */}
      {!isLoading && !isError && reports.length > 0 && (
        <motion.div variants={staggerChildren} className="space-y-3">
          {reports.map((r) => {
            const cfg = statusConfig[r.status] || statusConfig.REQUESTED;
            const testName = r.testCatalog?.testName ?? 'Lab Test';
            const doctorName = r.doctor
              ? `Dr. ${r.doctor.user?.firstName ?? ''} ${r.doctor.user?.lastName ?? ''}`.trim()
              : 'Unknown Doctor';
            const requestedDate = r.requestedAt
              ? new Date(r.requestedAt).toLocaleDateString('en-IN', {
                  day: 'numeric', month: 'short', year: 'numeric'
                })
              : '—';

            return (
              <motion.div key={r.id} variants={fadeIn}>
                <Card hoverable>
                  <Card.Body className="flex items-center justify-between gap-4 flex-wrap">
                    <div className="flex items-start gap-3 min-w-0">
                      <div className="p-2 rounded-sm bg-[var(--color-info-bg)] text-[var(--color-info)] shrink-0 mt-0.5">
                        <FlaskConical className="w-4 h-4" />
                      </div>
                      <div className="min-w-0">
                        <p className="font-semibold text-sm text-[var(--color-navy-900)] m-0 truncate">
                          {testName}
                        </p>
                        <p className="text-xs text-[var(--color-text-muted)] m-0 mt-0.5">
                          Ordered by {doctorName} &middot; {requestedDate}
                        </p>
                      </div>
                    </div>

                    <div className="flex items-center gap-3 shrink-0">
                      <Badge variant={cfg.variant} icon={cfg.icon}>
                        {cfg.label}
                      </Badge>
                      {(r.status === 'RELEASED' || r.status === 'RESULT_ENTERED') && (
                        <button
                          className="inline-flex items-center gap-1.5 px-3 py-1.5 text-xs font-semibold
                            bg-[var(--color-navy-800)] text-white rounded-sm hover:bg-[var(--color-navy-900)]
                            transition-colors focus-visible:outline-none"
                          onClick={() => alert('PDF download coming soon.')}
                        >
                          <Download className="w-3.5 h-3.5" />
                          Download PDF
                        </button>
                      )}
                    </div>
                  </Card.Body>
                </Card>
              </motion.div>
            );
          })}
        </motion.div>
      )}
    </motion.div>
  );
};

export default LabReports;
