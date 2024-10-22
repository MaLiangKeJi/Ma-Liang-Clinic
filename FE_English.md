

# Frontend Documentation for Ma Liang Clinic Management System

## Overview

The frontend of the Ma Liang Clinic Management System is built using **Vue 3** and is located in the `web` directory at the root of the project. This section of the project handles the user interface for clinic management and interacts with the backend services via API calls.

Before you can run the frontend, ensure that you have the correct **Node.js** version installed, which for this project is **Node.js v18.17.1**. Please verify that your environment is compatible with this version to avoid any potential issues.

---


## Links

[中文文档](FE_Chinese.md)

[Back-end documentation](BE_English.md) | [Official Website](https://www.maliang.work)


## Prerequisites

1. **Node.js Version**: Ensure that you are using **Node.js v18.17.1** or a compatible version.
    - You can check your Node.js version by running:
      ```bash
      node -v
      ```

    - If you don’t have the correct version, download and install it from the [official Node.js website](https://nodejs.org).

2. **Package Manager**: The project uses **npm** for managing dependencies. If you don't have npm installed, it comes bundled with Node.js, so installing Node.js should also install npm.

---

## Project Structure (Frontend)

The frontend is contained in the `web` folder within the root of the project:

```
web/
├── public/                   # Public assets (images, icons, etc.)
├── src/                      # Main source code
├── .env.development           # Environment variables for development
├── .env.production            # Environment variables for production
├── Dockerfile                 # Docker configuration for frontend deployment
├── index.html                 # HTML entry point for the Vue application
├── jsconfig.json              # JavaScript configuration
├── LICENSE                    # Project license
├── nginx.conf                 # NGINX configuration for serving the app
├── package.json               # Project dependencies and scripts
├── pnpm-lock.yaml             # Dependency lock file (for pnpm users)
├── README.en.md               # README for English documentation
├── README.md                  # README for general project information
├── yarn.lock                  # Dependency lock file (for yarn users)
```

---

## Setup Instructions

### 1. Navigate to the Frontend Directory

Open a terminal and navigate to the `web` folder in the root of the project.

```bash
cd web
```

### 2. Install Dependencies

Once inside the `web` directory, run the following command to install all the necessary dependencies:

```bash
npm install
```

This command will download and install all the required packages defined in the `package.json` file.

### 3. Run the Development Server

After the dependencies are installed, you can start the development server by running:

```bash
npm run dev
```

This command will start the Vue.js development server. You should see output similar to:

```bash
  VITE vX.X.X  ready in XXX ms

  ➜  Local:   http://localhost:3000/
```

### 4. Access the Application

Once the development server is running, open a browser and navigate to `http://localhost:3000` to access the frontend of the clinic management system.

### 5. Build for Production (Optional)

If you want to build the application for production, you can use the following command:

```bash
npm run build
```

This will generate optimized static files in the `dist` directory, ready to be deployed on a production server.

---

## Docker Support

The frontend project is Dockerized, allowing for easy deployment. If you want to run the frontend in a Docker container, follow these steps:

1. Ensure that **Docker** is installed on your machine.
2. Build the Docker image:
   ```bash
   docker build -t ma-liang-clinic-frontend .
   ```
3. Run the Docker container:
   ```bash
   docker run -p 80:80 ma-liang-clinic-frontend
   ```
4. Access the frontend through `http://localhost`.

---

## Common Issues

### 1. Node.js Version Mismatch

If your Node.js version is not **v18.17.1**, you might encounter errors during dependency installation or when running the application. You can use **nvm (Node Version Manager)** to install and switch between different Node.js versions:

```bash
nvm install 18.17.1
nvm use 18.17.1
```

### 2. Port Conflict

By default, the development server runs on port **80**. If port **80** is already in use on your machine, you can specify a different port by editing the `package.json` or using the following command:

```bash
npm run dev -- --port 8080
```

This will start the development server on port **8080** instead.

---

## Conclusion

With the above steps, you should be able to set up and run the frontend of the Ma Liang Clinic Management System. If you encounter any issues, please refer to the **Common Issues** section, or check the project's [GitHub issues page](https://github.com/yourGitHubAccount/clinic-management-system/issues) for more assistance.

