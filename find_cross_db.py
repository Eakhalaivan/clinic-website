import os
import re

clinic_packages = ["identity", "patient", "doctor", "nursing", "reception", "appointment", 
            "medicalrecord", "laboratory", "billing", "insurance", "hr", "finance", "branch", 
            "superadmin", "marketing", "ecommerce", "support", "vendor", "ambulance", 
            "radiology", "analytics", "notification", "clinicaldecision", 
            "backoffice.inventory"]
pharmacy_packages = ["inventory.entity", "inventory.pharmacy.entity", "inventory.sales.model"]

def is_clinic(pkg):
    for p in clinic_packages:
        if pkg.endswith("." + p) or ".%s." % p in pkg:
            return True
    return False

def is_pharmacy(pkg):
    for p in pharmacy_packages:
        if pkg.endswith("." + p) or ".%s." % p in pkg:
            return True
    return False

# Parse all java files
java_files = []
for root, dirs, files in os.walk("./backend/src/main/java"):
    for file in files:
        if file.endswith(".java"):
            java_files.append(os.path.join(root, file))

entities = {} # className -> package

# First pass: map classes to their packages
for f in java_files:
    with open(f, 'r') as fp:
        content = fp.read()
        pkg_match = re.search(r'package\s+([a-zA-Z0-9_\.]+);', content)
        if pkg_match:
            pkg = pkg_match.group(1)
            class_match = re.search(r'class\s+([a-zA-Z0-9_]+)', content)
            if class_match:
                cls = class_match.group(1)
                entities[cls] = pkg

# Second pass: find relationships
crossings = []
for f in java_files:
    with open(f, 'r') as fp:
        content = fp.read()
        pkg_match = re.search(r'package\s+([a-zA-Z0-9_\.]+);', content)
        if not pkg_match:
            continue
        pkg = pkg_match.group(1)
        
        # Is this file clinic or pharmacy?
        this_is_clinic = is_clinic(pkg)
        this_is_pharmacy = is_pharmacy(pkg)
        
        if not this_is_clinic and not this_is_pharmacy:
            continue
            
        imports = {}
        for line in content.split('\n'):
            imp_match = re.search(r'import\s+([a-zA-Z0-9_\.]+)\.([a-zA-Z0-9_]+);', line)
            if imp_match:
                imports[imp_match.group(2)] = imp_match.group(1)
                
        # Find fields with @ManyToOne, @OneToMany, @JoinColumn
        lines = content.split('\n')
        for i, line in enumerate(lines):
            if '@ManyToOne' in line or '@OneToMany' in line or '@JoinColumn' in line or '@OneToOne' in line:
                # look ahead for the field type
                for j in range(i+1, min(i+10, len(lines))):
                    field_match = re.search(r'private\s+(?:List<)?([a-zA-Z0-9_]+)(?:>)?\s+[a-zA-Z0-9_]+', lines[j])
                    if field_match:
                        field_type = field_match.group(1)
                        # Check where field_type is from
                        target_pkg = imports.get(field_type)
                        if not target_pkg:
                            target_pkg = pkg # same package
                        
                        target_is_clinic = is_clinic(target_pkg)
                        target_is_pharmacy = is_pharmacy(target_pkg)
                        
                        if this_is_clinic and target_is_pharmacy:
                            crossings.append(f"{f}: {field_type} (CLINIC -> PHARMACY)")
                        elif this_is_pharmacy and this_is_clinic:
                            crossings.append(f"{f}: {field_type} (PHARMACY -> CLINIC)")
                        break

for c in crossings:
    print(c)

