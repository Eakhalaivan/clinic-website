# Core Workflow Route Map

This document maps the core clinical UI routes to their backend controllers, roles, and underlying database entities.

## 1. Patient Portal
- **Dashboard** (`/patient/dashboard`) -> `PatientDashboardController` (`ROLE_PATIENT`) -> `Patient`, `Appointment`
- **Book Appointment** (`/patient/book`) -> `PatientAppointmentController` (`ROLE_PATIENT`) -> `Appointment`, `DoctorSchedule`
- **Appointments** (`/patient/appointments`) -> `PatientAppointmentController` (`ROLE_PATIENT`) -> `Appointment`
- **Medical Records** (`/patient/records`) -> `PatientRecordController` (`ROLE_PATIENT`) -> `ClinicalNote`, `Vitals`
- **Prescriptions** (`/patient/prescriptions`) -> `PatientPrescriptionController` (`ROLE_PATIENT`) -> `Prescription`
- **Lab Reports** (`/patient/lab-reports`) -> `PatientLabController` (`ROLE_PATIENT`) -> `LabReport`
- **Radiology** (`/patient/radiology-reports`) -> `PatientRadiologyController` (`ROLE_PATIENT`) -> `RadiologyReport`
- **Billing** (`/patient/billing`) -> `PatientInvoiceController` (`ROLE_PATIENT`) -> `Invoice`, `Payment`

## 2. Reception Portal
- **Dashboard** (`/reception/dashboard`) -> `ReceptionDashboardController` (`ROLE_RECEPTION`)
- **Register Patient** (`/reception/register`) -> `PatientController` (`ROLE_RECEPTION`) -> `Patient`
- **Queue Management** (`/reception/queue`) -> `QueueController` (`ROLE_RECEPTION`) -> `QueueToken`
- **Tokens** (`/reception/tokens`) -> `QueueController` (`ROLE_RECEPTION`) -> `QueueToken`

## 3. Doctor Portal
- **Dashboard** (`/doctor/dashboard`) -> `DoctorDashboardController` (`ROLE_DOCTOR`)
- **Queue** (`/doctor/queue`) -> `ConsultationQueueController` (`ROLE_DOCTOR`) -> `QueueToken`, `Appointment`
- **Patients** (`/doctor/patients`) -> `DoctorPatientController` (`ROLE_DOCTOR`) -> `Patient`
- **Clinical Notes** (`/doctor/patients/:id/notes`) -> `ClinicalNoteController` (`ROLE_DOCTOR`) -> `ClinicalNote`
- **Prescribe** (`/doctor/patients/:id/prescriptions/new`) -> `PrescriptionController` (`ROLE_DOCTOR`) -> `Prescription`
- **Lab Request** (`/doctor/lab-request`) -> `LabOrderController` (`ROLE_DOCTOR`) -> `LabOrder`

## 4. Nurse Portal
- **Dashboard** (`/nurse/dashboard`) -> `NurseDashboardController` (`ROLE_NURSE`)
- **Vitals** (`/nurse/vitals`) -> `VitalsController` (`ROLE_NURSE`) -> `Vitals`
- **Medication** (`/nurse/medication`) -> `MedicationController` (`ROLE_NURSE`) -> `MedicationAdministration`

## 5. Pharmacy Portal
- **Dashboard** (`/pharmacy/dashboard`) -> `PharmacyDashboardController` (`ROLE_PHARMACIST`, `ROLE_PHARMACY_STAFF`)
- **Dispensing** (`/pharmacy/dispense-worklists`) -> `PharmacyDispensingController` (`ROLE_PHARMACIST`) -> `Prescription`, `InventoryMovement`
- **Inventory** (`/pharmacy/medicine-stock`) -> `PharmacyInventoryController` (`ROLE_PHARMACIST`) -> `StockBatch`

## 6. Lab & Radiology Portals
- **Lab Dashboard** (`/lab/dashboard`) -> `LabDashboardController` (`ROLE_LAB_TECH`) -> `LabOrder`
- **Radiology Dashboard** (`/radiologist/dashboard`) -> `RadiologyDashboardController` (`ROLE_RADIOLOGIST`) -> `RadiologyOrder`

## 7. Finance/Billing Portal
- **Invoices** (`/finance/invoices`) -> `InvoiceController` (`ROLE_FINANCE`) -> `Invoice`
- **Payments** (`/finance/payments`) -> `PaymentController` (`ROLE_FINANCE`) -> `Payment`
