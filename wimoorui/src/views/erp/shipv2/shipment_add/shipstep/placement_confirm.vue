<template>
	<div class="box-margin">
		<div class="pag-radius-bor mar-bot" >
			
			<div class="con-body three-ship" v-if="planData.invtype=='1'">
				<Header ref="headerRef" ></Header>
				<el-card class="text-center" >虚拟发货，可以直接出库</el-card>
				<el-button  style="width:49%;margin-top:20px">耗材与箱子出库</el-button>  
				<el-button type="primary" style="width:49%;margin-top:20px">确认发货</el-button>
			</div>
			<div v-else class="con-body three-ship"  > 
			  <Header ref="headerRef" ></Header>
			  	  </div>
				   <div>
				   <div class="title-name">发货日期</div>
				   <el-date-picker
							@change="submitShipDate"
				          v-model="planData.shippingDate"
				          type="date"
						  
				          placeholder="选择日期"
				        />
				   </div>
				    <div class="placementOption">
				   <div class="title-name" v-loading="optionsloading"  element-loading-text="加载入库配置方案...">选择入库配置服务</div>
					    <RadioCardGroup v-model="placementOptionId" @change="handlePlacementChange" :isspace="true">
								   <RadioCard  v-for="option in options" :value="option.placementOptionId" shadow="hover"   style="width:400px" >
										   <div class="font-base font-bold ">{{option.shipmentIds.length}}个货件</div>
										   <div class="font-extraSmall m-t-8" v-if="option.shipmentIds.length>3">亚马逊优化货件拆分</div>
										   <div  v-else>部分货件拆分</div>
										   <el-divider />
										   <div v-for="fee in option.fees">  
										   <strong v-if="fee.value.amount>0">起价 ${{fee.value.amount}}</strong>
										   <span v-else><strong class="text-black">$0.00 </strong> <el-tag round  type='warning' effect="dark">无配置服务费</el-tag></span> 
										   </div>
										   <div class="font-extraSmall m-t-8" v-if="option.shipmentIds.length>3">可以将库存发往多个位置</div>
										   <div class="font-extraSmall m-t-8" v-else> 可以将库存发往较少位置,亚马逊会分拨库存</div>
								   </RadioCard>
					 </RadioCardGroup>
				   </div>
				   <div>
				   <div class="shipmentlist" v-if="option&&option.shipmentIds" v-loading="shipmentloading" element-loading-text="加载货件信息...">
					   <div class="flex-between">
					   <div class="title-name">货件数量：{{option.shipmentIds.length}}</div>
					   </div>
					   <el-row :gutter="16">
						   <el-col :span="8" v-for="(shipmentid,index)  in option.shipmentIds" :key="index">
							<div class="shipment-card-wrap" v-if="shipments[shipmentid]">
						   <el-card  shadow="false">
							  <template #header>
								<div class="font-base font-bold">
									 货件{{index+1}}
								</div>
							  </template>
							 
								<div class="font-base ship-address-wrap">
									<span class="light-font ">收货地址：</span>
								({{shipments[shipmentid].destination.warehouseId}})
								{{shipments[shipmentid].destination.address.addressLine1}} 
								{{shipments[shipmentid].destination.address.addressLine2}}
								{{shipments[shipmentid].destination.address.city}}
								{{shipments[shipmentid].destination.address.countryCode}}
								{{shipments[shipmentid].destination.address.name}}
								{{shipments[shipmentid].destination.address.postalCode}}
								{{shipments[shipmentid].destination.address.stateOrProvinceCode}}
								&nbsp;&nbsp;<el-tag v-if="shipments[shipmentid].destination.address.area"  >{{shipments[shipmentid].destination.address.area}}</el-tag>
								&nbsp;&nbsp;<el-tag v-if="shipments[shipmentid].destination.address.isfar"  type="danger">偏远</el-tag>
								</div>
								<el-divider  />	
								<div class="shipment-body">
								 <el-row >
								     <el-col :span="24"  >
										 <span class="font-base font-bold">货件内商品</span>  
									</el-col> 
									<el-col :span="24"  >
										 <div class="shipment-data-wrap">
											 <div class="font-base" v-if="shipments[shipmentid].boxinfo&&shipments[shipmentid].boxinfo.boxes">
											 	 <span class="light-font">箱数:</span> 
											 	 {{shipments[shipmentid].boxinfo.boxes.length}}
											 </div>
											 <div   class="font-base">
											 	 <span class="light-font">商品数量:</span>
											 	 {{shipments[shipmentid].skunum}}
											 </div>
											 <div class="font-base">
											 	  <span class="light-font">商品总数:</span>
											 	  {{shipments[shipmentid].totalqty}}
											 </div>
											 <div class="font-base">
										       <span class="light-font">重量:</span>
										       {{shipments[shipmentid].weight}}
											 </div>
											 <div class="font-base">
										        <span class="light-font">体积:</span>
										        {{shipments[shipmentid].volume}}
											 </div>
										 </div>
									</el-col> 
								 </el-row>
								 <div>
								 </div>
								  <el-space wrap>
								 <div v-for="item in shipments[shipmentid].items" class="box-product-img">
									 <img :src="item.skuinfo.image"   />
									 <p class="font-base">x{{item.quantity}}</p>
									 </div>
								 </el-space>
								 <div class="details-btn">
									 <el-link 
									  :underline="false"
									  type="primary" @click="showDetailInfo(shipment)">
										 查看商品详情
									 </el-link>
								</div>
								 
								 </div>
								 <template  #footer>
									 <div   class="shipment-footer-wrap">
									 <span class="font-base text-abb">配送方式:
									 <el-button link :icon="Refresh" @click="generateTransportationOptions" :loading="transportationloading"></el-button></span>
									 <el-select  
									 :loading="transportationloading"
									  style="width: 440px" filterable clearable 
									  @change="handleTransportation(shipments[shipmentid],transportation[shipmentid])"
									  v-model="shipments[shipmentid].transportationOptionId" >
									 												 <el-option 
									 														 v-for="itemtrans in  transportation[shipmentid]" 
									 														 :key="itemtrans.transportationOptionId"
									 														 :label="itemtrans.shippingMode+'---'+itemtrans.carrier.name"
									 														 :value="itemtrans.transportationOptionId">
									 														 <span
									 														   style="
									 															 float: left;
									 															 color: var(--el-text-color-secondary);
									 															 font-size: 12px;
									 														   "
									 														 >
									 														   {{ itemtrans.shippingMode }}
									 														 </span>
									 														   <span style="float: right;font-size: 14px;">{{ itemtrans.carrier.name }}</span>
									 															   
									 												 </el-option>
									 											</el-select>
									 </div>
									 	<el-divider style="margin:12px 0px;"/>
									 <div   class="shipment-footer-wrap">
								   <el-space>
									   <span class="font-base text-abb">物流:</span>
									   <span class="font-extraSmall" v-if="shipments[shipmentid].transinfo">{{shipments[shipmentid].transinfo.company}}</span>
									   <span class="font-extraSmall"  v-if="shipments[shipmentid].transinfo">{{shipments[shipmentid].transinfo.channame}}</span>
									   <el-icon @click="handleShowTrans(shipments[shipmentid])" class="ic-cen" :size="16" color="#666">
									     <Edit />
									   </el-icon>
								   </el-space>
							 
									</div>
								
									
								 </template>
							</el-card>
							</div>   
							</el-col>
					   </el-row>
				  </div>
					   <el-row >
						   <el-col :span="4">
						   		<el-button   style="width:100%" @click="generatePlacementOptions">重新生成方案</el-button>
						   </el-col>
						   <el-col :span="13" :offset="1">
							    <Operation title='确认配送方案'   ref="operationRef"  @change="handleOperationChange"></Operation>
								<Operation title='生成配送方案'   ref="operationRef2" @change="handleOperationChange"></Operation>
								<Operation title="生成物流信息"   ref="operationRef3" @change="handleOperationChange"></Operation>
						   </el-col>
						   <el-col :span="6"  >
							  <div class="text-right">
								  <el-space>
								 <el-button   v-if="planData.auditstatus>=5" style="width:120px" v-loading="shipmentsLoading" @click="getShipments">重新同步货件</el-button>
									  <el-button   v-if="planData.auditstatus>=5" style="width:120px" @click="nextStep">下一步</el-button>
									  <el-button type="primary" :disabled="planData.auditstatus!=4" :loading="submitconfirmloading" v-else style="width:100%" @click="handleShipped">接受费用并确认发货方案</el-button>
								  </el-space>
							  </div>
						  </el-col>
						</el-row>
			</div>
		</div>
		<Footer  ref="footerRef"></Footer>
		<Table ref="tableRef" ></Table>
		<Trans ref="transRef" @change="handleTransChange" :isadd="true"></Trans>
	</div>
</template>

<script setup>
	import { ref,reactive,onMounted,toRefs,computed } from 'vue';
	import { useRoute,useRouter } from 'vue-router';
	import {Edit,Van,ShoppingCart,Printer,Document,ArrowDown,Refresh} from '@element-plus/icons-vue';
	import {Gift} from '@icon-park/vue-next';
	import Footer from "./components/footer.vue";
	import Header from "./components/header.vue";
	import Table  from "./components/table.vue";
	import RadioCard from "@/components/Radio/RadioCard/radio_card.vue";
	import RadioCardGroup from "@/components/Radio/RadioCard/radio_card_group.vue";
	import Operation from "./components/operation.vue";
	import { ElMessage,ElMessageBox } from 'element-plus';
	import addressApi from '@/api/amazon/inbound/addressApi.js';
	import shipmentplanApi from '@/api/erp/shipv2/shipmentplanApi.js';
	import shipmentPlacementApi from '@/api/erp/shipv2/shipmentPlacementApi.js';
	import shipmentPlacementV1Api from '@/api/erp/ship/shipmenthandlingApi.js';
	import Trans from "./components/trans.vue";
	import {dateFormat,formatFloat,getValue,formatPercent,CheckInputIntLGZero,CheckInputFloat} from '@/utils/index.js';
	import shipmentTransportationApi from '@/api/erp/shipv2/shipmentTransportationApi.js';
	let router = useRouter();
	const planid=router.currentRoute.value.query.id;
	const footerRef=ref();
	const headerRef=ref();
	const consumableRef=ref();
	const globalTable=ref();
	const transRef=ref();
	const operationRef=ref();
	const operationRef2=ref();
	const operationRef3=ref();
	const planShippedRef=ref();
	const tableRef=ref();
	let state =reactive({
		planData:{}, 
		shipments:{},
		tableData:{records:[],total:0},
		options:[],
		option:{},
		transportationloading:false,
		transportation:{},
		submitconfirmloading:false,
		deliveryWindowOptions:{},
		paper:{},
		shipall:true,
		shipmentloading:false,
		optionsloading:false,
		placementOptionId:"",
		 shipmentsLoading:false,
	})
	let{planData,tableData,submitconfirmloading,transportation,paper,shipall,deliveryWindowOptions,transportationloading,
	    options,option,placementOptionId,shipments,shipmentloading,optionsloading,shipmentsLoading}=toRefs(state);
		
	function getShipments(){
		state.shipmentsLoading=true;
		var shipmentids=[];
		state.planData.shipmentids.forEach(item=>{
			shipmentids.push(item.shipmentId);
		});
		  shipmentPlacementApi.saveshipments(planid,shipmentids).then((res)=>{
			  state.shipmentsLoading=false;
			 ElMessage.success("已同步成功！"); 
		 })
	}
	
	function nextStep(){
		router.push({
			path:'/e/s/s/three',
			query:{
			  id:planid,
			  title:"发货处理_结束",
			  path:'/e/s/s/three',
			  replace:true
			}
		})	
	}
	function handleTransChange(shipment,planData){
		 state.shipments.forEach(item=>{
			  if(item.shipmentId==shipment.shipmentId){
				 item.channelitem= shipment.channelitem;
				 item.transtypeItem= shipment.transtypeItem;
				 item.companyItem= shipment.companyItem; 
				 item.companyid=item.companyItem.id;
				 item.channelid=item.channelitem.id;
				 item.transtype=item.transtypeItem.id;
				 item.transinfo={"company":item.companyItem.name,"channame":item.channelitem.channame}
			  }
			  if(shipment.allcheck){
				  if(!item.channelitem||item.channelitem.length==0){
					  item.channelitem= shipment.channelitem;
					  item.transtypeItem= shipment.transtypeItem;
					  item.companyItem= shipment.companyItem; 
					  item.companyid=item.companyItem.id;
					  item.channelid=item.channelitem.id;
					  item.transtype=item.transtypeItem.id;
					  item.transinfo={"company":item.companyItem.name,"channame":item.channelitem.channame}
				  }
			  }
		 })
	}
    function showConsumable(ftype){
		consumableRef.value.show(ftype,state.planData);
	}
	function handleShipped(){
		state.submitconfirmloading=true;
		state.planData.placementOptionId=state.placementOptionId;
		shipmentTransportationApi.saveTransportationNeedInfo(state.planData).then((res)=>{
				 shipmentPlacementApi.confirmPlacementOption({"planid":state.planData.id,
				                                              "placementOptionId":state.planData.placementOptionId}
															  ).then((res)=>{
					if(res.data&&res.data.operationid){
						 operationRef.value.show(res.data.operationid);
					} 
				 });
						  
		});
		
		 
	}
	function generatePlacementOptions(){
		shipmentPlacementApi.generatePlacementOptions({"formid":planid}).then(res=>{
			if(res.data.operationid){
				operationRef2.value.show(res.data.operationid);
			}
		})
	}
    function handleShowTrans(shipment){
		transRef.value.show(state.planData,shipment);
	}
	function handleTransportation(shipment,transportation){
		transportation.forEach(item=>{
			if(shipment.transportationOptionId==item.TransportationOptionId){
				shipment.transactionName=item.carrier.name+'----'+item.shippingMode;
			}
		})
		 
	}
	function generateTransportationOptions(){
		state.transportationloading=true;
		shipmentTransportationApi.generateTransportationOptions(state.planData).then((res)=>{
			 if(res.data.operationid){
				operationRef3.value.show(res.data.operationid);
			 }
		});
	}
	
	function handleOperationChange(data){
		state.shipmentloading=false;
		if(data && data.operationStatus=="SUCCESS"&&data.operation=="confirmPlacementOption" ){
			var myshipments=[];
			state.option.shipmentIds.forEach(shipmentid=>{
				myshipments.push(state.shipments[shipmentid]);
			});
			shipmentPlacementApi.shippedInboundPlan(state.planData.id,myshipments).then(res=>{
					state.submitconfirmloading=false;
					loadData();
			})
		}
		else if(data && data.operationStatus=="SUCCESS" && data.operation=="generatePlacementOptions" ){
				listPlacementOptions();
		}else if(data && data.operationStatus=="SUCCESS" && data.operation=="generateTransportationOptions" ){
			  if(state.planData.auditstatus==6||state.planData.auditstatus==5){
				    nextStep();
			  }else{
				   state.transportationloading=true;
				   loadTransportation(state.planData,false);
			  }
			  
		}
		
	}
	function loadTransportation(planData,withOutGenerate,paginationToken){
		shipmentTransportationApi.listTransportationOptions({
		"formid":planData.id,
		"placementOptionId":state.placementOptionId,
		"paginationToken":paginationToken,
		"pageSize":20,
		"shipmentid":''}).then((res)=>{
			if(res&&res.data&&res.data.pagination&&res.data.pagination.nextToken){
				 res.data.transportationOptions.forEach(item=>{
					if(state.transportation[item.shipmentId]&&state.transportation[item.shipmentId].length>0){
						state.transportation[item.shipmentId].push(item);
					}else{
						state.transportation[item.shipmentId]=[item];
					}
					
				 });
				loadTransportation(planData,withOutGenerate,res.data.pagination.nextToken);
			}else{
				 if(state.planData.auditstatus<=8){
					 if(res.data.transportationOptions.length==0&&withOutGenerate!=true&&state.transportationloading==false){
					 	generateTransportationOptions();//获取列表时发现没有任何记录，则主动构造一次，发现构造
					 }
				  }
				res.data.transportationOptions.forEach(item=>{
									if(state.transportation[item.shipmentId]&&state.transportation[item.shipmentId].length>0){
										state.transportation[item.shipmentId].push(item);
									}else{
										state.transportation[item.shipmentId]=[item];
									}
				});
				state.transportationloading=false;
		
				state.planData.shipmentids.forEach(shipmentid=>{
										 if(state.transportation[shipmentid]){
											state.transportation[shipmentid].sort(function(a, b){
												if (a.shippingMode.charAt(0) < b.shippingMode.charAt(0)) {
												    return -1;
												  } else if (a.shippingMode.charAt(0) > b.shippingMode.charAt(0)) {
												    return 1;
												  } else {
												    return 0;
												  }
												  
											} );
										 }
				})
			}
			
		});	
		
	}
	function handlePlacementChange(){
		state.shipmentloading=true;
		var filter_options =state.options.filter((option) => option.placementOptionId == state.placementOptionId);
		if(filter_options&&filter_options.length>0){
		     state.option=filter_options[0];
			 state.planData.placementOptionId=state.placementOptionId;
		     state.planData.shipmentids=state.option.shipmentIds;
		}
		loadShipment();
		if(state.planData.hasSubmitPackage){
		   state.transportation={};
		   loadTransportation(state.planData,false);
		} 
	}
	function showDetailInfo(shipment){
		tableRef.value.show(state.planData,shipment);
	}
	
	
	  function loadShipment(){
		var skuMap={};
		state.planData.itemlist.forEach(item=>{  skuMap[item.sku]=item; });
		if(state.option){
			state.shipments={};
			state.shipmentloading=true;
			state.option.shipmentIds.forEach(shipmentid=>{
				  shipmentPlacementApi.getShipment({"planid":planid,"shipmentId":shipmentid}).then(  res=>{
					var mshipment=res.data;
					if(mshipment.placementOptionId==state.placementOptionId){
						  state.shipments[shipmentid]=mshipment;
							state.shipmentloading=false;
							shipmentPlacementApi.getShipmentItems({"formid":planid,"shipmentid":mshipment.shipmentId}).then(mres=>{
								var total=0;
								var shipment=state.shipments[mres.data.shipmentid];
								if(mres&&mres.data&&mres.data.items&&mres.data.items.items.length>0){
									var data=mres.data.items;
									var weight=0;
									var volume=0;
									for(var i=0;i<data.items.length;i++){
										var item=data.items[i]
										total=total+item.quantity;
										item.skuinfo= skuMap[item.msku];
										weight=weight+item.skuinfo.pkgweight*item.quantity;
										volume=volume+item.skuinfo.skuvolume*item.quantity;
									}
									shipment.skunum=data.items.length;
									shipment.items=data.items;
									shipment.totalqty=total;
									shipment.weight=formatFloat(weight);
									shipment.volume=formatFloat(volume);
								}
							   });
							  shipmentPlacementApi.listShipmentBoxes({"formid":planid,"shipmentid":mshipment.shipmentId}).then(mres=>{
								   var shipment=null;
								   state.shipments[mshipment.shipmentId].boxinfo=mres.data.box;	   
								});
								}
						});
			      });
			
	    }
	}
	
	function listPlacementOptions(){
		state.optionsloading=true;
		shipmentPlacementApi.listPlacementOptions({"formid":planid}).then(res=>{
			state.optionsloading=false;
			if(res.data&&res.data.options&&res.data.options.length>0){
				 state.options=res.data.options;
				 var maxLength=0;
				 for(var i=0;state.options&&i<state.options.length;i++){
					 if(maxLength<state.options[i].shipmentIds.length){
						 maxLength=state.options[i].shipmentIds.length;
						 state.placementOptionId=state.options[i].placementOptionId;
					 }
				 }
				 handlePlacementChange();
			}else{
				if(state.planData.auditstatus==4){
					 generatePlacementOptions();
				}
			
			}
		});
	}
	
	function submitShipDate(){
		
		shipmentplanApi.updatePlanShipDate({"formid":planid,"shipdate":dateFormat(state.planData.shippingDate)}).then((res)=>{
			if(res.data){
				ElMessage.success("发货日期修改成功！"); 
			}
		});
		
	}
	
	function loadData(){
		shipmentplanApi.getPlanInfo({"formid":planid}).then((res)=>{
			if(!res.data.shippingDate){
				const start = new Date()
				start.setTime(start.getTime() + 3600 * 1000 * 24 );
				res.data.shippingDate=start;
			}
			state.planData=res.data;
			state.itemcount=state.planData.itemlist.length;
			state.placementOptionId=state.planData.placementOptionId;
			headerRef.value.show(state.planData,2);
			footerRef.value.show(state.planData);
			if(!state.planData.transtyle){
				state.planData.transtyle="SP";
			}
			if(!state.planData.shippingSolution){
				state.planData.shippingSolution="USE_YOUR_OWN_CARRIER";
			}
			listPlacementOptions();
	 
		});
	}
 
	function downloadLabel(labeltype,shipment){
		var data={};
		data.shipmentid=shipment.shipmentId;
		data.pagetype=shipment.printType;
		data.labeltype=labeltype;
		data.pannum=0;
		shipmentPlacementApi.downLabel(data);
	}
 
 
	 
	
	onMounted(()=>{
		loadData();
	})
</script>
<style scoped>
	.shipment-card-wrap{
		margin-bottom:16px;
	}
	.logistics-details{
		margin-top: 8px;
		margin-left:40px;
		 
	}
	.ship-address-wrap{
		margin:8px 16px;
	}
	.text-black{
		color:#333;
	}
	.three-ship{
		font-size:14px;
	}
	.fee-item{
		 width:100%;
		 padding-bottom:10px;
	}
	.title-name{
		padding-top:16px;
		padding-bottom:8px;
		font-size: 14px;
	}
    .shipment-data-wrap{
		display: flex;
		flex-wrap: wrap;
		flex-grow:1;
		margin-top: 12px;
		margin-bottom:4px;
	}
    .shipment-data-wrap div{
		flex-grow:1;
		margin-right: 16px;
		margin-bottom:8px
	}
	.shipment-body{
		padding-left:16px;
		padding-right:16px;
	}
	.box-product-img img{
		height:48px;
		width: 48px;
		border:1px solid #eee;
		border-radius: 4px;
	}
	.mar-bot{
		margin-bottom:16px;
	}
	.details-btn{
		margin-top: 12px;
		margin-bottom: 12px;
	}
	.shipment-footer-wrap{
		display: flex;
		justify-content: space-between;
		align-items: center;
		cursor: pointer;
	}
	.transportion-wrap{
		display: flex;
		align-items: center;
		white-space: nowrap;
		padding:12px;
		font-size: 14px;
	}
</style>
<style>
	.v-enter-active,
	.v-leave-active {
	  transition: max-height 0.5s ease-out;
	  max-height:100px;
	}
	
	.v-enter-from,
	.v-leave-to {
	 max-height:0px;
	}
	.three-ship .el-card__header {
	    padding:8px 16px;
	    border-bottom: 1px solid var(--el-card-border-color);
	    box-sizing: border-box;
	    background-color: #F8F8F8;
	}
	.shipment-card-wrap .el-card__header{
		padding: 12px 16px;
		background-color: #F8F8F8;
	}
	.dark .three-ship .el-card__header {
	    padding:8px 16px;
	    border-bottom: 1px solid var(--el-card-border-color);
	    box-sizing: border-box;
	    background-color: #070707;
	}
	.dark .shipment-card-wrap .el-card__header{
		padding: 12px 16px;
		background-color: #070707;
	}
	.shipment-card-wrap .el-card__footer{
		padding: 8px 16px;
	}
	
	.placementOption .el-space--horizontal{
		align-items: normal !important;
	}
	.shipmentlist .el-card__body{
		padding:0px;
	}
	
	.box-margin{
		padding:16px;
		min-height:calc(100vh - 36px)
	}
</style>