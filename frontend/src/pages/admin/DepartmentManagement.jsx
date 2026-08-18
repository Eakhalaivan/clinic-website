import React, { useState } from 'react';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { Building, Plus, Edit2, Trash2 } from 'lucide-react';
import { axiosPrivate } from '../../api/axios';
import { motion } from 'framer-motion';
import DataTable from '../../components/ui/DataTable';
import Badge from '../../components/ui/Badge';
import Button from '../../components/ui/Button';
import { fadeIn } from '../../components/ui/motion';

const DepartmentManagement = () => {
    const queryClient = useQueryClient();
    const [page, setPage] = useState(0);
    const [size] = useState(10);

    const { data, isLoading } = useQuery({
        queryKey: ['admin-departments', page, size],
        queryFn: async () => {
            const res = await axiosPrivate.get(`/departments?page=${page}&size=${size}`);
            return res.data;
        }
    });

    const deleteMutation = useMutation({
        mutationFn: async (id) => axiosPrivate.delete(`/departments/${id}`),
        onSuccess: () => queryClient.invalidateQueries(['admin-departments'])
    });

    const deptList = Array.isArray(data) ? data : (data?.content || []);
    const totalPages = data?.totalPages || 1;

    const columns = [
        { key: 'id', title: 'ID' },
        { 
            key: 'name', 
            title: 'Department Name', 
            render: (val) => (
                <span className="font-semibold text-[var(--color-navy-900)]">
                    {val || '—'}
                </span>
            )
        },
        { key: 'description', title: 'Description', render: (val) => val || '—' },
        { key: 'headDoctorId', title: 'Head Doctor ID', render: (val) => val || '—' },
        {
            key: 'isActive',
            title: 'Status',
            render: (isActive) => (
                <Badge variant={isActive ? 'success' : 'danger'}>
                    {isActive ? 'Active' : 'Inactive'}
                </Badge>
            )
        },
        {
            key: 'actions',
            title: 'Actions',
            render: (_, row) => (
                <div className="flex items-center gap-2">
                    <Button variant="ghost" size="sm" className="text-[var(--color-navy-600)] hover:text-[var(--color-navy-800)] hover:bg-[var(--color-navy-50)]">
                        <Edit2 size={16} />
                    </Button>
                    <Button 
                        variant="ghost" 
                        size="sm" 
                        className="text-[var(--color-danger)] hover:bg-red-50"
                        onClick={() => {
                            if (window.confirm('Are you sure you want to delete this department?')) {
                                deleteMutation.mutate(row.id);
                            }
                        }}
                    >
                        <Trash2 size={16} />
                    </Button>
                </div>
            )
        }
    ];

    return (
        <motion.div initial="hidden" animate="visible" variants={fadeIn} className="space-y-6">
            <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
                <div>
                    <h1 className="text-2xl sm:text-3xl font-bold font-display text-[var(--color-navy-900)] m-0 flex items-center gap-2">
                        <Building className="w-7 h-7 text-[var(--color-navy-800)]" />
                        Clinical Departments
                    </h1>
                    <p className="text-sm text-[var(--color-text-muted)] m-0 mt-1">
                        Manage clinic departments, assign head doctors, and update details.
                    </p>
                </div>
                <Button className="bg-[var(--color-navy-600)] hover:bg-[var(--color-navy-700)] text-white font-medium flex items-center gap-2">
                    <Plus size={18} />
                    Add Department
                </Button>
            </div>

            <div className="bg-[var(--color-surface)] rounded-xl border border-[var(--color-border)] overflow-hidden">
                <DataTable 
                    columns={columns} 
                    data={deptList} 
                    isLoading={isLoading} 
                />
                
                {totalPages > 1 && (
                    <div className="p-4 border-t border-[var(--color-border)] flex items-center justify-between bg-gray-50/50">
                        <button
                            onClick={() => setPage(p => Math.max(0, p - 1))}
                            disabled={page === 0}
                            className="px-3 py-1.5 text-sm font-medium border border-gray-200 rounded bg-white hover:bg-gray-50 disabled:opacity-50"
                        >
                            Previous
                        </button>
                        <span className="text-sm text-gray-500">
                            Page {page + 1} of {totalPages}
                        </span>
                        <button
                            onClick={() => setPage(p => Math.min(totalPages - 1, p + 1))}
                            disabled={page >= totalPages - 1}
                            className="px-3 py-1.5 text-sm font-medium border border-gray-200 rounded bg-white hover:bg-gray-50 disabled:opacity-50"
                        >
                            Next
                        </button>
                    </div>
                )}
            </div>
        </motion.div>
    );
};

export default DepartmentManagement;
