import { create } from 'zustand';
import { axiosPrivate } from '../api/axios';

export const useClinicalDecisionStore = create((set, get) => ({
  rules: [],
  alerts: [],
  pathwayTemplates: [],
  patientPathways: [],
  orderSets: [],
  loading: false,
  error: null,

  fetchRules: async () => {
    set({ loading: true, error: null });
    try {
      const res = await axiosPrivate.get('/cds/rules');
      set({ rules: res.data.data || [], loading: false });
    } catch (err) {
      set({ error: err.response?.data?.message || err.message, loading: false });
    }
  },

  saveRule: async (rule) => {
    set({ loading: true, error: null });
    try {
      const res = await axiosPrivate.post('/cds/rules', rule);
      await get().fetchRules();
      set({ loading: false });
      return res.data.data;
    } catch (err) {
      set({ error: err.response?.data?.message || err.message, loading: false });
      throw err;
    }
  },

  fetchPatientAlerts: async (patientId) => {
    set({ loading: true, error: null });
    try {
      const res = await axiosPrivate.get(`/cds/alerts/patient/${patientId}`);
      set({ alerts: res.data.data || [], loading: false });
    } catch (err) {
      set({ error: err.response?.data?.message || err.message, loading: false });
    }
  },

  fetchPendingAlerts: async () => {
    set({ loading: true, error: null });
    try {
      const res = await axiosPrivate.get('/cds/alerts/pending');
      set({ alerts: res.data.data || [], loading: false });
    } catch (err) {
      set({ error: err.response?.data?.message || err.message, loading: false });
    }
  },

  acknowledgeAlert: async (alertId, overrideReason = '') => {
    try {
      await axiosPrivate.post(`/cds/alerts/${alertId}/acknowledge`, { overrideReason });
      set(state => ({
        alerts: state.alerts.map(a => a.id === alertId ? { ...a, status: overrideReason ? 'OVERRIDDEN' : 'ACKNOWLEDGED' } : a)
      }));
    } catch (err) {
      set({ error: err.response?.data?.message || err.message });
    }
  },

  fetchPathwayTemplates: async () => {
    set({ loading: true, error: null });
    try {
      const res = await axiosPrivate.get('/care-pathways/templates');
      set({ pathwayTemplates: res.data.data || [], loading: false });
    } catch (err) {
      set({ error: err.response?.data?.message || err.message, loading: false });
    }
  },

  savePathwayTemplate: async (template) => {
    set({ loading: true, error: null });
    try {
      let res;
      if (template.id) {
        res = await axiosPrivate.put(`/care-pathways/templates/${template.id}`, template);
      } else {
        res = await axiosPrivate.post('/care-pathways/templates', template);
      }
      await get().fetchPathwayTemplates();
      set({ loading: false });
      return res.data.data;
    } catch (err) {
      set({ error: err.response?.data?.message || err.message, loading: false });
      throw err;
    }
  },

  deletePathwayTemplate: async (id) => {
    try {
      await axiosPrivate.delete(`/care-pathways/templates/${id}`);
      await get().fetchPathwayTemplates();
    } catch (err) {
      set({ error: err.response?.data?.message || err.message });
    }
  },

  assignPathway: async (patientId, templateId) => {
    set({ loading: true, error: null });
    try {
      const res = await axiosPrivate.post('/care-pathways/assign', { patientId, templateId });
      await get().fetchPatientPathways(patientId);
      set({ loading: false });
      return res.data.data;
    } catch (err) {
      set({ error: err.response?.data?.message || err.message, loading: false });
      throw err;
    }
  },

  fetchPatientPathways: async (patientId) => {
    set({ loading: true, error: null });
    try {
      const res = await axiosPrivate.get(`/care-pathways/patient/${patientId}`);
      set({ patientPathways: res.data.data || [], loading: false });
    } catch (err) {
      set({ error: err.response?.data?.message || err.message, loading: false });
    }
  },

  startPathwayStep: async (stepId, patientId) => {
    try {
      await axiosPrivate.post(`/care-pathways/steps/${stepId}/start`);
      if (patientId) await get().fetchPatientPathways(patientId);
    } catch (err) {
      set({ error: err.response?.data?.message || err.message });
    }
  },

  completePathwayStep: async (stepId, patientId) => {
    try {
      await axiosPrivate.post(`/care-pathways/steps/${stepId}/complete`);
      if (patientId) await get().fetchPatientPathways(patientId);
    } catch (err) {
      set({ error: err.response?.data?.message || err.message });
    }
  },

  fetchOrderSets: async (diagnosisCode = '') => {
    set({ loading: true, error: null });
    try {
      const res = await axiosPrivate.get(`/order-sets${diagnosisCode ? `?diagnosisCode=${encodeURIComponent(diagnosisCode)}` : ''}`);
      set({ orderSets: res.data.data || [], loading: false });
    } catch (err) {
      set({ error: err.response?.data?.message || err.message, loading: false });
    }
  },

  applyOrderSet: async (templateId, patientId) => {
    set({ loading: true, error: null });
    try {
      const res = await axiosPrivate.post(`/order-sets/apply/${templateId}`, { patientId });
      set({ loading: false });
      return res.data.data;
    } catch (err) {
      set({ error: err.response?.data?.message || err.message, loading: false });
      throw err;
    }
  }
}));
