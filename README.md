# Clinica Microservice

This project is a simple Spring Boot microservice for managing doctors in a clinic. It provides a RESTful API for all standard CRUD (Create, Read, Update, Delete) operations.

## Features

- **CRUD Operations**: Full support for creating, reading, updating, and deleting doctor records.
- **Pagination**: The API supports pagination for retrieving lists of doctors.
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

### Running with Docker

1. Build the Docker image:

   ```bash
   docker build -t clinica .
   ```

2. Run the container:
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

The API provides the following endpoints for managing doctors:

| Method   | Endpoint                         | Description                                 |
| -------- | -------------------------------- | ------------------------------------------- |
| `POST`   | `/api/doctors`                   | Creates a new doctor.                       |
| `GET`    | `/api/doctors`                   | Retrieves a paginated list of all doctors.  |
| `GET`    | `/api/doctors?pincode={pincode}` | Finds doctors by their pincode (paginated). |
| `GET`    | `/api/doctors/{id}`              | Retrieves a single doctor by their ID.      |
| `PUT`    | `/api/doctors/{id}`              | Updates the details of an existing doctor.  |
| `DELETE` | `/api/doctors/{id}`              | Deletes a doctor by their ID.               |
| `DELETE` | `/api/doctors`                   | Deletes all doctors.                        |

### Pagination Parameters

The `GET /api/doctors` endpoint supports the following query parameters for pagination:

- `page`: The page number to retrieve (0-indexed).
- `size`: The number of items per page.
- `sort`: A comma-separated list of properties to sort by (e.g., `lastName,asc`).

**Example:**

```
GET /api/doctors?page=0&size=5&sort=lastName,asc
```

This request retrieves the first page of 5 doctors, sorted by their last name in ascending order.

## API Documentation (Swagger UI)

Once the application is running, you can access the interactive Swagger UI to explore the API endpoints in detail.

- **Swagger UI**: [http://localhost:9091/swagger-ui.html](http://localhost:9091/swagger-ui.html)

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

The project uses a Jenkins pipeline for Continuous Integration and Continuous Deployment.

### CI Stages:

- **Build**: Compiles the application.
- **Test**: Runs tests and generates coverage reports.
- **Code Quality**: Performs SonarQube analysis.
- **Quality Gate**: Waits for SonarQube quality gate.
- **Build Docker Image**: Creates a Docker image.
- **Push to DockerHub**: Pushes the image to DockerHub.

### CD Stage:

- **Deploy**: Deploys to Kubernetes on the `main` branch.

### Prerequisites for Jenkins:

- SonarQube server configured in Jenkins.
- DockerHub credentials set up.
- `sonar-scanner` installed on the Jenkins agent.
- Kubernetes cluster access for deployment.

See `Jenkinsfile` for the pipeline configuration.
