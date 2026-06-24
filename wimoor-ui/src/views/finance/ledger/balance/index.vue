<template  class="main-sty">
  <div class="app-container">
    <!-- 搜索区域 -->
    <el-row :gutter="10" class="mb8">
      <el-form :model="searchForm" ref="searchRef" :inline="true" v-show="showSearch" label-width="98px">
        <el-form-item label="会计期间范围">
          <el-date-picker
            v-model="searchForm.periodRange"
            type="monthrange"
            placeholder="选择月份范围"
            format="YYYY年MM月"
            range-separator="至"
            style="width:280px"
            value-format="YYYYMM"
          ></el-date-picker>
        </el-form-item>
        <el-form-item label="科目编码">
          <el-input
            v-model="searchForm.subjectCode"
            placeholder="请输入科目编码"
            clearable
            @keyup.enter="handleSearch"
          ></el-input>
        </el-form-item>
        <el-form-item label="科目名称">
          <el-input
            v-model="searchForm.subjectName"
            placeholder="请输入科目名称"
            clearable
            @keyup.enter="handleSearch"
          ></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="Search" @click="handleSearch">查询</el-button>
          <el-button icon="Refresh" @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList">
        <el-button type="primary" plain icon="Download" @click="handleExport">导出</el-button>
      </right-toolbar>
    </el-row>



    <!-- 汇总信息 -->
    <div v-if="summary" class="summary-card">
      <div class="summary-item">
        <el-descriptions title="期初余额" :column="2"   direction="vertical" border size="small">
          <el-descriptions-item label="借方" align="center">
            <span class="summary-value">{{ formatNumber(summary.totalBeginDebit) }}</span>
          </el-descriptions-item>
          <el-descriptions-item label="贷方" align="center">
            <span class="summary-value">{{ formatNumber(summary.totalBeginCredit) }}</span>
          </el-descriptions-item>
        </el-descriptions>
      </div>
      <div class="summary-item">
        <el-descriptions title="本期发生额" :column="2"   direction="vertical" border size="small">
          <el-descriptions-item label="借方" align="center">
            <span class="summary-value">{{ formatNumber(summary.totalDebitAmount) }}</span>
          </el-descriptions-item>
          <el-descriptions-item label="贷方" align="center">
            <span class="summary-value">{{ formatNumber(summary.totalCreditAmount) }}</span>
          </el-descriptions-item>
        </el-descriptions>
      </div>
      <div class="summary-item">
        <el-descriptions title="期末余额" :column="2"   direction="vertical" border size="small">
          <el-descriptions-item label="借方" align="center">
            <span class="summary-value">{{ formatNumber(summary.totalEndDebit) }}</span>
          </el-descriptions-item>
          <el-descriptions-item label="贷方" align="center">
            <span class="summary-value">{{ formatNumber(summary.totalEndCredit) }}</span>
          </el-descriptions-item>
        </el-descriptions>
      </div>
    </div>

    <!-- 表格区域 -->
    <div class="table-container">
      <!-- 树形表格 -->
      <el-table
        v-loading="loading"
        :data="treeData"
        style="width: 100%"
        row-key="subjectCode"
        v-if="refreshTable"
        :tree-props="{children: 'children', hasChildren: 'hasChildren'}"
        :default-expand-all="isExpandAll"
      >
        <el-table-column label="科目编码" prop="subjectCode" width="156" align="left">
          <template #header>
            <space>科目编码        <el-button
              type="info"
              link
              size="small"
              icon="Sort"
              @click="toggleExpandAll"
          >展开/折叠</el-button></space>
          </template>
          <template #default="{ row }">
            <span v-if="row.subjectLevel == 1" style="font-weight: bold;">{{ row.subjectCode }}</span>
            <span v-else>{{ row.subjectCode }}</span>
          </template>
        </el-table-column>
        <el-table-column label="科目名称" prop="subjectName" show-overflow-tooltip  align="left">
          <template #header>
            <el-space>科目名称  <el-checkbox @change="handleSearch" v-model="searchForm.isFilterZero"><span class="font-extraSmall">不显示为0的科目</span></el-checkbox>      </el-space>
          </template>
          <template #default="{ row }">
            <span v-if="row.subjectLevel == 1" style="font-weight: bold;">{{ row.subjectName }}</span>
            <span v-else>{{ row.subjectName }}</span>
          </template>
        </el-table-column>
        <el-table-column label="科目级别" align="center" prop="subjectLevel" width="100">
          <template #default="scope">
            <dict-tag :options="fin_subject_level" :value="scope.row.subjectLevel"/>
          </template>
        </el-table-column>
        <el-table-column label="科目类别" align="center" prop="subjectTypeName" width="140">
          <template #default="scope">
            <el-tag type="success">{{ scope.row.subjectTypeName }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="期初余额" align="center">
          <el-table-column label="借方" width="130" align="right">
            <template #default="{ row }">
              <span class="debit">{{ formatNumber(row.beginDebit) }}</span>
            </template>
          </el-table-column>
          <el-table-column label="贷方" width="130" align="right">
            <template #default="{ row }">
              <span class="credit">{{ formatNumber(row.beginCredit) }}</span>
            </template>
          </el-table-column>
        </el-table-column>
        <el-table-column label="本期发生额" align="center">
          <el-table-column label="借方" width="130" align="right">
            <template #default="{ row }">
              <span class="debit">{{ formatNumber(row.debitAmount) }}</span>
            </template>
          </el-table-column>
          <el-table-column label="贷方" width="130" align="right">
            <template #default="{ row }">
              <span class="credit">{{ formatNumber(row.creditAmount) }}</span>
            </template>
          </el-table-column>
        </el-table-column>
        <el-table-column label="本年累计发生额" align="center">
          <el-table-column label="借方" width="130" align="right">
            <template #default="{ row }">
              <span class="debit">{{ formatNumber(row.yearDebitAmount) }}</span>
            </template>
          </el-table-column>
          <el-table-column label="贷方" width="130" align="right">
            <template #default="{ row }">
              <span class="credit">{{ formatNumber(row.yearCreditAmount) }}</span>
            </template>
          </el-table-column>
        </el-table-column>
        <el-table-column label="期末余额" align="center">
          <el-table-column label="借方" width="130" align="right">
            <template #default="{ row }">
              <span class="debit">{{ formatNumber(row.endDebit) }}</span>
            </template>
          </el-table-column>
          <el-table-column label="贷方" width="130" align="right">
            <template #default="{ row }">
              <span class="credit">{{ formatNumber(row.endCredit) }}</span>
            </template>
          </el-table-column>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script setup name="SubjectBalance">
import { ref, reactive, onMounted, nextTick } from 'vue'
import { getCurrentInstance } from '@vue/runtime-core'
import {
  querySubjectBalanceTree,
  getBalanceSummary,
  exportSubjectBalance
} from '@/api/finance/subject_balance.js'

const { proxy } = getCurrentInstance()
import finStore from '@/hooks/store/useFinanceStore.js'

const { fin_subject_level, fin_direction } = proxy.useDict('fin_subject_level', 'fin_direction')

const loading = ref(false)
const treeData = ref([])
const summary = ref(null)
const showSearch = ref(true)
const isExpandAll = ref(false)
const searchRef = ref()
const refreshTable=ref(true);
const searchForm = reactive({
  periodRange: [],
  isFilterZero:true,
  subjectCode: '',
  subjectName: ''
})
/** 展开/折叠操作 */
function toggleExpandAll() {
  refreshTable.value = false
  isExpandAll.value = !isExpandAll.value
  nextTick(() => {
    refreshTable.value = true;
  })
}

// 获取当前期间
async function getCurrentPeriod() {
  const now = new Date()
  const currentYear = now.getFullYear()
  const currentMonth = String(now.getMonth() + 1).padStart(2, '0')
  const period = await finStore.getCurrentPeriod()
  const currentPeriod = `${currentYear}${currentMonth}`
  
  if (period && period.periodCode) {
    const periodYear = parseInt(period.periodCode.substring(0, 4))
    if (periodYear <= currentYear + 1) {
      searchForm.periodRange = [period.periodCode, currentPeriod]
    } else {
      searchForm.periodRange = [currentPeriod, currentPeriod]
    }
  } else {
    searchForm.periodRange = [currentPeriod, currentPeriod]
  }
}

// 查询数据
async function handleSearch() {
  loading.value = true
  try {
    const groupid = await finStore.getCurrentTenantId()
    const beginPeriod = searchForm.periodRange[0] || null
    const endPeriod = searchForm.periodRange[1] || null
    
    const params = {
      groupid: groupid,
      beginPeriod: beginPeriod,
      endPeriod: endPeriod,
      isFilterZero:searchForm.isFilterZero,
      subjectCode: searchForm.subjectCode || null,
      subjectName: searchForm.subjectName || null
    }

    const response = await querySubjectBalanceTree(params)
    treeData.value = response.data

    const summaryResponse = await getBalanceSummary({ groupid, period: beginPeriod || endPeriod })
    summary.value = summaryResponse.data
  } catch (error) {
    console.error('查询科目余额失败:', error)
    proxy.$message.error('查询失败')
  } finally {
    loading.value = false
  }
}

// 重置查询条件
function handleReset() {
  proxy.resetForm("searchRef")
  getCurrentPeriod()
}

// 刷新数据
function handleRefresh() {
  handleSearch()
}


// 导出数据
async function handleExport() {
  loading.value = true
  try {
    const groupid = await finStore.getCurrentTenantId()
    const beginPeriod = searchForm.periodRange[0] || null
    const endPeriod = searchForm.periodRange[1] || null
    
    const params = {
      groupid: groupid,
      beginPeriod: beginPeriod,
      endPeriod: endPeriod,
      subjectCode: searchForm.subjectCode || null,
      subjectName: searchForm.subjectName || null
    }

    const response = await exportSubjectBalance(params)
    const data = response.data

    const headers = ['科目编码', '科目名称', '科目级别', '科目类别', '余额方向', '期初余额', '本期借方', '本期贷方', '期末余额']
    const rows = data.map(item => [
      item.subjectCode,
      item.subjectName,
      item.subjectLevel,
      item.subjectTypeName,
      item.direction === 1 ? '借方' : '贷方',
      item.beginBalance || 0,
      item.debitAmount || 0,
      item.creditAmount || 0,
      item.endBalance || 0
    ])

    const csvContent = [headers.join(','), ...rows.map(row => row.join(','))].join('\n')
    const blob = new Blob(['\uFEFF' + csvContent], { type: 'text/csv;charset=utf-8;' })
    const link = document.createElement('a')
    link.href = URL.createObjectURL(blob)
    const filename = beginPeriod === endPeriod 
      ? `科目余额表_${beginPeriod}.csv` 
      : `科目余额表_${beginPeriod}_${endPeriod}.csv`
    link.download = filename
    link.click()

    proxy.$message.success('导出成功')
  } catch (error) {
    console.error('导出科目余额失败:', error)
    proxy.$message.error('导出失败')
  } finally {
    loading.value = false
  }
}

// 格式化数字
function formatNumber(num) {
  if (!num || num === 0) {
    return '0.00'
  }
  return Number(num).toLocaleString('zh-CN', {
    minimumFractionDigits: 2,
    maximumFractionDigits: 2
  })
}

// 列表查询（供 right-toolbar 使用）
function getList() {
  handleSearch()
}

onMounted(async () => {
  await getCurrentPeriod()
  handleSearch()
})
</script>
<style scoped>
.summary-card .el-descriptions--small .el-descriptions__header{
 
}
</style>
<style scoped>
.app-container {
  padding: 16px;
}

.summary-card {
  display: flex;
  gap: 16px;
  margin-bottom: 16px;
  flex-wrap: wrap;
}

.summary-item {
  flex: 1;
  min-width: 280px;
  border-radius: 8px;
}

.summary-item :deep(.el-descriptions__header) {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 5px 16px;
   margin-bottom: 0 !important;
  border-radius: 8px 8px 0 0;
}

.summary-item :deep(.el-descriptions__title) {
  color: #fff;
  font-size: 15px;
  font-weight: 600;
}

.summary-item :deep(.el-descriptions__label) {
  background-color: #f5f7fa;
  color: #606266;
  font-weight: 500;
}

.summary-value {
  font-size: 16px;
  font-weight: 600;
  color: #303133;
  font-family: 'DIN Alternate', 'Barlow', monospace;
}

.table-container {
  background: #fff;
  border-radius: 8px;
  overflow: hidden;
}

/deep/ .el-table {
  --el-table-header-text-color: #606266;
  --el-table-row-hover-bg-color: #f5f7fa;
}

/deep/ .el-table__header th {
  background-color: #fafafa;
}

/deep/ .el-table--border::after,
/deep/ .el-table--group::after,
/deep/ .el-table td,
/deep/ .el-table th {
  border-color: #ebeef5;
}

/deep/ .el-table--border::after {
  display: none;
}

/deep/ .el-date-editor--month {
  width: 120px;
}
</style>