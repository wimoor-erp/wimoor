<template>
		<el-row>
			<GlobalTable  ref="globalTable" :tableData="tableData"   height="calc(100vh - 256px)" @loadTable="loadTableData" :stripe="true"  style="width: 100%;margin-bottom:16px;">
				 <template #field>
				  <el-table-column prop="groupname"  label="站点" width="100"  sortable="custom" >
					  <template #default="scope"> {{scope.row.groupname}}-{{scope.row.marketname}}
					    </template>
					  </el-table-column>
				   <el-table-column prop="orderId" label="订单号" width='180' sortable="custom" />
			      <el-table-column prop="image" label="图片" width="60" >
			       <template #default="scope">
			        <el-image :src="scope.row.image"   width="40" height="40"  ></el-image>
			      </template>
			    </el-table-column>
			    <el-table-column prop="name" label="商品信息"  show-overflow-tooltip sortable="custom" >
			       <template #default="scope">
			          <div class='name'>{{scope.row.name}}</div>
			          <div class='sku'>{{scope.row.sku}} 
			            <copy title='复制' theme="outline" size="14" fill="#666" :strokeWidth="3"/> 
						<span style="padding-left:20px" class="font-extraSmall">ASIN:{{scope.row.asin}}</span>
			          </div>
					 
			      </template>
			    </el-table-column>
				<el-table-column prop="returnDate" label="购买时间" width="100"  sortable="custom" >
					<template #default="scope">
					    {{dateFormat(scope.row.purchaseDate)}} 
					</template>
				</el-table-column>	
			    <el-table-column prop="returnDate" label="退款时间" width="100"  sortable="custom" >
					<template #default="scope">
					    {{dateFormat(scope.row.returnDate)}} 
					</template>
				</el-table-column>	
			    <el-table-column prop="quantity" label="退货数量" width="100"  sortable="custom"  />
				<el-table-column prop="itemPrice" label="退货金额" width="100"  sortable="custom"  />
				<el-table-column prop="detailedDispositionName" label="商品状况" width="120" sortable="custom"   >
					<template #default="scope">
					   <div> {{scope.row.detailedDispositionName}} </div>
					   <div class="font-extraSmall"> {{scope.row.statusname}} </div>
					</template>
				</el-table-column>
				<el-table-column prop="reasonname" label="退货原因"   sortable="custom"/>
				<el-table-column prop="customerComments" label="退货留言"    sortable="custom"/>
				<el-table-column prop="licensePlateNumber" label="追踪编号"    sortable="custom"/>
			  </template>
			 </GlobalTable>
			</el-row>
</template>

<script setup>
	import { ref ,watch,reactive,onMounted,onUpdated} from 'vue'
	import GlobalTable from "@/components/Table/GlobalTable/index.vue";
	import orderListApi from "@/api/amazon/order/orderListApi.js";
	import {dateFormat} from '@/utils/index.js';
	import {ElMessage } from 'element-plus';
	const globalTable=ref(GlobalTable);
		let fromDate =ref()
		let toDate =ref()
		let tableData=reactive({records:[],total:0})
		let queryparams=ref();
		function handleQuery(){
			loadData(queryparams.value);
		}
		function loadData(params){
				globalTable.value.loadTable(params);
		}
		function loadTableData(params){
			queryparams.value=params;
			orderListApi.getOrderReturnList(params).then((res)=>{
				tableData.records = res.data.records
				tableData.total =res.data.total
			})
		}
		function downloadList(){
			orderListApi.downloadReturnlist(queryparams.value); 
		}
		defineExpose({
		   loadData,downloadList
		 })
</script>

<style>
</style>
