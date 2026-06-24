<!-- 添加产品时，共用的产品弹窗 -->
<template>
  <div>
  	<el-dialog v-model="dialog.visible" class="materialDialog" :title="queryParams.title"  width="80%" top="6vh" :before-close="handleClose">
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
  		<GlobalTable ref="globalTableRef" 
  		    :inDialog="true" 
			height="calc(100vh*0.55)"
  		    :tableData="tableData" 
  			@loadTable="loadTableData"
  			@selectionChange='selectChange' 
			@row-click="rowClick" 
  			border 
  			style="width: 100%;">
  			<template #field>
  				<el-table-column type="selection" :reserve-selection="false" width="38" />
  				<el-table-column prop="imgage" label="图片" width="60">
  					<template #default="scope">
  						<el-image v-if="scope.row.image" :src="scope.row.image" width="40" height="40"></el-image>
						<el-image v-else :src="$require('empty/noimage40.png')"   width="40" height="40"  ></el-image>
  					</template>
  				</el-table-column>
  				<el-table-column prop="sku" label="名称/SKU" show-overflow-tooltip>
  					<template #default="scope">
  						<div class='name'>{{scope.row.name}}</div>
  	 				    <div class='sku'>{{scope.row.sku}} 
						<el-tag v-if="scope.row.issfg=='1'" type="warning">组装成品</el-tag>
						</div>
  					</template>
  				</el-table-column>
  				<el-table-column prop="fulfillable" label="可用库存" width="100"  sortable="custom">
  					<template #default="scope">
  					    <div @click.stop="emptyClick">
							<el-popover
							    placement="top"
							    title="产品库存"
							    :width="250"
							    trigger="click"
								@before-enter="loadInventory(scope.row)"
							  >
							    <template #reference>
							     <span class="pointer">	
								 <div class='name'>{{scope.row.fulfillable}}</div>
										<span v-if="scope.row.willfulfillable>0">
											可组装:{{scope.row.willfulfillable}} 
										</span>
							     </span> 
										
							    </template>
								<el-table :data="inventoryList" >
									<el-table-column label="仓库名称" width="140" prop="name"></el-table-column>
									<el-table-column label="库存" prop="quantity"></el-table-column>
								</el-table>
							  </el-popover>
							</div>
  					</template>
  				</el-table-column>
  				<el-table-column prop="supplier" label="供应商">
					<template #default="scope">
					<el-link v-if="scope.row.supplier" @click.stop="openPurchase(scope.row.purchaseUrl)" >{{scope.row.supplier}}</el-link>
					<el-link v-else>-</el-link>
					<div ><span class="font-extraSmall">供货周期：</span>{{NullTransform(scope.row.delivery_cycle)}}</div>
					</template>
  				</el-table-column>
				<el-table-column prop="price" label="单价" width="80">
					<template #default="scope">
						<div @click.stop="emptyClick">
						<el-popover
						    placement="top"
						    title="阶梯采购价"
						    :width="200"
										  trigger="click"
										  @before-enter="getWisePriceList(scope.row)"
						  >
						    <template #reference>
						     <span v-if="scope.row.price" class="pointer text-orange"> ￥{{scope.row.price}}</span> 
										   <span v-else  class="pointer text-orange"> -</span> 
						    </template>
							<el-table :data="pricelist" v-loading="loading">
								<el-table-column label="起订量" prop="amount"></el-table-column>
								<el-table-column label="单价" prop="price"></el-table-column>
							</el-table>
						  </el-popover>
						  </div>
					</template>
				</el-table-column>
				<el-table-column prop="weight" label="品类">
					<template #default="scope">
									{{NullTransform(scope.row.category)}}
									<div class="font-extraSmall" >备注:{{NullTransform(scope.row.remark)}}</div>
					</template>
				</el-table-column>
				<el-table-column prop="weight" label="重量(kg)">
					<template #default="scope">
					<div v-if="scope.row.weight">{{NullTransform(scope.row.weight)}}</div>
					<div v-else>-</div>
					<div class="font-extraSmall" v-if="scope.row.length">{{scope.row.length}}*{{scope.row.width}}*{{scope.row.height}} cm</div>
					</template>
				</el-table-column>
				<el-table-column prop="operator" label="操作数量"  width="180"   v-if="hasInput" >
					<template #default="scope">
						 <el-input-number 
						   min=0 
						   :precision="0"
						   v-model.number="scope.row.optquantity" 
						   @change="optQuantityChange(scope.row)">
						 </el-input-number>
					</template>
				</el-table-column>
  			</template>
  	 </GlobalTable>
  		<template #footer>
  			<span class="dialog-footer">
  				<el-button @click="dialog.visible = false">取消</el-button>
  				<el-button type="primary" @click="submitFunc">确认</el-button>
  			</span>
  		</template>
  	</el-dialog>
  </div>    
</template>

<script setup> 
	import { ref, reactive, onMounted,toRefs ,watch, nextTick} from 'vue'
	import { Search, ArrowDown,Close } from '@element-plus/icons-vue'
	import { ElDivider, ElMessageBox, ElMessage } from 'element-plus'
	import materialApi from '@/api/erp/material/materialApi.js';
	import GlobalTable from "@/components/Table/GlobalTable/index.vue";
	import NullTransform from"@/utils/null-transform";
	import inventoryApi from '@/api/erp/inventory/inventoryApi.js';
	const state=reactive({
		tableData:{records: [],total: 0},
		ids:[],
		dialog:{visible:false},
		loading:false,
		pricelist:[],
		inventoryList:[],
		supplierid:"",
		queryParams:{ftype:'shipment',search:"",searchtype:"sku",issfg:"",title:"添加产品"}
	});
	const { loading,
			tableData,
			queryParams,
			dialog,
			search,
			pricelist,
			inventoryList,
			ids,
			 supplierid
		  } = toRefs(state);
	let props = defineProps({hasInput:false,isAssemblyItem:false,isAssemblySKU:false,isfulfillable:false,warehouseid:"",type:"",mainid:""});
	const { hasInput,isAssemblyItem,isfulfillable,warehouseid,isAssemblySKU,type,mainid } = toRefs(props);
	let globalTableRef = ref(GlobalTable);
	const emit = defineEmits(['getdata']);
	function handleClose(){
		state.dialog.visible=false;
	}
	
	function loadData() {
		if (globalTableRef.value && globalTableRef.value["loadTable"]) {
			   state.queryParams.searchtype="sku";
			   globalTableRef.value.loadTable(state.queryParams);
		} else{
			setTimeout(function() {   globalTableRef.value.loadTable(state.queryParams);}, 100); 
		}
	}
	function getWisePriceList(row){
		state.loading =true;
		materialApi.getWisePriceList({"mid":row.id}).then((res)=>{
			state.pricelist=res.data;
				state.loading =false
		});
	}
	function loadInventory(row){
		state.loading =true;
		inventoryApi.getInventoryByMaterial({"mid":row.id}).then((res)=>{
			state.inventoryList=res.data;
			state.loading =false
		});
	}
	function rowClick(row){
			  globalTableRef.value.toggleRowSelection(row,true);
	}
	function openPurchase(url){
		 window.open(url, '_blank');
	}
	function emptyClick(){
		
	}
	function loadTableData(data) {
		if(props.isAssemblyItem==true){
			data.issfg="0,2";
		}
		if(props.isAssemblySKU==true){
			data.issfg="1";
		}
		if(props.isfulfillable==true ){
			data.ftype="shipment";
		}else{
			data.ftype=undefined;
		}
		if(props.type&&props.type!=""){
			data.mtype=props.type;
		}
		if(props.warehouseid!=undefined && props.warehouseid!=null && props.warehouseid!=""){
				data.warehouseid=props.warehouseid;
		}else{
				data.warehouseid=undefined;
		}
		if(props.mainid!=undefined && props.mainid!=null && props.mainid!=""){
				data.mainid=props.mainid;
		}else{
				data.mainid=null;
		}
		data.withoutTags=true;
		data.withPriceHis=true;
		materialApi.getMaterialList(data).then((res) => {
				state.tableData.records = res.data.records;
				state.tableData.total = res.data.total;
		})
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
		state.dialog.visible=true;
		nextTick(()=>{
			if(params){
				state.queryParams=params;
			}else{
				state.queryParams.supplier="";
			}
			loadData();
		})
		
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
	.materialDialog .el-dialog__body{
		padding-top:4px;
		padding-bottom:4px;
	}
</style>
