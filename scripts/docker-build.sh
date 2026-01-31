#!/bin/bash

# Docker Build Script for Clinica Microservice
# This script builds the Docker image for the clinica application

set -e

echo "🏗️  Building Clinica Docker Image..."

# Check if Docker is installed
if ! command -v docker &> /dev/null; then
    echo "❌ Docker is not installed. Please install Docker first."
    exit 1
fi

# Build the Docker image
echo "📦 Building Docker image..."
./gradlew dockerBuild

echo "✅ Docker image built successfully!"
echo "📋 Available images:"
docker images | grep clinica

echo ""
echo "🚀 To run the container locally:"
echo "   ./gradlew dockerRun"
echo ""
echo "📤 To push to Docker Hub:"
echo "   export DOCKER_HUB_USERNAME=your_username"
echo "   export DOCKER_HUB_PASSWORD=your_password"
echo "   ./gradlew dockerPush"