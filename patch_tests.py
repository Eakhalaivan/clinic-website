import sys

filepath = 'backend/src/test/java/com/healthcare/clinic/doctor/service/PrescriptionServiceTest.java'
with open(filepath, 'r') as f:
    content = f.read()

content = content.replace("import com.healthcare.clinic.inventory.entity.PharmacyPrescriptionRecord;", "import com.healthcare.clinic.pharmacy.entity.PharmacyPrescriptionRecord;")
content = content.replace("sendPrescription(100L)", "sendPrescription(100L, null)")
content = content.replace("com.healthcare.clinic.inventory.entity.PharmacyPrescriptionRecord", "com.healthcare.clinic.pharmacy.entity.PharmacyPrescriptionRecord")

with open(filepath, 'w') as f:
    f.write(content)
