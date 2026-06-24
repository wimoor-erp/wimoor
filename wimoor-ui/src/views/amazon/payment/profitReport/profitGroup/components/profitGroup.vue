<template>
  <div  >
    <div class="con-header">
      <!-- 添加标签页组件 -->

          <!-- 原profitGroup页面内容 -->
          <el-row>
            <Group @change="getGroup" ref="groupRef" />
            <el-space>
              <div class="date-picker-group">
                <el-select v-model="queryParams.datetype" style="width:100px" placeholder="结算日期" @change="handleQuery">
                  <el-option v-for="item in dateOptions" :label="item.label" :value="item.value"></el-option>
                </el-select>
                <Datepicker ref="datepickers" :shortIndex="1" @changedate="changedate" />
              </div>
              <el-select v-model="queryParams.currency" @change="handleQuery">
                <el-option label="人民币" value="CNY"></el-option>
                <el-option label="美元" value="USD"></el-option>
                <el-option label="站点币种" value="market"></el-option>
              </el-select>
            </el-space>

            <div class='rt-btn-group'>
              <el-button @click="downloadList">导出</el-button>
            </div>
          </el-row>
          <el-table :data="tableData"
                    :row-class-name="rowclassname"
                    v-loading="loading" >
            <el-table-column prop="name" label="店铺"   >
              <template #default="scope">
                <el-space>
                  <el-icon v-show="scope.row.pid=='0'" class="ic-cen font-small"><ArrowDownBold /></el-icon>
                  {{scope.row.name}}
                </el-space>
              </template>
            </el-table-column >
            <el-table-column prop="tocurrency" label="币种"  />
            <el-table-column prop="principal" label="销售额" align="right">
            </el-table-column>
            <el-table-column prop="commission" label="销售佣金" align="right">
            </el-table-column>
            <el-table-column prop="fbafee" label="FBA费用" align="right">
            </el-table-column>
            <el-table-column prop="refund" label="退款金额" align="right">
            </el-table-column>
            <el-table-column prop="storagefee" label="仓储费" align="right">
            </el-table-column>
            <el-table-column prop="advfee" label="广告费" align="right">
            </el-table-column>
            <el-table-column prop="other" label="其它" align="right">
            </el-table-column>
            <el-table-column prop="setin" label="结算收入" align="right">
            </el-table-column>
            <el-table-column prop="price" label="采购成本" align="right">
            </el-table-column>
            <el-table-column prop="profit" label="利润" align="right">
            </el-table-column>
          </el-table>


    </div>
  </div>

  <!-- 弹窗组件 -->
  <el-popover
      ref="popoverRef"
      placement="top"
      :virtual-ref="editPriceRef"
      trigger="click"
      virtual-triggering
      width="200px"
  >
    <el-input v-model="skuprice"></el-input>
  </el-popover>
  <OtherFeeDialog ref="otherFeeDiaRef"></OtherFeeDialog>
  <AccCalcDialog ref="calcDialogRef"></AccCalcDialog>
  <RefreshDataDialog ref="refreshDataDialogRef"></RefreshDataDialog>
</template>

<script setup>
import { ref, reactive, onMounted, toRefs } from 'vue'
import { Search, ArrowDownBold, Download, Upload, InfoFilled, HomeFilled, Switch, CaretTop, CaretBottom, Edit } from '@element-plus/icons-vue'
import { Plus, Formula, Help, Copy, MoreOne } from '@icon-park/vue-next'
import { formatFloat, formatPercent, formatInteger, decodeText, getCurrencyMark, outputmoney } from '@/utils/index.js'
import Datepicker from '@/components/header/datepicker.vue'
import Group from '@/views/amazon/listing/common/group.vue';
import GroupSelect from '@/components/header/group.vue';
import Currency from '@/components/header/currency.vue'
import OtherFeeDialog from '@/views/amazon/payment/finance/components/other_fee_dialog.vue'
// 在import部分添加MonthReport组件的导入
import QuarterPicker from '@/components/header/quarter_picker.vue'
import AccCalcDialog from '@/views/amazon/payment/profitReport/components/accCalc_dialog.vue'
import RefreshDataDialog from '@/views/amazon/payment/common/refresh_data_dialog.vue'
import Month from '@/views/amazon/payment/finance/components/month.vue'
import MonthReport from '@/views/amazon/payment/monthReport/index.vue'  // 添加这行

// 导入API
import settlementAccRptApi from '@/api/amazon/finances/settlementAccRptApi.js'
import financesApi from '@/api/amazon/finances/monthFinance.js'

// 定义响应式数据
const activeTab = ref('profitGroup')
const router = useRouter()

// profitGroup相关数据
const groupRef = ref()
const datepickers = ref()
const profitGroupState = reactive({
  dateOptions: [{ label: '结算日期', value: '' }, { label: '转账日期', value: 'acc' }],
  queryParams: { datetype: '', currency: 'CNY' },
  tableData: [],
  loading: false,
  isload: true,
})

// SKU财务数据相关数据
const skuGroupRef = ref()
const skuGlobalTable = ref()
// 在skuState定义之后，添加monthState的定义
const skuState = reactive({
  queryParams: {
    currency: 'MARKET',
    charttype: 'sku',
    fatype: 'sku',
    monthDate: new Date(new Date().getFullYear(), new Date().getMonth() - 1, 1),
    search: ''  // 添加search属性
  },
  tableData: {
    records: [],
    total: 1,
  }
})

// 添加monthState定义
const quarterPickerRef = ref()
const monthDatepickers = ref()
const monthRef = ref()
const monthState = reactive({
  queryParams: {
    currency: 'MARKET',
    charttype: 'sku',
    fatype: 'sku',
    quarterValue: new Date()
  },
  quarterData: {}
})

// 在修复解构赋值部分添加profitGroupState的解构
const { dateOptions, queryParams, tableData, loading, isload } = toRefs(profitGroupState)
const { queryParams: skuQueryParams, tableData: skuTableData } = toRefs(skuState)
const { queryParams: monthQueryParams, quarterData } = toRefs(monthState)

// 标签页切换处理
function handleTabChange(tab) {
  // 根据切换的标签页执行相应的初始化操作
  if (tab === 'monthReport') {
    getQuarter()
    getMonthData()
  }
  // 可以根据需要为月度结算页面添加初始化逻辑
}

// profitGroup页面方法
function changedate(dates) {
  profitGroupState.queryParams.fromDate = dates.start
  profitGroupState.queryParams.endDate = dates.end
  if (profitGroupState.isload === false) {
    handleQuery()
  }
}

function getGroup(obj) {
  profitGroupState.queryParams.groupid = obj.groupid
  profitGroupState.queryParams.marketplaceid = obj.marketplaceid
  handleQuery()
  profitGroupState.isload = false
}

function handleQuery() {
  // 查询表格数据
  profitGroupState.loading = true
  settlementAccRptApi.getSettlementOverAll(profitGroupState.queryParams).then((res) => {
    profitGroupState.loading = false
    if (res.data) {
      res.data.forEach(item => {
        item.principal = item.principal ? outputmoney(item.principal) : item.principal
        item.commission = item.commission ? outputmoney(item.commission) : item.commission
        item.fbafee = item.fbafee ? outputmoney(item.fbafee) : item.fbafee
        item.refund = item.refund ? outputmoney(item.refund) : item.refund
        item.storagefee = item.storagefee ? outputmoney(item.storagefee) : item.storagefee
        item.advfee = item.advfee ? outputmoney(item.advfee) : item.advfee
        item.other = item.other ? outputmoney(item.other) : item.other
        item.setin = item.setin ? outputmoney(item.setin) : item.setin
        item.price = item.price ? outputmoney(item.price) : item.price
        item.profit = item.profit ? outputmoney(item.profit) : item.profit
      })
    }
    profitGroupState.tableData = res.data
  }).catch(e => {
    profitGroupState.loading = false
  })
}

function downloadList() {
  settlementAccRptApi.downloadSettlementOverAll(profitGroupState.queryParams)
}

function rowclassname({ row }) {
  if (row.pid === '0') {
    return 'sumrowclass'
  }
}

function changeGroupSelect(obj){
  monthState.queryParams.groupid=obj.groupid;
  monthState.queryParams.marketplaceid=obj.marketplaceid;
  getMonthData();
}

function changeCurrency(value){
  monthState.queryParams.currency=value;
  getMonthData();
}


// 月度报表方法
function getQuarter() {
  var myparams = monthState.queryParams
  myparams.fromDate = monthState.queryParams.quarterValue
  financesApi.getQuarter(myparams).then((res) => {
    monthState.quarterData = res.data
    var rate = 0
    if (monthState.quarterData.oldamount) {
      rate = (parseFloat(monthState.quarterData.amount) - parseFloat(monthState.quarterData.oldamount)) / parseFloat(monthState.quarterData.oldamount) * 100
    }
    monthState.quarterData.rate = formatFloat(rate)
  })
}

function getMonthData() {
  var params = monthState.queryParams
  params.fromDate = monthState.queryParams.monthfromDate
  params.endDate = monthState.queryParams.monthendDate
  params.currency = monthState.queryParams.currency
  financesApi.getMonth(params).then((res) => {
    monthRef.value.show(res.data)
  })
  getQuarter();
}

function monthChangedaterange(dates) {
  monthState.queryParams.monthfromDate = dates.start
  monthState.queryParams.monthendDate = dates.end
  getMonthData()
}

function monthDownloadData() {
  var params = monthState.queryParams
  params.fromDate = monthState.queryParams.monthfromDate
  params.endDate = monthState.queryParams.monthendDate
  financesApi.downloadMonth(params)
}

function disabledQuarter(date) {
  // 可以根据需要添加禁用日期的逻辑
  return false
}


function exchangeRateSet() {
  router.push({
    path: '/finance/exchangeRate',
    query: {
      title: '汇率管理',
      path: '/finance/exchangeRate',
    }
  })
}

function cellClassName(data) {
  if (data.column.property === 'setincome') {
    return 'cell-setincome'
  }
}

function recalculation() {
  refreshDataDialogRef.value.show()
}

// 初始化
onMounted(() => {
  // 页面加载时执行的初始化操作
})
</script>

<style>
.cell-setincome {
  background-color: #dedede;
}

.cell-setincome:hover {
  background-color: #dedede;
}

.sumrowclass {
  background: #f1f1f1 !important;
  font-weight: 700;
}

.dark .sumrowclass {
  background: #080808 !important;
  font-weight: 700;
}

.flex-top-between {
  display: flex;
  justify-content: space-between;
}

.flex-center-between {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>