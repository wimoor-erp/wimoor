<template>
	<div class="main-sty">
<div class="con-header" >
	     <MyHeader @getdata="loadData"></MyHeader>
		</div>
	<el-row>
	  <GlobalTable ref="globalTable" :tableData="tableData" height="calc(100vh - 254px)" @loadTable="loadTableData" :stripe="true"  style="width: 100%;margin-bottom:16px;">
		   <template #field>
				<el-table-column prop="marketname" label="站点" width="100"  >
					 <template #default="scope">
						 <span>{{scope.row.groupname}}</span> <br />
						 <span>{{scope.row.marketname}}</span>
					 </template>
				 </el-table-column>
				 <el-table-column prop="amazon_order_id" label="订单" width="170"  >
				 	 <template #default="scope">
				 		 <span>{{scope.row.amazon_order_id}}</span> <br />
				 		 <el-tag>{{scope.row.order_status}}</el-tag>
				 	 </template>
				  </el-table-column>
				  <el-table-column prop="purchase_date" label="购买时间" width="150"  >
				  	 <template #default="scope">
				  		 <span>{{dateFormat(scope.row.purchase_date)}}</span>  
				  	 </template>
				   </el-table-column>
				  <el-table-column prop="feeddate" label="评论时间" width="150"  >
				  	 <template #default="scope">
				  		 <span>{{dateFormat(scope.row.feeddate)}}</span>  
				  	 </template>
				   </el-table-column>
				  <el-table-column prop="sku" label="产品" width="300"  show-overflow-tooltip >
				  	 <template #default="scope">
						 <el-space>
							 <el-image :src="scope.row.image" style="width:45px;height:45px"></el-image>
							 <div>
								 <div>{{scope.row.name}}</div><div class="text-warning">{{scope.row.sku}}</div>
							 </div>
						 </el-space>
				  	 </template>
				   </el-table-column>
				   <el-table-column prop="rating" label="评星" width="150"  >
				   	 <template #default="scope">
				   						   <el-rate
				   						      v-model="scope.row.rating"
				   						      disabled
				   						      text-color="#ff9900"
				   						    />
				   	 </template>
				    </el-table-column>
					<el-table-column prop="comments" label="评论"    >
						 <template #default="scope">
								<div>{{scope.row.comments}}</div>
						 </template>
					 </el-table-column>
					 <el-table-column prop="response" label="回复"    >
					 	 <template #default="scope">
					 			<div>{{scope.row.response}}</div>
					 	 </template>
					  </el-table-column>
					  <el-table-column prop="email" label="邮箱"    >
					  	 <template #default="scope">
					  			<div>{{scope.row.email}}</div>
					  	 </template>
					   </el-table-column>
		  </template>
	  </GlobalTable>
	</el-row>
 </div>
</template>
<script>
    export default{ name:"Coupon" };
</script>
<script setup>
	import { ref ,watch,reactive,onMounted,onUpdated,getCurrentInstance, toRefs} from 'vue';
	import GlobalTable from "@/components/Table/GlobalTable/index.vue";
	import orderFeedbackApi from "@/api/amazon/performance/orderFeedbackApi.js";
	import MyHeader from "./header.vue"
	import {dateFormat,formatFloat,getCurrencyMark} from '@/utils/index.js';
	import {ElMessage } from 'element-plus';
	        const globalTable=ref(GlobalTable);
			let state=reactive({
				tableData:{records:[],total:0},
				summaryData:{},
				queryParams:{},
			});
			defineExpose({
			  loadData,
			})
			
			 const {
			   tableData,
			   summaryData,
			 } = toRefs(state);
			 
			onMounted(()=>{
				 
			})
		 
			function loadData(params){
				globalTable.value.loadTable(params);
			}
			
			function loadTableData(params){
				state.queryParams=params;
				orderFeedbackApi.list(params).then((res)=>{
					if(res.data){
						state.tableData.records = res.data.records;
						state.tableData.total =res.data.total;
					}else{
						state.tableData.records = [];
						state.tableData.total =0;
					}
					
				})
			}
	 
</script>

<style>
	.sale-price {
	    font-weight: 600;
	    color: #f5a20c;
	} 
</style>

