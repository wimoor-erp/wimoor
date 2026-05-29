## Context

### 现有状态

Wimoor ERP 的商品主数据 (`t_erp_material`) 当前仅支持两个图片引用字段：
- `image` → `t_picture.id`：单张主图
- `pkgimage` → `t_picture.id`：单张带包装图

Amazon 模块通过 `ProductListingsItemServiceImpl.refreshByAuthority` 从 SP-API 抓取 `summary.getMainImage()` 下载主图，但 **完全忽略** 了 `images[]` 数组中的附图（PT01~PT08）。

前端商品详情页（`details/index.vue`）左侧竖 tab 当前为：基本信息、组合信息、采购信息、规格信息、辅料关联、物流信息、库存信息。无"图片视频"独立 tab。

图片存储使用 `StorageService`（抽象层，支持 OSS/MinIO/FTP），路径格式为 `{bucketName}/erp/photos/materialImg/{companyId}/{materialId}-{timestamp}.{ext}`，URL 展示通过 `FileUpload.getPictureImage()` 转换。

### PDM 系统架构参考（docs/11）

PDM 基础资料系统已验证的成熟架构：
- **双表分离**：`sku_pic`(图片元数据) + `sku_pic_pack`(图片-商品关联) 实现多对多
- **SPU图片池→SKU分配**：图片先上传到 SPU 维度，再按属性/SKU 分配到具体变体
- **三维分类**：picType(用途) + genre(来源) + picClass(分配角色)
- **异步同步**：Redis MQ 异步推送到 OSS + 图片检索服务
- **缓存策略**：`pic_sku::` Redis 缓存，1小时过期

### 约束

- Java 8 环境，Spring Cloud 微服务架构
- 前端：Vue 3 + Element Plus + Vite，动态路由菜单
- 对象存储：已有 `StorageService` 抽象（OSS/MinIO），`PictureServiceImpl` 负责图片上传/缩略图
- 数据库：MySQL 8，各服务独立库（db_erp、db_amazon）
- 保持 `t_erp_material.image`/`pkgimage` 向后兼容
- 现有 `t_picture` 表仅存储 url、location、尺寸，无关联业务元数据
- **未来扩展**：AI Agent/Workflow 将用于自动生成白底图、智能场景图、多平台尺寸适配等

## Goals / Non-Goals

**Goals:**
- 建立 SKU/SPU 级别的媒体资产库，采用双表分离（元数据+关联）架构支持一图多用
- 实现 SPU 图片池 → SKU 分配模型（借鉴 PDM 核心理念）
- 支持图片用途分类（成品图/橱窗图/公共图/说明图/场景图）和来源标记（自拍/引用/白底/同步）
- 实现 MD5 文件去重，同企业同文件不重复存储
- 前端在商品详情页新增"图片视频"tab，提供 SPU 图片池 + SKU 分配管理 UI
- 从 Amazon SP-API 自动同步全量图片（MAIN + PT01~PT08）
- 保持现有主图/包装图字段兼容，无 breaking change
- 为 AI Agent/Workflow 预留扩展接口（处理回调、状态追踪、来源标记）

**Non-Goals:**
- A+ Content 的内容编辑与上传
- 自动视频转码/多码率
- AI 智能抠图/自动生成白底图（预留接口，本期不实现）
- 图片上传到 Amazon/TikTok 平台（仅管理本地资产）
- 图片编辑器（裁剪/加水印等）
- 图片审核工作流（PDM 有此能力，Wimoor 暂不引入）

## Decisions

### D1：数据建模 — 双表分离架构（借鉴 PDM）

**决策：** 采用「媒体元数据表 `t_erp_media` + 媒体-商品关联表 `t_erp_media_ref`」双表分离设计，对标 PDM 的 `sku_pic` + `sku_pic_pack` 架构。

**理由：**
- PDM 系统已验证该架构可支撑大规模商品图片管理（百万级图片资产）
- 关联表实现一图多用（同一图分配给多个 SKU/Listing）
- SPU 图片池概念天然适配 Wimoor 的父子商品关系（isparent/groupid）
- 媒体元数据独立于商品，方便未来 AI Agent 直接操作媒体资源

**替代方案及放弃原因：**
- 方案B（原设计单表 `t_erp_material_media` 平铺）：无法支持一图多用、无法实现 SPU 图片池分配
- 方案C（扩展 `t_picture` 表）：`t_picture` 是跨模块公共表，加业务字段污染范围太大

**数据库设计：**

```sql
-- ============================================================
-- db_erp: 媒体资源元数据表（对标 PDM sku_pic + sku_video）
-- ============================================================
CREATE TABLE `t_erp_media` (
  `id`             bigint unsigned NOT NULL COMMENT '主键(Snowflake)',
  `shopid`         varchar(64) NOT NULL COMMENT '企业ID(租户隔离)',
  `media_type`     tinyint NOT NULL DEFAULT 0 
                   COMMENT '媒体类型: 0=图片 1=视频',
  `usage_type`     tinyint NOT NULL DEFAULT 40 
                   COMMENT '用途类型: 10=参考图 30=原图 40=成品图 60=橱窗图 70=公共图 90=说明图 100=场景图',
  `source`         tinyint NOT NULL DEFAULT 1 
                   COMMENT '来源: 1=引用图 2=自拍图 3=白底图 4=Amazon同步 5=批量导入 6=AI生成',
  `url`            varchar(500) COMMENT '外部原始URL(如Amazon CDN)',
  `location`       varchar(500) COMMENT '对象存储相对路径',
  `thumb_location` varchar(500) COMMENT '缩略图/视频封面存储路径',
  `name`           varchar(255) COMMENT '原始文件名',
  `width`          int COMMENT '宽度px',
  `height`         int COMMENT '高度px',
  `file_size`      bigint COMMENT '文件大小(bytes)',
  `duration`       int COMMENT '视频时长(秒,仅视频)',
  `content_type`   varchar(100) COMMENT 'MIME类型',
  `md5`            varchar(32) COMMENT '文件MD5(用于去重)',
  `process_status` tinyint DEFAULT 0 
                   COMMENT 'AI处理状态: 0=无需处理 1=处理中 2=处理完成 3=处理失败',
  `process_task_id` varchar(128) COMMENT 'AI处理任务ID(用于回调关联)',
  `creator`        varchar(64) COMMENT '上传人',
  `create_time`    datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time`    datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_shop` (`shopid`),
  KEY `idx_md5` (`shopid`, `md5`),
  KEY `idx_process` (`process_status`, `shopid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='媒体资源元数据表';

-- ============================================================
-- db_erp: 媒体-商品关联表（对标 PDM sku_pic_pack）
-- ============================================================
CREATE TABLE `t_erp_media_ref` (
  `id`             bigint unsigned NOT NULL COMMENT '主键',
  `media_id`       bigint unsigned NOT NULL COMMENT 'FK → t_erp_media.id',
  `material_id`    bigint unsigned NOT NULL COMMENT 'FK → t_erp_material.id',
  `shopid`         varchar(64) NOT NULL COMMENT '企业ID',
  `ref_type`       tinyint NOT NULL DEFAULT 0 
                   COMMENT '关联类型: 0=SPU级图片池 1=SKU级展示图',
  `pic_class`      tinyint NOT NULL DEFAULT 1 
                   COMMENT '分配角色: 1=成品图 2=橱窗图 3=公共图 4=说明图 5=场景图',
  `sort_order`     int NOT NULL DEFAULT 0 COMMENT '排序(越小越靠前)',
  `is_main`        tinyint NOT NULL DEFAULT 0 COMMENT '是否主图 0=否 1=是',
  `platform`       varchar(32) DEFAULT NULL COMMENT '平台标识(amazon/tiktok/ebay) NULL=通用',
  `marketplace_id` varchar(32) DEFAULT NULL COMMENT '站点ID(用于多站点差异图)',
  `slot_position`  varchar(20) DEFAULT NULL COMMENT '图片位(MAIN/PT01~PT08) 用于Listing绑定',
  `create_time`    datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_media_material` (`media_id`, `material_id`, `platform`, `marketplace_id`),
  KEY `idx_material` (`material_id`, `ref_type`),
  KEY `idx_shop_material` (`shopid`, `material_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='媒体-商品关联表';

-- ============================================================
-- db_amazon: Amazon 商品媒体同步缓存表
-- ============================================================
CREATE TABLE `t_amz_product_media` (
  `id`             bigint unsigned NOT NULL,
  `shopid`         varchar(64) NOT NULL COMMENT '企业ID',
  `authority_id`   varchar(64) NOT NULL COMMENT '授权店铺ID',
  `marketplace_id` varchar(32) NOT NULL COMMENT '站点ID',
  `sku`            varchar(200) NOT NULL,
  `asin`           varchar(50),
  `media_type`     tinyint NOT NULL DEFAULT 0 COMMENT '0=图片 1=视频',
  `variant`        varchar(20) NOT NULL COMMENT 'MAIN/PT01~PT08/SWCH',
  `sort_order`     int NOT NULL DEFAULT 0,
  `url`            varchar(500) COMMENT 'Amazon CDN URL',
  `location`       varchar(500) COMMENT '本地缓存路径',
  `width`          int,
  `height`         int,
  `sync_time`      datetime COMMENT '最近同步时间',
  `create_time`    datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_sku_variant` (`authority_id`, `marketplace_id`, `sku`, `variant`),
  KEY `idx_shop_sku` (`shopid`, `authority_id`, `sku`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Amazon商品多媒体同步缓存';

-- ============================================================
-- db_erp: t_erp_material 兼容变更
-- ============================================================
ALTER TABLE `t_erp_material` 
  ADD COLUMN `has_media` tinyint NOT NULL DEFAULT 0 COMMENT '是否有多媒体 0=否 1=是';
```

### D2：SPU 图片池 → SKU 分配模型

**决策：** 借鉴 PDM 的 SPU→SKU 图片分配核心理念，通过 `t_erp_media_ref.ref_type` 区分图片池（SPU级）和展示图（SKU级）。

**流转逻辑：**
1. 图片上传时关联到 SPU（父商品 `isparent=1`），`ref_type=0`
2. 用户通过「分配」操作将图片从 SPU 池分配到子 SKU，创建 `ref_type=1` 记录
3. 同一 media_id 可分配给多个 SKU（一图多用）
4. 独立商品（无父子）直接关联 `ref_type=1`
5. 每个 SKU 仅一张主图（`is_main=1`）

**与 PDM 的差异：**
- 不引入 PDM 的 `mark`（数据空间）概念，用 `shopid` 做租户隔离
- 不引入 PDM 的 ES 索引，使用 MySQL 查询 + Redis 缓存
- Listing 绑定合并到 `t_erp_media_ref` 的 `platform`/`marketplace_id`/`slot_position` 字段，不单独建表

### D3：合并 Listing 绑定到关联表

**决策：** 取消原设计中独立的 `t_erp_media_listing_rel` 表，将 Listing 绑定信息合并到 `t_erp_media_ref` 中通过 `platform`/`marketplace_id`/`slot_position` 字段表达。

**理由：**
- 减少表数量，降低系统复杂度
- Listing 绑定本质上就是"图片分配给特定平台的特定位置"，与 SKU 图片分配是同一层抽象
- PDM 系统中 `sku_pic_pack.picClass` 也是在关联表中表达用途，而非独立建表
- 未来 AI Agent 生成的图片也通过同一关联表分配到 Listing

**使用方式：**
- `platform=NULL, marketplace_id=NULL`：通用图片（不绑定特定平台）
- `platform='amazon', marketplace_id='ATVPDKIKX0DER', slot_position='MAIN'`：绑定到 Amazon US 主图位
- 查询某 Listing 的图片：`WHERE material_id=? AND platform=? AND marketplace_id=?`

### D4：物理存储路径规范

**决策：** 采用 `erp/media/{shopid}/{materialid}/{yyyy}/{MM}/{dd}/{mediaId}_{filename}` 层级结构。

```
{bucketName}/
├── erp/
│   ├── photos/materialImg/    ← 现有路径（保持不变）
│   └── media/                 ← 新增媒体库路径
│       └── {shopid}/
│           └── {materialid}/
│               └── {yyyy}/{MM}/{dd}/
│                   ├── {mediaId}_{filename}.jpg      ← 原图
│                   ├── thumb/{mediaId}_{filename}.jpg ← 缩略图
│                   └── video/{mediaId}_{filename}.mp4 ← 视频
└── amz/photos/productImg/     ← 现有Amazon图路径
```

**理由：**
- 日期分级避免单目录文件过多
- mediaId 前缀确保全局唯一
- 与 PDM 的 `{年}/{月}/{日}/{SPU}/original/` 类似但适配多租户

### D5：MD5 去重机制

**决策：** 上传时计算文件 MD5，查 `t_erp_media WHERE shopid=? AND md5=?`。已存在则不重复上传物理文件，直接创建新的 `t_erp_media_ref` 关联。

**理由：** PDM 系统通过 picId（文件名即ID）实现去重；Wimoor 使用 MD5 更灵活，不依赖文件命名。

### D6：前端入口设计

**决策：** 在商品详情页/编辑页新增"图片视频"tab，内部分为「SPU图片池」和「SKU分配」两个区域（仅对有子商品的SPU显示分配区域）。

**组件结构：**
```
MediaGallery.vue（核心组件）
├── SPU图片池区域（ref_type=0 的图片，可拖拽排序）
│   ├── 图片缩略图网格
│   ├── [上传图片] 按钮
│   └── 图片操作（设主图/删除/修改用途/分配到SKU）
├── SKU分配区域（ref_type=1 的图片，按SKU分组展示）
│   ├── SKU选择器（切换查看不同子SKU）
│   └── 已分配图片列表（含主图标记）
└── 视频区域
    ├── 视频缩略图（封面+播放按钮）
    └── [上传视频] 按钮
```

### D7：API 设计

**决策：** 独立 `MaterialMediaController`，路径前缀 `/api/v1/material/media/`。

| Method | Path | 说明 |
|---|---|---|
| POST | `/upload` | 上传单个媒体（multipart） |
| POST | `/uploadBatch` | ZIP批量上传 |
| GET | `/list` | 按 materialid 查询媒体列表 |
| GET | `/pool` | 查询SPU图片池（ref_type=0） |
| POST | `/assign` | 将图片从SPU池分配到SKU |
| POST | `/batchAssign` | 批量分配 |
| POST | `/unassign` | 取消SKU图片分配 |
| POST | `/setMain` | 设为主图 |
| POST | `/sort` | 调整排序 |
| POST | `/updateUsage` | 修改图片用途类型 |
| DELETE | `/{id}` | 删除媒体资源 |
| POST | `/syncFromAmazon` | 从Amazon同步图片到ERP |
| POST | `/processCallback` | AI处理结果回调（预留） |

### D8：主图兼容策略

**决策：** `t_erp_material.image` 字段保留，由媒体服务自动同步。

**规则：**
1. 上传第一张产品图 → 自动设为主图 → 同步写 `material.image`（通过 `t_picture`）
2. "设为主图"操作 → 清除旧主图 + 设新主图 + 同步写 `material.image`
3. 删除主图 → 自动将 sort_order 最小的图升为主图
4. 现有 `saveData` 接口的 `file` 参数保留兼容 → 上传时同时写入媒体库

### D9：AI Agent/Workflow 扩展预留

**决策：** 在 `t_erp_media` 中预留 `process_status`、`process_task_id` 字段，并设计回调接口。

**扩展场景（本期不实现）：**
- 自动生成白底图：上传原图 → Agent 调用抠图服务 → 回调写入新 media 记录
- 智能场景图合成：选择产品图 → Agent 生成场景图 → 回调写入 source=6(AI生成)
- 多平台尺寸适配：一张原图 → Agent 裁剪为各平台尺寸 → 批量回调

**回调接口设计（预留）：**
```
POST /api/v1/material/media/processCallback
Body: { taskId, status, resultMediaIds[], errorMessage }
```

Agent 处理完成后回调此接口：
1. 更新原 media 记录 `process_status`
2. 如有新生成文件，创建新 `t_erp_media` 记录（source=6）
3. 自动关联到同一 material

### D10：视频处理方案

**决策：** 第一期视频仅支持上传 MP4 + 手动设置封面图，不做自动抽帧。

**限制：** MP4(H.264) / ≤500MB / 每SKU最多3个

**理由：** 自动抽帧需 FFmpeg，后续作为 AI Agent 能力引入（视频分析+关键帧提取）。

### D11：缓存策略

**决策：** 借鉴 PDM 的 `pic_sku::` Redis 缓存模式。

| Key Pattern | TTL | 清除时机 |
|---|---|---|
| `erp:media:list:{materialId}` | 1h | 上传/删除/分配/排序 |
| `erp:media:main:{materialId}` | 1h | 设主图/删除主图 |

## Risks / Trade-offs

| 风险 | 影响 | 缓解措施 |
|---|---|---|
| 大文件上传超时 | 视频>100MB 可能导致网关超时 | Gateway 对 `/media/upload` 路径单独配置超时（120s） |
| `material.image` 同步一致性 | 并发操作可能导致主图状态不一致 | 操作加乐观锁，同步逻辑幂等 |
| Amazon API 限流 | 批量同步附图可能触发 SP-API 限流 | 复用现有刷新节奏，不新增独立调用 |
| 存储成本增长 | 多图+视频显著增加存储用量 | MD5去重 + 缩略图压缩 + 视频限制大小 |
| 双表JOIN性能 | 大数据量下 media + media_ref JOIN | 核心查询走 material_id 索引，结果缓存 Redis |
| AI Agent 回调安全 | 外部回调可能被伪造 | 回调接口需验证 taskId 对应关系 + 签名校验 |

## Migration Plan

### 部署步骤

1. **数据库变更**（先于代码部署）
   - 执行 `t_erp_media` DDL
   - 执行 `t_erp_media_ref` DDL
   - 执行 `t_amz_product_media` DDL
   - `ALTER TABLE t_erp_material ADD COLUMN has_media ...`
   - 无需数据迁移（新表新数据，旧 `t_picture` 继续工作）

2. **后端部署**
   - 编译部署 wimoor-erp（新增媒体接口，不影响现有接口）
   - 编译部署 wimoor-amazon（附图同步为增量逻辑，现有行为不变）

3. **前端部署**
   - 部署新版 wimoorui（新增 tab + 组件）

4. **权限配置**
   - 新增权限标识：`erp:material:media:upload`、`erp:material:media:delete`、`erp:material:media:assign`
   - Gateway Redis 鉴权注册 `/erp/api/v1/material/media/**`

### 回滚策略

- 前端回滚：还原去掉新 tab 即可
- 后端回滚：新增 Controller 不影响现有路由
- 数据库：新表保留不删（空表无成本）

### 数据兼容

- 历史图片通过 `material.image → t_picture` 链路继续工作
- 新上传图片同时写入 `t_erp_media` + `t_erp_media_ref` + 同步 `material.image`
- 可选后续脚本：将 `t_picture` 中已有商品图迁移到新表

## Open Questions

1. ~~是否需要第一期就实现 Listing 关联表？~~ **已决策：合并到 `t_erp_media_ref`，通过 `platform`/`slot_position` 字段表达**
2. **视频大小上限？** 500MB（对齐 docs/11 规划）
3. ~~是否需要图片审核状态？~~ **不引入。PDM 有此能力，但 Wimoor 团队规模小，暂不需要**
4. **历史数据迁移优先级？** 低优先级，新功能稳定后再考虑批量迁移脚本
