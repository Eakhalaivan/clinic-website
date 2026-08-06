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
    "com.healthcare.clinic.inventory.sales.model",
    "com.healthcare.clinic.inventory.pharmacy.repository",
    "com.healthcare.clinic.inventory.sales.repository",
    "com.healthcare.clinic.inventory.repository"
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
        if file.endswith("Service.java"):
            java_files.append(os.path.join(root, file))

cross_services = []
for f in java_files:
    with open(f, 'r') as fp:
        content = fp.read()
        
        if '@Transactional' not in content:
            continue
            
        imports = []
        for line in content.split('\n'):
            imp_match = re.search(r'import\s+([a-zA-Z0-9_\.]+);', line)
            if imp_match: imports.append(imp_match.group(1))
            
        has_clinic_repo = False
        has_pharmacy_repo = False
        
        for imp in imports:
            if imp.endswith("Repository"):
                # find package
                pkg = ".".join(imp.split(".")[:-1])
                if is_clinic(pkg): has_clinic_repo = True
                if is_pharmacy(pkg): has_pharmacy_repo = True
                
        if has_clinic_repo and has_pharmacy_repo:
            cross_services.append(f)

for c in cross_services:
    print(c)

