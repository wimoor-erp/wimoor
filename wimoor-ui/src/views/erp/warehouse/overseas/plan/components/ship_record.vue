<template>
	<el-dialog title="发货记录" 
	           v-model="dialog.visible"
	           :append-to-body="true" 
			   width="80%">
			<el-table border :data="tableData"    height="calc(100vh - 300px)">
				<el-table-column label="编码/创建日期">
					<template #default="scope">
						<el-link :underline="false" type="primary" class="font-small" @click.stop="shipmentfollow(scope.row)">{{scope.row.shipmentid}}</el-link>
						<p>{{scope.row.createdate}}</p>
					</template>
				</el-table-column>
				<el-table-column label="SKU">
					<template #default="scope">
						<span>{{scope.row.sku}}</span>
					</template>
				</el-table-column>
				<el-table-column label="渠道">
					<template #default="scope">
						<span> {{scope.row.channame}} </span>
					</template>
				</el-table-column>
				<el-table-column label="数量" width="65">
					<template #default="scope">
						<span>{{scope.row.Quantity}}</span>
					</template>
				</el-table-column>
				<el-table-column label="状态">
					<template #default="scope">
						<el-tag v-if="scope.row.auditstatus==1" effect="plain" type="info" >待审核</el-tag>
						<el-tag v-if="scope.row.auditstatus==2"  effect="plain" type="primary">配货中</el-tag>
						<el-tag v-if="scope.row.auditstatus==3" effect="plain" type="success">已发货</el-tag>
						<el-tag v-if="scope.row.auditstatus==4" type="success" effect="plain">已完成</el-tag>
						<el-tag v-if="scope.row.auditstatus==5" type="danger" effect="plain">已驳回</el-tag>
					</template>
				</el-table-column>
				<el-table-column label="发货日期">
					<template #default="scope">
						<span>{{scope.row.shiped_date}}</span>
					</template>
				</el-table-column>
				<el-table-column label="预期到货时间">
					<template #default="scope">
						<span  >{{dateFormat(scope.row.oldArrivalTime)}}</span>
					</template>
				</el-table-column>
				<el-table-column label="备注">
					<template #default="scope">
						<div>
							{{scope.row.remark}}
						</div>
					</template>
				</el-table-column>
			</el-table>
		</el-dialog>
</template>

<script setup>
	import{ref,reactive,toRefs,onMounted,inject}from"vue"
	import {Close} from '@element-plus/icons-vue';
	import * as echarts from 'echarts';
	import shipmentApi from "@/api/amazon/inbound/shipmentApi.js";
	import {dateFormat,getDateValue} from "@/utils/index.js";
	import { ElMessage, ElMessageBox } from 'element-plus';
	import {tranStatus,tranStatusType} from "@/hooks/erp/shipment/shipment_status.js";
	import { useRoute,useRouter } from 'vue-router';
	import orderPlanApi from '@/api/erp/order/orderPlanApi.js';
	import dispatchOverseaApi from '@/api/erp/inventory/dispatchOverseaApi.js';
	
	
	const state=reactive({
		  dialog:{visible:false},tableData:[],uploading:false
	})
	const {
	   dialog ,tableData,loading,
	} = toRefs(state);
	let router = useRouter();
	const emitter = inject("emitter");
	function show(itemrow,parentrow){
		state.dialog.visible=true;
		dispatchOverseaApi.getShipArrivalTimeRecord({country:"",groupid:"",sku:itemrow.sku,warehouseid:itemrow.warehouseid}).then(res=>{
			state.tableData=res.data;
		})
	}
	function editShipHistory(row){
		row.state=false;
	}
	function rowClassName({row, rowIndex}){
		if(row.badstatus=='isbad'){
			 return 'badShip';
		}else return '';
	}
	//跟踪货件
	function shipmentfollow(row){
		state.dialog.visible=false;
		router.push({
			path:"/e/w/os/d",
			query:{
				title:'海外仓备货单详情',
				path:"/e/w/os/d",
				id:row.id
			},
		})
	}
	 
	defineExpose({
	  show
	})
</script>

<style>
	.badShip{
		 background: #fff3ec !important;
	}
	.reveiveQty{
		padding-right:10px;
	}
</style>