<template>
	  <el-dialog
	    v-model="dialogVisible"
	    :title="title?title:'提示'"
	    width="30%"
	    :before-close="handleClose"
	  >
	   <el-space>
		  <div v-if="warning"><el-icon class="text-orange font-extraLarge"><WarningFilled /></el-icon></div>
		  <div>{{message}}</div>
		  
	   </el-space>
	    <template #footer>
	      <span class="dialog-footer">
			<span style="padding-right:20px;"><el-checkbox  v-model="noticehide" label="忽略提示" size="large" /></span>   
	        <el-button @click="handleCancel">取消</el-button>
	        <el-button type="primary" @click="handleConfirm">确认</el-button>
	      </span>
	    </template>
	  </el-dialog>
</template>

<script setup>
	import { ref,reactive,onMounted,toRefs } from 'vue';
	import {WarningFilled} from '@element-plus/icons-vue';
	 const emit = defineEmits(['change']);
	 let state =reactive({
	 	dialogVisible:false,
		title:"提示",
		type:"",
		noticehide:false,
		warning:false,
		message:"您确认要进行此操作吗？"
	 })
	 let{dialogVisible,title,noticehide,warning,message}=toRefs(state);
	 //let props = defineProps({ });
	// const {  } = toRefs(props);
	 var callback=null;
	 function show(type,title,message,mcallback){
		 var noticehide= sessionStorage.getItem("notice_"+type);
		 if(noticehide=="none"){
			 if(mcallback){
			 			 mcallback();
						  emit('change',"confirm");
			 }
			 return ;
		 }
		 state.dialogVisible=true;
		 state.title=title;
		 state.type=type;
		 if(type.indexOf("warning")>=0){
			 state.warning=true;
		 }
		 state.message=message;
		 callback=mcallback;
	 }
	 function handleCancel(){
		  state.dialogVisible = false;
		  emit('change',"cancel");
	 }
	 function handleConfirm(){
		 state.dialogVisible = false;
		 if(callback){
			 callback();
		 }
		 if(state.noticehide){
		     sessionStorage.setItem("notice_"+state.type,"none");
		 }
		 emit('change',"confirm");
	 }
	 defineExpose({
	   show
	 })	 
</script>

<style>
</style>