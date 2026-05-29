<template>
	<el-dialog class="mypaymentdailog" top="3vh"
	 :title="operateType.dialogTitle"
	 lock-scroll
	  v-model="dialogVisable" width="1300px">
	  	<el-scrollbar height="calc(100vh - 180px)" :always="true" :native="true">
				<el-row class="bg-full">
					<el-col :span="layout.left">
						<el-card shadow="never" class="bg-full">
						<div  class="product-box">
							<el-image v-if="queryParams.entry.image" :src="queryParams.entry.image" class="img-60"  width="60" height="40"  ></el-image>
							<el-image v-else :src="$require('empty/noimage40.png')"  class="img-60"  width="60" height="40"  ></el-image>
							<div>
								<p class="name">{{queryParams.entry.mname}}</p>
								<p class="sku">{{queryParams.entry.sku}}</p>
							</div>
						  </div>
						  <div class="flex-center-between">
						  			 <el-space  :spacer="spacer">
						  				 <span v-if="recMap.entry.amount>=recMap.entry.totalin"><span class="font-bold" >{{recMap.entry.amount-recMap.entry.totalin}}</span><p class="font-extraSmall">待收货</p></span>
						  				  <span v-else><span  style="color: #ff0f47;">已超收{{recMap.entry.totalin-recMap.entry.amount}}</span><p class="font-extraSmall">待收货</p></span>
						  				 <span><span  class="font-bold">{{recMap.entry.totalin}}</span><p class="font-extraSmall">已收货</p></span>
										  <span><span  class="font-bold">{{recMap.weight}}kg</span><p class="font-extraSmall">预估重</p></span>
						  			 </el-space>
									 <el-progress type="dashboard" :width="120" :percentage="myformatFloat(recMap.entry.totalin/recMap.entry.amount*100)">
									       <template #default="{ percentage }">
									         <span class="font-bold">{{ percentage }}%</span>
									         <p class="font-extraSmall m-t-8">收货进度</p>
									       </template>
									     </el-progress>
						  </div>
					<el-form label-width="100px">
						<el-form-item :label="operateType.warehouseType">
							 <Warehouse ref="warehouseRef" @changeware="getWarehouse" :defaultid="warehouseid"
							   warehouseType="self_usable" defaultValue="only" />
						</el-form-item>
						<el-form-item :label="operateType.tabsType+'数量'">
							<el-input
							v-model="formData.amount" @input="formData.amount=CheckInputInt(formData.amount)">
							</el-input>
						</el-form-item>
						<el-form-item label="备注">
							<el-input v-model="formData.remark"  :rows="2"
								type="textarea"></el-input>
						</el-form-item>
						<el-form-item >
						<el-button type="primary" v-if="queryParams.entry.inwhstatus==0" @click.stop="receive" >提交{{operateType.tabsType}}</el-button>
						<el-button type="primary" v-if="queryParams.entry.inwhstatus==1" disabled >提交{{operateType.tabsType}}</el-button>
						</el-form-item>
					</el-form>
					</el-card>	
					</el-col>
					<!-- v-if="operateType.tabsType=='收货'" -->
				<!-- 到货记录 -->
			
		        
				<el-col :span="layout.mid"> 
					<el-card shadow="never" class="bg-full">	
				 <LogisticsRecord ref="logisticsRef" @change="handleHasLogisticsRecord" />
				 </el-card>
				</el-col>
				<el-col :span="layout.right"  class="record-box">
				<!-- 物流信息 -->
					<ArrivalRecord @change="changeDelete" ref="arrRecordRef" />
				</el-col>
				</el-row>
					 </el-scrollbar>
		  <template #footer>
			  <el-button @click="dialogVisable=false">关闭</el-button>
			  <el-button type="primary" v-if="queryParams.entry.inwhstatus==0" @click="stopReceive" plain>结束收货</el-button>
			  <el-button type="primary" v-if="queryParams.entry.inwhstatus==1" @click="startReceive" plain>继续收货</el-button>
		  </template>
	</el-dialog>
</template>

<script setup>
	import {h, ref,reactive,onMounted,watch,inject,toRefs,nextTick} from 'vue'
	import { ElDivider,ElMessage, ElMessageBox } from 'element-plus';
	import NullTransform from"@/utils/null-transform";
	import ArrivalRecord from "./arrival_record.vue";
	import LogisticsRecord from "./logistics_record.vue";
	import purchaselistApi from '@/api/erp/purchase/form/listApi.js';
	import Warehouse from '@/components/header/warehouse.vue';
	import {CheckInputFloat,CheckInputInt,dateFormat,dateTimesFormat,formatFloat} from '@/utils/index.js';
	const emit = defineEmits(['change']);
	const spacer = h(ElDivider, { direction: 'vertical'});
	const warehouseRef=ref();
	const arrRecordRef=ref();
	const logisticsRef=ref();
	const state = reactive({
		operateType:{
			dialogTitle:'',
			warehouseType:'',
			tabsType:''
		},
		dialogVisable:false,
		activeName:'1',
		layout:{left:7,mid:12,right:5},
		// 查询参数
		queryParams: {
			entry:{totalin:0},
		} ,
		formData:{
			amount:"",
			remark:"",
		},
		warehouseid:null,
		recMap:{
			entry:{},
		},
		
	})
	const {
		activeName,
		dialogVisable,
		queryParams,
		operateType,
		layout,
		formData,
		logisticsData,
		recMap,
		warehouseid,
	}=toRefs(state)
    function myformatFloat(value){
		if(value){
			return formatFloat(value);
		}else{
			return 0;
		}
	}
	function handleHasLogisticsRecord(hasdata){
		if(hasdata){
			state.layout={left:7,mid:12,right:5};
		}else{
			state.layout={left:10,mid:7,right:7};
		}
	}
	function loadDetail(){
		purchaselistApi.getRecdetail({"id":state.queryParams.entry.id,"ftype":"rec","actiontype":"all"}).then((res)=>{
			if(res.data){
				state.recMap=res.data;
				state.queryParams.entry.totalin=res.data.entry.totalin;
				state.queryParams.entry.inwhstatus=res.data.entry.inwhstatus;
				state.queryParams.entry.auditstatus=res.data.entry.auditstatus;
			 
				nextTick(()=>{
				   arrRecordRef.value.show(state.queryParams.entry,res.data.receivelist);
				})
			}
		});
		nextTick(()=>{
		  logisticsRef.value.show(state.queryParams.entry.id);
		})
		
	}
	function receive(){
		if(state.queryParams.entry.inwhstatus==1){
			ElMessage.error( '当前收货状态已完结！');
			return;
		}
		var data={};
		data.recid=null;
		data.remark=state.formData.remark;
		data.amount=state.formData.amount;
		data.warehouseid=state.warehouseid;
		data.status="0";
		data.entryid=state.queryParams.entry.id;
		if(state.operateType.tabsType=="收货"){
			data.ftype="in";
		}else if(state.operateType.tabsType=="退货"){
			data.ftype="out";
		}
		if(state.formData.amount==null||state.formData.amount==undefined||
		state.formData.amount==""||parseFloat(state.formData.amount)<=0){
				ElMessage.error('操作数量不能小于等于0！')	;
				return;
		 }
		 
		if(data.ftype=="out"){
			if(state.recMap.entry.totalin<state.formData.amount){
				ElMessage.error('当前退货数量大于已经入库数量,不能继续操作！');	
				return;
			}
		}
		var needconfirm=false;
		if(data.ftype=="in"){
			var total=0;
			total=total+(state.recMap.entry.totalin?parseInt(state.recMap.entry.totalin):0);
			total=total+(state.recMap.entry.amount?parseInt(state.formData.amount):0);
			if(total>parseInt(state.queryParams.entry.amount)){
				needconfirm=true;
			}
		}
		if(needconfirm){
			ElMessageBox.confirm(
				'当前收货数量大于采购数量，请确认是否继续入库？',
				{
				  confirmButtonText: '确认',
				  cancelButtonText: '取消',
				  type: 'warning',
				  callback:(action)=>{
					 if(action=="confirm"){ 
						 purchaselistApi.receive(data).then((res)=>{
						 	if(res.data){
						 		ElMessage.success('收货成功');
						 		state.recMap=res.data;
						 		nextTick(()=>{
						 		   arrRecordRef.value.show(state.queryParams.entry,res.data.receivelist);
						 		})
						 		state.queryParams.entry.totalin=res.data.entry.totalin;
						 		state.queryParams.entry.inwhstatus=res.data.entry.inwhstatus;
						 		state.queryParams.entry.auditstatus=res.data.entry.auditstatus;
						 		emit("change");
						 		state.activeName ="2";
						 	}
						 });
					 }
				  }
				}
			  )
		}else{
			purchaselistApi.receive(data).then((res)=>{
				if(res.data){
					ElMessage.success('收货成功');
					state.recMap=res.data;
					nextTick(()=>{
					   arrRecordRef.value.show(state.queryParams.entry,res.data.receivelist);
					})
					state.queryParams.entry.totalin=res.data.entry.totalin;
					state.queryParams.entry.inwhstatus=res.data.entry.inwhstatus;
					state.queryParams.entry.auditstatus=res.data.entry.auditstatus;
					emit("change");
					state.activeName ="2";
				}
			});
		}
		
	}
	function getWarehouse(val){
		state.warehouseid=val;
	}
	function show(type,entry){
		state.activeName="1";
		state.queryParams.entry=entry;
		if(type=="rec"){
			state.operateType.dialogTitle = "采购收货"
			state.operateType.warehouseType = "入库仓库"
			state.operateType.tabsType  ="收货"
		}else{
			state.operateType.dialogTitle = "采购退货"
			state.operateType.warehouseType = "出库仓库"
			state.operateType.tabsType  ="退货"
		}
		loadDetail();
		state.dialogVisable = true;
		state.warehouseid=entry.warehouseid;
		if(warehouseRef && warehouseRef.value){
			warehouseRef.value.warehouseid=entry.warehouseid;
		}
	}
	//结束收货
	function stopReceive(){
		ElMessageBox.confirm(
			'请确认是否结束收货？',
			{
			  confirmButtonText: '确认',
			  cancelButtonText: '取消',
			  type: 'warning',
			  callback:(action)=>{
				 if(action=="confirm"){
					 var data={};
					 data.recid=null;
					 data.remark=null;
					 data.amount=0;
					 data.warehouseid=state.warehouseid;
					 data.status="1";
					 data.entryid=state.queryParams.entry.id;
					 data.ftype="in";
					 purchaselistApi.receive(data).then((res)=>{
					 	if(res.data){
					 		ElMessage.success('操作成功');
					 		state.recMap=res.data;
					 		state.queryParams.entry.totalin=res.data.entry.totalin?res.data.entry.totalin:0;
					 		state.queryParams.entry.inwhstatus=res.data.entry.inwhstatus;
					 		state.queryParams.entry.auditstatus=res.data.entry.auditstatus;
					 		emit("change");
					 		state.dialogVisable=false;
					 	}
					 });
					  
				 }
			  }
			}
		  )
	}
	function changeDelete(){
		loadDetail();
		emit("change");
	}
	function startReceive(){
		var data={};
		data.recid=null;
		data.remark=null;
		data.amount=0;
		data.warehouseid=state.warehouseid;
		data.status="2";
		data.entryid=state.queryParams.entry.id;
		data.ftype="in";
		purchaselistApi.receive(data).then((res)=>{
			if(res.data){
				ElMessage.success('操作成功');
				state.recMap=res.data;
				state.queryParams.entry.totalin=res.data.entry.totalin;
				state.queryParams.entry.inwhstatus=res.data.entry.inwhstatus;
				state.queryParams.entry.auditstatus=res.data.entry.auditstatus;
				emit("change");
				//state.dialogVisable=false;
			}
		});
	}
 
	defineExpose({
		show,
	})
</script>
<style>
	.myreceiptdailog .el-dialog__footer{
		background:#f5f5f5;
		border-bottom-right-radius:2px;
		border-bottom-left-radius:2px;
		text-align:center;
	}
	.dark .mypaymentdailog .el-dialog__footer{
		background:#1b1b1b;
		border-bottom-right-radius:2px;
		border-bottom-left-radius:2px;
		text-align:center;
	}
	.mypaymentdailog .el-dialog__body{
			 padding:1px 0px;
			 background-color:#f5f5f5;
	}
</style>
<style scoped="scoped">
	.bg-full{
		height:100%;
	}
.m-b-16{
		margin-bottom: 16px;
	}
	.record-box{
		padding:12px;
	}
.product-box{
	display: flex;
	margin-bottom: 24px;
}	
.product-box .el-image{
	margin-right: 16px;
}
.product-box .name{
font-size: 12px;
margin-bottom:8px;
}
.product-box .sku{
font-size: 12px;
color:var(--el-color-blue)
}
.m-t-32{
	margin-top: 32px;
}
.m-t-16{
	margin-top:16px;
}
.img-60{width: 60px;
height: 60px;flex: none;
margin-right: 8px;}
</style>
