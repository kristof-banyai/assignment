# Assignment (Spring Boot + PostgreSQL)

Spring Boot 3 (Java 17) REST API for managing Dogs. Uses PostgreSQL + Liquibase for migrations, and includes a scheduler that fills missing dog images using the Dog CEO API.

## Tech stack

- Java 17
- Spring Boot (Web, Validation, Data JPA)
- PostgreSQL
- Liquibase (with contexts: `dev`, `uat`)
- springdoc-openapi (Swagger UI)
- Lombok
- Maven Wrapper

## Features

### REST API (Dogs)
Base path: `/api/v1/dogs`

- `GET /api/v1/dogs` — list all dogs
- `GET /api/v1/dogs/{id}` — get dog by id (returns 404 if not found)
- `POST /api/v1/dogs` — create dog (validation enabled)
- `DELETE /api/v1/dogs/{id}` — delete dog (returns 404 if not found)

### Public endpoints
- `GET /health` — returns a simple health string
- `GET /version` — returns application version from Spring Boot build-info

### Swagger / OpenAPI
- Swagger UI: `/swagger-ui/index.html`
- OpenAPI JSON: `/v3/api-docs`

### Scheduled job: fill missing dog images
A scheduled task periodically finds the first dog with a missing image and fetches a random image URL from Dog CEO API, then updates the record.

Config (see `src/main/resources/application.yaml`):

- `dog.ceo.random-image-url`
- `scheduler.dog-image.fixed-delay`

## Running with Docker Compose (recommended)

This repo supports running `dev` and `uat` in parallel **without .env files** by using compose override files and different host ports.

### Prerequisites
- Docker Desktop (with `docker compose`)
- PowerShell (commands below)

### Run DEV (API: 8000, DB: 5432)

```powershell 
$env:POSTGRES_PASSWORD="<dev-password>" docker compose -p assignment-dev -f docker-compose.yaml -f docker-compose-dev.yaml up --build -d
```

DEV URLs:
- API: http://localhost:8000
- Swagger UI: http://localhost:8000/swagger-ui/index.html

### Run UAT (API: 8001, DB: 5433)

```powershell 
$env:POSTGRES_PASSWORD="<uat-password>" docker compose -p assignment-uat -f docker-compose.yaml -f docker-compose-uat.yaml up --build -d
```

UAT URLs:
- API: http://localhost:8001
- Swagger UI: http://localhost:8001/swagger-ui/index.html

### Stop
Keep DB data:

```powershell 
docker compose -p assignment-dev -f docker-compose.yaml -f docker-compose-dev.yaml down docker compose -p assignment-uat -f docker-compose.yaml -f docker-compose-uat.yaml down
```

Remove DB data too (destructive):
```powershell 
docker compose -p assignment-dev -f docker-compose.yaml -f docker-compose-dev.yaml down -v docker compose -p assignment-uat -f docker-compose.yaml -f docker-compose-uat.yaml down -v
```

## Running tests

### Prerequisites
- Java 17 (JDK)
- Maven Wrapper (`mvnw` / `mvnw.cmd`)
- Docker Desktop (required for the Testcontainers-based integration test)


> Note: Maven uses the Java runtime available in your terminal/CI environment.  
> If you have multiple JDKs installed, make sure Maven runs with **Java 17**.

### Tests included
- Unit tests (e.g., scheduler logic with Mockito)
- Web slice tests (`@WebMvcTest` for controllers)
- Integration test: `DogApiIntegrationTest` (boots Spring + runs against PostgreSQL via Testcontainers)


### Run all tests

```powershell 
.\mvnw.cmd clean test
```

## Configuration notes

- Default config: `src/main/resources/application.yaml`
- Profile configs:
    - `application-dev.yaml` (Liquibase context: `dev`)
    - `application-uat.yaml` (Liquibase context: `uat`)

When running in Docker Compose, the profile and datasource settings are provided via container environment variables in:
- `docker-compose-dev.yaml`
- `docker-compose-uat.yaml`