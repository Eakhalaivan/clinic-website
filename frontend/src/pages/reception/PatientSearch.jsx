import React, { useState } from 'react';
import { useQuery } from '@tanstack/react-query';
import { axiosPrivate } from '../../api/axios';
import { Search, UserPlus, CheckCircle2, AlertCircle } from 'lucide-react';
import { Link } from 'react-router-dom';
import { motion } from 'framer-motion';
import Card from '../../components/ui/Card';
import Button from '../../components/ui/Button';
import FormField from '../../components/ui/FormField';
import { fadeIn } from '../../components/ui/motion';
import toast from 'react-hot-toast';

const PatientSearch = () => {
  const [searchParams, setSearchParams] = useState({ query: '', opNumber: '' });
  const [activeSearch, setActiveSearch] = useState(null);

  const { data: searchResults, isLoading, refetch } = useQuery({
    queryKey: ['patientSearch', activeSearch],
    queryFn: async () => {
      if (!activeSearch) return [];
      const res = await axiosPrivate.get('/reception/patients/search', { params: activeSearch });
      return res.data;
    },
    enabled: !!activeSearch
  });

  const handleSearch = (e) => {
    e.preventDefault();
    if (!searchParams.query && !searchParams.opNumber) {
      toast.error('Please enter a search term');
      return;
    }
    setActiveSearch(searchParams);
  };

  const handleVerifyIdentity = async (patientId) => {
    try {
      const res = await axiosPrivate.post(`/reception/patients/${patientId}/verify-identity`, {
        verificationMethod: 'STAFF_CONFIRMATION',
        documentReference: 'None'
      });
      if (res.data.status === 'SUCCESS') {
        toast.success('Identity verified successfully');
      }
    } catch (err) {
      toast.error('Failed to verify identity');
    }
  };

  return (
    <motion.div initial="hidden" animate="visible" variants={fadeIn} className="max-w-5xl mx-auto space-y-6">
      <div className="flex items-center justify-between gap-4">
        <div>
          <h1 className="text-2xl sm:text-3xl font-bold font-display text-[var(--color-navy-900)] m-0 flex items-center gap-2">
            <Search className="w-7 h-7 text-[var(--color-navy-800)]" />
            Patient Search
          </h1>
          <p className="text-sm text-[var(--color-text-muted)] m-0 mt-1">
            Search for existing patients or verify identity.
          </p>
        </div>
        <Link to="/reception/register">
          <Button variant="primary" icon={UserPlus}>New Patient</Button>
        </Link>
      </div>

      <Card>
        <Card.Body>
          <form onSubmit={handleSearch} className="flex gap-4 flex-wrap items-end">
            <div className="flex-1 min-w-[200px]">
              <FormField label="Search by Name/Phone" id="query">
                <input 
                  id="query"
                  type="text"
                  value={searchParams.query}
                  onChange={e => setSearchParams({ ...searchParams, query: e.target.value })}
                  placeholder="e.g. John or 9876543210"
                  className="input-field"
                />
              </FormField>
            </div>
            <div className="flex-1 min-w-[200px]">
              <FormField label="Search by OP Number" id="opNumber">
                <input 
                  id="opNumber"
                  type="text"
                  value={searchParams.opNumber}
                  onChange={e => setSearchParams({ ...searchParams, opNumber: e.target.value })}
                  placeholder="e.g. OP-2026-001"
                  className="input-field"
                />
              </FormField>
            </div>
            <Button type="submit" variant="primary" icon={Search} isLoading={isLoading}>Search</Button>
          </form>
        </Card.Body>
      </Card>

      {searchResults && (
        <Card>
          <div className="overflow-x-auto">
            <table className="min-w-full text-left border-collapse">
              <thead>
                <tr className="bg-[var(--color-background-alt)] border-b border-[var(--color-border)] text-sm font-medium text-[var(--color-text-muted)]">
                  <th className="p-4">OP Number</th>
                  <th className="p-4">Name</th>
                  <th className="p-4">Phone</th>
                  <th className="p-4">DOB/Gender</th>
                  <th className="p-4 text-right">Actions</th>
                </tr>
              </thead>
              <tbody className="divide-y divide-[var(--color-border)]">
                {searchResults.length === 0 ? (
                  <tr>
                    <td colSpan="5" className="p-8 text-center text-[var(--color-text-muted)] flex flex-col items-center justify-center gap-2">
                      <AlertCircle className="w-8 h-8 opacity-50" />
                      No patients found matching your search.
                    </td>
                  </tr>
                ) : (
                  searchResults.map(p => (
                    <tr key={p.id} className="hover:bg-slate-50/50 transition-colors">
                      <td className="p-4 font-medium text-[var(--color-navy-700)]">{p.opNumber || 'N/A'}</td>
                      <td className="p-4 font-semibold text-[var(--color-text-main)]">{p.firstName} {p.lastName}</td>
                      <td className="p-4 text-[var(--color-text-secondary)]">{p.phone}</td>
                      <td className="p-4 text-[var(--color-text-secondary)]">{p.dateOfBirth} • {p.gender}</td>
                      <td className="p-4 text-right">
                        <div className="flex justify-end gap-2">
                          <Button variant="outline" size="sm" icon={CheckCircle2} onClick={() => handleVerifyIdentity(p.id)}>
                            Verify Identity
                          </Button>
                        </div>
                      </td>
                    </tr>
                  ))
                )}
              </tbody>
            </table>
          </div>
        </Card>
      )}
    </motion.div>
  );
};

export default PatientSearch;
