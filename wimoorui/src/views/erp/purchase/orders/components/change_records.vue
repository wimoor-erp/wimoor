<template>
	<el-dialog v-model="dialogVisable"
	title="近期审核记录"
	>
	<el-table border :data="tableData">
		<el-table-column label="单据编码" width="180" prop="number">
			<template #default="scope">
			<div></div> {{scope.row.number}}
			<div><span class="font-extraSmall">备注:</span>{{scope.row.remark}}</div>
			</template>
		</el-table-column>
		<el-table-column label="供应商"  prop="supplier"></el-table-column>
		<el-table-column label="数量" width="80" prop="amount" ></el-table-column>
		<el-table-column label="单价" width="80" prop="itemprice"></el-table-column>
		<el-table-column label="总金额" width="120" prop="orderprice"></el-table-column>
		<el-table-column label="最后操作日期"  prop="opttime">
			<template #default="scope">
			<div></div> {{dateTimesFormat(scope.row.opttime)}}
			<div><span class="font-extraSmall">操作人:</span>{{scope.row.operator}}</div>
			</template>
		</el-table-column>
	</el-table>
	<template #footer>
		<el-button @click="dialogVisable=false">关闭</el-button>
	</template>
	</el-dialog>
</template>

<script setup>
	import {reactive,toRefs}from"vue";
	import {dateFormat,dateTimesFormat} from '@/utils/index.js';
	import purchaselistApi from '@/api/erp/purchase/form/listApi.js';
	const state = reactive({
		dialogVisable:false,
		tableData:[]
	})
	const {
		dialogVisable,
		tableData,
	}=toRefs(state)
	function show(rowid){
		state.dialogVisable =true;
		state.tableData=[];
		purchaselistApi.getEntryData({"id":rowid}).then((res)=>{
			if(res.data && res.data.length>0){
				state.tableData=res.data;
			}
		});
	}
	defineExpose({
		show,
	})
</script>

<style>
</style>
