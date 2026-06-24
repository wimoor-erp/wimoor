<template>
  <el-dialog title="自定义计算规则列表" v-model="dialogVisible" width="1200px" style="height: 700px;" >
    <el-table :data="customRules" stripe style="width: 100%" height="550">
      <el-table-column prop="code" label="规则代码" width="180"></el-table-column>
      <el-table-column prop="name" label="规则名称" width="150"></el-table-column>
      <el-table-column prop="description" label="规则描述" width="200"></el-table-column>
      <el-table-column prop="formula" label="计算公式"></el-table-column>
    </el-table>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="dialogVisible = false">关闭</el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup name="TemplateItemCustomDialog">
import { ref, reactive, defineExpose } from 'vue';

// 对话框显示状态
const dialogVisible = ref(false)

// 自定义计算规则列表
const customRules = reactive([
  // 资产负债表相关规则
  { code: 'ASSET_CURRENT', name: '流动资产合计', description: '流动资产合计', formula: 'ASSET_CASH + ASSET_RECEIVABLE + ASSET_INVENTORY + ASSET_OTHER_CURRENT' },
  { code: 'ASSET_CASH', name: '货币资金', description: '货币资金（现金+银行存款+其他货币资金）', formula: '1001 + 1002 + 1012' },
  { code: 'ASSET_RECEIVABLE', name: '应收款项', description: '应收票据+应收账款+预付账款+其他应收款', formula: '1121 + 1122 + 1123 + 1221' },
  { code: 'ASSET_INVENTORY', name: '存货', description: '库存商品+原材料+在途物资+生产成本', formula: '1405 + 1403 + 1402 + 5001' },
  { code: 'ASSET_OTHER_CURRENT', name: '其他流动资产', description: '其他流动资产', formula: '1471' },
  { code: 'ASSET_NON_CURRENT', name: '非流动资产合计', description: '非流动资产合计', formula: 'ASSET_LONG_INVEST + ASSET_FIXED + ASSET_INTANGIBLE + ASSET_OTHER_NON_CURRENT' },
  { code: 'ASSET_LONG_INVEST', name: '长期投资', description: '长期股权投资+持有至到期投资', formula: '1511 + 1501' },
  { code: 'ASSET_FIXED', name: '固定资产', description: '固定资产原价-累计折旧+在建工程', formula: '1601 - 1602 + 1604' },
  { code: 'ASSET_INTANGIBLE', name: '无形资产', description: '无形资产-累计摊销', formula: '1701 - 1702' },
  { code: 'ASSET_OTHER_NON_CURRENT', name: '其他非流动资产', description: '其他非流动资产', formula: '1901' },

  // 负债相关规则
  { code: 'LIABILITY_CURRENT', name: '流动负债合计', description: '流动负债合计', formula: 'LIABILITY_SHORT_LOAN + LIABILITY_PAYABLE + LIABILITY_TAX + LIABILITY_OTHER_CURRENT' },
  { code: 'LIABILITY_SHORT_LOAN', name: '短期借款', description: '短期借款', formula: '2001' },
  { code: 'LIABILITY_PAYABLE', name: '应付款项', description: '应付票据+应付账款+预收账款+其他应付款', formula: '2201 + 2202 + 2203 + 2241' },
  { code: 'LIABILITY_TAX', name: '应交税费', description: '应交税费', formula: '2221' },
  { code: 'LIABILITY_OTHER_CURRENT', name: '其他流动负债', description: '其他流动负债', formula: '2401' },
  { code: 'LIABILITY_NON_CURRENT', name: '非流动负债合计', description: '非流动负债合计', formula: 'LIABILITY_LONG_LOAN + LIABILITY_BOND + LIABILITY_OTHER_NON_CURRENT' },
  { code: 'LIABILITY_LONG_LOAN', name: '长期借款', description: '长期借款', formula: '2501' },
  { code: 'LIABILITY_BOND', name: '应付债券', description: '应付债券', formula: '2502' },
  { code: 'LIABILITY_OTHER_NON_CURRENT', name: '其他非流动负债', description: '其他非流动负债', formula: '2701' },

  // 所有者权益相关规则
  { code: 'EQUITY_TOTAL', name: '所有者权益合计', description: '所有者权益合计', formula: 'EQUITY_CAPITAL + EQUITY_SURPLUS + EQUITY_RETAINED + EQUITY_OTHER' },
  { code: 'EQUITY_CAPITAL', name: '实收资本', description: '实收资本', formula: '4001' },
  { code: 'EQUITY_SURPLUS', name: '资本公积', description: '资本公积', formula: '4002' },
  { code: 'EQUITY_RETAINED', name: '盈余公积', description: '盈余公积', formula: '4101' },
  { code: 'EQUITY_OTHER', name: '其他权益工具', description: '其他权益工具', formula: '4003' },

  // 利润表相关规则
  { code: 'REVENUE_TOTAL', name: '营业收入', description: '主营业务收入+其他业务收入', formula: '6001 + 6051' },
  { code: 'COST_TOTAL', name: '营业成本', description: '主营业务成本+其他业务成本', formula: '6401 + 6402' },
  { code: 'PROFIT_OPERATING', name: '营业利润', description: '营业收入-营业成本-税金及附加-销售费用-管理费用-财务费用', formula: 'REVENUE_TOTAL - COST_TOTAL - 6403 - 6601 - 6602 - 6603' },
  { code: 'PROFIT_TOTAL', name: '利润总额', description: '营业利润+营业外收入-营业外支出', formula: 'PROFIT_OPERATING + 6301 - 6711' },
  { code: 'PROFIT_NET', name: '净利润', description: '利润总额-所得税费用', formula: 'PROFIT_TOTAL - 6801' },

  // 每股收益相关规则
  { code: 'EPS_BASIC', name: '基本每股收益', description: '基本每股收益', formula: 'PROFIT_NET_PARENT / SHARE_BASIC' },
  { code: 'EPS_DILUTED', name: '稀释每股收益', description: '稀释每股收益', formula: '(PROFIT_NET_PARENT + DILUTION_ADJUSTMENT) / (SHARE_BASIC + DILUTION_SHARES)' },
  { code: 'PROFIT_NET_PARENT', name: '归属于母公司净利润', description: '归属于母公司所有者的净利润', formula: 'PROFIT_NET - 6901' },
  { code: 'DILUTION_ADJUSTMENT', name: '稀释调整额', description: '稀释调整额（如可转换债券利息）', formula: '0' },
  { code: 'SHARE_BASIC', name: '基本股数', description: '基本股数', formula: '0' },
  { code: 'DILUTION_SHARES', name: '稀释股数', description: '稀释股数', formula: '0' },

  // 增值税相关规则
  { code: 'TAX_VAT_INPUT', name: '进项税额', description: '进项税额', formula: '22210101' },
  { code: 'TAX_VAT_OUTPUT', name: '销项税额', description: '销项税额', formula: '22210102' },
  { code: 'TAX_VAT_PAYABLE', name: '应缴增值税', description: '应缴增值税（销项-进项）', formula: 'TAX_VAT_OUTPUT - TAX_VAT_INPUT' }
])

// 显示对话框
const show = () => {
  dialogVisible.value = true
}

// 关闭对话框
const hide = () => {
  dialogVisible.value = false
}

// 暴露方法给父组件
defineExpose({
  show,
  hide
})
</script>

<style scoped lang="scss">
/* 组件样式 */
</style>