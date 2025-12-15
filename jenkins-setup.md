# Jenkins Setup for RevCart

## Prerequisites
- Jenkins installed with Docker plugin
- Docker and Docker Compose available on Jenkins agent
- GitHub repository access

## Jenkins Configuration Steps

### 1. Create New Pipeline Job
```
1. Open Jenkins Dashboard
2. Click "New Item"
3. Enter job name: "RevCart-Pipeline"
4. Select "Pipeline"
5. Click "OK"
```

### 2. Configure Pipeline
```
General:
- Description: "RevCart E-commerce CI/CD Pipeline"
- GitHub project URL: https://github.com/sridhar1511/RevCart_P1-Final.git

Build Triggers:
- ✓ Poll SCM
- Schedule: H/10 * * * * (every 10 minutes)

Pipeline:
- Definition: Pipeline script from SCM
- SCM: Git
- Repository URL: https://github.com/sridhar1511/RevCart_P1-Final.git
- Branch: */main
- Script Path: Jenkinsfile
```

### 3. Required Jenkins Plugins
```bash
# Install via Jenkins Plugin Manager
- Docker Pipeline
- Git
- GitHub
- Pipeline
- Workspace Cleanup
```

### 4. Docker Permissions (Linux)
```bash
# Add jenkins user to docker group
sudo usermod -aG docker jenkins
sudo systemctl restart jenkins
```

### 5. Test Pipeline
```
1. Save pipeline configuration
2. Click "Build Now"
3. Monitor console output
4. Verify services at:
   - Frontend: http://localhost:4200
   - Backend: http://localhost:8081/api
```

## Troubleshooting

### Common Issues
- **Docker permission denied**: Add jenkins to docker group
- **Port conflicts**: Stop existing services on ports 4200, 8081, 3307, 27017
- **Build failures**: Check Docker Compose logs

### Useful Commands
```bash
# Check Jenkins logs
sudo journalctl -u jenkins -f

# Check Docker containers
docker ps -a

# View application logs
docker-compose logs -f

# Restart services
docker-compose down && docker-compose up -d
```