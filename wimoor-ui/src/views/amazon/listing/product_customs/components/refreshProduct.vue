<template>
		<el-dialog v-model="refreshProVisable" title="产品同步" destroy-on-close='true' width="800px"  >
				<el-row :gutter="32" style="padding-left: 15px;padding-right: 15px;">
					<Group  @change="groupChange" defaultValue="only"></Group>
					 <el-input style="margin-top: 10px;" type="text" placeholder="输入要同步的平台SKU" v-model="state.queryParams.sku"></el-input>
					 <el-button class="m-t-8" type="primary" @click.stop="submitProduct" >提交</el-button>
				</el-row>
		  <template #footer>
		  	<span class="dialog-footer">
		  		<el-button @click="refreshProVisable = false"> 关闭</el-button>
		  	</span>
		  </template>
		</el-dialog>
</template>

<script setup>
	import {ref,reactive,onMounted,toRefs } from "vue";
	import {ElMessage} from 'element-plus';
	import Group from '@/components/header/group.vue';
	import productRefreshApi from '@/api/amazon/product/productRefreshApi.js';
	let state=reactive({
		refreshProVisable:false,
		queryParams:{"sku":''},
	});
	const {
	  refreshProVisable,
	  queryParams
	} = toRefs(state);
	defineExpose({
	  refreshProVisable,
	})
	
	
	function groupChange(obj){
		state.queryParams.groupid=obj.groupid;
		state.queryParams.marketplaceid=obj.marketplaceid;
	}
	function submitProduct(){
		productRefreshApi.refreshItemBySKU(state.queryParams).then((res)=>{
			 if(res.data){
				 ElMessage.success( '同步成功！');
				 state.refreshProVisable=false;
			 }else{
				 ElMessage.error( '同步失败！');
			 }
		});
	}
	
	
</script>

<style>
</style>
