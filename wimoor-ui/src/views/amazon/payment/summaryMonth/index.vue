<template>
  <div  >

    <!-- 筛选条件 -->
    <div  >
      <el-space style="margin-bottom: 20px;">
        <Group @change="groupChange" />
        <el-select style="width:100px;" v-model="searchForm.currency" @change="handleSearch">
          <el-option v-for="item in currentyOptions" :label="item.label" :value="item.value"></el-option>
        </el-select>
        <div class="date-picker-group">
          <el-select style="width:120px;" placeholder="结算日期" v-model="searchForm.datetype" @change="handleSearch">
            <el-option v-for="item in dateOptions" :label="item.label" :value="item.value"></el-option>
          </el-select>
          <Datepicker ref="datepickers" :shortIndex="1" @changedate="changedate" />
        </div>
        <el-button type="primary" @click="handleSearch" :loading="searchLoading" size="default">查询</el-button>
        <el-button @click="resetForm" size="default">重置</el-button>
      </el-space>
    </div>

    <!-- 数据统计 -->
    <div class="stats-container">
      <el-row :gutter="20">
        <el-col :span="6" v-for="(item, index) in statistics" :key="index">
          <el-card shadow="hover" class="stats-card">
            <template #header>
              <div class="card-header-title">{{ item.label }}</div>
            </template>
            <div class="stats-value">{{ item.label === '费用项目数' ? item.value : formatCurrency(item.value) }}</div>
            <div class="stats-subtitle">{{ item.description }}</div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <!-- 图表展示 -->
    <div class="chart-row">
      <div class="chart-container">
        <el-card shadow="hover">
          <template #header>
            <div class="card-header-title">交易类型占比</div>
          </template>
          <div v-loading="chartLoading" element-loading-text="图表加载中..." ref="chartRef" class="chart"></div>
        </el-card>
      </div>
      <div class="chart-container">
        <el-card shadow="hover">
          <template #header>
            <div class="card-header-title">金额类型分布</div>
          </template>
          <div v-loading="chartLoading" element-loading-text="图表加载中..." ref="amountChartRef" class="chart"></div>
        </el-card>
      </div>
    </div>

    <!-- 数据表格 -->
    <div class="table-container">
      <!-- 进项和出项交易（一行显示） -->
      <div >
  <!-- 分类统计 -->
      <el-tabs v-model="activeTab" class="analysis-tabs">
        <el-tab-pane label="交易类型统计" name="transaction">
          <el-card shadow="never">
            <el-table :data="transactionSummary" stripe style="width: 100%">
              <el-table-column prop="type" label="交易类型" width="180" />
              <el-table-column prop="count" label="费用项目数" width="120">
                <template #default="{ row }">
                  <el-tag :type="row.count > 0 ? 'success' : 'info'">
                    {{ row.count }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column prop="amount" label="金额">
                <template #default="{ row }">
                  <span :class="row.amount >= 0 ? 'positive' : 'negative'">
                    {{ formatCurrency(row.amount) }}
                  </span>
                </template>
              </el-table-column>
              <el-table-column prop="percentage" label="占比">
                <template #default="{ row }">
                  <el-progress
                      :percentage="row.percentage"
                      :stroke-width="10"
                      :color="row.amount >= 0 ? '#67c23a' : '#f56c6c'"
                      striped
                  />
                </template>
              </el-table-column>
            </el-table>
          </el-card>
        </el-tab-pane>

        <el-tab-pane label="费用类型统计" name="amountType">
          <el-card shadow="never">
            <el-table :data="amountTypeSummary" stripe>
              <el-table-column prop="type" label="费用类型" width="200" />
              <el-table-column prop="count" label="笔数" width="100" />
              <el-table-column prop="amount" label="金额">
                <template #default="{ row }">
                  <span :class="row.amount >= 0 ? 'positive' : 'negative'">
                    {{ formatCurrency(row.amount) }}
                  </span>
                </template>
              </el-table-column>
              <el-table-column label="趋势" width="150">
                <template #default="{ row }">
                  <el-tag
                      :type="row.amount >= 0 ? 'success' : 'danger'"
                      effect="dark"
                  >
                    {{ row.amount >= 0 ? '收入' : '支出' }}
                  </el-tag>
                </template>
              </el-table-column>
            </el-table>
          </el-card>
        </el-tab-pane>

        <el-tab-pane label="SKU统计" name="sku">
          <el-card shadow="never">
            <el-row :gutter="20">
              <el-col :span="24">
                <el-space class="sku-stats">
                  <div class="sku-stat-item">
                  <div class="sku-label">有SKU记录</div>
                  <div class="sku-value">
                    <el-tag type="success">{{ skuStats.withSku }}</el-tag>
                    笔
                  </div>
                </div>
                <div class="sku-stat-item">
                  <div class="sku-label">无SKU记录</div>
                  <div class="sku-value">
                    <el-tag type="warning">{{ skuStats.withoutSku }}</el-tag>
                    笔
                  </div>
                </div>
                <div class="sku-stat-item">
                  <div class="sku-label">总费用项目数</div>
                  <div class="sku-value">
                    <el-tag type="primary">{{ skuStats.total }}</el-tag>
                    笔
                  </div>
                </div>
                </el-space>
              </el-col>
            </el-row>
            <el-row :gutter="20" style="margin-top: 20px;">
              <el-col :span="12">
                <div class="chart-container">
                  <h3>SKU的费用类型分布</h3>
                  <div ref="skuWithChart" style="height: 300px;"></div>
                </div>
              </el-col>
              <el-col :span="12">
                <div class="chart-container">
                  <h3>店铺费用类型分布</h3>
                  <div ref="skuWithoutChart" style="height: 300px;"></div>
                </div>
              </el-col>
            </el-row>
          </el-card>
        </el-tab-pane>

        <el-tab-pane label="详细数据" name="detail">
           <!-- 所有交易记录 -->
      <el-card shadow="hover" class="mb-20">
        <template #header>
          <div class="card-header">
            <div class="card-header-title">所有交易记录</div>
            <div class="card-header-actions">
              <el-space>
                <el-select v-model="tableFilter.amountType" placeholder="金额类型" clearable @change="handleTableFilter">
                  <el-option
                    v-for="type in uniqueAmountTypes"
                    :key="type"
                    :label="type"
                    :value="type"
                  />
                </el-select>
                <el-select v-model="tableFilter.transactionType" placeholder="交易类型" clearable @change="handleTableFilter">
                  <el-option
                    v-for="type in uniqueTransactionTypes"
                    :key="type"
                    :label="type"
                    :value="type"
                  />
                </el-select>
                <el-select v-model="tableFilter.marketplace" placeholder="Marketplace" clearable @change="handleTableFilter">
                  <el-option
                    v-for="marketplace in uniqueMarketplaces"
                    :key="marketplace"
                    :label="marketplace"
                    :value="marketplace"
                  />
                </el-select>
                <el-button type="info" size="small" @click="resetTableFilter">重置筛选</el-button>
                <el-button type="success" size="small" @click="exportData">导出数据</el-button>
              </el-space>
            </div>
          </div>
        </template>
        <el-table
          v-loading="loading"
          :data="transactionList"
          style="width: 100%"
          border
          stripe
          :default-sort="{prop: 'amount', order: 'descending'}"
        >
          <el-table-column prop="amount_type" label="金额类型" width="180" sortable>
            <template #default="scope">
              <span>{{ scope.row.amount_typename || scope.row.amount_type }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="transaction_type" label="交易类型" width="150" sortable>
            <template #default="scope">
              <span>{{ scope.row.transaction_typename || scope.row.transaction_type }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="amount" label="金额" width="150" sortable>
            <template #default="scope">
              <span :class="{ 'amount-positive': scope.row.amount >= 0, 'amount-negative': scope.row.amount < 0 }">
                {{ formatCurrency(scope.row.amount) }}
              </span>
            </template>
          </el-table-column>
          <el-table-column prop="amount_description" label="金额描述" min-width="200">
            <template #default="scope">
              <div>
                <div>{{ scope.row.amount_descriptionname || scope.row.amount_description }}</div>
                <div v-if="scope.row.amount_descriptionname && scope.row.amount_description" class="text-secondary">{{ scope.row.amount_description }}</div>
              </div>
            </template>
          </el-table-column>
          <el-table-column prop="marketplace_name" label="Marketplace" width="150" sortable />
          <el-table-column prop="isnosku" label="是否无SKU" width="100" sortable>
            <template #default="scope">
              <el-tag :type="scope.row.isnosku === '1' ? 'warning' : 'success'">
                {{ scope.row.isnosku === '1' ? '是' : '否' }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="120" fixed="right">
            <template #default="scope">
              <el-button type="primary" size="small" @click="viewDetail(scope.row)">
                查看详情
              </el-button>
            </template>
          </el-table-column>
        </el-table>


      </el-card>
        </el-tab-pane>
         <el-tab-pane label="进项交易" name="in">
          <el-card shadow="hover">
            <template #header>
              <div class="card-header">
                <div class="card-header-title"></div>
                <div class="card-header-actions">
                  <el-space>
                  <el-tag type="success">{{ financialCategories.income.items.length }} 笔交易</el-tag>
                  <el-tag type="info">{{ formatCurrency(financialCategories.income.total) }}</el-tag>
                  </el-space>
                </div>
              </div>
            </template>
            <el-table
              v-loading="loading"
              :data="financialCategories.income.items"
              style="width: 100%"
    
              border
              stripe
              :default-sort="{prop: 'amount', order: 'descending'}"
            >
              <el-table-column prop="amount_type" label="金额类型" width="180" sortable>
                <template #default="scope">
                  <span>{{ scope.row.amount_typename || scope.row.amount_type }}</span>
                </template>
              </el-table-column>
              <el-table-column prop="transaction_type" label="交易类型" width="150" sortable>
                <template #default="scope">
                  <span>{{ scope.row.transaction_typename || scope.row.transaction_type }}</span>
                </template>
              </el-table-column>
              <el-table-column prop="amount" label="金额" width="150" sortable>
                <template #default="scope">
                  <div><span class="amount-positive">{{ formatCurrency(scope.row.amount) }}</span></div>
                  <div>
                    <el-tag v-if="scope.row.isnosku === '1' " type="warning">
                      店铺费用
                    </el-tag>
                  </div>
                </template>
              </el-table-column>
              <el-table-column prop="amount_description" label="金额描述" min-width="200">
                <template #default="scope">
                  <div>
                    <div>{{ scope.row.amount_descriptionname || scope.row.amount_description }}</div>
                    <div v-if="scope.row.amount_descriptionname && scope.row.amount_description" class="text-secondary">{{ scope.row.amount_description }}</div>
                  </div>
                </template>
              </el-table-column>

              <el-table-column label="操作" width="120"  >
                <template #default="scope">
                  <el-button type="primary" size="small" @click="viewDetail(scope.row)">
                    查看详情
                  </el-button>
                </template>
              </el-table-column>
            </el-table>
          </el-card>
         </el-tab-pane>
         <el-tab-pane label="出项交易" name="out">
               <el-card shadow="hover">
            <template #header>
              <div class="card-header">
              <div class="card-header-title"></div>
                <div class="card-header-actions">
                  <el-space>
                  <el-tag type="danger">{{ financialCategories.expenses.items.length }} 笔交易</el-tag>
                  <el-tag type="info">{{ formatCurrency(financialCategories.expenses.total) }}</el-tag>
                  </el-space>
                </div>
              </div>
            </template>
            <el-table
              v-loading="loading"
              :data="financialCategories.expenses.items"
              style="width: 100%"
           
              border
              stripe
              :default-sort="{prop: 'amount', order: 'ascending'}"
            >
              <el-table-column prop="amount_type" label="金额类型" width="180" sortable>
                <template #default="scope">
                  <span>{{ scope.row.amount_typename || scope.row.amount_type }}</span>
                </template>
              </el-table-column>
              <el-table-column prop="transaction_type" label="交易类型" width="150" sortable>
                <template #default="scope">
                  <span>{{ scope.row.transaction_typename || scope.row.transaction_type }}</span>
                </template>
              </el-table-column>
              <el-table-column prop="amount" label="金额" width="150" sortable>
                <template #default="scope">
                 <div> <span class="amount-negative">{{ formatCurrency(scope.row.amount) }}</span></div>
                  <div>
                    <el-tag v-if="scope.row.isnosku === '1' " type="warning">
                    店铺费用
                  </el-tag>
                  </div>
                </template>
              </el-table-column>
              <el-table-column prop="amount_description" label="金额描述" min-width="200">
                <template #default="scope">
                  <div>
                    <div>{{ scope.row.amount_descriptionname || scope.row.amount_description }}</div>
                    <div v-if="scope.row.amount_descriptionname && scope.row.amount_description" class="text-secondary">{{ scope.row.amount_description }}</div>
                  </div>
                </template>
              </el-table-column>
              <el-table-column label="操作" width="120"  >
                <template #default="scope">
                  <el-button type="primary" size="small" @click="viewDetail(scope.row)">
                    查看详情
                  </el-button>
                </template>
              </el-table-column>
            </el-table>
          </el-card>
         </el-tab-pane>
      </el-tabs>
    
 
      </div>

      



    </div>

    <!-- 详情对话框 -->
    <el-dialog
      v-model="detailDialogVisible"
      title="交易详情"
      width="80%"
      center
      :close-on-click-modal="false"
    >
      <div v-if="selectedTransaction">
        <h3>{{ (selectedTransaction.amount_typename || selectedTransaction.amount_type) }} - {{ (selectedTransaction.transaction_typename || selectedTransaction.transaction_type) }}</h3>
        <el-divider />
        <div class="detail-stats">
          <el-row :gutter="20">
            <el-col :span="8">
              <div class="detail-stat-item">
                <span class="stat-label">金额:</span>
                <span class="stat-value">{{ formatCurrency(selectedTransaction.amount) }}</span>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="detail-stat-item">
                <span class="stat-label">货币:</span>
                <span class="stat-value">{{ selectedTransaction.currency || '未指定' }}</span>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="detail-stat-item">
                <span class="stat-label">Marketplace:</span>
                <span class="stat-value">{{ selectedTransaction.marketplace_name || '未指定' }}</span>
              </div>
            </el-col>
          </el-row>
          <el-row :gutter="20" style="margin-top: 20px;">
            <el-col :span="8">
              <div class="detail-stat-item">
                <span class="stat-label">金额类型:</span>
                <span class="stat-value">{{ selectedTransaction.amount_typename || selectedTransaction.amount_type }}</span>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="detail-stat-item">
                <span class="stat-label">交易类型:</span>
                <span class="stat-value">{{ selectedTransaction.transaction_typename || selectedTransaction.transaction_type }}</span>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="detail-stat-item">
                <span class="stat-label">是否无SKU:</span>
                <span class="stat-value">{{ selectedTransaction.isnosku === '1' ? '是' : '否' }}</span>
              </div>
            </el-col>
          </el-row>
          <el-row :gutter="20" style="margin-top: 20px;">
            <el-col :span="12">
              <div class="detail-stat-item">
                <span class="stat-label">金额描述:</span>
                <span class="stat-value">{{ selectedTransaction.amount_descriptionname || selectedTransaction.amount_description || '无' }}</span>
              </div>
            </el-col>
            <el-col :span="12">
              <div class="detail-stat-item">
                <span class="stat-label">原始描述:</span>
                <span class="stat-value">{{ selectedTransaction.amount_description || '无' }}</span>
              </div>
            </el-col>
          </el-row>
        </div>
        
        <!-- 额外信息 -->
        <div v-if="selectedTransaction.sku || selectedTransaction.asin || selectedTransaction.order_id" style="margin-top: 30px;">
          <h4>相关信息</h4>
          <el-divider />
          <el-row :gutter="20">
            <el-col :span="8" v-if="selectedTransaction.sku">
              <div class="detail-stat-item">
                <span class="stat-label">SKU:</span>
                <span class="stat-value">{{ selectedTransaction.sku }}</span>
              </div>
            </el-col>
            <el-col :span="8" v-if="selectedTransaction.asin">
              <div class="detail-stat-item">
                <span class="stat-label">ASIN:</span>
                <span class="stat-value">{{ selectedTransaction.asin }}</span>
              </div>
            </el-col>
            <el-col :span="8" v-if="selectedTransaction.order_id">
              <div class="detail-stat-item">
                <span class="stat-label">订单ID:</span>
                <span class="stat-value">{{ selectedTransaction.order_id }}</span>
              </div>
            </el-col>
          </el-row>
        </div>
      </div>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="detailDialogVisible = false">关闭</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed, watch } from 'vue'
import { ElMessage } from 'element-plus'
import * as echarts from 'echarts'
import Datepicker from '@/components/header/datepicker.vue';
import Group from '@/components/header/group.vue';
import settlementApi from '@/api/amazon/finances/settlementApi'

// 搜索表单
const searchForm = reactive({
  month: '',
  accountId: '',
  site: '',
  currency:'market',
  datetype: '',
  isload:false,
})

// 数据状态
const loading = ref(false)
const searchLoading = ref(false)
const chartLoading = ref(false)
const detailLoading = ref(false)
const transactionList = ref([])
const total = ref(0)
const currentPage = ref(1)
const pageSize = ref(20)
const chartRef = ref(null)
const amountChartRef = ref(null)
const chartInstance = ref(null)
const amountChartInstance = ref(null)
const detailDialogVisible = ref(false)
const selectedTransaction = ref(null)
const currencySymbol = ref('¥')
const dateOptions = ref([
 {label:'结算日期',value:''},{label:'转账日期',value:'acc'}
])
 
// 表格筛选条件
const tableFilter = reactive({
  amountType: '',
  transactionType: '',
  marketplace: ''
})

// 下拉选项
const siteList = ref([
  { label: '美国', value: 'US' },
  { label: '加拿大', value: 'CA' },
  { label: '墨西哥', value: 'MX' },
  { label: '英国', value: 'GB' },
  { label: '德国', value: 'DE' },
  { label: '法国', value: 'FR' },
  { label: '意大利', value: 'IT' },
  { label: '西班牙', value: 'ES' },
  { label: '日本', value: 'JP' },
  { label: '澳大利亚', value: 'AU' }
])
const currentyOptions=[{label:'原币种',value:'market'},{label:'CNY',value:'CNY'},{label:'USD',value:'USD'}]

// 计算属性
const rawTotalSales = computed(() => {
  return transactionList.value.reduce((sum, item) => {
    // 假设正数为销售额
    return sum + (item.amount > 0 ? item.amount : 0)
  }, 0)
})

const rawTotalFees = computed(() => {
  return transactionList.value.reduce((sum, item) => {
    // 假设负数为费用
    return sum + (item.amount < 0 ? Math.abs(item.amount) : 0)
  }, 0)
})

const rawTotalProfit = computed(() => {
  return rawTotalSales.value - rawTotalFees.value
})

const totalSales = computed(() => {
  return formatCurrency(rawTotalSales.value)
})

const totalFees = computed(() => {
  return formatCurrency(rawTotalFees.value)
})

const totalProfit = computed(() => {
  return formatCurrency(rawTotalProfit.value)
})

const transactionTypeCount = computed(() => {
  const types = new Set()
  transactionList.value.forEach(item => types.add(item.transaction_type))
  return types.size
})

const transactionCount = computed(() => {
  return transactionList.value.length
})

// 参考test.vue的statistics计算属性，优化数据统计卡片
const statistics = computed(() => {
  const data = transactionList.value
  const totalAmount = data.reduce((sum, item) => sum + Number(item.amount || 0), 0)
  const totalIncome = data.filter(item => Number(item.amount || 0) > 0)
      .reduce((sum, item) => sum + Number(item.amount || 0), 0)
  const totalExpense = data.filter(item => Number(item.amount || 0) < 0)
      .reduce((sum, item) => sum + Number(item.amount || 0), 0)
  const orderCount = data.filter(item => (item.transaction_type === 'Order'&& item.amount_type === 'ItemPrice'&&item.amount_description === 'Principal')).reduce((sum, item) => sum + Number(item.orders || 0), 0);
  const refundCount = data.filter(item => (item.transaction_type === 'Refund'&&item.amount_type === 'ItemPrice'&&item.amount_description === 'Principal')).reduce((sum, item) => sum + Number(item.refundOrders || 0), 0);

  return [
    {
      label: '总金额',
      value: totalAmount,
      description: '所有交易净额'
    },
    {
      label: '总收入',
      value: totalIncome,
      description: '正数金额总和'
    },
    {
      label: '总支出',
      value: totalExpense,
      description: '负数金额总和'
    },
    {
      label: '费用项目数',
      value: data.length,
      description: `订单:${orderCount} | 退款:${refundCount}`
    }
  ]
})

// 财务分类数据
const financialCategories = computed(() => {
  // 进项和出项分类
  const income = transactionList.value.filter(item => item.amount > 0)
  const expenses = transactionList.value.filter(item => item.amount < 0)
  
  // 店铺费用和SKU费用分类
  // 店铺费用：通常是与店铺运营相关的固定费用
  // SKU费用：通常是与具体产品相关的费用
  const storeExpenses = expenses.filter(item => {
    const type = (item.amount_typename || item.amount_type || '').toLowerCase()
    const transactionType = (item.transaction_typename || item.transaction_type || '').toLowerCase()
    
    // 常见的店铺费用关键词
    const storeExpenseKeywords = ['subscription', 'monthly fee', 'store fee', 'account fee', '店铺', '月租', '账户']
    
    return storeExpenseKeywords.some(keyword => type.includes(keyword) || transactionType.includes(keyword))
  })
  
  // SKU费用：排除店铺费用后的其他费用
  const skuExpenses = expenses.filter(item => !storeExpenses.includes(item))
  
  // 费用类型分类
  const expenseTypes = new Map()
  expenses.forEach(item => {
    const type = item.amount_typename || item.amount_type || '其他'
    const amount = Math.abs(item.amount)
    if (expenseTypes.has(type)) {
      expenseTypes.set(type, expenseTypes.get(type) + amount)
    } else {
      expenseTypes.set(type, amount)
    }
  })
  
  // 交易类型分类（用于更详细的分析）
  const transactionTypes = new Map()
  transactionList.value.forEach(item => {
    const type = item.transaction_typename || item.transaction_type || '其他'
    const amount = item.amount
    if (transactionTypes.has(type)) {
      transactionTypes.set(type, transactionTypes.get(type) + amount)
    } else {
      transactionTypes.set(type, amount)
    }
  })
  
  return {
    income: {
      items: income,
      total: income.reduce((sum, item) => sum + item.amount, 0)
    },
    expenses: {
      items: expenses,
      total: expenses.reduce((sum, item) => sum + Math.abs(item.amount), 0)
    },
    storeExpenses: {
      items: storeExpenses,
      total: storeExpenses.reduce((sum, item) => sum + Math.abs(item.amount), 0)
    },
    skuExpenses: {
      items: skuExpenses,
      total: skuExpenses.reduce((sum, item) => sum + Math.abs(item.amount), 0)
    },
    expenseTypes: Array.from(expenseTypes.entries())
      .map(([name, value]) => ({ name, value }))
      .sort((a, b) => b.value - a.value),
    transactionTypes: Array.from(transactionTypes.entries())
      .map(([name, value]) => ({ name, value }))
      .sort((a, b) => Math.abs(b.value) - Math.abs(a.value))
  }
})

// 唯一类型列表（用于筛选）
const uniqueAmountTypes = computed(() => {
  const types = new Set()
  transactionList.value.forEach(item => {
    const type = item.amount_typename || item.amount_type
    if (type) types.add(type)
  })
  return Array.from(types).sort()
})

const uniqueTransactionTypes = computed(() => {
  const types = new Set()
  transactionList.value.forEach(item => {
    const type = item.transaction_typename || item.transaction_type
    if (type) types.add(type)
  })
  return Array.from(types).sort()
})

const uniqueMarketplaces = computed(() => {
  const marketplaces = new Set()
  transactionList.value.forEach(item => {
    if (item.marketplace_name) marketplaces.add(item.marketplace_name)
  })
  return Array.from(marketplaces).sort()
})

// 过滤后的数据
const filteredTransactionList = computed(() => {
  return transactionList.value.filter(item => {
    // 金额类型筛选
    if (tableFilter.amountType) {
      const itemType = item.amount_typename || item.amount_type
      if (itemType !== tableFilter.amountType) return false
    }
    
    // 交易类型筛选
    if (tableFilter.transactionType) {
      const itemType = item.transaction_typename || item.transaction_type
      if (itemType !== tableFilter.transactionType) return false
    }
    
    // Marketplace筛选
    if (tableFilter.marketplace) {
      if (item.marketplace_name !== tableFilter.marketplace) return false
    }
    
    return true
  })
})

// 方法
const disabledDate = (time) => {
  return time.getTime() > Date.now()
}

// 表格筛选处理
const handleTableFilter = () => {
  // 筛选逻辑已在computed中处理
}

// 重置表格筛选
const resetTableFilter = () => {
  tableFilter.amountType = ''
  tableFilter.transactionType = ''
  tableFilter.marketplace = ''
}

const handleSearch = () => {
  searchLoading.value = true
  currentPage.value = 1
  getTransactionList()
    .finally(() => {
      searchLoading.value = false;
      searchForm.isload=true;
    })
}

const resetForm = () => {
  searchForm.accountId = ''
  searchForm.site = ''
  searchForm.currency = 'market'
  searchForm.datetype = 'settlement'
}

const getTransactionList = () => {
  loading.value = true
  // 调用后台接口
  return settlementApi.getMonthDetail(searchForm)
    .then(response => {
      if (response.code === 200) {
        transactionList.value = response.data || []
        total.value = response.data.length || 0
        renderChart()
        renderAmountChart()
      } else {
        ElMessage.error(response.msg || '获取数据失败')
      }
    })
    .catch(error => {
      ElMessage.error('网络错误，请稍后重试')
      console.error('Error:', error)
    })
    .finally(() => {
      loading.value = false
    })
}

const renderChart = () => {
  if (!chartRef.value) return

  chartLoading.value = true

  if (chartInstance.value) {
    chartInstance.value.dispose()
  }

  chartInstance.value = echarts.init(chartRef.value)

  // 统计交易类型分布
  const transactionTypeMap = new Map()
  transactionList.value.forEach(item => {
    const type = item.transaction_typename || item.transaction_type
    if (transactionTypeMap.has(type)) {
      transactionTypeMap.set(type, transactionTypeMap.get(type) + 1)
    } else {
      transactionTypeMap.set(type, 1)
    }
  })

  const chartData = Array.from(transactionTypeMap.entries()).map(([name, value]) => ({
    name,
    value
  }))

  const option = {
    tooltip: {
      trigger: 'item',
      formatter: '{a} <br/>{b}: {c} ({d}%)',
      backgroundColor: 'rgba(255, 255, 255, 0.95)',
      borderColor: '#ddd',
      borderWidth: 1,
      textStyle: {
        color: '#333',
        fontSize: '12px'
      },
      padding: [8, 12]
    },
    legend: {
      orient: 'vertical',
      left: 'left',
      data: chartData.map(item => item.name),
      type: 'scroll',
      height: 300,
      textStyle: {
        color: '#666'
      }
    },
    series: [
      {
        name: '交易类型',
        type: 'pie',
        radius: ['40%', '70%'],
        center: ['60%', '50%'],
        data: chartData,
        emphasis: {
          itemStyle: {
            shadowBlur: 15,
            shadowOffsetX: 0,
            shadowColor: 'rgba(0, 0, 0, 0.6)'
          },
          label: {
            show: true,
            fontSize: '12',
            fontWeight: 'normal'
          }
        },
        label: {
          formatter: '{b}: {d}%',
          fontSize: '12',
          color: '#666'
        },
        labelLine: {
          length: 10,
          length2: 20,
          lineStyle: {
            color: '#999'
          }
        },
        animationType: 'scale',
        animationEasing: 'elasticOut',
        animationDelay: function(idx) {
          return idx * 50
        }
      }
    ]
  }

  chartInstance.value.setOption(option)
  chartLoading.value = false

  // 响应式调整
  window.addEventListener('resize', () => {
    chartInstance.value?.resize()
  })
}

const renderAmountChart = () => {
  if (!amountChartRef.value) return

  chartLoading.value = true

  if (amountChartInstance.value) {
    amountChartInstance.value.dispose()
  }

  amountChartInstance.value = echarts.init(amountChartRef.value)

  // 统计金额类型分布
  const amountTypeMap = new Map()
  transactionList.value.forEach(item => {
    const type = item.amount_typename || item.amount_type
    const amount = Math.abs(item.amount)
    if (amountTypeMap.has(type)) {
      amountTypeMap.set(type, amountTypeMap.get(type) + amount)
    } else {
      amountTypeMap.set(type, amount)
    }
  })

  const chartData = Array.from(amountTypeMap.entries()).map(([name, value]) => ({
    name,
    value
  }))

  const option = {
    tooltip: {
      trigger: 'item',
      formatter: function(params) {
        return params.seriesName + ' <br/>' + params.name + ': ' + currencySymbol.value + params.value.toFixed(2) + ' (' + params.percent.toFixed(2) + '%)'
      },
      backgroundColor: 'rgba(255, 255, 255, 0.95)',
      borderColor: '#ddd',
      borderWidth: 1,
      textStyle: {
        color: '#333',
        fontSize: '12px'
      },
      padding: [8, 12]
    },
    legend: {
      orient: 'vertical',
      left: 'left',
      data: chartData.map(item => item.name),
      type: 'scroll',
      height: 300,
      textStyle: {
        color: '#666'
      }
    },
    series: [
      {
        name: '金额类型',
        type: 'pie',
        radius: ['40%', '70%'],
        center: ['60%', '50%'],
        data: chartData,
        emphasis: {
          itemStyle: {
            shadowBlur: 15,
            shadowOffsetX: 0,
            shadowColor: 'rgba(0, 0, 0, 0.6)'
          },
          label: {
            show: true,
            fontSize: '12',
            fontWeight: 'normal'
          }
        },
        label: {
          formatter: '{b}: {d}%',
          fontSize: '12',
          color: '#666'
        },
        labelLine: {
          length: 10,
          length2: 20,
          lineStyle: {
            color: '#999'
          }
        },
        animationType: 'scale',
        animationEasing: 'elasticOut',
        animationDelay: function(idx) {
          return idx * 50
        }
      }
    ]
  }

  amountChartInstance.value.setOption(option)
  chartLoading.value = false

  // 响应式调整
  window.addEventListener('resize', () => {
    amountChartInstance.value?.resize()
  })
}

function groupChange(obj){
  searchForm.groupid=obj.groupid;
  searchForm.marketplaceid=obj.marketplaceid;
  searchForm.marketplace_name=obj.market.pointName;
  searchForm.marketcurrency=obj.market.currency;
  currencySymbol.value = obj.market.currency === 'USD' ? '$' : '¥';
  handleSearch();
}
//日期改变
function changedate(dates){
  searchForm.fromDate=dates.start;
  searchForm.endDate=dates.end;
  if(searchForm.isload){
    handleSearch();
  }
}

const viewDetail = (row) => {
  detailLoading.value = true
  selectedTransaction.value = { ...row }
  setTimeout(() => {
    detailDialogVisible.value = true
    detailLoading.value = false
  }, 300)
}

const handleSizeChange = (size) => {
  pageSize.value = size
  getTransactionList()
}

const handleCurrentChange = (current) => {
  currentPage.value = current
  getTransactionList()
}



const formatPercentage = (value) => {
  if (value === null || value === undefined) return '0%'
  return (Number(value) * 100).toFixed(2) + '%'
}

const exportData = () => {
  if (transactionList.value.length === 0) {
    ElMessage.warning('暂无数据可导出')
    return
  }
  
  // 处理CSV中的特殊字符
  const escapeCsvValue = (value) => {
    if (value === null || value === undefined) return ''
    const str = String(value)
    if (str.includes(',') || str.includes('"') || str.includes('\n')) {
      return '"' + str.replace(/"/g, '""') + '"'
    }
    return str
  }
  
  // 导出字段
  const headers = ['金额类型', '交易类型', '金额', '货币', '金额描述', '原始描述', 'Marketplace', '是否无SKU', 'SKU', 'ASIN', '订单ID']
  
  // 生成CSV内容，添加BOM以确保中文显示正确
  const csvContent = [
    // BOM (Byte Order Mark) for UTF-8
    '\ufeff' + headers.join(','),
    ...transactionList.value.map(item => [
      escapeCsvValue(item.amount_typename || item.amount_type),
      escapeCsvValue(item.transaction_typename || item.transaction_type),
      escapeCsvValue(item.amount),
      escapeCsvValue(item.currency || ''),
      escapeCsvValue(item.amount_descriptionname || item.amount_description),
      escapeCsvValue(item.amount_description),
      escapeCsvValue(item.marketplace_name || ''),
      escapeCsvValue(item.isnosku === '1' ? '是' : '否'),
      escapeCsvValue(item.sku || ''),
      escapeCsvValue(item.asin || ''),
      escapeCsvValue(item.order_id || '')
    ].join(','))
  ].join('\n')
  
  // 生成文件名，包含筛选条件信息
  const generateFileName = () => {
    const date = new Date().toISOString().slice(0, 10)
    let fileName = '亚马逊支付汇总'
    
    if (searchForm.fromDate && searchForm.endDate) {
      fileName += `_${searchForm.fromDate}_${searchForm.endDate}`
    }
    
    if (searchForm.site) {
      fileName += `_${searchForm.site}`
    }
    
    fileName += `_${date}.csv`
    return fileName
  }
  
  try {
    const blob = new Blob([csvContent], { type: 'text/csv;charset=utf-8;' })
    const link = document.createElement('a')
    const url = URL.createObjectURL(blob)
    link.setAttribute('href', url)
    link.setAttribute('download', generateFileName())
    link.style.visibility = 'hidden'
    document.body.appendChild(link)
    link.click()
    document.body.removeChild(link)
    
    // 释放URL对象
    setTimeout(() => {
      URL.revokeObjectURL(url)
    }, 100)
    
    ElMessage.success(`导出成功，共导出 ${transactionList.value.length} 条记录`)
  } catch (error) {
    console.error('导出失败:', error)
    ElMessage.error('导出失败，请稍后重试')
  }
}

const initData = () => {
  // 初始加载数据
  // 注意：这里不再自动加载，而是等待用户选择条件后手动查询
}

// 假设这是从API获取的数据
const transactionData = reactive( {
    type: Array,
    default: () => [] // 初始为空，从父组件传入或API获取
  })

const rawData = ref(transactionData.length > 0 ? transactionData : [])
const selectedCurrency = ref('USD')
const activeTab = ref('transaction')



// 交易类型汇总
const transactionSummary = computed(() => {
  const groups = {}
  transactionList.value.forEach(item => {
    const type = item.transaction_typename || item.transaction_type
    if (!groups[type]) {
      groups[type] = {
        type: type,
        count: 0,
        amount: 0
      }
    }
    groups[type].count++
    groups[type].amount += Number(item.amount || 0)
  })

  const total = Math.abs(Object.values(groups).reduce((sum, item) => sum + Math.abs(item.amount), 0))

  return Object.values(groups).map(item => ({
    ...item,
    percentage: total > 0 ? Math.round((Math.abs(item.amount) / total) * 100) : 0
  })).sort((a, b) => Math.abs(b.amount) - Math.abs(a.amount))
})

// 费用类型汇总
const amountTypeSummary = computed(() => {
  const groups = {}
  transactionList.value.forEach(item => {
    const type = item.amount_typename || item.amount_type
    if (!groups[type]) {
      groups[type] = {
        type: type,
        count: 0,
        amount: 0
      }
    }
    groups[type].count++
    groups[type].amount += Number(item.amount || 0)
  })

  return Object.values(groups)
      .sort((a, b) => Math.abs(b.amount) - Math.abs(a.amount))
})

// SKU统计
const skuStats = computed(() => {
  console.log(transactionList.value);
  const withSku = transactionList.value.filter(item => item.isnosku === '0').length
  const withoutSku = transactionList.value.filter(item => item.isnosku === '1').length
  return {
    withSku,
    withoutSku,
    total: transactionList.value.length
  }
})

// 过滤后的数据
const filteredData = computed(() => {
  if (selectedCurrency.value === 'ALL') return transactionList.value
  return transactionList.value.filter(item => item.currency === selectedCurrency.value)
})

// 货币格式化
const formatCurrency = (value) => {
  const num = Number(value)
  return new Intl.NumberFormat('en-US', {
    style: 'currency',
    currency: 'USD',
    minimumFractionDigits: 2,
    maximumFractionDigits: 2
  }).format(num)
}



// 初始化ECharts图表
let skuWithChartInstance = null
let skuWithoutChartInstance = null
const skuWithChart = ref(null)
const skuWithoutChart = ref(null)

// 有SKU的费用类型分布
const skuWithAmountTypeSummary = computed(() => {
  const groups = {}
  transactionList.value.filter(item => item.isnosku === '0').forEach(item => {
    const type = item.amount_typename || item.amount_type
    if (!groups[type]) {
      groups[type] = {
        type: type,
        count: 0,
        amount: 0
      }
    }
    groups[type].count++
    groups[type].amount += Number(item.amount || 0)
  })

  return Object.values(groups)
      .sort((a, b) => Math.abs(b.amount) - Math.abs(a.amount))
})

// 无SKU的费用类型分布
const skuWithoutAmountTypeSummary = computed(() => {
  const groups = {}
  transactionList.value.filter(item => item.isnosku === '1').forEach(item => {
    const type = item.amount_typename || item.amount_type
    if (!groups[type]) {
      groups[type] = {
        type: type,
        count: 0,
        amount: 0
      }
    }
    groups[type].count++
    groups[type].amount += Number(item.amount || 0)
  })

  return Object.values(groups)
      .sort((a, b) => Math.abs(b.amount) - Math.abs(a.amount))
})

const initChart = () => {
  console.log('initChart called');
  console.log('skuWithChart.value:', skuWithChart.value);
  console.log('skuWithoutChart.value:', skuWithoutChart.value);

  // 初始化有SKU的费用类型分布图表
  if (skuWithChart.value) {
    // 确保DOM元素可见且有尺寸
    if (skuWithChart.value.offsetWidth === 0 || skuWithChart.value.offsetHeight === 0) {
      console.log('有SKU的费用类型分布图表容器尺寸为0，等待下次初始化');
      // 延迟一段时间后重新尝试初始化
      setTimeout(() => {
        if (skuWithChart.value && skuWithChart.value.offsetWidth > 0 && skuWithChart.value.offsetHeight > 0) {
          initChart()
        }
      }, 100);
      return
    }
    
    skuWithChartInstance = echarts.init(skuWithChart.value)

    const option = {
      tooltip: {
        trigger: 'item',
        formatter: '{a} <br/>{b}: {c} ({d}%)'
      },
      legend: {
        orient: 'vertical',
        left: 'left',
        data: skuWithAmountTypeSummary.value.map(item => item.type)
      },
      series: [
        {
          name: '费用类型',
          type: 'pie',
          radius: '50%',
          data: skuWithAmountTypeSummary.value.map(item => ({
            value: Math.abs(item.amount),
            name: item.type
          })),
          emphasis: {
            itemStyle: {
              shadowBlur: 10,
              shadowOffsetX: 0,
              shadowColor: 'rgba(0, 0, 0, 0.5)'
            }
          }
        }
      ]
    }

    skuWithChartInstance.setOption(option)
    console.log('有SKU的费用类型分布图表初始化成功');
  }

  // 初始化无SKU的费用类型分布图表
  if (skuWithoutChart.value) {
    // 确保DOM元素可见且有尺寸
    if (skuWithoutChart.value.offsetWidth === 0 || skuWithoutChart.value.offsetHeight === 0) {
      console.log('无SKU的费用类型分布图表容器尺寸为0，等待下次初始化');
      // 延迟一段时间后重新尝试初始化
      setTimeout(() => {
        if (skuWithoutChart.value && skuWithoutChart.value.offsetWidth > 0 && skuWithoutChart.value.offsetHeight > 0) {
          initChart()
        }
      }, 100);
      return
    }
    
    skuWithoutChartInstance = echarts.init(skuWithoutChart.value)

    const option = {
      tooltip: {
        trigger: 'item',
        formatter: '{a} <br/>{b}: {c} ({d}%)'
      },
      legend: {
        orient: 'vertical',
        left: 'left',
        data: skuWithoutAmountTypeSummary.value.map(item => item.type)
      },
      series: [
        {
          name: '费用类型',
          type: 'pie',
          radius: '50%',
          data: skuWithoutAmountTypeSummary.value.map(item => ({
            value: Math.abs(item.amount),
            name: item.type
          })),
          emphasis: {
            itemStyle: {
              shadowBlur: 10,
              shadowOffsetX: 0,
              shadowColor: 'rgba(0, 0, 0, 0.5)'
            }
          }
        }
      ]
    }

    skuWithoutChartInstance.setOption(option)
    console.log('无SKU的费用类型分布图表初始化成功');
  }
}



// 监听有SKU的费用类型分布变化更新图表
watch(skuWithAmountTypeSummary, () => {
  if (skuWithChartInstance) {
    initChart()
  }
}, { deep: true })

// 监听无SKU的费用类型分布变化更新图表
watch(skuWithoutAmountTypeSummary, () => {
  if (skuWithoutChartInstance) {
    initChart()
  }
}, { deep: true })

// 监听父组件传递的transactionData变化
watch(() => transactionData, (newData) => {
  console.log('父组件传递的transactionData变化:', newData);
  if (newData && newData.length > 0) {
    transactionList.value = newData
    initChart()
  }
}, { deep: true, immediate: true })

// 监听标签切换，当切换到SKU标签时初始化图表
watch(activeTab, (newTab) => {
  if (newTab === 'sku') {
    // 使用nextTick确保DOM已经更新
    nextTick(() => {
      initChart()
    })
  }
})

// 模拟数据（如果父组件没有传入数据）
if (transactionData.length === 0) {
  // 这里可以放置API调用
}
// 生命周期
onMounted(() => {
  initData()
  // 这里可以改为从API获取数据
  if (transactionData.length === 0) {
    // 模拟API调用获取数据
    // fetchTransactionData()
  } else {
    initChart()
  }

  // 监听窗口大小变化，重置图表大小
  window.addEventListener('resize', () => {
    if (skuWithChartInstance) {
      skuWithChartInstance.resize()
    }
    if (skuWithoutChartInstance) {
      skuWithoutChartInstance.resize()
    }
  })
})

// 监听交易数据变化
watch(() => transactionList.value, () => {
  renderChart()
  renderAmountChart()
  initChart()
}, { deep: true })

// 监听窗口大小变化
window.addEventListener('resize', () => {
  renderChart()
  renderAmountChart()
  if (skuWithChartInstance) {
    skuWithChartInstance.resize()
  }
  if (skuWithoutChartInstance) {
    skuWithoutChartInstance.resize()
  }
})
</script>

<style scoped>
.app-container {
  padding: 20px;
  min-height: 100vh;
  background-color: #f0f2f5;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.filter-container {
  margin-bottom: 20px;
  padding: 15px;
  background-color: #ffffff;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
  transition: all 0.3s ease;
}

.filter-container:hover {
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.stats-container {
  margin-bottom: 20px;
}

.stats-card {
  transition: all 0.3s ease;
  cursor: pointer;
}

.stats-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 16px rgba(0, 0, 0, 0.1);
}

.chart-row {
  display: flex;
  gap: 20px;
  margin-bottom: 20px;
  transition: all 0.3s ease;
}

.chart-container {
  flex: 1;
  min-width: 0;
  transition: all 0.3s ease;
}

.chart {
  width: 100%;
  height: 400px;
  transition: all 0.3s ease;
}

.table-container {
  margin-top: 20px;
}

.mb-20 {
  margin-bottom: 20px;
}

/* 进项和出项表格一行显示 */
.income-expense-row {
  display: flex;
  gap: 20px;
  margin-bottom: 20px;
  transition: all 0.3s ease;
}

.income-expense-col {
  flex: 1;
  min-width: 0;
  transition: all 0.3s ease;
  overflow: hidden;
}

.income-expense-col .el-card {
  display: flex;
  flex-direction: column;
}

.income-expense-col .el-table {
  flex: 1;
  overflow: auto;
}

/* 响应式调整 */
@media (max-width: 1200px) {
  .income-expense-row {
    flex-direction: column;
  }
  
  .income-expense-col {
    width: 100%;
  }
}

.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}

.profit-positive {
  color: #67c23a;
  transition: color 0.3s ease;
}

.profit-negative {
  color: #f56c6c;
  transition: color 0.3s ease;
}

.profit {
  color: #67c23a;
  transition: color 0.3s ease;
}

.detail-stats {
  margin-top: 20px;
}

.detail-stat-item {
  margin-bottom: 15px;
  padding: 10px;
  background-color: #f9f9f9;
  border-radius: 4px;
  transition: all 0.3s ease;
}

.detail-stat-item:hover {
  background-color: #f0f0f0;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
}

.stat-label {
  font-weight: bold;
  margin-right: 10px;
  color: #606266;
}

.stat-value {
  font-size: 16px;
  font-weight: 500;
}

.card-header-title {
  font-size: 16px;
  font-weight: bold;
  color: #303133;
}

.stats-value {
  font-size: 24px;
  font-weight: bold;
  text-align: center;
  margin-top: 10px;
  transition: all 0.3s ease;
}

/* 响应式布局 */
@media screen and (max-width: 1200px) {
  .stats-container {
    margin-bottom: 15px;
  }

  .stats-card {
    margin-bottom: 15px;
  }

  .chart {
    height: 350px;
  }
}

@media screen and (max-width: 768px) {
  .app-container {
    padding: 10px;
  }

  .filter-container {
    padding: 10px;
  }

  .stats-container {
    margin-bottom: 10px;
  }

  .chart {
    height: 300px;
  }

  .stats-value {
    font-size: 20px;
  }

  .detail-stat-item {
    margin-bottom: 10px;
    padding: 8px;
  }
  
  /* 表格筛选区域响应式调整 */
  .card-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 10px;
  }
  
  .card-header-actions {
    width: 100%;
  }
  
  .card-header-actions .el-space {
    width: 100%;
    flex-wrap: wrap;
  }
  
  .card-header-actions .el-select {
    flex: 1;
    min-width: 120px;
  }
  
  /* 表格响应式调整 */
  .el-table {
    font-size: 12px;
  }
  
  .el-table th {
    padding: 8px;
    font-size: 12px;
  }
  
  .el-table td {
    padding: 8px;
  }
  
  /* 表格列宽响应式调整 */
  .el-table-column[prop="amount_type"] {
    width: 140px !important;
  }
  
  .el-table-column[prop="transaction_type"] {
    width: 120px !important;
  }
  
  .el-table-column[prop="amount"] {
    width: 120px !important;
  }
  
  .el-table-column[prop="marketplace_name"] {
    width: 120px !important;
  }
  
  /* 操作列响应式调整 */
  .el-table-column:last-child {
    width: 100px !important;
  }
  
  .el-table-column:last-child .el-button {
    padding: 4px 8px;
    font-size: 11px;
  }
  
  /* 分页区域响应式调整 */
  .pagination-container {
    justify-content: center;
  }
  
  .el-pagination {
    font-size: 12px;
  }
  
  .el-pagination__sizes {
    display: none;
  }
  
  /* 图表容器响应式调整 */
  .chart-row {
    flex-direction: column;
  }
  
  .chart-container {
    margin-bottom: 20px;
  }
  
  /* 筛选条件区域响应式调整 */
  .filter-container .el-space {
    flex-direction: column;
    align-items: stretch;
  }
  
  .filter-container .el-select,
  .filter-container .el-button {
    width: 100%;
  }
  
  .date-picker-group {
    width: 100%;
    display: flex;
    gap: 10px;
  }
  
  .date-picker-group .el-select {
    flex: 1;
  }
}

/* 平板设备响应式调整 (769px - 1024px) */
@media screen and (min-width: 769px) and (max-width: 1024px) {
  .app-container {
    padding: 15px;
  }
  
  /* 表格筛选区域调整 */
  .card-header-actions {
    flex-wrap: wrap;
    gap: 10px;
  }
  
  .card-header-actions .el-select {
    min-width: 140px;
  }
  
  /* 图表高度调整 */
  .chart {
    height: 320px;
  }
  
  /* 统计卡片调整 */
  .stats-card .el-card__body {
    padding: 16px;
  }
}

/* 大屏设备响应式调整 (1025px - 1440px) */
@media screen and (min-width: 1025px) and (max-width: 1440px) {
  /* 图表容器宽度调整 */
  .chart-container {
    flex: 1;
  }
  
  /* 表格列宽调整 */
  .el-table-column[prop="amount_type"] {
    width: 160px !important;
  }
  
  .el-table-column[prop="transaction_type"] {
    width: 130px !important;
  }
}

/* 超小屏幕设备响应式调整 (<= 480px) */
@media screen and (max-width: 480px) {
  .app-container {
    padding: 8px;
  }
  
  /* 页面标题调整 */
  .page-header h2 {
    font-size: 18px;
  }
  
  .page-description {
    font-size: 12px;
  }
  
  /* 筛选条件区域调整 */
  .filter-container {
    padding: 8px;
  }
  
  /* 统计卡片调整 */
  .stats-card .el-card__body {
    padding: 10px;
  }
  
  .card-header-title {
    font-size: 12px;
  }
  
  .stats-value {
    font-size: 18px;
  }
  
  .stats-subtitle {
    font-size: 11px;
  }
  
  /* 图表高度调整 */
  .chart {
    height: 250px;
  }
  
  /* 表格调整 */
  .el-table {
    font-size: 11px;
  }
  
  .el-table th,
  .el-table td {
    padding: 6px;
  }
  
  /* 对话框调整 */
  .el-dialog {
    width: 95% !important;
  }
  
  .el-dialog__title {
    font-size: 16px;
  }
}

/* 动画效果 */
@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.app-container {
  animation: fadeIn 0.5s ease-in-out;
}

.chart-container, .table-container {
  animation: fadeIn 0.6s ease-in-out;
}

/* 表格样式优化 */
.el-table {
  border-radius: 8px;
  overflow: hidden;
}

.el-table th {
  background-color: #f5f7fa;
  font-weight: bold;
}

.el-table tr:hover {
  background-color: #f9f9f9;
}

/* 按钮样式优化 */
.el-button {
  border-radius: 4px;
  transition: all 0.3s ease;
}

.el-button:hover {
  transform: translateY(-1px);
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

/* 对话框样式优化 */
.el-dialog {
  border-radius: 8px;
  overflow: hidden;
}

.el-dialog__header {
  background-color: #f5f7fa;
  padding: 15px 20px;
}

.el-dialog__title {
  font-size: 18px;
  font-weight: bold;
}

.dialog-footer {
  display: flex;
  justify-content: center;
  padding: 15px;
}

/* 统计卡片增强样式 */
.card-header-title {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 16px;
  font-weight: bold;
  color: #303133;
  transition: all 0.3s ease;
}

/* 趋势标签样式 */
.trend-tag {
  font-size: 12px;
  padding: 2px 8px;
  border-radius: 10px;
}

/* 统计卡片副标题样式 */
.stats-subtitle {
  text-align: center;
  margin-top: 10px;
  font-size: 14px;
  color: #909399;
  transition: all 0.3s ease;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 5px;
}

/* 统计变化值样式 */
.stats-change {
  font-size: 12px;
  font-weight: 500;
  transition: all 0.3s ease;
}

.stats-change.positive {
  color: #67c23a;
}

.stats-change.negative {
  color: #f56c6c;
}

/* 统计卡片悬停效果增强 */
.stats-card:hover .stats-value {
  transform: scale(1.05);
  transition: transform 0.3s ease;
}

/* 统计卡片内容布局 */
.stats-card .el-card__body {
  padding: 20px;
  transition: all 0.3s ease;
}

/* 动画效果 */
@keyframes slideUp {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.animated-card {
  animation: slideUp 0.6s ease-out;
}

/* 响应式调整统计卡片 */
@media screen and (max-width: 1200px) {
  .stats-card .el-card__body {
    padding: 15px;
  }
  
  .card-header-title {
    font-size: 14px;
  }
  
  .trend-tag {
    font-size: 11px;
    padding: 1px 6px;
  }
}

@media screen and (max-width: 768px) {
  .stats-card .el-card__body {
    padding: 12px;
  }
  
  .card-header-title {
    font-size: 13px;
    flex-direction: column;
    align-items: flex-start;
    gap: 5px;
  }
  
  .trend-tag {
    align-self: flex-start;
  }
  
  .stats-subtitle {
    font-size: 12px;
  }
  
  .stats-change {
    font-size: 11px;
  }
}
</style>