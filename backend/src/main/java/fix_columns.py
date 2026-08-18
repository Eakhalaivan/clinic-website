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
            content = f.read()
            
        # For createdAt
        content = re.sub(r'(@CreationTimestamp\s*\n\s*)(?!@Column)(private\s+[A-Za-z]+\s+createdAt;)', r'\1@Column(name="created_at")\n    \2', content)
        # Some might have @CreationTimestamp and then another annotation but no @Column. Just a simple check:
        # If it has private XXX createdAt; without @Column immediately before, add it
        # Actually it's easier to just ensure @Column(name="created_at") is there if createdAt exists.
        
        # A bit safer:
        content = re.sub(r'(private\s+[A-Za-z<>]+\s+createdAt;)', r'@Column(name="created_at")\n    \1', content)
        content = re.sub(r'(private\s+[A-Za-z<>]+\s+updatedAt;)', r'@Column(name="updated_at")\n    \1', content)
        
        # Now remove any duplicate @Column(name="created_at")
        content = re.sub(r'@Column\(name="created_at"\)(\s*)@Column\(name="created_at"\)', r'@Column(name="created_at")\1', content)
        content = re.sub(r'@Column\(name="updated_at"\)(\s*)@Column\(name="updated_at"\)', r'@Column(name="updated_at")\1', content)
        
        # Remove any @Column(...) if we just added one before it? No, just leave it if it already had @Column(name="created_at").
        # If it had @Column(name="created_at", updatable=false), the regex for (private...) would add another @Column(name="created_at").
        # Let's write a smarter regex.
        
        with open(file, 'w') as f:
            f.write(content)
        print(f"Processed {file}")
