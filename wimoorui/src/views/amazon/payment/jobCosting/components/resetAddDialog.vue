<template>
	<el-dialog v-model="visable" title="添加批次" top="5vh" width="80%">
		<div class="con-header">
			<el-row>
		<el-space>
		<WarehouseFba />
		<el-input  v-model="searchKeywords" @input="searchConfirm" placeholder="请输入" class="input-with-select" >
		  <template #prepend>
		    <el-select v-model="selectlabel" @change='searchTypeChange' style="width: 110px">
		      <el-option label="MSKU" value="sku"></el-option>
		      <el-option label="单据号" value="code"></el-option>
		      <el-option label="货件号" value="shipmentCode"></el-option>
		    </el-select>
		  </template>
		  <template #append>
		    <el-button @click="searchConfirm">
		       <el-icon class="ic-cen font-medium">
		        <search />
		     </el-icon>
		    </el-button>
		  </template>
		</el-input>
		</el-space>
		  </el-row>
		</div>
	    <GlobalTable
	    :defaultSort="defaultSort" 
	     @selectionChange='selectChange'
	     :tableData="tableData"
	     height="calc(100vh - 380px)"  @loadTable="loadTableData" >
	     <template #field>
	    	 <el-table-column type="selection" />
	       <el-table-column label="仓库名称" prop="warehouse"></el-table-column>
	    	<el-table-column label="批次号 "  prop="batch" />
	    	<el-table-column label="图片" prop="image" >
	    		<template #default="scope">
	    			<el-image class="product-image" :src="scope.row.image"></el-image>
	    		</template>
	    	</el-table-column>
	    	<el-table-column label="Msku"  prop="msku" >
	    	</el-table-column>
	    	<el-table-column label="入库日期"   prop="arriveTime"  sortable="custom">	</el-table-column>
	    	<el-table-column label="货件号" prop="shipmentCode" >	</el-table-column>
	    	<el-table-column label="数量"   prop="number" >	</el-table-column>
	    	</template>
	    </GlobalTable>
		 <template #footer>
		        <el-button @click="visable = false">取消</el-button>
		        <el-button type="primary" @click="handleConfirm">
		         确认
		        </el-button>
		    </template>
	  </el-dialog>
</template>

<script setup>
	import { ref,reactive,onMounted,toRefs} from 'vue'
	import { ElMessage, ElMessageBox} from 'element-plus'
	import {Search,} from '@element-plus/icons-vue'
	import WarehouseFba from '@/components/header/warehouse_fba.vue';
	import $require from '@/utils/system/require.js';
	const state=reactive({
		tableData:{
			records:[
			{
				warehouse:'AB-NA北美仓',
				batch:'PC20220011',
				shipTime:'2023-02-23',
				image:$require('testpic.png'),
				msku:'A2008FPJYB',
				arriveTime:'2023-03-23',
				orderCode:'PF202211140008',
				shipmentCode:'PF202211140008',
				number:'21',
				price:'9.84',
				sumPrice:'206.64',
				InitialCost:'43.25',
				SumInitialCost:'908.25',
			}
		],total:1},
	  selectlabel:"sku",
	  visable:false,
	  selectRow:[],
	})
	const {
		visable,
		selectlabel,
		tableData,
		selectRow,
	}=toRefs(state)
	const emit = defineEmits([
		'getRowData'
	])
	function show(){
		state.visable = true
	}
	function handleAdd(){
		state.tableData.push({
			name:'',
		})
	}
	function handleDelete(index){
		state.tableData.splice(index)
	}
	
	function handleConfirm(){
	     emit("getRowData",state.selectRow)
		 state.visable = false
	}
	function selectChange(selection){
		state.selectRow = selection
	}
	defineExpose({
		show,
		selectChange,
	})
</script>

<style>
	.m-b-8{
		margin-bottom: 8px;
	}
</style>
