<template>
  <div class="item-management">
    <!-- 工具栏 -->
    <el-row :gutter="10" class="mb-3">
      <el-col :span="12">
        <h3 class="mb-0">
          <i class="bi bi-list-check"></i>
          {{ templateName }}项目列表
        </h3>
      </el-col>
      <el-col :span="12" class="text-right">
        <el-button
          type="primary"
          icon="Plus"
          @click="handleAddItem"
        >
          新增项目
        </el-button>
       
        <el-button
          type="success"
          icon="Check"
          @click="handleValidate"
        >
          验证
        </el-button>
      </el-col>
    </el-row>

    <!-- 项目列表 -->
    <el-card class="mb-4">
      <el-table
        v-loading="loading"
        :data="itemsList"
        border
        size="small"
        :row-style="setRowStyle"
      >
        <el-table-column prop="lineNumber" label="行次" width="60" align="center"></el-table-column>
        <el-table-column prop="itemCode" label="项目编码" width="300"></el-table-column>
        <el-table-column label="项目名称"  >
          <template #default="{ row }">
            <span :style="{ 'padding-left': (row.itemLevel - 1) * 20 + 'px' }">{{ row.itemName }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="itemLevel" label="级别" width="80" align="center"></el-table-column>
        <el-table-column prop="dataType" label="计算类型" width="120" align="center">
          <template #default="{ row }">
            <dict-tag :options="formula_type" :value="row.formulaType"/>
          </template>
        </el-table-column>
        <el-table-column prop="calculationRule" label="计算规则" width="300"></el-table-column>
        <el-table-column prop="status" label="状态" width="100" align="center">
          <template #default="{ row }">
             <dict-tag :options="subject_status" :value="row.status"/>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="120" align="center">
          <template #default="{ row }">
            <el-button
              type="text"
              size="small"
              icon="Edit"
              @click="handleUpdateItem(row)"
            ></el-button>
            <el-button
              type="text"
              size="small"
              icon="Delete"
              @click="handleDeleteItem(row)"
              text-color="#ff4d4f"
            ></el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>


    <!-- 项目编辑表单 -->
    <template-item-edit
        ref="templateItemEditRef"
        :form-data="form"
        :title="formTitle"
        :template-id="props.templateId"
        @close="handleCancel"
        @save="handleItemSaved"
        @delete="handleItemDeleted"
    ></template-item-edit>
  </div>
</template>

<script setup name="TemplateItems">
import { ref, reactive, toRefs, defineProps, defineEmits, watch } from 'vue'
import { listItems, getItems, delItems, addItems, updateItems } from "@/api/finance/report_items.js"
import { listSubjects } from "@/api/finance/subjects.js"
import TemplateItemEdit from './template_item_edit.vue'
import { ElMessageBox, ElMessage } from 'element-plus'; // 新增导入
const { proxy } = getCurrentInstance()
const { formula_type, subject_status } = proxy.useDict('formula_type', 'subject_status')
// 定义props和emits
const templateItemEditRef=ref();
const props = defineProps({
  templateId: {
    type: String,
    required: true
  },
  templateName: {
    type: String,
    required: true
  }
})

const emits = defineEmits(['item-updated'])

// 状态管理
const state = reactive({
  itemsList: [],
  parentItems: [],
  subjectList: [],
  selectedSubjects: [],
  openForm: false,
  formTitle: '添加报表项目',
  loading: false,
  form: {
    itemId: null,
    templateId: '',
    itemCode: '',
    itemName: '',
    parentCode: '',
    itemLevel: '1',
    lineNumber: null,
    sortOrder: 0,
    itemType: '',
    dataType: '',
    formulaType: 'DIRECT',
    dataSource: 'SUBJECT',
    amountType: 'END_BALANCE',
    formulaContent: '',
    calculationRule: '',
    direction: '',
    status: '启用',
    isShow: true,
    isShowZero: true,
    isLeaf: false,
    description: ''
  },
  rules: {
    itemCode: [
      { required: true, message: '项目编码不能为空', trigger: 'blur' }
    ],
    itemName: [
      { required: true, message: '项目名称不能为空', trigger: 'blur' }
    ],
    itemLevel: [
      { required: true, message: '项目级别不能为空', trigger: 'change' }
    ]
  }
})

const { itemsList, form, rules } = toRefs(state)
const itemFormRef = ref(null)

// 监听templateId变化，加载对应项目
watch(
  () => props.templateId,
  (newVal) => {
    if (newVal) {
      loadTemplateItems(newVal)
    }
  },
  { immediate: true }
)

// 加载项目列表
function loadTemplateItems(templateId) {
  state.loading = true
  
  // 删除模拟数据，使用真实API调用
  listItems({ templateId: templateId }).then(response => {
    console.log('加载项目列表响应:', response)
    state.itemsList = response.data
    state.parentItems = response.data.filter(item => ['1', '2', '3'].includes(item.itemLevel))
    state.loading = false
  }).catch(error => {
    ElMessage.error('加载项目失败：' + (error.msg || '网络错误'))
    state.loading = false
  })
}


// 新增项目
function handleAddItem() {
  state.form = {
    itemId: null,
    templateId: props.templateId,
    itemCode: '',
    itemName: '',
    parentCode: '',
    itemLevel: '1',
    lineNumber: null,
    sortOrder: 0,
    itemType: '',
    dataType: '',
    formulaType: 'DIRECT',
    dataSource: 'SUBJECT',
    amountType: 'END_BALANCE',
    formulaContent: '',
    calculationRule: '',
    direction: '',
    status: '1',
    isShow: true,
    isShowZero: true,
    isLeaf: false,
    description: ''
  }
  state.formTitle = '新增项目'
  templateItemEditRef.value.show(state.form);
}

// 编辑项目
async function handleUpdateItem(row) {
  state.loading = true
  const response = await getItems(row.itemId)
  state.loading = false
  if (response.code === 200) {
    state.form = { ...response.data, templateId: props.templateId, parentCode: response.data.parentCode || '', itemLevel: response.data.itemLevel.toString(), status: response.data.status.toString() }
    state.formTitle = '编辑项目'
    templateItemEditRef.value.show(state.form);// 显示新组件
  } else {
    ElMessage.error('获取项目详情失败：' + response.msg)
  }
}

// 删除项目
async function handleDeleteItem(row) {
  try {
    await ElMessageBox.confirm(
      '确定要删除此项目吗？此操作不可撤销。',
      '删除确认',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    const response = await delItems([row.itemId])
    if (response.code === 200) {
      ElMessage.success('删除成功')
      loadTemplateItems(props.templateCode)
    } else {
      ElMessage.error('删除失败：' + response.msg)
    }
  } catch (error) {
    if (error === 'cancel') return
    ElMessage.error('删除失败：' + (error.msg || '网络错误'))
  }
}

// 表单提交处理
async function handleSubmit() {
  try {
    const valid = await itemFormRef.value.validate()
    if (valid) {
      // 转换状态值为数字类型
      form.value.status = form.value.status === '启用' ? '1' : '0'
      
      if (form.value.itemId) {
        // 修改项目
        await updateItems(form.value)
        ElMessage.success('修改成功')
      } else {
        // 新增项目
        await addItems(form.value)
        ElMessage.success('新增成功')
      }
      state.openForm = false
      loadTemplateItems(props.templateCode)
      emits('item-updated')
    }
  } catch (error) {
    if (error.name === 'ValidationError') return
    ElMessage.error('操作失败：' + (error.msg || '网络错误'))
  }
}

// 表格行样式设置
const setRowStyle = ({ row }) => {
  return {
    // 可以根据行数据设置样式
  }
}

// 验证功能
function handleValidate() {
  // 实现验证逻辑
  emits('item-updated')
}


// 其他方法保持不变...
</script>

<style scoped>
/* 项目列表相关样式 */
.mb-3{
  margin-bottom: 10px;
}
.template-items {
  margin-top: 20px;
}
</style>
