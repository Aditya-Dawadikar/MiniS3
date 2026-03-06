# MiniS3Master

This is a minimal Spring Boot skeleton for the MiniS3 master service.

Run locally with Maven:

```bash
mvn spring-boot:run
```

Or build and run the jar:

```bash
mvn package
java -jar target/minis3-master-0.0.1-SNAPSHOT.jar
```

Endpoints:

- `GET /api/health` — health check
- `GET /api/buckets` — sample buckets list

## Swagger API Documentation

Once the application is running, you can access the interactive API documentation:

### Swagger UI
- **URL**: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
- **Description**: Interactive Swagger UI to explore and test all API endpoints

### OpenAPI JSON
- **URL**: [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)
- **Description**: OpenAPI specification in JSON format

### Steps to Access:
1. Start the application:
   ```bash
   mvn spring-boot:run
   ```
2. Open your browser and navigate to `http://localhost:8080/swagger-ui.html`
3. Browse available endpoints and use the **Try it out** button to test API calls directly from the UI
