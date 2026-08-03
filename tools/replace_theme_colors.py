import re
import os

files_to_update = [
    'frontend/src/components/NotificationBell.jsx',
    'frontend/src/App.jsx',
    'frontend/src/pages/super-admin/SuperAdminConsole.jsx',
    'frontend/src/pages/super-admin/SuperAdminOverview.jsx',
    'frontend/src/pages/doctor/FollowUps.jsx',
    'frontend/src/pages/doctor/DoctorCalendar.jsx',
    'frontend/src/pages/doctor/ConsultationQueue.jsx',
    'frontend/src/pages/doctor/PatientDetail.jsx',
    'frontend/src/pages/doctor/PatientList.jsx',
    'frontend/src/pages/doctor/DoctorAnalytics.jsx',
    'frontend/src/pages/doctor/ClinicalNotes.jsx',
    'frontend/src/pages/doctor/AppointmentListToday.jsx',
    'frontend/src/pages/doctor/LabRequest.jsx',
    'frontend/src/pages/doctor/DoctorEarnings.jsx',
    'frontend/src/pages/pharmacist/PharmacistDashboard.jsx',
    'frontend/src/pages/hr/Employees.jsx',
    'frontend/src/pages/hr/LeaveManagement.jsx',
    'frontend/src/pages/insurance/InsuranceDashboard.jsx',
    'frontend/src/pages/finance/FinanceDashboard.jsx',
    'frontend/src/pages/finance/InvoicesList.jsx',
    'frontend/src/pages/inventory/WarehousesList.jsx',
    'frontend/src/pages/ecommerce/EcommerceDashboard.jsx',
    'frontend/src/pages/patient/PatientBilling.jsx',
    'frontend/src/pages/patient/AppointmentHistory.jsx',
    'frontend/src/pages/support/SupportDashboard.jsx',
    'frontend/src/pages/marketing/MarketingDashboard.jsx',
    'frontend/src/pages/vendor/VendorDashboard.jsx',
    'frontend/src/pages/nurse/VitalSignsEntry.jsx',
    'frontend/src/pages/nurse/WardManagement.jsx',
    'frontend/src/pages/nurse/NurseAssignedPatients.jsx'
]

# Note: GlobalSearchBar was already updated manually

mapping = {
    r'#fff(fff)?\b': 'var(--color-surface)',
    r'#(f8fafc|f1f5f9|f7f8fc|f9fafb|f3f4f6|e5e7eb)\b': 'var(--color-surface-alt)',
    r'#(e2e8f0|e4e6f1|d1d5db|cbd5e1)\b': 'var(--color-border)',
    r'#(0f172a|111827|1a1f36|1e293b|1f2937|374151)\b': 'var(--color-text)',
    r'#(64748b|475569|6b7280|94a3b8|9ca3af)\b': 'var(--color-text-muted)',
    r'#(22c55e|16a34a|15803d|10b981|059669)\b': 'var(--color-success)',
    r'#(dcfce7|f0fdf4|ecfdf5|d1fae5)\b': 'var(--color-success-bg)',
    r'#(ef4444|dc2626|b91c1c|f87171)\b': 'var(--color-danger)',
    r'#(fee2e2|fef2f2)\b': 'var(--color-danger-bg)',
    r'#(f59e0b|ea580c|d97706|fbbf24|f59e0b)\b': 'var(--color-warning)',
    r'#(fef3c7|fff7ed|fef08a)\b': 'var(--color-warning-bg)',
    r'#(3b82f6|2563eb|1d4ed8|60a5fa|38bdf8)\b': 'var(--color-info)',
    r'#(eff6ff|e0e7ff|dbeafe)\b': 'var(--color-info-bg)'
}

compiled_mapping = {re.compile(k, re.IGNORECASE): v for k, v in mapping.items()}

def replace_hex_in_string(s):
    # This will apply mapping to any hex string
    for pattern, replacement in compiled_mapping.items():
        s = pattern.sub(replacement, s)
    return s

def process_file(filepath):
    if not os.path.exists(filepath):
        print(f"Skipping {filepath} (does not exist)")
        return
        
    with open(filepath, 'r') as f:
        content = f.read()

    # Strategy: we want to replace hex colors only inside style={{...}} or style.background = '...'
    # Let's find all hex colors and replace them if they match our known hexes.
    # Actually, replacing all matching hex colors globally in the file is generally safe for these JSX files
    # because they are mostly inside inline styles, or sometimes classNames, but our mapping is safe.
    # Wait, in classNames like 'bg-[#f1f5f9]' it would become 'bg-[var(--color-surface-alt)]' which works in tailwind arbitrary values but is redundant.
    # Let's replace ONLY #hex values that are inside single or double quotes, or backticks, to be safe.
    
    def replacer(match):
        hex_val = match.group(0)
        return replace_hex_in_string(hex_val)

    # find # followed by 3-6 hex chars inside quotes
    new_content = re.sub(r'#[0-9a-fA-F]{3,6}\b', replacer, content)
    
    if new_content != content:
        with open(filepath, 'w') as f:
            f.write(new_content)
        print(f"Updated {filepath}")
    else:
        print(f"No changes for {filepath}")

for f in files_to_update:
    process_file(f)
