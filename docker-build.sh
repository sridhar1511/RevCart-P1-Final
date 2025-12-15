#!/bin/bash

echo "Building RevCart Docker Images..."

# Build backend image
echo "Building backend image..."
docker build -f Dockerfile.backend -t revcart-backend:latest .

# Build frontend image
echo "Building frontend image..."
docker build -f Dockerfile.frontend -t revcart-frontend:latest .

echo "Docker images built successfully!"
echo "Run 'docker-compose up -d' to start the application"