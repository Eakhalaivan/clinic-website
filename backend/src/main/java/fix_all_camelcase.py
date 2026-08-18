import re
import os

files = [
    "com/healthcare/clinic/inpatient/entity/Bed.java",
    "com/healthcare/clinic/nursing/entity/Bed.java",
    "com/healthcare/clinic/doctor/entity/ClinicalReferral.java",
    "com/healthcare/clinic/emr/entity/ClinicalReferral.java",
    "com/healthcare/clinic/inpatient/entity/Ward.java",
    "com/healthcare/clinic/nursing/entity/Ward.java"
]

def camel_to_snake(name):
    s1 = re.sub('(.)([A-Z][a-z]+)', r'\1_\2', name)
    return re.sub('([a-z0-9])([A-Z])', r'\1_\2', s1).lower()

for file in files:
    if os.path.exists(file):
        with open(file, 'r') as f:
            lines = f.readlines()
            
        new_lines = []
        for i in range(len(lines)):
            line = lines[i]
            
            # Match private fields that are camelCase
            match = re.search(r'^\s*(?:@.+?\s+)*private\s+[A-Za-z<>]+\s+([a-z]+[A-Z][A-Za-z0-9]*)\s*[=;]', line)
            
            if match:
                field_name = match.group(1)
                snake_name = camel_to_snake(field_name)
                
                # Check if the previous lines already have @Column(name="...")
                # To be safe, just look at the line itself (if it has @Column) or the line before
                has_column = False
                if '@Column' in line:
                    has_column = True
                if i > 0 and '@Column' in lines[i-1]:
                    has_column = True
                if i > 1 and '@Column' in lines[i-2]:
                    has_column = True
                    
                if not has_column:
                    # add @Column right before this line
                    indent = len(line) - len(line.lstrip())
                    new_lines.append(' ' * indent + f'@Column(name="{snake_name}")\n')
            
            new_lines.append(line)
            
        with open(file, 'w') as f:
            f.writelines(new_lines)
        print(f"Processed {file}")
