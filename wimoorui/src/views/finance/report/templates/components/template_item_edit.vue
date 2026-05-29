<template>
  <el-dialog :title="formTitle" :model-value="dialogVisible"   width="800px" append-to-body>
    <div class="form-container">
      <el-form ref="itemFormRef" :model="form" :rules="rules" label-width="100px">
        <el-row :gutter="28">
          <el-col :span="12">
     
            <div class="form-section">
              <div class="form-section-title">基本信息</div>

              <el-form-item label="项目编码" prop="itemCode">
                <el-input v-model="form.itemCode" placeholder="请输入项目编码"></el-input>
              </el-form-item>

              <el-form-item label="项目名称" prop="itemName">
                <el-input v-model="form.itemName" placeholder="请输入项目名称"></el-input>
              </el-form-item>

              <el-form-item label="父级项目" prop="parentCode">
                <el-select v-model="form.parentCode" placeholder="-- 无父级项目 --">
                  <el-option v-for="item in parentItems" :key="item.itemCode" :label="`${item.itemCode} - ${item.itemName}`" :value="item.itemCode"></el-option>
                </el-select>
              </el-form-item>

              <el-form-item label="项目级别" prop="itemLevel">
                <el-select v-model="form.itemLevel" placeholder="请选择项目级别">
                  <el-option value="1" label="一级项目"></el-option>
                  <el-option value="2" label="二级项目"></el-option>
                  <el-option value="3" label="三级项目"></el-option>
                  <el-option value="4" label="四级项目"></el-option>
                </el-select>
              </el-form-item>
            </div>
          </el-col>

          <el-col :span="12">
            <div class="form-section">
              <div class="form-section-title">项目属性</div>
               <el-form-item label="数据类型" prop="dataType">
                  <el-select v-model="form.dataType" placeholder="请选择数据类型">
                    <el-option value="GROUP" label="组项目"></el-option>
                    <el-option value="DIRECT" label="直接取值"></el-option>
                    <el-option value="FORMULA" label="公式计算"></el-option>
                  </el-select>
                </el-form-item>

              <el-form-item label="状态" prop="status">
                <el-switch v-model="form.status" active-value="1" inactive-value="0" active-text="启用" inactive-text="禁用"></el-switch>
              </el-form-item>

              <el-form-item label="是否末级项目" prop="isLeaf">
                <el-switch v-model="form.isLeaf" active-text="是" inactive-text="否"></el-switch>
              </el-form-item>

              <el-form-item label="显示零值" prop="isShowZero">
                <el-switch v-model="form.isShowZero" active-text="显示" inactive-text="隐藏"></el-switch>
              </el-form-item>
            </div>
          </el-col>
 </el-row>

        <el-row>
          <el-col :span="24">
            <div class="form-section">
              <div class="form-section-title">计算规则</div>
                <el-form-item label="公式类型" prop="formulaType">
                  <el-select v-model="form.formulaType" placeholder="请选择公式类型">
                    <el-option value="DIRECT" label="直接取值"></el-option>
                    <el-option value="FORMULA" label="公式计算"></el-option>
                    <el-option value="CUSTOM" label="自定义"></el-option>
                    <el-option value="CALCULATED" label="自动计算"></el-option>
                  </el-select>
                </el-form-item>

              <el-form-item label="公式内容" prop="calculationRule">
                <el-input v-model="form.calculationRule" type="textarea" rows="4" placeholder="请输入公式内容"></el-input>
              </el-form-item>
            </div>
          </el-col>
        </el-row>
      </el-form>
    </div>

    <template #footer>
      <div class="d-flex justify-content-end gap-2">
        <el-button   @click="handleCancel">取消</el-button>
        <el-button type="danger" @click="handleDelete" v-if="form.itemId">删除</el-button>
        <el-button type="primary" @click="handleSubmit">保存</el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup name="TemplateItemEdit">
import { ref, reactive, toRefs, defineProps, defineEmits, watch } from 'vue'
import { ElMessageBox, ElMessage } from 'element-plus'
const { proxy } = getCurrentInstance()
const { formula_type, subject_status } = proxy.useDict('formula_type', 'subject_status')
const props = defineProps({
 
  modelValue: {
    type: Object,
    default: () => ({})
  },
  parentItems: {
    type: Array,
    default: () => []
  },
  templateId: {
    type: String,
    required: true
  },
  title: {
    type: String,
    default: '编辑项目'
  }
})

const emits = defineEmits([  'save', 'delete', 'cancel'])

import { toRaw } from 'vue'

const state = reactive({
  form: {
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
  },
  dialogVisible: false,
  formTitle: props.title,
  rules: {
    itemCode: [
      { required: true, message: '项目编码不能为空', trigger: 'blur' },
      { max: 50, message: '项目编码长度不能超过50个字符', trigger: 'blur' }
    ],
    itemName: [
      { required: true, message: '项目名称不能为空', trigger: 'blur' },
      { max: 100, message: '项目名称长度不能超过100个字符', trigger: 'blur' }
    ],
    itemLevel: [
      { required: true, message: '项目级别不能为空', trigger: 'change' }
    ],
    dataType: [
      { required: true, message: '数据类型不能为空', trigger: 'change' }
    ],
    formulaType: [
      { required: true, message: '公式类型不能为空', trigger: 'change' }
    ],
    formulaContent: [
      { required: true, message: '公式内容不能为空', trigger: 'blur' },
      { max: 500, message: '公式内容长度不能超过500个字符', trigger: 'blur' }
    ]
  }
})

const { form, formTitle, dialogVisible } = toRefs(state)
const itemFormRef = ref(null)

// 监听modelValue变化，初始化表单数据
watch(
  () => props.modelValue,
  (newVal) => {
    if (newVal) {
      // 使用toRaw获取原始对象，避免响应式引用
      state.form = { ...toRaw(newVal) }
    } else {
      // 新增时初始化表单
      state.form = {
        ...state.form,
        itemId: null,
        templateId: props.templateId,
        status: '1',
        isLeaf: false,
        isShowZero: true
      }
    }
    state.formTitle = props.title
  },
  { immediate: true }
)

// 表单提交
const handleSubmit = () => {
  itemFormRef.value.validate((valid) => {
    if (valid) {
      emits('save', { ...form.value })
    }
  })
}

// 取消操作
const handleCancel = () => {
  emits('cancel')
  emits('update:open', false)
}

const handleDialogClose = (value) => {
  emits('update:open', value)
}

// 删除操作
const handleDelete = () => {
  ElMessageBox.confirm(
    '确定要删除此项目吗？此操作不可撤销。',
    '删除确认',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(() => {
    emits('delete', form.value.itemId)
  }).catch(() => {})
}
const show = (data) => {
  if (data) {
    state.form = { ...toRaw(data) }
  } else {
    // 新增时初始化表单
    state.form = {
      ...state.form,
      itemId: null,
      templateId: props.templateId,
      status: '1',
      isLeaf: false,
      isShowZero: true
    }
  }
  state.formTitle = props.title;
  state.dialogVisible = true;
}
defineExpose({
  show
})
</script>

<style scoped lang="scss">
.form-container {
  padding: 10px 0;
}

.form-section {
  margin-bottom: 20px;
}

.form-section-title {
  font-size: 1.1em;
  font-weight: 600;
  margin-bottom: 15px;
  padding-bottom: 8px;
  border-bottom: 2px solid var(--el-color-primary);
}

.mb-3 {
  margin-bottom: 15px;
}

.form-label {
  margin-bottom: 5px;
  display: inline-block;
  font-weight: 500;
}

.text-danger {
  color: #dc3545;
}
</style>