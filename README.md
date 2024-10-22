

# 诊所系统开源项目文档

欢迎来到码良科技的诊所管理系统开源项目！本项目旨在为中小型诊所提供高效的管理工具。通过现代化的技术栈（包括 Spring Boot、MyBatis Plus、Vue 3、Nacos 等），我们力求为用户打造一套稳定、灵活的诊所管理解决方案。

## Links
 [English Document](BE_English.md) 

[官网](https://www.maliang.work)  | [前端文档](FE_Chinese.md)


## 项目简介

本项目由后端的 **auth** 服务和 **clinic** 服务组成，这两个服务之间存在依赖关系。**auth** 服务主要负责认证和权限管理，**clinic** 服务是诊所系统的核心业务逻辑。服务间的通信使用 **Nacos** 作为配置中心和服务注册发现工具，同时依赖 **MySQL** 作为数据库，**Redis** 作为缓存系统。

### 技术栈

- **后端**: Spring Boot, MyBatis Plus, Redis
- **前端**: Vue 3, Element UI Plus
- **配置中心**: Nacos 2.3.2
- **数据库**: MySQL 8.0.22
- **缓存**: Redis

### 项目结构（包含图片展示）

您所提供的项目图片展示了项目的主要目录结构：

#### `auth` 服务结构：

```
auth/
├── src/main/java/...
├── src/main/resources/
│   ├── auth-local.yaml       # 需修改的本地配置文件
│   ├── bootstrap.properties   # Nacos 命名空间配置
│   ├── redisson.yml           # Redis 连接配置文件
│   └── auth.sql               # 数据库初始化脚本
└── ...
```

#### `clinic` 服务结构：

```
clinic/
├── src/main/java/...
├── src/main/resources/
│   ├── clinic-local.yaml      # 需修改的本地配置文件
│   ├── bootstrap.properties   # Nacos 命名空间配置
│   └── clinic.sql             # 数据库初始化脚本
└── ...
```

## 环境配置要求

在启动项目之前，请确保本地已正确安装并运行以下服务：

- **MySQL**: 用于存储诊所系统的数据
- **Nacos**: 用作配置中心和服务发现
- **Redis**: 用于缓存

## 快速启动指南

### 1. 克隆项目

首先，从 GitHub 上克隆项目的代码库：

```bash
git clone https://github.com/MaLiangKeJi/Ma-Liang-Clinic.git
```

### 2. 数据库初始化

项目中分别为 **auth** 服务和 **clinic** 服务提供了 SQL 脚本文件 (`auth.sql` 和 `clinic.sql`)，您需要在 MySQL 中执行这些脚本来创建数据库表和初始化数据。

```bash
# 登录到你的 MySQL 数据库并执行以下命令：
source /auth/resources/auth.sql
source /clinic/resources/clinic.sql
```

**注意**: 请根据您的本地 MySQL 环境，将脚本中的数据库名、用户名和密码等配置信息进行相应调整。

### 3. 修改配置文件

为了确保项目能够正确运行，需要对以下配置文件进行修改，以适配本地环境的 MySQL、Redis 和 Nacos 设置。

#### 3.1 `auth-local.yaml` 修改

这是 `auth` 服务的本地配置文件，您需要根据自己的 MySQL、Redis 和 Nacos 配置进行相应修改。将文件中的占位符 `xxxxxx` 替换为具体的值。

```yaml
# 在 auth-local.yaml 中替换 "xxxxxx" 为实际的值
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

**说明**:
- `datasource.url` 是 MySQL 的连接地址。
- `nacos.server-addr` 是 Nacos 服务地址，默认本地地址为 `localhost:8848`。
- `redis.host` 和 `redis.port` 是 Redis 连接信息。

#### 3.2 `clinic-local.yaml` 修改

同样，`clinic-local.yaml` 是 `clinic` 服务的本地配置文件，您也需要根据自己的本地环境进行配置修改。

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

#### 3.3 `bootstrap.properties` 修改

每个服务（`auth` 和 `clinic`）都有 `bootstrap.properties` 文件，需要配置 Nacos 的命名空间和服务地址：

```properties
# 配置 Nacos 命名空间
spring.cloud.nacos.config.namespace=your_namespace_id
spring.cloud.nacos.config.server-addr=localhost:8848
```

请确保 Nacos 的 `namespace` 和 `server-addr` 具体值与您的 Nacos 实例相对应。

#### 3.4 `redisson.yml` 修改

`auth` 服务依赖 Redis 缓存，因此 `redisson.yml` 文件中需要配置 Redis 连接信息：

```yaml
singleServerConfig:
  address: "redis://127.0.0.1:6379"
  password: null  # 如果您的 Redis 设置了密码，请在此处填写
  connectionPoolSize: 64
  connectionMinimumIdleSize: 24
```

### 4. 启动顺序

**请务必先启动 `auth` 服务，然后再启动 `clinic` 服务**，否则 `clinic` 服务会由于缺少认证而报错。

#### 启动 `auth` 服务

```bash
cd auth
mvn spring-boot:run
```

#### 启动 `clinic` 服务

当 `auth` 服务成功启动后，再启动 `clinic` 服务：

```bash
cd clinic
mvn spring-boot:run
```

### 5. 测试与验证

在服务启动后，您可以使用以下地址访问诊所系统的前端页面和后端 API：

- **前端访问地址**: `http://localhost:8080`
- **后端 API 地址**: `http://localhost:8081/api`

请根据实际情况调整端口号。

---

## 提交代码的步骤与规范（欢迎大家批评指正）

我们鼓励社区开发者积极参与项目的维护与改进。在贡献代码时，请确保遵循以下步骤和提交规范。

### 1. 提交代码的步骤

1. **Fork 项目**: 在 GitHub 上 fork 该仓库到您的个人账户。
2. **克隆仓库**: 将 fork 的仓库克隆到您的本地开发环境：
   ```bash
   git clone https://github.com/yourGitHubAccount/clinic-management-system.git
   ```
3. **创建新分支**: 每个新功能或 bug 修复都应在一个独立的分支上进行：
   ```bash
   git checkout -b feature/your-feature-name
   ```
4. **提交代码**: 在本地完成开发后，使用如下命令提交您的代码：
   ```bash
   git add .
   git commit -m "简洁描述您所作的更改"
   ```
5. **推送到远程仓库**: 将本地的修改推送到您的 fork 版本的 GitHub 仓库：
   ```bash
   git push origin feature/your-feature-name
   ```
6. **创建 Pull Request**: 提交 Pull Request，并简要说明您的更改目的和内容。

### 2. 提交规范

#### 提交信息格式：

请使用英文撰写提交信息，并遵循以下格式：

```
[类型]: [简短描述]

- [具体更改说明]
- [影响范围]
```

#### 提交类型：

- **feat**: 新功能
- **fix**: 修复 bug
- **docs**: 更新文档
- **style**: 代码风格改进（不影响代码功能）
- **refactor**: 代码重构
- **test**: 增加测试
- **chore**: 其他修改（如构建过程、依赖管理等）

---

## 常见问题 (FAQ)

### 1. 为什么需要按顺序启动 `auth` 和 `clinic` 服务？

`clinic` 服务依赖于 `auth` 服务提供的认证功能。如果 `auth` 服务未启动，`clinic` 服务将无法正确运行，并且会报缺少依赖的错误。

### 2. 如何在 Nacos 中上传配置文件？

您可以通过 Nacos 的管理控制台上传 `auth-local.yaml` 和 `clinic-local.yaml`。确保将这些文件上传到正确的命名空间，并在 `bootstrap.properties` 中正确配置 Nacos 的服务地址和命名空间。

