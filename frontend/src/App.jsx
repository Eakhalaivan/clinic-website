import { useEffect } from 'react';
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';

// Layouts
import PublicLayout from './layouts/PublicLayout';
import AuthLayout from './layouts/AuthLayout';
import DashboardLayout from './layouts/DashboardLayout';

// Route guard
import RoleRoute from './components/auth/RoleRoute';

// Public pages
import Home from './pages/public/Home';
import DoctorList from './pages/public/DoctorList';
import PortalLoginPage from './pages/auth/PortalLoginPage';
import Register from './pages/public/Register';

// Patient pages
import PatientDashboard from './pages/patient/PatientDashboard';
import PatientProfileEdit from './pages/patient/PatientProfileEdit';
import BookAppointment from './pages/patient/BookAppointment';
import MedicalRecords from './pages/patient/MedicalRecords';
import PatientBilling from './pages/patient/PatientBilling';
import PatientPrescriptions from './pages/patient/PatientPrescriptions';
import AppointmentHistory from './pages/patient/AppointmentHistory';
import LabReports from './pages/patient/LabReports';

// Doctor pages
import DoctorDashboard from './pages/doctor/DoctorDashboard';
import AppointmentListToday from './pages/doctor/AppointmentListToday';
import ConsultationQueue from './pages/doctor/ConsultationQueue';
import DoctorCalendar from './pages/doctor/DoctorCalendar';
import PatientList from './pages/doctor/PatientList';
import PatientDetail from './pages/doctor/PatientDetail';
import ClinicalNotes from './pages/doctor/ClinicalNotes';
import NewPrescription from './pages/doctor/NewPrescription';
import LabRequest from './pages/doctor/LabRequest';
import PrescriptionTemplates from './pages/doctor/PrescriptionTemplates';
import FollowUps from './pages/doctor/FollowUps';
import DoctorEarnings from './pages/doctor/DoctorEarnings';
import DoctorAnalytics from './pages/doctor/DoctorAnalytics';
import ClinicalDecisionSupport from './pages/doctor/ClinicalDecisionSupport';
import CarePathwayBuilder from './pages/doctor/CarePathwayBuilder';
import PatientCarePathwayView from './pages/doctor/PatientCarePathwayView';
import DoctorScheduleSettings from './pages/doctor/DoctorScheduleSettings';

// Admin pages (legacy — kept for ROLE_ADMIN / ROLE_BRANCH_ADMIN via old AuthLayout)
import AdminDashboard from './pages/admin/AdminDashboard';

// Clinical Dashboard pages
import NurseDashboard from './pages/nurse/NurseDashboard';
import NurseAssignedPatients from './pages/nurse/NurseAssignedPatients';
import VitalSignsEntry from './pages/nurse/VitalSignsEntry';
import MedicationAdministration from './pages/nurse/MedicationAdministration';
import WardManagement from './pages/nurse/WardManagement';
import ReceptionDashboard from './pages/reception/ReceptionDashboard';
import QueueManagement from './pages/reception/QueueManagement';
import PatientRegistration from './pages/reception/PatientRegistration';
import TokenGeneration from './pages/reception/TokenGeneration';
import PharmacistDashboard from './pages/pharmacist/PharmacistDashboard';
import LabDashboard from './pages/lab/LabDashboard';
import AccountantDashboard from './pages/accountant/AccountantDashboard';
import RadiologistDashboard from './pages/radiologist/RadiologistDashboard';

// Back-office dashboard pages
import HrDashboard from './pages/hr/HrDashboard';
import Employees from './pages/hr/Employees';
import LeaveManagement from './pages/hr/LeaveManagement';
import FinanceDashboard from './pages/finance/FinanceDashboard';
import InvoicesList from './pages/finance/InvoicesList';
import PnLStatement from './pages/finance/PnLStatement';
import InventoryDashboard from './pages/inventory/InventoryDashboard';
import WarehousesList from './pages/inventory/WarehousesList';
import StockTransfers from './pages/inventory/StockTransfers';

// Phase 4 portal dashboard pages
import MarketingDashboard from './pages/marketing/MarketingDashboard';
import EcommerceDashboard from './pages/ecommerce/EcommerceDashboard';
import SupportDashboard from './pages/support/SupportDashboard';
import VendorDashboard from './pages/vendor/VendorDashboard';
import InsuranceDashboard from './pages/insurance/InsuranceDashboard';
import AmbulanceDashboard from './pages/ambulance/AmbulanceDashboard';
import SuperAdminConsole from './pages/super-admin/SuperAdminConsole';
import BranchManagement from './pages/admin/BranchManagement';
import UserManagement from './pages/admin/UserManagement';
import DicomViewer from './pages/radiologist/DicomViewer';

// Pharmacy full module routes
import { PharmacyRoutes } from './pages/pharmacy/PharmacyRoutes';
import MainLayout from './components/pharmacy/layout/MainLayout';

// Generic placeholder for unbuilt sub-pages
import PlaceholderPage from './pages/common/PlaceholderPage';

import AIAssistantWidget from './components/ui/AIAssistantWidget';

const queryClient = new QueryClient();

/**
 * Helper — wraps a route block in DashboardLayout with role guard.
 * Note: ROLE_ADMIN and ROLE_SUPER_ADMIN bypass allowedRoles by design.
 * See the comment in components/auth/RoleRoute.jsx for rationale and
 * instructions on how to restrict the bypass if ever needed.
 */
const DashboardRoute = ({ path, portalSlug, allowedRoles, defaultRedirect, children }) => (
  <Route
    path={path}
    element={
      <RoleRoute allowedRoles={allowedRoles} portalSlug={portalSlug}>
        <DashboardLayout portalSlug={portalSlug} allowedRoles={allowedRoles} />
      </RoleRoute>
    }
  >
    {defaultRedirect && (
      <Route index element={<Navigate to={defaultRedirect} replace />} />
    )}
    {children}
  </Route>
);

// Shorthand: renders a PlaceholderPage with the given title
const PH = (title, subtitle) => <PlaceholderPage title={title} subtitle={subtitle} />;

function App() {
  useEffect(() => {
    // Wake up backend (e.g., Render free tier) on app load
    const baseUrl =
      (typeof window !== 'undefined' && window.__ENV__?.VITE_API_BASE_URL) ||
      import.meta.env.VITE_API_BASE_URL ||
      'http://localhost:8080/api';
    fetch(`${baseUrl}/health`)
      .then(res => res.json())
      .catch(() => {
        // Silently fail if unavailable
      });
  }, []);

  return (
    <QueryClientProvider client={queryClient}>
      <BrowserRouter>
        <AIAssistantWidget />
        <Routes>

          {/* ── Public Routes ───────────────────────────────────────────── */}
          <Route element={<PublicLayout />}>
            <Route path="/" element={<Home />} />
            <Route path="/doctors" element={<DoctorList />} />
            <Route path="/register" element={<Register />} />
            <Route path="/:portalSlug/register" element={<Register />} />
          </Route>
          
          <Route path="/login" element={<Navigate to="/patient/login" replace />} />
          <Route path="/:portalSlug/login" element={<PortalLoginPage />} />

          {/* ── Patient Routes ──────────────────────────────────────────── */}
          <Route
            path="/patient"
            element={
              <RoleRoute allowedRoles={['ROLE_PATIENT', 'ROLE_SUPER_ADMIN']} portalSlug="patient">
                <DashboardLayout portalSlug="patient" allowedRoles={['ROLE_PATIENT', 'ROLE_SUPER_ADMIN']} />
              </RoleRoute>
            }
          >
            <Route index element={<Navigate to="/patient/dashboard" replace />} />
            <Route path="dashboard" element={<PatientDashboard />} />
            <Route path="profile" element={<PatientProfileEdit />} />
            <Route path="profile-edit" element={<PatientProfileEdit />} />
            <Route path="book" element={<BookAppointment />} />
            <Route path="book/:doctorId" element={<BookAppointment />} />
            <Route path="appointments" element={<AppointmentHistory />} />
            <Route path="records" element={<MedicalRecords />} />
            <Route path="billing" element={<PatientBilling />} />
            <Route path="payments" element={<PatientBilling />} />
            <Route path="prescriptions" element={<PatientPrescriptions />} />
            <Route path="lab-reports" element={<LabReports />} />
            {/* Previously missing patient routes */}
            <Route path="radiology-reports" element={PH('Radiology Reports', 'Your radiology and imaging reports will appear here once uploaded by the radiologist.')} />
            <Route path="insurance" element={PH('Insurance', 'View your insurance coverage, claims, and pre-authorization requests here.')} />
            <Route path="timeline" element={PH('Health Timeline', 'A chronological view of all your medical events, visits, and reports.')} />
            <Route path="orders" element={PH('Orders', 'Track your medical equipment, prescription refill, and wellness product orders here.')} />
          </Route>

          {/* ── Doctor Routes ───────────────────────────────────────────── */}
          <Route
            path="/doctor"
            element={
              <RoleRoute allowedRoles={['ROLE_DOCTOR', 'ROLE_SUPER_ADMIN']} portalSlug="doctor">
                <DashboardLayout portalSlug="doctor" allowedRoles={['ROLE_DOCTOR', 'ROLE_SUPER_ADMIN']} />
              </RoleRoute>
            }
          >
            <Route index element={<Navigate to="/doctor/dashboard" replace />} />
            <Route path="dashboard" element={<DoctorDashboard />} />
            <Route path="appointments/today" element={<AppointmentListToday />} />
            <Route path="queue" element={<ConsultationQueue />} />
            <Route path="calendar" element={<DoctorCalendar />} />
            <Route path="patients" element={<PatientList />} />
            <Route path="patients/:patientId" element={<PatientDetail />} />
            <Route path="patients/:patientId/notes" element={<ClinicalNotes />} />
            <Route path="patients/:patientId/prescriptions/new" element={<NewPrescription />} />
            <Route path="patients/:patientId/prescriptions/:prescriptionId/edit" element={<NewPrescription />} />

            <Route path="patients/:patientId/lab-request" element={<LabRequest />} />
            <Route path="patients/:patientId/care-pathways" element={<PatientCarePathwayView />} />
            <Route path="prescription-templates" element={<PrescriptionTemplates />} />
            <Route path="follow-ups" element={<FollowUps />} />
            <Route path="earnings" element={<DoctorEarnings />} />
            <Route path="analytics" element={<DoctorAnalytics />} />
            <Route path="cds" element={<ClinicalDecisionSupport />} />
            <Route path="care-pathways/builder" element={<CarePathwayBuilder />} />
            <Route path="schedule-settings" element={<DoctorScheduleSettings />} />
          </Route>

          {/* ── Nurse Routes ────────────────────────────────────────────── */}
          <Route
            path="/nurse"
            element={
              <RoleRoute allowedRoles={['ROLE_NURSE', 'ROLE_SUPER_ADMIN']} portalSlug="nurse">
                <DashboardLayout portalSlug="nurse" allowedRoles={['ROLE_NURSE', 'ROLE_SUPER_ADMIN']} />
              </RoleRoute>
            }
          >
            <Route index element={<Navigate to="/nurse/dashboard" replace />} />
            <Route path="dashboard" element={<NurseDashboard />} />
            <Route path="patients" element={<NurseAssignedPatients />} />
            <Route path="vitals" element={<VitalSignsEntry />} />
            <Route path="medication" element={<MedicationAdministration />} />
            <Route path="wards" element={<WardManagement />} />
            {/* Previously missing nurse routes */}
            <Route path="notes" element={PH('Nursing Notes', 'Document and review nursing notes for assigned patients.')} />
            <Route path="monitoring" element={PH('Patient Monitoring', 'Live vitals monitoring and alert escalation panel.')} />
            <Route path="tasks" element={PH('Task Management', 'View and complete assigned clinical tasks and care checklists.')} />
          </Route>

          {/* ── Reception Routes ────────────────────────────────────────── */}
          <Route
            path="/reception"
            element={
              <RoleRoute allowedRoles={['ROLE_RECEPTION', 'ROLE_SUPER_ADMIN']} portalSlug="reception">
                <DashboardLayout portalSlug="reception" allowedRoles={['ROLE_RECEPTION', 'ROLE_SUPER_ADMIN']} />
              </RoleRoute>
            }
          >
            <Route index element={<Navigate to="/reception/dashboard" replace />} />
            <Route path="dashboard" element={<ReceptionDashboard />} />
            <Route path="queue" element={<QueueManagement />} />
            <Route path="register" element={<PatientRegistration />} />
            <Route path="tokens" element={<TokenGeneration />} />
            {/* Previously missing reception routes */}
            <Route path="walk-in" element={PH('Walk-In Check-In', 'Register walk-in patients and assign them to a doctor queue.')} />
            <Route path="book/:doctorId" element={<BookAppointment />} />
            <Route path="billing" element={PH('Billing', 'Create and manage patient invoices and front-desk payments.')} />
            <Route path="checkin" element={PH('Check-In / Check-Out', 'Manage patient arrival check-ins and discharge check-outs.')} />
            <Route path="search" element={PH('Patient Search', 'Search patient records by name, phone, or patient ID.')} />
          </Route>

          {/* ── Pharmacist Routes ───────────────────────────────────────── */}
          <Route
            path="/pharmacist"
            element={
              <RoleRoute allowedRoles={['ROLE_PHARMACIST', 'ROLE_SUPER_ADMIN']} portalSlug="pharmacist">
                <DashboardLayout portalSlug="pharmacist" allowedRoles={['ROLE_PHARMACIST', 'ROLE_SUPER_ADMIN']} />
              </RoleRoute>
            }
          >
            <Route index element={<Navigate to="/pharmacist/dashboard" replace />} />
            <Route path="dashboard" element={<PharmacistDashboard />} />
            {/* Previously missing pharmacist routes */}
            <Route path="prescriptions" element={PH('Prescriptions', 'View and process incoming e-prescriptions from doctors.')} />
            <Route path="dispense" element={PH('Dispensing', 'Dispense medications against verified prescriptions.')} />
            <Route path="sales" element={PH('OTC Sales', 'Record and manage over-the-counter medication sales.')} />
            <Route path="inventory" element={PH('Inventory', 'Monitor drug stock levels, reorder points, and restocking.')} />
            <Route path="batches" element={PH('Batch Tracking', 'Track medicine batches, lot numbers, and batch histories.')} />
            <Route path="expiry" element={PH('Expiry Alerts', 'View medicines nearing or past their expiry dates.')} />
            <Route path="purchase-orders" element={PH('Purchase Orders', 'Raise and track purchase orders to suppliers.')} />
            <Route path="suppliers" element={PH('Suppliers', 'Manage approved pharmaceutical suppliers and contracts.')} />
            <Route path="deliveries" element={PH('Deliveries', 'Track incoming delivery schedules and GRNs.')} />
            <Route path="reports" element={PH('Reports', 'Pharmacy sales, dispensing, and stock analytics reports.')} />
          </Route>

          {/* ── Lab Tech Routes (/lab) ───────────────────────────────────── */}
          <Route
            path="/lab"
            element={
              <RoleRoute allowedRoles={['ROLE_LAB_TECH', 'ROLE_SUPER_ADMIN']} portalSlug="lab">
                <DashboardLayout portalSlug="lab" allowedRoles={['ROLE_LAB_TECH', 'ROLE_SUPER_ADMIN']} />
              </RoleRoute>
            }
          >
            <Route index element={<Navigate to="/lab/dashboard" replace />} />
            <Route path="dashboard" element={<LabDashboard />} />
            {/* Previously missing lab routes */}
            <Route path="requests" element={PH('Test Requests', 'View and process incoming lab test requests from doctors.')} />
            <Route path="samples" element={PH('Sample Collection', 'Log sample collection events and specimen tracking.')} />
            <Route path="results" element={PH('Result Entry', 'Enter and review test results for assigned requests.')} />
            <Route path="verify" element={PH('Report Verification', 'Verify and digitally sign lab reports before releasing to patients.')} />
            <Route path="notifications" element={PH('Notifications', 'View alerts and patient notification events.')} />
          </Route>
          {/* Redirect legacy /lab-tech → /lab */}
          <Route path="/lab-tech/*" element={<Navigate to="/lab" replace />} />

          {/* ── Radiologist Routes ──────────────────────────────────────── */}
          <Route
            path="/radiologist"
            element={
              <RoleRoute allowedRoles={['ROLE_RADIOLOGIST', 'ROLE_SUPER_ADMIN']} portalSlug="radiologist">
                <DashboardLayout portalSlug="radiologist" allowedRoles={['ROLE_RADIOLOGIST', 'ROLE_SUPER_ADMIN']} />
              </RoleRoute>
            }
          >
            <Route index element={<Navigate to="/radiologist/dashboard" replace />} />
            <Route path="dashboard" element={<RadiologistDashboard />} />
            <Route path="viewer" element={<DicomViewer />} />
            {/* Previously missing radiologist routes */}
            <Route path="requests" element={PH('Imaging Requests', 'View pending and completed radiology imaging requests.')} />
            <Route path="upload" element={PH('Image Upload', 'Upload DICOM or JPEG images to the PACS system.')} />
            <Route path="reporting" element={PH('Reporting', 'Create and submit structured radiology reports.')} />
            <Route path="archive" element={PH('Archive', 'Browse archived imaging studies and historical scans.')} />
          </Route>

          {/* ── Accountant → Finance redirect ───────────────────────────── */}
          <Route path="/accountant" element={<Navigate to="/finance" replace />} />
          <Route path="/accountant/*" element={<Navigate to="/finance" replace />} />

          {/* ── Finance / Accountant Routes ─────────────────────────────── */}
          <Route
            path="/finance"
            element={
              <RoleRoute allowedRoles={['ROLE_FINANCE', 'ROLE_ACCOUNTANT', 'ROLE_SUPER_ADMIN']} portalSlug="finance">
                <DashboardLayout portalSlug="finance" allowedRoles={['ROLE_FINANCE', 'ROLE_ACCOUNTANT', 'ROLE_SUPER_ADMIN']} />
              </RoleRoute>
            }
          >
            <Route index element={<Navigate to="/finance/dashboard" replace />} />
            <Route path="dashboard" element={<FinanceDashboard />} />
            <Route path="invoices" element={<InvoicesList />} />
            <Route path="pnl" element={<PnLStatement />} />
            {/* Previously missing finance routes */}
            <Route path="payments" element={PH('Payments', 'Track all incoming and outgoing payments.')} />
            <Route path="insurance-claims" element={PH('Insurance Claims', 'Manage insurance claim submissions and reconciliation.')} />
            <Route path="revenue" element={PH('Revenue', 'Monthly and annual revenue analysis and trends.')} />
            <Route path="expenses" element={PH('Expenses', 'Record and categorize operational expenses.')} />
            <Route path="tax" element={PH('GST / Tax', 'Manage GST filings and tax liability reports.')} />
            <Route path="reports" element={PH('Reports', 'Comprehensive financial reports and audit trails.')} />
            <Route path="budget" element={PH('Budget', 'Annual budget planning and variance tracking.')} />
          </Route>

          {/* ── HR Routes ───────────────────────────────────────────────── */}
          <Route
            path="/hr"
            element={
              <RoleRoute allowedRoles={['ROLE_HR', 'ROLE_SUPER_ADMIN']} portalSlug="hr">
                <DashboardLayout portalSlug="hr" allowedRoles={['ROLE_HR', 'ROLE_SUPER_ADMIN']} />
              </RoleRoute>
            }
          >
            <Route index element={<Navigate to="/hr/dashboard" replace />} />
            <Route path="dashboard" element={<HrDashboard />} />
            <Route path="employees" element={<Employees />} />
            <Route path="leave" element={<LeaveManagement />} />
            {/* Previously missing HR routes */}
            <Route path="recruitment" element={PH('Recruitment', 'Post jobs, screen applicants, and manage onboarding.')} />
            <Route path="attendance" element={PH('Attendance', 'View staff attendance logs, shifts, and check-in history.')} />
            <Route path="payroll" element={PH('Payroll', 'Process monthly salaries, deductions, and payslip generation.')} />
            <Route path="performance" element={PH('Performance Reviews', 'Conduct and track staff performance evaluations.')} />
            <Route path="documents" element={PH('Documents', 'Manage staff contracts, certifications, and credentials.')} />
          </Route>

          {/* ── Inventory Routes ────────────────────────────────────────── */}
          <Route
            path="/inventory"
            element={
              <RoleRoute allowedRoles={['ROLE_INVENTORY_MANAGER', 'ROLE_SUPER_ADMIN']} portalSlug="inventory">
                <DashboardLayout portalSlug="inventory" allowedRoles={['ROLE_INVENTORY_MANAGER', 'ROLE_SUPER_ADMIN']} />
              </RoleRoute>
            }
          >
            <Route index element={<Navigate to="/inventory/dashboard" replace />} />
            <Route path="dashboard" element={<InventoryDashboard />} />
            <Route path="warehouses" element={<WarehousesList />} />
            <Route path="transfers" element={<StockTransfers />} />
            {/* Previously missing inventory routes */}
            <Route path="purchase-orders" element={PH('Purchase Orders', 'Raise and track purchase orders to suppliers.')} />
            <Route path="suppliers" element={PH('Suppliers', 'Manage supplier contracts, ratings, and contact details.')} />
            <Route path="batches" element={PH('Batch Tracking', 'Track product batches, lot numbers, and GRN records.')} />
            <Route path="expiry" element={PH('Expiry Tracking', 'Monitor items nearing expiry and plan disposal.')} />
            <Route path="branches" element={PH('Branch Stock', 'View and manage stock levels per branch location.')} />
            <Route path="reports" element={PH('Inventory Reports', 'Comprehensive stock movement and valuation reports.')} />
          </Route>

          {/* ── Marketing / CRM Routes ──────────────────────────────────── */}
          <Route
            path="/marketing"
            element={
              <RoleRoute allowedRoles={['ROLE_MARKETING', 'ROLE_SUPER_ADMIN']} portalSlug="marketing">
                <DashboardLayout portalSlug="marketing" allowedRoles={['ROLE_MARKETING', 'ROLE_SUPER_ADMIN']} />
              </RoleRoute>
            }
          >
            <Route index element={<Navigate to="/marketing/dashboard" replace />} />
            <Route path="dashboard" element={<MarketingDashboard />} />
            {/* Previously missing marketing routes */}
            <Route path="campaigns" element={PH('Campaigns', 'Create and manage health awareness and promotional campaigns.')} />
            <Route path="loyalty" element={PH('Loyalty Program', 'Configure patient loyalty tiers and reward point rules.')} />
            <Route path="membership" element={PH('Membership Plans', 'Manage premium membership packages and benefits.')} />
            <Route path="referrals" element={PH('Referrals', 'Track patient referral programs and referral rewards.')} />
            <Route path="gift-cards" element={PH('Gift Cards', 'Issue and manage digital health gift cards.')} />
            <Route path="coupons" element={PH('Coupons', 'Create discount coupons and promotional codes.')} />
            <Route path="email" element={PH('Email Campaigns', 'Design and send bulk email marketing campaigns.')} />
            <Route path="sms" element={PH('SMS Campaigns', 'Send bulk SMS health alerts and appointment reminders.')} />
            <Route path="analytics" element={PH('Campaign Analytics', 'Track campaign ROI, open rates, and patient acquisition.')} />
          </Route>

          {/* ── eCommerce Routes ─────────────────────────────────────────── */}
          <Route
            path="/ecommerce"
            element={
              <RoleRoute allowedRoles={['ROLE_STORE_MANAGER', 'ROLE_MARKETING', 'ROLE_SUPER_ADMIN']} portalSlug="store">
                <DashboardLayout portalSlug="store" allowedRoles={['ROLE_STORE_MANAGER', 'ROLE_MARKETING', 'ROLE_SUPER_ADMIN']} />
              </RoleRoute>
            }
          >
            <Route index element={<Navigate to="/ecommerce/dashboard" replace />} />
            <Route path="dashboard" element={<EcommerceDashboard />} />
          </Route>

          {/* Also mount at /store slug (matches portalConfig dashboardRoute) */}
          <Route
            path="/store"
            element={
              <RoleRoute allowedRoles={['ROLE_STORE_MANAGER', 'ROLE_MARKETING', 'ROLE_SUPER_ADMIN']} portalSlug="store">
                <DashboardLayout portalSlug="store" allowedRoles={['ROLE_STORE_MANAGER', 'ROLE_MARKETING', 'ROLE_SUPER_ADMIN']} />
              </RoleRoute>
            }
          >
            <Route index element={<Navigate to="/store/dashboard" replace />} />
            <Route path="dashboard" element={<EcommerceDashboard />} />
            <Route path="products" element={PH('Products', 'Manage your medical product catalog.')} />
            <Route path="categories" element={PH('Categories', 'Organize products into categories and subcategories.')} />
            <Route path="brands" element={PH('Brands', 'Manage product brand listings.')} />
            <Route path="inventory" element={PH('Inventory', 'Track product stock levels and restock points.')} />
            <Route path="orders" element={PH('Orders', 'Process and track customer orders.')} />
            <Route path="shipping" element={PH('Shipping', 'Configure shipping zones and track deliveries.')} />
            <Route path="coupons" element={PH('Coupons', 'Manage store discount codes and promotions.')} />
            <Route path="reviews" element={PH('Reviews', 'Moderate customer product reviews.')} />
            <Route path="returns" element={PH('Returns', 'Process return and refund requests.')} />
          </Route>

          {/* ── Support Routes ──────────────────────────────────────────── */}
          <Route
            path="/support"
            element={
              <RoleRoute allowedRoles={['ROLE_SUPPORT', 'ROLE_CUSTOMER_SUPPORT', 'ROLE_SUPER_ADMIN']} portalSlug="support">
                <DashboardLayout portalSlug="support" allowedRoles={['ROLE_SUPPORT', 'ROLE_CUSTOMER_SUPPORT', 'ROLE_SUPER_ADMIN']} />
              </RoleRoute>
            }
          >
            <Route index element={<Navigate to="/support/dashboard" replace />} />
            <Route path="dashboard" element={<SupportDashboard />} />
            {/* Previously missing support routes */}
            <Route path="tickets" element={PH('Tickets', 'View, assign, and resolve support tickets.')} />
            <Route path="chat" element={PH('Live Chat', 'Omnichannel live chat with patients and staff.')} />
            <Route path="queries" element={PH('Queries', 'Browse and respond to patient queries.')} />
            <Route path="complaints" element={PH('Complaints', 'Track and manage formal patient complaints.')} />
            <Route path="resolutions" element={PH('Resolutions', 'View resolved tickets and resolution analytics.')} />
            <Route path="kb" element={PH('Knowledge Base', 'Create and manage self-service help articles.')} />
          </Route>

          {/* ── Vendor Routes ───────────────────────────────────────────── */}
          <Route
            path="/vendor"
            element={
              <RoleRoute allowedRoles={['ROLE_VENDOR', 'ROLE_SUPER_ADMIN', 'ROLE_INVENTORY_MANAGER']} portalSlug="vendor">
                <DashboardLayout portalSlug="vendor" allowedRoles={['ROLE_VENDOR', 'ROLE_SUPER_ADMIN', 'ROLE_INVENTORY_MANAGER']} />
              </RoleRoute>
            }
          >
            <Route index element={<Navigate to="/vendor/dashboard" replace />} />
            <Route path="dashboard" element={<VendorDashboard />} />
            {/* Previously missing vendor routes */}
            <Route path="purchase-orders" element={PH('Purchase Orders', 'View and acknowledge purchase orders from the clinic.')} />
            <Route path="quotations" element={PH('Quotations', 'Submit price quotations for requested items.')} />
            <Route path="deliveries" element={PH('Deliveries', 'Log dispatch events and track delivery status.')} />
            <Route path="invoices" element={PH('Invoices', 'View and download invoices for completed orders.')} />
            <Route path="payments" element={PH('Payments', 'Track payment status for submitted invoices.')} />
            <Route path="requests" element={PH('Requests', 'View open item requests and tender documents.')} />
          </Route>

          {/* ── Insurance Routes ────────────────────────────────────────── */}
          <Route
            path="/insurance"
            element={
              <RoleRoute allowedRoles={['ROLE_INSURANCE', 'ROLE_SUPER_ADMIN', 'ROLE_ACCOUNTANT', 'ROLE_FINANCE']} portalSlug="insurance">
                <DashboardLayout portalSlug="insurance" allowedRoles={['ROLE_INSURANCE', 'ROLE_SUPER_ADMIN', 'ROLE_ACCOUNTANT', 'ROLE_FINANCE']} />
              </RoleRoute>
            }
          >
            <Route index element={<Navigate to="/insurance/dashboard" replace />} />
            <Route path="dashboard" element={<InsuranceDashboard />} />
            {/* Previously missing insurance routes */}
            <Route path="claims" element={PH('Claims', 'Review and adjudicate patient insurance claims.')} />
            <Route path="approvals" element={PH('Approvals', 'Approve or deny pre-authorization requests.')} />
            <Route path="policies" element={PH('Policies', 'Manage insurance policy catalog and coverage definitions.')} />
            <Route path="verify" element={PH('Verify', 'Verify patient insurance eligibility and coverage in real time.')} />
            <Route path="status" element={PH('Status Board', 'Live status board for pending and resolved claims.')} />
            <Route path="payments" element={PH('Payments', 'Track claim settlement payments to the clinic.')} />
          </Route>

          {/* ── Ambulance Routes ────────────────────────────────────────── */}
          <Route
            path="/ambulance"
            element={
              <RoleRoute allowedRoles={['ROLE_AMBULANCE', 'ROLE_SUPER_ADMIN', 'ROLE_ADMIN']} portalSlug="ambulance">
                <DashboardLayout portalSlug="ambulance" allowedRoles={['ROLE_AMBULANCE', 'ROLE_SUPER_ADMIN', 'ROLE_ADMIN']} />
              </RoleRoute>
            }
          >
            <Route index element={<Navigate to="/ambulance/dashboard" replace />} />
            <Route path="dashboard" element={<AmbulanceDashboard />} />
            {/* Previously missing ambulance routes */}
            <Route path="requests" element={PH('Emergency Requests', 'View and respond to active emergency dispatch requests.')} />
            <Route path="assign" element={PH('Assignments', 'Assign ambulances and paramedics to emergency requests.')} />
            <Route path="tracking" element={PH('Fleet Tracker', 'Live GPS tracking for all ambulance units.')} />
            <Route path="trips" element={PH('Trip History', 'Review completed trip logs, timings, and outcomes.')} />
          </Route>

          {/* ── Branch Admin Routes ─────────────────────────────────────── */}
          <Route
            path="/branch-admin"
            element={
              <RoleRoute allowedRoles={['ROLE_BRANCH_ADMIN', 'ROLE_SUPER_ADMIN']} portalSlug="branch-admin">
                <DashboardLayout portalSlug="branch-admin" allowedRoles={['ROLE_BRANCH_ADMIN', 'ROLE_SUPER_ADMIN']} />
              </RoleRoute>
            }
          >
            <Route index element={<Navigate to="/branch-admin/dashboard" replace />} />
            <Route path="dashboard" element={PH('Branch Overview', 'Branch performance, OPD stats, and staff summary.')} />
            <Route path="staff" element={PH('Staff', 'View and manage branch staff and duty rosters.')} />
            <Route path="doctors" element={PH('Doctors', 'Manage doctors assigned to this branch.')} />
            <Route path="patients" element={PH('Patients', 'View patients registered at this branch.')} />
            <Route path="appointments" element={PH('Appointments', 'Manage branch appointment schedule.')} />
            <Route path="pharmacy" element={PH('Pharmacy', 'Branch pharmacy stock and dispensing overview.')} />
            <Route path="laboratory" element={PH('Laboratory', 'Branch lab test queue and results.')} />
            <Route path="inventory" element={PH('Inventory', 'Branch stock levels and transfer requests.')} />
            <Route path="billing" element={PH('Billing', 'Branch billing and invoice management.')} />
            <Route path="reports" element={PH('Reports', 'Branch-level operational reports.')} />
            <Route path="analytics" element={PH('Analytics', 'Branch performance KPIs and analytics.')} />
          </Route>

          {/* ── Super Admin Routes ──────────────────────────────────────── */}
          <Route
            path="/super-admin"
            element={
              <RoleRoute allowedRoles={['ROLE_SUPER_ADMIN', 'ROLE_ADMIN']} portalSlug="super-admin">
                <DashboardLayout portalSlug="super-admin" allowedRoles={['ROLE_SUPER_ADMIN', 'ROLE_ADMIN']} />
              </RoleRoute>
            }
          >
            <Route index element={<Navigate to="/super-admin/dashboard" replace />} />
            <Route path="dashboard" element={<SuperAdminConsole />} />
            <Route path="branches" element={<BranchManagement />} />
            <Route path="users" element={<UserManagement />} />
            <Route path="rbac" element={<SuperAdminConsole defaultTab="security" />} />
            <Route path="facilities" element={<BranchManagement />} />
            <Route path="doctors" element={<UserManagement />} />
            <Route path="patients" element={<UserManagement />} />
            <Route path="analytics" element={<SuperAdminConsole />} />
            <Route path="security" element={<SuperAdminConsole defaultTab="security" />} />
            <Route path="audit-logs" element={<SuperAdminConsole defaultTab="audit" />} />
            <Route path="monitoring" element={<SuperAdminConsole defaultTab="health" />} />
            <Route path="notifications" element={<SuperAdminConsole defaultTab="notifications" />} />
            <Route path="subscriptions" element={<SuperAdminConsole defaultTab="plans" />} />
            <Route path="settings" element={<SuperAdminConsole defaultTab="config" />} />
            <Route path="cms" element={<SuperAdminConsole defaultTab="config" />} />
            <Route path="backup" element={<SuperAdminConsole defaultTab="health" />} />
            <Route path="console" element={<SuperAdminConsole />} />
          </Route>

          {/* ── Legacy Admin Routes (ROLE_ADMIN / ROLE_BRANCH_ADMIN) ────── */}
          <Route element={<AuthLayout allowedRoles={['ROLE_ADMIN', 'ROLE_BRANCH_ADMIN']} />}>
            <Route path="/admin/dashboard" element={<AdminDashboard />} />
          </Route>

          {/* ── Pharmacy Full Module ─────────────────────────────────────── */}
          <Route 
            path="/pharmacy" 
            element={
              <RoleRoute allowedRoles={['ROLE_ADMIN', 'ROLE_PHARMACY_STAFF', 'ROLE_STOREKEEPER', 'ROLE_MEDICAL_STAFF']} portalSlug="pharmacy">
                <MainLayout />
              </RoleRoute>
            }
          >
            {PharmacyRoutes}
          </Route>

          {/* ── Fallbacks ───────────────────────────────────────────────── */}
          <Route path="/unauthorized" element={
            <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'center', minHeight: '100vh', flexDirection: 'column', gap: 16, fontFamily: 'Inter, sans-serif' }}>
              <h1 style={{ fontSize: '4rem', fontWeight: 800, color: 'var(--color-danger)', margin: 0 }}>403</h1>
              <p style={{ color: 'var(--color-text-muted)' }}>You don't have permission to view this page.</p>
              <a href="/" style={{ color: 'var(--color-info)' }}>← Return Home</a>
            </div>
          } />
          <Route path="*" element={
            <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'center', minHeight: '100vh', flexDirection: 'column', gap: 16, fontFamily: 'Inter, sans-serif' }}>
              <h1 style={{ fontSize: '4rem', fontWeight: 800, color: 'var(--color-text)', margin: 0 }}>404</h1>
              <p style={{ color: 'var(--color-text-muted)' }}>Page not found.</p>
              <a href="/" style={{ color: 'var(--color-info)' }}>← Return Home</a>
            </div>
          } />

        </Routes>
      </BrowserRouter>
    </QueryClientProvider>
  );
}

export default App;
