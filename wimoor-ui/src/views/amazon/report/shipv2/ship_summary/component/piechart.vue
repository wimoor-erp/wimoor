<template>
    <div class="pag-radius-bor ship-pie-box">
		  <h4 class=" text-left">{{name}}</h4>
      <div :id="uuid" style="height:180px;width:100%"></div>
    </div>
</template>
<script setup>
import * as echarts from "echarts";
import { ref,reactive,onMounted,watch,toRefs } from 'vue';
  import {guid,formatFloat} from '@/utils/index.js';
 let prop=defineProps({
 	  data:[],
	  name:"",
	  keyvalue:{value:""},
	  chartdata:{value:""},
 });
 let {
   data,name,keyvalue,chartdata
 } =toRefs(prop);
 const state = reactive({
 				  uuid:guid(),
 				});
 const {
   uuid,
 } = toRefs(state);
 var kong=['#FF6700','#ff9249','#ffa76c','#ffc59e'];
 var lu=  ["#67c23a","#8dd968","#ade393","#d2f7c1"];
 var hai= ['#409eff','#66b1ff','#8cc5ff','#b3d8ff'];
 
	watch(prop.data,(val)=>{ handleData(); });
	watch(prop.keyvalue,(val)=>{ handleData();});
    watch(prop.chartdata,()=>{ handleData();});
	function handleData(){
		let  datas=[];
		 // data.sort( function(a, b){ 
		 //      return parseInt(a["quantity" ]) > parseInt(b["quantity" ]) ? 1 : parseInt(a[ "quantity"]) == parseInt(b[ "quantity" ])  ? 0 : -1;
		 // });
		 let names=[];
		 let colors=[];
		 let summary=0;
		 prop.data.forEach((item)=>{
			 var quantity=item[prop.keyvalue.value];
			 if(quantity){
				 if(typeof(quantity)=='string'){
			         summary=formatFloat(summary+parseInt(quantity));
				 }else{
					   summary=formatFloat(summary+quantity);
				 }
			 }
			 if(!names.includes(item.name)){
				 names.push(item.name);
			 }
			 datas.push({name:item.name,value:quantity});
		 });
		 var kongi=0,lui=0,haii=0;
		 
		 names.forEach(itemname=>{
			if(itemname){
				if(itemname.indexOf("空")>=0){
					colors.push(kong[kongi]);
					kongi=(kongi+1)%4;
				}else if(itemname.indexOf("陆")>=0||itemname.indexOf("铁")>=0){
					colors.push(lu[lui]);
					lui=(lui+1)%4;
				}else {
					colors.push(hai[haii]);
					haii=(haii+1)%4;
				} 
			}
			
		 })
		generalChart(summary,datas,colors)
	}
		  
	function generalChart(summary,datas,colors) {
	  var myChart = echarts.init(document.getElementById(state.uuid));
	  if(myChart!=null){
	  	 myChart.clear();
	  }
	  var option = {
	    tooltip: {
	      trigger: "item",
		  formatter:  '{b}:{d}%',
		  
	    },
	    title:{
	      text:summary,
	      left:'center',
	      top:'35%',
	      subtext:'总量',
	      textStyle:{
	          fontSize:16,
	          fontFamily:"Helvetica Neue",
	          color:'#303133'
	      },
	      subtextStyle:{
	           fontSize:12,
			   color:'#999'
	      },
		  itemGap:4,
	    },
	    legend: {
	      itemGap:16,
	      bottom:0,
	      icon: "circle",
	      itemWidth: 6,
	      itemHeight: 6
	    },
	    color:colors,
	    series: [
	      {
			left:10 ,
			top:10 ,
			right: 10 ,
			bottom:0 ,  
	        type: "pie",
	        center: ['50%', '40%'],
	        radius: ["50%", "66%"],
	        label: {
	          show: true,
	          position: "outside",
	          color:'#333',
			  width:100,
	          formatter: '{d}%'
	        },
	        labelLine:{
	          minTurnAngle:120,
			  
	        },
	        data:datas,
			percentPrecision:2,
	      }
	    ]
	  };
	
	  myChart.setOption(option);
	  window.addEventListener("resize", () => {
	    myChart.resize();
	  });
	}
 onMounted(()=>{
	  if(prop.data.length>0){
		  handleData();
	  }
 })
 
 
</script>
<style>
	.ship-pie-box{
		width:235px;
		margin:8px 8px;
	}
	@media screen and (max-width:1600px){
		.ship-pie-box{ 
			width:223px;
		} 
		}
</style>
