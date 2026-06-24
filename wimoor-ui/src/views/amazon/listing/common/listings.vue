<!-- 添加listing时，共用的产品弹窗 -->
<template>
  <div>
  	<el-dialog v-model="dialog.visible" class="listings" title="添加产品"  width="80%" top="6vh" :before-close="handleClose">
  		<div class="con-header">
  			<el-row>
				<el-space>
				<Group ref="groups" @change="getData" defaultValue="all"/>
  				<el-input v-model="queryParams.search" @input="loadData" placeholder="搜索产品SKU">
  					<template #suffix>
  						<el-icon :size="16">
  							<Search />
  						</el-icon>
  					</template>
  				</el-input>
				</el-space>
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
				<el-table-column label="店铺"  width="120"/>
				<el-table-column label="国家" width="120" />
  				<el-table-column prop="imgage" label="图片" width="60">
  					<template #default="scope">
  						<el-image v-if="scope.row.image" :src="scope.row.image" width="40" height="40"></el-image>
						<el-image v-else :src="$require('empty/noimage40.png')"   width="40" height="40"  ></el-image>
  					</template>
  				</el-table-column>
  				<el-table-column prop="sku" label="名称/SKU" show-overflow-tooltip>
  					<template #default="scope">
  						<div class='name'>{{scope.row.name}}</div>
  	 				<div class='sku'>{{scope.row.sku}} </div>
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
	import { ref, reactive, onMounted,toRefs ,watch} from 'vue'
	import { Search, ArrowDown, } from '@element-plus/icons-vue'
	import { ElDivider, ElMessageBox, ElMessage } from 'element-plus'
	import materialApi from '@/api/erp/material/materialApi.js';
	import GlobalTable from "@/components/Table/GlobalTable/index.vue";
	import Group from '@/components/header/group.vue';
	const state=reactive({
		tableData:{records: [],total: 0},
		ids:[],
		dialog:{visible:false},
		loading:false,
		queryParams:{ftype:'shipment',search:"",searchtype:"sku",issfg:""}
	});
	const { loading,
			tableData,
			queryParams,
			dialog,
			search,
			ids,
		  } = toRefs(state);
	let globalTableRef = ref(GlobalTable);
	const emit = defineEmits(['getdata']);
	function handleClose(){
		state.dialog.visible=false;
	}
	
	function loadData() {
		if (globalTableRef.value && globalTableRef.value["loadTable"]) {
			   state.queryParams.searchtype="sku";
			   globalTableRef.value.loadTable(state.queryParams);
		} 
	}
	
	function rowClick(row){
			  globalTableRef.value.toggleRowSelection(row,true);
	}
	
	function loadTableData(data) {
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
		if(params){
			state.queryParams=params;
		}
		state.dialog.visible=true;
		loadData();
	}
	function optQuantityChange(row){
		globalTableRef.value.toggleRowSelection(row,true);
	}
	defineExpose({
	  show,loadData
	})
    onMounted(()=>{
		loadData();
	})  
</script>

<style>
	.el-input__suffix {
		display: flex;
		align-items: center;
		font-size: 16px;
		padding-right: 8px;
	}
	.listings .el-dialog__body{
		padding-top:4px;
		padding-bottom:4px;
	}
</style>
