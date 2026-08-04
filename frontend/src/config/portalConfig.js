/**
 * portalConfig.js — Single source of truth for all 18 role portals.
 *
 * Each entry defines:
 *   slug         — URL prefix and login route key
 *   displayName  — Human-readable portal name
 *   role         — Spring Security role string
 *   themeColor   — Sidebar accent hex color
 *   dashboardRoute — Default redirect after login
 *   sidebarNav   — Ordered nav items for DashboardLayout sidebar
 *   authConfig   — Role-tailored login & registration hero text/badges
 */

export const PORTAL_CONFIGS = [
  // ── 1. Super Admin ────────────────────────────────────────────────────────
  {
    slug: 'super-admin',
    displayName: 'Super Admin',
    role: 'ROLE_SUPER_ADMIN',
    themeColor: '#1e1b4b',
    dashboardRoute: '/super-admin/dashboard',
    authConfig: {
      heroTitle: 'Enterprise Executive Control.',
      heroSubtitle: 'Multi-tenant branch management, security governance, audit logs, and platform health.',
      sideBadge: 'SUPER ADMIN CONSOLE',
      sideQuoteTitle: 'Centralized enterprise governance.',
      sideQuoteText: 'Global operational oversight, role compliance enforcement, and high-availability infrastructure metrics.',
      allowRegister: false,
    },
    dashboardTiles: [
      { label: 'Platform Overview', path: '/super-admin/dashboard',      icon: 'LayoutDashboard', description: 'View platform overview metrics' },
      { label: 'Branch Management', path: '/super-admin/branches',       icon: 'Building2', description: 'Manage and view branch management' },
      { label: 'User Management', path: '/super-admin/users',          icon: 'Users', description: 'Manage and view user management' },
      { label: 'Roles & Permissions', path: '/super-admin/rbac',           icon: 'ShieldCheck', description: 'Manage and view roles & permissions' },
      { label: 'Facilities', path: '/super-admin/facilities',     icon: 'Hospital', description: 'Manage and view facilities' },
      { label: 'Doctors', path: '/super-admin/doctors',        icon: 'Stethoscope', description: 'Manage and view doctors' },
      { label: 'Patients', path: '/super-admin/patients',       icon: 'UserRound', description: 'Manage and view patients' },
      { label: 'Analytics', path: '/super-admin/analytics',      icon: 'BarChart3', description: 'Manage and view analytics' },
      { label: 'Security Center', path: '/super-admin/security',       icon: 'Lock', description: 'Manage and view security center' },
      { label: 'Audit Logs', path: '/super-admin/audit-logs',     icon: 'FileText', description: 'Manage and view audit logs' },
      { label: 'Monitoring', path: '/super-admin/monitoring',     icon: 'Activity', description: 'Manage and view monitoring' },
      { label: 'Notifications', path: '/super-admin/notifications',  icon: 'Bell', description: 'Manage and view notifications' },
      { label: 'Subscription Plans', path: '/super-admin/subscriptions',  icon: 'CreditCard', description: 'Manage and view subscription plans' },
      { label: 'System Config', path: '/super-admin/settings',       icon: 'Settings', description: 'Configure system config preferences' },
      { label: 'CMS / Website', path: '/super-admin/cms',            icon: 'Globe', description: 'Manage and view cms / website' },
      { label: 'Backup & Recovery', path: '/super-admin/backup',         icon: 'HardDrive', description: 'Manage and view backup & recovery' },
    ],
  },

  // ── 2. Branch Admin ───────────────────────────────────────────────────────
  {
    slug: 'branch-admin',
    displayName: 'Branch Admin',
    role: 'ROLE_BRANCH_ADMIN',
    themeColor: '#5b21b6',
    dashboardRoute: '/branch-admin/dashboard',
    authConfig: {
      heroTitle: 'Branch Operations Portal.',
      heroSubtitle: 'Facility administration, staff scheduling, clinical inventory, and branch performance analytics.',
      sideBadge: 'BRANCH ADMIN SYSTEM',
      sideQuoteTitle: 'Excellence in local care delivery.',
      sideQuoteText: 'Coordinating doctors, reception staff, and resource allocations for maximum patient throughput.',
      allowRegister: false,
    },
    dashboardTiles: [
      { label: 'Branch Overview', path: '/branch-admin/dashboard',    icon: 'LayoutDashboard', description: 'View branch overview metrics' },
      { label: 'Staff', path: '/branch-admin/staff',        icon: 'Users', description: 'Manage and view staff' },
      { label: 'Doctors', path: '/branch-admin/doctors',      icon: 'Stethoscope', description: 'Manage and view doctors' },
      { label: 'Patients', path: '/branch-admin/patients',     icon: 'UserRound', description: 'Manage and view patients' },
      { label: 'Appointments', path: '/branch-admin/appointments', icon: 'CalendarDays', description: 'Manage and view appointments' },
      { label: 'Pharmacy', path: '/branch-admin/pharmacy',     icon: 'Pill', description: 'Manage and view pharmacy' },
      { label: 'Laboratory', path: '/branch-admin/laboratory',   icon: 'FlaskConical', description: 'Manage and view laboratory' },
      { label: 'Inventory', path: '/branch-admin/inventory',    icon: 'Package', description: 'Manage and view inventory' },
      { label: 'Billing', path: '/branch-admin/billing',      icon: 'Receipt', description: 'Manage and view billing' },
      { label: 'Reports', path: '/branch-admin/reports',      icon: 'FileBarChart', description: 'Manage and view reports' },
      { label: 'Analytics', path: '/branch-admin/analytics',    icon: 'BarChart3', description: 'Manage and view analytics' },
    ],
  },

  // ── 3. Doctor ─────────────────────────────────────────────────────────────
  {
    slug: 'doctor',
    displayName: 'Doctor',
    role: 'ROLE_DOCTOR',
    themeColor: '#15803d',
    dashboardRoute: '/doctor/dashboard',
    authConfig: {
      heroTitle: 'Provider Intelligence Access.',
      heroSubtitle: 'Empowering clinical precision, CDS safety checks, e-prescriptions, and patient care management.',
      sideBadge: 'CLINICAL PRECISION',
      sideQuoteTitle: 'Empowering physician excellence.',
      sideQuoteText: 'Real-time clinical decision support, instant EMR access, and seamless multidisciplinary care team collaboration.',
      allowRegister: false,
    },
    dashboardTiles: [], // Controlled entirely by DoctorDashboard.jsx now
  },

  // ── 4. Patient ────────────────────────────────────────────────────────────
  {
    slug: 'patient',
    displayName: 'Patient',
    role: 'ROLE_PATIENT',
    themeColor: '#0369a1',
    dashboardRoute: '/patient/dashboard',
    authConfig: {
      heroTitle: 'Welcome back to excellence.',
      heroSubtitle: 'Access your premium health concierge, appointment booking, and encrypted medical records.',
      sideBadge: 'SYSTEM SECURE',
      sideQuoteTitle: 'Privacy at the heart of care.',
      sideQuoteText: 'Your health data is encrypted with military-grade security, ensuring that your wellness journey remains private and exclusive.',
      allowRegister: true,
      registerTitle: 'Begin your journey towards precision medical care.',
      registerQuote: 'The art of medicine consists of amusing the patient while nature cures the disease. We provide the clarity nature requires.',
      registerQuoteAuthor: 'THE AURELIAN STANDARD',
    },
    dashboardTiles: [
      { label: 'Dashboard', path: '/patient/dashboard',      icon: 'LayoutDashboard', description: 'View dashboard metrics' },
      { label: 'Profile', path: '/patient/profile',        icon: 'UserCircle', description: 'Manage and view profile' },
      { label: 'Book Appointment', path: '/doctors',                icon: 'CalendarPlus', description: 'Manage and view book appointment' },
      { label: 'Appointments', path: '/patient/appointments',   icon: 'CalendarDays', description: 'Manage and view appointments' },
      { label: 'Medical Records', path: '/patient/records',        icon: 'FileHeart', description: 'Manage and view medical records' },
      { label: 'Prescriptions', path: '/patient/prescriptions',  icon: 'Pill', description: 'Manage and view prescriptions' },
      { label: 'Lab Reports', path: '/patient/lab-reports',    icon: 'FlaskConical', description: 'Manage and view lab reports' },
      { label: 'Radiology', path: '/patient/radiology-reports', icon: 'Scan', description: 'Manage and view radiology' },
      { label: 'Payments', path: '/patient/payments',       icon: 'CreditCard', description: 'Manage and view payments' },
      { label: 'Insurance', path: '/patient/insurance',      icon: 'ShieldCheck', description: 'Manage and view insurance' },
      { label: 'Health Timeline', path: '/patient/timeline',       icon: 'Activity', description: 'Manage and view health timeline' },
      { label: 'Orders', path: '/patient/orders',         icon: 'ShoppingCart', description: 'Manage and view orders' },
      { label: 'AI Assistant', path: '/patient/ai-assistant',   icon: 'Bot', description: 'Manage and view ai assistant' },
    ],
  },

  // ── 5. Reception ──────────────────────────────────────────────────────────
  {
    slug: 'reception',
    displayName: 'Reception',
    role: 'ROLE_RECEPTION',
    themeColor: '#b45309',
    dashboardRoute: '/reception/dashboard',
    authConfig: {
      heroTitle: 'Clinical Operations Gateway.',
      heroSubtitle: 'Streamlined patient triage, walk-in tokens, appointment scheduling, and front-desk billing.',
      sideBadge: 'FRONT DESK ACTIVE',
      sideQuoteTitle: 'Seamless patient intake.',
      sideQuoteText: 'Optimized patient check-ins, queue token generation, and real-time scheduling for maximum operational flow.',
      allowRegister: false,
    },
    dashboardTiles: [
      { label: 'Dashboard', path: '/reception/dashboard', icon: 'LayoutDashboard', description: 'View dashboard metrics' },
      { label: 'Register Patient', path: '/reception/register',  icon: 'UserPlus', description: 'Manage and view register patient' },
      { label: 'Walk-In', path: '/reception/walk-in',   icon: 'Footprints', description: 'Manage and view walk-in' },
      { label: 'Queue Management', path: '/reception/queue',     icon: 'ListOrdered', description: 'Manage and view queue management' },
      { label: 'Book Appointment', path: '/reception/book',      icon: 'CalendarPlus', description: 'Manage and view book appointment' },
      { label: 'Tokens', path: '/reception/tokens',    icon: 'Ticket', description: 'Manage and view tokens' },
      { label: 'Billing', path: '/reception/billing',   icon: 'Receipt', description: 'Manage and view billing' },
      { label: 'Check-in / Out', path: '/reception/checkin',   icon: 'CheckSquare', description: 'Manage and view check-in / out' },
      { label: 'Patient Search', path: '/reception/search',    icon: 'Search', description: 'Manage and view patient search' },
    ],
  },

  // ── 6. Nurse ──────────────────────────────────────────────────────────────
  {
    slug: 'nurse',
    displayName: 'Nurse',
    role: 'ROLE_NURSE',
    themeColor: '#0f766e',
    dashboardRoute: '/nurse/dashboard',
    authConfig: {
      heroTitle: 'Nursing Care Station.',
      heroSubtitle: 'Bedside vital signs tracking, medication administration logs, and ward monitoring.',
      sideBadge: 'TRIAGE READY',
      sideQuoteTitle: 'Dedicated to patient vitals & comfort.',
      sideQuoteText: 'Continuous vital sign tracking, medication verification, and direct physician alert escalation.',
      allowRegister: false,
    },
    dashboardTiles: [
      { label: 'Dashboard', path: '/nurse/dashboard',   icon: 'LayoutDashboard', description: 'View dashboard metrics' },
      { label: 'Assigned Patients', path: '/nurse/patients',    icon: 'Users', description: 'Manage and view assigned patients' },
      { label: 'Vital Signs', path: '/nurse/vitals',      icon: 'HeartPulse', description: 'Manage and view vital signs' },
      { label: 'Medication Admin', path: '/nurse/medication',  icon: 'Pill', description: 'Manage and view medication admin' },
      { label: 'Nursing Notes', path: '/nurse/notes',       icon: 'ClipboardList', description: 'Manage and view nursing notes' },
      { label: 'Patient Monitoring', path: '/nurse/monitoring',  icon: 'Activity', description: 'Manage and view patient monitoring' },
      { label: 'Ward Management', path: '/nurse/wards',       icon: 'BedDouble', description: 'Manage and view ward management' },
      { label: 'Task Management', path: '/nurse/tasks',       icon: 'CheckSquare', description: 'Manage and view task management' },
    ],
  },

  // ── 7. Pharmacist ─────────────────────────────────────────────────────────
  {
    slug: 'pharmacist',
    displayName: 'Pharmacist',
    role: 'ROLE_PHARMACIST',
    themeColor: '#4338ca',
    dashboardRoute: '/pharmacist/dashboard',
    authConfig: {
      heroTitle: 'Pharmaceutical Control Center.',
      heroSubtitle: 'e-Prescription dispensing, barcode safety checks, OTC sales, and batch expiry tracking.',
      sideBadge: 'GDR COMPLIANT',
      sideQuoteTitle: 'Safety in every prescription.',
      sideQuoteText: 'Automated drug interaction checks, barcode verification, and running narcotic register reconciliation.',
      allowRegister: false,
    },
    dashboardTiles: [
      { label: 'Dashboard', path: '/pharmacist/dashboard',        icon: 'LayoutDashboard', description: 'View dashboard metrics' },
      { label: 'Prescriptions', path: '/pharmacist/prescriptions',    icon: 'ClipboardList', description: 'Manage and view prescriptions' },
      { label: 'Dispensing', path: '/pharmacist/dispense',         icon: 'Pill', description: 'Manage and view dispensing' },
      { label: 'OTC Sales', path: '/pharmacist/sales',            icon: 'ShoppingCart', description: 'Manage and view otc sales' },
      { label: 'Inventory', path: '/pharmacist/inventory',        icon: 'Package', description: 'Manage and view inventory' },
      { label: 'Batch Tracking', path: '/pharmacist/batches',          icon: 'Layers', description: 'Manage and view batch tracking' },
      { label: 'Expiry Alerts', path: '/pharmacist/expiry',           icon: 'AlertTriangle', description: 'Manage and view expiry alerts' },
      { label: 'Purchase Orders', path: '/pharmacist/purchase-orders',  icon: 'FileText', description: 'Manage and view purchase orders' },
      { label: 'Suppliers', path: '/pharmacist/suppliers',        icon: 'Truck', description: 'Manage and view suppliers' },
      { label: 'Deliveries', path: '/pharmacist/deliveries',       icon: 'MapPin', description: 'Manage and view deliveries' },
      { label: 'Reports', path: '/pharmacist/reports',          icon: 'BarChart3', description: 'Manage and view reports' },
    ],
  },

  // ── 7b. Pharmacy (Full Module) ──────────────────────────────────────────────
  {
    slug: 'pharmacy',
    displayName: 'Pharmacy System',
    role: 'ROLE_PHARMACY_STAFF',
    themeColor: '#065f46', // Dark emerald
    dashboardRoute: '/pharmacy/dashboard',
    authConfig: {
      heroTitle: 'Comprehensive Pharmacy Management.',
      heroSubtitle: 'Inventory control, billing, prescription verification, and complete medical stock administration.',
      sideBadge: 'PHARMACY CORE',
      sideQuoteTitle: 'Precision in inventory and dispensing.',
      sideQuoteText: 'Manage stock alerts, multi-branch transfers, expiration trackers, and retail billing from a unified command center.',
      allowRegister: false,
    },
    dashboardTiles: [
      { label: 'Dashboard', path: '/pharmacy/dashboard', icon: 'LayoutDashboard', description: 'View dashboard metrics' },
      { label: 'Billing', path: '/pharmacy/billing-dashboard', icon: 'Receipt', description: 'Manage billing' },
      { label: 'Inventory', path: '/pharmacy/medicine-stock', icon: 'Package', description: 'Manage stock' },
      { label: 'Prescriptions', path: '/pharmacy/pending-prescriptions', icon: 'ClipboardList', description: 'Verify prescriptions' },
      { label: 'Purchase Orders', path: '/pharmacy/purchase-orders', icon: 'FileText', description: 'Manage procurement' },
      { label: 'Reports', path: '/pharmacy/analytics/analytics-dashboard', icon: 'BarChart2', description: 'View analytics' },
    ],
  },

  // ── 8. Lab Tech ───────────────────────────────────────────────────────────
  {
    slug: 'lab',
    displayName: 'Laboratory',
    role: 'ROLE_LAB_TECH',
    themeColor: '#0e7490',
    dashboardRoute: '/lab/dashboard',
    authConfig: {
      heroTitle: 'Diagnostic Pathology Lab.',
      heroSubtitle: 'Specimen sample collection, automated analyzer intake, and diagnostic report verification.',
      sideBadge: 'PATHOLOGY ONLINE',
      sideQuoteTitle: 'Precision diagnostic testing.',
      sideQuoteText: 'High-precision pathology testing with automated HL7 analyzer integration and rapid verification.',
      allowRegister: false,
    },
    dashboardTiles: [
      { label: 'Dashboard', path: '/lab/dashboard',      icon: 'LayoutDashboard', description: 'View dashboard metrics' },
      { label: 'Test Requests', path: '/lab/requests',       icon: 'FlaskConical', description: 'Manage and view test requests' },
      { label: 'Sample Collection', path: '/lab/samples',        icon: 'Pipette', description: 'Manage and view sample collection' },
      { label: 'Result Entry', path: '/lab/results',        icon: 'ClipboardPen', description: 'Manage and view result entry' },
      { label: 'Report Verify', path: '/lab/verify',         icon: 'BadgeCheck', description: 'Manage and view report verify' },
      { label: 'Notifications', path: '/lab/notifications',  icon: 'Bell', description: 'Manage and view notifications' },
    ],
  },

  // ── 9. Radiologist ────────────────────────────────────────────────────────
  {
    slug: 'radiologist',
    displayName: 'Radiologist',
    role: 'ROLE_RADIOLOGIST',
    themeColor: '#374151',
    dashboardRoute: '/radiologist/dashboard',
    authConfig: {
      heroTitle: 'Radiology & Imaging Suite.',
      heroSubtitle: 'Sub-second DICOM viewing, PACS image archiving, and structured radiological reporting.',
      sideBadge: 'PACS CONNECTED',
      sideQuoteTitle: 'Clarity in diagnostic imaging.',
      sideQuoteText: 'Sub-second DICOM rendering, multi-planar reconstruction, and AI-assisted lesion detection.',
      allowRegister: false,
    },
    dashboardTiles: [
      { label: 'Dashboard', path: '/radiologist/dashboard', icon: 'LayoutDashboard', description: 'View dashboard metrics' },
      { label: 'Imaging Requests', path: '/radiologist/requests',  icon: 'Scan', description: 'Manage and view imaging requests' },
      { label: 'DICOM Viewer', path: '/radiologist/viewer',    icon: 'MonitorPlay', description: 'Manage and view dicom viewer' },
      { label: 'Image Upload', path: '/radiologist/upload',    icon: 'Upload', description: 'Manage and view image upload' },
      { label: 'Reporting', path: '/radiologist/reporting', icon: 'FileText', description: 'Manage and view reporting' },
      { label: 'Archive', path: '/radiologist/archive',   icon: 'Archive', description: 'Manage and view archive' },
    ],
  },

  // ── 10. Inventory Manager ─────────────────────────────────────────────────
  {
    slug: 'inventory',
    displayName: 'Inventory',
    role: 'ROLE_INVENTORY_MANAGER',
    themeColor: '#c2410c',
    dashboardRoute: '/inventory/dashboard',
    authConfig: {
      heroTitle: 'Supply Chain & Logistics.',
      heroSubtitle: 'Warehouse management, purchase requisitions, supplier scoring, and inter-branch stock transfers.',
      sideBadge: 'INVENTORY ONLINE',
      sideQuoteTitle: 'Uninterrupted clinical supply chain.',
      sideQuoteText: 'Real-time stock auditing, supplier lead-time analysis, and automated minimum reorder thresholds.',
      allowRegister: false,
    },
    dashboardTiles: [
      { label: 'Dashboard', path: '/inventory/dashboard',       icon: 'LayoutDashboard', description: 'View dashboard metrics' },
      { label: 'Warehouses', path: '/inventory/warehouses',      icon: 'Warehouse', description: 'Manage and view warehouses' },
      { label: 'Purchase Orders', path: '/inventory/purchase-orders', icon: 'FileText', description: 'Manage and view purchase orders' },
      { label: 'Suppliers', path: '/inventory/suppliers',       icon: 'Truck', description: 'Manage and view suppliers' },
      { label: 'Stock Transfers', path: '/inventory/transfers',       icon: 'ArrowLeftRight', description: 'Manage and view stock transfers' },
      { label: 'Batch Tracking', path: '/inventory/batches',         icon: 'Layers', description: 'Manage and view batch tracking' },
      { label: 'Expiry Tracking', path: '/inventory/expiry',          icon: 'AlertTriangle', description: 'Manage and view expiry tracking' },
      { label: 'Branches', path: '/inventory/branches',        icon: 'Building2', description: 'Manage and view branches' },
      { label: 'Reports', path: '/inventory/reports',         icon: 'BarChart3', description: 'Manage and view reports' },
    ],
  },

  // ── 11. HR ────────────────────────────────────────────────────────────────
  {
    slug: 'hr',
    displayName: 'HR',
    role: 'ROLE_HR',
    themeColor: '#be185d',
    dashboardRoute: '/hr/dashboard',
    authConfig: {
      heroTitle: 'Human Capital Portal.',
      heroSubtitle: 'Staff duty rosters, credential licensing, attendance tracking, and payroll processing.',
      sideBadge: 'HR SECURE',
      sideQuoteTitle: 'Nurturing clinical talent.',
      sideQuoteText: 'Comprehensive staff administration, credential license compliance, and duty shift management.',
      allowRegister: false,
    },
    dashboardTiles: [
      { label: 'Dashboard', path: '/hr/dashboard',    icon: 'LayoutDashboard', description: 'View dashboard metrics' },
      { label: 'Employees', path: '/hr/employees',    icon: 'Users', description: 'Manage and view employees' },
      { label: 'Recruitment', path: '/hr/recruitment',  icon: 'UserPlus', description: 'Manage and view recruitment' },
      { label: 'Attendance', path: '/hr/attendance',   icon: 'CalendarCheck', description: 'Manage and view attendance' },
      { label: 'Leave', path: '/hr/leave',        icon: 'CalendarX', description: 'Manage and view leave' },
      { label: 'Payroll', path: '/hr/payroll',      icon: 'DollarSign', description: 'Manage and view payroll' },
      { label: 'Performance', path: '/hr/performance',  icon: 'TrendingUp', description: 'Manage and view performance' },
      { label: 'Documents', path: '/hr/documents',    icon: 'FolderOpen', description: 'Manage and view documents' },
    ],
  },

  // ── 12. Finance / Accountant ──────────────────────────────────────────────
  {
    slug: 'finance',
    displayName: 'Finance',
    role: 'ROLE_FINANCE',
    themeColor: '#3f6212',
    dashboardRoute: '/finance/dashboard',
    authConfig: {
      heroTitle: 'Financial Governance Hub.',
      heroSubtitle: 'Clinical invoices, insurance claims reconciliation, GST compliance, and revenue analytics.',
      sideBadge: 'AUDIT READY',
      sideQuoteTitle: 'Financial integrity & transparency.',
      sideQuoteText: 'Real-time ledger accounting, automated GST returns, and insurance claim settlement tracking.',
      allowRegister: false,
    },
    dashboardTiles: [
      { label: 'Dashboard', path: '/finance/dashboard',          icon: 'LayoutDashboard', description: 'View dashboard metrics' },
      { label: 'Invoices', path: '/finance/invoices',           icon: 'FileText', description: 'Manage and view invoices' },
      { label: 'Payments', path: '/finance/payments',           icon: 'CreditCard', description: 'Manage and view payments' },
      { label: 'Insurance Claims', path: '/finance/insurance-claims',   icon: 'ShieldCheck', description: 'Manage and view insurance claims' },
      { label: 'Revenue', path: '/finance/revenue',            icon: 'TrendingUp', description: 'Manage and view revenue' },
      { label: 'Expenses', path: '/finance/expenses',           icon: 'TrendingDown', description: 'Manage and view expenses' },
      { label: 'Profit & Loss', path: '/finance/pnl',                icon: 'BarChart3', description: 'Manage and view profit & loss' },
      { label: 'GST / Tax', path: '/finance/tax',                icon: 'Receipt', description: 'Manage and view gst / tax' },
      { label: 'Reports', path: '/finance/reports',            icon: 'FileBarChart', description: 'Manage and view reports' },
      { label: 'Budget', path: '/finance/budget',             icon: 'PieChart', description: 'Manage and view budget' },
    ],
  },

  // ── 13. Marketing / CRM ───────────────────────────────────────────────────
  {
    slug: 'marketing',
    displayName: 'Marketing',
    role: 'ROLE_MARKETING',
    themeColor: '#a21caf',
    dashboardRoute: '/marketing/dashboard',
    authConfig: {
      heroTitle: 'Patient Engagement & CRM.',
      heroSubtitle: 'Health campaigns, loyalty reward tiers, referral tracking, and automated reminders.',
      sideBadge: 'CAMPAIGNS LIVE',
      sideQuoteTitle: 'Empowering patient wellness journeys.',
      sideQuoteText: 'Automated health awareness broadcasts, membership perks, and patient feedback loops.',
      allowRegister: false,
    },
    dashboardTiles: [
      { label: 'Dashboard', path: '/marketing/dashboard',   icon: 'LayoutDashboard', description: 'View dashboard metrics' },
      { label: 'Campaigns', path: '/marketing/campaigns',   icon: 'Megaphone', description: 'Manage and view campaigns' },
      { label: 'Loyalty', path: '/marketing/loyalty',     icon: 'Star', description: 'Manage and view loyalty' },
      { label: 'Membership', path: '/marketing/membership',  icon: 'BadgeCheck', description: 'Manage and view membership' },
      { label: 'Referrals', path: '/marketing/referrals',   icon: 'Share2', description: 'Manage and view referrals' },
      { label: 'Gift Cards', path: '/marketing/gift-cards',  icon: 'Gift', description: 'Manage and view gift cards' },
      { label: 'Coupons', path: '/marketing/coupons',     icon: 'Ticket', description: 'Manage and view coupons' },
      { label: 'Email', path: '/marketing/email',       icon: 'Mail', description: 'Manage and view email' },
      { label: 'SMS', path: '/marketing/sms',         icon: 'MessageSquare', description: 'Manage and view sms' },
      { label: 'Analytics', path: '/marketing/analytics',   icon: 'BarChart3', description: 'Manage and view analytics' },
    ],
  },

  // ── 14. eCommerce / Store ─────────────────────────────────────────────────
  {
    slug: 'store',
    displayName: 'eCommerce Store',
    role: 'ROLE_STORE_MANAGER',
    themeColor: '#0f766e',
    dashboardRoute: '/store/dashboard',
    authConfig: {
      heroTitle: 'Healthcare Store Manager.',
      heroSubtitle: 'Online catalog management, medical equipment shipping, and customer order fulfillment.',
      sideBadge: 'STORE ONLINE',
      sideQuoteTitle: 'Quality wellness products delivered.',
      sideQuoteText: 'Direct-to-patient medical equipment delivery, prescription refilling, and order fulfillment.',
      allowRegister: false,
    },
    dashboardTiles: [
      { label: 'Dashboard', path: '/store/dashboard',  icon: 'LayoutDashboard', description: 'View dashboard metrics' },
      { label: 'Products', path: '/store/products',   icon: 'ShoppingBag', description: 'Manage and view products' },
      { label: 'Categories', path: '/store/categories', icon: 'Tag', description: 'Manage and view categories' },
      { label: 'Brands', path: '/store/brands',     icon: 'Award', description: 'Manage and view brands' },
      { label: 'Inventory', path: '/store/inventory',  icon: 'Package', description: 'Manage and view inventory' },
      { label: 'Orders', path: '/store/orders',     icon: 'ShoppingCart', description: 'Manage and view orders' },
      { label: 'Shipping', path: '/store/shipping',   icon: 'Truck', description: 'Manage and view shipping' },
      { label: 'Coupons', path: '/store/coupons',    icon: 'Ticket', description: 'Manage and view coupons' },
      { label: 'Reviews', path: '/store/reviews',    icon: 'Star', description: 'Manage and view reviews' },
      { label: 'Returns', path: '/store/returns',    icon: 'RefreshCw', description: 'Manage and view returns' },
    ],
  },

  // ── 15. Customer Support ──────────────────────────────────────────────────
  {
    slug: 'support',
    displayName: 'Customer Support',
    role: 'ROLE_SUPPORT',
    themeColor: '#0284c7',
    dashboardRoute: '/support/dashboard',
    authConfig: {
      heroTitle: 'Patient Concierge & Helpdesk.',
      heroSubtitle: 'Omnichannel live chat, ticket resolution, query tracking, and satisfaction metrics.',
      sideBadge: 'HELPDESK LIVE',
      sideQuoteTitle: 'Compassionate 24/7 patient assistance.',
      sideQuoteText: 'Rapid resolution of patient inquiries, appointment rescheduling, and billing support.',
      allowRegister: false,
    },
    dashboardTiles: [
      { label: 'Dashboard', path: '/support/dashboard',    icon: 'LayoutDashboard', description: 'View dashboard metrics' },
      { label: 'Tickets', path: '/support/tickets',      icon: 'TicketCheck', description: 'Manage and view tickets' },
      { label: 'Live Chat', path: '/support/chat',         icon: 'MessageCircle', description: 'Manage and view live chat' },
      { label: 'Queries', path: '/support/queries',      icon: 'HelpCircle', description: 'Manage and view queries' },
      { label: 'Complaints', path: '/support/complaints',   icon: 'AlertTriangle', description: 'Manage and view complaints' },
      { label: 'Resolutions', path: '/support/resolutions',  icon: 'CheckCircle', description: 'Manage and view resolutions' },
      { label: 'Knowledge Base', path: '/support/kb',        icon: 'BookOpen', description: 'Manage and view knowledge base' },
    ],
  },

  // ── 16. Vendor / Supplier ─────────────────────────────────────────────────
  {
    slug: 'vendor',
    displayName: 'Vendor Portal',
    role: 'ROLE_VENDOR',
    themeColor: '#78350f',
    dashboardRoute: '/vendor/dashboard',
    authConfig: {
      heroTitle: 'Supplier & Vendor Network.',
      heroSubtitle: 'Purchase order receipts, quotation submissions, GRN delivery tracking, and invoice status.',
      sideBadge: 'VENDOR GATEWAY',
      sideQuoteTitle: 'Trusted pharmaceutical partnerships.',
      sideQuoteText: 'Transparent purchase order fulfillment, digital invoices, and performance scorecard ratings.',
      allowRegister: false,
    },
    dashboardTiles: [
      { label: 'Dashboard', path: '/vendor/dashboard',       icon: 'LayoutDashboard', description: 'View dashboard metrics' },
      { label: 'Purchase Orders', path: '/vendor/purchase-orders', icon: 'FileText', description: 'Manage and view purchase orders' },
      { label: 'Quotations', path: '/vendor/quotations',      icon: 'FilePen', description: 'Manage and view quotations' },
      { label: 'Deliveries', path: '/vendor/deliveries',      icon: 'Truck', description: 'Manage and view deliveries' },
      { label: 'Invoices', path: '/vendor/invoices',        icon: 'Receipt', description: 'Manage and view invoices' },
      { label: 'Payments', path: '/vendor/payments',        icon: 'CreditCard', description: 'Manage and view payments' },
      { label: 'Requests', path: '/vendor/requests',        icon: 'ClipboardList', description: 'Manage and view requests' },
    ],
  },

  // ── 17. Insurance ─────────────────────────────────────────────────────────
  {
    slug: 'insurance',
    displayName: 'Insurance Portal',
    role: 'ROLE_INSURANCE',
    themeColor: '#9f1239',
    dashboardRoute: '/insurance/dashboard',
    authConfig: {
      heroTitle: 'Insurance Payer Portal.',
      heroSubtitle: 'Pre-authorization approvals, cashless claim verification, policy coverage lookup, and settlements.',
      sideBadge: 'PAYER NETWORK',
      sideQuoteTitle: 'Fast-track claim adjudication.',
      sideQuoteText: 'Direct integration for digital claim approvals, policy verification, and cashless hospitalizations.',
      allowRegister: false,
    },
    dashboardTiles: [
      { label: 'Dashboard', path: '/insurance/dashboard', icon: 'LayoutDashboard', description: 'View dashboard metrics' },
      { label: 'Claims', path: '/insurance/claims',    icon: 'FileText', description: 'Manage and view claims' },
      { label: 'Approvals', path: '/insurance/approvals', icon: 'BadgeCheck', description: 'Manage and view approvals' },
      { label: 'Policies', path: '/insurance/policies',  icon: 'ShieldCheck', description: 'Manage and view policies' },
      { label: 'Verify', path: '/insurance/verify',    icon: 'Search', description: 'Manage and view verify' },
      { label: 'Status Board', path: '/insurance/status',    icon: 'Activity', description: 'Manage and view status board' },
      { label: 'Payments', path: '/insurance/payments',  icon: 'CreditCard', description: 'Manage and view payments' },
    ],
  },

  // ── 18. Ambulance ─────────────────────────────────────────────────────────
  {
    slug: 'ambulance',
    displayName: 'Ambulance',
    role: 'ROLE_AMBULANCE',
    themeColor: '#b91c1c',
    dashboardRoute: '/ambulance/dashboard',
    authConfig: {
      heroTitle: 'Emergency Dispatch & Fleet.',
      heroSubtitle: 'Real-time GPS ambulance dispatch, emergency request triage, and paramedic telemetry.',
      sideBadge: 'EMERGENCY DISPATCH',
      sideQuoteTitle: 'Rapid emergency response.',
      sideQuoteText: 'Sub-minute dispatch response times, live vehicle tracking, and pre-hospital care telemetry.',
      allowRegister: false,
    },
    dashboardTiles: [
      { label: 'Dashboard', path: '/ambulance/dashboard',  icon: 'LayoutDashboard', description: 'View dashboard metrics' },
      { label: 'Emergencies', path: '/ambulance/requests',   icon: 'AlertTriangle', description: 'Manage and view emergencies' },
      { label: 'Assignments', path: '/ambulance/assign',     icon: 'UserCheck', description: 'Manage and view assignments' },
      { label: 'Fleet Tracker', path: '/ambulance/tracking',   icon: 'MapPin', description: 'Manage and view fleet tracker' },
      { label: 'Trip History', path: '/ambulance/trips',      icon: 'History', description: 'Manage and view trip history' },
    ],
  },

  // ── Accountant (mapped to finance) ────────────────────────────────────────
  {
    slug: 'accountant',
    displayName: 'Accountant',
    role: 'ROLE_ACCOUNTANT',
    themeColor: '#166534',
    dashboardRoute: '/finance/dashboard',
    authConfig: {
      heroTitle: 'Financial Governance Hub.',
      heroSubtitle: 'Clinical invoices, insurance claims reconciliation, GST compliance, and revenue analytics.',
      sideBadge: 'AUDIT READY',
      sideQuoteTitle: 'Financial integrity & transparency.',
      sideQuoteText: 'Real-time ledger accounting, automated GST returns, and insurance claim settlement tracking.',
      allowRegister: false,
    },
    dashboardTiles: [
      { label: 'Dashboard', path: '/finance/dashboard',        icon: 'LayoutDashboard', description: 'View dashboard metrics' },
      { label: 'Invoices', path: '/finance/invoices',         icon: 'FileText', description: 'Manage and view invoices' },
      { label: 'Payments', path: '/finance/payments',         icon: 'CreditCard', description: 'Manage and view payments' },
      { label: 'Insurance Claims', path: '/finance/insurance-claims', icon: 'ShieldCheck', description: 'Manage and view insurance claims' },
      { label: 'Revenue', path: '/finance/revenue',          icon: 'TrendingUp', description: 'Manage and view revenue' },
      { label: 'Reports', path: '/finance/reports',          icon: 'FileBarChart', description: 'Manage and view reports' },
    ],
  },
];

/** Look up a portal config by URL slug */
export const getPortalConfig = (slug) => {
  const config = PORTAL_CONFIGS.find((p) => p.slug === slug);
  if (config) return config;

  return {
    slug,
    displayName: 'Portal',
    themeColor: '#0F2A4A',
    dashboardRoute: '/unauthorized',
    dashboardTiles: [],
    authConfig: {
      heroTitle: 'Secure Portal Access.',
      heroSubtitle: 'Sign in to access your role-specific healthcare portal.',
      sideBadge: 'SYSTEM SECURE',
      sideQuoteTitle: 'Privacy at the heart of care.',
      sideQuoteText: 'Your health data is protected with enterprise security.',
      allowRegister: false,
    },
  };
};

/** Legacy object-keyed map for backward compatibility */
export const portals = Object.fromEntries(
  PORTAL_CONFIGS.map((p) => [
    p.slug,
    { title: `${p.displayName} Portal`, dashboard: p.dashboardRoute, color: p.themeColor },
  ])
);
