<template>
  <div class="main-sty">
	  <div class="con-header" >
	     <MyHeader @getdata="loadData"></MyHeader>
			 <el-row>
				      <div class='rt-btn-group'>
						 <span class="font-extraSmall">重要提示：当本地录入的产品类型与亚马逊分类不一致时，可能造成FBA费用不一样。如：Apparel/Clothing类型的产品，在美国站点每笔加收$0.4.
						</span>
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
	  <GlobalTable ref="globalTable" :tableData="tableData" height="calc(100vh - 254px)" @loadTable="loadTableData" :stripe="true"  style="width: 100%;margin-bottom:16px;">
		  <template #field>
			    <el-table-column prop="img"    label="图片" width="60" >
			  	       <template #default="scope">
			  	        <el-image :src="scope.row.image"   width="40" height="40"  ></el-image>
			  	      </template>
			  	    </el-table-column>
	    <el-table-column prop="product"  label="商品信息"    show-overflow-tooltip>
	       <template #default="scope">
				<div class='name'>{{scope.row.pname}}</div>
				<div class='sku'>{{scope.row.sku}}
				<el-tag  v-if="scope.row.isSmlAndLight==true"  type="success">低价产品</el-tag>
				 </div>  
			   
	      </template>
	    </el-table-column>
		<el-table-column prop="QuantityOrdered" label="参数类型" width='100' >
			<span>自测参数</span><br />FBA参数
		</el-table-column>
		<el-table-column prop="total_price" label="尺寸(长*宽*高cm)" width="150"  >
			 <template #default="scope">
				 <span>{{scope.row.selfsize}}</span> <br />
				 <span>{{scope.row.countrysize}}</span>
			 </template>
		 </el-table-column>
	    <el-table-column prop="sweight" label="重量（kg）" width="100"   >
			 <template #default="scope">
				 <span>{{scope.row.sweight}}</span><br />
				 <span>{{scope.row.countryweight}}</span>
			 </template>
		 </el-table-column>
		 <el-table-column prop="price" label="价格" width="100"   >
		 	 <template #default="scope">
		 		 <span class="sale-price" v-if="scope.row.price">{{scope.row.fcurrency}}{{scope.row.price}}</span> 
				 <span v-else class="sale-price">--</span>
		 	 </template>
		  </el-table-column>
		 
		 <el-table-column prop="price" label="尺寸分段" width="190"   >
		 	 <template #default="scope">
		 		 <span>{{scope.row.selftier}}</span><br />
		 		 <span>{{scope.row.countrytier}}</span>
		 	 </template>
		  </el-table-column>
		  <el-table-column prop="overcharge" label="FBA收费(每月预计超收费用)"  sortable="custom"    >
		  	 <template #default="scope">
		  		 <span>{{scope.row.fcurrency}}{{formatFloat(scope.row.selfFBA)}}</span><br />
		  		 <span>{{scope.row.fcurrency}}{{formatFloat(scope.row.amzFBA)}}</span>
				 <span v-if="scope.row.overcharge>0" style="margin-left: 5px;">
					 <el-tag class="ml-2" type="danger">{{scope.row.fcurrency}}{{formatFloat(scope.row.overcharge)}}</el-tag>
				 </span>
		  	 </template>
		   </el-table-column>
		   <el-table-column prop="price" label="更新时间" width="250"   >
		   	 <template #default="scope">
		   		 <span v-if="scope.row.lastupdate">
		   		 		{{dateTimesFormat(scope.row.lastupdate)}}
		   		 </span>
				 <span v-else>未能正常更新</span>
		   	 </template>
		    </el-table-column>
		  </template>
	  </GlobalTable>
	</el-row>
	</div>
</template>
<script>
    export default{ name:"FBA费用" };
</script>
<script setup>
	import { ref ,watch,reactive,onMounted,onUpdated,getCurrentInstance, toRefs} from 'vue';
	import GlobalTable from "@/components/Table/GlobalTable/index.vue";
	import productFbaFeeApi from "@/api/amazon/product/productFbaFeeApi.js";
	import MyHeader from "./header.vue"
	import {dateTimesFormat,formatFloat} from '@/utils/index.js';
	import {ElMessage } from 'element-plus';
	import {MenuUnfold,Plus,SettingTwo,Help,Copy,MoreOne,ChartPie} from '@icon-park/vue-next';
	        const globalTable=ref(GlobalTable);
		 
			let state=reactive({
				tableData:{records:[],total:0},
				summaryData:{},
				queryParams:{},
			});
			defineExpose({
			  loadData,
			})
			
			 const {
			   tableData,
			   summaryData,
			 } = toRefs(state);
			 
			onMounted(()=>{
				 
			})
		 
			function loadData(params){
				globalTable.value.loadTable(params);
			}
			
			function loadTableData(params){
				state.queryParams=params;
				productFbaFeeApi.getSizePro(params).then((res)=>{
					if(res.data){
						state.tableData.records = res.data.records;
						state.tableData.total =res.data.total;
					}else{
						state.tableData.records = [];
						state.tableData.total =0;
					}
					
				})
			}
	 
</script>

<style>
	.sale-price {
	    font-weight: 600;
	    color: #f5a20c;
	} 
</style>

