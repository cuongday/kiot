# Docker Setup for Kiot Backend

This repository contains Docker configuration for running the Kiot Backend application with MySQL database.

## Prerequisites

- Docker Engine (version 19.03.0+)
- Docker Compose (version 1.27.0+)

## Docker Configuration

The project includes the following Docker files:

1. `Dockerfile` - Defines the container for the Spring Boot application
2. `docker-compose.yml` - Orchestrates the application and database services
3. `src/main/resources/application-docker.properties` - Contains Docker-specific application configuration

## Getting Started

### Building and Running the Application

1. Clone the repository
2. Navigate to the project root directory
3. Run the following command to start the application:

```bash
docker-compose up
```

To run the services in detached mode (background):

```bash
docker-compose up -d
```

### Accessing the Application

- The application will be available at: `http://localhost:8081`
- The MySQL database will be accessible at: `localhost:3307`
  - Database: `kiotdb`
  - Username: `root`
  - Password: `123456`

### Stopping the Application

To stop the services:

```bash
docker-compose down
```

To stop the services and remove volumes:

```bash
docker-compose down -v
```

## Data Persistence

- MySQL data is persisted in a Docker volume named `mysql-data`
- File uploads are stored in the `./upload` directory which is mounted to `/app/upload` in the container

## Environment Variables

You can customize the application by modifying the environment variables in the `docker-compose.yml` file or by creating a `.env` file in the project root.

## Troubleshooting

If you encounter issues with the application not connecting to the database:

1. Ensure the database service is running:
   ```bash
   docker-compose ps
   ```

2. Check the database logs:
   ```bash
   docker-compose logs db
   ```

3. Check the application logs:
   ```bash
   docker-compose logs app
   ``` 