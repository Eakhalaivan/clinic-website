import re

with open('backend/src/main/java/com/healthcare/clinic/laboratory/controller/LabController.java', 'r') as f:
    content = f.read()

# 1. Remove the custom constructor I added:
constructor_regex = r'public LabController\(.*?\)\s*\{.*?\}'
content = re.sub(constructor_regex, '', content, flags=re.DOTALL)

# 2. Replace Doctor/DoctorRepository imports with DoctorProfile/DoctorProfileRepository
content = content.replace('import com.healthcare.clinic.pharmacy.entity.Doctor;', 'import com.healthcare.clinic.doctor.entity.DoctorProfile;')
content = content.replace('import com.healthcare.clinic.pharmacy.repository.DoctorRepository;', 'import com.healthcare.clinic.doctor.repository.DoctorProfileRepository;')

# 3. Replace DoctorRepository field
content = content.replace('private final DoctorRepository doctorRepository;', 'private final DoctorProfileRepository doctorProfileRepository;')

# 4. Fix createRequest usages of Doctor
content = content.replace('Doctor doctor = null;', 'DoctorProfile doctor = null;')
content = content.replace('doctor = doctorRepository.findByUserId(userId).orElse(null);', 'doctor = doctorProfileRepository.findByUserId(userId).orElse(null);')


with open('backend/src/main/java/com/healthcare/clinic/laboratory/controller/LabController.java', 'w') as f:
    f.write(content)
