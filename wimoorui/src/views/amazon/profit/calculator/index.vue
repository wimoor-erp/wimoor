<template>
		<el-row>
			<el-col  :span="7" class="el-white-bg">
				<div class="gird-line-head">
					<el-space>
						<el-select style="width:90px;" v-model="queryParams.searchMarket">
							<el-option v-for="(item,index) in marketOptions" :value="item.marketplaceid" :label="item.name">
								<el-space>
								  <el-image class="flex-center" :src="$require('country/'+item.market+'-flag-small.jpg')">></el-image>
								  {{item.name}}
								</el-space>
							</el-option>					  
						</el-select>
						<el-input  v-model="queryParams.searchKeywords" @input="sloading=false" clearable placeholder="请输入" class="input-with-select" >
						   <template #prepend>
						     <el-select v-model="queryParams.searchType"  placeholder="SKU" style="width:80px">
						       <el-option label="SKU" value="SKU"></el-option>
						       <el-option label="ASIN" value="ASIN"></el-option>
							  </el-select> 
						   </template>
						   <template #append>
						     <el-button @click.stop="searchParam" :loading="sloading">
						        <el-icon v-show="sloading==false" class="font-medium ic-cen">
						           <search />
						         </el-icon>
						     </el-button>
						   </template>
						 </el-input> 
						 </el-space>
				</div>
				<div class="gird-line-left">
				<el-form  :model="formData" label-position="top" :rules="rules">
					<el-form-item label="利润计算方案"> 
					   <div class="flex-center w-grow">
						<el-select class="w-grow m-r-8" v-model="formData.profitCfgId" @change="cfgChange">
							<el-option v-for="item in configOptions"  :value="item.id"  :label="item.name"></el-option>
						</el-select>
						<el-button @click="planConfig">方案配置</el-button>
					   </div>
					</el-form-item>
					<el-form-item label="采购成本" prop="cost"> 
                        <el-input  v-model="formData.cost" @input="formData.cost=CheckInputFloat(formData.cost)">
							<template #append>
								<el-select v-model="formData.currency" class="w-8">
									<el-option v-for="item in currencyOptions"  :value="item.value"  :label="item.label"></el-option>
								</el-select>
							</template>
						</el-input>
					</el-form-item>
					<el-form-item label="重量" style="margin-bottom: 0px;" prop="weight">
					    <el-input v-model="formData.weight" @input="changeWeight">
					    	<template #append>
					    		<el-select v-model="formData.wunit" class="w-8"  @change="changeWeight">
					    			<el-option v-for="item in weightOptions"  :value="item.value"  :label="item.label"></el-option>
					    		</el-select>
					    	</template>
					    </el-input>
						<span class="font-extraSmall">{{formData.weight2}}</span> 
						<span class="font-extraSmall" style="padding-left:10px;">{{formData.weight3}}</span>
					</el-form-item>
					<el-form-item style="margin-bottom: 0px;" label="产品尺寸" :prop="['length']" :rules="{ required: true, message: '不能为空',trigger: 'blur',}">
					 <div>
						<div class="flex-center w-grow">
							<el-row class="w-grow m-r-4" :gutter="8">
								<el-col :span="8" >
									<el-input placeholder="长" @input="changeLength"  v-model="formData.length"></el-input>
								</el-col>
								<el-col :span="8">
									<el-input placeholder="宽" @input="changeLength"  v-model="formData.width"></el-input>
								</el-col>
								<el-col :span="8">
									<el-input placeholder="高" @input="changeLength"  v-model="formData.height"></el-input>
								</el-col>
							</el-row>
							<el-select v-model="formData.lunit" class="w-8" @change="changeLength">
								<el-option v-for="item in sizeOptions"  :value="item.value"  :label="item.label"></el-option>
							</el-select>
						</div> 
						</div> 
					 
							 <el-col :span="7" >
							   <span class="font-extraSmall">{{formData.length2}}</span>
							 </el-col>
							 <el-col :span="6">
							   <span class="font-extraSmall">{{formData.width2}}</span>
							 </el-col>
							 <el-col :span="11">
							   <span class="font-extraSmall">{{formData.height2}}</span>
							</el-col>
						 
					</el-form-item>
					<el-form-item label="产品品类">
						<el-select v-model="formData.typeId" filterable class="w-grow">
							<el-option v-for="item in typeOptions"  :value="item.id"  :label="item.type"></el-option>
						</el-select>
					</el-form-item>
					<el-form-item label="产品运费" v-if="manuShipment">
						<el-input v-model="formData.shipment"></el-input>
					</el-form-item>
					
					<el-form-item>
						<el-row class="w-grow m-r-4" :gutter="8">
						<el-button type="primary" @click.stop="calculateProfit">计算成本利润</el-button>
						</el-row>
						 <el-select style="width:100px;margin-right:10px;" v-model="formData.sipp" size="small">
							 <el-option :key="true"  label="使用SIPP" :value="true"> </el-option>
							 <el-option :key="false" label="不使用SIPP" :value="false"> </el-option>
							 <el-option :key="null"  label="自动" :value="null"> </el-option>
						 </el-select>
						<span class="font-extraSmall" style="padding-right:3px;">
							  <el-tooltip
							    raw-content
							  >
							   <template #content> 
							   <p>英国售价范围，不大于10英镑</p>
							   <p>德国售价范围，不大于11欧元</p>
							   <p>法国、意大利，荷兰，西班牙 售价范围，不大于12欧元</p>
							   <p>瑞典售价范围，不大于140克朗</p>
							   <p>波兰售价范围，不大于55波兰兹罗提</p>
							   <p>美国售价范围，不大于10美元</p>
							   <p>加拿大售价范围，不大于14加元</p>
							   </template>
							    是否低价 
							  </el-tooltip>
							 </span>
						<el-switch
						    v-model="formData.isSmlAndLightStr"
						    inline-prompt
						    active-text="是"
							active-value="true"
						    inactive-text="否"
							inactive-value="false"
							@change="reloadSLData"
						  />
					</el-form-item>
				</el-form>
				<el-divider v-if="'点击了计算按钮'"></el-divider>
				<el-button v-if="'填写了国家售价显示'" @click="handleProfitDetails" class="w-100 m-t-16" size="large" text bg>
					<div class="flex-center-between w-100">
						<div class="flex-center ">
						<consume theme="filled" size="20" fill="#e6a23c"/>
						<span>&nbsp;成本明细</span>
						</div>
						<span class="text-right">
							<arrow-right theme="filled" size="16" />
						</span>
						
					</div>
				</el-button>
				<ProfitDetails ref="profitDetailsRef" 
				:isSmlAndLight="formData.isSmlAndLightStr" 
				:tableData="formData" 
				:countryOptions="countryMarketOptions"/>
				<div>
				<el-button v-if="'点击了计算按钮'" @click="handleFbaSize" class="w-100 m-t-16" size="large" text bg>
					<div class="flex-center-between w-100">
						<div class="flex-center ">
						<modify theme="filled" size="20" fill="#67c23a"/>
						<span>&nbsp;FBA费用各国标准尺寸对照</span>
						</div>
						<span class="text-right">
							<arrow-right theme="filled" size="16"  />
						</span>
					</div>
				</el-button>
				<FbaSize ref="fbaSizeRef" 
				:isSmlAndLight="formData.isSmlAndLightStr" 
				:tableData="formData" 
				:countryOptions="countryMarketOptions"/>
				</div>
			</div>	
			</el-col>
			<!-- 右边//右边///////////////////////////////////////////// -->
			<el-col  :span="17">
				<el-scrollbar class="height-screen gary-bg">
				<div class="gird-line-right ">
					<el-row :gutter="16">
					   <CountryCollapse 
					   @dataChange="marketPriceInput"
					   @rateChange="calculateProfit"
					    v-for="market in  countryMarketOptions" 
					   :country="market" 
					   :marginOptions="marginOptions"
					   :tableData="formData">
					   </CountryCollapse>
					</el-row>
				</div>
				</el-scrollbar>
			</el-col>
		</el-row>
</template>
<script>
    export default{ name:"利润计算" };
</script>
<script setup>
	import { ref ,watch,reactive,onMounted,toRefs} from 'vue'
	import {ArrowDown,Search,Coin } from '@element-plus/icons-vue';
	import {Consume,ArrowRight,Modify} from '@icon-park/vue-next';
	import ProfitDetails from"./components/profit_deatils.vue"
	import FbaSize from'./components/fba_size.vue'
	import CountryCollapse from'./components/country_collapse.vue'
	import { useRoute,useRouter } from 'vue-router'
	import calculateApi from "@/api/amazon/profit/calculateApi"
	import {CheckInputFloat,CheckInputInt,formatFloat} from '@/utils/index.js';
	import materialApi from "@/api/erp/material/materialApi"
	import productinfoApi from "@/api/amazon/product/productinfoApi"
	import {  ElMessage } from 'element-plus';
	let router  = useRouter();
	let profitDetailsRef =ref();
	let fbaSizeRef = ref();
	let state = reactive({
		//下拉选项
		loading:false,
		manuShipment:false,
		sloading:false,
		currencyOptions:[{label:'RMB',value:'RMB'},{label:'USD',value:'USD'}],
		weightOptions:[{label:'kg',value:'kg'},{label:'lb',value:'lb'},{label:'oz',value:'oz'}],
		sizeOptions:[{label:'cm',value:'cm'},{label:'in',value:'in'}],
		marketOptions:[],
		typeOptions:[],
		configOptions:[],
		countryOptions:[],
		marginOptions:[],
		countryMarketOptions:[],
		queryParams:{searchMarket:"ATVPDKIKX0DER",searchKeywords:"",searchType:"SKU"},
		tableData:{},
		formData:{
				   profittype:'',
				   profitCfgId:'',// 利润计算方案
				   shipment:'',
				   typeId:41, 
				   cost:'',// 高
				   currency:'RMB',
				   weight:'',// 重量
				   weight2:'',// 重量
				   weight3:'',// 重量
				   wunit:'kg',// 重量单位
				   lunit:'cm',// 长度单位
				   length:'',// 长
				   width:'',// 宽
				   height:'',// 高
				   length2:'',// 长
				   width2:'',// 宽
				   height2:'',// 高
				   categories:'',
				   shipmentType:'national',//计算印度利润，local,regional,national
				   declaredValue:'',//申报价值
				   declaredValueCur:'',//申报价值单位
				   taxrate:'',//印度进口税率
				   gstrate:'',//印度进口GST税率
				   sellingGSTRate:'',//印度销售GST税率
				   referralrate:'',//印度佣金比率
				   isSmlAndLightStr:'false',//是否轻小
				   sipp:null,
	},
	rules:{
		   cost:[ 
					{ required: true, message: '不能为空', trigger: 'blur' },
				 ],
		   weight:[ 
					{ required: true, message: '不能为空', trigger: 'blur' },
				  ],
	    }
	})
 
	 const {
	   loading,
	   manuShipment,
	   queryParams,
	   tableData,
	   sloading,
	   formData,
	   rules,
	   currencyOptions,
	   weightOptions,
	   sizeOptions,
	   typeOptions,
	   marketOptions,
	   configOptions,
	   countryOptions,
	   marginOptions,
	   countryMarketOptions,
	 } = toRefs(state);
function cfgChange(){
	state.manuShipment=false;
	state.formData.shipment='';
	if(state.formData.profitCfgId){
		state.configOptions.forEach(item=>{
			if(item.id==state.formData.profitCfgId){
				 if(item.shipmentstyle=='manually'){
					 state.manuShipment=true;
				 }
				
			}
		})
	}
 
}
function changeLength() {//改变尺寸
	var length =state.formData.length;
	var l =state.formData.lunit;
	var width =state.formData.width;
	var height =state.formData.height;
	state.formData.width=CheckInputFloat(state.formData.width);
	state.formData.height=CheckInputFloat(state.formData.height);
	state.formData.length=CheckInputFloat(state.formData.length);
	var cm = "cm";
	var i = "in";
	if (l == cm) {
		state.formData.length2="长："+(length * 0.3937).toFixed(2) + "in";
		state.formData.width2 ="宽："+(width * 0.3937).toFixed(2)  + "in";
		state.formData.height2="高："+(height * 0.3937).toFixed(2) + "in";
	}
	if (l == i) {
		state.formData.length2 ="长："+(length * 2.54).toFixed(2) + "cm";
		state.formData.width2  ="宽："+(width * 2.54).toFixed(2)  + "cm";
		state.formData.height2 ="高："+(height * 2.54).toFixed(2) + "cm";
	}
}
	 
   function changeWeight() {//改变重量
	var weight = state.formData.weight;
	state.formData.weight=CheckInputFloat(state.formData.weight);
	var w = state.formData.wunit;
	var kg = "kg";
	var lb = "lb";
	var oz = "oz";
	if(weight==""||weight=="0"){
		state.formData.weight2= "0lb";
		state.formData.weight3= "0oz";
	}
	if (w == kg) {
		//alert(weight);
		state.formData.weight2=(weight * 2.2046).toFixed(2) + "lb";
		state.formData.weight3=(weight * 2.2046 * 16).toFixed(2) + "oz";
	}
	if (w == lb) {
		state.formData.weight2=(weight * 0.4536).toFixed(2) + "kg";
		state.formData.weight3=(weight * 16).toFixed(2) + "oz";
	}
	if (w == oz) {
		state.formData.weight2=(weight / 16).toFixed(2) + "lb";
		state.formData.weight3=(weight / 16 * 0.4536).toFixed(2) + "kg";
	}
}
	function renderHeader(c){
		return "售价"+c

	}
	/* 显示成本明细 */
	function handleProfitDetails(){
		profitDetailsRef.value.drawer = true
	
	}
	/* 显示各国尺寸 */
	function handleFbaSize(){
		fbaSizeRef.value.drawer = true
	}
	/**
	 * 计算成本利润
	 */
	function calculateProfit(){
		calculateApi.showCost(state.formData).then(res=>{
			state.formData=res.data;
			marketPriceInput();
			changeLength();
			changeWeight();
		})
	}
	/* 方案配置*/
	function planConfig(){
		router.push({
			path:'/amazon/profit/config',
			query:{
				title:"计算方案",
				path:'/amazon/profit/config',
			}
		})
	}
	/* 售价输入时 */
	function marketPriceInput(){
		 calculateApi.showProfit(state.formData).then(res=>{
		 	 state.formData.country=res.data;
		 });
	}
	
	function searchParam(){
		if(state.queryParams.searchType=="SKU"){
			state.sloading=true;
			materialApi.getMaterialInventoryInfo({"sku":state.queryParams.searchKeywords,"warehouseid":""}).then(res=>{
				state.sloading=false;
				if(res.data.material){
					 state.formData.cost=res.data.material.price;
				}else{
					ElMessage.error('未找到对应产品信息');
					return;
				}
				if(res.data.pkgDim){
					state.formData.length=res.data.pkgDim.length;
					state.formData.width =res.data.pkgDim.width;
					state.formData.height=res.data.pkgDim.height;
					state.formData.weight=res.data.pkgDim.weight;
				}else{
					ElMessage.error('未找到对应尺寸信息');
					return;
				}
				state.formData.wunit="kg";
				state.formData.lunit="cm";
				changeLength();
				changeWeight();
				ElMessage.success('查询成功');
			}).catch(e=>{
				state.sloading=false;
			})
		}else if(state.queryParams.searchMarket){
			state.sloading=true;
			productinfoApi.getDim({"asin":state.queryParams.searchKeywords,
			                      "marketplaceid":state.queryParams.searchMarket}).then(res=>{
									  state.sloading=false;
									  state.formData.length=formatFloat(res.data.dimensions[0].package.length.value);
									  state.formData.width =formatFloat(res.data.dimensions[0].package.width.value);
									  state.formData.height=formatFloat(res.data.dimensions[0].package.height.value);
									  state.formData.weight=formatFloat(res.data.dimensions[0].package.weight.value);
									  if(res.data.dimensions[0].package.height.unit=="inches"){
										  	  state.formData.lunit="in";
									  }
									  if(res.data.dimensions[0].package.height.unit=="centimeters"){
									  		 state.formData.lunit="cm";
									  }
									  
									  if(res.data.dimensions[0].package.weight.unit=="pounds"){
									  		 state.formData.wunit="lb";
									  }
									  if(res.data.dimensions[0].package.weight.unit=="grams"){
										  state.formData.length=state.formData.length/1000;
										  state.formData.width=state.formData.width/1000;
										  state.formData.height=state.formData.height/1000;
									  	  state.formData.wunit="kg";
									  }
									  changeLength();
									  changeWeight();
									  ElMessage.success('查询成功');
								  }).catch(e=>{
										state.sloading=false;
									});
		}
		
	}
	function reloadSLData(){
		calculateApi.showProfitPage({"isSmlAndLight":state.formData.isSmlAndLightStr}).then(res=>{
			state.countryOptions=res.data.countryList;
			state.countryMarketOptions=res.data.countryMarketlist;
			 calculateProfit();
		});
	}
 
	/**
	 * 初始化
	 */
	onMounted(()=>{
		calculateApi.showProfitPage({"isSmlAndLight":"false"}).then(res=>{
			state.typeOptions=res.data.typeList;
			state.marketOptions= res.data.marketlist;
			state.configOptions=res.data.profitCfgList;
			state.countryOptions=res.data.countryList;
			state.marginOptions=res.data.marginList;
			state.countryMarketOptions=res.data.countryMarketlist;
			res.data.profitCfgList.forEach(item=>{
				if(item.isDefault){
					state.formData.profitCfgId=item.id;
					cfgChange();
				}
				changeLength();
				changeWeight();
			})
		});
	})
	
</script>

<style>
	.m-t-16{margin-top:16px;}
	.m-b-16{margin-bottom:16px;}
	.w-grow{
		flex:1;
	}
	.m-r-8 {
		margin-right:8px;
	}
	.w-8{
		width: 80px;
	}
	.m-r-4{
		margin-right: 4px!important;
	}
	.w-100{
		width: 100%;
	}
	.w-100>span{
		width: 100%;
	}
.height-screen{
	height:calc(100vh - 36px);
}	
tbody .green-text{
	font-weight: 600;
	color: var(--el-color-success);
}
tbody .red-text{
	font-weight: 600;
	color: var(--el-color-danger);
}
.gird-line-left .el-form--default.el-form--label-top .el-form-item .el-form-item__label {
    margin-bottom: 3px;
    line-height: 22px;
}
.dark .gary-bg{
	background-color:#1e1e1e;
}
.gird-line-left .el-form-item--default {
    margin-bottom: 10px;
}
</style>
