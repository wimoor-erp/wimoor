<template>
 
			 <div class="con-header" >
			 	  <el-row>
			 	    <el-space >
			 	    <MarketCountry @change="getCountry"  ref="marketCountryRef" />
					<div class="text-red font-small">佣金以美国为主，ETC为美国没有的分类，但是其某几个国家有</div>
			 	   </el-space>
			 	   <div class='rt-btn-group'>
			 		   <el-button @click.stop="handleAdd" type="primary"  >添加</el-button>
			 	   <el-button class='ic-btn'  title='列配置'>
			 	      <setting-two theme="outline" size="16"  :strokeWidth="3"/>
			 	   </el-button>
			 	    <el-button   class='ic-btn' title='帮助文档'>
			 	     <help theme="outline" size="16" :strokeWidth="3"/>
			 	   </el-button>
			 	   </div>
			 	</el-row>
			 	</div>
			 	
	 <el-row>
			 <GlobalTable ref="globalTable" :tableData="tableData"  @loadTable="loadTableData" :stripe="true"  style="width: 100%;margin-bottom:16px;">
			   <template #field>
			 	 <el-table-column prop="id" label="ID"  sortable='custom'  width="80">
			 		 <template #default="scope">
			 			 <el-input v-if="scope.row.edit" v-model="scope.row.id"  ></el-input>
			 			 <div v-else>{{scope.row.id}}</div>
			 		  </template>
			 	  </el-table-column>
			 	  <el-table-column prop="parentId" label="父ID"   width="80">
			 	  	<template #default="scope">
			 	  		 <el-input v-if="scope.row.edit"  v-model="scope.row.parentId" ></el-input>
			 	  		 <div v-else>{{scope.row.parentId}}</div>
			 	  	 </template>
			 	  </el-table-column>
			 	  <el-table-column prop="type" label="分类"     >
			 	  			 <template #default="scope">
			 	  			  		<el-input v-if="scope.row.edit"  v-model="scope.row.type" ></el-input>
			 	  			  		<div v-else>{{scope.row.type}}</div>
			 	  			  </template>
			 	   </el-table-column>
			 	<el-table-column prop="isMedia" label="是否媒体"  width="100" >
			 		<template #default="scope">
			 		 	<el-checkbox v-model="scope.row.ismedia" :disabled="!scope.row.edit" size="large" />
			 		 </template>
			 	</el-table-column>	
			 	 <el-table-column prop="name" label="名称"  width="80"  sortable='custom'>
			 	 	<template #default="scope">
			 	 		 <el-input v-if="scope.row.edit"  v-model="scope.row.name" ></el-input>
			 	 		 <div v-else>{{scope.row.name}}</div>
			 	 	 </template>
			 	 </el-table-column>	
			 	 <el-table-column prop="country" label="国家"   sortable='custom' width="100" >
			 	 	<template #default="scope">
			 	 		 <el-input v-if="scope.row.edit"  v-model="scope.row.country" ></el-input>
			 	 		 <div v-else>{{scope.row.country}}</div>
			 	 	 </template>
			 	 </el-table-column>	
			 	 <el-table-column prop="loweast" label="最低"   width="80">
			 	 	<template #default="scope">
			 	 		 <el-input v-if="scope.row.edit"  v-model="scope.row.loweast" ></el-input>
			 	 		 <div v-else>{{scope.row.loweast}}</div>
			 	 	 </template>
			 	 </el-table-column>	
			 	 <el-table-column prop="top1" label="第一级"   width="80">
			 	 	<template #default="scope">
			 	 		 <el-input v-if="scope.row.edit"  v-model="scope.row.top1" ></el-input>
			 	 		 <div v-else>{{scope.row.top1}}</div>
			 	 	 </template>
			 	 </el-table-column>	
			 	 <el-table-column prop="top2" label="第二级"   width="80">
			 	 	<template #default="scope">
			 	 		 <el-input v-if="scope.row.edit"  v-model="scope.row.top2" ></el-input>
			 	 		 <div v-else>{{scope.row.top2}}</div>
			 	 	 </template>
			 	 </el-table-column>	
			 	 <el-table-column prop="top3" label="第三级"   width="80">
			 	 	<template #default="scope">
			 	 		 <el-input v-if="scope.row.edit"  v-model="scope.row.top3" ></el-input>
			 	 		 <div v-else>{{scope.row.top3}}</div>
			 	 	 </template>
			 	 </el-table-column>	
			 	 <el-table-column prop="percent1" label="百分比1"  width="80" >
			 	 	<template #default="scope">
			 	 		 <el-input v-if="scope.row.edit"  v-model="scope.row.percent1" ></el-input>
			 	 		 <div v-else>{{scope.row.percent1}}</div>
			 	 	 </template>
			 	 </el-table-column>	
			 	 <el-table-column prop="percent2" label="百分比2"  width="80" >
			 	 	<template #default="scope">
			 	 		 <el-input v-if="scope.row.edit"  v-model="scope.row.percent2" ></el-input>
			 	 		 <div v-else>{{scope.row.percent2}}</div>
			 	 	 </template>
			 	 </el-table-column>	
			 	 <el-table-column prop="percent3" label="百分比3" width="80"  >
			 	 	<template #default="scope">
			 	 		 <el-input v-if="scope.row.edit"  v-model="scope.row.percent3" ></el-input>
			 	 		 <div v-else>{{scope.row.percent3}}</div>
			 	 	 </template>
			 	 </el-table-column>
			 	 
			 	 <el-table-column prop="turndays" label="操作"    width="120"  >
			 		 <template #default="scope">
			 			  <el-button v-if="!scope.row.edit" @click="scope.row.edit=true"   link type="primary">修改</el-button>
			 			  <el-button v-if="scope.row.edit"  @click="saveItem(scope.row)"   link type="primary">保存</el-button>
			 			  <el-button v-if="!scope.row.edit" @click="deleteItem(scope.row)" link type="danger">删除</el-button>
			 		 </template>
			 	 </el-table-column>
			 	 </template>
			  </GlobalTable>
  </el-row>
</template>

<script setup>
	import {h, ref ,watch,reactive,onMounted,toRefs} from 'vue'
	import {Search,ArrowDown,} from '@element-plus/icons-vue';
	import GlobalTable from "@/components/Table/GlobalTable/index.vue";
    import { ElMessage ,ElMessageBox} from 'element-plus';
	import {MenuUnfold,Plus,SettingTwo,Help,Copy,MoreOne,ChartPie} from '@icon-park/vue-next';
	import {dateFormat,getCurrencyMark} from '@/utils/index.js';
	import MarketCountry from '@/components/header/market_country.vue';
	import profitParamApi from '@/api/amazon/profit/profitParam.js';
	const globalTable=ref();
	const marketCountryRef =ref();
	let state=reactive({
		 downLoading:false,
		 queryParam:{
			 startDate:"",
			 endDate:"",
			 groupid:'',
			 search:"",
			 days:540,
			 marketplaceid:"",
			 fromDate:new Date().getFullYear().toString()+"-"+(new Date().getMonth()>=10?(new Date().getMonth()).toString():"0"+(new Date().getMonth()).toString()),
		 },
		 isload:true,
		 tableData:{records:[],total:0},
		 tierList:[],
	});
	let {
	   queryParam,
	   isload,
	   tableData,
	   downLoading,
	   tierList,
	} =toRefs(state);
 
    function handleAdd(){
		var row={country:state.queryParam.country,edit:true,
		producttierid:state.queryParam.tierid,dispatchType:state.queryParam.dispatchtype};
		state.tableData.records.unshift(row);
	}
	function getCountry(val){
		state.queryParam.country=val;
		handleQuery();
	}
	function handleQuery(){
	     globalTable.value.loadTable(state.queryParam);
	}
	
	function loadTableData(param){
		profitParamApi.getReferralFee(param).then((res)=>{
			state.tableData.records=res.data.records;
			state.tableData.total=res.data.total;
		}); 
	}
	function saveItem(row){
		if(row.id){
			profitParamApi.updateReferralFee(row).then((res)=>{
				 ElMessage.success('操作成功！');
				 handleQuery();
			});
		}else{
			profitParamApi.saveReferralFee(row).then((res)=>{
				 ElMessage.success('操作成功！');
				 handleQuery();
			});
		}
		
	}

	function deleteItem(row){
		ElMessageBox.confirm(
			     '您确定要删除此记录?',
			    '删除FBA费用',
			    {
			      confirmButtonText: '确定',
			      cancelButtonText: '取消',
			      type: 'warning',
			    }
			  ).then( () => {
				  profitParamApi.deleteReferralFee(row).then((res)=>{
					 ElMessage.success('删除成功！');
				  	 handleQuery();
				  });
			  })
	}
	 onMounted(()=>{
		 marketCountryRef.value.show();
	 })
	 
	 
</script>

<style>
</style>
