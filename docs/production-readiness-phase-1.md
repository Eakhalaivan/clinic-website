# Production Readiness Phase 1 Remediation Plan

## 1. Initial Audit Findings

### Uncommitted / Extraneous Files
- Multiple untracked files generated from local development or tests (e.g. `*.py`, `*.sh`, `.orig`, `.rej`, `.md` reports in frontend).
- `.env` files in root and frontend containing secrets or configuration variables that should not be tracked by Git.

### Backend Tests
- All 40 backend tests pass successfully (`mvn clean test`). The test baseline is stable.

### Frontend Tests
- The React/Vite frontend test suite fails during `npm test` due to an assertion failure in `LoginPage.test.jsx`.
- The Vite build throws 564 lint warnings and 1 error (`react-hooks/exhaustive-deps` in `DoctorDashboard.jsx`).

### Security Posture (Pre-Remediation)
- `/uploads/**` static resource handler is publicly exposing potential patient health records.
- Cross-Origin Resource Sharing (`@CrossOrigin("*")`) is unrestricted in some controllers.
- Access token is stored in `localStorage` in the browser, making it vulnerable to XSS.
- Lack of an explicit `RBAC Matrix` documentation means role boundaries are loosely defined.
- `ROLE_ADMIN` bypass is present in the UI routing.

## 2. Remediation Plan

### A. Security & File Tracking
1. Force remove any tracked `.env` and sensitive local files from Git history, adding them to `.gitignore`.
2. Delete untracked helper scripts (`*.py`, `*.sh`) and logs (`*.txt`, `*.md`) to sanitize the release package.
3. Migrate `/uploads/**` to an authenticated download REST Controller mapped to `/api/documents/download`.
4. Restrict CORS by removing `@CrossOrigin("*")` and enforcing environment variable-based `ALLOWED_ORIGINS`.

### B. Authentication & RBAC
1. Remove all frontend `ROLE_ADMIN` bypasses.
2. Ensure explicit backend method-level security (`@PreAuthorize`) based on roles and resource ownership.
3. Replace `localStorage` token storage with `httpOnly` secure cookies.
4. Write `docs/rbac-matrix.md` with explicit permissions for each role.

### C. Test Baseline & CI/CD
1. Fix the failing test in `LoginPage.test.jsx`.
2. Fix all frontend lint errors (specifically `exhaustive-deps` on hooks).
3. Create `.github/workflows/ci.yml` that mandates backend tests, frontend tests, linting, build, and `TruffleHog` secret scanning as quality gates.
