// Role definitions and configurations

export const ROLES = {
  SYSTEM_ADMIN: 'SYSTEM_ADMIN',
  SUPERVISOR: 'SUPERVISOR',
  SENIOR_MEDICAL_STAFF: 'SENIOR_MEDICAL_STAFF',
  MEDICAL_STAFF: 'MEDICAL_STAFF',
  BILLING_STAFF: 'BILLING_STAFF',
  PHARMACY_STAFF: 'PHARMACY_STAFF',
  RECEPTIONIST: 'RECEPTIONIST',
  AUDIT_COMPLIANCE: 'AUDIT_COMPLIANCE',
  LAB_TECHNICIAN: 'LAB_TECHNICIAN',
  STOREKEEPER: 'STOREKEEPER'
};

export const ROLE_LABELS = {
  [ROLES.SYSTEM_ADMIN]: 'System Admin',
  [ROLES.SUPERVISOR]: 'Supervisor',
  [ROLES.SENIOR_MEDICAL_STAFF]: 'Senior Medical Staff',
  [ROLES.MEDICAL_STAFF]: 'Medical Staff',
  [ROLES.BILLING_STAFF]: 'Billing Staff',
  [ROLES.PHARMACY_STAFF]: 'Pharmacy Staff',
  [ROLES.RECEPTIONIST]: 'Receptionist',
  [ROLES.AUDIT_COMPLIANCE]: 'Audit & Compliance',
  [ROLES.LAB_TECHNICIAN]: 'Lab Technician',
  [ROLES.STOREKEEPER]: 'Storekeeper'
};

export const ROLE_COLORS = {
  [ROLES.SYSTEM_ADMIN]: 'bg-slate-800 text-slate-100 border-slate-700', // Dark Navy
  [ROLES.SUPERVISOR]: 'bg-purple-100 text-purple-700 border-purple-200', // Purple
  [ROLES.SENIOR_MEDICAL_STAFF]: 'bg-teal-100 text-teal-700 border-teal-200', // Teal
  [ROLES.MEDICAL_STAFF]: 'bg-emerald-100 text-emerald-700 border-emerald-200', // Green
  [ROLES.BILLING_STAFF]: 'bg-amber-100 text-amber-700 border-amber-200', // Amber
  [ROLES.PHARMACY_STAFF]: 'bg-blue-100 text-blue-700 border-blue-200', // Blue
  [ROLES.RECEPTIONIST]: 'bg-rose-100 text-rose-700 border-rose-200', // Pink/Rose
  [ROLES.AUDIT_COMPLIANCE]: 'bg-orange-100 text-orange-700 border-orange-200', // Orange
  [ROLES.LAB_TECHNICIAN]: 'bg-cyan-100 text-cyan-700 border-cyan-200', // Cyan
  [ROLES.STOREKEEPER]: 'bg-stone-200 text-stone-700 border-stone-300' // Brown/Warm Gray
};

export const DASHBOARD_ROUTES = {
  [ROLES.SYSTEM_ADMIN]: '/pharmacy/admin-dashboard',
  [ROLES.SUPERVISOR]: '/pharmacy/supervisor-dashboard',
  [ROLES.SENIOR_MEDICAL_STAFF]: '/pharmacy/medical-dashboard',
  [ROLES.MEDICAL_STAFF]: '/pharmacy/medical-dashboard',
  [ROLES.BILLING_STAFF]: '/pharmacy/billing-dashboard',
  [ROLES.PHARMACY_STAFF]: '/pharmacy/dashboard',
  [ROLES.RECEPTIONIST]: '/pharmacy/role-dashboard',
  [ROLES.AUDIT_COMPLIANCE]: '/pharmacy/role-dashboard',
  [ROLES.LAB_TECHNICIAN]: '/pharmacy/role-dashboard',
  [ROLES.STOREKEEPER]: '/pharmacy/storekeeper-dashboard',
  // Legacy keys (safety net)
  'ADMIN':               '/pharmacy/admin-dashboard',
  'MEDICINE_USER':       '/pharmacy/dashboard',
  'BILLING_USER':        '/pharmacy/billing-dashboard',
};

export const MODULE_PERMISSIONS = {
  CLINICAL: [
    { id: 'PRESCRIPTIONS', label: 'Manage Prescriptions' },
    { id: 'CLINICAL_RECORDS', label: 'View Clinical Records' },
    { id: 'BASIC_PRESCRIPTIONS', label: 'Basic Prescriptions' }
  ],
  BILLING: [
    { id: 'BILLING', label: 'Process Billing' },
    { id: 'INVOICES', label: 'Manage Invoices' },
    { id: 'ADVANCES', label: 'Process Advances' },
    { id: 'CLEARANCE', label: 'Clearance Processing' }
  ],
  INVENTORY: [
    { id: 'INVENTORY', label: 'Manage Inventory' },
    { id: 'INDENT', label: 'Process Indents' },
    { id: 'RETURNS', label: 'Process Returns' },
    { id: 'STOCK_MANAGEMENT', label: 'Stock Management' },
    { id: 'PURCHASE_ORDERS', label: 'Purchase Orders' }
  ],
  REPORTS: [
    { id: 'VIEW_REPORTS', label: 'View Reports' },
    { id: 'VIEW_LOGS', label: 'View Logs' },
    { id: 'REPORTS', label: 'Manage Reports' }
  ],
  ADMINISTRATION: [
    { id: 'ALL', label: 'Full System Access' },
    { id: 'APPROVALS', label: 'Manage Approvals' },
    { id: 'PATIENT_REGISTRATION', label: 'Patient Registration' },
    { id: 'UHID', label: 'UHID Creation' }
  ]
};

export const getRoleColor = (roleName) => {
  if (!roleName) return 'bg-gray-100 text-gray-700 border-gray-200';
  const normalized = roleName.replace(/ /g, '_').toUpperCase();
  return ROLE_COLORS[normalized] || ROLE_COLORS[roleName] || 'bg-gray-100 text-gray-700 border-gray-200';
};

export const getBaseRoleForUI = (role) => {
  if (!role) return ROLES.PHARMACY_STAFF;
  if (Object.values(ROLES).includes(role)) return role;
  
  const upper = role.toUpperCase();
  if (upper.includes('ADMIN')) return ROLES.SYSTEM_ADMIN;
  if (upper.includes('PHARMAC')) return ROLES.PHARMACY_STAFF;
  if (upper.includes('BILL') || upper.includes('ACCOUNT') || upper.includes('CASH')) return ROLES.BILLING_STAFF;
  if (upper.includes('STORE') || upper.includes('INVENT') || upper.includes('PURCHASE')) return ROLES.STOREKEEPER;
  if (upper.includes('LAB') || upper.includes('PATHOLOG')) return ROLES.LAB_TECHNICIAN;
  if (upper.includes('SUPERVISOR') || upper.includes('MANAGER')) return ROLES.SUPERVISOR;
  if (upper.includes('RECEPTION') || upper.includes('FRONT')) return ROLES.RECEPTIONIST;
  if (upper.includes('AUDIT') || upper.includes('COMPLIANCE')) return ROLES.AUDIT_COMPLIANCE;
  if (upper.includes('SENIOR') && upper.includes('MEDIC')) return ROLES.SENIOR_MEDICAL_STAFF;
  if (upper.includes('MEDIC') || upper.includes('DOCTOR') || upper.includes('PHYSICIAN') || upper.includes('NURS')) return ROLES.MEDICAL_STAFF;
  
  return ROLES.PHARMACY_STAFF;
};



import { 
  Building2, ShoppingCart, RotateCcw, LayoutDashboard, CreditCard,
  Settings, ArrowLeftRight, ClipboardList, Store, Undo2, Syringe,
  Banknote, Receipt, FileCheck, Stethoscope, RefreshCw, Box,
  BarChart3, ListTodo, Pill, LogOut, ChevronDown, Truck, Users,
  FileText, AlertTriangle, CalendarX, ShieldAlert, Thermometer,
  ShieldCheck, ScanBarcode, Shield, PlusCircle, Calendar,
  TrendingUp, ClipboardCheck, FilePlus, ShoppingBag, BarChart2, UserCog, Zap, Package,
  UserCircle, KeyRound, Menu, UserRound
} from 'lucide-react';

export const NAV_BY_ROLE = {
  SYSTEM_ADMIN: [
    { label: 'System Admin Dashboard', path: '/dashboard/admin',      icon: LayoutDashboard, description: 'View pharmacy metrics' },
    { label: 'Medicine Master', path: '/medicines',            icon: Pill, description: 'Manage medicine master' },
    { label: 'Stock Management', path: '/stocks',               icon: Package, description: 'Manage stock management' },
    { label: 'Pharmacy Sales', path: '/sales',                icon: ShoppingCart, description: 'Manage pharmacy sales' },
    { label: 'Medicine Returns', path: '/returns',              icon: RotateCcw, description: 'Manage medicine returns' },
    { label: 'Return Worklists', path: '/return-worklists',     icon: ClipboardList, description: 'Manage return worklists' },
    { label: 'Pending Indent Pres.', path: '/pending-indents',      icon: FilePlus, description: 'Manage pending indent pres.' },
    { label: 'Pending Pharmacy Rep.', path: '/pending-replacement',  icon: RefreshCw, description: 'Manage pending pharmacy rep.' },
    { label: 'Consolidated Bills', path: '/consolidated-bills',   icon: Receipt, description: 'Manage consolidated bills' },
    { label: 'Purchase Orders', path: '/purchase-orders',      icon: ShoppingBag, description: 'Manage purchase orders' },
    { label: 'GRN Entry', path: '/grn',                  icon: Truck, description: 'Manage grn entry' },
    { label: 'Suppliers', path: '/suppliers',            icon: Building2, description: 'Manage suppliers' },
    { label: 'Doctors', path: '/doctors',              icon: UserRound, description: 'Manage doctors' },
    { label: 'Patients', path: '/patients',             icon: Users, description: 'Manage patients' },
    { label: 'Low Stock Alerts', path: '/low-stock-alerts',     icon: AlertTriangle, description: 'Manage low stock alerts' },
    { label: 'Expiry Tracker', path: '/expiry-tracker',       icon: Calendar, description: 'Manage expiry tracker' },
    { label: 'Drug Interactions', path: '/drug-interactions',    icon: Zap, description: 'Manage drug interactions' },
    { label: 'Temperature Logs', path: '/temperature-logs',     icon: Thermometer, description: 'Manage temperature logs' },
    { label: 'Narcotics Register', path: '/narcotics',            icon: Shield, description: 'Manage narcotics register' },
    { label: 'Barcode Scanner', path: '/barcode-scanner',      icon: ScanBarcode, description: 'Manage barcode scanner' },
    { label: 'Insurance Claims', path: '/insurance-claims',     icon: FileCheck, description: 'Manage insurance claims' },
    { label: 'Analytics', path: '/analytics',            icon: TrendingUp, description: 'Manage analytics' },
    { label: 'Reports', path: '/reports',              icon: BarChart2, description: 'Manage reports' },
    { label: 'User Management', path: '/users',                icon: UserCog, description: 'Manage user management' },
    { label: 'Role Management', path: '/roles',                icon: ShieldCheck, description: 'Manage role management' },
    { label: 'Pharmacy Advances', path: '/advances',             icon: Banknote, description: 'Manage pharmacy advances' },
    { label: 'Pharmacy Clearance', path: '/clearance',            icon: FileCheck, description: 'Manage pharmacy clearance' },
    { label: 'Product Performance', path: '/performance',          icon: TrendingUp, description: 'Manage product performance' },
    { label: 'Profile Settings', path: '/profile',              icon: UserCircle, description: 'Manage profile settings' },
    { label: 'Reset Password', path: '/reset-password',       icon: KeyRound, description: 'Manage reset password' },
    { label: 'Admin Dashboard', path: '/admin-dashboard', icon: ClipboardList, description: 'View Admin Dashboard' },
    { label: 'Billing Dashboard', path: '/billing-dashboard', icon: ClipboardList, description: 'View Billing Dashboard' },
    { label: 'Direct Medicine Returns', path: '/direct-medicine-returns', icon: ClipboardList, description: 'View Direct Medicine Returns' },
    { label: 'Direct Pharmacy Sales', path: '/direct-pharmacy-sales', icon: ClipboardList, description: 'View Direct Pharmacy Sales' },
    { label: 'Dispense Worklists', path: '/dispense-worklists', icon: ClipboardList, description: 'View Dispense Worklists' },
    { label: 'Force Change Password Page', path: '/force-change-password-page', icon: ClipboardList, description: 'View Force Change Password Page' },
    { label: 'Grnentry', path: '/grnentry', icon: ClipboardList, description: 'View Grnentry' },
    { label: 'Invoice Matching', path: '/invoice-matching', icon: ClipboardList, description: 'View Invoice Matching' },
    { label: 'Medical Dashboard', path: '/medical-dashboard', icon: ClipboardList, description: 'View Medical Dashboard' },
    { label: 'Medicine Credit Bills', path: '/medicine-credit-bills', icon: ClipboardList, description: 'View Medicine Credit Bills' },
    { label: 'Medicine Credit Returns', path: '/medicine-credit-returns', icon: ClipboardList, description: 'View Medicine Credit Returns' },
    { label: 'Medicine Master', path: '/medicine-master', icon: ClipboardList, description: 'View Medicine Master' },
    { label: 'Medicine Returns', path: '/medicine-returns', icon: ClipboardList, description: 'View Medicine Returns' },
    { label: 'Medicine Stock', path: '/medicine-stock', icon: ClipboardList, description: 'View Medicine Stock' },
    { label: 'Pending Indent Prescriptions', path: '/pending-indent-prescriptions', icon: ClipboardList, description: 'View Pending Indent Prescriptions' },
    { label: 'Pending Pharmacy Replacement', path: '/pending-pharmacy-replacement', icon: ClipboardList, description: 'View Pending Pharmacy Replacement' },
    { label: 'Pending Prescriptions', path: '/pending-prescriptions', icon: ClipboardList, description: 'View Pending Prescriptions' },
    { label: 'Pending Replacement Returns', path: '/pending-replacement-returns', icon: ClipboardList, description: 'View Pending Replacement Returns' },
    { label: 'Pharmacy Advances', path: '/pharmacy-advances', icon: ClipboardList, description: 'View Pharmacy Advances' },
    { label: 'Pharmacy Clearance', path: '/pharmacy-clearance', icon: ClipboardList, description: 'View Pharmacy Clearance' },
    { label: 'Dashboard', path: '/dashboard', icon: ClipboardList, description: 'View Dashboard' },
    { label: 'Pharmacy Sales', path: '/pharmacy-sales', icon: ClipboardList, description: 'View Pharmacy Sales' },
    { label: 'Product Sales Performance', path: '/product-sales-performance', icon: ClipboardList, description: 'View Product Sales Performance' },
    { label: 'Profile Settings', path: '/profile-settings', icon: ClipboardList, description: 'View Profile Settings' },
    { label: 'Purchase Order Detail', path: '/purchase-order-detail', icon: ClipboardList, description: 'View Purchase Order Detail' },
    { label: 'Role Dashboard', path: '/role-dashboard', icon: ClipboardList, description: 'View Role Dashboard' },
    { label: 'Role Management Panel', path: '/role-management-panel', icon: ClipboardList, description: 'View Role Management Panel' },
    { label: 'Storekeeper Dashboard', path: '/storekeeper-dashboard', icon: ClipboardList, description: 'View Storekeeper Dashboard' },
    { label: 'Supervisor Dashboard', path: '/supervisor-dashboard', icon: ClipboardList, description: 'View Supervisor Dashboard' },
    { label: 'Supplier Returns', path: '/supplier-returns', icon: ClipboardList, description: 'View Supplier Returns' },
    { label: 'User Management', path: '/user-management', icon: ClipboardList, description: 'View User Management' },
    { label: 'Reports Report Card', path: '/reports/report-card', icon: ClipboardList, description: 'View Reports Report Card' },
    { label: 'Reports Report Preview Panel', path: '/reports/report-preview-panel', icon: ClipboardList, description: 'View Reports Report Preview Panel' },
    { label: 'Reports Schedule Drawer', path: '/reports/schedule-drawer', icon: ClipboardList, description: 'View Reports Schedule Drawer' },
    { label: 'Reports Schedules Tab', path: '/reports/schedules-tab', icon: ClipboardList, description: 'View Reports Schedules Tab' },
    { label: 'Analytics Abcanalysis', path: '/analytics/abcanalysis', icon: ClipboardList, description: 'View Analytics Abcanalysis' },
    { label: 'Analytics Analytics Dashboard', path: '/analytics/analytics-dashboard', icon: ClipboardList, description: 'View Analytics Analytics Dashboard' },
    { label: 'Analytics Month Over Month', path: '/analytics/month-over-month', icon: ClipboardList, description: 'View Analytics Month Over Month' },
    { label: 'Analytics Supplier Analytics', path: '/analytics/supplier-analytics', icon: ClipboardList, description: 'View Analytics Supplier Analytics' },
],
  PHARMACY_STAFF: [
    { label: 'Pharmacy Dashboard', path: '/dashboard/pharmacy',   icon: LayoutDashboard, description: 'View pharmacy metrics' },
    { label: 'Pharmacy Sales', path: '/sales',                icon: ShoppingCart, description: 'Manage pharmacy sales' },
    { label: 'Medicine Returns', path: '/returns',              icon: RotateCcw, description: 'Manage medicine returns' },
    { label: 'Medicine Master', path: '/medicines',            icon: Pill, description: 'Manage medicine master' },
    { label: 'Barcode Scanner', path: '/barcode-scanner',      icon: ScanBarcode, description: 'Manage barcode scanner' },
    { label: 'Low Stock Alerts', path: '/low-stock-alerts',     icon: AlertTriangle, description: 'Manage low stock alerts' },
    { label: 'Expiry Tracker', path: '/expiry-tracker',       icon: Calendar, description: 'Manage expiry tracker' },
    { label: 'Drug Interactions', path: '/drug-interactions',    icon: Zap, description: 'Manage drug interactions' },
    { label: 'Temperature Logs', path: '/temperature-logs',     icon: Thermometer, description: 'Manage temperature logs' },
    { label: 'Narcotics Register', path: '/narcotics',            icon: Shield, description: 'Manage narcotics register' },
    { label: 'Profile Settings', path: '/profile',              icon: UserCircle, description: 'Manage profile settings' },
    { label: 'Reset Password', path: '/reset-password',       icon: KeyRound, description: 'Manage reset password' },
  ],
  BILLING_STAFF: [
    { label: 'Billing Dashboard', path: '/dashboard/billing',    icon: LayoutDashboard, description: 'View pharmacy metrics' },
    { label: 'Pharmacy Sales', path: '/sales',                icon: ShoppingCart, description: 'Manage pharmacy sales' },
    { label: 'Medicine Returns', path: '/returns',              icon: RotateCcw, description: 'Manage medicine returns' },
    { label: 'Consolidated Bills', path: '/consolidated-bills',   icon: Receipt, description: 'Manage consolidated bills' },
    { label: 'Patients', path: '/patients',             icon: Users, description: 'Manage patients' },
    { label: 'Insurance Claims', path: '/insurance-claims',     icon: FileCheck, description: 'Manage insurance claims' },
    { label: 'Pharmacy Advances', path: '/advances',             icon: Banknote, description: 'Manage pharmacy advances' },
    { label: 'Pharmacy Clearance', path: '/clearance',            icon: FileCheck, description: 'Manage pharmacy clearance' },
    { label: 'Profile Settings', path: '/profile',              icon: UserCircle, description: 'Manage profile settings' },
    { label: 'Reset Password', path: '/reset-password',       icon: KeyRound, description: 'Manage reset password' },
  ],
  STOREKEEPER: [
    { label: 'Store Dashboard', path: '/dashboard/store',      icon: LayoutDashboard, description: 'View pharmacy metrics' },
    { label: 'Stock Management', path: '/stocks',               icon: Package, description: 'Manage stock management' },
    { label: 'Purchase Orders', path: '/purchase-orders',      icon: ShoppingBag, description: 'Manage purchase orders' },
    { label: 'GRN Entry', path: '/grn',                  icon: Truck, description: 'Manage grn entry' },
    { label: 'Suppliers', path: '/suppliers',            icon: Building2, description: 'Manage suppliers' },
    { label: 'Doctors', path: '/doctors',              icon: UserRound, description: 'Manage doctors' },
    { label: 'Low Stock Alerts', path: '/low-stock-alerts',     icon: AlertTriangle, description: 'Manage low stock alerts' },
    { label: 'Expiry Tracker', path: '/expiry-tracker',       icon: Calendar, description: 'Manage expiry tracker' },
    { label: 'Temperature Logs', path: '/temperature-logs',     icon: Thermometer, description: 'Manage temperature logs' },
    { label: 'Profile Settings', path: '/profile',              icon: UserCircle, description: 'Manage profile settings' },
    { label: 'Reset Password', path: '/reset-password',       icon: KeyRound, description: 'Manage reset password' },
  ],
  SUPERVISOR: [
    { label: 'Supervisor Dashboard', path: '/dashboard/supervisor', icon: LayoutDashboard, description: 'View pharmacy metrics' },
    { label: 'Return Worklists', path: '/return-worklists',     icon: ClipboardList, description: 'Manage return worklists' },
    { label: 'Analytics', path: '/analytics',            icon: TrendingUp, description: 'Manage analytics' },
    { label: 'Reports', path: '/reports',              icon: BarChart2, description: 'Manage reports' },
    { label: 'Profile Settings', path: '/profile',              icon: UserCircle, description: 'Manage profile settings' },
    { label: 'Reset Password', path: '/reset-password',       icon: KeyRound, description: 'Manage reset password' },
  ],
  RECEPTIONIST: [
    { label: 'Reception Dashboard', path: '/dashboard/reception', icon: LayoutDashboard, description: 'View pharmacy metrics' },
    { label: 'Patients', path: '/patients',             icon: Users, description: 'Manage patients' },
    { label: 'Profile Settings', path: '/profile',              icon: UserCircle, description: 'Manage profile settings' },
    { label: 'Reset Password', path: '/reset-password',       icon: KeyRound, description: 'Manage reset password' },
  ],
  MEDICAL_STAFF: [
    { label: 'Medical Dashboard', path: '/dashboard/medical',    icon: LayoutDashboard, description: 'View pharmacy metrics' },
    { label: 'Drug Interactions', path: '/drug-interactions',    icon: Zap, description: 'Manage drug interactions' },
    { label: 'Profile Settings', path: '/profile',              icon: UserCircle, description: 'Manage profile settings' },
    { label: 'Reset Password', path: '/reset-password',       icon: KeyRound, description: 'Manage reset password' },
  ],
  SENIOR_MEDICAL_STAFF: [
    { label: 'Senior Medical Dashboard', path: '/dashboard/senior-medical', icon: LayoutDashboard, description: 'View pharmacy metrics' },
    { label: 'Drug Interactions', path: '/drug-interactions',    icon: Zap, description: 'Manage drug interactions' },
    { label: 'Profile Settings', path: '/profile',              icon: UserCircle, description: 'Manage profile settings' },
    { label: 'Reset Password', path: '/reset-password',       icon: KeyRound, description: 'Manage reset password' },
  ],
  AUDIT_COMPLIANCE: [
    { label: 'Audit Dashboard', path: '/dashboard/audit',   icon: LayoutDashboard, description: 'View pharmacy metrics' },
    { label: 'Reports', path: '/reports',            icon: BarChart2, description: 'Manage reports' },
    { label: 'Profile Settings', path: '/profile',            icon: UserCircle, description: 'Manage profile settings' },
    { label: 'Reset Password', path: '/reset-password',     icon: KeyRound, description: 'Manage reset password' },
  ],
  LAB_TECHNICIAN: [
    { label: 'Lab Dashboard', path: '/dashboard/lab',      icon: LayoutDashboard, description: 'View pharmacy metrics' },
    { label: 'Medicine Master', path: '/medicines',          icon: Pill, description: 'Manage medicine master' },
    { label: 'Drug Interactions', path: '/drug-interactions',  icon: Zap, description: 'Manage drug interactions' },
    { label: 'Profile Settings', path: '/profile',            icon: UserCircle, description: 'Manage profile settings' },
    { label: 'Reset Password', path: '/reset-password',     icon: KeyRound, description: 'Manage reset password' },
  ]
};