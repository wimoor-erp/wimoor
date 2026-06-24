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
		 <el-table-column prop="sellersku" label="平台SKU" width="180" />
		 <el-table-column prop="sku" label="本地SKU" width="180" />
		 <el-table-column prop="quantityShipped" label="发货数量" width="180" />
		 <el-table-column prop="invquantity" label="本地库存" width="180" />
		 <el-table-column prop="outbound" label="待出库" width="180" />
		</el-table>
		<template #footer>
		  <span class="dialog-footer">
			 <el-button type="primary"  @click="showConsDialog">辅料出库</el-button>
		    <el-button type="primary"  @click="submitDialog">同步并出库</el-button>
		    <el-button  @click="closeDialog">仅同步货件</el-button>
		  </span>
		</template>
   </el-dialog>
   <Consumable ref="consRef"></Consumable>
</template>
<script>
   import shipmenthandlingApi from '@/api/erp/ship/shipmenthandlingApi.js';
   import {reactive,ref,watch,onMounted} from "vue";
   import Warehouse from '@/components/header/warehouse.vue';
   import {ElMessage } from 'element-plus';
   import Consumable from '@/views/erp/baseinfo/material/consumableDialog.vue';
	export default{
		name:'shipAsyncDailog',
		components:{Warehouse,Consumable},
		emits:["change"],
		setup(props,context){
		   let dialogVisible=ref(false);
		   let consRef=ref();
		   let tableData=ref([]);
		   let myParams={};
		   let warehouseid="";
		   let title=ref("");
		   let loading =ref(false);
		   function closeDialog(){
			    dialogVisible.value = false;
				context.emit("change");
		   }
		   function showDailog(params){
			   myParams=params;
			   params.warehouseid=warehouseid;
			    loading.value=true;
				dialogVisible.value=true;   
			   shipmenthandlingApi.getUnSyncShipmentDetial(params).then(res=>{
					title.value="货件信息:"+params.shipmentid;
					tableData.value=res.data.itemList;
					loading.value=false;
			   });
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
					context.emit("change");
			   })
			   
		   }
		   function getWarehouse(value){
			   warehouseid=value;
			   showDailog(myParams)
		   }
		   return {
			   dialogVisible,consRef, //ref
			   showDailog,closeDialog, submitDialog, getWarehouse,showConsDialog,  //function
			   title,tableData,loading,//value
		   }
		  }
		}
</script>

<style>
	.asyncdailog .el-dialog__body{
		padding-top:0px;
	}
</style>