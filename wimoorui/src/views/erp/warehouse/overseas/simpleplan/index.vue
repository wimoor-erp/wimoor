<template>
	<div class="main-sty">
		<div class="con-header">
	<PlanHeader ref="headerRef" @getdata="changedata"></PlanHeader>
	</div>
	<div class="con-body ">
		<el-table height="40px">
			    <el-table-column label="图片"  width="70"/>
				<el-table-column label="名称/SKU"   />
				<el-table-column label="可用库存"  width="120"  />
				<el-table-column label="箱规"   width="120" />
				<el-table-column label="单箱尺寸"   width="120" />
				<el-table-column label="箱数"   width="120" />
				<el-table-column label="单箱体积cm³"  width="120"  />
				<el-table-column label="单箱体积m³"   width="120" />
				<el-table-column label="计划备货数量"   width="130" />
				<el-table-column label="总体积"    width="110"/>
				<el-table-column label="操作"   width="90"/>
		</el-table>
		<div 		 class="plan-table-order">
	<GlobalTable ref="globalTable"
				 :defaultSort="defaultSort"  
				 @selectionChange='selectChange' 
				 :tableData="tableData" 
				 height="calc(100vh - 300px)" 
				 :stripe="true"  
				 :show-header="false"
				 @loadTable="loadTableData" 
				 style="width: 100%;margin-bottom:16px;">
			<template #field>
				<el-table-column label="number" prop="number" >
				<template #default="scope">
					        <div class="erp-order-cell">
							  <el-space wrap :size="40">
							  	<div style="padding-left:10px">
									<el-checkbox v-model="scope.row.ischeck"  @change="handleRowSelectd" /></div>
								  <div><span class="font-extraSmall">订单号：</span>{{scope.row.number}}</div>
								  <div><span class="font-extraSmall">下单时间：</span>{{scope.row.createtime}}</div>
								  <el-tag type="danger" v-if="scope.row.auditstatus==0">已删除</el-tag>
								  <el-tag type="warning" v-if="scope.row.auditstatus==1">备货中</el-tag>
								  <el-tag type="success" v-if="scope.row.auditstatus==2">已完成</el-tag>
								  
								  <el-tag type="success" v-if="scope.row.isout">已出库</el-tag>
								  <el-tag type="warning" v-else>未出库</el-tag>
							</el-space>
								  <div style="width:90px;float:right;padding-left:20px"><el-button type="primary" @click="handleDetailShow(scope.row)" link>详情</el-button></div>
							</div>
							<el-table :data="scope.row.entrys" class="inner-table-order" :show-header="false">
								    <el-table-column label="图片"  width="70">
										<template #default="scope1">
													<el-image :src="scope1.row.image"  style="width:45px;height:45px"></el-image>
												</template>
										</el-table-column>
									<el-table-column label="名称/SKU"   >
										<template #default="scope1">
													<div>{{scope1.row.name}}</div>
													<div class="font-extraSmall">{{scope1.row.sku}}</div>
												</template>
										</el-table-column>
									<el-table-column label="库存" prop="qty" width="120">
									</el-table-column>
									<el-table-column label="箱规"   prop="boxnum"  width="120" />
									<el-table-column label="单箱尺寸"   width="110" >
									<template #default="scope">
											 <div v-if="scope.row.plength">
												 {{scope.row.plength}}*{{scope.row.pwidth}}*{{scope.row.pweight}}
											 </div>
											 <div v-else>--</div> 
										</template>
									</el-table-column>
									<el-table-column label="箱数"   width="120" >
									<template #default="scope">
													<div v-if="scope.row.boxnum>0&&scope.row.qty">{{formatFloat(scope.row.qty/scope.row.boxnum)}}</div>
												     <div else>--</div>
												</template>
									</el-table-column>
									<el-table-column label="单箱体积cm³"  width="120"  >
										<template #default="scope">
												 <div v-if="scope.row.plength">{{scope.row.plength*scope.row.pwidth*scope.row.pweight}}</div>
												 <div v-else>--</div> 
											 </template>
										</el-table-column>
									<el-table-column label="单箱体积m³"   width="120" >
										<template #default="scope">
												<div v-if="scope.row.quantity&&scope.row.plength">
												 {{parseFloat(scope.row.quantity)*parseFloat(scope.row.plength)*parseFloat(scope.row.pwidth)*parseFloat(scope.row.pweight)/1000000}}
											  </div>
												<div v-else>--</div>
											 </template>
										</el-table-column>
									<el-table-column label="计划备货数量"  prop="quantity"  width="130" >
									        <template #default="scope">
													{{scope.row.quantity}}	
											</template>
									</el-table-column>
									<el-table-column label="总体积"    width="110"/>
									<el-table-column label="操作"   width="90">
										  <template #default="scope1">
																			 
										  </template>
										</el-table-column>
							</el-table>
						</template>
				</el-table-column>
			</template>
		</GlobalTable>
		</div>
	</div>
	</div>
	<PlanDetail ref="planDetailRef" @change="handleQuery"></PlanDetail>
</template>
<script setup>
	import PlanHeader from"./components/plan_header.vue";
	import {ref,reactive,toRefs,onMounted}from"vue"
	import {MoreOne,} from '@icon-park/vue-next';
	import {Histogram} from '@element-plus/icons-vue'
	import { ElMessage, ElMessageBox, ElDivider } from 'element-plus'
	import {useRouter } from 'vue-router';
	import orderApi from "@/api/erp/order/orderApi.js";
	import planFormApi from "@/api/erp/order/planFormApi.js";
	import PlanDetail from "./components/plan_detail_dialog.vue";
	import {dateFormat,dateTimesFormat,formatFloat} from '@/utils/index.js';
	const router = useRouter();
	let globalTable=ref();
	let headerRef=ref();
	let planDetailRef=ref();
	const state = reactive({
		tableData:{records:[],total:0},
		queryParams:{},
		defaultSort:{"prop": 'opttime', "order": 'desc' },
		selectrows:[],
	})
	const {
		tableData,
		queryParams,
		defaultSort,
		selectrows,
	}=toRefs(state)
	 
	function changedata(params){
		state.queryParams.paramother=params;
		state.queryParams.sort=state.defaultSort.prop;
		state.queryParams.order=state.defaultSort.order;
		globalTable.value.loadTable(state.queryParams);
	}
	function handleQuery(){
		globalTable.value.loadTable(state.queryParams);
	}
    function handleDetailShow(row){
		planDetailRef.value.show(row);
	}
	function handleRowSelectd(selection) {
		var selection =[];
		state.tableData.records.forEach(item=>{
			if(item.ischeck){
				selection.push(item);
			}
		})
		state.selectrows=selection;
		headerRef.value.selectChange(state.selectrows);
	}
	
	function loadTableData(params){
		planFormApi.findPlanFrom(params).then((res)=>{
			state.tableData.records = res.data.records;
			state.tableData.records.forEach(item=>{
				item.ischeck=false;
			})
			state.tableData.total =res.data.total;
		})
	}
	
	onMounted(() => {
	   
	});
</script>

<style>
	.plan-table-order .el-table--default .cell{
		padding-left:0px;
		padding-right:0px;
	}
	.plan-table-order .el-table--default .inner-table-order .cell{
		padding-left:10px;
	}
	.erp-order-cell{
		padding:10px;
		border-top:solid 1px var(--el-card-border-color);
		border-bottom:solid 1px var(--el-card-border-color);
		background-color:var(--el-bg-color);
	}
</style>
