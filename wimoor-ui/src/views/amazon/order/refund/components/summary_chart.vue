<template>
	 <SummaryChartLine ref="summaryChartLineRef"></SummaryChartLine>
	 <SummaryChartPie ref="summaryChartPieRef"></SummaryChartPie>
</template>
<script setup>
import * as echarts from 'echarts'
import { ref,reactive,onMounted,watch,nextTick } from 'vue'
import SummaryChartLine from "./summary_chart_line.vue";  
import SummaryChartPie from "./summary_chart_pie.vue";
const summaryChartLineRef=ref();
const summaryChartPieRef=ref();
function show(param){
	summaryChartLineRef.value.show(param);
	summaryChartPieRef.value.show(param);
}
	defineExpose({
	  show
	}) 
</script>
<style >

</style>
