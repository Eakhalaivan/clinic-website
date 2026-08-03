import React, { useState } from 'react';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { Users, Edit2, CheckCircle, XCircle } from 'lucide-react';
import { axiosPrivate } from '../../api/axios';
import { motion } from 'framer-motion';
import toast from 'react-hot-toast';
import Card from '../../components/ui/Card';
import Button from '../../components/ui/Button';
import Badge from '../../components/ui/Badge';
import DataTable from '../../components/ui/DataTable';
import Modal from '../../components/ui/Modal';
import FormField from '../../components/ui/FormField';
import { fadeIn } from '../../components/ui/motion';

const UserManagement = () => {
    const queryClient = useQueryClient();
    const [page, setPage] = useState(0);
    const [size] = useState(10);

    const [editingUser, setEditingUser] = useState(null);
    const [formData, setFormData] = useState({ firstName: '', lastName: '', email: '', enabled: true });

    const { data, isLoading } = useQuery({
        queryKey: ['users', page, size],
        queryFn: async () => {
            const res = await axiosPrivate.get(`/users?page=${page}&size=${size}`);
            return res.data;
        }
    });

    const updateMutation = useMutation({
        mutationFn: async (updatedData) => {
            const res = await axiosPrivate.put(`/users/${updatedData.id}`, updatedData);
            return res.data;
        },
        onSuccess: () => {
            toast.success('User updated successfully');
            queryClient.invalidateQueries(['users']);
            setEditingUser(null);
        },
        onError: () => {
            toast.error('Failed to update user');
        }
    });

    const toggleStatusMutation = useMutation({
        mutationFn: async (userId) => {
            await axiosPrivate.patch(`/users/${userId}/toggle-status`);
        },
        onSuccess: () => {
            toast.success('User status updated');
            queryClient.invalidateQueries(['users']);
        },
        onError: () => {
            toast.error('Failed to update user status');
        }
    });

    const handleEditClick = (user) => {
        setEditingUser(user);
        setFormData({
            id: user.id,
            firstName: user.firstName || user.name || '',
            lastName: user.lastName || '',
            email: user.email || '',
            enabled: user.enabled !== false
        });
    };

    const handleSave = (e) => {
        e.preventDefault();
        updateMutation.mutate(formData);
    };

    const userList = Array.isArray(data) ? data : (data?.content || []);
    const totalPages = data?.totalPages || (data?.last ? page + 1 : page + 2);

    const formatRoles = (user) => {
        if (!user || (!user.roles && !user.roleNames)) return 'USER';
        const roles = user.roles || user.roleNames;
        if (Array.isArray(roles)) {
            return roles.map(r => {
                if (typeof r === 'string') return r.replace('ROLE_', '');
                if (r && typeof r === 'object' && r.name) return r.name.replace('ROLE_', '');
                return String(r);
            }).join(', ');
        }
        if (typeof roles === 'string') return roles.replace('ROLE_', '');
        return 'USER';
    };

    const columns = [
        { key: 'id', title: 'ID', render: (val) => val || '—' },
        { 
            key: 'name', 
            title: 'Name', 
            render: (_, row) => (
                <span className="font-semibold text-[var(--color-navy-900)]">
                    {row.firstName || row.name || ''} {row.lastName || ''}
                </span>
            )
        },
        { key: 'email', title: 'Email Address' },
        {
            key: 'roles',
            title: 'Assigned Roles',
            render: (_, row) => <Badge variant="info">{formatRoles(row)}</Badge>
        },
        {
            key: 'enabled',
            title: 'Account Status',
            render: (val, row) => (
                <button
                    type="button"
                    onClick={() => row.id && toggleStatusMutation.mutate(row.id)}
                    className="focus-visible:outline-none"
                >
                    <Badge variant={val !== false ? 'success' : 'danger'}>
                        {val !== false ? 'Active' : 'Disabled'}
                    </Badge>
                </button>
            )
        },
        {
            key: 'actions',
            title: 'Actions',
            align: 'right',
            render: (_, row) => (
                <Button
                    variant="secondary"
                    size="sm"
                    icon={Edit2}
                    onClick={() => handleEditClick(row)}
                >
                    Edit
                </Button>
            )
        }
    ];

    return (
        <motion.div initial="hidden" animate="visible" variants={fadeIn} className="space-y-6">
            <div>
                <h1 className="text-2xl sm:text-3xl font-bold font-display text-[var(--color-navy-900)] m-0 flex items-center gap-2">
                    <Users className="w-7 h-7 text-[var(--color-navy-800)]" />
                    User Directory & Roles
                </h1>
                <p className="text-sm text-[var(--color-text-muted)] m-0 mt-1">
                    Manage system accounts, edit user details, and toggle access permissions.
                </p>
            </div>

            <DataTable
                columns={columns}
                data={userList}
                isLoading={isLoading}
                searchPlaceholder="Search users by name, email or role..."
                emptyTitle="No users found"
                pagination={{
                    page: page + 1,
                    totalPages: totalPages,
                    onPageChange: (p) => setPage(p - 1)
                }}
            />

            {/* Edit User Modal */}
            <Modal
                isOpen={!!editingUser}
                onClose={() => setEditingUser(null)}
                title="Edit User Account"
            >
                <form onSubmit={handleSave} className="space-y-4">
                    <FormField label="First Name" required id="edit-fn">
                        <input 
                            id="edit-fn"
                            type="text" 
                            value={formData.firstName}
                            onChange={e => setFormData({ ...formData, firstName: e.target.value })}
                            className="input-field"
                            required
                        />
                    </FormField>

                    <FormField label="Last Name" id="edit-ln">
                        <input 
                            id="edit-ln"
                            type="text" 
                            value={formData.lastName}
                            onChange={e => setFormData({ ...formData, lastName: e.target.value })}
                            className="input-field"
                        />
                    </FormField>

                    <FormField label="Email Address" required id="edit-email">
                        <input 
                            id="edit-email"
                            type="email" 
                            value={formData.email}
                            onChange={e => setFormData({ ...formData, email: e.target.value })}
                            className="input-field"
                            required
                        />
                    </FormField>

                    <div className="flex items-center gap-2 pt-2">
                        <input 
                            type="checkbox" 
                            id="enabled"
                            checked={formData.enabled}
                            onChange={e => setFormData({ ...formData, enabled: e.target.checked })}
                            className="w-4 h-4 rounded border-[var(--color-border)] text-[var(--color-navy-600)] focus:ring-[var(--color-navy-600)]"
                        />
                        <label htmlFor="enabled" className="text-sm font-medium text-[var(--color-text)]">
                            Account Active
                        </label>
                    </div>

                    <div className="flex justify-end gap-3 pt-4 border-t border-[var(--color-border)]">
                        <Button 
                            type="button" 
                            variant="secondary"
                            onClick={() => setEditingUser(null)}
                        >
                            Cancel
                        </Button>
                        <Button 
                            type="submit"
                            variant="primary"
                            isLoading={updateMutation.isPending}
                        >
                            Save Changes
                        </Button>
                    </div>
                </form>
            </Modal>
        </motion.div>
    );
};

export default UserManagement;
