import React, { useState } from 'react';
import { Ticket, Users, Activity, Plus } from 'lucide-react';
import { motion } from 'framer-motion';
import toast from 'react-hot-toast';
import { axiosPrivate } from '../../api/axios';
import useAuthStore from '../../store/authStore';
import Card from '../../components/ui/Card';
import Button from '../../components/ui/Button';
import Input from '../../components/ui/Input';
import { staggerChildren } from '../../components/ui/motion';

const WalkInCheckIn = () => {
  const user = useAuthStore(state => state.user);
  const branchId = user?.branchId || 1;

  const [formData, setFormData] = useState({
    patientId: '',
    reasonForVisit: '',
    priorityLevel: 0,
    department: 'GENERAL'
  });
  const [loading, setLoading] = useState(false);
  const [issuedToken, setIssuedToken] = useState(null);

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!formData.reasonForVisit) {
      toast.error('Reason for visit is required');
      return;
    }

    try {
      setLoading(true);
      const res = await axiosPrivate.post(`/reception/walk-in/branch/${branchId}/register`, formData);
      toast.success('Walk-in registered successfully!');
      setIssuedToken(res.data);
      setFormData({
        patientId: '',
        reasonForVisit: '',
        priorityLevel: 0,
        department: 'GENERAL'
      });
    } catch (error) {
      toast.error('Failed to register walk-in');
      console.error(error);
    } finally {
      setLoading(false);
    }
  };

  return (
    <motion.div
      initial="hidden"
      animate="visible"
      variants={staggerChildren}
      className="max-w-4xl mx-auto space-y-6"
    >
      <div>
        <h1 className="text-2xl sm:text-3xl font-bold font-display text-[var(--color-navy-900)] flex items-center gap-2">
          <Ticket className="w-7 h-7 text-[var(--color-navy-800)]" />
          Walk-In Check-In
        </h1>
        <p className="text-sm text-[var(--color-text-muted)] mt-1">
          Register new walk-in patients and assign them to the correct queue.
        </p>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
        <Card>
          <Card.Header>
            <h2 className="font-display font-bold text-lg text-[var(--color-navy-900)]">Register Patient</h2>
          </Card.Header>
          <Card.Body>
            <form onSubmit={handleSubmit} className="space-y-4">
              <Input
                label="Patient ID (Optional if new)"
                placeholder="Enter registered patient ID..."
                value={formData.patientId}
                onChange={(e) => setFormData({ ...formData, patientId: e.target.value })}
              />
              <Input
                label="Reason for Visit"
                placeholder="e.g. Fever, Consultation"
                value={formData.reasonForVisit}
                onChange={(e) => setFormData({ ...formData, reasonForVisit: e.target.value })}
                required
              />
              
              <div className="space-y-1">
                <label className="text-sm font-semibold text-[var(--color-navy-900)]">Department</label>
                <select
                  className="w-full h-10 px-3 py-2 bg-transparent border border-[var(--color-border)] rounded-md focus:outline-none focus:ring-2 focus:ring-[var(--color-primary)] text-sm"
                  value={formData.department}
                  onChange={(e) => setFormData({ ...formData, department: e.target.value })}
                >
                  <option value="GENERAL">General</option>
                  <option value="CARDIOLOGY">Cardiology</option>
                  <option value="PEDIATRICS">Pediatrics</option>
                  <option value="ORTHOPEDICS">Orthopedics</option>
                  <option value="DERMATOLOGY">Dermatology</option>
                </select>
              </div>

              <div className="space-y-1">
                <label className="text-sm font-semibold text-[var(--color-navy-900)]">Priority</label>
                <select
                  className="w-full h-10 px-3 py-2 bg-transparent border border-[var(--color-border)] rounded-md focus:outline-none focus:ring-2 focus:ring-[var(--color-primary)] text-sm"
                  value={formData.priorityLevel}
                  onChange={(e) => setFormData({ ...formData, priorityLevel: parseInt(e.target.value) })}
                >
                  <option value={0}>Normal (0)</option>
                  <option value={1}>Urgent (1)</option>
                  <option value={2}>Emergency (2)</option>
                </select>
              </div>

              <div className="pt-2">
                <Button type="submit" variant="primary" fullWidth icon={Plus} isLoading={loading}>
                  Register & Generate Token
                </Button>
              </div>
            </form>
          </Card.Body>
        </Card>

        {issuedToken && (
          <Card>
            <Card.Header>
              <h2 className="font-display font-bold text-lg text-[var(--color-success)]">Token Generated</h2>
            </Card.Header>
            <Card.Body className="flex flex-col items-center justify-center space-y-4">
              <div className="text-center p-6 bg-[var(--color-success-bg)] rounded-lg border border-[var(--color-success)] w-full">
                <h3 className="text-5xl font-extrabold font-display text-[var(--color-success)] mb-2">
                  T-WAIT
                </h3>
                <p className="text-lg font-bold text-[var(--color-navy-900)]">
                  Walk-In ID: {issuedToken.id}
                </p>
                <p className="text-sm text-[var(--color-text-muted)] mt-1">
                  Reason: {issuedToken.reasonForVisit}
                </p>
                <p className="text-xs text-[var(--color-text-muted)] mt-4">
                  Please direct patient to the waiting area.
                </p>
              </div>
              <Button variant="outline" fullWidth onClick={() => setIssuedToken(null)}>
                Clear
              </Button>
            </Card.Body>
          </Card>
        )}
      </div>
    </motion.div>
  );
};

export default WalkInCheckIn;
