import os
import re

directory = '/Users/eakhalaivan/Downloads/clinic-website/backend/src/main/java/com/healthcare/clinic/inventory/entity/'

for filename in os.listdir(directory):
    if filename.endswith(".java"):
        filepath = os.path.join(directory, filename)
        with open(filepath, 'r') as file:
            content = file.read()
        
        # Add (fetch = FetchType.LAZY) if not present
        content = re.sub(r'@ManyToOne(?!\()', '@ManyToOne(fetch = FetchType.LAZY)', content)
        content = re.sub(r'@ManyToOne\(\s*\)', '@ManyToOne(fetch = FetchType.LAZY)', content)
        content = re.sub(r'@ManyToOne\((?!.*fetch)', '@ManyToOne(fetch = FetchType.LAZY, ', content)
        
        content = re.sub(r'@OneToMany(?!\()', '@OneToMany(fetch = FetchType.LAZY)', content)
        content = re.sub(r'@OneToMany\(\s*\)', '@OneToMany(fetch = FetchType.LAZY)', content)
        content = re.sub(r'@OneToMany\((?!.*fetch)', '@OneToMany(fetch = FetchType.LAZY, ', content)
        
        # Cleanup extra commas like (fetch = FetchType.LAZY, ) -> (fetch = FetchType.LAZY)
        content = re.sub(r'fetch = FetchType\.LAZY,\s*\)', 'fetch = FetchType.LAZY)', content)
        
        with open(filepath, 'w') as file:
            file.write(content)

