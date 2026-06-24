<template>
  <el-dialog title="结账凭证模板设置" v-model="visible" top="2vh" width="80%">
    <el-row>
      <el-col :span="6">
        <TemplateTree ref="templateTreeRef" @node-click="handleNodeClick" @change="handleTemplateChange"></TemplateTree>
      </el-col>
      <el-col :span="18">
        <div class="template-content">
          <component ref="currentComponentRef" 
          :is="currentTemplateComponent" 
          v-if="selectedTemplate" 
          :selectedTemplate="selectedTemplate" />
          <div v-else class="empty-template">
            <el-empty description="请选择左侧模板" />
          </div>
        </div>
      </el-col>
    </el-row>
    <template #footer>
      <el-space>
        <el-button type="primary" @click="visible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </el-space>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, computed, nextTick } from 'vue';
import TemplateTree from './template-tree.vue';
import AmazonFinMonthReport from './template/amazon_fin_month_report.vue';
import EndPeriodAdjustment from './template/period_change_rate.vue';
import Feishu from './template/feishu_table_record.vue';
import OtherCustom from './template/other_custom.vue';
import PeriodProfitLossTransfer from './template/period_profit_loss_transfer.vue';
const emit = defineEmits(['change']);
const visible = ref(false);
const templateTreeRef = ref();
const selectedTemplate = ref(null);
const currentComponentRef = ref(null);

// 根据模板类型计算当前应显示的组件
const currentTemplateComponent = computed(() => {
  if (!selectedTemplate.value) return null;
  
  // 根据模板类型返回对应的组件
  // 假设模板类型字段为type，1表示亚马逊月度营业数据，2表示其他自定义
  const templateName = selectedTemplate.value.ftype;
  
  switch (templateName) {
    case "amzpayment":
      return AmazonFinMonthReport;
    case "fct":
      return EndPeriodAdjustment;
    case "feishu":
      return Feishu;
    case "loss":
      return PeriodProfitLossTransfer;
    default:
      return OtherCustom;
  }
});

function handleSubmit() {
  // 调用当前组件的批量保存方法
  if (currentComponentRef.value && currentComponentRef.value.handleBatchSave) {
    currentComponentRef.value.handleBatchSave().then(() => {
      // 重新加载模板列表，确保selectedTemplate包含最新数据
      if (selectedTemplate.value) {
        templateTreeRef.value.show(selectedTemplate.value);
      }
      emit('change', selectedTemplate.value);
      visible.value = false;
    });
  } else {
    // 如果组件没有批量保存方法，直接关闭对话框
    visible.value = false;
  }
}

function show(template) {
  visible.value = true;
  nextTick(() => {
    templateTreeRef.value.show(template);
    if(template){
      selectedTemplate.value = template;
      }
  })
}

// 显示模板框并选中指定模板
function showWithTemplate(template) {
  visible.value = true;
  nextTick(() => {
    templateTreeRef.value.show(template);
    // 选中指定模板
    selectedTemplate.value = template;
  })
}

// 处理树节点点击
function handleNodeClick(data) {
  selectedTemplate.value = data;
  // 当选择模板时，加载该模板已有的数据
  if (data) {
    setTimeout(() => {
      if (currentComponentRef.value && currentComponentRef.value.loadExistingTemplateItems) {
        currentComponentRef.value.loadExistingTemplateItems();
      }
    }, 100);
  }
}

// 处理模板变更事件（新增/删除后刷新）
function handleTemplateChange() {
  emit('change');
}

defineExpose({
  show,
})
</script>

<style scoped>
.template-content {
  max-height: 800px;
  overflow-y: auto;
  padding: 10px;
  border-left: 1px solid #e4e7ed;
}

.empty-template {
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
}
</style>