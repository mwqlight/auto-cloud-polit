# API 文档

## 认证

所有 API 请求都需要通过 JWT Token 认证，除了登录接口。

### 登录

```http
POST /api/auth/login
```

**请求体:**
```json
{
  "username": "string",
  "password": "string"
}
```

**响应:**
```json
{
  "code": 200,
  "message": "登录成功",
  "data": {
    "token": "string",
    "user": {
      "id": "integer",
      "username": "string",
      "roles": ["string"]
    }
  },
  "timestamp": "long"
}
```

## 插件管理

### 获取所有插件组件

```http
GET /api/plugins/components
```

**响应:**
```json
{
  "code": 200,
  "message": "操作成功",
  "data": [
    {
      "id": "string",
      "name": "string",
      "type": "string"
    }
  ],
  "timestamp": "long"
}
```

### 获取插件组件详情

```http
GET /api/plugins/components/{id}
```

**路径参数:**
- `id` - 插件组件ID

**响应:**
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "id": "string",
    "name": "string",
    "type": "string",
    "operations": [
      {
        "id": "string",
        "name": "string",
        "description": "string"
      }
    ],
    "dashboardWidgets": [
      {
        "id": "string",
        "name": "string",
        "type": "string",
        "config": {}
      }
    ]
  },
  "timestamp": "long"
}
```

### 执行插件操作

```http
POST /api/plugins/components/{id}/operations/{operationId}
```

**路径参数:**
- `id` - 插件组件ID
- `operationId` - 操作ID

**请求体:**
```json
{
  "parameters": {}
}
```

**响应:**
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {},
  "timestamp": "long"
}
```

## 计算资源

### 获取计算实例列表

```http
GET /api/compute/instances
```

**查询参数:**
- `page` - 页码 (可选)
- `size` - 每页大小 (可选)

**响应:**
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "items": [
      {
        "id": "string",
        "name": "string",
        "status": "string",
        "type": "string",
        "region": "string"
      }
    ],
    "total": "integer"
  },
  "timestamp": "long"
}
```

### 创建计算实例

```http
POST /api/compute/instances
```

**请求体:**
```json
{
  "name": "string",
  "type": "string",
  "region": "string",
  "imageId": "string"
}
```

**响应:**
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "id": "string",
    "name": "string",
    "status": "string",
    "type": "string",
    "region": "string"
  },
  "timestamp": "long"
}
```

### 删除计算实例

```http
DELETE /api/compute/instances/{id}
```

**路径参数:**
- `id` - 实例ID

**响应:**
```json
{
  "code": 200,
  "message": "操作成功",
  "data": null,
  "timestamp": "long"
}
```

## 存储资源

### 获取存储桶列表

```http
GET /api/storage/buckets
```

**查询参数:**
- `page` - 页码 (可选)
- `size` - 每页大小 (可选)

**响应:**
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "items": [
      {
        "id": "string",
        "name": "string",
        "region": "string",
        "size": "long"
      }
    ],
    "total": "integer"
  },
  "timestamp": "long"
}
```

## 网络资源

### 获取网络列表

```http
GET /api/network/networks
```

**查询参数:**
- `page` - 页码 (可选)
- `size` - 每页大小 (可选)

**响应:**
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "items": [
      {
        "id": "string",
        "name": "string",
        "cidr": "string",
        "region": "string"
      }
    ],
    "total": "integer"
  },
  "timestamp": "long"
}
```

## 安全资源

### 获取安全组列表

```http
GET /api/security/groups
```

**查询参数:**
- `page` - 页码 (可选)
- `size` - 每页大小 (可选)

**响应:**
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "items": [
      {
        "id": "string",
        "name": "string",
        "description": "string"
      }
    ],
    "total": "integer"
  },
  "timestamp": "long"
}
```

## 监控资源

### 获取监控指标

```http
GET /api/monitoring/metrics
```

**查询参数:**
- `resourceId` - 资源ID
- `metricName` - 指标名称
- `startTime` - 开始时间 (时间戳)
- `endTime` - 结束时间 (时间戳)

**响应:**
```json
{
  "code": 200,
  "message": "操作成功",
  "data": [
    {
      "timestamp": "long",
      "value": "double"
    }
  ],
  "timestamp": "long"
}
```