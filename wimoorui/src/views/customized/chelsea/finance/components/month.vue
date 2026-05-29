<template>
	<el-table
	border
	ref="tableRef"
	 :row-class-name="tableRowClassName"
	  show-summary
	    :summary-method="getSummaries"
	:data="tableData"
	>
		<el-table-column
		 class-name="column-lable-style"
		 label="费用项" prop="name"></el-table-column>
		 <el-table-column v-for="item in colunmData" :key="item.value" :prop="item.label">
			 <template #header>
				 <span v-if="item.isnow">
					 <el-space>
					 {{item.label}}
					 <el-tooltip
					 	  effect="dark"
					 	  content=""
					 	  placement="top-start"
					 	>
					 	 <template #content> 
					 	 <div>本月费用为预估费用:广告费用，coupon，仓储费和赔偿费用以及退货费用取前三个月平均值.</div>
					 	  </template>
					 	  <el-icon class="ic-cen font-small"><InfoFilled /></el-icon>
					 	</el-tooltip>
					 	</el-space>
				 </span>
				 <span v-else>
					 {{item.label}}
				 </span>
				 
			 </template>
			 <template #default="scope">
				 {{outputmoney(scope.row[item.label])}}
			 </template>
		 </el-table-column>
		 <el-table-column 
		 class-name="column-lable-sum-style"
		 label="合计" align="right" prop="sum">
			 <template #default="scope" >
				<span :class="scope.row.name==='销售额'||scope.row.name==='结算费用'?'text-red':'text-green'">{{outputmoney(scope.row.sum)}}</span>
			 </template>
		 </el-table-column>
	</el-table>
</template>

<script setup>
import { reactive,toRefs,ref,onMounted, nextTick } from 'vue';
import {Search,InfoFilled,HomeFilled} from '@element-plus/icons-vue';
import financesApi from '@/views/customized/chelsea/api/finances/financesApi';
import {outputmoney} from '@/utils/index.js';
	const state =reactive({
		tableData:[
			{name:"销售额(含税）"},
			{name:"销售额",},
			{name:"退货金额"},
			{name:"FBA费"},
			{name:"退货金额"},
			{name:"数字服务费"},
			{name:"佣金"},
			{name:"赔偿金"},
			{name:"仓储费"},
			{name:"低价清仓"},
			{name:"广告花费"},
			{name:"采购成本"},
			{name:"利润"},
			
		],
		colunmData:[
			// {label:"2024-09",},
			// {label:"2024-10",},
			// {label:"2024-11",},
		],
	})
	const {
	 tableData,	
	 colunmData,
	}=toRefs(state)
	const tableRef=ref();
	function show(data){
		state.colunmData=[];
		nextTick(()=>{
		state.colunmData=data.colunmData;
		state.tableData=data.tableData;
		if(tableRef.value){
				tableRef.value.doLayout();
		}
		})
		
	}
	function tableRowClassName({ row,  rowIndex, } ){
	  if (row.name =="结算费用") {
	    return 'warning-row'
	  } else if (rowIndex === 3) {
	    return 'success-row'
	  }
	  return ''
	}
function getSummaries({columns,data}){
	console.log(columns,data);
		var arr = ["利润"];
	   var cost=["退货金额","广告花费","采购成本","仓储费","FBA费","佣金","赔偿金","低价清仓","数字服务费"];
	   var salesamount=[];
	columns.forEach((item,index)=>{
				if(item.property=="name" ){
					return ;
				}
				if(index>=1){
				 arr[index]=0;
				 data.forEach(mitem=>{
					 if(cost.includes(mitem.name)){
						arr[index]=arr[index]+mitem[item.property];
					 }else if(mitem.name=="销售额"){
						arr[index]=arr[index]+mitem[item.property];
						salesamount[index]=mitem[item.property];
					 }
				 })
				}
			})
			arr.forEach((item,index)=>{
				if(index>=1){
					 arr[index]=outputmoney(arr[index])+" | 利润率("+ outputmoney(arr[index]/salesamount[index])+")";
					}
			});

		return  arr
	}
	defineExpose({
		show,
	})
	
	 
</script>

<style >
	td.column-lable-style .cell{
		line-height: 40px;
	}
	td.column-lable-sum-style .cell{
		font-weight: 700;
	}
	.el-table .warning-row {
	  background-color: var(--el-color-warning-light-9);
	}
	.el-table .success-row {
	  --el-table-tr-bg-color: var(--el-color-success-light-9);
	}
	
</style>
 