import re

with open('frontend/src/config/portalConfig.js', 'r') as f:
    config = f.read()

# I want to remove specific blocks from PORTAL_CONFIGS.
# They are delimited by comments like "// ── 1. Super Admin"
# Let's just remove everything between "// ── 11. HR" and "// ── Accountant" 

pattern = re.compile(r'// ── 11\. HR ──.*?// ── Accountant \(mapped to finance\) ──', re.DOTALL)
new_config = pattern.sub('// ── Accountant (mapped to finance) ──', config)

# Also remove Super Admin and Branch Admin (1 and 2)
pattern2 = re.compile(r'// ── 1\. Super Admin ──.*?// ── 3\. Doctor ──', re.DOTALL)
new_config = pattern2.sub('// ── 3. Doctor ──', new_config)

with open('frontend/src/config/portalConfig.js', 'w') as f:
    f.write(new_config)

with open('frontend/src/App.jsx', 'r') as f:
    app_jsx = f.read()

pattern_hr = re.compile(r'\{/\* ── HR Routes ──.*?(?=\{/\* ── Pharmacy Full Module ──)', re.DOTALL)
app_jsx = pattern_hr.sub('', app_jsx)

# We also need to remove SuperAdmin and BranchAdmin routes
pattern_admin = re.compile(r'\{/\* ── Branch Admin Routes ──.*?(?=\{/\* ── Legacy Admin Routes)', re.DOTALL)
app_jsx = pattern_admin.sub('', app_jsx)

with open('frontend/src/App.jsx', 'w') as f:
    f.write(app_jsx)

