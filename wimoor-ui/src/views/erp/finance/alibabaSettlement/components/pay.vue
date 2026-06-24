<template>
    <div  >
        <!--表单-->
        <el-row>
			<GlobalTable ref="globalTable"
			 :tableData="tableData"  height="calc(100vh - 250px)" 
			 :defaultSort="{ prop: 'paytime', order: 'descending' }"  @loadTable="loadTableData" :stripe="true"  
			 style="width: 100%;margin-bottom:16px;">
				<template #field>
					<el-table-column prop="paytime"  label="还款时间" sortable="custom" >
					<template #default="scope">
											<div >{{dateTimesFormat(scope.row.paytime)}}</div>
										 
										</template>
					</el-table-column>
			    <el-table-column prop="paymethod" label="还款方式"  sortable="custom" show-overflow-tooltip>
			    </el-table-column>
				<el-table-column prop="paytype"  label="还款类型"  sortable="custom" >
				</el-table-column>
				 <el-table-column prop="amount"    label="还款金额(元)" sortable="custom" >
				</el-table-column>
			</template>
			</GlobalTable>
        </el-row>

    </div>
 
</template>
<script setup>
    import {MenuUnfold,Plus,SettingTwo,Help,Copy,MoreOne} from '@icon-park/vue-next';
    import { ref,reactive,onMounted,toRefs} from 'vue'
    import {Search,ArrowDown,} from '@element-plus/icons-vue'
	import listApi from '@/api/erp/purchase/form/listApi.js';
	import warehouseApi from '@/api/erp/warehouse/warehouseApi.js';
	import { ElMessageBox,ElMessage } from 'element-plus';
	import Warehouse from '@/components/header/warehouse.vue';
	import Datepicker from '@/components/header/datepicker.vue';
	import UploadDialog from '@/components/Upload/uploadDialog.vue';
	import {dateFormat,dateTimesFormat} from '@/utils/index.js';
	import Supplier from '@/components/header/supplier.vue';
    import purchaseAlibabaSettlementApi from '@/api/erp/finances/purchaseAlibabaSettlementApi.js';
	 
	 let globalTable=ref();

	 const state = reactive({
	 		tableData:{records:[],total:0},
	 		queryParams:{
	 			search:"",
	 		},
	 });
	 const { queryParams,tableData, } = toRefs(state);
	  function handleQuery(){
	 	  globalTable.value.loadTable(state.queryParams);
	  }
	  function show(param){
		  state.queryParams=param;
		  handleQuery();
		  
	  }
	   defineExpose({ show});
	  function loadTableData(params){
	  		  purchaseAlibabaSettlementApi.payList(params).then(res=>{
	  				 state.tableData.records=res.data.records;
	  				 state.tableData.total=res.data.total;
	  		  })
	  }
	 function downLoadExcel(){
	 	listApi.getPaymentReportExcel(state.queryParams) 
	 }
	 
	 
</script>
<style>
</style>