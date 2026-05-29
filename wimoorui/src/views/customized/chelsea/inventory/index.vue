<template>
	<div class="main-sty">
	     <div class="con-header">
			 <div class="flex-center-between">
			 <el-space>
				 <Group  @change="groupChange" defaultValue="onlyEU" />
				 <el-input  v-model="queryParams.search" placeholder="请输入SKU"
				  clearable @input="handleQuery"  style="min-width:200px;" >
				    <template #append>
				      <el-button @click="handleQuery">
				          <el-icon style="font-size: 16px;align-itmes:center">
				          <search />
				       </el-icon>
				      </el-button>
				    </template>
				  </el-input>
			 </el-space>
			 <el-space>
				 <el-button @click="gotoSalePre" > 销售计划</el-button>
				  <el-button @click="showInvDialog">设置</el-button>
				<el-badge :value="plancount" :hidden="plancount==0">
						 <el-checkbox v-model="queryParams.isSelected" @change="handleQuery" label="已选计划"  border />
				</el-badge>
				<el-button @click="handleClearPlan">清除计划</el-button>
				<el-button @click="handleShowPlan">提交计划</el-button> 
			 </el-space>
			 
			 </div>
		 </div>
		 <div class="con-body">
			 <GlobalTable  ref="globalTable" 
			  :tableData="tableData" 
			  @loadTable="loadTableData"
			  height="calc(100vh - 198px)"
			 >
					 <template #field>
						 <el-table-column label="图片" width="65">
							 <template #default="scope">
								 <el-image
								  class="product-img"
								  v-if="scope.row.image"
								  :src="scope.row.image"></el-image>
								  <el-image v-else 
								  :src="$require('empty/noimage40.png')"
								  class="product-img"
								  ></el-image>
							 </template>
						 </el-table-column>
						 <el-table-column label="名称/SKU" min-width="240px" show-overflow-tooltip>
							 <template #default="scope">
								 <div >{{scope.row.name}}</div>
								 <span class="font-extraSmall">{{scope.row.sku}}</span>
							 </template>
						 </el-table-column>
						 <el-table-column label="店铺/国家" width="120">
							 <template #default="scope">
								 <div >{{scope.row.groupname}}</div>
								 <span class="font-extraSmall">{{scope.row.marketname}}</span>
							 </template>
						 </el-table-column>
						 <el-table-column label="FBA库存" width="300">
							 <template #default="scope">
                                 <el-space alignment="stretch" :size="24">
									 <el-space direction="vertical" alignment="normal" :size="0">
										 <el-space 
										 @click="handleShowFbaHistory(scope.row)"
										 :size="4" class="pointer">
										 <span><span class="light-font">可售库存：</span>{{scope.row.afn_fulfillable_quantity}}</span>
										  <el-icon><Tickets  /></el-icon>
										 </el-space>
										 <span><span class="light-font">预留：</span>{{scope.row.afn_reserved_quantity}}</span>
										 <div class="table-edit-flex">
										    <span><span class="light-font">安全库存上限：</span>
											<span v-if="scope.row.mysalesweek"> {{getValue(scope.row.high_qty*scope.row.mysalesweek)}}</span>
											<span v-else>  
												{{getValue(scope.row.high_qty*scope.row.sales_week)}}
											</span>
											  <el-icon
											  @click="setSafeInv(scope.row)"
											  ><edit></edit></el-icon>
										 </span>
										 </div>
										 <div class="table-edit-flex">
										 <span><span class="light-font">安全库存下限：</span>
										 <span v-if="scope.row.mysalesweek"> {{getValue(scope.row.low_qty*scope.row.mysalesweek)}}</span>
										 <span v-else>  {{getValue(scope.row.low_qty*scope.row.sales_week)}}</span>
										 <el-icon
										 @click="setSafeInv(scope.row)"
										 ><edit></edit></el-icon>
										 </span>
										 </div>
									 </el-space>
									 <el-space direction="vertical" alignment="normal" :size="0">
										 <span><span class="light-font">待接收：</span>
										 <span >
											  {{getValue(scope.row.afn_inbound_shipped_quantity+scope.row.afn_inbound_receiving_quantity)}}
										 </span>
										 
										 </span>
										 <span><span class="light-font">发货中：</span>
										 <span   > {{scope.row.afn_inbound_working_quantity}}</span>
										 
										 </span>
									 </el-space>
								 </el-space>
							 </template>
						 </el-table-column>
						 <el-table-column label="海外仓库存" width="140">
							 <template #default="scope">
									 <el-space direction="vertical" alignment="normal" :size="0">
										<!-- <span><span class="light-font">待发货：</span>{{}}</span> -->
										 <span><span class="light-font">待入库：</span>{{scope.row.inboundqty}}</span>
										 <el-space> <span ><span class="light-font">英&nbsp;&nbsp;&nbsp;国：</span>{{scope.row.ukqtyquantity}}</span>
										 <el-tooltip 
										 v-if="calcInvWarning(scope.row,'uk')"
										 placement="top"
										 content="警告: 海外仓库存+待入库库存不在安全范围">
											 <el-icon
											 :size="20"
											 class="text-red"><WarnTriangleFilled /></el-icon>
										 </el-tooltip>
										 </el-space>
										 <el-space>
											 <span><span class="light-font">欧&nbsp;&nbsp;&nbsp;洲：</span>{{scope.row.euqty}}</span>
											 <el-tooltip
											 v-if="calcInvWarning(scope.row,'eu')"
											 placement="top"
											 content="警告: 海外仓库存+待入库库存不在安全范围">
														 <el-icon
														 :size="20"
														 class="text-red"><WarnTriangleFilled /></el-icon>
											 </el-tooltip>
										  </el-space>
									 </el-space>
								 
							 </template>
						 </el-table-column>
						 <el-table-column label="本地库存" width="120">
							 <template #default="scope">
									 <el-space direction="vertical" alignment="normal" :size="0">
										 <span><span class="light-font">待提交：</span>{{scope.row.needsubmitQty}}</span>
										 <span><span class="light-font">待生产：</span>{{scope.row.needProductQty}}</span>
										 <span><span class="light-font">待接收：</span>{{scope.row.needReceiveQty}}</span>
									 </el-space>
							 </template>
						 </el-table-column>
						 <el-table-column label="七日销量" width="120" sortable="custom">
							 <template #default="scope">
									 <el-space direction="vertical" alignment="normal" :size="0">
										 <span>{{scope.row.sales_week}}</span>
										 <span class="light-font">订单量：{{scope.row.order_week}}</span>
									 </el-space>
							 </template>
						 </el-table-column>
						 <el-table-column label="计划生产" width="350">
							 <template #header>
								 <span>计划生产</span>&nbsp;
								 <el-date-picker
								 type="month"
								 size="small"
								 style="width:100px"
								 :clearable="false"
								 v-model="queryParams.planmonth"
								 format="YYYY-MM"
								 value-format="YYYY-MM"
								 @change="handleQuery"
								 ></el-date-picker>
							 </template>
							 <template #default="scope">
								 	<span class="table-edit-flex" >
										  <el-space style="width: 100%;"  wrap :size="12">
											  <div v-for="item in scope.row.sales" :style="scope.row.sales&&scope.row.sales.length>4?'min-width:45px':'min-width:60px'">
											     <div class="light-font" style="padding-left:10px">{{item.week}}周</div>
											      <div v-if="item.quantity"><span class="light-font">发货:</span><span  @click="scope.row.amount=item.quantity">{{item.quantity}}</span></div>
												  <div v-else><span class="light-font">发货:</span><span  >0</span></div> 
											  </div>
										  </el-space>
										  </span>
										  
							 </template>
						 </el-table-column>
						 <el-table-column label="下单量" width="120" sortable="custom">
						 							 <template #default="scope">
						 									 <el-space  alignment="center" :size="8">
						 										 <el-input style="width:100px;" v-model="scope.row.amount" clearable></el-input>
						 									 </el-space>
						 							 </template>
						 </el-table-column>
						 <el-table-column label="操作" width="140" sortable="custom" fixed="right">
							 <template #default="scope">
								 <el-tooltip :show-after="200" content="加入生产计划" placement="top">
										 <el-button @click="addPlan(scope.row)" type="primary" 
										  :disabled="scope.row.isadd"
										  text bg>
										 		<el-icon :size="16"><Plus /></el-icon>
										 </el-button>
								 </el-tooltip>
								 <el-tooltip :show-after="200" content="移出生产计划" placement="top">
								 <el-button @click="removePlan(scope.row)" :disabled="!scope.row.isadd"  type="primary"  text bg> 
									 <el-icon :size="16"><Minus /></el-icon>
								 </el-button>
								 </el-tooltip>
							 </template>
						 </el-table-column>
					 </template>	 
			 </GlobalTable>
		 </div>
   </div>
   <el-dialog 
   v-model="invVisible"
   :title="safeinvData?.sku" width="460px">
	     <el-tabs v-model="activeName"  @tab-click="handleClick">
	       <el-tab-pane label="英国" :name="0">
			   <el-form label-position="top" :model="invForm">
			   			 <el-form-item label="英国安全库存上限">
			   				 <el-space>
			   					 <span class="font-extraSmall">近 7 日销量 x</span>
			   					 <el-input v-model="invForm.highQty"></el-input>
			   				 </el-space>
			   			 </el-form-item>
			   			 <el-form-item label="英国安全库存下限">
			   				 <el-space>
			   					 <span class="font-extraSmall">近 7 日销量 x</span>
			   					 <el-input v-model="invForm.lowQty"></el-input>
			   				 </el-space>
			   			 </el-form-item>
			   			 <el-form-item label="预估近 7 日销量">
			   				 <div>
			   				 <el-space :size="24">
			   					 <el-input v-model="invForm.salesWeek" placeholder="设置后替代近 7 日销量"></el-input>
			   					 <p>近 7 日销量：<span class="font-bold">{{safeinvData.sales_week}}</span></p>
			   					</el-space>
			   					<div class="m-t-8 font-extraSmall">
			   					<el-space>
			   						<el-icon><InfoFilled /></el-icon>
			   						<span >预估近7日销量设置后将替代近7日销量进行计算</span>
			   					</el-space></div>
			   				 </div>
			   			 </el-form-item>
			   </el-form>
		   </el-tab-pane>
	       <el-tab-pane label="欧洲" :name="1">
			   <el-form label-position="top" :model="euForm">
			   			 <el-form-item label="欧洲安全库存上限">
			   				 <el-space>
			   					 <span class="font-extraSmall">近 7 日销量 x</span>
			   					 <el-input v-model="euForm.highQty"></el-input>
			   				 </el-space>
			   			 </el-form-item>
			   			 <el-form-item label="欧洲安全库存下限">
			   				 <el-space>
			   					 <span class="font-extraSmall">近 7 日销量 x</span>
			   					 <el-input v-model="euForm.lowQty"></el-input>
			   				 </el-space>
			   			 </el-form-item>
			   			 <el-form-item label="预估近 7 日销量">
			   				 <div>
			   				 <el-space :size="24">
			   					 <el-input v-model="euForm.salesWeek" placeholder="设置后替代近 7 日销量"></el-input>
			   					 <p>近 7 日销量：<span class="font-bold">{{safeinvData.sales_week}}</span></p>
			   					</el-space>
			   					<div class="m-t-8 font-extraSmall">
			   					<el-space>
			   						<el-icon><InfoFilled /></el-icon>
			   						<span >预估近7日销量设置后将替代近7日销量进行计算</span>
			   					</el-space></div>
			   				 </div>
			   			 </el-form-item>
			   </el-form>
		   </el-tab-pane>
	     </el-tabs>
		 <template #footer>
			 <el-button @click="invVisible=false">取消</el-button>
			 <el-button type="primary" @click="saveSetting">保存</el-button>
		 </template>
   </el-dialog >
   <el-dialog v-model="setVisible" title="全局设置" width="460px" >
	   <el-form label-position="top" :model="globalForm">
			 <el-form-item label="英国安全库存上限">
				 <el-space>
					 <el-input v-model="globalForm.ukHighQty"></el-input>
					  <span class="font-extraSmall">x  近7日销量</span>
				 </el-space>
			 </el-form-item>
			 <el-form-item label="英国安全库存下限">
			 				 <el-space>
			 					 <el-input v-model="globalForm.ukLowQty"></el-input>
			 					  <span class="font-extraSmall">x  近7日销量</span>
			 				 </el-space>
			 </el-form-item>
			 <el-form-item label="欧洲安全库存上限">
				 <el-space>
					 <el-input v-model="globalForm.euHighQty"></el-input>
					  <span class="font-extraSmall">x  近7日销量</span>
				 </el-space>
			 </el-form-item>
			
			 <el-form-item label="欧洲安全库存下限">
				 <el-space>
					 <el-input v-model="globalForm.euLowQty"></el-input>
					  <span class="font-extraSmall">x  近7日销量</span>
				 </el-space>
			 </el-form-item>
			 <el-form-item label="生产周期(天)">
				 <el-space>
					 <el-input v-model="globalForm.pday"></el-input>
				 </el-space>
			 </el-form-item>
			 <el-form-item label="海运周期(天)">
					 <el-space>
						 <el-input v-model="globalForm.shipday"></el-input>
					 </el-space>
			 </el-form-item>
			 <el-form-item label="上架周期(天)">
					 <el-space>
						 <el-input v-model="globalForm.upday"></el-input>
					 </el-space>
			 </el-form-item>
	   </el-form>
	   <template #footer>
	   			 <el-button @click="setVisible=false">取消</el-button>
	   			 <el-button type="primary" @click="saveGlobalSetting">保存</el-button>
	   </template>
   </el-dialog>
 <DetailsDiglog @change="changeOrder" ref="orderDetailsRef" />
  <FbaInvHistoryDialog ref="fbaInvHistoryDialogRef"></FbaInvHistoryDialog>
  <SalesDialog ref="salesRef" ></SalesDialog>
 </template>

<script setup>
	import { ref,reactive,onMounted,toRefs, computed,} from 'vue'
	import {Search,Edit,Plus,Minus,InfoFilled,WarnTriangleFilled,Tickets} from '@element-plus/icons-vue'
	import { ElMessage, ElMessageBox } from 'element-plus';
	import Group from '@/components/header/group.vue';
	import FbaInvHistoryDialog from "@/views/amazon/listing/products/components/fbainvhistory_dialog.vue"
	import {CheckInputFloat,CheckInputInt,getValue,formatFloat} from '@/utils/index.js';
	import DetailsDiglog from './components/plan_dialog.vue';
	import SalesDialog from './components/sales_dialog.vue';
	import inventoryApi from "@/views/customized/chelsea/api/inventory/inventoryApi.js";
	import { useRouter } from 'vue-router'
	const router = useRouter()
	const hisRef = ref();
	const orderDetailsRef = ref();
	const globalTable =ref();
	const fbaInvHistoryDialogRef=ref();
	const salesRef=ref();
	const state = reactive({
       tableData:{records:[],total:1,},
	   invVisible:false,
	   activeName:0,
	   safeinvData:{},
	   invForm:{},
	   euForm:{},
	   globalForm:{},
	   queryParams:{search:"",planmonth:null},
	   plancount:0,
	   isload:false,
	   setVisible:false,
	})
	const {
      tableData,
	  invVisible,
	  setVisible,
	  activeName,
	  safeinvData,
	  invForm,
	  euForm,
	  queryParams,
	  isload,
	  plancount,
	  globalForm,
	} = toRefs(state)
	
	function changeOrder(){
		handleQuery();
		loadCount();
	}
	function gotoSalePre(){
		router.push({
			path:"/amazon/sales/forecast",
			query:{
				title:'销售计划',
				path:"/amazon/sales/forecast",
			}
		})
	}
   function showInvDialog(){
	   state.setVisible=true;
   }
   
   function calcInvWarning(row,type){
	   var lowqty=0;
	   if(row.mysalesweek){
		   lowqty=row.low_qty*row.mysalesweek;
	   }else{
		   lowqty=row.low_qty*row.sales_week;
	   }
	   if(type=='uk'){
		   if(row.ukqtyquantity+row.inboundqty<lowqty && lowqty>0){
		   		   return true;
		   }else{
		   		   return false;
		   }
	   }else{
		   if(row.euqtyquantity+row.inboundqty<lowqty && lowqty>0){
		   		   return true;
		   }else{
		   		   return false;
		   }
	   }
	
   }
   
   function handleShowFbaHistory(row){
   		fbaInvHistoryDialogRef.value.show(row.groupid,row.marketplaceid,row.sku);
   }
   
   function setSafeInv(row){
	   state.invVisible = true;
	   state.safeinvData =row;
	   inventoryApi.getInventorySetting({"amazonAuthid":row.amazonAuthId,"sku":row.sku}).then(res=>{
		   if(res.data && res.data.length>0){
			   var datas=res.data;
			   datas.forEach(item=>{
				   if(item.marketplaceid=='EU'){
					   state.euForm.lowQty=item.lowQty;
					   state.euForm.highQty=item.highQty;
					   state.euForm.salesWeek=item.salesWeek;
				   }else{
					   state.invForm.lowQty=item.lowQty;
					   state.invForm.highQty=item.highQty;
					   state.invForm.salesWeek=item.salesWeek;
				   }
			   });
		   }
	   });
   }
 function historyInv(row){
	 hisRef.value.show(row.id);
 }
 
 function addPlan(row){
	 var data={};
	 data.amazonAuthid=row.amazonAuthId;
	 data.marketplaceid=row.marketplaceid;
	 data.sku=row.sku;
	 data.quantity=row.amount;
	 if(data.quantity>0){
		 inventoryApi.savePlan(data).then(res=>{
			 ElMessage.success("加入计划成功!");
			 row.isadd = true;
			  handleQuery();
			 loadCount();
		 });
	 }else{
		 ElMessage.error("请正确输入数量！");
	 }
	
 }
 
 function removePlan(row){
	 var data={};
	 data.amazonAuthid=row.amazonAuthId;
	 data.marketplaceid=row.marketplaceid;
	 data.sku=row.sku;
	 inventoryApi.removePlan(data).then(res=>{
		 ElMessage.success("移除计划成功!");
		 row.isadd = false;
		  handleQuery();
		 loadCount();
	 });
 }
 
 
 function handleShowPlan(){
	  var data={};
	  data.groupid=state.queryParams.groupid;
	  data.marketplaceid=state.queryParams.marketplaceid;
	  inventoryApi.getPlan(data).then(res=>{
		  orderDetailsRef.value.show(res.data,state.tableData,state.globalForm);
	  });
 }
 
 function loadCount(){
	 var data={};
	  data.groupid=state.queryParams.groupid;
	  data.marketplaceid=state.queryParams.marketplaceid;
	  inventoryApi.countPlan(data).then(res=>{
		state.plancount=res.data;
	  });
	  
 }
 
 function handleClearPlan(){
	  var data={};
	   data.groupid=state.queryParams.groupid;
	   data.marketplaceid=state.queryParams.marketplaceid;
	  inventoryApi.clearPlan(data).then(res=>{
			 ElMessage.success("清除计划成功!");
			 handleQuery();
			 loadCount();
	  });
	    
	
 }
 function groupChange(obj){
		state.queryParams.groupid=obj.groupid;
		state.queryParams.marketplaceid=obj.marketplaceid;
		 loadCount();
		handleQuery();
 }
 
 
	function handleQuery(){
		   globalTable.value.loadTable(state.queryParams);
	  }
	  function loadTableData(params){
		  inventoryApi.getList(params).then(res=>{
				 res.data.records.forEach(item=>{
					 item.isadd=false;
					 if(item.sales_week){
						 item.amount=item.sales_week;
					 }
					 if(item.myquantity){
						item.amount=item.myquantity;
						item.isadd=true;
					 }
				 });
				 state.tableData.records=res.data.records;
				 state.tableData.total=res.data.total;
				 if(state.isload==false){
					state.isload=true;
				 }
		  })
	  }
	  function saveGlobalSetting(){
		  inventoryApi.saveGlobalSetting(state.globalForm).then(res=>{
		  	 ElMessage.success("操作成功!");
		  	 state.setVisible=false;
		  });
	  }
	  
	function saveSetting(){
		var data={};
		data.amazonAuthid=state.safeinvData.amazonAuthId;
		data.marketplaceid='A1F83G8C2ARO7P';
		data.sku=state.safeinvData.sku;
		data.highQty=state.invForm.highQty;
		data.lowQty=state.invForm.lowQty;
		data.salesWeek=state.invForm.salesWeek;
		inventoryApi.saveSetting(data).then(res=>{
			saveEUSetting();
		});
	}
	
	function saveEUSetting(){
		var data={};
		data.amazonAuthid=state.safeinvData.amazonAuthId;
		data.marketplaceid='EU';
		data.sku=state.safeinvData.sku;
		data.highQty=state.euForm.highQty;
		data.lowQty=state.euForm.lowQty;
		data.salesWeek=state.euForm.salesWeek;
		inventoryApi.saveSetting(data).then(res=>{
			 ElMessage.success("操作成功!");
			 state.invVisible=false;
			 handleQuery();
		});
	}
	function loadSetting(){
		inventoryApi.getGlobalSetting().then((res)=>{
			   if(res.data){
				   state.globalForm=res.data;
			   }else{
				   state.globalForm={};
			   }
		});
	}
	function showSalesDialog(row,item){
		item.groupid=row.groupid;
		salesRef.value.show(item);
	}

 onMounted(()=>{
	 const date = new Date();
	 const year = date.getFullYear();
	 const month = String(date.getMonth() + 1).padStart(2, '0');
	 state.queryParams.planmonth=`${year}-${month}`;
	 loadSetting();
 })
</script>

<style scoped>
  .con-body{
	  margin-top: 16px;
  }
  .flex-top-between{
	  display: flex;
	  align-items: flex-start;
  }
  .flex-top-between>div{
	  flex: 1;
  }
</style>