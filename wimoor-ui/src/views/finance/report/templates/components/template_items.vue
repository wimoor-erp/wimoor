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
                type="danger"
                icon="RefreshRight"
                @click="handleInitTemplate"
              >
                初始化模板
              </el-button>
        <el-button type="warning" icon="Refresh" @click="handleSyncFormula" :loading="syncLoading">
          同步公式
        </el-button>
        <!-- <el-button type="primary" @click="showCustomDialog">
          自定义规则项
        </el-button> -->
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

           <Helper name="报表模版" />
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
        :model-value="form"
        :parent-items="state.parentItems"
        :title="formTitle"
        :template-id="props.templateId"
        @cancel="handleCancel"
        @save="handleItemSaved"
        @delete="handleItemDeleted"
    ></template-item-edit>
  </div>
  <CustomDialog ref="customDialogRef"></CustomDialog>
</template>

<script setup name="TemplateItems">
import { ref, reactive, toRefs, defineProps, defineEmits, watch } from 'vue'
import { listItems, getItems, delItems, addItems, updateItems, getParentItems } from "@/api/finance/report_items.js"
import { listSubjects } from "@/api/finance/subjects.js"
import { syncReportFormulas } from "@/api/finance/mapping_rules.js"
import { initTemplateItems } from "@/api/finance/report_templates.js"
import { validateReport } from "@/api/finance/report.js"
import TemplateItemEdit from './template_item_edit.vue'
import CustomDialog from './template_item_custom_dialog.vue'
import { ElMessageBox, ElMessage, ElLoading } from 'element-plus';
import finStore from "@/hooks/store/useFinanceStore.js"
import Helper from '@/components/header/helper.vue';

const { proxy } = getCurrentInstance()
const { formula_type, subject_status } = proxy.useDict('formula_type', 'subject_status')

// 定义props和emits
const customDialogRef = ref();
const templateItemEditRef = ref();
const props = defineProps({
  templateId: {
    type: String,
    required: true
  },
  templateName: {
    type: String,
    required: true
  },
  templateType: {
    type: String,
    default: 'BALANCE_SHEET'
  },
  templateCode: {
    type: String,
    default: ''
  }
})

const emits = defineEmits(['item-updated'])

// 同步公式相关
const syncLoading = ref(false)

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
    status: '1',
    isShow: true,
    isShowZero: true,
    isLeaf: false,
    description: ''
  },
})

const {  form,formTitle,itemsList,parentItems,subjectList,selectedSubjects,openForm,loading
} = toRefs(state)
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

function showCustomDialog() {
  customDialogRef.value.show()
}

// 加载项目列表
function loadTemplateItems(templateId) {
  state.loading = true

  listItems({ templateId: templateId }).then(response => {
    console.log('加载项目列表响应:', response)
    state.itemsList = response.data
    state.loading = false

    // 加载父级项目列表
    loadParentItems(templateId)
  }).catch(error => {
    ElMessage.error('加载项目失败：' + (error.msg || '网络错误'))
    state.loading = false
  })
}

// 加载父级项目列表（用于下拉选择）
function loadParentItems(templateId) {
  getParentItems(templateId).then(response => {
    state.parentItems = response.data
  }).catch(error => {
    ElMessage.error('加载父级项目列表失败：' + (error.msg || '网络错误'))
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
    state.form = {
      ...response.data,
      templateId: props.templateId,
      parentCode: response.data.parentCode || '',
      itemLevel: response.data.itemLevel.toString(),
      status: response.data.status.toString()
    }
    state.formTitle = '编辑项目'
    templateItemEditRef.value.show(state.form);
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
      loadTemplateItems(props.templateId) // 修复引用错误
    } else {
      ElMessage.error('删除失败：' + response.msg)
    }
  } catch (error) {
    if (error === 'cancel') return
    ElMessage.error('删除失败：' + (error.msg || '网络错误'))
  }
}

// 新增：保存项目处理
async function handleItemSaved(formData) {
  formData.groupid= await finStore.getCurrentTenantId();
  
  // 验证SUM(CHILDREN)嵌套问题
  if (formData.formulaType === 'CALCULATED' && formData.calculationRule === 'SUM(CHILDREN)') {
    // 检查父级是否也使用SUM(CHILDREN)
    if (formData.parentCode) {
      const parentItem = state.itemsList.find(item => item.itemCode === formData.parentCode);
      if (parentItem && parentItem.formulaType === 'CALCULATED' && parentItem.calculationRule === 'SUM(CHILDREN)') {
        ElMessage.error(`不能保存：父级项目"${parentItem.itemName}"已使用SUM(CHILDREN)公式，子项目不能再使用SUM(CHILDREN)，否则会导致重复汇总。请使用FORMULA类型显式引用兄弟项目。`);
        return;
      }
    }
    
    // 检查子项目是否有使用SUM(CHILDREN)的
    const childrenWithSum = state.itemsList.filter(item => 
      item.parentCode === formData.itemCode && 
      item.formulaType === 'CALCULATED' && 
      item.calculationRule === 'SUM(CHILDREN)' &&
      item.itemId !== formData.itemId
    );
    if (childrenWithSum.length > 0) {
      ElMessage.error(`不能保存：子项目"${childrenWithSum[0].itemName}"已使用SUM(CHILDREN)公式，父项目不能再使用SUM(CHILDREN)，否则会导致重复汇总。请使用FORMULA类型显式引用子项目。`);
      return;
    }
  }
  
  try {
    state.loading = true
    let response;
    if (formData.itemId) {
      // 修改项目
      response = await updateItems(formData);
      //关闭dialog弹框
      templateItemEditRef.value.dialogVisible=false;
      ElMessage.success('修改成功')
    } else {
      // 新增项目
      templateItemEditRef.value.dialogVisible=false;
      response = await addItems(formData)
      ElMessage.success('新增成功')
    }
    state.loading = false
    loadTemplateItems(props.templateId) // 修复引用错误
    emits('item-updated')
  } catch (error) {
    state.loading = false
    ElMessage.error('操作失败：' + (error.msg || '网络错误'))
  }
}

// 新增：删除项目处理
async function handleItemDeleted(itemId) {
  try {
    state.loading = true
    const response = await delItems([itemId])
    if (response.code === 200) {
      ElMessage.success('删除成功')
      loadTemplateItems(props.templateId) // 修复引用错误
      emits('item-updated')
    } else {
      ElMessage.error('删除失败：' + response.msg)
    }
    state.loading = false
  } catch (error) {
    state.loading = false
    ElMessage.error('删除失败：' + (error.msg || '网络错误'))
  }
}

// 新增：取消操作
function handleCancel() {
  // 可以在这里添加取消操作的逻辑
}

// 表格行样式设置
const setRowStyle = ({ row }) => {
  return {
    // 可以根据行数据设置样式
  }
}

// 同步公式
async function handleSyncFormula() {
  if (!props.templateId) {
    ElMessage.warning('请先选择一个模板')
    return
  }
  
  try {
    await ElMessageBox.confirm(
      `确定要同步模板"${props.templateName}"的公式吗？这将根据映射规则重新生成计算公式。`,
      '同步公式确认',
      {
        confirmButtonText: '确定同步',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    
    syncLoading.value = true
    const groupid = await finStore.getCurrentTenantId()
    
    // 需要获取模板类型，从父组件传递或从API获取
    // 这里假设从props获取，如果没有则需要从API获取
    const templateType = props.templateType || 'BALANCE_SHEET'
    
    const res = await syncReportFormulas(groupid, props.templateId, templateType)
    if (res.code === 200) {
      ElMessage.success(res.msg || '公式同步成功')
      // 重新加载项目列表
      loadTemplateItems(props.templateId)
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

// 初始化模板
async function handleInitTemplate() {
  if (!props.templateId) {
    ElMessage.warning('请先选择一个模板')
    return
  }
  
  try {
    await ElMessageBox.confirm(
      `<div style="line-height: 1.8;">
        <p>您确定要初始化模板"<strong>${props.templateName}</strong>"吗？</p>
        <p style="color: #e6a23c; font-weight: bold;">⚠️ 警告：此操作将：</p>
        <ul>
          <li>删除该模板下的所有现有报表项目</li>
          <li>根据模板类型重新创建默认项目</li>
          <li>所有自定义配置将丢失</li>
        </ul>
        <p style="color: #f56c6c; font-weight: bold;">此操作不可撤销！</p>
      </div>`,
      '初始化模板确认',
      {
        confirmButtonText: '确定初始化',
        cancelButtonText: '取消',
        type: 'warning',
        dangerouslyUseHTMLString: true
      }
    )
    
    const groupid = await finStore.getCurrentTenantId()
    const templateType = props.templateType || 'BALANCE_SHEET'
    
    const res = await initTemplateItems(
      props.templateId,
      groupid,
      templateType
    )
    
    if (res.code === 200) {
      ElMessage.success(res.msg || '初始化成功')
      // 重新加载项目列表
      loadTemplateItems(props.templateId)
    } else {
      ElMessage.error(res.msg || '初始化失败')
    }
  } catch (error) {
    if (error === 'cancel') return
    ElMessage.error('初始化失败：' + (error.message || '网络错误'))
  }
}

// 验证功能
async function handleValidate() {
  if (!props.templateId) {
    ElMessage.warning('请先选择一个模板')
    return
  }

  try {
    // 弹出输入框让用户输入报告周期
    const { value: period } = await ElMessageBox.prompt(
      '请输入报告周期（格式：YYYY-MM，例如：2025-01）',
      '验证报告',
      {
        confirmButtonText: '开始验证',
        cancelButtonText: '取消',
        inputPattern: /^\d{4}-\d{2}$/,
        inputErrorMessage: '周期格式不正确，应为 YYYY-MM',
        inputPlaceholder: '2025-01'
      }
    )

    if (!period) return

    const groupid = await finStore.getCurrentTenantId()
    const templateCode = props.templateCode || props.templateType || 'BALANCE_SHEET'

    const loading = ElLoading.service({
      text: '正在验证报告数据...',
      background: 'rgba(0, 0, 0, 0.7)'
    })

    try {
      const res = await validateReport({
        groupid,
        templateCode,
        period
      })

      if (res.code === 200) {
        const data = res.data
        showValidationResult(data)
        emits('item-updated')
      } else {
        ElMessage.error(res.msg || '验证失败')
      }
    } finally {
      loading.close()
    }
  } catch (error) {
    if (error === 'cancel') return
    ElMessage.error('验证失败：' + (error.message || '网络错误'))
  }
}

// 显示验证结果
function showValidationResult(data) {
  const { errors = [], warnings = [], suggestions = [], period, templateCode } = data

  // 构建HTML内容
  let html = '<div style="max-height: 500px; overflow-y: auto;">'

  // 验证状态标题
  if (data.valid) {
    html += '<div style="background: #f0f9ff; border: 1px solid #67c23a; border-radius: 4px; padding: 12px; margin-bottom: 16px;">'
    html += '<p style="color: #67c23a; font-weight: bold; margin: 0;"><i class="el-icon-success"></i> 验证通过</p>'
    html += `<p style="color: #666; margin: 8px 0 0 0; font-size: 12px;">报告周期: ${period || '-'}</p>`
    html += '</div>'
  } else {
    html += '<div style="background: #fef0f0; border: 1px solid #f56c6c; border-radius: 4px; padding: 12px; margin-bottom: 16px;">'
    html += `<p style="color: #f56c6c; font-weight: bold; margin: 0;"><i class="el-icon-error"></i> 验证失败 - 发现 ${errors.length} 个错误</p>`
    html += `<p style="color: #666; margin: 8px 0 0 0; font-size: 12px;">报告周期: ${period || '-'}</p>`
    html += '</div>'
  }

  // 错误详情
  if (errors.length > 0) {
    html += '<div style="margin-bottom: 16px;">'
    html += '<h4 style="color: #f56c6c; margin: 0 0 12px 0; font-size: 14px;"><i class="el-icon-warning"></i> 错误详情</h4>'
    errors.forEach((error, index) => {
      html += '<div style="background: #fef0f0; border-left: 3px solid #f56c6c; padding: 12px; margin-bottom: 8px; border-radius: 0 4px 4px 0;">'
      html += `<p style="color: #333; font-weight: bold; margin: 0 0 8px 0;">${error.message || error}</p>`

      if (error.suggestion) {
        html += '<div style="background: #fff; padding: 8px; border-radius: 4px; margin-top: 8px;">'
        html += '<p style="color: #666; margin: 0; font-size: 12px;"><strong>解决建议：</strong></p>'
        html += `<p style="color: #666; margin: 4px 0 0 0; font-size: 12px; white-space: pre-line;">${error.suggestion}</p>`
        html += '</div>'
      }

      if (error.helpUrl) {
        html += `<p style="margin: 8px 0 0 0;"><a href="${error.helpUrl}" target="_blank" style="color: #409eff; font-size: 12px;">查看帮助文档 →</a></p>`
      }

      html += '</div>'
    })
    html += '</div>'
  }

  // 警告信息
  if (warnings.length > 0) {
    html += '<div style="margin-bottom: 16px;">'
    html += `<h4 style="color: #e6a23c; margin: 0 0 12px 0; font-size: 14px;"><i class="el-icon-warning"></i> 警告信息 (${warnings.length})</h4>`

    // 限制显示数量，避免过多警告
    const displayWarnings = warnings.slice(0, 10)
    displayWarnings.forEach((warning) => {
      html += '<div style="background: #fdf6ec; border-left: 3px solid #e6a23c; padding: 10px; margin-bottom: 6px; border-radius: 0 4px 4px 0;">'
      html += `<p style="color: #333; margin: 0; font-size: 13px;">${warning.message || warning}</p>`

      if (warning.suggestion) {
        html += `<p style="color: #666; margin: 4px 0 0 0; font-size: 12px;"><em>建议: ${warning.suggestion}</em></p>`
      }

      html += '</div>'
    })

    if (warnings.length > 10) {
      html += `<p style="color: #666; font-size: 12px; margin: 8px 0 0 0;">... 还有 ${warnings.length - 10} 条警告未显示</p>`
    }

    html += '</div>'
  }

  // 优化建议
  if (suggestions.length > 0) {
    html += '<div style="margin-bottom: 16px;">'
    html += '<h4 style="color: #409eff; margin: 0 0 12px 0; font-size: 14px;"><i class="el-icon-info"></i> 优化建议</h4>'
    suggestions.forEach((suggestion) => {
      html += '<div style="background: #ecf5ff; border-left: 3px solid #409eff; padding: 10px; margin-bottom: 6px; border-radius: 0 4px 4px 0;">'
      html += `<p style="color: #333; margin: 0; font-size: 13px;">${suggestion.message || suggestion}</p>`

      if (suggestion.action) {
        html += `<p style="color: #409eff; margin: 6px 0 0 0; font-size: 12px;"><strong>建议操作: </strong>${suggestion.action}</p>`
      }

      html += '</div>'
    })
    html += '</div>'
  }

  html += '</div>'

  // 根据验证结果显示不同类型的弹窗
  if (!data.valid) {
    ElMessageBox.alert(html, '报告验证结果', {
      dangerouslyUseHTMLString: true,
      type: 'error',
      confirmButtonText: '我知道了',
      customClass: 'validation-result-dialog'
    })
  } else if (warnings.length > 0) {
    ElMessageBox.alert(html, '报告验证结果', {
      dangerouslyUseHTMLString: true,
      type: 'warning',
      confirmButtonText: '我知道了',
      customClass: 'validation-result-dialog'
    })
  } else {
    ElMessage.success('报告验证通过！数据完整且正确。')
  }
}
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