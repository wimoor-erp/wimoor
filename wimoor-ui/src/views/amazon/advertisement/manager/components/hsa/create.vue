<template>
	<div class="lr-wrapper">
	     <div class="l-content">
			<div class="item-tab " v-for="item in tabs" 
			v-show="!item.show"
			:class="{'active':current==item.value}"> 
				<el-link type="info" :underline="false" :href="'#'+item.id">{{item.name}}</el-link>
			</div>
		 </div>
	     <div class="r-content">
			 <el-Scrollbar height="calc(100vh - 100px )" @scroll='scroll'>
				 <div class="ad-body">
					 <div class="flex-center">
					<div class="mark-squre"></div>
					<div id="campaign" class="font-base font-bold tab-scroll">
						广告活动
					</div>
					</div>
				<el-form 
				:model="form"
				:rules="rules"
				label-position="right"
				label-width="120px"
				>
					   <el-form-item label="店铺站点" prop="group" >
						 	<AdGroup :border="true"  @change="changeGroup"/>
						</el-form-item>
				<div v-if="show.campaign">
					 <el-form-item label="广告活动名称" prop="campaignname">
						<el-input v-model="form.campaignname" placeholder="必填" style="width:452px"> </el-input>
					</el-form-item>
					 <el-form-item label="广告活动时间"  prop="startDate">
						<el-date-picker
						        v-model="form.startDate"
						        type="date"
						        placeholder="开始时间"
						      />
							   &nbsp;<span class="font-extraSmall">-</span>&nbsp; 
					     	<el-date-picker
						        v-model="form.endDate"
						        type="date"
						        placeholder="结束时间(选填)"
						      />
					</el-form-item>
					<el-form-item label="广告组合" prop="portfolios">
						<el-select v-model="form.portfolioid" placeholder="请选择">
							<el-option v-for="item in  portfoloList" :key="item.id" :value="item.id" :label="item.name"></el-option>
						</el-select>
					</el-form-item>
					<el-form-item label="每日预算" prop="budget">
						<el-input v-model="form.budget" @input="form.budget=CheckInputFloat(form.budget)" placeholder="请输入" style="width:214px">
							<template #prepend>$</template>
							 <template #append>
							        <el-select v-model="form.budgetType" placeholder="Select" style="width: 80px">
							          <el-option label="Daily" value="DAILY" />
							          <el-option label="Life time" value="LIFETIME" />
							        </el-select>
							      </template>
						</el-input>
					</el-form-item>
					 
					
					
			
					 <el-form-item label="自动竞价" >
					 	 <el-switch v-model="form.autobid" size="small">
					 	 </el-switch> 
						 <span> 允许亚马逊自动优化搜索结果]顶部以外的广告位竞价。</span>
						 <div class="font-extraSmall">自动竞价或竞价调整不会应用于视频广告格式的广告</div>
						 
					 </el-form-item>	
					 <el-form-item v-if="form.autobid==false" label="自定义竞价调整" >
					 	 <el-input  style="width:214px" v-model="form.percentage"    placeholder="请输入">
							 <template #prepend>
								 <el-select style="width:80px;"  v-model="form.updown">
								 							 <el-option label="提高" value="up" key="up" ></el-option>
								 							 <el-option label="降低" value="down" key="down" ></el-option>
								 </el-select>
							 </template>
							 <template #suffix>%</template>
						 </el-input>
						 用于搜索结果顶部以外的广告位
					 						 
					 </el-form-item>	
					 <el-form-item   label="目标" >
						 <div>
						 <el-card>
							<h3>创建广告活动以实现广告目标</h3>
							<div>亚马逊将帮助您创建广告活动,以提高页面访问次数或提升品牌展示量份额。</div>
							选择与您的业务目标相符的广告活动目标。我们将提供竞价和投放目标建议,以帮助您实现这一目标。
						 </el-card>
						 <div style="margin-top:20px;padding-bottom:20px;">
							 <el-radio-group v-model="form.goal" class="ml-4">
							   <el-radio-button label="PAGE_VISIT"   size="large" border>
								   <h4>增加页面访问次数</h4>
								   <div>将流量引导至您的落地页和商品详情页。</div>
								   <div>
									   <div>费用类型:单次点击成本(CPC)</div>
									   <div>成功指标:点击量</div>
								   </div>
							   </el-radio-button>
							   <el-radio-button label="BRAND_IMPRESSION_SHARE"  style="text-align:left" size="large" border>
								   <h4>提升品牌展示量份额</h4>
								   <div>向搜索您的品牌或品牌品类的买家展示您的广告。</div>
								   <div>
									   <div>费用类型:每千次可见展示成本(vCPM)</div>
									   <div>成功指标:搜索结果首页首位展示量份额(IS)</div>
								   </div>

							   </el-radio-button>
							 </el-radio-group>
						 </div>
					   </div>
					 </el-form-item>
					 <el-form-item label="品牌"  >
						 <el-select v-model="form.store" @change="changeStore" placeholder="请选择一个">
						 								  <el-option  v-for="item in form.storeList" :value="item.brandentityid" 
						 								  :key="item.brandentityid" :label="item.brandregistryname"
						 								  ></el-option>
						 </el-select>
					 </el-form-item>
					</div>
					<div v-else>
						<el-form-item label="广告活动"   >
								<el-select-v2 style="width:90%;" v-model="campaignid" filterable @change="loadAdGroupList" :options="campaignList"   >
								</el-select-v2>	
						 </el-form-item>
					</div>
					<div class="flex-center">
					<div class="mark-squre"></div>
					<div id="adgroup" class="font-base font-bold tab-scroll">
						广告组
					</div>
					</div>
					<div v-if="show.adgroup">
						<el-form-item label="广告组名称" prop="adGroup" required>
							<el-input v-model="form.adGroup" placeholder="必填" style="width:452px"> </el-input>
						</el-form-item>	
					<!-- 	<Bidding v-if="campaign.targetingType=='AUTO' || form.target=='AUTO'"  :forms="form"/>
						<el-form-item v-else label="默认竞价" prop="bidding" required>
						<el-input   v-model="form.bidding" placeholder="请输入" style="width:214px">
							<template #prepend>$</template>
						</el-input>
						</el-form-item>	 -->
						
						<el-form-item  >
						 <el-button link type="primary"  @click="handleAdgroupSubmit" >保存广告组</el-button>
						 <span class="font-extraSmall"> 商品投放建议竞价需先保存广告组！</span>
						</el-form-item>	
					</div>
					<div v-else>
						<el-form-item label="广告组"   >
								<el-select-v2 style="width:90%;" v-model="adgroupid"  :options="adgroupsList"   >
								</el-select-v2>	
						 </el-form-item>
					</div>
					
					<div class="flex-center">
					<div class="mark-squre"></div>
					<div id="product" class="font-base font-bold tab-scroll">
						商品
					</div>
					</div>
					<!-- 添加商品 -->
					<ProductAdd ref="productAddRef" :isEdit="show.productAds"  @change="getProductData" />
					
				  
					<div v-if="show.adcreative"> 
					<div  class="flex-center" >
					<div class="mark-squre"></div>
					<div id="adcreative" class="font-base font-bold tab-scroll">
						广告创建<span class="font-extraSmall">  </span>
					</div>
					</div>
					 	<el-form-item label="广告格式" >
					 		 <el-radio-group v-model="form.adFormat">
					 			 <el-radio label="product" size="large">商品集</el-radio>
					 			 <el-radio label="brand" size="large">品牌旗舰店焦点模式</el-radio>
					 			 <el-radio label="video" size="large">视频</el-radio>
					 		 </el-radio-group>
					 	</el-form-item>	
					 	<el-form-item label="着陆页" >
					 		 <el-radio-group v-model="form.landpage">
					 			 <el-radio label="1" size="large">亚马逊品牌旗舰店(包括子页面)</el-radio>
					 			 <el-radio label="2" size="large">新着陆页,选择要推广的商品,我们将为您创建一个落地页</el-radio>
					 		 </el-radio-group> 
					 	</el-form-item>	
					 	<el-form-item label="亚马逊商店" >
							<el-select v-model="form.store" @change="changeStore" placeholder="请选择一个">
									  <el-option  v-for="item in form.storeList" :value="item.brandentityid" 
									  :key="item.brandentityid" :label="item.brandregistryname"
									  ></el-option>
							</el-select>
					 		  <el-select style="margin-left: 10px;"  v-model="form.storepage"  @change="changePage"  placeholder="请选择一个页面">
					 		  		<el-option  v-for="item in form.storepageList" :value="item.storepageid"
					 		  		:key="item.storepageid" :label="item.storepagename"
					 		  		></el-option>						  
					 		  </el-select>
					 		  <el-button style="margin-left:10px;" type="primary" @click="showPage" link>预览</el-button>
					 	</el-form-item>	
					 	<el-form-item label="广告名称" >
					 		   <el-input style="width:150px;" v-model="form.adname" placeholder="请输入广告名称" ></el-input>
					 	</el-form-item>	
					 	<el-form-item label="页面类型" >
					 		  <el-radio-group v-model="form.pagetype">
					 			  <el-radio label="PRODUCT_LIST" size="large">产品列表</el-radio>
					 			  <el-radio label="STORE" size="large">商店页面</el-radio>
					 			  <el-radio label="CUSTOM_URL" size="large">自定义URL</el-radio>
					 			  <el-radio label="DETAIL_PAGE" size="large">详情页</el-radio>
					 		  </el-radio-group>
					 	</el-form-item>	
					 	<el-form-item label="品牌文字" >
					 		   <el-input style="width:150px;" v-model="form.brandName" placeholder="请输入品牌名称" ></el-input>
					 		   <el-input style="width:250px;margin-left:10px;" v-model="form.headline" placeholder="请输入大字标题" ></el-input>
					 	</el-form-item>	
					 	<el-form-item label="品牌Logo" >
					 		 <el-image :src="form.image.url" style="width:80px;height:80px;margin-right: 10px;"></el-image>
					 		  <el-button type="primary" @click="showAssetLogo">选择Logo</el-button>
					 	</el-form-item>	 
					 	<!-- <Adcreative  ref="adcreativeRef"  @change="getAdcreativeData" /> -->
					 </div>
					
					<!-- 竞价 -->
					<div  v-if="show.target || show.keywords">
					<div class="flex-center ">
					<div class="mark-squre"></div>
					<div id="target" class="font-base font-bold tab-scroll">
						投放
					</div>
					</div>
				
					<Target 
					   ref="targetRef" @change="getKeywordOrTargeData" @changetype="changeStrategy" />
					 </div>
					 <div v-if="show.nkeywords || show.campnekeyword">
						<div    class="flex-center"  >
						<div class="mark-squre"></div>
						<div id="nekeyword" class="font-base font-bold tab-scroll">
							否定关键词<span class="font-extraSmall"> (选填)</span>
						</div>
						</div>
						<!-- 否定关键词 -->
					
						<NegativeKeywords  ref="negativeKeywordsRef" @change="getNeKeywordData" />
					</div>
					<div v-if="show.ntarget||show.campnetarget">
					<div  class="flex-center" >
					<div class="mark-squre"></div>
					<div id="netarget" class="font-base font-bold tab-scroll">
						否定商品投放<span class="font-extraSmall"> (选填)</span>
					</div>
					</div>
					<NegativeProduct  ref="negativeProductTargetRef"  @change="getNetargetData" />
					 </div>
				</el-form> 
				</div>
				<el-affix position="bottom" :offset="16">
				 <div class="footer-box">
					 <el-button @click.stop="handleToManagerPage"><span v-if="processmsg">取消</span><span v-else>返回</span></el-button>
					 <el-button type="primary" :loading="submitLoading" v-if="!processmsg" @click.stop="handleSubmit">提交</el-button>
					<span style="padding-left:10px;" class="font-extraSmall">{{processmsg}}</span>
				 </div>
				</el-affix>
				</el-Scrollbar>
		
		 </div>
	</div>	
	
	<el-dialog
		title="Logo图片列表"
		v-model="logoVisable"
		width="60%"
		>
		<div>
			<el-image v-for="item in form.imageList" @click="selectImage(item)"
			  style="width:80px;height:80px;border:1px solid #eee;margin-left:20px;"  :src="item.url"></el-image>
		</div>
		<template #footer>
			<el-button @click="logoVisable=false">关闭</el-button>
		</template>
		</el-dialog>
</template>

<script setup>
	import {ref,reactive,onMounted,watch,toRefs,computed} from 'vue'
	import Group from '@/components/header/group.vue';
	import BiddingTactics from './components/bidding_tactics_sp.vue'
	import Bidding from './components/bidding.vue'
	import ProductAdd from './components/product_add.vue'
	import NegativeKeywords from './components/negative_keywords.vue'
	import NegativeProduct from './components/negative_target.vue'
	import Target from './components/target.vue' ;
	import AdGroup from '@/components/header/ad_group.vue';
	import tabScroll from"@/utils/tab_scroll.js";
	//import Adcreative from './components/adcreative.vue' ;
	import portfoliosApi from '@/api/amazon/advertisement/portfolios/portfoliosApi.js'; 
	import advCampaignApi from '@/api/amazon/advertisement/report/advCampaignApi.js';
	import advAdgroupApi from '@/api/amazon/advertisement/report/advAdgroupApi.js';
	import advProductsApi from '@/api/amazon/advertisement/report/advProductsApi.js';
	import advKeywordsApi from '@/api/amazon/advertisement/report/advKeywordsApi.js';
	import advTargetApi from '@/api/amazon/advertisement/report/advTargetApi.js';
	import advertApi from '@/api/amazon/advertisement/report/advertApi.js';
	import advStoreApi from '@/api/amazon/advertisement/report/advStoreApi.js';
	import advSBAdsApi from '@/api/amazon/advertisement/report/advSBAdsApi.js';
	import productinfoApi from '@/api/amazon/product/productinfoApi.js';
	import {dateFormat,formatFloat,dateTimesFormat,CheckInputFloat,CheckInputInt} from '@/utils/index.js';
	import { useRoute,useRouter } from 'vue-router';
	import {ElMessage,ElDivider} from 'element-plus';
 
	let router = useRouter();
	const productAddRef=ref();
	const negativeKeywordsRef=ref();
	const negativeProductTargetRef=ref();
	const targetRef=ref();
	//const adcreativeRef=ref();
	const state = reactive({
		campaignList:[],
		show:{ntarget:true,nkeywords:true,target:true,adgroup:true,campaign:true,productAds:true,},
		adgroupsList:[],
		campaignid:null,
		adgroupid:null,
		adtype:null,
		profile:{},
		campaign:{},
		submitLoading:false,
		logoVisable:false,
		tabs:[
			{name:'广告活动',value:1,id:'campaign',},
			{name:'广告组',value:2,id:'adGroup',},
			{name:'商品',value:3,id:'product'},
			{name:'广告创建',value:4,id:'product'},
			{name:'关键词',value:5,id:'keywords'},
			{name:'投放',value:6,id:'target',show:true,},
			{name:'广告组-否定关键词',value:7,id:'nekeyword'}, 
			{name:'活动-否定关键词',value:8,id:'campnekeyword'}, 
			{name:'用户搜索词',value:9,id:'keywordquery'}, 
			{name:'广告组-否定投放',value:10,id:"netarget"},
			{name:'活动-否定投放',value:11,id:'campnetarget'}, 
			{name:'用户投放搜索',value:12,id:'targetquery'}, 
		],
		form:{
			image:{
				url:null
			},
			pagetype:"PRODUCT_LIST",
			imageList:[],
			autobid:true,
			placement:"OTHER",
			updown:"up",
			storepage:null,
			storepageList:[],
			store:null,
			storeList:[],
			adFormat:"product",
			landpage:"2",
			budgetType:"DAILY",
			goal:"PAGE_VISIT",
			target:"MANUAL",
			portfolioid:null,
			budget:1,
			campaignname:'',startDate:'',
			time:'',targetType:'keywords',
			biddingTable:[{
				status:true,
				name:'紧密匹配',
				sugBid:'-',
			},{
				status:true,
				name:'宽泛匹配',
				sugBid:'-',
			},{
				status:true,
				name:'同类商品',
				sugBid:'-',
			},{
				status:true,
				name:'关联商品',
				sugBid:'-',
			},],
			checkList:['广泛匹配','词组匹配'],
		},
		processmsg:"",
		rules:{
			campaignname:[ { required: true, message: '请输入广告活动名称', trigger: 'blur' },],
			adGroup:[ { required: true, message: '请输入广告组名称', trigger: 'blur' },],
			startDate:[ {required: true,message: '开始时间不能为空!',  trigger: 'change' },],
			budget:[ {required: true,message: '预算不能为空!',  trigger: 'blur' },],
			bidding:[ {required: true,message: '默认竞价不能为空!',  trigger: 'blur' },],
		},
		current:'1',
		portfoloList:[],
		saveparam:{ne_target_expression:[],target_expression:[],ne_keywords:[],keywords:[],targets:[],},
		strategy:"",
		productList:[],
	})
	const {
		tabs,
		form,
		rules,
		current,
		portfoloList,
		show,
		campaignid,
		adgroupid,
		adtype,
		processmsg,
		campaignList,
		adgroupsList,
		profile,
		campaign,
		saveparam,
		submitLoading,
		strategy,
		productList,
		logoVisable,
	}= toRefs(state)
	let timer = null
	//防抖
	state.campaignid=router.currentRoute.value.query.campaignid;
	state.adgroupid=router.currentRoute.value.query.adGroupid;
	state.adtype=router.currentRoute.value.query.type;
	function scroll(obj){
		clearTimeout(timer)
		timer = setTimeout(()=>{
			state.current= tabScroll(obj,"tab-scroll")
		},200)
	}
	function selectImage(item){
		state.form.image=item;
		state.logoVisable=false;
	}
	function getNetargetData(data){
		state.saveparam.ne_target_expression=data;
	}
	function getNeKeywordData(data){
		state.saveparam.ne_keywords=data;
	}
	function getProductData(data){
		state.productList=data;
		var params={};
		params.adtype=state.adtype;
		params.campaignType="sb";
		params.adgroup=state.adgroup;
		params.adgroupid=state.adgroupid; 
		params.campaignid=state.campaignid;
		params.sellerid=state.profile.sellerid;
		params.marketplaceid=state.profile.marketplaceid;
		params.profileid=state.form.profileid; 
		params.groupid=state.form.groupid;
		params.currency=state.profile.currency;
		params.querylist=router.currentRoute.value.query.querylist;
		params.productList=state.productList;
		params.campaign=state.campaign;
		params.ads=state.ads;
		if(targetRef&&targetRef.value&&targetRef.value.show){
				targetRef.value.show(params);
		}
		if(negativeKeywordsRef&&negativeKeywordsRef.value&&negativeKeywordsRef.value.show){
			negativeKeywordsRef.value.show(params);
		}
		if(negativeProductTargetRef&&negativeProductTargetRef.value&&negativeProductTargetRef.value.show){
			negativeProductTargetRef.value.show(params);
		}
		// if(adcreativeRef&&adcreativeRef.value&&adcreativeRef.value.show){
		// 	adcreativeRef.value.show(params);
		// }
	}
	function getKeywordOrTargeData(data){
		if(state.strategy=="target"){
			state.saveparam.targets=data;
		}else{
			state.saveparam.keywords=data;
		}
	}
	function changeStrategy(ftype){
		state.strategy=ftype;
	}
 
	
	function showAssetLogo(){
		state.logoVisable=true;
		advStoreApi.findAssetsForBrandEntityId(state.form.profileid,state.form.store).then((res)=>{
			state.form.imageList=res.data;
		});
	}
	
	function showPage(){
		if(state.form.storepage){
			var url="";
			state.form.storepageList.forEach(item=>{
				if(item.storepageid==state.form.storepage){
					url=item.storepageurl;
				}
			});
			 window.open(url,"_blank");
		}
	}
	
	function changePage(val){
		state.form.storepage=val;
	}
	
	function changeStore(){
		advStoreApi.findStoresForProfileId(state.form.profileid).then((res)=>{
			state.form.storepageList=res.data;
		});
	}
	
	function loadStoresForProfileId(){
		advStoreApi.findBrandForProfileId(state.form.profileid).then((res)=>{
			state.form.storeList=res.data;
			if(state.form.storeList&&state.form.storeList.length>0){
     			state.form.store=state.form.storeList[0].brandentityid;
				changeStore();
			}
		});
	}
	
	function loadPageForStore(){
		// advStoreApi.findAsinsForStorePageUrl(state.form.profileid,brandEntityId,storePageUrl).then((res)=>{
		// 	console.log(res.data);
		// })
	}
	
	function initShow(){
		if(state.adtype=="adcams"){
			state.show={adcreative:true,productAds:true,keywords:true,nekeywords:true,campnekeyword:true,target:true,netarget:true,campnetarget:true,adgroup:true,campaign:true};
			state.tabs=[
				{name:'广告活动',value:1,id:'campaign',},
				{name:'广告组',value:2,id:'adgroup',},
				{name:'商品',value:3,id:'product'},
				{name:'广告创建',value:4,id:'adcreative'}, 
				{name:'投放',value:5,id:'target'},
				{name:'否定关键词',value:6,id:'nekeyword'}, 
				{name:'否定投放',value:7,id:"netarget"},
			];
		}
		if(state.adtype=="adgroups"){
			state.show={adcreative:true,productAds:true,keywords:true,nkeywords:true,campnekeyword:true,target:true,ntarget:true,campnetarget:true,adgroup:true,campaign:false};
			state.tabs=[
				{name:'广告活动',value:1,id:'campaign',},
				{name:'广告组',value:2,id:'adgroup',},
				{name:'商品',value:3,id:'product'},
				{name:'广告创建',value:4,id:'adcreative'}, 
				{name:'投放',value:5,id:'target'},
				{name:'否定关键词',value:6,id:'nekeyword'}, 
				{name:'否定投放',value:7,id:"netarget"},
			];
		}
		if(state.adtype=="ProductAds"){
			state.show={adcreative:true,productAds:true,keywords:false,nkeywords:false,campnekeyword:false,target:false,ntarget:false,campnetarget:false,adgroup:false,campaign:false};
			state.tabs=[
				{name:'广告活动',value:1,id:'campaign',},
				{name:'广告组',value:2,id:'adgroup',},
				{name:'商品',value:3,id:'product'},
				{name:'广告创建',value:4,id:'adcreative'}, 
			];
		}
		if(state.adtype=="adkey"){
			state.show={adcreative:false,productAds:false,keywords:true,nkeywords:false,campnekeyword:false,target:false,ntarget:false,campnetarget:false,adgroup:false,campaign:false};
			state.tabs=[
				{name:'广告活动',value:1,id:'campaign',},
				{name:'广告组',value:2,id:'adgroup',},
				{name:'商品',value:3,id:'product'},
				{name:'关键词',value:4,id:'keywords'},
			];
		}
		if(state.adtype=="adnkey"){
			state.show={adcreative:false,productAds:false,keywords:false,nkeywords:true,campnekeyword:false,target:false,ntarget:false,campnetarget:false,adgroup:false,campaign:false};
			state.tabs=[
				{name:'广告活动',value:1,id:'campaign',},
				{name:'广告组',value:2,id:'adgroup',},
				{name:'商品',value:3,id:'product'},
				{name:'否定关键词',value:4,id:'nekeywords'}, 
			];
		}
		if(state.adtype=="adcampnkey"){
			state.show={adcreative:false,productAds:false,keywords:false,nkeywords:false,campnekeyword:true,target:false,ntarget:false,campnetarget:false,adgroup:false,campaign:false};
			state.tabs=[
				{name:'广告活动',value:1,id:'campaign',},
				{name:'广告组',value:2,id:'adgroup',},
				{name:'商品',value:3,id:'product'},
				{name:'活动-否定关键词',value:4,id:'campnekeyword'}, 
			];
		}
		if(state.adtype=="adtarget"){
			state.show={adcreative:false,productAds:false,keywords:false,nkeywords:false,campnekeyword:false,target:true,ntarget:false,campnetarget:false,adgroup:false,campaign:false};
			state.tabs=[
				{name:'广告活动',value:1,id:'campaign',},
				{name:'广告组',value:2,id:'adgroup',},
				{name:'商品',value:3,id:'product'},
				{name:'投放',value:4,id:'target'},
			];
		}
		if(state.adtype=="adntarget"){
			state.show={adcreative:false,productAds:false,keywords:false,nkeywords:false,campnekeyword:false,target:false,ntarget:true,campnetarget:false,adgroup:false,campaign:false};
			state.tabs=[
				{name:'广告活动',value:1,id:'campaign',},
				{name:'广告组',value:2,id:'adgroup',},
				{name:'商品',value:3,id:'product'},
				{name:'否定投放',value:4,id:"netarget"},
			];
		}
		if(state.adtype=="adcampntarget"){
			state.show={adcreative:false,productAds:false,keywords:false,nkeywords:false,campnekeyword:false,target:false,ntarget:false,campnetarget:true,adgroup:false,campaign:false};
			state.tabs=[
				{name:'广告活动',value:1,id:'campaign',},
				{name:'广告组',value:2,id:'adgroup',},
				{name:'商品',value:3,id:'product'},
				{name:'活动-否定投放',value:4,id:'campnetarget'}, 
			];
		}
	}
	function targetChange(val){
			state.tabs.forEach((item)=>{
				if(item.name=='投放'){
					if(val=='manual'){
						item.show = false
						
					}else{
						item.show = true
					}
					
				}
				if(item.name=='否定商品投放'){
					if(val=='manual'&&state.form.targetType=="keywords"){
						item.show = true
					}else{
						item.show = false
					}
				}
				if(item.name=='否定关键词'){
					if(val=='manual'){
						item.value=7
					}else{
						item.value = 6
					}
					if(val=='manual'&&state.form.targetType=="product"){
						item.show = true
					}else{
						item.show = false
					}
				}
			})
	}
	
	 
 
	function changeGroup(data,type){
		state.form.groupid=data.groupid;
		state.form.profileid=data.profileid;
		state.profile=data.profile;
		loadPofoData();
		loadCampaigns();
		loadStoresForProfileId();
		if(type=="load"){
		    initShow();
		}
		
	}
	function loadProductList(){
		var params={};
		params.adtype=state.adtype;
	    params.campaignType="sb";
		params.adgroupid=state.adgroupid; 
		params.campaignid=state.campaignid;
		if(state.adgroup){
			params.adgroup=state.adgroup;
		}else{
			params.adgroup={"defaultbid":state.form.bidding};
		}
		params.sellerid=state.profile.sellerid;
		params.marketplaceid=state.profile.marketplaceid;
		params.profileid=state.form.profileid; 
		params.groupid=state.form.groupid;
		params.currency=state.profile.currency;
		params.skulist=null;
		params.querylist=router.currentRoute.value.query.querylist;
		if(state.adgroupid && state.campaignid){
			var param={
			"adGroupIdFilter": {
				"include": [state.adgroupid]
				}
			};
			advSBAdsApi.getAds(state.form.profileid,params.campaignType,param).then((res)=>{
				var asins=[];
				var creatives=null;
				if(res.data && res.data.length>0){
					state.ads=res.data[0];
					creatives=res.data[0].creative;
					res.data.forEach(item=>{
						var creative=JSON.parse(item.creative);
						if(creative && creative.asins){
							creative.asins.forEach(asin=>{
								asins.push(asin);
							});
						}
					});
					params.creative=creatives;
				    params.asinlist=asins;
				 	productAddRef.value.show(params);
				}else{
					productAddRef.value.show(params);
				}
			});
		}else{
			productAddRef.value.show(params);
		}
		
		
	}
	function loadPofoData(){
		portfoliosApi.findPortfolios({"groupid":state.form.groupid,"profileid":state.form.profileid}).then((res)=>{
			if(res.data && res.data.length>0){
				state.portfoloList=res.data;
			}
		});
		
	}
	function loadAdGroupList(){
		advAdgroupApi.loadAdGroup({"profileid":state.form.profileid,"campaignsid":state.campaignid,"campaignType":"sb"}).then((res)=>{
			if(res.data&&res.data.length>0){
				var hasgroup=false;
				res.data.forEach(item=>{
					item.value=item.adgroupid;
					item.label=item.name;
					if(state.adgroupid==item.adgroupid){
						state.adgroup=item;
					    hasgroup=true;
					}
				});
				state.adgroupsList=res.data;
				if(hasgroup==false && state.adtype!="adgroups" && state.adtype!="adcams"){
					state.adgroupid=res.data[0].adgroupid;
				}
			   loadProductList();
			}else{
				state.adgroupid=null;
			}
		});
	}
	function loadCampaigns(){
		advCampaignApi.getCampaignDetail(state.form.profileid,"sb").then((res)=>{
			if(res.data&&res.data.length>0){
				var hascamp=false;
				res.data.forEach(item=>{
					item.value=item.campaignid;
					item.label=item.name;
					if(item.campaignid==state.campaignid){
						state.campaign=item;
						if(state.campaign.targetingType){
						   state.campaign.targetingType=state.campaign.targetingType.toUpperCase()
						}
						hascamp=true;
					}
					 
				});
				if(hascamp==false){
					state.campaignid=res.data[0].campaignid;
					state.campaign=res.data[0];
					if(state.campaign.targetingType){
					state.campaign.targetingType=state.campaign.targetingType.toUpperCase()
					}
				}
				state.campaignList=res.data;
				loadAdGroupList();
			}else{
				state.campaignid=null;
				state.adgroupid=null;
			}
		});
	}
	function saveAdvcams(callback){
		var data = {};
		var array=[];
		var addObj={};
		if(state.form.portfolioid){
			addObj.portfolioid=state.form.portfolioid;
		}
		addObj.profileid=state.form.profileid;
		addObj.groupid=state.form.groupid;
		addObj.budgetType=state.form.budgetType;
		addObj.campaignType="sb";
		addObj.productLocation="SOLD_ON_AMAZON";
		addObj.startDate=state.form.startDate;
		addObj.endDate=state.form.endDate;
		addObj.budget=parseFloat(state.form.budget);
		addObj.brandEntityId=state.form.store;
		addObj.name=state.form.campaignname;
		if(state.form.goal=="PAGE_VISIT"){
			addObj.costType="CPC";
			addObj.smartDefault=["MANUAL"];
		}else{
			addObj.costType="VCPM";
			addObj.smartDefault=["TARGETING"];
		}
		addObj.goal=state.form.goal;
		var bidding={};
		if(form.autobid==true){
			bidding.bidOptimization=true;
		}else{
			bidding.bidOptimization=false;
			var percentage=0;
			if(state.form.updown=="up"){
				percentage=parseInt(state.form.percentage);
			}else{
				percentage=parseInt(state.form.percentage)*-1;
			}
			if(percentage){
				bidding.bidAdjustmentsByPlacement=[{"placement":"OTHER","percentage":percentage}]
			}else{
				bidding.bidAdjustmentsByPlacement=[];
			}
			
		}
		addObj.bidding=bidding;
		addObj.state="ENABLED";
		array.push(addObj);
		data.campaignArray=array;
		var addstr=JSON.stringify(data);
		advCampaignApi.createCampaignList({"jsonstr":addstr}).then((res)=>{
		if(callback){
					callback(res);
				}else{
			    	handleToManagerPage();
				}
				}).catch(handleCatch); 
		}
	function saveAdvGroups(callback){
		var data = {};
		var array=[];
		var addObj={
			"name":state.form.adGroup,
			"campaignid": state.campaignid,
			"state": "ENABLED"
		}
		array.push(addObj);
		data.adGroupArray=array;
		var addstr=JSON.stringify(data);
		advAdgroupApi.createAdGroups( state.form.profileid,"sb",{"jsonstr":addstr}).then((res)=>{
			if(callback){
				callback(res);
			}else{
		    	handleToManagerPage();
			}
			}).catch(handleCatch); 
	}
	//保存ads
	function saveAdvProducts(callback){
		if(state.productList && state.productList.length>0){
			var data = {};
			var array=[];
			var url=null;
			if(state.form.storepage){
				state.form.storepageList.forEach(item=>{
					if(item.storepageid==state.form.storepage){
						url=item.storepageurl;
					}
				});
			}
			var asins=[];
			state.productList.forEach(item=>{
				asins.push(item.asin);
			});
			//商品集
			if(state.form.adFormat=="product"){
				var customImageAssetId=null;
				var brandLogoAssetID=null;
				if(state.form.image){
					brandLogoAssetID=state.form.image.assetid;
				}
				var creative={"brandLogoCrop":null,
				"asins":asins,
				"brandName":state.form.brandName,
				"customImageAssetId":customImageAssetId,"brandLogoAssetID":brandLogoAssetID,"headline":state.form.headline,
				"customImageCrop":null};
				var addObj={
					"landingPage":{"asins":asins,"pageType":state.form.pagetype,"url":null},
					"state": "ENABLED",
					"name":state.form.adname,
					"adGroupId":state.adgroupid,
					"creative":creative
				}
				array.push(addObj);
			}
			advSBAdsApi.createAds( state.form.profileid,"productCollection",{"ads":array}).then((res)=>{
			if(callback){
				callback(res);
			}else{
				handleToManagerPage();
			}
			}).catch(handleCatch); 
		}
	}
	function saveAdvKeys(callback){
		if(state.saveparam.keywords && state.saveparam.keywords.length>0){
			var data = {};
			var array=[];
			state.saveparam.keywords.forEach(item=>{
				if(item.bids && parseFloat(item.bids)>0){
					var addObj={
						"campaignid": state.campaignid,
						"state": "ENABLED",
						"adgroupid":state.adgroupid,
						"keywordtext":item.keywords,
						"matchtype":item.matchType,
						"bid":parseFloat(item.bids)
					}
					array.push(addObj);
				}
			});
			data.keywords=array;
			var addstr=JSON.stringify(data);
			advKeywordsApi.createKeywordList(state.form.profileid,"sb",{"jsonstr":addstr}).then((res)=>{
				if(callback){
					callback(res);
				}else{
					handleToManagerPage();
				}
				}).catch(handleCatch); 
		}
		
	}
	function saveAdvTarget(callback){
		if(state.saveparam.targets && state.saveparam.targets.length>0){
			var data = {};
			var array=[];
			state.saveparam.targets.forEach(item=>{
				if(item.bids && parseFloat(item.bids)>0){
					var expression=item.expression;
					var addObj={
						"campaignid": state.campaignid,
						"state": "ENABLED",
						"adgroupid":state.adgroupid,
						"expression":expression,
						"bid":parseFloat(item.bids)
					}
					array.push(addObj);
				}
			});
			data.targets=array;
			var addstr=JSON.stringify(data);
			advTargetApi.createTargetList(state.form.profileid,"sb",{"jsonstr":addstr}).then((res)=>{
				if(callback){
					callback(res);
				}else{
					handleToManagerPage();
				}
				}).catch(handleCatch); 
		}
		
	}
	
	//广告组-否定投放保存
	function saveAdvNegTarget(callback){
		if(state.saveparam.ne_target_expression && state.saveparam.ne_target_expression.length>0){
			var data = {};
			var array=[];
			var addObj={
				"campaignid": state.campaignid,
				"state": "ENABLED",
				"adgroupid": state.adgroupid,
				"expression":JSON.stringify(state.saveparam.ne_target_expression)
			}
			array.push(addObj);
			data.ntargets=array;
			var addstr=JSON.stringify(data);
			advTargetApi.createNegativaTargets(state.form.profileid,"sb",{"jsonstr":addstr}).then((res)=>{
				if(callback){
					callback(res);
				}else{
					handleToManagerPage();
				}
				}).catch(handleCatch); 
		}
		
	}
	
	//广告活动-否定投放保存
	function saveAdvCampNegTarget(callback){
		if(state.saveparam.ne_target_expression && state.saveparam.ne_target_expression.length>0){
			var data = {};
			var array=[];
			var addObj={
				"campaignid": state.campaignid,
				"state": "ENABLED",
				"expression":JSON.stringify(state.saveparam.ne_target_expression)
			}
			array.push(addObj);
			data.ntargets=array;
			var addstr=JSON.stringify(data);
			advTargetApi.createCampNegativaTargets(state.form.profileid,"sb",{"jsonstr":addstr}).then((res)=>{
				if(callback){
					callback(res);
				}else{
					handleToManagerPage();
				}
				}).catch(handleCatch); 
		}	
	}
	
	function saveAdvNeKeyword(callback){
		if(state.saveparam.ne_keywords && state.saveparam.ne_keywords.length>0){
			state.saveparam.ne_keywords.forEach(item=>{
				item.campaignid=state.campaignid;
				item.adgroupid=state.adgroupid;
			});
			var data={nkeywords:state.saveparam.ne_keywords};
			var addstr=JSON.stringify(data);
			advKeywordsApi.amzCreateKeywordNegativa(state.form.profileid,"sb",{"jsonstr":addstr}).then((res)=>{
					if(callback){
						callback(res);
					}else{
						handleToManagerPage();
					}
					}).catch(handleCatch); 
		 }
		
	}
	function saveAdvNeCampKeyword(callback){
		if(state.saveparam.ne_keywords && state.saveparam.ne_keywords.length>0){
			state.saveparam.ne_keywords.forEach(item=>{
				item.campaignid=state.campaignid;
				item.adgroupid=state.adgroupid;
			});
			var data={nkeywords:state.saveparam.ne_keywords};
			var addstr=JSON.stringify(data);
			advKeywordsApi.amzCreateCampKeywordNegativa(state.form.profileid,"sb",{"jsonstr":addstr}).then((res)=>{
				if(callback){
					callback(res);
				}else{
					handleToManagerPage();
				}
				}).catch(handleCatch);
		} 
	}
	
	function handleCatch(e){
		state.submitLoading=false;
	}
	function handleSubmit(){
		state.submitLoading=true;
		//否定投放提交
		if(state.adtype=="adntarget" ){
			saveAdvNegTarget();
		}
		if(state.adtype=="adcampntarget" ){
			saveAdvCampNegTarget();
		}
		if(state.adtype=="adnkey" ){
			saveAdvNeKeyword();
		}
		if(state.adtype=="adcampnkey" ){
			saveAdvNeCampKeyword();
		}
		if(state.adtype=="adkey" ){
			saveAdvKeys();
		}
		if(state.adtype=="adtarget" ){
			saveAdvTarget();
		}
		if(state.adtype=="ProductAds" ){
			saveAdvProducts();
		}
		if(state.adtype=="adgroups" ){
				state.processmsg="开始提交广告组";
				saveAdvGroups((resgroup)=>{
					 if(resgroup.data && resgroup.data.length>0){
				        state.processmsg="广告组提交成功，开始提价商品广告";
					    state.adgroupid=resgroup.data[0].adgroupid;
					 saveAdvProducts((resproduct)=>{
							state.processmsg="商品广告提交成功";
							saveAdvTarget(()=>{state.processmsg="投放提交成功";});
							saveAdvKeys(()=>{state.processmsg="关键词提交成功";});
							saveAdvNeKeyword(()=>{state.processmsg="否定关键词提交成功";});
							saveAdvNegTarget(()=>{state.processmsg="否定投放提交成功";});
					 	});
					  }
				});
		}
		
		if(state.adtype=="adcams" ){
				saveAdvcams(rescamp=>{
					state.campaignid=rescamp.data[0].campaignid;
					saveAdvGroups((resgroup)=>{
						state.adgroupid=resgroup.data[0].adgroupid;
						saveAdvProducts((resproduct)=>{
							state.processmsg="商品广告提交成功";
							saveAdvTarget(()=>{state.processmsg="投放提交成功";});
							saveAdvKeys(()=>{state.processmsg="关键词提交成功";});
							saveAdvNeKeyword(()=>{state.processmsg="否定关键词提交成功";});
							saveAdvNegTarget(()=>{state.processmsg="否定投放提交成功";});
						});
					});
				})
			}
			
	}
	
	function handleAdgroupSubmit(){
		   if(state.adtype=="adcams"){
			   saveAdvcams(rescamp=>{
				   state.campaignid=rescamp.data[0].campaignid;
				   saveAdvGroups((resgroup)=>{
				   	if(resgroup.data && resgroup.data.length>0){
				   		state.adgroupid=resgroup.data[0].adgroupid;
				   		state.adgroup=resgroup.data[0];
						ElMessage.success("广告组保存成功！");
						saveAdvProducts((resproduct)=>{
							ElMessage.success("商品广告提交成功！");
							getProductData(state.productList);
						})
				   	}
				   });
			   });
		   }
			if(state.adtype=="adgroups"){
				saveAdvGroups((resgroup)=>{
					if(resgroup.data && resgroup.data.length>0){
						state.adgroupid=resgroup.data[0].adgroupid;
						state.adgroup=resgroup.data[0];
						ElMessage.success("广告组保存成功！");
						saveAdvProducts((resproduct)=>{
							ElMessage.success("商品广告提交成功！");
							getProductData(state.productList);
						})
					}
				});
			}
	 
	}
	function handleToManagerPage(){
		router.push({
			path:'/amazon/manager',
			query:{
			  title:"广告管理",
			  path:'/amazon/manager',
			  replace:true,
			  refresh:true,
			}
		}) 
	}
</script>

<style scoped>
 .l-content{
	 width:208px;
	 padding:16px;
 }
 .ad-body{
	 padding:0px 24px;
 }
  .r-content{
	  padding:16px 0px;
  }
 .item-tab{
	 font-size:14px;
	 line-height:40px;
	 padding-left:24px;
	 border-radius: 2px;
	 cursor:pointer;
	 color:#999;
 }
  .item-tab.active {
	  background-color:#f5f5f5;
  }
  .item-tab.active .el-link{
	  color:#333!important;
  }
  .flex-center{
	  margin-bottom:24px;
  }
  .have-ass-text-radio .el-radio{
  	height:inherit!important;
  	line-height:24px;
  	white-space: inherit;
	margin-bottom:16px;
	align-items:start;
  }

  .have-ass-text-radio p{
  	font-size:12px;
  	color:#acb0b9;
  }
  .footer-box{
	  background-color: var(--el-bg-color-overlay);
	  box-shadow: 0 2px 6px 0 rgba(0, 0, 0, 0.1);
	  padding:16px 24px;
	  width:100%;
  }
</style>
<style>
	.have-ass-text-radio  .el-radio__input{
		  padding-top:6px;
	}
</style>