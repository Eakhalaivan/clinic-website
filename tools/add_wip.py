import os

files = [
    "CarePathwayBuilder.jsx", "PatientCarePathwayView.jsx", "TokenGeneration.jsx", "DicomViewer.jsx",
    "PnLStatement.jsx", "AIAssistant.jsx", "RoleDashboard.jsx", "RoleManagementPanel.jsx", "Reports.jsx",
    "PendingPharmacyReplacement.jsx", "ReturnWorklists.jsx", "ConsolidatedBills.jsx",
    "reports/SchedulesTab.jsx", "reports/ScheduleDrawer.jsx", "reports/ReportCard.jsx",
    "reports/ReportPreviewPanel.jsx", "PendingIndentPrescriptions.jsx",
    "PendingReplacementReturns.jsx", "InvoiceMatching.jsx", "SupplierReturns.jsx",
    "PharmacyClearance.jsx", "analytics/MonthOverMonth.jsx", "analytics/AnalyticsDashboard.jsx",
    "analytics/ABCAnalysis.jsx", "analytics/SupplierAnalytics.jsx", "MedicationAdministration.jsx",
    "StockTransfers.jsx"
]

frontend_dir = "/Users/eakhalaivan/Downloads/clinic-website/frontend/src"
pages_dir = os.path.join(frontend_dir, "pages")
components_dir = os.path.join(frontend_dir, "components")

import glob

for filename in files:
    matches = glob.glob(f"{pages_dir}/**/{filename}", recursive=True)
    if not matches:
        matches = glob.glob(f"{components_dir}/**/{filename}", recursive=True)
        
    for filepath in matches:
        with open(filepath, 'r') as f:
            content = f.read()
            
        if "WipBanner" in content:
            continue
            
        # Calculate relative path to components/ui/WipBanner
        rel_path = os.path.relpath(os.path.join(components_dir, "ui", "WipBanner"), os.path.dirname(filepath))
        import_stmt = f"import WipBanner from '{rel_path}';\n"
        
        # Insert import
        idx = content.find("import ")
        if idx == -1:
            content = import_stmt + content
        else:
            content = content[:idx] + import_stmt + content[idx:]
            
        # Insert banner right after first return (
        feature_name = os.path.basename(filepath).replace(".jsx", "")
        banner = f'<WipBanner feature="{feature_name}" note="Backend endpoint missing or unlinked" />'
        
        # Simple heuristic: find 'return (' or 'return <div' or similar
        return_idx = content.find('return (')
        if return_idx != -1:
            div_idx = content.find('<div', return_idx)
            if div_idx != -1:
                insert_pos = content.find('>', div_idx) + 1
                content = content[:insert_pos] + f"\n        {banner}" + content[insert_pos:]
        
        with open(filepath, 'w') as f:
            f.write(content)
        print(f"Updated {filepath}")

