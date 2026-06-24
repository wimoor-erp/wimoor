<template>
	<div class="shipdialog">
	<el-dialog v-model="arrivalVisible"
	 class="chart-tabs-dialog"  @close="handleClose"
	  :modal="false" :lock-scroll="false"    draggable>
		  <template #header>
		   <div style="padding-right: 20px; padding-top: 4px;">
			<el-tabs
			   v-model="activeMarket"
			   @tab-change="handleClick"
			 >
			   <el-tab-pane  v-for = "(item,index) in marketList" :label="item" :name="item"  >
			   				  
			   </el-tab-pane>
			 </el-tabs>
			 </div>
		  </template>
			<div class="flex-center-between">
						   <el-space :size="0" direction="vertical">
							<span class="font-small">预计到货图表</span>
						   </el-space>
						   <el-radio-group v-model="radiotime"  @change="changeTimeType" size="small">
						         <el-radio-button label="30天" />
						         <el-radio-button label="60天" />
						         <el-radio-button label="90天" />
						       </el-radio-group>
			</div>
		<div id='shipchart' style='height:160px;width:100%'>
		 
		</div>
	</el-dialog>
	</div>
</template>

<script setup>
	import * as echarts from 'echarts'
	import { ElDivider } from 'element-plus'
	import {ref,reactive,onMounted,watch,h,nextTick} from 'vue';
	import dispatchOverseaApi from '@/api/erp/inventory/dispatchOverseaApi.js';
	import orderPlanApi from '@/api/erp/order/orderPlanApi.js';
            var myChart =null;
			let radiotime=ref("30天")
			let activeMarket = ref('')
			let arrivalVisible = ref(false)
			let productData = reactive({})
			let spacer = h(ElDivider, { direction: 'vertical' })
			let marketList=ref([]);
			let chart;
			onMounted(()=>{
			})
			function handleClose(){
					 if(myChart!=null){
						 myChart.dispose();
					 }
			}
			function saleChart(){
				if(myChart!=null){
					 myChart.clear();
				}
				myChart = echarts.init(document.getElementById('shipchart'))
				var myseries=[];
			
				chart.lines.forEach(line=>{
					 var point1=[];
					 var point2=[];
					 if(line.markpoint){
						 line.markpoint.forEach(item=>{
						 							point1.push({
						 								value:item.value,
						 								name:item.name,
						 								xAxis:item.xaxis,
						 								yAxis:item.yaxis,
														 label:{
																 color:"#fff",
															},
															color:"#333",
						 							}) 
						 						 })
					 }
					 if(line.markpoint2){
						 line.markpoint2.forEach(item=>{
						 												point2.push({
						 													value:item.value,
						 													name:item.name,
						 													xAxis:item.xaxis,
						 													yAxis:item.yaxis,
																			 label:{
																					color:"#fff",
																				},
																				color:"#333",
						 												}) 
						 											 })
					 }
					 
					myseries.push({
				          smooth: 0.5,
				          name: line.name,
						  showAllSymbol:false,
						  markPoint:{
						  	symbolSize:12, 
						  	itemStyle : {
						  		normal : {
						  			color : '#FF7F50',
						  			label : {
						  				formatter : function(p) { // n name
						  					 return "售罄";
						  				}
						  			}
						  		}
						  	},
						  	data :point1,
						  },
				          type: 'line',
								data: line.data,
								lineStyle:{
									color:'#FF6700',
								},
								itemStyle:{
									color:'#FF6700',
								},
								label:{
									show: true,
								},
				      });
					  myseries.push({
					        smooth: 0.5,
							showAllSymbol:false,
					        name: '安全库存',
					        type: 'line',
							data: line.data2,
							lineStyle:{
								color:'#67c23a',
								width:1,
							},
							itemStyle:{
								color:'#67c23a',
							},
							label:{
							   color:"#333",
							 },
							markPoint: {
								symbolSize:48, 
								symbolRotate:180,
								symbol:'pin',
								itemStyle : {
									normal : {
										color : '#ffa400',
										label : {
											offset :[0, 6],
											formatter : function(p) { // n name
													return "\n到货";
											}
										}
									}
								},
								data :point2,
							}
					  });
								   
				}) 
				var option = {
				    tooltip : {
				    				backgroundColor:'rgba(0,0,0,0.8)',
				    				show: true ,
				    				position: 'bottom',
				    				formatter : function(params){
				    					if(params != null){
				    						if(params.data.name){
				    							return params.name+params.value;
				    						}else if(params.seriesIndex==0 ){
				    							return "安全库存: "+params.value 
				    						}else{
				    							return "日期:  "+params.name +"<br>"+"库存: "+ params.value;
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
				    				trigger: 'item',
				    				axisPointer : {            // 坐标轴指示器，坐标轴触发有效
				    					type : 'line', 
				    					axis: 'auto' ,// 默认为直线，可选为：'line' | 'shadow'
				    			        lineStyle : {          // 直线指示器样式设置
				    			        	color: '#333',
				    			            width: 1,
				    			            type: 'solid'
				    			        },
				    			    }
				    			},
				      legend:{
						  top:0,
						  type: "plain",
						  icon: "circle",
						   itemWidth: 8,
					  },
				      xAxis: {
						boundaryGap : true, 
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
									y : 30,
									y2: 30,
									borderWidth : 0,
								},
				      yAxis: {
								axisLabel:{
				                        color:"#acb0b9"
								},
								splitLine:{
									lineStyle:{
									color:"rgba(170,170,170,0.2)"
									}
								}
							},
				      series: myseries
				}
			  
		 
				 myChart.setOption(option,true);
				 window.addEventListener('resize',()=>{
						   myChart.resize();
				 })
			}
			 
			function show(row){
				var param={"sku":row.sku, "amount":row.amount};
				arrivalVisible.value=true;
				radiotime.value="30天";
				productData.daysize=30;
				productData.sku=row.sku;
				productData.warehouseid=null;
				nextTick(()=>{
					var list=[];
					orderPlanApi.getExpandCountryData(param).then(res=>{
						if(res.data){
							res.data.forEach(item=>{
								if(!list.includes(item.mycountry)){
									list.push(item.mycountry);
								}
							});
							activeMarket.value=res.data[0].mycountry;
							productData.warehouseid=res.data[0].mycountry;
						}
						marketList.value=list;
						 handleChartSaleLine();
					});
				});
			}
			defineExpose({
			  show
			}) 
			async function handleChartSaleLine(){
					await dispatchOverseaApi.shipLine(productData).then((res)=>{
						if(res.data){
							 chart=res.data;
						}
					});
				nextTick(()=>{
					saleChart();
				});
			}
			function handleClick(){
				if(productData.warehouseid!=activeMarket.value){
					productData.warehouseid=activeMarket.value;
					handleChartSaleLine();
				}
			}
			function changeTimeType(){
				if(radiotime.value=="30天"){
					productData.daysize=30;
				}else if(radiotime.value=="60天"){
					productData.daysize=60;
				}else if(radiotime.value=="90天"){
					productData.daysize=90;
				}
				handleChartSaleLine();
			}
			 
</script>

<style >
	.shipdialog .el-dialog__header,.shipdialog .el-dialog__body{
		padding-top:5px !important;
	} 
</style>


