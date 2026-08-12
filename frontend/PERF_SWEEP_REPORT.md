# Part B: Performance Sweep Findings

## 1. Client-side Pagination (Should be Server-side)
- src/pages/doctor/PatientList.jsx
- src/pages/pharmacy/ConsolidatedBills.jsx
- src/pages/pharmacy/DirectMedicineReturns.jsx
- src/pages/pharmacy/DirectPharmacySales.jsx
- src/pages/pharmacy/MedicineCreditBills.jsx
- src/pages/pharmacy/MedicineCreditReturns.jsx
- src/pages/pharmacy/MedicineMaster.jsx
- src/pages/pharmacy/MedicineReturns.jsx
- src/pages/pharmacy/MedicineStock.jsx
- src/pages/pharmacy/Patients.jsx
- src/pages/pharmacy/PendingIndentPrescriptions.jsx
- src/pages/pharmacy/PendingPharmacyReplacement.jsx
- src/pages/pharmacy/PendingPrescriptions.jsx
- src/pages/pharmacy/PendingReplacementReturns.jsx
- src/pages/pharmacy/PharmacyAdvances.jsx
- src/pages/pharmacy/PharmacyClearance.jsx
- src/pages/pharmacy/ReturnWorklists.jsx

## 2. Missing SSE Cleanups
None found.

## 3. Data Fetching Bypassing React Query
- src/components/GlobalSearchBar.jsx
- src/pages/common/TeleconsultationRoom.jsx
- src/pages/nurse/MedicationAdministration.jsx
- src/pages/patient/HealthTimeline.jsx
- src/pages/patient/Insurance.jsx
- src/pages/patient/Orders.jsx
- src/pages/patient/RadiologyReports.jsx
- src/pages/reception/TokenGeneration.jsx
