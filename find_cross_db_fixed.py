import os
import re

clinic_packages = [
    "com.healthcare.clinic.identity", 
    "com.healthcare.clinic.patient", 
    "com.healthcare.clinic.doctor", 
    "com.healthcare.clinic.nursing", 
    "com.healthcare.clinic.reception", 
    "com.healthcare.clinic.appointment", 
    "com.healthcare.clinic.medicalrecord", 
    "com.healthcare.clinic.laboratory", 
    "com.healthcare.clinic.billing", 
    "com.healthcare.clinic.insurance", 
    "com.healthcare.clinic.hr", 
    "com.healthcare.clinic.finance", 
    "com.healthcare.clinic.branch", 
    "com.healthcare.clinic.superadmin", 
    "com.healthcare.clinic.marketing", 
    "com.healthcare.clinic.ecommerce", 
    "com.healthcare.clinic.support", 
    "com.healthcare.clinic.vendor", 
    "com.healthcare.clinic.ambulance", 
    "com.healthcare.clinic.radiology", 
    "com.healthcare.clinic.analytics", 
    "com.healthcare.clinic.notification", 
    "com.healthcare.clinic.clinicaldecision", 
    "com.healthcare.clinic.backoffice.inventory"
]
pharmacy_packages = [
    "com.healthcare.clinic.inventory.entity", 
    "com.healthcare.clinic.inventory.pharmacy.entity", 
    "com.healthcare.clinic.inventory.sales.model"
]

def is_clinic(pkg):
    for p in clinic_packages:
        if pkg == p or pkg.startswith(p + "."):
            return True
    return False

def is_pharmacy(pkg):
    for p in pharmacy_packages:
        if pkg == p or pkg.startswith(p + "."):
            return True
    return False

java_files = []
for root, dirs, files in os.walk("./backend/src/main/java"):
    for file in files:
        if file.endswith(".java"):
            java_files.append(os.path.join(root, file))

crossings = []
for f in java_files:
    with open(f, 'r') as fp:
        content = fp.read()
        pkg_match = re.search(r'package\s+([a-zA-Z0-9_\.]+);', content)
        if not pkg_match: continue
        pkg = pkg_match.group(1)
        
        this_is_clinic = is_clinic(pkg)
        this_is_pharmacy = is_pharmacy(pkg)
        if not this_is_clinic and not this_is_pharmacy: continue
            
        imports = {}
        for line in content.split('\n'):
            imp_match = re.search(r'import\s+([a-zA-Z0-9_\.]+)\.([a-zA-Z0-9_]+);', line)
            if imp_match: imports[imp_match.group(2)] = imp_match.group(1)
                
        lines = content.split('\n')
        for i, line in enumerate(lines):
            if '@ManyToOne' in line or '@OneToMany' in line or '@JoinColumn' in line or '@OneToOne' in line:
                for j in range(i+1, min(i+10, len(lines))):
                    field_match = re.search(r'private\s+(?:List<)?([a-zA-Z0-9_]+)(?:>)?\s+[a-zA-Z0-9_]+', lines[j])
                    if field_match:
                        field_type = field_match.group(1)
                        target_pkg = imports.get(field_type)
                        if not target_pkg: target_pkg = pkg
                        
                        target_is_clinic = is_clinic(target_pkg)
                        target_is_pharmacy = is_pharmacy(target_pkg)
                        
                        if this_is_clinic and target_is_pharmacy:
                            crossings.append(f"{f}: {field_type} (CLINIC -> PHARMACY)")
                        elif this_is_pharmacy and target_is_clinic:
                            crossings.append(f"{f}: {field_type} (PHARMACY -> CLINIC)")
                        break

for c in crossings:
    print(c)

