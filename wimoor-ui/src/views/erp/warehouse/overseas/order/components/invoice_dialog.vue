<template>
	<el-dialog v-model="visible" class="invheader" title="TAX INVOICE" top="8vh" width="60%">
		<div style="margin-top: 10px;">
			  <el-divider direction="vertical" style="border-width:3px;border-color:var(--el-color-primary);" /> 发票信息
			  <el-button link style="float:right" type="primary" @click="editable=true">编辑</el-button>
			  <el-divider  style="margin-top:8px"/>
		     <el-form  class="invoice-form" label-width="auto" >
		     <el-form-item label="国家" >
				  <el-select v-model="country" placeholder="国家"  @change="loadData"  >
					<el-option v-for="country in countryOption" :label="country.name" :value="country.market" />
				  </el-select>
		     </el-form-item>
		     <el-form-item label="公司LOGO">
		       <el-upload
		         action="#"
		         list-type="picture-card"
		       	:show-file-list="false"
		       	:limit="1"
		       	:multiple="false"
		       	accept=".png,.jpeg,.jpg,.bmp"
		       	:headers="headers" 
		       	:http-request="uploadFiles"
		       	:before-upload="beforeUpload" 
		       	:on-exceed="handleExceed"
		       	ref="uploadRef"
				:disabled="!editable"
		       	auto-upload="true"
		       >
			   <div v-if="dataForm&&dataForm.id" >
			   	<img  width="100" height="100" style="width:190px;height:80px;padding-top:10px;"  v-if="dataForm&&dataForm.logoUrl && dataForm.logoUrl!=undefined"   :src="dataForm.logoUrl" alt="产品图片" />
			   	<img width="100" height="100" style="width:190px;height:80px;padding-top:10px;"    v-else   :src="$require('empty/noimage40.png')" alt="暂无图片" />
			   </div>
			   <el-icon v-else>
			   	<img  width="100" height="100" style="width:190px;height:80px;padding-top:10px;"   v-if="dataForm&&dataForm.logoUrl && dataForm.logoUrl!=undefined"   :src="dataForm.logoUrl" alt="产品图片" />
				<img width="100" height="100" style="width:190px;height:80px;padding-top:10px;"   v-else   :src="$require('empty/noimage40.png')" alt="暂无图片" />
			   	<Plus />
			   </el-icon>
			   </el-upload>
		     </el-form-item>
			 <el-row  :gutter="20">
				 <el-col :span="12">
					 <el-form-item label="公司名称">
					    <el-input :disabled="!editable" v-model="dataForm.company"></el-input>
					 </el-form-item>
				 </el-col>
				 <el-col :span="12">
					 <el-form-item label="公司简称">
					  <el-input :disabled="!editable" v-model="dataForm.companySimple"></el-input>
					 </el-form-item>
				 </el-col>
			 </el-row>
		    <el-row  :gutter="20">
		    	<el-col :span="12">
					<el-form-item label="电话">
					  <el-input :disabled="!editable" v-model="dataForm.phone"></el-input>
					</el-form-item>
			    </el-col>
				<el-col :span="12">
					<el-form-item label="地址">
					  <el-input :disabled="!editable" v-model="dataForm.address"></el-input>
					</el-form-item>
				</el-col>
			</el-row>
			<el-row  :gutter="20">
				<el-col :span="12">
					<el-form-item label="BANK">
					  <el-input :disabled="!editable" v-model="dataForm.bank"></el-input>
					</el-form-item>
			    </el-col>
				<el-col :span="12">
					<el-form-item label="ACCOUNT">
					 <el-input :disabled="!editable" v-model="dataForm.account"></el-input>
					</el-form-item>
				</el-col>
			</el-row>
			<el-row  :gutter="20">
				<el-col :span="12">
					<el-form-item label="ABN">
					 <el-input :disabled="!editable" v-model="dataForm.abn"></el-input>
					</el-form-item>
			    </el-col>
				<el-col :span="12">
					<el-form-item label="税率">
					  <el-input :disabled="!editable" v-model="dataForm.rate"></el-input>
					</el-form-item>
				</el-col>
			</el-row>
			<el-row  :gutter="20">
				<el-col :span="12">
					<el-form-item label="发票号前缀">
					 <el-input :disabled="!editable" v-model="dataForm.province"></el-input>
					</el-form-item>
			    </el-col>
				<el-col :span="12">
					<el-form-item label="BILL TO">
					  <el-input :disabled="!editable" v-model="dataForm.billto"></el-input>
					</el-form-item>
				</el-col>
			</el-row>
			<el-row>
				<el-col :span="2" :offset="20">
						<el-button @click="handleCancel"  :disabled="!editable" >取消</el-button>
				</el-col>
				<el-col :span="2" >
						<el-button @click="handleSave" :disabled="!editable" type="primary">保存</el-button>
				</el-col>
			</el-row>
		   </el-form>
	        <el-divider direction="vertical" style="border-width:3px;border-color:var(--el-color-primary)" /> 交易信息
	        <el-divider  style="margin-top:8px"/>
	        <el-form :inline="true"  class="invoice-form">
	            <el-form-item label="发票号">
	              <el-input v-model="invoiceInfo.invNo"></el-input>
	            </el-form-item>
	            <el-form-item label="日期">
	              <el-date-picker
	                type="date"
					 v-model="invoiceInfo.date"
	                placeholder="点击选择日期"
	                clearable
	              />
	            </el-form-item>
	            <el-form-item label="Sales Rep">
	              <el-input v-model="invoiceInfo.sales"></el-input>
	            </el-form-item>
	          </el-form>
				<el-table :data="list">
					<el-table-column label="商品描述"   prop="name" >
					     <template #default="scope">
						   <el-input v-model="scope.row.name"></el-input> 
						 </template>
					</el-table-column>
					<el-table-column label="ITEM No."    >
					     <template #default="scope">
							<el-input v-model="scope.row.item"></el-input> 
						 </template>
					</el-table-column>
					<el-table-column label="售价(不含税)">
					<template #default="scope">
						    {{scope.row.price}}
						 </template>
					</el-table-column>
					<el-table-column label="数量">
					<template #default="scope">
							<div><el-input v-model="scope.row.quantity"  @input="handleQty(scope.row)"></el-input> </div> 
						 </template>
					</el-table-column>
					<el-table-column label="销售额(不含税)">
					<template #default="scope">
							<div>{{scope.row.totalPrice}}</div> 
						 </template>
					</el-table-column>
					<el-table-column label="税费"    >
					<template #default="scope">
							<div>{{scope.row.tax}}</div> 
						 </template>
					</el-table-column>
					<el-table-column label="合计总计(含税)" >
						<template #default="scope">
								<el-input v-model="scope.row.total" @input="handleQty(scope.row)"></el-input> 
							 </template>
					</el-table-column>
					<el-table-column label="操作" >
						<template #default="scope">
								<el-space><el-button link type="primary" @click="handleAddRow">新增</el-button><el-button link type="danger" @click="deleteRow(scope.$index)">删除</el-button></el-space> 
							 </template>
					</el-table-column>
				</el-table>	  
				 <el-row>
					 <el-col :span="24">
						 <div style="height:80px">
						 	<div class="summary-footer">
						 		<div class="flex-between"><div class="label-width text-right">不含税合计：</div><div class="num-width">{{summary.totalPrice}}</div></div>
						 		<div class="flex-between"><div class="label-width text-right">税费：</div><div class="num-width">{{summary.tax}}</div></div>
						 		<div class="flex-between"><div class="label-width text-right">合计：</div><div class="num-width">{{summary.total}}</div></div>
						 		<div class="flex-between"><div class="label-width text-right">是否已付费：</div><div class="num-width" style="margin-top:-5px;"><el-checkbox v-model="state.invoiceInfo.ispay"></el-checkbox></div></div>
						 	</div>
						 </div>
					 </el-col>
				</el-row>
				</div>
		   <template #footer>
		   	<span class="dialog-footer">
		  <!-- 		<el-button @click="showDeatil()">预览发票</el-button> -->
		   		<el-button type="primary" @click="downloadInvoice">下载发票 </el-button>
		   	</span>
		   </template>
	</el-dialog>
   <!-- <InvoiceViewDialog ref="invoiceViewDialogRef"></InvoiceViewDialog> -->
</template>

<script setup>
	import {ref,reactive,onMounted,watch,h,toRefs,computed, nextTick} from 'vue'
	import {Help,Plus,MenuUnfold,SettingTwo} from '@icon-park/vue-next';
	import {ElMessage,ElDivider,genFileId} from 'element-plus';
	import MaterialDialog from "@/views/erp/baseinfo/material/materialDialog.vue";
	/* import InvoiceViewDialog from "./invoice_view_dialog.vue"; */
	import orderApi from "@/api/erp/order/orderApi.js";
	import orderInvoiceApi from "@/api/erp/order/orderInvoiceApi.js";
	import materialApi from "@/api/erp/material/materialApi.js";
	import planFormApi from "@/api/erp/order/planFormApi.js";
	import serialnumberApi from '@/api/erp/common/serialnumberApi.js';
	import {dateFormat,dateTimesFormat,formatFloat} from '@/utils/index.js';
	import CountryName from "@/model/erp/order/countryName.json";
	const emit = defineEmits(['change']);
	const state =reactive({
		visible:false,
		editable:true,
		imgfile:null,
		countryOption:[],
		invoiceInfo:{sales:"KG",date:new Date(),invNo:"MEN"},
		country:"",
		dataForm:{},
		list:[],
	})
	const invoiceViewDialogRef=ref();
	const uploadRef=ref();
	const {
		visible,imgfile,dataForm,countryOption,country,editable,invoiceInfo,list
	}=toRefs(state)
 //文件上传之前
 function beforeUpload(file){
 	if (file.type != "" || file.type != null || file.type != undefined){
 	    //截取文件的后缀，判断文件类型
 		//const FileExt = file.name.replace(/.+\./, "").toLowerCase();
 		//计算文件的大小
 		const isLt5M = file.size / 1024  < 5000; //这里做文件大小限制
 		//如果大于50M
 		if (!isLt5M) {
 			ElMessage.error('上传文件大小不能超过 5000KB!!');
 			return false;
 		}
 		else {
 		   state.dataForm.logoUrl=URL.createObjectURL(file);
 		   return true;
 		}
 	}
 }
 const summary = computed(() => {
 	var summary={totalPrice:0,total:0,tax:0};
 	state.list.forEach(row=>{
 		 summary.totalPrice=formatFloat(summary.totalPrice+parseFloat(row.totalPrice));
		 summary.total=formatFloat(summary.total+parseFloat(row.total));
		 summary.tax=formatFloat(summary.tax+parseFloat(row.tax));
 	});
 	return summary;
     }
 )
 function uploadFiles(item){
 	//上传文件的需要formdata类型;所以要转
 	state.imgfile=item.file;
 }
 function showDeatil(){
	 invoiceViewDialogRef.value.show(state);
 }
 function loadData(){
	 if(state.country){
		 orderInvoiceApi.getInvoice({"country":state.country}).then(res=>{
		 		 state.dataForm=res.data;
				 if(!state.dataForm){
					 state.dataForm={};
					 state.editable=true;
				 }else if(state.dataForm.id){
					 state.editable=false;
				 }else{
					 state.editable=true;
				 }
				 if(state.dataForm.province){
					 serialnumberApi.getSerialNumber({"ftype":state.dataForm.province,"isfind":"false"}).then((res)=>{
					 	if(res.data){
					 		state.invoiceInfo.invNo=res.data; 
					 	}
					 });
				 }
		 })
	 }
	
 }
 function deleteRow(index){
 	state.list.splice(index,1);
 }	
 function handleAddRow(){
	 state.list.push({total:0,tax:0,totalPrice:0,quantity:0,price:0});
 }
 function handleSave(){
	 let FormDatas = new FormData()
	 if(state.imgfile){
	    FormDatas.append('file',state.imgfile);
	 }else{
	 	FormDatas.append('file',{});
	 }
	 FormDatas.append('country',state.country);
	 FormDatas.append('image',state.dataForm.image);
	 FormDatas.append("company",state.dataForm.company);
	 FormDatas.append("companySimple",state.dataForm.companySimple);
	 FormDatas.append("phone",state.dataForm.phone);
	 FormDatas.append("logoUrl",state.dataForm.logoUrl);
	 FormDatas.append("rate",state.dataForm.rate);
	 FormDatas.append("abn",state.dataForm.abn);
	 FormDatas.append("billto",state.dataForm.billto);
	 FormDatas.append("account",state.dataForm.account);
	 FormDatas.append("city",state.dataForm.city);
	 FormDatas.append("address",state.dataForm.address);
	 FormDatas.append("bank",state.dataForm.bank);
	 FormDatas.append("province",state.dataForm.province);
	 orderInvoiceApi.saveInvoice(FormDatas).then(res=>{
		 loadData()
		 ElMessage.success('保存成功');
	 })
 }
 
 function handleExceed(files){
 	uploadRef.value.clearFiles();//清空图片list
 	const file=files[0];
 	file.uid = genFileId();
 	uploadRef.value.handleStart(file);//手动选择图片
 	state.dataForm.logoUrl=URL.createObjectURL(file);
 	uploadRef.value.submit();//上传图片
 }
 
 function handleQty(row){
	 var rate=0
	 if(row.quantity&&row.total){
		 if(state.dataForm.rate){
		 	 if(state.dataForm.rate.indexOf("%")>0){
		 		rate=parseFloat(state.dataForm.rate.replace("%",""))/100;
		 	 }else{
		 		rate=parseFloat(state.dataForm.rate); 
		 	 }
		  } 
		 row.totalPrice=formatFloat(parseFloat(row.total)/(1+rate));
		 row.price=formatFloat(row.totalPrice/parseFloat(row.quantity));
		 row.tax= formatFloat(row.totalPrice*rate);
	 }
	
 }
 function downloadInvoice(){
	 var summary={totalPrice:0,total:0,tax:0};
	 state.list.forEach(row=>{
	 	 summary.totalPrice=formatFloat(summary.totalPrice+parseFloat(row.totalPrice));
	 	 summary.total=formatFloat(summary.total+parseFloat(row.total));
	 	 summary.tax=formatFloat(summary.tax+parseFloat(row.tax));
	 });
	 
	 var param={"{国家}":CountryName[state.country],
	            "{图片}":state.dataForm.logoUrl,
				"{公司简称}":state.dataForm.companySimple,
	            "{公司}":state.dataForm.company,
				"{电话}":state.dataForm.phone,
				"{税率}":state.dataForm.rate,
				"{ABN}":state.dataForm.abn,
				"{BILLTO}":state.dataForm.billto,
				"{ACCOUNT}":state.dataForm.account,
				"{城市}":state.dataForm.city,
				"{地址}":state.dataForm.address,
				"{BANK}":state.dataForm.bank,
				"{总价}":summary.totalPrice,
				"{总税费}":summary.tax,
				"{总费用}":summary.total,
				"{日期}":state.invoiceInfo.date,
				"{发票号}":state.invoiceInfo.invNo,
				"{Sales Rep}":state.invoiceInfo.sales};
	 if(state.invoiceInfo.ispay){
		 param["{图片.是否已付费}"]="https://wimoor-file.oss-cn-shenzhen.aliyuncs.com/sys/photos/paid.png";
	 }else{
		 param["{图片.是否已付费}"]="";
	 }
	 param.列表=[];
	  state.list.forEach(item=>{
		  var myitem={"{列表.商品描述}":item.name,
		              "{列表.ITEM}":item.item,
					  "{列表.售价}":item.price,
					  "{列表.数量}":item.quantity,
					  "{列表.税费}":item.tax,
					  "{列表.总价}":item.totalPrice,
					  "{列表.总费用}":item.total}
		  param.列表.push(myitem);
	  });
	 orderInvoiceApi.downInvoice(param);
 }
	function show(data,query){
		state.list=[];
		state.visible=true;
		var mydata=JSON.parse(JSON.stringify(data));
		mydata.total=0;
		mydata.tax=0;
		mydata.totalPrice=0;
		mydata.quantity=0;
		mydata.price=0;
		state.list.push(mydata);
		nextTick(()=>{
			orderApi.countrys().then(res=>{
				state.countryOption=res.data;
				loadData()
			})
		})
	}
	function handleCancel(){
		 loadData();
	}
	defineExpose({
		show,
	})
</script>

<style>
	.label-width{
		width:140px;
		float:left;
	}
	.num-width{
		 float:right;
	}
	.summary-footer{
		padding-top:20px;
		padding-bottom:20px;
		float:right;
	}
	.summary-footer div{
		padding-top:3px;
		padding-bottom:3px;
	}
	.summary-footer span{
		 font-weight: 600;
	}
	.invoice-form{
		padding-left:40px;
		padding-right:40px;
	}
	.invheader .el-dialog__body{
		padding-top:0px;
	}
	.invheader .el-upload--picture-card {
			width: 200px;
			height: 80px;
	}
</style>