<template>
<div>
  	<el-dialog v-model="dialog.visible" class="skuinventory" title="添加产品" @opened="loadData()" width="80%" top="6vh" :before-close="handleClose">
  		<div class="con-header">
  			<el-row>
  				<el-input v-model="queryParams.search" @input="loadData" placeholder="搜索产品SKU">
  					<template #suffix>
  						<el-icon :size="16">
  							<Search />
  						</el-icon>
  					</template>
  				</el-input>
  			</el-row>
  		</div>
		<el-row>
			<el-col :span="12"  >
			 <el-card style="margin-right:10px;">
			  <GlobalTable ref="globalTableRef"
				  		    :inDialog="true" 
							height="calc(100vh*0.55)"
				  		    :tableData="tableData" 
				  			@loadTable="loadTableData"
				  			:border="false" 
				  			style="width: 100%;">
				  			<template #field>
				  				<el-table-column prop="image" label="图片" width="60">
				  					<template #default="scope">
				  						<el-image :src="scope.row.image" width="40" height="40"></el-image>
				  					</template>
				  				</el-table-column>
				  				<el-table-column label="名称/SKU" width="200" prop="sku" sortable="custom"  show-overflow-tooltip>
				  					<template #default="scope">
				  						<div class='name'>{{scope.row.name}}</div>
				  						<div class='sku'>{{scope.row.sku}}
				  						</div>
				  					</template>
				  				</el-table-column>
				  				<el-table-column prop="shelfname" label="库位" sortable="custom" >
									<template #default="scope">
										<div class='name'>{{scope.row.shelfname}}</div>
										<div class='sku'><span class="font-extraSmall">库位库存: </span>{{scope.row.quantity}}
										</div>
									</template>
								</el-table-column>
								<el-table-column prop="amount"  label="盘点数量" sortable="custom" >
									<template #default="scope">
				                 <el-input v-if="queryParams.stocktype==1&&!disable"   v-model="scope.row.amount" 
								 @input="scope.row.amount=CheckInputInt(scope.row.amount)">
								   <template #append>	<el-button :disabled="disable" size="small" type="primary"
										@click="saveShelfItem(scope.row,scope.$index)">保存</el-button>
										</template>
								 </el-input>
								<div v-else>{{scope.row.amount}}</div>
										<el-space v-if="scope.row.overamount||scope.row.lossamount">
											<div>
												<span class="font-extraSmall">盘盈:</span><span class="text-green">{{scope.row.overamount}}</span>
											</div>
											<div>
												<span class="font-extraSmall">盘亏:</span><span class="text-danger">{{scope.row.lossamount}}</span>
											</div>
										</el-space>
									</template>
									</el-table-column >
				  			</template>
				</GlobalTable>  
				</el-card>
			</el-col>
			<el-col :span="12">
				<el-card style="margin-left:10px;">
				<GlobalTable ref="globalTableRef2"
				  		    :inDialog="true" 
							height="calc(100vh*0.55)"
				  		    :tableData="tableData2" 
				  			@loadTable="loadTableData2"
				  			:border="false" 
				  			style="width: 100%;">
				  			<template #field>
				  				 <el-table-column prop="image" label="图片" width="60">
				  					<template #default="scope">
				  						<el-image :src="scope.row.image" width="40" height="40"></el-image>
				  					</template>
				  				</el-table-column>  
				  				<el-table-column label="名称/SKU" width="200" prop="sku" sortable="custom"  show-overflow-tooltip>
				  					<template #default="scope">
				  						<div class='name'>{{scope.row.name}}</div>
				  						<div class='sku'>{{scope.row.sku}}
				  						</div>
				  					</template>
				  				</el-table-column>
				  			   <el-table-column prop="warehouse" label="仓位" sortable="custom" >
									<template #default="scope">
										<div class='name'>{{scope.row.warehouse}}</div>
										<div class='sku'><span class="font-extraSmall">库存: </span>{{scope.row.fulfillable}}
										</div>
										<div class='sku'><span class="font-extraSmall">待出库: </span>{{scope.row.outbound}}
										</div>
									</template>
								</el-table-column>
								<el-table-column prop="amount"  label="盘点数量"   sortable="custom" >
									<template #default="scope">
										<div v-if="!disable&&queryParams.stocktype==2">
											<el-input    v-model="scope.row.amount"
											@input="scope.row.amount=CheckInputInt(scope.row.amount)">
											  <template   #append>	<el-button size="small" type="primary"
																					@click="saveWarehouseItem(scope.row,scope.$index)">保存</el-button>
																					</template>
											</el-input>
																			
																					
										</div>
										<div v-else>
											{{scope.row.amount}}
										</div>
				                 <el-space>
				                 	<div>
				                 		<span class="font-extraSmall">盘盈:</span><span class="text-green">{{scope.row.overamount}}</span>
				                 	</div>
				                 	<div>
				                 		<span class="font-extraSmall">盘亏:</span><span class="text-danger">{{scope.row.lossamount}}</span>
				                 	</div>
				                 </el-space>
									</template>
									</el-table-column >  
				  			</template>
				</GlobalTable>
				</el-card>
			</el-col>
		</el-row>
  		
  		<template #footer>
  			<span class="dialog-footer">
  				<el-button @click="dialog.visible = false">关闭</el-button>
  				 
  			</span>
  		</template>
  	</el-dialog>
  </div>    
</template>

<script setup>
	 import shelfproductApi from '@/api/erp/warehouse/shelfproductApi.js';
	  import stocktakingApi from '@/api/erp/inventory/stocktakingApi.js';
	import { ref, reactive, onMounted,toRefs ,watch} from 'vue'
	import { Search, ArrowDown, } from '@element-plus/icons-vue'
	import {CheckInputFloat,CheckInputInt} from '@/utils/index.js';
	import { ElDivider, ElMessageBox, ElMessage } from 'element-plus'
	import materialApi from '@/api/erp/material/materialApi.js';
	import GlobalTable from "@/components/Table/GlobalTable/index.vue";
	import warehouseApi from "@/api/erp/warehouse/warehouseApi.js";
	import shelfApi from '@/api/erp/warehouse/shelf.js';
	const state=reactive({
		tableData:{records: [],total: 0},
		tableData2:{records: [],total: 0},
		ids:[],
		dialog:{visible:false},
		loading:false,
		supplierid:"",
		queryParams:{warehouseid:'',sku:""}
	});
	const { loading,
			tableData,
			tableData2,
			queryParams,
			dialog,
			search,
			ids,
			 supplierid
		  } = toRefs(state);
	let props = defineProps({hasInput:false,isAssemblyItem:false,disable:false,isAssemblySKU:false,isfulfillable:false,warehouseid:"",type:""});
	const { hasInput,isAssemblyItem,isfulfillable,warehouseid,isAssemblySKU,type,disable } = toRefs(props);
	let globalTableRef = ref(GlobalTable);
	let globalTableRef2 = ref(GlobalTable);
	
	const emit = defineEmits(['change']);
	function handleClose(){
		state.dialog.visible=false;
	}
	function saveWarehouseItem(row){
		if(row!=""&& parseInt(row.amount)>=0){
			var data={};
			data.id=state.queryParams.stocktakingid;
			data.stocktakingid=state.queryParams.stocktakingid;
			data.warehouseid=row.warehouseid;
			data.materialid=row.materialid;
			data.amount=row.amount;
			if(row.itemid){
				data.itemid=row.itemid;
			}else{
				data.itemid="";
			}
			var fulfillable=0;
			var outbound=0;
			if(row["fulfillable"]) {
				fulfillable=row.fulfillable;
			}
			if(row["outbound"]) {
				outbound=row.outbound;
			}
			var overamount =parseInt(row.amount)- parseInt(fulfillable+outbound);
			data.overamount=overamount;
			stocktakingApi.saveItem(data).then((res)=>{
				ElMessage.success("添加成功！");
				loadData();
			});
		}
	}
	function saveShelfItem(row){
		var data={};
		data.stocktakingid=state.queryParams.stocktakingid;
		data.materialid=state.queryParams.materialid;
		data.shelfid=row.shelfid;
		data.amount=parseInt(row.amount);
		var quantity=parseInt(row.quantity);
		data.warehouseid=state.queryParams.warehouseid;
		if(data.amount>quantity){
			data.overamount=row.amount -quantity;
			data.lossamount=0;
		}else{
			data.overamount=0;
			data.lossamount=quantity-row.amount;
		}
		if(row.id){
			data.id=row.id;
			stocktakingApi.updateStock(data).then((res)=>{
				ElMessage.success('操作成功！');
			    pushrow();
				loadData() ;
			})
		}else{
			stocktakingApi.saveStock(data).then((res)=>{
				ElMessage.success('操作成功！');
				pushrow();
				loadData() ;
			})
		}
		
		
	}
	function pushrow(){
		var amount=0,fulfillable=0,outbound=0;
		state.tableData.records.forEach(item=>{
			if(item.overamount||item.lossamount){
			    amount=amount+parseInt(item.overamount)-parseInt(item.lossamount);
			}
		})
		if(state.queryParams.fulfillable) {
			fulfillable=state.queryParams.fulfillable;
		}
		if(state.queryParams.outbound) {
			outbound=state.queryParams.outbound;
		}
	   state.queryParams.amount= fulfillable+outbound+amount;
	   emit("change",  state.queryParams);
	}
	function loadData() {
		if(state.queryParams.addressid){
				loadDataByAddressId(res.data.addressid);
		}else{
			if(state.queryParams.stocktype==1){
					var warehouseid=state.queryParams.warehouseid;
					warehouseApi.detail(warehouseid).then(res=>{
						if(res.data){
							state.queryParams.addressid=res.data.addressid;
							loadDataByAddressId(res.data.addressid);
						}
					})
			}else{ 
				var shelfid=state.queryParams.warehouseid;
				shelfApi.detailShelf(shelfid).then(res=>{
					if(res.data){
						loadDataByAddressId(res.data.addressid);
					}
				})
			}
		}
	}
	function loadDataByAddressId(addressid){
		state.queryParams.searchtype="sku";
		var data={};
		data.materialid=state.queryParams.materialid;
		data.sku=state.queryParams.sku;
		data.addressid=addressid;
		data.stocktakingid=state.queryParams.stocktakingid;
		if (globalTableRef.value && globalTableRef.value["loadTable"]) {
			   globalTableRef.value.loadTable(data);
		} else{
			setTimeout(function() { globalTableRef.value.loadTable(data); }, 500); 
		}
		if (globalTableRef2.value && globalTableRef2.value["loadTable"]) {
					   globalTableRef2.value.loadTable(data);
		 } else{
			 setTimeout(function() {
				 globalTableRef2.value.loadTable(data); }, 500); 
		 }
	}
	function rowClick(row){
			  globalTableRef.value.toggleRowSelection(row,true);
	}
	
	function loadTableData(params) {
		 params.isstocking="true";
		 if(params.addressid){
			 shelfproductApi.getShelfInventoryStockingList(params).then(function(res) {
				if(res&&res.data){
					state.tableData.records = res.data.records;
					state.tableData.total = res.data.total;
				}else{
					state.tableData.records = [];
					state.tableData.total = 0;
				}
			 })
		 }
	 }
	 
	 function loadTableData2(params){
		 if(params.addressid){
			 stocktakingApi.searchCondition(params).then((res)=>{
			 	state.tableData2.records = res.data.records;
			 	state.tableData2.total =res.data.total;
			 });
		   }
	 }
	 
	function submitFunc() {
		if (state.ids.length > 0) {
			state.dialog.visible = false;
			emit("getdata",state.ids);
		} else {
			ElMessage.error('提交的行数不能小于1！')
			state.dialog.visible = true;
		}
	}
	function selectChange(selection) {
		state.ids = selection;
	}
	function show(params){
		if(params){
			state.queryParams=params;
		}else{
			state.queryParams.supplier="";
		}
		state.queryParams.addressid=null;
		state.dialog.visible=true;
	}
	function optQuantityChange(row){
		globalTableRef.value.toggleRowSelection(row,true);
	}
	defineExpose({
	  show,
	})
</script>
 
 

<style>
	.el-input__suffix {
		display: flex;
		align-items: center;
		font-size: 16px;
		padding-right: 8px;
	}
	.skuinventory .el-dialog__body{
		padding-top:4px;
		padding-bottom:4px;
	}
</style>
