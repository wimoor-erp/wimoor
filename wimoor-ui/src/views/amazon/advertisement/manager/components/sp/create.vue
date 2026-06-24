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
						</el-input>
					</el-form-item>
					<el-form-item label="投放类型" prop="target" required>
						<el-radio-group  v-model="form.target"
						 @change="targetChange"
						 class="have-ass-text-radio">
							<div>
							<div>
						     <el-radio label="AUTO" >
							 自动投放
							  <p >亚马逊会根据您的商品信息将您的广告定位到买家的搜索词，这能帮助您了解到哪些关键词在手动投放中效果良好。</p>
							  </el-radio>
							  </div>
							<el-radio label="MANUAL" >
							  手动投放
							  <p>当您知道哪些关键字为您的企业带来最高价值时，可使用该策略。您可以为单个关键字设置自定义出价。</p>
							  </el-radio>
							    </div>
						</el-radio-group>
					</el-form-item>
					<!-- 竞价策略 -->	
					<BiddingTactics  :forms="form"/>
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
						<Bidding v-if="campaign.targetingType=='AUTO' || form.target=='AUTO'"  :forms="form"/>
						<el-form-item v-else label="默认竞价" prop="bidding" required>
						<el-input   v-model="form.bidding" @change="bidingChange" placeholder="请输入" style="width:214px">
							<template #prepend>$</template>
						</el-input>
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
					<el-form-item v-if="!adgroupid"  >
					 <el-button link type="primary"   @click="handleAdgroupSubmit" >保存广告组</el-button>
					 <span class="font-extraSmall"> 商品投放建议竞价需先保存广告组！</span>
					</el-form-item>	
					<!-- 竞价 -->
					<div  v-if="show.target || show.keywords">
					<div class="flex-center ">
					<div class="mark-squre"></div>
					<div id="target" class="font-base font-bold tab-scroll">
						投放
					</div>
					</div>
				
					<Target 
					   ref="targetRef" @change="getKeywordOrTargeData" :param="state.targetparam" @changetype="changeStrategy" />
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
					 <el-button @click.stop="handleToManagerPage"><span v-if="processmsg">返回</span><span v-else>取消</span></el-button>
					 <el-button type="primary" :loading="submitLoading" v-if="!processmsg" @click.stop="handleSubmit">提交</el-button>
					<span style="padding-left:10px;" class="font-extraSmall">{{processmsg}}</span>
				 </div>
				</el-affix>
				</el-Scrollbar>
		
		 </div>
	</div>	
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
	import tabScroll from"@/utils/tab_scroll";
	import portfoliosApi from '@/api/amazon/advertisement/portfolios/portfoliosApi.js'; 
	import advCampaignApi from '@/api/amazon/advertisement/report/advCampaignApi.js';
	import advAdgroupApi from '@/api/amazon/advertisement/report/advAdgroupApi.js';
	import advProductsApi from '@/api/amazon/advertisement/report/advProductsApi.js';
	import advKeywordsApi from '@/api/amazon/advertisement/report/advKeywordsApi.js';
	import advTargetApi from '@/api/amazon/advertisement/report/advTargetApi.js';
	import advertApi from '@/api/amazon/advertisement/report/advertApi.js';
	import productinfoApi from '@/api/amazon/product/productinfoApi.js';
	import {dateFormat,formatFloat,dateTimesFormat,CheckInputFloat,CheckInputInt} from '@/utils/index.js';
	import { useRoute,useRouter } from 'vue-router';
	import bidLimit from '@/model/amazon/advertisement/bidLimit.json';
	import {ElMessage,ElDivider} from 'element-plus';
 
	let router = useRouter();
	const productAddRef=ref();
	const negativeKeywordsRef=ref();
	const negativeProductTargetRef=ref();
	const targetRef=ref();
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
		hasSubmitAdgroup:false,
		tabs:[
			{name:'广告活动',value:1,id:'campaign',},
			{name:'广告组',value:2,id:'adGroup',},
			{name:'商品',value:3,id:'product'},
			{name:'关键词',value:4,id:'keywords'},
			{name:'投放',value:5,id:'target',show:true,},
			{name:'广告组-否定关键词',value:6,id:'nekeyword'}, 
			{name:'活动-否定关键词',value:7,id:'campnekeyword'}, 
			{name:'用户搜索词',value:8,id:'keywordquery'}, 
			{name:'广告组-否定投放',value:9,id:"netarget"},
			{name:'活动-否定投放',value:10,id:'campnetarget'}, 
			{name:'用户投放搜索',value:11,id:'targetquery'}, 
		],
		form:{
			target:"MANUAL",
			dynamicBidding:{"strategy":'LEGACY_FOR_SALES',
			  "placementBidding":[
				  {"percentage":0,"placement":"PLACEMENT_TOP"},
				  {"percentage":0,"placement":"PLACEMENT_PRODUCT_PAGE"},
			  ]
			},
			portfolioid:null,
			budget:1,
			campaignname:'',startDate:new Date(),
			time:'',targetType:'keywords',
			biddingTable:[{
				status:true,
				name:'紧密匹配',
				sugBid:{bid:"",start:"",end:""},
				type:"CLOSE_MATCH",
				submitType:"QUERY_BROAD_REL_MATCHES"
			},{
				status:true,
				name:'宽泛匹配',
				sugBid:{bid:"",start:"",end:""},
				type:"LOOSE_MATCH",
				submitType:"QUERY_HIGH_REL_MATCHES"
			},{
				status:true,
				name:'同类商品',
			    sugBid:{bid:"",start:"",end:""},
				type:"SUBSTITUTES",
				submitType:"ASIN_SUBSTITUTE_RELATED"
			},{
				status:true,
				name:'关联商品',
				sugBid:{bid:"",start:"",end:""},
				type:"COMPLEMENTS" ,
				submitType:"ASIN_ACCESSORY_RELATED"
			}],
			checkList:['广泛匹配','词组匹配'],
			biddingType:'1',
			
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
		targetparam:{},
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
		targetparam,
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
	function getNetargetData(data){
		state.saveparam.ne_target_expression=data;
	}
	function getNeKeywordData(data){
		state.saveparam.ne_keywords=data;
	}
	function bidingChange(){
		if(state.adgroup){
			state.targetparam.adgroup=state.adgroup;
		}else{
			state.targetparam.adgroup={"defaultbid":state.form.bidding};
		}
	}
	function getProductData(data){
		state.productList=data;
		state.targetparam.adtype=state.adtype;
		state.targetparam.campaignType="sp";
		if(state.adgroup){
			state.targetparam.adgroup=state.adgroup;
		}else{
			state.targetparam.adgroup={"defaultbid":state.form.bidding};
		}
		state.targetparam.adgroupid=state.adgroupid; 
		state.targetparam.campaignid=state.campaignid;
		state.targetparam.sellerid=state.profile.sellerid;
		state.targetparam.marketplaceid=state.profile.marketplaceid;
		state.targetparam.profileid=state.form.profileid; 
		state.targetparam.groupid=state.form.groupid;
		state.targetparam.currency=state.profile.currency;
		state.targetparam.querylist=router.currentRoute.value.query.querylist;
		state.targetparam.productList=state.productList;
		state.targetparam.campaign=state.campaign;
		if(targetRef&&targetRef.value&&targetRef.value.show){
				targetRef.value.show(state.targetparam);
		}
		if(negativeKeywordsRef&&negativeKeywordsRef.value&&negativeKeywordsRef.value.show){
			negativeKeywordsRef.value.show(state.targetparam);
		}
		if(negativeProductTargetRef&&negativeProductTargetRef.value&&negativeProductTargetRef.value.show){
			negativeProductTargetRef.value.show(state.targetparam);
		}
		//如果是auto且为投放组竞价 则根据asin加载建议竞价
		if(state.form.biddingType=="2"){
			if(state.productList && state.productList.length>0){
				var asins=[];
				state.productList.forEach(item=>{
					asins.push(item.asin);
				});
				loadAutoBidSuggest(asins);
			}
			
		}
	}
	function loadAutoBidSuggest(asins){
		var params={ "asins":asins,
						"targetingExpressions": [
							{ "type": "CLOSE_MATCH" },
							{ "type": "LOOSE_MATCH" },
							{ "type": "SUBSTITUTES" },
							{ "type": "COMPLEMENTS" }
						],
						"bidding": {
						"adjustments": state.form.dynamicBidding.placementBidding,
						"strategy":state.form.dynamicBidding.strategy
						},
						"recommendationType": "BIDS_FOR_NEW_AD_GROUP"
						};
		advTargetApi.getTargetsBidRecommendations(state.form.profileid,"sp",params).then((res)=>{
				if(res.data){
					var datas=JSON.parse(res.data);
					if(datas){
						datas.bidRecommendations.forEach(item=>{
							if(item.theme=="CONVERSION_OPPORTUNITIES"){
								item.bidRecommendationsForTargetingExpressions.forEach(items=>{
									state.form.biddingTable.forEach(biditem=>{
										if(biditem.type==items.targetingExpression.type){
											 biditem.sugBid={
														bid:items.bidValues[0].suggestedBid,
														start:items.bidValues[1].suggestedBid,
														end:items.bidValues[2].suggestedBid,
														bidding:items.bidValues[0].suggestedBid,
											  };
										}
									}) 
								});
							}
						});
					}
				}
			}).catch(()=>{
				 
			});
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
	 
	function initShow(){
		if(state.adtype=="adcams"){
			state.show={productAds:true,keywords:true,nekeywords:true,campnekeyword:true,target:true,netarget:true,campnetarget:true,adgroup:true,campaign:true};
			state.tabs=[
				{name:'广告活动',value:1,id:'campaign',},
				{name:'广告组',value:2,id:'adgroup',},
				{name:'商品',value:3,id:'product'},
				{name:'投放',value:4,id:'target'},
				{name:'否定关键词',value:5,id:'nekeyword'}, 
				{name:'否定投放',value:6,id:"netarget"},
			];
		}
		if(state.adtype=="adgroups"){
			var targetingType="MANUAL";
			if(state.campaign&&state.campaign.targetingType){
				targetingType=state.campaign.targetingType.toUpperCase();
			}
			if(targetingType=="AUTO"){
				state.show={productAds:true,keywords:false,nkeywords:true,campnekeyword:false,target:false,ntarget:true,campnetarget:false,adgroup:true,campaign:false};
				state.tabs=[
					{name:'广告活动',value:1,id:'campaign',},
					{name:'广告组',value:2,id:'adgroup',},
					{name:'商品',value:3,id:'product'},
					{name:'否定关键词',value:4,id:'nekeyword'}, 
					{name:'否定投放',value:5,id:"netarget"},
				];
			}else{
				state.show={productAds:true,keywords:true,nkeywords:true,campnekeyword:false,target:true,ntarget:true,campnetarget:false,adgroup:true,campaign:false};
				state.tabs=[
					{name:'广告活动',value:1,id:'campaign',},
					{name:'广告组',value:2,id:'adgroup',},
					{name:'商品',value:3,id:'product'},
					{name:'投放',value:4,id:'target'},
					{name:'否定关键词',value:5,id:'nekeyword'}, 
					{name:'否定投放',value:6,id:"netarget"},
				];
			}
		}
		if(state.adtype=="ProductAds"){
			state.show={productAds:true,keywords:false,nkeywords:false,campnekeyword:false,target:false,ntarget:false,campnetarget:false,adgroup:false,campaign:false};
			state.tabs=[
				{name:'广告活动',value:1,id:'campaign',},
				{name:'广告组',value:2,id:'adgroup',},
				{name:'商品',value:3,id:'product'},
			];
		}
		if(state.adtype=="adkey"){
			state.show={productAds:false,keywords:true,nkeywords:false,campnekeyword:false,target:false,ntarget:false,campnetarget:false,adgroup:false,campaign:false};
			state.tabs=[
				{name:'广告活动',value:1,id:'campaign',},
				{name:'广告组',value:2,id:'adgroup',},
				{name:'商品',value:3,id:'product'},
				{name:'投放',value:4,id:'keywords'},
			];
		}
		if(state.adtype=="adnkey"){
			state.show={productAds:false,keywords:false,nkeywords:true,campnekeyword:false,target:false,ntarget:false,campnetarget:false,adgroup:false,campaign:false};
			state.tabs=[
				{name:'广告活动',value:1,id:'campaign',},
				{name:'广告组',value:2,id:'adgroup',},
				{name:'商品',value:3,id:'product'},
				{name:'否定关键词',value:4,id:'nekeywords'}, 
			];
		}
		if(state.adtype=="adcampnkey"){
			state.show={productAds:false,keywords:false,nkeywords:false,campnekeyword:true,target:false,ntarget:false,campnetarget:false,adgroup:false,campaign:false};
			state.tabs=[
				{name:'广告活动',value:1,id:'campaign',},
				{name:'广告组',value:2,id:'adgroup',},
				{name:'商品',value:3,id:'product'},
				{name:'活动-否定关键词',value:4,id:'campnekeyword'}, 
			];
		}
		if(state.adtype=="adtarget"){
			state.show={productAds:false,keywords:false,nkeywords:false,campnekeyword:false,target:true,ntarget:false,campnetarget:false,adgroup:false,campaign:false};
			state.tabs=[
				{name:'广告活动',value:1,id:'campaign',},
				{name:'广告组',value:2,id:'adgroup',},
				{name:'商品',value:3,id:'product'},
				{name:'投放',value:4,id:'target'},
			];
		}
		if(state.adtype=="adntarget"){
			state.show={productAds:false,keywords:false,nkeywords:false,campnekeyword:false,target:false,ntarget:true,campnetarget:false,adgroup:false,campaign:false};
			state.tabs=[
				{name:'广告活动',value:1,id:'campaign',},
				{name:'广告组',value:2,id:'adgroup',},
				{name:'商品',value:3,id:'product'},
				{name:'否定投放',value:4,id:"netarget"},
			];
		}
		if(state.adtype=="adcampntarget"){
			state.show={productAds:false,keywords:false,nkeywords:false,campnekeyword:false,target:false,ntarget:false,campnetarget:true,adgroup:false,campaign:false};
			state.tabs=[
				{name:'广告活动',value:1,id:'campaign',},
				{name:'广告组',value:2,id:'adgroup',},
				{name:'商品',value:3,id:'product'},
				{name:'活动-否定投放',value:4,id:'campnetarget'}, 
			];
		}
	}
	function targetChange(val){
		 state.show={productAds:true,keywords:false,nekeywords:false,campnekeyword:false,target:false,netarget:false,campnetarget:false,adgroup:true,campaign:true};
	}
	
	 
 
	function changeGroup(data,type){
		state.form.groupid=data.groupid;
		state.form.profileid=data.profileid;
		state.profile=data.profile;
		loadPofoData();
		loadCampaigns();
		if(type=="load"){
		    initShow();
		}
	}
	function loadProductList(){
		var params={};
		params.adtype=state.adtype;
	    params.campaignType="sp";
		params.adgroupid=state.adgroupid; 
		params.campaignid=state.campaignid;
		params.adgroup=state.adgroup;
		params.sellerid=state.profile.sellerid;
		params.marketplaceid=state.profile.marketplaceid;
		params.profileid=state.form.profileid; 
		params.groupid=state.form.groupid;
		params.currency=state.profile.currency;
		params.skulist=null;
		params.querylist=router.currentRoute.value.query.querylist;
		if(state.adgroupid && state.campaignid){
			advProductsApi.getProductAdsDetail(state.form.profileid,params.campaignType,{"adgroupid":state.adgroupid}).then((res)=>{
				if(res.data && res.data.length>0){
					var skulist=[];
					res.data.forEach(item=>{
						skulist.push(item.sku);
					});
					params.skulist=skulist;
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
		if(state.campaignid!=state.campaign.campaignid){
			state.campaignList.forEach(item=>{
				if(item.campaignid==state.campaignid){
					state.campaign=item;
					if(state.campaign.targetingType){
					   state.campaign.targetingType=state.campaign.targetingType.toUpperCase()
					}
				}
			});
		}
		advAdgroupApi.loadAdGroup({"profileid":state.form.profileid,"campaignsid":state.campaignid,"campaignType":"sp"}).then((res)=>{
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
		advCampaignApi.getCampaignDetail(state.form.profileid,"sp").then((res)=>{
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
		addObj.campaignType="sp";
		addObj.startDate=state.form.startDate;
		addObj.endDate=state.form.endDate;
		addObj.budget={"budgetType":"DAILY","budget":parseFloat(state.form.budget)};
		addObj.name=state.form.campaignname;
		addObj.targetingType=state.form.target;
		var placementBidding=[];
		if(state.form.dynamicBidding.placementBidding){
			state.form.dynamicBidding.placementBidding.forEach(item=>{
				if(item.percentage){
					item.percentage=parseFloat(item.percentage);
					placementBidding.push(item);
				}
			});
			state.form.dynamicBidding.placementBidding=placementBidding;
		}
		addObj.bidding=state.form.dynamicBidding;
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
		var market=state.profile.market;
		var addObj={
				"name":state.form.adGroup,
				"campaignid": state.campaignid,
				"defaultbid": 0.1,
				"state": "ENABLED"
		};
		if(state.campaign.targetingType=="AUTO"){
			var bidding=0.1;
			//根据国家拿取bid
			if(bidLimit[market] && bidLimit[market].SP){
				bidding=bidLimit[market].SP.from;
			}
			addObj.defaultbid=bidding;
		}else{
			addObj.defaultbid=state.form.bidding;
		}
		var data = {};
		var array=[];
		
		array.push(addObj);
		data.adGroupArray=array;
		var addstr=JSON.stringify(data);
		advAdgroupApi.createAdGroups( state.form.profileid,"sp",{"jsonstr":addstr}).then((res)=>{
			if(callback){
				callback(res);
			}else{
		    	handleToManagerPage();
			}
			}).catch(handleCatch); 
	}
	function saveAdvProducts(callback){
		if(state.productList && state.productList.length>0){
			var data = {};
			var array=[];
			state.productList.forEach(item=>{
				var addObj={
					"campaignid": state.campaignid,
					"state": "ENABLED",
					"sku":item.sku,
					"adgroupid":state.adgroupid
				}
				array.push(addObj);
			});
			data.productAds=array;
			var addstr=JSON.stringify(data);
			advProductsApi.createProductAdList( state.form.profileid,"sp",{"jsonstr":addstr}).then((res)=>{
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
			advKeywordsApi.createKeywordList(state.form.profileid,"sp",{"jsonstr":addstr}).then((res)=>{
				if(callback){
					callback(res);
				}else{
					handleToManagerPage();
				}
				}).catch(handleCatch); 
		}
		
	}
	function saveAdvAutoTarget(callback){
			var data = {};
			var array=[];
			var param={adGroupIdFilter:{include:[state.adgroupid]},expressionTypeFilter:{include:["AUTO"]}}
			advTargetApi.targetsByParam(state.form.profileid,"sp",param).then(res=>{
				var targets=[];
				res.data.forEach(item=>{
					var bid=0;
					if(state.forms.biddingType=="1"&&state.forms.bidding){
						bid=state.forms.bidding;
					}else{
						state.form.biddingTable.forEach(mybid=>{
							if(item.expression.indexOf(mybid.submitType)>=0){
								bid=mybid.sugBid.bidding;
							}
						})
					}
					if(bid){
						var addObj={
							"campaignid": item.campaignid,
							"state": "ENABLED",
							"adgroupid":item.adgroupid,
							"expression":item.expression,
							"targetid":item.targetid,
							"expressiontype":item.expressiontype,
							"bid":bid
						}
						 targets.push(addObj);
					}
				})
				data.targets=targets;
				var addstr=JSON.stringify(data);
				advTargetApi.updateTargetList(state.form.profileid,"sp",{"jsonstr":addstr}).then((res)=>{
						if(callback){
							callback(res);
						} 
					}).catch(handleCatch); 
			});
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
						"expressiontype":state.campaign.targetingType.toUpperCase(),
						"bid":parseFloat(item.bids)
					}
					array.push(addObj);
				}
			});
			data.targets=array;
			var addstr=JSON.stringify(data);
			advTargetApi.createTargetList(state.form.profileid,"sp",{"jsonstr":addstr}).then((res)=>{
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
			advTargetApi.createNegativaTargets(state.form.profileid,"sp",{"jsonstr":addstr}).then((res)=>{
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
			advTargetApi.createCampNegativaTargets(state.form.profileid,"sp",{"jsonstr":addstr}).then((res)=>{
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
			advKeywordsApi.amzCreateKeywordNegativa(state.form.profileid,"sp",{"jsonstr":addstr}).then((res)=>{
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
			advKeywordsApi.amzCreateCampKeywordNegativa(state.form.profileid,"sp",{"jsonstr":addstr}).then((res)=>{
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
			if(state.adgroupid && state.adgroupid!=null && state.adgroupid!="" && state.adgroupid!=undefined){
						saveAdvProducts((resproduct)=>{
							state.processmsg="商品广告提交成功";
							if(state.campaign.targetingType=="AUTO"){
								saveAdvAutoTarget(()=>{state.processmsg="投放提交成功";});
							}else{
								saveAdvTarget(()=>{state.processmsg="投放提交成功";});
								saveAdvKeys(()=>{state.processmsg="关键词提交成功";});
							}
							saveAdvNeKeyword(()=>{state.processmsg="否定关键词提交成功";});
							saveAdvNegTarget(()=>{state.processmsg="否定投放提交成功";});
						});
			}else{
				if(state.form.bidding && parseFloat(state.form.bidding)>0){
					state.processmsg="开始提交广告组";
					saveAdvGroups((resgroup)=>{
						if(resgroup.data && resgroup.data.length>0){
					        state.processmsg="广告组提交成功，开始提价商品广告";
							state.adgroupid=resgroup.data[0].adgroupid;
							saveAdvProducts((resproduct)=>{
								state.processmsg="商品广告提交成功";
								if(state.campaign.targetingType=="AUTO"){
									saveAdvAutoTarget(()=>{state.processmsg="投放提交成功";});
								}else{
									saveAdvTarget(()=>{state.processmsg="投放提交成功";});
									saveAdvKeys(()=>{state.processmsg="关键词提交成功";});
								}
								saveAdvNeKeyword(()=>{state.processmsg="否定关键词提交成功";});
								saveAdvNegTarget(()=>{state.processmsg="否定投放提交成功";});
							});
						}
					});
				}else{
					ElMessage.erroe("请保存广告组默认竞价！");
				}
				
			}
			
		}
		
		if(state.adtype=="adcams" ){
			if(state.hasSubmitAdgroup){
				saveAdvTarget(()=>{state.processmsg="投放提交成功";});
				saveAdvKeys(()=>{state.processmsg="关键词提交成功";});
				saveAdvNeKeyword(()=>{state.processmsg="否定关键词提交成功";});
				saveAdvNegTarget(()=>{state.processmsg="否定投放提交成功";});
			}
			else if(state.adgroupid && state.adgroupid!=null && state.adgroupid!="" && state.adgroupid!=undefined){
						saveAdvProducts((resproduct)=>{
							state.processmsg="商品广告提交成功";
							saveAdvTarget(()=>{state.processmsg="投放提交成功";});
							saveAdvKeys(()=>{state.processmsg="关键词提交成功";});
							saveAdvNeKeyword(()=>{state.processmsg="否定关键词提交成功";});
							saveAdvNegTarget(()=>{state.processmsg="否定投放提交成功";});
					});
			}else{
				if(state.form.bidding && parseFloat(state.form.bidding)>0){
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
				}else{
					ElMessage.erroe("请保存广告组默认竞价！");
				}
				
			}
			
			
		}
	}
	
	function handleAdgroupSubmit(){
		if(state.form.bidding && parseFloat(state.form.bidding)>0){
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
										state.hasSubmitAdgroup=true;
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
										state.hasSubmitAdgroup=true;
									})
								}
							});
						}
		}else{
			ElMessage.erroe("请保存广告组默认竞价！");
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