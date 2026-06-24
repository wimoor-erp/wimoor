<template>
	  <el-dialog
	    v-model="dialogVisible"
	    title="退货总览"
	    width="70%"
		top="1vh"
	    :before-close="handleClose"
	  >
	    <div>
			<el-space>
				<div>
					  <el-image :src="row.image"   style="width:80px;height:80px" ></el-image>
				</div>
				<div>
					<div style="padding-bottom:5px">{{row.name}}</div>
					<div style="padding-bottom:10px" class="font-extraSmall">{{row.sku}}</div>
					<el-space spacer="|" >
						<div><span class="font-extraSmall">ASIN：</span>{{row.asin}}</div>
						<div><span class="font-extraSmall">店铺：</span>{{row.groupname}}</div>
						<div><span class="font-extraSmall">国家：</span>{{row.marketname}}</div>
					</el-space>
				</div>
			</el-space>
			 <el-tabs v-model="activeName" class="demo-tabs" @tab-change="handleTabChange">
				 <div style="padding-bottom:20px"> <Datepicker ref="datepickers" :shortIndex="1" @changedate="changedate" /></div>
			     <el-tab-pane label="退货汇总" name="summary">
					 <SummaryChartLine ref="summaryChartLineRef"></SummaryChartLine>
					 <SummaryChartPie ref="summaryChartPieRef"></SummaryChartPie>
				 </el-tab-pane>
			     <el-tab-pane label="订单明细" name="detail">
					 <Table ref="tableRef"   @change="getdata" />
				 </el-tab-pane>
			   </el-tabs>   
				
		</div>
	    <template #footer>
	      <div class="dialog-footer">
	        <el-button @click="dialogVisible = false">关闭</el-button>
	      </div>
	    </template>
	  </el-dialog>
</template>

<script setup>
		import { ref,reactive,onMounted,watch,nextTick,toRefs } from 'vue';
		import SummaryChartLine from "./summary_chart_line.vue";
		import SummaryChartPie from "./summary_chart_pie.vue";
		import Table from "./table.vue";
		import Datepicker from '@/components/header/datepicker.vue';
		const summaryChartLineRef=ref();
		const summaryChartPieRef=ref();
		const tableRef=ref();
		let state=reactive({
			dialogVisible:false,
			queryParam:{},
			activeName:"summary",
			row:null
		});
		const {  dialogVisible,activeName,queryParam, row} = toRefs(state);
			function changedate(dates){
				state.queryParam.startDate=dates.start;
				state.queryParam.endDate=dates.end;
				state.row.startDate=state.queryParam.startDate;
				state.row.endDate=state.queryParam.endDate;
				tableRef.value.loadData(state.row);
			}
		function handleTabChange(){
			if(state.activeName=="summary"){
				summaryChartLineRef.value.show(state.row);
				summaryChartPieRef.value.show(state.row);
			}
		}
		function show(row){
	         state.dialogVisible=true;
			 state.row=row;
			 state.row.amazonauthid=row.amazonAuthId;
			 state.row.startDate=state.queryParam.startDate;
			 state.row.endDate=state.queryParam.endDate;
			 nextTick(()=>{
			 summaryChartLineRef.value.show(state.row);
			 summaryChartPieRef.value.show(state.row);
			 tableRef.value.loadData(state.row);
			 })
		}
		defineExpose({show})
</script>

<style>
</style>