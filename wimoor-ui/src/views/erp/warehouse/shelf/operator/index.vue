<template>
	<div class="main-sty">
		<div class="con-header">
	     <el-row>
		  <el-space>
			  <el-cascader
			        v-model="warehouseCheck"
			        :options="warehouseData"
			  	    @change="getWarehouse"
					:props="{'value':'id','label':'name'}"
			  	   	placeholder="全部仓库"
			  	    clearable
			      />
		 <Datepicker ref="datepickers" @changedate="changedate" />
			<!-- <el-button @click.stop="uploadFile">导入</el-button> -->
		<el-input  v-model="queryParams.search" @input="handleQuery" clearable placeholder="请输入SKU" style="width: 250px;" class="input-with-select" >
		   <template #append>
		     <el-button @click="handleQuery" >
		        <el-icon class="ic-cen font-medium">
		         <search/>
		      </el-icon>
		     </el-button>
		   </template>
		 </el-input>
		<!-- <el-button type="primary" @click.stop="downloadExcel">导出</el-button> -->
		 
		 </el-space>
		<!-- <div class='rt-btn-group'>
		 		<el-checkbox v-model="queryParams.hasinv" @change="handleQuery">仅查看有库存产品</el-checkbox>	   
		 </div> -->
		 </el-row>
		 </div>
		 <div class="con-body">
			 <GlobalTable ref="globalTable"
			  :tableData="tableData"  height="calc(100vh - 200px)" @selectionChange='handleSelect' 
			  :defaultSort="{ prop: 'opttime', order: 'descending' }"  @loadTable="loadTableData" :stripe="true"  
			  style="width: 100%;margin-bottom:16px;">
			 	<template #field>
			 	<el-table-column label="产品信息" prop="sku"    sortable="custom" >
					<template #default="scope">
					<div class="flex-center">
						   <el-image v-if="scope.row.image" :src="scope.row.image" class="img-40"  width="40" height="40"  ></el-image>
						   <el-image v-else :src="$require('empty/noimage40.png')"  class="img-40"  width="40" height="40"  ></el-image>
						   <div >
							   <div   >{{scope.row.mname}}</div>
							   <p class="sku">{{scope.row.sku}} 
							  </p>
						   </div>
					</div>
					</template>
				</el-table-column>
				<el-table-column label="仓库-库位" prop="shelfname"    sortable="custom" >
					<template #default="scope">
					<div class="flex-center">
						    <div v-if="scope.row.shelfname">
								{{scope.row.wname}}- {{scope.row.shelfname}}</div>
							<div v-else class="font-extraSmall">暂无记录</div>
					</div>
						<div class="font-extraSmall">库位库存：{{scope.row.quantity2}}</div>
					</template>
				</el-table-column>
			 	 <el-table-column label="操作数量" prop="quantity"  width="120" sortable="custom"  >
			 	 </el-table-column>
				 <el-table-column label="结余数量" prop="balance_qty"  width="120" sortable="custom"  >
				 </el-table-column>
			 	 <el-table-column label="单据类型" prop="formname"  width="120" sortable="custom"  >
			 	 </el-table-column>
				 <el-table-column label="操作类型" prop="optname"  width="120" sortable="custom"  >
				 </el-table-column>
				 <el-table-column label="操作时间" prop="opttime"   sortable="custom"  >
					 <template #default="scope">
						 {{dateTimesFormat(scope.row.opttime)}}
						 </template>
				 </el-table-column>
			  <el-table-column label="操作人" prop="operator"  width="120" sortable="custom"  >
			  </el-table-column>
			 	</template>
			 </GlobalTable>
		 </div>
	</div>
</template>

<script setup>
	import {ref,reactive,toRefs,onMounted}from"vue"
	import { ElMessage, ElMessageBox,ElForm } from 'element-plus'
	import Warehouse from '@/components/header/warehouse.vue';
	import Datepicker from '@/components/header/datepicker.vue';
	import warehouseApi from '@/api/erp/warehouse/warehouseApi.js';
	import {dateFormat,formatFloat,dateTimesFormat} from '@/utils/index.js';
	import {Search,Refresh} from '@element-plus/icons-vue';
	import shelfApi from '@/api/erp/warehouse/shelf.js';
	const assbRef =ref();
	let detailsRef =ref()
	let globalTable=ref()
	const dataFormRef = ref(ElForm);
	let state = reactive({
		tableData: {records:[],total:0},
		queryParams:{
			itemsku:"",
			conssku:"",
			hasinv:true,
		},
		editRow:{},
		warehouseCheck:[],
		warehouseData:[],
		inboundlist:[],
		outboundlist:[],
		asslist:[],
		dialogVisable:false,
		productType:'全部',
		moreSearchVisible:false,
		allfulfillable:0,
		allinbound:0,
		alloutbound:0,
		isload:true,
		mprops : {
		  value:'id',
		  label:'name',
		  multiple: false,
		  expandTrigger: 'hover' ,
		}
	})
	let {
		tableData,queryParams,warehouseCheck,editRow,
		warehouseData,inboundlist,outboundlist,asslist,dialogVisable,productType,moreSearchVisible,
		allfulfillable,allinbound,alloutbound,mprops,
		 } =toRefs(state);
		 
		 //日期改变
		 function changedate(dates){
		 	state.queryParams.startDate=dates.start;
		 	state.queryParams.endDate=dates.end;
		 	if(state.isload==false){
		 		handleQuery();
		 	}
		 }
				 function getWarehouse(val){
				 	var warehouseid =''
				 	if(val&&val.length>1){
				 		state.warehouseData.forEach((item)=>{
				 			if(item.children && item.children.length>0){
				 				item.children.forEach((sub)=>{
				 					if(sub.id == val[1]){
				 						warehouseid =sub.id;
				 					}
				 				})
				 			}
				 		})
				 	}else{
				 		warehouseid =''
				 	}
				 	state.warehouseCheck=warehouseid;
				 	//每次改变仓库 变换list
				 	if(warehouseid!=''){
						if(warehouseid=="#"){
							warehouseid="";
							state.warehouseCheck=warehouseid;
						}
				 		handleQuery();
				 	}
				 }
		 
		 function handleQuery(){
			state.queryParams.warehouseid=state.warehouseCheck;
			globalTable.value.loadTable(state.queryParams);
		 }
		 function loadTableData(params){
			 shelfApi.getOptList(params).then((res)=>{
				 state.tableData.records=res.data.records;
				 state.tableData.total=res.data.total;
				 state.isload=false;
			 });
		 }
		 
		 
		 
		onMounted(()=>{ 
			warehouseApi.getWarehouseList().then(function(res){
				res.data.push({name:"全部仓库",id:"#",children:[{name:"全部仓位",id:"#"}]});
				state.warehouseData = res.data;
				if(res.data && res.data.length>0){
					state.warehouseCheck=res.data[0].children[0].id;
					handleQuery();
				}
			})
		}) 
</script>

<style scoped>
	.padding-left-5{
		padding-left:5px;
		font-size:16px;
	}
	.img-40{width: 40px;
	height: 40px;flex: none;
	margin-right: 8px;}
</style>
