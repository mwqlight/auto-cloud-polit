# 部署文档

## 环境要求

### 后端环境
- JDK 17 或更高版本
- MySQL 8.0
- Redis 6.0 或更高版本
- Maven 3.8 或更高版本

### 前端环境
- Node.js 16 或更高版本
- npm 8 或更高版本

## 数据库配置

### MySQL 初始化

1. 创建数据库:
```sql
CREATE DATABASE cloud_polit CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

2. 创建用户并授权:
```sql
CREATE USER 'cloud_polit'@'localhost' IDENTIFIED BY 'password';
GRANT ALL PRIVILEGES ON cloud_polit.* TO 'cloud_polit'@'localhost';
FLUSH PRIVILEGES;
```

3. 执行数据库脚本:
```bash
mysql -u cloud_polit -p cloud_polit < DATABASE_DOCUMENTATION.md
```

## 后端部署

### 1. 配置文件

在 `src/main/resources/application.yml` 中配置数据库和Redis连接:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/cloud_polit?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai
    username: cloud_polit
    password: password
  redis:
    host: localhost
    port: 6379
    password: 
  
server:
  port: 8080
```

### 2. 构建项目

```bash
cd auto-cloud-polit-backend
mvn clean package
```

### 3. 运行应用

```bash
java -jar target/auto-cloud-polit-backend-0.0.1-SNAPSHOT.jar
```

或者使用 Maven 运行:

```bash
mvn spring-boot:run
```

### 4. Docker 部署 (可选)

创建 Dockerfile:

```dockerfile
FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/auto-cloud-polit-backend-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
```

构建和运行:

```bash
docker build -t cloud-polit-backend .
docker run -p 8080:8080 cloud-polit-backend
```

## 前端部署

### 1. 安装依赖

```bash
cd auto-cloud-polit-frontend
npm install
```

### 2. 构建项目

```bash
npm run build
```

构建后的文件位于 `dist` 目录。

### 3. 运行开发服务器

```bash
npm run dev
```

### 4. 生产环境部署

将 `dist` 目录中的文件部署到 Web 服务器 (如 Nginx、Apache)。

### 5. Nginx 配置示例

```nginx
server {
    listen 80;
    server_name your-domain.com;
    
    location / {
        root /path/to/dist;
        index index.html;
        try_files $uri $uri/ /index.html;
    }
    
    location /api {
        proxy_pass http://localhost:8080;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
    }
}
```

## 环境变量配置

### 后端环境变量

在 `application.yml` 中配置:

```yaml
jwt:
  secret: your-jwt-secret
  expiration: 86400000 # 24小时

plugin:
  directory: ./plugins
```

### 前端环境变量

在 `.env.production` 中配置:

```env
VITE_API_BASE_URL=/api
VITE_APP_TITLE=Cloud Polit
```

## 监控和日志

### 日志配置

后端使用 Logback 进行日志记录，配置文件位于 `src/main/resources/logback-spring.xml`。

### 监控端点

Spring Boot Actuator 提供了以下监控端点:

- `/actuator/health` - 健康检查
- `/actuator/info` - 应用信息
- `/actuator/metrics` - 系统指标
- `/actuator/loggers` - 日志配置

## 故障排除

### 常见问题

1. **数据库连接失败**
   - 检查数据库服务是否启动
   - 检查数据库连接配置是否正确
   - 检查防火墙设置

2. **Redis连接失败**
   - 检查Redis服务是否启动
   - 检查Redis连接配置是否正确

3. **前端无法访问API**
   - 检查后端服务是否启动
   - 检查跨域配置
   - 检查Nginx代理配置

### 日志查看

```bash
# 后端日志
tail -f logs/spring.log

# 前端日志
# 在浏览器开发者工具中查看控制台输出
```