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
        parts = file.split('/')
        pkg = parts[-3]
        cls = parts[-1].replace('.java', '')
        entity_name = f"{pkg.capitalize()}{cls}"
        
        with open(file, 'r') as f:
            content = f.read()
            
        content = re.sub(r'@Entity\s*\n', f'@Entity(name="{entity_name}")\n', content)
        content = re.sub(r'@Entity\(\s*\)\s*\n', f'@Entity(name="{entity_name}")\n', content)
        
        with open(file, 'w') as f:
            f.write(content)
        print(f"Fixed {file}")
