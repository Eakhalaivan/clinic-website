import re

with open('backend/src/main/java/com/healthcare/clinic/laboratory/controller/LabController.java', 'r') as f:
    content = f.read()

content = content.replace('import com.healthcare.clinic.doctor.entity.Doctor;', 'import com.healthcare.clinic.pharmacy.entity.Doctor;')
content = content.replace('import com.healthcare.clinic.doctor.repository.DoctorRepository;', 'import com.healthcare.clinic.pharmacy.repository.DoctorRepository;')

if 'import com.healthcare.clinic.pharmacy.repository.DoctorRepository;' not in content:
    print("Warning: could not find the import to replace!")
    
with open('backend/src/main/java/com/healthcare/clinic/laboratory/controller/LabController.java', 'w') as f:
    f.write(content)
