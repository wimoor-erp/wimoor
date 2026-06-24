<template>
	<el-dialog v-model="dialogVisible" top="3vh" title="修改分摊费用" style="width:70%">
		<div class="con-header">
			<el-row>
				 <el-alert title="可以对未关联发货单的商品成本进行重置,
				 修改后将会影响财务模块的利润报表与销售模块的利润统计的成本数据,
				 系统数据次日生效.
				 " type="warning" show-icon />
			</el-row>
		</div>
			<el-row>
				<el-table :data="tableData">
					 <el-table-column label="店铺-站点" prop="warehouse" width="130">
						 <template #default="scope">
							({{scope.row.groupname}} - {{scope.row.marketname}})
						 </template>
					 </el-table-column>
							<el-table-column label="平台SKU"  width="130" prop="sku" ></el-table-column>
							<el-table-column label="发出数量"  width="100" prop="QuantityShipped" ></el-table-column>
							<el-table-column label="采购成本" prop="unitcost" >
								<template #default="scope">
									<el-input  
									 @input="changePrice(scope.row)"
									 v-model="scope.row.unitcost" clearable>
										<template #prefix>
											￥
										</template>
									</el-input>
								</template>
							</el-table-column>
							<el-table-column label="采购总成本"   prop="totalcost"  >
							<template #default="scope">
									<el-input
									 @input="changeSumPrice(scope.row)"
									 v-model="scope.row.totalcost"   clearable>
										<template #prefix>
											￥
										</template>
									</el-input>
								</template>
							</el-table-column>
							<el-table-column label="头程费用"  prop="unittransfee"  >
							<template #default="scope">
									<el-input 
									@input="changeInitialCost(scope.row)"
									v-model="scope.row.unittransfee"  clearable>
										<template #prefix>
											￥
										</template>
									</el-input>
								</template>
							</el-table-column>
							<el-table-column label="头程总费用"  prop="totaltransfee"  >
							<template #default="scope">
									<el-input
									@input="changeSumInitialCost(scope.row)"
									 v-model="scope.row.totaltransfee"   clearable>
										<template #prefix>
											￥
										</template>
									</el-input>
								</template>
							</el-table-column>
							<el-table-column label="操作"  width="65" prop="image" >
								<template #default="scope">
									<el-button type="primary" link @click="deleteItem(scope.$index)">删除</el-button>
								</template>
							</el-table-column>
				</el-table>
			</el-row>
	
	<template #footer>
				  <div style="padding-top:10px;">
				  	<el-space>
				  		<el-button type="" @click="dialogVisible=false">关闭</el-button>
				  		<el-button type="primary" @click="submitForm">提交</el-button>
				  	</el-space>
				  </div>
	</template>
	</el-dialog>
	
</template>
<!-- <ResetAddDialog ref="dialogRef" @getRowData="getRowData" /> -->
<script setup>
	import { ref,reactive,onMounted,toRefs,inject} from 'vue'
	import ResetAddDialog from "./resetAddDialog.vue"
	import {useRouter } from 'vue-router';
	import { ElMessage, ElMessageBox} from 'element-plus';
	import {formatFloat,CheckInputFloat} from '@/utils/index.js';
	import inboundItemApi from '@/api/amazon/inbound/inboundItemApi.js';
	import shipmentPlacementApi from '@/api/erp/shipv2/shipmentPlacementApi.js';
	  const emit = defineEmits([ 'change']);
	const router = useRouter()
	const dialogRef = ref()
	const state =reactive({
		tableData:[],
		dialogVisible:false,
		version:'',
	})
	const {
		tableData,dialogVisible,version,
	}=toRefs(state)
	
	function changePrice(row){
		row.unitcost=CheckInputFloat(row.unitcost);
		row.totalcost = formatFloat(row.unitcost*row.QuantityShipped);
	}
	function changeSumPrice(row){
		row.totalcost=CheckInputFloat(row.totalcost);
		row.unitcost = formatFloat(row.totalcost/row.QuantityShipped);
	}
	function changeInitialCost(row){
		row.unittransfee=CheckInputFloat(row.unittransfee);
		row.totaltransfee = formatFloat(row.unittransfee*row.QuantityShipped);
	}
	function changeSumInitialCost(row){
		row.totaltransfee=CheckInputFloat(row.totaltransfee);
		row.unittransfee = formatFloat(row.totaltransfee/row.QuantityShipped);
	}
	function deleteItem(index){
		state.tableData.splice(index,1)
	}
	function show(rows,version){
		state.version=version;
		state.dialogVisible=true;
		state.tableData=JSON.parse(JSON.stringify(rows));
	}
	function submitForm(){
		state.tableData.forEach(item=>{
			if(item.sku){
				item.sellersku=item.sku;
			}
		});
		if(state.version=='isV2'){
			shipmentPlacementApi.updateFeeByShipment(state.tableData).then((res)=>{
				ElMessage.success('操作成功！');
				state.dialogVisible=false;
				emit("change");
			});
		}else{
			inboundItemApi.updateFeeByShipment(state.tableData).then((res)=>{
				ElMessage.success('操作成功！');
				state.dialogVisible=false;
				emit("change");
			});
		}
		
	}
	
	defineExpose({
		show
	})
</script>

<style>
	.he-scr-car{
		height:calc(100vh - 150px);
		margin-bottom: 20px;
	}
</style>
