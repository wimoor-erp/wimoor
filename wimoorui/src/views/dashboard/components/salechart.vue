<template>
 <el-col :span='12'>
		  <el-space style='margin-bottom:8px;'>
		    <span class='pag-title'>销售趋势</span><span class='pag-small-Extra'>{{chartTitle}}</span>
		  </el-space>
		  <div class='pag-radius-bor'>
		<div class='ch-da-toggle'>
			<el-space :size="16" spacer="|">
					    <div v-for='(h,index) in summarysaleData' :key='index' class="data-group">
							   <div >
				               <div style='font-size:12px;color:#666'>
							    {{h.label}} 
							   </div>
				               <div class='pag-data-num cell-t-8' @mouseenter="chartLineLabelShowHide(h.label)" @mouseleave="chartLineLabelShowHide('#')" >{{h.data}}</div>
							   </div>
							   <el-checkbox  v-model="h.checked"  @change="lineCheckChange"  label="" size="large"></el-checkbox>
				            </div>
					 
			  
			       
			</el-space>
		 </div>
			  <div id='salechart' style='height:299px;width:100%'>
               
			  </div>
		  </div>
	 </el-col>
</template>
<script>
import * as echarts from 'echarts'
import { ref,reactive,onMounted,watch,nextTick } from 'vue'
import summaryDataApi from '@/api/amazon/summary/summaryDataApi.js';
import { useDark, useToggle } from "@vueuse/core";
export default{
	 name: 'Salechart',
	 props:["parameter"],
	 components:{useDark,},
	 setup(prop,context){
      let summarysaleData =ref([ {    label:'销量',
									  data:'0',
									  checked:true,
								  },{ label:'订单量',
									  data:'0',
									  checked:false,
								  },{
									  label:'退货量',
									  data:'0',
									  checked:false,
								  },
								]);
	  let chartTitle=ref("");
	  let checkboxGroup=ref(['销量']);
	  
	  
	  function lineCheckChange(){
		  	  var myChart=echarts.getInstanceByDom(document.getElementById('salechart'));
		      var option=myChart.getOption();
		      summarysaleData.value.forEach((item)=>{
				  if(item.checked){
					   option.legend[0].selected[item.label]=true;
				  }else{
					   option.legend[0].selected[item.label]=false;
				  }
			  })
		  	myChart.setOption(option);
	  }
	  function chartLineLabelShowHide(label){
	  	   var myChart=echarts.getInstanceByDom(document.getElementById('salechart'));
	       if(myChart&&myChart.getOption){
			   var option=myChart.getOption();
			   
			      option.series.forEach((series,index)=>{
			   	   	      	  if(option.legend[0].selected[label]&&label==series.name){
			   	   	      		  series.itemStyle={
			   	   	      	    			normal : {
			   	   	      	    				label : {
			   	   	      	    					show : true,
			   	   	      	    					textStyle : {
			   	   	      	    						color : '#333',
			   	   	      	    					},
			   	   	      	    					formatter:function(params){return params.value}
			   	   	      	    				},
			   	   	      	    				lineStyle:{
			   	    						width:5
			   	    					},
			   	   	      	    		
			   	   	      	    		}};
			   	   	      	  }else{
			   	   	      		  series.itemStyle={
			   	   	    	    			normal : {
			   	   	    	    				label : {
			   	   	    	    					show : false,
			   	   	    	    					textStyle : {
			   	   	    	    						color : '#333',
			   	   	    	    					},
			   	   	    	    					formatter:function(params){return params.value}
			   	   	    	    				},
			   	   	    	    				lineStyle:{
			   	   	      						width:2
			   	   	      					},
			   	   	    	    		
			   	   	    	    		}};
			   	   	      	  }
			   	    
			      })
			   myChart.setOption(option);
		   }
	  }
	  function generalChart(series,label,titles){
		  var myChart = echarts.init(document.getElementById('salechart'));
	  	  var option = {
	           tooltip : {
	  			trigger : 'axis',
	  		     },
	            legend: {
	  				data:titles,
	  				right:0,
	  				top:16,
	  				icon: "circle",
	  				itemWidth:6,
	  				itemHeight:6,
	  				show:true,
					selected:{'销量':true,"订单量":false,"退货量":false}
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
	            series: series
	  	  }
	  	   
	  	   myChart.setOption(option);
	  	   window.addEventListener('resize',()=>{
	  		   myChart.resize();
	  	   })
	  }
	 function loadChartData(){
		 		      prop.parameter.bytime="Daily";
		 			  chartTitle.value=prop.parameter.beginDate+ " ~ "+prop.parameter.endDate.substring(0,10);
		 		  summaryDataApi.queryChartSales(prop.parameter).then(async (res)=>{
		 				 let label=res.data.labels;
		 				 let quantity=res.data.quantityList;
		 				 let ordernumber=res.data.orderNumberList;
		 				 let mres=await summaryDataApi.queryChartReturn(prop.parameter);
		 				 let returnOrder=mres.data.quantityList;
		 				 let titles=['销量','订单量','退货量'];
		 				 let series=[];
		 				 var serieQuantity={
		 				           smooth: 0.5,
		 				           name: '销量',
		 				           type: 'line',
		 				 				data:quantity,
		 				 				lineStyle:{
		 				 					color:'#FF6700',
		 				 				},
		 				 				itemStyle:{
		 				 					color:'#FF6700',
		 				 				}
		 				       };
		 				 var serieoOrdernumber={
		 					             smooth: 0.5,
		 					             name: '订单量',
		 					             type: 'line',
		 					   				data:ordernumber,
		 					   				lineStyle:{
		 					   					color:'#E6A23C',
		 					   				},
		 					   				itemStyle:{
		 					   					color:'#E6A23C',
		 					   				}
		 					         };
		 			      var serieReturn={
		 									 smooth: 0.5,
		 									 name: '退货量',
		 									 type: 'line',
		 										data:returnOrder,
		 										lineStyle:{
		 											color:'#67C23A',
		 										},
		 										itemStyle:{
		 											color:'#67C23A',
		 										}
		 								 };
		 				series.push(serieQuantity); 
		 				series.push(serieoOrdernumber); 
		 				series.push(serieReturn); 
		 				nextTick(()=>{
		 					generalChart(series,label,titles);
		 				})
		 				
		 			 });
		 			summaryDataApi.queryChartSalesSummary(prop.parameter).then(async (res)=>{ 
		 				let mres=await summaryDataApi.queryChartReturnSummary(prop.parameter);
		 				if(res.data&&res.data.quantity){
		 					summarysaleData.value[0].data=res.data.quantity;
		 				}else{
		 					summarysaleData.value[0].data=0;
		 				}
		 				if(res.data&&res.data.ordernumber){
		 					summarysaleData.value[1].data=res.data.ordernumber;
		 				}else{
		 					summarysaleData.value[1].data=0;
		 				}
		 				if(mres.data&&mres.data.quantity){
		 					summarysaleData.value[2].data=mres.data.quantity;
		 				}else{
		 					summarysaleData.value[2].data=0;
		 				}
		 				 
		 			});
	 }
	watch(prop.parameter,(val)=>{ loadChartData();  });
           //返回数据
		 return{
          summarysaleData,chartTitle,checkboxGroup,lineCheckChange,chartLineLabelShowHide
		 }
		 
	 },
	  
}
</script>
<style>
.ch-da-toggle .el-checkbox{margin-left:auto;}
.ch-da-toggle .el-space{display:flex}
.ch-da-toggle .el-space__item{width:100%}
.ch-da-toggle span{color:var(--el-border-color-base)}
.mcheckbox{display: flex;}
.mcheckbox .data-group {
    
    justify-content: space-between;
}
.mcheckbox .el-checkbox__label{
	float:left;
}
.orange{
	color:#FF6700;
}
.warning{
	color:#E6A23C;
}
.green{
	color:#67C23A;
}
</style>
