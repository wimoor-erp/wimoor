<template>
	 <el-dialog
	 	   v-model="dialogVisable"
	 	   title="SKU头程历史(每周费用分摊)"
	 	   width="800px"
	 	 >
		 <div id='mychart1' style='height:230px;width:100%'>
		  
		 </div>
	 	 <GlobalTable
	 	   ref="globalTable" :tableData="tableData"   max-height="300px" @loadTable="loadTableData" :stripe="true"  style="width: 100%;margin-bottom:16px;">
	 	 	 <template #field>
	 	 	  <el-table-column prop="opttime"   sortable="custom"     label="发货日期"   >
				  <template #default="scope">
				  		  {{dateFormat(scope.row.opttime)}} 
				  </template>
				</el-table-column>
	 	 	 <el-table-column prop="qty"   sortable="custom"     label="发货数量"   />
			  <el-table-column prop="skufee"   sortable="custom"     label="SKU头程费用(元)"   />
			   <el-table-column prop="avgfee"   sortable="custom"     label="平均单件头程费用(元)"   />
	 	   </template>
	 	  </GlobalTable>
		
	 	   <template #footer>
	 	     <span class="dialog-footer">
	 	       <el-button @click="dialogVisable = false">关闭</el-button>
	 	     </span>
	 	   </template>
	 	 </el-dialog>
</template>

<script setup>
	import {h, ref ,watch,reactive,onMounted,toRefs} from 'vue';
	import * as echarts from 'echarts';
	import {dateFormat,getCurrencyMark,formatFloat} from '@/utils/index.js';
	import tranCompanyApi from '@/api/amazon/inbound/tranCompanyApi.js';
	const globalTable=ref();
	let state=reactive({
		dialogVisable:false,
		tableData:{records:[],total:0},
		queryParams:{}, 
		chartdata:{},
	});
	let {
	  dialogVisable,
	  tableData,
	  queryParams
	} =toRefs(state);
	
	function handleQuery(){
		setTimeout(function(){
			globalTable.value.loadTable(state.queryParams);
		},200)
		
	}
	function show(queryParam,sku){
		queryParam.search=sku;
		state.queryParams=queryParam;
		state.dialogVisable=true;
		handleQuery();
	}
	function loadTableData(params){
		tranCompanyApi.getShipmentSkuFeeReport(params).then((res)=>{
			state.tableData.records = res.data.records;
			state.tableData.total =res.data.total;
			if(state.tableData.total>0){
				state.chartdata=res.data.records[0].chartdata;
				lineChart(state.chartdata);
			}else{
				lineChart({});
			}
		});
	}
	
	function lineChart(data) {
		var myChart =echarts.init(document.getElementById("mychart1"));
		var labels=data.labels;
		var series=data.series;
		var seriesfee=data.seriesfee;
		var seriesavgfee=data.seriesavgfee;
		var chartseries=[];
	
		chartseries.push({
			name : '运费',
			type : 'line',
			smooth : true,
			itemStyle : {
				normal : {
					label : {
						show : true,
						textStyle : {
							color : '#333',
						},
					},
					color :  '#75D6AA',
				}
			},
			data :seriesfee 
		});
		chartseries.push({
			name : '发货数量',
			type : 'line',
			smooth : true,
			itemStyle : {
				normal : {
					label : {
						show : true,
						textStyle : {
							color : '#333',
						},
					},
					color : '#ffa400',
				}
			},
			data :series
		});
		chartseries.push({
			name : '平均单价',
			type : 'line',
			smooth : true,
			itemStyle : {
				normal : {
					label : {
						show : true,
						textStyle : {
							color : '#333',
						},
					},
					color :'#EB6A79'
				}
			},
			data :seriesavgfee 
		});
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
						data : ["运费","发货数量","平均单价"],
						y : 'top',
						x : 'center',
						selected: {
							legendsfirst: true
					    },
						selectedMode:"single"
					},
					color : [ '#ffa400', '#75D6AA', '#EB6A79' ],
					grid : {
						x : 70,
						x2 : 60,
						y : 40,
						y2 : 40,
						borderWidth : 0,
					},
					calculable : false,
					xAxis : [ {
						axisLabel : {
							show : true,
							textStyle : {
								color : '#999'
							}
						},
						splitLine : {
							lineStyle : {
								color : '#f1f1f1',
								width : 1,//这里是为了突出显示加上的
							}
						},
						axisLine : {
							lineStyle : {
								color : '#f1f1f1',
								width : 1,//这里是为了突出显示加上的
							}
						},
						axisTick : {
							show : false,
						},
						type : 'category',
						boundaryGap : false,
						data :labels
					} ],
					yAxis : {
						axisLabel : {
							show : true,
							textStyle : {
								color : '#999'
							}
						},
						splitLine : {
							lineStyle : {
								color : '#f1f1f1',
								width : 1,//这里是为了突出显示加上的
							}
						},
						axisLine : {
							lineStyle : {
								color : '#f1f1f1',
								width : 1,//这里是为了突出显示加上的
							}
						},
					},
					series : chartseries
				};
		// 使用刚指定的配置项和数据显示图表。
		myChart.setOption(option);
	 
	}
	
	defineExpose({ show })	
</script>

<style>
</style>
