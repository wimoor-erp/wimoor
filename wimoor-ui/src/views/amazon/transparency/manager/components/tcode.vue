<template>
    <div style="padding-bottom:20px"  >
		<el-space>
			<el-select v-model="queryParam.groupid"     placeholder="全部店铺"  @change="handleQuery">
				  <el-option key="" label="全部店铺" value=""></el-option>
			      <el-option  v-for="item in groupOption"   :key="item.id"  :label="item.name" :value="item.id"   >
			      </el-option>
			</el-select>
			<el-select v-model="queryParam.authid"     placeholder="全部授权"  @change="handleQuery">
			  <el-option key="" label="全部授权" value=""></el-option>
			  <el-option  v-for="item in authOption"   :key="item.clientId"  :label="item.clientName" :value="item.clientId"   >
			  </el-option>
			</el-select>
			<el-select v-model="queryParam.status"     placeholder="全部状态" @change="handleQuery" >
			  <el-option key="" label="全部状态" value=""></el-option>
			    <el-option    key="used"  label="已使用" value="used"   ></el-option>
				<el-option    key="unused"  label="未使用" value="unused"   ></el-option>
			</el-select>
			<el-input v-model="queryParam.sku" clearable placeholder="请输入sku" >
				<template #append>
				      <el-button :icon="Search" @click="handleQuery"  />
				</template>
			</el-input>
		</el-space>
	    <div style="padding-top:10px">
			<el-button type="primary" @click="handleDownload('excel')">导出T-Code(Excel)</el-button>
			<el-button type="primary" @click="handleDownload('pdf')">导出T-Code(PDF)</el-button>
			<el-button @click="saveUsed()">设为已用</el-button>
			<el-button @click="saveunUsed()">解除已用</el-button>
		</div>
	</div>
	<GlobalTable 
		ref="globalTable" 
		:tableData="tableData" 
		:defaultSort="{ prop: 'createtime', order: 'descending' }"  
		@loadTable="loadTableData" 
		:stripe="true"
		@selectionChange='selectChange' 
		height="calc(100vh - 300px)"
	>
		<template #field>
		<el-table-column fixed type="selection" width="38" />
	    <el-table-column prop="name"  label="图片" width="70" >
			<template #default="scope">
			        <el-image style="width:60px;height:60px" :src="scope.row.image"></el-image>
			    </template>
			</el-table-column>
		<el-table-column prop="sku"  label="名称/SKU"  show-overflow-tooltip="" sortable="custom">
		<template #default="scope">
		        <div class="name">{{scope.row.name}}</div>
				<div class="font-bold">{{scope.row.sku}}</div>
		    </template>
		</el-table-column>
		<el-table-column prop="groupname"  label="店铺" width="120"  sortable="custom"/>
		<el-table-column prop="client_name"  label="授权" width="120"  sortable="custom"/>
		<el-table-column prop="asin"  label="ASIN" width="120"  sortable="custom" />
		<el-table-column prop="gtin"  label="GTIN" width="140"  sortable="custom" >
		<template #default="scope">
					<div class="font-bold">{{scope.row.gtin}}</div>
			</template>
		</el-table-column>
		<el-table-column prop="jobid"  label="任务ID" width="200"  sortable="custom" />
		<el-table-column prop="tcode"  label="T-Code" width="300" sortable="custom"  >
			<template #default="scope">
					<div >{{scope.row.tcode}}</div>
			</template>
		</el-table-column>
		<el-table-column prop="tcode"  label="Type of Code" width="160"   >
			<template #default="scope">
					<div >{{scope.row.type_of_codes}}</div>
			</template>
		</el-table-column>
		<el-table-column prop="usetime"  label="状态" width="120"  sortable="custom" >
			<template #default="scope">
		        <el-tag v-if="scope.row.usetime">已使用</el-tag>
				<el-tag v-else type="success">未使用</el-tag>
			</template>
		</el-table-column>
	    <el-table-column prop="createtime"  label="创建时间"  width="100" sortable="custom"/>
		</template>
	</GlobalTable>
	 
</template>

<script setup>
	import { ref,reactive,onMounted,watch,toRefs } from 'vue';
	import { Search } from '@element-plus/icons-vue';
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
		 selection:[],
	 });
	 
	const { tableData,name,queryParam,formData,dialogVisible,selection,dialogTaskVisible,authOption,groupOption,taskForm}=toRefs(state);
	
	function handleAddShow(){
		state.dialogVisible=true;
		state.formData.groupid=state.groupOption&&state.groupOption.length>0?state.groupOption[0].id:'';
		state.formData.authid=state.authOption&&state.authOption.length>0?state.authOption[0].clientId:'';
	}
	function saveUsed(){
		var list=[];
		if(state.selection&&state.selection.length>0){
			state.selection.forEach(item=>{
				list.push({"taskid":item.taskid,"tcode":item.tcode,"usetime":dateFormat(new Date())});
			});
			authApi.saveCode(list).then(res=>{
				ElMessage.success("操作成功");
				handleQuery();
			});
		}
	}
	function handleDownload(type){
		if(state.selection&&state.selection.length>0){
			state.queryParam.tcodelist=[]
			state.selection.forEach(item=>{
				state.queryParam.tcodelist.push(item.tcode);
			});
			if(type=="pdf"){
				state.queryParam.ftype="H1";
				authApi.downloadPdfList(state.queryParam);
			}else{
				authApi.downExcel(state.queryParam);
			}
			
		}
		
	}
	function saveunUsed(){
		var list=[];
		if(state.selection&&state.selection.length>0){
			state.selection.forEach(item=>{
				list.push({"taskid":item.taskid,"tcode":item.tcode,"usetime":null});
			});
			authApi.saveCode(list).then(res=>{
				ElMessage.success("操作成功");
				handleQuery();
			});
		}
	}
	function handleQuery(){
		globalTable.value.loadTable(state.queryParam);
	}
 
	function loadTableData(param){
		authApi.listCode(param).then(res=>{
			state.tableData.records=res.data.records;
			state.tableData.total=res.data.total;
		});
	}
	function selectChange(value){
		state.selection=value;
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