<template >
	<GlobalTable
	 @selectionChange='selectChange'
	 :tableData="tableData"
	 height="calc(100vh - 248px)"  @loadTable="loadTableData" 
	 ref="globalTable"
	 >
	 <template #field>
		 <el-table-column type="selection" />
		<el-table-column label="图片"  width="65" prop="image" >
			<template #default="scope">
				<el-image v-if="scope.row.image" :src="scope.row.image" class="product-image"   ></el-image>
				<el-image v-else :src="$require('empty/noimage40.png')"  class="product-image" ></el-image>
			</template>
		</el-table-column>
		<el-table-column label="平台SKU"  min-width="200px"  show-overflow-tooltip  prop="sku" >
			<template #default="scope">
				 <div>{{scope.row.sku}} <span class="font-extraSmall"> ({{scope.row.groupname}} - {{scope.row.marketname}})</span></div>
				 <div class="font-extraSmall">{{scope.row.name}}</div>
			</template>
		</el-table-column>
		<el-table-column label="发货日期"  width="110" prop="shipdate" sortable="custom">
			<template #default="scope">
			 {{dateFormat(scope.row.shipdate)}}
			</template>
		</el-table-column>
		<el-table-column label="接收日期" width="110"  prop="ReceivedDate"  sortable="custom">
			<template #default="scope">
			 {{dateFormat(scope.row.ReceivedDate)}}
			</template>
			</el-table-column>
		<el-table-column label="状态"  width="120" prop="status" >
			<template #default="scope">
				<el-tag v-if="scope.row.status==1"   >未开始</el-tag>
				<el-tag v-else-if="scope.row.status==2" type="warning" >扣减中</el-tag>
				<el-tag v-else-if="scope.row.status==3" type="success" >已扣完</el-tag>
				<el-tag v-else type="info" >未处理</el-tag>
				 <div  class="font-extraSmall">货件状态：{{scope.row.shipmentstatus}}</div>
			</template>
		</el-table-column>
		<el-table-column label="单据号" width="200" prop="number">
			<template #default="scope">
				<div>{{scope.row.number}}</div>
				<div  class="font-extraSmall">操作时间：{{dateTimesFormat(scope.row.opttime)}}</div>
			</template>
		</el-table-column>
		<el-table-column label="货件号" width="140" prop="shipmentid" >	
		<template #default="scope">
		 <div>{{scope.row.shipmentid}}</div>
		 <div><el-button size="small"  @click.stop="handleResetFee(scope.row)" title="重新由系统分摊该货件的运费">重算运费</el-button></div>
		</template>
		</el-table-column>
		<el-table-column label="发出数量" width="100" prop="QuantityShipped"  sortable="custom">	</el-table-column>
		<el-table-column label="接收数量"  width="100" prop="QuantityReceived"  sortable="custom">	</el-table-column>
		<el-table-column label="扣除数量"  width="120" prop="QuantityReceivedSub"  sortable="custom">
			<template #default="scope">
			 {{scope.row.QuantityReceivedSub}} 
			 <div >
				 <span class="font-extraSmall">剩余量:</span>
				 <span  > {{scope.row.QuantityReceivedBalance}} </span>
			 </div>
			</template>
		</el-table-column>
		<el-table-column label="采购成本"  width="120" prop="totalcost" sortable="custom" >	
				<template #default="scope">
				 {{formatFloat(scope.row.totalcost)}} 
				 <div >
					 <span class="font-extraSmall">单件成本:</span>
					 <span  > {{formatFloat(scope.row.unitcost)}} </span>
				 </div>
				</template>
		</el-table-column>
		<el-table-column label="头程成本"  width="120" prop="totaltransfee" sortable="custom">	
			<template #default="scope">
			 {{formatFloat(scope.row.totaltransfee)}} 
			 <div >
				 <span class="font-extraSmall">单件成本:</span>
				 <span  > {{formatFloat(scope.row.unittransfee)}} </span>
			 </div>
			</template>
		</el-table-column>
		<el-table-column label="操作"  width="140"  fixed="right" >
			<template #default="scope">
				<div><el-button link type="primary" @click.stop="showDetailDialog(scope.row)">扣减明细</el-button></div>
			</template>
		</el-table-column>
		</template>
	</GlobalTable>
	<el-dialog v-model="dialogVisable" title="查看详情" width="800px" top="1vh">
		 <table style="margin-top: -20px;">
			 <tr><td>
			 <el-image v-if="titlerow.image" :src="titlerow.image" class="image-100"   ></el-image>
			 <el-image v-else :src="$require('empty/noimage40.png')"  class="image-100"  ></el-image>
			 </td>
			 <td>
				 <div><span class="font-extraSmall">SKU:</span>{{titlerow.sku}}</div>
				 <div><span class="font-extraSmall">货件号:</span>{{titlerow.shipmentid}}</div>
				  <div><span class="font-extraSmall">名称:{{titlerow.name}}</span></div>
			 </td>
			 </tr>
		 </table>
		 <el-table :data="infotableData" height="calc(100vh - 320px)"  >
			 <el-table-column label="订单号"  prop="amazon_order_id" >
			 				  <template #default="scope">
			 				       <div>{{scope.row.amazon_order_id}}</div>
			 					   <div><span class="font-extraSmall">发货数量：</span>{{scope.row.quantity}}</div> 
			 					   <div><span class="font-extraSmall">售价：</span>{{scope.row.item_price}}</div> 
			 				  </template>
			 </el-table-column>
			  <el-table-column label="日期"  prop="purchase_date" >
				  <template #default="scope">
                     <table>
				       <tr  style="background: none;"><td><span class="font-extraSmall">购买日期</span></td><td>{{dateTimesFormat(scope.row.purchase_date)}}</td></tr>
					   <tr  style="background: none;"><td><span class="font-extraSmall">付款日期</span></td><td>{{dateTimesFormat(scope.row.payments_date)}}</td></tr>
					   <tr  style="background: none;"><td><span class="font-extraSmall">发货日期</span></td><td>{{dateTimesFormat(scope.row.shipment_date)}}</td></tr>
					   <tr  style="background: none;"><td><span class="font-extraSmall">预计到货日期</span></td><td>{{dateTimesFormat(scope.row.estimated_arrival_date)}}</td></tr>
				</table>
				  </template>
			 </el-table-column>
			 <el-table-column label="收货信息"   prop="purchase_date" >
				  <template #default="scope">
					<table>
						<tr  style="background: none;"><td><span class="font-extraSmall">城市</span></td><td>{{scope.row.ship_city}}</td></tr>
					    <tr  style="background: none;"><td><span class="font-extraSmall">国家</span></td><td>{{scope.row.ship_country}}</td></tr>
					    <tr  style="background: none;"><td><span class="font-extraSmall">物流</span></td><td>{{scope.row.carrier}}</td></tr>
					    <tr  style="background: none;"><td><span class="font-extraSmall">物流编码</span></td><td>{{scope.row.tracking_number}}</td></tr>
					   </table>
				  </template>
			 </el-table-column>
		 </el-table>
		  <template #footer>
			   <span v-if="infotableData" class="font-extraSmall" style="float:left">{{infotableData.length}}笔记录 , 总发货量：{{infotableShipTotal}} 个</span>
		  	<span class="dialog-footer">
		  		<el-button @click="dialogVisable = false">关闭</el-button>
		  	</span>
		  </template>
	</el-dialog>
</template>

<script setup>
	import { ref,reactive,onMounted,toRefs} from 'vue'
	import {Search,ArrowDown,Histogram,Download,Upload} from '@element-plus/icons-vue';
	import inboundItemApi from '@/api/amazon/inbound/inboundItemApi.js';
	import {dateFormat,dateTimesFormat,formatFloat} from '@/utils/index.js';
	const globalTable=ref();
	const state = reactive({
		tableData: {records:[],total:0}  ,
		selectRow:[],
		queryParams:{},
		dialogVisable:false,
		infotableData:[],
		titlerow:{},
		infotableShipTotal:0,
	})
	const{
		tableData,
		selectRow,
		queryParams,
		infotableData,
		dialogVisable,
		titlerow,
		infotableShipTotal,
	}=toRefs(state)
	
	function handleResetFee(row){
		 inboundItemApi.handleResetFee({"shipmentid":row.shipmentid}).then((res)=>{
			 handleQuery();
		 });
	}
	
	function selectChange(selection){
		state.selectRow = selection
	}
	function show(params){
		state.queryParams=params;
		handleQuery();
	}
	function handleQuery(){
		globalTable.value.loadTable(state.queryParams);
	}
	function loadTableData(params){
		 inboundItemApi.getInboundItemBatchList(params).then(res=>{
			 state.tableData.records=res.data.records;
			 state.tableData.total=res.data.total;
		 })
	}
	function showDetailDialog(row){
		state.dialogVisable=true;
		state.titlerow=row;
		inboundItemApi.findDetailByShipment({"shipmentid":row.shipmentid,"sku":row.sku}).then(res=>{
			  state.infotableData=res.data;
			   if(res.data){
					  var infotableShipTotal=0;
					  res.data.forEach(item=>{
						  infotableShipTotal=infotableShipTotal+parseInt(item.quantity);
					  })
					  state.infotableShipTotal=infotableShipTotal;
			  	}
		})
		
	}
	defineExpose({
		state,show
	})
</script>

<style >
	.m-l-8{
		margin-left: 8px;
	}
	.image-100{
		height:100px;
		width:100px;
	}
</style>
 