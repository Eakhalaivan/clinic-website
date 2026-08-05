#!/bin/bash

# Database Backup Script for Clinic App
# This script should be run as a cron job for daily backups.

DB_NAME="clinic_db"
DB_USER="postgres"
BACKUP_DIR="./backups"
TIMESTAMP=$(date +"%Y%m%d_%H%M%S")
BACKUP_FILE="$BACKUP_DIR/${DB_NAME}_backup_$TIMESTAMP.sql"

mkdir -p "$BACKUP_DIR"

echo "Starting backup of database $DB_NAME..."

# Ensure PGPASSWORD is set in the environment or use pg_hba.conf
pg_dump -U "$DB_USER" -d "$DB_NAME" -F c -f "$BACKUP_FILE"

if [ $? -eq 0 ]; then
  echo "Backup successful! File saved to $BACKUP_FILE"
  
  # Optional: Keep only last 7 days of backups
  find "$BACKUP_DIR" -type f -name "${DB_NAME}_backup_*.sql" -mtime +7 -exec rm {} \;
  echo "Cleaned up old backups."
else
  echo "Backup failed!"
  exit 1
fi
