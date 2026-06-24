<template>
  <el-dialog :title="formTitle" v-model="dialogVisible" width="800px" append-to-body>
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

              <el-form-item label="行次" prop="lineNumber">
                <el-input-number v-model="form.lineNumber" placeholder="请输入行次" :min="1" :precision="0"></el-input-number>
              </el-form-item>
            </div>
          </el-col>

          <el-col :span="12">
            <div class="form-section">
              <div class="form-section-title">项目属性</div>

              <el-form-item label="项目类型" prop="itemType">
                <el-select v-model="form.itemType" placeholder="请选择项目类型">
                  <el-option value="ASSET" label="资产"></el-option>
                  <el-option value="LIABILITY" label="负债"></el-option>
                  <el-option value="EQUITY" label="权益"></el-option>
                  <el-option value="INCOME" label="收入"></el-option>
                  <el-option value="COST" label="成本"></el-option>
                  <el-option value="EXPENSE" label="费用"></el-option>
                </el-select>
              </el-form-item>

              <el-form-item label="项目分类" prop="itemCategory">
                <el-input v-model="form.itemCategory" placeholder="请输入项目分类"></el-input>
              </el-form-item>

              <el-form-item label="状态" prop="status">
                <el-switch v-model="form.status" active-value="1" inactive-value="0" active-text="启用" inactive-text="禁用"></el-switch>
              </el-form-item>

              <el-form-item label="是否末级项目" prop="isLeaf">
                <el-switch v-model="form.isLeaf" :active-value="true" :inactive-value="false" active-text="是" inactive-text="否"></el-switch>
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

              <el-form-item label="数据来源" prop="dataSource">
                <el-select v-model="form.dataSource" placeholder="请选择数据来源">
                  <el-option value="SUBJECT" label="会计科目"></el-option>
                  <el-option value="CUSTOM" label="自定义"></el-option>
                  <el-option value="CALCULATED" label="计算"></el-option>
                  <el-option value="CONSTANT" label="常量"></el-option>
                </el-select>
              </el-form-item>

              <el-form-item label="关联科目编码" prop="subjectCodes">
                <el-input v-model="form.subjectCodes" placeholder="请输入关联科目编码（多个用逗号分隔）"></el-input>
              </el-form-item>

              <el-form-item label="金额类型" prop="amountType">
                <el-select v-model="form.amountType" placeholder="请选择金额类型">
                  <el-option value="END_BALANCE" label="期末余额"></el-option>
                  <el-option value="BEGIN_BALANCE" label="期初余额"></el-option>
                  <el-option value="DEBIT_TOTAL" label="借方发生额"></el-option>
                  <el-option value="CREDIT_TOTAL" label="贷方发生额"></el-option>
                </el-select>
              </el-form-item>

              <el-form-item label="金额方向" prop="direction">
                <el-select v-model="form.direction" placeholder="请选择金额方向">
                  <el-option value="1" label="正数"></el-option>
                  <el-option value="-1" label="负数"></el-option>
                  <el-option value="" label="自动判断"></el-option>
                </el-select>
              </el-form-item>

              <el-form-item label="计算规则" prop="calculationRule">
                <el-input v-model="form.calculationRule" type="textarea" rows="2" placeholder="请输入计算规则"></el-input>
              </el-form-item>

              <el-form-item label="公式内容" prop="formulaContent">
                <el-input v-model="form.formulaContent" type="textarea" rows="4" placeholder="请输入公式内容"></el-input>
              </el-form-item>
            </div>
          </el-col>
        </el-row>

        <el-row :gutter="28">
          <el-col :span="12">
            <div class="form-section">
              <div class="form-section-title">显示设置</div>

              <el-form-item label="显示格式" prop="displayFormat">
                <el-select v-model="form.displayFormat" placeholder="请选择显示格式">
                  <el-option value="NORMAL" label="正常"></el-option>
                  <el-option value="BOLD" label="加粗"></el-option>
                  <el-option value="ITALIC" label="斜体"></el-option>
                  <el-option value="UNDERLINE" label="下划线"></el-option>
                </el-select>
              </el-form-item>

              <el-form-item label="显示零值" prop="isShowZero">
                <el-switch v-model="form.isShowZero" :active-value="true" :inactive-value="false" active-text="显示" inactive-text="隐藏"></el-switch>
              </el-form-item>

              <el-form-item label="是否显示" prop="isShow">
                <el-switch v-model="form.isShow" :active-value="true" :inactive-value="false" active-text="显示" inactive-text="隐藏"></el-switch>
              </el-form-item>
            </div>
          </el-col>

          <el-col :span="12">
            <div class="form-section">
              <div class="form-section-title">其他设置</div>

              <el-form-item label="排序" prop="sortOrder">
                <el-input-number v-model="form.sortOrder" placeholder="请输入排序" :min="0" :precision="0"></el-input-number>
              </el-form-item>

              <el-form-item label="描述" prop="description">
                <el-input v-model="form.description" type="textarea" rows="3" placeholder="请输入项目描述"></el-input>
              </el-form-item>
            </div>
          </el-col>
        </el-row>
      </el-form>
    </div>

    <template #footer>
      <div class="d-flex justify-content-end gap-2">
        <el-button @click="handleCancel">取消</el-button>
        <el-button type="danger" @click="handleDelete" v-if="form.itemId">删除</el-button>
        <el-button type="primary" @click="handleSubmit">保存</el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup name="TemplateItemEdit">
import { ref, reactive, toRefs, defineProps, defineEmits, watch, getCurrentInstance } from 'vue'
import { ElMessageBox, ElMessage } from 'element-plus'

// 获取当前实例
const { proxy } = getCurrentInstance()
const { formula_type, subject_status } = proxy.useDict('formula_type', 'subject_status')

const props = defineProps({
  modelValue: {
    type: Object,
    default: () => null
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

const emits = defineEmits(['save', 'delete', 'cancel', 'update:dialogVisible'])

const state = reactive({
  form: {
    // 基本信息
    itemId: null,
    groupid: '', // 租户ID
    templateId: props.templateId,
    itemCode: '',
    itemName: '',
    parentCode: '',
    itemLevel: 1,
    lineNumber: null,

    // 项目属性
    itemType: '',
    itemCategory: '',

    // 计算规则
    calculationRule: '',
    formulaType: 'DIRECT',
    formulaContent: '',
    dataSource: 'SUBJECT',
    subjectCodes: '',
    amountType: 'END_BALANCE',
    direction: null,

    // 显示设置
    displayFormat: 'NORMAL',
    isShowZero: 1,
    isShow: 1,
    isLeaf: 1,

    // 其他设置
    sortOrder: 0,
    status: 1,
    description: '',

    // 时间戳
    created_time: null,
    updated_time: null,
    created_by: null
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
    itemType: [
      { required: true, message: '项目类型不能为空', trigger: 'change' }
    ],
    formulaType: [
      { required: true, message: '公式类型不能为空', trigger: 'change' }
    ],
    dataSource: [
      { required: true, message: '数据来源不能为空', trigger: 'change' }
    ],
    amountType: [
      { required: true, message: '金额类型不能为空', trigger: 'change' }
    ]
  }
})

const { form, formTitle, dialogVisible, rules } = toRefs(state)
const itemFormRef = ref(null)

// 监听modelValue变化，初始化表单数据
watch(
    () => props.modelValue,
    (newVal) => {
      if (newVal) {
        // 使用toRaw获取原始对象，避免响应式引用
        state.form = { ...newVal }
      } else {
        // 新增时初始化表单
        state.form = {
          ...state.form,
          itemId: null,
          templateId: props.templateId,
          status: 1,
          isLeaf: 1,
          isShowZero: 1,
          isShow: 1,
          formulaType: 'DIRECT',
          dataSource: 'SUBJECT',
          amountType: 'END_BALANCE',
          displayFormat: 'NORMAL',
          sortOrder: 0
        }
      }
      state.formTitle = props.title
    },
    { immediate: true }
)

// 监听dialogVisible变化，通知父组件
watch(
    () => state.dialogVisible,
    (newVal) => {
      emits('update:dialogVisible', newVal)
    }
)

// 表单提交
const handleSubmit = () => {
  itemFormRef.value.validate((valid) => {
    if (valid) {
      // 转换数据类型为数字类型
      const formData = { ...form.value }

      // 转换为数字类型
      formData.itemLevel = Number(formData.itemLevel)
      formData.lineNumber = formData.lineNumber !== null ? Number(formData.lineNumber) : null
      formData.isLeaf = Number(formData.isLeaf)
      formData.isShowZero = Number(formData.isShowZero)
      formData.isShow = Number(formData.isShow)
      formData.sortOrder = Number(formData.sortOrder)
      formData.status = Number(formData.status)
      formData.direction = formData.direction !== null && formData.direction !== '' ? Number(formData.direction) : null

      emits('save', formData)
    }
  })
}

// 取消操作
const handleCancel = () => {
  emits('cancel')
  state.dialogVisible = false
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

// 显示对话框
const show = (data) => {
  if (data) {
    state.form = { ...data }
    state.formTitle = `${props.title} - ${data.itemName}`
  } else {
    // 新增时初始化表单
    state.form = {
      ...state.form,
      itemId: null,
      templateId: props.templateId,
      status: 1,
      isLeaf: 1,
      isShowZero: 1,
      isShow: 1,
      formulaType: 'DIRECT',
      dataSource: 'SUBJECT',
      amountType: 'END_BALANCE',
      displayFormat: 'NORMAL',
      sortOrder: 0
    }
    state.formTitle = '新增项目'
  }
  state.dialogVisible = true
}

defineExpose({
  show,dialogVisible,
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