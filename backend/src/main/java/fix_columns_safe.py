import re
import os

files = [
    "com/healthcare/clinic/ai/entity/AiChatMessage.java",
    "com/healthcare/clinic/patient/entity/AiChatMessage.java",
    "com/healthcare/clinic/ai/entity/AiChatSession.java",
    "com/healthcare/clinic/patient/entity/AiChatSession.java",
    "com/healthcare/clinic/inpatient/entity/Bed.java",
    "com/healthcare/clinic/nursing/entity/Bed.java",
    "com/healthcare/clinic/doctor/entity/ClinicalReferral.java",
    "com/healthcare/clinic/emr/entity/ClinicalReferral.java",
    "com/healthcare/clinic/homevisit/entity/HomeVisitRequest.java",
    "com/healthcare/clinic/patient/entity/HomeVisitRequest.java",
    "com/healthcare/clinic/integration/entity/IntegrationConfig.java",
    "com/healthcare/clinic/superadmin/entity/IntegrationConfig.java",
    "com/healthcare/clinic/inpatient/entity/Ward.java",
    "com/healthcare/clinic/nursing/entity/Ward.java"
]

for file in files:
    if os.path.exists(file):
        with open(file, 'r') as f:
            lines = f.readlines()
            
        new_lines = []
        for i in range(len(lines)):
            line = lines[i]
            # check for createdAt
            if re.search(r'private\s+[A-Za-z<>]+\s+createdAt;', line):
                # check if previous line has @Column
                if i > 0 and '@Column' not in lines[i-1]:
                    new_lines.append('    @Column(name="created_at")\n')
            # check for updatedAt
            if re.search(r'private\s+[A-Za-z<>]+\s+updatedAt;', line):
                # check if previous line has @Column
                if i > 0 and '@Column' not in lines[i-1]:
                    new_lines.append('    @Column(name="updated_at")\n')
                    
            # check for sentAt
            if re.search(r'private\s+[A-Za-z<>]+\s+sentAt;', line):
                # check if previous line has @Column
                if i > 0 and '@Column' not in lines[i-1]:
                    new_lines.append('    @Column(name="sent_at")\n')
                    
            new_lines.append(line)
            
        with open(file, 'w') as f:
            f.writelines(new_lines)
        print(f"Processed {file}")
