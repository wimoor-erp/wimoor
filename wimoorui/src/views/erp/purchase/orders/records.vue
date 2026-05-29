<template>
	<el-dialog v-model="dialogVisable"
	title="近期采购记录"
	>
	<el-table border :data="tableData">
		<el-table-column label="单据编码" prop="number"></el-table-column>
		<el-table-column label="供应商"  prop="supplierName"></el-table-column>
		<el-table-column label="订单状态"  prop="auditstatus">
			<template #default="scope">
				<el-tag  v-if="scope.row.auditstatus==0" type="danger" >已取消</el-tag>
				<el-tag  v-if="scope.row.auditstatus==1" type="info" >待审核</el-tag>
				<el-tag  v-if="scope.row.auditstatus==2" type="warning" >审核通过</el-tag>
				<el-tag  v-if="scope.row.auditstatus==3" type="success" >已完成</el-tag>
				<el-tag  v-if="scope.row.auditstatus==4" type="info" >审核待下单</el-tag>
			</template>
		</el-table-column>
		<el-table-column label="数量"  prop="amount" ></el-table-column>
		<el-table-column label="单价"  prop="itemprice"></el-table-column>
		<el-table-column label="金额"  prop="orderprice"></el-table-column>
		<el-table-column label="收货详情"  prop="totalin">
			<template #default="scope">
			 {{scope.row.totalin}}/{{scope.row.amount}}
			</template>
		</el-table-column>
		<el-table-column label="付款详情"  prop="paystatus">
			<template #default="scope">
			 <el-tag  v-if="scope.row.paystatus==1" type="success"  effect="plain">已付清</el-tag>
			 <el-tag  v-else type="info"  effect="plain">未付清</el-tag>
			</template>
		</el-table-column>
		<el-table-column label="采购日期"  prop="createdate">
			<template #default="scope">
			 {{dateTimesFormat(scope.row.createdate)}}
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
		purchaselistApi.getdetail({"id":rowid}).then((res)=>{
			if(res.data && res.data.recordList && res.data.recordList.length>0){
				state.tableData=res.data.recordList;
			}
		});
	}
	defineExpose({
		show,
	})
</script>

<style>
</style>
