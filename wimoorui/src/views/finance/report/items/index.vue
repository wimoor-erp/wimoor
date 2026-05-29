<template>
    <div class="app-container">
      <el-tabs v-model="activeName" class="rpt-tabs" @tab-click="handleClick">
        <el-tab-pane  v-for="tab in tabs" :label="tab.label" :name="tab.name">
        </el-tab-pane>
      </el-tabs>
      <report-sheet :type="activeName"></report-sheet>
 
    </div>
</template>

<script setup name="Items">
import { listItems, getItems, delItems, addItems, updateItems } from "@/api/finance/report_items.js"
import ReportSheet from './components/report_sheet.vue'
import { listTemplates, addTemplates, updateTemplates, delTemplates } from "@/api/finance/report_templates.js"
const { proxy } = getCurrentInstance()
import finStore from "@/hooks/store/useFinanceStore.js"
const { formula_type } = proxy.useDict('formula_type')
const state = reactive({
    activeName: 'BALANCE_SHEET_STANDARD',
    tabs: []
 })
const { activeName,tabs } = toRefs(state);
  const handleClick = (tab) => {
    console.log(tab)
    state.activeName = tab.name
  }
// 加载模板列表
async function getTemplateList() {
  let queryParams = {
    pageNum: 1,
    pageSize: 1000,
    groupid: await finStore.getCurrentTenantId()
  }
  listTemplates(queryParams).then(response => {
    // 将后台返回数据转换为树形结构
    const templates = response.rows.map(item => ({
      templateId: item.templateId,
      name: item.templateCode,
      label: item.templateName,
    }))
    state.tabs= templates
    state.activeName=templates[0].name;

  })
}
getTemplateList()
</script>
<style scoped>
.rpt-tabs {
    margin-top: -15px;
}
</style>
