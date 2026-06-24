<template>
<div class="con-header" >
	     <MyHeader @getdata="loadData"></MyHeader>
		</div>
	<el-row>
	  <GlobalTable ref="globalTable" :tableData="tableData" height="calc(100vh - 254px)" @loadTable="loadTableData" :stripe="true"  style="width: 100%;margin-bottom:16px;">
		   <template #field>
		<el-table-column prop="total_price" label="站点" width="100"  >
			 <template #default="scope">
				 <span>{{scope.row.groupname}}</span> <br />
				 <span>{{scope.row.marketname}}</span>
			 </template>
		 </el-table-column>
	    <el-table-column prop="couponid" label="couponid"    >
			 <template #default="scope">
				 <span>{{scope.row.couponid}} </span>&nbsp;
				 <el-tag v-if="scope.row.discount_type=='PERCENT_OFF_LIST_PRICE'">打折</el-tag>
				 <el-tag v-else-if="scope.row.discount_type=='AMOUNT_OFF_LIST_PRICE'">折扣价</el-tag><br />
				 <span>{{scope.row.name}}</span>
			 </template>
		 </el-table-column>
		 <el-table-column prop="couponid" label="产品"    >
		 	 <template #default="scope">
		 		 <el-space>
					 <div style="borderRadius:var(--el-border-radius-base) " v-for="info in scope.row.asindetail">
						 <el-space>
						      <el-image :src="info.image" style="width: 60px; height: 60px"/>
							  <div><div><span class="font-extraSmall">ASIN:</span>{{info.asin}}</div>
							  <div><span class="font-extraSmall">SKU:</span><span >{{info.sku}}</span></div>
							  <div><span class="font-extraSmall">库存:</span>{{info.afnWarehouseQuantity}}
							  <span class="font-extraSmall"> |  </span>
							   <span class="font-extraSmall">价格:</span> <span class="sale-price">{{getCurrencyMark(scope.row.currency)}}{{info.price}} </span>
							  </div>
							  </div>
						  </el-space>
					 </div>
				 </el-space>
		 	 </template>
		  </el-table-column>
		 <el-table-column prop="start_date_time" label="时间区间" width="120"   >
		 	 <template #default="scope">
		 		 <span >{{dateFormat(scope.row.start_date_time)}} </span> <br />
				 <span  class="sale-price">{{dateFormat(scope.row.end_date_time)}} </span>
		 	 </template>
		  </el-table-column>
		 <el-table-column prop="currency" label="币种" width="80"   >
		 	 <template #default="scope">
		 		 <span>{{scope.row.currency}}</span> 
		 	 </template>
		  </el-table-column>
		 <el-table-column prop="budget" label="预算" width="140"  sortable="custom"  >
		 	 <template #default="scope">
		 		 <span>{{scope.row.budget}}</span> <span class="font-extraSmall">({{formatFloat(scope.row.budget_spend)}}/{{formatFloat(scope.row.budget_remaining)}})</span><br />
				      <el-progress
				        :text-inside="true"
				        :stroke-width="15"
				        :percentage="scope.row.budget_percentage_used"
				        status="exception"
				      >
				        <span>{{scope.row.budget_spend}}%</span>
				      </el-progress>
		 	 </template>
		  </el-table-column>
		  <el-table-column prop="discount_amount" label="折扣"  sortable="custom"   width="100"   >
		  	 <template #default="scope">
		  		 <span> {{formatFloat(scope.row.discount_amount)}}</span><br />
		  		 <span> {{formatFloat(scope.row.total_discount)}}</span>
		  	 </template>
		   </el-table-column>
		   <el-table-column prop="clips" label="赠券" width="80"  sortable="custom" >
		   	 <template #default="scope">
		   		 <span  >
		   		 		{{ scope.row.clips}}
		   		 </span>
		   	 </template>
		    </el-table-column>
			<el-table-column prop="redemptions" label="赠券抵用" width="100"  sortable="custom" >
				 <template #default="scope">
					 <span  >
					 		{{ scope.row.redemptions}}
					 </span>
				 </template>
			 </el-table-column>
			 <el-table-column prop="sales" label="销售额" width="100"  sortable="custom" >
			 	 <template #default="scope">
			 		 <span  >
			 		 		{{ scope.row.sales}}
			 		 </span>
			 	 </template>
			  </el-table-column>
			  <el-table-column prop="refreshtime" label="更新时间" width="100"   >
			  	 <template #default="scope">
			  		 <span >
			  		 	{{scope.row.refreshtime}} 
			  		 </span>
			  	 </template>
			   </el-table-column>
		  </template>
	  </GlobalTable>
	</el-row>
 
</template>
<script>
    export default{ name:"Coupon" };
</script>
<script setup>
	import { ref ,watch,reactive,onMounted,onUpdated,getCurrentInstance, toRefs} from 'vue';
	import GlobalTable from "@/components/Table/GlobalTable/index.vue";
	import couponApi from "@/api/amazon/performance/couponApi.js";
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
				couponApi.list(params).then((res)=>{
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

