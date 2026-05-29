<template>
	<div class="box-margin">
		<div class="pag-radius-bor mar-bot" >
			<div class="con-body lylStepTwo" > 
			  <Header ref="headerRef" ></Header>
			  <div class="font-base m-b-8">货件数 {{shipmentNum}}
			  <span class="text-red">  {{notice}}</span>
			  </div>
			  <el-skeleton animated :loading="loading">
			     <template #template> 
					 <el-row :gutter="16">
					<el-col :span="8" v-for="item in 3" :key="item">
			       <el-skeleton-item variant="rect"  style="width: 100%; height: 80px"/>
					  <el-skeleton-item variant="h3"  style="width: 50%;"/>
					  <el-skeleton-item variant="h3"  style="width:90%;"/>
					  </el-col>
					  </el-row>
				</template>
				<template #default>
			  <el-row :gutter="16" v-loading="shipmentLoading">
				  <el-col :span="8" v-for="(item,index) in tableList" :key="index" >
					  <div class="shipment-card-wrap">
					  <el-card shadow="none">
						  <template #header>
                              <div class="font-base font-bold">{{item.shipment.name}}</div>
						  </template>
						  <el-space 
						  class="shipment-message-header"
						  direction="vertical" alignment="normal">
								  <el-space class="font-base">
									  <div style="width:70px" class="light-font" >	货件编码： </div>
									  <div>{{item.shipment.shipmentConfirmationId}}<span class="font-extraSmall" style="padding-left:10px">({{item.shipment.destination}})</span></div>
								  </el-space>
								  <el-space class="font-base">
									  <div style="width:90px" class="light-font">Reference ID:</div>
									  <div>{{item.shipment.referenceid}}</div>
								  </el-space>
								  <el-space class="font-base flex-center ">
									  <div style="width:70px" class="light-font " >	发货地址： </div>
									  <span class="text-omit-1">
									  <span>{{item.fromAddress.name}}</span>,
									  <span>{{item.fromAddress.addressline1}}</span>,
									  <span>{{item.fromAddress.addressline2}}</span>,
									  <span>{{item.fromAddress.city}}</span>,
									  <span>{{item.fromAddress.stateorprovincecode}}</span>,
									  <span>{{item.fromAddress.postalcode}}</span>,
									  <span>{{item.fromAddress.countrycode}}</span>
									  </span>
								  </el-space>
								  <el-space class="font-base flex-center ">
									  <div  style="width:70px" class="light-font " >	收货地址： </div>
									  <span class="text-omit-1">
									  <span>{{item.toAddress.name}}</span>,
									  <span>{{item.toAddress.addressLine1}}</span>,
									  <span>{{item.toAddress.city}}</span>,
									  <span>{{item.toAddress.stateOrProvinceCode}}</span>,
									  <span>{{item.toAddress.postalCode}}</span>,
									  <span>{{item.toAddress.countryCode}}</span>
									  </span>
									  <span v-if="item.toAddress.area" >&nbsp;<el-tag style="padding-left:5px">{{item.toAddress.area}}</el-tag></span> 
									  <span v-if="item.toAddress.area" >&nbsp;<el-tag v-if="item.toAddress.isfar" type="danger">偏远</el-tag> </span>
								  </el-space>
						  </el-space>
						  <div class="shiment-goods-wrap">
							  <div class="s-g-header">
								  <el-space :size="20" >
									  <div style="width:70px">
										  <div class="font-extraSmall">SKU数量</div>
										  <div class="strong-font">{{item.skunum}}</div>
									  </div>
									  <div style="width:70px">
										  <div class="font-extraSmall">商品总数</div>
										  <div class="strong-font">{{item.sumqty}}</div>
									  </div>
									  <div style="width:70px">
										  <div class="font-extraSmall">重量(kg)</div>
										  <div class="strong-font">{{item.totalweight}}</div>
									  </div>
									  <div style="width:70px">
										  <div class="font-extraSmall">尺寸(方)</div>
										  <div class="strong-font">{{item.totalBoxSize}}</div>
									  </div>
									  <div style="width:70px">
									  	<div class="font-extraSmall">材积</div>
										<div class="strong-font">{{item.totalvolume}}</div>
									  </div>
									  <div style="width:70px">
									  	<div class="font-extraSmall">货重比</div>
										<div class="strong-font">{{getVolumeValue(item)}}</div>
									  </div>
								  </el-space>
							  </div>
							  <div class="s-g-body">
								  <div class="shipment-product-img-wrap">
									  <el-space wrap>
										  <img :src="productInfo.image"
										   class="box-product-img"
										   v-for="(productInfo,index) in item.itemlist" :key="index">
									  </el-space>
								  </div>
								 <div 
								 @click="showDetailInfo(item)"
								 class="m-t-8"><el-link type="primary">查看商品详情</el-link> </div>
							  </div>
							  <div class="s-g-footer">
								  <el-row>
									  <el-col :span="24">
										  <el-space fill size="default">
										  <div class="flex-center-between font-base">
											  <span class=" ">装箱：</span>
											  <div>
												  <span class="text-blue pointer"  @click.stop="openShipmentInfo('simple',item.shipment.shipmentid,item.shipment.shipmentConfirmationId)">打印配货单(简洁版)</span>
												 <el-dropdown style="margin-top:5px;margin-left:3px">
													<span class="el-dropdown-link">
													  <el-icon class="el-icon--right">
														<arrow-down />
													  </el-icon>
													</span>
													<template #dropdown>
													  <el-dropdown-menu>
													   <el-dropdown-item @click.stop="openShipmentInfo('detail',item.shipment.shipmentid,item.shipment.shipmentConfirmationId)">打印配货单(详细版)</el-dropdown-item>
													  </el-dropdown-menu>
													</template>
												  </el-dropdown>
											</div>
						 
											  <div  v-if="item.totalBoxNum>0">
												 <el-link  @click="openPackList(item)"  type="primary">
												 	<ad-product class="ic-cen" theme="filled" size="16"/>&nbsp;
												     <div class="font-base">装箱表单（共{{item.totalBoxNum}}箱）</div>
												 </el-link> 
											  </div>
                                             <div 
											 v-else
											 class="flex-center-between pointer"   @click="openPackList(item)" >
                                              <span class=" light-font">去装箱&nbsp;</span>
                                               <el-icon color="#666"  :size="12" class="ic-cen"><CaretRight /></el-icon>
                                              </div>
										 </div>
										 <div class="flex-center-between font-base">
										 		<span >预计到货时间：<span class="font-extraSmall">必填<span class="text-red">*</span></span></span>
										 <el-select v-model="item.shipment.deliveryWindowOptionId" @change="saveTime(item.shipment)">
										 												 <el-option 
										 													v-for="itemtime in item.deliveryWindowOptions" 
										 													 :key="itemtime.deliveryWindowOptionId"
										 													 :label="itemtime.startDate.year+'/'+itemtime.startDate.monthValue+'/'+itemtime.startDate.dayOfMonth+' - '+itemtime.endDate.year+'/'+itemtime.endDate.monthValue+'/'+itemtime.endDate.dayOfMonth"
										 													 :value="itemtime.deliveryWindowOptionId">
										 												 </el-option>
										 </el-select>
										 										  </div>
									  <div class="flex-center-between font-base" >
										  <span>承运人
										   <el-select :disabled="item.plan.auditstatus!=6" v-model="item.shipment.carrier" placeholder="请选择承运人" style="width: 240px">
											  <el-option  key="USE_YOUR_OWN_CARRIER" label="其他承运人" value="USE_YOUR_OWN_CARRIER" />
											  <el-option  key="AMAZON_PARTNERED_CARRIER" label="亚马逊合作伙伴" value="AMAZON_PARTNERED_CARRIER" />
											  <el-option  key="" label="空" value="" />
											</el-select>
										  </span>
										  <span>
											  货件类型
											<el-select :disabled="item.plan.auditstatus!=6"  v-model="item.shipment.transtyle" placeholder="货件类型" style="width: 240px">
											  <el-option  key="SP" label="小包裹快递(SP)" value="SP" />
											  <el-option  key="LTL" label="汽运零担(LTL)" value="LTL" />
											  <el-option  key="" label="空" value="" />
											</el-select>
										  </span>
									  </div>
										 <div class="flex-center-between font-base">
										 	<span >配送方式：<el-button link v-if="item.shipment.loading" :loading="true"></el-button></span>
											<div class="flex-center-between"> 
											      <el-select :disabled="item.plan.auditstatus!=6"  :loading="item.shipment.loading" 
												   style="width: 440px" filterable clearable 
												   @change="saveDetail(item.shipment,transportation[item.shipment.shipmentid])"
												   v-model="item.shipment.transportationOptionId" >
															 <el-option 
																	 v-for="itemtrans in filterTransportation(item.shipment,transportation[item.shipment.shipmentid])" 
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
										  </div>
										
										  <div class="flex-center-between font-base"  >
											  <span >物流：</span>
											  <div >
											 <span v-if="item.transinfo"> {{item.transinfo.company}}-{{item.transinfo.channame}} </span> <el-icon class="pointer" @click.stop="showTransEdit(item.detail,item)"><Edit ></Edit></el-icon></div>
												</div>
										 </el-space>
									  </el-col>
								  </el-row>
							  </div>
						  </div>
						  <template #footer>
						 <el-space>
						 <span class="font-base">打印标签</span>
					   <el-select :disabled="item.plan.auditstatus<7||item.plan.auditstatus>11||item.plan.auditstatus>11||!item.shipment.deliveryWindowOptionId" v-model="item.shipment.printType">
								<template v-if="item.plan.transtyle==='SP'">
														<el-option  
														v-for="(value,key) in item.pkgpaper" 
														:key="key"
														:value="key" 
														:label="value"
														></el-option>	
								</template>
								<template v-else>
								<el-option  v-for="(value,key) in item.pkgpaperltl" 
														:key="key"
														:value="key" 
														:label="value"
														></el-option>	
							  </template>
							</el-select>
							
						 <el-dropdown>
						    <el-button type="primary" 
							:disabled="item.plan.auditstatus<7||item.plan.auditstatus>11||item.plan.auditstatus>11||!item.shipment.deliveryWindowOptionId"> 
							下载&nbsp;
							 <el-icon >
							   <arrow-down />
							 </el-icon>
							 </el-button>
						   <template #dropdown>
							<el-dropdown-menu>
									 <el-dropdown-item @click="downloadLabel('SELLER_LABEL',item.shipment)">箱子标签</el-dropdown-item>
									 <el-dropdown-item @click="downloadLabel('BARCODE_2D',item.shipment)">2D条形码</el-dropdown-item>
									 <el-dropdown-item @click="downloadLabel('PALLET',item.shipment)">托盘标签</el-dropdown-item>
							</el-dropdown-menu>
						   </template>
						 </el-dropdown> 
						 <div v-if="item.shipment.status==4" class="font-extraSmall">未下载</div>
						 <div v-else-if="item.shipment.status>4"  class="font-extraSmall">已下载</div>
							  </el-space>
						  </template>
					  </el-card>
					  </div>
				  </el-col>
			  </el-row>
			  <el-row>
				  <el-col :span="20"><Operation ref="operationRef" @change="handleOperationChange"></Operation></el-col>
				  <el-col :span="4" >
					   <div class="allstate-wrap">
						  <div class="flex-center-between font-base">
							  <span>是否完成装箱</span>
							  <el-icon size="16" v-if="planData.auditstatus>5&&planData.auditstatus<10" color="#ff7315"><CircleCheckFilled /></el-icon>
							  <el-icon size="16" v-else color="#999"><CircleCloseFilled /></el-icon>
						  </div>
						  <div class="flex-center-between font-base">
							  <span>是否完成配送信息选择</span>
							<el-icon size="16" v-if="planData.auditstatus>6&&planData.auditstatus<10" color="#ff7315"><CircleCheckFilled /></el-icon>
							<el-icon size="16" v-else color="#999"><CircleCloseFilled /></el-icon>
						  </div>
						  <div class="flex-center-between font-base">
								  <span>发货出库</span>
								 <el-icon  @click="handleRefresh" v-loading="planData.refreshloading" class='pointer'
								 size="16" v-if="planData.auditstatus>7&&planData.auditstatus<10" color="#ff7315"><CircleCheckFilled /></el-icon>
								 <el-icon size="16" v-else color="#999"><CircleCloseFilled /></el-icon>
						  </div>
						  </div>
				  </el-col>
				  <el-col :span="12" >
					  <el-space>
						  <el-button    @click="generateDeliveryWindowOptionsAll()">重新生成预计到货时间</el-button>
						   <el-button    @click="generateTransportationOptions()">重新生成物流信息</el-button>
						 <!-- <el-button  v-if="planData.auditstatus>=8"  @click="showConsumable('isshow')">查看耗材出库</el-button> -->
						  <el-button    @click="showConsumable('isopt')">耗材出库</el-button>
						 
					  </el-space>
					    </el-col>
						  <el-col :span="12" >
					  <div class="text-right ">
					   <el-button type="primary" v-if="planData.auditstatus==7" 
					   :loading="submitloading" 
					   @click.stop="doneAllShipment">发货出库</el-button>
					  <el-button type="primary"
					    @click.stop="submitDetail('submit')"
						:loading="submitloading" 
						 v-else-if="planData.auditstatus==6"
					    >提交配送信息</el-button>
					  <el-button type="primary"
					    v-else-if="planData.auditstatus==5"
						:loading="submitloading" 
					   @click.stop="submitBox('submit')"
					   >提交全部装箱</el-button>
					   <el-button v-else @click.stop="toNext()">下一步</el-button>
					  </div>
				  </el-col>
			  </el-row>
			  </template>
			  </el-skeleton> 
			</div>
		</div>
		<Footer  ref="footerRef"></Footer>
	</div>
	<Consumable  ref="consumableRef"  ></Consumable>
	<!-- <Trans ref="transRef" @change="loadTable" ></Trans> -->
	
	<el-dialog
	title="物流信息"
	v-model="transVisiable"
	top="2vh"
	class="transClz"
	width="80%"
	>
	<div class="con-body"  >
		<TransCompany ref="transRef" @stepdata="stepdataChange"   onlyTrans="true" ></TransCompany>
	</div>
	</el-dialog>
	
	<Table ref="tableDialogRef" ></Table>
	 <el-dialog
	 title="装箱"
	 v-model="boxVisiable"
	 width="80%"
	 top="1vh"
	 >
	 <div class="con-body"  >
	 <div style="width:100%;overflow-x: auto;margin-bottom:10px;">
	   <ShipBoxCase ref="shipBoxCaseRef" v-if="planData.areCasesRequired" @change="handleBoxSaveChange"></ShipBoxCase>
	   <ShipBox ref="shipBoxRef" v-else @change="handleBoxSaveChange"></ShipBox>
	 </div>
	 </div>
	 
	 </el-dialog>
	</template>

<script setup>
	import { ref,reactive,onMounted,toRefs,nextTick } from 'vue';
	import { useRoute,useRouter } from 'vue-router';
	import {CircleCheckFilled,Van,CaretRight,CircleCloseFilled,ArrowDown,Edit} from '@element-plus/icons-vue';
	import {AdProduct} from '@icon-park/vue-next';
	import Footer from "./components/footer.vue";
	import Header from "./components/header.vue";
	import Operation from "./components/operation.vue";
	import Consumable from "./components/consumable.vue";
	import TransCompany from "@/views/erp/shipv2/shipment_handing/shipstep/components/three_deliver.vue";;
	import ShipBox from "@/views/erp/shipv2/shipment_handing/shipstep/components/ship_box.vue";
	import ShipBoxCase from "@/views/erp/shipv2/shipment_handing/shipstep/components/ship_box_case.vue";
	import { ElMessage,ElMessageBox } from 'element-plus';
	import {dateFormat,dateTimesFormat,formatFloat} from '@/utils/index.js';
	import addressApi from '@/api/amazon/inbound/addressApi.js';
	import shipmentplanApi from '@/api/erp/shipv2/shipmentplanApi.js';
	import shipmentBoxApi from '@/api/erp/shipv2/shipmentBoxApi.js';
	import shipmentTransportationApi from '@/api/erp/shipv2/shipmentTransportationApi.js';
	import Table  from "./components/table.vue";
	import Trans from "@/views/erp/shipv2/shipment_add/shipstep/components/trans.vue";
	import shipmentPlacementApi from '@/api/erp/shipv2/shipmentPlacementApi.js';
	import shipmentQuotaApi from '@/api/erp/shipv2/shipmentQuotaApi.js';
	let router = useRouter();
	const planid=router.currentRoute.value.query.id;
	const footerRef=ref();
	const headerRef=ref();
	const shipBoxRef=ref();
	const shipBoxCaseRef=ref();
	const consumableRef=ref();
	const transRef=ref();
	const tableDialogRef=ref();
	const globalTable=ref();
	const operationRef=ref();
	let state =reactive({
		planData:{}, 
		tableList:[],
		boxVisiable:false,
		transVisiable:false,
		transportationloading:false,
		transportation:{},
		step:0,
		loading:true,
		itemcount:0,
		placementOptionId:'',
		shipmentNum:0,
		addressVisiable:false,
		operatorVisiable:false,
		operatorRow:null,
		submitloading:false,
		btnStatus:{
			"submitForm":false,
			"submitStep":false,
			"number":1,
		},
		groupList:[],
		params:{},
		shipmentLoading:false,
		shipmentids:null,
		shipmentLoadNum:1,
		notice:'',
	})
	const{
	planData,
	step,
	loading,
	tableList,
	itemcount,
	transportation,
	boxVisiable,
	transportationloading,
	shipmentNum,
	addressVisiable,
	operatorVisiable,
	shipmentLoading,
	transVisiable,
	operatorRow,
	btnStatus,
	params,
	submitloading,
	placementOptionId,
	shipmentids,
	shipmentLoadNum,
	notice,
	groupList}=toRefs(state);
	
	function stepChange(val){
		state.step=val;
	}
	function toNext(){
		router.push({
			path:'/e/s/s/end',
			query:{
			  id:state.planData.id,
			  title:"发货处理_结束",
			  path:'/e/s/s/end',
			  replace:true
			}
		})	
	}
	function doneAllShipment(){
		 ElMessageBox.confirm(
		    '您确认要提交发货吗?',
		    'Warning',
		    {
		      confirmButtonText: '确定',
		      cancelButtonText: '取消',
		      type: 'warning',
		    }
		  )
		    .then(() => {
				if(state.submitloading==false){
					state.submitloading=true;
					if(state.planData.invstatus=='1'&&state.planData.invtype!='2'){
										getInvParam();
					}else{
										confirmShipemnt();
					}
				}
				 
		    })
		    .catch(() => {
		      ElMessage({
		        type: 'info',
		        message: '取消操作',
		      })
		    })
	}
	function getInvParam(){
		shipmentplanApi.getInvParam({"id":state.planData.id}).then((res)=>{
			    state.invparam=res.data;
				outShipInventory(state.invparam);
		});
	}
	function outShipInventory(params){
		shipmentplanApi.outShipInventory(params).then((res)=>{
				setInvStatus();
		});
	}
	function setInvStatus(){
		shipmentplanApi.setInvStatus({"id":state.planData.id,"status":2}).then((res)=>{
			if(state.planData.invstatus=='1'){
				confirmShipemnt();
			}
		});
		}
	async function confirmShipemnt(){
		//保存物流跟踪
		 for(var i=0;i<state.tableList.length;i++){
			 var shipmentDetail=state.tableList[i];
			 var data={};
			 var boxinfos = [];
			 state.submitloading=true;
			 shipmentDetail.listbox.forEach(function(item, index) {
			 	var trackinginfo = {};
			 	trackinginfo.boxnum = item.boxnum;
			 	trackinginfo.id=item.id;
			 	trackinginfo.value = item.tracking_id!=null?item.tracking_id:"";
			 	boxinfos.push(trackinginfo);
			 })
			 data.boxinfo = boxinfos;
			 data.shipmentid = shipmentDetail.shipment.shipmentid;
			 data.transtype=shipmentDetail.shipment.tranType;
			 data.actiontype="Shipped";
			await shipmentPlacementApi.saveTransTrace(data).then(res => {
			 	state.submitloading=false;
			 	if(res.data && res.data.operationid){
			 		operationRef.value.show(res.data.operationid,0);
					map[res.data.operationid]= data.shipmentid;
			 	}
				
			 }).catch(e=>{
			 	state.submitloading=false;
			 })
		 }
		 
		 await shipmentplanApi.donePlan({"formid":state.planData.id}).then((res)=>{
		         loadData();
		  });
		 
	}
	function showTransEdit(row,shipment){
		// if(shipment.transchannel){
		// 	row.tranType=shipment.transchannel.tranType;
		// }
		// shipment.plan.reallyShipmentid=shipment.shipment.shipmentConfirmationId;
		// row.marketplaceid=shipment.plan.marketplaceid;
		// row.reallyShipmentid=shipment.shipment.shipmentConfirmationId;
		// transRef.value.show(row,shipment.plan);
		 state.transVisiable=true;
		 nextTick(()=>{
			transRef.value.loadOptData(shipment);
		 })
	}
	function showConsumable(ftype){
		consumableRef.value.show(ftype,state.planData);
	}
	var map={};
	function handleOperationChange(data){
		 if(data && data.operationStatus=="SUCCESS" && data.operation=="generateDeliveryWindowOptions" ){
				state.tableList.forEach(shipmentDetails=>{
				    listDeliveryWindowOptions(shipmentDetails,true);
				})
		}else if(data && data.operationStatus=="SUCCESS" && data.operation=="generateTransportationOptions" ){
			   //state.transportationloading=true;
			   state.transportation={};
			   state.tableList.forEach(shipmentDetails=>{
			        loadTransportation(shipmentDetails,true);
			   })
		} else if(data && data.operationStatus=="SUCCESS" && data.operation=="confirmTransportationOptions" ){
			 shipmentplanApi.confirmTransportation({"formid":planid}).then((res)=>{
			 	 loadData();
			 });
		}else if(data && data.operationStatus=="SUCCESS" && data.operation=="updateShipmentTrackingDetails" ){
			var shipmentid=map[data.operationid] ;
			refreshStatus(shipmentid);
			 
		}else if(data && data.operation=="setPackingInformation"){
			boxShipment()
		}
	}
	function refreshStatus(shipmentid){
		shipmentPlacementApi.saveshipment({"shipid":shipmentid}).then((res)=>{
				ElMessage.success("当前提交货件同步成功！"); 
		})
	}
	function generateTransportationOptions(){
		shipmentTransportationApi.generateTransportationOptions(state.planData).then((res)=>{
			 if(res.data.operationid){
				operationRef.value.show(res.data.operationid,1);
			 }
		});
	}
	
	function generateDeliveryWindowOptions(shipmentDetails){
		var param={"formid":shipmentDetails.plan.id,"shipmentId":shipmentDetails.shipment.shipmentid};
		  shipmentPlacementApi.generateDeliveryWindowOptions(param).then(res=>{
			if(res.data.operationid){
				operationRef.value.show(res.data.operationid);
			}
		});
	}
	function listDeliveryWindowOptions(shipmentDetails,withoutGenerate){
		var param={"formid":shipmentDetails.plan.id,"shipmentid":shipmentDetails.shipment.shipmentid};
		shipmentPlacementApi.listDeliveryWindowOptions(param).then(res=>{
				if(res&&res.data&&res.data.deliveryWindowOptions&&res.data.deliveryWindowOptions.length>0){
					shipmentDetails.deliveryWindowOptions = res.data.deliveryWindowOptions;
					shipmentDetails.deliveryWindowOptions.sort((a, b)=>{
						if(a.startDate.year!=b.startDate.year){
							return a.startDate.year-b.startDate.year;
						}
						if(a.startDate.monthValue!=b.startDate.monthValue){
							return a.startDate.monthValue-b.startDate.monthValue;
						}
						if(a.startDate.dayOfMonth!=b.startDate.dayOfMonth){
							return a.startDate.dayOfMonth-b.startDate.dayOfMonth;
						}
						return 0;
					})
				}else {
					if(withoutGenerate!=true){
						generateDeliveryWindowOptions(shipmentDetails);
					}
				}
			});
	}
	 function loadTable(){
	   state.params.planid = planid;
		    shipmentPlacementApi.getBaseInfoByPlan(state.params).then(res=>{
		   			 if(res&&res.data){
		   				 state.tableList=res.data;
						 state.shipmentNum = res.data.length;
						 if(state.shipmentids && state.shipmentNum!=state.shipmentids.length){
							 state.notice="(货件数不匹配,系统尝试第"+state.shipmentLoadNum+"次加载！)";
							 if(state.shipmentLoadNum<8){
								 var timer=setTimeout(function(){
									 loadData();
									 state.shipmentLoadNum++;
								 },3000);
							 }else{
								 state.notice="(尝试失败,请等待系统自动同步货件中......)";
								 getShipments();
							 }
						 }
		   				 state.loading = false;
						 if(res.data[0] && res.data[0].shipment){
							  state.placementOptionId = res.data[0].shipment.placementOptionId;
						 }
						 state.transportation={};
						 //state.transportationloading=true;
						 state.tableList.forEach(shipmentDetails=>{
							 if(!shipmentDetails.shipment.transtyle){
								 shipmentDetails.shipment.transtyle="SP";
							 }
							 if(!shipmentDetails.shipment.carrier){
							 	shipmentDetails.shipment.carrier="USE_YOUR_OWN_CARRIER";
							 }
							   
								 listDeliveryWindowOptions(shipmentDetails);
								 loadTransportation(shipmentDetails,false);
						 })
						 
		   			 }
		   		 })
	
	}
	function getShipments(){
		state.shipmentLoading=true;
		  shipmentPlacementApi.saveshipments(planid,state.shipmentids).then((res)=>{
			 state.shipmentLoading=false;
			 state.notice="";
			 ElMessage.success("已同步货件成功！"); 
			  loadData();
		 })
	}
	
	//预计到货时间保存
	function saveTime(itemdata){
		var data={"formid":itemdata.formid,"deliveryWindowOptionId":itemdata.deliveryWindowOptionId ,"shipmentid":itemdata.shipmentid}
		shipmentPlacementApi.confirmDeliveryWindowOptions(data).then(res=>{
			if(res.data&&res.data.operationid){
				 operationRef.value.show(res.data.operationid,null,null,"预估时间");
			}
		}).catch(()=>{
 
		});
	}
	function loadTransportation(shipmentDetails,withOutGenerate,paginationToken){
		shipmentDetails.shipment.loading=true;
		shipmentTransportationApi.listTransportationOptions({
		"formid":planid,
		"placementOptionId":state.placementOptionId,
		"paginationToken":paginationToken,
		"pageSize":20,
		"shipmentid":shipmentDetails.shipment.shipmentid}).then((res)=>{
			if(res&&res.data&&res.data.pagination&&res.data.pagination.nextToken){
				 res.data.transportationOptions.forEach(item=>{
					if(state.transportation[item.shipmentId]&&state.transportation[item.shipmentId].length>0){
						state.transportation[item.shipmentId].push(item);
					}else{
						state.transportation[item.shipmentId]=[item];
					}
					
				 });
				loadTransportation(shipmentDetails,withOutGenerate,res.data.pagination.nextToken);
			}else{
				 if(state.planData.auditstatus<=8){
					 if(res.data.transportationOptions.length==0&&withOutGenerate!=true){
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
				shipmentDetails.shipment.loading=false;
				state.tableList.forEach(shipmentDetails=>{
										 if(state.transportation[shipmentDetails.shipment.shipmentid]){
											state.transportation[shipmentDetails.shipment.shipmentid].sort(function(a, b){
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
	function downloadLabel(labeltype,shipment){
		var data={};
		data.shipmentid=shipment.shipmentid;
		data.pagetype=shipment.printType;
		data.labeltype=labeltype;
		data.pannum=0;
		shipmentPlacementApi.downLabel(data,()=>{
			loadTable();
		});
	}
	
	function getFilterShipingSolutionList(list,carrier){
		if(carrier&&list&&list.length>0){
			var result=[];
			list.forEach(item=>{
						  if(item.shippingSolution==carrier){
							  result.push(item);
						  } 
			});
			return result;
		}else{
			return list;
		}
		
	}
	function getFilterShippingModeList(list,shippingMode){
		if(shippingMode&&list&&list.length>0){
			var result=[];
			  list.forEach(item=>{
				  if(item.shippingMode==shippingMode){
					  result.push(item);
				  } 
			  });
			  return result;
	  }else{
		  return list;
	  }
	}
	function filterTransportation(shipment,trans){
		var shippingMode=null;
		  if(shipment.transtyle=="LTL"){
			  shippingMode="FREIGHT_LTL";
		  }else if(shipment.transtyle=="SP"){
			    shippingMode="GROUND_SMALL_PARCEL";
		  }
		if(shipment.carrier){
		      trans=getFilterShipingSolutionList(trans,shipment.carrier)
			  if(shippingMode){
				trans=getFilterShippingModeList(trans,shippingMode);
			  }
		}else if(shippingMode){
			trans=getFilterShippingModeList(trans,shippingMode);
		}
		return trans;
	}
	function loadData(){
		shipmentplanApi.getPlanInfo({"formid":planid}).then((res)=>{
			state.planData=res.data;
			if(res.data.shipments){
				state.shipmentids=res.data.shipments.split(",");
			}
			state.itemcount=state.planData.itemlist.length;
			headerRef.value.show(state.planData,3);
			footerRef.value.show(state.planData);
			loadTable();
		});
	}
	function handleBoxSaveChange(){
		 state.boxVisiable=false;
		 loadTable();
	}
 
    function openShipmentInfo(ftype,shipmentid,name){
		var shipmentids=[];
		shipmentids.push(shipmentid);
		var ftypename="";
		if(ftype=="detail"){
			ftypename="-详细版";
		}else{
			ftypename="-简单版";
		}
		shipmentQuotaApi.downPDFShipmentForm(ftype,shipmentids,name+ftypename+"-配货单");
	}
	 function openPackList(groupInfo){
		 state.boxVisiable=true;
		 setTimeout(function(){
			 if(state.planData.areCasesRequired){
				 shipBoxCaseRef.value.loadOptData(groupInfo);
			 }else{
				 shipBoxRef.value.loadOptData(groupInfo);
			 }
		 },300);
	 }
	function showDetailInfo(shipment){
		var obj = {};
		var arr =[]
		shipment.itemlist.forEach((item,index)=>{
			obj.items = [];
			obj.items.skuinfo = item;
			obj.items.quantity = item.quantity;
			arr[index]=obj.items;
		})
		obj.items = arr;
		tableDialogRef.value.show(state.planData,obj);
	}
	
	//配送信息提交
			function submitDetail(){
				state.submitloading=true;
				shipmentTransportationApi.confirmTransportationOptionsByForm(state.planData.id).then(res=>{
					state.submitloading=false;
					if(res.data&&res.data.operationid){
						 operationRef.value.show(res.data.operationid,null,null,'配送信息');
					}
				}).catch(()=>{
					state.submitloading=false;
				});
			}
	//配送信息保存
	 function saveDetail(shipment,transaction){
			var transactionName="";
			transaction.forEach(itemtrans=>{
				if(itemtrans.transportationOptionId==shipment.transportationOptionId){
					transactionName=itemtrans.shippingMode+'---'+itemtrans.carrier.name;
				}
			})
	 		shipmentTransportationApi.saveConfirmTransportationOptionsInfo({
	 			"shipmentid":shipment.shipmentid,
	 			"transportationOptionId":shipment.transportationOptionId,
	 			"transactionName":transactionName
	 			}).then(res=>{
	 			if(res.data){
	 				ElMessage.success("配送信息保存成功！");
	 			}
	 		}).catch(()=>{
	 		});
	 }
	 function generateDeliveryWindowOptionsAll(){
		 state.tableList.forEach(shipmentDetails=>{
		     generateDeliveryWindowOptions(shipmentDetails);
		 })
	 }
	 function boxShipment(){
		 state.planData.shipmentids=[];
		 state.tableList.forEach(shipmentDetails=>{
			 state.planData.shipmentids.push(shipmentDetails.shipment.shipmentid);
		 	});
	 	shipmentPlacementApi.boxShipment(state.planData.id,state.planData.shipmentids).then((res)=>{
			loadData();
	 		generateTransportationOptions();
			state.tableList.forEach(shipmentDetails=>{
			    generateDeliveryWindowOptions(shipmentDetails);
			})
	 	});
	 }
	 //提交装箱信息
	 function submitBox(){
	 		 //集体提交装箱信息
	 		 ElMessageBox.confirm(
	 		    '请确认是否填写完成所有货件的装箱信息？',
	 		    '确认提交装箱',
	 		    {
	 		      confirmButtonText: '确认',
	 		      cancelButtonText: '取消',
	 		      type: 'warning',
	 		    }
	 		  )
	 		    .then(() => {
	 					state.submitloading=true;
	 					shipmentBoxApi.submitPackingInformation({"formid":state.planData.id}).then((res)=>{
	 						state.submitloading=false;
	 						ElMessage.success("已提交装箱信息！"); 
	 						if(res.data && res.data.operationid){
	 							operationRef.value.show(res.data.operationid,0);
	 						}
	 					}).catch(e=>{
	 						state.submitloading=false;
	 					});
	 		      
	 		    })
	 		
	 		 
	 }
	 
	 function stepdataChange(step,shipmentid){
		 shipmentPlacementApi.getBaseInfo({
		 	"shipmentid":shipmentid
		 }).then(res=>{
			 transRef.value.loadOptData(res.data);
		 })
		 loadTable();
	 }
	 
	 function handleRefresh(){
		 state.planData.refreshloading=true;
		 shipmentplanApi.refreshInboundPlan({"formid":state.planData.id}).then((res)=>{
		 	 ElMessage.success("已成功！"); 
			 state.planData.refreshloading=false;
		 })
	 }
	 
	 function getVolumeValue(row){
		 if(row.totalBoxSize>0){
			return formatFloat(row.totalweight/row.totalBoxSize);
		 }else{
			 return "-";
		 }
	 }
	  
	onMounted(()=>{
		loadData();
	})
</script>
<style scoped>
	.allstate-wrap div{
		margin-bottom:12px;
	}
	.shipment-message-header{
		padding-right: 16px;
		padding-left:16px;
	}
	.shiment-goods-wrap .s-g-header{
		padding:8px 16px;
		border-bottom:1px solid #eee;
		border-top:1px solid #eee;
	
	}
	.strong-font{
		font-size:14px;
		font-weight: 600;
	}
	.s-g-body{
		padding-right: 16px;
		padding-left:16px;
		padding-top: 12px;
	}
	.box-margin{
		padding:16px;
	}
	.box-product-img{
		height:48px;
		width: 48px;
		border:1px solid #eee;
		border-radius: 4px;
	}
	.mar-bot{
		margin-bottom: 16px;
	}
	.s-g-footer{
		padding-top: 16px;
		padding-right: 16px;
		padding-left:16px
	}
	.lylStepTwo .shipment-card-wrap{
		margin-bottom: 16px;
	}
</style>
<style>
	.lylStepTwo .shipment-card-wrap .el-card__header {
	    padding: 12px 16px;
		background-color: #f8f8f8;
	}
	.lylStepTwo .shipment-card-wrap .el-card__body{
	    padding: 12px 0px;
	}
	.lylStepTwo .shipment-card-wrap .el-card__footer{
	    padding: 8px 16px;
		background-color: #f8f8f8;
	}
	.dark .lylStepTwo .shipment-card-wrap .el-card__header {
	    padding: 12px 16px;
		background-color: #070707;
	}
 
	.dark .lylStepTwo .shipment-card-wrap .el-card__footer{
	    padding: 8px 16px;
		background-color: #060606;
	}
	.transClz .el-dialog__body{
		padding:5px;
	}

</style>