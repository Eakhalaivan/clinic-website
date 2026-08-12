import re

with open('frontend/src/App.jsx', 'r') as f:
    content = f.read()

# Remove PH(...) routes
content = re.sub(r'^\s*<Route[^>]+element=\{PH\([^\)]+\)\}[^>]*/>\s*\n', '', content, flags=re.MULTILINE)

# Remove AIAssistantComingSoon routes and imports
content = re.sub(r'^\s*const AIAssistantComingSoon.*?\n', '', content, flags=re.MULTILINE)
content = re.sub(r'^\s*<Route path="ai-assistant"[^\n]*\n', '', content, flags=re.MULTILINE)

# Remove non-core module route blocks from App.jsx
# HR, Marketing, Ecommerce, Support, Vendor, Insurance, Ambulance, Branch Admin, Super Admin
# Wait, let's just do it manually with multi_replace_file_content.
with open('frontend/src/App.jsx', 'w') as f:
    f.write(content)

with open('frontend/src/config/portalConfig.js', 'r') as f:
    config = f.read()

# I will manually edit portalConfig.js since it's an array of objects.
