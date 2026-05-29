<template>
  <el-col :span="6">
    <el-space style="margin-bottom:8px;">
      <span class="pag-title">市场销量</span>
      <span class="pag-small-Extra">{{chartTitle}}</span>
    </el-space>
    <div class="pag-radius-bor">
      <div id="piechart" style="height:360px;width:100%"></div>
    </div>
  </el-col>
</template>
<script>
import * as echarts from "echarts";
import { ref,reactive,onMounted,watch } from 'vue'
import summaryDataApi from '@/api/amazon/summary/summaryDataApi.js';
import { useDark, useToggle } from "@vueuse/core";
export default {
  name: "Piechart",
  props:["parameter"],
  components:{useDark},
  setup(prop,context){
    //返回数据
	let chartTitle =ref();
	const isDark = useDark();
	watch(isDark,(val)=>{ loadChart();   }  );
	watch(prop.parameter,(val)=>{  loadChart();  }  );
	function loadChart(){
		prop.parameter.bytime="Daily";
		chartTitle.value=prop.parameter.beginDate+ " ~ "+prop.parameter.endDate.substring(0,10);
		summaryDataApi.queryChartMarket(prop.parameter).then((res)=>{
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
	      subtext:'总销量',
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
	      itemWidth: 6,
	      itemHeight: 6,
		  textStyle: {
				color: isDark.value==true?"#d0d0d0":'#303133'
			  }
	    },
	    color:['#FF6700','#ff9249','#ffa76c','#ffc59e','#409eff','#66b1ff','#8cc5ff','#b3d8ff',"#67c23a","#8dd968","#ade393","#d2f7c1"],
	    series: [
	      {
	        type: "pie",
	        center: ['50%', '40%'],
	        radius: ["30%", "55%"],
	        label: {
	          show: true,
	          position: "outside",
	          color:isDark.value==true?"#d0d0d0": '#666',
	          formatter: '{d}%'
	        },
	        labelLine:{
	          minTurnAngle:120,
	        },
	        data:datas
	      }
	    ]
	  };
	
	  myChart.setOption(option);
	  window.addEventListener("resize", () => {
	    myChart.resize();
	  });
	}
 
 
    return {chartTitle	};
  },
  
};
</script>
<style>
</style>
