

# 码良诊所管理系统前端文档

## 概述

码良诊所管理系统的前端部分基于 **Vue 3** 框架构建，前端项目位于项目根目录下的 `web` 文件夹中。此部分负责诊所系统的用户界面，并通过 API 与后端服务进行交互。

在开始运行前端项目之前，请确保您的环境中已经安装了正确的 **Node.js** 版本。该项目使用的 **Node.js 版本为 v18.17.1**，请确认您的环境与此版本兼容。

---

## Links
[English Document](FE_English.md)

[官网](https://www.maliang.work)  | [后端文档](README.md)


## 运行前准备

1. **Node.js 版本**：请确保您已安装 **Node.js v18.17.1** 或与之兼容的版本。
    - 您可以通过以下命令检查 Node.js 版本：
      ```bash
      node -v
      ```

    - 如果您的 Node.js 版本不符，请前往 [Node.js 官方网站](https://nodejs.org) 下载并安装正确的版本。

2. **包管理工具**：项目使用 **npm** 来管理依赖包。npm 会随 Node.js 一起安装，因此确保 Node.js 安装成功后，npm 也会安装。

---

## 项目结构（前端）

前端代码位于根目录的 `web` 文件夹中，具体结构如下：

```
web/
├── public/                   # 静态资源文件（图片、图标等）
├── src/                      # 核心源码
├── .env.development           # 开发环境变量
├── .env.production            # 生产环境变量
├── Dockerfile                 # 前端 Docker 部署配置
├── index.html                 # Vue 应用入口 HTML 文件
├── jsconfig.json              # JavaScript 配置
├── LICENSE                    # 项目许可证
├── nginx.conf                 # NGINX 配置文件
├── package.json               # 项目依赖和脚本
├── pnpm-lock.yaml             # pnpm 依赖锁定文件（如果使用 pnpm）
├── README.en.md               # 英文版说明文档
├── README.md                  # 项目说明文档
├── yarn.lock                  # yarn 依赖锁定文件（如果使用 yarn）
```

---

## 前端项目启动步骤

### 1. 进入前端目录

首先，打开终端并导航到项目根目录下的 `web` 文件夹：

```bash
cd web
```

### 2. 安装依赖

在 `web` 目录下运行以下命令，安装项目所需的依赖包：

```bash
npm install
```

此命令会根据 `package.json` 文件中的依赖信息，自动下载并安装所有依赖包。

### 3. 启动开发服务器

依赖安装完成后，您可以通过以下命令启动前端开发服务器：

```bash
npm run dev
```

运行该命令后，终端会输出类似如下的信息：

```bash
  VITE vX.X.X  ready in XXX ms

  ➜  Local:   http://localhost:3000/
```

### 4. 访问前端页面

开发服务器启动成功后，您可以在浏览器中访问 `http://localhost:3000` 来查看前端页面。

### 5. 生产环境构建（可选）

如果需要将应用程序打包部署到生产环境，可以运行以下命令：

```bash
npm run build
```

该命令会在 `dist` 文件夹中生成优化后的静态文件，准备在生产环境中部署。

---

## Docker 支持

前端项目已经 Docker 化，可以通过 Docker 容器来部署前端。执行以下步骤来构建和运行 Docker 容器：

1. 确保您的系统已经安装了 **Docker**。
2. 构建 Docker 镜像：
   ```bash
   docker build -t ma-liang-clinic-frontend .
   ```
3. 运行 Docker 容器：
   ```bash
   docker run -p 80:80 ma-liang-clinic-frontend
   ```
4. 打开浏览器，访问 `http://localhost` 查看前端页面。

---

## 常见问题

### 1. Node.js 版本不兼容

如果您没有安装 **Node.js v18.17.1** 版本，可能会在依赖安装或运行项目时遇到问题。建议使用 **nvm（Node 版本管理器）** 来安装和切换不同的 Node.js 版本：

```bash
nvm install 18.17.1
nvm use 18.17.1
```

### 2. 端口冲突

默认情况下，开发服务器会在 **80 端口** 上运行。如果该端口已经被其他服务占用，您可以通过以下命令指定一个不同的端口运行开发服务器：

```bash
npm run dev -- --port 8080
```

这样开发服务器将会在 **8080 端口** 上运行。

---

## 总结

通过以上步骤，您应该能够成功设置并运行 码良诊所管理系统的前端项目。如果在安装或运行过程中遇到任何问题，请参阅上面的 **常见问题** 部分，或访问项目的 [GitHub Issues 页面](https://github.com/yourGitHubAccount/clinic-management-system/issues) 获取更多帮助。

