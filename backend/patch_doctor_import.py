import re

with open('src/main/java/com/healthcare/clinic/laboratory/controller/LabController.java', 'r') as f:
    content = f.read()

content = content.replace('import com.healthcare.clinic.doctor.entity.Doctor;', 'import com.healthcare.clinic.pharmacy.entity.Doctor;')
content = content.replace('import com.healthcare.clinic.doctor.repository.DoctorRepository;', 'import com.healthcare.clinic.pharmacy.repository.DoctorRepository;')

with open('src/main/java/com/healthcare/clinic/laboratory/controller/LabController.java', 'w') as f:
    f.write(content)
