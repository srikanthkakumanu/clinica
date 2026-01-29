# Continuous Deployment (CD)

This repository includes a GitHub Actions workflow to build, test, build/push a Docker image, and deploy to Kubernetes.

- **Workflow**: [.github/workflows/ci-cd.yml](.github/workflows/ci-cd.yml)
- **Kubernetes manifests**: [k8s/deployment.yaml](k8s/deployment.yaml) and [k8s/service.yaml](k8s/service.yaml)
- **Deploy script**: [scripts/deploy.sh](scripts/deploy.sh)

Placeholders in the workflow and manifests must be supplied in your repository secrets or replaced before use:

- `REGISTRY` — Docker registry host (for example `ghcr.io/<owner>`).
- `REGISTRY_USERNAME` and `REGISTRY_PASSWORD` — credentials for the registry.
- `KUBE_CONFIG_DATA` — base64-encoded kubeconfig for `kubectl` access in the deploy job.

To enable CD:

1. Add the above secrets in your GitHub repository settings.
2. Ensure `k8s/deployment.yaml` uses the correct container port and environment variables for your production profile.
3. Push to the `main` branch to trigger the workflow, or run the `scripts/deploy.sh` locally with an image reference:

```bash
./scripts/deploy.sh my-registry/clinica:tag ~/.kube/config
```

Notes:

- The workflow uses placeholders and minimal steps; adapt and secure it to match your org policies (image signing, approvals, staging promotion, etc.).
- `KUBE_CONFIG_DATA` must be base64-encoded content of your kubeconfig file (for runner use). Alternatively, replace the deploy step with a deployment action that reads from secrets or uses a deployment gate.
