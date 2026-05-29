<template>
		<div style="margin:20px" v-loading="loading" element-loading-text="正在努力加载中...">
			<el-row :gutter="16">
				<el-col :span="6">
					<el-scrollbar style="height:calc(100vh - 75px)">
					<el-card v-if="orderData&&(orderData instanceof Array)&&orderData.length>0" v-for="item in orderData" style="margin-bottom:20px">
						<template #header>
						     <span class="font-small">单据信息</span>
						 </template>
						<div class="flex-center-between">
							<el-space direction="vertical" alignment="flex-start">
								<h3 v-if="shipmentid">{{shipmentid}}</h3>
								<h3 v-else-if="item.number">{{item.number}}</h3>
								<span class="font-extraSmall">
									创建于{{dateFormat(item.createdate)}}
								</span>
							</el-space>
							<span class="font-extraSmall" v-if="item.auditime">{{dateFormat(item.auditime)}}审核</span>
						</div>
						 <div v-if="item.shipments&& item.shipments.indexOf('sh')>=0">
							  <el-tag type="success" >{{item.shipments}}</el-tag> 
						 </div>
						<el-timeline class="m-t-24" >
						    <el-timeline-item 
						       v-for="(activity, index) in item.activities"
						      :key="index"
						      :icon="activity.icon"
						      :type="activity.type"
						      :color="activity.color"
						      :hollow="activity.hollow"
						      :timestamp="activity.timestamp"
						    >
						      {{ activity.content }}
						    </el-timeline-item>
						  </el-timeline>
						 
						  <el-row  class="m-t-24">
							  <el-col :span="12" >
								  <el-space direction="vertical" alignment="flex-start">
									  <span class="font-bold" v-if="item.sumQuantity">
										 {{item.sumQuantity}}
									  </span>
									  <span class="font-bold" v-else-if="item.sumquantity">
									  										 {{item.sumquantity}}
									  </span>
									  <span  class="font-extraSmall">发货数量</span>
								  </el-space>
							  </el-col>
						  </el-row>
						  <el-divider class="m-t-16"></el-divider>
						  <el-row class="m-t-16">
							  <el-col :span="4">
								  <span class="font-base-nine">备注</span>
							  </el-col>
							  <el-col :span="20">
								  <span v-if="item.remark" class="font-base">
									  {{item.remark}}</span>
									  <span v-else>-</span>
							  </el-col>
						  </el-row>
					</el-card>
					<el-card v-else>
						<template #header>
						     <span class="font-small">单据信息</span>
						 </template>
						<div class="flex-center-between">
							<el-space direction="vertical" alignment="flex-start">
								<h3 v-if="shipmentid">{{shipmentid}}</h3>
								<h3 v-else-if="orderData.number">{{orderData.number}}</h3>
								<span class="font-extraSmall">
									创建于{{dateFormat(orderData.createdate)}}
								</span>
							</el-space>
							<span class="font-extraSmall" v-if="orderData.auditime">{{dateFormat(orderData.auditime)}}审核</span>
						</div>
						 <div v-if="shipments" class="flex gap-2" v-for="shipmentid in shipments" style="padding-bottom:10px">
							 <el-tag type="success" >{{shipmentid}}</el-tag> 
						 </div>
						 <div v-if="orderData.shipments&& orderData.shipments.indexOf('sh')<0" class="flex gap-2" v-for="shipmentid in orderData.shipments" style="padding-bottom:10px">
						 		 <el-tag type="success" >{{shipmentid}}</el-tag> 
						 </div>
						 <div v-if="orderData.shipments&& orderData.shipments.indexOf('sh')>=0">
							  <el-tag type="success" >{{orderData.shipments}}</el-tag> 
						 </div>
						<el-timeline class="m-t-24" >
						    <el-timeline-item 
						       v-for="(activity, index) in activities"
						      :key="index"
						      :icon="activity.icon"
						      :type="activity.type"
						      :color="activity.color"
						      :hollow="activity.hollow"
						      :timestamp="activity.timestamp"
						    >
						      {{ activity.content }}
						    </el-timeline-item>
						  </el-timeline>
						 
						  <el-row  class="m-t-24">
							  <el-col :span="12" >
								  <el-space direction="vertical" alignment="flex-start">
									  <span class="font-bold" v-if="orderData.sumQuantity">
										 {{orderData.sumQuantity}}
									  </span>
									  <span class="font-bold" v-else-if="orderData.sumquantity">
									  										 {{orderData.sumquantity}}
									  </span>
									  <span  class="font-extraSmall">发货数量</span>
								  </el-space>
							  </el-col>
						  </el-row>
						  <el-divider class="m-t-16"></el-divider>
						  <el-row class="m-t-16">
							  <el-col :span="4">
								  <span class="font-base-nine">备注</span>
							  </el-col>
							  <el-col :span="20">
								  <span v-if="orderData.remark" class="font-base">
									  {{orderData.remark}}</span>
									  <span v-else>-</span>
							  </el-col>
						  </el-row>
					</el-card>
					</el-scrollbar>
				</el-col>
				<el-col :span="18">
					<el-card>
						<ShelfProduct  ref="shelfProductRef"  @change="handleSubmit"/>
					</el-card>
				</el-col>
			</el-row>
		</div>
</template>

<script setup>
	import { ref,reactive,onMounted, toRefs} from 'vue'
	import orderblankApi from '@/api/erp/ship/orderblankApi.js'
	import ShelfProduct from "./components/shelfproduct.vue"
	import {dateFormat} from '@/utils/index.js';
	import { useRoute } from "vue-router";
	import shipmenthandlingApi from '@/api/erp/ship/shipmenthandlingApi.js';
	import {assemblyQuotainfos,assemblyQuotainfosById,assemblyCheckInv } from '@/api/erp/assembly/assemblyApi.js';
	import shipmentQuotaApi from '@/api/erp/shipv2/shipmentQuotaApi.js';
	import preprocessApi from '@/api/erp/purchase/plan/preprocessApi.js';
	const route = useRoute();
	const shelfProductRef=ref();
	const shipmentid=route.query.shipmentid;
	const shipments=route.query.shipments;
	const planid=route.query.formid;
	const planids=route.query.planids;
    const assemblys=route.query.assemblys?(route.query.assemblys instanceof Array ? route.query.assemblys:[route.query.assemblys]):null;
	const recpreprocessingid=route.query.recpreprocessingid;
	const asspreprocessingid=route.query.asspreprocessingid;
	const state = reactive({
		activities:[
			{
				content: '-',
			    timestamp: '发货仓库',
			    type: 'primary',
			    hollow: true,
			},
			{
			    content: '-',
			    timestamp: '目的仓库',
			    type: 'primary',
			    hollow: true,
			},
		],
		formid:undefined,
		orderData:{},
		loading:false,
		skuList:[],
		quatoInfo:{
			
		}
	})
	const {
		activities,
		orderData,
		skuList,
		loading,
		quatoInfo,
	} = toRefs(state)
	onMounted(()=>{
		getshipFormData()
	})
	function handleSubmit(){
		   if(shipments){
		        shipmenthandlingApi.checkinv(state.formid,shipments).then(res=>{ getshipFormData(); })
		   }else if(shipmentid){
			    var myshipments=[];
			    myshipments.push(shipmentid);
			    shipmenthandlingApi.checkinv(state.formid,myshipments).then(res=>{ getshipFormData(); })
		   }else if(assemblys){
			    assemblyCheckInv(state.formid,assemblys).then(res=>{ getshipFormData(); });
				preprocessApi.update({"formid":state.formid,"donetype":"checkinv"}).then(res=>{ getshipFormData(); })
		   }else if(recpreprocessingid){
			    preprocessApi.update({"formid":state.formid,"donetype":"checkinv"}).then(res=>{ getshipFormData(); })
		   }else if(asspreprocessingid){
			    preprocessApi.update({"formid":state.formid,"donetype":"checkinv"}).then(res=>{ getshipFormData(); })
		   }else if(planids){
			    shipmentQuotaApi.checkinv(state.quatoInfo.formid,planids).then(res=>{ getshipFormData(); })
		   }else if(planid){
			    var list=[];
			    list.push(state.quatoInfo.formid);
			    shipmentQuotaApi.checkinv(state.quatoInfo.formid,list).then(res=>{ getshipFormData(); })
		   }
	}
	const getshipFormData = function(){
		 state.loading=true;
		if(shipmentid){
			orderblankApi.getQuotainfo(shipmentid).then((res)=>{
				state.loading=false;
				state.orderData = res.data
				state.activities[0].content = state.orderData.warehouse
				state.activities[1].content = state.orderData.groupname+'-'+state.orderData.country+'('+state.orderData.center+')'
				state.skuList = res.data.itemList;
				state.formid=res.data.checkinv;
				setTimeout(function(){
					shelfProductRef.value.show(state.skuList,res.data.checkinv);
				},200)
			})
		}else if(shipments){
			orderblankApi.getQuotainfos(shipments).then((res)=>{
				state.loading=false;
				state.orderData = res.data
				state.activities[0].content = state.orderData.warehouse
				state.activities[1].content = state.orderData.groupname+'-'+state.orderData.country+'('+state.orderData.center+')'
				state.skuList = res.data.itemList
				state.formid=res.data.checkinv;
				setTimeout(function(){
					shelfProductRef.value.show(state.skuList,res.data.checkinv);
				},200)
			})
		}else if(assemblys){
			assemblyQuotainfos(assemblys).then((res)=>{
				state.loading=false;
				state.orderData = res.data;
				state.orderData.shipments=state.orderData.shipmentid.split(",");
				state.activities[0].content = state.orderData.warehouse;
				state.activities[1].content = state.orderData.warehouse;
				state.skuList = res.data.itemList
				state.formid=res.data.checkinv;
				setTimeout(function(){
					shelfProductRef.value.show(state.skuList,res.data.checkinv);
				},200)
				});
		}else if(asspreprocessingid){
			assemblyQuotainfosById({"formid":asspreprocessingid}).then((res)=>{
				state.loading=false;
				state.orderData = res.data;
				state.orderData.shipments=state.orderData.shipmentid.split(",");
				state.activities[0].content = state.orderData.warehouse;
				state.activities[1].content = state.orderData.warehouse;
				state.skuList = res.data.itemList
				state.formid=res.data.checkinv;
				setTimeout(function(){
					shelfProductRef.value.show(state.skuList,res.data.checkinv);
				},200)
				});
		}else if(recpreprocessingid){
			preprocessApi.receiveQuotainfos({"planid":recpreprocessingid}).then((res)=>{
				state.loading=false;
				state.orderData = res.data;
				state.orderData.shipments=state.orderData.shipmentid.split(",");
				state.activities[0].content = state.orderData.warehouse;
				state.activities[1].content = state.orderData.warehouse;
				state.skuList = res.data.itemList
				state.formid=res.data.checkinv;
				setTimeout(function(){
					shelfProductRef.value.show(state.skuList,res.data.checkinv);
				},200)
			})
		}else if(planid){
				shipmentQuotaApi.info(planid).then((res)=>{
					state.loading=false;
					if(res.data){
						state.orderData=res.data.shipPlan;
						state.quatoInfo=res.data.quatoInfo;
						state.activities[0].content = state.orderData.warename
						state.activities[1].content = state.orderData.groupname+'-'+state.orderData.country;
						state.skuList = state.quatoInfo.list;
						var time=setTimeout(function(){
							shelfProductRef.value.show(state.skuList,state.quatoInfo.formid,state.orderData);
							clearTimeout(time);
						},200);
					}
				});
		}else if(planids){
			if(!(planids instanceof Array)){
				shipmentQuotaApi.info(planids).then((res)=>{
					state.loading=false;
					if(res.data){
						state.orderData=res.data.shipPlan;
						state.quatoInfo=res.data.quatoInfo;
						state.activities[0].content = state.orderData.warename
						state.activities[1].content = state.orderData.groupname+'-'+state.orderData.country;
						state.skuList = state.quatoInfo.list;
						var time=setTimeout(function(){
							shelfProductRef.value.show(state.skuList,state.quatoInfo.formid,state.orderData);
							clearTimeout(time);
						},200);
					}
				});
			}else{
				//批量勾选plan
				shipmentQuotaApi.quotainfos(planids).then((res)=>{
					state.loading=false;
					if(res.data){
						state.orderData=res.data.shipPlan;
						state.quatoInfo=res.data.quatoInfo;
						state.orderData.forEach(item=>{
								item.activities=[
																{
																	content: '-',
																	timestamp: '发货仓库',
																	type: 'primary',
																	hollow: true,
																},
																{
																	content: '-',
																	timestamp: '目的仓库',
																	type: 'primary',
																	hollow: true,
																},
															];
								item.activities[0].content = item.warename
								item.activities[1].content =item.groupname+'-'+item.country;
							})
						state.skuList = state.quatoInfo.list;
						var time=setTimeout(function(){
							shelfProductRef.value.show(state.skuList,state.quatoInfo.formid,state.orderData);
							clearTimeout(time);
						},200);
					}
				});
			}
			
		}
	}
</script>

<style scoped="scoped">
	.height-screen{
		height: calc(100vh - 36px)!important;
	}
	.m-t-24{
		margin-top: 24px;
	}
	.m-t-16{
		margin-top:16px!important;
	}
</style>
