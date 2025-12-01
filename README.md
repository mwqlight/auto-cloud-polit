# Cloud Polit - 云资源管理平台

Cloud Polit 是一个基于 Spring Boot + Vue3 的云资源管理平台，支持插件化扩展，可统一管理计算、存储、网络、安全和监控等各类云资源。

## 功能特性

- **插件化架构**：支持动态加载和管理各类云资源插件
- **统一管理界面**：提供直观的 Web 界面管理所有云资源
- **3D可视化**：通过 3D 可视化展示云资源拓扑结构
- **权限控制**：基于 JWT 和 RBAC 的权限管理系统
- **RESTful API**：提供完整的 RESTful API 接口

## 技术栈

### 后端
- Spring Boot 3.0+
- Spring Security + JWT
- JPA/Hibernate
- MySQL 8.0
- Redis
- Maven

### 前端
- Vue3 + TypeScript
- Element Plus
- ECharts
- Three.js
- Vite
- Pinia

## 快速开始

### 环境要求
- JDK 17+
- Node.js 16+
- MySQL 8.0
- Redis

### 后端启动
```bash
cd auto-cloud-polit-backend
mvn spring-boot:run
```

### 前端启动
```bash
cd auto-cloud-polit-frontend
npm install
npm run dev
```

## 项目结构

### 后端结构
```
src/main/java/com/company/
├── config/         # 配置类
├── controller/     # 控制层
├── dto/            # 数据传输对象
│   ├── request/    # 请求DTO
│   └── response/   # 响应DTO
├── service/        # 业务逻辑
│   ├── impl/       # 实现类
│   └── Service.java # 接口
├── repository/     # 数据访问层
├── entity/         # 实体类
├── exception/      # 全局异常处理
├── util/           # 工具类
├── plugin/         # 插件管理
└── Application.java # 启动类
```

### 前端结构
```
src/
├── api/            # API接口封装
│   ├── modules/    # 按模块拆分
│   └── index.ts    # 统一出口
├── assets/         # 静态资源
├── components/     # 公共组件
│   ├── base/       # 基础组件
│   └── layout/     # 布局组件
├── composables/    # 组合式API
├── router/         # 路由配置
├── store/          # 状态管理
├── styles/         # 全局样式
├── types/          # TypeScript类型定义
├── utils/          # 工具函数
├── views/          # 页面视图
│   ├── dashboard/  # 驾驶舱
│   └── plugins/    # 插件管理
├── App.vue         # 根组件
└── main.ts         # 入口文件
```

## 插件系统

平台支持以下核心插件类型：
- 计算资源插件 (Compute)
- 存储资源插件 (Storage)
- 网络资源插件 (Network)
- 安全资源插件 (Security)
- 监控资源插件 (Monitoring)

每个插件可以定义自己的操作和仪表盘部件。

## 开发规范

本项目遵循 5A6S 开发规范：
- **5A原则**：Architecture(架构)、API(接口)、Automation(自动化)、Assurance(质量保障)、Agility(敏捷协作)
- **6S标准**：Structure(结构)、Standards(标准)、Security(安全)、Stability(稳定性)、Scalability(可扩展性)、Sustainability(可持续维护)

## 许可证

[MIT License](LICENSE)
