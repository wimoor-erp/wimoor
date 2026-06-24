<template>
	<div class="box-margin">
		<div   v-loading="loading">
	<div class="pag-radius-bor mar-bot">
		<el-row>
		<el-col :span="24">
		<el-steps :active="step" align-center  > 
		  <el-step class="pointer"  @click="stepChange(0)" title="配货" />
		  <el-step class="pointer"  @click="stepChange(1)" title="装箱"  />
		  <el-step class="pointer" @click="stepChange(2)" title="发货出库" />
		  <el-step class="pointer"  @click="stepChange(3)" title="亚马逊接收" />
		</el-steps>
		</el-col>
		</el-row>
	</div>
	<div class="pag-radius-bor mar-bot" >
	  <div class="con-body">
		  <h3 class="m-b-8"><span v-if="step==0">配货</span>
		  <span v-if="step==1">装箱</span>
		  <span v-if="step==2">发货出库</span>
		  <span v-if="step==3">亚马逊接收</span>
		  </h3>
		  <OnePicking @stepdata="stepChange" ref="oneRef"  v-if="step==0"/>
		   <TwoBox @stepdata="stepChange" ref="twoRef"  @change="stepChange(1)" v-if="step==1" />
		  <ThreeDeliver @stepdata="stepChange" ref="threeRef" @change="stepChange(3)" v-if="step==2" />
		  <FourReceive ref="fourRef" v-if="step==3"/> 
	  </div>
	</div>
	  <div class="pag-radius-bor"  >
	  		  <ShipmentInfo ref="shipmentRef" @change="handleShipmentInfo" />
	  </div>
</div>
</div>
</template>
<script>
    export default{ name:"发货流程" };
</script>
<script setup>
	import { ref,reactive,onMounted } from 'vue'
	import { useRoute,useRouter } from 'vue-router'
	import OnePicking from"./components/one_picking.vue"
	import ThreeDeliver from"./components/three_deliver.vue"
	import FourReceive from"./components/four_receive.vue";
	import TwoBox from"./components/two_box.vue"
	import ShipmentInfo from"./components/shipment_info.vue"
	import shipmenthandlingApi from '@/api/erp/ship/shipmenthandlingApi.js';
	let router = useRouter()
	let oneRef =ref()
	let shipmentRef =ref()
	let step =ref(-1)
	let threeRef=ref()
	let fourRef=ref()
	let twoRef=ref()
	let loading=ref(false);
	const shipmentid = router.currentRoute.value.query.shipmentid;
			loading.value=true;
			onMounted(()=>{
				setTimeout(()=>{
					shipmentRef.value.getBaseInfo(shipmentid);
				},80)
			})
			
			function handleShipmentInfo(data){
				loading.value=false;
				    if(step.value==-1){
						if(data.shipment.status=="0"){
							step.value=0;
						}else if(data.shipment.status=="2"){
							step.value=0;
						}else if(data.shipment.status=="3"){
							step.value=1;
						}else if(data.shipment.status=="4"){
							step.value=2;
						}else if(data.shipment.status=="5"){
							step.value=3;
						}else if(data.shipment.status=="6"){
							step.value=3;
						}
						handleStep(data);
					}else{
						handleStep(data);
					}
			}
			
			function handleStep(data){
				if(step.value==0){
					if(oneRef.value){
						oneRef.value.loadOptData(data);
					}else{
						setTimeout(function(){oneRef.value.loadOptData(data);},300);
					}
				}else if(step.value==1){
					if(twoRef.value){
						twoRef.value.loadOptData(data);
					}else{
						setTimeout(function(){twoRef.value.loadOptData(data);},300);
					}
				}else if(step.value==2){
					if(threeRef.value){
						threeRef.value.loadOptData(data);
					}else{
						setTimeout(function(){threeRef.value.loadOptData(data);},300);
					}
					
				}else if(step.value==3){
					if(fourRef.value){
						fourRef.value.loadOptData(data.itemlist);
					}else{
						setTimeout(function(){fourRef.value.loadOptData(data.itemlist);},300);
					}
					
				}
			}
			
			function stepChange(val){
				step.value=val;
				loading.value=true;
				shipmentRef.value.getBaseInfo(shipmentid);
			}
</script>

<style>
	.is-success .el-step__line{
		background-color: var(--el-color-success);
	}
	.is-finish .el-step__line{
		background-color: #FF6700;
	}
	.con-footer{margin-top:16px;}
	.mar-bot{
		margin-bottom:16px;
	}
	.box-margin{
		padding:16px;
		min-height:calc(100vh - 36px)
	}
	
</style>
