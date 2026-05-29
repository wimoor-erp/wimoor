<template>
	<el-dialog v-model="visable" title="自动付款-支付方式">
		<el-row :gutter="24">
		 <el-col :span="12">
		<el-form-item label="支付方式">
			<el-select v-model="formData.paymethod" @change="loadPaymentAccount(formData.paymethod)">
				 <el-option  v-for="item in payMethodList"   :key="item.id"  :label="item.name" :value="item.id"  ></el-option>
			</el-select>
		</el-form-item>
		</el-col>
		<el-col :span="12">
		<el-form-item label="支付账户"  >
			<el-select v-model="formData.payacc">
				 <el-option  v-for="item in payAccList"   :key="item.id"  :label="item.name+'['+item.paymethName+']'" :value="item.id"  >
				 </el-option>
			</el-select>
		</el-form-item>
		</el-col>
		</el-row>
		<template #footer>
				 <el-button @click="visable=false">取消</el-button>
				 <el-button type="primary" :loading="submitloading" @click.stop="handleSubmit">确认</el-button>
		</template>
	</el-dialog>
</template>

<script setup>
	import {h,ref,reactive,toRefs,watch,} from 'vue';
	import { ElMessage, ElMessageBox } from 'element-plus';
	import faccountApi from '@/api/erp/finances/faccountApi.js';
	import purchaselistApi from '@/api/erp/purchase/form/listApi.js';
	const emit = defineEmits(['change']);
	let state=reactive({
		visable:false,nowrow:{},idslist:null,formData:{"paymethod":null,"payacc":null},
		payAccList:[],payMethodList:[],submitloading:false,
	});
	let{
		visable,nowrow,idslist,formData,payAccList,payMethodList,submitloading
		}=toRefs(state);
	
	function handleSubmit(){
		state.submitloading=true;
		purchaselistApi.autopay(state.idslist,state.formData.paymethod,state.formData.payacc).then((res)=>{
				ElMessage.success('自动付款成功！');
				state.submitloading=false;
				state.visable=false;
				emit("change",state.idslist,res.data,state.nowrow);
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
   
   function show(idlist,row){
	   loadPaymentMethod();
	   state.nowrow=row;
	   state.idslist=idlist;
	   state.visable=true;
   }
   defineExpose({
      show,
   })
</script>

<style>
</style>