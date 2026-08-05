#!/bin/bash

# Database Restore Script for Clinic App

DB_NAME="clinic_db"
DB_USER="postgres"

if [ -z "$1" ]; then
  echo "Usage: ./restore_db.sh <path_to_backup_file>"
  exit 1
fi

BACKUP_FILE=$1

if [ ! -f "$BACKUP_FILE" ]; then
  echo "Error: Backup file not found at $BACKUP_FILE"
  exit 1
fi

echo "Starting restore of database $DB_NAME from $BACKUP_FILE..."

# Terminate existing connections and drop/create the database
psql -U "$DB_USER" -d postgres -c "SELECT pg_terminate_backend(pid) FROM pg_stat_activity WHERE datname = '$DB_NAME';"
psql -U "$DB_USER" -d postgres -c "DROP DATABASE IF EXISTS $DB_NAME;"
psql -U "$DB_USER" -d postgres -c "CREATE DATABASE $DB_NAME;"

# Restore the dump
pg_restore -U "$DB_USER" -d "$DB_NAME" -1 "$BACKUP_FILE"

if [ $? -eq 0 ]; then
  echo "Restore successful!"
else
  echo "Restore failed!"
  exit 1
fi
