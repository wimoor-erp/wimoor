<template>
	 <el-dialog
	    v-model="visible"
		class="orderinouthis"
		top="1vh"
	    title="库存历史"
	  >
	      <el-tabs v-model="activeName" class="demo-tabs" @tab-click="handleClick">
	         <el-tab-pane label="入库记录" name="first">
				<div style="padding-bottom:10px">
					 <Datepicker ref="datepickers" @changedate="changedate" />
				</div>
				 <InboundList :inForm="true" ref="inboundListRef"></InboundList>
			 </el-tab-pane>
	         <el-tab-pane label="出库记录" name="second">
				 <div style="padding-bottom:10px">
				 	 <Datepicker ref="datepickers" @changedate="changedate" />
				 </div>
				 <OutboundList :inForm="true" ref="outboundListRef"></OutboundList>
			 </el-tab-pane>
	       </el-tabs>
	    <template #footer>
	      <div class="dialog-footer">
	        <el-button @click="visible = false">关闭</el-button>
	      </div>
	    </template>
	  </el-dialog>
</template>

<script setup>
	import {ref,reactive,toRefs,onMounted,nextTick} from"vue";
	import InboundList from "@/views/erp/warehouse/inbound/components/table.vue";
	import OutboundList from "@/views/erp/warehouse/outbound/components/table.vue";
	import Datepicker from '@/components/header/datepicker.vue';
	const state =reactive({ visible:false,activeName:"first" ,queryParam:{}});
	const { visible,activeName,queryParam }=toRefs(state);
	const outboundListRef=ref();
	const inboundListRef=ref();
	function changedate(dates){
		state.queryParam.fromDate=dates.start;
		state.queryParam.toDate=dates.end;
		handleQuery();
	}
	function handleQuery(){
		outboundListRef.value.load(state.queryParam);
		inboundListRef.value.load(state.queryParam);
	}
	function show(warehouseid){
		state.visible=true;
		state.queryParam.warehouseid=warehouseid
		nextTick(()=>{
			handleQuery();
		})
	}
	defineExpose({
		show,
	})
</script>

<style>
	.orderinouthis .el-dialog__body{
		padding-top:0px;
	}
</style>