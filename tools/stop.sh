#!/bin/bash
echo "Stopping Clinic App..."
kill $(lsof -t -i:8080)
