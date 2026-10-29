# Student API - Berkinbay Abdigappar

**Author:** Berkinbay Abdigappar
**Student ID:** 37409
**Course:** DevOps - midterm project

Status: CRUD API ready for Docker and Jenkins

## About

A Spring Boot REST API for managing students (create, read, update, delete).
It stores data in PostgreSQL, runs in Docker, and is built and tested by a Jenkins pipeline.

## Technologies

- Java 17, Spring Boot 3.3, Gradle 8.10 (wrapper)
- PostgreSQL 16
- Docker, Docker Compose
- Jenkins (declarative pipeline)

## Project structure

```
berkinbay-abdigappar-student-api/
├── scripts/            Bash script: Berkinbay_Abdigappar_system_report.sh
├── logs/               system report log and log analysis
├── backups/            backup of README.md
├── src/                Spring Boot source code and tests
├── Dockerfile          multi-stage image build
├── docker-compose.yml  application + PostgreSQL
├── Jenkinsfile         CI pipeline
├── build.gradle        Gradle build
└── README.md
```

## API

Base URL: `http://localhost:8081/api/berkinbay-abdigappar/students`

| Method | URL | Description |
|--------|-----|-------------|
| GET | `/` | Application info (student name, ID, version) |
| GET | `/api/berkinbay-abdigappar/students` | All students |
| GET | `/api/berkinbay-abdigappar/students/{id}` | One student |
| POST | `/api/berkinbay-abdigappar/students` | Create a student |
| PUT | `/api/berkinbay-abdigappar/students/{id}` | Update a student |
| DELETE | `/api/berkinbay-abdigappar/students/{id}` | Delete a student |

A request for a nonexistent id returns `404 Not Found` with a JSON error message.

## Running

```bash
cp .env.example .env          # set your own password in .env
docker compose up -d --build
curl http://localhost:8081/
```

Tests: `./gradlew test`

Docker image: `berkinbay-abdigappar-student-api:v1.0-berkinbay-abdigappar`

## Changes
- Added GET /api/berkinbay-abdigappar/students/search?lastName=... - search students by last name (Berkinbay Abdigappar)
