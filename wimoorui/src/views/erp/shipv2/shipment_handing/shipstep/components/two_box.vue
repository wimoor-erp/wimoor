<template>
	<div class="box-ship">
			<el-row>
				<el-col :span="6" >
					      <el-form :model="form" label-position="top"  :inline="true" >
								<el-form-item label="预计到货时间" prop="carrier">
									<el-space>
									<el-select v-model="planData.shipment.deliveryWindowOptionId" @change="saveTime()">
										 <el-option 
											v-for="item in deliveryWindowOptions" 
											 :key="item.deliveryWindowOptionId"
											 :label="item.startDate.year+'/'+item.startDate.monthValue+'/'+item.startDate.dayOfMonth+'-'+item.endDate.year+'/'+item.endDate.monthValue+'/'+item.endDate.dayOfMonth"
											 :value="item.deliveryWindowOptionId">
										 </el-option>
									</el-select>
									<el-icon 
									size="16"
									 :loading="submitloading"
									color="#16bc31"
									v-if="!loadingDeliverWindow"
									><SuccessFilled /></el-icon></el-space>
								</el-form-item>
					       </el-form>
					</el-col>
							<el-col :span="6">
							<div class="m-t-8 m-b-8 font-extraSmall">打印箱子标签</div>
						<el-space >
							  <el-button 
								  @click="downloadBoxInfo">下载装箱详情
							  </el-button>
							 <el-select v-model="boxmarksType" class="m-2 myshipdrop"  placeholder="选择打印箱子标签类型">
							    <el-option
							      v-for="item in boxMarks"
							      :key="item.value"
							      :label="item.label"
							      :value="item.value"
							   />
							   </el-select>
							   <el-dropdown trigger="click" >
							     <el-button type="primary" :disabled="disabledBtn"   >
							                打印箱子标签<el-icon ><arrow-down /></el-icon>
							     </el-button>
							     <template #dropdown>
							       <el-dropdown-menu >
									 <el-dropdown-item @click="downloadLabel('SELLER_LABEL')">下载箱子标签</el-dropdown-item>
									 <el-dropdown-item @click="downloadLabel('BARCODE_2D')">下载2D条形码</el-dropdown-item>
									 <el-dropdown-item @click="downloadLabel('PALLET')">下载托盘标签</el-dropdown-item>
							       </el-dropdown-menu>
							     </template>
							   </el-dropdown>
						</el-space>
						</el-col>
						<el-col :span="6" :offset="6">
							<div class="m-t-8 m-b-8 font-extraSmall">打印配货单</div>
							  <el-dropdown split-button type="primary"  @click.stop="openShipmentInfo('simple',planData.shipment.shipmentid,planData.shipment.shipmentConfirmationId)" >
							      打印配货单(简洁版)
							      <template #dropdown>
							        <el-dropdown-menu>
										<el-dropdown-item @click.stop="openShipmentInfo('detail',planData.shipment.shipmentid,planData.shipment.shipmentConfirmationId)">打印配货单(详细版)</el-dropdown-item>
							        </el-dropdown-menu>
							      </template>
							    </el-dropdown>
						</el-col>
			</el-row>
			  <el-divider  />
			  <div class="box-list-title">
				<h4>装箱列表</h4>
				</div>
		<div class="con-body"  >
			<div style="width:100%;overflow-x: auto;margin-bottom:10px;">
			   <ShipBoxCase ref="shipBoxCaseRef"  v-if="planData?.plan?.areCasesRequired" :isview="true"></ShipBoxCase>
			   <ShipBox ref="shipBoxRef" v-else :isview="true"></ShipBox>
			</div>
		</div>
	
	  
</div>
	  
</template>

<script setup>
	import {h, ref ,watch,reactive,onMounted,computed,nextTick,toRefs} from 'vue'
	import {Search,ArrowDown,Close,SuccessFilled} from '@element-plus/icons-vue'
	import { ElDivider,ElMessageBox ,ElMessage } from 'element-plus'
	import '@/assets/css/packbox_table.css'
	import materialApi from '@/api/erp/material/materialApi.js';
	import feedApi from '@/api/amazon/feed/feedApi.js';
	import ShipBox from "@/views/erp/shipv2/shipment_handing/shipstep/components/ship_box.vue";
	import ShipBoxCase from "@/views/erp/shipv2/shipment_handing/shipstep/components/ship_box_case.vue";
	import shipmenthandlingApi from '@/api/erp/ship/shipmenthandlingApi.js';
	import shipmentPlacementApi from '@/api/erp/shipv2/shipmentPlacementApi.js';
	import shipmentBoxApi from '@/api/erp/shipv2/shipmentBoxApi.js';
	import shipmentTransportationApi from '@/api/erp/shipv2/shipmentTransportationApi.js';
	import shipmentQuotaApi from '@/api/erp/shipv2/shipmentQuotaApi.js';
	import ShipmentOpt from"./shipment_operator.vue"
	import RadioCard from "@/components/Radio/RadioCard/radio_card.vue";
	import RadioCardGroup from "@/components/Radio/RadioCard/radio_card_group.vue";
	import Operation from "@/views/erp/shipv2/shipment_add/shipstep/components/operation.vue";
	import {formatFloat,CheckInputIntLGZero,CheckInputFloat} from '@/utils/index.js';
	import {pointKeyChnage} from '@/utils/jquery/key/point-key';
	import boxMarks from '@/model/erp/ship/boxMarks.json';
    const emit = defineEmits(['stepdata',"carrData","change"]);
	
	let state =reactive({
		planData:{shipment:{}},
		transportation:[],
		deliveryWindowOptions:[],
		infoData:{shipment:{}},
		boxDetail:{},
		totalBoxNum:0,
		submitloading:false,
		carrierData:[],
		boxDisable:false,
		inputboxNum:"",
		loadingDeliverWindow:false,
		loadingTransportation:false,
		boxWeightData:[],
		titles:['箱子信息','配送信息生成','预计到货日期生成','提交预计到货日期'],
		tableData:[],
		boxListData:[{boxlength:'',boxwidth:'',boxheight:'',boxcheck:[]}],
		form:{
			selectedTransportationOptionId:null,
			deliveryWindowOptionId:null,
			tranType:"SP",
		},
		boxed:false,
		disabledBtn:false,
	})
	let{planData,
		transportation,
		titles,
		deliveryWindowOptions,
		loadingDeliverWindow,
		loadingTransportation,
		infoData,
		boxDetail,
		totalBoxNum,
		submitloading,
		carrierData,
		boxDisable,
		inputboxNum,
		boxWeightData,
		tableData,
		boxListData,
		form,
		boxed,
		disabledBtn,
		}=toRefs(state);
	const shipBoxRef=ref();
	const shipBoxCaseRef=ref();
	let spacer = h(ElDivider, { direction: 'vertical' })
	const operationRef=ref();
	let boxmarksType=ref("PackageLabel_Plain_Paper");
	let panNum=ref(0);
			 function cellTitle(sku,index){
				 return sku+"    箱号:"+(index+1)+"";
			 }
			 function inputBoxNumChange(){
				 state.inputboxNum=CheckInputIntLGZero(state.inputboxNum);
				 if(state.inputboxNum){
				    state.inputboxNum=parseInt(state.inputboxNum)
				 }
				 addBoxSize("init");
				pointKeyChnage(".sd-table");
			 }
			//确认装箱数量
			function subimtBoxNum(){
				state.tableData.forEach((item)=>{
					for(var i=0;i<state.inputboxNum ;i++){
						item['boxNum'+i] = undefined
						state.boxWeightData[i] = undefined
					}
				})
				state.boxed =true
			}
			function radioChange(val){
				radio1.value= val
			}
			 
			//箱子尺寸单选
			function checkboxChange(i,a){
				if(i==0&&a==0&&state.boxListData.length==1){
					state.boxListData[0].boxcheck=[];
					for(var ibox=0;ibox<state.inputboxNum ;ibox++){
						state.boxListData[0].boxcheck.push(true);
					}
				}else{
					state.boxListData.forEach((item,index)=>{
						if(a==index){
							item.boxcheck[i] = true
						}else{
							item.boxcheck[i] = false
						}
					})
				}
			}
			function rowBoxsumNum(row){
				let sum = 0
				for(var i=0;i<state.inputboxNum ;i++){
					if(row['boxNum'+i]){
				       sum +=row['boxNum'+i]
					}
				}
				return sum
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
			//总计合计
			function getSummmary(index){
				let sumitem=0;
				if(index!=undefined){
					state.tableData.forEach((item)=>{
							if(item['boxNum'+index]!=undefined&&item['boxNum'+index]!=""){
								sumitem+=parseInt(item['boxNum'+index]);
							}
						});
				}else{
					state.tableData.forEach((item)=>{
						for(var i=0;i<state.inputboxNum ;i++){
							if(item['boxNum'+i]!=undefined&&item['boxNum'+i]!=""){
								sumitem+=parseInt(item['boxNum'+i]);
							}
							}
						});
				}
				return sumitem; 
			}
			//箱数立方尺寸求和
			function rowboxNumAndsize(row,index){
				let obj={}
				let boxnum = 0;
				let size =0
				if(row){
					row.boxcheck.forEach((item)=>{
						if(item == true){
							boxnum++;
						}
					})
					obj.boxnum = boxnum
					size = row.boxlength*row.boxwidth*row.boxheight*boxnum/1000000;
					obj.size = formatFloat(size);
				}
				return obj;
			} 
			 
			//计算属性
			let sumbsn = computed(()=>{
				let obj={}
				let boxnum = 0
				let size = 0
				state.boxListData.forEach((row)=>{
					let obj=rowboxNumAndsize(row);
					size=size+obj.size;
					boxnum=boxnum+obj.boxnum;
				})
				obj.boxnum = formatFloat(boxnum);
				obj.size =formatFloat(size) ;
				return obj
			})  
			
			let boxweightSum =   computed(()=>{
				let sumweight = 0;
				for(var i=0;i<state.boxWeightData.length;i++){
					if(state.boxWeightData[i]&&state.boxWeightData[i]!=""){
					   sumweight +=parseFloat(state.boxWeightData[i]);
					}
				}
				return formatFloat(sumweight);
			})  
				//竖向向装箱数求和
			state.tableData.colBoxsumNum = computed(()=>{
				const sumarr = [];
				state.tableData.forEach((item)=>{
					for(var i=0;i<state.inputboxNum ;i++){
						let sumitem=0;
						if(item['boxNum'+i]!=undefined){
							sumitem=item['boxNum'+i];
						}
						if(sumarr[i]){
							sumarr[i] +=sumitem;
						}else{
							sumarr[i] = sumitem;
						}
					}
				})
				
				return sumarr
			}) 
			//箱子重量
			function packNumSum(row){
				let sum = 0
				for(var i=0;i<state.inputboxNum ;i++){
				    if(row['boxNum'+i]){
					sum +=parseInt(row['boxNum'+i]);
					}
				}
				row.sum=sum;
			}
			function packNumchange(row,index){
				var boxindex='boxNum'+index;
				if(row[boxindex]&&row[boxindex]!="0"){
				   row[boxindex]=CheckInputIntLGZero(row[boxindex]);
				}
				packNumSum(row);
			}
		 
			function saveTime(){
				state.submitloading=true;
				var data={"formid":state.planData.plan.id,"deliveryWindowOptionId":state.planData.shipment.deliveryWindowOptionId ,"shipmentid":state.planData.shipment.shipmentid}
				shipmentPlacementApi.confirmDeliveryWindowOptions(data).then(res=>{
					state.submitloading=false;
					if(res.data&&res.data.operationid){
						 operationRef.value.show(res.data.operationid,3);
					}
				}).catch(()=>{
					state.submitloading=false;
				});
				
				
			}
			 
			function refreshShipment(){
				shipmentPlacementApi.boxShipment(state.planData.plan.id,[state.planData.shipment.shipmentid]).then((res)=>{
		 
				});
			}
			function loadDeliverWindow(shipmentid){
					if(shipmentid){
					state.loadingDeliverWindow=true;
						shipmentPlacementApi.listDeliveryWindowOptions({"formid":state.planData.plan.id,"shipmentid":shipmentid}).then(res=>{
							state.loadingDeliverWindow=false;
							state.deliveryWindowOptions=res.data.deliveryWindowOptions;
							state.deliveryWindowOptions.sort((a, b)=>{
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
						});
					}
			}
			
			function showEditBoxNum(row){
				changeTotalBoxNum();
			}
		  
			function downloadLabel(labeltype){
				var data={};
				data.shipmentid=state.infoData.shipmentid;
				data.pagetype=boxmarksType.value;
				data.labeltype=labeltype;
				data.pannum=panNum.value;
				shipmentPlacementApi.downLabel(data,()=>{
					emit('change');
				});
			}
			function loadOptData(datas){
				state.planData=datas;
				if(state.planData.plan){
					if(state.planData.plan.auditstatus<7){
						state.disabledBtn=true;
					}else{
						state.disabledBtn=false;
					}
				}
				state.infoData=datas.shipmentAll;
				if(datas.plan.submitbox==false && datas.shipment.status==3){
					state.boxDisable=false;
					state.boxed=false;
				}else{
					state.boxDisable=true;
					state.boxed=true;
				}
				if(state.infoData.transtyle){
				   state.form.tranType=state.infoData.transtyle;
				}
				if(datas.shipment.status>3){
					if(datas.shipment.deliveryWindowOptionId){
						loadDeliverWindow(state.infoData.shipmentid);
					} 
				}
				nextTick(()=>{
					if(state.planData.plan.areCasesRequired){
						shipBoxCaseRef.value.loadOptData(state.planData);
					}else{
						shipBoxRef.value.loadOptData(state.planData);
					}
				});
			}
			function handleOperationChange(data){	
				state.submitloading=false;
				if(data && data.operationStatus=="SUCCESS" && data.operation=="setPackingInformation" ){
							boxShipment();
					}else if(data && data.operationStatus=="SUCCESS" && data.operation=="generateDeliveryWindowOptions" ){
						    loadDeliverWindow(state.infoData.shipmentid);
					}
			}
			
			function stepclick(step){
				emit("stepdata",step);
			}
			 defineExpose({loadOptData})
</script>

<style scoped="scoped">
    .shipment-card-wrap{
		padding-bottom:8px ;
		margin-bottom: 16px;
		border-bottom: 1px solid #eee;
	}
	.dark .shipment-card-wrap{
		padding-bottom:8px ;
		margin-bottom: 16px;
		border-bottom: 1px solid #121212;
	}
	.box-ship {
		margin:16px;
	}
    .box-list-title{
		margin-top:16px;
		margin-bottom:16px;
	}
	.sd-table td{
		background-color: var(--el-bg-color);
	}
</style>
<style>
	.el-input.is-disabled .el-input__inner{
		color:#333;
		-webkit-text-fill-color:#333;
	}
	.el-radio__input.is-disabled+span.el-radio__label{color:inherit;}
</style>