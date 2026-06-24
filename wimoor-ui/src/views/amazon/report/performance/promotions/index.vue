<template>
<div class="con-header" >
	     <MyHeader @getdata="loadData"></MyHeader>
		</div>
	<el-row>
	  <GlobalTable ref="globalTable" :tableData="tableData" height="calc(100vh - 254px)" :default-expand-all="true" @loadTable="loadTableData" :stripe="true"  style="width: 100%;margin-bottom:16px;">
	
		   <template #field>
		   <el-table-column type="expand">
		   				 <template #default="props">
		   				   <div style="width:100%">
		   					 <el-table :data="props.row.asindetail" :border="childBorder">
								  <el-table-column label="产品信息" prop="image" show-overflow-tooltip  >
									  <template #default="scope">
										  <el-space>
											   <el-image style="width:60px;height:60px;" :src="scope.row.image"></el-image>
											   <div>
												   {{scope.row.name}}
												   <div>
												    <span class="font-extraSmall">ASIN:</span> {{scope.row.asin}}   <span class="font-extraSmall">| SKU:{{scope.row.sku}}</span>
												   </div>
											   </div>
											  
										  </el-space>
									   </template>
									  </el-table-column>
							    <el-table-column label="库存"  width="140"  prop="afnWarehouseQuantity" />
		   					   <el-table-column label="浏览次数"  width="140"  prop="productGlanceViews" />
		   					   <el-table-column label="销售量" width="140" prop="productUnitsSold" />
		   					   <el-table-column label="收益" width="140"  prop="productRevenue" />
		   					 </el-table>
		   				   </div>
		   				 </template>
		   			   </el-table-column>
		<el-table-column prop="marketname" label="站点" width="100"  >
			 <template #default="scope">
				 <span>{{scope.row.groupname}}</span> <br />
				 <span>{{scope.row.marketname}}</span>
			 </template>
		 </el-table-column>
	    <el-table-column prop="promotionid" label="Promotion"    >
			 <template #default="scope">
				 <span>{{scope.row.promotionid}} </span>&nbsp;
				 <el-tag v-if="scope.row.discount_type=='PERCENT_OFF_LIST_PRICE'">打折</el-tag>
				 <el-tag v-else-if="scope.row.discount_type=='AMOUNT_OFF_LIST_PRICE'">折扣价</el-tag><br />
				 <span>{{scope.row.name}}</span>
			 </template>
		 </el-table-column>
		 <el-table-column prop="couponid" label="类型"  width="140"    >
		 	 <template #default="scope">
		 	  <span>{{scope.row.type}}</span>
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
		 <el-table-column prop="budget" label="status" width="140"  sortable="custom"  >
		 	 <template #default="scope">
		 		 <span>{{scope.row.status}}</span>  
				 
		 	 </template>
		  </el-table-column>
		  <el-table-column prop="glance_views" label="浏览次数"  width="140"  sortable="custom" >
		  	 <template #default="scope">
		  		 <span  >
		  		 		{{ scope.row.glance_views}}
		  		 </span>
		  	 </template>
		   </el-table-column>
		   <el-table-column prop="units_sold" label="销售量"  width="140"  sortable="custom" >
		   	 <template #default="scope">
		   		 <span  >
		   		 		{{ scope.row.units_sold}}
		   		 </span>
		   	 </template>
		    </el-table-column>
		   		
		  <el-table-column prop="revenue" label="收益"  width="140"  sortable="custom"      >
		  	 <template #default="scope">
		  		 <span> {{formatFloat(scope.row.revenue)}}</span> 
		  	 </template>
		   </el-table-column>
	
		  </template>
	  </GlobalTable>
	</el-row>
 
</template>
<script>
    export default{ name:"促销" };
</script>
<script setup>
	import { ref ,watch,reactive,onMounted,onUpdated,getCurrentInstance, toRefs} from 'vue';
	import GlobalTable from "@/components/Table/GlobalTable/index.vue";
	import promotionsApi from "@/api/amazon/performance/promotionsApi.js";
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
				promotionsApi.list(params).then((res)=>{
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

