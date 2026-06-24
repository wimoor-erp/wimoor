# 发货页面 (three_ship.vue) 详细帮助文档

## 1. 页面概述

**发货页面**是 Wimoor ERP 系统中 FBA 发货流程的第三个步骤，主要负责管理货件信息、配置配送信息、物流信息，以及处理发货出库等操作。该页面是连接装箱和最终发货的关键环节，提供了完整的货件管理功能。

**核心功能点**：
- 展示货件列表及其详细信息
- 配置预计到货时间
- 选择承运人及货件类型
- 配置配送方式
- 管理物流信息
- 下载货件标签
- 提交配送信息
- 处理发货出库
- 管理耗材出库

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
      <div class="con-body lylStepTwo" > 
        <Header ref="headerRef" ></Header>
        <div class="font-base m-b-8">货件数 {{shipmentNum}} <span class="text-red"> {{notice}}</span></div>
        <el-skeleton animated :loading="loading">
          <template #template> 
            <!-- 骨架屏内容 -->
          </template>
          <template #default>
            <el-row :gutter="16" v-loading="shipmentLoading">
              <el-col :span="8" v-for="(item,index) in tableList" :key="index" >
                <div class="shipment-card-wrap">
                  <el-card shadow="none">
                    <template #header>
                      <div class="font-base font-bold">{{item.shipment.name}}</div>
                    </template>
                    <!-- 货件信息内容 -->
                    <template #footer>
                      <!-- 打印标签相关操作 -->
                    </template>
                  </el-card>
                </div>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="20"><Operation ref="operationRef" @change="handleOperationChange"></Operation></el-col>
              <el-col :span="4" >
                <!-- 状态信息 -->
              </el-col>
              <el-col :span="12" >
                <!-- 操作按钮 -->
              </el-col>
              <el-col :span="12" >
                <!-- 提交按钮 -->
              </el-col>
            </el-row>
          </template>
        </el-skeleton> 
      </div>
    </div>
    <Footer  ref="footerRef"></Footer>
  </div>
  <Consumable  ref="consumableRef"  ></Consumable>
  <!-- 物流信息对话框 -->
  <el-dialog title="物流信息" v-model="transVisiable" top="2vh" class="transClz" width="80%">
    <div class="con-body"  >
      <TransCompany ref="transRef" @stepdata="stepdataChange"   onlyTrans="true" ></TransCompany>
    </div>
  </el-dialog>
  <!-- 商品详情对话框 -->
  <Table ref="tableDialogRef" ></Table>
  <!-- 装箱对话框 -->
  <el-dialog title="装箱" v-model="boxVisiable" width="80%" top="1vh" >
    <div class="con-body"  >
      <div style="width:100%;overflow-x: auto;margin-bottom:10px;">
        <ShipBoxCase ref="shipBoxCaseRef" v-if="planData.areCasesRequired" @change="handleBoxSaveChange"></ShipBoxCase>
        <ShipBox ref="shipBoxRef" v-else @change="handleBoxSaveChange"></ShipBox>
      </div>
    </div>
  </el-dialog>
</template>
```

### 2.2 核心功能模块

| 模块名称 | 功能描述 | 实现方式 |
|---------|---------|----------|
| 头部信息 | 展示货件计划的基本信息，如名称、状态等 | 引入 Header 组件 |
| 货件列表 | 展示所有货件的详细信息，包括货件编码、地址、商品信息等 | 使用 el-card 组件展示货件信息 |
| 预计到货时间 | 配置货件的预计到货时间 | 使用 el-select 组件选择时间窗口 |
| 承运人选择 | 选择货件的承运人 | 使用 el-select 组件选择承运人 |
| 货件类型 | 选择货件类型（SP/LTL） | 使用 el-select 组件选择货件类型 |
| 配送方式 | 配置货件的配送方式 | 使用 el-select 组件选择配送选项 |
| 物流信息 | 管理货件的物流信息 | 打开物流信息编辑对话框 |
| 打印标签 | 下载货件的箱子标签、2D条形码和托盘标签 | 使用 el-dropdown 组件提供下载选项 |
| 操作按钮 | 提供重新生成时间、物流信息和耗材出库等操作 | 使用 el-button 组件提供操作按钮 |
| 提交按钮 | 根据审核状态提供不同的提交操作 | 使用 el-button 组件提供提交按钮 |
| 状态信息 | 显示货件的当前状态，如是否完成装箱、配送信息等 | 使用 el-icon 组件显示状态图标 |

## 3. 核心功能分析

### 3.1 数据加载

**功能描述**：页面加载时，通过 URL 参数获取计划 ID，然后调用 API 获取货件计划详情和货件列表。

**实现代码**：

```javascript
onMounted(() => {
  loadData();
});

function loadData() {
  shipmentplanApi.getPlanInfo({"formid": planid}).then((res) => {
    state.planData = res.data;
    if(res.data.shipments){
      state.shipmentids = res.data.shipments.split(",");
    }
    state.itemcount = state.planData.itemlist.length;
    headerRef.value.show(state.planData, 3);
    footerRef.value.show(state.planData);
    loadTable();
  });
}

function loadTable() {
  state.params.planid = planid;
  shipmentPlacementApi.getBaseInfoByPlan(state.params).then(res => {
    if(res && res.data){
      state.tableList = res.data;
      state.shipmentNum = res.data.length;
      // 处理货件数不匹配的情况
      if(state.shipmentids && state.shipmentNum != state.shipmentids.length){
        state.notice = "(货件数不匹配,系统尝试第" + state.shipmentLoadNum + "次加载！)";
        if(state.shipmentLoadNum < 8){
          var timer = setTimeout(function(){
            loadData();
            state.shipmentLoadNum++;
          }, 3000);
        }else{
          state.notice = "(尝试失败,请等待系统自动同步货件中......)";
          getShipments();
        }
      }
      state.loading = false;
      if(res.data[0] && res.data[0].shipment){
        state.placementOptionId = res.data[0].shipment.placementOptionId;
      }
      state.transportation = {};
      state.tableList.forEach(shipmentDetails => {
        if(!shipmentDetails.shipment.transtyle){
          shipmentDetails.shipment.transtyle = "SP";
        }
        if(!shipmentDetails.shipment.carrier){
          shipmentDetails.shipment.carrier = "USE_YOUR_OWN_CARRIER";
        }
        listDeliveryWindowOptions(shipmentDetails);
        loadTransportation(shipmentDetails, false);
      });
    }
  });
}
```

**后端实现**：对应 `ShipInboundPlanPlacementV2Controller.getBaseInfoByPlanAction` 方法，返回货件计划的详细信息和货件列表。

### 3.2 预计到货时间配置

**功能描述**：用户为每个货件选择预计到货时间，系统保存选择的时间窗口。

**实现代码**：

```javascript
function saveTime(itemdata) {
  var data = {"formid": itemdata.formid, "deliveryWindowOptionId": itemdata.deliveryWindowOptionId, "shipmentid": itemdata.shipmentid};
  shipmentPlacementApi.confirmDeliveryWindowOptions(data).then(res => {
    if(res.data && res.data.operationid){
      operationRef.value.show(res.data.operationid, null, null, "预估时间");
    }
  }).catch(() => {
  });
}

function generateDeliveryWindowOptionsAll() {
  state.tableList.forEach(shipmentDetails => {
    generateDeliveryWindowOptions(shipmentDetails);
  });
}

function generateDeliveryWindowOptions(shipmentDetails) {
  var param = {"formid": shipmentDetails.plan.id, "shipmentId": shipmentDetails.shipment.shipmentid};
  shipmentPlacementApi.generateDeliveryWindowOptions(param).then(res => {
    if(res.data.operationid){
      operationRef.value.show(res.data.operationid);
    }
  });
}
```

**后端实现**：对应 `ShipInboundPlanPlacementV2Controller.generateDeliveryWindowOptionsAction` 和 `confirmDeliveryWindowOptionsAction` 方法，生成和确认到货时间窗口。

### 3.3 配送方式配置

**功能描述**：用户为每个货件选择配送方式，系统保存选择的配送选项。

**实现代码**：

```javascript
function saveDetail(shipment, transaction) {
  var transactionName = "";
  transaction.forEach(itemtrans => {
    if(itemtrans.transportationOptionId == shipment.transportationOptionId){
      transactionName = itemtrans.shippingMode + '---' + itemtrans.carrier.name;
    }
  });
  shipmentTransportationApi.saveConfirmTransportationOptionsInfo({
    "shipmentid": shipment.shipmentid,
    "transportationOptionId": shipment.transportationOptionId,
    "transactionName": transactionName
  }).then(res => {
    if(res.data){
      ElMessage.success("配送信息保存成功！");
    }
  }).catch(() => {
  });
}

function generateTransportationOptions() {
  shipmentTransportationApi.generateTransportationOptions(state.planData).then((res) => {
    if(res.data.operationid){
      operationRef.value.show(res.data.operationid, 1);
    }
  });
}

function loadTransportation(shipmentDetails, withOutGenerate, paginationToken) {
  shipmentDetails.shipment.loading = true;
  shipmentTransportationApi.listTransportationOptions({
    "formid": planid,
    "placementOptionId": state.placementOptionId,
    "paginationToken": paginationToken,
    "pageSize": 20,
    "shipmentid": shipmentDetails.shipment.shipmentid
  }).then((res) => {
    // 处理分页加载和数据排序
  });
}
```

**后端实现**：对应 `ShipInboundPlanTransportationController.generateTransportationOptionsAction` 和 `listTransportationOptionsAction` 方法，生成和获取配送选项。

### 3.4 物流信息管理

**功能描述**：用户为每个货件配置物流信息，包括物流公司、渠道等。

**实现代码**：

```javascript
function showTransEdit(row, shipment) {
  state.transVisiable = true;
  nextTick(() => {
    transRef.value.loadOptData(shipment);
  });
}

function stepdataChange(step, shipmentid) {
  shipmentPlacementApi.getBaseInfo({
    "shipmentid": shipmentid
  }).then(res => {
    transRef.value.loadOptData(res.data);
  });
  loadTable();
}
```

**后端实现**：对应 `ShipInboundPlanTransportationController.saveTransCompanyData` 方法，保存物流信息。

### 3.5 打印标签

**功能描述**：用户可以下载货件的箱子标签、2D条形码和托盘标签。

**实现代码**：

```javascript
function downloadLabel(labeltype, shipment) {
  var data = {};
  data.shipmentid = shipment.shipmentid;
  data.pagetype = shipment.printType;
  data.labeltype = labeltype;
  data.pannum = 0;
  shipmentPlacementApi.downLabel(data, () => {
    loadTable();
  });
}
```

**后端实现**：对应 `ShipInboundPlanPlacementV2Controller.getPkgLabelUrlAction` 方法，获取标签下载 URL。

### 3.6 提交配送信息

**功能描述**：用户提交所有货件的配送信息，系统处理配送信息的确认。

**实现代码**：

```javascript
function submitDetail() {
  state.submitloading = true;
  shipmentTransportationApi.confirmTransportationOptionsByForm(state.planData.id).then(res => {
    state.submitloading = false;
    if(res.data && res.data.operationid){
      operationRef.value.show(res.data.operationid, null, null, '配送信息');
    }
  }).catch(() => {
    state.submitloading = false;
  });
}
```

**后端实现**：对应 `ShipInboundPlanTransportationController.confirmTransportationOptionsByFormAction` 方法，确认所有货件的配送信息。

### 3.7 发货出库

**功能描述**：用户确认发货出库，系统处理库存出库和货件状态更新。

**实现代码**：

```javascript
function doneAllShipment() {
  ElMessageBox.confirm(
    '您确认要提交发货吗?',
    'Warning',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    }
  )
    .then(() => {
      if(state.submitloading == false){
        state.submitloading = true;
        proxy.$modal.loading("请勿关闭此页面，正在处理出库，货件标发，请稍候...");
        if(state.planData.invstatus == '1' && state.planData.invtype != '2'){
          getInvParam();
        }else{
          confirmShipemnt();
        }
      }
    })
    .catch(() => {
      ElMessage({
        type: 'info',
        message: '取消操作',
      })
    })
}

function getInvParam() {
  shipmentplanApi.getInvParam({"id": state.planData.id}).then((res) => {
    state.invparam = res.data;
    outShipInventory(state.invparam);
  }).catch(() => {
    proxy.$modal.closeLoading();
  });
}

function outShipInventory(params) {
  shipmentplanApi.outShipInventory(params).then((res) => {
    setInvStatus();
  }).catch(() => {
    proxy.$modal.closeLoading();
  });
}

function setInvStatus() {
  shipmentplanApi.setInvStatus({"id": state.planData.id, "status": 2}).then((res) => {
    confirmShipemnt();
  }).catch(() => {
    proxy.$modal.closeLoading();
  });
}

async function confirmShipemnt() {
  // 保存物流跟踪信息
  for(var i = 0; i < state.tableList.length; i++){
    var shipmentDetail = state.tableList[i];
    var data = {};
    var boxinfos = [];
    state.submitloading = true;
    shipmentDetail.listbox.forEach(function(item, index) {
      var trackinginfo = {};
      trackinginfo.boxnum = item.boxnum;
      trackinginfo.id = item.id;
      trackinginfo.value = item.tracking_id != null ? item.tracking_id : "";
      boxinfos.push(trackinginfo);
    });
    data.boxinfo = boxinfos;
    data.shipmentid = shipmentDetail.shipment.shipmentid;
    data.transtype = shipmentDetail.shipment.tranType;
    data.actiontype = "Shipped";
    await shipmentPlacementApi.saveTransTrace(data).then(res => {
      state.submitloading = false;
      if(res.data && res.data.operationid){
        operationRef.value.show(res.data.operationid, 0);
        map[res.data.operationid] = data.shipmentid;
      }
    }).catch(e => {
      state.submitloading = false;
    });
  }
  
  // 完成货件计划
  await shipmentplanApi.donePlan({"formid": state.planData.id}).then((res) => {
    proxy.$modal.closeLoading();
    loadData();
  }).catch(() => {
    proxy.$modal.closeLoading();
  });
  state.submitloading = false;
}
```

**后端实现**：对应 `ShipInboundPlanController.getInvParamAction`、`outShipInventoryAction`、`setInvStatusAction` 和 `donePlanAction` 方法，处理库存出库和货件状态更新。

### 3.8 耗材出库

**功能描述**：用户处理货件的耗材出库，系统记录耗材使用情况。

**实现代码**：

```javascript
function showConsumable(ftype) {
  consumableRef.value.show(ftype, state.planData);
}
```

**后端实现**：对应相关的耗材出库控制器方法，处理耗材出库操作。

### 3.9 货件状态同步

**功能描述**：用户可以手动刷新货件状态，系统同步最新的货件信息。

**实现代码**：

```javascript
function handleRefresh() {
  state.planData.refreshloading = true;
  shipmentplanApi.refreshInboundPlan({"formid": state.planData.id}).then((res) => {
    ElMessage.success("已成功！");
    state.planData.refreshloading = false;
  });
}
```

**后端实现**：对应 `ShipInboundPlanController.refreshInboundPlanAction` 方法，刷新货件计划状态。

## 4. 前端 API 调用

### 4.1 API 模块引入

```javascript
import addressApi from '@/api/amazon/inbound/addressApi.js';
import shipmentplanApi from '@/api/erp/shipv2/shipmentplanApi.js';
import shipmentBoxApi from '@/api/erp/shipv2/shipmentBoxApi.js';
import shipmentTransportationApi from '@/api/erp/shipv2/shipmentTransportationApi.js';
import Table  from "./components/table.vue";
import Trans from "@/views/erp/shipv2/shipment_add/shipstep/components/trans.vue";
import shipmentPlacementApi from '@/api/erp/shipv2/shipmentPlacementApi.js';
import shipmentQuotaApi from '@/api/erp/shipv2/shipmentQuotaApi.js';
```

### 4.2 核心 API 调用

| API 方法 | 功能描述 | 参数说明 | 后端对应方法 |
|---------|---------|---------|------------|
| shipmentplanApi.getPlanInfo | 获取货件计划详情 | `{formid: 计划ID}` | ShipInboundPlanV2Controller.getPlanInfoAction |
| shipmentplanApi.getInvParam | 获取库存参数 | `{id: 计划ID}` | ShipInboundPlanV2Controller.getInvParamAction |
| shipmentplanApi.outShipInventory | 出库操作 | `{库存参数对象}` | ShipInboundPlanV2Controller.outShipInventoryAction |
| shipmentplanApi.setInvStatus | 设置库存状态 | `{id: 计划ID, status: 状态}` | ShipInboundPlanV2Controller.setInvStatusAction |
| shipmentplanApi.donePlan | 完成货件计划 | `{formid: 计划ID}` | ShipInboundPlanV2Controller.donePlanAction |
| shipmentplanApi.refreshInboundPlan | 刷新货件计划 | `{formid: 计划ID}` | ShipInboundPlanV2Controller.refreshInboundPlanAction |
| shipmentBoxApi.submitPackingInformation | 提交装箱信息 | `{formid: 计划ID}` | ShipInboundPlanBoxV2Controller.submitPackingInformationAction |
| shipmentTransportationApi.generateTransportationOptions | 生成配送选项 | `{计划对象}` | ShipInboundPlanTransportationController.generateTransportationOptionsAction |
| shipmentTransportationApi.listTransportationOptions | 获取配送选项列表 | `{formid: 计划ID, shipmentid: 货件ID, ...}` | ShipInboundPlanTransportationController.listTransportationOptionsAction |
| shipmentTransportationApi.saveConfirmTransportationOptionsInfo | 保存配送选项 | `{shipmentid: 货件ID, transportationOptionId: 选项ID, transactionName: 选项名称}` | ShipInboundPlanTransportationController.confirmTransportationOptionsAction |
| shipmentTransportationApi.confirmTransportationOptionsByForm | 确认所有配送选项 | `{formid: 计划ID}` | ShipInboundPlanTransportationController.confirmTransportationOptionsByFormAction |
| shipmentPlacementApi.generateDeliveryWindowOptions | 生成到货时间窗口 | `{formid: 计划ID, shipmentId: 货件ID}` | ShipInboundPlanPlacementV2Controller.generateDeliveryWindowOptionsAction |
| shipmentPlacementApi.confirmDeliveryWindowOptions | 确认到货时间窗口 | `{formid: 计划ID, shipmentid: 货件ID, deliveryWindowOptionId: 时间窗口ID}` | ShipInboundPlanPlacementV2Controller.confirmDeliveryWindowOptionsAction |
| shipmentPlacementApi.getBaseInfoByPlan | 获取计划的货件信息 | `{planid: 计划ID}` | ShipInboundPlanPlacementV2Controller.getBaseInfosByPlanAction |
| shipmentPlacementApi.saveTransTrace | 保存物流跟踪信息 | `{shipmentid: 货件ID, boxinfo: 箱子信息, transtype: 运输类型, actiontype: 操作类型}` | ShipInboundPlanPlacementV2Controller.saveTransTraceAction |
| shipmentPlacementApi.downLabel | 下载标签 | `{shipmentid: 货件ID, pagetype: 页面类型, labeltype: 标签类型, pannum: 托盘数}` | ShipInboundPlanPlacementV2Controller.getPkgLabelUrlAction |
| shipmentQuotaApi.downPDFShipmentForm | 下载配货单 | `{ftype: 类型, shipmentids: 货件ID列表, name: 文件名}` | ShipInboundPlanQuotaController.downPDFShipmentFormAction |

## 5. 后端 API 实现

### 5.1 核心控制器：ShipInboundPlanTransportationController

**控制器路径**：`com.wimoor.amazon.inboundV2.controller.ShipInboundPlanTransportationController`

**核心方法**：

| 方法名 | URL | 功能描述 | 请求方式 |
|-------|-----|---------|--------|
| generateTransportationOptionsAction | /api/v2/shipInboundPlan/transportation/generateTransportationOptions | 生成配送选项 | POST |
| listTransportationOptionsAction | /api/v2/shipInboundPlan/transportation/listTransportationOptions | 获取配送选项列表 | POST |
| confirmTransportationOptionsAction | /api/v2/shipInboundPlan/transportation/confirmTransportationOptions/{formid} | 确认配送选项 | POST |
| confirmTransportationOptionsByFormAction | /api/v2/shipInboundPlan/transportation/confirmTransportationOptionsByForm | 确认所有配送选项 | GET |
| saveConfirmTransportationOptionsInfoAction | /api/v2/shipInboundPlan/transportation/saveConfirmTransportationOptionsInfo | 保存配送选项 | GET |
| saveTransCompanyData | /api/v2/shipInboundPlan/transportation/saveTransCompany | 保存物流信息 | POST |

**示例实现**：

```java
@ApiOperation(value = "生成不同收货地址数对应方案")
@PostMapping("/generateTransportationOptions")
@Transactional
public Result<ShipInboundOperation> generateTransportationOptionsAction(@ApiParam("发货计划")@RequestBody ShipInboundPlan inplan){
  try {
    UserInfo user = UserInfoContext.get();
    ShipInboundPlan old = shipInboundPlanV2Service.getById(inplan.getId());
    old.setOperator(user.getId());
    old.setOpttime(new Date());
    if(inplan.getShippingDate() != null){
      old.setShippingDate(inplan.getShippingDate());
    }
    if(inplan.getShippingSolution() != null){
      old.setShippingSolution(inplan.getShippingSolution());
    }else if(old.getShippingSolution() == null){
      old.setShippingSolution("USE_YOUR_OWN_CARRIER");
    }
    if(inplan.getTranstyle() != null){
      old.setTranstyle(inplan.getTranstyle());
    }
    if(inplan.getPlacementOptionId() != null){
      old.setPlacementOptionId(inplan.getPlacementOptionId());
    }
    old.setTransportationToken(null);
    if(inplan.getShipmentids() == null){
      List<ShipInboundShipment> listshipment = this.shipInboundShipmentV2Service.lambdaQuery().eq(ShipInboundShipment::getFormid, old.getId()).list();
      List<String> shipmentids = new ArrayList<String>();
      for(ShipInboundShipment item : listshipment){
        shipmentids.add(item.getShipmentid());
        item.setTransportationToken(null);
        item.setTransportationOptionId(null);
        this.shipInboundShipmentV2Service.updateById(item);
      }
      old.setShipmentids(shipmentids);
    }else{
      old.setShipmentids(inplan.getShipmentids());
    }

    shipInboundPlanV2Service.updateById(old);
    if(inplan.getShipmentids() != null && (old.getShipmentids() == null || old.getShipmentids().isEmpty())){
      old.setShipmentids(inplan.getShipmentids());
    }
    return Result.success(iShipInboundTransportationService.generateTransportationOptions(old));
  }catch(FeignException e) {
    throw new BizException("生成方案失败" + e.getMessage());
  }catch(Exception e) {
    throw new BizException("生成方案失败" + e.getMessage());
  }
}
```

### 5.2 核心控制器：ShipInboundPlanPlacementV2Controller

**控制器路径**：`com.wimoor.amazon.inboundV2.controller.ShipInboundPlanPlacementV2Controller`

**核心方法**：

| 方法名 | URL | 功能描述 | 请求方式 |
|-------|-----|---------|--------|
| generateDeliveryWindowOptionsAction | /api/v2/shipInboundPlan/shipment/generateDeliveryWindowOptions/ignoreRepeat | 生成到货时间窗口 | GET |
| confirmDeliveryWindowOptionsAction | /api/v2/shipInboundPlan/shipment/confirmDeliveryWindowOptions/ignoreRepeat | 确认到货时间窗口 | POST |
| getBaseInfosByPlanAction | /api/v2/shipInboundPlan/shipment/getBaseInfosByPlan | 获取计划的货件信息 | GET |
| getPkgLabelUrlAction | /api/v2/shipInboundPlan/shipment/getPkgLabelUrl | 获取标签下载 URL | GET |
| saveShipment | /api/v2/shipInboundPlan/shipment/saveShipment | 保存货件信息 | POST |

**示例实现**：

```java
@ApiOperation(value = "生成不同收货地址数对应方案")
@GetMapping("/generateDeliveryWindowOptions/ignoreRepeat")
@Transactional
public Result<ShipInboundOperation> generateDeliveryWindowOptionsAction(String formid, String shipmentId){
  try {
    return Result.success(shipInboundShipmentV2Service.generateDeliveryWindowOptions(formid, shipmentId));
  }catch(FeignException e) {
    throw new BizException("生成方案失败" + e.getMessage());
  }catch(Exception e) {
    throw new BizException("生成方案失败" + e.getMessage());
  }
}
```

## 6. 数据模型

### 6.1 核心实体类

#### ShipInboundShipment（货件）

**表名**：`t_erp_ship_v2_inboundshipment`

| 字段名 | 数据类型 | 描述 |
|-------|---------|------|
| shipmentid | String | 货件ID |
| shipment_confirmation_id | String | 货件确认ID |
| destination | String | 亚马逊仓库中心代码 |
| destinationbox | String | 亚马逊仓库中心代码（箱子） |
| addressid | String | 发货地址ID |
| shipmentstatus | String | 货件状态（平台） |
| inboundplanid | String | 订单ID |
| formid | String | 计划ID |
| placement_option_id | String | 收货地址方案ID |
| transaction_name | String | 交易名称 |
| transportation_option_id | String | 配送选项ID |
| delivery_window_option_id | String | 到货时间窗口ID |
| name | String | 货件名称 |
| status | Integer | 货件状态（本地） |
| carrier | String | 海外物流 |
| remark | String | 备注 |
| transtyle | String | 配送方式 |
| transtype | String | 运输类型 |
| refreshtime | Date | 同步时间 |
| createdate | Date | 创建日期 |
| opttime | Date | 操作日期 |
| operator | String | 操作人 |
| creator | String | 创建人 |
| shiped_date | Date | 发货日期 |
| closed_date | Date | 关闭日期 |
| start_receive_date | Date | 开始接受日期 |
| referenceid | String | 参考ID |
| transport_status | String | 运输状态 |
| sync_inv | Integer | 同步货件：1代表没有扣库存，2代表已经扣库存 |
| totalunits | Integer | 装运单位数 |
| feeunit | BigDecimal | 单位手工加工费 |
| totalfee | BigDecimal | 装运的总手工加工费 |
| currency | String | 币种 |
| check_inv | BigInteger | 配货批次 |
| ignorerec | Boolean | 忽略异常 |
| isquote | Boolean | 是否报价 |
| transportation_token | String | Transportation Token |
| ordguid | String | order海关guid |
| invguid | String | inv海关guid |
| decguid | String | decguid |

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
| auditor | String | 审核人 |
| auditstatus | Integer | 审核状态 |
| auditime | Date | 审核时间 |
| shopid | String | 公司ID |
| batchnumber | String | 计划ID |
| createtime | Date | 创建时间 |
| creator | String | 创建人 |
| remark | String | 备注 |
| invtype | Integer | 单据库存类型 |
| invstatus | Integer | 库存状态 |
| shipping_date | Date | 发货日期 |
| transtype | String | 运输类型 |
| shipping_solution | String | 发货类型 |
| transtyle | String | 发货类型 |
| check_inv | BigInteger | 配货批次 |
| submitbox | Boolean | 提交箱子信息 |
| are_cases_required | Boolean | 是否原装 |
| inbound_plan_id | String | 亚马逊计划id |
| placement_option_id | String | 收货地址方案id |
| packing_option_id | String | 打包方案id |
| transportation_token | String | Transportation Token |
| shipments | String | 货件ID列表 |

### 6.2 数据传输对象 (DTO)

#### TransportationDTO

用于提交配送选项的参数对象，包含货件ID、配送选项ID、交易名称等信息。

#### DeliveryWindowDTO

用于提交到货时间窗口的参数对象，包含计划ID、货件ID、时间窗口ID等信息。

#### ShipmentDTO

用于确认发货的参数对象，包含货件ID、物流公司ID、渠道ID、运输选项ID等信息。

## 7. 业务流程

### 7.1 货件信息加载流程

1. **初始化**：页面加载时，通过 URL 参数获取计划 ID，调用 `shipmentplanApi.getPlanInfo` 获取货件计划详情。

2. **加载货件列表**：调用 `loadTable` 函数，通过 `shipmentPlacementApi.getBaseInfoByPlan` 获取货件列表。

3. **处理货件数不匹配**：如果返回的货件数与计划中的货件数不匹配，系统会尝试重新加载，最多尝试 8 次。

4. **加载相关数据**：为每个货件加载到货时间窗口和配送选项。

### 7.2 预计到货时间配置流程

1. **生成时间窗口**：用户点击「重新生成预计到货时间」按钮，调用 `generateDeliveryWindowOptionsAll` 函数，为每个货件生成到货时间窗口。

2. **选择时间窗口**：用户从下拉列表中选择一个到货时间窗口。

3. **确认时间窗口**：系统调用 `saveTime` 函数，通过 `shipmentPlacementApi.confirmDeliveryWindowOptions` 确认选择的时间窗口。

### 7.3 配送方式配置流程

1. **生成配送选项**：用户点击「重新生成物流信息」按钮，调用 `generateTransportationOptions` 函数，生成配送选项。

2. **选择配送选项**：用户从下拉列表中选择一个配送选项。

3. **保存配送选项**：系统调用 `saveDetail` 函数，通过 `shipmentTransportationApi.saveConfirmTransportationOptionsInfo` 保存选择的配送选项。

4. **提交配送信息**：用户点击「提交配送信息」按钮，调用 `submitDetail` 函数，通过 `shipmentTransportationApi.confirmTransportationOptionsByForm` 确认所有货件的配送信息。

### 7.4 物流信息配置流程

1. **打开物流信息编辑**：用户点击物流信息旁边的编辑图标，打开物流信息编辑对话框。

2. **编辑物流信息**：用户在对话框中编辑物流信息，包括物流公司、渠道等。

3. **保存物流信息**：用户点击「保存」按钮，系统保存物流信息。

4. **同步物流信息**：系统调用 `stepdataChange` 函数，同步最新的物流信息。

### 7.5 发货出库流程

1. **确认发货**：用户点击「发货出库」按钮，系统弹出确认对话框。

2. **处理库存**：如果需要处理库存，系统调用 `getInvParam`、`outShipInventory` 和 `setInvStatus` 函数，处理库存出库。

3. **保存物流跟踪**：系统调用 `saveTransTrace` 函数，为每个货件保存物流跟踪信息。

4. **完成货件计划**：系统调用 `donePlan` 函数，完成货件计划，更新货件状态。

5. **刷新数据**：系统重新加载货件数据，显示最新的货件状态。

### 7.6 耗材出库流程

1. **打开耗材出库**：用户点击「耗材出库」按钮，打开耗材出库对话框。

2. **配置耗材**：用户在对话框中配置耗材信息，包括耗材类型、数量等。

3. **提交耗材出库**：用户点击「提交」按钮，系统处理耗材出库操作。

## 8. 技术要点

### 8.1 前端技术要点

1. **Composition API**：使用 Vue 3 的 Composition API 组织代码，提高代码可读性和可维护性。

2. **响应式状态管理**：使用 `reactive` 和 `ref` 管理页面状态，确保数据变化能够及时反映到 UI 上。

3. **异步操作处理**：使用 `async/await` 和 Promise 处理 API 调用等异步操作。

4. **组件通信**：使用 `ref` 和 `emit` 实现组件之间的通信，如父组件与子组件之间的数据传递。

5. **条件渲染**：使用 `v-if` 和 `v-for` 实现条件渲染，根据不同的状态显示不同的 UI 元素。

6. **表单验证**：使用 Element Plus 的表单验证功能，确保用户输入的数据符合要求。

7. **对话框管理**：使用 Element Plus 的对话框组件，实现物流信息编辑、商品详情查看等功能。

8. **加载状态管理**：使用 Element Plus 的加载组件，显示数据加载和操作处理的状态。

### 8.2 后端技术要点

1. **RESTful API 设计**：遵循 RESTful 设计风格，使用合适的 HTTP 方法和状态码。

2. **事务管理**：使用 `@Transactional` 注解管理事务，确保数据操作的一致性。

3. **用户认证**：使用 `UserInfoContext` 获取当前用户信息，实现权限控制。

4. **数据校验**：使用 `@Valid` 和 `@NotNull` 等注解进行数据校验，确保数据的合法性。

5. **异常处理**：使用 `try-catch` 捕获和处理异常，返回友好的错误信息。

6. **文件处理**：使用 Apache POI 处理 Excel 文件，实现标签和配货单的生成和下载。

7. **异步处理**：使用 `@Async` 注解和线程池，处理耗时的操作，如货件同步。

8. **缓存管理**：使用 Redis 缓存，提高系统性能，如缓存货件信息和配送选项。

## 9. 常见问题与解决方案

### 9.1 常见问题

| 问题描述 | 可能原因 | 解决方案 |
|---------|---------|----------|
| 货件数不匹配 | 系统同步延迟或货件信息未完全创建 | 等待系统自动同步，或手动点击刷新按钮 |
| 配送选项加载失败 | 网络问题或 API 调用失败 | 检查网络连接，或重新生成配送选项 |
| 到货时间窗口生成失败 | 网络问题或 API 调用失败 | 检查网络连接，或重新生成时间窗口 |
| 物流信息保存失败 | 数据格式错误或 API 调用失败 | 检查物流信息格式，确保数据正确 |
| 标签下载失败 | 网络问题或权限不足 | 检查网络连接，确保用户有足够的权限 |
| 发货出库失败 | 库存不足或 API 调用失败 | 检查库存状态，确保库存充足 |
| 耗材出库失败 | 耗材不足或 API 调用失败 | 检查耗材库存，确保耗材充足 |

### 9.2 性能优化建议

1. **前端优化**：
   - 使用虚拟滚动处理大量货件数据
   - 合理使用缓存，减少重复 API 调用
   - 优化组件渲染，避免不必要的重渲染
   - 使用防抖和节流，减少频繁的 API 调用

2. **后端优化**：
   - 使用索引优化数据库查询
   - 合理使用缓存，减少数据库访问
   - 优化文件生成过程，提高标签和配货单的下载速度
   - 使用异步处理，提高系统响应速度

## 10. 代码优化建议

### 10.1 前端代码优化

1. **代码结构优化**：
   - 将复杂的业务逻辑拆分为多个子函数，提高代码可读性
   - 使用自定义 Hook 封装重复的逻辑，如 API 调用、数据处理等
   - 优化组件结构，减少组件的嵌套层次

2. **性能优化**：
   - 使用 `computed` 缓存计算结果，避免重复计算
   - 使用 `watchEffect` 替代 `watch`，减少不必要的依赖追踪
   - 使用 `v-memo` 指令，减少不必要的组件渲染

3. **错误处理优化**：
   - 统一处理 API 错误，提供友好的错误提示
   - 使用 `try-catch` 捕获异步操作中的错误
   - 实现全局错误处理，避免应用崩溃

4. **代码风格优化**：
   - 统一代码风格，使用 ESLint 和 Prettier 检查和格式化代码
   - 优化变量命名，使用语义化的变量名
   - 添加适当的注释，提高代码可读性

### 10.2 后端代码优化

1. **代码结构优化**：
   - 将业务逻辑从控制器中分离到服务层，提高代码可维护性
   - 使用 DTO 封装请求和响应数据，避免直接使用实体类
   - 优化包结构，按照功能模块组织代码

2. **性能优化**：
   - 使用批量操作减少数据库访问次数
   - 合理使用索引，优化查询性能
   - 使用连接池，提高数据库连接效率
   - 优化文件处理，使用流式处理减少内存占用

3. **安全性优化**：
   - 加强参数校验，防止 SQL 注入等攻击
   - 使用 HTTPS 加密传输数据，提高安全性
   - 实现接口限流，防止恶意请求
   - 加强日志记录，便于审计和问题排查

4. **代码风格优化**：
   - 统一代码风格，使用 Checkstyle 和 Spotbugs 检查代码质量
   - 优化变量命名，使用语义化的变量名
   - 添加适当的注释，提高代码可读性
   - 遵循 SOLID 原则，提高代码的可维护性和可扩展性

## 11. 总结

**发货页面**是 Wimoor ERP 系统中 FBA 发货流程的重要组成部分，主要负责管理货件信息、配置配送信息、物流信息，以及处理发货出库等操作。通过本文档的详细解析，我们可以了解到：

1. **页面结构**：采用模块化设计，包含头部信息、货件列表、操作按钮等核心模块。

2. **功能实现**：通过前端 API 调用和后端控制器的配合，实现了货件信息管理、预计到货时间配置、配送方式配置、物流信息管理、标签下载、发货出库等核心功能。

3. **数据模型**：使用 `ShipInboundShipment` 和 `ShipInboundPlan` 等实体类，构建了完整的数据模型体系。

4. **业务流程**：清晰的货件信息加载流程、预计到货时间配置流程、配送方式配置流程、物流信息配置流程和发货出库流程，确保了发货操作的顺利进行。

5. **技术要点**：使用了 Vue 3 + Composition API + Element Plus 等前端技术，以及 Spring Boot + MyBatis Plus 等后端技术，构建了一个高效、可靠的发货管理系统。

通过不断优化和改进，发货页面将为用户提供更加便捷、高效的发货体验，助力企业更好地管理 FBA 发货流程。