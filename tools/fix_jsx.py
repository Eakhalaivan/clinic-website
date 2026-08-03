import re

files = [
    "/Users/eakhalaivan/Downloads/clinic-website/frontend/src/pages/hr/Employees.jsx",
    "/Users/eakhalaivan/Downloads/clinic-website/frontend/src/pages/inventory/WarehousesList.jsx",
    "/Users/eakhalaivan/Downloads/clinic-website/frontend/src/pages/finance/InvoicesList.jsx"
]

for filepath in files:
    with open(filepath, 'r') as f:
        content = f.read()
    
    # Check if already wrapped
    if "return (" in content and "<>" not in content[content.find("return (") : content.find("return (")+20]:
        content = content.replace("return (", "return (\n    <>")
        content = content.replace("  );\n};", "    </>\n  );\n};")
        
        with open(filepath, 'w') as f:
            f.write(content)
        print(f"Fixed {filepath}")

