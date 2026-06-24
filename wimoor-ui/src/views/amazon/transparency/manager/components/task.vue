<template>
    <div style="padding-bottom:20px">
		<el-space>
			<el-select v-model="queryParam.groupid"     placeholder="全部店铺"  @change="handleQuery" >
				  <el-option key="" label="全部店铺" value=""></el-option>
			      <el-option  v-for="item in groupOption"   :key="item.id"  :label="item.name" :value="item.id"   >
			      </el-option>
			</el-select>
			<el-select v-model="queryParam.authid"     placeholder="全部授权"  @change="handleQuery" >
			  <el-option key="" label="全部授权" value=""></el-option>
			  <el-option  v-for="item in authOption"   :key="item.clientId"  :label="item.clientName" :value="item.clientId"   >
			  </el-option>
			</el-select>
			<el-input v-model="queryParam.sku" clearable placeholder="请输入sku" >
				<template #append>
				      <el-button :icon="Search" @click="handleQuery"  />
				</template>
			</el-input>
		</el-space>
	</div>
	<GlobalTable 
		ref="globalTable" 
		:tableData="tableData" 
		:defaultSort="{ prop: 'createtime', order: 'descending' }"  
		@loadTable="loadTableData" 
		:stripe="true"
		height="calc(100vh - 258px)"
	>
		<template #field>
	    <el-table-column prop="name"  label="图片" width="70" >
			<template #default="scope">
			        <el-image style="width:40px;height:40px" :src="scope.row.image"></el-image>
			    </template>
			</el-table-column>
		<el-table-column prop="sku"  label="名称/SKU"  show-overflow-tooltip="" >
		<template #default="scope">
		        <div class="name">{{scope.row.name}}</div>
				<div class="font-bold">{{scope.row.sku}}</div>
		    </template>
		</el-table-column>
		<el-table-column prop="groupname"  label="店铺" width="120"  />
		<el-table-column prop="client_name"  label="授权" width="140"  />
		<el-table-column prop="asin"  label="ASIN" width="120"   />
		<el-table-column prop="gtin"  label="GTIN" width="140"   >
		<template #default="scope">
					<div class="font-bold">{{scope.row.gtin}}</div>
			</template>
		</el-table-column>
		<el-table-column prop="count"  label="数量" width="100"   >
			<template #default="scope">
					<div >{{scope.row.count}}</div>
			</template>
		</el-table-column>
		<el-table-column prop="jobid"  label="任务ID" width="200"   >
			<template #default="scope">
					<div >{{scope.row.jobid}}</div>
			</template>
		</el-table-column>
		<el-table-column prop="url"  label="链接" show-overflow-tooltip="" width="140"   >
			<template #default="scope">
					<div >{{scope.row.url}}</div>
			</template>
		</el-table-column>
		<el-table-column prop="status"  label="状态" width="180"   >
			<template #default="scope">
					<el-space ><el-tag>{{scope.row.status}}</el-tag>
					<el-button :loading="scope.row.loading" @click="handleRefreshTask(scope.row)" :icon="Refresh" size="small"></el-button>
					</el-space>
			</template>
		</el-table-column>
		<el-table-column prop="codenum"  label="T-Code数(已使用)" width="130"   >
			<template #default="scope">
					<div >{{scope.row.codenum}}</div>
			</template>
		</el-table-column>
	    <el-table-column prop="createtime"  label="操作时间"  width="100" sortable="custom" />
		<el-table-column prop="operator"  label="操作" width="200"  >
		    <template #default="scope">
				<el-space>
					<el-dropdown>
					   <span class="el-dropdown-link">
					     下载
					     <el-icon class="el-icon--right">
					       <arrow-down />
					     </el-icon>
					   </span>
					   <template #dropdown>
					     <el-dropdown-menu>
					       <el-dropdown-item @click="handleDownload(scope.row,'excel')">Excel</el-dropdown-item>
					       <el-dropdown-item @click="handleDownload(scope.row,'pdf')">PDF</el-dropdown-item>
					     </el-dropdown-menu>
					   </template>
					 </el-dropdown>
					 <el-dropdown>
					    <span class="el-dropdown-link">
					      T-Code标记
					      <el-icon class="el-icon--right">
					        <arrow-down />
					      </el-icon>
					    </span>
					    <template #dropdown>
					      <el-dropdown-menu>
					        <el-dropdown-item @click="usedByTask(scope.row)">T-Code标为已用</el-dropdown-item>
					        <el-dropdown-item @click="unUsedByTask(scope.row)">T-Code标为未使用</el-dropdown-item>
					      </el-dropdown-menu>
					    </template>
					  </el-dropdown>
				</el-space>
		    </template>
		</el-table-column>
		</template>
	</GlobalTable>
	 
</template>

<script setup>
	import { ref,reactive,onMounted,watch,toRefs } from 'vue';
	import { Search,Refresh,ArrowDown} from '@element-plus/icons-vue';
	import authApi from '@/api/amazon/transparency/authApi.js';
	import groupApi from '@/api/amazon/group/groupApi.js';
	import { ElMessage, ElMessageBox } from 'element-plus';
	import {dateFormat,formatFloat} from '@/utils/index.js';
	
	const emit = defineEmits(['change']);
	
	const globalTable=ref();
	
	const state = reactive({
	 	 tableData:{records:[],total:0},
		 name:"",
		 queryParam:{},
		 taskForm:{},
		 dialogVisible:false,
		 dialogTaskVisible:false,
		 formData:{},
		 authOption:[],
		 groupOption:[],
	 });
	 
	const { tableData,name,queryParam,formData,dialogVisible,dialogTaskVisible,authOption,groupOption,taskForm}=toRefs(state);
	
	function handleAddShow(){
		state.dialogVisible=true;
		state.formData.groupid=state.groupOption&&state.groupOption.length>0?state.groupOption[0].id:'';
		state.formData.authid=state.authOption&&state.authOption.length>0?state.authOption[0].clientId:'';
	}
	
	function handleQuery(){
		globalTable.value.loadTable(state.queryParam);
	}
    function usedByTask(row){
		authApi.usedByTask({"taskid":row.taskid}).then(res=>{
			 handleQuery();
		});
	}
	function unUsedByTask(row){
		authApi.unUsedByTask({"taskid":row.taskid}).then(res=>{
			 handleQuery();
		});
	}
	function handleRefreshTask(row){
		row.loading=true;
		authApi.refreshTask({"taskid":row.taskid}).then(res=>{
			 row.loading=false;
			 handleQuery();
		});
	}
	function loadTableData(param){
		authApi.listTask(param).then(res=>{
			state.tableData.records=res.data.records;
			state.tableData.total=res.data.total;
		});
	}
	 function handleDownload(row,ftype){
		state.queryParam.taskid=row.taskid;
		if(ftype=='pdf'){
			state.queryParam.ftype='H1';
			authApi.downloadPdfList(state.queryParam);
		}else{
			authApi.downExcel(state.queryParam);
		}
	 }
	function initLoadData(){
		groupApi.getAmazonGroup().then((res)=>{
			state.groupOption=res.data;
			handleQuery();
		});
		authApi.listAuthority({"name":""}).then(res=>{
			state.authOption=res.data;
		})
	}
	
	onMounted(()=>{
		initLoadData();
	})
</script>

<style>
</style>