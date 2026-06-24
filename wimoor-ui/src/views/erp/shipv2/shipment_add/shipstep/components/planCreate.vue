<template>
	 <el-dialog 
	 title="操作流程信息"
	 v-model="visiable"
	 @closed="handleClose"
	 width="30%"
	 >
	  <h2 v-if="disabled" class="text-red" style="margin-bottom:20px">操作请勿关闭此对话框</h2>
		<el-steps
			:active="step"
			finish-status="success"
		  >
			 <el-step :title="title1" />
			 <el-step :title="title2" />
			 <el-step :title="title3" />
		  </el-steps>
		  <Operation ref="operationRef" @change="handleOperationChange"></Operation>
		  <template #footer>
		  	<el-button @click="visiable=false" >关闭</el-button>
		  	<el-button @click="toStep"  :disabled="disabled" type="primary">下一步</el-button>
		  </template>
	  </el-dialog>
</template>

<script setup>
		import { ref,reactive,onMounted,toRefs,nextTick } from 'vue';
		import shipmentplanApi from '@/api/erp/shipv2/shipmentplanApi.js';
		import Operation from "./operation.vue";
		let state =reactive({ planData:{},title1:"库存扣除",title2:"亚马逊计划创建",title3:"完成配货",visiable:false,step:0,disabled:true,invparam:null, })
		let{planData,visiable,step,disabled,invparam,title1,title2,title3}=toRefs(state);
		const emit = defineEmits(['change']);
		const operationRef=ref();
		function show(planData){
			state.planData=planData;
			state.visiable=true;
			if(state.planData.auditstatus!=2){
				state.step=2;
				state.title1="库存锁定完成";
				state.title2="亚马逊计划创建完成";
				state.title3="完成配货";
				state.isshowNotice=false;
				emit("change","success");
			}else{
				if(state.planData.invstatus=='0'){
					getInvParam();
				}else{
					createInboundPlan();
				}
			}
			
			
			
		}
		
		function getInvParam(){
			state.step=0;
			state.title1="获取库存锁定参数...";
			shipmentplanApi.getInvParam({"id":state.planData.id}).then((res)=>{
				    state.invparam=res.data;
					outBoundShipInventory(state.invparam);
				 
			});
		}
		function handleClose(){
			 emit("change");
		}
		function outBoundShipInventory(params){
			state.step=1;
			state.title1="开始库存锁定...";
			shipmentplanApi.outBoundShipInventory(params).then((res)=>{
				if(state.planData.invstatus=='0'){
					setInvStatus();
				}
			});
		}
		
		function setInvStatus(){
			state.step=1;
			state.title1="标记库存锁定...";
			shipmentplanApi.setInvStatus({"id":state.planData.id,"status":1}).then((res)=>{
				state.title1="库存锁定完成";
				if(state.planData.invstatus=='0'){
					createInboundPlan();
				}
			});
			
		}
		function handleOperationChange(data){
			//跳转路由至第二步
			if(data && data.operationStatus=="SUCCESS" ){
				 state.step=2;
				 state.title3="完成配货";
				 state.disabled=false;
				 emit("change","success");
			}
			
		}
		
		function toStep(){
			var timer1=setTimeout(function(){
					router.push({
						path:'/e/s/s/two',
						query:{
						  id:state.planData.id,
						  title:"发货处理_装箱",
						  path:'/e/s/s/two',
						  replace:true
						}
					})		 
					clearTimeout(timer1);
			},500);
		}
		
		function createInboundPlan(){
			state.step=1;
			state.title2="开始亚马逊计划创建...";
			shipmentplanApi.confirmInboundPlan({"formid":state.planData.id}).then((res)=>{
				state.title2="亚马逊计划创建完成";
				if(res.data&&res.data.operationid){
					 operationRef.value.show(res.data.operationid);
				}else{
					state.step=2;
					state.title3="完成配货";
					state.disabled=false;
					emit("change","success");
				}
			}).catch(()=>{
			   emit("change","fail");
		    });
		}
		
		
		 defineExpose({ show })
</script>

<style>
</style>