with open("flyway_cols.txt") as f:
    flyway = set([line.strip() for line in f if line.strip()])
with open("clinic_cols.txt") as f:
    clinic = set([line.strip() for line in f if line.strip()])

print("--- IN FLYWAY, NOT IN CLINIC ---")
for x in sorted(flyway - clinic): print(x)
print("\n--- IN CLINIC, NOT IN FLYWAY ---")
for x in sorted(clinic - flyway): print(x)
