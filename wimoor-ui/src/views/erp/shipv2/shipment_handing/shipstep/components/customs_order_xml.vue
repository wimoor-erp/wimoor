<template>
  <el-dialog v-model="xmlVisible"
             width="80%"
             top="2vh"
             title="FBA货件订单推送"
             style="padding: 15px;"
  >
    <!-- FBA订单表单内容 -->
    <div class="fba-order-container">
      <el-form ref="orderForm" :model="orderData" label-width="120px" class="order-form">
        <!-- 订单详细信息 -->
        <el-card shadow="never" class="section-card">



          <!-- 订单信息 -->
            <el-row :gutter="20">
              <el-col :span="6">
                <el-form-item label="订单编号：">
                  <el-input v-model="orderData.orderNo" placeholder="订单编号"   class="bold-input" />
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item label="报送类型：">
                  <el-radio-group v-model="orderData.appType">
                    <el-radio label="1">新增</el-radio>
                    <el-radio label="2">变更</el-radio>
<!--                    <el-radio label="3">删除</el-radio>-->
                  </el-radio-group>
                </el-form-item>
              </el-col>




              <el-col :span="6">
                <el-form-item label="报送日期：">
                  <el-date-picker
                      v-model="orderData.appTime"
                      type="datetime"
                      placeholder="选择报送日期"
                      format="YYYY-MM-DD HH:mm:ss"
                      value-format="YYYYMMDDHHmmss"
                      style="width: 100%"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item label="报送状态：">
                  <el-radio-group v-model="orderData.appStatus"  placeholder="报送状态">
                    <el-radio label="2">申报</el-radio>
                    <el-radio label="1">暂存</el-radio>
                  </el-radio-group>
                </el-form-item>
              </el-col>
            </el-row>


            <el-row :gutter="20">
              <el-col :span="6">
                <el-form-item label="电商平台名称：">
                  <el-input v-model="orderData.ebpName" placeholder="电商平台名称"   />
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item label="电商平台代码：">
                  <el-input v-model="orderData.ebpCode" placeholder="电商平台代码"   />
                </el-form-item>
              </el-col>




              <el-col :span="6">
                <el-form-item label="订单类型：">
                  <el-radio-group v-model="orderData.orderType">
                    <el-radio label="W">海外仓</el-radio>
                    <el-radio label="E">B2C</el-radio>
                    <el-radio label="B">B2B</el-radio>
                    <!--                    <el-radio label="3">删除</el-radio>-->
                  </el-radio-group>
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item label="订单总金额：">
                  <el-input v-if="orderData" v-model="orderData.goodsValue" placeholder="订单总金额"  >
                  </el-input>
                </el-form-item>
              </el-col>
            </el-row>



            <el-row :gutter="20">
              <el-col :span="6">
                <el-form-item label="电商企业名称：">
                  <el-input v-model="orderData.ebcName" placeholder="电商企业名称"   />
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item label="电商企业代码：">
                  <el-input v-model="orderData.ebcCode" placeholder="电商企业代码"   />
                </el-form-item>
              </el-col>


              <el-col :span="6">
                <el-form-item label="运杂费：">
                  <el-input v-model="orderData.shippingFee" placeholder="运杂费"  > </el-input>
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item label="币制：">
                  <CustomsSelect v-model="orderData.currency" :options="currencyOptions" valueField="code" labelField="encode" type="currency" placeholder="币制"   />
                </el-form-item>
              </el-col>
            </el-row>

          <el-form-item label="其他备注：">
            <el-input
                v-model="orderData.note"
                type="textarea"
                :rows="2"
                placeholder="其他备注"
                class="remarks-textarea"
            />
          </el-form-item>



        <!-- 商品信息表格 -->
         <div class="flex-between">
            <h3 style="width:90%">商品信息


              <el-button size="small" @click="openSortDialog">调整货件排序</el-button>
            </h3>
            <el-space>
              <div class="font-extraSmall">选中一个之后按住shift键再选择最后一个商品，可以完成自动多选</div>
              <el-button type="primary" @click="handleMergeItems" >合并选中行</el-button>

            </el-space>
         </div>


          <el-table ref="orderTableRef" :data="tableData" row-key="itemNo" @selection-change="handleSelectionChange" border style="width: 100%" class="product-table" @select="handleSelect">
            <el-table-column type="selection" width="55" align="center" />
            <el-table-column prop="gnum" label="序号" width="70" align="center" >
              <template #header>
                <div class="flex-center">序号 <el-icon @click="sortIndex()" style="padding-left:2px" class="pointer"><Sort /></el-icon></div>
              </template>
            </el-table-column>
            <el-table-column prop="image" label="企业商品货号" show-overflow-tooltip width="270">
              <template #default="scope">
                <table>
                  <tr>
                  <td>
                    <el-image  :src="scope.row.image"   style="width: 36px;height: 36px;margin-right: 3px;"   ></el-image>
                  </td>
                  <td>
                    <div><span class="font-bold">{{scope.row.itemNo}}</span></div>
                    <div v-if="scope.row.itemInfoExt">{{scope.row.itemInfoExt.pname}}</div>
                  </td>
                  </tr>
                </table>
              </template>
            </el-table-column>
            <el-table-column prop="itemName" label="企业商品名称" width="150" />
            <el-table-column prop="barCode" label="条形码" width="120" />
            <el-table-column prop="unit" label="计量单位" width="110" align="center" >
            <template #default="scope">
              <CustomsSelect v-model="scope.row.unit" :filterable="true" :options="unitOptions" style="width:80px" valueField="code" labelField="encode" type="unit" placeholder="单位"   />
            </template>
            </el-table-column>
            <el-table-column prop="currency" label="币制" width="110" align="center" >
              <template #default="scope">
                <CustomsSelect v-model="scope.row.currency" style="width:80px" :options="currencyOptions" valueField="code" labelField="encode" type="currency" placeholder="币制"   />
              </template>
            </el-table-column>
            <el-table-column prop="qty" label="数量" width="90" align="center" >
              <template #default="scope">
                <el-input v-model="scope.row.qty"  @input="calculateTotalPrice(scope.row,'qty')" placeholder="数量" style="width:60px" />
              </template>
            </el-table-column>
            <el-table-column prop="price" label="单价" width="140" align="center">
              <template #default="scope">
                <el-input v-model="scope.row.price"  @input="calculateTotalPrice(scope.row,'price')" placeholder="单价" style="width:110px" />
              </template>
            </el-table-column>
            <el-table-column prop="totalPrice" label="总价" width="160" align="center">
              <template #default="scope">
               <el-input v-model="scope.row.totalPrice" @input="calculateTotalPrice(scope.row,'totalPrice')" placeholder="总价" style="width:120px" />
              </template>
            </el-table-column>
            <el-table-column prop="note" label="备注"  align="center" >
             <template #default="scope">
               <el-input v-model="scope.row.note"   placeholder="备注" style="width:100%"  type="textarea" :rows="2" />
              </template>
            </el-table-column>
          </el-table>



            <div class="table-summary flex-between">
              <div>

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
                    <el-select v-model="optType" placeholder="请选择XML生成名称" style="width:120px" @change="getFileName"  >
                      <el-option label="深圳跨综服" value="dxpId" >DXP-深圳跨综服-深圳跨境电商综合服务平台</el-option>
                      <el-option label="深关数" value="ediCode" >H2018-深关数-深圳海关大数据平台</el-option>
                    </el-select>
                  </template>
                </el-input>

              </div>
              <div style="  display: flex; justify-content: flex-end;">
              <div class="summary-item">
                <span class="summary-label">商品总数：</span>
                <span class="summary-value">{{ totalProductsCount }}</span>
              </div>
              <div class="summary-item">
                <span class="summary-label">商品总价：</span>
                <span class="summary-value total-amount" v-if="orderData">{{ orderData.goodsValue }}  </span>
              </div>
              </div>
            </div>


        </el-card>

      </el-form>
    </div>
    <el-space>
      当前文件:
      <el-space v-if="xmlform.filePath">
        <el-link type="primary" :href="xmlform.filePath" target="_blank" :title="xmlform.fileName">点此下载XML文件</el-link>
        <el-icon class="pointer" @click.stop="disableCustomsXml" style="margin-left:20px"><Delete/></el-icon>
      </el-space>
      <span v-else>暂无XML文件记录</span>

      <el-link type="primary" @click="downloadCustomsExcel"  style="margin-left: 25px;"  :loading="xmlLoading">生成物流Excel文件</el-link>
    </el-space>

    <template #footer>
    <span class="dialog-footer">
      <el-button @click="xmlVisible = false">关闭</el-button>
      <el-button type="primary" @click="downloadCustomsFile" :loading="xmlLoading">提交</el-button>
    </span>
    </template>
  </el-dialog>

  <!-- 货件排序弹窗 -->
  <el-dialog v-model="sortDialogVisible" title="调整货件排序" top="20vh" width="600px">
    <div class="sortable-list" ref="sortableListRef" style="width: 100%; border: 1px solid #e4e7ed; border-radius: 4px; padding: 10px; min-height: 200px;">
      <div v-for="(item, index) in shipmentNumberList" :key="item" class="sortable-item">
        <span class="drag-handle">☰</span>
        <span>{{ item }}</span>
      </div>
    </div>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="sortDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSaveSort">保存排序</el-button>
      </span>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, reactive, onMounted, toRefs, computed, nextTick, watch, onUnmounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Delete, Sort } from '@element-plus/icons-vue'
import Sortable from 'sortablejs'
import shipmentCustomsApi from '@/api/erp/shipv2/shipmentCustomsApi.js';
import { formatFloat }  from '@/utils/index.js'
import CustomsSelect from '@/views/erp/shipv2/shipment_handing/shipstep/components/customs_data.vue'

const emit = defineEmits(['stepdata'])
const xmlViewerRef = ref(null)
const orderTableRef = ref(null)
const sortableListRef = ref(null)
let shipInfo = ref({})

let state = reactive({
  xmlVisible: false,
  sortDialogVisible: false,
  groupid: null,
  shipmentid: null,
  currencyOptions:[],
  unitOptions: [],
  selectRows:[],
  shiftKeyPressed: false,
  lastSelectedRow: null,
  xmlform: {
    xmlType: "CEB303",
    appType: "1",
    appStatus: "2",
    number: null,
  },
  optType:'dxpId',
  xmlLoading: false,
  // FBA订单表单数据
  orderData: { orderItems: [] },
  // 表格数据
  tableData: []
})

let { xmlVisible, sortDialogVisible, xmlform,editShipmentIndex, xmlLoading, orderData, tableData, currencyOptions, unitOptions, optType } = toRefs(state)

// 计算属性
const shipmentNumberList = computed(() => {
  if (xmlform.value.number) {
    return xmlform.value.number.split(',').map(item => item.trim())
  }
  return []
})

const totalProductsCount = computed(() => {
  return state.tableData.reduce((sum, item) => sum + parseInt(item.qty|| 0), 0)
})

// 处理选择项变化
function handleSelectionChange(selection) {
    state.selectRows = selection;
}

// 处理表格行选中事件
function handleSelect(selection, row) {
  // 判断当前是选中还是取消选中
  const isSelected = selection.some(item => item.itemNo === row.itemNo)
  
  if (isSelected && state.shiftKeyPressed && state.lastSelectedRow && orderTableRef.value) {
    // 获取当前选中行的索引
    const currentIndex = state.tableData.findIndex(item => item.itemNo === row.itemNo)
    // 获取上一次选中行的索引
    const lastIndex = state.tableData.findIndex(item => item.itemNo === state.lastSelectedRow.itemNo)
    
    if (currentIndex !== -1 && lastIndex !== -1 && currentIndex !== lastIndex) {
      // 计算起始和结束索引
      const startIndex = Math.min(currentIndex, lastIndex)
      const endIndex = Math.max(currentIndex, lastIndex)
      
      // 保留之前的选中状态，只追加选中区间内的行
      for (let i = startIndex; i <= endIndex; i++) {
        const item = state.tableData[i]
        // 检查该行是否已被选中
        const isAlreadySelected = state.selectRows.some(selectedItem => selectedItem.itemNo === item.itemNo)
        if (!isAlreadySelected) {
          orderTableRef.value.toggleRowSelection(item, true)
        }
      }
      
      // 更新选中行列表（包含之前选中的和新选中的）
      const rangeItems = state.tableData.slice(startIndex, endIndex + 1)
      const newSelectRows = [...state.selectRows]
      rangeItems.forEach(item => {
        if (!newSelectRows.some(selectedItem => selectedItem.itemNo === item.itemNo)) {
          newSelectRows.push(item)
        }
      })
      state.selectRows = newSelectRows
    }
  }
  
  // 更新上一次选中的行（只有选中时才更新）
  if (isSelected) {
    state.lastSelectedRow = { ...row }
  }
}

// Shift键按下事件处理
function handleKeyDown(e) {
  if (e.key === 'Shift') {
    state.shiftKeyPressed = true
  }
}

// Shift键释放事件处理
function handleKeyUp(e) {
  if (e.key === 'Shift') {
    state.shiftKeyPressed = false
  }
}

function mergeOrderItems(orderItems, gnumsToMerge) {
  // 如果没有需要合并的项或数组为空，直接返回原数组
  if (!gnumsToMerge || gnumsToMerge.length === 0 || !orderItems || orderItems.length === 0) {
    return orderItems;
  }
  // 1. 找到第一个被选中项目的位置
  const firstSelectedIndex = orderItems.findIndex(item => gnumsToMerge.includes(item.gnum));
  if (firstSelectedIndex === -1) {
    return orderItems;
  }

  // 2. 分离需要合并的项和不需要合并的项
  const itemsToMerge = orderItems.filter(item => gnumsToMerge.includes(item.gnum));
  const otherItems = orderItems.filter(item => !gnumsToMerge.includes(item.gnum));

  // 如果没有找到需要合并的项，直接返回原数组
  if (itemsToMerge.length === 0) {
    return orderItems;
  }

  // 如果只有一个需要合并的项，不需要合并，直接返回
  if (itemsToMerge.length === 1) {
    return orderItems;
  }

  // 检查所有待合并项的price是否一致
  const firstPrice = itemsToMerge[0].price;
  const allPricesSame = itemsToMerge.every(item => item.price === firstPrice);

  // 3. 创建合并后的item
  const mergedItem = {
    // 取第一个item的信息
    gnum: Math.min(...itemsToMerge.map(item => item.gnum)), // 取最小的gnum
    itemNo: itemsToMerge[0].itemNo,
    itemName: itemsToMerge[0].itemName,
    image:itemsToMerge[0].image,
    itemDescribe: itemsToMerge[0].itemDescribe,
    barCode: "",
    unit: itemsToMerge[0].unit,
    currency: itemsToMerge[0].currency,

    // qty累加
    qty: itemsToMerge.reduce((sum, item) => sum + (item.qty || 0), 0),

    // totalPrice累加
    totalPrice: itemsToMerge.reduce((sum, item) => sum + (item.totalPrice || 0), 0),

    // note自动合并逗号分隔
    note: itemsToMerge.map(item => {
      const unitInfo = state.unitOptions.find(unit => unit.value === item.unit);
      const unitText = unitInfo ? unitInfo.label : '';
      return `${item.itemNo}(${item.qty}${unitText})`;
    }).join(', '),
  };

  // 4. 合并itemInfoExt字段
  if (itemsToMerge[0].itemInfoExt) {
    mergedItem.itemInfoExt = {
      ...itemsToMerge[0].itemInfoExt,
      // 更新数量相关字段
      quantity: mergedItem.qty,
      qty2: itemsToMerge[0].itemInfoExt.unitrate ? 
        String(mergedItem.qty * parseInt(itemsToMerge[0].itemInfoExt.unitrate)) : '',
      // 更新总价
      totalprice: formatFloat(mergedItem.totalPrice),
      // 累加重量
      weight: itemsToMerge.reduce((sum, item) => {
        const itemWeight = item.itemInfoExt && item.itemInfoExt.weight ? 
          parseFloat(item.itemInfoExt.weight) : 0;
        return sum + itemWeight;
      }, 0).toString() || ''
    };
  }

  // 4. 计算price：如果所有price一致则使用该价格，否则置为空
  if (allPricesSame && firstPrice !== undefined && firstPrice !== null) {
    mergedItem.price = formatFloat(firstPrice);
  } else {
    mergedItem.price = ""; // 价格不一致时置为空字符串
  }

  mergedItem.totalPrice = formatFloat(mergedItem.totalPrice);

  // 5. 保持原始顺序，将合并后的项插入到第一个选中项的位置
  // 分离出第一个选中项之前和之后的不需要合并的项
  const itemsBeforeFirstSelected = otherItems.filter(item => orderItems.indexOf(item) < firstSelectedIndex);
  const itemsAfterFirstSelected = otherItems.filter(item => orderItems.indexOf(item) > firstSelectedIndex);

  // 构建新的数组：前半部分 + 合并后的项 + 后半部分
  const allMergedItems = [...itemsBeforeFirstSelected, mergedItem, ...itemsAfterFirstSelected];

  // 6. 重新生成gnum（按顺序从1开始）
  return allMergedItems.map((item, index) => ({
    ...item,
    gnum: index + 1
  }));
}
function handleMergeItems(){
  const selectedRows = orderTableRef.value.getSelectionRows()
  if(selectedRows.length===0){
    ElMessage.error('请选择需要合并的商品项')
    return
  }
  // 调用合并函数
  state.tableData=mergeOrderItems(state.tableData,selectedRows.map(item=>item.gnum));
  // 更新orderData中的orderItems
  //state.orderData.orderItems = state.tableData;
  initSortable();
}
function totalProductsAmount(){
  orderData.value.goodsValue= state.tableData.reduce((sum, item) => {
    const quantity = item.qty || 0
    const unitPrice = parseFloat(item.price) || 0
    return sum + (quantity * unitPrice)
  }, 0).toFixed(4)
}

// 方法
const calculateTotalPrice = (row,type) => {
  if(type==='price'||type==='qty'){
    const quantity = row.qty || 0
    const unitPrice = parseFloat(row.price) || 0
    row.totalPrice= (quantity * unitPrice).toFixed(4);
  }else{
    const quantity = row.qty || 0
    const totalPrice = parseFloat(row.totalPrice) || 0
    row.price= (totalPrice/(quantity*1.0)).toFixed(4);
  }
  totalProductsAmount();
}

const getReportTypeTag = (type) => {
  const tagMap = {
    '新增': 'success',
    '修改': 'warning',
    '删除': 'danger'
  }
  return tagMap[type] || 'info'
}

async function getFileName(){
  if(  state.xmlform.orderData){
       state.xmlform.orderData.orderItems=state.tableData;
  }
  let data=JSON.parse(JSON.stringify(state.xmlform));
  data.optType=state.optType;
  await shipmentCustomsApi.generateFileName(data).then((res) => {
    if(res.data){
      state.xmlform.fileName=res.data;
      state.xmlform.filePath=null;
    }
  })
}

function downloadCustomsExcel(){
  state.xmlLoading = true
  // 构建请求数据
  let data = {
    groupid: state.groupid,
    number: state.shipmentid,
    orderData: state.orderData
  }
  // 如果有商品数据，添加到请求中
  if (state.orderData && state.tableData.length > 0) {
    data.orderData.orderItems = state.tableData
  }

  shipmentCustomsApi.downloadCustomsExcel(data).then(() => {
    state.xmlLoading = false
  }).catch(() => {
    state.xmlLoading = false
  })
}


function downloadCustomsFile() {
  state.xmlLoading = true
  state.xmlform.number = state.shipmentid;
  state.xmlform.groupid = state.groupid;
  state.xmlform.orderData=state.orderData;
  if(  state.xmlform.orderData){
       state.xmlform.orderData.orderItems=state.tableData;
  }
  // 验证商品项数据
  for (const item of state.orderData.orderItems) {

    if (!item.itemName || !item.unit) {
      ElMessage.error('商品项必须填写商品名称和商品单位')
      state.xmlLoading= false
      return
    }
    if (!item.price) {
      ElMessage.error('商品项必须填写商品单价')
      state.xmlLoading= false
      return
    }
  }

  shipmentCustomsApi.generateCustomsXml(state.xmlform).then((res) => {
    ElMessage.success('生成海关XML成功')
    state.guid = res.data;
    loadXml()
    state.xmlLoading = false
  })
}

function disableCustomsXml() {
  ElMessageBox.confirm(
      '您确认要删除吗?',
      '提示',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      }
  )
      .then(() => {
        let groupid = state.groupid
        let ftype = state.xmlform.xmlType
        let number = state.shipmentid
        shipmentCustomsApi.disabledCustomsXml({"groupid": groupid, "ftype": ftype, "number": number}).then((res) => {
          ElMessage.success('删除海关XML成功')
          loadXml()
        })
      })
      .catch(() => {
        ElMessage({
          type: 'info',
          message: '取消操作',
        })
      })
}
 
// 初始化拖拽排序
function initSortable() {
  nextTick(() => {
    if (orderTableRef.value) {
      const tbody = orderTableRef.value.$el.querySelector('.el-table__body-wrapper tbody')
      if (tbody) {
        new Sortable(tbody, {
          animation: 150,
          ghostClass: 'sortable-ghost',
          onEnd: function(evt) {
            // 拖拽结束后，先更新数组顺序
            const oldIndex = evt.oldIndex
            const newIndex = evt.newIndex
            if (oldIndex !== newIndex) {
              const [movedItem] = state.tableData.splice(oldIndex, 1);
              state.tableData.splice(newIndex, 0, movedItem);
              // 重新生成gnum（按顺序从1开始）
              state.tableData = state.tableData.map((item, index) => ({
                ...item,
                gnum: index + 1
              }))
            }
          }
        })
      }
    }
  })
}
function loadOrderData(){
  if(  state.xmlform.orderData){
       state.xmlform.orderData.orderItems=state.tableData;
  }
  shipmentCustomsApi.viewXmlData(state.xmlform).then((res) => {
    if(res.data) {
      state.xmlform=res.data;
      state.orderData = state.xmlform.orderData;
      // 将orderItems赋值给tableData
      state.tableData = state.orderData.orderItems;
      // 重置上次选中的行，避免数据变化后引用失效
      state.lastSelectedRow = null;
      initSortable();
    }
  })
}

function loadXml(){
  state.xmlVisible=true;
  let groupid=state.groupid;
  let ftype=state.xmlform.xmlType;
  let number=state.shipmentid;
  if(!state.guid){
    state.guid="";
  }
  shipmentCustomsApi.getCustomsXml({"guid":state.guid}).then(async (res)=>{
    if(res.data && res.data.number){
      state.xmlform=res.data;
      state.shipmentid=state.xmlform.number;
      if(!res.data.fileName){
        await getFileName();
      }
    }else{
      state.xmlform.xmlType="CEB303";
      state.xmlform.appType="1";
      state.xmlform.appStatus="2";
      state.xmlform.number=state.shipmentid;
      state.orderData={};
      state.xmlform.orderData={};
      await getFileName();
    }
    if( state.xmlform.content && "undefined"!= state.xmlform.content){
      state.orderData=JSON.parse(state.xmlform.content);
      state.xmlform.orderData=state.orderData;
      // 将orderItems赋值给tableData
      state.tableData = state.orderData.orderItems;
      // 重置上次选中的行，避免数据变化后引用失效
      state.lastSelectedRow = null;
      initSortable();
      // 初始化拖拽排序
    }else{
      loadOrderData();
    }

  });

}
function openSortDialog(){
  state.sortDialogVisible = true;
  // 延迟初始化拖拽排序，确保DOM已经渲染
  setTimeout(() => {
    initShipmentSortable();
  }, 100);
}

function handleSaveSort(){
  // 更新货件单号到xmlform.number
  state.xmlform.number = shipmentNumberList.value.join(',');
  state.shipmentid=state.xmlform.number;
  state.sortDialogVisible = false;
  loadOrderData();
}

function initShipmentSortable(){
  nextTick(() => {
    if (sortableListRef.value) {
      new Sortable(sortableListRef.value, {
        animation: 150,
        onEnd: function(evt) {
          // 获取当前货件单号列表
          const currentList = [...shipmentNumberList.value];
          // 重新排序
          const [movedItem] = currentList.splice(evt.oldIndex, 1);
          currentList.splice(evt.newIndex, 0, movedItem);
          // 更新xmlform.number
          state.xmlform.number = currentList.join(',');
        }
      });
    }
  });
}

// 监听sortDialogVisible变化，初始化拖拽排序
watch(sortDialogVisible, (newVal) => {
  if (newVal) {
    initShipmentSortable();
  }
});
function handleQuery() {
  shipmentCustomsApi.getShipmentCustomsRecord().then((res) => {
    state.tableData = res.data
  })
}

async function show(groupid, shipmentid,guid) {
  state.guid=guid;
  state.groupid = groupid
  state.shipmentid = shipmentid
  state.xmlVisible = true;
  state.xmlform.number=shipmentid;
  state.xmlform.groupid=groupid;
  state.currencyOptions=await loadOptions("currency","code","encode");
  state.unitOptions=await loadOptions("unit","code","name");
  loadXml();


}

// 模拟异步加载数据
async function loadOptions (type,valueField,labelField)  {
  // 调用接口获取数据
  const response = await shipmentCustomsApi.customsData({"type": type});
  // 处理API返回的数据
  if (response && response.data) {
    let data = response.data;
    // 如果返回的是数组
    if (Array.isArray(data)) {
      let options = data.map(item => ({
        value: item[valueField] || item.value || item.id,
        label: item[labelField] || item.label || item.name,
      }))
      return options;
    }
  }
}

onMounted(() => {
  // 添加键盘事件监听
  window.addEventListener('keydown', handleKeyDown)
  window.addEventListener('keyup', handleKeyUp)
})

onUnmounted(() => {
  // 移除键盘事件监听
  window.removeEventListener('keydown', handleKeyDown)
  window.removeEventListener('keyup', handleKeyUp)
})

defineExpose({ show })
</script>

<style scoped>
.fba-order-container {
  padding: 10px 0;
}

.form-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  padding: 15px;
  background-color: #f8f9fa;
  border-radius: 8px;
}

.form-title {
  margin: 0;
  color: #303133;
  font-size: 18px;
  font-weight: 600;
}

.order-form {
  margin-top: 10px;
}

.section-card {
  margin-bottom: 15px;
  border: 1px solid #ebeef5;
  border-radius: 6px;
}

.section-header {
  padding: 10px 0;
}

.section-title {
  margin: 0;
  color: #303133;
  font-size: 15px;
  font-weight: 600;
}

.sub-section {
  margin-bottom: 20px;
}

.sub-title {
  margin: 0 0 12px 0;
  color: #606266;
  font-size: 13px;
  font-weight: 500;
  padding-left: 6px;
  border-left: 3px solid #409eff;
}

.bold-input :deep(.el-input__inner) {
  font-weight: 600;
  color: #303133;
}

.product-table {
  margin-top: 8px;
  font-size: 12px;
}

.product-table :deep(.el-table__header) {
  background-color: #f5f7fa;
}

.table-summary {

  align-items: center;
  margin-top: 15px;
  padding: 12px;
  background-color: #f8f9fa;
  border-radius: 4px;
}

.summary-item {
  margin-left: 25px;
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

.shipment-numbers {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

.shipment-tag {
  margin-right: 6px;
}

.remarks-textarea {
  font-size: 13px;
}

/* 对话框内表单样式优化 */
:deep(.el-dialog__body) {
  max-height: 70vh;
  overflow-y: auto;
}

/* 响应式调整 */
@media (max-width: 1200px) {
  :deep(.el-dialog) {
    width: 90% !important;
  }
}

@media (max-width: 768px) {
  .form-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 10px;
  }

  .summary-item {
    margin-left: 0;
    margin-right: 15px;
  }

  .table-summary {
    flex-wrap: wrap;
    justify-content: flex-start;
  }
}
/* 货件排序样式 */
.shipment-sort-container {
  margin-top: 10px;
}

.sortable-list {
  min-height: 100px;
}

.sortable-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 8px 12px;
  margin-bottom: 8px;
  background-color: #f5f7fa;
  border-radius: 4px;
  cursor: grab;
  transition: all 0.2s ease;
}

.sortable-item:hover {
  background-color: #ecf5ff;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.sortable-item:active {
  cursor: grabbing;
}

.drag-handle {
  margin-right: 12px;
  color: #909399;
  font-size: 16px;
}

.sortable-item span:not(.drag-handle) {
  flex: 1;
}

.add-item {
  margin-top: 10px;
  display: flex;
  align-items: center;
}

.sortable-item .el-button {
  margin-left: 10px;
}
</style>