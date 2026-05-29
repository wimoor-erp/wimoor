## Context

当前 Wimoor ERP 产品主档表 `t_erp_material` 仅有 `name`（中文名称）和 `remark`（备注）两个文本字段，没有多语言标题和 HTML 富文本描述的存储能力。运营人员将商品刊登到 eBay、Amazon 多语言站点时，需要为不同语言维护独立的标题和产品详情 HTML，当前系统缺少此能力。

现有产品编辑/详情页采用"左侧锚点 tab + 右侧滚动 section"的交互模式，每个功能区域独立为 Vue 组件，保存时按模块调用独立 API（`/saveBaseInfo`、`/saveSupplier` 等）。前端已集成 TinyMCE 富文本编辑器（用于系统通知、记事本等场景）。

## Goals / Non-Goals

**Goals:**
- 为每个 ERP 产品提供按语言独立存储的 Listing 标题和 HTML 描述
- 在产品编辑页和详情页提供"标题描述"选项卡，支持多语言切换和富文本编辑
- 输出的 HTML 描述可直接用于 eBay 等平台 Listing 商品详情
- 完全独立于现有产品保存流程，不影响原有业务逻辑

**Non-Goals:**
- 不做平台自动刊登/推送功能（仅存储和编辑）
- 不做翻译功能（用户手动维护各语言内容）
- 不修改 Amazon 侧的 `t_product_info` 表或已有 Amazon Listing 流程
- 不做描述模板库/共享模板功能
- 不做版本历史/变更追踪

## Decisions

### 1. 独立表 vs 扩展主表

**决策**：新建独立关联表 `t_erp_material_listing`，而非在主表增加字段。

**理由**：
- 多语言是一对多关系（一个产品 N 种语言），不适合扁平加列
- 遵循现有模式（`t_erp_material_customs` 也是按 materialid+country 拆分的独立表）
- 不影响主表查询性能
- 避免 ALTER TABLE 对大表的锁定风险

**备选方案**：JSON 字段存储 → 不便于按语言查询和索引，且 MySQL 5.7/8 JSON 性能不如直接列

### 2. 语言标识方式

**决策**：`lang` 字段使用 IETF BCP 47 简码（如 `en`、`de`、`fr`、`es`、`it`、`nl`、`pl`、`sv`、`tr`、`ja`、`ru`），VARCHAR(10)。

**理由**：
- 系统已有 `t_marketplace.language` 字段使用类似语言码
- 用户截图显示的语言列表与跨境电商主流站点对应
- 简短固定码便于前端 tab 映射和 API 参数传递

### 3. 描述字段类型

**决策**：`description` 使用 MEDIUMTEXT（最大 16MB）。

**理由**：
- eBay Listing 描述可能包含大量 HTML + 内联图片 base64/URL
- 普通 TEXT 限 64KB 可能不够长描述
- MEDIUMTEXT 足够应对所有合理场景

### 4. 富文本编辑器选型

**决策**：复用现有 TinyMCE 组件（`wimoorui/src/components/TinyMCE/TinyMCEEditor.vue`）。

**理由**：
- 项目中已有 TinyMCE 组件并在系统通知/记事本中使用
- TinyMCE 输出干净的 HTML，适合直接用于 eBay 等平台
- Quill 全局 Editor 组件输出 Delta JSON 格式，不适合直接生成 HTML Listing
- 无需引入新依赖

### 5. API 设计

**决策**：新建独立 Controller `MaterialListingController`，路径前缀 `/erp/api/v1/material/listing`。

**理由**：
- 遵循现有分模块 Controller 模式（如 `ZipRarUploadController` 独立于 `MaterialController`）
- 避免进一步膨胀已超 1700 行的 `MaterialController`
- API 独立便于后续权限细粒度控制

**接口清单**：
| Method | Path | 说明 |
|--------|------|------|
| GET | `/erp/api/v1/material/listing/list?materialid={id}` | 获取某产品所有语言的 标题描述 |
| GET | `/erp/api/v1/material/listing/get?materialid={id}&lang={lang}` | 获取某产品指定语言的 标题描述 |
| POST | `/erp/api/v1/material/listing/save` | 保存（新增或更新）某语言的 标题描述 |
| DELETE | `/erp/api/v1/material/listing/delete?id={id}` | 删除指定 Listing 记录 |

### 6. 前端 Tab 位置

**决策**：在编辑页/详情页的"图片视频"之前插入"标题描述"选项卡。

**理由**：
- 标题描述属于产品基本信息范畴，逻辑上位于"规格信息"之后、"图片视频"之前
- 与图片无关联，不应混入媒体 tab

### 7. 权限控制

**决策**：复用现有 `v-hasPerm` 指令，新增权限标识 `erp:material:listing:edit`。

**理由**：
- 遵循现有权限体系
- 允许管理员对 Listing 编辑能力进行角色级控制

## Risks / Trade-offs

- **[大文本性能]** → MEDIUMTEXT 字段在列表查询中不应 SELECT *；API 接口按需返回 title 摘要或完整 description，前端列表页不加载 description 内容。
- **[TinyMCE 输出安全]** → 后端保存时对 HTML 做 XSS 过滤（移除 `<script>`、`on*` 事件属性），确保存储的 HTML 安全可直接渲染。使用服务端 HTML 白名单清洗（如 jsoup）。
- **[语言列表硬编码]** → 初期前端硬编码支持的语言列表（与用户截图一致）。后续如需扩展可改为从 `t_marketplace` 读取。这是可接受的短期方案。
- **[首次加载空数据]** → 新 tab 在产品无任何 Listing 数据时显示空状态提示，引导用户选择语言后开始编辑。
