<template>
	<div class='pag-radius-bor '>
	 <el-tabs v-model="activeName"  @tab-change="handleTabChange">
	    <el-tab-pane label="广告数据分析" name="chart">
			<div class="con-header">
			<el-row class="flex-center-between " >
					  <el-space>
					  <Datepicker ref="datepickersRef" :days="1" class="date-picker-width"   @changedate="changedate" />
					  <AdGroup  :border="true"   @change="changeGroup" :isAll="true"/>
					 </el-space>
					<el-radio-group v-model="queryParams.currency"  @change="handleQuery">
					     <el-radio-button label="CNY" />
					     <el-radio-button label="USD" />
					   </el-radio-group>
			</el-row>
			<el-row :gutter="16">
				   <el-col :span="5" class="data-item" :class="{'active':item.active}"  v-for="item in adList">
					   <el-card shadow="hover"  @click="handleCardChange(item)" >
						  <div class='flex-center-between'>
							  <div>
								  <p class="text-gray">{{item.name}}</p>
								  <span class="font-large font-bold">
									  <span>{{item.prefix}}</span>
									  <span v-if="item.format=='float'">{{formatFloat(summaryData[item.field])}}</span>
									  <span v-else> {{summaryData[item.field]}}</span>
									  <span>{{item.suffix}}</span>
								  </span>
							  </div>
							<!--  <div class="font-extraSmall text-right">
								  <p v-if="item.rate" :class="item.rate>0?'text-green':'text-red'">{{item.rate}}%</p>
								  <p v-else>-</p>
								<span> 较{{item.date}}</span>
							  </div> -->
						  </div>
					   </el-card>
				   </el-col>
			</el-row>
			<el-row>
				  <div id="mychart" style='height:320px;width:100%'>
					  
				  </div>
			</el-row>
			</div>
		</el-tab-pane>
	    <el-tab-pane label="广告报表统计"  name="table">
				<div class="con-header">
					  <el-row>
						  <el-col :span="24">
						<div class='flex-center-between'>
							<el-space>
						  <Datepicker  type="monthrange"  ref="datepickersRef" :days="1" class="date-picker-width"  @changedate="changedate"  />
						  <AdGroup  :border="true"      @change="changeGroup"  :isAll="true"/>
						  </el-space>
						  <el-radio-group class="el-radio-group-button"  v-model="queryParams.currency"  @change="handleQuery">
						    <el-radio-button label="CNY" />
						    <el-radio-button label="USD" />
						   </el-radio-group>
						  </div>	  
						  </el-col>
					  </el-row>
					  <table class="sd-table" cellpadding="0" cellspacing="0">
						  <thead>
							  <tr>
							    <th>项目</th>
								<th v-for="item in monthData?.impressions?.listLabel">{{item}}</th>
								<th >合计</th>
							  </tr>
						  </thead>
						  <tbody>
							  <tr v-for="(item,index) in rows">
								 <td>
									 <el-space>
									 <span class="circle" :class="'text-'+item.group"></span>{{item.name}}
									 </el-space>
									 </td> 
								 <td v-for="fd in monthData[item.field]?.listData">{{item.prefix}}
								 <span v-if="item.format=='float'">
									{{formatFloat(parseFloat(fd))}} 
								 </span>
								 <span v-else>
									 {{fd}} 
								 </span>
								 {{item.suffix}}</td> 
								 <td class="font-bold">{{item.prefix}}
								 <span v-if="item.format=='float'">
								 	 {{formatFloat(parseFloat(monthData["sumAll"+item.field]))}} 
								 </span>
								 <span v-else>
								 	  {{monthData["sumAll"+item.field]}} 
								 </span>
								 {{item.suffix}}
								 </td> 
							  </tr>
						  </tbody>
					  </table>
			</div>
		</el-tab-pane>
	  </el-tabs>
</div>
</template>

<script setup>
	import{ref,reactive,toRefs,onMounted}from'vue'
	import Datepicker from '@/components/header/datepicker.vue';
	import '@/assets/css/packbox_table.css'
	import AdGroup from '@/components/header/ad_group.vue';
	import {formatFloat,dateFormat,dateTimesFormat,outputmoney} from '@/utils/index.js';
	import summaryApi from '@/api/amazon/advertisement/summary/summaryApi.js';
	import * as echarts from 'echarts';
	const state = reactive({
		activeName:'chart',
		queryParams:{fromDate:"",endDate:"",groupid:"",profileid:"",search:"","currency":"USD"},
		adList:[  {"field":"ordersummary",name:"销售额",color:"#FF6700",
			            clasz:"allsale",prefix:"$",suffix:"",format:"float"},
				  {"field":"attributedSales",name:"广告销售额",color:"#409eff",
					clasz:"attributedSales",active:true,
					prefix:"$",suffix:"",format:"float"},
				  {"field":"cost",name:"广告花费",active:true,color:"#ffa400",
					clasz:"cost ",prefix:"$",suffix:"",format:"float"},
				  {"field":"impressions",name:"曝光量",color:"#00aa7f",
					clasz:"impressions ",prefix:"",suffix:"",format:""},
				  {"field":"clicks",name:"点击数",color:"#aa5e3a",
					clasz:"clicks ",prefix:"",suffix:"",format:""},
				  {"field":"attributedUnitsOrdered",name:"订单数",color:"#0055ff",
					clasz:"order ",prefix:"",suffix:"",format:""},
				  {"field":"ctr",name:"点击率",color:"#a45faa",
					 clasz:"ctr",prefix:"",suffix:"%",format:"float"},
				   {"field":"cr",name:"转化率",color:"#36db74",
					clasz:"cr ",prefix:"",suffix:"%",format:"float"},
				   {"field":"acos",name:"ACOS",active:true,color:"#10B3FF",
					clasz:"acos",prefix:"",suffix:"%",format:"float"},
				   {"field":"acosas",name:"ACOAS",
					clasz:"data-right",prefix:"",suffix:"%",format:"float"},
				   {"field":"cpc",name:"CPC",
					clasz:"data-right",prefix:"",suffix:"",format:"float"},
				   {"field":"roi",name:"ROAS",
					clasz:"data-right",prefix:"",format:"float"},
		],
		dataSort:['曝光量','点击率'],
		rows:[
			{name:'总销售额',group:"yellow","prefix":"$",suffix:"",field:"allsale",format:"float"},
			{name:'有广告的ASIN数量' ,group:"yellow","prefix":"",suffix:"",field:"asinNum"},
			{name:'有广告的ASIN数量占比',group:"yellow","prefix":"",suffix:"%",field:"asinPer"},
			{name:'广告组数量',group:"blue","prefix":"",suffix:"",field:"adgroupNum"},
			{name:'广告贡献的销售额',group:"blue","prefix":"$",suffix:"",field:"attributedSales",format:"float"},
			{name:'广告贡献的销售额占比',group:"blue","prefix":"",suffix:"%",field:"salespall"},
			{name:'点击/展示比率(CTR)',group:"blue","prefix":"",suffix:"%",field:"ctr",format:"float"},
			{name:'广告曝光量',group:"green","prefix":"",suffix:"",field:"impressions"},
			{name:'广告点击量',group:"green","prefix":"",suffix:"",field:"clicks"},
			{name:'广告贡献的订单量',group:"green","prefix":"",suffix:"",field:"order"},
			{name:'广告转化率(CR)',group:"green","prefix":"",suffix:"",field:"cr",format:"float"},
			{name:'广告花费',group:"green","prefix":"$",suffix:"",field:"cost"},
			{name:'单次点击费用(CPC)',group:"green","prefix":"$",suffix:"",field:"cpc"},
			{name:'广告花费/总销售额比率(ACoAS)',group:"green","prefix":"",suffix:"%",field:"acoas"},
			{name:'广告投入产出比(ACoS)',group:"green","prefix":"",suffix:"%",field:"acos"},
		],
		monthData:{},
		summaryData:{},
	})
	const{	
		activeName,
		queryParams,
		adList,
		dataSort,
		rows,
		monthData,
		summaryData
	}=toRefs(state)
	 
	function handleTabChange(){
			   handleQuery();
	}
	function changeGroup(data){
			state.queryParams.groupid=data.groupid;
			state.queryParams.profileid=data.profileid;
			state.queryParams.marketplaceid=data.marketplaceid;
			state.queryParams.profile=data.profile;
			handleQuery();
	}
	function handleCardChange(row){
				var mytype =  row.field;
				if(row.active){
					var activenum=0;
					state.adList.forEach(item=>{
						if(item.active){
							activenum=activenum+1;
						}
					})
					if(activenum==1){
						ElMessage.warning("必选中一个指标");
					}else{
	    				row.active=false;
					}
				} else{
					if (mytype == "cr") {
						state.adList.forEach(item=>{
							 if (item.field == "attributedUnitsOrdered") {
								 item.active=true;
							 } else if(item.field == "clicks"){
								 item.active=true;
							 }else if(item.field=="cr"){
								 item.active=true;
							 }else {
								 item.active=false;
							 }
						 });
					}else if( mytype == "ctr" ){
						state.adList.forEach(item=>{
							 if (item.field == "clicks") {
								 item.active=true;
							 } else if(item.field == "impressions"){
								 item.active=true;
							 }else if(item.field=="ctr"){
								 item.active=true;
							 }else {
								 item.active=false;
							 }
						 });
					}else if(mytype == "acos"){
						state.adList.forEach(item=>{
							 if (item.field == "cost") {
								 item.active=true;
							 } else if(item.field == "attributedSales"){
								 item.active=true;
							 } else if(item.field=="acos"){
								 item.active=true;
							 }else {
								 item.active=false;
							 }
						 });
					} else {
						if(row.clasz!="data-right"){
					       row.active=true;
						}else{
							return; 
						}
					}
				} 
				refreshChart();
	}
	//日期改变
	function changedate(dates,value,opt){
		state.queryParams.begin=dates.start;
		state.queryParams.end=dates.end;
		if(opt!="load"){
			handleQuery();
		}
	}
	function handleQuery(){
		if(state.activeName=="chart"){
			loadSummaryChartData();
		}else{
			loadMonthSummaryData() ;
		}
	}
	function loadSummaryChartData(){
		summaryApi.getsumproduct(state.queryParams).then(res=>{
			state.summaryData=res.data.summary;
			state.chartData=res.data.chartdata;
			var data=res.data.summary;
			data.ordersummary=res.data.ordersummary;
			state.summaryData.acosas = isNanPvalue(parseFloat(data.cost), data.ordersummary);
			state.summaryData.cpc = isNanvalue(parseFloat(data.cost),parseFloat( data.clicks));
			state.summaryData.roi = isNanvalue(parseFloat(data.attributedSales), parseFloat(data.cost));
			if(state.queryParams.currency=="USD"){
				state.adList.forEach(item=>{
					if(item.prefix=='￥'){
						item.prefix='$';
					} 
				})
			}else{
				state.adList.forEach(item=>{
					if(item.prefix=='$'){
						item.prefix='￥';
					} 
				})
			}
			
			refreshChart();
		})
	}
	function isNanPvalue(datadivisor, datadividend) {
		if (datadividend == 0) {
			return 0;
		} else {
			return formatFloat(datadivisor * 100.0 / datadividend);
		}
	}
	
	function isNanvalue(datadivisor, datadividend) {
		if (datadividend == 0) {
			return 0;
		} else {
			return formatFloat(datadivisor * 1.0 / datadividend);
		}
	}
	function loadMonthSummaryData() {
		summaryApi.getmonthsum(state.queryParams).then(res=>{ 
			state.monthData=res.data;
			if(state.queryParams.currency=="USD"){
				state.rows.forEach(item=>{
					if(item.prefix=='￥'){
						item.prefix='$';
					} 
				})
			}else{
				state.rows.forEach(item=>{
					if(item.prefix=='$'){
						item.prefix='￥';
					} 
				})
			}
		});
	}
	
	function refreshChart() {
		var chartdata=state.chartData;
		if (chartdata != null) {
			var labels = null;
			var color = [];
			var legends = [];
			var series = [];
			state.adList.forEach(row=>{
				var type = row.field;
			
				if (row.active&&chartdata[type]) {
					labels = chartdata[type]["listLabel"];
					legends.push(row.name);
					color.push(row.color);
					var datas = {};
					datas.name = row.name;
					if (type == "cr" || type == "ctr" || type == "acos") {
						datas.type = "line";
						datas.yAxisIndex = 1;
						datas.symbol = 'emptycircle';// 拐点样式
						datas.smooth = true;
						datas.symbolSize = 3;
						datas.itemStyle = {
							normal : {
								lineStyle : {
									width : 2
								}
							}
						}
					} else {
						datas.type = "bar";
						datas.barGap = "0%";
						datas.boundaryGap = "0%";
						datas.barMaxWidth = 32, datas.itemStyle = {
							normal : {
								barBorderRadius : [ 4, 4, 0, 0 ]
							}
						}
	
					}
					datas.data = chartdata[type].listData;
					series.push(datas);
				}
			});
			if (labels != null) {
				lineChart(legends, labels, series, color);
			} else {
				document.getElementById("mychart").innerHTML="<div style='padding-top:10%' clas='font-extraSmall'>暂无数据</div>";
			}
		} else {
				document.getElementById("mychart").innerHTML="<div style='padding-top:10%' clas='font-extraSmall'>暂无数据</div>";
		}
	}
	var myChart=null;
	function lineChart(legends, labels, series, color) {
		if (myChart != null && myChart != "" && myChart != undefined) {
			myChart.dispose();//销毁
	    }
		myChart = echarts.init(document.getElementById("mychart"));
		// 指定图表的配置项和数据
		var option = {
			tooltip : {
				trigger : 'axis',
				axisPointer : { // 坐标轴指示器，坐标轴触发有效
					type : 'line', // 默认为直线，可选为：'line' | 'shadow'
					lineStyle : { // 直线指示器样式设置
						color : '#ccc',
						width : 1,
						type : 'solid'
					},
				}
			},
			
			legend : {
				data : legends,
				itemGap : 24,
				icon: "circle",
				itemWidth:6,
				itemHeight:6,
			},
			color : color,
			grid:{
				left:'0%',
				right:'2%',
				containLabel: true,
			},
			calculable : false,
			xAxis : [ {
				axisLabel : {
					show : true,
					color:"#acb0b9",
					textStyle : {
						color : '#999'
					}
				},
				 axisTick: {
				  show: true
				},
				splitLine : {
					lineStyle : {
						color : '#f1f1f1',
						width : 1,// 这里是为了突出显示加上的
					}
				},
				axisLine : {
					show: false,
					lineStyle : {
						color : '#f1f1f1',
						width : 1,// 这里是为了突出显示加上的
					}
				},
				boundaryGap : true,
				data : labels
			} ],
			yAxis : [ {
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
			}, {
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
			}, ],
			series : series
		};
		// 使用刚指定的配置项和数据显示图表。
		myChart.setOption(option);
		window.addEventListener('resize',()=>{
		myChart.resize();
		})
	}
</script>

<style>
	.data-item .el-card__body{
		background-color:#f5f5f5;
		padding:16px;
	}
	.dark .data-item .el-card__body{
		background-color:var(--el-bg-color-overlay);
		padding:16px;
	}
	
	.data-item.active .el-card__body{
		background-color:#fff5ee;
	}
	
	.dark .data-item.active .el-card__body{
		background-color:#817c78;
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
		font-size:14px;
	}
    .circle{
		width:6px;
		height:6px;
		border:2px solid;
		border-radius: 4px;
	}
</style>