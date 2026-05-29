<template>
	<div class="main-sty">
		<div class="con-header">
	    <div class="flex-between" style="padding-bottom:20px">
		  <el-space>
			 <Group  @change="groupChange" defaultValue="" isproduct="ok"></Group>
		<el-input  v-model="queryParams.search" @input="handleQuery" clearable placeholder="请输入SKU" style="width: 250px;" class="input-with-select" >
		   <template #append>
		     <el-button @click="handleQuery" >
		        <el-icon class="ic-cen font-medium">
		         <search/>
		      </el-icon>
		     </el-button>
		   </template>
		 </el-input>
		  
		 <el-button type="primary" @click.stop="downloadExcel">导出</el-button>
		 
		 <el-radio-group v-if="queryParams.marketplaceid=='IEU'" v-model="radiotype" @change="handleQuery">
		      <el-radio-button label="all" >欧洲</el-radio-button>
			  <el-radio-button label="other" >欧洲各国</el-radio-button>
		 </el-radio-group>
		 
		 </el-space>
		 <div>
			 <UploadRpt ref="uploadRptRef" type="GET_FBA_MYI_UNSUPPRESSED_INVENTORY_DATA"></UploadRpt>
		  </div>
		 </div>
		 </div>
		 <div class="con-body">
			
			 <GlobalTable ref="globalTable"
			  show-summary
			  v-if="radiotype=='all'"
			:summary-method="getSummaries"  
			  :tableData="tableData"  height="calc(100vh - 210px)" @selectionChange='handleSelect' 
			  :defaultSort="{ prop: 'sku', order: 'descending' }"  @loadTable="loadTableData" :stripe="true"  
			  style="width: 100%;margin-bottom:16px;">
			 	<template #field>
			 	<el-table-column label="产品信息" prop="name"    sortable="custom" >
					<template #default="scope">
					<div class="flex-center">
						   <el-image v-if="scope.row.image" :src="scope.row.image" class="img-40"  width="40" height="40"  ></el-image>
						   <el-image v-else :src="$require('empty/noimage40.png')"  class="img-40"  width="40" height="40"  ></el-image>
						   <div >
							   <div   >{{scope.row.pname}}</div>
							   <p class="sku">{{scope.row.sku}} 
							   </p>
						   </div>
					</div>
					</template>
				</el-table-column>
				<el-table-column label="仓库" prop="warehouse"  width="120"  sortable="custom" >
					<template #default="scope">
					<div class="flex-center">
						    <div v-if="scope.row.warehouse">{{scope.row.warehouse}}</div>
							<div v-else class="font-extraSmall">暂无记录</div>
					</div>
					</template>
				</el-table-column>
			 	<el-table-column label="库存" prop="afnWarehouseQuantity"  width="120" sortable="custom"  />
				<el-table-column label="可售库存" prop="afnFulfillableQuantity"  width="120" sortable="custom"  />
			    <el-table-column label="预留库存" prop="afnReservedQuantity"  width="120" sortable="custom"  />
				<el-table-column label="正在发货" prop="afnInboundWorkingQuantity"  width="120" sortable="custom"  />
				<el-table-column label="待接收" prop="afnInboundShippedQuantity"  width="120" sortable="custom"  />
				<el-table-column label="正在接收" prop="afnInboundReceivingQuantity"  width="120" sortable="custom"  />
				<el-table-column label="不可用" prop="afnUnsellableQuantity"  width="120" sortable="custom"  />
				<el-table-column label="异常" prop="afnResearchingQuantity"  width="120" sortable="custom"  />
			 	</template>
			 </GlobalTable>
			 <GlobalTable ref="globalTableEU"
			   v-else
			   :tableData="tableDataEU"  height="calc(100vh - 180px)"  
			   :defaultSort="{ prop: 'sku', order: 'descending' }"  
			   @loadTable="loadTableDataEU" :stripe="true"  
			   style="width: 100%;margin-bottom:16px;">
			  	<template #field>
			  	<el-table-column label="产品信息" prop="name"    sortable="custom" >
			 		<template #default="scope">
			 		<div class="flex-center">
			 			   <el-image v-if="scope.row.image" :src="scope.row.image" class="img-40"  width="40" height="40"  ></el-image>
			 			   <el-image v-else :src="$require('empty/noimage40.png')"  class="img-40"  width="40" height="40"  ></el-image>
			 			   <div >
			 				   <div   >{{scope.row.NAME}}</div>
			 				   <p class="sku">{{scope.row.sku}} 
			 				   </p>
			 			   </div>
			 		</div>
			 		</template>
			 	</el-table-column>
			  
			  	<el-table-column label="英国" prop="GB"  width="120" sortable="custom"  />
			 	<el-table-column label="德国" prop="DE"  width="120" sortable="custom"  />
			     <el-table-column label="法国" prop="FR"  width="120" sortable="custom"  />
			 	<el-table-column label="意大利" prop="IT"  width="120" sortable="custom"  />
			 	<el-table-column label="西班牙" prop="ES"  width="120" sortable="custom"  />
			 	<el-table-column label="波兰" prop="PL"  width="120" sortable="custom"  />
			  	</template>
			  </GlobalTable>
		 </div>
	</div>
	 
</template>
<script>
	export default{ name:"FBA库存" };
</script>
<script setup>
	import {ref,reactive,toRefs,onMounted}from"vue"
	import {MoreOne} from '@icon-park/vue-next';
	import Group from '@/components/header/group.vue';
	import { ElMessage, ElMessageBox,ElForm } from 'element-plus'
	import inventoryRptApi from "@/api/amazon/inventory/inventoryRptApi.js";
	import {Search} from '@element-plus/icons-vue';
	import UploadRpt from '@/components/Upload/uploadRpt.vue';
	
	let detailsRef =ref();
	let globalTable=ref();
	let globalTableEU=ref();
	const dataFormRef = ref(ElForm);
	const props = {
	  expandTrigger: 'hover',
	  value:'id',
	  label:'name',
	}
	let state = reactive({
		tableData: {records:[],total:0},
		tableDataEU:{records:[],total:0},
		queryParams:{
			search:"",
		},
		summary:{},
		radiotype:"all",
	})
	let {
		tableData,queryParams,tableDataEU,
		summary,radiotype,
		 } =toRefs(state);
		 
		 function groupChange(obj){
			state.queryParams.groupid=obj.groupid;
			state.queryParams.marketplaceid=obj.marketplaceid;
			handleQuery();
		 }
		 
		 
		 function downloadExcel(){
			 inventoryRptApi.getWarehouseExport(state.queryParams);
		 }
		 
		 function handleQuery(){
			state.queryParams.warehouseid=state.warehouseCheck;
			if(state.radiotype=='all'){
			   globalTable.value.loadTable(state.queryParams);
			}else{
			   globalTableEU.value.loadTable(state.queryParams);
			}
		 }
		 function loadTableDataEU(params){
			 inventoryRptApi.getFBACountry(params).then(res=>{
			 				 state.tableDataEU.records=res.data.records;
			 				 state.tableDataEU.total=res.data.total;
			 })
		 }
		 function loadTableData(params){
			 inventoryRptApi.getWarehouse(params).then(res=>{
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
		 function getSummaries(){
		 	var arr = ["合计"]
		 	arr[2]=state.summary.afnWarehouseQuantity;
			arr[3]=state.summary.afnFulfillableQuantity;
			arr[4]=state.summary.afnReservedQuantity;
			arr[5]=state.summary.afnInboundWorkingQuantity;
			arr[6]=state.summary.afnInboundShippedQuantity;
			arr[7]=state.summary.afnInboundReceivingQuantity;
			arr[8]=state.summary.afnUnsellableQuantity
		 	arr[9]=state.summary.afnResearchingQuantity;
		 	return  arr
		 }
		 
		onMounted(()=>{ 
		}) 
</script>

<style scoped>
	.img-40{width: 40px;
	height: 40px;flex: none;
	margin-right: 8px;}
</style>
