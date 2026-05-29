  <template>
	<div class="main-sty">
		<div class="con-header">
	    <InvHeader ref="headerRef" @getdata="changedata" :tableData="selectrows"></InvHeader>
		</div>
		<div class="con-body">
		<GlobalTable ref="globalTable" :defaultSort="defaultSort"  @selectionChange='selectChange' :tableData="tableData" height="calc(100vh - 300px)" :stripe="true"  @loadTable="loadTableData" style="width: 100%;margin-bottom:16px;">
				<template #field>
				<el-table-column fixed width="38" >
				<template #default="scope">
						<el-checkbox v-model="scope.row.ischeck" @change="handleCheckChange(scope.row)"></el-checkbox>
					</template>
				</el-table-column>
				<el-table-column label="图片" prop="image" width="70">
					<template #default="scope">
						<el-image :src="scope.row.image" style="width:45px;height:45px"></el-image>
					</template>
				</el-table-column>
				<el-table-column label="名称/SKU" prop="name" show-overflow-tooltip>
				        <template #default="scope">
							<div>{{scope.row.name}}</div>
							<div class="font-extraSmall">{{scope.row.sku}}</div>
						</template>
				</el-table-column>
				<el-table-column label="品类" prop="category" width="120">	 
				</el-table-column>
				<el-table-column label="可用库存" prop="qty" width="120" sortable="custom">
				</el-table-column>
				<el-table-column label="待出库" prop="outbound" width="120" sortable="custom">	 
				</el-table-column>
				<el-table-column label="当月库存周期" prop="quantity" width="120" sortable="custom">
					 <template #default="scope">
						<div>{{formatFloat(scope.row.quantity)}}</div>
					 </template>
				</el-table-column>
				<el-table-column label="采购成本" prop="price" width="120" sortable="custom">
					<template #default="scope">
						 <div>¥ {{formatFloat(scope.row.price)}}</div>
					</template>
				</el-table-column>
				<el-table-column label="箱规" prop="boxnum" width="120">
				</el-table-column>
				<el-table-column label="单箱尺寸" prop="quantity" width="120" sortable="custom">
					<template #default="scope">
						 <div v-if="scope.row.plength">
							 {{scope.row.plength}}*{{scope.row.pwidth}}*{{scope.row.pweight}}
						 </div>
						 <div v-else>--</div> 
					</template>
				</el-table-column>
				<el-table-column label="箱数" prop="qty" width="120">
					<template #default="scope">
								<div v-if="scope.row.boxnum&&scope.row.boxnum>0">{{formatFloat(scope.row.qty/scope.row.boxnum)}}</div>
					</template>
				</el-table-column>
				<el-table-column label="单箱体积cm³" prop="quantity" width="120">
					<template #default="scope">
						 <div v-if="scope.row.plength">{{scope.row.plength*scope.row.pwidth*scope.row.pweight}}</div>
						 <div v-else>--</div> 
					 </template>
				</el-table-column>
				<el-table-column label="单箱体积m³" prop="quantity" width="120">
					<template #default="scope">
						<div v-if="scope.row.plength">{{scope.row.plength*scope.row.pwidth*scope.row.pweight/1000000}}</div> 
						<div v-else>--</div> 
					 </template>
				</el-table-column>
				<el-table-column label="操作" prop="price" width="120">
					<template #default="scope">
						  <el-button type="primary" link @click="showInvoiceDailog(scope.row)" >开票</el-button>
					</template>
				</el-table-column>
				</template>
			</GlobalTable>
		</div>
		</div>
		<InvoiceDialog ref="invoiceDialogRef"></InvoiceDialog>
</template>
<script>
    export default{ name:"海外仓库存" };
</script>
<script setup>
	import InvHeader from "./components/inv_header.vue";
	import InvoiceDialog from "./components/invoice_dialog.vue";
	import {ref,reactive,toRefs,onMounted} from"vue"
	import {MoreOne,} from '@icon-park/vue-next';
	import {Histogram} from '@element-plus/icons-vue'
	import { ElMessage, ElMessageBox, ElDivider } from 'element-plus'
	import {useRouter } from 'vue-router';
	import {dateFormat,dateTimesFormat,formatFloat} from '@/utils/index.js';
	import orderApi from "@/api/erp/order/orderApi.js";
	const router = useRouter();
	let globalTable=ref();
	let headerRef=ref();
	let invoiceDialogRef=ref();
	const state = reactive({
		tableData:{records:[],total:0},
		queryParams:{},
		defaultSort:{"prop": 'opttime', "order": 'desc' },
		selectrows:[],
	})
	const {
		tableData,
		queryParams,
		defaultSort,
		selectrows,
	}=toRefs(state)
	 
	function changedata(params){
		state.queryParams.paramother=params;
		state.queryParams.sort=state.defaultSort.prop;
		state.queryParams.order=state.defaultSort.order;
		globalTable.value.loadTable(state.queryParams);
	}
	function deletes(row){
		orderApi.remove({"id":row.id}).then((res)=>{
			ElMessage.success("删除成功");
			globalTable.value.loadTable(state.queryParams);
		})
	}
	function selectChange(selection) {
		state.selectrows=selection;
	}
	function showInvoiceDailog(row){
		invoiceDialogRef.value.show(row);
	}
	function loadTableData(params){
		orderApi.findMaterialByCondition(params).then((res)=>{
			state.tableData.records = res.data.records;
			state.tableData.total =res.data.total;
			if(state.tableData.records){
				state.tableData.records.forEach(item=>{
					item.ischeck=item.ischeck?true:false;
				})
			}
		})
	}
	function handleCheckChange(row){
		if(row.ischeck){
		   orderApi.set({"materialId":row.id,"operate":"add"});
		}else{
		   orderApi.set({"materialId":row.id,"operate":"delete"});
		}
	}
	onMounted(() => {
	   
	});
</script>

<style>
</style>
