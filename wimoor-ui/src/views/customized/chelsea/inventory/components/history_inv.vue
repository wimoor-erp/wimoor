<template>
	<el-dialog 
	title="库存详情"
	v-model="visible">
	    <el-radio-group v-model="radio">
	      <el-radio-button label="近 7 天" value="1" />
	      <el-radio-button label="近 15 天" value="2" />
	      <el-radio-button label="近 30 天" value="3" />
	    </el-radio-group>
		<div id='invChart' style='height:299px;width:100%'>
		 
		</div>
	</el-dialog>
</template>

    <script setup>
		import { ref,reactive,onMounted,toRefs, nextTick,} from 'vue'
		import * as echarts from 'echarts'
		const state = reactive({
			visible:false,
			radio:'近 7 天',
		})
		const {
			visible,
			radio,
		
		}=toRefs(state)
		defineExpose({
			show,
		})
		
		function getChart(){
			var myChart = echarts.init(document.getElementById('invChart'));
			var option = {
				  legend:{
					 bottom:8,
					 icon: "circle",
					 itemWidth: 6,
					 itemHeight: 6,
				  },
				  xAxis: {
					type: 'category',
					data: ['06/01', '06/02', '06/03', '06/04', '06/05', '06/06', '06/07'],
					boundaryGap:false,
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
				  series: [
					{
					  data: [120, 200, 150, 80, 70, 110, 130],
					  color:'#ff7315',
					   name: 'ka111d',
					  type: 'bar'
					}
				  ]
				};
				myChart.setOption(option);
				window.addEventListener('resize',()=>{
					  		   myChart.resize();
				})
		}
		
		function show(id){
			state.visible = true;
			nextTick(()=>{
				getChart();
			})
		}
	</script>

<style>
	
</style>