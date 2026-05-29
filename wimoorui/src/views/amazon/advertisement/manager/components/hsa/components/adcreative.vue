<template>
	<el-form-item>
		<div class="flex-center" style="width:90%;">
		 <div   class="p-l"    >
			 <el-form  v-if="form.isdefault" >
				 <el-form-item  class="mgn-20" label="选择您想要自定义商品广告的方式">
					 <span class="text-red font-extraSmall">{{statusText}}</span>
				 </el-form-item>			 
				 <el-form-item  class="mgn-20" label="徽标" >
					 <el-switch v-model="form.islogo" >
					 </el-switch>
					 <div style="width:80%">
					  <el-button @click="changeLogoType" style="float:right;" :disabled="!form.islogo" link><el-icon  ><ArrowRight /></el-icon> </el-button>
					 </div>
				 </el-form-item>
				<el-form-item class="mgn-20" label="标题" >
					 <el-switch v-model="form.isheadline"  >
					 </el-switch>
					 <div style="width:80%">
					  <el-button @click="changeHeadlineType" style="float:right;" :disabled="!form.isheadline" link><el-icon  ><ArrowRight /></el-icon> </el-button>
					 </div>
				</el-form-item>	
				<el-form-item class="mgn-20" label="品牌名" >
					 <el-switch v-model="form.isbrand"  >
					 </el-switch>
					 <div style="width:80%">
					  <el-button @click="changeBrandType" style="float:right;" :disabled="!form.isbrand" link><el-icon  ><ArrowRight /></el-icon> </el-button>
					 </div>
				</el-form-item>	
				<el-form-item class="mgn-20" label="商品" >
					 <el-switch v-model="form.isproduct"  >
					 </el-switch>
					 <div style="width:80%">
					  <el-button @click="changeProductType" style="float:right;" :disabled="!form.isproduct" link><el-icon  ><ArrowRight /></el-icon> </el-button>
					 </div>
				</el-form-item>	
				<el-form-item class="mgn-20"  label="图片">
					 <el-switch v-model="form.isimage"  >
					 </el-switch>
					 <div style="width:80%">
					  <el-button @click="changeImageType" style="float:right;" :disabled="!form.isimage" link><el-icon  ><ArrowRight /></el-icon> </el-button>
					 </div>
				</el-form-item>	
			 </el-form>
		     <div v-if="form.openheadline">
				  <el-input   v-model="form.headline"   placeholder="请输入标题"></el-input>
				  <div>
					  <el-button size="small" type="primary" @click="form.isdefault=true;form.openheadline=false">返回</el-button>
					  <el-button size="small" type="primary" @click="submitHeadline">确认</el-button>
				  </div>
			 </div>
			 <div v-if="form.openbrand">
					  <el-input   v-model="form.brandname"   placeholder="品牌名称"></el-input>
					  <div>
						  <el-button size="small" type="primary" @click="form.isdefault=true;form.openbrand=false">返回</el-button>
						  <el-button size="small" type="primary" @click="submitBrand">确认</el-button>
					  </div>
			 </div>
			 <div v-if="form.openproduct">
					  <el-input   v-model="form.brandname"   placeholder="商品列表"></el-input>
					  <div>
						  <el-button size="small" type="primary" @click="form.isdefault=true;form.openproduct=false">返回</el-button>
						  <el-button size="small" type="primary" @click="submitProduct">确认</el-button>
					  </div>
			 </div>
			 <div v-if="form.openlogo">
					<div v-loading="logoloading">
						<el-space>
						 <el-select v-model="form.entityid" placeholder="请选择徽标" @change="changeLogo">
							 <el-option v-for="item in logoList" :label="item.brandregistryname" :value="item.brandentityid" :key="item.brandentityid"></el-option>
						 </el-select>
						 <el-select v-model="form.assetid"  @change="changeImage">
							 <el-option v-for="image in imageList" :label="image.name" :key="image.assetid"  :value="image.assetid" ></el-option>
						 </el-select>
						 </el-space>
						 <div style="margin-top:10px;">
						<el-scrollbar  style="width:500px;height:400px;">
							 <el-image :src="form.url">
							 </el-image>   
						 </el-scrollbar>
						</div>
						<el-button size="small" type="primary" @click="form.isdefault=true;form.openlogo=false">返回</el-button>
						<el-button size="small" type="primary" @click="submitLogo">确认</el-button>
					</div>
			 </div>
			 <div v-if="form.openimage">
				   <span>暂未开发。。后续将支持</span>
			 		<!-- <el-input   v-model="form.image"   placeholder="请输入图片"></el-input> -->
					<div>
						<el-button size="small" type="primary" @click="form.isdefault=true;form.openimage=false">返回</el-button>
						<el-button size="small" type="primary" @click="submitImage">确认</el-button>
					</div>
			 </div>
		  </div>
		  <div class="p-r " >
			  <el-row style="margin:7px">
				  <el-select placeholder="请选择尺寸" size="small" v-model="form.imagesize" @change="changeSize">
					  <el-option v-for="item in sizeList" :label="item.size" :value="item.size" :key="item.size"></el-option>
				  </el-select>
			  </el-row>
			  <div class="bg-line">
				  <el-scrollbar >
				  <div class="iframeCenter" v-loading="htmlloading">
				     <iframe ref="viewhtmlIframeRef" style="border:none;"  :width="size.width" :height="size.height" /> 
				    </div>
					</el-scrollbar>
			  </div>
			 
		  </div>
		  </div>
	</el-form-item>
	
</template>

<script setup>
	import { ref ,reactive,onMounted,toRefs,watch} from 'vue';
	import {Search,ArrowRight} from '@element-plus/icons-vue';
	import advCreativeApi from '@/api/amazon/advertisement/report/advCreativeApi.js';
	import advStoreApi from '@/api/amazon/advertisement/report/advStoreApi.js';
	import productinfoApi from '@/api/amazon/product/productinfoApi.js';
	import {getCurrencyMark,formatFloat} from '@/utils/index.js';
	import formatterUtil from '@/utils/formatter.js';
	
	let props = defineProps({  isEdit:"", })
	const { isEdit } = toRefs(props);
	const emit = defineEmits(['change',]);
	const globalTable=ref();
	const viewhtmlIframeRef=ref();
	const state = reactive({
		tableData:{records:[],total:0},
		addedList:[],
		queryParams:{search:''},
		currency:'$',
		htmlloading:false,
		logoloading:false,
		statusText:'',
		form:{
			"islogo":false,
			"isheadline":false,
			"isimage":false,
			"headline":null,
			"image":null,
			"isdefault":true,
			"imagesize":"414x125",
			"entityid":null,
			 "url":null,
			 "assetid":null,
			 "brandLogo":null,
			 "id":null,
			 "properties":null,
		},
		size:{
			"width":"414px",
			"height":"125px"
		},
		sizeList:[
			{"size":"245x250","isMobile":false,"isOnAmazon":true},
			{"size":"650x130","isMobile":false,"isOnAmazon":true},
			{"size":"414x125","isMobile":false,"isOnAmazon":true},
			{"size":"320x50","isMobile":false,"isOnAmazon":true},
			{"size":"160x600","isMobile":false,"isOnAmazon":true},
			{"size":"300x250","isMobile":false,"isOnAmazon":true},
			{"size":"728x90","isMobile":false,"isOnAmazon":true},
			{"size":"970x250","isMobile":false,"isOnAmazon":true},
			{"size":"300x600","isMobile":false,"isOnAmazon":true},
			{"size":"980x55","isMobile":false,"isOnAmazon":true},
			{"size":"270x150","isMobile":null,"isOnAmazon":null},
		],
		logoList:[],
		imageList:[],
	})
	const{
		tableData,
		addedList,
		htmlloading,
		logoloading,
		queryParams,
		currency,
		form,
		size,
		sizeList,
		logoList,
		imageList,
		statusText,
	}=toRefs(state);
	
	function submitProduct(){
		state.form.openproduct=false;
		state.form.isdefault=true;
		loadPreviewHTML();
	}
	function submitBrand(){
		state.form.openbrand=false;
		state.form.isdefault=true;
		loadPreviewHTML();
	}
	
	function submitHeadline(){
		state.form.openheadline=false;
		state.form.isdefault=true;
		loadPreviewHTML();
	}
	
	function submitLogo(){
		state.form.openlogo=false
		state.form.isdefault=true;
		//选择logo assetid处理一下
		var version="version_v1";
		var assetid="";
		var array=state.form.assetid.split(":");
		if(array.length>1){
			version=array[1];
			assetid=array[0];
		}else{
			assetid=array[0];
		}
		state.form.brandLogo={'assetId':assetid,'assetVersion':version};
		loadPreviewHTML();
	}
	
	function submitImage(){
		state.form.openimage=false
		state.form.isdefault=true;
	}
	function changeHeadlineType(val){
		state.form.openheadline=true;
		state.form.isdefault=false;
	}
	function changeBrandType(){
		state.form.openbrand=true;
		state.form.isdefault=false;
	}
	function changeProductType(){
		state.form.openproduct=true;
		state.form.isdefault=false;
	}
	function changeImageType(){
		state.form.openimage=true;
		state.form.isdefault=false;
	}
	
	function changeLogoType(){
		state.form.openlogo=true;
		state.form.isdefault=false;
	}
	function changeSize(){
		loadPreviewHTML();
	}
	function changeImage(){
		var assetid=state.form.assetid;
		state.imageList.forEach(item=>{
			if(item.assetid==assetid){
				state.form.url=item.url;
			}
		});
	}
	 
	 
	function loadTableData(params){
		productinfoApi.getProductInfoList(params).then((res)=>{
			state.tableData.records = res.data.records
			state.tableData.total =res.data.total
		})
	}
	function show(params){
		state.queryParams=params;
		state.currency=params.currency;
		loadLogo();
		loadCreative();
	}
	function loadLogo(){
		state.logoloading=true;
		advStoreApi.findBrandForProfileId(state.queryParams.profileid).then((res)=>{
			state.logoloading=false;
			if(res.data && res.data.length>0){
				state.logoList=res.data;
				state.form.entityid=res.data[0].brandentityid;
				changeLogo();
			}
		});
	}
	
	function changeLogo(){
		advStoreApi.findAssetsForBrandEntityId(state.queryParams.profileid,state.form.entityid).then((res)=>{
			if(res.data && res.data.length>0){
				state.imageList=res.data;
				state.form.assetid=res.data[0].assetid;
				state.form.url=res.data[0].url;
			}
		});
	}
	
	function selectImage(image){
		
	}
	 
	function loadPreviewHTML(){
		var size=state.form.imagesize.split("x");
		var asins=[];
		if(state.queryParams.productList&&state.queryParams.productList.length>0){
			asins.push({"asin":state.queryParams.productList[0].asin});
		}
		var params={
			"creative": {
				"properties": {}
			},
			"previewConfiguration": {
				"size": {
					"width": parseInt(size[0]),
					"height": parseInt(size[1])
				},
				"products":asins,
				"isMobile": false,
				"isOnAmazon": true
			}
		}
		if(state.form.headline){
		    params.creative.properties.headline=state.form.headline;
		}
		if(state.form.brandLogo){
		    params.creative.properties.brandLogo=state.form.brandLogo;
		}
		state.form.properties=params.creative.properties;
		emitChange();
		state.htmlloading=true;
		advCreativeApi.getCreativePreviewHTML(state.queryParams.profileid,"sd",params).then((res)=>{
			state.htmlloading=false;
			if(res.data){
				var data=JSON.parse(res.data);
				 viewhtmlIframeRef.value.contentDocument.body.innerHTML=data.previewHtml;
				 state.size.width=size[0]+"px";
				 state.size.height=size[1]+"px";
			}
		}).catch(e=>{state.htmlloading=false;});
	}
	
	function loadEntity(assetid){
		state.form.assetid=assetid;
		advStoreApi.getAsset({"assetid":assetid}).then((res)=>{
			if(res.data && res.data.length>0){
				state.form.islogo=true;
				var asset=res.data[0];
				state.form.entityid= asset.brandentityid;
				state.form.url= asset.url;
			}
		   loadPreviewHTML();
		});
	}
	
	function loadCreative(){
		advCreativeApi.getListOfCreatives(state.queryParams.profileid,"sd",{"adGroupIdFilter":state.queryParams.adgroupid}).then((res)=>{
			if(res.data){
				var datas=formatterUtil.parseJSONWithBigInt(res.data);
				if(datas&&datas.length>0){
					var data=datas[0];
					if(data.moderationStatus=="PENDING_REVIEW"){
						state.statusText="当前处于PENDING状态,修改将无效。";
					}
					state.form.id=data.creativeId;
					if(data.properties.headline){
						state.form.headline=data.properties.headline;
						state.form.isheadline=true;
					}
					if(data.properties.brandLogo){
						loadEntity(data.properties.brandLogo.assetId);
					}else{
					    loadPreviewHTML();	
					}
				}else{
					loadPreviewHTML();
				}
			}else{
				loadPreviewHTML();
			}
		});
	}
	
	function emitChange(){
		emit('change', {"id":state.form.id,"properties":state.form.properties});
	}
	
	defineExpose({
		show,
	})
	 
</script>

<style scoped>
	.bg-line{
		display: flex;
		justify-content: center;
		align-items: center;
		 --size: 20px;
		  background:
		  conic-gradient(from 270deg at 1px 1px, rgba(71, 71, 71, 1.0) 90deg, transparent 0deg) -1px -1px,
		  conic-gradient(from 270deg at 1px 1px, rgba(71, 71, 71, 1.0) 90deg, transparent 0deg) -1px -1px,
		  linear-gradient(transparent calc(var(--size) - 1px), rgba(71, 71, 71, 1.0) 0),
		  linear-gradient(to right,transparent calc(var(--size) - 1px), rgba(71, 71, 71, 1.0) 0),
		  #000;
		  background-size: var(--size) var(--size)
	}
	.iframeCenter{
		padding-top: 30%;
		height:521px;
	}
	.m-l-8{
		margin-left:8px;
	}
	.p-l{
		width: 40%;
		height: 560px;
		padding:15px;
		border:1px solid #dedede;
	}
	.p-r{
		width: 60%;
		height: 560px;
		background-color:#f5f5f5;
	}
	.input-with-select{
		margin-bottom:16px;
	}
	.mgn-20{
		margin-bottom:20px;
	}
</style>