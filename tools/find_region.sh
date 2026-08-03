#!/bin/bash
# find_region.sh — finds which Supabase pooler region accepts a psql connection.
# Usage: SUPABASE_PROJECT_ID=<id> PGPASSWORD=<password> ./find_region.sh

set -euo pipefail

# Require both env vars to be set — never hardcode credentials in scripts.
if [[ -z "${SUPABASE_PROJECT_ID:-}" ]]; then
  echo "ERROR: SUPABASE_PROJECT_ID env var is not set." >&2
  exit 1
fi

if [[ -z "${PGPASSWORD:-}" ]]; then
  echo "ERROR: PGPASSWORD env var is not set." >&2
  exit 1
fi

USER="postgres.${SUPABASE_PROJECT_ID}"

REGIONS=(
  "us-east-1"
  "us-west-1"
  "us-west-2"
  "eu-west-1"
  "eu-west-2"
  "eu-west-3"
  "eu-central-1"
  "ap-southeast-1"
  "ap-northeast-1"
  "ap-northeast-2"
  "ap-southeast-2"
  "ap-south-1"
  "sa-east-1"
  "ca-central-1"
)

for REGION in "${REGIONS[@]}"; do
  HOST="aws-0-${REGION}.pooler.supabase.com"
  echo "Testing $REGION..."
  # Use a timeout of 5 seconds for the connection attempt
  if psql -h "$HOST" -p 6543 -U "$USER" -d postgres -c "SELECT 1;" > /dev/null 2>&1; then
    echo "SUCCESS: $REGION"
    exit 0
  fi
done

echo "FAILED: no region responded"
exit 1
