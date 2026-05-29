<template>
<div v-if="homeAuthority==1" style='padding:16px'>
  <el-row style='margin-bottom:16px'>
    <el-space>
      <el-select v-model="groupid" style="width: 240px" placeholder="全部店铺" @change="groupChange">
        <el-option
          v-for="item in storeData"
          :key="item.value"
          :label="item.label"
          :value="item.value"
        ></el-option>
      </el-select>
       	 <Datepicker @changedate="changedate" :days="1" />
    </el-space>
	 <div class='rt-btn-group'>
    <el-button   class='ic-btn' title='帮助文档'>
     <help theme="outline" size="16" :strokeWidth="3"/>
   </el-button>
   </div>
  </el-row>
<!-- 头部数据 -->
 <el-row style='margin-bottom:8px'> 
		 <el-space>
		 <span class='pag-title'>月销售报告</span>
		 <span class='pag-small-Extra'>更新时间：{{refreshtime}}</span>
		 </el-space>
 </el-row>
<el-row :gutter="16" style='margin-bottom:16px;'>
  <el-col :span="6" v-for='(s,index) in saleData' :key="index">
	  <div class="pag-radius-bor" >
          <div class='data-group'>
		  <div>
		  <div style='font-size:12px;color:#666'>{{s.label}}</div>
		  <div class='pag-data-num cell-t-8'>{{s.data}}</div>
		   </div>
		   <!--div class='arrow'>
			   <up-small v-show='s.data > s.lastdata' theme="outline" size="20" fill="var(--el-color-success)" :strokeWidth="3"/>
			   <down-small v-show='s.data < s.lastdata' theme="outline" size="20" fill="var(--el-color-danger)" :strokeWidth="3"/>
		   </div-->
          </div>
		 <!-- <div class='pag-small-Extra'><span>昨日 </span><span> {{s.lastdata}}</span></div> -->
	  </div>
  </el-col>
</el-row>

<el-row :gutter="16" style='margin-bottom:16px;'>
	<!-- 销售趋势 -->
	  <Salechart ref="salechart.value" :parameter="param"/>
	  <!-- 市场销量 -->
	   <Piechart :parameter="param"/>
	 <el-col :span="6">
	  <!-- 销量对比 -->
      <Comparechart :qtydata="lastqty"/>
	  <!-- 节日销售分布 -->
       <Stockchart/>
	 </el-col>
 </el-row>
 <el-row :gutter="16" style='margin-bottom:16px;'>
	   <!-- 广告表现 -->
	    <Adchart  :parameter="param"/>
	   <!--店铺绩效-->
	   <Storeshow :parameter="param"/>
	   <!--销售业绩排名-->
	   <Personrank/>
 </el-row>
  <el-row :gutter="16">
	  <!--商品销售排名 -->
     <Goodsrank :parameter="param"/>
	  <!--公告 -->
	  <Notice  :span="6"/>
	   <!--开发服务-->
	   <Service :span="6"/>
  </el-row>
 </div>
 <div v-else-if="homeAuthority==2" class='main-sty' style='flex: 1 1 0%;'  >
	 <el-row :gutter="16"  style="height: calc(100vh - 70px);">
	 <!--公告 -->
	 <Notice  :span="12"/>
	  <!--开发服务-->
	  <Service :span="12"/>
	  </el-row>
 </div>
 <div v-else class='main-sty' style='flex: 1 1 0%;'  >
 	 <el-row :gutter="16"  style="height: calc(100vh - 70px);text-align: center;justify-content: center;align-items: center;">
 	 <!--公告 -->
 	    正在加载中...
 	  </el-row>
 </div>
 </template>

<script>
import Service from "./components/service.vue";
import Notice from "./components/notice.vue";
import Goodsrank from "./components/goodsrank.vue";
import Personrank from "./components/personrank.vue";
import Storeshow from "./components/storeshow.vue";
import Adchart from "./components/adchart.vue";
import Stockchart from "./components/stockchart.vue";
import Comparechart from "./components/comparechart.vue";
import Piechart from "./components/piechart.vue";
import Salechart from "./components/salechart.vue";
import {Help,UpSmall,DownSmall} from '@icon-park/vue-next';
import {} from '@element-plus/icons-vue'
import summaryDataApi from '@/api/amazon/summary/summaryDataApi.js'
import groupApi from '@/api/amazon/group/groupApi.js';
import userApi from '@/api/sys/admin/userApi.js';
import { ref,reactive,onMounted,watch } from 'vue'
 import Datepicker from '@/components/header/datepicker.vue';
export default{
	 name: '主页',
	 components: {Help,UpSmall,DownSmall,Salechart,Piechart,Comparechart,
	 Stockchart,Adchart,Storeshow,Personrank,Goodsrank,Notice,Service,Datepicker
	 },
     setup(){
      let salechart=ref(Salechart);
	  let storeData=ref([]);
	  let groupid=ref('');
	  let homeAuthority=ref(0);
	  let lastqty=reactive({});
	  let loading=ref(true);
	  let refreshtime=ref('');
	  let saleData=ref([{
		  label:'产品数量',
		  data:'0',
		  lastdata:'0'
	  },
	  {
		  label:'月销量',
		  data:'0',
		  lastdata:'0' 
	  },
	  {
		  label:'月订单量',
		  data:'0',
		  lastdata:'0' 
	  },
	  {
		  label:'月退货量',
		  data:'0',
		  lastdata:'0' 
	  },
	  ])
	  //获取店铺列表
	  function getGroupData(){
	  	groupApi.getAmazonGroup().then((res)=>{
			storeData.value.push({"value":"","label":"全部店铺"})
	  		 res.data.forEach((item)=>{
				 storeData.value.push({"value":item.id,"label":item.name})
			 });
	  	})
	  }
	  function getSummaryData(){
		  summaryDataApi.getSummaryData().then((res)=>{
			  if(res&&res.data){
				res.data.forEach((data)=>{ 
						if(data.ftype=="main-2-pro"){
							 let lastdata=0;
							 if(data.mapdata){
								 lastdata=parseInt(data.mapdata);
							 }
							  saleData.value[0]={"label":"产品数量","data":data.value,"lastdata":lastdata} ;
											  
						} 
						if(data.ftype=="lastqty"){
							lastqty.mapdata=data.mapdata ;	
							lastqty.updatetime=data.updatetime ;	
						}
						if(data.ftype=="main-2-sales"){
							 let lastdata=0;
							 if(data.mapdata){
								lastdata=parseInt(data.mapdata);
							 }
							  refreshtime.value=new Date(data.updatetime).format("yyyy-MM-dd hh:mm:ss"); 
							  saleData.value[1]={"label":"月销量","data":data.value,"lastdata":lastdata}; 
						} 
						if(data.ftype=="main-2-orders"){
							 let lastdata=0;
							 if(data.mapdata){
								lastdata=parseInt(data.mapdata);
							 }
							  saleData.value[2]={"label":"月订单量","data":data.value,"lastdata":lastdata};
						} 
						if(data.ftype=="main-2-return"){
							 let lastdata=0;
							 if(data.mapdata){
								lastdata=parseInt(data.mapdata);
							 }
							 saleData.value[3]={"label":"月退货量","data":data.value,"lastdata":lastdata};
						} 
				});
				 
				 
			  }
		  })
	  }
	  
	   
	  onMounted(async ()=>{
		await userApi.limitData("home").then(res=>{
			 if(res&&res.data=="true"){
				 homeAuthority.value=2;
			 }else{
				 homeAuthority.value=1;
				 getGroupData();
				 getSummaryData();
			 }
			 loading.value=false;
		 })
	  });
	  let param=ref({});
	  
	  function changedate(dates){
		param.value.start=dates.start;
		param.value.begin=dates.start;
		param.value.end=dates.end;
		param.value.beginDate=dates.start;
	    param.value.endDate=dates.end;
	  }
	 
	  function groupChange(){
		  param.value.groupid=groupid.value;
		   
	  }
 
	  //返回数据
	  return {
		  salechart,
		  saleData,
		  groupid,		 
		  storeData,
		  changedate,
		  groupChange,
		  param,
		  lastqty,
		  refreshtime,
		  homeAuthority,
	  }
  },
 


}
</script>

 <style>
.echart-box{height:320px;width:100%}
.arrow{margin-left:auto;}
.data-group{display:flex}
.pag-data-num{font-size:18px;font-weight:600;margin-bottom:8px;}
	.cell-t-8{
		margin-top:8px;
	}
</style>
<style scoped>
	.main-sty{
	   background-color:var(--el-color-info-lighter)
	}
	.dark .main-sty{
	   background-color:#000;
	}

</style>