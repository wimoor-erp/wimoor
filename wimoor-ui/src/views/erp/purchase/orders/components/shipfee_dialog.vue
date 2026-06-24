<template>
	<el-dialog v-model="shipfeeVisable" title="自动付运费" width="800px">
			
			<el-form-item label="订单总运费"> 
				<el-input   v-model="shipfee"  @input="shipfee=CheckInputFloat(shipfee)" placeholder="请输入订单总运费" style="width: 200px;" ></el-input>
				<el-button type="primary"  style="margin-left: 15px;" @click="autoShipfeeAll">分摊运费</el-button>
			</el-form-item>
			<el-table :data="shipList" style="margin-bottom: 15px;">
				<el-table-column prop="image" label="图片">
					 <template #default="scope">
						  <img  width="60" height="60" v-if="scope.row.image && scope.row.image!=undefined"   :src="scope.row.image" alt="产品图片" />
						  <img width="60" height="60" v-else   :src="$require('empty/noimage40.png')" alt="暂无图片" />
					 </template>
				</el-table-column>
				<el-table-column prop="sku" label="SKU">
					 <template #default="scope">
					<div class="text-omit-2" v-if="scope.row.name">{{scope.row.name}}</div>
					<div class="text-omit-2" v-if="scope.row.mname">{{scope.row.mname}}</div>
					<p  ><span class="sku">{{scope.row.sku}}</span> </p>
					 </template>
				</el-table-column>
				<el-table-column prop="amount" label="采购数量">
				</el-table-column>
				<el-table-column prop="sku" label="分摊运费金额">
					 <template #default="scope">
						 <el-input v-model="scope.row.itemprice" placeholder="运费" ></el-input>
					 </template>
				</el-table-column>
			</el-table>
			<el-form-item label="支付方式">
				<el-select v-model="feedRow.paymethod" @change="loadPaymentAccount(feedRow.paymethod)">
					 <el-option  v-for="item in payMethodList"   :key="item.id"  :label="item.name" :value="item.id"  ></el-option>
				</el-select>
			</el-form-item>
			 
			<el-form-item label="支付账户"  >
				<el-select v-model="feedRow.payacc">
					 <el-option  v-for="item in payAccList"   :key="item.id"  :label="item.name+'['+item.paymethName+']'" :value="item.id"  >
					 </el-option>
				</el-select>
			</el-form-item>
		  <template #footer>
		  		<el-button @click="submitShipfeeAll"   type="primary">提交</el-button>
		  	<el-button @click="shipfeeVisable=false"  >关闭</el-button>
		  </template>
	</el-dialog>
</template>

<script setup>
	import {h,ref,reactive,toRefs, onMounted,} from 'vue'
	import { ElMessage, ElMessageBox } from 'element-plus';
	import purchaseExtApi from '@/api/erp/purchase/form/extApi.js';
	import faccountApi from '@/api/erp/finances/faccountApi.js';
	import {dateFormat,formatFloat,dateTimesFormat,CheckInputFloat,CheckInputInt} from '@/utils/index.js';
 
	const emit = defineEmits(['change']);
	const state = reactive({
		loading:false,
		shipfee:0,
		shipfeeVisable:false,
		feedRow:{
			paymethod:null,
			payacc:null,
		},
		payAccList:[],payMethodList:[],
		shipList:[],
	})
	const {
		loading,
		shipfee,
		shipfeeVisable,
		feedRow,
		payAccList,payMethodList,
		shipList,
	} = toRefs(state)
 
	 
	function loadPaymentMethod(){
		faccountApi.getPaymentMethod().then((res)=>{
			if(res.data && res.data.length>0){
				state.payMethodList=res.data;
				state.feedRow.paymethod=res.data[0].id;
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
			state.feedRow.payacc=defaultid==""?normalDefault:defaultid;
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
					state.feedRow.payacc=defaultid==""?normalDefault:defaultid;
					 
				}else{
					state.payAccList=[];
					state.feedRow.payacc="";
				}
			});
		}
		
	}
	// function showAutoShipfeeAll(row){
	// 	state.feedRow=row;
	// 	state.shipList=row.itemlist;
	// 	state.shipList.forEach(item=>{
	// 		item.itemprice=null;
	// 	});
	// 	state.shipfeeVisable=true;
	// 	state.shipfee=0;
	// 	loadPaymentMethod();
	// }
	function autoShipfeeAll(){
		//分摊
		var data={};
		data.itemlist=state.shipList;
		data.shipfee=state.shipfee;
		purchaseExtApi.shareFee(data).then((res)=>{
			if(res.data && res.data.length>0){
				ElMessage.success("分摊成功!");
				state.shipList=res.data;
			}
		});
	}
	function submitShipfeeAll(){
		//提交费用
		var amount=0;
		state.shipList.forEach(item=>{
			console.log(formatFloat(item.itemprice));
			amount=amount+formatFloat(item.itemprice);
		})
		if(state.shipfee==formatFloat(amount)){
			purchaseExtApi.autoShipPay(state.feedRow.paymethod,state.feedRow.payacc,state.shipList).then((res)=>{
				ElMessage.success("自动付款运费成功!");
				state.shipfeeVisable=false;
				emit("change");
			});
		}else{
			
			ElMessage.error("自动付款的总运费:"+state.shipfee+"和分摊费用总和:"+formatFloat(amount)+"不相同,请调整各个产品的分摊费用!");
		}
		
	}
	 function show(row){
		state.feedRow=row;
		state.shipList=row.itemlist;
		state.shipList.forEach(item=>{
			item.itemprice=null;
		});
		state.shipfeeVisable=true;
		state.shipfee=0;
		loadPaymentMethod();
	 }
	onMounted(()=>{
		//loadPaymentMethod();
	})
	defineExpose({
	   show,
	})
	
</script>

<style>
</style>