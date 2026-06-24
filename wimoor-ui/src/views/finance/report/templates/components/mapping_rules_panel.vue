<template>
  <div class="mapping-rules-panel">
    <!-- 工具栏 -->
    <el-row :gutter="10" class="mb-3">
      <el-col :span="16">
        <el-form :inline="true" :model="queryParams" class="search-form">
          <el-form-item label="项目编码">
            <el-input v-model="queryParams.itemCode" placeholder="项目编码" clearable style="width: 150px" />
          </el-form-item>
          <el-form-item label="匹配类型">
            <el-select v-model="queryParams.matchType" placeholder="匹配类型" clearable style="width: 120px">
              <el-option label="前缀匹配" value="PREFIX" />
              <el-option label="精确匹配" value="EXACT" />
              <el-option label="范围匹配" value="RANGE" />
              <el-option label="父级匹配" value="PARENT" />
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="handleQuery" >查询</el-button>
            <el-button @click="resetQuery">重置</el-button>
          </el-form-item>
        </el-form>
      </el-col>
      <el-col :span="8" class="text-right">
        <el-button type="warning" icon="Refresh" @click="handleSyncFormula" :loading="syncLoading" >
          同步公式
        </el-button>
        <el-button type="success" icon="Plus" @click="handleAdd" >
          新增规则
        </el-button>
        <el-button type="primary" icon="Download" @click="handleInitDefault" >
          初始化默认
        </el-button>

           <Helper name="报表模版" />
      </el-col>
    </el-row>

    <!-- 规则列表 -->
    <el-table :data="rulesList" v-loading="loading" border stripe  style="height:calc(100vh - 250px)" >
      <el-table-column label="规则类型" prop="ruleType" width="100">
        <template #default="{ row }">
          <el-tag :type="row.ruleType === 'SUBJECT' ? 'primary' : row.ruleType === 'ITEM_SUM' ? 'success' : 'warning'" >
            {{ getRuleTypeLabel(row.ruleType) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="项目编码" prop="itemCode"  show-overflow-tooltip />
      <el-table-column label="规则名称" prop="ruleName"  show-overflow-tooltip />
      <el-table-column label="匹配类型" prop="matchType" width="100">
        <template #default="{ row }">
          {{ getMatchTypeLabel(row.matchType) }}
        </template>
      </el-table-column>
      <el-table-column label="匹配值" prop="matchValue" width="120" show-overflow-tooltip />
      <el-table-column label="操作符" prop="operator" width="80">
        <template #default="{ row }">
          <el-tag :type="row.operator === 'ADD' ? 'success' : 'danger'" >
            {{ row.operator === 'ADD' ? '加' : '减' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="科目类型" prop="subjectType" width="80">
        <template #default="{ row }">
          {{ getSubjectTypeLabel(row.subjectType) }}
        </template>
      </el-table-column>
      <el-table-column label="优先级" prop="priority" width="70" />
      <el-table-column label="状态" prop="status" width="70">
        <template #default="{ row }">
          <el-tag :type="row.status === 1 ? 'success' : 'danger'"  >
            {{ row.status === 1 ? '启用' : '停用' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="180" fixed="right">
        <template #default="{ row }">
          <el-button type="text" size="small" @click="handleEdit(row)">编辑</el-button>
          <el-button type="text" size="small" @click="handleDelete(row)" style="color: #ff4d4f">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <pagination
      v-show="total > 0"
      :total="total"
      v-model:page="queryParams.pageNum"
      v-model:limit="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 新增/编辑对话框 -->
    <el-dialog :title="dialogTitle" v-model="dialogVisible" width="500px" append-to-body>
      <el-form ref="ruleFormRef" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="模板类型" prop="templateType">
          <el-select v-model="form.templateType" placeholder="请选择模板类型">
            <el-option label="资产负债表" value="BALANCE_SHEET" />
            <el-option label="利润表" value="INCOME_STATEMENT" />
            <el-option label="现金流量表" value="CASH_FLOW" />
            <el-option label="所有者权益变动表" value="EQUITY_CHANGE" />
          </el-select>
        </el-form-item>
        <el-form-item label="规则类型" prop="ruleType">
          <el-select v-model="form.ruleType" placeholder="请选择规则类型">
            <el-option label="科目映射" value="SUBJECT" />
            <el-option label="项目汇总" value="ITEM_SUM" />
            <el-option label="条件汇总" value="ITEM_CONDITIONAL" />
          </el-select>
        </el-form-item>
        <el-form-item label="项目编码" prop="itemCode">
          <el-input v-model="form.itemCode" placeholder="请输入项目编码" />
        </el-form-item>
        <el-form-item label="规则名称" prop="ruleName">
          <el-input v-model="form.ruleName" placeholder="请输入规则名称" />
        </el-form-item>
        <el-form-item label="匹配类型" prop="matchType">
          <el-select v-model="form.matchType" placeholder="请选择匹配类型">
            <el-option label="科目前缀" value="PREFIX" />
            <el-option label="科目精确" value="EXACT" />
            <el-option label="科目范围" value="RANGE" />
            <el-option label="父级科目" value="PARENT" />
            <el-option label="报表项目" value="ITEM" />
          </el-select>
        </el-form-item>
        <el-form-item label="匹配值" prop="matchValue">
          <el-input v-model="form.matchValue" placeholder="请输入匹配值" />
        </el-form-item>
        <el-form-item label="匹配结束值" v-if="form.matchType === 'RANGE'">
          <el-input v-model="form.matchValueEnd" placeholder="请输入匹配结束值" />
        </el-form-item>
        <el-form-item label="操作符" prop="operator">
          <el-select v-model="form.operator" placeholder="请选择操作符">
            <el-option label="加" value="ADD" />
            <el-option label="减" value="SUBTRACT" />
            <el-option label="排除" value="EXCLUDE" />
          </el-select>
        </el-form-item>
        <el-form-item label="科目类型">
          <el-select v-model="form.subjectType" placeholder="请选择科目类型" clearable>
            <el-option label="资产" :value="1" />
            <el-option label="负债" :value="2" />
            <el-option label="所有者权益" :value="3" />
            <el-option label="成本" :value="4" />
            <el-option label="损益" :value="5" />
          </el-select>
        </el-form-item>
        <el-form-item label="余额方向">
          <el-select v-model="form.direction" placeholder="请选择余额方向" clearable>
            <el-option label="借方" :value="1" />
            <el-option label="贷方" :value="2" />
          </el-select>
        </el-form-item>
        <el-form-item label="仅末级科目">
          <el-radio-group v-model="form.isLeafOnly">
            <el-radio :label="1">是</el-radio>
            <el-radio :label="0">否</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="优先级" prop="priority">
          <el-input-number v-model="form.priority" :min="1" :max="999" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio :label="1">启用</el-radio>
            <el-radio :label="0">停用</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="form.description" type="textarea" rows="2" placeholder="请输入描述" />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitForm">确定</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="MappingRulesPanel">
import { ref, reactive, watch } from 'vue'
import {
  listMappingRules,
  addMappingRules,
  updateMappingRules,
  delMappingRules,
  initDefaultRules,
  syncReportFormulas
} from '@/api/finance/mapping_rules'
import { ElMessageBox, ElMessage } from 'element-plus'
import Pagination from '@/components/Pagination/index.vue'
import Helper from '@/components/header/helper.vue';
 

const props = defineProps({
  templateType: {
    type: String,
    default: 'BALANCE_SHEET'
  },
  groupid: {
    type: String,
    default: ''
  }
})

// 查询参数
const queryParams = reactive({
  pageNum: 1,
  pageSize: 20,
  groupid: '',
  templateType: '',
  itemCode: '',
  matchType: '',
  status: null
})

// 数据
const rulesList = ref([])
const total = ref(0)
const loading = ref(false)
const syncLoading = ref(false)

// 对话框
const dialogVisible = ref(false)
const dialogTitle = ref('新增规则')
const ruleFormRef = ref(null)
const form = ref({
  ruleId: null,
  templateType: '',
  ruleType: 'SUBJECT',
  itemCode: '',
  ruleName: '',
  matchType: 'PREFIX',
  matchValue: '',
  matchValueEnd: '',
  operator: 'ADD',
  subjectType: null,
  direction: null,
  isLeafOnly: 1,
  priority: 100,
  status: 1,
  description: ''
})

// 表单验证规则
const rules = {
  templateType: [{ required: true, message: '请选择模板类型', trigger: 'change' }],
  ruleType: [{ required: true, message: '请选择规则类型', trigger: 'change' }],
  itemCode: [{ required: true, message: '请输入项目编码', trigger: 'blur' }],
  ruleName: [{ required: true, message: '请输入规则名称', trigger: 'blur' }],
  matchType: [{ required: true, message: '请选择匹配类型', trigger: 'change' }],
  matchValue: [{ required: true, message: '请输入匹配值', trigger: 'blur' }],
  operator: [{ required: true, message: '请选择操作符', trigger: 'change' }],
  priority: [{ required: true, message: '请输入优先级', trigger: 'blur' }],
  status: [{ required: true, message: '请选择状态', trigger: 'change' }]
}

// 监听props变化，自动查询
watch(
  () => [props.templateType, props.groupid],
  ([newType, newGroupid]) => {
    if (newType && newGroupid) {
      queryParams.templateType = newType
      queryParams.groupid = newGroupid
      getList()
    }
  },
  { immediate: true }
)

// 获取列表
async function getList() {
  if (!queryParams.groupid) return
  loading.value = true
  try {
    const res = await listMappingRules(queryParams)
    rulesList.value = res.rows || res.data || []
    total.value = res.total || 0
  } catch (error) {
    console.error('获取规则列表失败', error)
  } finally {
    loading.value = false
  }
}

// 查询
function handleQuery() {
  queryParams.pageNum = 1
  getList()
}

// 重置
function resetQuery() {
  queryParams.itemCode = ''
  queryParams.matchType = ''
  queryParams.status = null
  handleQuery()
}

// 新增
function handleAdd() {
  form.value = {
    ruleId: null,
    templateType: props.templateType,
    itemCode: '',
    ruleName: '',
    matchType: 'PREFIX',
    matchValue: '',
    matchValueEnd: '',
    operator: 'ADD',
    subjectType: null,
    direction: null,
    isLeafOnly: 1,
    priority: 100,
    status: 1,
    description: ''
  }
  dialogTitle.value = '新增规则'
  dialogVisible.value = true
}

// 编辑
function handleEdit(row) {
  form.value = { ...row }
  dialogTitle.value = '编辑规则'
  dialogVisible.value = true
}

// 删除
async function handleDelete(row) {
  try {
    await ElMessageBox.confirm('确定要删除此规则吗？', '删除确认', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    const res = await delMappingRules(row.ruleId)
    if (res.code === 200) {
      ElMessage.success('删除成功')
      getList()
    }
  } catch (error) {
    if (error === 'cancel') return
    ElMessage.error('删除失败')
  }
}

// 提交表单
async function submitForm() {
  try {
    await ruleFormRef.value.validate()
    form.value.groupid = props.groupid

    if (form.value.ruleId) {
      await updateMappingRules(form.value)
      ElMessage.success('修改成功')
    } else {
      await addMappingRules(form.value)
      ElMessage.success('新增成功')
    }
    dialogVisible.value = false
    getList()
  } catch (error) {
    console.error('表单验证失败', error)
  }
}

// 初始化默认规则
async function handleInitDefault() {
  try {
    await ElMessageBox.confirm('初始化默认规则将覆盖现有规则，是否继续？', '初始化确认', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    const res = await initDefaultRules(props.groupid)
    if (res.code === 200) {
      ElMessage.success(res.msg || '初始化成功')
      getList()
    } else {
      ElMessage.error(res.msg || '初始化失败')
    }
  } catch (error) {
    if (error === 'cancel') return
    ElMessage.error('初始化失败：' + (error.message || '网络错误'))
  }
}

// 同步公式
async function handleSyncFormula() {
  try {
    await ElMessageBox.confirm(
      `确定要根据映射规则同步当前模板的公式吗？`,
      '同步公式确认',
      {
        confirmButtonText: '确定同步',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    
    syncLoading.value = true
    // 这里需要templateId，暂时使用0表示同步所有模板
    const res = await syncReportFormulas(props.groupid, 0, props.templateType)
    if (res.code === 200) {
      ElMessage.success(res.msg || '公式同步成功')
    } else {
      ElMessage.error(res.msg || '公式同步失败')
    }
  } catch (error) {
    if (error === 'cancel') return
    ElMessage.error('同步失败：' + (error.message || '网络错误'))
  } finally {
    syncLoading.value = false
  }
}

// 辅助函数
function getRuleTypeLabel(type) {
  const map = {
    'SUBJECT': '科目映射',
    'ITEM_SUM': '项目汇总',
    'ITEM_CONDITIONAL': '条件汇总'
  }
  return map[type] || type
}

function getMatchTypeLabel(type) {
  const map = {
    'PREFIX': '科目前缀',
    'EXACT': '科目精确',
    'RANGE': '科目范围',
    'PARENT': '父级科目',
    'ITEM': '报表项目'
  }
  return map[type] || type
}

function getSubjectTypeLabel(type) {
  const map = {
    1: '资产',
    2: '负债',
    3: '权益',
    4: '成本',
    5: '损益'
  }
  return map[type] || ''
}
</script>

<style scoped>
.mapping-rules-panel {
  padding: 10px 0;
}

.search-form {
  margin-bottom: 0;
}

.text-right {
  text-align: right;
}

.mt-2 {
  margin-top: 10px;
}
</style>
