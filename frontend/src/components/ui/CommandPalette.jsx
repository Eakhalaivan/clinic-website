import React, { useState, useEffect, useRef } from 'react';
import { 
  Search, Pill, FileText, Users, ShoppingCart, Activity, X, 
  Settings, User, LineChart, Package, HeartPulse, Stethoscope, 
  AlertTriangle, Thermometer, Shield, History, ClipboardList, 
  ScanLine, RotateCcw, Building2, BarChart3, CreditCard, ShieldCheck 
} from 'lucide-react';
import { useNavigate } from 'react-router-dom';

const quickLinks = [
  // Dashboards
  { id: 'pharmacy-dashboard', title: 'Pharmacy Dashboard', icon: BarChart3, path: '/pharmacy/dashboard', category: 'Dashboards' },
  { id: 'admin-dashboard', title: 'Admin Dashboard', icon: Shield, path: '/pharmacy/admin-dashboard', category: 'Dashboards' },
  { id: 'supervisor-dashboard', title: 'Supervisor Dashboard', icon: ShieldCheck, path: '/pharmacy/supervisor-dashboard', category: 'Dashboards' },
  { id: 'storekeeper-dashboard', title: 'Storekeeper Dashboard', icon: Package, path: '/pharmacy/storekeeper-dashboard', category: 'Dashboards' },
  
  // Inventory & Stock
  { id: 'medicines', title: 'Medicine Stock', icon: Pill, path: '/pharmacy/medicine-stock', category: 'Inventory' },
  { id: 'medicine-master', title: 'Medicine Master', icon: FileText, path: '/pharmacy/medicine-master', category: 'Inventory' },
  { id: 'expiry', title: 'Expiry Tracker', icon: Activity, path: '/pharmacy/expiry-tracker', category: 'Inventory' },
  { id: 'low-stock', title: 'Low Stock Alerts', icon: AlertTriangle, path: '/pharmacy/low-stock-alerts', category: 'Inventory' },
  { id: 'narcotics', title: 'Narcotics Register', icon: Shield, path: '/pharmacy/narcotics', category: 'Inventory' },
  { id: 'temperature-logs', title: 'Temperature Logs', icon: Thermometer, path: '/pharmacy/temperature-logs', category: 'Inventory' },
  { id: 'barcode-scanner', title: 'Barcode Scanner', icon: ScanLine, path: '/pharmacy/barcode-scanner', category: 'Inventory' },
  
  // Purchasing & Suppliers
  { id: 'purchase-orders', title: 'Purchase Orders', icon: ShoppingCart, path: '/pharmacy/purchase-orders', category: 'Purchasing' },
  { id: 'suppliers', title: 'Suppliers', icon: Building2, path: '/pharmacy/suppliers', category: 'Purchasing' },
  { id: 'grn-entry', title: 'GRN Entry', icon: ClipboardList, path: '/pharmacy/grnentry', category: 'Purchasing' },
  { id: 'supplier-returns', title: 'Supplier Returns', icon: RotateCcw, path: '/pharmacy/supplier-returns', category: 'Purchasing' },
  { id: 'invoice-matching', title: 'Invoice Matching', icon: FileText, path: '/pharmacy/invoice-matching', category: 'Purchasing' },
  
  // Sales & Dispensing
  { id: 'pharmacy-sales', title: 'Pharmacy Sales', icon: CreditCard, path: '/pharmacy/pharmacy-sales', category: 'Sales' },
  { id: 'direct-sales', title: 'Direct Pharmacy Sales', icon: CreditCard, path: '/pharmacy/direct-pharmacy-sales', category: 'Sales' },
  { id: 'pending-prescriptions', title: 'Pending Prescriptions', icon: ClipboardList, path: '/pharmacy/pending-prescriptions', category: 'Sales' },
  { id: 'dispense-worklists', title: 'Dispense Worklists', icon: ClipboardList, path: '/pharmacy/dispense-worklists', category: 'Sales' },
  { id: 'medicine-returns', title: 'Medicine Returns', icon: RotateCcw, path: '/pharmacy/medicine-returns', category: 'Sales' },
  { id: 'direct-medicine-returns', title: 'Direct Medicine Returns', icon: RotateCcw, path: '/pharmacy/direct-medicine-returns', category: 'Sales' },
  
  // Finance & Billing
  { id: 'billing', title: 'Billing Dashboard', icon: FileText, path: '/pharmacy/billing-dashboard', category: 'Finance' },
  { id: 'consolidated-bills', title: 'Consolidated Bills', icon: FileText, path: '/pharmacy/consolidated-bills', category: 'Finance' },
  { id: 'medicine-credit-bills', title: 'Medicine Credit Bills', icon: FileText, path: '/pharmacy/medicine-credit-bills', category: 'Finance' },
  { id: 'insurance-claims', title: 'Insurance Claims', icon: HeartPulse, path: '/pharmacy/insurance-claims', category: 'Finance' },
  { id: 'pharmacy-advances', title: 'Pharmacy Advances', icon: CreditCard, path: '/pharmacy/pharmacy-advances', category: 'Finance' },
  { id: 'pharmacy-clearance', title: 'Pharmacy Clearance', icon: ShieldCheck, path: '/pharmacy/pharmacy-clearance', category: 'Finance' },
  
  // Users & Settings
  { id: 'patients', title: 'Patients', icon: Users, path: '/pharmacy/patients', category: 'Profiles' },
  { id: 'doctors', title: 'Doctors', icon: Stethoscope, path: '/pharmacy/doctors', category: 'Profiles' },
  { id: 'user-management', title: 'User Management', icon: Users, path: '/pharmacy/user-management', category: 'Settings' },
  { id: 'role-management', title: 'Role Management', icon: Shield, path: '/pharmacy/role-management-panel', category: 'Settings' },
  { id: 'profile-settings', title: 'Profile Settings', icon: Settings, path: '/pharmacy/profile-settings', category: 'Settings' },
  
  // Analytics & Reports
  { id: 'reports', title: 'Reports', icon: FileText, path: '/pharmacy/reports', category: 'Analytics' },
  { id: 'analytics-dashboard', title: 'Analytics Dashboard', icon: LineChart, path: '/pharmacy/analytics/analytics-dashboard', category: 'Analytics' },
  { id: 'supplier-analytics', title: 'Supplier Analytics', icon: LineChart, path: '/pharmacy/analytics/supplier-analytics', category: 'Analytics' },
  { id: 'abc-analysis', title: 'ABC Analysis', icon: LineChart, path: '/pharmacy/analytics/abcanalysis', category: 'Analytics' },
];

export default function CommandPalette({ isOpen, onClose }) {
  const [query, setQuery] = useState('');
  const [selectedIndex, setSelectedIndex] = useState(0);
  const inputRef = useRef(null);
  const navigate = useNavigate();

  const filteredLinks = query === '' 
    ? quickLinks 
    : quickLinks.filter(link => 
        link.title.toLowerCase().includes(query.toLowerCase()) || 
        link.category.toLowerCase().includes(query.toLowerCase())
      );

  useEffect(() => {
    if (isOpen) {
      setQuery('');
      setSelectedIndex(0);
      setTimeout(() => inputRef.current?.focus(), 100);
    }
  }, [isOpen]);

  useEffect(() => {
    setSelectedIndex(0);
  }, [query]);

  useEffect(() => {
    if (!isOpen) return;

    const handleKeyDown = (e) => {
      if (e.key === 'ArrowDown') {
        e.preventDefault();
        setSelectedIndex((prev) => (prev + 1) % filteredLinks.length);
      } else if (e.key === 'ArrowUp') {
        e.preventDefault();
        setSelectedIndex((prev) => (prev - 1 + filteredLinks.length) % filteredLinks.length);
      } else if (e.key === 'Enter') {
        e.preventDefault();
        if (filteredLinks.length > 0) {
          const selected = filteredLinks[selectedIndex];
          navigate(selected.path);
          onClose();
        }
      } else if (e.key === 'Escape') {
        e.preventDefault();
        onClose();
      }
    };

    window.addEventListener('keydown', handleKeyDown);
    return () => window.removeEventListener('keydown', handleKeyDown);
  }, [isOpen, filteredLinks, selectedIndex, navigate, onClose]);

  if (!isOpen) return null;

  return (
    <div className="fixed inset-0 z-[100] flex items-start justify-center pt-[15vh] px-4 sm:px-6">
      <div 
        className="fixed inset-0 bg-slate-900/40 backdrop-blur-sm transition-opacity animate-in fade-in duration-200" 
        onClick={onClose}
      />
      
      <div className="relative w-full max-w-2xl bg-white rounded-2xl shadow-2xl overflow-hidden animate-in fade-in zoom-in-95 slide-in-from-top-10 duration-200 border border-slate-200">
        <div className="flex items-center border-b border-slate-100 px-4">
          <Search className="w-5 h-5 text-slate-400 shrink-0" />
          <input
            ref={inputRef}
            type="text"
            className="w-full bg-transparent border-0 py-4 pl-3 pr-4 text-[15px] text-slate-900 placeholder:text-slate-400 focus:ring-0 focus:outline-none"
            placeholder="Search medicines, invoices, suppliers..."
            value={query}
            onChange={(e) => setQuery(e.target.value)}
          />
          <button 
            onClick={onClose}
            className="p-1.5 rounded-lg text-slate-400 hover:text-slate-600 hover:bg-slate-100 transition-colors"
          >
            <X className="w-5 h-5" />
          </button>
        </div>

        <div className="max-h-[60vh] overflow-y-auto p-2">
          {filteredLinks.length === 0 ? (
            <div className="py-14 text-center">
              <div className="w-12 h-12 rounded-full bg-slate-100 flex items-center justify-center mx-auto mb-3">
                <Search className="w-5 h-5 text-slate-400" />
              </div>
              <p className="text-slate-900 font-medium text-[15px]">No results found</p>
              <p className="text-slate-500 text-[13px] mt-1">We couldn't find anything matching "{query}"</p>
            </div>
          ) : (
            <div className="space-y-1">
              <div className="px-3 pt-2 pb-1.5 text-[11px] font-bold text-slate-400 uppercase tracking-wider">
                Quick Navigation
              </div>
              {filteredLinks.map((link, index) => {
                const Icon = link.icon;
                const isSelected = index === selectedIndex;
                
                return (
                  <div
                    key={link.id}
                    onClick={() => {
                      navigate(link.path);
                      onClose();
                    }}
                    onMouseEnter={() => setSelectedIndex(index)}
                    className={`flex items-center gap-3 px-3 py-2.5 rounded-xl cursor-pointer transition-colors ${
                      isSelected ? 'bg-indigo-50 text-indigo-700' : 'text-slate-700 hover:bg-slate-50'
                    }`}
                  >
                    <div className={`p-2 rounded-lg ${isSelected ? 'bg-indigo-100 text-indigo-600' : 'bg-slate-100 text-slate-500'}`}>
                      <Icon className="w-4 h-4" />
                    </div>
                    <div className="flex-1">
                      <div className="text-[14px] font-medium leading-tight">{link.title}</div>
                      <div className={`text-[12px] mt-0.5 ${isSelected ? 'text-indigo-400/80' : 'text-slate-400'}`}>
                        {link.category}
                      </div>
                    </div>
                    {isSelected && (
                      <div className="text-[12px] font-medium text-indigo-500 px-2 flex items-center gap-1.5">
                        Jump to
                        <kbd className="bg-white border border-indigo-200 rounded px-1.5 py-0.5 shadow-sm text-[10px] text-indigo-600">↵</kbd>
                      </div>
                    )}
                  </div>
                );
              })}
            </div>
          )}
        </div>
        
        <div className="border-t border-slate-100 bg-slate-50/80 px-4 py-3 flex items-center justify-between text-[11px] font-medium text-slate-500">
          <div className="flex items-center gap-4">
            <div className="flex items-center gap-1.5">
              <kbd className="bg-white border border-slate-200 rounded-md px-1.5 py-0.5 shadow-sm text-[10px] font-sans">↑</kbd>
              <kbd className="bg-white border border-slate-200 rounded-md px-1.5 py-0.5 shadow-sm text-[10px] font-sans">↓</kbd>
              <span>to navigate</span>
            </div>
            <div className="flex items-center gap-1.5">
              <kbd className="bg-white border border-slate-200 rounded-md px-1.5 py-0.5 shadow-sm text-[10px] font-sans">↵</kbd>
              <span>to select</span>
            </div>
          </div>
          <div className="flex items-center gap-1.5">
            <kbd className="bg-white border border-slate-200 rounded-md px-1.5 py-0.5 shadow-sm text-[10px] font-sans">esc</kbd>
            <span>to close</span>
          </div>
        </div>
      </div>
    </div>
  );
}
