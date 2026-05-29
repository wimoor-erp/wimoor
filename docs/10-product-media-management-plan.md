# 10. 商品媒体资料管理 — 现状分析与功能规划

> 版本：v1.0  日期：2026-05-27

---

## 一、现状诊断

### 1.1 现有功能盘点

经过对 ERP 商品模块、Amazon 商品模块及前端代码的全面分析，现有媒体资料管理**部分存在、但功能极为有限**：

| 维度 | 现状 | 评价 |
|---|---|---|
| ERP 商品主图 | `t_erp_material.image` → `t_picture.id`，单张主图 | ✅ 已有，功能基础 |
| ERP 商品带包装图 | `t_erp_material.pkgimage` → `t_picture.id`，单张 | ✅ 已有，功能基础 |
| Zip压缩包批量导图 | `ZipRarUploadServiceImpl`，以SKU为文件名匹配 | ✅ 已有，仅主图 |
| Amazon 主图同步 | `ProductListingsItemServiceImpl.refreshByAuthority`，从SP-API `mainImage` 下载 | ✅ 已有，仅主图 |
| 商品额外图片（Gallery） | 无 | ❌ 缺失 |
| 视频管理 | 无 | ❌ 缺失 |
| Amazon 附图同步 | 无（SP-API `getListingsItem` 返回 `images[]` 但未使用） | ❌ 缺失 |
| 媒体库（DAM） | 无集中式管理入口 | ❌ 缺失 |
| 前端图片批量管理页 | `photoupload/index.vue` 存在但 SKU/materialid 硬编码，未接入真实流程 | ⚠️ 未完成 |

### 1.2 核心表结构

```sql
-- db_erp.t_picture (同结构存在于 db_amazon)
CREATE TABLE t_picture (
  id        bigint unsigned NOT NULL,   -- 图片ID (Snowflake)
  url       varchar(500),               -- 网络原始URL（Amazon/外部图片）
  location  varchar(500),               -- 对象存储本地路径
  height    decimal(10,2),
  width     decimal(10,2),
  opttime   datetime,
  PRIMARY KEY (id)
);

-- db_erp.t_erp_material 中的图片字段
image     bigint unsigned  -- 单张主图 FK → t_picture.id
pkgimage  bigint unsigned  -- 单张带包装图 FK → t_picture.id
```

### 1.3 存在的核心问题

1. **单图限制**：主档只能存一张主图 + 一张包装图，无法支持多角度展示所需的图片集。
2. **视频完全缺失**：跨境电商平台（Amazon、TikTok Shop）均支持商品视频，系统无对应数据模型和上传逻辑。
3. **Amazon 附图未同步**：SP-API 返回的 `images[]` 数组（最多9张）在 `refreshByAuthority` 中完全忽略，只取 `mainImage`。
4. **无媒体复用机制**：同一张图在 ERP 和 Amazon 模块各存各的 `t_picture`，没有跨模块复用。
5. **photoupload 页面未落地**：`src/views/erp/baseinfo/material/photoupload/index.vue` 中 SKU 和 materialid 均为硬编码测试值，API 调用 `/erp/api/v1/material/uploadimg` 对应的后端接口已被注释（`MaterialController.java` 第 778 行注释掉了 `@PostMapping(value="/uploadimg",...)`）。
6. **无图片顺序管理**：即便未来扩展为多图，也缺少排序（主图/次图/辅图）。

---

## 二、功能规划

### 2.1 目标范围

本规划覆盖以下三个场景：

| 场景 | 说明 |
|---|---|
| **场景A：ERP 商品多图管理** | 本地商品资料支持上传多张图片（主图+gallery），以及视频 |
| **场景B：Amazon 附图自动同步** | 从SP-API拉取listing全量图片，存入多图表 |
| **场景C：媒体库集中管理** | 提供独立媒体库入口，支持按SKU/分类检索、批量操作 |

---

### 2.2 数据库设计

#### 2.2.1 新增商品多媒体表（ERP）

```sql
-- db_erp 新增：商品媒体资产表
CREATE TABLE t_erp_material_media (
  id          bigint unsigned NOT NULL COMMENT '主键',
  materialid  bigint unsigned NOT NULL COMMENT '关联 t_erp_material.id',
  shopid      varchar(64) NOT NULL COMMENT '企业ID',
  media_type  tinyint NOT NULL DEFAULT 0 
              COMMENT '媒体类型 0=主图 1=附图 2=带包装图 3=视频封面 4=视频',
  sort_order  int NOT NULL DEFAULT 0 COMMENT '排序（越小越靠前）',
  url         varchar(500) COMMENT '外部原始URL',
  location    varchar(500) COMMENT '对象存储路径',
  thumb_url   varchar(500) COMMENT '缩略图路径（视频封面/大图缩略）',
  width       int COMMENT '宽度px',
  height      int COMMENT '高度px',
  file_size   bigint COMMENT '文件大小bytes',
  duration    int COMMENT '视频时长秒数（仅视频）',
  source      varchar(32) COMMENT '来源：manual/amazon/tiktok/import',
  creator     varchar(64),
  opttime     datetime,
  PRIMARY KEY (id),
  KEY idx_material (materialid),
  KEY idx_shop_type (shopid, media_type)
) ENGINE=InnoDB COMMENT='商品多媒体资产';
```

#### 2.2.2 新增 Amazon 商品多图表

```sql
-- db_amazon 新增：Amazon listing 多图
CREATE TABLE t_amz_product_media (
  id          bigint unsigned NOT NULL,
  shopid      varchar(64) NOT NULL,
  authorityid varchar(64) NOT NULL COMMENT '授权店铺ID',
  sku         varchar(200) NOT NULL,
  asin        varchar(50),
  media_type  tinyint NOT NULL COMMENT '0=主图 1=附图 4=视频',
  sort_order  int NOT NULL DEFAULT 0,
  url         varchar(500) COMMENT 'Amazon CDN URL',
  location    varchar(500) COMMENT '本地缓存路径',
  width       int,
  height      int,
  variant_attr varchar(100) COMMENT '图片属性（如MAIN/PT01..PT08/SWCH）',
  sync_time   datetime COMMENT '同步时间',
  PRIMARY KEY (id),
  KEY idx_sku (shopid, authorityid, sku)
) ENGINE=InnoDB COMMENT='Amazon商品多图';
```

#### 2.2.3 t_erp_material 变更（可选，向后兼容）

```sql
-- 保留原有 image/pkgimage 字段不动，新增 has_gallery 标记
ALTER TABLE t_erp_material 
  ADD COLUMN has_media tinyint DEFAULT 0 COMMENT '是否有多媒体资料 0=否 1=是';
```

---

### 2.3 后端实现规划

#### 2.3.1 ERP 多媒体管理接口（wimoor-erp）

**模块路径：** `wimoor-erp/erp-boot/src/main/java/com/wimoor/erp/material/`

新增文件：

```
controller/
  MaterialMediaController.java       -- 媒体CRUD接口
service/
  IMaterialMediaService.java         -- 接口定义
  impl/
    MaterialMediaServiceImpl.java    -- 业务实现（上传/排序/删除）
pojo/
  entity/MaterialMedia.java          -- 实体类
  dto/MaterialMediaDTO.java          -- 请求DTO
mapper/
  MaterialMediaMapper.java           -- MyBatis Plus Mapper
  resources/mapper/material/MaterialMediaMapper.xml
```

**接口清单：**

| 方法 | 路径 | 说明 |
|---|---|---|
| POST | `/api/v1/material/media/upload` | 上传单个媒体（图/视频），multipart | 
| POST | `/api/v1/material/media/uploadBatch` | 批量上传，ZIP解压 |
| GET | `/api/v1/material/media/list` | 查询商品媒体列表 |
| POST | `/api/v1/material/media/sort` | 调整排序 |
| DELETE | `/api/v1/material/media/delete` | 删除媒体资产 |
| GET | `/api/v1/material/media/download` | 代理下载外部图片到本地存储 |
| POST | `/api/v1/material/media/setMain` | 设为主图（同步更新 `t_erp_material.image`） |

**上传核心逻辑（`MaterialMediaServiceImpl`）：**

```java
// 图片上传流程
// 1. 校验文件类型（image/jpeg,image/png,image/webp）和大小（≤10MB）
// 2. 生成存储路径：/erp/photos/materialImg/{shopid}/{materialid}/{timestamp}_{filename}
// 3. 调用 StorageService.putObject() 上传
// 4. 如是图片，用 Thumbnails 生成缩略图存 /erp/photos/materialImg/{shopid}/{materialid}/thumb/
// 5. 写入 t_erp_material_media
// 6. 若 media_type=0 且原 material.image 为空，同步更新 t_erp_material.image

// 视频上传流程
// 1. 校验文件类型（video/mp4）和大小（≤500MB）
// 2. 生成路径：/erp/videos/materialVideo/{shopid}/{materialid}/
// 3. 上传原始视频
// 4. 写入 t_erp_material_media（media_type=4）
// 5. （可选）异步任务提取视频首帧作为封面（media_type=3）
```

#### 2.3.2 Amazon 附图同步扩展（wimoor-amazon）

**修改文件：**  
`wimoor-amazon/amazon-boot/src/main/java/com/wimoor/amazon/product/service/impl/ProductListingsItemServiceImpl.java`

在 `refreshByAuthority` 方法中，现有代码只读取 `summary.getMainImage()`，需要扩展为读取 `Item.images`（通过 `/listings/2021-08-01/items/{sku}` 接口带 `includedData=images`）。

**扩展点：**

```
ProductListingsItemServiceImpl.refreshByAuthority()
  └─ 现有：只处理 summary.getMainImage()
  └─ 扩展：调用 getListingsItem(sku, [images]) → images[].link, images[].variant
     → 写入 db_amazon.t_amz_product_media
     → 仅主图（MAIN）同步更新 t_product_info.image（保持现有行为）
```

新增接口（`AmzProductMediaController`）：

| 方法 | 路径 | 说明 |
|---|---|---|
| GET | `/api/v1/product/media/list` | 查询Amazon商品附图列表 |
| POST | `/api/v1/product/media/syncImages` | 手动触发指定SKU附图同步 |
| GET | `/api/v1/product/media/copyToErp` | 将Amazon图片复制到ERP商品资料 |

#### 2.3.3 视频存储方案说明

对象存储（已有 `StorageService` 抽象）直接支持视频文件存储，无需额外基础设施。  
视频封面提取建议采用异步方案：

```
上传视频
  → 写数据库（status=processing）
  → 发送内部异步消息（Quartz回调 or 简单线程池）
  → 调用 FFmpeg CLI / 或Java字节流方式抽帧
  → 上传封面图
  → 更新数据库 thumb_url，status=done
```

若当前环境无FFmpeg，可在第一期跳过自动封面提取，改为让用户手动上传封面图。

---

### 2.4 前端实现规划

#### 2.4.1 商品编辑页改造（`editinfo` 模块）

**改造文件：**  
`wimoorui/src/views/erp/baseinfo/material/editinfo/components/base.vue`

现有：仅 `el-upload` 主图 + 扩展图（pkgimage）两个上传框。

改为：独立的 `MediaGallery` 组件，功能包括：
- 图片 gallery 展示（支持拖拽排序）
- 点击添加按钮触发文件选择
- 视频卡片展示（含封面+时长）
- 主图标记（角标 "主图"）
- 悬浮删除按钮

新增文件：

```
wimoorui/src/views/erp/baseinfo/material/components/
  MediaGallery.vue          -- 主要媒体管理组件
  MediaUploadDialog.vue     -- 上传弹窗（图片/视频选择）
  VideoPlayerCard.vue       -- 视频卡片（封面+播放按钮）
```

**`MediaGallery.vue` 核心设计：**

```vue
<!-- 图片行 -->
<div class="media-section">
  <div class="section-title">商品图片（最多9张）</div>
  <draggable v-model="images" item-key="id" @end="onSortEnd">
    <template #item="{element}">
      <div class="media-card" :class="{'is-main': element.sort_order===0}">
        <el-image :src="element.location || element.url" fit="cover" />
        <div class="badge" v-if="element.sort_order===0">主图</div>
        <div class="actions">
          <el-button @click="setMain(element)">设主图</el-button>
          <el-button @click="deleteMedia(element)" type="danger">删除</el-button>
        </div>
      </div>
    </template>
  </draggable>
  <div class="add-btn" @click="openUpload('image')" v-if="images.length < 9">
    <el-icon><Plus /></el-icon>
  </div>
</div>

<!-- 视频行 -->
<div class="media-section">
  <div class="section-title">商品视频（最多1个）</div>
  <VideoPlayerCard v-if="videos.length" :item="videos[0]" @delete="deleteMedia" />
  <div class="add-btn" @click="openUpload('video')" v-else>
    <el-icon><VideoCamera /></el-icon>上传视频
  </div>
</div>
```

#### 2.4.2 API 模块

新增文件：`wimoorui/src/api/erp/material/materialMediaApi.js`

```javascript
import request from '@/utils/request.js'

// 获取商品媒体列表
function getMediaList(materialid) {
  return request.get('/erp/api/v1/material/media/list', { params: { materialid } });
}

// 上传媒体（图片/视频）
function uploadMedia(formData) {
  return request({
    method: 'POST',
    url: '/erp/api/v1/material/media/upload',
    data: formData,
    headers: { 'Content-Type': 'multipart/form-data' },
    timeout: 120000  // 视频上传较慢
  });
}

// 删除媒体
function deleteMedia(id) {
  return request.get('/erp/api/v1/material/media/delete', { params: { id } });
}

// 更新排序
function sortMedia(list) {
  return request.post('/erp/api/v1/material/media/sort', list);
}

// 设置主图
function setMainMedia(id, materialid) {
  return request.get('/erp/api/v1/material/media/setMain', { params: { id, materialid } });
}

export default { getMediaList, uploadMedia, deleteMedia, sortMedia, setMainMedia };
```

#### 2.4.3 媒体库独立页面

新增路由页面：`wimoorui/src/views/erp/baseinfo/material/media/index.vue`

功能：
- 按商品名/SKU检索
- 媒体类型筛选（图片/视频）
- 瀑布流展示
- 批量下载（打包ZIP）
- 批量删除

后端菜单 `component` 字段：`erp/baseinfo/material/media/index`

---

### 2.5 Amazon 附图同步详细方案

#### 2.5.1 数据来源

Amazon SP-API `Listings Items` 接口：

```
GET /listings/2021-08-01/items/{sellerId}/{sku}
  ?marketplaceIds=...
  &includedData=summaries,images,attributes
```

返回的 `images` 数组：

```json
{
  "images": [
    { "marketplaceId": "ATVPDKIKX0DER", "images": [
      { "link": "https://...", "height": 2000, "width": 2000, "variant": "MAIN" },
      { "link": "https://...", "height": 2000, "width": 2000, "variant": "PT01" },
      ...
    ]}
  ]
}
```

`variant` 字段含义（Amazon 图片位）：

| variant | 含义 |
|---|---|
| MAIN | 主图（白底，亚马逊展示首选） |
| PT01~PT08 | 附图1～8（多角度展示） |
| SWCH | 颜色/款式切换图 |
| TOPP | 顶视图 |
| BOTT | 底视图 |

#### 2.5.2 同步策略

```
定时任务（每日一次，Quartz 回调）
  → 对每个已授权店铺的每个 ASIN
  → 调用 getListingsItem(sku, includedData=images)
  → 比较现有 t_amz_product_media 记录的 url 与返回值
  → 有变化则：
    1. 下载图片到对象存储 /amz/photos/productImg/{shopid}/{authorityid}/{marketplaceId}/{sku}/
    2. 写入或更新 t_amz_product_media
  → MAIN 图同步更新 t_product_info.image（现有行为保持）
```

#### 2.5.3 "Amazon图片→ERP商品" 同步入口

前端入口（在商品资料页）：

```
[从Amazon同步图片] 按钮
  → 调用 /amazon/api/v1/product/media/copyToErp?sku=xxx
  → 后端：查 t_amz_product_media，按 sort_order 批量写入 t_erp_material_media
  → 前端刷新媒体列表
```

---

### 2.6 约束与限制说明

| 项目 | 限制 | 原因 |
|---|---|---|
| 图片数量 | 每个SKU最多9张（Amazon标准） | 对齐Amazon listing限制 |
| 图片格式 | JPG/JPEG/PNG/WEBP | 主流格式，Amazon要求 |
| 图片大小 | 单张≤10MB | 存储与带宽考量 |
| 图片分辨率 | 主图建议≥2000px（长边） | Amazon主图最低1000px |
| 视频格式 | MP4（H.264） | 兼容性最好 |
| 视频大小 | ≤500MB | Amazon视频最大5GB，本地存储节约 |
| 视频时长 | ≤10分钟 | 商品视频一般较短 |
| 视频数量 | 每个SKU最多1个 | 简化第一期，Amazon主视频位1个 |

---

### 2.7 实施分期

#### 第一期（核心功能，约2周）

- [ ] 新建 `t_erp_material_media` 数据表（DDL）
- [ ] 后端：`MaterialMediaController` + `MaterialMediaServiceImpl`（图片上传/列表/删除/设主图）
- [ ] 前端：`MediaGallery.vue` 基础版（图片列表 + 上传 + 删除）
- [ ] 修复 `photoupload/index.vue` 或废弃，改为使用 `MediaGallery` 组件
- [ ] 菜单权限：`erp:material:media:add` / `erp:material:media:delete`

#### 第二期（视频+Amazon同步，约1.5周）

- [ ] 新建 `t_amz_product_media` 数据表（DDL，db_amazon）
- [ ] 后端：`AmzProductMediaController` + Amazon附图同步逻辑（扩展 `refreshByAuthority`）
- [ ] 前端：视频上传支持（`VideoPlayerCard.vue`）
- [ ] 前端：Amazon图片一键同步到ERP按钮

#### 第三期（媒体库，约1周）

- [ ] 媒体库独立页面 `media/index.vue`
- [ ] 批量操作（下载ZIP / 批量删除）
- [ ] 图片排序拖拽功能（依赖 `vuedraggable`）

---

### 2.8 依赖确认

| 依赖 | 状态 | 说明 |
|---|---|---|
| 对象存储（OSS/MinIO） | ✅ 已有 `StorageService` 抽象 | 直接复用 |
| `Thumbnails`（缩略图） | ✅ 已引入（`PictureServiceImpl` 使用） | 直接复用 |
| Amazon SP-API Listings Items | ✅ 已有 SDK | 需扩展 `includedData=images` 参数 |
| `vuedraggable` | ❓ 需确认 | 用于前端图片排序拖拽 |
| FFmpeg（视频封面） | ❌ 未引入 | 第一期可跳过，改为手动上传封面 |
| Element Plus `el-image-viewer` | ✅ 随 Element Plus 内置 | 图片预览直接使用 |

---

### 2.9 数据库初始化文件位置

按项目约定，新增 DDL 应放置到：

```
init-config/mysql/数据库结构/db_erp/t_erp_material_media.sql
init-config/mysql/数据库结构/db_amazon/t_amz_product_media.sql
deploy/mysql-init/（视部署需要补充）
```

---

## 三、结论

本项目**不具备**商品多图或视频管理能力，仅有**单主图+单包装图**的初级支持。  
`photoupload/index.vue` 虽然文件存在，但处于未完成状态（硬编码参数，后端接口注释）。  
Amazon 附图（PT01~PT08）虽然 SP-API 可以获取，但系统未作处理。

建议按上述三期规划逐步落地，优先第一期（ERP多图管理），再扩展Amazon附图同步，最后完善媒体库。

---

*文档生成来源：代码分析（`MaterialController`、`MaterialServiceImpl`、`ZipRarUploadServiceImpl`、`ProductListingsItemServiceImpl`、`t_erp_material.sql`、`t_picture.sql`、`base.vue`、`photoupload/index.vue`）*
