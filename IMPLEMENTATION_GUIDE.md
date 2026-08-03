# Clinical Decision Support (CDS), Care Pathways, and Smart Order Sets — Implementation Guide

## 1. System Architecture Overview

The Clinical Decision Support architecture strictly separates **synchronous blocking safety gates** from **asynchronous non-blocking rules engines**:

```
                              [ Doctor Submits Order / Prescription ]
                                                │
                                                ▼
                     ┌─────────────────────────────────────────────────────┐
                     │ 1. Synchronous Blocking Safety Check                │
                     │    (CdsSafetyCheckService.java)                     │
                     └──────────────────────────┬──────────────────────────┘
                                                │
                       ┌────────────────────────┴────────────────────────┐
                       │                                                 │
          [ CRITICAL Safety Violation ]                       [ Clean / WARNING ]
                       │                                                 │
                       ▼                                                 ▼
        - Save CdsAlert (REQUIRES_NEW)                      - Persist Prescription Entity
        - Throw CdsCriticalSafetyException                               │
        - Rollback Transaction                                           ▼
        - Return HTTP 422 Unprocessable Entity            ┌──────────────────────────────┐
                                                          │ 2. Publish Domain Event      │
                                                          │    (PrescriptionCreatedEvent) │
                                                          └──────────────┬───────────────┘
                                                                         │
                                                                         ▼ (Async / Fire & Forget)
                                                          ┌──────────────────────────────┐
                                                          │ 3. Async CDS Rules Listener  │
                                                          │    (CdsEventListener.java)   │
                                                          └──────────────┬───────────────┘
                                                                         │
                                                                         ▼
                                                          - Evaluate Active CdsRules
                                                          - Persist Advisory CdsAlerts
                                                          - Send In-App Notifications
```

### Why They Are Separated
1. **Transaction Isolation**: Spring Application Events running asynchronously outside the caller's transaction cannot reject an HTTP request or rollback an active JPA transaction once published.
2. **Patient Safety Guarantee**: Critical drug-allergy or drug-disease contraindications MUST block the prescription write synchronously before it touches the database.
3. **Flexibility**: Admin-configured advisory rules (e.g., suggesting an order set or offering clinical guidance) execute fire-and-forget without impacting user-perceived latency.

---

## 2. Pre-Work Inspection Findings

| Component / Artifact | Project Location | Architecture Decision |
|---|---|---|
| **`ApiResponse<T>` Wrapper** | Scoped at `com.healthcare.clinic.inventory.pharmacy.dto.ApiResponse` | Created module-scoped envelope `com.healthcare.clinic.clinicaldecision.dto.ApiResponse` to avoid cross-boundary leaks. |
| **Shared UI Components** | `frontend/src/components/pharmacy/ui/` (`DataTable.jsx`, `AppModal.jsx`, `KPICard.jsx`) | Imported from real path `components/pharmacy/ui/`. Promoting to a shared `components/ui/` design library is flagged as a follow-up. |
| **Frontend Test Environment** | `vitest` + `@testing-library/react` (15 passing tests) | Included Vitest unit test suite `CdsAlertBanner.test.jsx`. |
| **Medication Entity Linkage** | Free-text string (`PrescriptionItem.medicationName`) | Designed best-effort case-insensitive name matching with explicit warning comments. |

---

## 3. Database Migration Notes

Flyway migration `V29__init_clinical_decision_schema.sql` contains additive schema changes:

- **Patient Profiles Extension**:
  `ALTER TABLE patient_profiles ADD COLUMN IF NOT EXISTS allergies JSONB DEFAULT '[]'::jsonb;`
  `ALTER TABLE patient_profiles ADD COLUMN IF NOT EXISTS chronic_conditions JSONB DEFAULT '[]'::jsonb;`
- **New Tables Created**:
  - `cds_rules`: Decision engine rules and triggers.
  - `cds_alerts`: Audit history of generated critical/warning alerts and override reasons.
  - `care_pathway_templates`: Master care pathway protocols.
  - `patient_care_pathways`: Active assigned patient pathways.
  - `care_pathway_steps`: Step execution tracking.
  - `order_set_templates`: Diagnosis-guided ICD-10 order set bundles.

---

## 4. Running Backend Unit Tests

Run the JUnit 5 test suite for the CDS safety check engine:
```bash
mvn test -Dtest=CdsSafetyCheckServiceTest,JwtUtilsTest
```

### Extending Safety Rules
To add new drug-disease contraindications or cross-reactivity rules in tests or production:
1. Open `CdsSafetyCheckService.java`.
2. Add entries to `CONTRAINDICATED_DISEASES` or `ALLERGY_CROSS_REACTIVITY`.
3. Add corresponding test cases in `CdsSafetyCheckServiceTest.java` verifying both clean execution and `CdsCriticalSafetyException` exception throwing.

---

## 5. Frontend Integration Steps

1. **Routing**: New pages registered in `App.jsx` under `/doctor/`:
   - `/doctor/cds` → `ClinicalDecisionSupport.jsx` (Doctor Dashboard Widget)
   - `/doctor/care-pathways/builder` → `CarePathwayBuilder.jsx` (Admin Pathway Protocol Builder)
   - `/doctor/patients/:patientId/care-pathways` → `PatientCarePathwayView.jsx` (Embedded Patient Chart Timeline)
2. **State Management**: Zustand store `useClinicalDecisionStore.js` manages async REST API calls to `/api/cds/*`, `/api/care-pathways/*`, and `/api/order-sets/*`.
3. **Modal Component**: `OrderSetPicker.jsx` can be rendered inline during prescription composition to trigger diagnosis-guided order set insertion.

---

## 6. Known Limitations & Recommended Follow-ups

> [!WARNING]
> The following items represent deliberate design tradeoffs based on current repo capabilities. They must NOT be relied upon for live unmonitored medical practice without completing the listed follow-ups:

1. **Free-Text Medication Name Matching**:
   - *Current Behavior*: `PrescriptionItem.medicationName` is a free-text String. The CDS safety check uses case-insensitive substring matching (`upperMed.contains(...)`).
   - *Follow-up*: Refactor prescription items to reference standard `Medicine.id` or RxNorm / SNOMED CT identifiers.
2. **Example-Only Contraindication Mapping**:
   - *Current Behavior*: `CdsSafetyCheckService` contains demonstrative hardcoded contraindication mappings (e.g. NSAIDs in CKD).
   - *Follow-up*: Integrate a licensed clinical drug interaction database API (e.g., First Databank, Lexicomp, or openFDA RxNorm).
3. **Human-Notified Care Pathway Step Initiation**:
   - *Current Behavior*: Initiating an `APPOINTMENT` or `LAB_ORDER` step in a care pathway marks the step `IN_PROGRESS` and dispatches an in-app task notification to the reception/lab team rather than auto-booking.
   - *Follow-up*: Build automated slot matching and catalog mapping for direct auto-booking.
4. **UI Component Promotion**:
   - *Current Behavior*: Shared UI components (`AppModal`, `DataTable`) are imported from `components/pharmacy/ui/`.
   - *Follow-up*: Refactor component hierarchy into a top-level shared design system at `components/ui/`.
