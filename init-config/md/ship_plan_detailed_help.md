# FBA发货规划页面详细帮助文档

## 1. 页面概述

FBA发货规划页面是Wimoor系统中用于管理和规划亚马逊FBA发货的核心功能模块。该页面提供了全面的产品发货数据展示、库存分析、发货计划管理等功能，帮助用户制定合理的FBA发货策略，优化库存管理，提高运营效率。

**主要功能亮点：**
- 产品发货数据的可视化展示
- 智能库存分析和预警
- 灵活的发货计划管理
- 多维度数据报表
- 详细的库存和发货记录查询

**页面位置：** `wimoor666\wimoor-ui\src\views\erp\ship\ship_plan\index.vue`

## 2. 功能模块

### 2.1 头部搜索和筛选模块

头部模块包含各种搜索和筛选条件，用于精确查询需要管理的产品数据。

**核心功能：**
- 仓库选择
- 店铺/站点筛选
- 产品状态筛选
- 搜索条件设置
- 数据汇总统计

### 2.2 产品列表模块

产品列表是页面的核心部分，展示了所有符合条件的产品信息，支持排序、筛选和展开查看详情。

**核心功能：**
- 产品基本信息展示（SKU、名称、标签等）
- 发货需求量和实际发货总量显示
- 可用库存和可组装库存信息
- 批量展开/折叠操作
- 行点击事件处理

### 2.3 展开详情模块

点击产品行展开按钮，可查看该产品的详细发货计划信息，支持编辑和管理发货计划。

**核心功能：**
- 按国家/地区展示发货计划
- 发货数量编辑
- 运输方式选择
- 海外仓选择
- 发货计划拆分

### 2.4 报表和分析模块

提供产品销量和预计到货的可视化报表，帮助用户分析销售趋势和库存状况。

**核心功能：**
- 销量报表（柱状图）
- 预计到货报表（折线图）
- 库存差异分析（FBA库存差异图表）

### 2.5 辅助功能模块

页面还包含多种辅助功能，增强用户操作体验和数据管理能力。

**核心功能：**
- 备货周期管理
- 库存详情查询
- 库存库龄分析
- 产品备注管理
- 产品组装管理

## 3. 核心功能解析

### 3.1 发货计划管理

**功能描述：** 允许用户为产品创建、编辑和删除发货计划，支持按国家/地区细分。

**实现原理：**
1. 通过 `planApi.getPlanList` 获取产品列表数据
2. 点击"编辑"按钮进入编辑模式
3. 在展开的详情表格中设置发货数量和运输方式
4. 点击"保存"按钮通过 `planApi.save` 保存发货计划
5. 点击"移除"按钮通过 `planApi.remove` 删除发货计划

**关键代码：**
- 加载数据：`loadTableData` 函数（800行）
- 保存计划：`savePlanItem` 函数（940行）
- 提交表单：`submitForm` 函数（952行）
- 删除计划：`handleDelete` 函数（929行）

### 3.2 库存分析和预警

**功能描述：** 提供产品库存的详细分析，包括可用库存、预留库存、待入库库存等信息，并通过图表展示库存差异。

**实现原理：**
1. 通过 `inventoryApi.getInventory` 获取库存数据
2. 通过 `inventoryRptApi.syncInventorySupply` 同步FBA库存
3. 点击库存数量查看详细库存信息
4. 点击FBA库存差异图表查看库存差异分析

**关键代码：**
- 刷新库存：`refreshInventory` 函数（563行）
- 库存图表：`FBAinventoryChart` 函数（490行）
- 查看库存详情：`handleShowInventory` 函数（1245行）

### 3.3 发货计划拆分

**功能描述：** 支持将一个发货计划拆分为多个子计划，适用于不同运输方式或不同批次的发货需求。

**实现原理：**
1. 点击"拆分"按钮打开拆分对话框
2. 设置拆分后的子计划数量和运输方式
3. 保存拆分结果并更新主计划

**关键代码：**
- 显示拆分对话框：`showSplitRow` 函数（38行）
- 处理拆分结果：`handleSplitRow` 函数（764行）
- 计划拆分API：`planApi.subsplit` 调用（882行）

### 3.4 销量和到货分析

**功能描述：** 通过图表展示产品的销量趋势和预计到货情况，帮助用户做出更合理的发货决策。

**实现原理：**
1. 点击销量报表图标打开销量图表对话框
2. 点击预计到货报表图标打开到货图表对话框
3. 图表数据通过后端API获取并使用ECharts渲染

**关键代码：**
- 销量报表：`handlesaleChart` 函数（684行）
- 到货报表：`handlarrivalChart` 函数（784行）

### 3.5 产品组装管理

**功能描述：** 对于组合产品，显示其组成部分和可组装数量，帮助用户了解组合产品的库存状况。

**实现原理：**
1. 点击组合产品标签查看组装详情
2. 通过 `sublit` API获取组装部件信息
3. 显示可组装数量和部件库存状态

**关键代码：**
- 显示组装对话框：`handleAssemblyShow` 函数（549行）
- 获取组装列表：`getAssembliyList` 函数（609行）

## 4. 前端API调用

### 4.1 核心API调用

| API名称 | 调用函数 | 功能描述 | 参数说明 | 调用位置 |
|--------|---------|---------|---------|----------|
| 获取计划列表 | `planApi.getPlanList` | 获取产品发货计划列表 | `{plantype: "ship", ...筛选条件}` | 800行 |
| 获取国家数据 | `planApi.getExpandCountryData` | 获取产品按国家细分的发货数据 | `{groupid, msku, warehouseid, plantype, ...}` | 709行, 1197行 |
| 保存发货计划 | `planApi.save` | 保存产品发货计划 | `[发货计划对象列表]` | 941行 |
| 删除发货计划 | `planApi.remove` | 删除产品发货计划 | `{warehouseid, msku, groupid}` | 931行 |
| 计划拆分 | `planApi.subsplit` | 获取计划拆分数据 | `{发货计划对象}` | 882行 |
| 同步库存 | `inventoryRptApi.syncInventorySupply` | 同步FBA库存数据 | `{skus, groupid, marketplaceid}` | 566行 |
| 获取库存 | `inventoryApi.getInventory` | 获取产品库存数据 | `{warehouseid, materialid}` | 638行 |
| 获取海外仓 | `warehouseApi.getOversaWarehouse` | 获取可用海外仓列表 | `{ftype: "oversea_usable", groupid, country}` | 620行 |
| 获取运输方式 | `transportationApi.getTransTypeAll` | 获取所有运输方式 | 无 | 1223行 |
| 隐藏产品 | `markApi.hide` | 隐藏产品 | `{materialid}` | 1065行 |
| 显示产品 | `markApi.show` | 显示产品 | `{materialid}` | 1079行 |
| 获取组装列表 | `sublit` | 获取产品组装部件列表 | `{materialid, warehouseid}` | 612行 |

### 4.2 API依赖文件

| API文件 | 路径 | 功能描述 |
|---------|------|----------|
| planApi.js | `@/api/erp/ship/planApi.js` | 发货计划相关API |
| inventoryApi.js | `@/api/erp/inventory/inventoryApi.js` | 库存相关API |
| inventoryRptApi.js | `@/api/amazon/inventory/inventoryRptApi.js` | 库存报表相关API |
| warehouseApi.js | `@/api/erp/warehouse/warehouseApi.js` | 仓库相关API |
| transportationApi.js | `@/api/erp/ship/transportationApi.js` | 运输方式相关API |
| markApi.js | `@/api/erp/material/markApi.js` | 产品标记相关API |
| assemblyApi.js | `@/api/erp/assembly/assemblyApi.js` | 产品组装相关API |

## 5. 后端API实现

### 5.1 发货计划控制器

**控制器路径：** `wimoor666\wimoor-amazon\amazon-boot\src\main\java\com\wimoor\amazon\product\controller\AmzProductSalesPlanController.java`

**核心接口：**

| API路径 | 方法 | 功能描述 | 请求参数 | 响应结果 |
|---------|------|----------|----------|----------|
| `/api/v1/product/salesplan/refreshPlanData` | GET | 计划数据刷新 | 无 | 成功状态 |
| `/api/v1/product/salesplan/refreshDataByGroup` | GET | 按店铺刷新计划 | `groupid` | 成功状态 |
| `/api/v1/product/salesplan/refreshDataBySKU` | GET | 按SKU刷新计划 | `groupid, marketplaceid, sku` | 成功状态 |
| `/api/v1/product/salesplan/getExpandCountryData` | POST | 获取国家细分数据 | `PlanDetailDTO` | 国家细分数据列表 |
| `/api/v1/product/salesplan/getPlanModel` | POST | 获取计划模型数据 | `PlanDTO` | 计划数据分页列表 |

### 5.2 发货计划项目控制器

**控制器路径：** `wimoor666\wimoor-amazon\amazon-boot\src\main\java\com\wimoor\amazon\product\controller\AmzProductSalesPlanShipItemController.java`

**核心接口：**

| API路径 | 方法 | 功能描述 | 请求参数 | 响应结果 |
|---------|------|----------|----------|----------|
| `/api/v1/product/salesplan/shipItem/save` | POST | 保存发货计划项目 | `List<AmzProductSalesPlanShipItem>` | 保存数量 |
| `/api/v1/product/salesplan/shipItem/remove` | DELETE | 删除发货计划项目 | `groupid, warehouseid, msku` | 成功状态 |
| `/api/v1/product/salesplan/shipItem/subsplit` | POST | 计划拆分 | `AmzProductSalesPlanShipItem` | 拆分后的计划列表 |
| `/api/v1/product/salesplan/shipItem/clear` | GET | 清除计划 | `groupid, warehouseid` | 成功状态 |
| `/api/v1/product/salesplan/shipItem/getSummary` | GET | 获取计划汇总 | `groupid, warehouseid` | 汇总数据 |
| `/api/v1/product/salesplan/shipItem/list` | GET | 获取计划列表 | `groupid, warehouseid` | 计划列表 |
| `/api/v1/product/salesplan/shipItem/batch` | POST | 计划打包 | `List<AmzProductSalesPlanShipItem>` | 批次号 |
| `/api/v1/product/salesplan/shipItem/removeBatch` | POST | 计划归档 | `batchnumber` | 成功状态 |

## 6. 数据模型

### 6.1 发货计划项目实体

**实体类：** `AmzProductSalesPlanShipItem`
**表名：** `t_amz_product_sales_plan_ship_item`

| 字段名 | 数据类型 | 描述 |
|-------|---------|------|
| id | BigInteger | 主键ID |
| sku | String | 产品SKU |
| msku | String | 主SKU |
| shopid | BigInteger | 店铺ID |
| marketplaceid | String | 市场ID |
| groupid | BigInteger | 分组ID |
| amazonauthid | BigInteger | 亚马逊授权ID |
| warehouseid | BigInteger | 仓库ID |
| overseaid | BigInteger | 海外仓ID |
| transtype | BigInteger | 运输方式ID |
| batchnumber | String | 批次号 |
| amount | Integer | 数量 |
| aftersalesday | Integer | 售后天数 |
| opttime | LocalDateTime | 操作时间 |
| operator | BigInteger | 操作人ID |
| isdefault | Boolean | 是否默认 |
| subnum | Integer | 子计划数量（非数据库字段） |
| subList | List<AmzProductSalesPlanShipItem> | 子计划列表（非数据库字段） |

### 6.2 产品销售计划实体

**实体类：** `AmzProductSalesPlan`
**表名：** `t_amz_product_sales_plan`

| 字段名 | 数据类型 | 描述 |
|-------|---------|------|
| id | BigInteger | 主键ID |
| sku | String | 产品SKU |
| msku | String | 主SKU |
| shopid | BigInteger | 店铺ID |
| marketplaceid | String | 市场ID |
| groupid | BigInteger | 分组ID |
| amazonauthid | BigInteger | 亚马逊授权ID |
| shipday | Integer | 发货天数 |
| salesday | Integer | 销售天数 |
| deliveryCycle | Integer | 交付周期 |
| needship | Integer | 需要发货数量 |
| shipMinCycleSale | Integer | 最小发货周期销量 |
| needshipfba | Integer | FBA需要发货数量 |
| avgsales | Integer | 平均销量 |
| needpurchase | Integer | 需要采购数量 |
| opttime | Date | 操作时间 |
| shortTime | LocalDate | 短缺时间 |

## 7. 业务流程

### 7.1 发货计划创建流程

1. **数据加载**：用户设置筛选条件，系统通过 `planApi.getPlanList` 获取产品列表数据
2. **选择产品**：用户在产品列表中找到需要创建发货计划的产品
3. **编辑计划**：点击"编辑"按钮，进入编辑模式
4. **设置参数**：在展开的详情表格中，设置各国家/地区的发货数量、运输方式等参数
5. **保存计划**：点击"保存"按钮，系统通过 `planApi.save` 保存发货计划
6. **数据更新**：保存成功后，系统更新产品列表中的发货状态和数据

### 7.2 库存分析流程

1. **数据获取**：系统通过 `inventoryApi.getInventory` 获取产品库存数据
2. **库存计算**：计算可用库存、可组装库存等关键指标
3. **状态判断**：根据库存数据判断产品库存状态
4. **预警提示**：对库存不足的产品进行预警提示
5. **数据展示**：在产品列表中展示库存状态和相关数据

### 7.3 计划拆分流程

1. **选择计划**：在展开的详情表格中，找到需要拆分的发货计划
2. **点击拆分**：点击"拆分"按钮，打开拆分对话框
3. **设置参数**：在对话框中设置拆分后的子计划数量、运输方式等参数
4. **确认拆分**：点击"确认"按钮，系统通过 `planApi.subsplit` 处理拆分数据
5. **更新计划**：拆分完成后，系统更新发货计划数据

## 8. 操作指南

### 8.1 基本操作

#### 8.1.1 搜索和筛选产品

1. 在页面顶部的搜索栏中设置筛选条件
2. 选择仓库、店铺、站点等筛选条件
3. 点击"查询"按钮，系统会根据条件加载产品数据

#### 8.1.2 查看产品详情

1. 在产品列表中找到需要查看的产品
2. 点击产品行左侧的展开按钮，或点击"展开全部"按钮查看所有产品详情
3. 在展开的详情表格中查看产品的详细发货数据

#### 8.1.3 创建发货计划

1. 找到需要创建发货计划的产品，点击"编辑"按钮
2. 在展开的详情表格中，为各国家/地区设置发货数量
3. 选择合适的运输方式和海外仓（如需）
4. 点击"保存"按钮，完成发货计划创建

#### 8.1.4 修改发货计划

1. 找到需要修改的发货计划，点击"编辑"按钮
2. 修改发货数量、运输方式等参数
3. 点击"保存"按钮，完成发货计划修改

#### 8.1.5 删除发货计划

1. 找到需要删除的发货计划，点击"移除"按钮
2. 在弹出的确认对话框中点击"确定"，完成发货计划删除

### 8.2 高级操作

#### 8.2.1 查看销量报表

1. 在产品列表中找到需要查看销量的产品
2. 点击产品行中的销量报表图标（柱状图）
3. 在弹出的销量报表对话框中查看详细的销量数据

#### 8.2.2 查看预计到货报表

1. 在产品列表中找到需要查看预计到货的产品
2. 点击产品行中的预计到货报表图标（折线图）
3. 在弹出的预计到货报表对话框中查看详细的到货数据

#### 8.2.3 查看库存详情

1. 在产品列表中找到需要查看库存的产品
2. 点击产品行中的可用库存数量
3. 在弹出的库存详情对话框中查看详细的库存数据

#### 8.2.4 计划拆分

1. 在展开的详情表格中，找到需要拆分的发货计划
2. 点击"拆分"按钮，打开拆分对话框
3. 设置拆分后的子计划数量、运输方式等参数
4. 点击"确认"按钮，完成计划拆分

#### 8.2.5 产品组装管理

1. 在产品列表中找到需要查看组装信息的组合产品（带有"组合"标签）
2. 点击"组合"标签，打开组装产品对话框
3. 在对话框中查看产品的组装部件和可组装数量

## 9. 常见问题

### 9.1 库存数据不准确

**问题描述**：产品列表中显示的库存数据与实际库存不符

**解决方法**：
1. 点击库存数量旁边的刷新图标，手动同步FBA库存数据
2. 检查库存数据来源是否正确
3. 确认仓库选择是否正确

### 9.2 无法保存发货计划

**问题描述**：点击"保存"按钮后，发货计划无法保存

**解决方法**：
1. 检查发货数量是否超过可用库存
2. 确认运输方式和海外仓选择是否正确
3. 检查网络连接是否正常
4. 刷新页面后重新尝试

### 9.3 计划拆分失败

**问题描述**：点击"拆分"按钮后，计划拆分失败

**解决方法**：
1. 检查发货计划数据是否完整
2. 确认拆分参数设置是否合理
3. 刷新页面后重新尝试

### 9.4 报表数据加载缓慢

**问题描述**：点击报表图标后，报表数据加载缓慢

**解决方法**：
1. 检查网络连接是否正常
2. 确认产品数据量是否过大
3. 尝试减少时间范围，查看数据量较小的报表

### 9.5 产品无法隐藏

**问题描述**：点击"隐藏产品"后，产品仍然显示在列表中

**解决方法**：
1. 检查产品是否已加入发货计划（已加入计划的产品无法隐藏）
2. 确认操作权限是否足够
3. 刷新页面后重新尝试

## 10. 技术实现细节

### 10.1 前端技术栈

- **框架**：Vue 3 + Composition API
- **UI组件**：Element Plus
- **图表库**：ECharts
- **状态管理**：reactive + ref
- **生命周期**：onMounted, nextTick

### 10.2 后端技术栈

- **框架**：Spring Boot
- **ORM**：MyBatis Plus
- **API风格**：RESTful
- **事务管理**：@Transactional
- **认证授权**：UserInfoContext

### 10.3 性能优化

1. **数据加载优化**：
   - 使用分页查询减少数据传输量
   - 延迟加载非关键数据
   - 缓存常用数据

2. **渲染优化**：
   - 使用虚拟滚动处理大量数据
   - 优化组件渲染性能
   - 减少不必要的DOM操作

3. **交互优化**：
   - 异步处理耗时操作
   - 提供加载状态反馈
   - 优化用户操作流程

### 10.4 代码结构

**前端代码结构**：
- `index.vue`：主页面组件
- `components/expand_table.vue`：展开详情表格组件
- `components/ship_record.vue`：发货记录组件
- `components/remarks_dialog.vue`：备注对话框组件
- `components/split_plan_dialog.vue`：计划拆分对话框组件

**后端代码结构**：
- `AmzProductSalesPlanController.java`：发货计划控制器
- `AmzProductSalesPlanShipItemController.java`：发货计划项目控制器
- `AmzProductSalesPlanShipItem.java`：发货计划项目实体
- `AmzProductSalesPlan.java`：产品销售计划实体

## 11. 总结

FBA发货规划页面是Wimoor系统中一个功能强大、设计合理的核心模块，它通过直观的数据展示、智能的库存分析和灵活的计划管理，为用户提供了全面的FBA发货管理解决方案。

**核心价值**：
- 提高库存管理效率，减少库存积压和断货风险
- 优化发货计划，降低物流成本
- 提供数据支持，帮助用户做出更合理的运营决策
- 简化操作流程，提高工作效率

该页面功能丰富，操作便捷，为用户提供了全面的FBA发货管理解决方案。同时，通过不断的优化和改进，可以进一步提高页面性能和用户体验，为用户创造更大的价值。