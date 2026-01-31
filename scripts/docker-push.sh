#!/bin/bash

# Docker Push Script for Clinica Microservice
# This script pushes the Docker image to Docker Hub

set -e

echo "📤 Pushing Clinica Docker Image to Docker Hub..."

# Check if required environment variables are set
if [ -z "$DOCKER_HUB_USERNAME" ]; then
    echo "❌ DOCKER_HUB_USERNAME environment variable is not set"
    echo "   Please set it with: export DOCKER_HUB_USERNAME=your_username"
    exit 1
fi

if [ -z "$DOCKER_HUB_PASSWORD" ]; then
    echo "❌ DOCKER_HUB_PASSWORD environment variable is not set"
    echo "   Please set it with: export DOCKER_HUB_PASSWORD=your_password"
    exit 1
fi

# Check if Docker is installed
if ! command -v docker &> /dev/null; then
    echo "❌ Docker is not installed. Please install Docker first."
    exit 1
fi

echo "🔐 Logging into Docker Hub..."
echo "$DOCKER_HUB_PASSWORD" | docker login -u "$DOCKER_HUB_USERNAME" --password-stdin

echo "🏷️  Tagging images..."
docker tag clinica:1.0 "$DOCKER_HUB_USERNAME/clinica:1.0"
docker tag clinica:latest "$DOCKER_HUB_USERNAME/clinica:latest"

echo "📤 Pushing images to Docker Hub..."
docker push "$DOCKER_HUB_USERNAME/clinica:1.0"
docker push "$DOCKER_HUB_USERNAME/clinica:latest"

echo "✅ Images pushed successfully!"
echo "🔗 Docker Hub Repository: https://hub.docker.com/r/$DOCKER_HUB_USERNAME/clinica"
echo ""
echo "📋 Available tags:"
echo "   - $DOCKER_HUB_USERNAME/clinica:1.0"
echo "   - $DOCKER_HUB_USERNAME/clinica:latest"