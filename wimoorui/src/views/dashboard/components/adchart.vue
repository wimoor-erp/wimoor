<template>
 <el-col :span='12'>
		  <el-space style='margin-bottom:8px;'>
		    <span class='pag-title'>广告表现</span><span class='pag-small-Extra'>{{chartTitle}}</span>
		  </el-space>
		  <div class='pag-radius-bor'>
		<div class='ch-da-toggle'>
			<el-space :size="16" spacer="|">
                <div v-for='(a,index) in adData' :key='index' class="data-group">
			   <div >
               <div style='font-size:12px;color:#666'>{{a.label}}</div>
               <div class='pag-data-num cell-t-8' >{{a.data}}</div>
			   </div>
			   
            </div>
			</el-space>
		 </div>
			  <div id='adchart' style='height:301px;width:100%'>
			  </div>
		  </div>
	 </el-col>
</template>
<script>
import * as echarts from 'echarts'
import { ref,reactive,onMounted,watch } from 'vue'
import reportApi from '@/api/amazon/advertisement/report/reportApi.js'
import {formatFloat} from '@/utils/index.js';
import { useDark, useToggle } from "@vueuse/core";
export default{
	 name: 'Adchart',
	 components:{useDark},
     props:["parameter"],
     setup(prop,context){
	  let chartTitle =ref();
	  const isDark = useDark();
	  let adData = ref([
	  {
	  		  label:'点击量',
	  		  data:'',
	  		  checked:true,
	  },
	  {
	  		  label:'广告销量',
	  		  data:'',
			  checked:false,
	  },
	  {
	  		  label:'广告费$',
	  		  data:'',
	  		  checked:true,
	  },
	  {
	  		  label:'ACOS',
	  		  data:'0.0%',
			  checked:false,
	  },
	  ]);
	  function generateChart(label,title,series){
	  	  var myChart = echarts.init(document.getElementById('adchart'))
	  	  var option = {
	           tooltip : {
	  			trigger : 'axis',
	  		},
	            legend: {
	  				data:title,
	  				right:0,
	  				top:16,
	  				icon: "circle",
	  				itemWidth:6,
	  				itemHeight:6
	  				
	            },
	            xAxis: {
	  				boundaryGap:false,
	  				data: label,
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
	            yAxis: [{
	  				axisLabel:{
	  				 color:isDark.value==true?"#e5eaf5":"#acb0b9"
	  				},
	  				splitLine:{
	  					lineStyle:{
	  					color:isDark.value==true?"#3f4144":"#F2F3F6"
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
				},],
	            series:series
	  	  }
	  	   
	  	   myChart.setOption(option);
	  	   window.addEventListener('resize',()=>{
	  		   myChart.resize();
	  	   })
	  }
	  function loadChartData(){
		  
		  	      chartTitle.value=prop.parameter.beginDate+ " ~ "+prop.parameter.endDate.substring(0,10);
		  				   let myparam=prop.parameter;
		  			 
		  				  myparam.profileid="all";
		  				  myparam.currency="USD";
		  				  reportApi.getsumproduct(myparam).then((res)=>{
		  					   adData.value[0].data=res.data.summary.clicks;
		  					   adData.value[1].data=res.data.summary.attributedUnitsOrdered;
		  					   adData.value[2].data=formatFloat(res.data.summary.cost);
		  					   adData.value[3].data=formatFloat(res.data.summary.acos)+"%";
		  			   let label=res.data.chartdata.impressions.listLabel;
		  					   let title=['点击量','广告销量','广告费$','ACOS'];
		  					   let series=[{
		  					       smooth: 0.5,
		  					       name: '点击量',
		  					       type: 'line',
		  							data:res.data.chartdata.clicks.listData,
		  							lineStyle:{
		  								color:'#67C23A',
		  							},
		  							itemStyle:{
		  								color:'#67C23A',
		  							},
		  							},
		  					      {
		  					       smooth: 0.5,
		  					       name: '广告销量',
		  					       type: 'line',
		  							data:res.data.chartdata.order.listData,
		  							lineStyle:{
		  								color:'#FF6700',
		  							},
		  							itemStyle:{
		  								color:'#FF6700',
		  							}
		  					      },{
		  					       smooth: 0.5,
		  					       name: '广告费$',
		  					       type: 'line',
		  							data: res.data.chartdata.cost.listData,
		  							lineStyle:{
		  								color:'#409eff',
		  							},
		  							itemStyle:{
		  								color:'#409eff',
		  							}
		  						   }, 
		  					      {
		  					       smooth: 0.5,
		  					       name: 'ACOS',
		  					       type: 'line',
		  						   yAxisIndex:1,
		  							data:res.data.chartdata.acos.listData,
		  							lineStyle:{
		  								color:'#E6A23C',
		  								type: 'dashed',
		  							},
		  							itemStyle:{
		  								color:'#E6A23C',
		  							}
		  					      }]
		  					   generateChart(label,title,series);
		  		  });
	  }
		 watch(prop.parameter,(val)=>{loadChartData() });
		 watch(isDark,(val)=>{loadChartData() });
           //返回数据
		 return{
          adData,chartTitle
		 }
		 
	 },
	  
}
</script>
<style>
.ch-da-toggle {margin-top: 8px;}
.ch-da-toggle .el-checkbox{margin-left:auto;}
.ch-da-toggle .el-space{display:flex}
.ch-da-toggle .el-space__item{width:100%}
.ch-da-toggle span{color:var(--el-border-color-base)}
</style>
