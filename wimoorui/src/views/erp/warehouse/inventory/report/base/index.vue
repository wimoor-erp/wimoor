<template>
	<div class="main-sty">
		<div class="con-header">
	     <el-row>
		  <el-space>
			  <Supplier  ref="supplierRef"   @change="changeSupplier" />
			  <el-cascader
			        v-model="warehouseCheck"
			        :options="warehouseData"
			  	    :value = "id"
			  	    :label="name"
			        :props="props"
			        @change="getWarehouse"
			  	     placeholder="全部仓库"
			  	     clearable
			      />
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
		 <el-popover   :popper-append-to-body="false" v-model:visible="moreSearchVisible" :width="480" trigger="click">
		 	<template #reference>
		 	
		 		      <el-button :color="filterBtnColor" plain class='ic-btn'>
		 		      	 <Filtericon></Filtericon>
		 		      </el-button>
		 	</template>
		 	 <el-form :model="form"  label-width="auto"  label-position="left">
				 <el-form-item label="子产品SKU">
				 		 <el-input v-model="queryParams.itemsku" clearable="true"></el-input>
				   </el-form-item>
				   <el-form-item label="辅料SKU">
				   		 	<el-input v-model="queryParams.conssku" clearable="true"></el-input>
				     </el-form-item>
		 	    <el-form-item label="产品分类">
		 			 <Category ref="cateRef" @change="getCategory" />
		 		 </el-form-item>
		 		 
		 			  <el-form-item label="产品类型">
						 <el-radio-group v-model="productType">
							  <el-radio-button label="全部" />
							  <el-radio-button label="单独产品" />
							  <el-radio-button label="组合" />
							  <el-radio-button label="待组装" />
							</el-radio-group>
					 </el-form-item>	
		 				
		 			    <el-form-item>
		 			  <el-button type="primary" @click="submitSearch(formRef)">搜索</el-button>
		 			  <el-button @click="resetSearch(formRef)">取消</el-button>
		 			    </el-form-item>
		 			
		 	</el-form>
		   </el-popover>
		 <el-button type="primary" @click.stop="downloadExcel">导出</el-button>
		 </el-space>
		 <div class='rt-btn-group'>
			    <el-checkbox v-model="queryParams.notlike" @change="handleQuery">精确查询</el-checkbox>	
		 		<el-checkbox v-model="queryParams.hasinv" @change="handleQuery">仅查看有库存产品</el-checkbox>	   
		 </div>
		 </el-row>
		 </div>
		 <div class="con-body">
			 <GlobalTable ref="globalTable"
			  show-summary
			  :summary-method="getSummaries"
			  :tableData="tableData"  height="calc(100vh - 200px)" @selectionChange='handleSelect' 
			  :defaultSort="{ prop: 'sku', order: 'descending' }"  @loadTable="loadTableData" :stripe="true"  
			  style="width: 100%;margin-bottom:16px;">
			 	<template #field>
			 	<el-table-column label="产品信息" prop="name"    sortable="custom" >
					<template #default="scope">
					<div class="flex-center">
						   <el-image v-if="scope.row.image" :src="scope.row.image" class="img-40"  width="40" height="40"  ></el-image>
						   <el-image v-else :src="$require('empty/noimage40.png')"  class="img-40"  width="40" height="40"  ></el-image>
						   <div >
							   <div   >{{scope.row.name}}</div>
							   <p class="sku">{{scope.row.sku}} 
							    <span v-if="scope.row.issfg=='1'"   @click.stop="handleEmtpy">
									<el-tag
									 title="组合产品"
									 @click.stop="e=>handleAssemblyShow(e,scope.row)"
									 @mouseenter="e=>handleAssemblyShow(e,scope.row)"
									 type="warning" class="pointer" 
									 size="small" 
									>组合</el-tag>
							    </span>
							  </p>
						   </div>
					</div>
					</template>
				</el-table-column>
				<el-table-column label="仓库" prop="warehouse"    sortable="custom" >
					<template #default="scope">
					<div class="flex-center">
						    <div v-if="scope.row.warehouse">{{scope.row.warehouse}}</div>
							<div v-else class="font-extraSmall">暂无记录</div>
					</div>
					</template>
				</el-table-column>
			 	<el-table-column label="待入库" prop="inbound"  width="150" sortable="custom"  >
			 		<template #default="scope">
						<el-popover
						  placement="top"
						  trigger="hover"
						  @show="findInboundDetail(scope.row)"
						>
						  <div v-if="inboundlist&&inboundlist.length>0" v-for="(item,index) in inboundlist" style="margin-top: 3px;"><span>{{item.name}}:</span>
						  <el-tag class="ml-2" type="success" >{{item.quantity}}</el-tag>
						  <span>
							  <el-icon class="pointer padding-left-5" title="修复待入库库存,此操作存在风险,若不确定库存错误,请勿执行" @click.stop="handleRefreshInbound(item,'inbound')"><Refresh /></el-icon>
						  </span>
						  </div>
						   <div v-else>暂无数据</div>
						  <template #reference>
								<span>
									{{scope.row.inbound}}
								</span>
						  </template>
						</el-popover>
			 		</template>
			 	</el-table-column>
			 	<el-table-column label="可用库存" prop="fulfillable"   width="150" sortable="custom"   >
			 		<template #default="scope">
			 			<div> {{scope.row.fulfillable}} </div>
			 		</template>
			 	</el-table-column>
			 	<el-table-column label="待出库" prop="outbound"  width="150" sortable="custom" >
					<template #default="scope">
						<el-popover
						  placement="top"
						  trigger="hover"
						  @show="findOutboundDetail(scope.row)"
						>
						  <p v-if="outboundlist&&outboundlist.length>0" v-for="(item,index) in outboundlist" style="margin-top: 3px;"><span>{{item.name}}:</span>
						  <el-tag class="ml-2" type="warning" >{{item.quantity}}</el-tag>
						  <span>
						  		 <el-icon class="pointer padding-left-5" title="修复待出库库存,此操作存在风险,若不确定库存错误,请勿执行" @click.stop="handleRefreshInbound(item,'outbound')"><Refresh /></el-icon>
						  </span>
						  </p>
						  <p v-else>暂无数据</p>
						  <template #reference>
								<span>
									{{scope.row.outbound}}
								</span>
						  </template>
						</el-popover>
					</template>
				</el-table-column>
			 	</template>
			 </GlobalTable>
		 </div>
	</div>
	 <AssemblyDialog :assbRef="assbRef" 
	                 @change="getAssembliyList(editRow)" 
	                 :loading="editRow.assloading"
	                 :assemblyList="editRow.assemblyList"/>
</template>

<script setup>
	import {ref,reactive,toRefs,onMounted}from"vue"
	import {MoreOne} from '@icon-park/vue-next';
	import Filtericon from '@/components/icon/filtericon.vue';
	import Category from '@/components/header/category.vue';
	import { ElMessage, ElMessageBox,ElForm } from 'element-plus';
	import Supplier from '@/components/header/supplier.vue';
	import Warehouse from '@/components/header/warehouse.vue';
	import inventoryApi from '@/api/erp/inventory/inventoryApi.js';
	import warehouseApi from '@/api/erp/warehouse/warehouseApi.js';
	import {dateFormat,formatFloat,dateTimesFormat} from '@/utils/index.js';
	import {Search,Refresh} from '@element-plus/icons-vue';
	import AssemblyDialog from "@/views/erp/components/assembly_dialog.vue";
	import {sublit} from "@/api/erp/assembly/assemblyApi.js";
	const assbRef =ref();
	let detailsRef =ref()
	let globalTable=ref()
	const dataFormRef = ref(ElForm);
	let state = reactive({
		tableData: {records:[],total:0},
		queryParams:{
			itemsku:"",
			conssku:"",
			notlike:false,
			islike:true,
		},
		editRow:{},
		warehouseCheck:null,
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
		props : {
		  'value':'id',
		  'label':'name',
		}
	})
	let {
		tableData,queryParams,warehouseCheck,editRow,
		warehouseData,inboundlist,outboundlist,asslist,dialogVisable,productType,moreSearchVisible,
		allfulfillable,allinbound,alloutbound,props
		 } =toRefs(state);
		 
		 function downloadExcel(){
			 inventoryApi.getWarehouseExport(state.queryParams);
		 }
		 function handleAssemblyShow(e,row){
		 		 const evt = e || window.e || window.event;
		 		 assbRef.value = evt.currentTarget
		 		 state.editRow = row
		 		 state.editRow.oldwarehouseid=state.editRow.warehouseid;
		 	}
		 function changeSupplier(id,load){
			 state.queryParams.supplierid=id;
			 if(load!="load"){
				 handleQuery();
			 }
		 }
		 function submitSearch(){
			 var issfg="";
			 if(state.productType=="全部"){
			 	issfg="";
			 }else if(state.productType=="单独产品"){
			 	issfg="0";
			 }else if(state.productType=="组合"){
			 	issfg="1";
			 }else if(state.productType=="待组装"){
			 	issfg="2";
			 }
			 state.queryParams.ftypes=issfg;
			 handleQuery();
			 state.moreSearchVisible=false;
		 }
		 function getCategory(category){
		 	state.queryParams.category=category;
		 }
		 function getAssembliyList(row){
		 	if(!row["assemblyList"]){
		 		row.assloading=true;
		 		sublit({materialid:row.materialid,warehouseid:row.warehouseid}).then(res=>{
		 			row.assemblyList=res.data;
		 			row.assloading=false;
		 		});
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
			 inventoryApi.getWarehouse(params).then(res=>{
				 state.tableData.records=res.data.records;
				 state.tableData.total=res.data.total;
				 if(params.currentpage==1){
					 if(res.data.total>0){
						  state.allfulfillable=res.data.records[0].allfulfillable;
						  state.allinbound=res.data.records[0].allinbound;
						  state.alloutbound=res.data.records[0].alloutbound;
					 }
				 }
					
				 
			 })
		 }
		 function handleRefreshInbound(item,type){
			 inventoryApi.refreshInventory(item).then((res)=>{
			 		ElMessage.success("操作成功");	
					if(type=="inbound"){
						findInboundDetail(item);		 
					}else{
						findOutboundDetail(item)
					}
					
				    handleQuery();
			 });
		 }
		 function findInboundDetail(row){
			 if(row.warehouseid && row.materialid){
				 inventoryApi.findInboundDetail({"mid":row.materialid,"warehouseid":row.warehouseid}).then((res)=>{
					 state.inboundlist=res.data;
				 });
			 }
		 }
		 function findOutboundDetail(row){
			 if(row.warehouseid && row.materialid){
				 inventoryApi.findOutboundDetail({"mid":row.materialid,"warehouseid":row.warehouseid}).then((res)=>{
					 state.outboundlist=res.data;
				 });
			 }
		 }
		 /* 合计行数据 */
		 function getSummaries(){
		 	var arr = ["合计"]
		 	arr[2]=state.allinbound;
		 	arr[3]=state.allfulfillable;
		 	arr[4]=state.alloutbound;
		 	return  arr;
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
