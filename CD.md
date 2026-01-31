# Continuous Deployment (CD)

This repository supports Continuous Deployment using both Jenkins and GitHub Actions with standardized Kubernetes labels.

- **Jenkins Pipeline**: [Jenkinsfile](Jenkinsfile)
- **GitHub Actions Workflow**: [.github/workflows/ci-cd.yml](.github/workflows/ci-cd.yml)
- **Kubernetes manifests**:
  - [k8s/deployment.yaml](k8s/deployment.yaml) - Application deployment
  - [k8s/service.yaml](k8s/service.yaml) - Service definition
  - [k8s/pod.yaml](k8s/pod.yaml) - Standalone pod specification
  - [k8s/configmap.yaml](k8s/configmap.yaml) - Application configuration
  - [k8s/kustomization.yaml](k8s/kustomization.yaml) - Kustomize configuration
- **Deploy script**: [scripts/deploy.sh](scripts/deploy.sh)

## Kubernetes Labels

All Kubernetes resources use standardized labels following the [Kubernetes recommended labels](https://kubernetes.io/docs/concepts/overview/working-with-objects/common-labels/):

- `app.kubernetes.io/name`: clinica
- `app.kubernetes.io/component`: backend
- `app.kubernetes.io/instance`: clinica-prod
- `app.kubernetes.io/version`: "1.0"
- `app.kubernetes.io/managed-by`: kubectl

## Jenkins CD

### Jenkins CD Configuration

The Jenkins pipeline includes a CD stage that deploys to Kubernetes when changes are pushed to the `main` branch.

### Prerequisites:

- Jenkins agent with `kubectl` configured for your Kubernetes cluster.
- DockerHub credentials set up in Jenkins (credential ID: `dockerhub-credentials`).
- Replace `your-dockerhub-username` in `Jenkinsfile` with your actual DockerHub username.

### Deployment Process:

1. Build and push Docker image to DockerHub.
2. Apply Kubernetes manifests using `kubectl apply -f k8s/`.

## GitHub Actions CD

### GitHub Actions CD Configuration

The GitHub Actions workflow deploys to Kubernetes after successful CI on the `main` branch.

### Prerequisites:

- Repository secrets: `DOCKERHUB_USERNAME`, `DOCKERHUB_PASSWORD`, `KUBE_CONFIG_DATA` (base64-encoded kubeconfig).
- Ensure `k8s/deployment.yaml` uses the correct image name.

### Deployment Process:

1. CI job builds and pushes Docker image.
2. CD job applies Kubernetes manifests.

## Manual Deployment

To deploy manually:

1. Build and push the Docker image:

   ```bash
   docker build -t your-dockerhub-username/clinica:latest .
   docker push your-dockerhub-username/clinica:latest
   ```

2. Deploy to Kubernetes using kubectl:

   ```bash
   kubectl apply -f k8s/
   ```

   Or using Kustomize:

   ```bash
   kubectl apply -k k8s/
   ```

Or use the deploy script:

```bash
./scripts/deploy.sh
```

## Configuration

The application uses a ConfigMap for externalized configuration:

- **SPRING_PROFILES_ACTIVE**: Set to "prod" for production
- **JAVA_OPTS**: JVM options for memory and performance tuning
- **DOCTOR_SERVICE_URL**: URL of the external doctor microservice
- **DOCTOR_SERVICE_USERNAME/PASSWORD**: Credentials for doctor service authentication

To modify configuration, update the `k8s/configmap.yaml` file and redeploy:

```bash
kubectl apply -f k8s/configmap.yaml
kubectl rollout restart deployment/clinica
```

## Health Checks

The application includes comprehensive health checks:

- **Liveness Probe**: Checks `/actuator/health` every 30 seconds after 60 seconds initial delay
- **Readiness Probe**: Ensures the application is ready to serve traffic
- **Startup Probe**: Prevents premature termination during application startup

## Resource Management

- **Memory**: 256Mi request, 512Mi limit
- **CPU**: 250m request, 500m limit
- **Rolling Update Strategy**: Max 1 unavailable pod, max 1 surge pod

## Docker Hub Integration

The project supports automated Docker image building and pushing to Docker Hub through both Jenkins and GitHub Actions pipelines.

### Jenkins Docker Configuration

- **Image Name**: `DOCKERHUB_USERNAME/clinica` (update with your DockerHub username in Jenkinsfile)
- **Tag Strategy**: Uses build number as tag (`clinica:${BUILD_NUMBER}`)
- **Credentials**: Requires `dockerhub-credentials` in Jenkins

### GitHub Actions Docker Configuration

- **Image Name**: Uses `DOCKERHUB_USERNAME/clinica` from secrets
- **Tag Strategy**: Uses run number as tag (`clinica:${{ github.run_number }}`)
- **Secrets Required**:
  - `DOCKERHUB_USERNAME`: Your DockerHub username
  - `DOCKERHUB_PASSWORD`: Your DockerHub password or access token

### Local Docker Development

For local development and testing, use the provided Gradle tasks and scripts:

```bash
# Secure authentication setup (recommended)
./scripts/set-docker-credentials.sh

# Build and run locally
./gradlew dockerBuild dockerRun

# Push to Docker Hub
./gradlew dockerPush

# Interactive management
./scripts/docker-manager.sh

# Individual scripts
./scripts/docker-build.sh    # Build with custom tagging
./scripts/docker-run.sh      # Run with custom options
./scripts/docker-push.sh     # Push with authentication
```

#### Docker Credentials Script

The `scripts/set-docker-credentials.sh` script provides secure, interactive setup of Docker Hub credentials:

- **Secure Input**: Password/token input is hidden
- **Token Support**: Supports both passwords and access tokens
- **Session-Based**: Credentials are only set for the current shell session
- **Git-Ignored**: Script is excluded from version control for security

### Manual Docker Hub Authentication

Alternatively, set environment variables manually:

```bash
export DOCKER_HUB_USERNAME=your_dockerhub_username
export DOCKER_HUB_PASSWORD=your_dockerhub_password_or_token
```
