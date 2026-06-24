<template>
	<el-dialog title="发货记录" 
	           v-model="dialog.visible"
	           :append-to-body="true" 
			   width="80%">
			<el-table border :data="tableData" :row-class-name="rowClassName"  height="calc(100vh - 300px)">
				<el-table-column label="货件编码/创建日期">
					<template #default="scope">
						<el-link :underline="false" type="primary" class="font-small" @click.stop="shipmentfollow(scope.row)">{{scope.row.shipmentid}}</el-link>
						<p>{{scope.row.createdate}}</p>
					</template>
				</el-table-column>
				<el-table-column label="SKU">
					<template #default="scope">
						<span>{{scope.row.SellerSKU}}</span>
					</template>
				</el-table-column>
				<el-table-column label="渠道">
					<template #default="scope">
						<span> {{scope.row.company}}<br>{{scope.row.channel}}</span>
					</template>
				</el-table-column>
				<el-table-column label="数量" width="65">
					<template #default="scope">
						<span>{{scope.row.Quantity}}</span>
					</template>
				</el-table-column>
				<el-table-column label="已接收/状态">
					<template #default="scope">
						<span class="reveiveQty">{{scope.row.reveiveQuantity}}</span>
						<span class="font-success" v-if="scope.row.statusName">{{scope.row.statusName}}</span>
						<span class="font-success" v-else >{{scope.row.status}}</span>
					</template>
				</el-table-column>
				<el-table-column label="发货日期">
					<template #default="scope">
						<span>{{scope.row.shiped_date}}</span>
					</template>
				</el-table-column>
				<el-table-column label="进出港日期" width="160">
					<template #default="scope">
						<p v-if="scope.row.state">进港:{{dateFormat(scope.row.oldInarrtime)}}</p>
						<div v-else class="flex-center m-b-8">
							<span style="width: 75px;">进港:</span>
						 <el-date-picker
						        v-model="scope.row.inarrtime"
						        type="date"
								size="small"
						        placeholder="选择时间"
						        :default-value="scope.row.inarrtime"
						      />
						</div>
						<p v-if="scope.row.state">出港:{{dateFormat(scope.row.oldOutarrtime)}}</p>
						<div v-else class="flex-center">
							<span style="width: 75px;">进港:</span>
						 <el-date-picker
						        v-model="scope.row.outarrtime"
						        type="date"
								size="small"
						        placeholder="选择时间"
						        :default-value="scope.row.outarrtime"
						      />
						</div>
					</template>
				</el-table-column>
				<el-table-column label="预期到货时间">
					<template #default="scope">
						<span v-if="scope.row.state">{{dateFormat(scope.row.oldArrivalTime)}}</span>
						<div  class="flex-center" v-else>
						<el-date-picker
						       v-model="scope.row.arrivalTime"
						       type="date"
								size="small"
						       placeholder="选择时间"
						       :default-value="scope.row.arrivalTime"
						     /></div>
					</template>
				</el-table-column>
				<el-table-column label="备注">
					<template #default="scope">
						<div>
							{{scope.row.remark}}
						</div>
					</template>
				</el-table-column>
				
				<el-table-column label="操作" width="65">
					<template #default="scope">
						<el-button v-if="scope.row.state" type="primary" @click="editShipHistory(scope.row)" link>修改</el-button>
						<el-button v-else @click="saveShipHistory(scope.row)"  type="primary" link>保存</el-button>
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
		shipmentApi.getShipRecord({groupid:itemrow.groupid,marketplaceid:itemrow.marketplaceid,sku:itemrow.sku}).then(res=>{
			state.tableData=res.data.records;
			state.tableData.forEach(item=>{
				item.oldArrivalTime=item.arrivalTime;
				item.oldOutarrtime=item.outarrtime;
				item.oldInarrtime=item.inarrtime;
				item.state=true;
			})
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
		 if(row.isv2=="1"){
			 	router.push({
			 		path:'/e/s/s/one',
			 		query:{
			 		  id:row.formid,
			 		  title:"发货处理_配货",
			 		  path:'/e/s/s/one',
			 		}
			 	})
		 }else if(row.isv2=="2"){
			router.push({
				 path:'/shipment_handing/shipv2step',
				query:{
				  row:JSON.stringify(row),
				  step:3,
				  title:"发货流程_"+row.shipmentid,
				  path:'/shipment_handing/shipv2step',
				}
			})
		 }else{
			 emitter.emit("removeCache", "发货流程"+row.shipmentid);
			 router.push({
			 	path:'/shipment_handing/shipstep',
			 	query:{
			 	  shipmentid:row.shipmentid,
			 	  title:"发货流程"+row.shipmentid,
			 	  path:'/shipment_handing/shipstep',
			 	}
			 })
		 }
		
	}
	function saveShipHistory(row){
		row.arrivalTime=getDateValue(row.arrivalTime);
		row.outarrtime=getDateValue(row.outarrtime);
		row.inarrtime=getDateValue(row.inarrtime);
		shipmentApi.updateTransInfo(row).then(res=>{
			    row.state=true;
				row.oldArrivalTime=row.arrivalTime;
				row.oldOutarrtime=row.outarrtime;
				row.oldInarrtime=row.inarrtime;
			    ElMessage.success("保存成功");
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