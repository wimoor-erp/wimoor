<template>
	<div class="pag-radius-bor">
		<div class="flex-center-between">
			<h4>支出项</h4>
	<el-popover
	    :visible="visible"
	    placement="left"
	    :width="250"
		@show="handleFocus"
	  >
	  <template #reference>
	    <el-button size="small" @click="visible = !visible" >
	     {{dateVal}}
		  <el-icon><ArrowDown /></el-icon>
		  </el-button >
	  </template>
	      <el-radio-group v-model="dateType" @change="handleDateTypeChange">
	        <el-radio-button label="year"  >年</el-radio-button>
	        <el-radio-button label="month" >月</el-radio-button>
	      </el-radio-group>
		  <el-date-picker
		    class="m-t-16"
			ref="datepickerRef"
			@change="handleChange"
			v-model="dateVal"
			:type="dateType"
			:editable="false"
			:clearable="false"
		  />
	   
	  </el-popover>
		</div>
		<el-row>
			<el-col :span="24">
				<el-col :span="24" class="m-t-16 text-center">
				<p class="font-bold font-large">{{formatFloat(summary)}}</p>
				<p class="font-extraSmall">总金额（元）</p>
				</el-col>
		<div class="m-t-16" >
			<div id="piechart" style="height:320px;width:100%"></div>
		</div>
		</el-col>
		</el-row>
	</div>
</template>

<script setup>
	import * as echarts from "echarts";
	import { ref,reactive,onMounted,watch,toRefs} from 'vue';
	import {ArrowDown} from '@element-plus/icons-vue';
	import {dateYearFormat,dateMonthFormat,formatFloat} from '@/utils/index.js';
	import journalApi from '@/api/erp/finances/journalApi.js';
	const nowDate=dateYearFormat(new Date());
	const datepickerRef=ref();
	const state = reactive({
		datas:[ ],
		dateType:"year",
		dateVal:nowDate,
		visible:false,	
		 rowdata:{},
		 summary:0,
	})
	const {
		datas,
		visible,
		dateVal,
		dateType,
		summary,
	}= toRefs(state)
	
	function getvalue (name){
		var val = ''
		state.datas.forEach((item)=>{
			if(item.name==name){
				val = item.value
			}
		})
		return val
	}
	function generalChart(datas) {
	  var myChart = echarts.init(document.getElementById("piechart"));
	  var option = {
	    tooltip: {
	      trigger: "item"
	    },
	    legend: {
	      itemGap:16,
	      bottom:8,
	      icon: "circle",
	      itemWidth: 6,
	      itemHeight: 6,
	      formatter: function (name) {
	          return '{a|'+name+'}'+' '+getvalue (name) ;
	      },
		  textStyle: {
			rich:{
        a: {
            color: '#999',
        },
    }
		 },
		 
	    },
	    color:['#ff6d18','#409eff',"#67c23a",'#ff9249','#66b1ff','#ffa76c',"#8dd968",'#ffc59e','#b3d8ff',"#d2f7c1",'#8cc5ff',"#ade393"],
	    series: [
	      {
	        type: "pie",
			center:['50%','38%'],
	        radius: ["35%", "70%"],
	        label: {
	          show: false,
	          position: "outside",
	          color:'#666',
	          formatter: '{d}%'
	        },
	        labelLine:{
	          minTurnAngle:120,
	        },
	        data: datas,
	      }
	    ]
	  };
	
	  myChart.setOption(option);
	  window.addEventListener("resize", () => {
	    myChart.resize();
	  });
	}
 
	function loadPieChart(){
		var data={};
		data.acc=state.rowdata.id;
		if(state.dateType=='year'){
			 data.year=state.dateVal;
			 data.month="";
		}else{
			data.year=state.dateVal.split("-")[0];
			data.month=state.dateVal.split("-")[1];
		}
		state.summary=0;
		journalApi.getPieData(data).then(res=>{
			for (var val in  res.data) {
			    var mydata=res.data[val];
				 mydata.value=mydata.pay;
				 state.summary=state.summary+parseFloat(mydata.pay);
			}
			state.datas=res.data;
			generalChart(state.datas);
		})
	}
	function handleChange(val){
		state.visible = false
		formatDate(val);
		loadPieChart();
	}
	function formatDate(date){
		if(state.dateType=='year'){
			state.dateVal = dateYearFormat(date)
		}else{
			state.dateVal = dateMonthFormat(date)
		}
	}
	function handleDateTypeChange(){
		handleFocus();
		if(state.dateType=='year'){
			state.dateVal = dateYearFormat(state.dateVal)
		}else{
			if(state.dateVal.length<=4){
				state.dateVal=state.dateVal+"-"+(new Date().getMonth()+1);
			}
			state.dateVal = dateMonthFormat(state.dateVal)
		}
		 loadPieChart();
		 
	}
	function handleFocus(){
		datepickerRef.value.focus();
			
	}
	function show(item){
		state.rowdata=item;
		loadPieChart();
	}
	defineExpose({ show })
</script>

<style>
	.m-t-16{
		margin-top: 16px;
	}
</style>
