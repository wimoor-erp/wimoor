<template>
     <div class="main-sty">
			 <div class="con-header" >
			 	  <el-row>
			 	    <el-space >
			 	    <MarketCountry @change="getCountry"  ref="marketCountryRef" />
					<el-input  v-model="queryParam.search" clearable   placeholder="请输入" class="input-with-select" >
					   <template #append>
					     <el-button @click.stop="handleQuery">
					        <el-icon class="ic-cen font-medium">
					         <search />
					      </el-icon>
					     </el-button>
					   </template>
					 </el-input>
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
			 <GlobalTable ref="globalTable" :tableData="tableData" height="calc(100vh - 200px)" :size="20" @loadTable="loadTableData" :stripe="true"  style="width: 100%;margin-bottom:16px;">
			   <template #field>
			 	 <el-table-column prop="code" label="Code"  sortable='custom'  width="120">
			 		 <template #default="scope">
			 			 <el-input v-if="scope.row.edit" v-model="scope.row.code"  ></el-input>
			 			 <div v-else>{{scope.row.code}}</div>
			 		  </template>
			 	  </el-table-column>
			 	  <el-table-column prop="country" label="国家"   width="120">
			 	  	<template #default="scope">
			 	  		 <el-input v-if="scope.row.edit"  v-model="scope.row.country" ></el-input>
			 	  		 <div v-else>{{scope.row.country}}</div>
			 	  	 </template>
			 	  </el-table-column>
			 	  <el-table-column prop="address_name" label="地址名称"     >
			 	  			 <template #default="scope">
			 	  			  		<el-input v-if="scope.row.edit"  v-model="scope.row.addressName" ></el-input>
			 	  			  		<div v-else>{{scope.row.addressName}}</div>
			 	  			  </template>
			 	   </el-table-column>
			 	 <el-table-column prop="city" label="城市"  width="180"  sortable='custom'>
			 	 	<template #default="scope">
			 	 		 <el-input v-if="scope.row.edit"  v-model="scope.row.city" ></el-input>
			 	 		 <div v-else>{{scope.row.city}}</div>
			 	 	 </template>
			 	 </el-table-column>	
			 	 <el-table-column prop="state" label="州/区"   sortable='custom' width="100" >
			 	 	<template #default="scope">
			 	 		 <el-input v-if="scope.row.edit"  v-model="scope.row.state" ></el-input>
			 	 		 <div v-else>{{scope.row.state}}</div>
			 	 	 </template>
			 	 </el-table-column>	
			 	 <el-table-column prop="zip" label="邮编"   width="120">
			 	 	<template #default="scope">
			 	 		 <el-input v-if="scope.row.edit"  v-model="scope.row.zip" ></el-input>
			 	 		 <div v-else>{{scope.row.zip}}</div>
			 	 	 </template>
			 	 </el-table-column>	
			 	 <el-table-column prop="area" label="区域"   width="120">
			 	 	<template #default="scope">
			 	 		 <el-input v-if="scope.row.edit"  v-model="scope.row.area" ></el-input>
			 	 		 <div v-else>{{scope.row.area}}</div>
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
  </div>
</template>

<script setup>
	import {h, ref ,watch,reactive,onMounted,toRefs} from 'vue'
	import {Search,ArrowDown,} from '@element-plus/icons-vue';
	import GlobalTable from "@/components/Table/GlobalTable/index.vue";
    import { ElMessage ,ElMessageBox} from 'element-plus';
	import {MenuUnfold,Plus,SettingTwo,Help,Copy,MoreOne,ChartPie} from '@icon-park/vue-next';
	import {dateFormat,getCurrencyMark} from '@/utils/index.js';
	import MarketCountry from '@/components/header/market_country.vue';
	import fbacenterApi from '@/api/amazon/inbound/fbacenterApi.js';
	const globalTable=ref();
	const marketCountryRef =ref();
	let state=reactive({
		 downLoading:false,
		 queryParam:{
			 groupid:'',
			 search:"",
			 marketplaceid:"",
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
		var row={country:state.queryParam.country,
		         edit:true,
				};
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
		fbacenterApi.getpage(param).then((res)=>{
			state.tableData.records=res.data.records;
			state.tableData.total=res.data.total;
		}); 
	}
	function saveItem(row){
		if(row.code){
			fbacenterApi.update(row).then((res)=>{
				 ElMessage.success('操作成功！');
				 handleQuery();
			});
		}else{
			fbacenterApi.save(row).then((res)=>{
				 ElMessage.success('操作成功！');
				 handleQuery();
			});
		}
		
	}

	function deleteItem(row){
		ElMessageBox.confirm(
			     '您确定要删除此记录?',
			    '删除',
			    {
			      confirmButtonText: '确定',
			      cancelButtonText: '取消',
			      type: 'warning',
			    }
			  ).then( () => {
				  fbacenterApi.remove(row).then((res)=>{
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
