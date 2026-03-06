# MiniS3Worker

Minimal Spring Boot skeleton for the MiniS3 worker service.

Run locally with Maven:

```bash
mvn spring-boot:run -f MiniS3Worker/pom.xml
```

Or build and run the jar:

```bash
mvn -f MiniS3Worker/pom.xml package
java -jar MiniS3Worker/target/minis3-worker-0.0.1-SNAPSHOT.jar
```

Endpoints:

- `GET /api/health` — health check
- `GET /api/tasks` — sample tasks list
