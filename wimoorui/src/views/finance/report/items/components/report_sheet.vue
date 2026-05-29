<template>
    <!-- 顶部查询区域 -->
    <div class="query-header">
      <el-row :gutter="20">
        <el-col :span="6">
          <el-select  v-model="period"    placeholder="选择报表期间" >
            <el-option v-for="item in periodOptions" :key="item.value" :label="item.label" :value="item.value"></el-option>
          </el-select>
        </el-col>
        <el-col :span="6">
          <el-select v-model="comparePeriod" placeholder="选择对比期间"  >
              <el-option v-for="item in periodOptions" :key="item.value" :label="item.label" :value="item.value"></el-option>
          </el-select>
        </el-col>
        <el-col :span="4">
          <el-select v-model="amountUnit" placeholder="金额单位">
            <el-option label="元" value="1"></el-option>
            <el-option label="千元" value="1000"></el-option>
            <el-option label="万元" value="10000"></el-option>
          </el-select>
        </el-col>
        <el-col :span="8" class="query-buttons">
          <el-button type="primary" @click="handleQuery" icon="Search">查询</el-button>
          <el-button type="default" @click="handleExport" icon="Download">导出</el-button>
          <el-button type="default" @click="handlePrint" icon="Printer">打印</el-button>
        </el-col>
      </el-row>
    </div>

    <!-- 主要内容区域 -->
    <el-row :gutter="20">
      <!-- 左侧表格区域 -->
      <el-col :lg="24" :md="24">
        <el-table
          :data="balanceSheetData"
          border
          default-expand-all
          class="balance-sheet-table"
        >
          <el-table-column prop="itemName" label="项目" min-width="200">
            <template #default="{ row }">
              <span :style="{ 'padding-left': (row.itemLevel - 1) * 20 + 'px' }">{{ row.itemName }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="lineNumber" label="行次" align="center" width="80"></el-table-column>
          <el-table-column prop="currentAmount" label="本期金额" align="right">
            <template #default="scope">

              <span v-if="scope.row.itemLevel!=1||formatAmount(scope.row.currentAmount)!=0.00" :class="scope.row.amountClass">{{ formatAmount(scope.row.amount) }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="compareAmount" label="对比期间金额" align="right">
            <template #default="scope">
              <span v-if="scope.row.itemLevel!=1||formatAmount(scope.row.comparisonAmount)!=0.00"  :class="scope.row.amountClass">{{ formatAmount(scope.row.comparisonAmount) }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="difference" label="增减额及幅度" align="right">
            <template #default="scope">
              <div v-if="scope.row.itemLevel!=1||formatAmount(scope.row.changeRate)!=0.00" class="difference-cell">
                <span :class="scope.row.diffClass">{{ formatAmount(scope.row.changeAmount) }}</span>
                <el-tag v-if="scope.row.changeRate" :type="scope.row.percentType" size="small">{{ scope.row.changeRate }}</el-tag>
              </div>
            </template>
          </el-table-column>
        </el-table>

        <!-- 财务指标卡片 -->
       
      </el-col>

    </el-row>
</template>

<script setup>
import * as echarts from 'echarts';
import { ref, onMounted, reactive } from 'vue';
import { ElMessage } from 'element-plus';
import { dateTimesFormat } from '@/utils/index.js';
import { generateReport } from '@/api/finance/report';
import finStore from "@/hooks/store/useFinanceStore.js"

const props = defineProps({
    type: {
        type: String,
        default: 'BALANCE_SHEET_STANDARD'
    }
})
const {type} = toRefs(props);
// 状态变量
const comparePeriod = ref('');
const amountUnit = ref('1');
const reportDate = ref('');
const balanceSheetData = ref([]);
const charts = ref({});

// 表格头样式
const headerCellStyle = {
  'background-color': 'var(--el-bg-color)',
  'font-weight': 'bold',
  'text-align': 'center'
};

// 报表期间选项
const periodOptions = ref([]);
const period = ref('');

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

onMounted(() => {
  // 初始化期间选项
  watch(props, (newType) => {
    if (newType) {
      handleQuery();
    }
  })
  periodOptions.value = generateRecentMonths();
  // 默认选中最近一个月
  if (periodOptions.value.length > 0) {
    period.value = periodOptions.value[0].value;
    }
    handleQuery();
});


// 方法定义
const handleQuery = async () => {
    try {
    const response = await generateReport({
        period: period.value,
        comparePeriod: comparePeriod.value,
        groupid: await finStore.getCurrentTenantId(),
        templateCode:props.type,
        amountUnit: amountUnit.value
    });
        balanceSheetData.value = [];
        balanceSheetData.value = response.data.items;
        //帮我对balanceSheetData.value 排序，lineNumber 从小到大
    initCharts();
  } catch (error) {
    ElMessage.error('获取资产负债表数据失败');
    console.error(error);
  }
};

const handleExport = () => {
  ElMessage.info('导出功能开发中...');
};

const handlePrint = () => {
  window.print();
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
    margin-bottom: 5px;
  }
}

.balance-sheet-table {
  width: 100%;
  .el-table__body-wrapper {
    max-height: 600px;
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

.difference-cell {
  display: flex;
  align-items: center;
  justify-content: flex-end;
  gap: 5px;
}

.positive-amount {
  color: var(--el-color-success);
}

.negative-amount {
  color: var(--el-color-danger);
}

.increase {
  color: var(--el-color-success);
}

.decrease {
  color: var(--el-color-danger);
}
</style>
 