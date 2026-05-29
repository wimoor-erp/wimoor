<template>
	<el-dialog v-model="detailVisible" title="货件详情" class="ship-detail-dialog" top="3vh" width="85%"  >
	    <template #header="{ close, titleId,titleClass  }">
	      <div class="my-header">
	        <div :id="titleId"  :class="titleClass">
				  <el-tabs
				    v-model="tabActive"
				    type="card"
				    class="demo-tabs"
				    @tab-change="tabchange"
				  >
				    <el-tab-pane label="货件详情" :name="0"></el-tab-pane>
				    <el-tab-pane label="物流出库" :name="1"></el-tab-pane>
				    <el-tab-pane label="海关申报" :name="2"></el-tab-pane>
				    <el-tab-pane label="接收详情" :name="3"></el-tab-pane>
				  </el-tabs>
			</div>
	      </div>
	    </template>	
	<div   v-loading="loading">
		  <TwoBox @stepdata="stepChange" ref="twoRef"  @change="stepChange(0)" v-if="tabActive===0" />
		  <ThreeDeliver @stepdata="stepChange" ref="threeRef" @change="stepChange(1)" v-if="tabActive===1" />
		  <CustomsPicking ref="customsPickingRef" v-if="tabActive===2" ></CustomsPicking>
		  <FourReceive ref="fourRef" v-if="tabActive===3"/> 
	   <ShipmentInfo ref="shipmentRef" @change="handleShipmentInfo" />
</div>
     <template #footer>
           <el-button @click="detailVisible = false">关闭</el-button>
       </template>
	</el-dialog>
	
</template>
<script>
    export default{ name:"发货流程" };
</script>
<script setup>
	import { ref,reactive,onMounted,nextTick, toRefs } from 'vue'
	import { useRoute,useRouter } from 'vue-router'
	import {Printer,AddPrint,InboxOut,ShoppingCartDel} from '@icon-park/vue-next';
	import CustomsPicking from"./components/customs_picking.vue"
	import TwoBox from"./components/two_box.vue"
	import ThreeDeliver from"./components/three_deliver.vue"
	import FourReceive from"./components/four_receive.vue"
	import ShipmentInfo from"./components/shipment_info.vue"
	import shipmenthandlingApi from '@/api/erp/ship/shipmenthandlingApi.js';
	const emit = defineEmits(['change']);
	const state =reactive({
		tabActive:0,
		shipmentData:{},
	})
	const{
		shipmentData,
		tabActive,
	}=toRefs(state)
	let router = useRouter()
	const oneRef =ref();
	const shipmentRef =ref();
	const twoRef=ref();
	const threeRef=ref();
	const customsPickingRef=ref();
	const fourRef=ref();
	let detailVisible=ref(false);
	let loading=ref(false);
	let shipmentid = ref("");
	let localRow=ref({});
			function show(row,mstep){
				localRow.value=row;
				state.tabActive=mstep;
				shipmentid.value=row.shipmentid;
				detailVisible.value=true;
				loading.value=true;
				nextTick(()=>{
					var timer=setTimeout(()=>{
						shipmentRef.value.getBaseInfo(row.shipmentid);
					},200);
				});
			}
			defineExpose({show})
			function handleShipmentInfo(data){
				    loading.value=false;
					state.shipmentData = data;
					nextTick(()=>{
					     handleStep(data);
					});
			}
			function tabchange(val){
				handleStep(state.shipmentData);
			}
			function handleStep(data){
				nextTick(()=>{
					if(state.tabActive===0){
								twoRef.value.loadOptData(data);
						}else if(state.tabActive===1){
								threeRef.value.loadOptData(data);
						}else if(state.tabActive===2){
								customsPickingRef.value.loadOptData(data);
						}else if(state.tabActive===3){
								fourRef.value.loadOptData(data);
						}
				})
			}
			
			function stepChange(val){
				console.log("stepchange");
				emit("change");
				loading.value=true;
				shipmentRef.value.getBaseInfo(shipmentid.value);
			}
</script>

<style >

	.ship-detail-dialog .el-tabs--card>.el-tabs__header .el-tabs__item.is-active{
		    border-top-color: #ff7315;
	}
 .ship-detail-dialog .el-tabs--card>.el-tabs__header .el-tabs__item{
	 border-top: 2px solid transparent;
 }
</style>
