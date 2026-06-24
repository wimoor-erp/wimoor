<template>
  <div>
    <el-space style="margin-bottom:8px;">
      <span class="pag-title">销量对比</span>
      <span class="pag-small-Extra">{{refreshtime}}</span>
    </el-space>
    <div class="pag-radius-bor">
      <div id="comparechart" style="height:147px;width:100%"></div>
	  <div class=""></div>
    </div>
 </div>
</template>
<script>
import * as echarts from "echarts";
import { ref,reactive,onMounted,watch } from 'vue'
export default {
  name: "Comparechart",
  components: {},
  props:["qtydata"],
  setup(props,context){
    let refreshtime=ref();
	watch(props.qtydata,(val)=>{
		initChartData();
		});
	 onMounted(() => {
		initChartData(); 
		})
		function initChartData(){
			if(props.qtydata&&props.qtydata.updatetime){
				let datas=[];
				if(props.qtydata&&props.qtydata.updatetime){
					refreshtime.value=new Date(props.qtydata.updatetime).format("yyyy-MM-dd hh:mm:ss");
					let mydata=JSON.parse(props.qtydata.mapdata);
					datas.push(mydata[0].quantity);
					datas.push(mydata[0].OldLast);
					datas.push(mydata[0].OldWeek);
				}
				generalChart(datas);
			}
			
		}
	function generalChart(datas) {
	  var myChart = echarts.init(document.getElementById("comparechart"));
	  var option = {
	    tooltip: {
	      trigger: "axis",
	      axisPointer: {
	        type: "shadow"
	      }
	    },
	    legend: {
			top:0,
	      itemGap: 16,
	      icon: "circle",
	      itemWidth: 6,
	      itemHeight: 6
	    },
	    grid: {
			  bottom:8,
			  top:8,
	      left: 0,
	      right: 8,
	      containLabel: true
	    },
	    xAxis: {
	      type: "value",
	      axisLabel: {
	        color: "#acb0b9"
			},
			splitLine:{
						lineStyle:{
						color:"rgba(170,170,170,0.2)"
						}
					}
	    },
	    yAxis: {
	      type: "category",
	      data: ["上周同期", "前日", "昨日"],
	      axisLine: {
	        show: false
	      },
	      axisTick: {
	        show: false
	      }
	    },
	    color: "#ff7315",
	    series: [
	      {
			  barWidth:8,
	        type: "bar",
			  data: datas,
			  itemStyle:{
				  borderRadius:4,
			  }
	      }
	    ]
	  };
	
	  myChart.setOption(option);
	  window.addEventListener("resize", () => {
	    myChart.resize();
	  });
	}
    return {refreshtime};
  },
  
};
</script>
<style>
</style>