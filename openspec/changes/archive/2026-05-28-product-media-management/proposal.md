## Why

跨境电商的商品媒体资料（图片/视频）是运营核心资产，直接影响转化率和品牌一致性。当前系统仅支持 ERP 商品单张主图 + 单张包装图，无法满足 Amazon（主图+8张附图+视频）、TikTok Shop（主图+多张卖点图+视频）、eBay（12张Gallery）等多平台的多图多视频管理需求。

经过对 PDM 基础资料系统的完整调研（参见 docs/11-product-media-storage-design.md），其成熟的「媒体元数据 + 关联包」双表分离架构已验证了一套可靠的图片管理范式：图片作为独立资源实体存在，通过关联表实现与商品（SPU/SKU）的多对多关系，支持一图多用、SPU图片池→SKU分配等核心场景。

本提案在 PDM 架构基础上，适配 Wimoor 的多租户模型和跨境电商场景，建立完整的媒体资产管理能力。同时为未来引入 AI Agent/Workflow（自动生成白底图、智能场景图合成、多平台图片适配等）预留扩展接口。

## What Changes

- 新增媒体资源元数据表 `t_erp_media`（借鉴 PDM `sku_pic` 双表设计），作为统一的图片/视频资产存储
- 新增媒体-商品关联表 `t_erp_media_ref`（借鉴 PDM `sku_pic_pack`），实现一图多用、SPU图片池→SKU分配
- 新增 Amazon 商品媒体缓存表 `t_amz_product_media`，同步 SP-API 返回的全量图片（MAIN+PT01~PT08）
- 新增前端"图片视频"选项卡（嵌入商品详情页），提供 SPU 图片池 + SKU 图片分配 + 拖拽排序管理
- 新增视频上传与管理能力（MP4，手动封面图）
- 新增 MD5 文件去重机制，同企业同文件不重复存储
- 扩展 Amazon `refreshByAuthority` 同步附图逻辑
- 保持对现有 `t_erp_material.image`/`pkgimage` 字段的向后兼容（主图自动同步写入）
- 预留 AI Agent 扩展点：媒体处理回调接口、异步任务状态追踪

## Capabilities

### New Capabilities
- `media-asset-storage`: 媒体资产存储层——双表分离架构（t_erp_media + t_erp_media_ref）、物理存储路径规范、文件上传/缩略图生成/MD5去重、Amazon多图缓存表
- `media-crud-api`: 媒体资源 CRUD 后端接口——上传、列表、删除、排序、设主图、SPU图片池分配到SKU、批量导入、从 Amazon 同步、AI处理回调预留
- `media-listing-binding`: 媒体与 Listing 绑定管理——SKU 图片分配到平台 Listing 各图片位（MAIN/PT01~PT08）、多站点差异图管理
- `media-frontend-ui`: 前端图片视频管理界面——SPU图片池 + SKU分配面板、Gallery 组件、上传组件、视频播放卡片、拖拽排序

### Modified Capabilities
<!-- No existing openspec specs to modify -->

## Impact

- **数据库**：db_erp 新增 `t_erp_media`、`t_erp_media_ref`；db_amazon 新增 `t_amz_product_media`；`t_erp_material` 新增 `has_media` 字段
- **后端 ERP**：新增 `MaterialMediaController`、`IMaterialMediaService`、`ErpMediaMapper`、`ErpMediaRefMapper`；修改 `MaterialServiceImpl`（保存时同步主图）
- **后端 Amazon**：修改 `ProductListingsItemServiceImpl.refreshByAuthority`（扩展附图同步）；新增 `AmzProductMediaController`
- **前端**：新增 `MediaGallery.vue`、`MediaUploadDialog.vue`、`MediaAssignDialog.vue`；修改 `details/index.vue` 和 `editinfo/index.vue`（新增"图片视频"tab）；新增 `mediaApi.js`
- **存储**：复用现有 `StorageService`（OSS/MinIO），新增路径 `erp/media/{shopid}/{materialid}/`
- **依赖**：前端需要 `vuedraggable`（图片拖拽排序）
- **扩展预留**：`t_erp_media` 包含 `source` 字段支持追踪 AI 生成来源；预留异步处理状态字段
