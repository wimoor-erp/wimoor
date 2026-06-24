<template>
	<div class="pag-radius-bor">
		<div class="flex-center-between">
			<h4>收支统计</h4>
			<el-popover
			    :visible="visible"
			    placement="left"
			    :width="250"
				@show="handleFocus"
			  >
			  <template #reference>
			    <el-button size="small" @click="visible = !visible" >
			     {{dateVal}}
				  <el-icon><ArrowDown /></el-icon>
				  </el-button >
			  </template>
			      <el-radio-group v-model="dateType" @change="handleDateTypeChange">
			        <el-radio-button label="year"  >年</el-radio-button>
			        <el-radio-button label="month" >月</el-radio-button>
			      </el-radio-group>
				  <el-date-picker
				    class="m-t-16"
					ref="datepickerRef"
					@change="handleChange"
					v-model="dateVal"
					:type="dateType"
					:editable="false"
					:clearable="false"
				  />
			   
			  </el-popover>
		</div>
		<div class="m-t-16">
			<el-row :gutter="16">
				<el-col :span="12">
					<el-card shadow="never" >
						<div class="flex-center ">
							<div class="round-b-32">
							<bank-card class="ic-cen"
							 theme="filled" size="16" fill="#55aaff" :strokeWidth="3"/>
							</div>
							<div class="m-l-16">
								<p class="font-bold font-18">{{summary.outsum}}</p>
								<p class="font-extraSmall">本月支出（元）</p>
							</div>
							</div>
					</el-card>
				</el-col>
				<el-col :span="12">
					<el-card shadow="never">
						<div class="flex-center ">
							<div class="round-y-32">
							<finance class="ic-cen"
							 theme="filled" size="17" fill="#ffaa00" :strokeWidth="3"/>
							</div>
							<div class="m-l-16">
								<p class="font-bold font-18">{{summary.insum}}</p>
								<p class="font-extraSmall">本月收入（元）</p>
							</div>
						</div>
					</el-card>
				</el-col>
			</el-row>
			<el-row>
				<div id='chart' style='height:291px;width:100%'>
				 
				</div>
			</el-row>
		</div>
	</div>
</template>

<script setup>
	import {BankCard,Finance} from '@icon-park/vue-next';
	import {ArrowDown} from '@element-plus/icons-vue'
	import {dateYearFormat,dateMonthFormat} from '@/utils/index.js';
	import * as echarts from "echarts";
	import journalApi from '@/api/erp/finances/journalApi.js';
	import { ref,reactive,onMounted,watch,toRefs} from 'vue';
	const nowDate=dateYearFormat(new Date());
	const datepickerRef=ref();
	const state = reactive({
		visible:false,
		dateType:"year",
		dateVal:nowDate,
		rowdata:{},
		summary:{},
		series: [
		    {
			  name: '收入',
			   smooth: true,
		      data: [],
		      type: 'line',
			  lineStyle:{
			  	color:'#FF6700',
			  },
			  itemStyle:{
			  	color:'#FF6700',
			  }
		    },
			{
			  name: '支出',
			  smooth: true,
			  data: [],
			  type: 'line',
			  lineStyle:{
			  	color:'#3094ff',
			  },
			  itemStyle:{
			  	color:'#3094ff',
			  }
			}
		  ],
		dates:[], 
	})
	const {
		series,
		dates,
		visible,
		dateVal,
		dateType,
		summary
	}= toRefs(state)
	function generalChart(series,dates){
		  var myChart = echarts.init(document.getElementById('chart'));
		  var option = {
	         tooltip : {
				trigger : 'axis',
			     },
	          legend:{
					data:['支出','收入'],
					icon: "circle",
					itemWidth:6,
					itemHeight:6,
                    show: true ,
					top:16,
	          },
	          xAxis: {
					boundaryGap:false,
					data:dates,
					axisLine:{
	              show: false
					},
					axisTick:{
					show: false
					},
					axisLabel:{
	               color:"#acb0b9"
					},
					
				},
				grid:{
					right:32,
					left:45,
	              bottom:32
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
	          series: series
		  }
		   
		   myChart.setOption(option);
		   window.addEventListener('resize',()=>{
			   myChart.resize();
		   })
	}
	function handleChange(val){
		state.visible = false
		formatDate(val);
		loadChartData();
	}
	function formatDate(date){
		if(state.dateType=='year'){
			state.dateVal = dateYearFormat(date)
		}else{
			state.dateVal = dateMonthFormat(date)
		}
	}
	function handleDateTypeChange(){
		handleFocus();
		 
		if(state.dateType=='year'){
			state.dateVal = dateYearFormat(state.dateVal)
		}else{
			if(state.dateVal.length<=4){
				state.dateVal=state.dateVal+"-"+(new Date().getMonth()+1);
			}
			state.dateVal = dateMonthFormat(state.dateVal)
		}
		 loadChartData();
		 
	}
	function handleFocus(){
		datepickerRef.value.focus();
			
	}
	
	function loadChartData(){
		var data={};
		data.acc=state.rowdata.id;
		if(state.dateType=='year'){
			 data.year=state.dateVal;
			 data.month="";
		}else{
			data.year=state.dateVal.split("-")[0];
			data.month=state.dateVal.split("-")[1];
		}
		
		journalApi.getLineData(data).then(res=>{
			var mylabel=[];
			var series0=[];
			var series1=[];
			for (var val in  res.data) {
				if(state.dateType=='year'){
					  mylabel.push(val+"月");
				}else{
					  mylabel.push(val+"日");
				}
			    var inoutdata=res.data[val];
				series0.push(inoutdata.rec);
				series1.push(inoutdata.pay);
			}
			state.series[0].data=series0;
			state.series[1].data=series1;
			state.dates=mylabel;
			generalChart(state.series,state.dates)
		})
	}
	function show(item){
		var data={acc:item.id};
		state.rowdata=item;
		journalApi.outinsum(data).then(res=>{
			if(res.data){
			  state.summary=res.data;
			}else{
			   state.summary={"insum":0.00,"outsum":0.00};
			}
		})
		loadChartData();
	}
	defineExpose({ show })
</script>

<style scoped="scoped">
	.pag-radius-bor{
		    border-radius: var(--el-border-radius-base);
		    width: 100%;
		    padding: 16px 24px;
	}
	.round-b-32{
		display: flex;
		align-items: center;
		justify-content: center;
		height:40px;
		width:40px;
		border-radius:32px;
		background-color: rgba(27,135,255,0.1);
	}
	.round-y-32{
		display: flex;
		align-items: center;
		justify-content: center;
		height:40px;
		width:40px;
		border-radius:32px;
		background-color: rgba(255, 119, 0, 0.1);
	}
	.m-t-16{
		margin-top:16px;
	}
	.m-b-32{
		margin-bottom: 32px;
	}
	.m-b-16{
		margin-bottom: 16px;
	}
	.font-18{
		font-size:20px;
		font-family:DIN Alternate,Helvetica Neue,Helvetica,Arial, SF Pro Display;
	}
	.m-l-16{
		margin-left: 16px;
	}
</style>
