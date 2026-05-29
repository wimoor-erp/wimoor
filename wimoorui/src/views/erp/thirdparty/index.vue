<template>
     <div class="main-sty">
	 <el-row><el-button type="primary" style="margin-bottom: 10px;" @click.stop="showaddApi">添加API（物流商/海外仓）</el-button></el-row>
	 <el-row>
			 <GlobalTable ref="globalTable" :nopage="true" :tableData="tableData"     @loadTable="loadTableData" :stripe="true"  style="width: 100%;margin-bottom:16px;">
			   <template #field>
			 	 
				  <el-table-column prop="name" label="名称"  sortable='custom'  width="290">
				   </el-table-column>
				   <el-table-column prop="systemName" label="系统名称"  sortable='custom'  width="280" >
					   <template #default="scope">
						   <div>
							   {{scope.row.systemName}}
							   <el-tag v-if="scope.row.systemEntity.support.indexOf('logistics')>=0" type="warning">物流</el-tag>
							   <el-tag v-if="scope.row.systemEntity.support.indexOf('warehouse')>=0" type="success">海外仓</el-tag>
						   </div>
					   </template>
				    </el-table-column>
				   <el-table-column prop="appsecret" label="授权信息"  sortable='custom' >
					   <template #default="scope">
					   						   <div v-if="scope.row.appkey">
					   							<span class="font-extraSmall">App Key :</span>   {{scope.row.appkey}}
					   						   </div>
											   <div v-if="scope.row.appsecret">
											      <span class="font-extraSmall">App Secret :</span>   {{scope.row.appsecret}}
											   </div>
											   <div v-if="scope.row.token">
											      <span class="font-extraSmall">Access Token :</span>   {{scope.row.token}}
											   </div>
					   </template>
				    </el-table-column>
			 	  <el-table-column prop="api" label="Api"  sortable='custom'  >
			 	   </el-table-column>
			 	<el-table-column prop="turndays" label="操作"    width="120"  >
			 		 <template #default="scope">
			 			  <el-button v-if="!scope.row.isdelete" @click="disableApi(scope.row)"   link type="primary">禁用</el-button>
						  <el-button v-else @click="enableApi(scope.row)"   link type="primary">启用</el-button>
						   <el-button   @click="showaddApi(scope.row)"   link type="primary">编辑</el-button>
			 		 </template>
			 	 </el-table-column>
			 	 </template>
			  </GlobalTable>
  </el-row>
  </div>
  
 <EditDialog ref="dialogRef" @change="handleQuery" ></EditDialog>
</template>

<script setup>
	import {h, ref ,watch,reactive,onMounted,toRefs} from 'vue'
	import {Search,ArrowDown,} from '@element-plus/icons-vue';
	import GlobalTable from "@/components/Table/GlobalTable/index.vue";
    import { ElMessage ,ElMessageBox} from 'element-plus';
	import {MenuUnfold,Plus,SettingTwo,Help,Copy,MoreOne,ChartPie} from '@icon-park/vue-next';
	import {dateFormat,getCurrencyMark} from '@/utils/index.js';
	import MarketCountry from '@/components/header/market_country.vue';
	import EditDialog from './components/edit_dialog.vue';
	import thirdpartyApi from '@/api/erp/thirdparty/thirdpartyApi.js';
	const globalTable=ref();
	const dialogRef=ref();
	const marketCountryRef =ref();
	let state=reactive({
		 downLoading:false,
		 queryParam:{
			 groupid:'',
			 search:"",
			 marketplaceid:"",
		 },
		 isload:true,
		 tableData:{records:[],total:0},
		 addVisible:false,
	});
	let {
	   queryParam,
	   isload,
	   tableData,
	   downLoading,
	   addVisible,
	} =toRefs(state);
 
  
	 
	function handleQuery(){
	     globalTable.value.loadTable(state.queryParam);
	}
	
	function loadTableData(param){
		thirdpartyApi.getlist().then((res)=>{
			state.tableData.records=res.data;
			state.tableData.total=res.data.length;
		}); 
	}
	
	function disableApi(row){
		thirdpartyApi.deleteItem({"id":row.id}).then((res)=>{
			ElMessage.success("禁用成功");
			handleQuery();
		});
	}
	
	function enableApi(row){
		thirdpartyApi.enableItem({"id":row.id}).then((res)=>{
			ElMessage.success("启用成功");
			handleQuery();
		});
	}
	
	function showaddApi(row){
		dialogRef.value.show(row);
	}
	

	
	function updateApi(){
		var params={};
		thirdpartyApi.update(params).then((res)=>{
			ElMessage.success("添加成功");
			handleQuery();
		});
	}
	
	
 

 
	 onMounted(()=>{
		 handleQuery();
	 })
	 
	 
</script>

<style>
</style>
