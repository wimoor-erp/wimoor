<template>
     <div class="main-sty">
		 <div class="con-header" style="padding-bottom:20px">
       <el-row :gutter="20">
         <el-col :span="16">
				  <div class="flex-between">
					   <el-button @click="handleAdd">添加新任务</el-button>
					   <div>
						   <el-space>
						   			<div style="padding-top:10px" class="font-extraSmall">注意：修改后必须重载任务才能生效</div>			
						   		 <el-button type="primary" @click="refreshTask">重载任务</el-button>				 
						   </el-space>
						    
					   </div>
				  </div>
         </el-col>
         <el-col :span="4"><h3>运行列表</h3></el-col>
       </el-row>
		 </div>
		 <div class="con-body">
	 <el-row :gutter="20">
     <el-col :span="16">
			 <GlobalTable ref="globalTable" :nopage="true" :tableData="tableData"     @loadTable="loadTableData" :stripe="true"  style="width: 100%;margin-bottom:16px;">
			   <template #field>
			 	 <el-table-column prop="name" label="名称"  sortable='custom'  width="160">
					 <template #default="scope">
					 						   <div>{{scope.row.name}}</div>
					 						   <div class="font-extraSmall">优先级{{scope.row.priority}}</div>
                         <div><span class="font-extraSmall">分组:</span>{{scope.row.fgroup}}</div>
					 </template>
			 	  </el-table-column>
				   <el-table-column prop="cron" label="执行时间"  sortable='custom'  width="240">
					   <template #default="scope">
					   						   <div>{{scope.row.cron}}</div>
					   						   <el-tag type="danger" v-if="scope.row.isdelete">停用</el-tag>
											   <el-tag type="success" v-else>启用</el-tag>
               <div><span class="font-extraSmall">执行服务器:</span>{{scope.row.target}}</div>
					   </template>
					   
				    </el-table-column>
				   <el-table-column prop="description" label="描述"  show-overflow-tooltip sortable='custom'  >
					   <template #default="scope">
						   <div>{{scope.row.description}} <span class="font-extraSmall"> - 创建时间: {{dateFormat(scope.row.createdate)}}</span></div>
						   <div>{{scope.row.path}}</div>
					   </template>
				    </el-table-column>
			 	 <el-table-column prop="turndays" label="操作"    width="90"  >
			 		 <template #default="scope">
             <el-space direction="vertical">
						 <div><el-button   @click="handleEdit(scope.row)"   link type="warning">编辑</el-button></div>
             <el-dropdown>
                    <span class="el-dropdown-link">
                      更多
                      <el-icon class="el-icon--right">
                        <arrow-down />
                      </el-icon>
                    </span>
               <template #dropdown>
                 <el-dropdown-menu>
                   <el-dropdown-item v-if="!scope.row.isdelete" @click="disableApi(scope.row)"  >禁用</el-dropdown-item>
                   <el-dropdown-item v-else @click="enableApi(scope.row)"  >启用</el-dropdown-item>
                   <el-dropdown-item v-if="!scope.row.isdelete" @click="runApi(scope.row)"  >单次运行</el-dropdown-item>
                   <el-dropdown-item @click="addScheduler(scope.row)"  >添加到运行列表</el-dropdown-item>
                 </el-dropdown-menu>
               </template>
             </el-dropdown>
             </el-space>
			 		 </template>
			 	 </el-table-column>
			 	 </template>
			  </GlobalTable>
     </el-col>
     <el-col :span="8">

       <GlobalTable ref="globalTableScheduler" :nopage="true" :tableData="schedulerTableData"     @loadTable="loadSchedulerTableData" :stripe="true"  style="width: 100%;margin-bottom:16px;">
         <template #field>
           <el-table-column prop="jobDetailName" label="名称"  sortable='custom'  >
             <template #default="scope">
               <div>{{scope.row.jobDetailName}}</div>
               <div><span class="font-extraSmall">分组:</span>{{scope.row.groupName}}</div>
             </template>
           </el-table-column>
           <el-table-column prop="jobCronExpression" label="执行时间"  sortable='custom' >
             <template #default="scope">
               <div>{{scope.row.jobCronExpression}}</div>
               <el-tag  v-if="scope.row.status==='NORMAL'">正常运行</el-tag>
               <el-tag  v-if="scope.row.status==='PAUSED'">暂停运行</el-tag>
               <div><span class="font-extraSmall">时区:</span>{{scope.row.timeZone}}</div>
             </template>

           </el-table-column>

           <el-table-column prop="turndays" label="操作"    width="90"  >
             <template #default="scope">
               <div><el-button   @click="puaseScheduler(scope.row)"   link type="warning">暂停</el-button></div>
               <div><el-button   @click="resumeScheduler(scope.row)"   link type="success">恢复</el-button></div>
               <div><el-button   @click="deleteScheduler(scope.row)"   link type="primary">删除</el-button></div>
             </template>
           </el-table-column>
         </template>
       </GlobalTable>
     </el-col>
  </el-row>
  </div>
  </div>
   <el-dialog v-model="dialogFormVisible" title="任务信息" width="60%">
      <el-form :model="editTask"  label-width="auto">
        <el-form-item label="任务名称" >
          <el-input v-model="editTask.name"  />
        </el-form-item>
		<el-form-item label="任务分组" >
		  <el-input v-model="editTask.fgroup"  />
		</el-form-item>
		<el-form-item label="目标服务器" >
		  <el-input v-model="editTask.target"  />
		</el-form-item>
		<el-form-item label="执行时间" >
		  <el-input v-model="editTask.cron"  />
		</el-form-item>
		<el-form-item label="参数设置"  >
		  <el-input v-model="editTask.parameter"  />
		</el-form-item>
		<el-form-item label="描述信息" >
		  <el-input v-model="editTask.description"  />
		</el-form-item>
		<el-form-item label="执行路径" >
		  <el-input v-model="editTask.path"  />
		</el-form-item>
		<el-form-item label="微服务名称" >
		  <el-input v-model="editTask.server"  />
		</el-form-item>
		<el-form-item label="优先级" >
		  <el-input v-model="editTask.priority"  />
		</el-form-item>
		<el-form-item label="对应类" >
		  <el-input v-model="editTask.bean"  />
		</el-form-item>
		<el-form-item label="对应方法" >
		  <el-input v-model="editTask.method"  />
		</el-form-item>
        <el-form-item label="禁用" >
           <el-checkbox v-model="editTask.isdelete"></el-checkbox>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="dialogFormVisible = false">取消</el-button>
          <el-button type="primary" @click="handleSubmit">
            确认
          </el-button>
        </div>
      </template>
    </el-dialog>
</template>

<script setup>
import {h, ref, watch, reactive, onMounted, toRefs} from 'vue'
import {Search, ArrowDown,} from '@element-plus/icons-vue';
import GlobalTable from "@/components/Table/GlobalTable/index.vue";
import {ElMessage, ElMessageBox} from 'element-plus';
import {MenuUnfold, Plus, SettingTwo, Help, Copy, MoreOne, ChartPie} from '@icon-park/vue-next';
import {dateFormat, getCurrencyMark} from '@/utils/index.js';
import MarketCountry from '@/components/header/market_country.vue';
import taskApi from '@/api/sys/admin/taskApi.js';

const globalTable = ref();
const globalTableScheduler = ref();
const formLabelWidth = '140px'
const marketCountryRef = ref();
let state = reactive({
  downLoading: false,
  dialogFormVisible: false,
  queryParam: {
    groupid: '',
    search: "",
    marketplaceid: "",
  },
  editTask: {},
  isload: true,
  tableData: {records: [], total: 0},
  schedulerTableData: {records: [], total: 0},
});
let {
  queryParam,
  isload,
  editTask,
  dialogFormVisible,
  tableData,
  downLoading,
  schedulerTableData,
} = toRefs(state);


function handleQuery() {
  globalTable.value.loadTable(state.queryParam);
  globalTableScheduler.value.loadTable(state.queryParam);
}

function loadTableData(param) {
  taskApi.getTaskList().then((res) => {
    state.tableData.records = res.data;
    state.tableData.total = res.data.length;
  });
}

function disableApi(row) {
  taskApi.disableApi(row).then((res) => {
    ElMessage.success("操作成功");
    handleQuery();
  });
}

function handleEdit(row) {
  state.editTask = row;
  state.dialogFormVisible = true;
}

function handleAdd() {
  state.editTask = {};
  state.dialogFormVisible = true;
}

function handleSubmit() {
  taskApi.saveQuartzTask(state.editTask).then(res => {
    ElMessage.success("操作成功");
    handleQuery();
    state.dialogFormVisible = false;
  })
}

function enableApi(row) {
  taskApi.enableApi(row).then((res) => {
    ElMessage.success("操作成功");
    handleQuery();
  });
}

function runApi(row) {
  taskApi.runApi(row).then((res) => {
    ElMessage.success("执行成功");
  });
}

function refreshTask() {
  taskApi.refreshTask().then((res) => {
    ElMessage.success("操作成功");
  });
}

function loadSchedulerTableData() {
  taskApi.listScheduler().then((res) => {
    state.schedulerTableData.records = res.data;
    state.schedulerTableData.total = res.data.length;
  });
}

function puaseScheduler(row) {
  let jobDetailName = row.jobDetailName;
  let jobDetailGroup = row.groupName;
  taskApi.puaseScheduler({"jobDetailName": jobDetailName, "jobDetailGroup": jobDetailGroup}).then((res) => {
    ElMessage.success("操作成功");
    handleQuery();
  });
}

function resumeScheduler(row) {
  let jobDetailName = row.jobDetailName;
  let jobDetailGroup = row.groupName;
  taskApi.resumeScheduler({"jobDetailName": jobDetailName, "jobDetailGroup": jobDetailGroup}).then((res) => {
    ElMessage.success("操作成功");
    handleQuery();
  });
}
function deleteScheduler(row){
  let jobDetailName = row.jobDetailName;
  let jobDetailGroup = row.groupName;
  taskApi.deleteScheduler({"jobDetailName": jobDetailName, "jobDetailGroup": jobDetailGroup}).then((res) => {
    ElMessage.success("操作成功");
    handleQuery();
  });
}
function addScheduler(row){
  taskApi.addScheduler(row).then((res) => {
    ElMessage.success("操作成功");
    handleQuery();
  });
}
onMounted(() => {
  handleQuery();
})


</script>

<style>
</style>
