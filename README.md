# Clinic Website

A monolithic clinic management platform with a Spring Boot backend and a React/Vite frontend.

## Architecture

| Layer | Technology |
|-------|------------|
| Runtime | Java 21 |
| Framework | **Spring Boot 4.1.0** |
| Persistence | Spring Data JPA + Flyway migrations |
| Auth | Spring Security — JWT (JJWT 0.11.5) |
| Database | PostgreSQL (hosted on Supabase) |
| Frontend | React 19 + Vite 8 |

## Project Structure

```
clinic-website/
├── backend/            ← Spring Boot monolith
│   ├── pom.xml
│   └── src/main/java/com/healthcare/clinic/
│       ├── config/     ← DataSeeder, etc.
│       ├── security/   ← JWT filter, SecurityConfig
│       ├── identity/   ← Auth, users, roles
│       ├── patient/    ← Patient module
│       ├── doctor/     ← Doctor module
│       ├── appointment/
│       └── ...         ← (25 domain modules total)
├── frontend/           ← React/Vite SPA
│   └── src/
├── docker-compose.yml
├── .env.example        ← Copy to .env and fill in values
└── find_region.sh      ← Helper to detect Supabase region
```

## Prerequisites

- Java 21+
- Maven 3.9+
- Node.js 22+ / npm 10+
- Docker (for local PostgreSQL via docker-compose)
- A [Supabase](https://supabase.com) project **or** a local PostgreSQL instance

## Getting Started

### 1. Configure environment variables

```bash
cp .env.example .env
# Edit .env and fill in all required values.
# See comments in .env.example for each variable.
```

> ⚠️ **Never commit `.env`** — it is listed in `.gitignore`.

### 2. Start the database

```bash
docker-compose up -d postgres
```

### 3. Start the backend

```bash
cd backend
mvn spring-boot:run
# Or: mvn clean package && java -jar target/clinic-app-*.jar
```

The backend runs on `http://localhost:8080`.

### 4. Start the frontend

```bash
cd frontend
npm install
npm run dev
```

The frontend runs on `http://localhost:5173`.

## API

All REST endpoints are served under `http://localhost:8080/api/*`.

Key public endpoints (no auth required):
- `POST /api/auth/{portal}/login`
- `POST /api/auth/register`

All other endpoints require a valid `Authorization: Bearer <token>` header.

## find_region.sh

Discovers which Supabase pooler region accepts a connection. Requires env vars:

```bash
SUPABASE_PROJECT_ID=your-project-id PGPASSWORD=your-db-password ./find_region.sh
```

## Security Notes

- Seed passwords for `admin@clinic.com` and `doctor@clinic.com` are read from `SEED_ADMIN_PASSWORD` / `SEED_DOCTOR_PASSWORD` env vars. The app **refuses to start** if these are unset.
- `X-Forwarded-For` is trusted as-is for IP logging. In production behind a reverse proxy, set `server.forward-headers-strategy=NATIVE` in `application.yml` and configure trusted proxies.
- JJWT 0.11.5 uses some deprecated API methods (`SignatureAlgorithm`). Migration to JJWT 0.12.x is recommended when time permits.
- No automated tests currently exist. Adding JUnit/Mockito unit tests is a tracked follow-up.

## Deployment

The entire application stack (Frontend, Backend, and Database) can be containerized and run locally using Docker Compose.

To start the complete stack:
```bash
# Ensure .env is fully configured with SEED_* passwords and SUPABASE_* values
docker-compose up --build -d
```

- **Frontend**: Available at `http://localhost:5173`
- **Backend API**: Available at `http://localhost:8080`
- **Database**: Postgres running on port `5432`

To shut down the stack:
```bash
docker-compose down
```
