<template>
	 <el-dialog 
	 title="操作流程信息"
	 v-model="visiable"
     @closed="handleClose"
	 width="30%"
	 >
	 <h2 v-if="isshowNotice" class="text-red" style="margin-bottom:20px">操作请勿关闭此对话框</h2>
		<el-steps
				:active="step"
				finish-status="success"
			  >
				 <el-step :title="title1" />
				 <el-step :title="title2"  />
				 <el-step :title="title3" />
			  </el-steps>
           <Operation ref="operationRef" @change="handleOperationChange"></Operation>
		  <template #footer>
		  	<el-button @click="visiable=false" >关闭</el-button>
		  </template>
	  </el-dialog>
</template>

<script setup>
		import { ref,reactive,onMounted,toRefs,nextTick } from 'vue';
		import shipmentplanApi from '@/api/erp/shipv2/shipmentplanApi.js';
		import Operation from "./operation.vue";
		let state =reactive({ planData:{},isok:"fail",isshowNotice:false,visiable:false,step:0,invparam:null,title1:"亚马逊同步",title2:"库存待处理",title3:"待完成"})
		let{planData,visiable,step,isshowNotice,isok,invparam,title1,title2,title3}=toRefs(state);
		const emit = defineEmits(['change']);
		const operationRef=ref();
		function show(planData){
			state.visiable=true;
			state.planData=planData;
			if(state.planData.auditstatus==12 &&  parseInt(state.planData.invstatus) ==0){
				state.step=2;
				state.title1="API同步完成";
				state.title2="库存撤回完成";
				state.title3="取消完成";
				state.isshowNotice=false;
				state.isok="success";
				emit("change",state.isok);
			}else{
				 cancelInboundPlan();
			}
		}
		
		function cancelInboundPlan(){
			state.step=0;
			state.title1="平台计划取消...";
			state.isshowNotice=true;
			if(state.planData.auditstatus>1 && state.planData.auditstatus<12){
				shipmentplanApi.cancelInboundPlan({"planid":state.planData.id}).then((res)=>{
					if(res.data&&res.data.operationid){
						 operationRef.value.show(res.data.operationid);
					}else if(state.planData.invstatus!='0'){
						 getInvParam();
					} else{
						state.step=4; 
					}
					state.title1="平台计划取消完成";
				});
			}else{
				if(state.planData.invstatus!='0'){
					 getInvParam();
				} else{
					state.step=4; 
				}
				state.title1="平台计划取消完成";
			}
			
		}
		
		function getInvParam(){
			state.step=1;
			state.title2="库存撤回中...";
			shipmentplanApi.getInvParam({"id":state.planData.id}).then((res)=>{
				state.invparam=res.data;
				if(state.planData.invstatus=='1'){
					state.invparam.opttype="cancelOutbound&cancelassembly";
				}
				if(state.planData.invstatus=='2'){
					state.invparam.opttype="cancelfulfillable&cancelassembly";
				}
				updateDisable(state.invparam);
			});
		}
		 
		
		function updateDisable(params){
			state.step=2;
			state.title2="库存撤回中...";
			shipmentplanApi.updateDisable(params).then((res)=>{
				state.title2="库存撤回完成";
					setInvStatus();
			}).catch(e=>{
				state.isok="fail";
				emit("change",state.isok);
			});
		}
		
		function handleClose(){
			 emit("change",state.isok);
		}
		
		function handleOperationChange(data){
			if(data && data.operationStatus=="SUCCESS" ){
				if(state.planData.invstatus!='0'){
					 getInvParam();
				} else{
					state.step=4; 
				}
			}else{
				state.isok="fail";
				emit("change",state.isok);
			}
				
		}
		
		function setInvStatus(){
			state.step=3;
			state.title3="计划取消中...";
			shipmentplanApi.setInvStatus({"id":state.planData.id,"status":0}).then((res)=>{
				 if(res.data){
					state.step=4; 
					state.title3="取消完成";
					state.isshowNotice=false;
					state.isok="success";
					emit("change",state.isok);
				 }
			});
			
		}
		
		
		
		
		 defineExpose({ show })
</script>

<style>
</style>