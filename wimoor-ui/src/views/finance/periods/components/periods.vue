<template>
  <div class="periods-container">

    <!-- 年份显示和选择器 -->
    <div class="year-section">
      <div class="year-display">
        {{ year }}年
      </div>
      <div class="year-picker" v-if="showYearPicker">
        <div class="year-picker-header">
          <span class="year-range">{{ startYear }} - {{ endYear }}</span>
          <el-button size="small" @click="prevYears">«</el-button>
          <el-button size="small" @click="nextYears">»</el-button>
        </div>
        <div class="year-grid">
          <div 
            v-for="y in displayYears" 
            :key="y"
            class="year-item"
            :class="{ 'active': y === year }"
            @click="selectYear(y)"
          >
            {{ y }}
          </div>
        </div>
      </div>
      <el-button size="small" @click="showYearPicker = !showYearPicker">选择年份</el-button>
    </div>

    <!-- 月份卡片网格 -->
    <div class="periods-grid">
      <div v-if="periodsList.length === 0" class="no-data">
        暂无数据
      </div>
      <div 
        v-for="item in periodsList" 
        :key="item.periodId" 
        class="period-card"
        :class="{ 
          'closed': item.periodStatus === 3, 
          'current': item.isCurrent === 1,
          'opened': item.periodStatus === 2 && item.isCurrent !== 1,
          'not-open': item.periodStatus === 1
        }"
        @click="selectPeriod(item.periodId)"
      >
        <div class="period-number">{{ item.periodName }}</div>
        <div v-if="item.isCurrent === 1" class="current-label">当前</div>
        <div v-if="item.periodStatus === 3" class="closed-label">已结账 ✓</div>
        <div v-if="item.periodStatus === 2 && item.isCurrent !== 1" class="opened-label">已开启</div>
        <div v-if="item.periodStatus === 1" class="not-open-label">未开启</div>
        <div v-if="selectedPeriods.includes(item.periodId)" class="checkmark">✓</div>
      </div>
    </div>
    <!-- 底部反结账按钮和警告 -->
    <div class="bottom-section">
      <div class="btn-group">
        <el-button type="warning" class="fix-btn" @click="handleFixPeriod">会计期间修正</el-button>
        <el-button type="primary" class="uncheck-btn" @click="handleUnclosing" :disabled="selectedPeriods.length === 0">反结账</el-button>
      </div>
      <div class="warning-text">
        反结账是违反会计法规的非正常操作，会影响历史报表数据，请慎用！
      </div>
      <div class="info-text" v-if="earliestSelectedPeriod">
        账套将反结账到{{ earliestSelectedPeriod.periodName }}
      </div>
      <div class="info-text" v-else>
        请选择要反结账的期间
      </div>
    </div>
  </div>
</template>

<script setup name="Periods">
import { ref, computed, reactive, toRefs, getCurrentInstance, onMounted } from "vue"
import { ElMessage, ElMessageBox, ElLoading } from 'element-plus'
import { listPeriods, getPeriods, delPeriods, addPeriods, updatePeriods, unclosing, fixCurrentPeriod } from "@/api/finance/periods.js"
import finStore from "@/hooks/store/useFinanceStore.js";
const { proxy } = getCurrentInstance()

const periodsList = ref([])
const open = ref(false)
const loading = ref(true)
const showSearch = ref(true)
const ids = ref([])
const single = ref(true)
const multiple = ref(true)
const total = ref(0)
const title = ref("")
const year = ref(new Date().getFullYear())
const selectedPeriods = ref([])
const showYearPicker = ref(false)
const startYear = ref(2011)
const endYear = ref(2025)

// 计算显示的年份列表
const displayYears = computed(() => {
  const years = []
  for (let y = startYear.value; y <= endYear.value; y++) {
    years.push(y)
  }
  return years
})

// 计算最早的选中期间
const earliestSelectedPeriod = computed(() => {
  if (selectedPeriods.value.length === 0) return null
  
  // 找出所有选中的期间
  const selectedItems = periodsList.value.filter(item => 
    item && selectedPeriods.value.includes(item.periodId)
  )
  
  // 按期间编码排序，找出最早的
  selectedItems.sort((a, b) => {
    return a.periodCode.localeCompare(b.periodCode)
  })
  
  return selectedItems[0]
})

// 切换到上一年份范围
function prevYears() {
  startYear.value -= 15
  endYear.value -= 15
}

// 切换到下一年份范围
function nextYears() {
  startYear.value += 15
  endYear.value += 15
}

/** 选择年份 */
function selectYear(selectedYear) {
  year.value = selectedYear
  showYearPicker.value = false
  // 重新加载期间数据
  getList()
}

// 组件挂载时调用getList
onMounted(() => {
  getList()
})
const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 1000,
    periodCode: null,
    periodName: null,
    startDate: null,
    groupid:"#",
    endDate: null,
    periodStatus: null,
    isCurrent: null,
    createdTime: null
  },
  rules: {
    tenantId: [
      { required: true, message: "租户ID不能为空", trigger: "blur" }
    ],
    periodCode: [
      { required: true, message: "期间编码，格式：YYYYMM不能为空", trigger: "blur" }
    ],
    periodName: [
      { required: true, message: "期间名称，如：2024年1月不能为空", trigger: "blur" }
    ],
    startDate: [
      { required: true, message: "期间开始日期不能为空", trigger: "blur" }
    ],
    endDate: [
      { required: true, message: "期间结束日期不能为空", trigger: "blur" }
    ],
  }
})

const { queryParams, form, rules } = toRefs(data)

/** 查询会计期间管理列表 */
async function getList() {
  loading.value = true
  queryParams.value.groupid = await finStore.getCurrentTenantId();
   // 根据当前选择的年份过滤数据
  queryParams.value.periodName = year.value.toString();
  listPeriods(queryParams.value).then(response => {
    periodsList.value = response.rows
    total.value = response.total
    loading.value = false
    // 清空选中状态
    selectedPeriods.value = []
    // 调试输出
    console.log('periodsList:', periodsList.value)
    console.log('year:', year.value)
    console.log('periodsList length:', periodsList.value.length)
  })
}

// 取消按钮
function cancel() {
  open.value = false
  reset()
}


// 表单重置
function reset() {
  form.value = {
    periodId: null,
    tenantId: null,
    periodCode: null,
    periodName: null,
    startDate: null,
    endDate: null,
    periodStatus: null,
    isCurrent: null,
    createdTime: null
  }
  proxy.resetForm("periodsRef")
}

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1
  getList()
}

/** 重置按钮操作 */
function resetQuery() {
  proxy.resetForm("queryRef")
  handleQuery()
}

// 多选框选中数据
function handleSelectionChange(selection) {
  ids.value = selection.map(item => item.periodId)
  single.value = selection.length != 1
  multiple.value = !selection.length
}

/** 新增按钮操作 */
function handleAdd() {
  reset()
  open.value = true
  title.value = "添加会计期间管理"
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset()
  const _periodId = row.periodId || ids.value
  getPeriods(_periodId).then(response => {
    form.value = response.data
    open.value = true
    title.value = "修改会计期间管理"
  })
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["periodsRef"].validate(valid => {
    if (valid) {
      if (form.value.periodId != null) {
        updatePeriods(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功")
          open.value = false
          getList()
        })
      } else {
        addPeriods(form.value).then(response => {
          proxy.$modal.msgSuccess("新增成功")
          open.value = false
          getList()
        })
      }
    }
  })
}

/** 删除按钮操作 */
function handleDelete(row) {
  const _periodIds = row.periodId || ids.value
  proxy.$modal.confirm('是否确认删除会计期间管理编号为"' + _periodIds + '"的数据项？').then(function() {
    return delPeriods(_periodIds)
  }).then(() => {
    getList()
    proxy.$modal.msgSuccess("删除成功")
  }).catch(() => {})
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download('finance/periods/export', {
    ...queryParams.value
  }, `periods_${new Date().getTime()}.xlsx`)
}

/** 选择期间 - 只能选择最近的已结账期间 */
function selectPeriod(periodId) {
  const period = periodsList.value.find(item => item && item.periodId === periodId)
  if (!period || period.periodStatus !== 3) return // 3=已关闭（已结账）
  
  // 找出所有已结账的期间并按时间倒序排列
  const closedPeriods = periodsList.value
    .filter(item => item && item.periodStatus === 3) // 3=已关闭（已结账）
    .sort((a, b) => b.periodCode.localeCompare(a.periodCode))
  
  // 找出最近的已结账期间（第一个）
  const latestClosedPeriod = closedPeriods[0]
  
  // 只允许选择最近的已结账期间
  if (latestClosedPeriod && periodId === latestClosedPeriod.periodId) {
    const index = selectedPeriods.value.indexOf(periodId)
    if (index > -1) {
      selectedPeriods.value.splice(index, 1)
    } else {
      selectedPeriods.value = [periodId] // 只允许选一个
    }
  } else {
    ElMessage.warning('请先反结账最近的已结账期间：' + (latestClosedPeriod ? latestClosedPeriod.periodName : ''))
  }
}

/** 执行会计期间修正 */
async function handleFixPeriod() {
  try {
    await ElMessageBox.confirm(
      '此操作将根据凭证情况自动修正会计期间状态，确定继续吗？',
      '会计期间修正确认',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    
    const tenantId = await finStore.getCurrentTenantId()
    ElMessage({ message: '正在修正会计期间，请稍候...', type: 'info' })
    
    const response = await fixCurrentPeriod(tenantId)
    
    if (response && response.code === 200) {
      ElMessage({ message: '会计期间修正成功！', type: 'success' })
      getList()
    } else {
      ElMessage({ message: '修正失败：' + (response.msg || '未知错误'), type: 'error' })
    }
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage({ message: '请求失败：' + error.message, type: 'error' })
    }
  }
}

/** 执行反结账 */
async function handleUnclosing() {
  if (selectedPeriods.value.length === 0) {
    ElMessage.warning('请选择要反结账的期间')
    return
  }
  
  const periodId = selectedPeriods.value[0]
  const period = periodsList.value.find(item => item && item.periodId === periodId)
  
  if (!period) {
    ElMessage.error('期间信息不存在')
    return
  }
  
  try {
    // 弹出确认框，要求输入反结账原因
    const { value: reason } = await ElMessageBox.prompt(
      `确定要反结账期间 ${period.periodName} 吗？请输入反结账原因：`, 
      '反结账确认', 
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        inputType: 'textarea',
        inputValidator: (value) => {
          if (!value || !value.trim()) {
            return '请输入反结账原因'
          }
          return true;
        }
      }
    )
    
    const tenantId = await finStore.getCurrentTenantId()
    
    const loadingInstance = ElLoading.service({
      lock: true,
      text: '正在执行反结账，请稍候...',
      background: 'rgba(0, 0, 0, 0.7)'
    })
    
    try {
      const response = await unclosing(tenantId, period.periodCode, reason.trim())
      
      if (response && response.code === 200) {
        ElMessage({ message: '反结账成功！', type: 'success' })
        selectedPeriods.value = []
        getList()
      } else {
        ElMessage({ message: '反结账失败：' + (response.msg || '未知错误'), type: 'error' })
      }
    } finally {
      loadingInstance.close()
    }
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage({ message: '请求失败：' + error.message, type: 'error' })
    }
  }
}

getList()
</script>
<style>
.periods-container {
  padding: 20px;
}

.top-tabs {
  margin-bottom: 30px;
}

.top-tabs .el-button {
  margin-right: 10px;
}

.year-section {
  text-align: center;
  margin-bottom: 30px;
  position: relative;
}

.year-display {
  font-size: 18px;
  font-weight: bold;
  margin-bottom: 10px;
}

.year-picker {
  position: absolute;
  top: 100%;
  left: 50%;
  transform: translateX(-50%);
  background-color: #fff;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  padding: 10px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  z-index: 1000;
  margin-top: 10px;
  min-width: 300px;
}

.year-picker-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
  padding-bottom: 10px;
  border-bottom: 1px solid #dcdfe6;
}

.year-range {
  font-weight: bold;
}

.year-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 10px;
}

.year-item {
  padding: 8px 12px;
  text-align: center;
  cursor: pointer;
  border-radius: 4px;
  transition: all 0.3s ease;
}

.year-item:hover {
  background-color: #ecf5ff;
}

.year-item.active {
  background-color: #409eff;
  color: #fff;
}

.periods-grid {
  display: grid;
  grid-template-columns: repeat(6, 1fr);
  gap: 20px;
  margin-bottom: 40px;
}

/* 响应式设计 */
@media screen and (max-width: 1200px) {
  .periods-grid {
    grid-template-columns: repeat(4, 1fr);
  }
}

@media screen and (max-width: 768px) {
  .periods-grid {
    grid-template-columns: repeat(3, 1fr);
  }
  
  .top-tabs {
    display: flex;
    flex-wrap: wrap;
  }
  
  .top-tabs .el-button {
    margin-bottom: 10px;
  }
}

.period-card {
  position: relative;
  height: 100px;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  background-color: #f5f7fa;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24px;
  font-weight: bold;
  transition: all 0.3s ease;
}

.period-card.closed {
  background-color: #f6ffed;
  border-color: #b7eb8f;
}

.period-card.current {
  background-color: #fff7e6;
  border-color: #ffa940;
  box-shadow: 0 0 8px rgba(255, 169, 64, 0.3);
}

.period-card.opened {
  background-color: #e6f7ff;
  border-color: #91d5ff;
}

.period-card.not-open {
  background-color: #f5f7fa;
  border-color: #e4e7ed;
  color: #c0c4cc;
}

.current-label {
  position: absolute;
  top: 10px;
  left: 10px;
  font-size: 12px;
  color: #fa8c16;
  font-weight: bold;
}

.closed-label {
  position: absolute;
  top: 10px;
  right: 10px;
  font-size: 12px;
  color: #52c41a;
}

.opened-label {
  position: absolute;
  top: 10px;
  right: 10px;
  font-size: 12px;
  color: #1890ff;
}

.not-open-label {
  position: absolute;
  bottom: 10px;
  left: 50%;
  transform: translateX(-50%);
  font-size: 11px;
  color: #909399;
}

.checkmark {
  position: absolute;
  bottom: 10px;
  right: 10px;
  font-size: 14px;
  color: #409eff;
  font-weight: bold;
}

.bottom-section {
  text-align: center;
}

.btn-group {
  display: flex;
  justify-content: center;
  gap: 20px;
  margin-bottom: 20px;
}

.fix-btn {
  margin-bottom: 0;
}

.uncheck-btn {
  margin-bottom: 0;
}

.warning-text {
  color: #f56c6c;
  margin-bottom: 10px;
  font-size: 14px;
}

.info-text {
  color: #909399;
  font-size: 14px;
}
</style>
