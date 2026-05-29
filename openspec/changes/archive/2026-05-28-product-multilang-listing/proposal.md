## Why

当前 ERP 产品主档（`t_erp_material`）仅有单一 `name` 和 `remark` 字段，无法存储面向多平台刊登所需的多语言标题和 HTML 格式商品详情描述。运营人员在将商品发布到 eBay、Amazon 等多站点时，需要为每种语言单独维护标题和产品描述，目前只能在站外手工管理，缺少统一的数据中心。此功能属于跨境电商产品基础信息的关键一环，补齐后可直接用于多平台 Listing 刊登。

## What Changes

- 新增 ERP 数据库表 `t_erp_material_listing`，按 `(materialid, lang)` 唯一键存储每个产品的多语言标题（`title`，限 500 字符）和 HTML 详情描述（`description`，MEDIUMTEXT）。
- 在 `wimoor-erp` 后端新增独立的 `MaterialListingController`，提供多语言 标题描述的 CRUD REST API（查询、保存、删除），遵循现有分模块保存模式。
- 在前端产品编辑页和详情页各新增一个"标题描述"选项卡，支持语言切换（英文/德语/法语/西班牙语/意大利语/荷兰语/波兰语/瑞典语/土耳其语/日本语/俄语等），标题编辑用普通输入框，描述编辑复用现有 TinyMCE 富文本编辑器组件生成简洁 HTML。
- 新增对应前端 API 文件 `materialListingApi.js`。
- 不修改现有 `t_erp_material` 表结构、不影响现有产品保存/查询/删除逻辑。

## Capabilities

### New Capabilities
- `product-multilang-listing`: 产品多语言 标题描述（标题+HTML描述）的独立存储、编辑和展示能力

### Modified Capabilities
<!-- 无需修改现有 spec -->

## Impact

- **数据库**：ERP 库新增表 `t_erp_material_listing`；需同步到 `init-config/mysql/数据库结构/db_erp/` 初始化 SQL。
- **后端**：`wimoor-erp/erp-boot` 新增 entity、mapper、service、controller；不改动现有 Material 相关类。
- **前端**：`wimoorui/src/views/erp/baseinfo/material/editinfo/` 新增 tab 组件 `listing.vue`；`wimoorui/src/views/erp/baseinfo/material/details/` 新增展示 section；新增 API 文件。
- **依赖**：无新外部依赖（TinyMCE 已存在于项目中）。
- **权限**：需在 Admin 菜单中为新 tab 注册菜单按钮权限 `erp:material:listing:edit`。
