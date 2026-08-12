import re

with open('frontend/src/pages/patient/PatientDashboard.jsx', 'r') as f:
    content = f.read()

# Replace TIMELINE_EVENTS with empty array
content = re.sub(r'const TIMELINE_EVENTS = \[.*?\];', 'const TIMELINE_EVENTS = [];', content, flags=re.DOTALL)

# Remove non-core quick action buttons
# Order Medicine
content = re.sub(r'<button onClick=\{.*?/patient/order-medicine.*?</button>', '', content, flags=re.DOTALL)
# Home Visit
content = re.sub(r'<button onClick=\{.*?/patient/home-visit.*?</button>', '', content, flags=re.DOTALL)
# Upload Vitals
content = re.sub(r'<button onClick=\{.*?/patient/upload-vitals.*?</button>', '', content, flags=re.DOTALL)
# Tele Consult
content = re.sub(r'<button onClick=\{.*?/patient/tele-consult.*?</button>', '', content, flags=re.DOTALL)
# Insurance
content = re.sub(r'<button onClick=\{.*?/patient/insurance.*?</button>', '', content, flags=re.DOTALL)
# Orders
content = re.sub(r'<button onClick=\{.*?/patient/orders.*?</button>', '', content, flags=re.DOTALL)
# Download Records
content = re.sub(r'<button onClick=\{.*?/patient/download-records.*?</button>', '', content, flags=re.DOTALL)

# AI Assistant
content = re.sub(r'\{/\* AI Assistant Button \(New Design\) \*/\}.*?</button>', '', content, flags=re.DOTALL)

with open('frontend/src/pages/patient/PatientDashboard.jsx', 'w') as f:
    f.write(content)
