<template>
	<el-dialog v-model="visible"
	title="设置日均销量"
	top="3vh"
	width="30%"
	>
		<div>当前设置日期:{{dateFormat(localRow.weekDate)}}(第{{localRow.week}}周)</div>
		<div>
			<el-input style="width: 220px;margin-top: 15px;" @input="localRow.editquantity=CheckInputInt(localRow.editquantity)" v-model="localRow.editquantity" 
			 placeholder="请输入销量"></el-input>
		</div>
		<template #footer>
				  <div class="flex-center-between">
							  <div></div>
					  <div>
					  <el-button  @click="visible=false">关闭</el-button>
					  <el-button type="primary"    @click="saveSales">提交</el-button>
					  </div>
				  </div>
		</template>
	</el-dialog>
</template>

<script setup>
	import { ref,reactive,onMounted,toRefs, nextTick,} from 'vue'
	import {Edit,Warning,UploadFilled,ArrowDown,ArrowDownBold} from '@element-plus/icons-vue'
	import { ElMessage, ElMessageBox } from 'element-plus';
	import {formatFloat,CheckInputInt,dateFormat,dateTimesFormat} from '@/utils/index.js';
	import {save} from "@/api/amazon/listing/preSalesApi.js";
	
	const state = reactive({
		visible:false,
		localRow:{
			
		},
	})
	const {
		visible,
		localRow,
	} = toRefs(state)
	
	function saveSales(){
		var lists=[];
		var  date=new Date(dateFormat(state.localRow.weekDate));
		lists.push({"sku":state.localRow.sku,"marketplaceid":state.localRow.marketplaceid,
		"groupid":state.localRow.groupid,"amazonauthid":state.localRow.amazonauthid,
		"date":dateFormat(date),"quantity":state.localRow.editquantity});
		for(var i=1;i<7;i++){
			 date.setTime(date.getTime() + 3600 * 1000 * 24 );
			lists.push({"sku":state.localRow.sku,"marketplaceid":state.localRow.marketplaceid,
			"groupid":state.localRow.groupid,"amazonauthid":state.localRow.amazonauthid,
			"date":dateFormat(date),"quantity":0});
			date=new Date(dateFormat(date));
		}
		save(lists).then((res)=>{
			 ElMessage.success("保存成功");
			 state.localRow.quantity=state.localRow.editquantity;
			 state.visible=false;
		});
	}
	
	function show(row){
		state.localRow=row;
		state.localRow.editquantity=row.quantity;
		state.visible=true;
	}
	
	defineExpose({
		show,
	})
</script>

<style>
</style>