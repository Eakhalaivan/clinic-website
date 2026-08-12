import re

with open("src/App.jsx", "r") as f:
    content = f.read()

# Make sure lazy and Suspense are imported from react
if "lazy" not in content or "Suspense" not in content:
    content = content.replace("import { useEffect } from 'react';", "import { useEffect, lazy, Suspense } from 'react';")

# Also import PageLoadingSkeleton
if "PageLoadingSkeleton" not in content:
    content = content.replace("import { Toaster } from 'react-hot-toast';", "import { Toaster } from 'react-hot-toast';\nimport PageLoadingSkeleton from './components/ui/PageLoadingSkeleton';")

# Find all imports like: import MyPage from './pages/path/MyPage';
# Replace with: const MyPage = lazy(() => import('./pages/path/MyPage'));
pattern = r"import\s+([A-Za-z0-9_]+)\s+from\s+(['\"])(./pages/.*?)(\2);"

def replacer(match):
    component_name = match.group(1)
    import_path = match.group(3)
    return f"const {component_name} = lazy(() => import('{import_path}'));"

content = re.sub(pattern, replacer, content)

# Wrap <Routes> with <Suspense fallback={<PageLoadingSkeleton />}>
if "<Suspense" not in content:
    content = content.replace("<Routes>", "<Suspense fallback={<PageLoadingSkeleton />}>\n            <Routes>")
    content = content.replace("</Routes>", "</Routes>\n            </Suspense>")

with open("src/App.jsx", "w") as f:
    f.write(content)
