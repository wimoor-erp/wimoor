<template>
    <div  >
        <div class="con-header">
		<el-row>
			<el-space>
			<el-input  v-model="queryParams.search" @clear="handleQuery" placeholder="请输入订单编号" class="input-with-select" clearable>
			    <template #append>
			        <el-button @click="handleQuery">
			            <el-icon style="font-size: 16px;align-itmes:center">
			                <search />
			            </el-icon>
			        </el-button>
			    </template>
			</el-input>
			<el-checkbox v-model="queryParams.ischeck" @change="handleQuery"> 检查异常单据</el-checkbox>
			</el-space>
			<div class='rt-btn-group'>
				<el-button   class='ic-btn' title='帮助文档'>
			        <help theme="outline" size="16" :strokeWidth="3"/>
			    </el-button>
			</div>
		</el-row>
        </div>
        <!--表单-->
        <el-row>
			<GlobalTable ref="globalTable"
			 :tableData="tableData"  height="calc(100vh - 250px)" 
			 :defaultSort="{ prop: 'paytime', order: 'descending' }"  @loadTable="loadTableData" :stripe="true"  
			 style="width: 100%;margin-bottom:16px;">
				<template #field>
			    <el-table-column prop="orderid"  label="订单号" width="190" sortable="custom" show-overflow-tooltip>
					<template #default="scope">
											<div @click="handleToRow(scope.row.orderid)">{{scope.row.orderid}}
											<div v-if="scope.row.remark" class="text-red">{{scope.row.remark}}</div>
											</div>
										 
										</template>
					</el-table-column>
					<el-table-column prop="paytime"  width="158" label="订单支付时间" sortable="custom" >
					<template #default="scope">
											<div >{{dateTimesFormat(scope.row.paytime)}}</div>
										 
										</template>
					</el-table-column>
			    <el-table-column prop="name" label="订单名称"  sortable="custom" show-overflow-tooltip>
			    </el-table-column>
		
				<el-table-column prop="payamount" width="120" label="支付金额(元)"  sortable="custom" >
				</el-table-column>
				 <el-table-column prop="confirmamount"  width="145" label="确认收货金额(元)" sortable="custom" >
				</el-table-column>
			<el-table-column prop="fee_type"  width="155" label="确认收货时间" sortable="custom" >
			<template #default="scope">
				    <div >{{dateTimesFormat(scope.row.confirmtime)}}</div>
				</template>
			</el-table-column>
				   <el-table-column prop="paytype"  width="100" label="账期类型" sortable="custom" > </el-table-column>
				   <el-table-column prop="returntype"  label="是否有退款"  width="110"  sortable="custom" />
				   <el-table-column prop="returnamount"  label="退款金额(元)" width="120"  sortable="custom" />
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
     const emit = defineEmits(['change']);
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
	  function handleToRow(orderid){
		  emit("change",orderid);
	  }
	  function show(param){
		  state.queryParams=param;
		  handleQuery();
		  
	  }
	   defineExpose({ show});
	  function loadTableData(params){
	  		  purchaseAlibabaSettlementApi.orderList(params).then(res=>{
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