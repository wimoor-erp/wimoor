<template>
	<el-dialog title="日均销量干预" v-model="dialog.visible" @close="handleClose" @opened = "handleOpen" :append-to-body="true" width="65%">
		<el-row :gutter="24">
			<el-col :span="10">
           <div class="flex-center-between">
			   <el-image class="product-img" :src="data.image"></el-image>
			   <div class="pro-message">
				   <div class="name">{{data.name}}</div>
				   <div class="sku">{{data.sku}}</div>
					   <el-space class="data-group" :size="32">
						   <div>{{data.summonth}}<p >月销 </p></div>
						   <div>{{data.sumseven}}<p >周销</p></div>
						   <div>{{data.sysavgsales}}<p >日均</p></div>
					   </el-space>
			   </div>
		   </div>		
			</el-col>
			<el-col :span="14">
				<div class="gary-bg  p-16">
					<el-tabs  tab-position="left" style="height: 200px" @tab-change="handleTabsChange" class="demo-tabs">
					    <el-tab-pane label="近期">
							<el-form label-width="120px"  >
											 <el-form-item   label="预计日均销量">
												 <div class="flex-center-between" style="flex: 1;">
													 <el-input   
													 ref="preinputRef" clearable 
													 autofocus  v-model="presale" 
													 @input="presale=CheckInputInt(presale)"
													 style="width: 120px;"></el-input>
												 </div>
											 </el-form-item>
											  <el-form-item>
												 <el-radio-group v-model="lastType" size="small" @change="handleChangeDateType">
												       <el-radio-button :label="30" >近1个月</el-radio-button>
												       <el-radio-button :label="60" >近2个月</el-radio-button>
												       <el-radio-button :label="90" >近3个月</el-radio-button>
												       <el-radio-button :label="180" >近半年</el-radio-button>
													   <el-radio-button :label="365" >近1年</el-radio-button>
												     </el-radio-group>
										 </el-form-item>
											 
											 <el-form-item>
											 	 <el-button type="primary" :loading="saveloading" @click="submitFormLast" >保存</el-button> 
											 </el-form-item>
							</el-form>
	
						</el-tab-pane>
					    <el-tab-pane label="区间">
							<el-form label-width="120px"  >
											 <el-form-item   label="预计日均销量">
												 <div class="flex-center-between" style="flex: 1;">
													 <el-input autofocus  
													   clearable v-model="presaleRange" 
													   	@input="presaleRange=CheckInputInt(presaleRange)"
													   style="width: 120px;"></el-input>
												 </div>
											 </el-form-item>
											 <el-form-item label="预估时间段">
											 		 <el-date-picker
											 		        v-model="dateRange"
											 		        type="daterange"
											 		        unlink-panels
											 		        range-separator="至"
											 		        start-placeholder="开始时间"
											 		        end-placeholder="结束时间"
															:disabled-date="disableDate"
											 		        :shortcuts="shortcuts"
											 		        :size="size"
											 		      />
											 <p class="font-extraSmall line-h-16">不选择时间段，则干预时间默认360天！</p>			  
											 </el-form-item>
											 <el-form-item >
												 
											 			<el-button :loading="saveloading" @click="submitFormRange" >添加时间段区间</el-button>
											 </el-form-item>
							</el-form>
						</el-tab-pane>
					    <el-tab-pane label="按月">
							 <el-space wrap>
							 <div v-for="item in listPresale">
								 <span v-if="item.month>=10">{{item.month}}</span>
								 <span v-else style="padding-left:8px;">{{item.month}}</span>月
								 <el-input type="number" autofocus 
								 v-model="item.amount" 
								@input="item.amount=CheckInputInt(item.amount)"
								 clearable style="width: 80px;"></el-input>
							 </div>
							   </el-space>     
							<el-button  type="info" style="margin-top:5px;margin-right:20px;float:right"  @click="handleClearData" >清空</el-button>
							 
						   <el-button type="primary" :loading="saveloading" @click="submitFormMonth" style="margin-top:5px;margin-right:30px;float:right">保存</el-button>
						
						</el-tab-pane>
					  </el-tabs>
			 
				 </div>
			</el-col>
		</el-row>
		<el-row class="m-t-16">
			<el-col :span="24">
				    <el-radio-group v-model="radiotype" @change="handleQuery" size="small">
				       <el-radio-button label="预估销量" />
				       <el-radio-button label="预估同期" />
					   <el-radio-button label="往年同期" />
				     </el-radio-group>
				<div v-if="radiotype=='预估销量'" :id="chartid"  style='height:160px;width:100%'>
				 
				</div>
				<div v-else-if="radiotype=='预估同期'" v-loading="calendarloading" >
					<div  :id="'calendar'+chartid" style='height:300px;width:100%'></div>
					
				</div>
				<div v-else v-loading="yearloading" >
					<div  :id="'yearChart'+chartid"   style='height:300px;width:100%'></div>
				</div>
			</el-col>
		</el-row>
	<template #footer>
		<el-button @click="handleClose">关闭</el-button>
		<el-button @click="handleDelete">清空干预</el-button>
	</template>	
	</el-dialog>
</template>

<script setup>
	import{ref,reactive,toRefs,onMounted,nextTick}from"vue"
	import {Close} from '@element-plus/icons-vue';
	import * as echarts from 'echarts';
	import {getSales,save,clear,getProductPreSalesByMonth,getProductPreSales} from "@/api/amazon/listing/preSalesApi.js";
	import salessumApi from "@/api/amazon/summary/salessumApi.js"
	import {CheckInputInt,dateFormat} from "@/utils/index.js";
	import { ElMessage, ElMessageBox } from 'element-plus'
	const preinputRef=ref()
	const emit = defineEmits(['confirm']);
	const state=reactive({
		  dialog:{visible:false},
		  lastType:90,
		  preEndDay:new Date().setDate(new Date().getDate()+90),
		  presale:"",
		  activeTab:0,
		  presaleRange:"",
		  chartid:"adjustchart",
		  radiotype:"预估销量",
		  chart:null,
		  yeartype:"year",
		  itemrow:{},
		  calendarloading:false,
		  yearloading:false,
		  dateRange:{},
		  saveloading:false,
		  clearloading:false,
		  listPresale:[],
		  parentrow:{},
		  data:{name:"",sku:"",image:""},
	})
	const {
	  formData,radiotype,chart,dialog,data,lastType,chartid,presale,listPresale,presaleRange,dateRange,saveloading,preEndDay,calendarloading,yeartype,
	  yearloading,clearloading,
	} = toRefs(state);
	var myChart =null ;
	function show(itemrow,parentrow){
		state.data.name=parentrow.name;
		state.data.sku=itemrow.sku;
		state.parentrow=parentrow;
		state.data.summonth=itemrow.summonth;
		state.data.sumseven=itemrow.sumseven;
		state.data.sysavgsales=itemrow.sysavgsales;
		state.data.image=parentrow.image;
		if(itemrow.shipday){
		   state.data.shipday=parseInt(itemrow.shipday);
		   state.data.avgsales=parseInt(itemrow.avgsales);
		}
		state.itemrow=itemrow;
		state.dialog.visible=true;
	}
	onMounted(()=>{
		var date=new Date();
		state.chartid="adjustchart"+date.getTime();
		for(var i=1;i<=12;i++){
			var month=date.format("yy-MM");
			state.listPresale.push({month:month,amount:"",index:i,date:date});
			date=date.clone();
			date=date.setMonth(date.getMonth() +1);
			date=new Date(date); 
			date=date.setDate(1);
			date=new Date(date); 
		}
		 
	})
	
	defineExpose({
	  show
	})
	 function handleOpen(){
		 
		 setTimeout(()=>{
		       preinputRef.value.focus();
			    handleQuery();
		 },500);
		
	 }
	 function handleClose(){
		 if(myChart!=null){
			 myChart.dispose();
		 }
		 state.dialog.visible=false;
	 }
	 function disableDate(date){
		 var value=new Date(date);
		  value=value.setDate(date.getDate()+1);
		  value=new Date(value);
		 if(value>new Date())return false;
		 return true;
	 }
	 function handleSave(preSaleList){
		 state.saveloading=true;
		 save(preSaleList).then(res=>{
			         state.saveloading=false;
		 			 ElMessage.success( "保存成功");
					 emit("confirm",state.parentrow);
		 			 handleQuery()
		 }).catch(e=>{
			  state.saveloading=false;
		 })
	 }
	 function handleChangeDateType(){
		var date=new Date();
		date=date.setDate(date.getDate()+state.lastType);
		date=new Date(date);
		state.preEndDay=date;
		saleChart(null);
	 }
	 function handleClearData(){
		 state.listPresale.forEach(item=>{
			 item.amount="";
		 });
	 }
	 function submitFormMonth(){
		 var preSaleList=[];
		 var monthdata={};
		 state.listPresale.forEach(item=>{
			 monthdata[item.index]=item;
		 });
		 for(var i=1;i<=12;i++){
		 			 var amount=monthdata[i].amount;
		 			 if(amount!=""){
		 				 var start= monthdata[i].date.clone();
		 				 var end = start.clone();
		 				 end=end.setMonth(end.getMonth() +1);
		 				 end=new Date(end); 
		 				 end=end.setDate(1);
		 				 end=new Date(end); 
						 var daynum=0;
		 				 while(start<end){
							  start=start.clone();
							  start=start.setDate(start.getDate()+1);
							  start=new Date(start);
		 					 daynum++;	  
		 				 }
						monthdata[i].daynum=daynum; 
		 			 }
		 }
		 for(var i=1;i<=12;i++){
			 var amount=monthdata[i].amount;
			 if(amount!=""){
				 var sub=amount%monthdata[i].daynum;
				 var myamount=parseInt(amount/monthdata[i].daynum);
				 var start= monthdata[i].date.clone();
				 var end = start.clone();
				 end=end.setMonth(end.getMonth() +1);
				 end=new Date(end); 
				 end=end.setDate(1);
				 end=new Date(end); 
				 while(start<end){
				 			  var param={};
				 			  param.sku=state.itemrow.sku;
				 			  param.groupid=state.itemrow.groupid;
				 			  param.amazonauthid=state.itemrow.amazonauthid;
				 			  param.marketplaceid=state.itemrow.marketplaceid;
				 			  param.quantity=myamount;
							  if(sub>0){
								   param.quantity=param.quantity+1;
								   sub=sub-1;
							  }
				 			  param.date=start;
				 			  preSaleList.push(param);
							  start=start.clone();
				 			  start=start.setDate(start.getDate()+1);
				 			  start=new Date(start);
							  
				 }
			 }
		 }
		 handleSave(preSaleList);
	 }
	 function submitFormRange(){
	       var start=state.dateRange[0];
		   var end=state.dateRange[1];
		   var preSaleList=[];
		   if(state.presaleRange==""){
		   			  ElMessage.error("预设销量不能为空");
		   			  return ;
		   }
		   if(!state.dateRange || !state.dateRange[0]){
		   			  ElMessage.error("预估时间段不能为空");
		   			  return ;
		   }
		   while(start<=end){
			  var param={};
			  param.sku=state.itemrow.sku;
			  param.groupid=state.itemrow.groupid;
			  param.amazonauthid=state.itemrow.amazonauthid;
			  param.marketplaceid=state.itemrow.marketplaceid;
			  param.quantity=state.presaleRange;
			  param.date=start;
			  preSaleList.push(param);
			  start=start.clone();
			  start=start.setDate(start.getDate()+1);
			  start=new Date(start); 
		   }
		 handleSave(preSaleList);
	 }
	 function submitFormLast(){
		 var preSaleList=[];
		 var date=new Date();
		 if(state.presale==""){
			  ElMessage.error("预设销量不能为空");
			  return ;
		 }
		 for(var i=1;i<=state.lastType;i++){
			 var param={};
			 param.sku=state.itemrow.sku;
			 param.groupid=state.itemrow.groupid;
			 param.amazonauthid=state.itemrow.amazonauthid;
			 param.marketplaceid=state.itemrow.marketplaceid;
			 param.quantity=state.presale;
			 param.date=date;
			 preSaleList.push(param);
			 date=date.clone();
			 date=date.setDate(date.getDate()+1);
			 date=new Date(date);
		 }
		handleSave(preSaleList);
	 }
	 function handleDelete(){
		 var param={};
		 param.sku=state.itemrow.sku;
		 param.groupid=state.itemrow.groupid;
		 param.amazonauthid=state.itemrow.amazonauthid;
		 param.marketplaceid=state.itemrow.marketplaceid;
		 clear(param).then(res=>{
		 			 ElMessage.success("清除成功");
		 			 handleQuery();
					 emit("confirm",state.parentrow);
		 })
	 }
	 function handleQuery(){
			 state.presale="";
	 		 var data={};
	 		 data.marketplaceid=state.itemrow.marketplaceid;
	 		 data.amazonauthid=state.itemrow.amazonauthid;
	 		 data.sku=state.itemrow.sku;
	 		 if(state.radiotype=="预估销量"){
	 			 getSales(data).then(res=>{
	 			 	 saleChart(res.data);
	 			 })
	 		 }else if(state.radiotype=="预估同期"){
	 			 data.groupid=state.itemrow.groupid;
	 			 //加载月度预估图表
				 state.calendarloading=true;
	 			 getProductPreSalesByMonth(data).then((res)=>{
					 chartPreSale(res.data,"month");
	 			 })
	 		 }else{
				 state.yearloading=true;
				 var datas={};
				 var day=new Date(); 
				 var fromyear = day.clone();
				 fromyear=fromyear.setMonth(fromyear.getMonth() -24);
				 datas.groupid=state.itemrow.groupid;
				 datas.marketplace=state.itemrow.marketplaceid=="EU"?"IEU":state.itemrow.marketplaceid;
				 datas.fromdatestr=dateFormat(fromyear).substring(0,4)+"-01-01";
				 datas.enddatestr=dateFormat(day).substring(0,4)+"-12-31"
				 datas.summaryType="month";
				 datas.sku=state.itemrow.sku;
				 //加载月度销售图表
				 salessumApi.getOrderData(datas).then(res=>{
					var serialyear=[];
					var leng=[];
					var label=[];
					var saledata=res.data.records[0];
					for(var i=1;i<=12;i++){
						var month=i<10?"0"+i:""+i;
						 label.push(month+"月");
					}
					for(var yearindex=1;yearindex<=3;yearindex++){
						
						var serial=[];
						var totalqty=0;
						for(var i=1;i<=12;i++){
								var month=i<10?"0"+i:""+i;
								 var year=dateFormat(fromyear).substring(0,4);
								 var day="01";
								 if(saledata&&saledata[year+month+day]!=undefined){
								     serial.push(saledata[year+month+day]);
									 totalqty=totalqty+parseInt(saledata[year+month+day]);
								 }else{
									 serial.push(0);
								 }
						}
					     leng.push(dateFormat(fromyear).substring(2,4)+"年("+totalqty+"个)");
						 serialyear.push(serial);
						 fromyear=new Date(fromyear);
						 fromyear=fromyear.setMonth(fromyear.getMonth() +12);
					}
				 	 chartYearSale(leng,label,serialyear);
				 })
			 }
	 		
	 	 }
	 
	function handleTabsChange(value){
		state.activeTab=value;
		saleChart();
	}
    function printSettingMarkPointLine(myseries,zerodatalength){
		if(state.activeTab!=0)return;
		var zerodata=[];
		for(var i=0;i<zerodatalength;i++){
		    zerodata.push(0);
		}
		var  d = new Date(state.preEndDay);
		 var formatPreEndDay=d.format("MM-dd");
		 var pointSetting=[];
		pointSetting.push({
			 value:"设定",
			 coord: [formatPreEndDay, 0],
		 })
		 				 
		 
		 var preline={
		        smooth: 0.5,
		 	    showAllSymbol:false,
				show:false,
				 showSymbol:false,
				lineStyle: {opacity:0},
		        type: 'line',
		 			data: zerodata ,
		 			label:{
		 				show: false,
		 			},
		    };
			preline.markPoint={
							   symbolSize:50, 
							   itemStyle : {
									normal : {
										color :'#FF6700',
										label : {
											show: true,
											color:'#ffffef'
										}
									}
								},
								data :pointSetting,
			              };
			myseries.push(preline);
	}
	function saleChart(chart){
		if(chart!=null){
			state.chart=chart;
		}else{
			chart=state.chart;
		}
		nextTick(()=>{
			var saleschart=document.getElementById(state.chartid);
			if(myChart!=null){
			   myChart.dispose();
			}
			myChart = echarts.init(saleschart);
			var myseries=[];
			var myvisualMap = {};
			var bIndex = 0;
			 
			 chart.lines.forEach(line=>{
			 	 var point1=[];
				 var yAxisList=[];
			 	 if(line.markpoint&&line.markpoint.length>0){
					 bIndex = line.markpoint.length-1;
			 		 line.markpoint.forEach((item,index)=>{
												 if(line.markpoint.length-1===index){
												    point1.push({
														 value:"预估",
														 coord: [item.name, item.yaxis],
													 })
													 }
												yAxisList.push(item.xaxis);
			 		 						 })
			 	 }else{
					bIndex = 1 
				 }
				printSettingMarkPointLine(myseries,line.data.length);
				window.yAxisList=yAxisList;
				 myvisualMap = {
				   show: false,
				   dimension: 0,
				   pieces: [
				   {gt:bIndex,color: '#FF6700'},   
				   {gt: 0, lte:bIndex,color: '#529b2e'}, 
				   ]
				 };
			
				var line={
			           smooth: 0.5,
			           name: line.name,
			 		   showAllSymbol:false,
			           type: 'line',
			 				data: line.data,
			 				label:{
			 					show: true,
			 				},
			       };
				if(point1&&point1.length>0){
					 line.markPoint={
									   symbolSize:50, 
									   itemStyle : {
											normal : {
												color :'#529b2e',
												label : {
													show: true,
													color:'#fff'
												}
											}
										},
										data :point1,
			 		               };
				}
				var startday="";
				var endday="";
				if(state.data.shipday){
					chart.labels.forEach((item,index)=>{
						if(index==0){
						   startday=item;
						}
						if(index==state.data.shipday){
							endday=item;
						}
					});
					line.markArea= {
					  itemStyle: {
					    color: 'rgba(255, 220, 213, 0.4)'
					  },
					
					  label:{
						  position: ['50%', '50%']
					  },
					     emphasis: {
					         label:{
					         	 position: ['50%', '50%']
					         },
							 itemStyle: {
							   color: 'rgba(255, 195, 180, 0.4)'
							 },
					        },
					  data: [
					    [
					      {
					        name: '备货周期'+state.data.shipday+"天",
							value:state.data.avgsales,
					        xAxis: startday
					      },
					      {
					        xAxis: endday
					      }
					    ],
					  ]
					}
				}
				
			 	myseries.push(line);
			 }) 
			 
			
			 var option = {
			     tooltip : {
			     				backgroundColor:'rgba(0,0,0,0.8)',
			     				show: true ,
								trigger: "item",
			     				position: 'bottom',
			     				formatter : function(params){
			     					if(params != null&&!Array.isArray(params)){
										if(params.name.indexOf("备货周期")>=0){
											 return params.name +"<br>"+"平均日销量："+params.value ;
										}else{
											var title="系统预估销量";
											if(window.yAxisList&&window.yAxisList.includes(params.name)){
											  title="手动预估销量";	
											}
											return "日期:  "+params.name +"<br>"+title+": "+ params.value;
										}
										
			     					}
			     				},
			 					label:{
			 						show:true,
			 					    color:"#333",
			 					 },
			 					 textStyle:{
			 						 color:"#fff"
			 					 },
			 					borderColor:"#000",
			     			},
			       legend:{
					   top:0,
			 		  type: "plain",
			 		  icon: "circle",
			 		   itemWidth: 8,
			 	  },
			       xAxis: {
			 						data : chart.labels,
			 						axisLabel:{
			 							show : true,
			 							textStyle:{
			 								color:'#999'
			 							}
			 						},
			 						splitLine:{
			 							 show:false
			 				        } ,
			 						axisLine:{
			 							lineStyle:{
			 								color:'#eee',
			 								width:1,// 这里是为了突出显示加上的
			 							}
			 						}
			 				
			 			},
			 			grid : {
			 					x:50,
			 					x2:45,
			 					y :50,
			 					y2:20,
			 					borderWidth : 0,
			 				},
			       yAxis: {
			 				axisLabel:{
			                         color:"#acb0b9"
			 				},
			 				splitLine:{
			 					lineStyle:{
			 					color:"#F2F3F6"
			 					}
			 				}
			 			},
				   visualMap: myvisualMap,
			       series: myseries
			 }
			 myChart.setOption(option,true);
			 window.addEventListener('resize',()=>{
					   myChart.resize();
			 })
		});
	}
	const shortcuts= [
	  {
	    text: '未来7天',
	    value: () => {
	      const start = new Date()
	      const end = new Date()
	      end.setTime(end.getTime() + 3600 * 1000 * 24 * 7)
	      return [start, end]
	    },
	  },
	  {
	    text: '未来30天',
	    value: () => {
	      const start = new Date()
	      const end = new Date()
	      end.setTime(end.getTime() + 3600 * 1000 * 24 * 30)
	      return [start, end]
	    },
	  },
	  {
	    text: '未来90天',
	    value: () => {
	      const start = new Date()
	      const end = new Date()
	      end.setTime(end.getTime() + 3600 * 1000 * 24 * 90)
	      return [start, end]
	    },
	  },
	]
 
 
 var isdown=false;
 var yAx=[];
 var calendarMyChart=null;
 var olddata=[];
 var moveindex=null;
 var stopplus=false;
 function chartPreSale(data,type){
 	//initCalendar(groupid,marketplaceid,sku,rowid,data);
 	calendarMyChart = echarts.init(document.getElementById("calendar"+state.chartid));
 	document.getElementById("calendar"+state.chartid).oncontextmenu=function(){return false;}
 	  var symbolSize = 12;
 	  var labeldata=[];
 	  var color=[];
 	  olddata=data;
 	  var serialdata = [];
 	  var oldserialdata  =[]
 	  for(var i=0;i<data.length;i++){
 	      var holiday="";
 		  if(data[i]["holiday"]){
 			  holiday=data[i].holiday;
 		  } 
 		  if(data[i].value){
 			  var item={value:data[i].value,date:data[i].date,holiday:holiday, itemStyle:{normal:{color:'#00a65a'}}};
 			  serialdata.push(item);
 		  }else{
 			  var item={value:data[i].oldvalue,date:data[i].date,holiday:holiday, itemStyle:{normal:{color:'#808080'}}};
 			  serialdata.push(item);
 		  }
 		  if(data[i].oldvalue){
 			  var item={value:data[i].oldvalue,date:data[i].date,holiday:holiday, itemStyle:{normal:{color:'#808080'}}};
 			  oldserialdata.push(item);
 		  }else{
 			  var item={value:0,date:data[i].date,holiday:holiday, itemStyle:{normal:{color:'#808080'}}};
 			  oldserialdata.push(item);
 		  }
 		  yAx.push(0);
 	      labeldata.push(data[i].date);
 	  }
 	  var option = {
 	      tooltip: {
 	            trigger: 'axis',
 	            formatter: function (option) {
 	            	var holiday="";
 	            	if(option[0].data["holiday"]){
 	            		holiday="<br/>节日："+option[0].data["holiday"];
 	            	}
 	            	  if(option[0].data.itemStyle.color=="#00a65a"){
 	            		  return  "日期:"+option[0].name+"<br>当前预估:"+parseInt(option[0].value)+"<br/>去年同期:"+parseInt(option[1].value)+holiday;
 	            	  }else if(option[0].data.itemStyle.color=="#FA8072"){
 	            		  return  "日期:"+option[0].name+"<br>当前预估:"+parseInt(option[0].value)+"<br/>去年同期:"+parseInt(option[1].value)+holiday;
 	            	  }else{
 	            		  return  "日期:"+option[0].name+"<br>去年同期:"+parseInt(option[1].value)+holiday;
 	            	  }
 		          },
 			  axisPointer: {
 			        lineStyle: {
 			            type: 'dashed'
 			        } 
 			    }
 	        },
 	      grid: {
 	          top:'30px',
 	          bottom:'30px',
 	          left:'50px',
 	          right:'30px'
 	      },
 	      xAxis: {
 	          type: 'category',
 	          data: labeldata,
 	          splitLine:{show:true},
			  
 	      },
 	 
 	      yAxis: {
 	          type: 'value',
 	          axisLabel:{
 	        	  formatter: function (value) {
 	        	      return parseInt(value);
 	        	  }
 	          },
 	          splitLine:{show:false},
 	          scale:false,
 	          minInterval:1
 	      },
 	      series: [
 	          {   id: 'a',
 	              type: 'line',
 	              smooth: false,
 	              symbolSize: symbolSize,
 	              showAllSymbol:true,
 	              data: serialdata,
 	      		  itemStyle:{
 	      		 		normal: {
 	      		 			lineStyle:{
 	                              width:1,
 	                              color:'#ffa400',  
 	                              type:'solid'  //'dotted'虚线 'solid'实线
 	                          },
 	      		 			 label:{
 	      		 		     formatter: function (option) {
 	      			              return  parseInt(option.data.value);
 	      			          },
 	      		 				show: true,
 	      		 				textStyle:{
 	      		 					color:'#999'
 	      		 				},
 	      		 			},
 	      		  
 	      		 		}       
 	      		 	}
 	          },
 	          {   id: 'old',
 	              type: 'line',
 	              smooth: false,
 	              symbolSize: 0,
 	              showAllSymbol:true,
 	              data: oldserialdata,
 	      		  itemStyle:{
 	      		 		normal: {
 	      		 			lineStyle:{
 	                              width:1,
 	                              color:'#b1b1b1',  
 	                              type:'dotted'  //'dotted'虚线 'solid'实线
 	                          },
 	      		 			 label:{
 	      		 		     formatter: function (option) {
 	      			              return  parseInt(option.data.value);
 	      			          },
 	      		 				show: false,
 	      		 				textStyle:{
 	      		 					color:'#797979'
 	      		 				},
 	      		 			},
 	      		  
 	      		 		}       
 	      		 	}
 	          }
 	      ]
 	  };
  
 	 
 	 nextTick(()=>{
		 // Add shadow circles (which is not visible) to enable drag.
		 calendarMyChart.setOption(option);
		  state.calendarloading=false;
		 for(var i=0;i<labeldata.length;i++){
		    calendarMyChart.on('click', {seriesIndex: 0, name: labeldata[i]}, function (params) {
		   	    if("month"==type){
		   	    	showCalendarByDay(params.name);
		   	    }else{
		 						handleQuery();
		 		 }
		   	});
			calendarMyChart.on('click', {seriesIndex: 1, name: labeldata[i]}, function (params) {
			    if("month"==type){
			    	showCalendarByDay(params.name);
			    }else{
								handleQuery();
				 }
			});
		 }
	 })
 	  
 }
 
 function showCalendarByDay(month){
 	 var param={};
	 param.sku=state.itemrow.sku;
	 param.groupid=state.itemrow.groupid;
	 param.marketplaceid=state.itemrow.marketplaceid;
	 param.month=month;
	 	 state.calendarloading=true;
 	getProductPreSales(param).then((res)=>{
		chartPreSale(res.data,"day");
	});
 }
 var yearMyChart =null;
 function chartYearSale(leng,label,serdata){
	 nextTick(()=>{
		 if(yearMyChart!=null){
			  yearMyChart.dispose();
		 }
 	  yearMyChart = echarts.init(document.getElementById("yearChart"+state.chartid));
 	document.getElementById("yearChart"+state.chartid).oncontextmenu=function(){return false;}
 	  var symbolSize = 12;
 	  var color=["#3c8bfa","#67c23a","#ff7315"];
 	  olddata=data;
	  var myseries=[];
 	  var oldserialdata  =[]
 	  myseries.push( {
 	  								  name: leng[0],
 	  								  type: 'line',
 	  								 
 	  								  data: serdata[0],
 	  								  smooth: false,
 	  								  symbolSize: symbolSize,
 	  								  showAllSymbol:true,
									  areaStyle: {
									  									  opacity: 0.1 ,
									  },
 	  								  itemStyle:{
 	  								   	   normal: {
 	  								   	      	 lineStyle:{
 	  								                  width:1,
 	  								                  type:'solid'  //'dotted'虚线 'solid'实线
 	  								              },
 	  											 label:{
 	  												show: true,
 	  											},
 	  								  
 	  								   	     }       
 	  								  }
 	  								})
myseries.push( {
								  name: leng[1],
								  type: 'line',
								 
								  data: serdata[1],
								  smooth: false,
								  symbolSize: symbolSize,
								  showAllSymbol:true,
								  areaStyle: {
								  									  opacity: 0.1 ,
								  },
								  itemStyle:{
								   	   normal: {
								   	      	 lineStyle:{
								                  width:1,
								                  type:'solid'  //'dotted'虚线 'solid'实线
								              },
											 label:{
												show: true,
											},
								  
								   	     }       
								  }
								})
myseries.push( {
								  name: leng[2],
								  type: 'line',
								 
								  data: serdata[2],
								  smooth: false,
								  symbolSize: symbolSize,
								  showAllSymbol:false,
								  areaStyle: {
									  opacity: 0.1 ,
								  },
								  itemStyle:{
								   	   normal: {
								   	      	 lineStyle:{
								                  width:2,
								                  type:'solid'  //'dotted'虚线 'solid'实线
								              },
											 label:{
												show: true,
											},
								  
								   	     }       
								  }
								})																	
 	 var testoption = {
					  tooltip: {
						trigger: 'axis'
					  },
					  legend: {
						  top:0,
						data: leng
					  },
					  color:color,
					  grid: {
						left: '3%',
						right: '4%',
						bottom: '3%',
						containLabel: true
					  },
					  toolbox: {
						feature: {
						  saveAsImage: {}
						}
					  },
					  xAxis: {
						  type: 'category',
						  data: label,
						  splitLine:{show:false},
					  },
					 
					  yAxis: {
						type: 'value',
						axisLabel:{
						color:"#acb0b9"
						},
						splitLine:{
							lineStyle:{
							color:"#F2F3F6"
							}
						}
					  },
					  series: myseries
					};
   yearMyChart.setOption(testoption,true);
  
   yearMyChart.on('click', 'series', function (params) {
    
		  if(state.yeartype=="year"){
		  	 state.yeartype="month";
			 showYearChartByMonth(params.name.replace("月",""));
		  }else{
			  state.yeartype="year";
			   handleQuery();
		  }
   });
	state.yearloading=false;
   window.addEventListener('resize',()=>{
   					   yearMyChart.resize();
   })
 });
 }
 
 function getFirstDayOfMonth(today) {
     return new Date(today.getFullYear(), today.getMonth(), 1);
 }
 
 // 获取当月最后一天
 function getLastDayOfMonth(today) {
     const nextMonth = today.getMonth() + 1;
     const lastDay = new Date(today.getFullYear(), nextMonth, 0);
     return lastDay;
 }
 
 
  
 async function showYearChartByMonth(month){
	 state.yearloading=true;
	 var datas={};
	 var day=new Date(); 
	 var fromyear = day.clone();
	 fromyear=fromyear.setMonth(fromyear.getMonth() -24);
	 datas.groupid=state.itemrow.groupid;
	 datas.marketplace=state.itemrow.marketplaceid=="EU"?"IEU":state.itemrow.marketplaceid;
	 datas.fromdatestr=dateFormat(new Date(fromyear)).substring(0,4)+"-01-01";
	 datas.enddatestr=dateFormat(day).substring(0,4)+"-12-31"
	 datas.summaryType="day";
	 datas.sku=state.itemrow.sku;
	 var leng=[];
	 var label=[];
	 var serialyear=[];
	 var monthdays=30;
	 if("01,03,05,07,08,10,12".indexOf(month)>=0){
		 monthdays=31;
	 }else if(parseInt(month)==2){
		 monthdays=29;
	 }
	 for(var i=1;i<=monthdays;i++){
	 	 label.push(i>=10?""+i:"0"+i);
	 }
	 for(var i=1;i<=3;i++){
		 var  year=dateFormat(fromyear).substring(0,4);
		 var start= getFirstDayOfMonth(new Date(year+"-"+month+"-01"));
		 var end=getLastDayOfMonth(new Date(year+"-"+month+"-01"));
		 fromyear=new Date(fromyear);
		 fromyear=fromyear.setMonth(fromyear.getMonth() +12);
		 datas.fromdatestr=start;
		 datas.enddatestr=end;
		 var totalqty=0;
		  var serdata=[];
		 await salessumApi.getOrderData(datas).then(res=>{
			     if(res.data&&res.data.records&&res.data.records.length>0){
					 if(res.data.records[0]["vsum"]){
						  totalqty=parseInt(res.data.records[0].vsum);
					 }
					 var data=res.data.records[0];
					 for(var i=1;i<=monthdays;i++){
						 var key=year+month+(i>=10?""+i:"0"+i);
						 serdata.push(data[key]);
					 }
				 }else{
					 for(var i=1;i<=monthdays;i++){
					 	  serdata.push(0);
					 }
				 }
				 
		 })
		  serialyear.push(serdata);
		  leng.push(year+"-"+month+"月（"+totalqty+")" );
	 }
	 chartYearSale(leng,label,serialyear);
	 
 }
    
</script>

<style>
	.line-h-16{
		line-height: 16px;
	}
	.flex-center-between .product-img{
		height: 60px;
		width: 60px;
		overflow:visible;
		margin-right: 16px;
	}
	.flex-center-between .product-img img{
		height: 60px;
		width: 60px;
	}
	.pro-message .sku{color: var(--el-color-blue);margin-top:8px;margin-bottom:16px;}
	.data-group p{
		font-size: 12px;
		color: var(--el-text-color-placeholder);
	}
	.p-16{
		padding-top: 16px;
	}
	.m-t-16{margin-top:16px;}
	.m-c{background-color:#fff!important;line-height:16px;}
</style>
