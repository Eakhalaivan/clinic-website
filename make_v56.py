import re

with open("missing_tables.sql", "r") as f:
    sql = f.read()

# Replace CREATE TABLE with CREATE TABLE IF NOT EXISTS
sql = sql.replace("CREATE TABLE public.", "CREATE TABLE IF NOT EXISTS public.")
# Remove ALTER TABLE OWNER
sql = re.sub(r'ALTER TABLE public\..* OWNER TO .*;\n', '', sql)

out = "-- V56: Reconcile Schema Drift\n"
out += "-- Drops orphaned pharmacy tables and creates tables that were missing from the live DB.\n\n"

out += "DROP TABLE IF EXISTS medicine_batches CASCADE;\n"
out += "DROP TABLE IF EXISTS prescription_dispensed_items CASCADE;\n"
out += "DROP TABLE IF EXISTS prescriptions_dispensed CASCADE;\n\n"

out += sql

with open("backend/src/main/resources/db/migration/clinic/V56__reconcile_schema_drift.sql", "w") as f:
    f.write(out)
