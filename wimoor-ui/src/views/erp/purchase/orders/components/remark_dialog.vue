<template>
	<el-dialog v-model="visable" title="批量修改产品备注" width="500px">
		<el-input v-model="remark" :rows="3"
    type="textarea"  placeholder="请输入备注" ></el-input>
		<template #footer>
				<el-button @click="submitRemarkAll"   type="primary">提交</el-button>
			<el-button @click="visable=false"  >关闭</el-button>
		</template>
	</el-dialog>
</template>

<script setup>
	import {h,ref,reactive,toRefs, onMounted,} from 'vue';
	import { ElMessage, ElMessageBox } from 'element-plus';
	import {dateFormat,formatFloat,dateTimesFormat,CheckInputFloat,CheckInputInt} from '@/utils/index.js';
	import purchaselistApi from '@/api/erp/purchase/form/listApi.js';
	
	const emit = defineEmits(['change']);
	const state = reactive({
		remark:"",
		visable:false,
		entryids:null,
	})
	const {
		remark,
		visable,
		feedRow,
		entryids,
	} = toRefs(state)
	 
	function submitRemarkAll(){
		purchaselistApi.updateNoticeAll({"entryids":state.entryids,"notice":state.remark}).then((res)=>{
			ElMessage.success('批量修改备注成功!');
			state.visable=false;
			emit("change");
		});
	}
	
	function show(ids){
		state.remark="";
		state.visable=true;
		state.entryids=ids;
	}
	
	defineExpose({
	   show,
	})
</script>

<style>
</style>