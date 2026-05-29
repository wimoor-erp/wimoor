<template>
	<div class="el-white-bg">
	<div class="gird-line-head">
		<h3>添加利润计算方案</h3>
		<el-button   class='ic-btn' title='帮助文档'>
		  <help theme="outline" size="16" :strokeWidth="3"/>
		</el-button>
	</div>
	<div class="list-from m-l-r-16"  >
		<el-row>
			<el-col>
		        <el-form label-width="260px" ref="dataFormRef" :model="formData" :rules="rules">
					<el-row>
						<el-col :span="12">
			      <el-form-item label="利润计算方案名称" prop="name" >
					  <el-input
					      v-model="formData.name"
					      placeholder="请输入"
					      type="text"
					    />
				  </el-form-item>
				  <el-form-item label="卖家销售计划" prop="sellerplan">
				        <el-radio-group v-model="formData.sellerplan">
				          <el-radio :label="item.value"  v-for="item in sellerplanOptions">{{item.name}}</el-radio>
				        </el-radio-group>
				      </el-form-item>
					  <el-form-item label="销售渠道及发货方式" >
				       <el-select v-model="formData.salesChannel">
						   <el-option v-for="item in salesChannelOptions"  :label="item.name" :value="item.value"/>  
					   </el-select>
				      </el-form-item>
					  <el-form-item label="运费计算方式" >
				       <el-select v-model="formData.shipmentstyle">
						   <el-option v-for="item in shipmentStyleOptions" :label="item.name" :value="item.value"/>
					   </el-select>
				      </el-form-item>
				  </el-col>
				  </el-row>
				  <el-tabs v-model="activeCountry"  @tab-click="handleClick" class="label-form-item">
					   <el-tab-pane label="美国" name="us">
						     <Form :marketData="formData.countryMap.US" 
								   :fbaMonthsOptions="fbaMonthsOptions"
								   :shipmentstyle="formData.shipmentstyle"
								   :manualProcessingFeeOptions="manualProcessingFeeUSOptions"
								   :inplacefeeOptions="inplacefeeOptionsUS"
								   country="US"
								   currency="USD"
								   currencyicon="$"
							 />
					   </el-tab-pane>
					   <el-tab-pane label="英国" name="uk">
						      <Form :marketData="formData.countryMap.UK"
							        :shipmentstyle="formData.shipmentstyle"
							        :storageFee="storageFeeMap.storageFeeMap_UK"
									country="UK"
									currency="GBP"
									currencyicon="£"
							  />
					   </el-tab-pane>
					   <el-tab-pane label="欧洲" name="eur">
						      <Form :marketData="formData.countryMap.DE"
							        :shipmentstyle="formData.shipmentstyle"
									:storageFee="storageFeeMap.storageFeeMap_DE"
									:countryMapData="formData.countryMap"
									country="EU"
									currency="EUR"
									currencyicon="€"
									/>
					   </el-tab-pane>
					   <el-tab-pane label="日本" name="jp">
						      <Form :marketData="formData.countryMap.JP"
							  		:shipmentstyle="formData.shipmentstyle"
									:inplacefeeOptions="inplacefeeOptionsJP"
									:storageFee="storageFeeMap.storageFeeMap_JP"
									country="JP"
									currency="JPY"
									currencyicon="¥"/>
					   </el-tab-pane>
					   <el-tab-pane label="加拿大" name="ca">
						      <Form :marketData="formData.countryMap.CA"
							        :shipmentstyle="formData.shipmentstyle"							  
									:storageFee="storageFeeMap.storageFeeMap_CA"
									country="CA"
									currency="CAD"
									currencyicon="C$"/>
					   </el-tab-pane>
					   <el-tab-pane label="澳大利亚" name="au">
						      <Form :marketData="formData.countryMap.AU"
							        :shipmentstyle="formData.shipmentstyle"							  
									:storageFee="storageFeeMap.storageFeeMap_AU"
									country="AU"
									currency="AUD"
									currencyicon="A$"/>
					   </el-tab-pane>
					   <el-tab-pane label="印度" name="in">
						      <Form :marketData="formData.countryMap.IN"
							        :shipmentstyle="formData.shipmentstyle"							  
									:storageFee="storageFeeMap.storageFeeMap_IN"
									country="IN"
									currency="INR"
									currencyicon="₹"/>
					   </el-tab-pane>
					   <el-tab-pane label="墨西哥" name="mx">
						      <Form :marketData="formData.countryMap.MX"
							        :shipmentstyle="formData.shipmentstyle"							  
									:storageFee="storageFeeMap.storageFeeMap_MX"
									country="MX"
									currency="MXN"
									currencyicon="Mex$"/>
					   </el-tab-pane>
					   <el-tab-pane label="阿联酋" name="ae">
						      <Form :marketData="formData.countryMap.AE"
							        :shipmentstyle="formData.shipmentstyle"							  
									:storageFee="storageFeeMap.storageFeeMap_AE"
									country="AE"
									currency="AED"
									currencyicon="AED"/>
					   </el-tab-pane>
					   <el-tab-pane label="沙特阿拉伯" name="sa">
						      <Form :marketData="formData.countryMap.SA"
							        :shipmentstyle="formData.shipmentstyle"							  
									:storageFee="storageFeeMap.storageFeeMap_SA"
									country="SA"
									currency="SAR"
									currencyicon="SAR"/>
					   </el-tab-pane>
				  </el-tabs>
				  <el-form-item >
				  	<el-button @click.stop="submitForm" type="primary">提交</el-button>
				  	<el-button @click.stop="resetForm">重置</el-button>
				  	<el-button @click.stop="closeTab">取消</el-button>
				  </el-form-item> 
		        </el-form>
	    	</el-col>
		</el-row>
	</div>
	</div>
</template>

<script setup>
	import {ref,reactive,onMounted,toRefs,getCurrentInstance,inject}from"vue"
	import configApi from "@/api/amazon/profit/config"
	import { useRoute,useRouter } from 'vue-router'
	import countryData from '@/model/amazon/profit/configData.json';
	import Form from"./market_fee/form.vue"
	import {Help} from '@icon-park/vue-next'
	import { ElForm, ElMessage, ElMessageBox } from 'element-plus';
	const { proxy } = getCurrentInstance();
	const dataFormRef=ref();
	let router = useRouter();
	const state=reactive({
		formData:{shipmentstyle:"dim_weight",sellerplan:"profession",salesChannel:"amz_fba",countryMap:countryData},
		backupFormData:{},
		rules: {
		  name: [{ required: true, message: '名称不能为空', trigger: 'blur' }],
		},
		fbaMonthsOptions:[{label:"1月-9月",value:1},
		                  {label:"10月-12月",value:12},
						  {label:"自动使用当前月",value:0}],
	    dispatchTypeEUOptions:[{label:"Cross-border fulfilment(EFN)",value:"EFN"},
		                       {label:"Pan EU",value:"PAN_EU"}],
		sellerplanOptions:[],
		salesChannelOptions:[],
		shipmentStyleOptions:[],
		inplacefeeOptionsUS:[],
		inplacefeeOptionsJP:[],
		manualProcessingFeeUSOptions:[],
		storageFeeMap:{},
		activeCountry:"us",
	});
	const {
	  formData,
	  rules,
	  activeCountry,
	  fbaMonthsOptions,
	  sellerplanOptions,
	  salesChannelOptions,
	  shipmentStyleOptions,
	  inplacefeeOptionsUS,
	  inplacefeeOptionsJP,
	  storageFeeMap,
	  manualProcessingFeeUSOptions,
	} = toRefs(state);
	 function checkData(country){
		 if(!state.formData.countryMap[country]||!state.formData.countryMap[country].country){
		 	state.formData.countryMap[country]=countryData[country];
		 }
	 }
	 const emitter = inject("emitter"); // Inject `emitter`
	 function closeTab(){
	 	emitter.emit("removeTab", 100);
	 };
	 function resetForm() {
	      dataFormRef.value.resetFields();
		  state.formData=state.backupFormData;
		  state.backupFormData=JSON.parse(JSON.stringify(state.formData))
	   }
	function loadData(id){
		configApi.findConfig({"id":id}).then(res=>{
			state.formData=res.data;
			checkData("US"); checkData("CA");checkData("MX");
			checkData("DE"); checkData("FR"); checkData("IT");
			checkData("ES"); checkData("NL"); checkData("PL");
			checkData("SE"); checkData("UK");
			checkData("AE"); checkData("AU");
			checkData("JP"); checkData("IN"); 
			checkData("SA");checkData("BE");
			state.backupFormData=JSON.parse(JSON.stringify(state.formData))
		});
	}
	function loadOptions(){
		 proxy.listDictsByCode("sellerplan").then(res=>{
			state.sellerplanOptions=res.data;
		 }); 
		 proxy.listDictsByCode("salesChannel").then(res=>{
			state.salesChannelOptions=res.data;
		 }); 
		 proxy.listDictsByCode("shipmentstyle").then(res=>{
			state.shipmentStyleOptions=res.data;
		 }); 
		 configApi.findManualProcessingFee().then(res=>{
			state.manualProcessingFeeUSOptions=res.data;
		 });
		configApi.storageFee().then(res=>{
			state.storageFeeMap=res.data;
		});
		configApi.findInplacefee("US").then(res=>{
			 state.inplacefeeOptionsUS=res.data;
		});
		configApi.findInplacefee("JP").then(res=>{
			 state.inplacefeeOptionsJP=res.data; 
		});
	}
	function submitForm() {
	  dataFormRef.value.validate((isValid) => {
		if (isValid) {
			   configApi.editConfig(state.formData).then(res=>{
				   state.formData=res.data;
				   checkData("US"); checkData("CA");checkData("MX");
				   checkData("DE"); checkData("FR"); checkData("IT");
				   checkData("ES"); checkData("NL"); checkData("PL");
				   checkData("SE"); checkData("UK");
				   checkData("AE"); checkData("AU");
				   checkData("JP"); checkData("IN"); 
				   checkData("SA");
				   state.backupFormData=JSON.parse(JSON.stringify(state.formData))
				     ElMessage.success('保存成功');
			   })
			}
	    });
    }
	onMounted(()=>{
		var id = router.currentRoute.value.query.id;
		loadOptions();
		if(id){
		  loadData(id);
		}else{
			state.backupFormData=JSON.parse(JSON.stringify(state.formData))
		}
	})
</script>

<style>
	.list-from{margin-top:16px;}
	.m-l-r-16{
		margin-right: 24px;
		margin-left:24px;
	}
    .label-form-item .el-form-item--default .el-form-item__label{
		line-height: inherit;
	}
	.label-form-item p{
		color: var(--el-text-color-secondary);
	}
</style>
