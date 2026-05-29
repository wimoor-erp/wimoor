# 产品编辑模块化保存方案

## 背景

产品编辑页面（`editinfo/index.vue`）当前使用一个全局"提交"按钮，将所有模块数据（基本信息、组合信息、采购信息、规格信息、辅料关联、物流信息、图片视频）一次性提交到 `/erp/api/v1/material/saveData`。

需求：将编辑拆分为按左侧菜单的每个模块独立更新，即每个模块各有自己的"保存"按钮，调用独立接口完成局部更新。

## 模块拆分

| 模块 | 前端组件 | 后端接口 | 备注 |
|------|---------|---------|------|
| 基本信息 | `base.vue` | `POST /erp/api/v1/material/saveBaseInfo` (新) | 含产品图片、SKU、名称、品牌、品类、标签等 |
| 组合信息 | `assemble_info.vue` | `POST /erp/api/v1/material/saveAssembly` (新) | 组装清单 |
| 采购信息 | `purchase.vue` | `POST /erp/api/v1/material/saveSupplier` (新) | 供应商列表、采购成本 |
| 规格信息 | `specs.vue` | `POST /erp/api/v1/material/saveSpecs` (新) | 尺寸重量 |
| 辅料关联 | `consumables.vue` | `POST /erp/api/v1/material/saveConsumable` (新) | 辅料列表 |
| 物流信息 | `logistics.vue` | `POST /erp/api/v1/material/saveCustoms` (已有) | 海关信息 |
| 图片视频 | `MediaEditor.vue` | 已独立（media CRUD API） | 无需改动 |

## 实现方案

### 后端新增接口

在 `MaterialController.java` 新增 5 个分模块保存端点，复用 `MaterialServiceImpl` 中已有的私有方法（适当改为 public 或新增 service 方法）：

1. `saveBaseInfo` - 保存基本信息（material 主表 + 标签 + 图片）
2. `saveAssembly` - 保存组合信息
3. `saveSupplier` - 保存采购/供应商信息
4. `saveSpecs` - 保存规格尺寸
5. `saveConsumable` - 保存辅料关联

### 前端改造

1. 移除全局 `el-form` 包裹和全局提交按钮
2. 每个子组件内部增加独立的保存按钮和表单校验
3. 新增对应的 API 方法到 `materialApi.js`
4. 每个子组件 emit `saved` 事件通知父组件更新状态

### 约束

- 新建产品仍使用全局提交（必须先创建基本信息才有 material ID）
- 编辑已有产品时，各模块独立保存
- 图片视频模块已经是独立的 MediaEditor，无需改动
