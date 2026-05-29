<template>
	<div class="main-sty">
		<div class="con-header">
	<el-tabs v-model="activeName"  @tab-change="handleClick">
	  <el-tab-pane v-for="item in orderState" :label="item.label" :name="item.name" :key="item.name">
		  </el-tab-pane>
	</el-tabs>
		    <el-row>
               <el-space>
               	<el-button type="primary" class="im-but-one" @click="handleAdd">
               	  <plus theme="outline" size="18" fill="#fff" :strokeWidth="4"/>
               	  <span>添加盘点单</span>
               	</el-button>
               	<Warehouse @changeware="getWarehouse" />
               	<Datepicker ref="datepickers" @changedate="changedate" />
               	<el-input v-model="searchKeywords" @input="handleQuery" placeholder="单据编码"  >
               		<template #suffix>
               		<el-icon style="font-size:16px;" class="el-input__icon"><search/></el-icon>
               		 </template> 
               	</el-input>
               </el-space>
		     <div class='rt-btn-group'>
		  		<el-button   class='ic-btn' title='帮助文档'>
		  		 <help theme="outline" size="16" :strokeWidth="3"/>
		  	   </el-button>
		     </div>
		  </el-row>
	  
	</div>
	<div class="con-body">
		<Table ref="tableRef"/>
	</div>	
	</div>
</template>
<script>
    export default{ name:"库存盘点" };
</script>
<script setup>
	import {MenuUnfold,Plus,SettingTwo,Help,Copy,MoreOne} from '@icon-park/vue-next';
	import {Search,ArrowDown,UploadFilled} from '@element-plus/icons-vue'
	import { useRoute,useRouter } from 'vue-router'
	import Warehouse from '@/components/header/warehouse.vue';
	import Datepicker from '@/components/header/datepicker.vue';
	import { ref,reactive,onMounted,watch,toRefs } from 'vue'
	import Table from"./components/table.vue";
	import stocktakingApi from '@/api/erp/inventory/stocktakingApi.js';
	const router = useRouter()
	let tableRef=ref();
	const state = reactive({
		activeName:'',
		orderState:[{
			label:"全部订单",name:'',
		},
		{
			label:"进行中",name:'1',
		},
		{
			label:"已完成",name:'0',
		},
		],
		queryParam:{},
		isload:true,
		searchKeywords:"",
	})
	const {
	activeName,
	orderState,	
	queryParam,
	isload,
	searchKeywords
	}= toRefs(state)
	function handleAdd(){
		router.push({
			path:"/e/w/s",
			query:{
				title:'添加盘点单',
				path:"/e/w/s",
			},
		})
	}
	function handleQuery(){
		state.queryParam.isworking=state.activeName;
		state.queryParam.search=state.searchKeywords;
		if(!state.queryParam.warehouseid){
			state.queryParam.warehouseid="";
		}
		tableRef.value.load(state.queryParam);
		if(state.isload==true){
			state.isload=false;
		}
	}
	function getWarehouse(wid){
		state.queryParam.warehouseid=wid;
		handleQuery();
	}
	//日期改变
	function changedate(dates){
		state.queryParam.fromDate=dates.start;
		state.queryParam.toDate=dates.end;
		if(state.isload==false){
			handleQuery();
		}
	}
	function handleClick(){
		state.queryParam.isworking=state.activeName;
		handleQuery();
	}
</script>

<style>
</style>
