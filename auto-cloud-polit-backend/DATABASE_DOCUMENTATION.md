# 数据库文档

## 概述

Cloud Polit 使用 MySQL 8.0 作为主数据库，存储用户信息、权限信息和插件配置等核心数据。

## 数据库设计

### 用户表 (users)

存储系统用户信息。

```sql
CREATE TABLE users (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  username VARCHAR(50) NOT NULL UNIQUE,
  password VARCHAR(255) NOT NULL,
  email VARCHAR(100),
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

**字段说明:**
- `id` - 用户ID，主键，自增
- `username` - 用户名，唯一，不能为空
- `password` - 密码，不能为空
- `email` - 邮箱地址
- `created_at` - 创建时间，默认当前时间
- `updated_at` - 更新时间，默认当前时间，更新时自动刷新

### 角色表 (roles)

存储系统角色信息。

```sql
CREATE TABLE roles (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(50) NOT NULL UNIQUE,
  description VARCHAR(255),
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

**字段说明:**
- `id` - 角色ID，主键，自增
- `name` - 角色名，唯一，不能为空
- `description` - 角色描述
- `created_at` - 创建时间，默认当前时间

### 用户角色关联表 (user_roles)

存储用户和角色的多对多关系。

```sql
CREATE TABLE user_roles (
  user_id BIGINT NOT NULL,
  role_id BIGINT NOT NULL,
  PRIMARY KEY (user_id, role_id),
  FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
  FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE
);
```

**字段说明:**
- `user_id` - 用户ID，外键关联users表
- `role_id` - 角色ID，外键关联roles表

### 权限表 (permissions)

存储系统权限信息。

```sql
CREATE TABLE permissions (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(100) NOT NULL UNIQUE,
  description VARCHAR(255),
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

**字段说明:**
- `id` - 权限ID，主键，自增
- `name` - 权限名，唯一，不能为空
- `description` - 权限描述
- `created_at` - 创建时间，默认当前时间

### 角色权限关联表 (role_permissions)

存储角色和权限的多对多关系。

```sql
CREATE TABLE role_permissions (
  role_id BIGINT NOT NULL,
  permission_id BIGINT NOT NULL,
  PRIMARY KEY (role_id, permission_id),
  FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE,
  FOREIGN KEY (permission_id) REFERENCES permissions(id) ON DELETE CASCADE
);
```

**字段说明:**
- `role_id` - 角色ID，外键关联roles表
- `permission_id` - 权限ID，外键关联permissions表

### 插件表 (plugins)

存储插件信息。

```sql
CREATE TABLE plugins (
  id VARCHAR(100) PRIMARY KEY,
  name VARCHAR(100) NOT NULL,
  type VARCHAR(50) NOT NULL,
  version VARCHAR(20),
  description TEXT,
  enabled BOOLEAN DEFAULT TRUE,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

**字段说明:**
- `id` - 插件ID，主键
- `name` - 插件名称，不能为空
- `type` - 插件类型，不能为空
- `version` - 插件版本
- `description` - 插件描述
- `enabled` - 是否启用，默认为TRUE
- `created_at` - 创建时间，默认当前时间
- `updated_at` - 更新时间，默认当前时间，更新时自动刷新

## 初始化数据

### 默认角色

```sql
INSERT INTO roles (name, description) VALUES 
('ADMIN', '系统管理员'),
('USER', '普通用户');
```

### 默认权限

```sql
INSERT INTO permissions (name, description) VALUES 
('PLUGIN_MANAGE', '插件管理'),
('COMPUTE_CREATE', '创建计算实例'),
('COMPUTE_DELETE', '删除计算实例'),
('STORAGE_CREATE', '创建存储桶'),
('STORAGE_DELETE', '删除存储桶');
```

### 管理员角色权限

```sql
INSERT INTO role_permissions (role_id, permission_id) 
SELECT r.id, p.id FROM roles r, permissions p 
WHERE r.name = 'ADMIN';
```

### 默认管理员用户

```sql
INSERT INTO users (username, password, email) VALUES 
('admin', '$2a$10$abcdefghijklmnopqrstuvABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789', 'admin@example.com');

INSERT INTO user_roles (user_id, role_id) 
SELECT u.id, r.id FROM users u, roles r 
WHERE u.username = 'admin' AND r.name = 'ADMIN';
```