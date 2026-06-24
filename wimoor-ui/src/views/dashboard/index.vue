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
   <Helper name="团队公告" />
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
 <!-- 系统管理员欢迎页面 -->
<div v-else-if="homeAuthority==3" class="admin-welcome-container">
	 <div class="welcome-header">
		 <el-icon class="welcome-icon"><Setting /></el-icon>
		 <h1 class="welcome-title">欢迎使用 Wimoor 系统管理员控制台</h1>
	 </div>
	 
	 <div class="admin-quick-actions">
		 <el-row :gutter="16">
			 <el-col :span="8">
				 <el-card  shadow="always" class="action-card">
					 <div class="card-header">
						 <el-icon class="card-icon system"><Setting /></el-icon>
						 <h3 class="card-title">系统管理</h3>
					 </div>
					 <p class="card-desc">管理系统配置、菜单权限和定时任务</p>
					 <el-space    :size="48" class="card-buttons" direction="vertical">
						 <el-button type="warning" @click="goPage(menu)">
							 <el-icon><Menu /></el-icon> 菜单管理
						 </el-button>
						 <el-button type="info" @click="goPage(dict)">
							 <el-icon><Document /></el-icon> 字典管理
						 </el-button>
						 <el-button type="danger" @click="goPage(task)">
							 <el-icon><Timer /></el-icon> 系统任务
						 </el-button>
						 <el-button type="warning" @click="goPage(notify)">
							 <el-icon><Bell /></el-icon> 公告发布
						 </el-button>
					 </el-space>
				 </el-card >
			 </el-col>
			 
			 <el-col :span="8">
				 <el-card  shadow="always" class="action-card">
					 <div class="card-header">
						 <el-icon class="card-icon user"><User /></el-icon>
						 <h3 class="card-title">用户管理</h3>
					 </div>
					 <p class="card-desc">管理用户账号、角色权限和客户信息</p>
					 <el-space :size="12"  direction="vertical"	 class="card-buttons">
						 <el-button type="primary" @click="goPage(subuser)">
							 <el-icon><User /></el-icon> 用户管理
						 </el-button>
						 <el-button type="success" @click="goPage(ruleAuth)">
							 <el-icon><Key /></el-icon> 角色管理
						 </el-button>
						 <el-button type="info" @click="goPage(customer)">
							 <el-icon><OfficeBuilding /></el-icon> 客户管理
						 </el-button>
						 <el-button type="warning" @click="goPage(customerOrder)">
							 <el-icon><ShoppingCart /></el-icon> 客户订单
						 </el-button>
						 <el-button type="primary" @click="goPage(tariffpackages)">
							 <el-icon><Wallet /></el-icon> 套餐管理
						 </el-button>
						 <el-button type="success" @click="goPage(online)">
							 <el-icon><Connection /></el-icon> 在线客户
						 </el-button>
					 </el-space>
				 </el-card >
			 </el-col>
			 
			 <el-col :span="8">
				 <el-card  shadow="always" class="action-card">
					 <div class="card-header">
						 <el-icon class="card-icon dev"><Cpu /></el-icon>
						 <h3 class="card-title">开发管理</h3>
					 </div>
					 <p class="card-desc">管理代码生成、API文档和数据迁移</p>
					 <el-space :size="94"  direction="vertical"	 class="card-buttons">
						 <el-button type="success" @click="goPage(gen)">
							 <el-icon><Cpu /></el-icon> 代码生成
						 </el-button>
						 <el-button type="info" @click="goPage(api)">
							 <el-icon><DocumentCopy /></el-icon> 文档API
						 </el-button>
						 <el-button type="warning" @click="goPage(movedata)">
							 <el-icon><Switch /></el-icon> 数据迁移
						 </el-button>
					 </el-space>
				 </el-card >	
			 </el-col>
		 </el-row>
	 </div>
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
import {Setting, Warning, CircleCheck, User as UserIcon, Monitor, Key, Menu, Document, Timer, Bell, OfficeBuilding, Cpu, Wallet, Connection, Switch, DocumentCopy} from '@element-plus/icons-vue'
import summaryDataApi from '@/api/amazon/summary/summaryDataApi.js'
import groupApi from '@/api/amazon/group/groupApi.js';
import userApi from '@/api/sys/admin/userApi.js';
import { ref,reactive,onMounted,watch } from 'vue'
import { useRouter } from 'vue-router';
import Datepicker from '@/components/header/datepicker.vue';
import Helper from '@/components/header/helper.vue';
export default{
	 name: '主页',
	 components: {Help,UpSmall,DownSmall,Salechart,Piechart,Comparechart,
	 Stockchart,Adchart,Storeshow,Personrank,Goodsrank,Notice,Service,Datepicker,
	 Setting, Warning, CircleCheck, UserIcon, Monitor, Key, Menu, Document,
	 Timer, Bell, OfficeBuilding, Cpu, Wallet, Connection, Switch, DocumentCopy
	 },
     setup(){
      const router = useRouter();
      const subuser = {name:'用户管理', url:'/sys/subuser'};
      const ruleAuth = {name:'角色管理', url:'/sys/ruleAuth'};
      const menu = {name:'菜单管理', url:'/sys/menu'};
      const dict = {name:'字典管理', url:'/sys/dict'};
      const task = {name:'系统任务', url:'/sys/task'};
      const notify = {name:'公告发布', url:'/sys/notify'};
      const customer = {name:'客户管理', url:'/sys/customer'};
      const customerOrder = {name:'客户订单', url:'/sys/customer/order'};
      const gen = {name:'代码生成', url:'/sys/gen'};
      const tariffpackages = {name:'套餐管理', url:'/sys/tariffpackages'};
      const online = {name:'在线客户', url:'/sys/online'};
      const movedata = {name:'数据迁移', url:'/sys/movedata'};
      const api = {name:'文档API', url:'/doc/api/index'};

      function goPage(item){
        router.push({
          path: item.url,
          query: {
            title: item.name,
            path: item.url,
          }
        })
      }

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
		await userApi.getInfo().then(res=>{
			 if(res&&res.data&&res.data.usertype=='admin'){
				 homeAuthority.value=3;
			 }else{
				 userApi.limitData("home").then(res=>{
					 if(res&&res.data=="true"){
						 homeAuthority.value=2;
					 }else{
						 homeAuthority.value=1;
						 getGroupData();
						 getSummaryData();
					 }
					 loading.value=false;
				 });
			 }
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
		  subuser,
		  ruleAuth,
		  customerOrder,
		  menu,
		  dict,
		  task,
		  notify,
		  customer,
		  gen,
		  tariffpackages,
		  online,
		  movedata,
		  api,
		  goPage,
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
	.admin-welcome-container {
		padding: 32px;
		max-width: 1200px;
		margin: 0 auto;
	}
	.welcome-header {
		display: flex;
		align-items: center;
		justify-content: center;
		margin-bottom: 40px;
		padding: 24px;
		background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
		border-radius: 12px;
		box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
	}
	.welcome-icon {
		font-size: 48px;
		color: #fff;
		margin-right: 20px;
	}
	.welcome-title {
		font-size: 28px;
		font-weight: 600;
		color: #fff;
		margin: 0;
	}
	.admin-quick-actions {
		padding-bottom: 24px;
	}
	.action-card {
		border-radius: 8px;
		padding: 20px;
		height: 100%;
	}
 
	.card-header {
		display: flex;
		align-items: center;
		margin-bottom: 16px;
		padding-bottom: 12px;
		border-bottom: 1px solid #e4e7ed;
	}
	.card-icon {
		font-size: 28px;
		margin-right: 12px;
	}
	.card-icon.system { color: #409eff; }
	.card-icon.user { color: #67c23a; }
	.card-icon.dev { color: #909399; }
	.card-title {
		font-size: 16px;
		font-weight: 600;
		color: #303133;
		margin: 0;
	}
	.card-desc {
		font-size: 13px;
		color: #909399;
		margin: 0 0 12px 0;
		line-height: 1.5;
	}
	.card-buttons {
		display: flex;
		flex-direction: column;
		gap: 10px;
		padding: 0 4px;
	}
	.card-buttons .el-button {
		width: 300px;
		height: 44px;
		padding: 0 24px;
		border-radius: 22px;
		font-size: 14px;
		font-weight: 500;
		display: flex;
		align-items: center;
		justify-content: center;
		gap: 8px;
	}
	.dark .admin-welcome-container {
		background-color: #1a1a2e;
	}
	.dark .action-card {
		background-color: #16213e;
		box-shadow: 0 2px 8px rgba(0, 0, 0, 0.3);
	}
	.dark .card-title {
		color: #e0e0e0;
	}
	.dark .card-header {
		border-bottom-color: #2a2a4a;
	}
	.dark .admin-quick-actions h2 {
		color: #e0e0e0;
	}
</style>