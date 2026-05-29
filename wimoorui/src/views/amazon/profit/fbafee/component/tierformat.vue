<template>
		<div class="con-header" >
		  <el-row>
		    <el-space >
		  <MarketCountry @change="getCountry"  ref="marketCountryRef" />
		<el-select v-model="queryParam.tierid" filterable  @change="handleQuery">
			<el-option v-for="item in tierList" :label="item.name" :key="item.id" :value="item.id"></el-option>
		</el-select>
		<div class="text-red font-small">请谨慎调整次序，系统会按次序逐条判断产品的尺寸区间（低价次序必须大于100，非低价必须小于100）</div>
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
		 <el-table-column prop="country" label="国家"  sortable='custom'  width="80"  >
			 <template #default="scope">
			  				 <el-input v-model="scope.row.country" v-if="scope.row.edit"></el-input>
							 <div v-else>{{scope.row.country}}</div>
			  </template>
		  </el-table-column>
		  <el-table-column prop="country" label="尺寸分段"  sortable='custom'  width="250"  >
		  			 <template #default="scope">
		  			  		<el-select v-model="scope.row.producttierid" :disabled="!scope.row.edit" filterable  >
		  			  			<el-option v-for="item in tierList" :label="item.name"
								 :key="item.id" 
								 :value="item.id"></el-option>
		  			  		</el-select>
		  			  </template>
		   </el-table-column>
		  
		<el-table-column prop="lengthUnit" label="长度"  sortable='custom'  width="75"  >
			 <template #default="scope">
			 				 <el-input v-model="scope.row.lengthUnit" v-if="scope.row.edit"></el-input>
							 <div v-else>{{scope.row.lengthUnit}}</div>
			 </template>
		</el-table-column>
		 <el-table-column prop="weightUnit" label="重量"  sortable='custom'  width="75"  >
		 	 <template #default="scope">
		 	 				 <el-input  v-model="scope.row.weightUnit" v-if="scope.row.edit"></el-input>
							 <div v-else>{{scope.row.weightUnit}}</div>
		 	 </template>
		 </el-table-column>
		 <el-table-column prop="sort" label="次序"  sortable='custom'  width="75"  >
		 	 <template #default="scope">
		 	 				 <el-input  v-model="scope.row.sort" v-if="scope.row.edit"></el-input>
							 <div v-else>{{scope.row.sort}}</div>
		 	 </template>
		 </el-table-column>
		 	
		 <el-table-column prop="format" label="标准"  sortable='custom'   >
			<template #default="scope">
			 				 <el-input type="textarea" v-if="scope.row.edit" v-model="scope.row.format" ></el-input>
							 <div v-else>{{scope.row.format}}</div>
			 </template>
		</el-table-column>	 
	
		 <el-table-column prop="turndays" label="操作"    width="120"  >
			 <template #default="scope">
				  <el-button @click="saveItem(scope.row)" v-if="scope.row.edit"  link type="primary">保存</el-button>
				  <el-button @click="scope.row.edit=true" v-if="!scope.row.edit"  link type="primary">编辑</el-button>
				 <el-button @click="deleteItem(scope.row)"  v-if="!scope.row.edit"  link type="danger">删除</el-button>
			 </template>
		 </el-table-column>
		 </template>
	 </GlobalTable>
	</el-row>
 <a href=""></a>
	 
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
		loadTier();
	}
	function handleQuery(){
	     globalTable.value.loadTable(state.queryParam);
	}
	function loadTableData(param){
		profitParamApi.getTierFormat(param).then((res)=>{
			state.tableData.records=res.data.records;
			state.tableData.total=res.data.total;
		}); 
	}
	function loadTier(){
		profitParamApi.getTier(state.queryParam).then((res)=>{
			state.tierList=res.data;
			if(res.data.length>0){
			   state.tierList.push({id:"",name:"全部"})
			   state.queryParam.tierid="";
			}
			handleQuery();
		}); 
	}
	function saveItem(row){
		if(row.id){
			profitParamApi.updateTierFormat(row).then((res)=>{
				 ElMessage.success('操作成功！');
				 handleQuery();
			});
		}else{
			profitParamApi.saveTierFormat(row).then((res)=>{
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
				  profitParamApi.deleteTierFormat(row).then((res)=>{
				  	 ElMessage.success( "删除成功！");
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
