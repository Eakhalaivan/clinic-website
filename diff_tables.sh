#!/bin/bash
psql -h localhost -d clinic -t -c "SELECT table_name || '.' || column_name || '.' || data_type FROM information_schema.columns WHERE table_schema='public' AND table_name NOT LIKE 'pharmacy_%' ORDER BY table_name, column_name;" | sed -e 's/^[ \t]*//' > clinic_cols.txt
psql -h localhost -d clinic_flyway -t -c "SELECT table_name || '.' || column_name || '.' || data_type FROM information_schema.columns WHERE table_schema='public' AND table_name NOT LIKE 'pharmacy_%' ORDER BY table_name, column_name;" | sed -e 's/^[ \t]*//' > flyway_cols.txt
