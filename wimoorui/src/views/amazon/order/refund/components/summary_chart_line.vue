<template>
	<div class="amz-order-refund-summary-chart">
 <el-col :span='24'>
		  <div  >
		<div class='ch-da-toggle' @click.stop="emptyHandle">
			<el-space :size="16"  >
					    <el-card v-for='(h,index) in summarysaleData' :key='index'  shadow="hover" :class="h.checked?'data-group active ':'data-group'" >
							   <div  @mouseenter.stop="chartLineLabelShowHide(h.label)" @mouseleave.stop="chartLineLabelShowHide('#')" >
				               <div style='font-size:12px;color:#666' class="flex-center-between">
							    {{h.label}}   
								<el-checkbox  v-model="h.checked"  @change="lineCheckChange"  label="" size="large">
								</el-checkbox>
							   </div>
				               <div class='pag-data-num cell-t-8'>{{h.data}}{{h.suffix}}</div>
							   </div>
				            </el-card>
			</el-space>
		 </div>
			  <div id='salechart' style='height:250px;width:100%'>
			  </div>
		  </div>
	 </el-col>
	 </div>
</template>
<script setup>
import * as echarts from 'echarts'
import { ref,reactive,onMounted,watch,nextTick } from 'vue'
import orderListApi from "@/api/amazon/order/orderListApi.js";
import { useDark, useToggle } from "@vueuse/core";
      let summarysaleData =ref([ {    label:'退货数量',
									  data:'0',
									  checked:true,
									  suffix:"",
								  },{ label:'退款金额',
									  data:'0',
									  checked:false,
									  suffix:"",
								  },{
									  label:'退货率',
									  data:'0',
									  checked:false,
									  suffix:"%",
								  },{
									  label:'销量',
									  data:'0',
									  checked:false,
									  suffix:"",
								  },
								]);
	  let chartTitle=ref("");
	  let queryParams=ref({});
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
	  function emptyHandle(){
		  
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
	   var myChart =null;
	  function generalChart(series,label,titles){
		  if (myChart != null && myChart != "" && myChart != undefined) {
		  	myChart.dispose();//销毁
		  }
		    myChart = echarts.init(document.getElementById('salechart'));
			var color=["#ff7315","#3c8bfa","#67c23a","#de51fa"];
	  	  var option = {
	           tooltip : {
	  			trigger : 'axis',
	  		     },
				color:color,
	            legend: {
	  				data:titles,
	  				right:0,
	  				top:16,
	  				icon: "circle",
	  				itemWidth:6,
	  				itemHeight:6,
	  				show:true,
					selected:{'退货数量':true,"退款金额":false,"退货率":false, "销量":false }
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
	  	   
	  	   myChart.setOption(option,true);
	  	   window.addEventListener('resize',()=>{
	  		   myChart.resize();
	  	   })
	  }
	 function loadChartData(){
		    orderListApi.selectReturnsSummaryByDay(queryParams.value).then(async (res)=>{
		 				 let label=res.data.label;
		 				 let returnqty=res.data.returnqty;
		 				 let returnItemPrice=res.data.returnItemPrice;
						 let rateqty=res.data.rateqty;
						 let quantity=res.data.quantity;
		 				 let titles=['退货数量','退款金额','退货率','销量' ];
		 				 let series=[];
						 summarysaleData.value[0].data=res.data.returnqtySummary;
						 summarysaleData.value[0].checked=true;
						 summarysaleData.value[1].data=res.data.returnItemPriceSummary;
						 summarysaleData.value[1].checked=false;
						 summarysaleData.value[2].data=res.data.rateSummary;
						 summarysaleData.value[2].checked=false;
						 summarysaleData.value[3].data=res.data.quantitySummary;
						 summarysaleData.value[3].checked=false;
		 				 var serieQuantity={
		 				           smooth: 0.5,
		 				           name: '退货数量',
		 				           type: 'line',
		 				 		   data:returnqty,
								   areaStyle: {opacity: 0.1},
		 				       };
						 var serierateqty={
		 				           smooth: 0.5,
		 				           name: '退货率',
		 				           type: 'line',
		 				 		   data:rateqty,
								   areaStyle: {opacity: 0.1},
		 				       };
						var seriequantity={
							             smooth: 0.5,
							             name: '销量',
							             type: 'line',
							   			 data:quantity,
										 areaStyle: {opacity: 0.1},
							         };
		 				 var serieoItemPrice={
		 					             smooth: 0.5,
		 					             name: '退款金额',
		 					             type: 'line',
		 					   			 data:returnItemPrice,
										 areaStyle: {opacity: 0.1},
		 					         };
		 				series.push(serieQuantity); 
		 				series.push(serieoItemPrice); 
						series.push(serierateqty); 
						series.push(seriequantity); 
		 				nextTick(()=>{
		 					generalChart(series,label,titles);
		 				})
		 				
		 			 });
		 		 
	 }
	 function show(params){
		 queryParams.value=params;
		 loadChartData();
	 }
	defineExpose({
	  show
	}) 
</script>
<style >
.amz-order-refund-summary-chart .ch-da-toggle .el-checkbox{margin-left:auto;}
.amz-order-refund-summary-chart .ch-da-toggle .el-space{display:flex}
.amz-order-refund-summary-chart .ch-da-toggle .el-space__item{width:100%}
.amz-order-refund-summary-chart .ch-da-toggle span{color:var(--el-border-color-base)}
.amz-order-refund-summary-chart .mcheckbox{display: flex;}
.amz-order-refund-summary-chart .mcheckbox .data-group {
    
    justify-content: space-between;
}
.amz-order-refund-summary-chart .mcheckbox .el-checkbox__label{
	float:left;
}
.amz-order-refund-summary-chart .orange{
	color:#FF6700;
}
.amz-order-refund-summary-chart  .el-card__body{
	padding-left:20px;
	padding-right:20px;
	padding-top:5px;
	padding-bottom:15px;
}
.amz-order-refund-summary-chart{
	margin-bottom:20px;
}
.amz-order-refund-summary-chart  .el-card.active{
   border: 1px solid var(--el-color-primary);
}
.amz-order-refund-summary-chart .warning{
	color:#E6A23C;
}
.amz-order-refund-summary-chart .green{
	color:#67C23A;
}
</style>
