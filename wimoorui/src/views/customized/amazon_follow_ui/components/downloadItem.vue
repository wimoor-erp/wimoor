<template>
	<el-dialog
	title="ASIN导出"
	v-model="visable"
	width="400px"
	>
			<el-form-item label="店 铺">
				<el-select style="width:100%;" v-model="groupid" @change="handleQuery">
					<el-option v-for="item in grouplist" :key="item.value"  :label="item.label" :value="item.value"></el-option>
				</el-select>
			</el-form-item>	 
	<template #footer>
		<el-button @click="visable=false">关闭</el-button>
		<el-button type="primary" @click="submitForm">提交</el-button>
	</template>
	</el-dialog>
</template>

<script setup>
	import {ref,reactive,toRefs,onMounted,}from "vue";
	import {ElMessageBox ,ElMessage } from 'element-plus';
	import authApi from '@/api/amazon/auth/authApi.js';
	const emit = defineEmits(['change']);
	const state=reactive({
		visable:false,
		grouplist:[],
		groupid:null,
	})
	const {
		visable,
		grouplist,
		groupid,
	}=toRefs(state)
	
	function submitForm(){
		emit("change",state.groupid);
		state.visable=false;
	}
	
	function show(){
		state.visable = true;
		authApi.getAuthMaketplace().then((res)=>{
			state.grouplist=res.data;
			if(res.data && res.data.length>0){
				state.groupid=res.data[0].value;
			}
		});
	}
	defineExpose({
		show,
	})
</script>

<style>
</style>