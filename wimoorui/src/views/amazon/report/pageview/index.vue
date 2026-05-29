<template>
 <div class="main-sty">
	<div class="con-header" >
	  <el-row>
	    <el-space >
		<Group ref="groups" @change="changeGroup" defaultValue="only"/>
		<Datepicker ref="datepickers" @changedate="changedate" />
	    <el-input  v-model="searchKeywords" clearable @input="searchConfirm" placeholder="请输入SKU" class="input-with-select" >
        </el-input>
	 </el-space>
	 </el-row>
	 </div>
	 <div class="con-body" >
	 <global-table  ref="globalTable" :tableData="tableData"  height="calc(100vh - 198px)" @loadTable="loadTableData" :stripe="true"  >
	 	 <template #field>
			<el-table-column prop="image" label="图片" width="60" >
			    <template #default="scope">
			     <el-image :src="scope.row.image"   width="40" height="40"  ></el-image>
			   </template>
			</el-table-column>
			<el-table-column prop="sku" label="SKU"  sortable="custom" show-overflow-tooltip >
			    <template #default="scope">
			     <div>{{scope.row.name}}</div>
				 <div>SKU:{{scope.row.sku}}</div>
			   </template>
			</el-table-column>
			<el-table-column prop="byday" label="日期" width="140"  sortable="custom" >
			    <template #default="scope">
			     <div>{{dateFormat(scope.row.byday)}}</div>
			   </template>
			</el-table-column>
			<el-table-column prop="sessions"  label="访问次数" width="110"  sortable="custom"/>
			<el-table-column prop="sessionPercentage"  label="访问转化率" width="110" sortable="custom">
				<template #default="scope">
					  <div>{{scope.row.sessionPercentage}}%</div>
					</template>
			   </el-table-column>
			<el-table-column prop="pageViews"  label="浏览次数" width="110" sortable="custom"/>
			<el-table-column prop="pageViewsPercentage" label="浏览转化率"  width="140" sortable="custom">
				<template #default="scope">
				  <div>{{scope.row.pageViewsPercentage}}%</div>
				</template>
			</el-table-column>
	 	    <el-table-column prop="buyBoxPercentage" label="购物车比例" width='130'   sortable="custom">
				<template #default="scope">
				  <div>{{scope.row.buyBoxPercentage}}%</div>
				</template>
			</el-table-column>
	      	<el-table-column prop="unitsOrdered" label="销量" width='100'   sortable="custom" >
				<template #default="scope">
				  <div>{{scope.row.unitsOrdered}}</div>
				  <div v-if="scope.row.unitsOrderedB2B">B2B:{{scope.row.unitsOrderedB2B}}</div>
				</template>
	      	</el-table-column>
			<el-table-column prop="unitSessionPercentage" label="商品转化率" width='120'   sortable="custom" >
				<template #default="scope">
					  <div>{{scope.row.unitSessionPercentage}}%</div>
					  <div v-if="scope.row.unitSessionPercentageB2B">B2B:{{scope.row.unitSessionPercentageB2B}}%</div>
					</template>
			</el-table-column>
			<el-table-column prop="orderedProductSales" label="销售额" width='100' sortable="custom" >
			<template #default="scope">
					  <div>{{scope.row.orderedProductSales}}</div>
					  <div v-if="scope.row.orderedProductSalesB2B">B2B:{{scope.row.orderedProductSalesB2B}}</div>
					</template>
			</el-table-column>
			<el-table-column prop="totalOrderItems"  label="订单" width='100' sortable="custom" >
			<template #default="scope">
					  <div>{{scope.row.totalOrderItems}}</div>
					  <div v-if="scope.row.totalOrderItemsB2B">B2B:{{scope.row.totalOrderItemsB2B}}</div>
					</template>
			</el-table-column>
		 </template>
		</global-table>
		</div>
	</div>
</template>

<script>
	import { ref ,reactive} from 'vue'
	import Group from '@/components/header/group.vue';
	import Datepicker from '@/components/header/datepicker.vue';
	import businessApi from "@/api/amazon/product/businessApi.js";
	import {dateFormat } from "@/utils/index"
	export default{
		name:'流量报表',
		components:{Group,Datepicker},
		setup(props){
			let globalTable=ref();
			let tableData=reactive({records:[],total:0});
			let searchKeywords=ref("");
			let myparams={};
			function loadData(){
				globalTable.value.loadTable(myparams);
			}
			function changeGroup(value){
				myparams.groupid =value.groupid;
				myparams.marketplaceid =value.marketplaceid;
				loadData();
			}
			function changedate(value){
				if(myparams.startDate){
					myparams.startDate =value.start;
					myparams.endDate =value.end;
					loadData();
				}else{
					myparams.startDate =value.start;
					myparams.endDate =value.end;
				}
			
			}
			function searchConfirm(value){
				myparams.search=searchKeywords.value;
				loadData();
			}
			function loadTableData(params){
				businessApi.getPageViewsList(params).then((res)=>{
					tableData.records = res.data.records
					tableData.total =res.data.total
				})
			}
			
			return {globalTable,//ref
			        tableData,searchKeywords,//modal
					loadTableData,loadData,changeGroup,changedate,searchConfirm,dateFormat,//funciton
				
			}
			}
		}
</script>

<style>
 
</style>