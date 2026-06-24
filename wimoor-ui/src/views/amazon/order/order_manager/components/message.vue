<template>
	<!-- 订单详情 -->
	<el-dialog v-model="visible"  title="订单详情"  @close='closeDialog'>
		<div v-loading="loading" style="margin-bottom:20px">
			<div style="margin-bottom:10px">可操作列表</div>
			<el-space wrap><el-tag v-if="response&&response.links&&response.links.actions" v-for="item in response.links.actions">{{item.name}}</el-tag></el-space>
		</div>
		 <el-tabs tab-position="right"   class="demo-tabs">
		    <el-tab-pane label="订单细节确认（confirmCustomizationDetails）">	<el-input type="textarea" rows="20"></el-input></el-tab-pane>
		    <el-tab-pane label="确认送货详情（createConfirmDeliveryDetails）"></el-tab-pane>
		    <el-tab-pane label="创建法律公示（createLegalDisclosure）"></el-tab-pane>
		    <el-tab-pane label="创建差评消除（createNegativeFeedbackRemoval）"></el-tab-pane>
			<el-tab-pane label="确认订单详细（createConfirmOrderDetails）"></el-tab-pane>
			<el-tab-pane label="确认服务详细（createConfirmServiceDetails）"></el-tab-pane>
			<el-tab-pane label="创建售后保修（createWarranty）"></el-tab-pane>
			<el-tab-pane label="获取属性（getAttributes）"></el-tab-pane>
			<el-tab-pane label="创建数字访问密钥（createDigitalAccessKey）"></el-tab-pane>
			<el-tab-pane label="意想不到的问题（createUnexpectedProblem）"></el-tab-pane>
			<el-tab-pane label="发票提交（sendInvoice）"></el-tab-pane>
		  </el-tabs>
	 
		
		<template #footer>
			<span class="dialog-footer">
				<el-button @click="visible = false">关闭</el-button>
			</span>
		</template>
	</el-dialog>
</template>

<script setup>
	import {ref ,watch,reactive,onMounted,onUpdated,toRefs} from 'vue';
	import {formatFloat,dateFormat,dateTimesFormat} from '@/utils/index.js';
	import {ElMessage,ElLoading} from 'element-plus';
	import messageApi from "@/api/amazon/order/messageApi.js"
	import {View,Refresh} from '@element-plus/icons-vue'
	import {decodeText} from '@/utils/index.js';
	const emit = defineEmits(['change' ]);
	 let state=reactive({
	 	visible:false,
		loading:false,
		rowdata:{},
		response:{},
	 })
	 let{visible,rowdata,loading,response}=toRefs(state)
	function closeDialog(){
		state.visible=false;
	}
	 function show(row){
		 state.rowdata=row;
		 var data={"amazonOrderId":row.orderid,"amazonauthid":row.authid,"marketplaceId":row.marketplaceId};
		 state.loading=true;
		 messageApi.getMessagingActionsForOrder(data).then(res=>{
			 state.loading=false;
			 state.response=res.data;
		 })
	 	 state.visible=true;
	 }
	 defineExpose({
	   show,
	 })
</script>

<style>
</style>