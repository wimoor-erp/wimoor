<template>
  <el-col  >
    <div  >
      <div id="piechart" style="height:360px;width:70%"></div>
    </div>
  </el-col>
</template>
<script setup>
import * as echarts from "echarts";
import { ref,reactive,onMounted,watch } from 'vue'
	import orderListApi from "@/api/amazon/order/orderListApi.js";
import { useDark, useToggle } from "@vueuse/core";
    //返回数据
	let chartTitle =ref();
	const isDark = useDark();
	let queryParam=ref({});
	watch(isDark,(val)=>{ loadChart();   }  );
	function show(param){
		queryParam.value=param;
		loadChart();
	}
	function loadChart(){
		orderListApi.selectReturnsSummaryByType(queryParam.value).then((res)=>{
							let  datas=[];
							res.data.sort( function(a, b){ 
							      return parseInt(a["quantity" ]) > parseInt(b["quantity" ]) ? 1 : parseInt(a[ "quantity"]) == parseInt(b[ "quantity" ])  ? 0 : -1;
							 });
							
							 let summary=0;
							 res.data.forEach((item)=>{
								 summary=summary+item.quantity;
								 datas.push({name:item.name,value:item.quantity});
							 });
							generalChart(summary,datas)
		});
	}
	function generalChart(summary,datas) {
	  var myChart = echarts.init(document.getElementById("piechart"));
	  var option = {
	    tooltip: {
	      trigger: "item"
	    },
	    title:{
	      text:summary,
	      left:'center',
	      top:'35%',
	      subtext:'总退货',
	      textStyle:{
	          fontSize:16,
	          fontFamily:"Helvetica Neue",
	          color:isDark.value==true?"#d0d0d0":'#303133'
	      },
	      subtextStyle:{
	           fontSize:12,
			   color: isDark.value==true?"#d0d0d0":'#303133'
	      },
	    },
	    legend: {
	      itemGap:16,
	      bottom:8,
	      icon: "circle",
		  type: 'scroll',
		  orient: 'vertical',
		  right: 10,
		  top: 5,
		  bottom: 5,
	      itemWidth: 6,
	      itemHeight: 6,
		  textStyle: {
				color: isDark.value==true?"#d0d0d0":'#303133'
			  }
	    },
		  tooltip: {
		    trigger: 'item',
		    formatter: '{a} <br/>{b} : {c} ({d}%)'
		  },
	    color:["#d2f7c1","#ade393","#8dd968","#67c23a",
		       '#b3d8ff','#8cc5ff','#66b1ff','#409eff',
			   '#ffc59e','#ffa76c','#ff9249','#FF6700' ],
	    series: [
	      {
	        type: "pie",
			 name: '退货量',
	        center: ['50%', '40%'],
	        radius: ["30%", "55%"],
	        label: {
	          show: true,
	          position: "outside",
	          color:isDark.value==true?"#d0d0d0": '#666',
	     
	        },
	        labelLine:{
	          minTurnAngle:120,
	        },
	        data:datas
	      }
	    ]
	  };
	
	  myChart.setOption(option,true);
	  window.addEventListener("resize", () => {
	    myChart.resize();
	  });
	}
 defineExpose({
   show
 }) 
 
</script>
<style>
</style>
