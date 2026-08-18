/**
 * publicApi.js – React Query hooks for all public-facing data
 * (no auth token required)
 */
import { useQuery, useMutation } from '@tanstack/react-query';
import { axiosPublic } from './axios';

/* ─── Doctors ─────────────────────────────────────────────────────────────── */
export const usePublicDoctors = () =>
  useQuery({
    queryKey: ['public-doctors'],
    queryFn: async () => {
      const res = await axiosPublic.get('/doctors');
      return res.data;
    },
    staleTime: 5 * 60_000,
    retry: 2,
  });

/* ─── Departments ─────────────────────────────────────────────────────────── */
export const usePublicDepartments = () =>
  useQuery({
    queryKey: ['public-departments'],
    queryFn: async () => {
      const res = await axiosPublic.get('/departments');
      return res.data;
    },
    staleTime: 10 * 60_000,
    retry: 2,
  });

/* ─── Book Appointment (public – no auth needed for guest booking inquiry) ── */
export const useBookAppointment = () =>
  useMutation({
    mutationFn: async (data) => {
      const res = await axiosPublic.post('/appointments/guest', data);
      return res.data;
    },
  });

/* ─── Clinic Stats (patient count, doctor count, surgery count) ─────────── */
export const useClinicStats = () =>
  useQuery({
    queryKey: ['clinic-stats'],
    queryFn: async () => {
      try {
        const res = await axiosPublic.get('/clinic/stats');
        return res.data;
      } catch {
        // Graceful fallback when endpoint not yet implemented
        return { happyPatients: 15000, expertDoctors: 25, successfulSurgeries: 12000 };
      }
    },
    staleTime: 30 * 60_000,
  });
