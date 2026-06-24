<template>
  <!-- 顶部查询区域 -->
  <div class="query-header">
    <div class="flex-between">

        <el-space>
        <el-select v-model="period" placeholder="选择报表期间" style="width: 120px;">
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
        <el-button type="default" @click="handleExport" icon="Download">导出</el-button>
        <el-button type="default" @click="handlePrint" icon="Printer">打印</el-button>
      </el-space>
    </div>
  </div>

  <!-- 主要内容区域 -->
  <el-row :gutter="20">
    <el-col :lg="24" :md="24">
      <el-table
        :data="equityData"
        border
        v-loading="loading"
        class="equity-table"
        :row-class-name="rowClassName"
      >
        <el-table-column prop="itemName" label="项　目"  width="440">
          <template #default="{ row }">
            <span :style="{ 'padding-left': (row.itemLevel - 1) * 20 + 'px', 'font-weight': row.itemLevel <= 2 ? 'bold' : 'normal' }">{{ row.itemName }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="lineNumber" label="行次" align="center" width="60"></el-table-column>
        <el-table-column label="本年金额" align="right" width="200">
          <template #default="{ row }">
            <span v-if="row.amount && Math.abs(Number(row.amount)) > 0.001" :class="row.amountClass">{{ formatAmount(row.amount) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="上年金额" align="right" width="200">
          <template #default="{ row }">
            <span v-if="row.yearBeginAmount && Math.abs(Number(row.yearBeginAmount)) > 0.001" :class="row.amountClass">{{ formatAmount(row.yearBeginAmount) }}</span>
          </template>
        </el-table-column>
       <el-table-column label="" >
          <template #default="{ row }">
          </template>
        </el-table-column>
      </el-table>
    </el-col>
  </el-row>
</template>

<script setup>
import { ref, onMounted, computed, watch, toRefs } from 'vue';
import { ElMessage } from 'element-plus';
import { dateTimesFormat } from '@/utils/index.js';
import { generateReport } from '@/api/finance/report';
import finStore from "@/hooks/store/useFinanceStore.js";

const props = defineProps({
  type: {
    type: String,
    default: 'EQUITY_CHANGE_STANDARD'
  }
});

const { type } = toRefs(props);

// 状态变量
const amountUnit = ref('1');
const loading = ref(false);
const equityData = ref([]);
const unitName = ref('');

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

// 计算上年对应期间
const getComparePeriod = (currentPeriod) => {
  if (!currentPeriod || currentPeriod.length < 6) return null;
  const year = parseInt(currentPeriod.substring(0, 4));
  const month = currentPeriod.substring(4, 6);
  return (year - 1) + month;
};

// 方法定义
const handleQuery = async () => {
  if (!period.value) {
    ElMessage.warning('请选择报表期间');
    return;
  }

  loading.value = true;
  try {
    const groupid = await finStore.getCurrentTenantId();
    const comparePeriod = getComparePeriod(period.value);
    const response = await generateReport({
      period: period.value,
      groupid: groupid,
      templateCode: props.type,
      amountUnit: amountUnit.value,
      includeComparison: true,
      comparePeriod: comparePeriod
    });

    equityData.value = [];

    if (response.data.success === false) {
      ElMessage.warning(response.data.message || '报表生成失败');
      return;
    }

    const items = response.data.items || [];
    items.sort((a, b) => a.lineNumber - b.lineNumber);
    equityData.value = items;

    if (response.data.warnings && response.data.warnings.length > 0) {
      response.data.warnings.forEach(w => ElMessage.warning(w));
    }

  } catch (error) {
    console.error('查询报表失败:', error);
    ElMessage.error('查询报表失败: ' + (error.message || '未知错误'));
  } finally {
    loading.value = false;
  }
};

const handleExport = () => {
  ElMessage.info('导出功能开发中');
};

const handlePrint = () => {
  window.print();
};

// 行样式
const rowClassName = ({ row }) => {
  if (row.itemLevel === 1) return 'equity-header-row';
  if (row.itemCode && (row.itemCode.endsWith('_TOTAL'))) return 'equity-total-row';
  return '';
};

// 格式化金额
const formatCurrency = (value, decimalPlaces) => {
  if (isNaN(value)) return '0.00';
  const formattedValue = Number(value).toFixed(decimalPlaces);
  const [integerPart, decimalPart] = formattedValue.split('.');
  const thousands = integerPart.replace(/\B(?=(\d{3})+(?!\d))/g, ',');
  return decimalPart ? `${thousands}.${decimalPart}` : thousands;
};

const formatAmount = (value) => {
  return formatCurrency(value, amountUnit.value === '1' ? 2 : 4);
};
</script>

<style scoped>
.query-header {
  margin-bottom: 20px;
}

.report-title {
  text-align: center;
  margin-bottom: 10px;
}

.report-title h2 {
  margin: 0 0 8px 0;
  font-size: 18px;
}

.report-title p {
  margin: 0;
  color: #666;
  font-size: 14px;
}

.equity-table {
  width: 100%;
}

.equity-table :deep(.el-table__header th) {
  background-color: #f5f7fa;
  color: #333;
  font-weight: bold;
  text-align: center;
}

.equity-table :deep(.equity-header-row) {
  background-color: #fafafa;
}

.equity-table :deep(.equity-header-row td) {
  font-weight: bold;
  background-color: #fafafa;
}

.equity-table :deep(.equity-total-row) {
  background-color: #f0f9ff;
}

.equity-table :deep(.equity-total-row td) {
  font-weight: bold;
  background-color: #f0f9ff;
}

.query-buttons {
  display: flex;
  gap: 8px;
}
</style>
