<template>
  <div class="amazon-fin-month-report">
    <el-card class="box-card" shadow="never">
      <template #header>
        <div class="card-header">
          <div class="voucher-type-info">
            <el-input v-model="form.name" placeholder="请输入模板名称" clearable style="width: 300px; margin-right: 10px;"></el-input>
            <span class="voucher-type-label">凭证字</span>
            <el-select v-model="form.voucherType" placeholder="记" style="width:70px;margin-right: 5px; border-radius: 0; height: 30px;">
              <el-option v-for="item in voucherTypeList" :key="item.id" :label="item.name" :value="item.name"></el-option>
            </el-select>
            <span class="voucher-no-label">国家</span>
            <el-select v-model="form.country" placeholder="选择国家" style="width: 90px; text-align: center; height: 30px; border-radius: 0; margin-right: 30px;">
                <el-option v-for="item in marketList" :key="item.market" :label="item.name" :value="item.market"></el-option>
            </el-select>
          </div>
          <div>
            <el-button :loading="loading" @click="handleInitItem">初始化</el-button>
            <el-button type="success" @click="openAddSubjectDialog">新增科目</el-button>
          </div>
        </div>
      </template>
      <el-table :data="subjectGroups" style="width: 100%" border v-loading="loading" height="385">
            <el-table-column prop="summary" label="摘要" width="220">
          <template #default="scope">
            <el-input type="textarea" rows="2" v-model="scope.row.summary" placeholder="请输入摘要"   />
          </template>
        </el-table-column>
        <el-table-column prop="subjectName" label="科目名称"  >
          <template #default="scope">
            <div>{{ scope.row.subjectCode }} {{ scope.row.subjectName }}</div>
          </template>
        </el-table-column>
    
        <el-table-column prop="direction" label="余额方向" width="120">
          <template #default="scope">
            <el-select v-model="scope.row.direction" placeholder="选择方向"  style="width: 90px">
              <el-option label="借方" :value="1"></el-option>
              <el-option label="贷方" :value="2"></el-option>
            </el-select>
          </template>
        </el-table-column>
        <el-table-column prop="amountField" label="模板字段"  >
          <template #default="scope">
            <div style="display: flex; align-items: center; justify-content: space-between;">
              <span>{{scope.row.amountField}}</span>
              <el-button  link small  @click="openFormulaDialog(scope.row)">
              <calculator></calculator>
              </el-button>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="55">
          <template #default="scope">
            <el-button type="danger" link size="small" @click="deleteSubject(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>



  <!-- 新增科目对话框 -->
  <AddSubjectDialog
    v-model="addSubjectVisible"
    :subjects="subjects"
    :selected-subject-ids="selectedSubjectIds"
    @confirm="handleAddSubjects"
  />
    <!-- 公式编辑对话框 -->
  <el-dialog v-model="formulaDialogVisible" :title="'编辑公式 - ' + (currentSubject ? (currentSubject.subjectCode + ' ' + currentSubject.subjectName) : '')" top="5vh" width="800px">
    <div >
      <el-input
        v-model="currentFormula"
        type="textarea"
        :rows="4"
        placeholder="输入公式，例如：`FBA product sales` - `FBA selling fees`"
        style="width: 100%;"
      />
    </div>

    <div style="display: flex; gap: 20px;">
      <div style="flex: 1;">
        <h4>可用字段</h4>
        <el-table :scrollbar-always-on="true" :data="allAmountFields" style="width: 100%; margin-bottom: 10px;" height="500" size="small" @row-click="addFieldToFormulaDirectly">
          <el-table-column prop="field" label="字段名称">
            <template #default="scope">
              {{ scope.row }}
            </template>
          </el-table-column>
          <el-table-column label="操作" width="80">
            <template #default="scope">
              <el-button type="primary" size="small" @click.stop="addFieldToFormulaDirectly(scope.row)">加入</el-button>
            </template>
          </el-table-column>
        </el-table>
      
      </div>
      <div style="flex: 1;">
        <h4>计算符号</h4>
        <div style="display: grid; grid-template-columns: repeat(4, 1fr); gap: 8px; margin-bottom: 15px;">
          <el-button type="info" style="height: 50px; font-size: 18px; font-weight: bold; border-radius: 8px;margin-left:12px" @click="addSymbolToFormula('(')">(</el-button>
          <el-button type="info" style="height: 50px; font-size: 18px; font-weight: bold; border-radius: 8px;" @click="addSymbolToFormula(')')">)</el-button>
          <el-button type="info" style="height: 50px; font-size: 18px; font-weight: bold; border-radius: 8px;" @click="addSymbolToFormula('/')">/</el-button>
          <el-button type="info" style="height: 50px; font-size: 18px; font-weight: bold; border-radius: 8px;" @click="addSymbolToFormula('*')">*</el-button>
          <el-button type="info" style="height: 50px; font-size: 18px; font-weight: bold; border-radius: 8px;" @click="addSymbolToFormula('7')">7</el-button>
          <el-button type="info" style="height: 50px; font-size: 18px; font-weight: bold; border-radius: 8px;" @click="addSymbolToFormula('8')">8</el-button>
          <el-button type="info" style="height: 50px; font-size: 18px; font-weight: bold; border-radius: 8px;" @click="addSymbolToFormula('9')">9</el-button>
          <el-button type="info" style="height: 50px; font-size: 18px; font-weight: bold; border-radius: 8px;" @click="addSymbolToFormula('-')">-</el-button>
          <el-button type="info" style="height: 50px; font-size: 18px; font-weight: bold; border-radius: 8px;" @click="addSymbolToFormula('4')">4</el-button>
          <el-button type="info" style="height: 50px; font-size: 18px; font-weight: bold; border-radius: 8px;" @click="addSymbolToFormula('5')">5</el-button>
          <el-button type="info" style="height: 50px; font-size: 18px; font-weight: bold; border-radius: 8px;" @click="addSymbolToFormula('6')">6</el-button>
          <el-button type="info" style="height: 100px; font-size: 18px; font-weight: bold; border-radius: 8px; grid-row: span 2;" @click="addSymbolToFormula('+')">+</el-button>
          <el-button type="info" style="height: 50px; font-size: 18px; font-weight: bold; border-radius: 8px;" @click="addSymbolToFormula('1')">1</el-button>
          <el-button type="info" style="height: 50px; font-size: 18px; font-weight: bold; border-radius: 8px;" @click="addSymbolToFormula('2')">2</el-button>
          <el-button type="info" style="height: 50px; font-size: 18px; font-weight: bold; border-radius: 8px;" @click="addSymbolToFormula('3')">3</el-button>

          <el-button type="info" style="height: 50px; font-size: 18px; font-weight: bold; border-radius: 8px; grid-column: span 2;" @click="addSymbolToFormula('0')">0</el-button>
                    <el-button type="info" style="height: 50px; font-size: 18px; font-weight: bold; border-radius: 8px;" @click="addSymbolToFormula('.')">.</el-button>
          <el-button type="primary" size="small" style="height: 50px; font-size: 18px; font-weight: bold; border-radius: 8px;" @click="clearFormula">清空</el-button>
        </div>
      </div>
    </div>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="formulaDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="confirmFormula">确定</el-button>
      </span>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, onMounted, watch, reactive, toRefs, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Calculator} from '@icon-park/vue-next';
import { getMonthReportField, addTemplateItem, updateTemplateItem, listTemplateItem, delTemplateItem } from '@/api/finance/closing_template_amazon.js'
import { addFinClosingTemplate, updateFinClosingTemplate,initTemplateItem } from '@/api/finance/closing_template.js'
import { listAll as listSubjects } from '@/api/finance/subjects.js'
import { listVoucherTypes } from '@/api/finance/voucher_type'
import finStore from '@/hooks/store/useFinanceStore.js'
import marketApi from '@/api/amazon/market/marketApi.js'
import AddSubjectDialog from './add_subject_dialog.vue'
const props = defineProps({
  selectedTemplate: {
    type: Object,
    default: null
  }
})
const state = reactive({
    marketList:[]
});
const {marketList} = toRefs(state);

const reportFields = ref([])
const subjects = ref([])
const loading = ref(false)
const batchSaving = ref(false)
const allAmountFields = ref([])

const addSubjectVisible = ref(false)

// 公式编辑相关变量
const formulaDialogVisible = ref(false)
const currentFormula = ref('')
const selectedField = ref('')
let currentSubject = null

// 按科目分组的数据
const subjectGroups = computed(() => {
  // 直接返回所有报告字段，不进行分组
  return reportFields.value.map(field => {
    const subject = subjects.value.find(s => s.subjectId === field.subjectId)
    
    return {
      subjectId: field.subjectId,
      subjectCode: subject?.subjectCode || '',
      subjectName: subject?.subjectName || '',
      summary: field.summary || '',
      direction: field.direction || '',
      amountField: field.amountField || '',
      id: field.id,
      saving: field.saving || false
    }
  })
})

const form = ref({
  voucherType: '记',
  voucherNo: '001',
  country: null,
  voucherDate: new Date().toISOString().split('T')[0]
})

const voucherTypeList = ref([])

// 获取报告字段
const fetchReportFields = async () => {
  loading.value = true
  try {
    const response = await getMonthReportField()
    if (response.code === 200) {
      // 存储所有可用的模板字段
      allAmountFields.value = response.data
    } else {
      ElMessage.error('获取报告字段失败')
    }
  } catch (error) {
    ElMessage.error('获取报告字段失败')
  } finally {
    loading.value = false
  }
}
async function getMarketData(){
  const groupid = await finStore.getCurrentTenantId()
		marketApi.getMarketByGroup({'groupid':groupid}).then((res)=>{
        if(res.data&&res.data.length>0){
          state.marketList=res.data;
          form.value.country = res.data[0].market;
        }
		})
}

// 加载凭证字列表
async function loadVoucherTypes() {
  try {
    const groupid = await finStore.getCurrentTenantId()
    const res = await listVoucherTypes({ groupid, pageNum: 1, pageSize: 100 })
    voucherTypeList.value = res.rows || []
  } catch (error) {
    console.error('加载凭证字列表失败', error)
  }
}
// 获取科目数据
const fetchSubjects = async () => {
  try {
    const groupid = await finStore.getCurrentTenantId()
    const response = await listSubjects({ groupid, status: 1 })
    if (response.code === 200) {
      subjects.value = response.data
    } else {
      ElMessage.error('获取科目数据失败')
    }
  } catch (error) {
    ElMessage.error('获取科目数据失败')
  }
}

// 获取已有的模板数据
const fetchExistingTemplateItems = async () => {
  try {
    const response = await listTemplateItem({
      closingTemplateId: props.selectedTemplate.id
    })
    if (response.code === 200) {
      const existingItems = response.rows || []
      // 清空现有数据
      reportFields.value = []
      
      // 为每个模板字段添加对应的科目数据
      existingItems.forEach(item => {
        reportFields.value.push({
          amountField: item.amountField,
          subjectId: item.subjectId,
          summary: item.summary,
          direction: item.direction,
          id: item.id,
          saving: false
        })
      })
    }
  } catch (error) {
    console.error('获取已有的模板数据失败', error)
  }
}

// 已选择的科目ID（用于传递给子组件）
const selectedSubjectIds = computed(() => {
  return [...new Set(reportFields.value.map(field => field.subjectId).filter(Boolean))]
})

// 打开新增科目对话框
const openAddSubjectDialog = () => {
  addSubjectVisible.value = true
}
const handleInitItem = async () => {
  try {
    await ElMessageBox.confirm(
      '<div style="max-width: 300px;">' +
      '<div style="font-weight:bold; margin-bottom:8px;">初始化将重置所有科目设置到初始状态，之前的设置将会丢失，确定继续吗？</div>' +
      '<div style="font-weight:bold; margin-bottom:8px;">币种与会计科目结尾对应关系</div>' +
      '<div>CAD → 01</div>' +
      '<div>USD → 02</div>' +
      '<div>EUR → 03</div>' +
      '<div>GBP → 04</div>' +
      '<div>PLN → 05</div>' +
      '<div>SEK → 06</div>' +
      '<div>TRY → 07</div>' +
      '<div style="margin-top:8px;">系统自动根据币种匹配对应会计科目结尾</div>' +
      '</div>',
      '确认初始化',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
        dangerouslyUseHTMLString: true
      }
    )
    initItem()
  } catch {
    // 用户取消操作
  }
}

const initItem= async ()=>{
  if (!props.selectedTemplate || !props.selectedTemplate.id) {
    ElMessage.warning('请选择模板')
    return
  }
  
  loading.value = true
  try {
    const templateData = {
      id: props.selectedTemplate.id,
      name:  form.value.name,
      ftype: props.selectedTemplate.ftype,
      groupid: await finStore.getCurrentTenantId(),
      country: form.value.country,
      voucherType: form.value.voucherType
    }

    let templateResponse
    if (props.selectedTemplate.id) {
      templateResponse = await updateFinClosingTemplate(templateData)
    } else {
      templateResponse = await addFinClosingTemplate(templateData)
    }

    if (templateResponse.code !== 200) {
      ElMessage.error('保存模板失败')
      return
    }
    await initTemplateItem(props.selectedTemplate.id);
    await loadExistingTemplateItems()
    ElMessage.success('初始化成功')
  } catch (error) {
    console.error('初始化失败', error)
    ElMessage.error('初始化失败')
  } finally {
    loading.value = false
  }
}
// 处理新增科目
const handleAddSubjects = (subjectIds) => {
  // 直接添加到subjectGroups中，避免刷新表格
  subjectIds.forEach(subjectId => {
    const subject = subjects.value.find(s => s.subjectId === subjectId)
    if (subject) {
      // 手动添加到reportFields，这样subjectGroups会自动更新
      reportFields.value.push({
        amountField: '',
        subjectId: subjectId,
        summary: '',
        direction: '',
        saving: false
      })
    }
  })
}

// 删除科目
const deleteSubject = (subject) => {
  // 显示确认对话框
  ElMessageBox.confirm('确定要删除这个科目吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      // 从reportFields中移除对应的记录
      const index = reportFields.value.findIndex(item => item.id === subject.id)
      if (index > -1) {
        // 检查是否有id，如果有则调用后台API删除
        const field = reportFields.value[index]
        if (field.id) {
          await delTemplateItem(field.id)
        }
        // 从前端移除
        reportFields.value.splice(index, 1)
        ElMessage.success('删除成功')
      }
    } catch (error) {
      console.error('删除失败', error)
      ElMessage.error('删除失败')
    }
  }).catch(() => {
    // 取消删除
  })
}

// 处理模板字段选择变化
const handleFieldChange = (subject) => {
  // 移除当前记录
  reportFields.value = reportFields.value.filter(field => field.id !== subject.id)
  
  // 如果有amountField，更新记录
  if (subject.amountField) {
    reportFields.value.push({
      amountField: subject.amountField,
      subjectId: subject.subjectId,
      summary: subject.summary,
      direction: subject.direction,
      id: subject.id,
      saving: false,
      isFormula: true
    })
  }
}

// 处理公式输入变化
const handleFormulaChange = (subject) => {
  const index = reportFields.value.findIndex(field => field.id === subject.id)
  if (index > -1) {
    reportFields.value[index].amountField = subject.amountField
  }
}

// 打开公式编辑对话框
const openFormulaDialog = (subject) => {
  currentSubject = subject
  // 优先使用amountField
  if (subject.amountField) {
    currentFormula.value = subject.amountField
  } else {
    currentFormula.value = ''
  }
  formulaDialogVisible.value = true
}

// 直接添加字段到公式
const addFieldToFormulaDirectly = (field) => {
  if (field) {
    // 确保field是字符串
    const fieldValue = String(field)
    
    if (currentFormula.value) {
      // 获取当前公式的最后一个字符
      const lastChar = currentFormula.value.slice(-1)
      // 检查最后一个字符是否是运算符
      const operators = ['+', '-', '*', '/']
      if (!operators.includes(lastChar)) {
        // 如果不是运算符，默认添加一个+号
        currentFormula.value += '+'
      }
    }
    // 使用反引号将变量名包裹起来，支持包含空格的变量名
    currentFormula.value += fieldValue 
  }
}

// 添加计算符号到公式
const addSymbolToFormula = (symbol) => {
  currentFormula.value += symbol
}

// 清空公式
const clearFormula = () => {
  currentFormula.value = ''
}

// 确认公式编辑
const confirmFormula = () => {
  if (currentSubject) {
    currentSubject.amountField = currentFormula.value
    handleFormulaChange(currentSubject)
  }
  formulaDialogVisible.value = false
  currentSubject = null
}

// 保存单个模板字段
const handleSaveField = async (row) => {
  try {
    if (!row.subjectId) {
      ElMessage.warning('请选择科目ID')
      return
    }
    if (!props.selectedTemplate || !props.selectedTemplate.id) {
      ElMessage.warning('请选择模板')
      return
    }
    
    row.saving = true
    const now = new Date().toISOString()
    const templateItem = {
      amountField: row.amountField,
      subjectId: row.subjectId,
      summary: row.summary,
      direction: row.direction,
      closingTemplateId: props.selectedTemplate.id,
      createdTime: row.id ? undefined : now,
      updatedTime: now
    }
    
    let response
    if (row.id) {
      // 修改
      templateItem.id = row.id
      response = await updateTemplateItem(templateItem)
    } else {
      // 新增
      response = await addTemplateItem(templateItem)
    }
    
    if (response.code === 200) {
      ElMessage.success('保存成功')
      if (!row.id) {
        row.id = response.data.id
      }
    } else {
      ElMessage.error('保存失败')
    }
  } catch (error) {
    ElMessage.error('保存失败')
  } finally {
    row.saving = false
  }
}

// 保存科目及其模板字段
const saveSubject = async (subject) => {
  try {
    if (!subject.subjectId) {
      ElMessage.warning('请选择科目ID')
      return
    }
    if (!props.selectedTemplate || !props.selectedTemplate.id) {
      ElMessage.warning('请选择模板')
      return
    }
    if (!subject.amountField) {
      ElMessage.warning('请至少添加一个模板字段')
      return
    }
    
    let successCount = 0
    const now = new Date().toISOString()
    
    // 为科目创建一个模板条目（使用amountField）
    // 保存科目对应的模板字段
    const templateItem = {
      amountField: subject.amountField,
      subjectId: subject.subjectId,
      summary: subject.summary,
      direction: subject.direction,
      closingTemplateId: props.selectedTemplate.id,
      createdTime: now,
      updatedTime: now
    }
    
    // 查找是否已存在该记录
    const existingField = reportFields.value.find(f => 
      f.id === subject.id
    )
    
    let response
    if (existingField && existingField.id) {
      // 修改
      templateItem.id = existingField.id
      response = await updateTemplateItem(templateItem)
    } else {
      // 新增
      response = await addTemplateItem(templateItem)
    }
    
    if (response.code === 200) {
      successCount++
    }
    
    ElMessage.success(`成功保存 ${successCount} 条记录`)
  } catch (error) {
    ElMessage.error('保存失败')
  }
}

// 批量保存
const handleBatchSave = async () => {
  return new Promise(async (resolve, reject) => {
    if (subjectGroups.value.length === 0) {
      ElMessage.warning('请至少选择一个科目')
      resolve()
      return
    }
    
    if (!props.selectedTemplate || !props.selectedTemplate.id) {
      ElMessage.warning('请选择模板')
      resolve()
      return
    }
    
    batchSaving.value = true
    try {
      // 1. 保存template表数据
      const templateData = {
        id: props.selectedTemplate.id,
        name:  form.value.name,
        ftype: props.selectedTemplate.ftype,
        groupid: await finStore.getCurrentTenantId(),
        country: form.value.country,
        voucherType: form.value.voucherType
      }
      
      let templateResponse
      if (props.selectedTemplate.id) {
        templateResponse = await updateFinClosingTemplate(templateData)
      } else {
        templateResponse = await addFinClosingTemplate(templateData)
      }
      
      if (templateResponse.code !== 200) {
        ElMessage.error('保存模板失败')
        resolve()
        return
      }
      
      const templateId = (templateResponse.data?.id) || props.selectedTemplate.id
      if (!templateId) {
        ElMessage.error('获取模板ID失败')
        resolve()
        return
      }
      
      // 2. 保存item表数据
      let successCount = 0
      const now = new Date().toISOString()
      
      // 遍历每个科目
      for (const subject of subjectGroups.value) {
        if (!subject.amountField) continue
        
        // 为科目创建一个模板条目（使用amountField）
        // 保存科目对应的模板字段
        const templateItem = {
          amountField: subject.amountField,
          subjectId: subject.subjectId,
          summary: subject.summary,
          direction: subject.direction,
          closingTemplateId: templateId,
          createdTime: now,
          updatedTime: now
        }
        
        // 查找是否已存在该记录
        const existingField = reportFields.value.find(f => 
          f.id === subject.id
        )
        
        let response
        if (existingField && existingField.id) {
          // 修改
          templateItem.id = existingField.id
          response = await updateTemplateItem(templateItem)
        } else {
          // 新增
          response = await addTemplateItem(templateItem)
        }
        
        if (response.code === 200) {
          successCount++
        }
      }
      ElMessage.success(`成功保存 ${successCount} 条记录`)
      resolve()
    } catch (error) {
      console.error('批量保存失败', error)
      ElMessage.error('批量保存失败')
      resolve()
    } finally {
      batchSaving.value = false
    }
  })
}



// 加载已有的模板数据
const loadExistingTemplateItems = async () => {
    // 初始化时获取模板字段
  fetchReportFields()
  if (!props.selectedTemplate || !props.selectedTemplate.id) {
    ElMessage.warning('请选择模板')
    return
  }
  form.value.name = props.selectedTemplate.name;
  // 更新表单数据
  if (props.selectedTemplate.voucherType) {
    form.value.voucherType = props.selectedTemplate.voucherType
  }
  if (props.selectedTemplate.country) {
    form.value.country = props.selectedTemplate.country
  }
  
  try {
    const response = await listTemplateItem({
      closingTemplateId: props.selectedTemplate.id
    })
    if (response.code === 200) {
      // 优先使用response.rows，如果没有则使用response.data
      const existingItems = response.rows || response.data || []
      // 清空现有数据
      reportFields.value = []
      
      // 为每个模板字段添加对应的科目数据
      existingItems.forEach(item => {
        reportFields.value.push({
          amountField: item.amountField,
          subjectId: item.subjectId,
          summary: item.summary,
          direction: item.direction,
          id: item.id,
          saving: false
        })
      })
    }
  } catch (error) {
    console.error('获取已有的模板数据失败', error)
    ElMessage.error('加载模板数据失败')
  }
}

// 导出方法
defineExpose({
  handleBatchSave,
  loadExistingTemplateItems
})

onMounted(() => {
  fetchSubjects()
  getMarketData()
  loadVoucherTypes()
})
</script>
<style>
.box-card, .box-card .el-card__body{
  padding: 0;
}
</style>
<style scoped>

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  flex-wrap: wrap;
  gap: 10px;
}

.voucher-type-info {
  display: flex;
  align-items: center;
  gap: 5px;
}

.voucher-type-label,
.voucher-no-label,
.voucher-date-label {
  font-size: 14px;
  font-weight: 500;
}

.voucher-date-info {
  display: flex;
  align-items: center;
  gap: 5px;
}

/* .el-table {
   margin-top: 20px; 
} */

.account-option {
  display: flex;
  align-items: center;
}

.account-option span:first-child {
  margin-right: 10px;
  font-weight: 500;
}
</style>