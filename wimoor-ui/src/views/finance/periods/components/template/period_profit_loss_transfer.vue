<template>
  <div class="period-profit-loss-transfer">
    <el-card class="box-card" shadow="never">
      <div class="card-body">
        <!-- 结转周期 -->
        <div class="form-row">
          <label class="form-label label-w88">
            结转周期
            <el-tooltip content="选择按月或按年结转损益" placement="top">
              <el-icon class="help-icon"><QuestionFilled /></el-icon>
            </el-tooltip>
            ：
          </label>
          <el-radio-group v-model="form.transferCycle">
            <el-radio label="0">按月结转损益</el-radio>
            <el-radio label="1">按年结转损益</el-radio>
          </el-radio-group>
        </div>

        <!-- 结转方式 -->
        <div class="form-row mb14">
          <label class="form-label label-w298">当有新的未结转损益金额时，默认的结转方式：</label>
          <el-radio-group v-model="form.transferMethod">
            <el-radio label="0">追加结转</el-radio>
            <el-radio label="1">重新结转</el-radio>
          </el-radio-group>
        </div>

        <!-- 凭证设置 -->
        <section class="form-section">
          <div class="section-title">凭证设置</div>
          <div class="section-content">
            <div class="form-row-wrap">
              <div class="form-row-inline mb14">
                <label class="form-label label-w88">凭证日期：</label>
                <el-date-picker
                  v-model="form.voucherDate"
                  type="date"
                  value-format="YYYY-MM-DD"
                  placeholder="请选择日期"
                  style="width: 180px; height: 32px;"
                />
              </div>
              <div class="form-row-inline mb14">
                <label class="form-label label-w60">凭证字：</label>
                <el-select v-model="form.voucherType" placeholder="记" style="width: 80px; height: 32px;">
                  <el-option v-for="item in voucherTypeList" :key="item.id" :label="item.name" :value="item.name"></el-option>
                </el-select>
              </div>
              <div class="form-row-inline mb14">
                <label class="form-label label-w88">凭证摘要：</label>
                <el-input v-model="form.summary" placeholder="结转本期损益" style="width: 200px;" />
              </div>
            </div>
            <div class="form-row mb34">
              <label class="form-label label-w88">凭证分类：</label>
              <el-radio-group v-model="form.voucherClassify">
                <el-radio label="1">收益与损益分开结转</el-radio>
                <el-radio label="-1">收益与损益同时结转</el-radio>
              </el-radio-group>
              <span class="radio-tip">分别生成收益凭证与损益凭证</span>
            </div>
          </div>
        </section>

        <!-- 借贷方设置 -->
        <section class="form-section">
          <div class="section-title">借贷方设置</div>
          <div class="form-row">
            <label class="form-label label-w314">
              当待结转损益科目的余额与科目方向不一致时
              <el-tooltip content="选择结转时金额方向的处理方式" placement="top">
                <el-icon class="help-icon"><QuestionFilled /></el-icon>
              </el-tooltip>
              ：
            </label>
            <el-radio-group v-model="form.directionHandling">
              <el-radio label="false">按科目方向反向结转</el-radio>
              <el-radio label="true">按金额正数结转</el-radio>
            </el-radio-group>
          </div>
        </section>

        <!-- 本年利润处理 -->
        <section class="form-section">
          <div class="section-title">本年利润处理</div>
          <div class="form-row">
            <label class="form-label label-w184">"损益类科目"的结转科目：</label>
            <el-select
              v-model="form.profitLossSubject"
              placeholder="请选择科目"
              filterable
              style="width: 250px;"
            >
              <el-option
                v-for="item in subjectList"
                :key="item.subjectCode"
                :label="`${item.subjectCode} ${item.subjectName}`"
                :value="item.subjectCode"
              />
            </el-select>
            <el-button type="primary" link class="btn-detail-subject" @click="openDetailSubject('profitLoss')">按明细科目指定</el-button>
          </div>
          <div class="form-row">
            <label class="form-label label-w184">"本年利润"的结转科目：</label>
            <el-select
              v-model="form.currentYearProfitSubject"
              placeholder="请选择科目"
              filterable
              style="width: 250px;"
            >
              <el-option
                v-for="item in subjectList"
                :key="item.subjectCode"
                :label="`${item.subjectCode} ${item.subjectName}`"
                :value="item.subjectCode"
              />
            </el-select>
          </div>
        </section>

        <!-- 以前年度损益处理 -->
        <section class="form-section">
          <div class="section-title">以前年度损益处理</div>
          <div class="form-row">
            <label class="form-label label-w254">"以前年度损益调整"科目：</label>
            <el-select
              v-model="form.priorYearAdjustmentSubject"
              placeholder="请选择科目"
              filterable
              style="width: 250px;"
            >
              <el-option
                v-for="item in subjectList"
                :key="item.subjectCode"
                :label="`${item.subjectCode} ${item.subjectName}`"
                :value="item.subjectCode"
              />
            </el-select>
          </div>
          <div class="form-row">
            <label class="form-label label-w254">"以前年度损益调整"科目的结转科目：</label>
            <el-select
              v-model="form.priorYearAdjustTransferSubject"
              placeholder="请选择科目"
              filterable
              style="width: 250px;"
            >
              <el-option
                v-for="item in subjectList"
                :key="item.subjectCode"
                :label="`${item.subjectCode} ${item.subjectName}`"
                :value="item.subjectCode"
              />
            </el-select>
          </div>
        </section>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, watch } from 'vue'
import { ElMessage } from 'element-plus'
import { QuestionFilled } from '@element-plus/icons-vue'
import { addFinClosingTemplate, updateFinClosingTemplate } from '@/api/finance/closing_template.js'
import { getProfitLossConfigByTemplateId, addProfitLossConfig, updateProfitLossConfig } from '@/api/finance/closing_template_profit_loss.js'
import { listAll } from '@/api/finance/subjects.js'
import { listVoucherTypes } from '@/api/finance/voucher_type'
import finStore from '@/hooks/store/useFinanceStore.js'

const props = defineProps({
  selectedTemplate: {
    type: Object,
    default: null
  }
})

const emit = defineEmits(['save', 'cancel'])

const saving = ref(false)
const subjectList = ref([])
const voucherTypeList = ref([])
const profitLossConfigId = ref(null) // 存储结转损益配置ID

// 默认科目编码配置
const defaultSubjectCodes = {
  profitLossSubject: '3103',
  currentYearProfitSubject: '310415',
  priorYearAdjustmentSubject: '6000',
  priorYearAdjustTransferSubject: '310415'
}

const form = reactive({
  transferCycle: '0',
  transferMethod: '0',
  voucherDate: new Date().toISOString().split('T')[0],
  voucherType: '记',
  summary: '结转本期损益',
  voucherClassify: '1',
  directionHandling: 'false',
  profitLossSubject: '3103',
  currentYearProfitSubject: '310415',
  priorYearAdjustmentSubject: '6000',
  priorYearAdjustTransferSubject: '310415'
})

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

// 加载会计科目列表
async function loadSubjects() {
  try {
    const groupid = await finStore.getCurrentTenantId()
    const res = await listAll({ groupid, status: 1 })
    if (res.data && res.data.length > 0) {
      subjectList.value = res.data
      
      // 科目加载完成后，根据编码设置默认值（仅在没有已保存配置时）
      if (!props.selectedTemplate || !props.selectedTemplate.id) {
        setDefaultSubjects()
      }
    }
  } catch (error) {
    console.error('加载科目列表失败', error)
  }
}

// 根据编码设置默认科目
function setDefaultSubjects() {
  for (const [field, code] of Object.entries(defaultSubjectCodes)) {
    if (!form[field]) {
      form[field] = code
    }
  }
}

// 加载已有的模板数据
const loadExistingTemplateItems = async () => {
  if (!props.selectedTemplate || !props.selectedTemplate.id) {
    return
  }

  const tpl = props.selectedTemplate

  // 加载模板基础字段
  if (tpl.voucherType) {
    form.voucherType = tpl.voucherType
  }
  if (tpl.voucherClass !== undefined && tpl.voucherClass !== null) {
    form.voucherClassify = String(tpl.voucherClass)
  }

  // 加载结转损益专用配置
  try {
    const res = await getProfitLossConfigByTemplateId(tpl.id)
    if (res.data) {
      const config = res.data
      profitLossConfigId.value = config.id
      
      if (config.transferCycle !== undefined && config.transferCycle !== null) {
        form.transferCycle = String(config.transferCycle)
      }
      if (config.transferMethod !== undefined && config.transferMethod !== null) {
        form.transferMethod = String(config.transferMethod)
      }
      if (config.voucherDate) {
        form.voucherDate = config.voucherDate
      }
      if (config.summary) {
        form.summary = config.summary
      }
      if (config.directionHandling !== undefined && config.directionHandling !== null) {
        form.directionHandling = config.directionHandling ? 'true' : 'false'
      }
      if (config.profitLossSubjectCode) {
        form.profitLossSubject = config.profitLossSubjectCode
      }
      if (config.currentYearProfitSubjectCode) {
        form.currentYearProfitSubject = config.currentYearProfitSubjectCode
      }
      if (config.priorYearAdjustmentSubjectCode) {
        form.priorYearAdjustmentSubject = config.priorYearAdjustmentSubjectCode
      }
      if (config.priorYearAdjustTransferSubjectCode) {
        form.priorYearAdjustTransferSubject = config.priorYearAdjustTransferSubjectCode
      }
    }
  } catch (error) {
    console.error('加载结转损益配置失败', error)
  }
}

// 打开按明细科目指定对话框
function openDetailSubject(type) {
  ElMessage.info('按明细科目指定功能开发中')
}

// 保存模板
const handleBatchSave = async () => {
  saving.value = true
  try {
    if (!props.selectedTemplate || !props.selectedTemplate.name) {
      ElMessage.warning('请选择模板')
      saving.value = false
      return
    }

    const groupid = await finStore.getCurrentTenantId()

    // 1. 保存基础模板信息
    const templateData = {
      name: props.selectedTemplate.name,
      ftype: props.selectedTemplate.ftype,
      groupid: groupid,
      voucherType: form.voucherType,
      voucherClass: form.voucherClassify === '1' ? 1 : 0
    }

    let templateResponse
    if (props.selectedTemplate.id) {
      templateData.id = props.selectedTemplate.id
      templateResponse = await updateFinClosingTemplate(templateData)
    } else {
      templateResponse = await addFinClosingTemplate(templateData)
    }

    if (templateResponse.code === 200) {
      const templateId = props.selectedTemplate.id || templateResponse.data.id

      // 2. 保存结转损益专用配置
      const profitLossData = {
        templateId: templateId,
        groupid: groupid,
        transferCycle: form.transferCycle === '0' ? 0 : 1,
        transferMethod: form.transferMethod === '0' ? 0 : 1,
        voucherDate: form.voucherDate,
        summary: form.summary,
        directionHandling: form.directionHandling === 'true' ? 1 : 0,
        profitLossSubjectCode: form.profitLossSubject || null,
        currentYearProfitSubjectCode: form.currentYearProfitSubject || null,
        priorYearAdjustmentSubjectCode: form.priorYearAdjustmentSubject || null,
        priorYearAdjustTransferSubjectCode: form.priorYearAdjustTransferSubject || null
      }

      let profitLossResponse
      if (profitLossConfigId.value) {
        profitLossData.id = profitLossConfigId.value
        profitLossResponse = await updateProfitLossConfig(profitLossData)
      } else {
        profitLossResponse = await addProfitLossConfig(profitLossData)
      }

      if (profitLossResponse.code === 200) {
        ElMessage.success('保存成功')
        emit('save')
      } else {
        ElMessage.error('保存结转损益配置失败')
      }
    } else {
      ElMessage.error('保存模板失败')
    }
  } catch (error) {
    console.error('保存模板失败', error)
    ElMessage.error('保存失败')
  } finally {
    saving.value = false
  }
}

// 导出方法
defineExpose({
  loadExistingTemplateItems,
  handleBatchSave
})

onMounted(async () => {
  await loadSubjects()
  await loadVoucherTypes()
  if (props.selectedTemplate) {
    loadExistingTemplateItems()
  }
})

// 监听选中模板变化
watch(() => props.selectedTemplate, (newTemplate) => {
  if (newTemplate) {
    loadExistingTemplateItems()
  }
}, { deep: true })
</script>

<style>
.box-card, .box-card .el-card__body {
  padding: 0;
}
</style>

<style scoped>
.period-profit-loss-transfer {
  height: 100%;
}

.card-body {
  padding: 20px;
}

.form-row {
  display: flex;
  align-items: center;
  margin-bottom: 14px;
}

.form-row-wrap {
  display: flex;
  align-items: center;
  flex-wrap: wrap;
  gap: 10px;
}

.form-row-inline {
  display: flex;
  align-items: center;
  margin-bottom: 14px;
  margin-right: 20px;
}

.form-label {
  font-size: 14px;
  color: #606266;
  white-space: nowrap;
  display: flex;
  align-items: center;
  gap: 4px;
}

.label-w88 {
  min-width: 88px;
}

.label-w60 {
  min-width: 60px;
}

.label-w184 {
  min-width: 184px;
}

.label-w254 {
  min-width: 254px;
}

.label-w298 {
  min-width: 298px;
}

.label-w314 {
  min-width: 314px;
}

.help-icon {
  font-size: 14px;
  color: #909399;
  cursor: pointer;
}

.help-icon:hover {
  color: #409eff;
}

.mb14 {
  margin-bottom: 14px;
}

.mb34 {
  margin-bottom: 34px;
}

.form-section {
  margin-bottom: 10px;
}

.section-title {
  font-size: 15px;
  font-weight: 600;
  color: #303133;
  margin-bottom: 14px;
  padding-bottom: 8px;
  border-bottom: 1px solid #ebeef5;
}

.section-content {
  padding-left: 0;
}

.radio-tip {
  font-size: 13px;
  color: #909399;
  margin-left: 10px;
}

.btn-detail-subject {
  margin-left: 10px;
  font-size: 14px;
}

.el-radio-group {
  display: flex;
  gap: 20px;
}
</style>
