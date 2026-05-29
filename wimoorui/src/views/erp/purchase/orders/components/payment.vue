<template>
	<el-dialog top="3vh" class="mypaymentdailog" 
	lock-scroll
	:title="operateType.dialogTitle" 
	v-model="dialogVisable" width="1300px">
		<el-scrollbar height="calc(100vh - 180px)" :always="true" :native="true">
		<el-row  class="bg-full">
			<el-col :span="10">
				<el-card shadow="never" class="bg-full">
					<div  class="product-box">
						<el-image v-if="queryParams.entry.image" :src="queryParams.entry.image" class="img-40"  width="40" height="40"  ></el-image>
						<el-image v-else :src="$require('empty/noimage40.png')"  class="img-40"  width="40" height="40"  ></el-image>
						<div>
							<p class="name">{{queryParams.entry.mname}}</p>
							<p class="sku">{{queryParams.entry.sku}}</p>
						</div>
					</div>
					<div class="flex-center-between " v-if="operateType.tabsType=='付款'">
					<el-space :spacer="spacer" :size="24" >
					 <span ><span class="font-bold">{{payMap.payneed}}</span><p class="font-extraSmall">待付款</p></span>
					 <span><span class="font-bold">{{payMap.totalcost}}</span><p class="font-extraSmall">已付款</p></span>
					 <span><span class="font-bold">{{payMap.totalship}}</span><p class="font-extraSmall">已付运费</p></span>
					 <span><span class="font-bold">{{totalamount}}</span><p class="font-extraSmall">总付费</p></span>
					</el-space>
					<el-tag v-if="queryParams.paystatus==0" type="warning" size="large">待付款</el-tag>
					<el-tag v-if="queryParams.paystatus==1" type="success" size="large">付款完成</el-tag>
					</div>
					<el-divider class="divider-cell"></el-divider>
					<el-form label-width="100px" label-position="top">
						 <el-row :gutter="24">
						  <el-col :span="12">
							<el-form-item label="货物费用">
								<el-input v-model="formData.cost" clearable @input="formData.cost=CheckInputFloat(formData.cost)"></el-input>
							</el-form-item>
						  </el-col>
						  <el-col :span="12">
						<el-form-item label="运费">
							<template #label>
								<div class="flex-center-between">
								<span>运费</span>
								<p  class="font-extraSmall">预计重量：{{formatFloat(payMap.weight)}}kg</p>
								</div>
							</template>
							<el-input v-model="formData.ship" clearable @input="formData.ship=CheckInputFloat(formData.ship)"></el-input>
						</el-form-item>
						</el-col>
						 </el-row>
						 <div >
						<el-form-item label="其它费用">
						   <el-table :data="otherFeeData" border size="small">
							   <el-table-column type="index">
								   <template #header>
									   <el-link :underline="false" @click="handleAdd">
										   <el-icon class="ic-cen font-large"><Plus /></el-icon>
									   </el-link>
								   </template>
							   </el-table-column>
							   <el-table-column label="费用名称">
								   <template #header>
									   <div  class="flex-center">
									   <span>费用名称</span>
									   <span class="table-edit-flex">
										 <el-icon @click.stop="gotoFinPage" class="ic-cen"><Edit/></el-icon>  
									   </span>
									   </div>
								   </template>
								   <template #default='scope'>
									   <el-select v-model="scope.row.objectid" size="small">
										   <el-option  v-for="item in finlist"   :key="item.id"  :label="item.name" :value="item.id"  ></el-option>
									   </el-select>
								   </template>
							   </el-table-column>
							   <el-table-column label="金额">
								   <template #default='scope'>
								   <el-input size="small" v-model="scope.row.amount" @input="scope.row.amount=CheckInputFloat(scope.row.amount)"></el-input>
								   </template>
							   </el-table-column>
							   <el-table-column label="操作" width="60">
								   <template #default='scope'>
								   <el-link :underline="false" @click="handleDelete(scope.$index)">
								  <el-icon class="ic-cen font-medium"><Close /></el-icon>
								  </el-link>
								  </template>
							   </el-table-column>
						   </el-table>
						</el-form-item>
						</div>
						<el-form-item label="预计到货日期">
							<el-radio-group v-model="formData.datetype" >
							      <el-radio label="1" >
									 <el-date-picker
									        style="width:176px"
									         v-model="payMap.delivery_cycledate"
									         type="date"
									         placeholder="请选择"
									       />
								  </el-radio>
							      <el-radio label="2" >
									  <div>{{dateFormat(payMap.deliverydate)}}
									  <span class="font-extraSmall">旧预计到货日期</span>
									  </div>
								  </el-radio>
							    </el-radio-group>
						</el-form-item>
						<el-row :gutter="24">
						 <el-col :span="12">
						<el-form-item  >
							<template #label>
								<div class="pointer" @click.stop="showPayIndexDialog" >支付方式<el-icon class="font-extraSmall"><Sort /></el-icon></div>
							</template>
							<el-select v-model="formData.paymethod" @change="loadPaymentAccount(formData.paymethod)">
								 <el-option  v-for="item in payMethodList"   :key="item.id"  :label="item.name" :value="item.id"  ></el-option>
							</el-select>
						</el-form-item>
						</el-col>
						<el-col :span="12">
						<el-form-item    class="pointer" >
							<template #label>
								<div class="pointer" @click.stop="toPaymentPage">支付账户 </div>
							</template>
							<el-select v-model="formData.payacc">
								 <el-option  v-for="item in payAccList"   :key="item.id"  :label="item.name+'['+item.paymethName+']'" :value="item.id"  >
								 </el-option>
							</el-select>
						</el-form-item>
						</el-col>
						</el-row>
						<el-form-item label="备注">
							<el-input v-model="formData.remark"  :rows="2"
								type="textarea"></el-input>
						</el-form-item>
						<el-form-item >
						<el-button type="primary" v-if="queryParams.entry.paystatus==1" disabled >{{operateType.tabsType}}</el-button>
						<el-button type="primary" v-else @click.stop="payment" >{{operateType.tabsType}}</el-button>
						<el-button type="info" v-if="queryParams.entry.paystatus==1" disabled >申请{{operateType.tabsType}}</el-button>
						<el-button type="info" v-else @click.stop="applypayment" >申请{{operateType.tabsType}}</el-button>
						</el-form-item>
					</el-form>
				</el-card>	
			</el-col>
			<el-col :span="9">
				<el-card shadow="never" class="bg-full">
					<OrderRecord ref="orderRecordRef" @noauth="layoutChange" @change="handleAlibabaOrderFee"/>
				</el-card>
			</el-col>
			<el-col :span="5" class="record-box">
					 <PayRecord ref="recordRef" @change="loadRecord()" @loadpay="loadTotalPay" />
			</el-col>
   </el-row>	
		  </el-scrollbar>
		  <template #footer class="footerbg">
			  <el-button @click="dialogVisable=false">关闭</el-button>
			  <el-button type="primary"  v-if="queryParams.entry.paystatus==0"  @click.stop="stopPayment" plain>结束付款</el-button>
			  <el-button type="primary" v-if="queryParams.entry.paystatus==1" @click="startPayment" plain>继续付款</el-button>
		  </template>
	</el-dialog>
	<FinItem ref="finItemRef" @change="loadFacProject()"></FinItem>
	<PaymethodIndex ref="paymentIndexRef" @change="loadPaymentMethod"></PaymethodIndex>
</template>

<script setup>
	import {h, ref,reactive,onMounted,watch,inject,toRefs,nextTick} from 'vue'
	import { ElDivider } from 'element-plus'
    import {Close,Plus,Edit,Sort} from '@element-plus/icons-vue';
	import PayRecord from "./pay_record.vue"
	import OrderRecord from "./order_record.vue";
	import purchaselistApi from '@/api/erp/purchase/form/listApi.js';
	import faccountApi from '@/api/erp/finances/faccountApi.js';
	import FinItem from '@/views/erp/finance/account/components/finItem.vue';
	import PaymethodIndex from "@/views/erp/finance/account/components/paymethod_index_dialog.vue";
	import { ElMessage, ElMessageBox } from 'element-plus';
	import {CheckInputFloat,CheckInputInt,dateFormat,dateTimesFormat,formatFloat} from '@/utils/index.js';
	import {useRouter } from 'vue-router'
	const emitter = inject("emitter");
	const emit = defineEmits(['change']);
	const spacer = h(ElDivider, { direction: 'vertical'});
	const recordRef=ref();
	const finItemRef=ref();
	const orderRecordRef=ref();
	const paymentIndexRef=ref();
	const router = useRouter();
	const state = reactive({
		operateType:{
			dialogTitle:'采购付款',
			tabsType:'付款'
		},
		dialogVisable:false,
		// 查询参数
		queryParams: {
			entry:{},
		} ,
		payMap:{
			paylist:[],
			entry:{},
		},
		otherFeeData:[
			{objectid:"",amount:0}
		],
		formData:{
			datetype:'1',
			cost:0,
			ship:0,
			payacc:"",
			paymethod:null,
			remark:"",
		},
		layout:{left:10,right:14},
		finlist:[],
		payMethodList:[],
		payAccList:[],
		totalamount:0,
	})
	const {
		dialogVisable,
		queryParams,
		operateType,
		otherFeeData,
		layout,
		payMap,
		formData,
		finlist,
		payMethodList,
		payAccList,
		totalamount,
	}=toRefs(state)
	function gotoFinPage(){
		//跳转至fin页面
		finItemRef.value.show();
	}
	function layoutChange(){
		state.layout={left:23,right:1};
	}
	function showPayIndexDialog(){
		paymentIndexRef.value.show();
	}

	function handleAdd(){
		state.otherFeeData.push({
			name:'',
			price:'',
		})
	}
    
	function handleDelete(index){
		state.otherFeeData.splice(index);
	}
	function loadFacProject(){
		var list=[];
		faccountApi.getProject().then((res)=>{
			if(res.data && res.data.length>0){
				res.data.forEach(function(item){
					if(item.issys==false){
						list.push(item);
					}
				});
				state.finlist=list;
			}
		});
	}
	function loadPaymentMethod(){
		faccountApi.getPaymentMethod().then((res)=>{
			if(res.data && res.data.length>0){
				state.payMethodList=res.data;
				state.formData.paymethod=res.data[0].id;
				loadPaymentAccount(res.data[0].id);
			}
		});
	}
	function loadPaymentAccount(paymethod){
		if(state.payAccList!=null&&state.payAccList.length>0){
			var defaultid="";
			var normalDefault="";
			state.payAccList.forEach(item=>{
				if(item.isdefault&&item.paymeth==paymethod){
					defaultid=item.id;
				}
				if(normalDefault==""&&item.isdefault){
					normalDefault=item.id;
				}
				
			});
			state.formData.payacc=defaultid==""?normalDefault:defaultid;
		}else{
			faccountApi.getPaymentAccount({"paymethod":paymethod}).then((res)=>{
				if(res.data && res.data.length>0){
					state.payAccList=res.data;
					var defaultid="";
					var normalDefault="";
					state.payAccList.forEach(item=>{
						if(item.isdefault&&item.paymeth==paymethod){
							defaultid=item.id;
						}
						if(normalDefault==""&&item.isdefault){
							normalDefault=item.id;
						}
						
					});
					state.formData.payacc=defaultid==""?normalDefault:defaultid;
					 
				}else{
					state.payAccList=[];
					state.formData.payacc="";
				}
			});
		}
		
	}
	 
	function applypayment(){
		if(state.queryParams.entry.paystatus==1){
			ElMessage.error( '当前付款状态已完结！' );
			return;
		}
		if(state.formData.cost<=0){
			ElMessage.error('货物费用不能小于等于0');
			return;
		}
		var data={};
		data.paymethod=state.formData.paymethod;
		data.logisiter=null;
		data.status="0";
		data.payid=null;
		if(state.formData.datetype=="1"){
			data.deliverydate=payMap.delivery_cycledate;
		}else{
			data.deliverydatestr=payMap.deliverydate;
		}
		data.remark=state.formData.remark;
		data.shipamount=state.formData.ship?parseFloat(state.formData.ship):0;
		data.costamount=state.formData.cost?parseFloat(state.formData.cost):0;
		data.entryid=state.queryParams.entry.id;
		if(state.operateType.tabsType=="付款"){
			data.paytype="out";
		}else if(state.operateType.tabsType=="退款"){
			data.paytype="in";
		}
		if(data.paytype=="in"){
			if(state.formData.ship>0){
				data.shipamount=(-1)*parseFloat(state.formData.ship);
			}
			if(state.formData.cost>0){
				data.costamount=(-1)*parseFloat(state.formData.cost);
			}
		}
		if(state.otherFeeData && state.otherFeeData.length>0){
			var feeList=[];
			state.otherFeeData.forEach(function(item){
				if((item.amount!=undefined && item.amount!="" && parseFloat(item.amount)>0) && item.objectid){
					feeList.push(JSON.stringify(item));
				}
			});
			data.feelist=feeList.toString();
		}
		purchaselistApi.paymentApply(data).then((res)=>{
			if(res.data){
				ElMessage.success('申请'+state.operateType.tabsType+'成功');
				emit("change");
				state.queryParams.entry.paystatus=3;
			}
		});
	}
	function payment(){
		if(state.queryParams.entry.paystatus==3){
			ElMessage.error('当前存在已请款的费用，请跳转至请款单页面处理后再操作付款！');
			return;
		}
		if(state.queryParams.entry.paystatus==1){
			ElMessage.error('当前付款状态已完结！');
			return;
		}
		var data={};
		var sumpay=0;
		data.paymethod=state.formData.paymethod;
		data.payacc=state.formData.payacc;
		data.logisiter=null;
		data.status="0";
		data.payid=null;
		if(state.formData.datetype=="1"){
			data.deliverydate=payMap.delivery_cycledate;
		}else{
			data.deliverydatestr=payMap.deliverydate;
		}
		data.remark=state.formData.remark;
		data.shipamount=state.formData.ship?parseFloat(state.formData.ship):0;
		data.costamount=state.formData.cost?parseFloat(state.formData.cost):0;
		
		if(state.formData.cost){
			sumpay=sumpay+parseFloat(state.formData.cost);
		}
		if(state.formData.ship){
			sumpay=sumpay+parseFloat(state.formData.ship);
		}
		data.entryid=state.queryParams.entry.id;
		if(state.operateType.tabsType=="付款"){
			data.paytype="out";
		}else if(state.operateType.tabsType=="退款"){
			data.paytype="in";
		}
		if(data.paytype=="in"){
			if(state.formData.ship>0){
				data.shipamount=(-1)*parseFloat(state.formData.ship);
			}
			if(state.formData.cost>0){
				data.costamount=(-1)*parseFloat(state.formData.cost);
			}
		}
		if(state.otherFeeData && state.otherFeeData.length>0){
			var feeList=[];
			state.otherFeeData.forEach(function(item){
				if((item.amount!=undefined && item.amount!="" && parseFloat(item.amount)>0) && item.objectid){
				    sumpay=sumpay+parseFloat(item.amount);
					if(data.paytype=="in"){
						item.amount=(-1)*parseFloat(item.amount);
					}
					feeList.push(JSON.stringify(item));
				}
			});
			data.feelist=feeList.toString();
		}
		
		if(sumpay<=0.000001){
			ElMessage.error('费用不能小于等于0');
			return;
		}
		purchaselistApi.payment(data).then((res)=>{
			if(res.data){
				ElMessage.success('付款成功');
				loadRecord();
				state.payMap=res.data;
				state.queryParams.entry.totalpay=res.data.entry.totalpay;
				state.queryParams.entry.paystatus=res.data.entry.paystatus;
				state.queryParams.entry.auditstatus=res.data.entry.auditstatus;
				emit("change");
				emitter.emit("removeCache", "采购记账");
				//if(res.data.entry.paystatus==1){
					//state.dialogVisable=false;
				//}
			}
		});
	}
	function stopPayment(){
		if(state.queryParams.entry.paystatus==3){
			ElMessage.error('当前存在已请款的费用，请跳转至请款单页面处理后再操作束付款！');
			return;
		}
		ElMessageBox.confirm(
			'请确认是否结束付款？',
			{
			  confirmButtonText: '确认',
			  cancelButtonText: '取消',
			  type: 'warning',
			  callback:(action)=>{
				 if(action=="confirm"){
					 var data={};
					 data.entryid=state.queryParams.entry.id;
					 data.paytype="out";
					 data.costamount=0;
					 data.shipamount=0;
					 if(state.formData.datetype=="1"){
					 	data.deliverydate=payMap.delivery_cycledate;
					 }else{
					 	data.deliverydatestr=payMap.deliverydate;
					 }
					 data.payid=null;
					 data.status="1";
					 data.remark=state.formData.remark;
					 data.logisiter=null;
					 data.feelist="";
					 data.paymethod=state.formData.paymethod;
					 purchaselistApi.payment(data).then((res)=>{
					 	if(res.data){
					 		ElMessage.success('操作成功');
					 		state.payMap=res.data;
							state.queryParams.entry.totalpay=res.data.entry.totalpay;
							state.queryParams.entry.paystatus=res.data.entry.paystatus;
							state.queryParams.entry.auditstatus=res.data.entry.auditstatus;
					 		emit("change");
					 	}
					 });
				 }
			  }
			}
		  )
		
	}
	function startPayment(){
		var data={};
		data.entryid=state.queryParams.entry.id;
		data.paytype="out";
		data.costamount=0;
		data.shipamount=0;
		if(state.formData.datetype=="1"){
			data.deliverydate=payMap.delivery_cycledate;
		}else{
			data.deliverydatestr=payMap.deliverydate;
		}
		data.payid=null;
		data.status="2";
		data.remark=state.formData.remark;
		data.logisiter=null;
		data.feelist="";
		data.paymethod=state.formData.paymethod;
		purchaselistApi.payment(data).then((res)=>{
			if(res.data){
				ElMessage.success('操作成功');
				state.payMap=res.data;
				state.queryParams.entry.totalpay=res.data.entry.totalpay;
				state.queryParams.entry.paystatus=res.data.entry.paystatus;
				state.queryParams.entry.auditstatus=res.data.entry.auditstatus;
				emit("change");
			}
		});
		//state.dialogVisable=false;
	}
	function toPaymentPage(){
		router.push({
			path:"/finance/account",
			query:{
				title:'采购记账',
				path:"/finance/account",
			},
		})
	}
	function handleAlibabaOrderFee(fee){
		if(fee.price){
			state.formData.cost=fee.price;
		}
		if(fee.ship){
			state.formData.ship=fee.ship;
		}
	}
	
	function show(type,entry){
		state.queryParams.entry=entry;
		if(type=="pay"){
			state.operateType.dialogTitle = "采购付款"
			state.operateType.tabsType  ="付款"
		}else{
			state.operateType.dialogTitle = "采购退款"
			state.operateType.tabsType  ="退款"
		}
		loadRecord();
		loadFacProject();
		loadPaymentMethod();
		state.dialogVisable = true;
		nextTick(()=>{
	     	orderRecordRef.value.show(entry);
		})
		
	}
	function loadTotalPay(totalamounts){
		state.totalamount=totalamounts;
	}
	
	
	function loadRecord(){
			  purchaselistApi.getRecdetail({"id":state.queryParams.entry.id,"ftype":"pay","actiontype":"all"}).then((res)=>{
				if(res.data){
					state.payMap=res.data;
					nextTick(()=>{
						recordRef.value.show(state.payMap.paylist);
					})
				    
				}
			});
			
	}
	
	defineExpose({
		show,
	})
</script>
<style>

	.mypaymentdailog .el-dialog__footer{
		background:#f5f5f5;
		border-bottom-right-radius:2px;
		border-bottom-left-radius:2px;
		text-align:center;
	}
	.dark .mypaymentdailog .el-dialog__footer{
		background:#1b1b1b;
		border-bottom-right-radius:2px;
		border-bottom-left-radius:2px;
		text-align:center;
	}
	 .mypaymentdailog .el-dialog__body{
		 padding:1px 0px;
		 background-color:#f5f5f5;
	 }
	 .mypaymentdailog {
		 margin: var(--el-dialog-margin-top,15vh) auto 10px;
	 }
</style>
<style scoped="scoped">
	.bg-full{
		height:100%;
	}
	.divider-cell{
		margin-top:16px!important;
		margin-bottom:16px !important;
	}
	.record-box{
		padding:12px;
	}
.el-progress-bar{
	width: 200px;
}
.flex-grow{
	flex:1;
}
.product-box{
	display: flex;
	margin-bottom:16px;
}	
.product-box .el-image{
	margin-right: 16px;
}
.product-box .name{
font-size: 12px;
margin-bottom:8px;
}
.product-box .sku{
font-size: 12px;
color:var(--el-color-blue)
}
.m-t-32{
	margin-top: 32px;
}

.img-40{width: 40px;
height: 40px;flex: none;
margin-right: 8px;}
</style>
