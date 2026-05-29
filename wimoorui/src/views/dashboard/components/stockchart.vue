<template>
  <div>
    <el-space style="margin-bottom:8px;margin-top:16px;">
      <span class="pag-title">待办事项</span>
    </el-space>
    <div class="pag-radius-bor" style="padding:20px">
       <el-row gutter="16">
		   <el-col :span="8" class="pointer" @click="goPage(purchase)">
			    <div class="item-text"> 采购待审核</div>
				<p class="pag-data-num cell-t-8">
					
					<icon-park
						 :type="purchase.icon"
						class="ic-cen"
						style="float:left;padding-right:10px"
						theme="outline" size="16" 
						:fill="purchase.color" :strokeWidth="4"/>
					{{orderState.nopass}}</p>
		   </el-col>
		   <el-col :span="8" class="pointer" @click="goPage(purchase)">
			   <div class="item-text">采购待付款</div>
			   <p class="pag-data-num cell-t-8">
			   <icon-park
			   	 :type="purchase.icon"
			   	class="ic-cen"
			   	style="float:left;padding-right:10px"
			   	theme="outline" size="16" 
			   	:fill="purchase.color" :strokeWidth="4"/>
			   {{orderState.passnopay}}</p>
		   </el-col>
		   <el-col :span="8" class="pointer" @click="goPage(purchase)">
			   <div class="item-text">采购待入库</div>
			   <p class="pag-data-num cell-t-8">
			   <icon-park
			   	 :type="purchase.icon"
			   	class="ic-cen"
			   	style="float:left;padding-right:10px"
			   	theme="outline" size="16" 
			   	:fill="purchase.color" :strokeWidth="4"/>
			   {{orderState.passinware}}</p>
		   </el-col>
		   <el-col :span="8" class="pointer" @click="goPage(process)">
			   <div class="item-text">加工单处理中</div>
			   <p class="pag-data-num cell-t-8">
			   <icon-park
			   	 :type="process.icon"
			   	class="ic-cen"
			   	style="float:left;padding-right:10px"
			   	theme="outline" size="16" 
			   	:fill="process.color" :strokeWidth="4"/>
			   {{orderState.inProgressNum}}
			   </p>
		   </el-col>
		   <el-col :span="8" class="pointer" @click="goPage(shipment)">
			   <div class="item-text">发货待审核</div>
			   <p class="pag-data-num cell-t-8">
			   <icon-park
			   	 :type="shipment.icon"
			   	class="ic-cen"
			   	style="float:left;padding-right:10px"
			   	theme="outline" size="16" 
			   	:fill="shipment.color" :strokeWidth="4"/>
			   {{orderState.shipneedapply||0}}</p>
		   </el-col>
		   <el-col :span="8" class="pointer" @click="goPage(shipment)">
			   <div class="item-text">发货待处理</div>
			   <p class="pag-data-num cell-t-8">
			   <icon-park
			   	 :type="shipment.icon"
			   	class="ic-cen"
			   	style="float:left;padding-right:10px"
			   	theme="outline" size="16" 
			   	:fill="shipment.color" :strokeWidth="4"/>
			   {{orderState.shipneedprocess||0}}</p>
		   </el-col>
	   </el-row>
	   		   
    </div>
 </div>
</template>
<script setup>
import {IconPark} from '@icon-park/vue-next/es/all';
import { ref,reactive,onMounted,watch,nextTick, toRefs } from 'vue'
import {useRouter } from 'vue-router';
import purchaselistApi from '@/api/erp/purchase/form/listApi.js';
import shipmentplanApi from '@/api/erp/shipv2/shipmentplanApi.js';
import {getCountNum } from '@/api/erp/assembly/assemblyApi.js';
const router = useRouter()
const state =reactive({
	 orderState:{},
	 purchase:{
		name:'采购单',
		icon:"transaction",
		url:'/erp/purchase/orders',
		color:'#ff7315',
		bg:'#fff9f4',
	 },
	 shipment:{
	 		name:'新发货单',
	 		icon:"transporter",
	 		url:'/erp/shipv2/shipment_add/list',
			color:'#3c8bfa',
			bg:'#f0f8ff',
	 },
	 process:{
	 		name:'加工单',
	 		icon:"workbench",
	 		url:'/erp/purchase/process',
	 		color:'#67c23a',
	 	    bg:'#f0f9eb',
	 },
	 	 
})
const {
	 orderState,purchase,shipment,process
}=toRefs(state)
 function loadStatusNums(){
 	//加载各个状态下的num
 	purchaselistApi.getPurchaseNumAllStatus({"ftype":"orders"}).then((res)=>{
		if(res.data){
			state.orderState.nopass=res.data.nopass;
			state.orderState.passinware=res.data.passinware;
			state.orderState.passnopay=res.data.passnopay+res.data.passpay;
		}
 	});
	getCountNum().then(res=>{
		if(res.data){
			state.orderState.inProgressNum=res.data.inProgressNum;
			state.orderState.pendingNointerNum=res.data.pendingNointerNum;
			state.orderState.pendingNum=res.data.pendingNum;
		}
	});
	shipmentplanApi.findNum().then(res=>{
		if(res.data){
			state.orderState.shipneedapply=res.data.needapply;
			state.orderState.shipneedprocess=res.data.needprocess;
		}
	})
 }
 function goPage(item){
 	router.push({
 		path:item.url,
 		query:{
 		title:item.name,
 		path:item.url,
 		}
 	})
 }
 onMounted(()=>{
	 loadStatusNums();
 })
</script>
<style scoped>
 .item-fun{
	text-align: center;
	display: flex;
	flex-direction: column;
	align-items: center;
	margin-bottom: 16px;
	cursor: pointer;
 }
 .item-text{
	 font-size:12px;
	 color:var(--el-color-info-dark-2);
	 margin-top: 10px;
 }
 .item-icon{
	 border-radius: 6px;
	 display: flex;
	 align-items: center;
	 justify-content: center;
	 width: 36px;
	 height:36px;
	 margin-bottom: 8px;
 }
 .pag-radius-bor{
	 padding-bottom: 0;
	 padding-right:0;
	 padding-left:0;
 }
</style>