# Clinica Microservice

This project is a secure Spring Boot microservice for managing clinics with HATEOAS support. It provides a RESTful API for all standard CRUD (Create, Read, Update, Delete) operations with comprehensive security and hypermedia navigation.

## Features

- **CRUD Operations**: Full support for creating, reading, updating, and deleting clinic records.
- **Pagination**: The API supports pagination for retrieving lists of clinics.
- **Doctor Integration**: Associate doctors with clinics via REST integration with a separate doctor microservice.
- **HATEOAS Support**: Hypermedia As The Engine Of Application State (HATEOAS) for discoverable APIs.
- **Input Validation**: Comprehensive Bean Validation with meaningful error messages.
- **Security**: HTTP Basic Authentication with OWASP security best practices.
- **CORS Support**: Cross-Origin Resource Sharing configuration for web applications.
- **In-Memory Database**: Uses H2 as an in-memory database for easy setup and testing.
- **API Documentation**: Integrated Swagger UI for clear, interactive API documentation.
- **Modern Java**: Built with modern, clean Java code.
- **Code Quality**: Integrated SonarQube for static code analysis.
- **Test Coverage**: JaCoCo for code coverage reporting.
- **CI/CD Pipeline**: Jenkins pipeline for automated building, testing, Docker image creation, and deployment.

## Technologies Used

- **Java 21**: The core programming language.
- **Spring Boot 3.4.4**: The application framework.
- **Spring Web**: For building the RESTful API.
- **Spring Data JPA**: For database interaction.
- **Spring HATEOAS**: For hypermedia-driven REST APIs.
- **Spring Security**: For authentication and authorization.
- **Spring Validation**: For input validation.
- **H2 Database**: An in-memory database.
- **Lombok**: To reduce boilerplate code.
- **Springdoc OpenAPI**: For generating Swagger API documentation.
- **Gradle 9.3.1**: The build automation tool.
- **JaCoCo**: For code coverage.
- **SonarQube**: For code quality analysis.
- **Docker**: For containerization.
- **Kubernetes**: For deployment orchestration.
- **Jenkins**: For CI/CD pipeline.

## Getting Started

### Prerequisites

- JDK 21 or later
- Gradle 8.x or later
- Docker (for containerization)
- Kubernetes cluster (for deployment)

### Building and Running the Application

1.  **Clone the repository:**

    ```bash
    git clone <repository-url>
    cd clinica
    ```

2.  **Run the application using Gradle:**
    ```bash
    ./gradlew bootRun
    ```

The application will start on `http://localhost:9091`.

**Note:** The API requires authentication. Use the default credentials:

- Username: `admin`, Password: `admin123`
- Username: `user`, Password: `user123`

### Running with Docker

1. Build the Docker image:

   ```bash
   docker build -t clinica .
   ```

2. Run the container:

   ```bash
   docker run -p 9091:9091 clinica
   ```

3. Build the Docker image:

   ```bash
   docker build -t clinica .
   ```

4. Run the container:
   ```bash
   docker run -p 9091:9091 clinica
   ```

## Accessing the H2 Database Console

To view and interact with the data directly, you can enable the H2 web console. The project is already configured for this.

- **H2 Console**: [http://localhost:9091/h2](http://localhost:9091/h2)

Use the default settings to connect (`JDBC URL: jdbc:h2:mem:clinica`).

## Getting Started

### Prerequisites

- JDK 21 or later
- Gradle 8.x

### Building and Running the Application

1.  **Clone the repository:**

    ```bash
    git clone <repository-url>
    cd clinica
    ```

2.  **Run the application using Gradle:**
    ```bash
    ./gradlew bootRun
    ```

The application will start on `http://localhost:9091`.

## Accessing the H2 Database Console

To view and interact with the data directly, you can enable the H2 web console. The project is already configured for this.

- **H2 Console**: [http://localhost:9091/h2](http://localhost:9091/h2)

Use the default settings to connect (`JDBC URL: jdbc:h2:mem:clinica`).

## API Endpoints

The API provides the following endpoints for managing clinics:

| Method   | Endpoint                                     | Description                                                    |
| -------- | -------------------------------------------- | -------------------------------------------------------------- |
| `POST`   | `/api/clinics`                               | Creates a new clinic.                                          |
| `GET`    | `/api/clinics`                               | Retrieves a paginated list of all clinics.                     |
| `GET`    | `/api/clinics?pincode={pincode}`             | Finds clinics by their pincode (paginated).                    |
| `GET`    | `/api/clinics/{id}`                          | Retrieves a single clinic by their ID with associated doctors. |
| `PUT`    | `/api/clinics/{id}`                          | Updates the details of an existing clinic.                     |
| `POST`   | `/api/clinics/{clinicId}/doctors/{doctorId}` | Adds a doctor to a clinic.                                     |
| `DELETE` | `/api/clinics/{clinicId}/doctors/{doctorId}` | Removes a doctor from a clinic.                                |
| `DELETE` | `/api/clinics/{id}`                          | Deletes a clinic by their ID.                                  |
| `DELETE` | `/api/clinics`                               | Deletes all clinics.                                           |

### Pagination Parameters

The `GET /api/clinics` endpoint supports the following query parameters for pagination:

- `page`: The page number to retrieve (0-indexed).
- `size`: The number of items per page.
- `sort`: A comma-separated list of properties to sort by (e.g., `name,asc`).

**Example:**

```
GET /api/clinics?page=0&size=5&sort=name,asc
```

This request retrieves the first page of 5 clinics, sorted by their name in ascending order.

## Security and Authentication

The API implements HTTP Basic Authentication with OWASP security best practices.

### Authentication

All API endpoints (except documentation and health checks) require authentication using HTTP Basic Authentication.

**Default Users:**

- **Username:** `admin`, **Password:** `admin123` (ADMIN role)
- **Username:** `user`, **Password:** `user123` (USER role)

### Security Features

- **HTTP Basic Authentication**: Secure username/password authentication
- **Stateless Sessions**: No server-side session storage
- **BCrypt Password Encoding**: Secure password hashing
- **CORS Support**: Configured for cross-origin requests
- **Input Validation**: Comprehensive Bean Validation on all inputs
- **CSRF Protection**: Disabled for stateless API design

### API Usage with Authentication

Include the `Authorization` header with Base64-encoded credentials in all requests:

```bash
# Using admin credentials
curl -H "Authorization: Basic YWRtaW46YWRtaW4xMjM=" \
     http://localhost:9091/api/clinics

# Or use -u flag
curl -u admin:admin123 \
     http://localhost:9091/api/clinics
```

### Public Endpoints

The following endpoints are accessible without authentication:

- `/swagger-ui/**` - API documentation
- `/v3/api-docs/**` - OpenAPI specification
- `/actuator/**` - Spring Boot actuator endpoints

## HATEOAS Support

The API implements Hypermedia As The Engine Of Application State (HATEOAS), providing discoverable navigation through hypermedia links in JSON responses.

### Link Relations

Clinic resources include the following links:

- `self`: Link to the current clinic resource
- `update`: Link to update the clinic (PUT request)
- `delete`: Link to delete the clinic (DELETE request)
- `all-clinics`: Link to retrieve all clinics
- `remove-doctor-{id}`: Links to remove specific doctors from the clinic

### Example HATEOAS Response

```json
{
  "id": 1,
  "name": "City Clinic",
  "address": "123 Main St",
  "city": "Anytown",
  "pincode": "12345",
  "phone": "1234567890",
  "email": "clinic@example.com",
  "doctorIds": [1, 2],
  "_links": {
    "self": {
      "href": "http://localhost:9091/api/clinics/1"
    },
    "update": {
      "href": "http://localhost:9091/api/clinics/1"
    },
    "delete": {
      "href": "http://localhost:9091/api/clinics/1"
    },
    "all-clinics": {
      "href": "http://localhost:9091/api/clinics"
    },
    "remove-doctor-1": {
      "href": "http://localhost:9091/api/clinics/1/doctors/1"
    },
    "remove-doctor-2": {
      "href": "http://localhost:9091/api/clinics/1/doctors/2"
    }
  }
}
```

## Input Validation

The API implements comprehensive input validation using Bean Validation annotations. All clinic data is validated before processing.

### Validation Rules

- **Name**: Required, 2-100 characters
- **Address**: Required, maximum 255 characters
- **City**: Required, maximum 100 characters
- **Pincode**: Required, must be 5 or 6 digits
- **Phone**: Required, must be exactly 10 digits
- **Email**: Required, must be a valid email format

### Validation Error Response

Invalid requests return HTTP 400 Bad Request with detailed error messages:

```json
{
  "timestamp": "2026-01-31T21:27:00.000+00:00",
  "status": 400,
  "error": "Bad Request",
  "path": "/api/clinics",
  "errors": [
    {
      "field": "email",
      "message": "Email must be valid",
      "rejectedValue": "invalid-email"
    },
    {
      "field": "phone",
      "message": "Phone must be 10 digits",
      "rejectedValue": "123"
    }
  ]
}
```

## Doctor Service Integration

The clinic service integrates with a separate doctor microservice to manage doctor associations. Clinics can have multiple doctors assigned to them.

- **Doctor Service URL**: Configurable via `doctor.service.url` (default: `http://localhost:9092`)
- **Authentication**: Uses HTTP Basic Auth with configurable username/password
- When retrieving a clinic, the API fetches associated doctor details from the doctor service
- Doctors are referenced by their IDs in the clinic entity

### Configuration Properties

Add the following to `application.yml` to configure the doctor service:

```yaml
doctor:
  service:
    url: http://localhost:9092
    username: admin
    password: password
```

## API Documentation (Swagger UI)

Once the application is running, you can access the interactive Swagger UI to explore the API endpoints in detail.

- **Swagger UI**: [http://localhost:9091/swagger-ui.html](http://localhost:9091/swagger-ui.html)

**Note:** You will need to authenticate using the credentials above to test the API endpoints in Swagger UI.

## Running Tests

To run the full suite of unit and integration tests, use the following Gradle command:

```bash
./gradlew test jacocoTestReport
```

Test reports will be generated in the `build/test-results/test/` directory, and coverage reports in `build/reports/jacoco/test/`.

## Code Quality and Coverage

### SonarQube Analysis

The project is configured for SonarQube code quality analysis. The `sonar-project.properties` file contains the necessary configuration.

To run SonarQube analysis locally:

```bash
sonar-scanner -Dsonar.host.url=https://your-sonarqube-server -Dsonar.login=YOUR_TOKEN
```

### JaCoCo Coverage

JaCoCo generates code coverage reports during testing. The HTML report can be found in `build/reports/jacoco/test/html/index.html` after running tests.

## CI/CD Pipeline

The project supports both Jenkins and GitHub Actions for Continuous Integration and Continuous Deployment.

### Jenkins Pipeline

- **Configuration**: [Jenkinsfile](Jenkinsfile)
- **CI Stages**: Build, Test (with JaCoCo), Code Quality (SonarQube), Quality Gate, Build Docker Image, Push to DockerHub.
- **CD Stage**: Deploy to Kubernetes on the `main` branch.

### GitHub Actions Pipeline

- **Workflow**: [.github/workflows/ci-cd.yml](.github/workflows/ci-cd.yml)
- **CI Job**: Build, Test (with JaCoCo), SonarQube Scan, Build and Push Docker Image to DockerHub.
- **CD Job**: Deploy to Kubernetes on push to `main` branch.

### Prerequisites for Both:

- SonarQube server with `SONAR_HOST_URL` and `SONAR_TOKEN` secrets.
- DockerHub credentials (`DOCKERHUB_USERNAME` and `DOCKERHUB_PASSWORD` for GitHub Actions; `dockerhub-credentials` for Jenkins).
- Kubernetes cluster access (`KUBE_CONFIG_DATA` secret for GitHub Actions; kubectl configured for Jenkins).
