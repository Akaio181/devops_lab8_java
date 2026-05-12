# Task API — CI/CD Pipeline Project

A secure REST API for task management, built with **Java 17 + Spring Boot 3.2 + Maven**,
with a complete CI/CD pipeline using **GitHub Actions**.

---

## Application Features

| Feature | Description |
|---|---|
| Task CRUD | Create, read, update, delete tasks with status filter |
| JWT Authentication | Stateless Bearer token auth on all `/api/tasks` endpoints |
| Input Validation | `@Valid` + Bean Validation on all inputs |

**Endpoints:**

```
POST   /api/auth/login        # Get JWT token
GET    /api/tasks             # List all tasks (filter: ?status=TODO)
GET    /api/tasks/{id}        # Get task by ID
POST   /api/tasks             # Create task
PUT    /api/tasks/{id}        # Update task
DELETE /api/tasks/{id}        # Delete task
GET    /actuator/health       # Health check (public)
```

---

## Quick Start

```bash
# Build
mvn clean package -DskipTests

# Run locally
JWT_SECRET=my-super-secret-32-char-key-here \
  java -jar target/task-api-1.0.0.jar

# Login and get token
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}'

# Use the token
curl http://localhost:8080/api/tasks \
  -H "Authorization: Bearer <YOUR_TOKEN>"
```

---

## CI/CD Pipeline

```
Push → Build+Lint → Test → Security → Package → Deploy Staging → Deploy Production
```

| Stage | Tool | Fails On |
|---|---|---|
| Lint | Checkstyle | Style violations |
| Unit Tests | JUnit 5 + Mockito | Test failures |
| Coverage | JaCoCo | < 60% line coverage |
| SAST | SpotBugs | High-severity bugs |
| Dependency Scan | OWASP Dep-Check | CVSS ≥ 7 CVEs |
| Package | Maven | Build errors |
| Deploy Staging | Simulated | — |
| Deploy Production | Simulated (main only) | — |

---

## Secrets

| Secret | Where | Description |
|---|---|---|
| `JWT_SECRET` | Env var / GitHub Secret | JWT signing key (min 32 chars) |
| `JWT_SECRET_STAGING` | GitHub Actions Secret | Staging JWT key |
| `JWT_SECRET_PROD` | GitHub Actions Secret | Production JWT key |
| `NVD_API_KEY` | GitHub Actions Secret | OWASP NVD API key |

**Never hardcode secrets.** Production uses `${JWT_SECRET}` injected at runtime.

---

## Monitoring

- **Logs** — written to `logs/app.log` via Logback
- **Actuator** — `/actuator/health`, `/actuator/metrics`
- **Prometheus** — add `micrometer-registry-prometheus` dep, scrape `/actuator/prometheus`
- **Grafana** — connect to Prometheus, import Spring Boot dashboard ID 4701

---

## Profiles

| Profile | Command |
|---|---|
| Default (H2) | `java -jar app.jar` |
| Staging | `java -jar app.jar --spring.profiles.active=staging` |
| Production | `java -jar app.jar --spring.profiles.active=production` |
