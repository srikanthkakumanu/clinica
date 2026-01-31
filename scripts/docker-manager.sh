#!/bin/bash

# Docker Management Script for Clinica Microservice
# Provides a unified interface for Docker operations

set -e

PROJECT_NAME="clinica"
DOCKER_IMAGE="$PROJECT_NAME:latest"

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Function to print colored output
print_info() {
    echo -e "${BLUE}ℹ️  $1${NC}"
}

print_success() {
    echo -e "${GREEN}✅ $1${NC}"
}

print_warning() {
    echo -e "${YELLOW}⚠️  $1${NC}"
}

print_error() {
    echo -e "${RED}❌ $1${NC}"
}

# Function to check Docker availability
check_docker() {
    if ! command -v docker &> /dev/null; then
        print_error "Docker is not installed. Please install Docker first."
        exit 1
    fi

    if ! docker info &> /dev/null; then
        print_error "Docker daemon is not running. Please start Docker."
        exit 1
    fi
}

# Function to build Docker image
build_image() {
    print_info "Building Docker image..."
    ./gradlew dockerBuild
    print_success "Docker image built successfully!"
    echo ""
    print_info "Available images:"
    docker images | grep clinica
}

# Function to run container locally
run_container() {
    print_info "Starting Clinica container..."

    # Stop existing container if running
    docker stop clinica-app 2>/dev/null || true
    docker rm clinica-app 2>/dev/null || true

    # Run new container
    docker run -d \
        --name clinica-app \
        -p 9091:9091 \
        --env SPRING_PROFILES_ACTIVE=prod \
        clinica:latest

    print_success "Container started successfully!"
    echo ""
    print_info "Application URLs:"
    echo "  🌐 Main App: http://localhost:9091"
    echo "  🗄️  H2 Console: http://localhost:9091/h2"
    echo "  📖 Swagger UI: http://localhost:9091/swagger-ui.html"
    echo ""
    print_info "Container logs:"
    docker logs clinica-app
}

# Function to push to Docker Hub
push_to_hub() {
    if [ -z "$DOCKER_HUB_USERNAME" ]; then
        print_error "DOCKER_HUB_USERNAME environment variable is not set"
        echo "Please set it with: export DOCKER_HUB_USERNAME=your_username"
        exit 1
    fi

    if [ -z "$DOCKER_HUB_PASSWORD" ]; then
        print_error "DOCKER_HUB_PASSWORD environment variable is not set"
        echo "Please set it with: export DOCKER_HUB_PASSWORD=your_password"
        exit 1
    fi

    print_info "Logging into Docker Hub..."
    echo "$DOCKER_HUB_PASSWORD" | docker login -u "$DOCKER_HUB_USERNAME" --password-stdin

    print_info "Tagging and pushing images..."
    docker tag clinica:1.0 "$DOCKER_HUB_USERNAME/clinica:1.0"
    docker tag clinica:latest "$DOCKER_HUB_USERNAME/clinica:latest"

    docker push "$DOCKER_HUB_USERNAME/clinica:1.0"
    docker push "$DOCKER_HUB_USERNAME/clinica:latest"

    print_success "Images pushed to Docker Hub!"
    echo ""
    print_info "Docker Hub Repository: https://hub.docker.com/r/$DOCKER_HUB_USERNAME/clinica"
    print_info "Available tags: 1.0, latest"
}

# Function to show container status
show_status() {
    echo ""
    print_info "Docker Images:"
    docker images | grep clinica || echo "No clinica images found"

    echo ""
    print_info "Running Containers:"
    docker ps | grep clinica || echo "No clinica containers running"

    echo ""
    print_info "All Containers:"
    docker ps -a | grep clinica || echo "No clinica containers found"
}

# Function to clean up Docker resources
cleanup() {
    print_warning "Cleaning up Docker resources..."

    docker stop clinica-app 2>/dev/null || true
    docker rm clinica-app 2>/dev/null || true
    docker rmi clinica:latest clinica:1.0 2>/dev/null || true
    docker system prune -f

    print_success "Cleanup completed!"
}

# Function to show logs
show_logs() {
    if docker ps | grep -q clinica-app; then
        print_info "Container logs:"
        docker logs clinica-app
    else
        print_warning "No clinica container is running"
    fi
}

# Function to show menu
show_menu() {
    echo ""
    echo "========================================"
    echo "🐳 Clinica Docker Management Script"
    echo "========================================"
    echo "1. Build Docker Image"
    echo "2. Run Container Locally"
    echo "3. Push to Docker Hub"
    echo "4. Show Status"
    echo "5. Show Logs"
    echo "6. Cleanup"
    echo "7. Exit"
    echo ""
}

# Main script
check_docker

while true; do
    show_menu
    read -p "Choose an option (1-7): " choice

    case $choice in
        1)
            build_image
            ;;
        2)
            run_container
            ;;
        3)
            push_to_hub
            ;;
        4)
            show_status
            ;;
        5)
            show_logs
            ;;
        6)
            cleanup
            ;;
        7)
            print_info "Goodbye! 👋"
            exit 0
            ;;
        *)
            print_error "Invalid option. Please choose 1-7."
            ;;
    esac

    echo ""
    read -p "Press Enter to continue..."
done