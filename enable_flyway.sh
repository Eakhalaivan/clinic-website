#!/bin/bash
sed -i '' 's/enabled: false/enabled: true/g' backend/src/main/resources/application.yml
sed -i '' '/enabled: true/a\
    baseline-on-migrate: true\
    baseline-version: 55
' backend/src/main/resources/application.yml
