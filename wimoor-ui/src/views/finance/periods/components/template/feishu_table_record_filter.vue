<template>
  <div class="feishu-table-filter">
    <!-- 默认显示的预览区域 -->
    <div class="preview-section">
      <div class="preview-header">
        <div class="header-left">
          <label class="form-label">筛选条件</label>
        </div>
        <div class="header-right">
          <el-button type="primary" size="small" icon="plus" @click="openEditDialog">
            添加条件
          </el-button>
        </div>
      </div>

      <!-- 条件模式预览 -->
      <div class="filter-preview">
        <div v-if="validConditions.length === 0" class="empty-preview">
          <span>暂无筛选条件</span>
        </div>
        <div v-else class="filter-content">
          <template v-for="(condition, index) in validConditions" :key="index">
            <div class="condition-tag">
              <span class="field-name">{{ condition.field_name }}</span>
              <span class="operator">{{ getOperatorText(condition.operator) }}</span>
              <span class="value" v-if="condition.value && condition.value.length > 0">
                {{ condition.value.join(', ') }}
              </span>
              <span class="value" v-else-if="condition.operator === 'isEmpty'">空</span>
              <span class="value" v-else-if="condition.operator === 'isNotEmpty'">非空</span>
            </div>
            <span class="conjunction-text" v-if="index < validConditions.length - 1">
              {{ filter.conjunction === 'and' ? '并且' : '或者' }}
            </span>
          </template>
        </div>
      </div>

      <!-- JSON 查看区域 -->
      <div class="json-toggle-section">
        <div class="flex-between">
          <el-button type="text" size="small" @click="showJson = !showJson">
            {{ showJson ? '隐藏 JSON' : '查看 JSON' }}
          </el-button>
          <el-button type="primary" link size="small" @click="handleSearch">查询</el-button>
        </div>

        <pre v-if="showJson" class="json-preview">{{ JSON.stringify(generateFilterObject(), null, 2) }}</pre>
      </div>
    </div>

    <!-- 编辑条件对话框 -->
    <el-dialog
      v-model="editDialogVisible"
      title="编辑筛选条件"
      width="700px"
      :before-close="handleDialogClose"
    >
      <!-- 逻辑连接词选择 -->
      <div class="conjunction-section">
        <label class="form-label">逻辑连接词</label>
        <el-select v-model="filter.conjunction" placeholder="请选择逻辑连接词" class="conjunction-select">
          <el-option label="AND (满足全部条件)" value="and" />
          <el-option label="OR (满足任一条件)" value="or" />
        </el-select>
      </div>

      <!-- 条件列表 -->
      <div class="conditions-section">
        <label class="form-label">筛选条件</label>
        <div v-for="(condition, index) in filter.conditions" :key="index" class="condition-row">
          <div class="condition-item field-item">
            <el-select
              v-model="condition.field_name"
              placeholder="请选择字段"
              class="field-select"
            >
              <el-option
                v-for="field in availableFields"
                :key="field.field_name"
                :label="field.field_name"
                :value="field.field_name"
              />
            </el-select>
          </div>
          <div class="condition-item operator-item">
            <el-select v-model="condition.operator" placeholder="运算符" class="operator-select">
              <el-option label="等于 (is)" value="is" />
              <el-option label="不等于 (isNot)" value="isNot" />
              <el-option label="包含 (contains)" value="contains" />
              <el-option label="不包含 (doesNotContain)" value="doesNotContain" />
              <el-option label="为空 (isEmpty)" value="isEmpty" />
              <el-option label="不为空 (isNotEmpty)" value="isNotEmpty" />
              <el-option label="大于 (isGreater)" value="isGreater" />
              <el-option label="大于等于 (isGreaterEqual)" value="isGreaterEqual" />
              <el-option label="小于 (isLess)" value="isLess" />
              <el-option label="小于等于 (isLessEqual)" value="isLessEqual" />
            </el-select>
          </div>
          <div class="condition-item value-item" v-if="condition.operator !== 'isEmpty' && condition.operator !== 'isNotEmpty'">
            <el-input
              v-model="condition.valueInput"
              placeholder="条件值（多个值用逗号分隔）"
              class="value-input"
              @blur="updateValue(index)"
            />
          </div>
          <div class="condition-item action-item">
            <el-button
              type="danger"
              size="small"
              icon="delete"
              @click="removeCondition(index)"
            />
          </div>
        </div>
        <el-button
          type="primary"
          size="small"
          icon="plus"
          @click="addCondition"
          class="add-btn"
        >
          添加条件
        </el-button>
      </div>

      <template #footer>
        <span class="dialog-footer">
          <el-button @click="editDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="confirmFilter">确认应用</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, watch, computed, onMounted } from 'vue';
import { ElMessage } from 'element-plus';

const props = defineProps({
  fields: {
    type: Array,
    default: () => []
  },
  modelValue: {
    type: Object,
    default: () => null
  }
});

const emit = defineEmits(['update:modelValue', 'filter-generated', 'search']);

// 可用字段列表
const availableFields = computed(() => {
  return props.fields || [];
});

// JSON 显示状态
const showJson = ref(false);

// 编辑对话框状态
const editDialogVisible = ref(false);

// 运算符中文映射
const operatorMap = {
  'is': '=',
  'isNot': '!=',
  'contains': 'like',
  'doesNotContain': 'not like',
  'isEmpty': 'is null',
  'isNotEmpty': 'is not null',
  'isGreater': '>',
  'isGreaterEqual': '>=',
  'isLess': '<',
  'isLessEqual': '<='
};

// 初始化 filter 对象
const filter = reactive({
  conjunction: 'and',
  conditions: [
    {
      field_name: '',
      operator: 'is',
      value: [],
      valueInput: ''
    }
  ]
});

// 有效的条件列表（用于预览）
const validConditions = computed(() => {
  return filter.conditions.filter(c => c.field_name && c.operator);
});

// 监听 operator 变化，如果是 isEmpty 或 isNotEmpty，清空 value
watch(() => filter.conditions, (conditions) => {
  conditions.forEach((condition) => {
    if (condition.operator === 'isEmpty' || condition.operator === 'isNotEmpty') {
      condition.value = [];
      condition.valueInput = '';
    }
  });
}, { deep: true });

// 监听外部传入的 modelValue 变化，更新内部 filter
watch(() => props.modelValue, (newValue) => {
    updateFilterFromModel(newValue);
}, { immediate: true });

// 从外部 modelValue 更新内部 filter
function updateFilterFromModel(model) {
  if (!model) {
    return;
  }else{
    filter.conjunction='and';
    filter.conditions=[
    {
      field_name: '',
      operator: 'is',
      value: [],
      valueInput: ''
    }
  ];

  }

  // 兼容多种格式：带 mode 的格式和不带 mode 的格式
  if (model.conjunction) {
    filter.conjunction = model.conjunction;
  }
  if (model.conditions && Array.isArray(model.conditions)) {
    filter.conditions = model.conditions.map(c => ({
      field_name: c.field_name || '',
      operator: c.operator || 'is',
      value: c.value || [],
      valueInput: Array.isArray(c.value) ? c.value.join(',') : ''
    }));
  }
}

// 获取运算符中文描述
function getOperatorText(operator) {
  return operatorMap[operator] || operator;
}

// 生成 filter 对象（用于 JSON 显示和发送到后端）
function generateFilterObject() {
  return {
    conjunction: filter.conjunction,
    conditions: validConditions.value.map(c => ({
      field_name: c.field_name,
      operator: c.operator,
      ...(c.value && c.value.length > 0 ? { value: c.value } : {})
    }))
  };
}

// 打开编辑对话框
function openEditDialog() {
  editDialogVisible.value = true;
}
function handleSearch(){
  emit('search');
}
// 关闭对话框
function handleDialogClose() {
  editDialogVisible.value = false;
}

// 添加条件
function addCondition() {
  filter.conditions.push({
    field_name: '',
    operator: 'is',
    value: [],
    valueInput: ''
  });
}

// 移除条件
function removeCondition(index) {
  filter.conditions.splice(index, 1);
}

// 更新 value（支持逗号分隔多个值）
function updateValue(index) {
  const condition = filter.conditions[index];
  if (condition.valueInput) {
    condition.value = condition.valueInput.split(',').map(v => v.trim()).filter(v => v);
  } else {
    condition.value = [];
  }
}

// 确认应用 filter
function confirmFilter() {
  // 条件模式验证
  for (let i = 0; i < filter.conditions.length; i++) {
    const condition = filter.conditions[i];
    if (!condition.field_name) {
      ElMessage.warning(`第 ${i + 1} 个条件的字段名称不能为空`);
      return;
    }
    if (!condition.operator) {
      ElMessage.warning(`第 ${i + 1} 个条件的运算符不能为空`);
      return;
    }
    // 对于非空值的操作符，检查值是否填写
    if (condition.operator !== 'isEmpty' && condition.operator !== 'isNotEmpty') {
      if (!condition.value || condition.value.length === 0) {
        ElMessage.warning(`第 ${i + 1} 个条件的值不能为空`);
        return;
      }
    }
  }

  // 构建最终的 filter 对象
  const finalFilter = generateFilterObject();

  // 更新 modelValue
  emit('update:modelValue', finalFilter);
  emit('filter-generated', finalFilter);

  editDialogVisible.value = false;
  ElMessage.success('筛选条件已应用');
}

// 初始化时如果有 modelValue，更新内部状态
onMounted(() => {
  if (props.modelValue) {
    updateFilterFromModel(props.modelValue);
  }
});
</script>

<style scoped>
.feishu-table-filter {
  padding: 10px;
}

.preview-section {
  background: #f8fafc;
 border: 1px solid #e2e8f0;
  border-radius: 8px;
  padding: 16px;
}

.preview-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
}

.form-label {
  font-weight: 600;
  color: #334155;
  font-size: 14px;
}

.filter-preview {
  min-height: 40px;
}

.empty-preview {
  text-align: center;
  color: #94a3b8;
  padding: 20px;
}

.filter-content {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  gap: 8px;
}

.condition-tag {
  display: inline-flex;
  align-items: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  padding: 6px 12px;
  border-radius: 20px;
  font-size: 13px;
  gap: 6px;
}

.condition-tag .field-name {
  font-weight: 600;
}

.condition-tag .operator {
  background: rgba(255, 255, 255, 0.2);
  padding: 2px 8px;
  border-radius: 10px;
  font-size: 12px;
}

.condition-tag .value {
  background: rgba(255, 255, 255, 0.3);
  padding: 2px 8px;
  border-radius: 10px;
  font-size: 12px;
}

.conjunction-text {
  color: #64748b;
  font-weight: 500;
  font-size: 14px;
  margin: 0 4px;
}

.json-toggle-section {
  margin-top: 16px;
  padding-top: 16px;
  border-top: 1px dashed #cbd5e1;
}

.json-preview {
  background: #1e293b;
  color: #e2e8f0;
  padding: 15px;
  border-radius: 4px;
  font-size: 12px;
  max-height: 200px;
  overflow-y: auto;
  word-break: break-all;
  font-family: 'Consolas', 'Monaco', monospace;
}

/* 对话框内样式 */
.conjunction-section {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
}

.conjunction-select {
  width: 200px;
}

.conditions-section {
  margin-bottom: 20px;
}

.condition-row {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 10px;
  flex-wrap: wrap;
}

.condition-item {
  flex: 1;
  min-width: 120px;
}

.field-select {
  width: 100%;
}

.operator-select {
  width: 100%;
}

.value-input {
  width: 100%;
}

.add-btn {
  margin-top: 10px;
}

.dialog-footer {
  text-align: right;
}
</style>