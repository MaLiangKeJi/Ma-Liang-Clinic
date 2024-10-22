

# Clinic Management System - Open Source Project Documentation

Welcome to the open-source clinic management system developed by Ma Liang Technology! This project aims to provide an efficient and scalable solution for managing small to medium-sized clinics using a modern technology stack, including Spring Boot, MyBatis Plus, Vue 3, Nacos, MySQL, and Redis.

In this documentation, we will walk you through the project setup, the file configuration required for local development, and how to contribute code. Let's get started!

---

## Links

 [中文文档](README.md) 

[Front-end documentation](FE_English.md) | [Official Website](https://www.maliang.work)

## Project Overview

This project is divided into two main backend services: the **auth** service and the **clinic** service. These two services are interdependent, with the **auth** service managing authentication and authorization, and the **clinic** service handling core clinic operations. We use **Nacos** for configuration management and service discovery, **MySQL** for database management, and **Redis** for caching.

### Tech Stack

- **Backend**: Spring Boot, MyBatis Plus, Redis
- **Frontend**: Vue 3, Element UI Plus
- **Configuration Center**: Nacos 2.3.2
- **Database**: MySQL 8.0.22
- **Caching**: Redis

### Project Structure (Based on Provided Images)

The project consists of two major services, **auth** and **clinic**, each with its own configurations and initialization files.

#### `auth` Service Structure:

```
auth/
├── src/main/java/...
├── src/main/resources/
│   ├── auth-local.yaml       # Configuration file to be modified for local use
│   ├── bootstrap.properties   # Nacos namespace configuration
│   ├── redisson.yml           # Redis configuration file
│   └── auth.sql               # SQL script for initializing the database
└── ...
```

#### `clinic` Service Structure:

```
clinic/
├── src/main/java/...
├── src/main/resources/
│   ├── clinic-local.yaml      # Configuration file to be modified for local use
│   ├── bootstrap.properties   # Nacos namespace configuration
│   └── clinic.sql             # SQL script for initializing the database
└── ...
```

---

## Prerequisites

Before running the project, ensure you have the following services installed and running locally:

- **MySQL**: Used for storing clinic system data
- **Nacos**: Acts as the configuration center and service registry
- **Redis**: Used for caching

---

## Quick Start Guide

### 1. Clone the Project

First, clone the project from the GitHub repository:

```bash
git clone https://github.com/MaLiangKeJi/Ma-Liang-Clinic.git
```

### 2. Database Initialization

The project includes two SQL scripts, `auth.sql` and `clinic.sql`, located in the `src/main/resources` directory of each service. These scripts are used to initialize the MySQL database tables for the **auth** and **clinic** services.

```bash
# Log in to your MySQL database and execute the following commands:
source /auth/resources/auth.sql
source /clinic/resources/clinic.sql
```

**Note**: Adjust the database names, usernames, and passwords in the SQL files to match your local environment.

### 3. Configuration File Modifications

To ensure the project runs correctly, you need to modify several configuration files to match your local MySQL, Redis, and Nacos environments.

#### 3.1 Modify `auth-local.yaml`

The `auth-local.yaml` file contains local configurations for the **auth** service. You will need to replace placeholder values with your actual MySQL, Redis, and Nacos configurations.

```yaml
# Replace "xxxxxx" with actual values in auth-local.yaml
datasource:
  url: jdbc:mysql://localhost:3306/auth_db?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai
  username: root
  password: your_password

nacos:
  server-addr: localhost:8848
  namespace: your_namespace_id
  username: nacos_username
  password: nacos_password

redis:
  host: localhost
  port: 6379
```

**Explanation**:
- `datasource.url`: The MySQL connection string.
- `nacos.server-addr`: The Nacos server address, typically `localhost:8848` for local setups.
- `redis.host` and `redis.port`: Redis connection information.

#### 3.2 Modify `clinic-local.yaml`

Similarly, the `clinic-local.yaml` file needs to be updated for the **clinic** service. Adjust it based on your environment:

```yaml
datasource:
  url: jdbc:mysql://localhost:3306/clinic_db?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai
  username: root
  password: your_password

nacos:
  server-addr: localhost:8848
  namespace: your_namespace_id
  username: nacos_username
  password: nacos_password

redis:
  host: localhost
  port: 6379
```

#### 3.3 Modify `bootstrap.properties`

Both the **auth** and **clinic** services have a `bootstrap.properties` file, which must be configured with the correct Nacos namespace and server address:

```properties
# Nacos namespace configuration
spring.cloud.nacos.config.namespace=your_namespace_id
spring.cloud.nacos.config.server-addr=localhost:8848
```

Ensure that the `namespace` and `server-addr` values match your local Nacos setup.

#### 3.4 Modify `redisson.yml`

The **auth** service relies on Redis for caching, so you'll need to configure the `redisson.yml` file:

```yaml
singleServerConfig:
  address: "redis://127.0.0.1:6379"
  password: null  # Add your Redis password if applicable
  connectionPoolSize: 64
  connectionMinimumIdleSize: 24
```

### 4. Service Startup Order

It is **essential to start the `auth` service first**, followed by the `clinic` service. If you attempt to start the `clinic` service without the `auth` service running, it will result in errors due to unmet dependencies.

#### Start the `auth` Service

```bash
cd auth
mvn spring-boot:run
```

#### Start the `clinic` Service

Once the `auth` service is running, you can start the `clinic` service:

```bash
cd clinic
mvn spring-boot:run
```

### 5. Testing and Verification

After the services are up and running, you can access the clinic management system via the following URLs:

- **Frontend URL**: `http://localhost:8080`
- **Backend API URL**: `http://localhost:8081/api`

You may need to adjust the port numbers based on your specific setup.

---

## Code Contribution Guidelines (Criticisms and corrections are welcome)

We encourage community developers to actively participate in maintaining and improving the project. When contributing code, please follow the steps and commit message guidelines outlined below.

### 1. Steps to Submit Code

1. **Fork the project**: On GitHub, fork the repository to your personal account.
2. **Clone the repository**: Clone your forked repository to your local development environment:
   ```bash
   git clone https://github.com/yourGitHubAccount/clinic-management-system.git
   ```
3. **Create a new branch**: Each new feature or bug fix should be developed on a separate branch:
   ```bash
   git checkout -b feature/your-feature-name
   ```
4. **Commit your changes**: After making changes locally, commit them using the following command:
   ```bash
   git add .
   git commit -m "Brief description of your changes"
   ```
5. **Push to the remote repository**: Push your changes to your forked GitHub repository:
   ```bash
   git push origin feature/your-feature-name
   ```
6. **Create a Pull Request**: Open a Pull Request (PR) on GitHub, providing a brief description of the changes and their purpose.

### 2. Commit Message Guidelines

Please write commit messages in English and follow this format:

```
[Type]: [Brief Description]

- [Detailed changes]
- [Affected areas]
```

### Commit Types:

- **feat**: New feature
- **fix**: Bug fix
- **docs**: Documentation updates
- **style**: Code style changes (formatting, etc. with no code logic changes)
- **refactor**: Code refactoring
- **test**: Adding or updating tests
- **chore**: Miscellaneous changes (e.g., build process, dependencies)

---

## FAQ

### 1. Why do I need to start the `auth` service before the `clinic` service?

The `clinic` service depends on the `auth` service for authentication. If the `auth` service is not running, the `clinic` service will not function properly and will throw errors related to missing dependencies.

### 2. How do I upload configuration files to Nacos?

You can upload the `auth-local.yaml` and `clinic-local.yaml` files to Nacos using its management console. Make sure you upload these configurations to the correct namespace, and that your `bootstrap.properties` files are configured correctly.

### 3. What should I do if the services fail to start?

Ensure that MySQL, Nacos, and Redis are running and properly configured. Check the logs for any specific error messages and ensure that all configuration files have been updated with your local environment's settings.

