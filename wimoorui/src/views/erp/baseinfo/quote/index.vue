<template>
		<div class="main-sty">
		<el-row style="margin-bottom: 15px;">
			<el-space>
				<Datepicker   ref="datepickers" @changedate="changeDate" />
				<el-input v-model="queryParam.search" @input="handleQuery" clearable  placeholder="请输入供应商/买家名称"></el-input>
			</el-space>
		</el-row>
	<GlobalTable ref="globalTable" :tableData="tableData"  height="calc(100vh - 198px)"  :defaultSort="{ prop: 'audittime', order: 'descending' }"  @loadTable="loadTableData" :stripe="false"  style="width: 100%;margin-bottom:16px;">
		<template #field>
		<el-table-column label="买家名称" prop="buyername"   width="130" sortable="custom" ></el-table-column>
		 <el-table-column label="买家公司" prop="buyercompany"   width="180" sortable="custom" ></el-table-column>
		  <el-table-column label="买家电话" prop="buyermobile"   width="130" sortable="custom" ></el-table-column>
		 <el-table-column label="供应商名称" prop="suppliername"   width="130" sortable="custom" ></el-table-column>
		  <el-table-column label="供应商公司" prop="suppliercompany"   width="180" sortable="custom" ></el-table-column>
		<el-table-column label="供应商电话" prop="supplierphone"   width="130" sortable="custom" ></el-table-column>
		<el-table-column label="供应商手机" prop="suppliermobile"   width="130" sortable="custom" ></el-table-column>
		 <el-table-column label="备注" prop="remark"    sortable="custom" ></el-table-column>
		<el-table-column label="审核时间" prop="audittime"  width="100" sortable="custom" >
			<template #default="scope">
				<span>{{dateFormat(scope.row.audittime)}}</span>
			</template>
		</el-table-column>
		<el-table-column label="操作"  width="100"  fixed="right" >
			<template #default='scope'>
			<el-space>
				<el-button @click="handleDetails(scope.row)" type="primary" link>详情</el-button>
			  </el-space>	
			</template>
		</el-table-column>
		</template>
	</GlobalTable>
	</div>
	<el-dialog
	   v-model="dialogVisible"
	   title="查看报价单详情"
	   width="800px"
	 >
	 <el-table :data="itemList" v-loading="loading">
		 <el-table-column label="图片" prop="name" width="60" >
			 <template #default="scope">
			 	<el-image :src="scope.row.image" width="40" height="40"></el-image>
			 </template>
		</el-table-column>
		<el-table-column label="名称" prop="name"   >
		</el-table-column>
		<el-table-column label="数量" prop="quantity"   >
		</el-table-column>
		<el-table-column label="状态" prop="statusstr"   >
		</el-table-column>
		<el-table-column label="单价" prop="price"   >
		</el-table-column>
		<el-table-column label="总价" prop="amount"   >
		</el-table-column>
	 </el-table>
	   <template #footer>
	     <span class="dialog-footer">
		   <el-button @click="cancel">关闭</el-button>
	     </span>
	   </template>
	 </el-dialog>
</template>

<script setup>
	import {ref,reactive,toRefs,onMounted}from"vue"
	import {MoreOne} from '@icon-park/vue-next';
	import Datepicker from '@/components/header/datepicker.vue';
	import { ElMessage, ElMessageBox,ElForm } from 'element-plus'
	import {dateFormat,formatFloat,dateTimesFormat} from '@/utils/index.js';
	import thirdpartyApi from "@/api/erp/thirdparty/thirdpartyApi.js";
	import purchaseApi from '@/api/quote/purchaseApi.js';
	let globalTable=ref()
	const dataFormRef = ref(ElForm);
	let state = reactive({
		tableData: {records:[],total:0},
		dialogVisible:false,
		itemList: [], 
		queryParam:{
			token:null,
			search:"",
		},
		loading:false,
	})
	let {
		tableData,itemList,rules,dialogVisible,queryParam,loading
		 } =toRefs(state);
	 
	function handleDetails(row){
		state.dialogVisible = true;
		state.loading=true;
		purchaseApi.getItem({"orderid":row.id}).then((res)=>{
			if(res.data && res.data.length>0){
				state.itemList=res.data;
			}else{
				state.itemList=[];
			}
			state.loading=false;
		});
	}
 
	function loadTableData(data){
		if(data.token){
			purchaseApi.list(data).then((res)=>{
				state.tableData.records = res.data.records
				state.tableData.total =res.data.total
			});
		}
	}
	
	function handleQuery(){
		globalTable.value.loadTable(state.queryParam);
	}
	function changeDate(strdate,timedate){
		state.queryParam.fromDate=timedate.start;
		state.queryParam.toDate=timedate.end;
		handleQuery();
	}
	
 
	function cancel() {
	  state.dialogVisible = false;
	}
 
 function loadToken(){
	 thirdpartyApi.getQuoteToken().then((res)=>{
	 			state.queryParam.token=res.data.buyertoken;
	 			state.isowner=res.data.isowner;
				console.log(res);
	 			if(state.queryParam.token){
	 				handleQuery();
	 			}else{
	 				state.tableData={records:[],total:0};
	 				state.orderTableData={records:[],total:0};
	 			}
		});
 }
	onMounted(()=>{
		loadToken();
	});
	
</script>

<style>
</style>
