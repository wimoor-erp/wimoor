<template>
  <div class="feishu-table-record">
    <!-- 模板信息和表格类型选择 -->
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
            <el-select v-model="form.country" placeholder="选择国家" style="width: 90px; text-align: center; height: 30px; border-radius: 0; margin-right: 10px;">
              <el-option  key="" label="" value=""></el-option>
                <el-option v-for="item in marketList" :key="item.market" :label="item.name" :value="item.market"></el-option>
            </el-select>

          </div>
          <div class="header-actions">
            <el-select v-model="selectType" @change="handleTable()" placeholder="请选择表格类型" style="width: 200px; margin-right: 10px;">
              <el-option v-for="item in typeList" :key="item.id" :label="item.name" :value="item.id"></el-option>
            </el-select>
            <el-button :icon="Setting" link @click="tableDataRef.show()"></el-button>
          </div>
        </div>
      </template>

      <!-- Filter 组件 -->
      <el-card title="筛选条件" class="filter-card" shadow="never">
        <feishu-table-record-filter 
          :fields="filterFields"
          @search="handleSearch"
          :model-value="currentFilter"
          @update:model-value="handleFilterUpdate"
          @filter-generated="handleFilterGenerated" 
        />
      </el-card>

      <!-- 字段映射配置 -->
      <el-card title="字段映射配置" class="filter-card" shadow="never" style="margin-top: 10px;margin-bottom:5px">
        <el-descriptions :column="5" border direction="vertical">
          <el-descriptions-item label="摘要字段" :label-width="120">
            <el-select 
              v-model="feishuConfig.summaryField" 
              placeholder="请选择摘要字段" 
              style="width: 180px;"
            >
              <el-option value="" label="请选择"></el-option>
              <el-option 
                v-for="field in fields" 
                :key="field.field_name" 
                :label="field.field_name" 
                :value="field.field_name" 
              />
            </el-select>
          </el-descriptions-item>
          <el-descriptions-item label="会计时间字段" :label-width="120">
            <el-select 
              v-model="feishuConfig.voucherDateField" 
              placeholder="请选择会计时间字段" 
              style="width: 180px;"
            >
              <el-option value="" label="请选择"></el-option>
              <el-option 
                v-for="field in fields" 
                :key="field.field_name" 
                :label="field.field_name" 
                :value="field.field_name" 
              />
            </el-select>
          </el-descriptions-item>
          <el-descriptions-item label="会计科目字段" :label-width="120">
            <el-select 
              v-model="feishuConfig.subjectField" 
              placeholder="请选择会计科目字段" 
              style="width: 180px;"
            >
              <el-option value="" label="请选择"></el-option>
              <el-option 
                v-for="field in fields" 
                :key="field.field_name" 
                :label="field.field_name" 
                :value="field.field_name" 
              />
            </el-select>
          </el-descriptions-item>
           <el-descriptions-item label="费用字段" :label-width="120">
            <el-select 
              v-model="feishuConfig.amountField" 
              placeholder="费用字段" 
              style="width: 180px;"
            >
              <el-option value="" label="请选择"></el-option>
              <el-option 
                v-for="field in fields" 
                :key="field.field_name" 
                :label="field.field_name" 
                :value="field.field_name" 
              />
            </el-select>
          </el-descriptions-item>
          <el-descriptions-item label="会计日期汇总类型" :label-width="100">
            <el-select 
              v-model="feishuConfig.datetype" 
              placeholder="请选择汇总类型" 
              style="width: 129px;"
            >
              <el-option :value="1" label="按月"></el-option>
            </el-select>
          </el-descriptions-item>
        </el-descriptions>
      </el-card>
      <el-card class="box-card" shadow="never">

        <el-table :data="reportFields" style="width: 100%" border  height="185">
          <el-table-column label="借方科目" width="400">
            <template #header="scope">
              <div style="display: flex; justify-content: space-between;">
                <span>借方科目</span>
                <el-button type="primary" size="small" @click="handleAddSubject(scope.row)">
                  <el-icon><Plus /></el-icon>
                  添加行</el-button>
              </div>
            </template>
            <template #default="scope">
              <el-select 
                v-model="scope.row.summary" 
                placeholder="请选择科目" 
                style="width: 100%"
                filterable
              >
                <el-option
                  v-for="subject in subjects"
                  :key="subject.subjectId"
                  :label="`${subject.subjectCode} ${subject.subjectName}`"
                  :value="subject.subjectId"
                />
              </el-select>
            </template>
          </el-table-column>
          <el-table-column label="贷方科目" width="400">
            <template #default="scope">
              <el-select 
                v-model="scope.row.subjectId" 
                placeholder="请选择科目" 
                style="width: 100%"
                filterable
              >
                <el-option
                  v-for="subject in subjects"
                  :key="subject.subjectId"
                  :label="`${subject.subjectCode} ${subject.subjectName}`"
                  :value="subject.subjectId"
                />
              </el-select>
            </template>
          </el-table-column>
          <el-table-column prop="amountField"  label="科目内容配对"  >
            <template #header="scope">
              <div style="display: flex; justify-content: space-between;">
                <span>科目内容配对</span>
               <el-tooltip style="float:right" content="点击选择数据表" placement="left">
              <template #content>
              <div >
                  <div class="tooltip-footer">
             通配符规则说明-必须完成“会计科目字段”配置 
          </div>
          
          <table class="rule-table">
            <thead>
              <tr>
                <th>您想匹配的内容</th>
                <th>填写规则示例</th>
              </tr>
            </thead>
            <tbody>
              <tr><td>所有内容</td><td><code>*</code></td></tr>
              <tr><td>固定值之一（逗号分隔）</td><td><code>apple, banana, cherry</code></td></tr>
              <tr><td>以 <code>.txt</code> 结尾</td><td><code>*.txt</code></td></tr>
              <tr><td>单个字符不同（如 data1.csv, data2.csv）</td><td><code>data?.csv</code></td></tr>
              <tr><td>数字编号（0-9）的文件</td><td><code>file[0-9].txt</code></td></tr>
              <tr><td>扩展名是 jpg 或 png</td><td><code>*.{jpg,png}</code></td></tr>
              <tr><td>首字母大写，后续任意小写字母</td><td><code>[A-Z][a-z]*</code></td></tr>
              <tr><td>首字符不是数字</td><td><code>[!0-9]*</code></td></tr>
              <tr><td>包含字面星号（如 <code>a*b</code>）</td><td><code>a\*b</code></td></tr>
            </tbody>
          </table>
          <div class="tooltip-footer">
            💡 提示：<code>*</code> 匹配任意长度字符串，<code>?</code> 匹配单个字符，<code>[ ]</code> 匹配字符集，<code>{ }</code> 多选一。
          </div>
        </div>
              </template>
               <el-icon><InfoFilled /></el-icon>  
            </el-tooltip>
              </div>
            </template>
            <template #default="scope">
              <div style="display: flex; justify-content: space-between;">
                <el-input v-model="scope.row.amountField" placeholder="请输入科目内容配对"></el-input>
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
      <!-- 查询结果表格 -->
      <div style="padding-top:10px;">
        <el-table scrollbar-always-on v-if="records&&records.items" v-loading="loading" height="400" :data="records.items" fit>
          <!-- <el-table-column type="selection" width="55" /> -->
          <el-table-column 
            :prop="field.field_name" 
            :width="getColumnWidth(field)"
            :min-width="getMinColumnWidth(field)"
            v-for="field in fields"  
            show-overflow-tooltip 
            :label="field.field_name" 
            resizable
          >
            <template #header>
              <el-tooltip placement="top" :content="field.field_name">
              <div class="text-omit-1">{{field.field_name}}</div>
              </el-tooltip>
            </template>
            <template #default="scope">
              <div v-if="JSON.stringify(scope.row.fields) !== '{}'">
                <div :class="field.field_name===formData.feedback ? 'text-success' : ''" v-if="hasFieldValue(scope.row.fields[field.field_name], field)">
                   <!-- 人员字段 (type: 11, ui_type: User) -->
                   <template v-if="field.type === 11 || field.ui_type === 'User'">
                     <el-avatar 
                       :size="18" 
                       :src="getPersonAvatar(scope.row.fields[field.field_name])"
                       :style="{ marginRight: '8px' }"
                     >
                       {{ getPersonName(scope.row.fields[field.field_name]) }}
                     </el-avatar>
                     <span>{{ getPersonName(scope.row.fields[field.field_name]) }}</span>
                   </template>
                   <!-- 日期字段 (type: 5, ui_type: DateTime) -->
                   <template v-else-if="field.type === 5 || field.ui_type === 'DateTime'">
                     {{ renderDateValue(scope.row.fields[field.field_name]) }}
                   </template>
                   <!-- 链接字段 (type: 15, ui_type: Url) -->
                   <template v-else-if="field.type === 15 || field.ui_type === 'Url'">
                     <el-link 
                       v-if="isLinkValue(scope.row.fields[field.field_name])" 
                       :href="getLinkUrl(scope.row.fields[field.field_name])" 
                       target="_blank"
                     >
                       {{ getLinkText(scope.row.fields[field.field_name]) }}
                     </el-link>
                     <span v-else>{{ scope.row.fields[field.field_name] }}</span>
                   </template>
                   <!-- 单选字段 (type: 3, ui_type: SingleSelect) -->
                   <template v-else-if="field.type === 3 || field.ui_type === 'SingleSelect'">
                     <el-tag :type="getTagType(scope.row.fields[field.field_name], field)" size="small">
                       {{ renderFieldValue(scope.row.fields[field.field_name]) }}
                     </el-tag>
                   </template>
                   <!-- 多选字段 (type: 13, ui_type: MultiSelect) 或数组值 -->
                   <template v-else-if="field.type === 13 || field.ui_type === 'MultiSelect' || isArrayValue(scope.row.fields[field.field_name])">
                     <el-tag 
                       v-for="(item, index) in getArrayItems(scope.row.fields[field.field_name])" 
                       :key="index" 
                       :type="getTagTypeForArray(item, field)" 
                       size="small"
                       :style="{ marginRight: '4px' }"
                     >
                       {{ item.text || item.name || item }}
                     </el-tag>
                   </template>
                   <!-- 其他类型 -->
                   <template v-else>
                     {{ renderFieldValue(scope.row.fields[field.field_name]) }}
                   </template>
                </div>
                <el-button size="small" type="primary"
                           v-if="field.field_name===formData.feedback &&scope.row.record_id&&scope.row.fields[field.field_name]!=='已同步'"
                           link
                           @click="handleFeedback(scope.row.record_id)">回写</el-button>
              </div>
              <div v-else> --</div>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </el-card>

    <TableData ref="tableDataRef"></TableData>
    <!-- 新增科目对话框 -->
    <AddSubjectDialog
        v-model="addSubjectVisible"
        :subjects="subjects"
        :selected-subject-ids="selectedSubjectIds"
        @confirm="handleAddSubjects"
    />

  </div>
</template>

<script setup>
import TableData from "@/views/finance/periods/components/template/feishu_table_bind.vue";
import FeishuTableRecordFilter from "./feishu_table_record_filter.vue";
import { onMounted, ref, reactive, toRefs, computed, watch } from 'vue';
import { ElMessage } from 'element-plus';
import {Plus, Setting,InfoFilled} from '@element-plus/icons-vue'; 
import feishuApi from '@/api/sys/tool/feishuApi.js';
import { listVoucherTypes } from '@/api/finance/voucher_type'
import { updateFinClosingTemplate, getFinClosingTemplate, addFinClosingTemplate } from '@/api/finance/closing_template.js';
import { getFeishuConfigByTemplateId, addFinClosingTemplateFeishu, updateFinClosingTemplateFeishu } from '@/api/finance/closing_template_feishu.js';
import { addTemplateItem, delTemplateItem, listTemplateItem, updateTemplateItem } from '@/api/finance/closing_template_item.js';
import { listAll as listSubjects } from '@/api/finance/subjects.js';
import finStore from '@/hooks/store/useFinanceStore.js'
import marketApi from '@/api/amazon/market/marketApi.js'
import {Calculator} from "@icon-park/vue-next";
import AddSubjectDialog from "@/views/finance/periods/components/template/add_subject_dialog.vue";
import { ElMessageBox } from 'element-plus';

const tableDataRef = ref(null);
let currentFilter = ref(null);

// 新增科目对话框显示状态
const addSubjectVisible = ref(false);

// 打开新增科目对话框
const openAddSubjectDialog = () => {
  addSubjectVisible.value = true;
};

// 科目列表
const subjects = ref([]);

// 报告字段列表（存储科目映射关系）
const reportFields = ref([]);

// 已选中的科目ID列表
const selectedSubjectIds = computed(() => {
  return reportFields.value.map(field => field.subjectId);
});

// 定义 props
const props = defineProps({
  selectedTemplate: {
    type: Object,
    default: null
  }
});

// 定义 emit
const emit = defineEmits(['filter-change', 'save']);


let state = reactive({
  typeList: [],
  dialogVisible: false,
  marketList:[],
  formData: { tableUrl: '', tableType: '' },
  fields: [],
  records: {},
  selectType: "",
  dialogRecordVisible: false,
  loading: false,
});

let {
  typeList,
  dialogVisible,
  formData,
  selectType,
  fields,
  records,
  marketList,
  dialogRecordVisible,
  loading,
} = toRefs(state);

// 表单数据（用于模板信息）
const voucherTypeList = ref([])
const form = ref({
  voucherType: '记',
  voucherNo: '001',
  country: null,
  name: ''
})

// 飞书字段映射配置
const feishuConfig = ref({
  summaryField: '',      // 摘要字段
  voucherDateField: '',  // 会计时间字段
  subjectField: '',      // 会计科目字段
  amountField: '',       // 费用字段
  datetype: 1           // 会计日期汇总类型：0按日，1按月，2单笔生成凭证
})

// 用于传递给 filter 组件的字段列表
const filterFields = computed(() => {
  return fields.value || [];
});

// 获取市场数据
async function getMarketData(){
  const groupid = await finStore.getCurrentTenantId()
		marketApi.getMarketByGroup({'groupid':groupid}).then((res)=>{
        if(res.data&&res.data.length>0){
          state.marketList=res.data;
          form.value.country = "";
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

// 处理 Filter 更新
function handleFilterUpdate(filter) {
  currentFilter.value = filter;
  // 保存 filter 到后端
  saveFilterToBackend(filter);
}

// 处理 Filter 生成
function handleFilterGenerated(filter) {
  currentFilter.value = filter;
  ElMessage.success('筛选条件已设置');
  // 保存 filter 到后端
  saveFilterToBackend(filter);
  // 重新查询数据
  handleRecord();
}
function handleSearch(){
  handleRecord();
}
// 加载模板字段列表
const loadReportFields = async () => {
  console.log('loadReportFields 开始执行');
  console.log('selectedTemplate:', props.selectedTemplate);

  if (!props.selectedTemplate || !props.selectedTemplate.id) {
    reportFields.value = [];
    currentFilter.value = null;
    console.log('selectedTemplate 为空或没有 id，清空 reportFields 和 filter');
    return;
  }

  try {
    console.log('调用 listTemplateItem，参数:', { closingTemplateId: props.selectedTemplate.id });
    const response = await listTemplateItem({ closingTemplateId: props.selectedTemplate.id });
    console.log('listTemplateItem 返回:', response);

    if (response && response.code === 200) {
      const data = response.rows || response.data || [];
      reportFields.value = data;
      console.log('reportFields 设置为:', data);
      if (!data || data.length === 0) {
        currentFilter.value = null;
        console.log('reportFields 为空，清空 filter');
      }
    } else {
      reportFields.value = [];
      currentFilter.value = null;
      console.log('response.code 不是 200，清空 reportFields 和 filter');
    }
  } catch (error) {
    console.error('加载模板字段失败:', error);
    reportFields.value = [];
    currentFilter.value = null;
  }
};

// 处理新增科目（点击添加按钮）
const handleAddSubject = (row) => {
  if (row && row.id) {
    const index = reportFields.value.findIndex(item => item.id === row.id);
    if (index > -1) {
      reportFields.value.splice(index + 1, 0, {
        summary: '',
        subjectId: '',
        direction: '',
        closingTemplateId: props.selectedTemplate.id,
      });
    } else {
      reportFields.value.push({
        summary: '',
        subjectId: '',
        direction: '',
        closingTemplateId: props.selectedTemplate.id,
      });
    }
  } else {
    reportFields.value.push({
      summary: '',
      subjectId: '',
      direction: '',
      closingTemplateId: props.selectedTemplate.id,
    });
  }
};

// 处理批量新增科目（从弹窗选择）
const handleAddSubjects = async () => {
  reportFields.value.push({
    summary: '',
    subjectId: '',
    direction: '',
    closingTemplateId: props.selectedTemplate.id,
  });

};

// 删除科目
const deleteSubject = (subject) => {
  ElMessageBox.confirm('确定要删除这个科目吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      const index = reportFields.value.findIndex(item => item.id === subject.id);
      if (index > -1) {
        const field = reportFields.value[index];
        if (field.id) {
          await delTemplateItem(field.id);
        }
        reportFields.value.splice(index, 1);
        ElMessage.success('删除成功');
      }
    } catch (error) {
      console.error('删除失败', error);
      ElMessage.error('删除失败');
    }
  }).catch(() => {
    // 取消删除
  });
};

// 保存 filter 到后端
function saveFilterToBackend(filter) {
  if (!props.selectedTemplate || !props.selectedTemplate.id) {
    return;
  }
  
  const filterJson = JSON.stringify(filter);
  
  // 保存到飞书配置表
  saveFeishuConfigToBackend(filterJson);
}

// 保存飞书配置到 fin_closing_template_feishu 表
function saveFeishuConfigToBackend(filterJson) {
  if (!props.selectedTemplate || !props.selectedTemplate.id) {
    ElMessage.warning('请选择模板');
    return;
  }
  
  const feishuConfigData = {
    templateid: props.selectedTemplate.id,
    feishuTableId: selectType.value ? parseInt(selectType.value) : null,
    filter: filterJson,
    summaryField: feishuConfig.value.summaryField || '',
    voucherDateField: feishuConfig.value.voucherDateField || '',
    subjectField: feishuConfig.value.subjectField || '',
    amountField: feishuConfig.value.amountField || '',
    datetype: feishuConfig.value.datetype || 0
  };
  
  console.log('保存飞书配置数据:', feishuConfigData);
  
  // 先查询是否已存在配置
  getFeishuConfigByTemplateId(props.selectedTemplate.id).then(res => {
    if (res.code === 200 && res.data) {
      // 已存在，更新
      updateFinClosingTemplateFeishu(feishuConfigData).then(res => {
        if (res.code === 200) {
          ElMessage.success('飞书配置更新成功');
          console.log('飞书配置更新成功');
        } else {
          ElMessage.error('更新飞书配置失败: ' + (res.msg || '未知错误'));
        }
      }).catch(err => {
        console.error('更新飞书配置失败:', err);
        ElMessage.error('更新飞书配置失败');
      });
    } else {
      // 不存在，新增
      addFinClosingTemplateFeishu(feishuConfigData).then(res => {
        if (res.code === 200) {
          ElMessage.success('飞书配置保存成功');
          console.log('飞书配置新增成功');
        } else {
          ElMessage.error('新增飞书配置失败: ' + (res.msg || '未知错误'));
        }
      }).catch(err => {
        console.error('新增飞书配置失败:', err);
        ElMessage.error('新增飞书配置失败');
      });
    }
  }).catch(err => {
    // 查询失败，尝试新增
    addFinClosingTemplateFeishu(feishuConfigData).then(res => {
      if (res.code === 200) {
        ElMessage.success('飞书配置保存成功');
        console.log('飞书配置新增成功');
      } else {
        ElMessage.error('新增飞书配置失败: ' + (res.msg || '未知错误'));
      }
    }).catch(err => {
      console.error('新增飞书配置失败:', err);
      ElMessage.error('新增飞书配置失败');
    });
  });
}

// 批量保存模板信息（包含名称、凭证字、国家等）
const handleBatchSave = async () => {
  if (!props.selectedTemplate || !props.selectedTemplate.id) {
    ElMessage.warning('请选择模板');
    return;
  }
  
  try {
    const filterJson = currentFilter.value ? JSON.stringify(currentFilter.value) : null;
    
    const templateData = {
      id: props.selectedTemplate.id,
      name: form.value.name,
      ftype: props.selectedTemplate.ftype,
      groupid: await finStore.getCurrentTenantId(),
      country: form.value.country,
      voucherType: form.value.voucherType
    };
    
    let templateResponse;
    if (props.selectedTemplate.id) {
      templateResponse = await updateFinClosingTemplate(templateData);
    } else {
      templateResponse = await addFinClosingTemplate(templateData);
    }
    
    if (templateResponse.code === 200) {
      // 保存科目映射关系
      await saveSubjectGroups();
      
      // 保存到飞书配置表
      saveFeishuConfigToBackend(filterJson);
      ElMessage.success('保存成功');
      emit('save', templateData);
    } else {
      ElMessage.error('保存失败');
    }
  } catch (error) {
    console.error('保存失败', error);
    ElMessage.error('保存失败');
  }
};

// 保存科目映射关系
const saveSubjectGroups = async () => {
  if (!props.selectedTemplate || !props.selectedTemplate.id) {
    return;
  }
  
  const now = new Date().toISOString();
  
  // 直接从 reportFields 读取数据，确保获取到最新的用户输入
  for (const field of reportFields.value) {
    const templateItem = {
      id: field.id,
      subjectId: field.subjectId,
      amountField: field.amountField || '',
      summary: field.summary || '',
      direction: field.direction,
      closingTemplateId: props.selectedTemplate.id,
      updatedTime: now
    };
    
    console.log('保存科目映射:', templateItem);
    
    try {
      if (field.id) {
        // 修改
        await updateTemplateItem(templateItem);
      } else {
        // 新增
        const response = await addTemplateItem(templateItem);
        if (response.code === 200) {
          field.id = response.data.id;
        }
      }
    } catch (error) {
      console.error('保存科目映射失败:', error);
      throw error;
    }
  }
};

// 处理记录查询
function handleRecord() {
  // 检查 formData 是否存在
  if (!state.formData) {
    console.warn('formData 为空，跳过请求');
    return;
  }
  
  const params = {
    url: state.formData.url || state.formData.tableUrl
  };
  
  // 如果有 filter，添加到请求参数中
  if (currentFilter.value) {
    params.filter = currentFilter.value;
  }else{
    params.filter = {"conjunction":"and","conditions":[]};
  }

  // 检查 url 是否存在
  if (!params.url) {
    console.warn('url 为空，跳过请求');
    return;
  }
  
  // 检查是否正在加载中，避免重复请求
  if (state.loading) {
    console.warn('正在加载中，跳过重复请求');
    return;
  }
  
  state.loading = true;
  feishuApi.getRecord(params).then(res => {
    state.loading = false;
    if (res.code === 200) {
      try {
        // 尝试解析数据
        state.records = typeof res.data === 'string' ? JSON.parse(res.data) : res.data;
          // 检查是否有 items
        if (!state.records) {
          state.records = { items: [] };
          return;
        }
        // 检查是否有 items
        if (!state.records.items || !Array.isArray(state.records.items)) {
          state.records = { items: [] };
          return;
        }
        
        // 去掉records中为空的行
        state.records.items = state.records.items.filter(item => {
          return item && item.fields && Object.keys(item.fields).length !== 0;
        });
        
      } catch (e) {
        console.error('数据解析失败:', e);
        state.records = { items: [] };
      }
    } else {
      console.warn('获取数据失败:', res.message);
      state.records = { items: [] };
    }
  }).catch(err => {
    console.error('请求失败:', err);
    state.records = { items: [] };
    state.loading = false;
  });
}

function handleTable() {
  let item = null;
  state.typeList.forEach(typeItem => {
    if (typeItem.id === state.selectType) {
      item = typeItem;
    }
  });
  
  // 检查 item 是否存在
  if (!item) {
    console.warn('未找到对应的表格类型');
    return;
  }
  
  state.formData = item;
  
  // 安全解析 fieldjson
  try {
    let fieldJson = JSON.parse(item.fieldjson);
    fields.value = fieldJson != null && fieldJson.items != null ? fieldJson.items : [];
  } catch (e) {
    console.error('解析 fieldjson 失败:', e);
    fields.value = [];
  }
  
  // 只在有有效 url 时调用 handleRecord
  if (item.url || item.tableUrl) {
    handleRecord();
  }
}

function loadTypeList() {
  state.loading = true;
  feishuApi.getTypeList()
      .then(res => {
        if (res.code === 200) {
          const data = res.data || [];
          // 使用 splice 保持响应式引用
          state.typeList.splice(0, state.typeList.length, ...data);
          state.formData = data[0];
          if(!state.selectType && data.length > 0){
            state.selectType = data[0].id;
          }
          handleTable();
        } else {
          ElMessage.error(res.message || '获取数据表类型列表失败');
          state.typeList.splice(0, state.typeList.length);
        }
      })
      .catch(err => {
        ElMessage.error('网络错误，请稍后重试');
        state.typeList.splice(0, state.typeList.length);
      })
      .finally(() => {
        state.loading = false;
      });
}

// 加载已有的模板数据
const loadExistingTemplateItems = async () => {
  if (!props.selectedTemplate || !props.selectedTemplate.id) {
    return;
  }

  // 初始化状态
  currentFilter.value = null;
  reportFields.value = [];
  feishuConfig.value = {
    summaryField: '',
    voucherDateField: '',
    subjectField: '',
    amountField: '',
    datetype: 0
  };

  form.value.name = props.selectedTemplate.name || '';
  if (props.selectedTemplate.voucherType) {
    form.value.voucherType = props.selectedTemplate.voucherType;
  }
  if (props.selectedTemplate.country) {
    form.value.country = props.selectedTemplate.country;
  }

  // 加载模板字段列表
  await loadReportFields();

  // 等待 typeList 加载完成
  while (typeList.value.length === 0) {
    await new Promise(resolve => setTimeout(resolve, 100));
  }

  // 从飞书配置表加载配置
  try {
    const feishuConfigRes = await getFeishuConfigByTemplateId(props.selectedTemplate.id);
    if (feishuConfigRes.code === 200 && feishuConfigRes.data) {
      // 加载 filter
      if (feishuConfigRes.data.filter) {
        try {
          currentFilter.value = JSON.parse(feishuConfigRes.data.filter);
        } catch (e) {
          currentFilter.value = null;
        }
      }else{
        currentFilter.value = null;
      }
      // 加载飞书表格ID
      console.log('飞书配置返回的 feishuTableId:', feishuConfigRes.data.feishuTableId);
      console.log('typeList:', typeList.value);
      if (feishuConfigRes.data.feishuTableId) {
        selectType.value = String(feishuConfigRes.data.feishuTableId);
        console.log('设置 selectType 为:', selectType.value);
        handleTable();
      }
      // 加载字段映射配置
      if (feishuConfigRes.data.summaryField) {
        feishuConfig.value.summaryField = feishuConfigRes.data.summaryField;
      }
      if (feishuConfigRes.data.voucherDateField) {
        feishuConfig.value.voucherDateField = feishuConfigRes.data.voucherDateField;
      }
      if (feishuConfigRes.data.subjectField) {
        feishuConfig.value.subjectField = feishuConfigRes.data.subjectField;
      }
      if (feishuConfigRes.data.amountField) {
        feishuConfig.value.amountField = feishuConfigRes.data.amountField;
      }
      if (feishuConfigRes.data.datetype !== undefined) {
        feishuConfig.value.datetype = feishuConfigRes.data.datetype;
      }
      return;
    }else{
        currentFilter.value = null;
    }
  } catch (e) {
    console.error('加载飞书配置失败:', e);
  }
  
  // 如果飞书配置表没有，从 template 对象中获取
  if (props.selectedTemplate.filter) {
    try {
      currentFilter.value = JSON.parse(props.selectedTemplate.filter);
    } catch (e) {
      currentFilter.value = null;
    }
  } else {
    currentFilter.value = null;
  }
  
  // 如果设置了表格类型，触发数据加载
  if (selectType.value) {
    handleTable();
  }
}

// 导出方法
defineExpose({
  handleBatchSave,
  loadExistingTemplateItems
})

onMounted(() => {
  loadTypeList();
  getMarketData();
  fetchSubjects();
  loadVoucherTypes();
});

// 监听 selectedTemplate 变化
watch(() => props.selectedTemplate, () => {
  loadExistingTemplateItems();
}, { deep: true });

// 根据飞书字段类型渲染不同的显示方式
function renderFieldValue(value, field) {
  if (!value) {
    return '';
  }

  // 公式/错误类型 (type:1) 且 value 是数组结构，如 {"type":1,"value":[{"text":"存在错误","type":"text"}]}
  if (typeof value === 'object' && value !== null && value.type === 1 && Array.isArray(value.value) && value.value.length > 0) {
    const firstItem = value.value[0];
    if (firstItem && firstItem.text) {
      return firstItem.text;
    }
  }

  // 日期类型：时间戳转换
  if (typeof value === 'number' && value > 1e10) {
    const date = new Date(value);
    return formatDate(date);
  }

  // 人员类型：数组，显示人员名称
  if (Array.isArray(value) && value.length > 0) {
    const person = value[0];
    if (person && person.name) {
      return person.name;
    }
    return JSON.stringify(value);
  }

  // 附件/链接类型：包含 link 和 text
  if (typeof value === 'object' && value !== null) {
    if (value.text) {
      return value.text;
    }
    if (value.link) {
      return '链接';
    }
    return JSON.stringify(value);
  }

  // 文本类型：直接返回
  return value;
}

// 判断是否为人员字段
function isPersonField(value) {
  if (!value) {
    return false;
  }
  
  // 人员字段是数组，且包含 name 属性
  if (Array.isArray(value) && value.length > 0) {
    const person = value[0];
    return person && (person.name || person.avatar_url);
  }
  
  return false;
}

// 获取人员头像
function getPersonAvatar(value) {
  if (!value || !Array.isArray(value) || value.length === 0) {
    return '';
  }
  const person = value[0];
  return person.avatar_url || '';
}

// 获取人员名称
function getPersonName(value) {
  if (!value || !Array.isArray(value) || value.length === 0) {
    return '';
  }
  const person = value[0];
  return person.name || '';
}

// 渲染日期值
function renderDateValue(value) {
  if (!value) {
    return '';
  }

  // 时间戳格式（毫秒）
  if (typeof value === 'number') {
    const date = new Date(value);
    return formatDate(date);
  }

  return value;
}

// 判断字段是否有值（用于控制不显示空值）
function hasFieldValue(value, field) {
  if (!value) {
    return false;
  }

  // 公式/错误类型 (type:1)
  if (typeof value === 'object' && value !== null && value.type === 1 && Array.isArray(value.value) && value.value.length > 0) {
    const firstItem = value.value[0];
    return !!(firstItem && firstItem.text);
  }

  // 日期类型
  if (typeof value === 'number' && value > 1e10) {
    return true;
  }

  // 数组类型（包括人员类型和普通数组，如[{"text":"YS项目部","type":"text"}]）
  if (Array.isArray(value) && value.length > 0) {
    const firstItem = value[0];
    return !!(firstItem && (firstItem.name || firstItem.avatar_url || firstItem.text));
  }

  // 链接类型
  if (typeof value === 'object' && value !== null) {
    if (value.link || value.text) {
      return true;
    }
    return false;
  }

  // 字符串空值检查
  if (typeof value === 'string' && value.trim() === '') {
    return false;
  }

  return true;
}

// 判断是否为链接值
function isLinkValue(value) {
  if (!value || typeof value !== 'object') {
    return false;
  }
  return value.link !== undefined;
}

// 获取链接URL
function getLinkUrl(value) {
  if (!value || typeof value !== 'object') {
    return '';
  }
  return value.link || '';
}

// 获取链接文本
function getLinkText(value) {
  if (!value || typeof value !== 'object') {
    return '';
  }
  return value.text || '链接';
}

// 渲染数组值（用于多选字段）
function renderArrayValue(value) {
  if (!value || !Array.isArray(value)) {
    return '';
  }
  
  // 如果是人员类型的数组
  if (value.length > 0 && value[0].name) {
    return value.map(item => item.name).join(', ');
  }
  
  // 如果是单选/多选选项类型的数组
  if (value.length > 0 && value[0].text) {
    return value.map(item => item.text).join(', ');
  }
  
  return value.join(', ');
}

// 根据字段配置获取 tag 类型
function getTagType(value, field) {
  if (!value || !field || !field.property || !field.property.options) {
    return '';
  }
  
  // 查找当前值对应的选项
  const options = field.property.options;
  let foundOption = null;
  
  if (Array.isArray(value)) {
    // 如果值是数组（多选），取第一个
    if (value.length > 0 && value[0].text) {
      foundOption = options.find(opt => opt.name === value[0].text);
    }
  } else if (typeof value === 'object' && value.text) {
    // 如果值是对象（单选）
    foundOption = options.find(opt => opt.name === value.text);
  } else {
    // 普通文本值
    foundOption = options.find(opt => opt.name === value);
  }
  
  // 根据颜色值返回对应的 tag 类型
  if (foundOption) {
    return getColorTagType(foundOption.color);
  }
  
  return '';
}

// 根据飞书颜色值返回 Element Plus tag 类型
function getColorTagType(color) {
  // 飞书颜色值对应关系：
  // 0: 灰色, 1: 红色, 2: 橙色, 3: 黄色, 4: 绿色, 5: 蓝色, 6: 紫色, 7: 粉色
  const colorMap = {
    0: '',        // 灰色 -> 默认
    1: 'danger',  // 红色 -> danger
    2: 'warning', // 橙色 -> warning
    3: 'warning', // 黄色 -> warning
    4: 'success', // 绿色 -> success
    5: 'primary', // 蓝色 -> primary
    6: '',        // 紫色 -> 默认
    7: ''         // 粉色 -> 默认
  };
  return colorMap[color] || '';
}

// 判断是否为数组值
function isArrayValue(value) {
  return Array.isArray(value) && value.length > 0;
}

// 根据字段类型获取列宽度
function getColumnWidth(field) {
  if (!field) return '';
  
  // 根据字段类型设置不同宽度
  const widthMap = {
    // 人员字段 - 需要显示头像和名称
    11: '150',           // User
    // 日期字段 - 需要显示完整日期时间
    5: '180',            // DateTime
    // 链接字段 - 可能较长
    15: '200',           // Url
    // 单选/多选字段 - 根据选项内容
    3: '',               // SingleSelect - 自适应
    13: '200',           // MultiSelect - 可能有多个选项
    // 数字字段 - 通常较短
    2: '100',            // Number
    // 复选框
    7: '80',             // Checkbox
    // 自动编号
    17: '120',           // AutoNumber
  };
  
  // 也可以根据 ui_type 判断
  const uiTypeWidthMap = {
    'User': '150',
    'DateTime': '180',
    'Url': '200',
    'SingleSelect': '',
    'MultiSelect': '200',
    'Text': '',
    'LongText': '300',
    'Number': '100',
    'Checkbox': '80',
  };
  
  // 优先使用 ui_type
  if (field.ui_type && uiTypeWidthMap[field.ui_type]) {
    return uiTypeWidthMap[field.ui_type];
  }
  
  // 其次使用 type
  if (field.type && widthMap[field.type]) {
    return widthMap[field.type];
  }
  
  // 默认不设置宽度，让表格自适应
  return '';
}

// 根据字段类型获取最小列宽度
function getMinColumnWidth(field) {
  if (!field) return '80';
  
  // 根据字段类型设置最小宽度
  const minWidthMap = {
    1: '120',            // Text
    2: '80',             // Number
    3: '100',            // SingleSelect
    5: '150',            // DateTime
    7: '60',             // Checkbox
    11: '120',           // User
    13: '150',           // MultiSelect
    15: '150',           // Url
    17: '100',           // AutoNumber
  };
  
  const uiTypeMinWidthMap = {
    'Text': '120',
    'LongText': '200',
    'Number': '80',
    'SingleSelect': '100',
    'DateTime': '150',
    'Checkbox': '60',
    'User': '120',
    'MultiSelect': '150',
    'Url': '150',
    'AutoNumber': '100',
  };
  
  // 优先使用 ui_type
  if (field.ui_type && uiTypeMinWidthMap[field.ui_type]) {
    return uiTypeMinWidthMap[field.ui_type];
  }
  
  // 其次使用 type
  if (field.type && minWidthMap[field.type]) {
    return minWidthMap[field.type];
  }
  
  // 默认最小宽度
  return '100';
}

// 获取数组项
function getArrayItems(value) {
  if (!Array.isArray(value)) {
    return [];
  }
  return value;
}

// 为数组项获取对应的 tag 类型
function getTagTypeForArray(item, field) {
  if (!field || !field.property || !field.property.options) {
    return '';
  }
  
  const options = field.property.options;
  const itemText = item.text || item.name || item;
  const foundOption = options.find(opt => opt.name === itemText);
  
  if (foundOption) {
    return getColorTagType(foundOption.color);
  }
  
  return '';
}

// 格式化日期
function formatDate(date) {
  const year = date.getFullYear();
  const month = String(date.getMonth() + 1).padStart(2, '0');
  const day = String(date.getDate()).padStart(2, '0');
  const hours = String(date.getHours()).padStart(2, '0');
  const minutes = String(date.getMinutes()).padStart(2, '0');
  const seconds = String(date.getSeconds()).padStart(2, '0');
  return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
}
</script>

<style>
.box-card, .box-card .el-card__body{
  padding: 0;
}
</style>
<style scoped>
.feishu-table-record {
  padding: 10px;
}

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

.header-actions {
  display: flex;
  align-items: center;
  gap: 10px;
}

.filter-card {
  margin-bottom: -10px;
  border-radius: 0px;
  border-top:none;
  border-left:none;
  border-right:none;
}

.text-success {
  color: #67c23a;
}
</style>