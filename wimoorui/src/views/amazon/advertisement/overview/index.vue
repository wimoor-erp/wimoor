<template>
	<div class="el-con-16 con-header">
	<el-row :gutter="16" >
		 <el-col :span="12">
			   <div class='pag-radius-bor '>
				    <h4 >运行中活动</h4>
				  <div class="data-center m-t-extra">
					  <div class="ad-data-item">
						  <h5>{{typedata.campaigns}}</h5>
						  <p class="name">广告活动数</p>
					  </div>
					  <div class="ad-data-item">
						  <h5>{{typedata.adGroups}}</h5>
						  <p class="name">广组数</p>
					  </div>
					  <div class="ad-data-item">
						  <h5>{{typedata.ads}}</h5>
						  <p class="name">商品广告数</p>
					  </div>
					  <div class="ad-data-item">
						  <h5>{{typedata.targets}}</h5>
						  <p class="name">关键词数</p>
					  </div>
				  </div>
			   </div>
		 </el-col>
		 <el-col :span="12">
			   <div class='pag-radius-bor '>
				   <div class="flex-center-between">
				    <h4 >异常数据预警</h4>
					<el-space>
					  <el-radio-group v-model="wardatatype" size="small" @change="loadWaringData">
					       <el-radio-button label="productads"  >商品广告</el-radio-button>
					       <el-radio-button label="keywords" >关键词</el-radio-button>
					    </el-radio-group>
						<el-select size="small" v-model="waringType" @change="loadWaringData" style="width:120px;">
							<el-option
							 v-for="item in dataWaring"
							 :label="item.name" :value="item.value"></el-option>
						</el-select>
						<el-button @click="handleIndicatorShow" size="small">
						<el-icon><Tools /></el-icon>
						</el-button>
					</el-space>
					</div>
				  <div class="data-center el-text-primary" v-if="waringType=='co'">
					  <div @click="loadWaringDataDetail('impco')" class="ad-data-item pointer">
						  <h5 >{{waringData.impco}}</h5>
						  <p class="name">曝光量突降</p>
					  </div>
					  <div  @click="loadWaringDataDetail('clickco')" class="ad-data-item pointer">
						  <h5>{{waringData.clickco}}</h5>
						  <p class="name">点击量突降</p>
					  </div>
					  <div class="ad-data-item pointer">
						  <h5 @click="loadWaringDataDetail('crco')">{{waringData.crco}}</h5>
						  <p class="name">转化率突降</p>
					  </div>
					  <div @click="loadWaringDataDetail('acosco')" class="ad-data-item pointer">
						  <h5 >{{waringData.acosco}}</h5>
						  <p class="name">ACOS突增</p>
					  </div>
				  </div>
				  <div class="data-center el-text-primary" v-else-if="waringType=='sequent'">
				  					  <div @click="loadWaringDataDetail('impsequent')" class="ad-data-item pointer">
				  						  <h5 >{{waringData.impsequent}}</h5>
				  						  <p class="name">曝光量突降</p>
				  					  </div>
				  					  <div @click="loadWaringDataDetail('clicksequent')" class="ad-data-item pointer">
				  						  <h5 >{{waringData.clicksequent}}</h5>
				  						  <p class="name">点击量突降</p>
				  					  </div>
				  					  <div @click="loadWaringDataDetail('crsequent')" class="ad-data-item pointer">
				  						  <h5>{{waringData.crsequent}}</h5>
				  						  <p class="name">转化率突降</p>
				  					  </div>
				  					  <div @click="loadWaringDataDetail('acossequent')" class="ad-data-item pointer">
				  						  <h5>{{waringData.acossequent}}</h5>
				  						  <p class="name">ACOS突增</p>
				  					  </div>
				  </div>
				    <div class="data-center el-text-primary" v-else>
								  <div @click="loadWaringDataDetail('impyestoday')" class="ad-data-item pointer">
									  <h5 >{{waringData.impyestoday}}</h5>
									  <p class="name">曝光量突降</p>
								  </div>
								  <div @click="loadWaringDataDetail('clickyestoday')" class="ad-data-item pointer">
									  <h5>{{waringData.clickyestoday}}</h5>
									  <p class="name">点击量突降</p>
								  </div>
								  <div @click="loadWaringDataDetail('cryestoday')" class="ad-data-item pointer">
									  <h5>{{waringData.cryestoday}}</h5>
									  <p class="name">转化率突降</p>
								  </div>
								  <div @click="loadWaringDataDetail('acosyestoday')" class="ad-data-item pointer">
									  <h5>{{waringData.acosyestoday}}</h5>
									  <p class="name">ACOS突增</p>
								  </div>
				    </div>
				      
			   </div>
		 </el-col>
	</el-row>
	<el-row :gutter="16" >
		<el-col :span="12">
			<AdFunnel/>
		</el-col>
		<el-col :span="12">
			<RoasRank/>
		</el-col>
	</el-row>
	<el-row>
		<el-col :span="24">
			<AdStatistics/>
		</el-col>
	</el-row>
	</div>
	<IndicatorDetail ref="indicatorDetailRef"></IndicatorDetail>
	<Indicator ref="indicatorRef"></Indicator>
</template>
<script>
    export default{ name:"广告统计" };
</script>
<script setup>
	import{Tools}from '@element-plus/icons-vue'
	import{ref,reactive,toRefs,onMounted}from'vue'
	import AdFunnel from "./components/adFunnel.vue"
	import RoasRank from "./components/roasRank.vue"
	import AdStatistics from "./components/adStatistics.vue"
	import IndicatorDetail from "./components/indicator_detail.vue"
	import Indicator from "./components/indicator.vue"
	import summaryApi from '@/api/amazon/advertisement/summary/summaryApi.js';
	const indicatorDetailRef=ref();
	const indicatorRef=ref();
	const state = reactive({
		wardatatype:'productads',
		dataWaring:[
			{name:'昨日变动',value:'yesterday'},
			{name:'连续变动',value:'sequent'},
			{name:'同期变动',value:'co'},
		],
		waringData:{},
		waringType:'co',
		warningVisible:false,
		formWarning:{},
		typedata:{},
		
	})
	const{	
		wardatatype,
		waringType,
		dataWaring,
		typedata,
		warningVisible,
		formWarning,
		waringData,
	}=toRefs(state)
	onMounted(()=>{
		summaryApi.getenablesumtype().then(res=>{
			state.typedata=res.data;
		});
		loadWaringData();
	})
	function loadWaringData(){
		var param={ftype:state.waringType};
		if(state.wardatatype=="productads"){
			summaryApi.getProductWarningIndicator(param).then(res=>{
				state.waringData=res.data;
			});
		}else{
			summaryApi.getKeywordsWarningIndicator(param).then(res=>{
				state.waringData=res.data;
			});
		}
	}
	function loadWaringDataDetail(ftype){
		indicatorDetailRef.value.show(ftype,state.waringType,state.wardatatype);
	}
	function handleIndicatorShow(){
		indicatorRef.value.show(state.waringType,state.wardatatype);
	}
</script>

<style scoped>
	.el-text-primary{
		color:#ff6700;
	}
	.data-center{
		display:flex;
		align-items: center;
		justify-content:center;
	}
	.m-t-extra{
		margin-top:4px;
	}
	.ad-data-item {
		flex-grow:1;
		margin-top:24px;
		margin-bottom:16px;
	}
	.ad-data-item h5{
		font-size:20px;
		font-weight:700;
		font-family: DIN Alternate,Helvetica Neue,Helvetica,Arial,SF Pro Display;
		margin-bottom:8px;
	}
	.ad-data-item .name{
		font-size:14px;
		color:#999;
	}
	.el-con-16{
		padding:16px;
		padding-bottom:0px;
	}
</style>