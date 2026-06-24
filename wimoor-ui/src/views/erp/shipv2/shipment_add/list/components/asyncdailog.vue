<template>
	  <el-dialog
	     v-model="dialogVisible"
	    :title="title"
		class="asyncdailog"
	     width="80%"
	  >
	  <el-row>
		   <el-col :span="24" style="padding-bottom:10px;">
			  <el-space>
				<Warehouse ref="warehouses" @changeware="getWarehouse" defaultValue="only" />
				</el-space>
			</el-col>
		</el-row>
		<el-table :data="tableData" v-loading="loading"  style="width: 100%">
		 <el-table-column prop="image" label="图片" width="60" >
			 <template #default="scope">
			 	<el-image :src="scope.row.image" width="40" height="40"></el-image>
			 </template>
		 </el-table-column>
		 <el-table-column prop="name" label="名称" show-overflow-tooltip  />
		 <el-table-column prop="sku" label="平台SKU" width="180" />
		 <el-table-column prop="msku" label="本地SKU" width="180" />
		 <el-table-column prop="confirmQuantity" label="发货数量" width="180" />
		 <el-table-column prop="invquantity" label="本地库存" width="180" />
		 <el-table-column prop="outbound" label="待出库" width="180" />
		</el-table>
		<template #footer>
		  <span class="dialog-footer">
		    <el-button  @click="closeDialog">同步货件</el-button>
		  </span>
		</template>
   </el-dialog>
   <Consumable ref="consRef"></Consumable>
</template>
<script setup>
	import shipmentplanApi from '@/api/erp/shipv2/shipmentplanApi.js';
   import {reactive,ref,watch,onMounted} from "vue";
   import Warehouse from '@/components/header/warehouse.vue';
   import {ElMessage } from 'element-plus';
   import Consumable from '@/views/erp/baseinfo/material/consumableDialog.vue';
   const emits = defineEmits(["change"]);
   let dialogVisible=ref(false);
   const consRef=ref();
   let tableData=ref([]);
   let myParams={};
   let warehouseid="";
   let title=ref("");
   let loading =ref(false);
   function closeDialog(){
	    myParams.loading=true;
		dialogVisible.value = false;
		//操作新增plan
		var data={};
		data.planid=myParams.inboundPlanId
		data.itemlist=tableData.value;
		data.groupid=myParams.groupid;
		data.marketplaceid=myParams.marketplaceid;
		data.warehouseid=warehouseid;
		shipmentplanApi.savePlanItemSync(data).then(res=>{
				ElMessage.success("同步货件成功!");
				emits("change");
		});
		
		
   }
   function showDailog(params){
	   myParams=params;
	   var data={};
	   data.groupid=params.groupid;
	   data.warehouseid=warehouseid;
	   data.marketplaceid=params.marketplaceid;
	   data.planid=params.inboundPlanId;
		loading.value=true;
		dialogVisible.value=true;   
		if(data.warehouseid){
			shipmentplanApi.syncPlanItem(data).then(res=>{
						title.value="货件信息";
						tableData.value=res.data;
						loading.value=false;
			});
		}
	   
   }
   function showConsDialog(){
	   let params=myParams;
	   var data={};
	   var skulist=[];
	   data.warehouseid=warehouseid;
	   data.number=params.shipmentid;
	   tableData.value.forEach(item=>{
		   if(item.msku){
			   var row={};
			   row.qty=item.quantityShipped;
			   row.sku=item.msku;
			   skulist.push(row);
		   }
	   });
	   data.skulist=skulist;
	   consRef.value.show(data);
   }
   function submitDialog(){
	   let params=myParams;
	   params.warehouseid=warehouseid;
	   let haserror=false;
	   tableData.value.forEach(item=>{
		   if(!item.invquantity){
			   item.invquantity=0;
		   }
		   if(item.quantityShipped> item.invquantity){
			   haserror=true;
		   }
	   });
	   if(haserror){
		   ElMessage.error('库存不够，请确认库存');
		   return ;
	   }
	   params.needsubinv="true";
	   shipmenthandlingApi.saveShipmentItemAndPlanBath(params).then(res=>{
			dialogVisible.value= false;
			ElMessage.success('同步成功！');
			emits("change");
	   })
	   
   }
   function getWarehouse(value){
	   warehouseid=value;
	   showDailog(myParams)
   }
   
   defineExpose({ showDailog})	 
 
</script>

<style>
	.asyncdailog .el-dialog__body{
		padding-top:0px;
	}
</style>