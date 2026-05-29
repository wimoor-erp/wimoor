<template>
	<div class="main-sty">
		<div class="con-header">
	     <el-row>
		  <el-space>
			  <el-cascader
			        v-model="warehouseCheck"
			        :options="warehouseData"
			  	   :value = "name"
			  	   :label="name"
			        :props="props"
			        @change="getWarehouse"
			  	  placeholder="全部仓库"
			  	  clearable
			      />
			 <Datepicker longtime="ok" ref="datepickers" @changedate="changedate" :days="1" />
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
			  :tableData="tableData"  height="calc(100vh - 200px)" @selectionChange='handleSelect' 
			  :defaultSort="{ prop: 'sku', order: 'descending' }"  @loadTable="loadTableData" :stripe="true"  
			  style="width: 100%;margin-bottom:16px;">
			 	<template #field>
			 	<el-table-column label="产品信息" prop="mname"   width="200" fixed='left' sortable="custom" show-overflow-tooltip >
					<template #default="scope">
					<div class="flex-center">
						   <el-image v-if="scope.row.image" :src="scope.row.image" class="img-40"  width="40" height="40"  ></el-image>
						   <el-image v-else :src="$require('empty/noimage40.png')"  class="img-40"  width="40" height="40"  ></el-image>
						   <div >
							   <div   >{{scope.row.mname}}</div>
							   <p class="sku">{{scope.row.sku}} 
							   </p>
						   </div>
					</div>
					</template>
				</el-table-column>
				<el-table-column label="仓库" prop="warehouse"   fixed='left' width="120"  sortable="custom" >
					<template #default="scope">
					<div class="flex-center">
						    <div v-if="scope.row.warehouse">{{scope.row.warehouse}}</div>
							<div v-else class="font-extraSmall">暂无记录</div>
					</div>
					</template>
				</el-table-column>
			 	 <el-table-column :label="item" :prop="item" v-for="item in fieldlist" min-width="120" sortable="custom"  />
			 	</template>
			 </GlobalTable>
		 </div>
	</div>
	 
</template>
<script>
    export default{ name:"每日库存" };
</script>
<script setup>
	import {ref,reactive,toRefs,onMounted}from"vue"
	import {MoreOne} from '@icon-park/vue-next';
	import { ElMessage, ElMessageBox,ElForm } from 'element-plus'
	import Warehouse from '@/components/header/warehouse.vue';
	import inventoryRptApi from '@/api/erp/inventory/inventoryRptApi.js';
	import warehouseApi from '@/api/erp/warehouse/warehouseApi.js';
	import {dateFormat,formatFloat,dateTimesFormat} from '@/utils/index.js';
	import {Search} from '@element-plus/icons-vue';
	import Datepicker from '@/components/header/datepicker.vue';
	
	const props = {
	  value:'id',
	  label:'name',
	}
	let globalTable=ref()
	const dataFormRef = ref(ElForm);
	let state = reactive({
		tableData: {records:[],total:0},
		queryParams:{
			 sku:'',
		},
		summary:{},
		warehouseCheck:null,
		warehouseData:[],
		isload:true,
		fieldlist:[],
	})
	let {
		tableData,queryParams,warehouseCheck,
		warehouseData,fieldlist
		 } =toRefs(state);
		 
		 function downloadExcel(){
			 inventoryRptApi.getInvDayDetailExport(state.queryParams);
		 }
		 //日期改变
		 function changedate(dates){
		 	state.queryParams.fromdate=dates.start;
		 	state.queryParams.enddate=dates.end;
			if(state.isload==false){
				handleQuery();
			}
		 	 
		 }
		 
		 function getWarehouse(val){
		 	var warehouseid =''
		 	if(val){
		 		state.warehouseData.forEach((item)=>{
		 			if(item.children && item.children.length>0){
		 				item.children.forEach((sub)=>{
		 					if(sub.id == val[1]){
		 						warehouseid =sub.id;
		 					}
		 				})
		 			}
		 		})
		 	}else{
		 		warehouseid =''
		 	}
		 	state.warehouseCheck=warehouseid;
		 	//每次改变仓库 变换list
		 	if(warehouseid!=''){
		 		handleQuery();
		 	}
		 }
		 
		 function handleQuery(){
			state.queryParams.warehouse=state.warehouseCheck;
			state.isload=false;
			inventoryRptApi.getInvDayField({"fromdatestr":state.queryParams.fromdate,"enddatestr":state.queryParams.enddate}).then((res)=>{
					state.fieldlist=res.data;
					globalTable.value.loadTable(state.queryParams);
			});
			
		 }
		 function loadTableData(params){
			 inventoryRptApi.getInvDayDetail(params).then(res=>{
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
		 
		onMounted(()=>{ 
			warehouseApi.getWarehouseList().then(function(res){
				state.warehouseData = res.data;
				if(res.data && res.data.length>0){
					state.warehouseCheck=res.data[0].children[0].id;
					handleQuery();
				}
			})
		}) 
</script>

<style scoped>
	.img-40{width: 40px;
	height: 40px;flex: none;
	margin-right: 8px;}
</style>
