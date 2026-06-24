<template  class="main-sty">
  <div class="app-container">
    <el-row :gutter="10" class="mb8">
      <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
        <el-form-item label="会计期间">
          <el-date-picker
              v-model="queryParams.periodRange"
              type="monthrange"
              placeholder="选择月份范围"
              format="YYYY年MM月"
              range-separator="至"
              style="width:280px"
              value-format="YYYYMM"
          ></el-date-picker>
        </el-form-item>
        <el-form-item label="会计科目">
          <el-select
              v-model="queryParams.subjectCodes"
              filterable
              multiple
              clearable
              placeholder="选择顶级科目"
              style="width:360px"
          >
            <el-option
                v-for="item in topSubjects"
                :key="item.subjectCode"
                :label="item.subjectCode + ' ' + item.subjectName"
                :value="item.subjectCode"
            />
          </el-select>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
          <el-button icon="Refresh" @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="Download"
          @click="handleExport"
        >导出</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="RefreshRight"
          @click="handleRebuild"
        >重建总账</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="Check"
          @click="handleVerify"
        >验证公式</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" height="calc(100vh - 160px)" :data="tableData" :span-method="objectSpanMethod" border>
      <el-table-column label="科目编码" prop="subjectCode" width="150" align="center">
        <template #default="scope">
          <el-link type="primary" @click="goToDetail(scope.row)">{{ scope.row.subjectCode }}</el-link>
        </template>
      </el-table-column>
      <el-table-column label="科目名称" prop="subjectName" width="300" show-overflow-tooltip/>
      <el-table-column label="期间" prop="period" width="100" align="center"/>
      <el-table-column label="摘要" prop="summary" width="120" align="center"/>
      <el-table-column label="借方" prop="debitTotal" width="180" align="right">
        <template #default="scope">
          <span>{{ formatMoney(scope.row.debitTotal) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="贷方" prop="creditTotal" width="180" align="right">
        <template #default="scope">
          <span>{{ formatMoney(scope.row.creditTotal) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="方向" prop="direction" width="80" align="center">
        <template #default="scope">
          <span v-if="scope.row.direction === 1">借</span>
          <span v-else-if="scope.row.direction === 2">贷</span>
          <span v-else>平</span>
        </template>
      </el-table-column>
      <el-table-column label="余额" prop="balance" width="180" align="right">
        <template #default="scope">
          <span>{{ formatMoney(scope.row.balance) }}</span>
        </template>
      </el-table-column>
       <el-table-column label="" prop=""  align="right">
        <template #default="scope">
         <div></div>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script setup name="Ledger">
import { topLevelSummaryFull, rebuildLedger, verifyLedger } from "@/api/finance/ledger_general"
import { listSubjects } from "@/api/finance/subjects"
import finStore from "@/hooks/store/useFinanceStore.js";
import {onMounted, inject} from "vue";
import * as XLSX from 'xlsx';
import { useRouter } from 'vue-router';
const { proxy } = getCurrentInstance()
const router = useRouter()
const emitter = inject("emitter");

const tableData = ref([])
const topSubjects = ref([])
const loading = ref(true)
const showSearch = ref(true)

const data = reactive({
  queryParams: {
    periodRange:[],
    subjectCodes: []
  }
})

const { queryParams } = toRefs(data)

// 跳转到明细账
function goToDetail(row) {
  if (row.subjectCode) {
    // 清除明细账缓存
    emitter.emit("removeCache", "明细账");
    router.push({
      path: '/fin/ledger/detail',
      query: {
        title: '明细账',
        path: '/fin/ledger/detail',
        subjectCode: row.subjectCode
      }
    })
  }
}

// 合并单元格配置
const spanArr = ref([])

// 格式化金额
function formatMoney(value) {
  if (!value) return '0.00'
  return Number(value).toFixed(2).replace(/\B(?=(\d{3})+(?!\d))/g, ',')
}

/** 查询总账列表 */
async function getList() {
  loading.value = true
  const groupid = await finStore.getCurrentTenantId();
  const params = { groupid };
  if (queryParams.value.periodRange && queryParams.value.periodRange.length === 2) {
    params.startPeriod = queryParams.value.periodRange[0];
    params.endPeriod = queryParams.value.periodRange[1];
  }
  if (queryParams.value.subjectCodes && queryParams.value.subjectCodes.length > 0) {
    params.subjectCodes = queryParams.value.subjectCodes.join(',');
  }
  topLevelSummaryFull(params).then(response => {
    // 后端已返回完整的总账数据，前端直接展示
    tableData.value = response.rows || []
    
    // 计算合并单元格
    calculateSpan()
    
    loading.value = false
  })
}

// 计算合并单元格
function calculateSpan() {
  const data = tableData.value
  const arr = []
  let pos = 0
  
  for (let i = 0; i < data.length; i++) {
    if (i === 0) {
      arr.push(1)
      pos = 0
    } else {
      // 判断当前单元格与上一个单元格是否属于同一个科目
      if (data[i].subjectCode === data[i - 1].subjectCode) {
        arr[pos] += 1
        arr.push(0)
      } else {
        arr.push(1)
        pos = i
      }
    }
  }
  spanArr.value = arr
}

// 合并单元格方法
function objectSpanMethod({ row, column, rowIndex, columnIndex }) {
  // 科目编码和科目名称列需要合并
  if (columnIndex === 0 || columnIndex === 1) {
    const rowspan = spanArr.value[rowIndex]
    if (rowspan > 0) {
      return {
        rowspan: rowspan,
        colspan: 1
      }
    } else {
      return {
        rowspan: 0,
        colspan: 0
      }
    }
  }
}

/** 搜索按钮操作 */
function handleQuery() {
  getList()
}

/** 重置按钮操作 */
function resetQuery() {
  proxy.resetForm("queryRef")
  handleQuery()
}

/** 重建总账 */
async function handleRebuild() {
  try {
    await proxy.$modal.confirm('确认要重建总账吗？此操作会删除现有总账数据并从凭证重新生成。')
  } catch {
    return
  }
  loading.value = true
  try {
    const groupid = await finStore.getCurrentTenantId()
    const res = await rebuildLedger(groupid)
    proxy.$modal.msgSuccess(res.msg || '重建完成')
    getList()
  } catch (e) {
    proxy.$modal.msgError('重建失败：' + (e.message || e))
  } finally {
    loading.value = false
  }
}

/** 验证总账公式 */
async function handleVerify() {
  loading.value = true
  try {
    const groupid = await finStore.getCurrentTenantId()
    const res = await verifyLedger(groupid)
    if (res.data && res.data.errors && res.data.errors.length > 0) {
      proxy.$modal.msgWarning(res.data.message || '发现不满足公式的记录')
      console.log('验证失败的记录:', res.data.errors)
    } else {
      proxy.$modal.msgSuccess(res.msg || '验证通过')
    }
  } catch (e) {
    proxy.$modal.msgError('验证失败：' + (e.message || e))
  } finally {
    loading.value = false
  }
}

/** 导出按钮操作 */
async function handleExport() {
  if (!tableData.value || tableData.value.length === 0) {
    proxy.$modal.msgWarning("没有可导出的数据")
    return
  }
  
  // 获取公司名称
  const companyName = finStore.currentTenantName?.value || ''
  
  // 获取期间范围
  const startPeriod = queryParams.value.periodRange?.[0] || ''
  const endPeriod = queryParams.value.periodRange?.[1] || ''
  const periodText = startPeriod && endPeriod ? `${startPeriod.substring(0,4)}年第${parseInt(startPeriod.substring(4,6))}期 至 ${endPeriod.substring(0,4)}年第${parseInt(endPeriod.substring(4,6))}期` : ''
  
  // 构建表格数据
  const headers = ['科目编码', '科目名称', '期间', '摘要', '借方', '贷方', '方向', '余额']
  const dataRows = tableData.value.map(row => [
    row.subjectCode,
    row.subjectName,
    row.period,
    row.summary,
    row.rowType === 'begin' ? '' : formatMoney(row.debitTotal || 0),
    row.rowType === 'begin' ? '' : formatMoney(row.creditTotal || 0),
    row.direction === 1 ? '借' : (row.direction === 2 ? '贷' : '平'),
    formatMoney(row.balance || 0)
  ])
  
  // 创建工作表数据
  const wsData = [
    [companyName],  // 第一行：公司名称
    [periodText],   // 第二行：期间范围
    [],             // 第三行：空行
    headers,        // 第四行：表头
    ...dataRows     // 数据行
  ]
  
  // 创建工作表
  const ws = XLSX.utils.aoa_to_sheet(wsData)
  
  // 设置合并单元格
  ws['!merges'] = [
    { s: { r: 0, c: 0 }, e: { r: 0, c: 7 } },  // 第一行合并A1:H1（公司名称）
    { s: { r: 1, c: 0 }, e: { r: 1, c: 7 } }   // 第二行合并A2:H2（期间范围）
  ]
  
  // 设置列宽
  ws['!cols'] = [
    { wch: 12 },  // 科目编码
    { wch: 20 },  // 科目名称
    { wch: 10 },  // 期间
    { wch: 12 },  // 摘要
    { wch: 15 },  // 借方
    { wch: 15 },  // 贷方
    { wch: 8 },   // 方向
    { wch: 15 }   // 余额
  ]
  
  // 创建工作簿
  const wb = XLSX.utils.book_new()
  XLSX.utils.book_append_sheet(wb, ws, '总账')
  
  // 导出文件
  XLSX.writeFile(wb, `总账_${new Date().getTime()}.xlsx`)
}

// 获取当前期间
async function initCurrentPeriod() {
  const now = new Date()
  const currentYear = now.getFullYear()
  const currentMonth = String(now.getMonth() + 1).padStart(2, '0')
  const period = await finStore.getCurrentPeriod()
  const currentPeriod = `${currentYear}${currentMonth}`

  if (period && period.periodCode) {
    const periodYear = parseInt(period.periodCode.substring(0, 4))
    if (periodYear <= currentYear + 1) {
      queryParams.value.periodRange = [period.periodCode, currentPeriod]
    } else {
      queryParams.value.periodRange = [currentPeriod, currentPeriod]
    }
  } else {
    queryParams.value.periodRange = [currentPeriod, currentPeriod]
  }
}

// 获取顶级科目列表
async function loadTopSubjects() {
  const groupid = await finStore.getCurrentTenantId();
  console.log('获取顶级科目, groupid:', groupid)
  listSubjects({ groupid: groupid, subjectLevel: 1 }).then(response => {
    console.log('顶级科目返回数据:', response)
    topSubjects.value = response.rows || []
  })
}

onMounted(async () => {
  await initCurrentPeriod();
  await loadTopSubjects();
  getList()
})
</script>

<style scoped>
.el-table {
  margin-top: 10px;
}

:deep(.el-table td) {
  vertical-align: middle !important;
}

:deep(.el-table th) {
  border-bottom-color: #666 !important;
}

:deep(.el-table td) {
  border-bottom-color: #bbb;
}

:deep(.el-table--border::after),
:deep(.el-table--group::after),
:deep(.el-table::before) {
  background-color: #999;
}

:deep(.el-table--border th),
:deep(.el-table--border td) {
  border-right-color: #e5e5e5;
}
</style>
