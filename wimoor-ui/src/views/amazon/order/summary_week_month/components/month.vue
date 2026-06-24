<template>
	<div class="m-b-8 ">
			<h3>订单趋势</h3>
	</div>
	<el-tabs
	    v-model="activeName"
	    type="card"
	    class="demo-tabs"
	    @tab-click="handleClick"
	  >
	    <el-tab-pane label="销量" name="first">
			<div class="flex-center">
				<el-space class="font-small" :size="24"><span>销量:15666</span><span>环比上期：<span class="font-red">+151866
				 <el-icon><caret-bottom /></el-icon></span>
				</span></el-space>
				<el-date-picker
				        v-model="value2"
				        type="daterange"
				        unlink-panels
				        range-separator="至"
				        start-placeholder="开始时间"
				        end-placeholder="结束时间"
				        :shortcuts="shortcuts"
				        :size="size"
				      />
			</div>
			<div id='salechart' class="chart-h">
			 
			</div>
		</el-tab-pane>
	    <el-tab-pane label="订单量" name="second">订单量</el-tab-pane>
	  </el-tabs>
</template>

<script>
	import {Search,ArrowDown,CaretBottom} from '@element-plus/icons-vue'
	import {MenuUnfold,Plus,SettingTwo,Help,Copy,MoreOne,ChartPie} from '@icon-park/vue-next';
	import {h, ref ,watch,reactive,onMounted} from 'vue'
	import * as echarts from 'echarts'
	export default{
		name:'Header',
		components:{
			CaretBottom,
		},
		setup(){
			let activeName = ref("first")
			onMounted(()=>{
				saleChart()
			})
			function saleChart(){
				var myChart = echarts.init(document.getElementById('salechart'))
				var option = {
				     tooltip : {
							trigger : 'axis',
						},
				   
				      xAxis: {
								boundaryGap:false,
								data: ["02-11","02-12","02-13","02-14","02-15","02-16","02-17"],
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
								right:48,
								left:32,
				                bottom:32,
								top:32,
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
				      series: [{
				          smooth: 0.5,
				          name: '销量',
				          type: 'line',
						  label: {
						        show: true
						      },
							markLine: {
							          data: [{ type: 'average', name: '平均' }]
							        },
							     
								data: [15, 25, 30, 20, 18, 24,18],
								lineStyle:{
									color:'#FF6700',
								},
								itemStyle:{
									color:'#FF6700',
								}
				      }]
				}
				 
				 myChart.setOption(option);
				 window.addEventListener('resize',()=>{
						   myChart.resize();
				 })
			}
			return{
				saleChart,
				activeName,
			}
		}
		}
</script>

<style scoped>
	.flex-center{
		display: flex;
		justify-content: space-between;
		align-items: center;
	}
	.font-red{
		color:var(--el-color-danger) ;
	}
	.chart-h{
		width: 100%;
		height:calc(100vh - 480px);
		min-height:300px;
	}
</style>
