# AGENTS.md

本文件面向后继 coding agent。只记录 Wimoor 项目里不读代码就容易不知道的约定、陷阱和操作入口。

## 开始前

1. 同时检查两个 Git 工作区：
   - 后端根仓库：`git status --short`
   - 前端子仓库：`git -C wimoorui status --short`
2. 不要把 `target/`、`dist/`、`node_modules/`、编译后的 class 当作源码依据，除非任务明确要求处理打包产物。
3. 如果存在 `docs/00-index-and-reading-guide.md` 和 `docs/01-system-overview.md`，先读它们建立业务地图；最终仍以代码、SQL 和配置为准。

## 仓库结构

- 根目录是 Maven 多模块 Spring Cloud 后端，父 POM 是 `pom.xml`。
- `wimoorui/` 是前端目录，**已直接纳入本仓库管理**（非独立 Git 子仓库/submodule），与 `wimoor-admin` 等后端模块处于同一 Git 历史中。修改前端直接在此目录操作并提交到主仓库即可。
  - 被忽略的运行时/构建产物：`wimoorui/node_modules/`、`wimoorui/dist/`、`wimoorui/dist-ssr/`、`wimoorui/*.local`。
- 主要模块边界：
  - `wimoor-gateway`：网关与鉴权过滤器。
  - `wimoor-admin`：用户、角色、菜单、权限、Quartz 任务中心。
  - `wimoor-erp`：供应链、产品、采购、库存、仓库、发货。
  - `wimoor-amazon`：Amazon Listing、订单、入库、财务、报表、SP-API、SQS 通知。
  - `wimoor-amazon-adv`：Amazon 广告、报表、快照、调度。
  - `wimoor-modules/wimoor-finance`：凭证、账簿、期间、报表、财务编码。
  - `wimoor-modules/wimoor-quote`：供应商报价与物流报价。
  - `wimoor-api`：跨服务 Feign 契约。
  - `wimoor-common`：Result、用户上下文、MVC 过滤器、Redis、MyBatis、Swagger、存储、飞书等公共能力。

## 运行拓扑

本地端口和 context path 以各模块的 `bootstrap-dev.yml` 为准：

| 服务 | 端口 | Context path | Spring application name |
| --- | ---: | --- | --- |
| Gateway | 8099 | 无 | `wimoor-gateway` |
| Admin | 8100 | `/admin` | `wimoor-admin` |
| ERP | 8101 | `/erp` | `wimoor-erp` |
| Amazon | 8102 | `/amazon` | `wimoor-amazon` |
| Amazon Adv | 8103 | `/amazonadv` | `wimoor-amazon-adv` |
| Data | 8104 | `/mdata` | `wimoor-data` |
| Quote | 8105 | `/quote` | `wimoor-quote` |
| Finance | 8106 | `/finance` | `wimoor-finance` |
| Code Gen | 8107 | `/code` | `wimoor-gen` |

`configs/gateway.yaml` 更像独立网关配置，里面 `quote` 和 `code` 的默认 target 当前是 `8500`、`8800`，与 Java `bootstrap-dev.yml` 不一致。使用它时要显式覆盖或确认实际启动方式。

运行依赖通常包括 MySQL 8、Redis、Nacos、Seata。Nacos 种子配置在 `init-config/nacos/DEFAULT_GROUP/`；库表结构和初始化数据在 `init-config/mysql/数据库结构/`、`init-config/mysql/数据/`。

## 后端约定

- 根 POM 和 README 声明 Java 8。部分新模块代码可能已经出现更高版本 API，做大范围构建前先确认本地 JDK；除非明确升级模块基线，不要继续引入 Java 9+ API。
- 不要做跨模块风格清理。项目混用 MyBatis Plus、tk.mybatis Mapper、XML Mapper、RuoYi 风格工具类，优先跟随所在模块的写法。
- Controller 通常在服务内部写 `/api/v1/...`，对前端暴露时还要带服务 context path，例如 `/erp/api/v1/material/list`。
- 登录态使用 `jsessionid`，不是 Bearer token。Gateway 从 Redis 的 `login_tokens:` 前缀读取会话，并注入 URL 编码后的 `X-USERINFO`；业务服务通过 `UserInfoContext.get()` 获取用户。
- Feign 调用依赖 `common-mvc` 的 `FeignConfiguration` 传播 `X-USERINFO` 和 Seata XID。不要随意改成裸 HTTP，除非保留这两个传播行为。
- Gateway URL 鉴权规则来自 Redis hash `system:perm_roles_rule:url:`，key 形如 `GET:/erp/api/v1/...`。`admin` 和 `manager` 用户类型绕过角色匹配。
- 常见 Result 有两套：
  - `com.wimoor.common.result.Result`：数字 `code`，成功为 `200`；匹配 `wimoorui/src/utils/request.js`。
  - `com.wimoor.common.core.domain.Result`：字符串 `code`，成功为 `"201"`；旧上传/导入 helper 仍会检查它。
  给普通 `request.js` 消费的新接口优先用数字 `200` 风格，除非同控制器已有旧契约。
- 用户可见的写操作如果邻近代码使用 `@SystemControllerLog`，新增方法也要按同模块习惯加上，便于操作日志完整。
- 很多列表接口使用前端字段 `currentpage`、`pagesize`，返回 `Result.data = { records, total }`。

## 后端构建检查

- 优先编译被改服务及其依赖：
  - `mvn -pl wimoor-erp/erp-boot -am -DskipTests package`
  - `mvn -pl wimoor-amazon/amazon-boot -am -DskipTests package`
  - `mvn -pl wimoor-modules/wimoor-finance -am -DskipTests package`
- 根目录全量构建可能较慢，也可能依赖模块 POM 中安装的本地第三方 jar。除非改动跨多个服务，优先用 `-pl ... -am`。
- 当前 checkout 里可靠测试较少。行为改动能加窄测试就加；没有现成测试模式时，至少编译相关模块，并在交付说明里写清未运行项。

## 前端 `wimoorui`

- `wimoorui/` 已作为普通目录纳入主仓库，**不再是独立 Git 项目**。前端改动直接 `git add wimoorui/...` 后与后端同步提交。
- 技术栈：Vite、Vue 3、Vue Router 4、Vuex 4、Element Plus、ECharts、Axios、Sass、SVG icon plugin。
- 前端命令都在 `wimoorui/` 下执行：
  - `npm install`
  - `npm run dev`
  - `npm run build`
- 开发端口默认 `8084`，`vite.config.js` 把 API 前缀代理到 `http://localhost:8099`。
- `vite.config.js` 里 `@` 指向 `wimoorui/src`，`~` 指向 `wimoorui` 根。
- Router 实际使用 `createWebHistory()`；文件里残留的 `mode: 'hash'` 不生效，不要按 hash 路由假设处理。
- 大部分菜单页是动态路由。`src/router/index.js` 启动时请求 `/api/admin/api/v1/menus/route`，写入 Vuex，然后用 `import.meta.glob('../views/**/*.vue')` 根据后端菜单的 `component` 字段 `addRoute()`。新增菜单页时，后端菜单 `component` 必须能匹配 `src/views` 下的 `.vue` 路径。
- 独立流程页放在 `src/router/modules/*.js`，例如登录、报价、发货向导、授权回调等。
- 布局缓存依赖 `route.query.title`。`layout/index.vue` 会用这个 title 生成 keep-alive wrapper 的组件名；缺少稳定 title 会影响标签页刷新和缓存清理。
- 权限指令：
  - `v-hasPerm="'erp:wh:shelf:add'"` 检查单个权限字符串。
  - `v-hasPermi="['a:b:c']"` 检查数组中任一权限，并支持 `*:*:*`。
  权限来自动态菜单第一项的 `meta.permissions`。
- API 模块统一从 `@/utils/request.js` 导入 `request`，URL 保留服务前缀，例如 `/erp/api/...`、`/amazon/api/...`、`/api/finance/...`。除现有路由引导代码外，不要直接用原生 axios。
- `request.js` 会按 URL 取消重复请求；需要允许并发或长轮询时，参考 `whiteMutiUrls` 或 URL 中的 `ignoreRepeat` 约定。
- 优先复用 `src/main.js` 已全局注册的组件：`GlobalTable`、`Pagination`、`RightToolbar`、`DictTag`、`FileUpload`、`ImageUpload`、`ImagePreview`、`Editor`、`svg-icon`。
- `GlobalTable` 期望分页数据为 `{ records, total }`，会维护 `currentpage/pagesize` 并通过 `loadTable` 触发加载。
- UI 是 Element Plus 后台系统风格。全局样式和变量在 `src/assets/css/global.css`、`src/assets/css/css-vars.css`；不要在业务改动中做无关重设计。

## 领域注意事项

- 系统按业务域协作，不是简单按服务隔离。ERP 库存/发货、Amazon 入库/订单/报表、Quote 物流报价、Finance 核算之间存在跨服务调用。
- Amazon 入库和发货流程可能写 ERP 库存，也可能创建 Quote 侧请求。改状态流转时同时查前端 `ship`、`shipv2` 视图和后端 ERP/Amazon 服务。
- Admin 的 Quartz 任务来自数据表 `t_sys_quartz_task`，很多批处理是配置化 HTTP 回调，不是直接 `@Scheduled` 方法。
- Amazon 通知处理使用 `wimoor-amazon` 中的 SQS 相关代码，服务启动可能拉起后台消费线程。
- 状态值经常以 SQL 注释或初始化数据为准。改状态时同时搜索 DDL、实体、Mapper、Service 和前端标签。

## 编辑规则

- 不要编辑 `target/` 和前端构建产物。
- 不要顺手格式化大型遗留文件；补丁范围贴近当前行为。
- 改共享契约前，先用完整 endpoint 路径和 DTO 字段同时搜索后端与 `wimoorui/src/api`。
- 改数据库时，同步维护 `init-config/mysql/数据库结构/` 下的结构 SQL；需要初始化或字典数据时同步维护 `init-config/mysql/数据/`。
- 改鉴权、菜单或权限时，把 Gateway Redis key、Admin 菜单接口、前端动态路由、`v-hasPerm` 使用视为一条链路一起验证。

## 测试规约（强制）

### 核心原则

1. **所有测试必须在 Docker 容器中进行**，严禁在 macOS 宿主机上直接运行 Java 服务测试。
2. **严禁使用操作系统绝对路径**（如 `/logs`、`/tmp/data`）作为应用配置；必须使用项目相对路径或容器内约定路径（如 `/logs/${APP_NAME}`）。
3. **严禁使用 `sudo` 在宿主机上创建目录来适配容器内路径**——如果容器启动报路径不存在，修复 Dockerfile 或 entrypoint，而非修改宿主机。

### Docker 容器测试方法

#### 基础设施启动

```bash
cd deploy
docker compose up -d  # 启动 MySQL、Redis、Nacos、Gateway 等基础设施
```

#### 单服务构建与部署

```bash
# 1. 构建 jar（在项目根目录）
mvn -pl wimoor-erp/erp-boot -am -DskipTests package -q

# 2. 复制 jar 到 deploy 目录
cp wimoor-erp/erp-boot/target/erp-boot.jar deploy/app.jar

# 3. 构建 Docker 镜像
docker build -f deploy/Dockerfile.service -t wimoor-erp:latest .

# 4. 启动容器
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

#### 健康检查

```bash
# 等待启动后检查容器状态
docker inspect wimoor-erp --format '{{.State.Status}} restarts={{.RestartCount}}'

# 检查 Nacos 注册
curl -s "http://localhost:8848/nacos/v1/ns/instance/list?serviceName=wimoor-erp"

# 端口探活
curl -s -o /dev/null -w "%{http_code}" http://localhost:8101/erp/api/v1/...
```

#### 查看日志

```bash
# 控制台日志（仅 bootstrap 阶段）
docker logs wimoor-erp

# 应用运行时日志（logback 输出到文件）
docker exec wimoor-erp cat /logs/wimoor-erp/log.log | tail -50
```

### API 测试方法

- 通过 Gateway（端口 8099）测试：模拟真实前端调用路径，含鉴权和路由
- 通过服务直连端口测试：绕过 Gateway，适合开发阶段快速验证

```bash
# 1. 获取登录 token（通过 Gateway）
curl -X POST http://localhost:8099/admin/api/v1/oauth/token \
  -H "Content-Type: application/json" \
  -d '{"account":"admin@wimoor.com","password":"wimoor123"}'

# 2. 带 session 调用 API
curl -H "Cookie: jsessionid=<token>" http://localhost:8099/erp/api/v1/material/media/pool?shopid=xxx
```

### 常见陷阱与解决

| 问题 | 原因 | 解决 |
|------|------|------|
| 容器启动后立即退出 exit=1 | 通常是 bean 初始化异常，logback 只写文件不打控制台 | 用 `--entrypoint sh` + `2>&1` 捕获全部输出 |
| `NoClassDefFoundError: org/apache/log4j/Priority` | Druid filters 配置了 `log4j` 但 classpath 缺少 log4j-1.2.x | 改 `druid.properties` 的 `druid.filters=stat,slf4j` |
| Docker 内存不足容器被 OOM 杀 | Docker Desktop VM 总内存有限，多服务累计超限 | 停止不相关容器；减小 `-Xmx`；`docker stats` 监控 |
| `druid.properties` 读取位置 | `@PropertySource("classpath:druid.properties")` 在 `common-mybatis` 模块中 | 优先修改 `wimoor-common/common-mybatis/src/main/resources/druid.properties` |
| Nacos 配置加载顺序 | bootstrap.yml → Nacos (wimoor-common, wimoor-commom-ext, wimoor-erp) → application | 注意 typo "commom"（不是 common） |

### Docker 网络约定

- 所有服务容器必须加入 `deploy_wimoor-net` 网络
- 服务间通过容器名解析（如 `wimoor-mysql`、`wimoor-nacos`、`wimoor-redis`）
- 宿主机通过 `localhost:端口` 访问映射端口

### 前端测试

```bash
# 在 wimoorui/ 下
cd wimoorui && npm install && npm run dev
# 浏览器访问 http://localhost:8084
# API 自动代理到 http://localhost:8099 (Gateway)
```
