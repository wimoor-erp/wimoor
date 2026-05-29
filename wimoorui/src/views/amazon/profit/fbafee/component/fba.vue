<template>
		<div class="con-header" >
		  <el-row>
		    <el-space >
		  <MarketCountry @change="getCountry"  ref="marketCountryRef" />
		<el-select v-model="queryParam.tierid" filterable  @change="handleQuery">
			<el-option v-for="item in tierList" :label="item.name" :key="item.id" :value="item.id"></el-option>
		</el-select>
		 <el-select v-model="queryParam.dispatchtype"  @change="handleQuery">
		 	<el-option label="全部" value=""  key=""></el-option>
			<el-option label="EFN" value="EFN"  key="EFN" ></el-option>
			<el-option label="PAN_EU" value="PAN_EU"  key="PAN_EU"></el-option>
		 </el-select>
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
			  				 <el-input v-if="scope.row.edit" v-model="scope.row.country"  ></el-input>
							 <div v-else>{{scope.row.country}}</div>
			  </template>
		  </el-table-column>
		  <el-table-column prop="country" label="尺寸分段"   width="250"  >
		  			 <template #default="scope">
		  			  		<el-select v-model="scope.row.producttierid" :disabled="!scope.row.edit" filterable  >
		  			  			<el-option v-for="item in tierList" :label="item.name"
								 :key="item.id" 
								 :value="item.id"></el-option>
		  			  		</el-select>
		  			  </template>
		   </el-table-column>
		  
		 <el-table-column prop="dispatchType" label="泛欧"   width="110" >
			 <template #default="scope">
				 <el-select v-model="scope.row.dispatchType" v-if="scope.row.edit" filterable  >
				 	<el-option  label="PAN_EU"  key="PAN_EU"   value="PAN_EU"></el-option>
					<el-option  label="EFN"  key="EFN"   value="EFN"></el-option>
					<el-option  label="无"  key=""   value="">无</el-option>
				 </el-select>
				 <div v-else>{{scope.row.dispatchType}}</div>
			 </template>
			 </el-table-column>
			 
		  <el-table-column prop="isclothing" label="服装"    width="100" >
		  <template #default="scope">
				  <el-checkbox v-model="scope.row.isclothing" :disabled="!scope.row.edit" size="large" />
		  	 </template>
		  </el-table-column>
		  <el-table-column prop="isclothing" label="媒体"     width="100" >
		  <template #default="scope">
		  				  <el-checkbox v-model="scope.row.ismedia" :disabled="!scope.row.edit" size="large" />
		  	 </template>
		  </el-table-column>
		 <el-table-column prop="weight" label="重量"  sortable='custom'  width="100"  >
		 	 <template #default="scope">
		 	 				 <el-input v-if="scope.row.edit" v-model="scope.row.weight"></el-input>
							 <div v-else>{{scope.row.weight}}</div>
		 	 </template>
		 </el-table-column>
		 <el-table-column prop="effectiveDate" label="生效日期"    width="180"  >
		 	 <template #default="scope">
		 	 				<el-date-picker
							        :disabled="!scope.row.edit"
		 	 				        v-model="scope.row.effectiveDate"
		 	 				        type="date"
									style="width:150px"
		 	 				        placeholder="不选立即生效"
		 	 				      />
		 						 
		 	 </template>
		 </el-table-column>
		 <el-table-column prop="expiryDate" label="失效日期"    width="180"  >
		 	 <template #default="scope">
		 	 				<el-date-picker
		 							:disabled="!scope.row.edit"
		 	 				        v-model="scope.row.expiryDate"
									style="width:150px"
		 	 				        type="date"
		 	 				        placeholder="不选永不失效"
		 	 				      />
		 						 
		 	 </template>
		 </el-table-column>
		 <el-table-column prop="fbaFormat" label="FBA费"   >
			<template #default="scope">
			 				 <el-input v-if="scope.row.edit" type="textarea" v-model="scope.row.fbaFormat" ></el-input>
							 <div v-else>{{scope.row.fbaFormat}}</div>
			 </template>
		</el-table-column>	 
		 <el-table-column prop="turndays" label="操作"    width="120"  >
			 <template #default="scope">
				  <el-button v-if="!scope.row.edit" @click="scope.row.edit=true" link type="primary">修改</el-button>
				  <el-button v-if="scope.row.edit"  @click="saveItem(scope.row)" link type="primary">保存</el-button>
				  <el-button v-if="!scope.row.edit" @click="deleteItem(scope.row)" link type="danger">删除</el-button>
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
		profitParamApi.getFBA(param).then((res)=>{
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
			profitParamApi.updateFBA(row).then((res)=>{
				 ElMessage.success('操作成功！');
				 handleQuery();
			});
		}else{
			profitParamApi.saveFBA(row).then((res)=>{
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
				  profitParamApi.deleteFBA(row).then((res)=>{
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
