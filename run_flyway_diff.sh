#!/bin/bash
dropdb -h localhost clinic_flyway || true
createdb -h localhost clinic_flyway

# Run up to V55 ONLY
ls -1 backend/src/main/resources/db/migration/clinic/V*.sql | awk -F/ '{print $NF, $0}' | sed -E 's/V([0-9]+)__.*/\1 &/' | sort -n | awk '$1 <= 55 {print $3}' | while read file; do
    echo "Running $file"
    psql -h localhost -d clinic_flyway -f "$file" > /dev/null
done

# Diff again
psql -h localhost -d clinic -t -c "SELECT table_name || '.' || column_name || '.' || data_type FROM information_schema.columns WHERE table_schema='public' AND table_name NOT LIKE 'pharmacy_%' ORDER BY table_name, column_name;" | sed -e 's/^[ \t]*//' > clinic_cols.txt
psql -h localhost -d clinic_flyway -t -c "SELECT table_name || '.' || column_name || '.' || data_type FROM information_schema.columns WHERE table_schema='public' AND table_name NOT LIKE 'pharmacy_%' ORDER BY table_name, column_name;" | sed -e 's/^[ \t]*//' > flyway_cols.txt

python3 compare.py
