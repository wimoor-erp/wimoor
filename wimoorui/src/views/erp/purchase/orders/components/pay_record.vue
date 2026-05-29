<template>
	<h5>付款记录 (已付总额:{{totalpay}})</h5>
	<el-card class="m-t-8" v-for="item in tableData" v-if="tableData.length>0">
		<div class="flex-center-between font-extraSmall">
			<span>{{NullTransform(item.methodname)}}</span>
			<span>{{item.opttime}}</span>
		</div>
		<div class="flex-center-between m-t-8" >
			<span class="font-bold">
				{{item.projectname}}
			</span>
			<span class="font-bold">￥{{item.payprice}}</span>
		</div>
		<div class="flex-center-between m-t-8" >
			<span class="font-extraSmall">
				操作人：{{item.operator}}
			</span>
			<el-button v-if="item.auditstatus==0"  size="small" disabled   >已撤销</el-button>
			<el-button v-else  size="small"  plain  @click.stop="cancelAccItem(item)">撤销</el-button> 
		</div>
		<div class="font-extraSmall">备注：{{item.remark}}</div>
	</el-card>
	 <el-empty v-else :image-size="100" description="暂无记录!"/>
</template>

<script setup>
	import {ref,reactive,onMounted,toRefs} from 'vue';
	import purchaselistApi from '@/api/erp/purchase/form/listApi.js';
    import { ElMessage, ElMessageBox } from 'element-plus';
	import NullTransform from"@/utils/null-transform";
	const emit = defineEmits(["change","loadpay"]);
	const state = reactive({
		tableData:[],
		ftype:"all",
		entryid:"",
		totalpay:0,
	})
	const {
		tableData,totalpay,
	}=toRefs(state)
	function show(table){
		 state.tableData=table;
		 if(state.tableData && state.tableData.length>0){
			 var amount=0;
			 state.tableData.forEach(item=>{
				 if(item.auditstatus!=0){
					amount=amount+item.payprice;
				 }
			 })
			 state.totalpay=amount;
			 emit("loadpay",state.totalpay);
		 }
	}
	 
	function cancelAccItem(row){
		ElMessageBox.confirm(
			'你确定要撤销该笔记录吗?',
			{
			  confirmButtonText: '确认',
			  cancelButtonText: '取消',
			  type: 'warning',
			  callback:(action)=>{
				 if(action=="confirm"){
						 purchaselistApi.cancelPayment({id:row.id}).then(res=>{
						   ElMessage.success('撤销成功');
						   emit("change");
						})  
				 }
			  }
			}
		  )
		
	}
	defineExpose({
		show,
	})
</script>

<style>
</style>
