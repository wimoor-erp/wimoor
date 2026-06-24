<template>
  <el-dialog v-model="visible" title="凭证导入" width="60%">
    <el-row :gutter="20">
      <el-col :span="6">
        <el-upload
          :drag="true"
          action
          :http-request="handleUpload"
          :limit="1"
          :before-upload="beforeUpload"
          :show-file-list="true"
          :on-remove="handleRemove"
          accept=".xls,.xlsx"
          class="upload-container"
        >
          <el-icon class="font-large"><upload-filled /></el-icon>
          <div class="el-upload__text">拖拽文件到此处或 <em>点击上传</em></div>
        </el-upload>
          <div class="flex-between" >
           <el-button type="success" @click="downloadTemplate">下载模板</el-button>
        <el-button type="primary" :loading="uploadLoading" @click="submitUpload">上传文件</el-button>
        </div>
        <div class="upload-tips">
          <el-alert title="上传提示" type="info" :closable="false">
            1. 仅支持Excel文件(.xls,.xlsx)，大小不超过5MB
            2. 请先下载模板并按格式填写数据
            3. 上传后系统将自动处理并显示结果
          </el-alert>
        </div>
      
      </el-col>
      <el-col :span="18">
        <div class="upload-list-container">
          <div class="list-header">
            <h3>上传处理记录</h3>
            <el-button type="text" size="small" @click="refreshUploadList">
              <el-icon><Refresh /></el-icon>刷新
            </el-button>
          </div>
          <el-empty v-if="!uploadList||uploadList.length === 0" description="暂无上传记录"></el-empty>
          
    <el-table v-else v-loading="listLoading" :data="uploadList"  >
      <el-table-column label="凭证日期" align="center" show-overflow-tooltip prop="voucherDate"  >
        <template #default="scope">
          <span>{{ parseTime(scope.row.voucherDate, '{y}-{m}-{d}') }}</span>
          <div class="font-extraSmall">账套：{{ scope.row.groupName }}</div>
        </template>
      </el-table-column>
      <el-table-column label="临时凭证字号" align="center" prop="voucherNo"  width="160">
        <template #default="scope">
        <div>{{ scope.row.voucherType }}-{{ scope.row.voucherNo }}</div>
          <div class="font-extraSmall">{{scope.row.createdTime}}</div>
        </template>
      </el-table-column>
      <el-table-column label="科目" align="center" prop="subjectName" width="120">
        <template #default="scope">
          <div>{{ scope.row.subjectName }}</div>
          <div><span class="font-extraSmall">摘要：{{ scope.row.summary }}</span></div>
        </template>
      </el-table-column>
      <el-table-column label="借方金额" align="center" prop="debitAmount" width="120"/>
      <el-table-column label="贷方金额" align="center" prop="creditAmount" width="120"/>
      <el-table-column label="状态" align="center" prop="auditStatus" show-overflow-tooltip width="120">
        <template #default="scope">
          <span v-if="scope.row.status == 0">待处理</span>
          <span v-else-if="scope.row.status == 1">处理中</span>
          <span v-else-if="scope.row.status == 2">已完成</span>
          <span v-else-if="scope.row.status == 3">已出错</span>
          <span v-else-if="scope.row.status == 4">已作废</span>
          <span v-else>未知状态</span>
          <div v-if="scope.row.statusLog" class="font-extraSmall">
            {{ scope.row.statusLog }}
          </div>
        </template>
      </el-table-column>
    </el-table>
    <pagination
        v-show="total>0"
        :total="total"
        v-model:page="queryParams.pageNum"
        v-model:limit="queryParams.pageSize"
        @pagination="getList"
    />
        </div>
      </el-col>
      
    </el-row>
  
  </el-dialog>
</template>

<script setup>
import { ref, defineProps, defineEmits, toRefs } from 'vue';
import { ElMessage } from 'element-plus';
import { UploadFilled, Refresh } from '@element-plus/icons-vue';
import { uploadVoucherFile, downloadVoucherTemplate, getUploadHistory } from '@/api/finance/voucherUploadApi';
import finStore from "@/hooks/store/useFinanceStore.js";
const emit = defineEmits([ 'upload-success']);
const visible = ref(false);
const uploadLoading = ref(false);
const file = ref(null);
const uploadList = ref([]);
const total = ref(0)
const listLoading = ref(false);
const data = reactive({
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    voucherNo: null,
    voucherDate: null,
    summary: null,
    subjectName: null,
    preparerName: null,
    voucherDateStr: null,
    preparerBy: null,
    subjectId: null,
    tenantId: null,
  }
})

const { queryParams } = toRefs(data)
// 上传前校验
function beforeUpload(rawFile) {
  file.value = rawFile;
  const isExcel = rawFile.type === 'application/vnd.ms-excel' || rawFile.type === 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet';
  if (!isExcel) {
    ElMessage.error('只能上传Excel文件');
    return false;
  }
  const isLt5M = rawFile.size / 1024 / 1024 < 5;
  if (!isLt5M) {
    ElMessage.error('文件大小不能超过5MB');
    return false;
  }
  return true;
}

// 处理文件移除
function handleRemove() {
  file.value = null;
}

// 处理文件上传
function handleUpload() {}

// 提交上传
async function submitUpload() {
  if (!file.value) {
    ElMessage.warning('请选择文件后上传');
    return;
  }

  const formData = new FormData();
  formData.append('file', file.value);

  try {
    uploadLoading.value = true;
    let groupId=await finStore.getCurrentTenantId();
    const response = await uploadVoucherFile(formData,groupId);
    ElMessage.success('上传成功');
    emit('upload-success', response.data);
    // 上传成功后刷新列表
    refreshUploadList();
    file.value = null;
  } catch (error) {
    ElMessage.error('上传失败: ' + (error.msg || '网络错误'));
  } finally {
    uploadLoading.value = false;
  }
}

// 下载模板
  function downloadTemplate() {
 
    downloadVoucherTemplate({ftype:'voucher_template'});
    
}

// 刷新上传列表
async function refreshUploadList() {
  queryParams.value.pageNum = 1;
  getList()
}
async function getList() { 
  try {

    listLoading.value = true;
    queryParams.value.orderByColumn="created_time";
    queryParams.value.isAsc="desc";
    queryParams.value.groupid=await finStore.getCurrentTenantId();
    const response = await getUploadHistory(queryParams.value);
     uploadList.value = response.rows || [];
     total.value = response.total
  } catch (error) {
    ElMessage.error('获取上传记录失败: ' + (error.msg || '网络错误'));
  } finally {
    listLoading.value = false;
  }
}
 
function show() {
  visible.value = true;
  refreshUploadList();
}
defineExpose({
  show
});
</script>

<style scoped>
.upload-container {
  margin-bottom: 20px;
}
.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}
.font-large {
  font-size: 24px;
}
.upload-tips {
  margin-top: 15px;
}
.upload-list-container {
  height: 100%;
  display: flex;
  flex-direction: column;
}
.list-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
}
.list-header h3 {
  margin: 0;
  font-size: 16px;
}
</style>