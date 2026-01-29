# Clinica Microservice

This project is a simple Spring Boot microservice for managing doctors in a clinic. It provides a RESTful API for all standard CRUD (Create, Read, Update, Delete) operations.

## Features

- **CRUD Operations**: Full support for creating, reading, updating, and deleting doctor records.
- **In-Memory Database**: Uses H2 as an in-memory database for easy setup and testing.
- **API Documentation**: Integrated Swagger UI for clear, interactive API documentation.
- **Modern Java**: Built with modern, clean Java code.

## Technologies Used

- **Java 21**: The core programming language.
- **Spring Boot 3.2.5**: The application framework.
- **Spring Web**: For building the RESTful API.
- **Spring Data JPA**: For database interaction.
- **H2 Database**: An in-memory database.
- **Lombok**: To reduce boilerplate code.
- **Springdoc OpenAPI**: For generating Swagger API documentation.
- **Gradle**: The build automation tool.

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

To view and interact with the data directly, you can enable the H2 web console.

1.  Add the following line to `src/main/resources/application.properties` (create the file if it doesn't exist):
    ```properties
    spring.h2.console.enabled=true
    ```

2.  Restart the application. The console will be available at:
    - **H2 Console**: [http://localhost:9091/h2-console](http://localhost:8080/h2-console)

    Use the default settings to connect (`JDBC URL: jdbc:h2:mem:testdb`).

## API Endpoints

The API provides the following endpoints for managing doctors:

| Method   | Endpoint                  | Description                               |
|----------|---------------------------|-------------------------------------------|
| `POST`   | `/api/doctors`            | Creates a new doctor.                     |
| `GET`    | `/api/doctors`            | Retrieves a list of all doctors.          |
| `GET`    | `/api/doctors?pincode={pincode}` | Finds doctors by their pincode.        |
| `GET`    | `/api/doctors/{id}`       | Retrieves a single doctor by their ID.    |
| `PUT`    | `/api/doctors/{id}`       | Updates the details of an existing doctor.|
| `DELETE` | `/api/doctors/{id}`       | Deletes a doctor by their ID.             |
| `DELETE` | `/api/doctors`            | Deletes all doctors.                      |

## API Documentation (Swagger UI)

Once the application is running, you can access the interactive Swagger UI to explore the API endpoints in detail.

- **Swagger UI**: [http://localhost:9091/swagger-ui.html](http://localhost:9091/swagger-ui.html)

## Running Tests

To run the full suite of unit and integration tests, use the following Gradle command:

```bash
./gradlew test
```

A test report will be generated in the `build/reports/tests/test/` directory.
