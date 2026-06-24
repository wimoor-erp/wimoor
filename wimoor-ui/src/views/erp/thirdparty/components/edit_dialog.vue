<template>
	<el-dialog
	    v-model="addVisible"
	    title="编辑API"
	    width="800px"
	  >
	  <el-form  :model="form" label-width="auto" style="max-width: 600px">
		  <el-form-item label="系统">
		  	<el-select placeholder="请选择API系统" v-model="form.system" @change="handleSystemChange" >
		  		<el-option v-for="item in systemlist" :label="item.name" :key="item.id" :value="item.id"></el-option>
		  	</el-select>
		  </el-form-item>
			<el-form-item label="公司名称" >
				<el-input placeholder="公司名称" v-model="form.name"></el-input>
			</el-form-item>
			<el-form-item label="API连接" >
				<el-input placeholder="Api" v-model="form.api"></el-input>
			</el-form-item>
			<el-form-item label="应用ID"  v-show="systemitem.needkey">
				<el-input placeholder="AppKey" v-model="form.appkey" ></el-input>
			</el-form-item>
			<el-form-item label="应用密钥"  v-show="systemitem.needkey">
				<el-input placeholder="AppSecret"  v-model="form.appsecret" ></el-input>
			</el-form-item>
			<el-form-item  label="令牌" v-show="systemitem.needtoken">
				<el-input placeholder="Token"  v-model="form.token" ></el-input>
			</el-form-item>
			<el-form-item label="API文档连接">
				<el-input placeholder="API链接" v-model="form.url" ></el-input>
			</el-form-item>
		</el-form>
	  
	    <template #footer>
	        <el-button @click="addVisible = false">关闭</el-button>
	        <el-button type="primary" @click="addApi">
	          保存
	        </el-button>
	    </template>
	  </el-dialog>
</template>

<script setup>
	import {h, ref ,watch,reactive,onMounted,toRefs} from 'vue';
	import { ElMessage ,ElMessageBox} from 'element-plus';
	import thirdpartyApi from '@/api/erp/thirdparty/thirdpartyApi.js';
	const emit = defineEmits(['change']);
	let state=reactive({
		 addVisible:false,
		 form:{
			 "id":null,
			 "name":null,
			 "api":null,
			 "appkey":null,
			 "appsecret":null,
			 "token":null,
			 "url":null,
			 "system":null,
			 "needkey":false,
			 "needtoken":false,
		 },
		 systemitem:{},
		 systemlist:[],
	});
	let {
	   addVisible,form,systemlist,systemitem,
	} =toRefs(state);
	
	function addApi(){
		if(state.form.id){
			thirdpartyApi.update(state.form).then((res)=>{
				ElMessage.success("更新成功");
				state.addVisible=false;
				emit("change");
			});
		}else{
			thirdpartyApi.save(state.form).then((res)=>{
				ElMessage.success("添加成功");
				state.addVisible=false;
				emit("change");
			});
		}
		
	}
	function handleSystemChange(){
		if(state.systemlist){
			state.systemlist.forEach(item=>{
				if(item.id==state.form.system){
					state.systemitem=item;
				}
			})
		}
		
		
	}
	function loadSystem(){
		thirdpartyApi.getAllSupportSystem().then((res)=>{
			if(res.data){
				state.systemlist=res.data;
			}
		});
	}
	
	async function show(row){
		if(row&& row.id){
			await thirdpartyApi.info({"id":row.id}).then((res)=>{
				 state.form=res.data;
				 if(row.systemEntity){
					 state.systemitem=row.systemEntity;
				 }
				 
			});
		}
		loadSystem();
		state.addVisible=true;
	}
	defineExpose({ show });
</script>

<style>
</style>