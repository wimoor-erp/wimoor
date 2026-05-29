# Wimoor 容器化测试指南

## 概述

本文档描述 Wimoor 微服务项目的完整测试方法。**所有测试必须在 Docker 容器环境中进行**，严禁在宿主机（macOS/Linux）上直接运行 Java 服务进行测试。

## 环境要求

- Docker Desktop（macOS）或 Docker Engine（Linux），推荐分配 ≥ 8GB 内存
- Maven 3.8+，JDK 17（仅用于编译，不在宿主机运行服务）
- Node.js 18+（前端开发服务器）

## 1. 基础设施启动

```bash
cd deploy
docker compose up -d
```

启动的服务：
| 服务 | 端口 | 说明 |
|------|------|------|
| wimoor-mysql | 3306 | MySQL 8.0，密码 123456 |
| wimoor-redis | 6379 | Redis 7.0，密码 123456 |
| wimoor-nacos | 8848 | Nacos v2.3.0 |
| wimoor-gateway | 8099 | 网关 |
| wimoor-admin | 8100 | 用户/权限服务 |
| wimoor-amazon | 8102 | Amazon 服务 |
| wimoor-amazon-adv | 8103 | Amazon 广告服务 |
| wimoor-data | 8104 | 数据服务 |
| wimoor-finance | 8106 | 财务服务 |

## 2. 单服务构建与部署

### 2.1 编译打包

```bash
# 在项目根目录执行
# 替换 MODULE 为目标模块路径
mvn -pl wimoor-erp/erp-boot -am -DskipTests package -q
```

### 2.2 构建 Docker 镜像

```bash
# 复制 jar 到标准位置
cp wimoor-erp/erp-boot/target/erp-boot.jar deploy/app.jar

# 构建镜像（使用统一 Dockerfile）
docker build -f deploy/Dockerfile.service -t wimoor-erp:latest .
```

### 2.3 启动容器

```bash
docker rm -f wimoor-erp 2>/dev/null

docker run -d --name wimoor-erp \
  --network deploy_wimoor-net \
  -p 8101:8101 \
  -e NACOS_SERVER_ADDR=wimoor-nacos:8848 \
  -e NACOS_NAMESPACE=public \
  -e APP_NAME=wimoor-erp \
  -e JAVA_OPTS="-Xms256m -Xmx768m \
    --add-opens=java.base/java.lang=ALL-UNNAMED \
    --add-opens=java.base/java.util=ALL-UNNAMED \
    --add-opens=java.base/java.lang.reflect=ALL-UNNAMED \
    --add-opens=java.base/java.util.concurrent=ALL-UNNAMED" \
  wimoor-erp:latest
```

### 2.4 验证启动

```bash
# 等待 60-90 秒后检查
docker inspect wimoor-erp --format '{{.State.Status}} restarts={{.RestartCount}}'
# 期望: running restarts=0

# 检查 Nacos 注册
curl -s "http://localhost:8848/nacos/v1/ns/instance/list?serviceName=wimoor-erp" | python3 -m json.tool

# 端口探活
curl -s -o /dev/null -w "%{http_code}" http://localhost:8101/erp/api/v1/material/media/pool
# 期望: 400（参数缺失）或 200
```

## 3. 排障方法

### 3.1 容器启动后立即退出

```bash
# 查看退出码和重启次数
docker inspect wimoor-erp --format '{{.State.Status}} exit={{.State.ExitCode}} restarts={{.RestartCount}}'

# 方法1: 查看 docker logs（只有 bootstrap 阶段日志）
docker logs wimoor-erp 2>&1 | tail -50

# 方法2: 交互式启动，捕获全部输出（推荐）
docker rm -f wimoor-erp
docker run --rm --name wimoor-erp-debug \
  --network deploy_wimoor-net \
  -p 8101:8101 \
  -e NACOS_SERVER_ADDR=wimoor-nacos:8848 \
  -e NACOS_NAMESPACE=public \
  -e APP_NAME=wimoor-erp \
  -e JAVA_OPTS="-Xms128m -Xmx512m \
    --add-opens=java.base/java.lang=ALL-UNNAMED \
    --add-opens=java.base/java.util=ALL-UNNAMED \
    --add-opens=java.base/java.lang.reflect=ALL-UNNAMED \
    --add-opens=java.base/java.util.concurrent=ALL-UNNAMED" \
  --entrypoint sh wimoor-erp:latest \
  -c "export NACOS_IP=\$(hostname -i) && java \$JAVA_OPTS -jar /app/app.jar 2>&1; echo EXIT=\$?"
```

### 3.2 内存不足（OOM Kill）

```bash
# 检查所有容器内存使用
docker stats --no-stream --format "table {{.Name}}\t{{.MemUsage}}"

# 停止不需要的容器释放内存
docker stop <不相关容器名>

# 调低 JVM 堆内存（-Xmx）
```

### 3.3 查看运行时日志

```bash
# logback 将日志写入容器内文件
docker exec wimoor-erp cat /logs/wimoor-erp/log.log | tail -100

# 持续跟踪
docker exec wimoor-erp tail -f /logs/wimoor-erp/log.log
```

## 4. API 测试

### 4.1 登录获取 Session

```bash
# 通过 Gateway 登录
TOKEN=$(curl -s -X POST http://localhost:8099/admin/api/v1/oauth/token \
  -H "Content-Type: application/json" \
  -d '{"account":"admin@wimoor.com","password":"wimoor123"}' \
  | python3 -c "import json,sys;print(json.load(sys.stdin).get('data',{}).get('token',''))")

echo "Token: $TOKEN"
```

### 4.2 调用 API

```bash
# 通过 Gateway（推荐，模拟真实场景）
curl -H "Authorization: $TOKEN" http://localhost:8099/erp/api/v1/material/media/pool?shopid=xxx

# 直连服务端口（绕过鉴权，快速验证）
curl http://localhost:8101/erp/api/v1/material/media/pool?shopid=xxx
```

### 4.3 文件上传测试

```bash
curl -X POST http://localhost:8099/erp/api/v1/material/media/upload \
  -H "Authorization: $TOKEN" \
  -F "file=@test-image.jpg" \
  -F "shopId=xxx" \
  -F "mediaType=IMAGE" \
  -F "usageType=PRODUCT"
```

## 5. 前端测试

```bash
cd wimoorui
npm install
npm run dev
# 访问 http://localhost:8084
# Vite 自动代理 API 请求到 http://localhost:8099
```

## 6. 数据库操作

```bash
# 连接容器内 MySQL
docker exec -it wimoor-mysql mysql -uroot -p123456

# 或从宿主机连接
mysql -h127.0.0.1 -P3306 -uroot -p123456

# 执行 SQL 文件
docker exec -i wimoor-mysql mysql -uroot -p123456 db_erp < deploy/mysql-init/xxx.sql
```

## 7. 禁止事项

| 禁止 | 原因 | 正确做法 |
|------|------|----------|
| 在宿主机 `java -jar xxx.jar` | 环境不一致，依赖容器网络 | 使用 Docker 容器运行 |
| `sudo mkdir -p /logs` 在宿主机 | 污染宿主系统，无法复现 | 修复 Dockerfile 添加目录 |
| 使用 OS 绝对路径配置 | 不可移植，容器/CI 不兼容 | 使用相对路径或容器内约定路径 |
| 直接修改运行中容器文件系统 | 重启后丢失 | 修改源码重新构建镜像 |
| 用 `--restart=always` 掩盖启动错误 | 隐藏真实问题 | 先用 `--rm` 交互式排障 |

## 8. 已知问题与解决方案

### Druid log4j 依赖缺失

**现象**: `NoClassDefFoundError: org/apache/log4j/Priority`

**原因**: `druid.properties` 中 `druid.filters=stat,log4j`，但 erp-boot jar 缺少 `log4j-1.2.17.jar`

**解决**: 将 `druid.filters` 改为 `stat,slf4j`（文件位置: `wimoor-common/common-mybatis/src/main/resources/druid.properties`）

### Docker Desktop 内存限制

**现象**: 容器启动后被立即杀死，日志只有 20-30 行

**原因**: Docker VM 总内存（默认 7-8GB）被多个服务占满

**解决**: `docker stats` 检查内存使用，停止不需要的容器，或在 Docker Desktop Settings 中增加内存分配
