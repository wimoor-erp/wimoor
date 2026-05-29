<template>
	<el-dialog title="库存历史" class="invhisdialog" v-model="dialog.visible" @close="handleClose" @opened = "handleOpen" :append-to-body="true" >
		<el-row class="m-t-16">
			<el-col :span="24">
				<div v-load="loading">
					   <el-radio-group v-model="lastType" style="float:right;margin-top:-15px" @change="handleQuery()" size="small">
					      <el-radio-button :label="7"  >近7天 </el-radio-button>
					      <el-radio-button :label="15" >近15天</el-radio-button>
					      <el-radio-button :label="30" >近30天</el-radio-button>
					    </el-radio-group>
				<div   :id="chartid"  style='height:200px;width:100%'></div>
				</div>
			</el-col>
		</el-row>
	</el-dialog>
</template>

<script setup>
	import{ref,reactive,toRefs,onMounted,nextTick}from"vue"
	import {Close} from '@element-plus/icons-vue';
	import * as echarts from 'echarts';
	import inventoryRptApi from "@/api/amazon/inventory/inventoryRptApi.js";
	import {CheckInputInt,dateFormat,dateFormatMMdd} from "@/utils/index.js";
	import { ElMessage, ElMessageBox } from 'element-plus';
	import { useDark, useToggle } from "@vueuse/core";
	
    const isDark = useDark();
	const preinputRef=ref()
	const emit = defineEmits(['confirm']);
	const state=reactive({
		  dialog:{visible:false},
		  lastType:15,
		  chartid:"fbahischart",
		  loading:false,
		  queryParam:{groupid:"",warehouse:"",sku:"",fromdate:"",enddate:""},
	})
	const { dialog,lastType,chartid,loading,queryParam,} = toRefs(state);
	var myChart =null ;
	function show(groupid,warehouse,sku){
		state.queryParam.groupid=groupid;
		state.queryParam.warehouse=warehouse;
		state.queryParam.sku=sku;
		state.dialog.visible=true;
		handleQuery();
	}
	onMounted(()=>{
		var date=new Date();
		state.chartid="fbahischart"+date.getTime();
	})
	
	defineExpose({show});
    function handleClose(){
		state.dialog.visible=false;
	}
	 function handleQuery(){
		 state.loading=true;
		 const end = new Date()
		 const start = new Date()
		 start.setTime(start.getTime() - 3600 * 1000 * 24 * state.lastType)
		 state.queryParam.fromdate=dateFormat(start);
		 state.queryParam.enddate=dateFormat(end);
		 state.queryParam.notlike=true;
		 //加载月度销售图表
		  inventoryRptApi.getFBAInvDayDetail(state.queryParam).then(res=>{
			  showYearChartByMonth(res.data);
		  })
	 	 }
  async function showYearChartByMonth(data){
  	 var day=new Date(); 
  	 var leng=[];
  	 var label=[];
  	 var serialyear=[];
  	 var monthdays=30;
	 var totalqty=0;
	 var serdata=[];
	const end = new Date()
	const start = new Date()
	 if(data&&data.records&&data.records.length>0){
		 var data=data.records[0];
		 start.setTime(start.getTime() - 3600 * 1000 * 24 * (state.lastType+1));
		 for(var i=state.lastType;i>0;i--){
			start.setTime(start.getTime() + 3600 * 1000 * 24 * 1);
			var key="v"+dateFormat(start);
			serdata.push(data[key]);
			label.push(dateFormatMMdd(start))
		 }
	 } 
     serialyear.push(serdata);
     leng.push(state.queryParam.sku);
	 console.log(leng,label,serialyear);
  	 chartYearSale(leng,label,serialyear);
  }
  var yearMyChart=null;
  function chartYearSale(leng,label,serdata){
  	 nextTick(()=>{
  		 if(yearMyChart!=null){
  			  yearMyChart.dispose();
  		 }
   	  yearMyChart = echarts.init(document.getElementById(state.chartid));
      document.getElementById(state.chartid).oncontextmenu=function(){return false;}
   	  var symbolSize = 12;
  	  var myseries=[];
   	  var oldserialdata  =[]
      myseries.push( {    name: leng[0],
						  type: 'bar',
						  data: serdata[0],
						  smooth: false,
						  color:"#ff7315",
						  symbolSize: symbolSize,
						  showAllSymbol:true,
						  areaStyle: { opacity: 0.1 ,  },
						  itemStyle:{
							   normal: {
									 lineStyle:{
										  width:1,
										  type:'solid'  //'dotted'虚线 'solid'实线
									  },
									 label:{
										 position: "outside",
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
  						data: leng,
						top:"bottom"
  					  },
  					  grid: {
  						left: '3%',
  						right: '4%',
  						bottom: '14%',
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
						  axisLabel:{
						  color:isDark.value==true?"#2f3032":"#A3A6AD"
						  },
  						  splitLine:{show:false},
  					  },
  					 
  					  yAxis: {
  						type: 'value',
  						axisLabel:{
  						color:isDark.value==true?"#2f3032":"#d2d6de"
  						},
  						splitLine:{
  							lineStyle:{
  							color:isDark.value==true?"#2f3032":"#F2F3F6"
  							}
  						}
  					  },
  					  series: myseries
  					};
     yearMyChart.setOption(testoption,true);
  	 state.loading=false;
     window.addEventListener('resize',()=>{  yearMyChart.resize();  })
   });
   }
</script>

<style>
	.invhisdialog .el-dialog__body{
		padding-top:0px;
		padding-bottom:10px;
	}
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
</style>
