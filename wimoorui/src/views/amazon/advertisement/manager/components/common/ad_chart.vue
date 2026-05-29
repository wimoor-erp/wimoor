<template>
	<el-dialog
	title="广告图表"
	v-model="visible"
	width="80%"
	>
   <el-row :gutter="16">
	   <el-col :span="5" class="data-item" :class="{'active':item.check}"  v-for="item in adList">
		   <el-card shadow="hover" @click.stop="getSelectData(item)">
			  <div class='flex-center-between'>
				  <div>
					  <p class="text-gray">{{item.name}}</p>
					  <span class="font-large font-bold">{{item.value}}</span>
				  </div>
				  <div class="font-extraSmall text-right">
					  <p v-if="item.rate" :class="item.rate>0?'text-green':'text-red'">{{formatFloat(item.rate)}}%</p>
					  <p v-else>-</p>
					<span> 较{{item.date}}</span>
				  </div>
			  </div>
		   </el-card>
	   </el-col>
  </el-row>
  <el-row>
	   <el-radio-group v-model="radio" @change="loadSelect">
	        <el-radio-button label="日" />
	        <el-radio-button label="周" />
	        <el-radio-button label="月" />
	      </el-radio-group>
	  <div id="adchart" style='height:320px;width:100%'>
		  
	  </div>
  </el-row>
	<template #footer>
		<el-button @click="visible=false">关闭</el-button>
	</template>
	</el-dialog>
</template>

<script setup>
	import {ref,reactive,toRefs,nextTick} from"vue"
	import * as echarts from 'echarts';
	import advertApi from '@/api/amazon/advertisement/report/advertApi.js';
	import advCampaignApi from '@/api/amazon/advertisement/report/advCampaignApi.js';
	import advAdgroupApi from '@/api/amazon/advertisement/report/advAdgroupApi.js';
	import advProductsApi from '@/api/amazon/advertisement/report/advProductsApi.js';
	import advKeywordsApi from '@/api/amazon/advertisement/report/advKeywordsApi.js';
	import advTargetApi from '@/api/amazon/advertisement/report/advTargetApi.js';
	import {dateFormat,formatFloat,getValue,formatPercent} from '@/utils/index.js';
	let props = defineProps({summary:{}});
	const { summary} = toRefs(props);
	const state = reactive({
		radio:'日',
		visible:false,
		adList:[
			{name:'曝光量',value:'--',rate:'+1.0',date:'昨日',field:'impressions',ftype:'bar',listdata:[]},
			{name:'花费',value:'--',rate:'',date:'昨日',field:'cost',ftype:'bar',listdata:[]},
			{name:'销量',value:'--',rate:'-0.2',date:'昨日',field:'sumUnits',ftype:'bar',listdata:[]},
			{name:'点击量',value:'--',rate:'-6.5',date:'昨日',field:'clicks',ftype:'bar',listdata:[]},
			{name:'点击率',value:'--',rate:'',date:'昨日',field:'CTR',ftype:'line',listdata:[]},
			{name:'ACOS',value:'--',rate:'',date:'昨日',field:'ACOS',ftype:'line',listdata:[]},
			{name:'ROAS',value:'--',rate:'',date:'昨日',field:'ROAS',ftype:'line',listdata:[]},
			{name:'转化率',value:'--',rate:'',date:'昨日',field:'CSRT',ftype:'line',listdata:[]},
			{name:'每次点击费用',value:'--',rate:'+2.3',date:'昨日',field:'avgcost',ftype:'line',listdata:[]},
		],
		queryParams:{},
		ftype:'',
		queryItem:{},
		labels:[],
		listdata1:[],
		listdata2:[],
		ftype1:"line",
		ftype2:"bar",
	})
	const{
		visible,
		adList,
		radio,
		queryParams,
		ftype,
		labels,
		listdata1,
		listdata2,
		ftype1,
		ftype2,
	}=toRefs(state)
	const loadSelect = function(){
		var bytime="Daily";
		//bytime: Daily Weekly Monthly
		if(state.radio=='日'){
			bytime="Daily";
		}else if(state.radio=='周'){
			bytime="Weekly";
		}else{
			bytime="Monthly";
		}
		state.queryParams.bytime=bytime;
		state.queryParams.searchlist=state.queryItem.field;
		loadData()
	}
	function handChartData(res){
		 state.labels=res.data.labels;
		 state.queryItem.listdata= res.data.listdata1;
		 adChart();
	}
	function loadData(){
		if(state.ftype=="adcams"){
			advCampaignApi.getCampaignChart(state.queryParams).then(handChartData);
		}
		if(state.ftype=="adgroups"){
			advAdgroupApi.getAdGroupChart(state.queryParams).then(handChartData);
		}
		if(state.ftype=="ProductAds"){
			advProductsApi.getProductAdChart(state.queryParams).then(handChartData);
		}
		if(state.ftype=="adkey"){
			advKeywordsApi.getKeywordChart(state.queryParams).then(handChartData);
		}
		if(state.ftype=="adtarget"){
			advTargetApi.getProductTargeChart(state.queryParams).then(handChartData);
		}
		if(state.ftype=="adsearch"){
			advKeywordsApi.getKeywordQueryChart(state.queryParams).then(handChartData);
		}
		if(state.ftype=="adtargetquery"){
			advTargetApi.getProductTargeQueryChart(state.queryParams).then(handChartData);
		}
		
	}
	function loadSummary(){
		 dataHandler(props.summary);
	}
	function dataHandler(summary){
		if(summary){
			state.adList[0].value=summary.impressions;
			state.adList[1].value=summary.cost;
			state.adList[2].value=summary.sumUnits;
			state.adList[3].value=summary.clicks;
			state.adList[4].value=summary.CTR;
			state.adList[5].value=summary.ACOS;
			state.adList[6].value=summary.ROAS;
			state.adList[7].value=summary.CSRT;
			state.adList[8].value=summary.avgcost;
			getSelectData(state.adList[0]);
		}
	}

	function getSelectData(item){
				 /* 不点击自己 */
		  if(item.check==true){
			    item.check = false; 
				item.listdata=[];
				adChart();
		  }else{
			   item.check = true;
			   state.queryItem=item;
			   loadSelect();
		  }
		 
	}
   function show(ftype,params){
	   state.queryParams=params;
	   state.ftype=ftype;
	   state.visible = true;
	   loadSummary();
	   nextTick(()=>{
	 	 adChart();
	   })
   }
   var myChart=null;
   function adChart(){
	   //var chartdata_bar=[];
	   if(myChart!=null){
	   		myChart.dispose();
	   }
	   var chartdata=[];
	   var legendname=[];
	   state.adList.forEach(item=>{
		   if(item.listdata&&item.listdata.length>0 && item.check==true){
			   if(item.ftype=="bar"){
				   var bar= {
				    name:item.name, 
				     data: item.listdata,
				     type: item.ftype
				   };
				   chartdata.push(bar);
			   }else{
				   var line= {
				     yAxisIndex: 1,
				     name:item.name, 
				     data: item.listdata,
				     type: item.ftype
				   };
				   chartdata.push(line);
			   }
			   
			   legendname.push(item.name);
		   }
	   })
	   if(!chartdata || chartdata.length==0){
		   return;
	   }
	  myChart = echarts.init(document.getElementById('adchart'));
	    var option = {
			grid:{
				left:'0%',
				right:'2%',
				containLabel: true,
			},
			  tooltip:{
			    trigger: 'axis'
			  },
		   legend: {
			   top:0,
		    data: legendname,
			icon: "circle",
			itemWidth:6,
			itemHeight:6,
		   },
	   	    xAxis: {
	   	       type: 'category',
	   	       data: state.labels,
			   axisLine:{
			   show: false
			   },
		       axisTick: {
		        show: true
		      },
			   axisLabel:{
			    color:"#acb0b9"
			   },
	   	     },
	   	     yAxis: [{
				    type: 'value',
					position: 'left',
				    axisLabel:{
					   color:"#acb0b9"
				   },
	   	       splitLine:{
	   	       	lineStyle:{
	   	       	color:"#F2F3F6"
	   	       	}
	   	       }
	   	     },
			 {
				  type: 'value',
			       position: 'right',
			       axisLabel:{
			        color:"#acb0b9"
			       },
			       splitLine:{
					    show: false,  
			       },
			       axisLabel: {
					   formatter: function (value) {
					       return formatFloat(value* 100) + '%';
					   }
			       }
			     },
			 ],
			  color:['#409eff','#FF6700','#00aa7f','#0055ff','#aa5e3a','#a45faa'],
	   	     series: chartdata,
	    }
	    myChart.setOption(option);
	    window.addEventListener('resize',()=>{
	    myChart.resize();
	    })
   }
   
   defineExpose({
   	show,
   })
</script>
<style>
	.data-item .el-card__body{
		background-color:#f5f5f5;
		padding:16px;
	}
	
	.data-item.active .el-card__body{
		background-color:#fff5ee;
	}
	.data-item.active .el-card{
		border:1px solid #ffac73;
		color:#ff6700;
	}
	.data-item.active .text-gray{
		color:#ff9853;
	}
</style>
<style scope>
	.data-item{
		    max-width:20%;
		    flex: 0 0 20%;
			margin-bottom:16px;
			cursor:pointer;
	}
	.text-gray{
		color:#777;
	}

</style>