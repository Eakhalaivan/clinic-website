#!/bin/bash
find src/main/java -name "*.java" | while read -r file; do
    is_clinic=0
    is_pharmacy=0
    
    if [[ "$file" == *"src/main/java/com/healthcare/clinic/inventory/"* ]]; then
        is_pharmacy=1
    else
        is_clinic=1
    fi
    
    if [ $is_clinic -eq 1 ]; then
        grep -nE "import com\.healthcare\.clinic\.inventory\.(entity|pharmacy\.entity|sales\.model)\." "$file" > /dev/null
        if [ $? -eq 0 ]; then
            echo "CLINIC package file importing PHARMACY package:"
            echo "$file"
            grep -nE "import com\.healthcare\.clinic\.inventory\.(entity|pharmacy\.entity|sales\.model)\." "$file"
            echo "---"
        fi
    fi
    
    if [ $is_pharmacy -eq 1 ]; then
        grep -nE "import com\.healthcare\.clinic\.(identity|patient|doctor|nursing|reception|appointment|medicalrecord|laboratory|billing|insurance|hr|finance|branch|superadmin|marketing|ecommerce|support|vendor|ambulance|radiology|analytics|notification|clinicaldecision|backoffice\.inventory)\." "$file" > /dev/null
        if [ $? -eq 0 ]; then
            echo "PHARMACY package file importing CLINIC package:"
            echo "$file"
            grep -nE "import com\.healthcare\.clinic\.(identity|patient|doctor|nursing|reception|appointment|medicalrecord|laboratory|billing|insurance|hr|finance|branch|superadmin|marketing|ecommerce|support|vendor|ambulance|radiology|analytics|notification|clinicaldecision|backoffice\.inventory)\." "$file"
            echo "---"
        fi
    fi
done
