<template>
		
		<div class="con-body">
			<el-row :gutter="20">
				<el-col :span="12">
					<div class="con-header">
						<el-row>
							<el-space>
							<div>数据库</div>
							<el-select v-model="database" @change="handleChange">
								<el-option v-for="item in databaseOptions" :key="item.Database" :value="item.Database" :label="item.Database"></el-option>
							</el-select>
							<el-input v-model="tableSearchName" @input="handleChange" placeholder="请输入表名称" clearable></el-input>
							<el-button @click="handleAdd" :loading="loading">添加到备份任务</el-button>
							</el-space>
					    </el-row>
					</div>
				  <el-table :data="tableOptions" 
				            height="calc(100vh - 200px)"  
							:default-sort="{ prop: 'data_length_mb', order: 'descending' }"
							 @selection-change="handleSelectionChange">
					<el-table-column type="selection" width="55" />
					<el-table-column property="name" label="名称" width="240" sortable/>
					<el-table-column property="rows" label="行数" width="120" sortable/>
					<el-table-column property="tablecomment" label="备注" sortable/>
					<el-table-column property="data_length_mb" label="数据大小(M)" width="120" sortable/>
					<el-table-column property="data_length_gb" label="数据大小(G)" width="120" sortable />
				  </el-table>
				</el-col>
				<el-col :span="12">
					<div class="con-header">
							<div class="flex-between">
						<el-row>
							<el-space>
							 <el-button type="success" @click="handleQueryTask">刷新任务进度</el-button>
							 <el-button type="primary"  @click="startTask">重启任务</el-button>
							 
							 <el-button @click="handleViewLog">查看log</el-button>
							</el-space>
						
					    </el-row>
								<el-button type="danger" @click="handleClear">清除</el-button></div>
					</div>
				  <el-table :data="tasklist"  height="calc(100vh - 200px)" 	:default-sort="{ prop: 'table_size', order: 'descending' }">
					<el-table-column property="database_name" label="数据库" width="110" sortable/>
					<el-table-column property="table_name" label="表名称" width="220" sortable/>
					<el-table-column property="table_size" label="行数" width="100" sortable/>
					<el-table-column property="download_size" label="迁移行" width="100" sortable />
				    <el-table-column  property="opttime"  label="操作时间" width="120"  sortable />
					<el-table-column  property="startdate"  label="开始时间" sortable />
					<el-table-column  property="enddate"  label="结束时间" sortable  />
				  </el-table>
				</el-col>
		  </el-row>
		</div>
		<el-dialog
    v-model="dialogVisible"
    title="错误信息"
    width="500"
  >
    <el-table>
			<el-table-column label="内容" prop="logs"></el-table-column>
			<el-table-column label="时间" prop="opttime"></el-table-column>
		</el-table>
    
  </el-dialog>
</template>

<script setup>
import {h, ref ,watch,reactive,onMounted,toRefs} from 'vue'
import {Search,ArrowDown,} from '@element-plus/icons-vue';
import { ElMessage ,ElMessageBox} from 'element-plus';
import dataMoveApi from "@/api/sys/tool/dataMoveApi.js";
const state = reactive({
	loading: false, databaseOptions: [], tableSearchName: "",
	database: "", tableOptions: "", tableList: [], tasklist: [], selectItems: [],
	dialogVisible: false,
});
const { databaseOptions, database, tableOptions, tableList, tasklist, selectItems, loading, tableSearchName, dialogVisible,dialogLogs } = toRefs(state);
function handleViewLog() { 
		dataMoveApi.showLog({"database":state.database}).then(res=>{
			state.dialogVisible = true;
			state.dialogLogs=res.data;
		})
}
function handleChange(value){
	dataMoveApi.showTables({"database":state.database,"tableSearchName":state.tableSearchName}).then(res=>{
		state.tableOptions=res.data;
		handleQueryTask();
	})
}
function handleQueryTask(){
	dataMoveApi.showtask({"database":state.database}).then(res=>{
		state.tasklist=res.data;
	})
}
function handleSelectionChange(val){
	state.selectItems=val;
}
function handleAdd(){
	var data=[];
	state.selectItems.forEach(item=>{
		var itemData={};
		itemData.database_name=state.database;
		itemData.table_name=item.name;
		itemData.table_size=item.rows;
		itemData.download_size=0;
		data.push(itemData);
	})
	state.loading=true;
	dataMoveApi.taskAdd(state.database,data).then(res=>{
		ElMessage.success("操作成功");
		handleQueryTask();
		state.loading=false;
	}).catch(e=>{
		state.loading=false;
	});
}
function handleClear(){
	dataMoveApi.clear({"database":state.database}).then(res=>{
		ElMessage.success("操作成功");
		handleQueryTask();
	})
}
function startTask(){
	dataMoveApi.startTask({"database":state.database}).then(res=>{
		ElMessage.success("操作成功");
		handleQueryTask();
	})
}
function loadDatabase(){
	dataMoveApi.showDataBase().then(res=>{
		 state.databaseOptions=res.data;
		 if(state.databaseOptions&&state.databaseOptions.length>0){
			 for(var i =0;i<state.databaseOptions.length;i++){
				 if(state.databaseOptions[i].Database.indexOf("db_")>=0){
					  state.database=state.databaseOptions[i].Database;
					   handleChange( state.database);
					   handleQueryTask();
					   break;
				 }
			 }
		    
		 }
	})
}
onMounted(()=>{
	loadDatabase();
})

 
</script>

<style>
</style>