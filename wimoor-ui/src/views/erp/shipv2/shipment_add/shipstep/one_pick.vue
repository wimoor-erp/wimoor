<template>
	<div class="box-margin">
	<div class="pag-radius-bor mar-bot" >
	<div class="con-body " > 
	<Header ref="headerRef" ></Header>
	<div class="adress-wrap">
	   <el-row :gutter="32">
		   <el-col :span="6">
		   <el-space>
			<el-icon 
			 :size="16"
			 color="#999"
			 ><Location /></el-icon>发货地址
		   </el-space>
		   <div v-if="planData.address" class="adress-text">
			   {{planData.address.addressline1}},{{planData.address.addressline2}},{{planData.address.stateorprovincecode}},{{planData.address.city}}
		   ,{{planData.address.districtorcounty}},{{planData.address.countrycode}},{{planData.address.postalcode}}
		   </div>
		   <div>
			   <el-link type="primary" @click="showAddressModal">选择其它地址</el-link>
		   </div>
		   </el-col>
		   <el-col :span="18">
		   <el-space>
		   	 <sphere 
			  class="ic-cen"
			  theme="outline" size="16" fill="#999" :strokeWidth="2"/>收货国家
		   </el-space>
		   <div class="adress-text">
		   		{{planData.destination}} 
		   </div>
		   </el-col>
	   </el-row>
		</div>
		<el-divider ></el-divider>
		<el-row>
			<div class="shipList-wrap">
			<el-table :data="planData.itemlist" border >
				<el-table-column prop="image" label="图片" width="70">
					<template #default="scope">
						<img :src="scope.row.image" @click.stop="handleToMaterial(scope.row)" style="width:40px;height:40px" />
					</template>
				</el-table-column>
				<el-table-column label="名称/SKU"  show-overflow-tooltip>
					<template #default="scope"> 
						<div class='name text-omit-1'>{{scope.row.name}}</div>
						
						<div class='sku pointer'  @click.stop="showPrintModal(scope.row)" >{{scope.row.sku}}
						<el-space>
						 <el-tag v-if="scope.row.issfg=='1'" type="success">组装</el-tag>
						 <el-tag v-if="scope.row.boxnum">单箱：{{scope.row.boxnum}}</el-tag>
						<span v-if="scope.row.prep&&scope.row.prep.prepInstructionList&&scope.row.prep.prepInstructionList.length>0">
							<el-tag style="margin-left:3px;" v-for="prepitem in scope.row.prep.prepInstructionList">
								 <span v-if="PrepInstruction[prepitem]" >{{PrepInstruction[prepitem]}}</span>
								 [{{prepitem}}]
							</el-tag>
						</span>
						</el-space>
						</div>
					</template>
				</el-table-column>
				<el-table-column prop="invquantity" label="库存" width="120"   />
				<el-table-column prop="outbound" label="待出库" width="120"   />
				<el-table-column  label="信息/操作" width="120">
					<template #default="scope">
						 <div  ><el-link  type="primary" @click="showInfoDialog(scope.row)">预处理信息</el-link></div>
					</template>
				</el-table-column>
				<el-table-column  label="计划发货数量" prop="quantity" width="120"></el-table-column>
				<el-table-column  label="实际发货数量" width="120">
					<template #default="scope">
						<el-input :disabled="piceDisable" v-model.number="scope.row.confirmQuantity"></el-input>
					</template>
				</el-table-column>
			</el-table>
			</div>
		</el-row>
		<div>
			<el-row>
			<el-col :span="12" >
				<span>共 {{itemcount}} 条</span>
			</el-col>
			<el-col :span="8" >
					<Operation ref="operationRef" @change="handleOperationChange"></Operation>
			</el-col>
			<el-col :span="4" >
				<div class="text-right">
					<span class="font-extraSmall">预处理费用和贴标费用总金额：</span>{{formatFloat(totalfee)}}</div>
			</el-col>
	
			
			</el-row>
		</div>
		<el-row style="margin-top:12px;">
			<el-col :span="8">
				<el-row>
					<el-col :span="3">
						<el-button type="primary" plain v-if="planData.auditstatus!=12 || planData.invstatus!=0" @click="cancelPlan">取消计划</el-button>
					</el-col>
				</el-row>
			</el-col>
			<el-col :span="16" >
				<div class="pull-right">
					<el-button   @click.stop="showBoxAnalysis">分析装箱</el-button>
					<!--el-button   @click.stop="showBoxPlan">装箱计划</el-button-->
					<el-button   @click.stop="showPre">准备分类</el-button>
					<el-button @click="handleShelf" style="margin-right: 10px;" >
						<el-icon :size="16"  color="#FF6700" ><ShoppingCartFull /></el-icon>&nbsp;
						下架SKU</el-button>
					  <el-dropdown split-button  @click.stop="openShipmentInfo('simple')" style="margin-right: 8px;">
						  <el-icon color="#FF6700"> <document   /></el-icon>
						<span  >打印配货单(简洁版)</span>
						<template #dropdown>
						  <el-dropdown-menu>
							<el-dropdown-item @click.stop="openShipmentInfo('detail')">打印配货单(详细版)</el-dropdown-item>
							<!-- <el-dropdown-item @click.stop="openShipmentInfo('mobile')">打印配货单(移动版)</el-dropdown-item> -->
						  </el-dropdown-menu>
						</template>
					  </el-dropdown>
					  <el-dropdown split-button @click.stop="downloadLabel('excel')" style="margin-right: 8px;" >
							  <el-icon  color="#FF6700" > <printer/></el-icon>
							<span    >打印所有SKU标签(Excel)</span>
							<template #dropdown>
							  <el-dropdown-menu>
								<el-dropdown-item @click.stop="downloadLabel('pdf')">打印所有SKU标签(PDF)</el-dropdown-item>
							  </el-dropdown-menu>
							</template>
					  </el-dropdown>
					  <el-space v-if="planData.auditstatus==2">
						   <el-button :loading="btnStatus.submitForm" v-if="piceDisable"  @click.stop="piceDisable=false">修改配货数量</el-button>
						   <el-button type="primary" :loading="btnStatus.submitForm"  v-else   @click.stop="submitForm">保存配货数量</el-button>
						   <el-button type="primary" :loading="btnStatus.submitStep" v-if="piceDisable"   @click.stop="submitStep" >完成配货</el-button>
						   <el-button type="primary" :loading="btnStatus.submitStep" v-else    :disabled="true" >完成配货</el-button>
					  </el-space>
					  <span v-else>
						  <el-button style="margin-left: 20px;"  type="info" plain @click="donePlanBox" v-if="planData.auditstatus<=3&&!planData.submitbox">跳过装箱(仅LTL,先分票后装箱)</el-button>
					     <el-button     @click.stop="toStep" >下一步</el-button>
					  </span>
				</div>
			</el-col>
		</el-row>
		<el-row v-if="hasAssembly" class="m-t-16 pointer">
					  <el-col :span="24">
						  <el-alert
						    @click.stop="handleToAssemblyForm" 
						     type="warning"
						     description="系统已经入库可组装库存，请在该货件发货前，完成相应主SKU的组装任务，"
						     show-icon
						  >
						  <template #title>
							  该货件生成了{{assemblyNum}}个组装任务,点击查看组装单. <span class="text-red">（注意：取消计划时，实际发货数量小于计划发货数量，自动组装将不会撤销）</span>
						  </template>
						   </el-alert>
					   </el-col>
		</el-row>
	</div>
	</div>

	
	 <Footer  ref="footerRef"></Footer>
	</div>
	
	<el-dialog
	title="修改发货地址"
	v-model="addressVisiable"
	width="50%"
	
	>
	<GlobalTable :inDialog="true" ref="globalTable" :tableData="tableData"  @loadTable="loadTableData" :stripe="true"  style="width: 100%;margin-bottom:16px;">
		  <template #field>
		    <el-table-column type="selection" width="38" />
		    <el-table-column prop="name"  label="名称" sortable='custom' >
				<template #default="scope">
								<span>{{scope.row.name}}</span>
								&nbsp;&nbsp;&nbsp; <el-tag class="ml-2" v-if="scope.row.isdefault" type="info">默认</el-tag>  
				</template>
				
			</el-table-column>
		    <el-table-column prop="city"  label="所在发货地"  sortable='custom' />
			<el-table-column prop="addressline1" label="街道/详细地址" sortable='custom' >
			   <template #default="scope">
				<span>{{scope.row.addressline1}}</span>
				<span v-if="scope.row.addressline2">,{{scope.row.addressline2}}</span>
			  </template>
			</el-table-column>
		    <el-table-column prop="postalcode"  label="邮政编码"  sortable='custom' />
			<el-table-column prop="phone"  label="电话"  sortable='custom' />
			<el-table-column prop="operator"  label="操作人"  sortable='custom' />
	    </template>
	</GlobalTable>
	<template #footer>
		<el-button @click="addressVisiable=false"  >关闭</el-button>
		<el-button @click="changeAddress" type="primary">确认</el-button>
	</template>
	</el-dialog>
	
	<el-dialog
	title="预处理信息设置"
	v-model="operatorVisiable"
	width="30%"
	>
	 <el-form label-width="auto"  label-position="left">
		  <el-form-item   label="预备信息处理人(PrepOwner)">
			  <el-select :disabled="piceDisable" style="width:200px" v-model="operatorRow.prepOwner">
				 <el-option value="NONE" key="NONE" label="NONE"></el-option> 
				 <el-option value="SELLER" key="SELLER" label="卖家(SELLER)"></el-option> 
				 <el-option value="AMAZON" key="AMAZON" label="亚马逊(AMAZON)"></el-option> 
			  </el-select>
		  </el-form-item>
		  <el-form-item   label="贴标人(LabelOwner)">
		  			  <el-select :disabled="piceDisable" style="width:200px" v-model="operatorRow.labelOwner">
						  <el-option value="NONE" key="NONE" label="NONE"></el-option>
						  <el-option value="SELLER" key="SELLER" label="卖家(SELLER)"></el-option>
						  <el-option value="AMAZON" key="AMAZON" label="亚马逊(AMAZON)"></el-option> 
					  </el-select>
		  </el-form-item>
		  <el-form-item   label="保质期(Expiration)">
				<el-date-picker :disabled="piceDisable"
					  v-model="operatorRow.expiration"
					  type="date"
					  placeholder="选择日期"
					/>
		  </el-form-item>
	 </el-form>
	<template #footer>
		<el-button @click="operatorVisiable=false" >关闭</el-button>
		<el-button @click="saveItem" :disabled="piceDisable" type="primary">确认</el-button>
	</template>
	</el-dialog>
	 <PlanCancel ref="planCancelRef" @change="handleStatusChange"></PlanCancel>
	 <PrintDialog ref="printRef"></PrintDialog>
	 <BoxPlan ref="boxPlanRef"></BoxPlan>
	 <Pre ref="preRef"></Pre>
	 <BoxAnalysisDialog ref="boxAnalysisDialogRef"></BoxAnalysisDialog>
</template>

<script setup>
	import { ref,reactive,onMounted,toRefs } from 'vue';
	import { useRoute,useRouter } from 'vue-router';
	import {Location,Van,Printer,Document,ShoppingCartFull} from '@element-plus/icons-vue';
	import {Sphere} from"@icon-park/vue-next"
	import Footer from "./components/footer.vue";
	import Header from "./components/header.vue";
	import PlanCancel from "./components/planCancel.vue";
	import PlanCreate from "./components/planCreate.vue";
	import {dateFormat,formatFloat,getValue,formatPercent,CheckInputIntLGZero,CheckInputFloat} from '@/utils/index.js';
	import shipmentBoxApi from '@/api/erp/shipv2/shipmentBoxApi.js';
	import { ElMessage,ElMessageBox } from 'element-plus';
	import addressApi from '@/api/amazon/inbound/addressApi.js';
	import shipmentplanApi from '@/api/erp/shipv2/shipmentplanApi.js';
	import shipmentQuotaApi from '@/api/erp/shipv2/shipmentQuotaApi.js';
	import {assemblyShipment} from '@/api/erp/assembly/assemblyApi.js';
	import Operation from "./components/operation.vue";
	import BoxPlan from "./components/box_plan.vue";
	import Pre from "./components/pre.vue";
	import BoxAnalysisDialog from "./components/box_analysis_dialog.vue";
	
	import PrintDialog from "@/views/erp/ship/shipment_handing/shipstep/components/print_dialog.vue";
	let router = useRouter();
	const printRef=ref();
	const planid=router.currentRoute.value.query.id;
	const footerRef=ref();
	const headerRef=ref();
	const globalTable=ref();
	const operationRef=ref();
	const planCancelRef=ref();
	const planCreateRef=ref();
	const boxPlanRef=ref();
	const preRef=ref();
	const boxAnalysisDialogRef=ref();
	let state =reactive({
		planData:{}, 
		tableData:{records:[],total:0},
		step:0,
		piceDisable:true,
		itemcount:0,
		addressVisiable:false,
		hasAssembly:false,
		assemblyNum:0,
		operatorVisiable:false,
		operatorRow:null,
		btnStatus:{
			"submitForm":false,
			"submitStep":false,
			"number":1,
		},
		totalfee:0,
		
	})
	let{planData,step,piceDisable,assemblyNum,hasAssembly,itemcount,tableData,addressVisiable,operatorVisiable,operatorRow,btnStatus,totalfee}=toRefs(state);
	function showPre(){
		preRef.value.show(state.planData);
	}
	  function showBoxAnalysis(){
					boxAnalysisDialogRef.value.show(state.planData);
	}
	
	function donePlanBox(){
		ElMessageBox.confirm('确认掉过装箱，此操作不可逆?', '警告', {
				confirmButtonText: '确定',
				cancelButtonText: '取消',
				type: 'warning',
		}).then(() => {
				shipmentBoxApi.donePlanBox({"formid":state.planData.id,"type":"skip"}).then(res=>{
					router.push({
						path:'/e/s/s/placement',
						query:{
						  id:state.planData.id,
						  title:"发货处理_配置",
						  path:'/e/s/s/placement',
						  replace:true
						}
					})	
				})
		}).catch(() => ElMessage.info('操作已取消'));
		
		
	}
	
	function showPrintModal(row){
		row.QuantityShipped=row.confirmQuantity;
		printRef.value.show(row,state.planData);
	}
	function showBoxPlan(){
		boxPlanRef.value.show(state.planData);
	}
	function handleToAssemblyForm(){
		router.push({
			path:'/erp/purchase/process',
			query:{
			  shipmentid:state.planData.number,
			  title:"加工单",
			  path:'/erp/purchase/process',
			}
		})
	}
	function loadData(){
		shipmentplanApi.getPlanInfo({"formid":planid}).then((res)=>{
			state.planData=res.data;
			state.itemcount=state.planData.itemlist.length;
			if(state.planData.auditstatus!=2){
				state.piceDisable=true;
			}
			headerRef.value.show(state.planData,0);
			footerRef.value.show(state.planData);
			if(state.planData.auditstatus>2&&state.planData.auditstatus<7){
			   operationRef.value.show(state.planData.id,"createInboundPlan");
			}
			assemblyShipment({"shipmentid":state.planData.number}).then(res=>{
				if(res.data&&parseInt(res.data)>0){
					state.assemblyNum=parseInt(res.data);
					state.hasAssembly=true;
				}
			})
			loadGuidance();
		});
	}
	function handleStatusChange(status){
		if(status=="success"){
			loadData();
		}
	}
	function handleToMaterial(row){
		 router.push({
			path:'/material/details',
			query:{
			  title:"产品详情",
			  path:'/material/details',
			  details:row.materialid,
			}
		 })
	}
	
	function handleShelf(){
		router.push({
			path:'/erp/ship/quota',
			query:{
			  formid:state.planData.id,
			  title:"配货单下架",
			  path:'/erp/ship/quota',
			}
		})
	}
	
	function submitForm(){
		//确认配货数
		state.btnStatus.submitForm=true;
		state.piceDisable=true;
		state.planData.planitemlist=state.planData.itemlist;
		shipmentplanApi.changeInboundPlan(state.planData).then((res)=>{
			state.btnStatus.submitForm=false;
			if(res.data){
				ElMessage.success("确认配货数量成功！");
			}
		}).catch(()=>{
			state.btnStatus.submitForm=false;
		});
		
	}
	
	function cancelPlan(){
		ElMessageBox.confirm('确认取消此发货计划?', '警告', {
				confirmButtonText: '确定',
				cancelButtonText: '取消',
				type: 'warning',
		}).then(() => {
				planCancelRef.value.show(state.planData);
		}).catch(() => ElMessage.info('操作已取消'));
	}
	
	function openShipmentInfo(ftype){
		if(ftype=="mobile"){
			var path={path:"/shipment_handing/pehuo","query":{"shipmentid":state.planData.id, title:"配货单",path:"/shipment_handing/pehuo"}};
			router.push(path);
		}else{
			//下载pdf
			var formids=[];
			formids.push(state.planData.id+'');
			var ftypename="";
			if(ftype=="detail"){
				ftypename="-详细版";
			}else{
				ftypename="-简单版";
			}
			shipmentQuotaApi.downPDFShipForm(ftype,formids,state.planData.number+ftypename+"-配货单");
		}
	}
	
	function downloadLabel(ftype){
		if(ftype=='pdf'){
			shipmentQuotaApi.downPDFLabel({"formid":state.planData.id},state.planData.number+"-");
		}else{
			shipmentQuotaApi.downExcelLabel({"formid":state.planData.id},state.planData.number+"-");
		}
	}
	
	function loadTableData(params){
		if(!params){
			params={isdisable:""};
		}
		params.groupid=state.planData.amazongroupid;
		addressApi.getAddress(params).then((res)=>{
			state.tableData.records = res.data.records;
			state.tableData.total =res.data.total;
		})
	}
	
	function showAddressModal(){
		state.addressVisiable=true;
	}
	
	function changeAddress(){
	   var rows=globalTable.value.getSelectionRows();
	   if(rows && rows.length==1){
		   shipmentplanApi.changeAddress({"formid":state.planData.id,"addressid":rows[0].id}).then((res)=>{
			   state.addressVisiable=false;
			   ElMessage.success("操作成功！");
			   loadData();
		   });
	   }else{
		   ElMessage.warning("请正确选择地址！");
	   }
	}
	
	function showInfoDialog(row){
		state.operatorVisiable=true;
		state.operatorRow=JSON.parse(JSON.stringify(row));
	}
	
	function saveItem(){
		shipmentplanApi.updatePlanItem(state.operatorRow).then((res)=>{
			 ElMessage.success("操作成功！");
			 state.operatorVisiable=false;
			 loadData();
		});
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
	function handleOperationChange(data){
		//跳转路由至第二步
		if(data && data.operationStatus=="SUCCESS" && state.btnStatus.submitStep==true){
		   state.btnStatus.submitStep=false;
		   if(state.planData.auditstatus==2){
			   shipmentplanApi.doneInboundPlan({"formid":state.planData.id}).then(res=>{
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
			   })
		   }
		  
			
		}
		
	}
 
	function submitStep(){
		state.btnStatus.number=1;
		state.btnStatus.submitStep=true;
		shipmentplanApi.confirmInboundPlan({"formid":state.planData.id}).then((res)=>{
			if(res.data&&res.data.operationid){
				 ElMessage.warning("完成配货，同步亚马逊...");
				 operationRef.value.show(res.data.operationid);
			}else if(state.planData.invtype!="1"&&state.planData.invtype!=1){
				  ElMessage.warning("完成配货，同步亚马逊异常！");
			 	  state.btnStatus.submitStep=false;
			}else{
				 ElMessage.success("完成配货");
				  state.btnStatus.submitStep=false;
			}
		}).catch(()=>{
		   		 state.btnStatus.submitStep=false;
		});
		 
	}
	
	//预处理费用和贴标费用总金额
	function loadGuidance(){
	  var skulist="";
	  state.planData.itemlist.forEach(item=>{
		  skulist=skulist+item.sku+",";
	  });
	  shipmentplanApi.guidance({"groupid":state.planData.amazongroupid,
	  "marketplaceid":state.planData.marketplaceid,
						   "skulist":skulist}).then((res)=>{
			 var totalfee=0; 
			 var data=res.data.guidance;
			 var prepInstructions=res.data.prepInstructions;
			 state.planData.itemlist.forEach(item=>{
				  if(prepInstructions[item.sku] && prepInstructions[item.sku].amazonPrepFeesDetailsList &&
				  prepInstructions[item.sku].amazonPrepFeesDetailsList.length>0){
					 var fee= prepInstructions[item.sku].amazonPrepFeesDetailsList[0].feePerUnit.value;
					 totalfee=totalfee+fee; 
				  }
			 });
			 state.totalfee=totalfee;
			 // if(data&&data.skUInboundGuidanceList){
				//  data.skUInboundGuidanceList.forEach(function(items){
				// 	 if(items.sellerSKU==item.sku){
				// 		item.asin=items.asin;	
				// 		 if(items.inboundGuidance=="INBOUNDOK"){
				// 			 var msg="";
				// 			 if(items.guidanceReasonList){
				// 				 items.guidanceReasonList.forEach(reason=>{
				// 					 if(reason =="NOAPPLICABLEGUIDANCE"){
				// 						 msg=msg+"【无适用指南】";
				// 					 }else{
				// 						 msg=msg+"【库存足，销售慢】";
				// 					 }
				// 				 })
				// 			 }
				// 			 item.msg=msg;
				// 			 item.guidance="success";									  
				// 		 }else{
				// 			 item.guidance="error";
				// 		 }
						
				// 	 }
				//  })
			 // }
			 
		})
	}
	
	onMounted(()=>{
		loadData();
		
	})
</script>

<style scoped>
	.pull-right{
		float: right;
	}
	.mar-bot{
		font-size:14px;
	}
	.mar-bot{
		margin-bottom:16px;
	}
	.box-margin{
		padding:16px;
		min-height:calc(100vh - 36px)
	}
	.adress-wrap{
		margin-top: 16px;
		margin-bottom:16px;
	}
	.adress-text{
		line-height: 1.5;
		margin-top:12px;
		margin-bottom: 4px;
	}
	.shipList-wrap{
		margin-top: 12px;
		margin-bottom: 12px;
		width: 100%;
	}
	.m-t-16{
		margin-top:16px;
	}
</style>
