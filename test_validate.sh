#!/bin/bash
sed -i '' 's/url: jdbc:postgresql:\/\/localhost:5432\/clinic/url: jdbc:postgresql:\/\/localhost:5432\/clinic_flyway/g' backend/src/main/resources/application.yml
sed -i '' 's/hbm2ddl.auto", "update"/hbm2ddl.auto", "validate"/g' backend/src/main/java/com/healthcare/clinic/config/ClinicDatabaseConfig.java
cd backend
mvn test -Dtest=HibernateValidationTest || echo "TEST FAILED"
cd ..
git checkout backend/src/main/resources/application.yml
git checkout backend/src/main/java/com/healthcare/clinic/config/ClinicDatabaseConfig.java
