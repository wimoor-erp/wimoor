<template>
	 <el-dialog 
	 title="操作流程信息"
	 v-model="visiable"
	 @closed="handleClose"
	 width="60%"
	 >
	  <h2 v-if="disabled" class="text-red" style="margin-bottom:20px">操作请勿关闭此对话框</h2>
		  <el-steps :active="step" finish-status="success">
			 <el-step :title="title0" />
			 <el-step :title="title1" />
			 <el-step :title="title2" />
			 <el-step :title="title3" />
			 <el-step :title="title4" />
		  </el-steps>
		  <Operation ref="operationRef" @change="handleOperationChange"></Operation>
		  <template #footer>
		  	<el-button @click="handleClose()"  >关闭</el-button>
		  	<el-button @click="toStep"  :disabled="disabled" type="primary">下一步</el-button>
		  </template>
	  </el-dialog>
</template>

<script setup>
		import { ref,reactive,onMounted,toRefs,nextTick } from 'vue';
	    import {Refresh} from '@element-plus/icons-vue';
		import shipmentplanApi from '@/api/erp/shipv2/shipmentplanApi.js';
		import shipmentPlacementApi from '@/api/erp/shipv2/shipmentPlacementApi.js';
		import shipmentTransportationApi from '@/api/erp/shipv2/shipmentTransportationApi.js';
		import { useRoute,useRouter } from 'vue-router';
		import Operation from "./operation.vue";
		let router = useRouter();
		let state =reactive({ planData:{},
		title0:"库存扣除",title1:"货件方案确认",title2:"收货日期确认",title3:"物流确认",title4:"完成发货",
		option:{},visiable:false,
		step:0,disabled:true,invparam:null,deliveryWindowOptions:{},shipmentids:[],
		shipments:[]})
		let{planData,visiable,step,disabled,invparam,title0,title1,title2,title3,title4,option,shipments,deliveryWindowOptions,shipmentids}=toRefs(state);
		const emit = defineEmits(['change']);
		const operationRef=ref();
		function show(planData,option,shipments){
			state.planData=planData;
			state.option=option;
			state.shipments=shipments;
			state.visiable=true;
			if(state.planData.auditstatus>7&&state.planData.auditstatus<12){
				state.step=4;
				state.title0="库存扣减完成";
				state.title1="货件方案确认";
				state.title2="收货日期确认完成";
				state.title3="物流确认完成";
				state.title4="完成发货";
				state.isshowNotice=false;
				emit("change","success");
			}else{
				if(state.planData.invstatus=='1'){
					getInvParam();
				}else{
					confirmPlacementOption();
				}
			}
		}
		
		function getInvParam(){
			state.step=0;
			state.title1="获取库存锁定参数...";
			shipmentplanApi.getInvParam({"id":state.planData.id}).then((res)=>{
				    state.invparam=res.data;
					state.title1="成功获取参数...";
					outShipInventory(state.invparam);
				 
			});
		}
		function handleClose(){
			state.visiable=false;
			 emit("change");
		}
		function outShipInventory(params){
			state.step=0;
			state.title0="开始库存扣减...";
			shipmentplanApi.outShipInventory(params).then((res)=>{
				state.title0="成功库存扣减...";
					setInvStatus();
			});
		}
		
		function setInvStatus(){
			state.title0="标记库存扣减...";
			shipmentplanApi.setInvStatus({"id":state.planData.id,"status":2}).then((res)=>{
				state.title0="库存扣减完成";
				state.step=1;
				if(state.planData.invstatus=='0'){
					confirmPlacementOption();
				}
			});
			
		}
		function handleOperationChange(data){
			//跳转路由至第二步
			if(data && data.operationStatus=="SUCCESS"&&data.operation=="confirmPlacementOption" ){
				  state.title1="亚马逊计划发货完成";
				  state.step=2;
				  if(state.planData.hasSubmitPackage){
				      confirmDeliveryWindowOptions();
				  }else{
					  shippedInboundPlan();
				  }
			}
			if(data && data.operationStatus=="SUCCESS"&&data.operation=="confirmTransportationOptions" ){
				  state.title3="物流确认完成";
				  state.step=4;
				  shippedInboundPlan();
			}
			if(data && data.operationStatus=="SUCCESS"&&data.operation=="confirmDeliveryWindowOptions" ){
				  state.shipmentids.push(state.deliveryWindowOptions[data.operationid]);
				  if(state.shipmentids.length==state.option.shipmentIds.length){
				  					  state.step=3;
				  					  state.title2="收货日期确认完成";
				                      confirmTransportationOptions();
				  }
				 
			}
		}
		function confirmDeliveryWindowOptions(){
			state.title2="开始收货日期...";
			state.step=2;
			state.shipments.forEach(item=>{
				var data={"formid":state.planData.id,"deliveryWindowOptionId":item.deliveryWindowOptionId ,"shipmentid":item.shipmentId}
				shipmentPlacementApi.confirmDeliveryWindowOptions(data).then(res=>{
					if(res.data&&res.data.operationid){
						 state.deliveryWindowOptions[res.data.operationid]=item.shipmentId;
						 operationRef.value.show(res.data.operationid);
					}else{
						state.title2="同步异常";
						emit("change","fail");
					}
				});
			})
			
		}
		function toStep(){
			var timer1=setTimeout(function(){
					router.push({
						path:'/e/s/s/end',
						query:{
						  id:state.planData.id,
						  title:"发货处理_装箱",
						  path:'/e/s/s/end',
						  replace:true
						}
					})		 
					clearTimeout(timer1);
			},500);
		}
		function confirmTransportationOptions(){
			var param=[];
			state.title3="开始物流确认...";
			state.step=3;
			state.shipments.forEach(item=>{
				var data={"transportationOptionId":item.selectedTransportationOptionId ,"shipmentId":item.shipmentId,"transactionName":shipment.transactionName}
				param.push(data);
			})
			shipmentTransportationApi.confirmTransportationOptions(state.planData.id,param).then(res=>{
				if(res.data&&res.data.operationid){
					 operationRef.value.show(res.data.operationid);
				}else{
					state.title3="同步异常";
					emit("change","fail");
				}
			});
		}
		function shippedInboundPlan(){
			state.title4="同步发货信息...";
			state.step=4;
			shipmentPlacementApi.shippedInboundPlan(state.planData.id,state.option.shipmentIds).then(res=>{
				state.step=5;
				state.title4="完成发货";
				state.disabled=false;
				emit("change","success");
			})
		}
		function confirmPlacementOption(){
			state.step=1;
			state.title1="货件方案确认...";
			shipmentPlacementApi.confirmPlacementOption({"planid":state.planData.id,"placementOptionId":state.planData.placementOptionId}).then((res)=>{
				if(res.data&&res.data.operationid){
					 operationRef.value.show(res.data.operationid);
				}else{
					state.title2="同步异常";
					emit("change","fail");
				}
			}).catch(()=>{
			   emit("change","fail");
		    });
		}
		
		
		 defineExpose({ show })
</script>

<style>
</style>