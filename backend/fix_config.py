import re
import os

filepath = "src/main/java/com/healthcare/clinic/config/ClinicDatabaseConfig.java"
packages = [
    '"com.healthcare.clinic.ai"', '"com.healthcare.clinic.ambulance"', '"com.healthcare.clinic.analytics"',
    '"com.healthcare.clinic.appointment"', '"com.healthcare.clinic.audit"', '"com.healthcare.clinic.backoffice"',
    '"com.healthcare.clinic.billing"', '"com.healthcare.clinic.branch"', '"com.healthcare.clinic.clinicaldecision"',
    '"com.healthcare.clinic.common"', '"com.healthcare.clinic.doctor"', '"com.healthcare.clinic.document"',
    '"com.healthcare.clinic.ecommerce"', '"com.healthcare.clinic.emergency"', '"com.healthcare.clinic.emr"',
    '"com.healthcare.clinic.engagement"', '"com.healthcare.clinic.exception"', '"com.healthcare.clinic.fhir"',
    '"com.healthcare.clinic.finance"', '"com.healthcare.clinic.health"', '"com.healthcare.clinic.homevisit"',
    '"com.healthcare.clinic.hr"', '"com.healthcare.clinic.identity"', '"com.healthcare.clinic.inpatient"',
    '"com.healthcare.clinic.insurance"', '"com.healthcare.clinic.integration"', '"com.healthcare.clinic.inventory"',
    '"com.healthcare.clinic.laboratory"', '"com.healthcare.clinic.marketing"', '"com.healthcare.clinic.medicalrecord"',
    '"com.healthcare.clinic.notification"', '"com.healthcare.clinic.nursing"', '"com.healthcare.clinic.patient"',
    '"com.healthcare.clinic.radiology"', '"com.healthcare.clinic.reception"', '"com.healthcare.clinic.security"',
    '"com.healthcare.clinic.subscription"', '"com.healthcare.clinic.superadmin"', '"com.healthcare.clinic.support"',
    '"com.healthcare.clinic.surgery"', '"com.healthcare.clinic.telemedicine"', '"com.healthcare.clinic.tenant"',
    '"com.healthcare.clinic.vendor"'
]
pkg_str = ",\n                ".join(packages)
new_code = f"em.setPackagesToScan(\n                {pkg_str}\n        );"

with open(filepath, 'r') as f:
    content = f.read()

# Replace the em.setPackagesToScan block
content = re.sub(r'em\.setPackagesToScan\([^;]+\);', new_code, content)

with open(filepath, 'w') as f:
    f.write(content)

