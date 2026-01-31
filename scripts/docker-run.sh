#!/bin/bash

# Docker Run Script for Clinica Microservice
# This script runs the Clinica application in a Docker container

set -e

echo "🚀 Starting Clinica Docker Container..."

# Check if Docker is installed
if ! command -v docker &> /dev/null; then
    echo "❌ Docker is not installed. Please install Docker first."
    exit 1
fi

# Stop and remove existing container if it exists
echo "🧹 Cleaning up existing containers..."
docker stop clinica-app 2>/dev/null || true
docker rm clinica-app 2>/dev/null || true

# Run the container
echo "🏃 Starting container..."
docker run -d \
    --name clinica-app \
    -p 9091:9091 \
    --env SPRING_PROFILES_ACTIVE=prod \
    clinica:latest

echo "✅ Container started successfully!"
echo "🌐 Application available at: http://localhost:9091"
echo "📊 H2 Console: http://localhost:9091/h2"
echo "📖 Swagger UI: http://localhost:9091/swagger-ui.html"
echo ""
echo "📋 Container logs:"
docker logs clinica-app
echo ""
echo "🔍 To follow logs: docker logs -f clinica-app"
echo "🛑 To stop: docker stop clinica-app"