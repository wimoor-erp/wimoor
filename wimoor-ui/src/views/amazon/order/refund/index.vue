<template>
	<div class="main-sty">
		<Header  @getdata="getdata" @download="downloadlist" />
		<el-tabs v-model="activeName"   @tab-change="handleQuery"  >
			<el-tab-pane label="概览图表" name="chart"> </el-tab-pane>
			<el-tab-pane label="SKU汇总" name="sku"> </el-tab-pane>
		    <el-tab-pane label="订单列表" name="order"> </el-tab-pane>
		</el-tabs>
		<Summarychart v-if="activeName=='chart'" ref="summarychartRef"></Summarychart>
		<Summarysku v-if="activeName=='sku'" ref="summaryskuRef"></Summarysku>
		<Table ref="tableRef" v-if="activeName=='order'" @change="getdata" />
	</div>
</template>

<script setup>
	import { ref ,watch,reactive,onMounted,nextTick} from 'vue'
	import Header from "./components/header.vue";
	import Table from "./components/table.vue";
	import Summarysku from './components/summary_sku.vue';
	import Summarychart from './components/summary_chart.vue';
 	const summarychartRef=ref();
	const summaryskuRef=ref();
	const tableRef = ref(Table); 
	let activeName=ref("chart");
	let amazonOrderRefundQueryPararm={};
	function getdata(data){
		if(!data.groupid){
			return ;
		}
		if(data.startDate==undefined || data.startDate==null || data.startDate==""){
			const end = new Date()
			const start = new Date()
			start.setTime(start.getTime() - 3600 * 1000 * 24 * 7)
			data.startDate =dateFormat(start);
			data.endDate =dateFormat(end);
		} 
		amazonOrderRefundQueryPararm=data;
		handleQuery();
	}
	function handleQuery(){
		nextTick(()=>{
			if(activeName.value=='chart'){
				summarychartRef.value.show(amazonOrderRefundQueryPararm);
			}else if(activeName.value=='sku'){
				summaryskuRef.value.handleQuery(amazonOrderRefundQueryPararm);
			}else{
			    tableRef.value.loadData(amazonOrderRefundQueryPararm);
			}
		})
	}
	function downloadlist(){
		tableRef.value.downloadList();
	}
 
</script>

<style>
</style>
