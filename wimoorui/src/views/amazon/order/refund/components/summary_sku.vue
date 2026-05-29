<template>
	<el-row>
	<GlobalTable  ref="globalTable" :tableData="tableData"   height="calc(100vh - 256px)" @loadTable="loadTableData" :stripe="true"  style="width: 100%;margin-bottom:16px;">
		 <template #field>
			 <el-table-column prop="image" label="图片" width="60" >
			    <template #default="scope">
			     <el-image :src="scope.row.image"   width="40" height="40"  ></el-image>
			   </template>
			 </el-table-column>
			 <el-table-column prop="sku" label="名称/SKU" width="320" sortable="custom" show-overflow-tooltip>
			    <template #default="scope">
			       <div class='name'>{{scope.row.name}}</div>
			       <div class='sku'>{{scope.row.sku}} 
			         <copy title='复制' theme="outline" size="14" fill="#666" :strokeWidth="3"/>
			 		<span class="font-extraSmall">ASIN:{{scope.row.asin}}</span>
			       </div>
			   </template>
			 </el-table-column>
		<el-table-column prop="marketname" label="站点"      sortable="custom">
			  <template #default="scope">
				  <div>{{scope.row.groupname}}-{{scope.row.marketname}}</div>
			    </template>
			  </el-table-column>
	    <el-table-column prop="quantity"   label="退货数量"   sortable="custom" />
		<el-table-column prop="returnrate" label="退货率"     sortable="custom" >
		<template #default="scope">
			               <div v-if="scope.row.returnrate>1">100%</div>
						  <div v-else-if="scope.row.returnrate">{{formatFloat(parseFloat(scope.row.returnrate)*100)}}%</div>
						  <div v-else>--</div>
		  </template>
		</el-table-column>
		<el-table-column prop="itemPrice"  label="退款金额"      sortable="custom" />
		<el-table-column prop="sellable"   label="可售数量"      sortable="custom" />
		<el-table-column prop="unsellable" label="不可售"        sortable="custom" />
		<el-table-column prop="defective"  label="可能瑕疵"      sortable="custom" />
		<el-table-column prop="other"     label="不确定可售状态" sortable="custom"  />
		<el-table-column prop="returnrate" label="操作" width="100"    >
		  <template #default="scope">
			  <el-button link @click="handleShowDetail(scope.row)" type="primary">详情</el-button>
		  </template>
		</el-table-column>
	  </template>
	 </GlobalTable>
	</el-row>
	<SkuDetail ref="skuDetailRef"></SkuDetail>
</template>

<script setup>
	import { ref ,watch,reactive,onMounted,onUpdated,getCurrentInstance, toRefs} from 'vue';
	import GlobalTable from "@/components/Table/GlobalTable/index.vue";
	import productFbaFeeApi from "@/api/amazon/product/productFbaFeeApi.js";
	import MyHeader from "./header.vue"
	import {dateTimesFormat,formatFloat} from '@/utils/index.js';
	import {ElMessage } from 'element-plus';
	import orderListApi from "@/api/amazon/order/orderListApi.js";
	import SkuDetail from "./skudetail.vue"
	import {MenuUnfold,Plus,SettingTwo,Help,Copy,MoreOne,ChartPie} from '@icon-park/vue-next';
	const emit = defineEmits(['change']);
	const skuDetailRef=ref();
	const globalTable=ref(GlobalTable);
	let state=reactive({
		tableData:{records:[],total:0},
		summaryData:{},
		queryParams:{},
	});
	const {
	  tableData,
	  summaryData,
	  queryParams:{}
	} = toRefs(state);
	
	function loadTableData(params){
		orderListApi.getOrderReturnSKUList(params).then((res)=>{
			state.tableData.records = res.data.records
			state.tableData.total =res.data.total;
		})
	}
	
	function loadData( ){
		globalTable.value.loadTable(state.queryParams);
	}
	function handleShowDetail(row){
		skuDetailRef.value.show(row);
	}
	function handleQuery(params){
		state.queryParams=params;
		loadData();
	}
	
	defineExpose({
	  handleQuery,
	})
</script>

<style>
</style>