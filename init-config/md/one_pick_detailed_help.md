# 配货页面 (one_pick.vue) 详细帮助文档

## 1. 页面概述

**配货页面**是 Wimoor ERP 系统中 FBA 发货流程的第一个步骤，主要负责展示和管理待发货的商品列表，允许用户进行配货操作、修改发货地址、查看商品详情等功能。

**核心功能点**：
- 展示货件计划的基本信息和商品列表
- 支持修改商品的配货数量和状态
- 支持修改发货地址
- 支持生成和打印配货单、标签等文档
- 支持完成配货操作，进入下一个发货步骤

**技术栈**：
- 前端：Vue 3 + Composition API + Element Plus
- 后端：Spring Boot + MyBatis Plus
- 数据可视化：ECharts

## 2. 功能模块

### 2.1 页面布局

```vue
<template>
  <div class="box-margin">
    <div class="pag-radius-bor mar-bot">
      <div class="con-body"> 
        <Header ref="headerRef" ></Header>
        <!-- 地址信息 -->
        <!-- 商品列表 -->
        <el-table :data="planData.itemlist" border >
          <!-- 商品信息列 -->
        </el-table>
        <!-- 操作按钮 -->
      </div>
    </div>
  </div>
</template>
```

### 2.2 核心功能模块

| 模块名称 | 功能描述 | 实现方式 |
|---------|---------|----------|
| 头部信息 | 展示货件计划的基本信息，如名称、状态等 | 引入 Header 组件 |
| 地址管理 | 展示和修改发货地址 | 弹窗选择地址，调用 addressApi.getAddress |
| 商品列表 | 展示待配货的商品信息，支持编辑数量 | el-table 组件，支持行内编辑 |
| 文档生成 | 生成配货单、标签等文档 | 调用 shipmentQuotaApi 相关方法 |
| 配货操作 | 完成配货，进入下一个步骤 | 调用 shipmentplanApi.doneInboundPlan |
| 库存管理 | 查看商品库存信息 | 调用 inventoryApi.getInventory |

## 3. 核心功能分析

### 3.1 数据加载

**功能描述**：页面加载时，通过 URL 参数获取计划 ID，然后调用 API 获取货件计划详情。

**实现代码**：

```javascript
onMounted(() => {
  let planid = route.query.formid;
  if (planid) {
    loadData(planid);
  }
});

function loadData(planid) {
  state.planData.id = planid;
  shipmentplanApi.getPlanInfo({"formid": planid}).then((res) => {
    if (res.code == 200) {
      state.planData = res.data;
      // 处理数据...
    }
  });
}
```

**后端实现**：对应 `ShipInboundPlanV2Controller.getPlanInfoAction` 方法，返回货件计划的详细信息。

### 3.2 配货操作

**功能描述**：用户确认配货完成后，调用 API 完成配货操作，更新货件计划状态。

**实现代码**：

```javascript
function donePlan() {
  ElMessageBox.confirm('确认完成配货吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    shipmentplanApi.doneInboundPlan({"formid": state.planData.id}).then(res => {
      if (res.code == 200) {
        ElMessage.success('操作成功');
        // 跳转到下一个步骤
        emit('nextStep');
      }
    });
  });
}
```

**后端实现**：对应 `ShipInboundPlanV2Controller.doneInboundPlan` 方法，更新货件计划状态为已完成配货。

### 3.3 地址修改

**功能描述**：用户可以修改发货地址，选择已有的地址或新增地址。

**实现代码**：

```javascript
function changeAddress() {
  // 打开地址选择弹窗
  const dialogRef = ElMessageBox.confirm(
    `<div style="height:450px;"><AddressSelect ref="addressSelectRef" @select="selectAddress" :companyid="userInfo.companyid" :addressid="state.planData.addressid" /></div>`,
    '选择地址',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
      dangerouslyUseHTMLString: true
    }
  );
}

function selectAddress(rows) {
  // 选择地址后调用 API 更新
  shipmentplanApi.changeAddress({"formid": state.planData.id, "addressid": rows[0].id}).then((res) => {
    if (res.code == 200) {
      ElMessage.success('操作成功');
      loadData(state.planData.id);
    }
  });
}
```

**后端实现**：对应 `ShipInboundPlanV2Controller.changeAddressAction` 方法，更新货件计划的发货地址。

### 3.4 商品编辑

**功能描述**：用户可以编辑商品的配货数量、贴标信息等。

**实现代码**：

```javascript
function handleEdit(row) {
  state.operatorRow = JSON.parse(JSON.stringify(row));
  state.dialogVisible = true;
}

function handleConfirm() {
  shipmentplanApi.updatePlanItem(state.operatorRow).then((res) => {
    if (res.code == 200) {
      ElMessage.success('操作成功');
      state.dialogVisible = false;
      loadData(state.planData.id);
    }
  });
}
```

**后端实现**：对应 `ShipInboundPlanV2Controller.updatePlanItemAction` 方法，更新商品的配货信息。

### 3.5 文档生成

**功能描述**：生成和打印配货单、标签等文档。

**实现代码**：

```javascript
function downLabel(ftype) {
  let formids = [];
  formids.push(state.planData.id);
  if (ftype == 'pdf') {
    shipmentQuotaApi.downPDFShipForm(ftype, formids, state.planData.number + ftypename + "-配货单");
  } else if (ftype == 'label') {
    shipmentQuotaApi.downPDFLabel({"formid": state.planData.id}, state.planData.number + "-");
  } else if (ftype == 'excel') {
    shipmentQuotaApi.downExcelLabel({"formid": state.planData.id}, state.planData.number + "-");
  }
}
```

**后端实现**：对应 `ShipQuotaController` 相关方法，生成和下载各种文档。

## 4. 前端 API 调用

### 4.1 API 模块引入

```javascript
import shipmentBoxApi from '@/api/erp/shipv2/shipmentBoxApi.js';
import addressApi from '@/api/amazon/inbound/addressApi.js';
import shipmentplanApi from '@/api/erp/shipv2/shipmentplanApi.js';
import shipmentQuotaApi from '@/api/erp/shipv2/shipmentQuotaApi.js';
```

### 4.2 核心 API 调用

| API 方法 | 功能描述 | 参数说明 | 后端对应方法 |
|---------|---------|---------|------------|
| shipmentplanApi.getPlanInfo | 获取货件计划详情 | `{formid: 计划ID}` | ShipInboundPlanV2Controller.getPlanInfoAction |
| shipmentplanApi.changeInboundPlan | 提交配货信息 | `{货件计划对象}` | ShipInboundPlanV2Controller.changeInboundPlanAction |
| shipmentplanApi.changeAddress | 修改发货地址 | `{formid: 计划ID, addressid: 地址ID}` | ShipInboundPlanV2Controller.changeAddressAction |
| shipmentplanApi.updatePlanItem | 更新商品信息 | `{商品对象}` | ShipInboundPlanV2Controller.updatePlanItemAction |
| shipmentplanApi.doneInboundPlan | 完成配货 | `{formid: 计划ID}` | ShipInboundPlanV2Controller.doneInboundPlan |
| shipmentplanApi.confirmInboundPlan | 确认配货 | `{formid: 计划ID}` | ShipInboundPlanV2Controller.confirmInboundPlan |
| addressApi.getAddress | 获取地址列表 | `{companyid: 公司ID}` | AddressController.getAddressList |
| shipmentQuotaApi.downPDFShipForm | 下载配货单 | `{ftype: 类型, formids: 计划ID列表, filename: 文件名}` | ShipQuotaController.downPDFShipForm |
| shipmentQuotaApi.downPDFLabel | 下载标签 | `{formid: 计划ID}` | ShipQuotaController.downPDFLabel |

## 5. 后端 API 实现

### 5.1 核心控制器：ShipInboundPlanV2Controller

**控制器路径**：`com.wimoor.amazon.inboundV2.controller.ShipInboundPlanV2Controller`

**核心方法**：

| 方法名 | URL | 功能描述 | 请求方式 |
|-------|-----|---------|--------|
| getPlanInfoAction | /api/v2/shipInboundPlan/getPlanInfo | 获取货件计划详情 | GET |
| changeInboundPlanAction | /api/v2/shipInboundPlan/changeInboundPlan | 提交配货信息 | POST |
| changeAddressAction | /api/v2/shipInboundPlan/changeAddress | 修改发货地址 | GET |
| updatePlanItemAction | /api/v2/shipInboundPlan/updatePlanItem | 更新商品信息 | POST |
| doneInboundPlan | /api/v2/shipInboundPlan/doneInboundPlan | 完成配货 | GET |
| confirmInboundPlan | /api/v2/shipInboundPlan/confirmInboundPlan | 确认配货 | GET |

**示例实现**：

```java
@ApiOperation(value = "获取货件计划")
@GetMapping("/getPlanInfo")
public Result<ShipPlanVo> getPlanInfoAction(String formid) {
    UserInfo user = UserInfoContext.get();
    try {
        if (StrUtil.isEmptyIfStr(formid)) {
            throw new BizException("计划ID不能为空");
        }
        ShipPlanVo vo = shipInboundPlanV2Service.getPlanBaseInfo(formid, user);
        return Result.success(vo);
    } catch (Exception e) {
        e.printStackTrace();
    }
    return Result.success(null);
}
```

## 6. 数据模型

### 6.1 核心实体类

#### ShipInboundPlan（货件计划）

**表名**：`t_erp_ship_v2_inboundplan`

| 字段名 | 数据类型 | 描述 |
|-------|---------|------|
| id | String | 计划ID |
| name | String | 计划名称 |
| number | String | 计划编码 |
| source_address | String | 发货地址ID |
| groupid | String | 店铺ID |
| marketplaceid | String | 站点ID |
| amazonauthid | String | 授权ID |
| warehouseid | String | 仓库ID |
| auditstatus | Integer | 审核状态 |
| shopid | String | 公司ID |
| createtime | Date | 创建时间 |
| creator | String | 创建人 |

#### ShipInboundItem（货件商品）

**表名**：`t_erp_ship_v2_inbounditem`

| 字段名 | 数据类型 | 描述 |
|-------|---------|------|
| id | String | 商品ID |
| formid | String | 计划ID |
| sku | String | 平台SKU |
| msku | String | ERP本地SKU |
| quantity | Integer | 订单数量 |
| confirm_quantity | Integer | 发货量 |
| label_owner | String | 贴标责任人 |
| prep_owner | String | 预备信息处理人 |
| expiration | Date | 过期日期 |

### 6.2 数据传输对象 (DTO)

#### ShipPlanListDTO

用于查询货件计划列表的参数对象，包含分页信息和查询条件。

#### ShipFormDTO

用于发货库存锁定和出库操作的参数对象，包含仓库ID、商品列表等信息。

## 7. 业务流程

### 7.1 配货流程

1. **初始化**：页面加载时，通过 URL 参数获取计划 ID，调用 `shipmentplanApi.getPlanInfo` 获取货件计划详情。

2. **配货操作**：
   - 用户查看商品列表，确认配货数量
   - 可以修改商品的配货数量和状态
   - 可以修改发货地址

3. **完成配货**：
   - 用户点击「完成配货」按钮
   - 调用 `shipmentplanApi.doneInboundPlan` 更新货件计划状态
   - 状态更新成功后，跳转到下一个发货步骤（装箱）

### 7.2 地址修改流程

1. **打开地址选择弹窗**：用户点击「修改地址」按钮，打开地址选择弹窗。

2. **获取地址列表**：调用 `addressApi.getAddress` 获取用户的地址列表。

3. **选择地址**：用户从列表中选择一个地址。

4. **更新地址**：调用 `shipmentplanApi.changeAddress` 更新货件计划的发货地址。

5. **刷新数据**：地址更新成功后，刷新页面数据，显示新的发货地址。

### 7.3 商品编辑流程

1. **打开编辑弹窗**：用户点击商品行的「编辑」按钮，打开编辑弹窗。

2. **修改信息**：用户修改商品的配货数量、贴标信息等。

3. **保存修改**：用户点击「保存」按钮，调用 `shipmentplanApi.updatePlanItem` 更新商品信息。

4. **刷新数据**：商品信息更新成功后，刷新页面数据，显示新的商品信息。

## 8. 技术要点

### 8.1 前端技术要点

1. **Composition API**：使用 Vue 3 的 Composition API 组织代码，提高代码可读性和可维护性。

2. **响应式状态管理**：使用 `reactive` 和 `ref` 管理页面状态，确保数据变化能够及时反映到 UI 上。

3. **组件通信**：使用 `ref` 和 `emit` 实现组件之间的通信，如头部组件和主页面之间的交互。

4. **生命周期钩子**：使用 `onMounted` 钩子在页面加载时初始化数据。

5. **异步操作处理**：使用 `async/await` 和 Promise 处理 API 调用等异步操作。

### 8.2 后端技术要点

1. **RESTful API 设计**：遵循 RESTful 设计风格，使用合适的 HTTP 方法和状态码。

2. **事务管理**：使用 `@Transactional` 注解管理事务，确保数据操作的一致性。

3. **用户认证**：使用 `UserInfoContext` 获取当前用户信息，实现权限控制。

4. **数据校验**：使用 `@Valid` 和 `@NotNull` 等注解进行数据校验，确保数据的合法性。

5. **异常处理**：使用 `try-catch` 捕获和处理异常，返回友好的错误信息。

## 9. 常见问题与解决方案

### 9.1 常见问题

| 问题描述 | 可能原因 | 解决方案 |
|---------|---------|----------|
| 页面加载失败 | 计划 ID 不存在或无权限 | 检查 URL 参数是否正确，确认用户权限 |
| 配货数量修改失败 | 库存不足或参数错误 | 检查库存是否充足，确认参数格式正确 |
| 地址修改失败 | 地址 ID 不存在或无权限 | 检查地址是否存在，确认用户权限 |
| 文档下载失败 | 生成文档时出错 | 检查服务器状态，确认参数正确 |

### 9.2 性能优化建议

1. **前端优化**：
   - 使用虚拟滚动处理大量商品数据
   - 合理使用缓存，减少重复 API 调用
   - 优化组件渲染，避免不必要的重渲染

2. **后端优化**：
   - 使用索引优化数据库查询
   - 合理使用缓存，减少数据库访问
   - 优化 API 响应时间，提高并发处理能力

## 10. 代码优化建议

### 10.1 前端代码优化

1. **代码结构优化**：
   - 将复杂的业务逻辑拆分为多个子函数，提高代码可读性
   - 使用自定义 Hook 封装重复的逻辑，如 API 调用、数据处理等

2. **性能优化**：
   - 使用 `computed` 缓存计算结果，避免重复计算
   - 使用 `watchEffect` 替代 `watch`，减少不必要的依赖追踪

3. **错误处理优化**：
   - 统一处理 API 错误，提供友好的错误提示
   - 使用 `try-catch` 捕获异步操作中的错误

### 10.2 后端代码优化

1. **代码结构优化**：
   - 将业务逻辑从控制器中分离到服务层，提高代码可维护性
   - 使用 DTO 封装请求和响应数据，避免直接使用实体类

2. **性能优化**：
   - 使用批量操作减少数据库访问次数
   - 合理使用索引，优化查询性能

3. **安全性优化**：
   - 加强参数校验，防止 SQL 注入等攻击
   - 使用 HTTPS 加密传输数据，提高安全性

## 11. 总结

**配货页面**是 Wimoor ERP 系统中 FBA 发货流程的重要组成部分，主要负责商品的配货操作和管理。通过本文档的详细解析，我们可以了解到：

1. **页面结构**：采用模块化设计，包含头部信息、地址管理、商品列表等核心模块。

2. **功能实现**：通过前端 API 调用和后端控制器的配合，实现了货件计划详情获取、配货信息提交、地址修改、商品编辑等核心功能。

3. **数据模型**：使用 `ShipInboundPlan` 和 `ShipInboundItem` 等实体类，构建了完整的数据模型体系。

4. **业务流程**：清晰的配货流程，从初始化到完成配货，每一步都有明确的操作和状态管理。

5. **技术要点**：使用了 Vue 3 + Composition API + Element Plus 等前端技术，以及 Spring Boot + MyBatis Plus 等后端技术，构建了一个高效、可靠的配货管理系统。

通过不断优化和改进，配货页面将为用户提供更加便捷、高效的配货体验，助力企业更好地管理 FBA 发货流程。