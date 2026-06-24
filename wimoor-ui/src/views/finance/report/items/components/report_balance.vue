<template>
  <!-- 顶部查询区域 -->
  <div class="query-header">

      <div class="flex-between">
        <el-space>
          <el-select  v-model="period"    placeholder="选择报表期间" style="width: 120px;">
            <el-option v-for="item in periodOptions" :key="item.value" :label="item.label" :value="item.value"></el-option>
          </el-select>
          <el-select v-model="amountUnit" placeholder="金额单位" style="width: 80px;">
            <el-option label="元" value="1"></el-option>
            <el-option label="千元" value="1000"></el-option>
            <el-option label="万元" value="10000"></el-option>
          </el-select>
          <el-button type="primary" @click="handleQuery" icon="Search">查询</el-button>
        </el-space>
      <el-space>
        <el-button type="warning" @click="handleValidate" icon="Check">检查</el-button>
        <el-button type="default" @click="handleExport" icon="Download">导出</el-button>
        <el-button type="default" @click="handlePrint" icon="Printer">打印</el-button>
      </el-space>
      </div>

  </div>

  <!-- 主要内容区域 -->
  <el-row :gutter="20">
    <!-- 资产表格 -->
    <el-col :lg="12" :md="24">
      <el-table
          :data="assetData"
          border
          default-expand-all
          v-loading="loading"
          class="balance-sheet-table"
          :header-cell-style="headerCellStyle"
          :row-class-name="rowClassName"
      >
        <el-table-column prop="itemName" label="资产" min-width="200">
          <template #default="{ row }">
            <span :style="{ 'padding-left': (row.itemLevel - 1) * 20 + 'px', 'font-weight': row.itemLevel === 1 ? 'bold' : 'normal' }">{{ row.itemName }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="lineNumber" label="行次" align="center" width="80"></el-table-column>
        <el-table-column prop="amount" label="期末余额" align="right" width="150">
          <template #default="scope">
            <span v-if="scope.row.amount&&scope.row.amount!=0.00 && Math.abs(Number(scope.row.amount)) > 0.001" :class="scope.row.amountClass">{{ formatAmount(scope.row.amount) }}</span>
           <span v-else> </span>
          </template>
        </el-table-column>
        <el-table-column prop="yearBeginAmount" label="年初余额" align="right" width="150">
          <template #default="scope">
            <span v-if="scope.row.yearBeginAmount&&scope.row.yearBeginAmount!=0.00 && Math.abs(Number(scope.row.yearBeginAmount)) > 0.001" :class="scope.row.amountClass">{{ formatAmount(scope.row.yearBeginAmount) }}</span>
            <span v-else> </span>
          </template>
        </el-table-column>
      </el-table>
    </el-col>

    <!-- 负债表格 -->
    <el-col :lg="12" :md="24">
      <el-table
          :data="liabilityData"
          border
          default-expand-all
          v-loading="loading"
          class="balance-sheet-table"
          :header-cell-style="headerCellStyle"
          :row-class-name="rowClassName"
      >
        <el-table-column prop="itemName" label="负债和所有者(或股东)权益" min-width="200">
          <template #default="{ row }">
            <span :style="{ 'padding-left': (row.itemLevel - 1) * 20 + 'px', 'font-weight': row.itemLevel === 1 ? 'bold' : 'normal' }">{{ row.itemName }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="lineNumber" label="行次" align="center" width="80"></el-table-column>
        <el-table-column prop="amount" label="期末余额" align="right" width="150">
          <template #default="scope"> 
            <span v-if="scope.row.amount&&scope.row.amount!=0.00 && Math.abs(Number(scope.row.amount)) > 0.001" :class="scope.row.amountClass">{{ formatAmount(scope.row.amount) }}</span>
            <span v-else> </span>
          </template>
        </el-table-column>
        <el-table-column prop="yearBeginAmount" label="年初余额" align="right" width="150">
          <template #default="scope">
            <span v-if="scope.row.yearBeginAmount&&scope.row.yearBeginAmount!=0.00 && Math.abs(Number(scope.row.yearBeginAmount)) > 0.001" :class="scope.row.amountClass">{{ formatAmount(scope.row.yearBeginAmount) }}</span>
            <span v-else> </span>
          </template>
        </el-table-column>
      </el-table>
    </el-col>
  </el-row>

  <!-- 检查结果展示区域 -->
  <!-- 报表检查结果弹框 -->
  <el-dialog v-model="showValidationDialog" title="报表检查结果" width="700px" destroy-on-close>
    <div v-if="validationResult">
      <div style="text-align: center; margin-bottom: 20px;">
        <el-tag :type="validationResult.valid ? 'success' : 'danger'" size="large">
          {{ validationResult.valid ? '检查通过' : '检查未通过' }}
        </el-tag>
      </div>

      <!-- 平衡检查 -->
      <div class="check-section" v-if="balanceCheck">
        <h4 class="check-title">
          <el-icon><CircleCheck /></el-icon>
          平衡检查
        </h4>
        <div :class="['check-content', balanceCheck.balanced ? 'success' : 'error']">
          <p v-if="balanceCheck.balanced" class="success-text">
            <el-icon><SuccessFilled /></el-icon>
            资产负债表平衡，资产总计 = 负债和权益总计
          </p>
          <p v-else class="error-text">
            <el-icon><WarningFilled /></el-icon>
            资产负债表不平，请检查报表项目公式设置
          </p>
          <div class="balance-details" v-if="!balanceCheck.balanced">
            <p>资产总计: {{ formatAmount(balanceCheck.assetTotal) }}</p>
            <p>负债和权益总计: {{ formatAmount(balanceCheck.liabilityEquityTotal) }}</p>
            <p>差额: {{ formatAmount(balanceCheck.difference) }}</p>
          </div>
        </div>
      </div>

      <!-- 损益结转检查 -->
      <div class="check-section" v-if="profitLossCheck">
        <h4 class="check-title">
          <el-icon><Warning /></el-icon>
          损益结转检查
        </h4>
        <div :class="['check-content', profitLossCheck.transferred ? 'success' : 'warning']">
          <p v-if="profitLossCheck.transferred" class="success-text">
            <el-icon><SuccessFilled /></el-icon>
            本期损益已结转
          </p>
          <p v-else class="warning-text">
            <el-icon><WarningFilled /></el-icon>
            本期尚未结转损益，可通过自动结转
            <el-button type="primary" size="small" @click="handleTransferProfitLoss" class="transfer-btn">
              结转损益
            </el-button>
          </p>
          <div class="profit-loss-details" v-if="!profitLossCheck.transferred">
            <p>未结转损益科目数量: {{ profitLossCheck.untransferredCount }}</p>
            <p>未结转金额: {{ formatAmount(profitLossCheck.untransferredAmount) }}</p>
          </div>
        </div>
      </div>

      <!-- 未设置报表项目的科目检查 -->
      <div class="check-section" v-if="unmappedSubjects && unmappedSubjects.length > 0">
        <h4 class="check-title">
          <el-icon><InfoFilled /></el-icon>
          未设置报表项目的科目
        </h4>
        <div class="check-content warning">
          <p class="warning-text">
            <el-icon><WarningFilled /></el-icon>
            您有未设置报表项目的科目:
          </p>
          <el-table :data="unmappedSubjects" border size="small" class="unmapped-table">
            <el-table-column prop="subjectCode" label="科目编码" width="120" />
            <el-table-column prop="subjectName" label="科目名称" min-width="200" />
            <el-table-column prop="balance" label="余额" align="right" width="150">
              <template #default="{ row }">
                {{ formatAmount(row.balance) }}
              </template>
            </el-table-column>
            <el-table-column label="操作" width="120" align="center">
              <template #default="{ row }">
                <el-button type="primary" size="small" @click="handleMappingSubject(row)">
                  设置映射
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </div>

      <!-- 错误信息 -->
      <div class="check-section" v-if="validationResult.errors && validationResult.errors.length > 0">
        <h4 class="check-title error">
          <el-icon><CircleCloseFilled /></el-icon>
          错误信息
        </h4>
        <div class="error-list">
          <div v-for="(error, index) in validationResult.errors" :key="index" class="error-item">
            <el-alert :title="error.message" type="error" :closable="false" show-icon>
              <template #default>
                <p v-if="error.suggestion" class="error-suggestion">{{ error.suggestion }}</p>
              </template>
            </el-alert>
          </div>
        </div>
      </div>

      <!-- 警告信息 -->
      <div class="check-section" v-if="validationResult.warnings && validationResult.warnings.length > 0">
        <h4 class="check-title warning">
          <el-icon><WarningFilled /></el-icon>
          警告信息
        </h4>
        <div class="warning-list">
          <div v-for="(warning, index) in validationResult.warnings" :key="index" class="warning-item">
            <el-alert :title="warning.message" type="warning" :closable="false" show-icon>
              <template #default>
                <p v-if="warning.suggestion" class="warning-suggestion">{{ warning.suggestion }}</p>
              </template>
            </el-alert>
          </div>
        </div>
      </div>
    </div>
  </el-dialog>
</template>

<script setup>
import * as echarts from 'echarts';
import { ref, onMounted, reactive, computed, watch } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import { CircleCheck, CircleCloseFilled, InfoFilled, SuccessFilled, Warning, WarningFilled } from '@element-plus/icons-vue';
import { dateTimesFormat } from '@/utils/index.js';
import { generateReport, validateReport } from '@/api/finance/report';
import finStore from "@/hooks/store/useFinanceStore.js"
import { useRouter } from 'vue-router'

const router = useRouter()

const props = defineProps({
  type: {
    type: String,
    default: 'BALANCE_SHEET_STANDARD'
  }
})
const {type} = toRefs(props);
// 状态变量
const amountUnit = ref('1');
const reportDate = ref('');
const unitName = ref('');
const balanceSheetData = ref([]);
const charts = ref({});
const validationResult = ref(null);
const showValidationDialog = ref(false);
const balanceCheck = ref(null);
const profitLossCheck = ref(null);
const unmappedSubjects = ref([]);
const loading = ref(false);

// 资产数据（根据itemType区分资产类项目）
const assetData = computed(() => {
  return (balanceSheetData.value || []).filter(item => {
    return item.itemType === 'ASSET';
  });
});

// 负债及所有者权益数据
const liabilityData = computed(() => {
  return (balanceSheetData.value || []).filter(item => {
    return item.itemType === 'LIABILITY' || item.itemType === 'EQUITY';
  });
});

// 表格头样式
const headerCellStyle = {
  'background-color': '#f5f7fa',
  'font-weight': 'bold',
  'text-align': 'center',
  'color': '#333'
};

// 行样式
const rowClassName = ({ row }) => {
  if (row.itemLevel === 1) return 'report-header-row';
  return '';
};

// 报表期间选项
const periodOptions = ref([]);
const period = ref('');

// 计算属性
const periodLabel = computed(() => {
  if (!period.value) return '';
  const opt = periodOptions.value.find(p => p.value === period.value);
  return opt ? opt.label : period.value;
});

const unitLabel = computed(() => {
  const units = { '1': '元', '1000': '千元', '10000': '万元' };
  return units[amountUnit.value] || '元';
});

// 生成最近12个月的期间选项
const generateRecentMonths = () => {
  const months = [];
  const now = new Date();

  for (let i = 0; i < 13; i++) {
    const currentDate = new Date(now.getFullYear(), now.getMonth() - i, 1);
    const value = dateTimesFormat(currentDate, 'yyyyMM');
    const label = dateTimesFormat(currentDate, 'yyyy年MM月');
    months.push({ value, label });
  }

  return months;
};

// 监听金额单位变化，自动查询
watch(amountUnit, () => {
  if (period.value) {
    handleQuery();
  }
});

// 监听props.type变化，自动查询
watch(() => props.type, () => {
  if (period.value) {
    handleQuery();
  }
});

onMounted(async () => {
  periodOptions.value = generateRecentMonths();
  // 获取当前会计期间
  try {
    const currentPeriodData = await finStore.getCurrentPeriod();
    if (currentPeriodData && currentPeriodData.periodCode) {
      period.value = currentPeriodData.periodCode;
    } else if (periodOptions.value.length > 0) {
      period.value = periodOptions.value[0].value;
    }
  } catch (error) {
    console.error('获取当前会计期间失败:', error);
    if (periodOptions.value.length > 0) {
      period.value = periodOptions.value[0].value;
    }
  }
  unitName.value = finStore.currentTenantName.value || '';
  handleQuery();
});


// 方法定义
const handleQuery = async () => {
  if (!period.value) {
    ElMessage.warning('请选择报表期间');
    return;
  }
  loading.value = true;
  try {
    // 年初余额 = 上一年12月份的期末余额
    const year = parseInt(period.value.substring(0, 4));
    const yearBeginPeriod = (year - 1) + '12';
    
    const response = await generateReport({
      period: period.value,
      comparePeriod: yearBeginPeriod,
      includeComparison: true,
      groupid: await finStore.getCurrentTenantId(),
      templateCode:props.type,
      amountUnit: amountUnit.value
    });
    balanceSheetData.value = [];
    if (response.data.success === false) {
      ElMessage.warning(response.data.message || '报表生成失败');
      return;
    }
    balanceSheetData.value = response.data.items || [];
    //帮我对balanceSheetData.value 排序，lineNumber 从小到大
    initCharts();
    // 显示验证警告
    if (response.data.warnings && response.data.warnings.length > 0) {
      response.data.warnings.forEach(w => ElMessage.warning(w));
    }
  } catch (error) {
    ElMessage.error('获取资产负债表数据失败');
    console.error(error);
  } finally {
    loading.value = false;
  }
};

const handleExport = () => {
  ElMessage.info('导出功能开发中...');
};

const handlePrint = () => {
  window.print();
};

// 检查报表数据
const handleValidate = async () => {
  if (!period.value) {
    ElMessage.warning('请先选择报表期间');
    return;
  }

  loading.value = true;
  try {
    const groupid = await finStore.getCurrentTenantId();
    const response = await validateReport({
      groupid,
      templateCode: props.type,
      period: period.value
    });

    validationResult.value = response.data;
    showValidationDialog.value = true;

    // 解析平衡检查结果
    if (response.data.errors) {
      const balanceError = response.data.errors.find(e => e.type === 'BALANCE_MISMATCH');
      if (balanceError) {
        // 从错误信息中提取金额
        const match = balanceError.message.match(/资产总计: ([\d,.]+), 负债和权益总计: ([\d,.]+), 差额: ([\d,.]+)/);
        if (match) {
          balanceCheck.value = {
            balanced: false,
            assetTotal: parseFormattedAmount(match[1]),
            liabilityEquityTotal: parseFormattedAmount(match[2]),
            difference: parseFormattedAmount(match[3])
          };
        }
      } else {
        balanceCheck.value = { balanced: true };
      }
    }

    // 解析损益结转检查结果（这里需要根据实际接口返回调整）
    // 假设后端会返回 profitLossCheck 字段
    if (response.data.profitLossCheck) {
      profitLossCheck.value = response.data.profitLossCheck;
    } else {
      // 默认值，实际需要从接口获取
      profitLossCheck.value = {
        transferred: true,
        untransferredCount: 0,
        untransferredAmount: 0
      };
    }

    // 解析未映射科目（这里需要根据实际接口返回调整）
    if (response.data.unmappedSubjects) {
      unmappedSubjects.value = response.data.unmappedSubjects;
    } else {
      unmappedSubjects.value = [];
    }

    if (!response.data.valid) {
      ElMessage.warning('报表检查发现问题，请查看详细信息');
    }
  } catch (error) {
    ElMessage.error('检查失败: ' + (error.message || '未知错误'));
    console.error(error);
  } finally {
    loading.value = false;
  }
};

// 解析格式化的金额字符串
const parseFormattedAmount = (str) => {
  if (!str) return 0;
  return parseFloat(str.replace(/,/g, ''));
};

// 处理自动结转折益
const handleTransferProfitLoss = async () => {
  try {
    await ElMessageBox.confirm(
      '确定要结转本期损益吗？确认将跳转到结账模块。',
      '确认结转',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    ).then(() => {
       router.push({
         path: '/fin/periods',
         query: {
           title: '结账',
           path: '/fin/periods'
         }
       });
    });
     
   
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('损益结转失败: ' + (error.message || '未知错误'));
    }
  }
};

// 处理设置科目映射
const handleMappingSubject = (subject) => {
  // TODO: 跳转到映射规则设置页面或打开映射设置弹窗
  ElMessage.info(`跳转到科目 ${subject.subjectCode} 的映射设置`);
};


const formatCurrency = (value, decimalPlaces) => {
  if (isNaN(value)) return '0.00';
  // 将数值转换为浮点数并保留指定小数位
  const formattedValue = Number(value).toFixed(decimalPlaces);
  // 分割整数和小数部分
  const [integerPart, decimalPart] = formattedValue.split('.');
  // 整数部分添加千位分隔符
  const thousands = integerPart.replace(/\B(?=(\d{3})+(?!\d))/g, ',');
  // 组合并返回结果
  return decimalPart ? `${thousands}.${decimalPart}` : thousands;
};

const formatAmount = (value) => {
  return formatCurrency(value, amountUnit.value === '1' ? 2 : 4);
};

// 图表实例引用
const chartInstances = ref({});

// 资产结构图配置
const assetChartOption = ref({
  tooltip: { trigger: 'item' },
  legend: { position: 'bottom' },
  series: [{
    type: 'pie',
    radius: '70%',
    data: [
      { value: 1258640, name: '货币资金' },
      { value: 1856420, name: '应收账款' },
      { value: 2156840, name: '存货' },
      { value: 5856420, name: '固定资产' },
      { value: 3071952, name: '其他资产' }
    ]
  }]
});

// 负债与权益结构图配置
const liabilityChartOption = ref({
  tooltip: { trigger: 'item' },
  legend: { position: 'bottom' },
  series: [{
    type: 'pie',
    radius: '70%',
    data: [
      { value: 4433410, name: '流动负债' },
      { value: 4500000, name: '长期负债' },
      { value: 5227861, name: '所有者权益' }
    ]
  }]
});

// 趋势分析图配置
const trendChartOption = ref({
  tooltip: { trigger: 'axis' },
  legend: { top: 0 },
  xAxis: { type: 'category', data: ['1月','2月','3月','4月','5月','6月','7月','8月','9月','10月','11月','12月'] },
  yAxis: { type: 'value' },
  series: [
    { name: '总资产', type: 'line', data: [12000000,12200000,12500000,12650000,12800000,13000000,13200000,13400000,13600000,13800000,14000000,14161272] },
    { name: '总负债', type: 'line', data: [8000000,8100000,8200000,8300000,8400000,8500000,8600000,8700000,8800000,8900000,8933410,8933410] }
  ]
});

// 初始化图表
const initCharts = () => {
  // 销毁已有图表实例
  Object.values(chartInstances.value).forEach(instance => {
    if (instance && instance.dispose) {
      instance.dispose();
    }
  });

  // 初始化资产结构图
  const assetDom = document.getElementById('assetChart');
  if (assetDom) {
    chartInstances.value.assetChart = echarts.init(assetDom);
    chartInstances.value.assetChart.setOption(assetChartOption.value);
  }

  // 初始化负债与权益结构图
  const liabilityDom = document.getElementById('liabilityChart');
  if (liabilityDom) {
    chartInstances.value.liabilityChart = echarts.init(liabilityDom);
    chartInstances.value.liabilityChart.setOption(liabilityChartOption.value);
  }

  // 初始化趋势分析图
  const trendDom = document.getElementById('trendChart');
  if (trendDom) {
    chartInstances.value.trendChart = echarts.init(trendDom);
    chartInstances.value.trendChart.setOption(trendChartOption.value);
  }

  // 处理窗口大小变化
  window.addEventListener('resize', () => {
    Object.values(chartInstances.value).forEach(instance => {
      if (instance && instance.resize) {
        instance.resize();
      }
    });
  });
};
</script>

<style scoped lang="scss">
.balance-sheet-container {
  padding: 20px;
}

.query-header {
  margin-bottom: 20px;
}

.query-buttons {
  text-align: right;
}

.report-title {
  text-align: center;
  margin-bottom: 20px;

  h2 {
    margin: 0 0 8px 0;
    font-size: 18px;
  }

  p {
    margin: 0;
    color: #666;
    font-size: 14px;
  }
}

.balance-sheet-table {
  width: 100%;

  :deep(.el-table__header th) {
    background-color: #f5f7fa;
    color: #333;
    font-weight: bold;
    text-align: center;
  }

  :deep(.report-header-row) {
    background-color: #fafafa;
  }

  :deep(.report-header-row td) {
    font-weight: bold;
    background-color: #fafafa;
  }
}

.metrics-cards {
  margin-top: 20px;
}

.metric-card {
  height: 100%;
  .metric-title {
    color: var(--el-text-color-secondary);
    font-size: 14px;
    margin-bottom: 5px;
  }
  .metric-value {
    font-size: 24px;
    font-weight: bold;
    margin-bottom: 5px;
  }
  .metric-hint {
    font-size: 12px;
    color: var(--el-text-color-secondary);
  }
}

.chart-card {
  .card-header {
    display: flex;
    align-items: center;
    font-weight: bold;
  }
  .chart-container {
    width: 100%;
    height: 100%;
    min-height: 280px;
    position: relative;
  }
}

.table-title {
  text-align: center;
  margin-bottom: 15px;
  font-size: 16px;
  font-weight: bold;
  color: var(--el-text-color-primary);
}

// 检查结果样式
.validation-section {
  margin-top: 20px;
}

.validation-card {
  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    font-weight: bold;
    font-size: 16px;
  }
}

.check-section {
  margin-bottom: 20px;
  padding: 15px;
  background-color: var(--el-bg-color-page);
  border-radius: 8px;
  border: 1px solid var(--el-border-color-lighter);

  &:last-child {
    margin-bottom: 0;
  }
}

.check-title {
  display: flex;
  align-items: center;
  gap: 8px;
  margin: 0 0 12px 0;
  font-size: 15px;
  font-weight: 600;
  color: var(--el-text-color-primary);

  &.error {
    color: var(--el-color-danger);
  }

  &.warning {
    color: var(--el-color-warning);
  }
}

.check-content {
  padding: 12px;
  border-radius: 6px;

  &.success {
    background-color: var(--el-color-success-light-9);
    border: 1px solid var(--el-color-success-light-5);
  }

  &.error {
    background-color: var(--el-color-danger-light-9);
    border: 1px solid var(--el-color-danger-light-5);
  }

  &.warning {
    background-color: var(--el-color-warning-light-9);
    border: 1px solid var(--el-color-warning-light-5);
  }
}

.success-text {
  display: flex;
  align-items: center;
  gap: 8px;
  color: var(--el-color-success);
  font-weight: 500;
  margin: 0;
}

.error-text {
  display: flex;
  align-items: center;
  gap: 8px;
  color: var(--el-color-danger);
  font-weight: 500;
  margin: 0;
}

.warning-text {
  display: flex;
  align-items: center;
  gap: 8px;
  color: var(--el-color-warning);
  font-weight: 500;
  margin: 0;
}

.balance-details,
.profit-loss-details {
  margin-top: 12px;
  padding: 10px;
  background-color: rgba(0, 0, 0, 0.02);
  border-radius: 4px;

  p {
    margin: 4px 0;
    font-size: 14px;
    color: var(--el-text-color-regular);
  }
}

.transfer-btn {
  margin-left: 12px;
}

.unmapped-table {
  margin-top: 12px;
}

.error-list,
.warning-list {
  .error-item,
  .warning-item {
    margin-bottom: 10px;

    &:last-child {
      margin-bottom: 0;
    }
  }
}

.error-suggestion,
.warning-suggestion {
  margin: 8px 0 0 0;
  font-size: 13px;
  color: var(--el-text-color-secondary);
  white-space: pre-line;
}
</style>
