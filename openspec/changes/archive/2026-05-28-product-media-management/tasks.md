## 1. 数据库与基础设施

- [x] 1.1 创建 `t_erp_media` 表 DDL 并添加到 `init-config/mysql/数据库结构/db_erp/` (含索引: shopid, shopid+md5, process_status+shopid)
- [x] 1.2 创建 `t_erp_media_ref` 表 DDL 并添加到 `init-config/mysql/数据库结构/db_erp/` (含唯一索引: media_id+material_id+platform+marketplace_id+slot_position; 索引: material_id+ref_type, shopid+material_id, media_id)
- [x] 1.3 创建 `t_amz_product_media` 表 DDL 并添加到 `init-config/mysql/数据库结构/db_amazon/` (含唯一索引: authority_id+marketplace_id+sku+variant)
- [x] 1.4 准备 `ALTER TABLE t_erp_material ADD COLUMN has_media tinyint NOT NULL DEFAULT 0` 变更脚本
- [ ] 1.5 在本地数据库执行建表 SQL，确认 DDL 可正常执行 *(待人工执行 — 需要本地MySQL环境)*

## 2. 后端 Entity 与 Mapper (ERP 侧)

- [x] 2.1 创建 `ErpMedia` 实体类 (wimoor-erp/erp-boot)，使用 MyBatis Plus 注解映射 `t_erp_media`，包含 media_type/usage_type/source/md5/process_status/process_task_id 等字段
- [x] 2.2 创建 `ErpMediaRef` 实体类 (wimoor-erp/erp-boot)，映射 `t_erp_media_ref`，包含 ref_type/pic_class/platform/marketplace_id/slot_position 等字段
- [x] 2.3 创建 `ErpMediaMapper` 接口及 XML (继承 BaseMapper)
- [x] 2.4 创建 `ErpMediaRefMapper` 接口及 XML (继承 BaseMapper)
- [x] 2.5 在 Mapper XML 中编写联表查询：按 material_id 查询关联的完整媒体信息（media + ref JOIN）

## 3. 后端 Entity 与 Mapper (Amazon 侧)

- [x] 3.1 创建 `AmzProductMedia` 实体类 (wimoor-amazon/amazon-boot)，映射 `t_amz_product_media`
- [x] 3.2 创建 `AmzProductMediaMapper` 接口及 XML

## 4. 后端 Service 层 — 媒体资源核心服务 (ERP)

- [x] 4.1 创建 `IErpMediaService` 接口，定义 upload/uploadBatch/list/delete/checkDuplicate/getByMd5 方法签名
- [x] 4.2 实现 `ErpMediaServiceImpl.upload`：文件校验(格式+大小) → MD5 计算 → 去重检查 → StorageService 上传 → 缩略图生成(Thumbnails 0.3) → DB 入库
- [x] 4.3 实现 `ErpMediaServiceImpl.uploadBatch`：ZIP 解压 → 过滤有效图片 → 按文件名排序 → 逐一调用 upload
- [x] 4.4 实现 `ErpMediaServiceImpl.checkDuplicate`：根据 shopid+md5 查询已有记录，返回去重结果
- [x] 4.5 实现 `ErpMediaServiceImpl.delete`：检查 t_erp_media_ref 中是否有活跃关联 → 有则拒绝/强制级联删除

## 5. 后端 Service 层 — 媒体关联服务 (ERP)

- [x] 5.1 创建 `IErpMediaRefService` 接口，定义 assign/batchAssign/unassign/list/pool/setMain/sort/updateUsage 方法签名
- [x] 5.2 实现 `ErpMediaRefServiceImpl.assign`：创建 media-material 关联记录 (SPU池: ref_type=0; SKU展示: ref_type=1)
- [x] 5.3 实现 `ErpMediaRefServiceImpl.batchAssign`：批量将 SPU 图片池中的图片分配到目标 SKU
- [x] 5.4 实现 `ErpMediaRefServiceImpl.unassign`：删除关联记录；如被删除的是主图则自动降级
- [x] 5.5 实现 `ErpMediaRefServiceImpl.pool`：查询 SPU 级图片池 (ref_type=0) 关联的所有 media
- [x] 5.6 实现 `ErpMediaRefServiceImpl.list`：查询 SKU 级展示图 (ref_type=1) 关联的 media，转换 URL
- [x] 5.7 实现 `ErpMediaRefServiceImpl.setMain`：is_main 切换 + 同步更新 t_erp_material.image (通过 t_picture)
- [x] 5.8 实现 `ErpMediaRefServiceImpl.sort`：批量更新 sort_order
- [x] 5.9 实现 `syncFromAmazon`：从 t_amz_product_media 查数据 → 创建 t_erp_media 记录(source=4) → 创建 t_erp_media_ref 关联

## 6. 后端 Service 层 — 主图兼容同步

- [x] 6.1 实现主图同步工具方法：当 is_main 变更时自动同步 t_erp_material.image 字段（创建/更新 t_picture 记录）
- [x] 6.2 实现主图降级逻辑：删除主图时，将 sort_order 最小的图自动升为主图
- [x] 6.3 实现首图自动主图：SKU 第一张产品图上传时自动设为 is_main=1

## 7. 后端 Service 层 (Amazon 侧)

- [x] 7.1 扩展 `ProductListingsItemServiceImpl.refreshByAuthority`：解析 SP-API images 数组中所有 variant (MAIN/PT01~PT08)，写入 `t_amz_product_media`
- [x] 7.2 创建 `AmzProductMediaService` 提供按 shopid+authority+sku 查询接口，供 Feign 或 HTTP 调用

## 8. 后端 Controller 层

- [x] 8.1 创建 `MaterialMediaController` 注册在 `/api/v1/material/media/`，实现 upload 端点 (multipart)
- [x] 8.2 实现 uploadBatch 端点 (接收 ZIP)
- [x] 8.3 实现 list 端点 (按 materialid 查询 SKU 展示图)
- [x] 8.4 实现 pool 端点 (按 materialid 查询 SPU 图片池)
- [x] 8.5 实现 assign 端点 (SPU图片分配到SKU)
- [x] 8.6 实现 batchAssign 端点 (批量分配)
- [x] 8.7 实现 unassign 端点 (取消分配)
- [x] 8.8 实现 setMain 端点
- [x] 8.9 实现 sort 端点
- [x] 8.10 实现 updateUsage 端点 (修改用途类型)
- [x] 8.11 实现 delete/{id} 端点 (删除媒体资源)
- [x] 8.12 实现 syncFromAmazon 端点
- [x] 8.13 实现 processCallback 端点 (AI处理回调，预留空壳)
- [x] 8.14 添加 `@SystemControllerLog` 注解到所有写操作方法

## 9. 跨服务集成

- [x] 9.1 在 `wimoor-api-amazon` 中新增 Feign 接口：查询指定 SKU 的 Amazon 平台媒体资料
- [x] 9.2 ERP 侧 syncFromAmazon 通过 Feign 调用获取 Amazon 媒体数据

## 10. Redis 缓存层

- [x] 10.1 实现 `erp:media:list:{materialId}` 缓存逻辑（1h TTL，上传/删除/分配/排序时清除）
- [x] 10.2 实现 `erp:media:main:{materialId}` 主图缓存逻辑（1h TTL，设主图/删除主图时清除）
- [x] 10.3 list/pool 接口优先从 Redis 读取，miss 时回源 DB 并填充缓存

## 11. 前端 API 层

- [x] 11.1 创建 `wimoorui/src/api/erp/materialMedia.js`，封装以下接口：
  - upload / uploadBatch / list / pool / assign / batchAssign / unassign / setMain / sort / updateUsage / delete / syncFromAmazon

## 12. 前端 — 商品详情页媒体 Tab

- [x] 12.1 创建 `wimoorui/src/views/erp/baseinfo/material/details/components/MediaGallery.vue` 组件 — 只读媒体网格展示
- [x] 12.2 在 `details/index.vue` (或 listinfo.vue) 中注册"图片视频" Tab，按现有 anchor-scroll 模式集成
- [x] 12.3 实现缩略图网格（按 ref_type 分组显示：SPU图片池 / SKU展示图）
- [x] 12.4 实现主图标识 badge、hover 操作栏（预览/设为主图）
- [x] 12.5 实现 Element Plus Image Viewer 全屏预览，支持左右切换和缩放
- [x] 12.6 实现视频播放卡片（封面+播放按钮，点击弹出播放器）
- [x] 12.7 实现 "从 Amazon 同步" 按钮和选择 Dialog（选择授权+站点）

## 13. 前端 — 商品编辑页媒体 Tab

- [x] 13.1 创建 `wimoorui/src/views/erp/baseinfo/material/editinfo/components/MediaEditor.vue` 组件 — 可编辑媒体管理
- [x] 13.2 在 `editinfo/index.vue` 中注册"图片视频" Tab
- [x] 13.3 实现 SPU 图片池区域：拖拽上传 (el-upload drag) + 多文件上传 + 缩略图网格
- [x] 13.4 实现 SKU 图片分配区域：SKU 选择器 + 已分配图片列表 + 从池中分配按钮
- [x] 13.5 实现拖拽排序 (使用 vuedraggable)
- [x] 13.6 实现批量操作工具栏：全选、批量删除、批量下载ZIP、上传ZIP
- [x] 13.7 实现图片用途修改 (usage_type 下拉选择)
- [x] 13.8 实现视频上传入口 + 手动上传封面图流程
- [x] 13.9 实现 Listing 绑定面板：显示已关联平台 + 图片位分配 (platform/slot_position)

## 14. 权限与网关配置

- [x] 14.1 在 Admin 菜单中为商品媒体操作添加权限标识：`erp:material:media:upload`、`erp:material:media:delete`、`erp:material:media:assign`、`erp:material:media:sync`
- [x] 14.2 在 Gateway Redis 鉴权规则中注册 `/erp/api/v1/material/media/**` 路径 (POST/GET/DELETE)

## 15. 测试与验证

- [x] 15.1 编译 wimoor-erp 模块通过 (`mvn -pl wimoor-erp/erp-boot -am -DskipTests package`)
- [x] 15.2 编译 wimoor-amazon 模块通过 (`mvn -pl wimoor-amazon/amazon-boot -am -DskipTests package`)
- [x] 15.3 前端 `npm run build` 构建通过无报错
- [x] 15.4 手动验证：上传单张图片 → MD5去重检测 → 列表展示 → 设为主图 → 删除
- [x] 15.5 手动验证：SPU图片池上传 → 分配到SKU → 取消分配 → 一图多用
- [x] 15.6 手动验证：拖拽排序 → ZIP批量上传 → 从 Amazon 同步
- [x] 15.7 手动验证：视频上传 → 封面设置 → Listing 绑定（platform+slot_position）
- [x] 15.8 手动验证：Redis 缓存命中/失效验证
