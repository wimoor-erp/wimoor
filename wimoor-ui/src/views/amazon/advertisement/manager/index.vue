<template>
	<div class="wi-content ">
		<div class="wi-left-c">
			<AdTree @change="getData" />
		</div>
		<div class="wi-right-c el-white-bg">
			<div class="bread-head flex-center-between">
				<div class="bread-nav">
					<el-icon class="ad-home" @click.stop="clearBtn"><HomeFilled /></el-icon>
					  <el-breadcrumb separator="/"  >
						<!--  :to="{ path: '/' }" -->
					       <el-breadcrumb-item     @click.stop="handleTitle('portfolios')">
							   <div  class="titleItem pointer">
								    portfolios
									<el-tooltip v-if="portfoliostitle" :content="portfoliostitle">({{portfoliostitle}})</el-tooltip>
								</div>
						    </el-breadcrumb-item>
					    <el-breadcrumb-item  @click.stop="handleTitle('campaigns')">
							<div class="titleItem pointer" >
								广告活动
							   <el-tooltip v-if="advcamstitle" :content="advcamstitle">({{advcamstitle}})</el-tooltip>
						   </div>
						</el-breadcrumb-item>
						<el-breadcrumb-item>
							<div class="titleItem" >
							    广告组 
							   <el-tooltip v-if="advgroupstitle"  :content="advgroupstitle">  ({{advgroupstitle}}) </el-tooltip>
							</div>
						</el-breadcrumb-item>
					  </el-breadcrumb>
				</div>
				<div class="font-extraSmall m-r-16">
					<span>{{marketname}}时间 :
					{{nowDate}} 
					{{nowTime}}
					{{nowWeek}}
					</span>
				</div>
			</div>
			
			<div class="ad-body-c">
				<div class="con-header">
					<div>
						  <el-tabs v-model="activeName"  ref="eltabsRef"    @tab-change="handleClick">
						    <el-tab-pane  v-for="(item,index) in tabsData"  :label="item.name" :name="item.value">
								<template #label>{{item.name}} &nbsp;<span>{{item.count}}</span></template>
							</el-tab-pane>
						  </el-tabs>
					</div>
				  <div class="con-body" v-if="queryParams.campaignType=='SP'">
					    <div v-show="activeName=='adcams'">
							<SPListCampaigns  
							ref="spListCampaignsRef" 
							:activeName="activeName" 
							@selectRow="selectRow" 
							@change="getSummary" 
							@bindData="handleBindData" />
						</div>
						<div v-show="activeName=='adgroups'">
							<SPListAdgroups 
							ref="spListAdgroupsRef" 
							:activeName="activeName" 
							@selectRow="selectRow" 
							@change="getSummary" 
							@bindData="handleBindData" />
						</div>
						<div v-show="activeName=='ProductAds'">
							<SPListProductAds 
							ref="spListProductAdsRef" 
							:activeName="activeName" 
							@selectRow="selectRow" 
							@change="getSummary" 
							@bindData="handleBindData" />
						</div>
					 
						<SPListKeywords v-if="activeName=='adkey'"
						ref="spListKeywordsRef" 
						:activeName="activeName" 
						@selectRow="selectRow" 
						@change="getSummary" 
						@bindData="handleBindData" />
					 
						<SPListKeywordsQuery 
						v-if="activeName=='adsearch'"
						ref="spListKeywordsQueryRef" 
						:activeName="activeName" 
						@selectRow="selectRow" 
						@change="getSummary" 
						@bindData="handleBindData" />
						
						<SPListNegativaKeywords 
						v-if="activeName=='adnkey'"
						ref="spListNegativaKeywordsRef" 
						:activeName="activeName" 
						@selectRow="selectRow" 
						@change="getSummary" 
						@bindData="handleBindData" />
						
						<SPListCampNegativaKeywords 
						v-if="activeName=='adcampnkey'"
						ref="spListCampNegativaKeywordsRef" 
						:activeName="activeName" 
						@selectRow="selectRow" 
						@change="getSummary" 
						@bindData="handleBindData" />
						
						<SPListTarget v-if="activeName=='adtarget'"
						ref="spListTargetRef" 
						:activeName="activeName" 
						@selectRow="selectRow" 
						@change="getSummary" 
						@bindData="handleBindData" />
						
						<SPListTargetQuery
						v-if="activeName=='adtargetquery'"
						ref="spListTargetQueryRef" 
						:activeName="activeName" 
						@selectRow="selectRow" 
						@change="getSummary" 
						@bindData="handleBindData" />
						
						<SPListNegativaTarget 
						v-if="activeName=='adntarget'"
						ref="spListNegativaTargetRef" 
						:activeName="activeName" 
						@selectRow="selectRow" 
						@change="getSummary" 
						@bindData="handleBindData" />
					 
						<SPListCampNegativaTarget
						v-if="activeName=='adcampntarget'"
						ref="spListCampNegativaTargetRef" 
						:activeName="activeName" 
						@selectRow="selectRow" 
						@change="getSummary" 
						@bindData="handleBindData" />
						
					 <div v-show="activeName=='purchaseProduct'">
					 	<SPListPurchaseProduct 
					 	ref="spListPurchaseProductRef" 
					 	:activeName="activeName" 
					 	@selectRow="selectRow" 
					 	@bindData="handleBindData" />
					 </div>
						
					</div> 
					<div class="con-body" v-if="queryParams.campaignType=='SB'">
					    <div v-show="activeName=='adcams'">
							<SBListCampaigns 
							ref="sbListCampaignsRef" 
							:activeName="activeName" 
							@selectRow="selectRow" 
							@change="getSummary" 
							@bindData="handleBindData" />
						</div>
						<div v-show="activeName=='adgroups'">
							<SBListAdgroups 
							ref="sbListAdgroupsRef" 
							:activeName="activeName" 
							@selectRow="selectRow" 
							@change="getSummary" 
							@bindData="handleBindData" />
						</div>
						<div v-show="activeName=='ProductAds'">
							<SBListProductAds 
							ref="sbListProductAdsRef" 
							:activeName="activeName" 
							@selectRow="selectRow" 
							@change="getSummary" 
							@bindData="handleBindData" />
						</div>
						
						<SBListKeywords 
						v-if="activeName=='adkey'"
						ref="sbListKeywordsRef" 
						:activeName="activeName" 
						@selectRow="selectRow" 
						@change="getSummary" 
						@bindData="handleBindData" />
						
						<SBListKeywordsQuery 
						v-if="activeName=='adsearch'"
						ref="sbListKeywordsQueryRef" 
						:activeName="activeName" 
						@selectRow="selectRow" 
						@change="getSummary" 
						@bindData="handleBindData" />
						
						<SBListNegativaKeywords 
						 v-if="activeName=='adnkey'"
						ref="sbListNegativaKeywordsRef" 
						:activeName="activeName" 
						@selectRow="selectRow" 
						@change="getSummary" 
						@bindData="handleBindData" />
						<SBListTarget 
						v-if="activeName=='adtarget'"
						ref="sbListTargetRef" 
						:activeName="activeName" 
						@selectRow="selectRow" 
						@change="getSummary" 
						@bindData="handleBindData" />
						
						<SBListTargetQuery 
						v-if="activeName=='adtargetquery'"
						ref="sbListTargetQueryRef" 
						:activeName="activeName" 
						@selectRow="selectRow" 
						@change="getSummary" 
						@bindData="handleBindData" />
						
						<SBListNegativaTarget 
						ref="sbListNegativaTargetRef" 
						v-if="activeName=='adntarget'"
						:activeName="activeName" 
						@selectRow="selectRow" 
						@change="getSummary" 
						@bindData="handleBindData" />
						
						<div v-show="activeName=='purchaseProduct'">
							<SBListPurchaseProduct 
							ref="sbListPurchaseProductRef" 
							:activeName="activeName" 
							@selectRow="selectRow" 
							@bindData="handleBindData" />
						</div>
					</div> 
					<div class="con-body" v-if="queryParams.campaignType=='SD'">
						<div v-show="activeName=='adcams'">
							<SDListCampaigns 
							ref="sdListCampaignsRef" 
							:activeName="activeName" 
							@selectRow="selectRow" 
							@change="getSummary" 
							@bindData="handleBindData" />
						</div> 
						<div v-show="activeName=='adgroups'">
							<SDListAdgroups 
							ref="sdListAdgroupsRef" 
							:activeName="activeName" 
							@selectRow="selectRow" 
							@change="getSummary" 
							@bindData="handleBindData" />
						</div>
						<div v-show="activeName=='ProductAds'">
							<SDListProductAds 
							ref="sdListProductAdsRef" 
							:activeName="activeName" 
							@selectRow="selectRow" 
							@change="getSummary" 
							@bindData="handleBindData" />
						</div>
						<SDListTarget
						v-if="activeName=='adtarget'"
						ref="sdListTargetRef" 
						:activeName="activeName" 
						@selectRow="selectRow" 
						@change="getSummary" 
						@bindData="handleBindData" />
						
						<SDListNegativaTarget 
						v-if="activeName=='adntarget'" 
						ref="sdListNegativaTargetRef" 
						:activeName="activeName" 
						@selectRow="selectRow" 
						@change="getSummary" 
						@bindData="handleBindData" />
						<div v-show="activeName=='purchaseProduct'">
							<SDListPurchaseProduct 
							ref="sdListPurchaseProductRef" 
							:activeName="activeName" 
							@selectRow="selectRow" 
							@bindData="handleBindData" />
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</template>
<script>
    export default{ name:"广告管理" };
</script>
<script setup>
	import {h, ref ,watch,reactive,onMounted,toRefs,nextTick,} from 'vue';
	import {HomeFilled,Search,Menu,Histogram,ArrowDown,Close} from '@element-plus/icons-vue';
	import {Plus,} from '@icon-park/vue-next';
	import {ElMessage,ElDivider} from 'element-plus';
	import AdTree from '@/views/amazon/advertisement/manager/components/common/ad_tree.vue';
	import SPListCampaigns from'@/views/amazon/advertisement/manager/components/sp/listCampaigns.vue';
	import SPListAdgroups from'@/views/amazon/advertisement/manager/components/sp/listAdgroups.vue';
	import SPListProductAds from'@/views/amazon/advertisement/manager/components/sp/listProductAds.vue';
	import SPListPurchaseProduct from'@/views/amazon/advertisement/manager/components/sp/listPurchaseProductAds.vue';
	import SPListKeywords from'@/views/amazon/advertisement/manager/components/sp/listKeywords.vue';
	import SPListKeywordsQuery from'@/views/amazon/advertisement/manager/components/sp/listKeywordsQuery.vue';
	import SPListNegativaKeywords from'@/views/amazon/advertisement/manager/components/sp/listNegativaKeywords.vue';
	import SPListCampNegativaKeywords from'@/views/amazon/advertisement/manager/components/sp/listCampNegativaKeywords.vue';
	import SPListCampNegativaTarget from'@/views/amazon/advertisement/manager/components/sp/listCampNegativaTarget.vue';
	import SPListTarget from'@/views/amazon/advertisement/manager/components/sp/listTarget.vue';
	import SPListTargetQuery from'@/views/amazon/advertisement/manager/components/sp/listTargetQuery.vue';
	import SPListNegativaTarget from'@/views/amazon/advertisement/manager/components/sp/listNegativaTarget.vue';
	
	import SBListCampaigns from'@/views/amazon/advertisement/manager/components/hsa/listCampaigns.vue';
	import SBListAdgroups from'@/views/amazon/advertisement/manager/components/hsa/listAdgroups.vue';
	import SBListProductAds from'@/views/amazon/advertisement/manager/components/hsa/listProductAds.vue';
	import SBListPurchaseProduct from'@/views/amazon/advertisement/manager/components/hsa/listPurchaseProductAds.vue';
	import SBListKeywords from'@/views/amazon/advertisement/manager/components/hsa/listKeywords.vue';
	import SBListKeywordsQuery from'@/views/amazon/advertisement/manager/components/hsa/listKeywordsQuery.vue';
	import SBListNegativaKeywords from'@/views/amazon/advertisement/manager/components/hsa/listNegativaKeywords.vue';
	import SBListTarget from'@/views/amazon/advertisement/manager/components/hsa/listTarget.vue';
	import SBListTargetQuery from'@/views/amazon/advertisement/manager/components/hsa/listTargetQuery.vue';
	import SBListNegativaTarget from'@/views/amazon/advertisement/manager/components/hsa/listNegativaTarget.vue';
	
	import SDListCampaigns from'@/views/amazon/advertisement/manager/components/sd/listCampaigns.vue';
	import SDListAdgroups from'@/views/amazon/advertisement/manager/components/sd/listAdgroups.vue';
	import SDListProductAds from'@/views/amazon/advertisement/manager/components/sd/listProductAds.vue';
	import SDListTarget from'@/views/amazon/advertisement/manager/components/sd/listTarget.vue';
	import SDListNegativaTarget from'@/views/amazon/advertisement/manager/components/sd/listNegativaTarget.vue';
	import SDListPurchaseProduct from'@/views/amazon/advertisement/manager/components/sd/listPurchaseProductAds.vue';
	
	import filtericon from "@/components/icon/filtericon.vue";
	import {useRouter } from 'vue-router';
	import advertApi from '@/api/amazon/advertisement/report/advertApi.js';
	import advCampaignApi from '@/api/amazon/advertisement/report/advCampaignApi.js';
	import advProductsApi from '@/api/amazon/advertisement/report/advProductsApi.js';
	import { useStore } from 'vuex';
	const store = useStore();
	const router = useRouter();
	
    const spListCampaignsRef=ref();
	const spListAdgroupsRef=ref();
    const spListProductAdsRef=ref();
    const spListKeywordsRef=ref();
    const spListKeywordsQueryRef=ref();
    const spListNegativaKeywordsRef=ref();
	const spListCampNegativaKeywordsRef=ref();
    const spListTargetRef=ref();
    const spListTargetQueryRef=ref();
    const spListNegativaTargetRef=ref();
    const spListCampNegativaTargetRef=ref();
	const spListPurchaseProductRef=ref();
	
	const sbListCampaignsRef=ref();
	const sbListAdgroupsRef=ref();
	const sbListProductAdsRef=ref();
	const sbListKeywordsRef=ref(); 
	const sbListKeywordsQueryRef=ref();
	const sbListNegativaKeywordsRef=ref();
	const sbListTargetRef=ref();
	const sbListTargetQueryRef=ref();
	const sbListNegativaTargetRef=ref();
	const sbListPurchaseProductRef=ref();
	
	const sdListCampaignsRef=ref();
	const sdListAdgroupsRef=ref();
	const sdListProductAdsRef=ref();
	const sdListTargetRef=ref();
	const sdListNegativaTargetRef=ref();
	const sdListPurchaseProductRef=ref();
 
	const eltabsRef=ref();
 

	const state =reactive({
		nowDate:null,
		nowTime:null,
		nowWeek:null,
		portfoliostitle:"",
		advcamstitle:"",
		advgroupstitle:"",
		summaryData:{},
		tabsData:[],
		tabsDataValue:[
			{name:'广告活动',value:'adcams',count:''},
			{name:'广告组',value:'adgroups',count:''},
			{name:'商品',value:'ProductAds',count:''},
			{name:'关键词',value:'adkey',count:''},
			{name:'广告组-否定关键词',value:'adnkey'},
			{name:'活动-否定关键词',value:'adcampnkey'},
			{name:'用户搜索词',value:'adsearch'},
			{name:'商品投放',value:'adtarget'},
			{name:'广告组-否定投放',value:'adntarget'},
			{name:'活动-否定投放',value:'adcampntarget'},
			{name:'用户投放搜索',value:'adtargetquery'},
			{name:'商品购买',value:'purchaseProduct'},
		],
		activeName:'adcams',
		adTypeOptions:[
			{name:'自动投放',value:'auto',},
			{name:'手动投放',value:'manual',},
			{name:'手动及自动',value:'all',},
		],
		adType:'all',
		matchOptions:[
			{name:'广泛匹配',value:'broad',},
			{name:'词组匹配',value:'phrase',},
			{name:'精确匹配',value:'exact',},
			{name:'全部匹配类型',value:'all',},
		],

		disabled:true,
		queryParams:{search:'',changeRate:""},
		bindParams:{},
		selectRows:[],
		updateType:null,
		myDate:null,
		summaryType:null,
		marketname:null,
	})

	const {
		tabsData,
		activeName,
		adStatesOptions,
		adstate,
		adTypeOptions,
		adType,
		disabled,
		queryParams,
		portfoliostitle,
		advcamstitle,
		advgroupstitle,
		summaryData,
		bindParams,
		nowDate,
		nowTime,
		nowWeek,
		matchOptions,
		selectRows,
		updateType,
		marketname,
	}=toRefs(state)
	function getTabs(filterTabs){
		var list=[];
		state.tabsDataValue.forEach(item=>{
			if(filterTabs.includes(item.value)){
				if(item.value=="ProductAds"){
					if(state.queryParams.campaignType=="SB"){
						item.name="广告";
					}else{
						item.name="商品";
					}
				}
				list.push(item);
			}
		});
		return list;
	}
	function bindCampaign(params){
		var active="adgroups";
		if(state.bindParams.campaignid){
			state.advcamstitle=state.bindParams.camname;
			if(params.adgroupname){
				state.advgroupstitle=params.adgroupname;
			}
			state.queryParams.campaignid=params.campaignid;
			if(params.adgroupid){
				state.queryParams.adGroupid=params.adgroupid;
				if(state.queryParams.campaignType=="SB"){
					state.activeName="ProductAds";
					//state.activeName="adkey";
					nextTick(()=>{
						var filterTabs=["ProductAds","adkey","adnkey","adsearch","adtarget" ,"adntarget","adtargetquery","purchaseProduct" ];
						state.tabsData=getTabs(filterTabs);
					});
				}
				if(state.queryParams.campaignType=="SP"){
					state.activeName="ProductAds";
					nextTick(()=>{
						var filterTabs=["ProductAds","adkey","adnkey","adsearch" ,"adtarget","adntarget","adtargetquery","purchaseProduct" ];
						state.tabsData=getTabs(filterTabs); 
					});
				}
				if(state.queryParams.campaignType=="SD"){
					state.activeName="ProductAds";
					nextTick(()=>{
					var filterTabs=["ProductAds","adtarget","adntarget","adtargetquery","purchaseProduct" ];
					state.tabsData=getTabs(filterTabs); 
					});
				}
			}else{
				if(state.queryParams.campaignType=="SB"){
					state.queryParams.adGroupid=null;
					state.activeName="adgroups";
					nextTick(()=>{
					var filterTabs=["ProductAds","adkey","adnkey","adsearch","adtarget" ,"adntarget","adtargetquery","adgroups","purchaseProduct"  ];
					state.tabsData=getTabs(filterTabs); 
					});
				}
				if(state.queryParams.campaignType=="SP"){
						state.activeName="adgroups";
						state.queryParams.adGroupid=null;
						nextTick(()=>{
						var filterTabs=["ProductAds","adkey","adcampnkey","adsearch" ,"adtarget","adcampntarget","adtargetquery","adgroups","purchaseProduct"  ];
						state.tabsData=getTabs(filterTabs); 
					    });
				}
				if(state.queryParams.campaignType=="SD"){
						state.queryParams.adGroupid=null;
						state.activeName="adgroups";
						nextTick(()=>{
						var filterTabs=["ProductAds","adtarget","adntarget","adtargetquery","adgroups","purchaseProduct"  ];
						state.tabsData=getTabs(filterTabs); 
						 });
				}
			}
		
			nextTick(()=>{
				handleQuery();
			})
			return true;
		}else{
			state.queryParams.campaignid=null;
			state.queryParams.adGroupid=null;
		}
		return false;
	}
	function getBindData(params){
		state.advgroupstitle='';
		state.advcamstitle='';
		state.portfoliostitle='';
		state.queryParams.search='';
		state.bindParams=params;
		var hascamp=bindCampaign(params);
		if(state.bindParams.portfoliosid){
			state.portfoliostitle=state.bindParams.poloname;
			state.queryParams.portfolios=state.bindParams.portfoliosid;
		} 
		if(hascamp==false){
				state.activeName="adcams";
				nextTick(()=>{
					if(state.queryParams.campaignType=="SB"){
						  var filterTabs=["adcams","adgroups","ProductAds" ];
						  state.tabsData=getTabs(filterTabs);
						}
					 if(state.queryParams.campaignType=="SP"){
							var filterTabs=["adcams","adgroups" ,"ProductAds"];
							state.tabsData=getTabs(filterTabs);
						}
					 if(state.queryParams.campaignType=="SD"){
							var filterTabs=["adcams","adgroups" ,"ProductAds"];
							state.tabsData=getTabs(filterTabs);
					   }
				   })
				nextTick(()=>{
					handleQuery();
				})
			}
	}
	function handleTitle(type){
		if(type=="campaigns"){
			state.bindParams.adgroupid=null;
			state.bindParams.adgroupname=null;
			getBindData(state.bindParams);
		}else{
			state.bindParams.adgroupid=null;
			state.bindParams.adgroupname=null;
			state.bindParams.camname=null;
			state.bindParams.campaignid=null;
			getBindData(state.bindParams);
		}
	}
	function handleBindData(param){
		if(param.campaignid){
			state.bindParams.camname=param.camname;
			state.bindParams.campaignid=param.campaignid;
		}else{
			state.bindParams.camname=null;
			state.bindParams.campaignid=null;
		}
		if(param.adgroupid){
			state.bindParams.adgroupid=param.adgroupid;
			state.bindParams.adgroupname=param.adgroupname;
		}else{
			state.bindParams.adgroupid=null;
			state.bindParams.adgroupname=null;
		}
	    getBindData(state.bindParams);
	}
 
	function handleClick(val){
		 if(state.advcamstitle=='无' && val!='adcams' && val!='adgroups' && val!='ProductAds'){
		 	ElMessage.error("请先锁定某个广告活动或广告组");
			return;
		 }else{
			 handleQuery();
		 }
		 if(isTime.value){
			 //日期发生变化，点击该页签时，发送请求
			store.commit("dateStore/setActiveDate",!store.state.dateStore.isActiveDate);
			//接收页签类型
			store.commit("dateStore/setTabName",val);
			isTime.value =false;
		 }
	}
	const isTime=ref(false);
    watch([
		()=>store.state.dateStore.dateValue,
		()=>store.state.dateStore.dateType,
		()=>store.state.dateStore.week,
	],()=>{
		isTime.value = true
	})
	function getData(params){
		state.queryParams.groupid=params.groupid;
		state.queryParams.profileid=params.profileid;
		state.queryParams.campaignType=params.campaignType;
		state.myDate=new Date(params.ftime);
		state.marketname=params.marketname;
		nextTick(()=>{
	       getBindData(params);
		})
	}
	
	function selectRow(seletions){
		if(seletions.length>0){
			state.disabled = false;
			state.selectRows=seletions;
		}else{
			state.disabled = true;
		}
	}
	function getSummary(data){
		state.summaryData=data;
	}
	
	
	function handleBidding(name){
		state.batch.input = true;
		state.batch.text = name;
		if(name==""){
			
		}
	}
	function handleUpdateStatus(value){
		state.ftype="status";
		
	}
	
	function changeCategory(val){
		state.queryParams.categoryid=val;
	}
	//日期改变

	function handleQuery(){
		state.queryParams.ftype=state.activeName;
		var activeName=state.activeName;
		if(state.queryParams.profileid){
			 nextTick(()=>{
				 if(state.queryParams.campaignType=="SP"){
					 if(activeName=='adcams'){
					 	spListCampaignsRef.value.show(state.queryParams);					 
					 }
					 if(activeName=='adgroups'){
					 	spListAdgroupsRef.value.show(state.queryParams);					 
					 }
					 if(activeName=='ProductAds'){
					 	spListProductAdsRef.value.show(state.queryParams);					 
					 }
					 if(activeName=='adkey'){
					 	spListKeywordsRef.value.show(state.queryParams);					 
					 }
					 if(activeName=='adsearch'){
					 	spListKeywordsQueryRef.value.show(state.queryParams);					 
					 }
					 if(activeName=='adnkey'){
					 	spListNegativaKeywordsRef.value.show(state.queryParams);					 
					 }
					 if(activeName=="adcampnkey"){
						 spListCampNegativaKeywordsRef.value.show(state.queryParams);		
					 }
					 if(activeName=='adtarget'){
					 	spListTargetRef.value.show(state.queryParams);					 
					 }
					 if(activeName=='adtargetquery'){
					 	spListTargetQueryRef.value.show(state.queryParams);					 
					 }
					 if(activeName=='purchaseProduct'){
					 	spListPurchaseProductRef.value.show(state.queryParams);					 
					 }
					 if(activeName=='adntarget'){
					 	spListNegativaTargetRef.value.show(state.queryParams);					 
					 }
					 if(activeName=='adcampntarget'){
						 spListCampNegativaTargetRef.value.show(state.queryParams);		
					 }
				 }
				 if(state.queryParams.campaignType=="SB"){
					 if(activeName=='adcams'){
					 	sbListCampaignsRef.value.show(state.queryParams);					 
					 }
					 if(activeName=='adgroups'){
					 	sbListAdgroupsRef.value.show(state.queryParams);					 
					 }
					 if(activeName=='ProductAds'){
					 	sbListProductAdsRef.value.show(state.queryParams);				 
					 }
					 if(activeName=='adkey'){
					 	sbListKeywordsRef.value.show(state.queryParams);					 
					 }
					 if(activeName=='adsearch'){
					 	sbListKeywordsQueryRef.value.show(state.queryParams);					 
					 }
					 if(activeName=='adnkey'){
					 	sbListNegativaKeywordsRef.value.show(state.queryParams);					 
					 }
					 if(activeName=='adtarget'){
					 	sbListTargetRef.value.show(state.queryParams);					 
					 }
					 if(activeName=='adtargetquery'){
					 	sbListTargetQueryRef.value.show(state.queryParams);					 
					 }
					 if(activeName=='purchaseProduct'){
					 	sbListPurchaseProductRef.value.show(state.queryParams);					 
					 }
					 if(activeName=='adntarget'){
					 	sbListNegativaTargetRef.value.show(state.queryParams);					 
					 }
				 }
				 if(state.queryParams.campaignType=="SD"){
					 if(activeName=='adcams'){
					 	sdListCampaignsRef.value.show(state.queryParams);				 
					 }
					 if(activeName=='adgroups'){
					 	sdListAdgroupsRef.value.show(state.queryParams);			 
					 }
					 if(activeName=='ProductAds'){
					 	sdListProductAdsRef.value.show(state.queryParams);					 
					 }
					 if(activeName=='adtarget'){
					 	sdListTargetRef.value.show(state.queryParams);					 
					 }
					 if(activeName=='purchaseProduct'){
					 	sdListPurchaseProductRef.value.show(state.queryParams);					 
					 }
					 if(activeName=='adntarget'){
					 	sdListNegativaTargetRef.value.show(state.queryParams);					 
					 }
				 }
				 loadSummaryCount();
			 })
		}
	}
	function formateDate(myDate) {
		    if(myDate){
				let yy = myDate.getFullYear();
				let mm = myDate.getMonth() + 1;
				let dd = myDate.getDate();
				let week = myDate.getDay();
				let hh = myDate.getHours();
				let ms =
					myDate.getSeconds() < 10 ?
					"0" + myDate.getSeconds() :
					myDate.getSeconds();
				let mf =
					myDate.getMinutes() < 10 ?
					"0" + myDate.getMinutes() :
					myDate.getMinutes();
				if (week == 1) {
					state.nowWeek = "周一";
				} else if (week == 2) {
					state.nowWeek = "周二";
				} else if (week == 3) {
					state.nowWeek = "周三";
				} else if (week == 4) {
					state.nowWeek = "周四";
				} else if (week == 5) {
					state.nowWeek = "周五";
				} else if (week == 6) {
					state.nowWeek = "周六";
				} else {
					state.nowWeek = "周日";
				}
				// nowTimeref.value = hh + ":" + mf + ":" + ms;
				//ms是秒，这里可以根据自己需要调整格式
				state.nowTime = hh + ":" + mf+ ":" + ms;
				// nowDateref.value = yy + "年" + mm + "月" + dd + "日";
				state.nowDate = yy + "年" + mm + "月" + dd+ "日";
			}
			
	}
 
	function loadSummaryCount(){
			var params=JSON.parse(JSON.stringify(state.queryParams));
			if(params.adGroupid){
				params.ftype='ProductAds';
			}else if(params.campaignid){
				params.ftype='adgroups';
			}else  {
				params.ftype='adcams';
			}
			if(state.activeName!="purchaseProduct"){
				advertApi.getallsumtype(params).then((res)=>{
					if(res.data){
						state.summaryType=res.data;
					}
					state.tabsDataValue.forEach(item=>{
						if(item.value=="adcams")item.count=state.summaryType.adcams;
						if(item.value=="adgroups")item.count=state.summaryType.adgroups;
						if(item.value=="ProductAds")item.count=state.summaryType.products;
						if(item.value=="adkey")item.count=state.summaryType.keywords;
						if(item.value=="adnkey")item.count=state.summaryType.negativeKeyword;
						if(item.value=="adtarget")item.count=state.summaryType.targets;
						if(item.value=="adsearch")item.count=state.summaryType.keyquery;
						if(item.value=="adtargetquery")item.count=state.summaryType.targetsquery;
						if(item.value=="adntarget")item.count=state.summaryType.adntarget;
					})
				});
			}
			
	}
	function clearBtn(){
		state.portfoliostitle='';
		state.advgroupstitle='';
		state.advcamstitle='';
		state.queryParams.campaignid=null;
		state.queryParams.adGroupid=null;
		state.queryParams.portfolios=null;
		var list=[];
		state.tabsDataValue.forEach(item=>{
			if(item.value=="adcams"||item.value=="adgroups"||item.value=="ProductAds"){
				list.push(item);
			}
		});
		state.tabsData=list;
		state.activeName="adcams";
		handleQuery();
	}
	function handleTime(){
		if(state.myDate!=null){
			 state.myDate=new Date(state.myDate.setSeconds(state.myDate.getSeconds() +1));
		}
		formateDate( state.myDate) 
	}
	onMounted(()=>{
		//加载汇总数值
		setInterval(handleTime, 1000);
	});
</script>

<style scoped>
	.con-body{
		padding-bottom:20px;
	}
	.el-tabs__item{
		color:#999
	}
	.titleItem{
		max-width:400px;
		white-space:nowrap;
		text-overflow:ellipsis;
		overflow:hidden;
	}
	.m-r-16{
		margin-right:16px;
	}
	.ad-body-c{
		padding:0px 16px;
	}
	.wi-content{
		display:flex;
		margin:16px;
		margin-left:0px;
	}
	.bread-head{
		border-bottom:1px solid #eee;
	}
	.bread-nav{
		display:flex;
		align-items: center;
	}
	.ad-home{
		    display: flex;
		    height: 48px;
		    width: 48px;
		    color:#999;
			border-right:1px solid #eee;
			cursor:pointer;
			margin-right:16px;
	}
	.ad-home:hover{
		background-color: #f5f5f5;
	}
	.wi-left-c{
	}
	.wi-right-c{
		flex-grow:1;
		box-shadow: 0 2px 6px 0 rgba(0, 0, 0, 0.1);
		margin-left:16px;
		overflow:hidden;
	}
	.date-picker-width{
		width:268px!important;
	}
</style>