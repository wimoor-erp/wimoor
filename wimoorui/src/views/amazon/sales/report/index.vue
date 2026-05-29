<template>
	<div class="main-sty">
		<div class="con-header" >
			<el-row>
				<el-space alignment="flex-start">
				<span class="font-extraSmall">销售渠道:</span>
				<div>
					 <el-checkbox
					    v-model="checkAll"
						 class="cell-b"
					    :indeterminate="isIndeterminate"
					    @change="handleCheckAllChange"
					    >全部渠道</el-checkbox
					  >
				  </div>
				  <el-checkbox-group
				     v-model="queryParam.salechannel"
					  @change="handleCheckedChange"
				  >
				
				  <div>
				    <el-checkbox class="cell-b"  v-for="item in channelNormallist" :key="item.sales_channel"  :label="item.sales_channel">
					  <el-tooltip :content="item.domain">
					    <div  class="text-black">{{item.name}}<span class="font-extraSmall">-{{item.sales_channel}}</span> </div>
					  </el-tooltip>
					</el-checkbox>
					</div>
					<el-checkbox class="cell-b checked-color"  v-for="item in channelDifflist" :key="item.sales_channel" :label="item.sales_channel">
					  <el-tooltip :content="item.domain">
							<div  class="text-black">{{item.name}}<span class="font-extraSmall">-{{item.sales_channel}}</span> </div>
					  </el-tooltip>
					</el-checkbox>
				  </el-checkbox-group>
				 </el-space >
			</el-row>	
			<el-row>
				<el-space >
					
						<div class="font-extraSmall">配送方式:</div>
						<el-checkbox-group
						  v-model="queryParam.fulfillmentChannel"
						  @change="handleQuery"
						>
						<el-checkbox  key="Merchant" label="Merchant">卖家自配送</el-checkbox>
						<el-checkbox  key="Amazon" label="Amazon"> 亚马逊配送</el-checkbox>
						</el-checkbox-group>
				</el-space >	
				<div class='rt-btn-group'> <el-button   @click.stop="showMoreSelect">更多条件筛选</el-button>
				</div>
			</el-row>
			<el-row v-if="showmore==true">
				<el-space >
					<div class="font-extraSmall">客户类型:</div>
					<el-checkbox-group
					  v-model="queryParam.isBusinessOrder"
					  @change="handleQuery"
					>
					<el-checkbox v-for="item in isBusinesslist" :key="item.value" :label="item.value">
					  <span   >{{item.name}}</span>
					</el-checkbox>
				   </el-checkbox-group>
				 </el-space >	 
			</el-row>
			<el-row v-if="showmore==true">
				<el-space >
					<span class="font-extraSmall">促销订单:</span>
					<div class="slider-block">					 
						<el-slider v-model="queryParam.discountrate" range  show-input />
					</div>
					<span class="font-extraSmall text-red"  style="margin-left: 10px;">折扣金额占原价</span>
				 </el-space >	 
			</el-row>
			<el-row v-if="showmore==true">
				<el-space >
					<div class="font-extraSmall">订单状态:</div>
					
					<el-checkbox-group
					  v-model="queryParam.orderStatus"
					  @change="handleQuery"
					>
					<el-checkbox v-for="item in statuslist" :key="item.value" :label="item.value">
					  <span    >{{item.name}}</span>
					</el-checkbox>
					</el-checkbox-group>
					 
				 </el-space >	 
			</el-row>
		  <el-row>
		    <el-space >
		  <Group @change="getGroup"></Group>
		  <Datepicker ref="datepickers" :days="1" :shortIndex="1"  @changedate="changeDate" />
		 <el-input  v-model="queryParam.search" @input="handleQuery" clearable placeholder="请输入SKU" style="width: 250px;" class="input-with-select" >
		    <template #append>
		      <el-button @click="handleQuery" >
		         <el-icon class="ic-cen font-medium">
		          <search/>
		       </el-icon>
		      </el-button>
		    </template>
		  </el-input>
		   </el-space>
		   <div class='rt-btn-group'>
		   <el-button class='ic-btn'  title='列配置'>
		      <setting-two theme="outline" size="16"  :strokeWidth="3"/>
		   </el-button>
		    <el-button   class='ic-btn' title='帮助文档'>
		     <help theme="outline" size="16" :strokeWidth="3"/>
		   </el-button>
		   </div>
		</el-row>
		</div>
			 <el-tabs v-model="activeName" class="demo-tabs" @tab-change="handleClick">
				<el-tab-pane label="units" name="units">
						  <template #label>
							  <el-space :size="16">
									 <span class="font-bold">Units</span>
									 <el-checkbox v-model="unitscheck"  @click.stop="handleQuery" size="large" />
									 <span class="font-extraSmall">销量:{{chartData.unitsum}}</span>
									 <span class="font-extraSmall">
										 环比上期:
										 <span v-if="chartData.lastunitsum>=0">+</span><span v-else></span>
										 {{chartData.lastunitsum}}
										 <span v-if="chartData.lastunitsum>=0">
											<el-icon color="#67c23a"><CaretTop  /></el-icon>
										 </span><span v-else><el-icon color="#f56c6c"><CaretBottom /></el-icon></span>
									 </span>
							  </el-space>
					</template>
				</el-tab-pane>
				<el-tab-pane label="orders" name="orders">
					 <template #label>
						 <el-space :size="16">
							 <span class="font-bold">Orders</span>
							 <el-checkbox v-model="orderscheck"  @click.stop="handleQuery" size="large" />
							 <span class="font-extraSmall">订单数:{{chartData.ordersum}}</span>
							 <span class="font-extraSmall">
								 环比上期:
								 <span v-if="chartData.lastordersum>=0">+</span><span v-else></span>
								 {{chartData.lastordersum}}
								 <span v-if="chartData.lastordersum>=0">
								 	<el-icon color="#67c23a"><CaretTop  /></el-icon>
								 </span><span v-else><el-icon color="#f56c6c"><CaretBottom /></el-icon></span>
							 </span>
						 </el-space>
				</template>
				</el-tab-pane>
				  </el-tabs>
				<div class="pull-right" style="position: relative;left:0;top:0;margin-top:-10px;">
								<el-radio-group v-model="queryParam.bytime"  @change="handleQuery">
								      <el-radio-button label="Daily" >每日</el-radio-button>
									  <el-radio-button label="Weekly" >每周</el-radio-button>
									  <el-radio-button label="Monthly" >每月</el-radio-button>
								</el-radio-group>
				</div>
				<div id='salechart' style='height:360px;width:100%;margin-top:20px;'>
				 
				</div>
			
 
  </div>
	 
</template>
<script>
    export default{ name:"销售报表" };
</script>
<script setup>
	import {h, ref ,watch,reactive,onMounted,toRefs,getCurrentInstance} from 'vue'
	import {Search,ArrowDown,CaretTop,CaretBottom} from '@element-plus/icons-vue'
	import { ElDivider } from 'element-plus'
	import {MenuUnfold,Plus,SettingTwo,Help,Copy,MoreOne,ChartPie} from '@icon-park/vue-next';
	import summaryChartApi from '@/api/amazon/order/summaryChartApi.js';
	import {dateFormat,getCurrencyMark} from '@/utils/index.js';
	import {ElMessage } from 'element-plus';
	import Group from '@/components/header/group_select.vue';
	import Datepicker from '@/components/header/datepicker.vue';
	import * as echarts from 'echarts'
	const globalTable=ref();
	const { proxy } = getCurrentInstance();
	let state=reactive({
		 downLoading:false,
		 queryParam:{
			 startDate:"",
			 endDate:"",
			 groupid:'',
			 search:"",
			 bytime:"Daily",
			 discountrate:[0,100],
			 fulfillmentChannel:["Merchant","Amazon"],
			 isBusinessOrder:[],
			 orderStatus:[],
			 salechannel:[],
		 },
		 checkAll:false,
		 orderscheck:false,
		 unitscheck:false,
		 isIndeterminate:false,
		 totalsales:0,
		 totalsalesrate:0,
		 totalorders:0,
		 totalordersrate:0,
		 statuslist:[],
		 channelNormallist:[],
		 channelDifflist:[],
		 isBusinesslist:[],
		 activeName:'units',
		 isload:true,
		 chartData:{},
		 showmore:true,
		 channelNum:0,
	});
	const {
	   channelNum,
	   queryParam,
	   isload,
	   checkAll,
	   downLoading,
	   isIndeterminate,
	   statuslist,
	   showmore,
	   orderscheck,
	   unitscheck,
	   totalsales,
	   totalsalesrate,
	   totalorders,
	   totalordersrate,
	   activeName,
	   chartData,
	   channelNormallist,
	   isBusinesslist,
	   channelDifflist,
	} =toRefs(state);
	var orderdata=[];
	var listdata=[];
	var labels=[];
	function initChart(type){
		var myChart = echarts.init(document.getElementById('salechart'));
		var myseries=[];
		var legends=[];
		if(type=="saleChart"||type=="all"){
			myseries.push({
								smooth: 0.5,
								type: 'line',
								data: listdata,
								lineStyle:{
									color:'#ff7315',
								},
								itemStyle:{
									color:'#ff7315',
								},
								label:{
									show:true,
								},
								showAllSymbol:false,
								areaStyle:{
									color:"rgba(255,115,21,0.3)",
								},
								markLine:{
												data : [ {
													silent:false,  
													type : 'average',
													name : '平均值'
												} ],
												itemStyle : {
													normal : {
														color : '#ff7315',
														lineStyle : {
															color : '#ff7315'
														},
														label : {
															show : true,
															textStyle : {
																color : '#ff7315',
																fontWeight : 'bold',
															}
														},
													},
												}
											},
			                   })
			
		}
		if(type=="orderChart"||type=="all"){
			var legends=[];
				myseries.push({
								smooth: 0.5,
								type: 'line',
								data: orderdata,
								lineStyle:{
									color:'#409EFF',
								},
								itemStyle:{
									color:'#409EFF',
								},
								label:{
									show:true,
								},
								areaStyle:{
									color:"rgba(64,158,255,0.3)",
								},
								showAllSymbol:true,
								markLine:{
												data : [ {
													silent:false,  
													type : 'average',
													name : '平均值'
												} ],
												itemStyle : {
													normal : {
														color : '#409EFF',
														lineStyle : {
															color : '#409EFF'
														},
														label : {
															show : true,
															textStyle : {
																color : '#409EFF',
																fontWeight : 'bold',
															}
														},
													},
												}
											},
			                   })
				 
			
		}	
		var option = {
			
		     tooltip : { trigger : 'axis', },
		     legend:{
				 top:0,
			  type: "plain",
			  icon: "circle",
			  itemWidth: 8,
			  selected: {
						legendsfirst: true
					},
			  selectedMode:"single"
		     },  
		      xAxis: {
						boundaryGap:false,
						data: labels,
						axisLine:{   show: false },
						axisTick:{ show: false },
						axisLabel:{  color:"#acb0b9" },
					},
					grid:{
						right:62,
						left:62,
		                bottom:20,
						top:18,
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
		      series: myseries
		}
		myChart.setOption(option,true);
		window.addEventListener('resize',()=>{
							  myChart.resize(); 
		})
	}
	 function saleChart(){
	 		initChart("saleChart");
	 	}
	function orderChart(){
			 initChart("orderChart");
		}
	function showMoreSelect(){
		if(state.showmore==false){
			state.showmore=true;
		}else{
			state.showmore=false;
		}
		
	}

	function handleClick(){
		 
		if(state.activeName=="units"){
			if(state.orderscheck==true){
				  setTimeout(function(){initChart("all");},100);
			}else{
				  setTimeout(function(){initChart("saleChart");},100);
			}
		  
		}else{
			if(state.unitscheck==true){
			   setTimeout(function(){initChart("all");},100);
			}else{
			   setTimeout(function(){initChart("orderChart");},100);
			}
		}
		
	}
	function getGroup(val){
		state.queryParam.groupid=val;
	}
	function changeDate(value){
		state.queryParam.enddate=value.end;
		state.queryParam.fromdate=value.start;
	}
	function handleCheckAllChange(value){
		 state.queryParam.salechannel=value? state.channelNum:[];
		 state.isIndeterminate = false;
		 handleQuery();
	}
	 function handleCheckedChange(value){
		   const checkedCount = value.length
		   state.checkAll = checkedCount === state.channelNum.length
		   state.isIndeterminate = checkedCount > 0 && checkedCount < state.channelNum.length
	       handleQuery();
	 }
	 
	function handleQuery(){
		state.queryParam.sku=state.queryParam.search;
		state.queryParam.discountfrom =state.queryParam.discountrate[0];
		state.queryParam.discountto =state.queryParam.discountrate[1];
		summaryChartApi.search(state.queryParam).then((res)=>{
			if(res.data){
				orderdata=res.data.orderdata;
				labels=res.data.labels;
				listdata=res.data.listdata;
				state.chartData.lastordersum=res.data.lastordersum;
				state.chartData.lastunitsum=res.data.lastunitsum;
				state.chartData.ordersum=res.data.ordersum;
				state.chartData.unitsum=res.data.unitsum;
			}else{
				orderdata=[];
				labels=[];
				listdata=[];
				state.chartData.lastordersum=0;
				state.chartData.lastunitsum=0;
				state.chartData.ordersum=0;
				state.chartData.unitsum=0;
			}
			
			 handleClick();
		});
	}
	 
	async function load(){
		await proxy.listDictsByCode("order_status").then(res=>{
			res.data.forEach(function(item){
				if(item.name!="Cancelled"){
					state.queryParam.orderStatus.push(item.value);
				} 
			});
			 state.statuslist=res.data;
		});
		await proxy.listDictsByCode("is_business_order").then(res=>{
			 state.isBusinesslist=res.data;
			 res.data.forEach(item=>{
				  state.queryParam.isBusinessOrder.push(item.value);
			 })
		});
		 
		state.channelNormallist=[];
		state.channelDifflist=[];
		state.queryParam.salechannel=[];
		await summaryChartApi.getOrderChannel().then((res)=>{
			var arr=[]
			 res.data.forEach(item=>{
				 if(item.isdifferent){
					 state.channelDifflist.push(item);
				 }else{
					  state.queryParam.salechannel.push(item.sales_channel);
					  state.channelNormallist.push(item);
				 }
				 arr.push(item.sales_channel)
			 });
			  arr = Array.from(new Set(arr))
			  state.channelNum = arr;
		});
		handleQuery();
	}
	onMounted(async ()=>{
		load()
	})
	 
</script>

<style scoped>
	.slider-block {
	  display: flex;
	  align-items: center;
	  width: 500px;
	}
	.slider-block .el-slider {
	  margin-top: 0;
	  margin-left: 12px;
	}
	.pull-right{
		float:right
	}
	.text-black{
		color:#333;
	}
	.cell-b{
		margin-bottom:8px;
		background-color:#f5f5f5;
		padding-right:16px;
		padding-left:16px;
		border-radius:3px;
		margin-right:16px;
	}
	
</style>
<style>
	.cell-b.is-checked{
		box-shadow: 0 0 0 1px rgba(255,183,135,1) inset;
		
	}
	.checked-color.is-checked{
		box-shadow: 0 0 0 1px rgba(103,194,58,1) inset;
	}
	.checked-color.is-checked .el-checkbox__input.is-checked .el-checkbox__inner{
		background-color: #67c23a;
		    border-color: #67c23a;
	}
</style>