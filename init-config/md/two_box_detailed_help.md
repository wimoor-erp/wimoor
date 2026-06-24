# 装箱页面 (two_box.vue) 详细帮助文档

## 1. 页面概述

**装箱页面**是 Wimoor ERP 系统中 FBA 发货流程的第二个步骤，主要负责生成和管理装箱方案，允许用户选择合适的装箱策略、配置箱子信息、提交装箱数据等功能。

**核心功能点**：
- 生成多种装箱方案，支持选择最优方案
- 配置箱子数量和装箱信息
- 提交箱子信息，进入下一个发货步骤
- 支持单箱和混装模式切换
- 支持下载装箱模板和相关文档

**技术栈**：
- 前端：Vue 3 + Composition API + Element Plus
- 后端：Spring Boot + MyBatis Plus
- 数据可视化：ECharts

## 2. 功能模块

### 2.1 页面布局

```vue
<template>
  <div class="box-margin">
    <div class="pag-radius-bor mar-bot" >
      <div class=" two-box" > 
        <Header ref="headerRef" ></Header>
        <el-space>
          <div class="boxgroup-subtext">以下装箱组中的SKU会一同配送，打包时请为包装箱编号，便于识别，装箱组中可包含多个包装箱。  </div>
          <el-button @click="generatePackingOptions" :icon="Refresh" size="small"> {{packagetitle}}</el-button>
          <el-switch
            v-model="planData.areCasesRequired"
            class="ml-2"
            inline-prompt
            @change="handleAreCasesRequired"
            style="--el-switch-on-color: #68cea4; --el-switch-off-color: #ffbb6c"
            active-text="单箱"
            inactive-text="混装"
          />
        </el-space>
        <div  v-loading="optionloading"  element-loading-text="加载装箱方案..." style="min-height:320px;">
          <!-- 装箱方案列表 -->
          <el-tabs v-model="selectPackingOptionId" type="border-card" class="demo-tabs" @tab-click="handleClick">
            <el-tab-pane v-for="(optitem,index) in options" :name="optitem.packingOptionId">
              <!-- 装箱方案内容 -->
            </el-tab-pane>
          </el-tabs>
          <!-- 操作按钮 -->
        </div>
      </div>
    </div>
    <Footer  ref="footerRef"></Footer>
    <BoxTable  ref="boxTableRef" @change="getBoxData"></BoxTable>
    <BoxTableCase  ref="boxTableCaseRef" @change="getBoxData"></BoxTableCase>
    <Table ref="tableRef" ></Table>
  </div>
</template>
```

### 2.2 核心功能模块

| 模块名称 | 功能描述 | 实现方式 |
|---------|---------|----------|
| 头部信息 | 展示货件计划的基本信息，如名称、状态等 | 引入 Header 组件 |
| 装箱方案 | 生成和展示多种装箱方案，支持选择 | el-tabs 组件，调用 shipmentBoxApi.generatePackingOptions |
| 装箱组管理 | 管理每个装箱组的箱子数量和配置 | el-card 组件，支持编辑箱子数量 |
| 箱子配置 | 配置每个箱子的详细信息，如尺寸、重量等 | 引入 BoxTable 组件 |
| 单箱/混装切换 | 切换单箱和混装模式 | el-switch 组件，调用 shipmentplanApi.updateAreCasesRequired |
| 文档生成 | 下载装箱模板和相关文档 | 调用 shipmentBoxApi.downExcelBoxCaseTemp |
| 提交装箱 | 提交箱子信息，进入下一个步骤 | 调用 shipmentBoxApi.submitPackingInformation |

## 3. 核心功能分析

### 3.1 数据加载

**功能描述**：页面加载时，通过 URL 参数获取计划 ID，然后调用 API 获取货件计划详情和装箱方案。

**实现代码**：

```javascript
onMounted(() => {
  loadData();
});

function loadData() {
  shipmentplanApi.getPlanInfo({"formid": planid}).then((res) => {
    state.planData = res.data;
    state.selectPackingOptionId = state.planData.selectPackingOptionId;
    state.itemcount = state.planData.itemlist.length;
    headerRef.value.show(state.planData, 1);
    footerRef.value.show(state.planData);
    // 获取箱子分组信息
    listPackingOptions();
  });
}
```

**后端实现**：对应 `ShipInboundPlanV2Controller.getPlanInfoAction` 方法，返回货件计划的详细信息。

### 3.2 生成装箱方案

**功能描述**：用户点击「重新分组」按钮时，调用 API 生成多种装箱方案。

**实现代码**：

```javascript
function generatePackingOptions() {
  state.packagetitle = "分组中....";
  shipmentBoxApi.generatePackingOptions({"formid": planid}).then((res) => {
    if (res.data && res.data.operationid) {
      operationRef.value.show(res.data.operationid, 1);
      state.packagetitle = "重新分组";
    }
  }).catch(() => {
    state.btnloading = false;
  });
}
```

**后端实现**：对应 `ShipInboundPlanBoxV2Controller.generatePackingOptionsAction` 方法，生成装箱方案。

### 3.3 选择装箱方案

**功能描述**：用户选择一个装箱方案，系统会确认该方案并更新货件计划。

**实现代码**：

```javascript
function confirmPackingOption(groupInfo) {
  var param = {};
  param.formid = planid;
  param.packingOptionId = groupInfo.packingOptionId;
  groupInfo.loading = true;
  if (state.planData.invtype != 1 && state.planData.invtype != "1") {
    shipmentBoxApi.confirmPackingOption(param).then((res) => {
      groupInfo.loading = false;
      if (res.data && res.data.operationid) {
        operationRef2.value.show(res.data.operationid, 1);
        state.packagetitle = "确认分组";
      }
    });
  } else {
    groupInfo.loading = false;
  }
}
```

**后端实现**：对应 `ShipInboundPlanBoxV2Controller.confirmPackingOptionAction` 方法，确认装箱方案。

### 3.4 配置箱子信息

**功能描述**：用户点击「打开装箱表单」按钮，配置每个箱子的详细信息，如尺寸、重量、商品数量等。

**实现代码**：

```javascript
function openPackList(groupInfo) {
  if (!(groupInfo.boxnum && groupInfo.boxnum > 1)) {
    groupInfo.boxnum = 1;
  }
  groupInfo.boxnum = parseInt(groupInfo.boxnum);
  state.operatorInfo = groupInfo;
  if (state.planData.areCasesRequired) {
    boxTableCaseRef.value.show(groupInfo, state.planData);
  } else {
    boxTableRef.value.show(groupInfo, state.planData);
  }
}
```

**后端实现**：对应 `ShipInboundPlanBoxV2Controller.savePackingInformationAction` 方法，保存箱子信息。

### 3.5 提交装箱信息

**功能描述**：用户点击「确认装箱信息」按钮，提交箱子信息，进入下一个发货步骤。

**实现代码**：

```javascript
function submitBox() {
  state.btnloading = true;
  shipmentBoxApi.submitPackingInformation({"formid": planid}).then((res) => {
    if (res.data && res.data.operationid) {
      ElMessage.warning("箱子信息已提交...");
      operationRef.value.show(res.data.operationid, 0);
    }
  }).catch(() => {
    state.btnloading = false;
  });
}
```

**后端实现**：对应 `ShipInboundPlanBoxV2Controller.submitPackingInformationAction` 方法，提交箱子信息。

### 3.6 单箱/混装切换

**功能描述**：用户通过开关切换单箱和混装模式，系统会更新货件计划的包装类型。

**实现代码**：

```javascript
function handleAreCasesRequired() {
  shipmentplanApi.updateAreCasesRequired({"formid": planid, "areCasesRequired": state.planData.areCasesRequired}).then((res) => {
    ElMessage.success("修改成功");
  });
}
```

**后端实现**：对应 `ShipInboundPlanV2Controller.updateAreCasesRequired` 方法，更新包装类型。

### 3.7 下载装箱模板

**功能描述**：用户点击「下载装箱模板」按钮，下载当前装箱组的装箱模板。

**实现代码**：

```javascript
function downloadGroupInfo(groupInfo, index) {
  let planData = JSON.parse(JSON.stringify(state.planData));
  let itemList = [];
  groupInfo.items.forEach(item => {
    let itemData = {sku: item.msku, quantity: item.quantity};
    itemList.push(itemData);
  });
  planData.itemlist = itemList;
  shipmentBoxApi.downExcelBoxCaseTemp(planData, planData.number + '-装箱组' + (index + 1) + ".xlsx");
}
```

**后端实现**：对应 `ShipInboundPlanBoxV2Controller.downExcelBoxCaseTempAction` 方法，生成和下载装箱模板。

## 4. 前端 API 调用

### 4.1 API 模块引入

```javascript
import shipmentplanApi from '@/api/erp/shipv2/shipmentplanApi.js';
import shipmentPlacementApi from '@/api/erp/shipv2/shipmentPlacementApi.js';
import shipmentBoxApi from '@/api/erp/shipv2/shipmentBoxApi.js';
```

### 4.2 核心 API 调用

| API 方法 | 功能描述 | 参数说明 | 后端对应方法 |
|---------|---------|---------|------------|
| shipmentplanApi.getPlanInfo | 获取货件计划详情 | `{formid: 计划ID}` | ShipInboundPlanV2Controller.getPlanInfoAction |
| shipmentplanApi.updateAreCasesRequired | 更新包装类型 | `{formid: 计划ID, areCasesRequired: 是否单箱}` | ShipInboundPlanV2Controller.updateAreCasesRequired |
| shipmentBoxApi.generatePackingOptions | 生成装箱方案 | `{formid: 计划ID}` | ShipInboundPlanBoxV2Controller.generatePackingOptionsAction |
| shipmentBoxApi.listPackingOptions | 获取装箱方案列表 | `{formid: 计划ID, paginationToken: 分页令牌}` | ShipInboundPlanBoxV2Controller.listPackingOptionsAction |
| shipmentBoxApi.listPackingGroupItems | 获取装箱组详情 | `{formid: 计划ID, packingOptionId: 方案ID, packingGroupId: 分组ID}` | ShipInboundPlanBoxV2Controller.listPackingGroupItemsAction |
| shipmentBoxApi.confirmPackingOption | 确认装箱方案 | `{formid: 计划ID, packingOptionId: 方案ID}` | ShipInboundPlanBoxV2Controller.confirmPackingOptionAction |
| shipmentBoxApi.selectPackgroupInfo | 查询装箱组信息 | `{packingGroupId: 分组ID}` | ShipInboundPlanBoxV2Controller.selectPackgroupInfoAction |
| shipmentBoxApi.downExcelBoxCaseTemp | 下载装箱模板 | `{planData: 计划数据, filename: 文件名}` | ShipInboundPlanBoxV2Controller.downExcelBoxCaseTempAction |
| shipmentBoxApi.submitPackingInformation | 提交箱子信息 | `{formid: 计划ID}` | ShipInboundPlanBoxV2Controller.submitPackingInformationAction |
| shipmentBoxApi.donePlanBox | 完成装箱 | `{formid: 计划ID, ftype: 类型}` | ShipInboundPlanBoxV2Controller.donePackingAction |
| shipmentPlacementApi.generatePlacementOptions | 生成收货地址方案 | `{formid: 计划ID}` | ShipInboundPlanPlacementV2Controller.generatePlacementOptionsAction |

## 5. 后端 API 实现

### 5.1 核心控制器：ShipInboundPlanBoxV2Controller

**控制器路径**：`com.wimoor.amazon.inboundV2.controller.ShipInboundPlanBoxV2Controller`

**核心方法**：

| 方法名 | URL | 功能描述 | 请求方式 |
|-------|-----|---------|--------|
| generatePackingOptionsAction | /api/v2/shipInboundPlan/box/generatePackingOptions | 生成装箱方案 | GET |
| listPackingOptionsAction | /api/v2/shipInboundPlan/box/listPackingOptions | 获取装箱方案列表 | POST |
| listPackingGroupItemsAction | /api/v2/shipInboundPlan/box/listPackingGroupItems | 获取装箱组详情 | POST |
| confirmPackingOptionAction | /api/v2/shipInboundPlan/box/confirmPackingOption | 确认装箱方案 | POST |
| selectPackgroupInfoAction | /api/v2/shipInboundPlan/box/selectPackgroupInfo | 查询装箱组信息 | GET |
| savePackingInformationAction | /api/v2/shipInboundPlan/box/savePackingInformation | 保存箱子信息 | POST |
| submitPackingInformationAction | /api/v2/shipInboundPlan/box/submitPackingInformation | 提交箱子信息 | GET |
| donePackingAction | /api/v2/shipInboundPlan/box/donePlanBox | 完成装箱 | GET |
| downExcelBoxCaseTempAction | /api/v2/shipInboundPlan/box/downExcelBoxCaseTemp | 下载装箱模板 | POST |

**示例实现**：

```java
@ApiOperation(value = "生成箱子分组")
@GetMapping("/generatePackingOptions")
@Transactional
public Result<ShipInboundOperation> generatePackingOptionsAction(String formid) {
    try {
        return Result.success(shipInboundBoxV2Service.generatePackingOptions(formid));
    } catch (FeignException e) {
        throw new BizException("生成失败" + e.getMessage());
    } catch (Exception e) {
        throw new BizException("生成失败" + e.getMessage());
    }
}
```

### 5.2 核心控制器：ShipInboundPlanPlacementV2Controller

**控制器路径**：`com.wimoor.amazon.inboundV2.controller.ShipInboundPlanPlacementV2Controller`

**核心方法**：

| 方法名 | URL | 功能描述 | 请求方式 |
|-------|-----|---------|--------|
| generatePlacementOptionsAction | /api/v2/shipInboundPlan/shipment/generatePlacementOptions | 生成收货地址方案 | GET |
| confirmPlacementOptionAction | /api/v2/shipInboundPlan/shipment/confirmPlacementOption | 确认收货地址方案 | GET |
| listPlacementOptionsAction | /api/v2/shipInboundPlan/shipment/listPlacementOptions | 获取收货地址方案列表 | POST |

**示例实现**：

```java
@ApiOperation(value = "生成不同收货地址数对应方案")
@GetMapping("/generatePlacementOptions")
@Transactional
public Result<ShipInboundOperation> generatePlacementOptionsAction(String formid) {
    try {
        return Result.success(shipInboundShipmentV2Service.generatePlacementOptions(formid));
    } catch (FeignException e) {
        throw new BizException("生成方案失败" + e.getMessage());
    } catch (Exception e) {
        throw new BizException("生成方案失败" + e.getMessage());
    }
}
```

## 6. 数据模型

### 6.1 核心实体类

#### ShipInboundBox（货件箱子信息）

**表名**：`t_erp_ship_v2_inboundbox`

| 字段名 | 数据类型 | 描述 |
|-------|---------|------|
| id | String | 箱子ID |
| formid | String | 计划ID |
| packing_group_id | String | 分组ID |
| shipmentid | String | 货件ID |
| boxnum | Integer | 箱号 |
| length | BigDecimal | 箱子长度 |
| width | BigDecimal | 箱子宽度 |
| height | BigDecimal | 箱子高度 |
| unit | String | 高宽高单位 |
| weight | BigDecimal | 重量 |
| wunit | String | 重量单位 |
| tracking_id | String | 物流追踪编号 |
| package_status | String | 物流追踪状态 |

#### ShipInboundCase（货件装箱详情）

**表名**：`t_erp_ship_v2_inboundcase`

| 字段名 | 数据类型 | 描述 |
|-------|---------|------|
| id | String | 详情ID |
| boxid | String | 箱子ID |
| sku | String | 平台SKU |
| quantity | Integer | 箱子内对应SKU的数量 |

### 6.2 数据传输对象 (DTO)

#### PackingDTO

用于查询装箱方案和装箱组详情的参数对象，包含计划ID、方案ID、分组ID等信息。

#### ShipCartDTO

用于提交箱子信息的参数对象，包含箱子列表、计划ID、分组ID等信息。

## 7. 业务流程

### 7.1 装箱方案生成流程

1. **初始化**：页面加载时，通过 URL 参数获取计划 ID，调用 `shipmentplanApi.getPlanInfo` 获取货件计划详情。

2. **生成方案**：
   - 用户点击「重新分组」按钮
   - 调用 `shipmentBoxApi.generatePackingOptions` 生成装箱方案
   - 方案生成成功后，调用 `listPackingOptions` 获取方案列表

3. **选择方案**：
   - 用户从多个装箱方案中选择一个
   - 调用 `shipmentBoxApi.confirmPackingOption` 确认选择的方案

4. **配置箱子**：
   - 用户为每个装箱组配置箱子数量
   - 点击「打开装箱表单」按钮，配置每个箱子的详细信息
   - 调用 `shipmentBoxApi.savePackingInformation` 保存箱子信息

5. **提交装箱**：
   - 用户点击「确认装箱信息」按钮
   - 调用 `shipmentBoxApi.submitPackingInformation` 提交箱子信息
   - 信息提交成功后，进入下一个发货步骤（配置收货地址）

### 7.2 单箱/混装切换流程

1. **切换模式**：用户通过开关切换单箱和混装模式。

2. **更新配置**：调用 `shipmentplanApi.updateAreCasesRequired` 更新货件计划的包装类型。

3. **重新加载**：模式切换成功后，页面会重新加载相关数据，显示对应模式的装箱配置界面。

### 7.3 箱子信息配置流程

1. **打开装箱表单**：用户点击「打开装箱表单」按钮，打开箱子信息配置表单。

2. **配置箱子**：用户为每个箱子配置尺寸、重量、商品数量等信息。

3. **保存信息**：用户点击「保存」按钮，调用 `shipmentBoxApi.savePackingInformation` 保存箱子信息。

4. **验证数据**：系统验证箱子信息的合法性，如尺寸、重量是否符合要求。

5. **更新状态**：箱子信息保存成功后，系统更新装箱组的状态为已配置。

## 8. 技术要点

### 8.1 前端技术要点

1. **Composition API**：使用 Vue 3 的 Composition API 组织代码，提高代码可读性和可维护性。

2. **响应式状态管理**：使用 `reactive` 和 `ref` 管理页面状态，确保数据变化能够及时反映到 UI 上。

3. **组件通信**：使用 `ref` 和 `emit` 实现组件之间的通信，如父组件与子组件之间的数据传递。

4. **生命周期钩子**：使用 `onMounted` 钩子在页面加载时初始化数据。

5. **异步操作处理**：使用 `async/await` 和 Promise 处理 API 调用等异步操作。

6. **条件渲染**：使用 `v-if` 和 `v-for` 实现条件渲染，根据不同的状态显示不同的 UI 元素。

### 8.2 后端技术要点

1. **RESTful API 设计**：遵循 RESTful 设计风格，使用合适的 HTTP 方法和状态码。

2. **事务管理**：使用 `@Transactional` 注解管理事务，确保数据操作的一致性。

3. **用户认证**：使用 `UserInfoContext` 获取当前用户信息，实现权限控制。

4. **数据校验**：使用 `@Valid` 和 `@NotNull` 等注解进行数据校验，确保数据的合法性。

5. **异常处理**：使用 `try-catch` 捕获和处理异常，返回友好的错误信息。

6. **文件处理**：使用 Apache POI 处理 Excel 文件，实现装箱模板的生成和下载。

## 9. 常见问题与解决方案

### 9.1 常见问题

| 问题描述 | 可能原因 | 解决方案 |
|---------|---------|----------|
| 无法生成装箱方案 | 计划 ID 不存在或无权限 | 检查 URL 参数是否正确，确认用户权限 |
| 箱子信息保存失败 | 数据格式错误或验证失败 | 检查输入数据是否符合要求，确保必填字段已填写 |
| 装箱方案选择失败 | 方案已过期或无效 | 重新生成装箱方案，选择有效的方案 |
| 模板下载失败 | 文件生成出错或网络问题 | 检查服务器状态，确保网络连接正常 |
| 单箱/混装切换失败 | API 调用失败或权限不足 | 检查网络连接，确认用户权限 |

### 9.2 性能优化建议

1. **前端优化**：
   - 使用虚拟滚动处理大量装箱方案数据
   - 合理使用缓存，减少重复 API 调用
   - 优化组件渲染，避免不必要的重渲染

2. **后端优化**：
   - 使用索引优化数据库查询
   - 合理使用缓存，减少数据库访问
   - 优化文件生成过程，提高模板下载速度

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

**装箱页面**是 Wimoor ERP 系统中 FBA 发货流程的重要组成部分，主要负责生成和管理装箱方案，配置箱子信息，提交装箱数据等功能。通过本文档的详细解析，我们可以了解到：

1. **页面结构**：采用模块化设计，包含头部信息、装箱方案、装箱组管理、箱子配置等核心模块。

2. **功能实现**：通过前端 API 调用和后端控制器的配合，实现了装箱方案生成、选择、箱子配置、信息提交等核心功能。

3. **数据模型**：使用 `ShipInboundBox` 和 `ShipInboundCase` 等实体类，构建了完整的数据模型体系。

4. **业务流程**：清晰的装箱方案生成流程、单箱/混装切换流程和箱子信息配置流程，确保了装箱操作的顺利进行。

5. **技术要点**：使用了 Vue 3 + Composition API + Element Plus 等前端技术，以及 Spring Boot + MyBatis Plus 等后端技术，构建了一个高效、可靠的装箱管理系统。

通过不断优化和改进，装箱页面将为用户提供更加便捷、高效的装箱体验，助力企业更好地管理 FBA 发货流程。