<template>
 <div>
	 <GlobalTable  ref="globalTable" 
	   :tableData="tableData"  
	   height="calc(100vh - 200px)"
	  @loadTable="loadTableData" :stripe="true" 
	  :defaultSort="{ prop: 'opttime', order: 'descending' }" >
		  <template #field>
		 <el-table-column  v-if="!isDailog" type="selection" width="38" />
		 <el-table-column v-if="!isDailog" label="图片" width="60">
			 <template #default="scope">
			   <el-image :src="scope.row.image"   width="40" height="40"  ></el-image>
			 </template>
		 </el-table-column>
		 <el-table-column v-if="!isDailog" label="SKU/Fnsku" prop="sku" sortable="custom">
		 			 <template #default="scope">
		 			 <div>{{scope.row.sku}}</div>
					 <div>fnsku:{{scope.row.fnsku}}</div>
		 			 </template>
		 </el-table-column>
		 <el-table-column v-if="!isDailog" label="店铺" prop="gname"/>
		 <el-table-column label="改价类型" prop="ftype">
			  <template #default="scope">
				<span v-if="scope.row.ftype=='forever'">永久调价</span>
				<span v-else-if="scope.row.ftype=='times'">限时调价</span>
				<span v-else>企业调价</span>
			  </template>
	     </el-table-column>
		   <el-table-column v-if="!isDailog" prop="mname"  label="国家" />
		    <el-table-column  prop="standardprice" label="调整前"  >
				<template #default="scope">
					<span v-if="scope.row.ftype=='forever'">
						{{formatFloat(scope.row.oldstandardprice)}}
					</span>
					<span v-else>
						{{formatFloat(scope.row.standardprice)}}
					</span>
				</template>
			</el-table-column>
		   <el-table-column  prop="standardprice" label="调整后"  >
			   <template #default="scope">
				   <span v-if="scope.row.ftype=='forever'">
				   	{{formatFloat(scope.row.standardprice)}}
				   </span>
				   <span v-else>
				   	{{formatFloat(scope.row.saleprice)}}
				   </span>
			   </template>
			</el-table-column>
			 <el-table-column  prop="status" label="处理状态"  width="160" >
				<template #default="scope">
					 <FeedStatus v-if="scope.row.status!='DONE'&&scope.row.status!='_DONE_'" filename="price" :feedid="scope.row.feedid"></FeedStatus>
					 <el-tag v-else  type="success"  >
						 {{scope.row.statusText}}
					 </el-tag>
				</template>
				 </el-table-column>
			 <el-table-column  prop="operator" label="操作人"  />
			  <el-table-column  prop="opttime" sortable="custom" label="创建时间" width="160"  >
				  <template #default="scope">
				  	 {{dateTimesFormat(scope.row.opttime)}}
				  </template>
			 </el-table-column>
			   <el-table-column  prop="successdate" sortable="custom" label="完成时间" width="160"  >
				   <template #default="scope">
				   	 {{dateFormat(scope.row.successdate)}}  
				   </template>
			    </el-table-column>
	 </template>
	 </GlobalTable>
 </div>
</template>

<script>
	import {ref,reactive,onMounted,watch,h} from 'vue'
	import {Help,Plus,MenuUnfold} from '@icon-park/vue-next';
	import {ElMessage,ElDivider} from 'element-plus';
	import {Search,ArrowDown} from '@element-plus/icons-vue';
    import productinfoApi from '@/api/amazon/product/productinfoApi.js';
	import FeedStatus from "@/components/feedstatus/index.vue";
	import GlobalTable from "@/components/Table/GlobalTable/index.vue";
	import {formatFloat,dateFormat,dateTimesFormat} from '@/utils/index.js';
	export default {
		name:'Table',
		components: {FeedStatus,GlobalTable
		},
		props:["isDailog"],
		setup() {
		let globalTable=ref(GlobalTable);	
        let tableData=reactive({records:[],total:0})
		function loadTableData(params){
			productinfoApi.priceQueue(params).then(({data})=>{
				tableData.records =  data.records
				tableData.total = data.total
			})
		}
		function loadData(data){
			  globalTable.value.loadTable(data);
		}
			return {
			tableData,loadData,loadTableData,globalTable,dateFormat,formatFloat,dateTimesFormat
			}
		},
	}
</script>

<style>

</style>