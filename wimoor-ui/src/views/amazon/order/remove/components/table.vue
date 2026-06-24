<template>
	<el-row>
	<GlobalTable  ref="globalTable" :tableData="tableData"   height="calc(100vh - 246px)" @loadTable="loadTableData" :stripe="true"  style="width: 100%;margin-bottom:16px;">
		 <template #field>

	      <el-table-column prop="image" label="图片" width="60" >
	       <template #default="scope">
	        <el-image :src="scope.row.image"   width="40" height="40"  ></el-image>
	      </template>
	    </el-table-column>
	    <el-table-column prop="name" label="商品信息"  show-overflow-tooltip>
	       <template #default="scope">
	          <div class='name'>{{scope.row.name}}</div>
	          <div class='sku'>{{scope.row.sku}} 
	            <copy title='复制' theme="outline" size="14" fill="#666" :strokeWidth="3"/>
				<span style="color: #000000;">ASIN:{{scope.row.asin}}</span>
	          </div>
			 
	      </template>
	    </el-table-column>
		<el-table-column prop="orderId" label="订单号" width='120' />
		<el-table-column prop="order_type" label="订单类型" width='100'  sortable="custom">
		<template #default="scope">
			    {{scope.row.orderType}} 
			</template>
		</el-table-column>
	    <el-table-column prop="purchase_date" label="订单时间" width="160"   sortable="custom" >
			<template #default="scope">
			    {{dateTimesFormat(scope.row.purchaseDate)}} 
			</template>
		</el-table-column>	
		<el-table-column prop="last_updated_date" label="更新时间" width="160"   sortable="custom" >
			<template #default="scope">
			    {{dateTimesFormat(scope.row.lastUpdatedDate)}} 
			</template>
		</el-table-column>
	    <el-table-column prop="requested_quantity" label="请求数量" width="100"    sortable="custom" >
		<template #default="scope">
			    {{scope.row.requestedQuantity}} 
			</template>
		</el-table-column>
		<el-table-column prop="in_process_quantity" label="处理数量"  width="100"  sortable="custom">
		<template #default="scope">
			    {{scope.row.inProcessQuantity}} 
			</template>
		</el-table-column>
		<el-table-column prop="disposed_quantity" label="完成数量"  width="100"  sortable="custom">
		<template #default="scope">
			    {{scope.row.disposedQuantity}} 
			</template>
		</el-table-column>
		<el-table-column prop="removal_fee" label="费用"  width="100"  sortable="custom">
		<template #default="scope">
			    {{scope.row.removalFee}} 
			</template>
		</el-table-column>
		<el-table-column prop="currency" label="币种"  width="80" />
	  </template>
	 </GlobalTable>
	</el-row>
</template>

<script setup>
	import { ref ,watch,reactive,onMounted,onUpdated} from 'vue'
	import GlobalTable from "@/components/Table/GlobalTable/index.vue";
	import orderListApi from "@/api/amazon/order/orderListApi.js";
	import {dateFormat,dateTimesFormat} from '@/utils/index.js';
	import {ElMessage } from 'element-plus';
	
	let fromDate =ref()
	let toDate =ref()
	let globalTable=ref(GlobalTable);
	let tableData=reactive({records:[],total:0})
	let queryparams=ref();
		onMounted(()=>{
		 
		})
	 
		function loadData(params){
			if(params.startDate==undefined || params.startDate==null || params.startDate==""){
				const end = new Date()
				const start = new Date()
				start.setTime(start.getTime() - 3600 * 1000 * 24 * 7)
				params.startDate =dateFormat(start);
				params.endDate =dateFormat(end);
			} 
			globalTable.value.loadTable(params);
		}
		
		function loadTableData(params){
			queryparams.value=params;
			orderListApi.getOrderRemoveList(params).then((res)=>{
				tableData.records = res.data.records
				tableData.total =res.data.total
			})
		}
		function downloadList(){
			orderListApi.downloadRemovelist(queryparams.value); 
		}
		
		defineExpose({ loadData,downloadList })	 
</script>

<style>
</style>
