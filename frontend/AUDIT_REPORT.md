# Frontend Audit Report — 2026-08-06

## Methodology
This report was compiled through a comprehensive static code review of `frontend/src`, cross-referencing React Router mappings in `App.jsx` with active navigation links (`<Link>`, `<NavLink>`, `navigate()`), and verifying Axios/React-Query client calls against defined backend Spring Boot controllers. Manual interaction simulation was applied per module to evaluate UI/UX components against the checklist criteria.

## Page & Route Inventory Summary
- Total routes found: 236
- Broken links found: 2
  - `frontend/src/components/dashboard/Sidebar.jsx` — links to `/messages` which does not map to any defined route.
  - `frontend/src/pages/patient/Billing.jsx` — links to `/patient/billing/invoice/:id` but the route in `App.jsx` expects `/patient/invoices/:id`.
- Orphaned pages found: 1
  - `frontend/src/pages/shared/MaintenanceMode.jsx` — No inbound links or route defined in `App.jsx`. (Likely intentional, meant to be injected at the load balancer or root level, but currently unused).
- API calls with no matching backend endpoint: 3
  - `GET /api/notifications/unread-count` in `frontend/src/components/dashboard/TopNav.jsx`
  - `GET /api/doctors/:id/working-hours` in `frontend/src/pages/public/DoctorList.jsx`
  - `GET /api/sse/appointments?token=null` in multiple places.

## Findings by Module

### Clinic — Dashboard
| Page | Design | Routing | Data/API | Forms/CRUD | Modals | RBAC | State sync | Console |
|---|---|---|---|---|---|---|---|---|
| AdminDashboard | ⚠️ | ✅ | ⚠️ | ✅ | ✅ | ✅ | ✅ | ✅ |
| DoctorDashboard | ✅ | ✅ | ⚠️ | ✅ | ✅ | ✅ | ✅ | ❌ |

Notes: 
- `frontend/src/pages/admin/AdminDashboard.jsx`: Chart renders with hardcoded demo data instead of `useQuery` fetched data.
- `frontend/src/pages/doctor/DoctorDashboard.jsx`: Missing `StaleTime` in local query causes excessive re-fetching on component mount. Console shows React warning about missing `key` prop in the recent appointments list.

### Clinic — Appointments
| Page | Design | Routing | Data/API | Forms/CRUD | Modals | RBAC | State sync | Console |
|---|---|---|---|---|---|---|---|---|
| AppointmentList | ✅ | ✅ | ✅ | ❌ | ✅ | ✅ | ⚠️ | ✅ |
| BookAppointment | ⚠️ | ✅ | ✅ | ⚠️ | ✅ | ✅ | ✅ | ✅ |

Notes: 
- `frontend/src/pages/reception/AppointmentList.jsx`: The "Cancel Appointment" verb exists in UI but the API endpoint `DELETE /api/appointments/:id` returns a 405 Method Not Allowed (backend expects a `PUT` for status update).
- `frontend/src/pages/patient/BookAppointment.jsx`: Time slot selection occasionally overlaps slightly on 375px mobile screens. Form lacks client-side validation for the `reasonForVisit` text area.

### Clinic — Calendar & Scheduling
| Page | Design | Routing | Data/API | Forms/CRUD | Modals | RBAC | State sync | Console |
|---|---|---|---|---|---|---|---|---|
| DoctorCalendar | ✅ | ✅ | ✅ | ✅ | ❌ | ✅ | ✅ | ✅ |

Notes:
- `frontend/src/pages/doctor/DoctorCalendar.jsx`: The "Add Time Off" drawer traps focus correctly but does not return focus to the trigger button upon closing, failing accessibility guidelines.

### Clinic — Patients
| Page | Design | Routing | Data/API | Forms/CRUD | Modals | RBAC | State sync | Console |
|---|---|---|---|---|---|---|---|---|
| PatientDirectory | ✅ | ✅ | ❌ | ✅ | ✅ | ✅ | ✅ | ✅ |

Notes:
- `frontend/src/pages/reception/PatientDirectory.jsx`: Search filter uses `.filter()` on an already-paginated array returned from the backend, meaning it only searches the current page of results.

### Clinic — Patient Details
| Page | Design | Routing | Data/API | Forms/CRUD | Modals | RBAC | State sync | Console |
|---|---|---|---|---|---|---|---|---|
| PatientProfile | ✅ | ✅ | ✅ | ✅ | ✅ | ⚠️ | ✅ | ✅ |

Notes:
- `frontend/src/pages/shared/PatientProfile.jsx`: "Delete Record" button is visible to the Nurse role but returns 403 Forbidden upon click. It should be visually hidden per RBAC rules.

### Clinic — Doctors
| Page | Design | Routing | Data/API | Forms/CRUD | Modals | RBAC | State sync | Console |
|---|---|---|---|---|---|---|---|---|
| DoctorList | ✅ | ✅ | ❌ | ✅ | ✅ | ✅ | ✅ | ✅ |

Notes:
- `frontend/src/pages/public/DoctorList.jsx`: Fails to fetch working hours due to missing `GET /api/doctors/:id/working-hours` endpoint, causing the booking slot preview to silently fail.

### Clinic — Prescriptions
| Page | Design | Routing | Data/API | Forms/CRUD | Modals | RBAC | State sync | Console |
|---|---|---|---|---|---|---|---|---|
| CreatePrescription | ✅ | ✅ | ✅ | ❌ | ✅ | ✅ | ✅ | ✅ |

Notes:
- `frontend/src/pages/doctor/CreatePrescription.jsx`: Submit button does not enter a `disabled` state during the mutation, allowing for double-submission of prescriptions if clicked rapidly.

### Clinic — Prescription Templates
| Page | Design | Routing | Data/API | Forms/CRUD | Modals | RBAC | State sync | Console |
|---|---|---|---|---|---|---|---|---|
| TemplateManager | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ |

Notes:
- All checks pass for this page. Implementation is solid.

### Clinic — Lab Requests & Reports
| Page | Design | Routing | Data/API | Forms/CRUD | Modals | RBAC | State sync | Console |
|---|---|---|---|---|---|---|---|---|
| LabDashboard | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ |
| UploadReports | ⚠️ | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ |

Notes:
- `frontend/src/pages/lab/UploadReports.jsx`: Relies on a generic `<input type="file">` which drifts from the shared design system. Missing usage of the new `FileUpload.jsx` component.

### Clinic — Messages & Notifications
| Page | Design | Routing | Data/API | Forms/CRUD | Modals | RBAC | State sync | Console |
|---|---|---|---|---|---|---|---|---|
| Notifications | ✅ | ✅ | ❌ | ✅ | ✅ | ✅ | ✅ | ❌ |

Notes:
- `frontend/src/components/dashboard/TopNav.jsx`: Repeated console errors due to `GET /api/notifications/unread-count` returning 401 Unauthorized or 404 Not Found depending on the environment.

### Pharmacy — Dashboard
| Page | Design | Routing | Data/API | Forms/CRUD | Modals | RBAC | State sync | Console |
|---|---|---|---|---|---|---|---|---|
| PharmacyDashboard | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ |

Notes:
- Displays correctly; recent updates to `QueryClient` stale times have resolved previous cache issues.

### Pharmacy — Inventory Management
| Page | Design | Routing | Data/API | Forms/CRUD | Modals | RBAC | State sync | Console |
|---|---|---|---|---|---|---|---|---|
| InventoryList | ✅ | ✅ | ⚠️ | ✅ | ✅ | ✅ | ⚠️ | ✅ |

Notes:
- `frontend/src/pages/pharmacy/InventoryList.jsx`: The pagination reflects the true total count, but navigating to page 2 doesn't trigger a re-fetch in Zustand/React-Query due to missing dependency arrays in the query hook.

### Pharmacy — Purchase & Sales
| Page | Design | Routing | Data/API | Forms/CRUD | Modals | RBAC | State sync | Console |
|---|---|---|---|---|---|---|---|---|
| POS | ✅ | ✅ | ✅ | ✅ | ⚠️ | ✅ | ✅ | ✅ |

Notes:
- `frontend/src/pages/pharmacy/POS.jsx`: The checkout modal does not close when the `Escape` key is pressed.

## Cross-Cutting Issues
1. **Search and Pagination Logic:** Many list pages (like `PatientDirectory` and `InventoryList`) are conflating client-side filtering with server-side pagination. If a dataset is server-paginated, local `.filter()` logic only searches the current page, resulting in incomplete search results.
2. **Missing Shared Components:** Some pages still use native HTML elements (like raw `<input type="file">` and standard `<table>`) instead of the shared `FileUpload.jsx` and `DataTable.jsx` components, causing visual inconsistencies.
3. **Double Submission:** Several forms lack `disabled={isSubmitting}` on their submit buttons.
4. **SSE Authorization:** Server-Sent Events (SSE) connections frequently fail with 401 Unauthorized due to `token=null` being appended to the URL string.

## Severity Summary
🔴 **Critical**
- SSE `token=null` failing connections.
- Form double-submission risks in Clinical features (Prescriptions).
- Broken search/filtering on server-paginated pages (Patient Directory).

🟠 **High**
- Missing API endpoints (`/api/notifications/unread-count`, `/api/doctors/:id/working-hours`).
- Incorrect HTTP verbs for actions (`DELETE` vs `PUT` for Appointment cancellation).
- Role-based buttons visible but returning 403 (Patient Profile Delete).

🟡 **Medium**
- Pagination state missing dependency arrays in React Query.
- Modal accessibility (Escape key to close, focus trapping/returning).

🟢 **Low**
- Minor layout shifts on mobile (Book Appointment).
- Use of non-standard UI components instead of design system (Upload Reports).
- Missing `key` props in lists.

## Recommended Fix Order
1. 🔴 Fix the SSE token injection logic so `token=null` is no longer sent, restoring real-time capabilities.
2. 🔴 Fix the client-side `.filter()` bugs on server-paginated tables (convert to API search params).
3. 🔴 Add `disabled={isSubmitting}` to all `react-hook-form` submit buttons to prevent double POSTs.
4. 🟠 Remove or correct UI elements that do not align with backend RBAC (e.g., Nurse delete button).
5. 🟠 Address missing backend endpoints (either remove the frontend call or mock them until backend is ready).
6. 🟡 Fix pagination dependency arrays across Pharmacy and Admin tables.
7. 🟡 Update all modals/drawers to support `Escape` key and focus restoration.
8. 🟢 Replace legacy HTML inputs with Design System components (`FileUpload`, etc.).
9. 🟢 Fix missing `key` props and minor mobile layout shifts.
