#!/bin/bash
echo "Starting Clinic App..."
if [ -f .env ]; then
  set -a
  source .env
  set +a
fi
cd backend
mvn spring-boot:run -Dspring-boot.run.profiles=dev -Dmaven.test.skip=true
