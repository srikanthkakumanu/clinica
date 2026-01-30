# Continuous Deployment (CD)

This repository includes a Jenkins pipeline for Continuous Deployment to Kubernetes.

- **Pipeline**: [Jenkinsfile](Jenkinsfile)
- **Kubernetes manifests**: [k8s/deployment.yaml](k8s/deployment.yaml) and [k8s/service.yaml](k8s/service.yaml)
- **Deploy script**: [scripts/deploy.sh](scripts/deploy.sh)

## Jenkins CD Configuration

The Jenkins pipeline includes a CD stage that deploys to Kubernetes when changes are pushed to the `main` branch.

### Prerequisites:

- Jenkins agent with `kubectl` configured for your Kubernetes cluster.
- DockerHub credentials set up in Jenkins (credential ID: `dockerhub-credentials`).
- Replace `your-dockerhub-username` in `Jenkinsfile` with your actual DockerHub username.

### Deployment Process:

1. Build and push Docker image to DockerHub.
2. Apply Kubernetes manifests using `kubectl apply -f k8s/`.

## Manual Deployment

To deploy manually:

1. Build and push the Docker image:

   ```bash
   docker build -t your-dockerhub-username/clinica:latest .
   docker push your-dockerhub-username/clinica:latest
   ```

2. Deploy to Kubernetes:
   ```bash
   kubectl apply -f k8s/
   ```

Or use the deploy script:

```bash
./scripts/deploy.sh
```

## Notes

- Ensure the Kubernetes manifests are configured for your environment (e.g., correct image name, ports, environment variables).
- Adapt the pipeline and manifests to match your organization's policies (e.g., image scanning, approvals, staging environments).
