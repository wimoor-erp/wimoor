<template>
  <el-dialog v-model="inventoryVisible"
             width="85%"
             top="1vh"
             title="海关出口清单维护"
             style="padding: 15px;"
  >
    <!-- 海关出口清单内容 -->
    <div class="inventory-container">
      <el-form ref="inventoryForm" :model="inventoryData" label-width="140px" class="inventory-form">
        <!-- 基本信息 -->
        <el-card shadow="never" class="section-card">
          <div class="section-header">
            <h3 class="section-title">清单基本信息</h3>
          </div>

          <el-row :gutter="20">
            <el-col :span="6">
              <el-form-item label="申报类型：" prop="appType" required>
                <el-radio-group v-model="inventoryData.appType">
                  <el-radio label="1">新增</el-radio>
                  <el-radio label="2">变更</el-radio>
                  <el-radio label="3">删除</el-radio>
                </el-radio-group>
              </el-form-item>
            </el-col>

            <el-col :span="6">
              <el-form-item label="申报状态：" prop="appStatus" required>
                <el-radio-group v-model="inventoryData.appStatus">
                  <el-radio label="2">申报</el-radio>
                  <el-radio label="1">暂存</el-radio>
                </el-radio-group>
              </el-form-item>
            </el-col>

            <el-col :span="6">
              <el-form-item label="申报时间：" prop="appTime" required>
                <el-date-picker
                    v-model="inventoryData.appTime"
                    type="datetime"
                    placeholder="选择申报时间"
                    format="YYYY-MM-DD HH:mm:ss"
                    value-format="YYYYMMDDHHmmss"
                    style="width: 100%"
                />
              </el-form-item>
            </el-col>

            <el-col :span="6">
              <el-form-item label="出口日期：" prop="ieDate" required>
                <el-date-picker
                    v-model="inventoryData.ieDate"
                    type="date"
                    placeholder="选择出口日期"
                    format="YYYY-MM-DD"
                    value-format="YYYY-MM-DD"
                    style="width: 100%"
                />
              </el-form-item>
            </el-col>
          </el-row>

          <el-row :gutter="20">
            <el-col :span="6">
              <el-form-item label="申报地海关：" prop="customsCode" required>
                <CustomsSelect v-model="inventoryData.customsCode" filterable
                               :options="portCode" valueField="code" labelField="name" type="portCode" placeholder="请选择申报地海关"   />
              </el-form-item>
            </el-col>

            <el-col :span="6">
              <el-form-item label="出口口岸：" prop="portCode" required>
                <CustomsSelect v-model="inventoryData.portCode" filterable
                               :options="portCode" valueField="code" labelField="name" type="portCode" placeholder="请选择出口口岸"   />
              </el-form-item>
            </el-col>

            <el-col :span="6">
              <el-form-item label="进出口标志：" prop="ieFlag">
                <el-input v-model="inventoryData.ieFlag" placeholder="E-出口" disabled />
              </el-form-item>
            </el-col>

            <el-col :span="6">
              <el-form-item label="贸易方式：" prop="tradeMode">
                <el-select v-model="inventoryData.tradeMode" placeholder="请选择贸易方式">
                  <el-option label="9610-跨境电商B2C" value="9610" />
                  <el-option label="9810-海外仓" value="9810" />
                  <el-option label="9710-跨境电商B2B" value="9710" />
                  <el-option label="1210-保税电商" value="1210" />
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
        </el-card>

        <!-- 企业信息 -->
        <el-card shadow="never" class="section-card" style="margin-top: 15px;">
          <div class="section-header">
            <h3 class="section-title">企业信息</h3>
          </div>

          <el-row :gutter="20">
            <el-col :span="6">
              <el-form-item label="电商平台代码：" prop="ebpCode" required>
                <el-input v-model="inventoryData.ebpCode" placeholder="请输入电商平台代码" />
              </el-form-item>
            </el-col>

            <el-col :span="6">
              <el-form-item label="电商平台名称：" prop="ebpName" required>
                <el-input v-model="inventoryData.ebpName" placeholder="请输入电商平台名称" />
              </el-form-item>
            </el-col>

            <el-col :span="6">
              <el-form-item label="电商企业代码：" prop="ebcCode" required>
                <el-input v-model="inventoryData.ebcCode" placeholder="请输入电商企业代码" />
              </el-form-item>
            </el-col>

            <el-col :span="6">
              <el-form-item label="电商企业名称：" prop="ebcName" required>
                <el-input v-model="inventoryData.ebcName" placeholder="请输入电商企业名称" />
              </el-form-item>
            </el-col>
          </el-row>

          <el-row :gutter="20">
            <el-col :span="6">
              <el-form-item label="生产销售单位代码：" prop="ownerCode">
                <el-input v-model="inventoryData.ownerCode" placeholder="请输入生产销售单位代码" />
              </el-form-item>
            </el-col>

            <el-col :span="6">
              <el-form-item label="生产销售单位名称：" prop="ownerName">
                <el-input v-model="inventoryData.ownerName" placeholder="请输入生产销售单位名称" />
              </el-form-item>
            </el-col>

            <el-col :span="6">
              <el-form-item label="报关企业代码：" prop="agentCode">
                <el-input v-model="inventoryData.agentCode" placeholder="请输入报关企业代码" />
              </el-form-item>
            </el-col>

            <el-col :span="6">
              <el-form-item label="报关企业名称：" prop="agentName">
                <el-input v-model="inventoryData.agentName" placeholder="请输入报关企业名称" />
              </el-form-item>
            </el-col>
          </el-row>
        </el-card>

        <!-- 运输信息 -->
        <el-card shadow="never" class="section-card" style="margin-top: 15px;">
          <div class="section-header">
            <h3 class="section-title">运输信息</h3>
          </div>

          <el-row :gutter="20">
            <el-col :span="6">
              <el-form-item label="运输方式：" prop="trafMode">
                <el-select v-model="inventoryData.trafMode" placeholder="请选择运输方式">
                  <el-option label="6-邮件" value="6" />
                  <el-option label="5-航空运输" value="5" />
                  <el-option label="4-汽车运输" value="4" />
                  <el-option label="3-铁路运输" value="3" />
                  <el-option label="2-江海运输" value="2" />
                </el-select>
              </el-form-item>
            </el-col>

            <el-col :span="6">
              <el-form-item label="运输工具名称：" prop="trafName">
                <el-input v-model="inventoryData.trafName" placeholder="请输入运输工具名称" />
              </el-form-item>
            </el-col>

            <el-col :span="6">
              <el-form-item label="航班航次号：" prop="voyageNo">
                <el-input v-model="inventoryData.voyageNo" placeholder="请输入航班航次号" />
              </el-form-item>
            </el-col>

            <el-col :span="6">
              <el-form-item label="提运单号：" prop="billNo">
                <el-input v-model="inventoryData.billNo" placeholder="请输入提运单号" />
              </el-form-item>
            </el-col>
          </el-row>

          <el-row :gutter="20">
            <el-col :span="6">
              <el-form-item label="运抵国：" prop="country">
                <CustomsSelect v-model="inventoryData.country" filterable
                               :options="country" valueField="code" labelField="name" type="country" placeholder="运抵国"   />
              </el-form-item>
            </el-col>

            <el-col :span="6">
              <el-form-item label="指运港代码：" prop="pod">
                <CustomsSelect v-model="inventoryData.pod" filterable
                               :options="pod" valueField="code" labelField="name" type="pod" placeholder="指运港代码"   />
              </el-form-item>
            </el-col>

            <el-col :span="6">
              <el-form-item label="物流企业代码：" prop="logisticsCode" required>
                <el-input v-model="inventoryData.logisticsCode" placeholder="请输入物流企业代码" />
              </el-form-item>
            </el-col>

            <el-col :span="6">
              <el-form-item label="物流企业名称：" prop="logisticsName" required>
                <el-input v-model="inventoryData.logisticsName" placeholder="请输入物流企业名称" />
              </el-form-item>
            </el-col>
          </el-row>

          <el-row :gutter="20">
            <el-col :span="6">
              <el-form-item label="物流运单编号：" prop="logisticsNo" required>
                <el-input v-model="inventoryData.logisticsNo" placeholder="请输入物流运单编号" />
              </el-form-item>
            </el-col>

            <el-col :span="6">
              <el-form-item label="订单编号：" prop="orderNo" required>
                <el-input v-model="inventoryData.orderNo" placeholder="请输入订单编号" class="bold-input" />
              </el-form-item>
            </el-col>

            <el-col :span="6">
              <el-form-item label="企业唯一编号：" prop="copNo" required>
                <el-input v-model="inventoryData.copNo" placeholder="请输入企业唯一编号" />
              </el-form-item>
            </el-col>

            <el-col :span="6">
              <el-form-item label="预录入编号：" prop="preNo">
                <el-input v-model="inventoryData.preNo" placeholder="预录入编号（可选）" />
              </el-form-item>
            </el-col>
          </el-row>
        </el-card>

        <!-- 费用和货物信息 -->
        <el-card shadow="never" class="section-card" style="margin-top: 15px;">
          <div class="section-header">
            <h3 class="section-title">费用与货物信息</h3>
          </div>

          <el-row :gutter="20">
            <el-col :span="6">
              <el-form-item label="运费：" prop="freight">
                <el-input-number v-model="inventoryData.freight" :min="0" :precision="2" style="width: 85%" />
              </el-form-item>
            </el-col>

            <el-col :span="6">
              <el-form-item label="运费币制：" prop="fCurrency">
                <CustomsSelect
                    v-model="inventoryData.fCurrency"
                    :options="currencyOptions"
                    valueField="code"
                    labelField="encode"
                    type="currency"
                    placeholder="选择运费币制"
                    style="width: 85%"
                />
              </el-form-item>
            </el-col>

            <el-col :span="6">
              <el-form-item label="运费标记：" prop="fFlag">
                <el-select v-model="inventoryData.fFlag" placeholder="选择运费标记">
                  <el-option label="1-率" value="1" />
                  <el-option label="2-单价" value="2" />
                  <el-option label="3-总价" value="3" />
                </el-select>
              </el-form-item>
            </el-col>

            <el-col :span="6">
              <el-form-item label="保险费：" prop="insuredFee">
                <el-input-number v-model="inventoryData.insuredFee" :min="0" :precision="2" style="width: 82%" />
              </el-form-item>
            </el-col>
          </el-row>

          <el-row :gutter="20">
            <el-col :span="6">
              <el-form-item label="毛重(kg)：" prop="grossWeight" required>
                <el-input-number v-model="inventoryData.grossWeight" :min="0" :precision="3" style="width:85%" />
              </el-form-item>
            </el-col>

            <el-col :span="6">
              <el-form-item label="净重(kg)：" prop="netWeight" required>
                <el-input-number v-model="inventoryData.netWeight" :min="0" :precision="3" style="width: 85%" />
              </el-form-item>
            </el-col>

            <el-col :span="6">
              <el-form-item label="件数：" prop="packNo" required>
                <el-input-number v-model="inventoryData.packNo" :min="1" style="width: 85%" />
              </el-form-item>
            </el-col>

            <el-col :span="6">
              <el-form-item label="包装种类：" prop="wrapType">
                <el-select v-model="inventoryData.wrapType" placeholder="选择包装种类">
                  <el-option label="1-纸箱" value="1" />
                  <el-option label="2-木箱" value="2" />
                  <el-option label="3-托盘" value="3" />
                  <el-option label="4-桶装" value="4" />
                  <el-option label="5-包" value="5" />
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>

          <el-form-item label="备注：" prop="note">
            <el-input
                v-model="inventoryData.note"
                type="textarea"
                :rows="2"
                placeholder="请输入备注信息"
            />
          </el-form-item>
        </el-card>

        <!-- 商品信息 -->
        <el-card shadow="never" class="section-card" style="margin-top: 15px;">
          <div class="section-header">
            <h3 class="section-title">商品信息</h3>
          </div>

          <el-table :data="inventoryData.inventoryItems" border style="width: 100%" class="inventory-table">
            <el-table-column prop="gnum" label="序号" width="150" align="center">
              <template #default="scope">
                <el-input-number
                    v-model="scope.row.gnum"
                    :min="1"
                    controls-position="right"
                    size="small"
                    @change="updateItemSequence"
                />
              </template>
            </el-table-column>

            <el-table-column label="商品基本信息"  >
              <template #default="scope">
                <div class="item-info">
                  <div class="item-row">
                    <span class="item-label">企业货号：</span>
                    <el-input v-model="scope.row.itemNo" placeholder="企业商品编号" size="small" />
                  </div>
                  <div class="item-row">
                    <span class="item-label">商品名称：</span>
                    <el-input v-model="scope.row.itemName" placeholder="企业商品名称" size="small" />
                  </div>
                </div>
              </template>
            </el-table-column>

            <el-table-column label="海关信息" width="220">
              <template #default="scope">
                <div class="item-info">
                  <div class="item-row">
                    <span class="item-label">海关编码：</span>
                    <el-input v-model="scope.row.gcode" placeholder="海关商品编码" size="small" />
                  </div>
                  <div class="item-row">
                    <span class="item-label">海关品名：</span>
                    <el-input v-model="scope.row.gname" placeholder="商品名称" size="small" />
                  </div>
                </div>
              </template>
            </el-table-column>

            <el-table-column label="数量与单位" width="200">
              <template #default="scope">
                <div class="item-info">
                  <div class="item-row">
                    <span class="item-label">数量：</span>
                    <el-input-number
                        v-model="scope.row.qty"
                        :min="0"
                        :precision="3"
                        size="small"
                        style="width: 100px"
                        @change="updateItemTotal(scope.row)"
                    />
                  </div>
                  <div class="item-row">
                    <span class="item-label">单位：</span>
                    <CustomsSelect
                        v-model="scope.row.unit"
                        :options="unitOptions"
                        style="width: 100px"
                        valueField="code"
                        labelField="encode"
                        type="unit"
                        placeholder="单位"
                        size="small"
                    />
                  </div>
                </div>
              </template>
            </el-table-column>

            <el-table-column label="价格信息" width="180">
              <template #default="scope">
                <div class="item-info">
                  <div class="item-row">
                    <span class="item-label">单价：</span>
                    <el-input-number
                        v-model="scope.row.price"
                        :min="0"
                        :precision="2"
                        size="small"
                        style="width: 100px"
                        @change="updateItemTotal(scope.row)"
                    />
                  </div>
                  <div class="item-row">
                    <span class="item-label">币制：</span>
                    <CustomsSelect
                        v-model="scope.row.currency"
                        :options="currencyOptions"
                        style="width: 100px"
                        valueField="code"
                        labelField="encode"
                        type="currency"
                        placeholder="币制"
                        size="small"
                    />
                  </div>
                </div>
              </template>
            </el-table-column>

            <el-table-column label="其他信息" width="200">
              <template #default="scope">
                <div class="item-info">
                  <div class="item-row">
                    <span class="item-label">目的国：</span>
                    <CustomsSelect size="small" style="width:100px" v-model="scope.row.country" :options="country" valueField="code" labelField="encode" type="country" placeholder="最终目的国"   />
                  </div>
                  <div class="item-row">
                    <span class="item-label">规格型号：</span>
                    <el-input v-model="scope.row.gmodel" placeholder="规格型号" size="small" style="width: 100px" />
                  </div>
                </div>
              </template>
            </el-table-column>

            <el-table-column label="总价" width="100" align="center">
              <template #default="scope">
                <span class="total-price">{{ scope.row.totalPrice || '0.00' }}</span>
              </template>
            </el-table-column>

            <el-table-column label="操作" width="80" align="center" fixed="right">
              <template #default="scope">
                <el-button link type="danger" @click="deleteInventoryItem(scope.$index)" icon="Delete" size="small">删除</el-button>
              </template>
            </el-table-column>
          </el-table>

          <!-- 商品统计 -->
          <div class="table-summary">
            <div class="summary-left">
              <el-input
                  v-model="xmlform.fileName"
                  placeholder="文件名"
                  style="width: 720px;"
                  readonly
                  class="input-with-select"
              >
                <template #prepend>
                  文件名
                </template>
                <template #append>
                  <el-select v-model="optType" placeholder="选择生成XML名称" style="width:100px" @change="getFileName">
                      <el-option label="深圳跨综服" value="dxpId" >DXP-深圳跨综服-深圳跨境电商综合服务平台</el-option>
                      <el-option label="深关数" value="ediCode" >H2018-深关数-深圳海关大数据平台</el-option>
                  </el-select>
                </template>
              </el-input>
            </div>

            <div class="summary-right">
              <div class="summary-item">
                <span class="summary-label">商品种类：</span>
                <span class="summary-value">{{ inventoryData.inventoryItems.length }}</span>
              </div>
              <div class="summary-item">
                <span class="summary-label">商品总数：</span>
                <span class="summary-value">{{ totalItemsCount }}</span>
              </div>
              <div class="summary-item">
                <span class="summary-label">商品总价：</span>
                <span class="summary-value total-amount">{{ totalItemsAmount }}</span>
              </div>
            </div>
          </div>
        </el-card>
      </el-form>
    </div>

    <!-- 文件信息 -->
    <div class="file-info">
      <el-space>
        当前文件:
        <el-space v-if="xmlform.filePath">
          <el-link type="primary" :href="xmlform.filePath" :title="xmlform.fileName">点此下载XML文件</el-link>
          <el-icon class="pointer" @click.stop="disableInventoryXml" style="margin-left:20px"><Delete/></el-icon>
        </el-space>
        <span v-else>暂无XML文件记录</span>
      </el-space>
    </div>

    <template #footer>
      <span class="dialog-footer">
        <el-button @click="inventoryVisible = false">关闭</el-button>
        <el-button type="info" @click="resetxmlform">重置</el-button>
        <el-button type="warning" @click="handleSave('save')">保存草稿</el-button>
        <el-button type="primary" @click="handleSave('submit')"    :loading="inventoryLoading">提交</el-button>
      </span>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, reactive, toRefs, computed, watch } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Delete } from '@element-plus/icons-vue'
import shipmentCustomsApi from '@/api/erp/shipv2/shipmentCustomsApi.js'
import CustomsSelect from '@/views/erp/shipv2/shipment_handing/shipstep/components/customs_data.vue'
import {useCustomsDict} from "@/hooks/erp/shipment/custom_data.js";
const { unit,currency,wrapType,trafMode,pod,country,portCode } = useCustomsDict('unit','currency','wrapType','trafMode','pod','country','portCode')

const emit = defineEmits(['refresh'])

// 状态管理
let state = reactive({
  inventoryVisible: false,
  groupid: null,
  shipmentid: null,
  currencyOptions: [],
  unitOptions: [],
  inventoryLoading: false,

  // 清单表单数据
  xmlform: {
    xmlType: "CEB603",
    appType: "1",
    appStatus: "2",
    number: null,
    fileName: "",
    filePath: "",
    optType: "dxpId"
  },
  optType:'dxpId',
  // 清单数据
  inventoryData: {
    appType: "1",
    appStatus: "2",
    appTime: "",
    customsCode: "",
    ebpCode: "",
    ebpName: "",
    orderNo: "",
    logisticsCode: "",
    logisticsName: "",
    logisticsNo: "",
    copNo: "",
    ieFlag: "E",
    portCode: "",
    ieDate: "",
    tradeMode: "9810",
    trafMode: "6",
    trafName: "",
    billNo: "",
    country: "",
    fCurrency: "142",
    fFlag: "3",
    freight: 0,
    iCurrency: "142",
    iFlag: "3",
    insuredFee: 0,
    wrapType: "1",
    packNo: 1,
    grossWeight: 0,
    netWeight: 0,
    note: "",
    ebcCode: "",
    ebcName: "",
    ownerCode: "",
    ownerName: "",
    agentCode: "",
    agentName: "",
    inventoryItems: []
  }
})

let {
  inventoryVisible,
  xmlform,
  inventoryLoading,
  inventoryData,
  currencyOptions,
  unitOptions,
  optType
} = toRefs(state)

// 计算属性
const totalItemsCount = computed(() => {
  return inventoryData.value.inventoryItems.reduce((sum, item) => sum + (Number(item.qty) || 0), 0)
})

const totalItemsAmount = computed(() => {
  return inventoryData.value.inventoryItems.reduce((sum, item) => {
    const total = Number(item.totalPrice) || 0
    return sum + total
  }, 0).toFixed(2)
})

// 监听商品项变化，更新总重量
watch(() => inventoryData.value.inventoryItems, (newItems) => {
  const totalGrossWeight = newItems.reduce((sum, item) => {
    // 这里可以根据实际情况计算商品重量
    const weightPerUnit = 0.5 // 假设每个商品0.5kg
    return sum + (Number(item.qty) || 0) * weightPerUnit
  }, 0)

  if (totalGrossWeight > 0) {
    inventoryData.value.grossWeight = totalGrossWeight
    inventoryData.value.netWeight = totalGrossWeight * 0.95 // 假设净重为毛重的95%
    inventoryData.value.packNo = Math.ceil(totalGrossWeight / 20) // 假设每件最大20kg
  }
}, { deep: true })

// 方法
const updateItemTotal = (item) => {
  if (item.qty && item.price) {
    item.totalPrice = Number(item.qty) * Number(item.price)
  } else {
    item.totalPrice = 0
  }
}

const updateItemSequence = () => {
  // 对商品项按gnum排序
  inventoryData.value.inventoryItems.sort((a, b) => a.gnum - b.gnum)
}

const addInventoryItem = () => {
  const newItem = {
    gnum: inventoryData.value.inventoryItems.length + 1,
    itemNo: "",
    itemRecordNo: "",
    itemName: "",
    gcode: "",
    gname: "",
    gmodel: "",
    barCode: "",
    country: "",
    currency: "142",
    qty: 0,
    qty1: 0,
    qty2: 0,
    unit: "",
    unit1: "",
    unit2: "",
    price: 0,
    totalPrice: 0,
    note: ""
  }
  inventoryData.value.inventoryItems.push(newItem)
}

const deleteInventoryItem = (index) => {
  ElMessageBox.confirm('确定删除该商品项吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    inventoryData.value.inventoryItems.splice(index, 1)
    // 重新排序
    inventoryData.value.inventoryItems.forEach((item, idx) => {
      item.gnum = idx + 1
    })
    ElMessage.success('删除成功')
  })
}

const importInventoryItems = () => {
  // 这里可以实现商品导入逻辑
  ElMessage.info('商品导入功能待实现')
}

async function getFileName(){
  let data = JSON.parse(JSON.stringify(state.xmlform))
  data.optType = state.optType
  await shipmentCustomsApi.generateFileName(data).then((res) => {
    if (res.data) {
      state.xmlform.fileName = res.data;
      state.xmlform.filePath=null;
    }
  })
}

function handleSave(ftype){
  state.xmlform.optType=ftype;
  generateInventoryXml();
}

const generateInventoryXml = () => {
  state.inventoryLoading = true
  state.xmlform.number = state.shipmentid
  state.xmlform.groupid = state.groupid
  state.xmlform.inventoryData = state.inventoryData

  // 验证必填字段
  if (!inventoryData.value.customsCode) {
    ElMessage.warning('请填写申报地海关')
    state.inventoryLoading = false
    return
  }

  if (!inventoryData.value.ebpCode || !inventoryData.value.ebpName) {
    ElMessage.warning('请填写电商平台信息')
    state.inventoryLoading = false
    return
  }

  if (!inventoryData.value.ebcCode || !inventoryData.value.ebcName) {
    ElMessage.warning('请填写电商企业信息')
    state.inventoryLoading = false
    return
  }

  if (!inventoryData.value.orderNo) {
    ElMessage.warning('请填写订单编号')
    state.inventoryLoading = false
    return
  }

  if (inventoryData.value.inventoryItems.length === 0) {
    ElMessage.warning('请至少添加一个商品项')
    state.inventoryLoading = false
    return
  }

  // 调用API生成清单XML
  shipmentCustomsApi.generateCustomsXml(state.xmlform).then((res) => {
    ElMessage.success('生成海关清单XML成功')
    state.guid = res.data;
    loadInventoryXml()
    state.inventoryLoading = false
  }).catch((error) => {
    ElMessage.error('生成清单XML失败：' + error.message)
    state.inventoryLoading = false
  })
}

const disableInventoryXml = () => {
  ElMessageBox.confirm(
      '您确认要删除吗?',
      '提示',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      }
  ).then(() => {
    let groupid = state.groupid
    let ftype = state.xmlform.xmlType
    let number = state.shipmentid
    shipmentCustomsApi.disabledCustomsXml({"groupid": groupid, "ftype": ftype, "number": number}).then((res) => {
      ElMessage.success('删除清单XML成功')
      loadInventoryXml()
    })
  }).catch(() => {
    ElMessage({
      type: 'info',
      message: '取消操作',
    })
  })
}

function loadInventoryXml(){
  let groupid = state.groupid
  let ftype = state.xmlform.xmlType
  let number = state.shipmentid
  if(!state.guid){
    state.guid="";
  }
  shipmentCustomsApi.getCustomsXml({"guid": state.guid}).then(async (res) => {
    if (res.data && res.data.filePath) {
      state.xmlform=res.data;
    } else {
      state.xmlform.xmlType = "CEB603"
      state.xmlform.appType = "1"
      state.xmlform.appStatus = "2"
      state.xmlform.number = state.shipmentid
      await getFileName()
    }
    if( state.xmlform.content&& "undefined"!= state.xmlform.content){
      var data=JSON.parse(state.xmlform.content);
      state.xmlform.inventoryData=data;
      state.inventoryData=state.xmlform.inventoryData;
    }else{
      loadInventoryData();
    }
  })
}

const loadInventoryData = () => {
  shipmentCustomsApi.viewXmlData(state.xmlform).then((res) => {
    if (res.data && res.data.inventoryData) {
      // 合并数据，保留默认值
      state.xmlform=res.data;
      state.inventoryData=res.data.inventoryData;
    }else{
      state.xmlform.xmlType = "CEB603"
      state.xmlform.appType = "1"
      state.xmlform.appStatus = "2"
      state.xmlform.number = state.shipmentid
    }
  })
}


const resetxmlform = () => {
  ElMessageBox.confirm('确定重置所有表单数据吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    // 重置清单数据
    Object.assign(state.inventoryData, {
      appType: "1",
      appStatus: "2",
      appTime: "",
      customsCode: "",
      ebpCode: "",
      ebpName: "",
      orderNo: "",
      logisticsCode: "",
      logisticsName: "",
      logisticsNo: "",
      copNo: "",
      ieFlag: "E",
      portCode: "",
      ieDate: "",
      tradeMode: "9810",
      trafMode: "6",
      trafName: "",
      billNo: "",
      country: "",
      fCurrency: "142",
      fFlag: "3",
      freight: 0,
      iCurrency: "142",
      iFlag: "3",
      insuredFee: 0,
      wrapType: "1",
      packNo: 1,
      grossWeight: 0,
      netWeight: 0,
      note: "",
      ebcCode: "",
      ebcName: "",
      ownerCode: "",
      ownerName: "",
      agentCode: "",
      agentName: "",
      inventoryItems: []
    })

    ElMessage.success('重置成功')
  })
}

async function show(groupid, shipmentid,guid) {
  state.guid=guid;
  state.groupid = groupid
  state.shipmentid = shipmentid
  state.inventoryVisible = true
  state.xmlform.number = shipmentid
  state.xmlform.groupid = groupid
  state.xmlform.xmlType="CEB603";
  // 加载下拉选项
  state.currencyOptions = await loadOptions("currency", "code", "encode")
  state.unitOptions = await loadOptions("unit", "code", "name")

  // 设置默认时间
  const now = new Date()
  state.inventoryData.appTime = now.toISOString().replace('T', ' ').substring(0, 19)
  state.inventoryData.ieDate = now.toISOString().substring(0, 10)

  // 加载现有数据和XML文件
  loadInventoryXml()
  //loadInventoryData()
}

// 加载选项数据
async function loadOptions(type, valueField, labelField) {
  const response = await shipmentCustomsApi.customsData({"type": type})
  if (response && response.data) {
    let data = response.data
    if (Array.isArray(data)) {
      return data.map(item => ({
        value: item[valueField] || item.value || item.id,
        label: item[labelField] || item.label || item.name,
      }))
    }
  }
  return []
}

defineExpose({ show })
</script>

<style scoped>
.inventory-container {
  padding: 10px 0;
}

.inventory-form {
  margin-top: 10px;
}

.section-card {
  margin-bottom: 15px;
  border: 1px solid #ebeef5;
  border-radius: 6px;
}

.section-header {
  padding: 12px 0;
  margin-bottom: 15px;
  border-bottom: 1px solid #ebeef5;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.section-title {
  margin: 0;
  color: #303133;
  font-size: 16px;
  font-weight: 600;
}

.section-actions {
  display: flex;
  gap: 10px;
}

.bold-input :deep(.el-input__inner) {
  font-weight: 600;
  color: #303133;
}

.inventory-table {
  margin-top: 8px;
  font-size: 12px;
}

.inventory-table :deep(.el-table__header) {
  background-color: #f5f7fa;
}

.item-info {
  padding: 5px 0;
}

.item-row {
  display: flex;
  align-items: center;
  margin-bottom: 8px;

  &:last-child {
    margin-bottom: 0;
  }
}

.item-label {
  width: 60px;
  color: #606266;
  font-size: 12px;
  white-space: nowrap;
}

.total-price {
  color: #f56c6c;
  font-weight: 600;
}

.table-summary {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 15px;
  padding: 12px;
  background-color: #f8f9fa;
  border-radius: 4px;
}

.summary-left {
  flex: 1;
}

.summary-right {
  display: flex;
  justify-content: flex-end;
  gap: 30px;
}

.summary-item {
  display: flex;
  align-items: center;
}

.summary-label {
  color: #606266;
  font-size: 13px;
}

.summary-value {
  margin-left: 6px;
  color: #303133;
  font-size: 14px;
  font-weight: 600;
}

.total-amount {
  color: #f56c6c;
  font-size: 15px;
}

.file-info {
  margin-top: 15px;
  padding: 10px;
  background-color: #f8f9fa;
  border-radius: 4px;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}

/* 对话框内表单样式优化 */
:deep(.el-dialog__body) {
  max-height: 75vh;
  overflow-y: auto;
}

/* 响应式调整 */
@media (max-width: 1200px) {
  :deep(.el-dialog) {
    width: 95% !important;
  }

  .table-summary {
    flex-direction: column;
    align-items: stretch;
    gap: 10px;
  }

  .summary-right {
    justify-content: space-between;
  }
}

@media (max-width: 768px) {
  .section-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 10px;
  }

  .section-actions {
    align-self: flex-end;
  }

  .summary-item {
    flex-direction: column;
    align-items: flex-start;
  }
}
</style>