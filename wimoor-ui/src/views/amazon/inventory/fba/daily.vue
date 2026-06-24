<template>
	<div class="main-sty">
		<div class="con-header">
	     <el-row>
		  <el-space>
			 <Group  @change="groupChange" defaultValue="" isproduct="ok"></Group>
			 <Datepicker longtime="ok" ref="datepickers" @changedate="changedate" />
		<el-input  v-model="queryParams.sku" @input="handleQuery" clearable placeholder="请输入SKU" style="width: 250px;" class="input-with-select" >
		   <template #append>
		     <el-button @click="handleQuery" >
		        <el-icon class="ic-cen font-medium">
		         <search/>
		      </el-icon>
		     </el-button>
		   </template>
		 </el-input>
		  
		 <el-button type="primary" @click.stop="downloadExcel">导出</el-button>
		 
		 </el-space>
		 </el-row>
		 </div>
		 <div class="con-body">
			
		    <GlobalTable ref="globalTable"
			 show-summary
			 :summary-method="getSummaries"  
			  :tableData="tableData"  height="calc(100vh - 210px)" @selectionChange='handleSelect' 
			  :defaultSort="{ prop: 'sku', order: 'ascending' }"  @loadTable="loadTableData" :stripe="true"  
			  style="width: 100%;margin-bottom:16px;">
			 	<template #field>
					<el-table-column label="产品信息" prop="sku"   width="200" fixed='left' sortable="custom" show-overflow-tooltip>
						<template #default="scope">
						<div class="flex-center">
							   <el-image v-if="scope.row.image" :src="scope.row.image" class="img-40"  width="40" height="40"  ></el-image>
							   <el-image v-else :src="$require('empty/noimage40.png')"  class="img-40"  width="40" height="40"  ></el-image>
							   <div >
								   <div>{{scope.row.pname}}</div>
								   <p class="sku">{{scope.row.sku}} 
								   </p>
							   </div>
						</div>
						</template>
					</el-table-column>
					<el-table-column label="仓库" prop="warehouse" fixed='left' width="120"  sortable="custom" />
					<el-table-column :label="item.byday" :prop="item.field" v-for="item in fieldlist" min-width="120" sortable="custom"  />
			 	</template>
			 </GlobalTable>
		 </div>
	</div>
	 
</template>
<script>
	export default{ name:"FBA每日库存" };
</script>
<script setup>
	import {ref,reactive,toRefs,onMounted}from"vue"
	import {MoreOne} from '@icon-park/vue-next';
	import Group from '@/components/header/group.vue';
	import { ElMessage, ElMessageBox,ElForm } from 'element-plus'
	import inventoryRptApi from "@/api/amazon/inventory/inventoryRptApi.js";
	import {Search} from '@element-plus/icons-vue';
	import Datepicker from '@/components/header/datepicker.vue';
	
	let globalTable=ref();
	const dataFormRef = ref(ElForm);
	let state = reactive({
		tableData: {records:[],total:0},
		queryParams:{
			sku:"",
		},
		isload:true,
		fieldlist:[],
		summary:{},
	})
	let {
		tableData,queryParams,fieldlist
		 } =toRefs(state);
		 
		 function groupChange(obj){
			state.queryParams.groupid=obj.groupid;
			if(obj.marketplaceid=="IEU"){
				state.queryParams.warehouse="EU";
			}else{
			    state.queryParams.warehouse=obj.marketplaceid;
			}
			handleQuery();
		 }
		 //日期改变
		 function changedate(dates){
		 	state.queryParams.fromdate=dates.start;
		 	state.queryParams.enddate=dates.end;
			if(state.isload==false){
		 	 handleQuery();
			}
		 	 
		 }
		 function downloadExcel(){
			 inventoryRptApi.getFBAInvDayDetailExport(state.queryParams);
		 }
		 /* 合计行数据 */
		 function getSummaries({columns,data}){
		 	var arr = ["合计"];
			columns.forEach((item,index)=>{
				if(index>=2){
					arr[index]=state.summary[item.label];
				}
			})
		 	return  arr
		 }
		 function handleQuery(){
			 state.isload=false;
			 inventoryRptApi.getFBAInvDayDetailField(state.queryParams).then((res)=>{
				 state.fieldlist=res.data;
				 globalTable.value.loadTable(state.queryParams);
			 });
		 }
		 
		 function loadTableData(params){
			 inventoryRptApi.getFBAInvDayDetail(params).then(res=>{
				 state.tableData.records=res.data.records;
				 state.tableData.total=res.data.total;
				 if(params.currentpage==1){
									 if(res.data.total>0){
									 	  state.summary=res.data.records[0].summary;
									 }else{
										 state.summary={};
									 }
				}
			 })
		 }
		 
		 
</script>

<style scoped>
	.img-40{width: 40px;
	height: 40px;flex: none;
	margin-right: 8px;}
</style>
