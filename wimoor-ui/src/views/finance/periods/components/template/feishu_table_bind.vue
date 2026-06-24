<template>
  <el-dialog v-model="visible" top="2vh"  title="绑定飞书表格" width="70%" destroy-on-close="true">
  <el-row :gutter="20" class="custom-row" align="middle">
  <el-col :span="10">
    <el-form  label-position="top">
      <el-form-item label="飞书表格URL">
        <el-input v-model="formData.tableUrl"  placeholder="请输入飞书多维表格地址栏URL链接" />

      </el-form-item>
      <el-form-item label="名称">
        <el-input v-model="formData.tableType"
                  placeholder="请输入数据表类型"
                  style="width: 300px;" />
        <el-button type="primary" @click="handleTableInfo" link>查询表面</el-button>
      </el-form-item>
      <el-form-item  >
      <el-button type="primary"
                 @click="getTableData"
                 :loading="loading">新增</el-button>
      </el-form-item>
    </el-form>
  </el-col>
    <div class="vertical-line "></div>
    <el-col :span="4" class="type-list">
      <p v-for="item in typeList" :key="item"
         @click="handleTable(item)"
         :class="item.active ? 'flex-between text item active' : 'flex-between text item'">
        
          <el-tooltip style="float:right" content="点击选择数据表" placement="left">
            <template #content>
              {{item.name}}<br />
              {{item.url}}
            </template>
             <div class="text-omit-1">{{ item.name }}</div>
          </el-tooltip>
           <el-icon style="font-size: 12px;padding-top: 8px;" @click="handleSelect(item)"><Refresh /></el-icon>
           <el-icon style="font-size: 12px;padding-top: 8px;margin-left:5px" @click="handleDelete(item)"><Delete /></el-icon>
      </p>
    </el-col>
    <div class="vertical-line"></div>
<el-col :span="8">
  <div class="flex-between">
    <el-button type="primary" v-if="formData.tableid&&formData.tableid!==''" @click="addCallbackField" link>新增同步状态字段</el-button>
  </div>
  <el-table :data="fields" style="width: 100%">
    <el-table-column prop="field_id" width="120" label="字段ID" />
    <el-table-column prop="field_name" label="字段名称" >
      <template #default="scope">{{ scope.row.field_name }}
      <el-tag v-if="scope.row.is_primary">主键</el-tag>

      </template>
    </el-table-column>
    <el-table-column prop="ui_type" label="字段类型" >
      <template #default="scope">{{ scope.row.ui_type }}
        <el-tag type="success" v-if="formData.callback&&formData.callback==scope.row.field_name">同步</el-tag>
      </template>
    </el-table-column>
  </el-table>
</el-col>
  </el-row>
<el-dialog v-model="dialogVisible" title="新增同步状态字段" width="30%">
  <el-input v-model="formData.callback" clearable></el-input>
  <template #footer>
    <el-button @click="dialogVisible = false">取 消</el-button>
    <el-button type="primary" @click="handleSubmitUpdate()">确 定</el-button>
  </template>
</el-dialog>

  </el-dialog>
</template>

<script setup>
import {onMounted, ref, reactive, toRefs} from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import feishuApi from '@/api/sys/tool/feishuApi.js';
import Template from "@/views/finance/periods/components/template.vue";

let state=reactive({
  typeList:[],
  dialogVisible:false,
  visible:false,
  formData:{ tableUrl: '', tableType: '' },
  fields:[],
  records:{},
  dialogRecordVisible:false,
  loading:false,
});
let {
  typeList,
  dialogVisible,
  visible,
  formData,
  fields,
  records,
  dialogRecordVisible,
  loading,
} =toRefs(state);
function handleSubmitUpdate(){
  state.dialogVisible = false;
  feishuApi.addCallback({"tableId":state.formData.id,"fieldName":state.formData.callback}).then(res=>{
    if (res.code === 200) {
      ElMessage.success('更新成功');
      loadTypeList(state.formData)
    } else {
      ElMessage.error(res.message || '更新失败');
    }
  });
}
function handleSelect(item){
    feishuApi.getTableFields(item.url, item.name).then(res=>{
      ElMessage.success('更新成功');
      loadTypeList();
  })
}
function handleRecord(){
  state.dialogRecordVisible = true;
  feishuApi.getRecord(state.formData.url).then(res=>{
    if (res.code === 200) {
      ElMessage.success('获取成功');
      state.records =JSON.parse(res.data) ;
      console.log(state.records)
    } else {
      ElMessage.error(res.message || '获取失败');
    }
  });
}
function handleTableInfo(){
  feishuApi.getTableInfo({"url":state.formData.tableUrl}).then(res=>{
    if (res.code === 200) {
      ElMessage.success('获取成功');
      state.formData.tableType = res.data;
    } else {
      ElMessage.error(res.message || '获取失败');
    }
  });
}
function handleTable(item){
  if (!item) {
    state.formData = {};
    fields.value = [];
    return;
  }
  state.formData= item;
  state.typeList.forEach(typeItem=>{
    typeItem.active = typeItem.id === item.id;
  })
  try {
    let fieldJson = JSON.parse(item.fieldjson);
    fields.value = fieldJson != null && fieldJson.items != null ? fieldJson.items : [];
  } catch (e) {
    console.error('解析 fieldjson 失败:', e);
    fields.value = [];
  }
}
function handleDelete(item) {
  ElMessageBox.confirm('确定要停用该数据表吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    // 更新 typeList 中对应 item 的 disabled 字段为 true
     item.disabled = true;
      feishuApi.updateTable(item).then(res=>{
        if (res.code === 200) {
          ElMessage.success('已停用');
          loadTypeList(state.formData)
        } else {
          ElMessage.error(res.message || '更新失败');
        }
      })
      
  }).catch(() => {
    ElMessage.info('已取消停用');
  });
}
function handleFeedback(record_id){
    feishuApi.updateCallback({"tableId":state.formData.id,"fieldkeys":[record_id]}).then(res=>{
      handleRecord()
     })
}
function addCallbackField(){
  state.dialogVisible=true;
  if(!state.formData.callback||state.formData.callback===""){
    state.formData.callback="同步状态";
  }
}
function getTableData(){
  if (!state.formData) {
    ElMessage.error('表单数据未初始化');
    return;
  }
  if (!state.formData.tableUrl) {
    ElMessage.error('请输入表格链接');
    return;
  }
  feishuApi.getTableFields(state.formData.tableUrl, state.formData.tableType).then(res=>{
    loadTypeList();
  })
}
function loadTypeList(item) {
  state.loading = true;
  feishuApi.getTypeList()
    .then(res => {
      if (res.code === 200) {
        state.typeList = res.data || [];
        if(!item){
          handleTable(state.typeList[0]);
        }else{
          handleTable(item);
        }
      } else {
        ElMessage.error(res.message || '获取数据表类型列表失败');
        state.typeList = [];
      }
    })
    .catch(err => {
      console.error('获取数据表类型列表失败:', err);
      if (err.message && err.message.includes('canceled')) {
        ElMessage.error('请求被取消，请重试');
      } else if (err.response) {
        const status = err.response.status;
        if (status === 500) {
          ElMessage.error('飞书配置错误，请联系管理员检查飞书应用配置');
        } else {
          ElMessage.error(`请求失败，状态码: ${status}`);
        }
      } else if (err.request) {
        ElMessage.error('网络错误，无法连接服务器');
      } else {
        ElMessage.error('请求失败: ' + err.message);
      }
      state.typeList = [];
    })
    .finally(() => {
      state.loading = false;
    });
}
function show(){
  state.visible = true;
  loadTypeList();
}
defineExpose({
  show
})

onMounted(() => {

});
</script>

<style scoped>
.type-list .text{
  padding: 5px 20px;
  border-radius: 4px;
}
.type-list .active{
  background-color: rgb(248, 227, 197);
}
.type-list .disabled{
  opacity: 0.5;
  cursor: not-allowed;
}
.type-list .actions{
  margin-right: 5px;
}
.custom-row {
  display: flex;
  align-items: stretch;
}

.vertical-line {
  width: 1px;
  background-color: #dcdfe6;
  margin: 0 10px;
  align-self: stretch;
}

.text.item {
  cursor: pointer;
  transition: color 0.3s ease;
}

.text.item:hover {
  color: #409eff;
}
</style>