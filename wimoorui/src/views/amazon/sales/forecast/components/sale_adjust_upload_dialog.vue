<template>
	<el-dialog
	   v-model="dialog.visible"
	   title="导入供应商"
	   width="400px"
	 >
	 <el-upload
	     :drag="true"
	      action
		 :http-request="uploadFiles"
		 :limit="1"
		 :before-upload="beforeUpload" 
		 :show-file-list="true" 
		 :headers="headers" 
		 accept=".xls,.xlsx"
	   >
	     <el-icon class="font-large"><upload-filled /></el-icon>
	     <div class="el-upload__text">
	      拖拽文件到此处或 <em>点击上传</em>
	     </div>
	   </el-upload>
	 <template #footer>
	   <span class="dialog-footer">
		   <div class="flex-center-between">
		 <el-button type="success" @click.stop="handleDownloadTemp" plain :loading="downloading">下载模板</el-button>
		 <div>
	     <el-button @click="dialog.visible = false">取消</el-button>
	     <el-button type="primary" @click.stop="uploadExcel" :loading="uploading">
	       上传文件
	     </el-button></div></div>
	   </span>
	 </template>
	  </el-dialog>
</template>

<script setup>
	import {ref,reactive,toRefs,onMounted} from "vue"
	import {Search,ArrowDown,UploadFilled} from '@element-plus/icons-vue'
	import {downExcelTemp,uploadSalesExcel} from "@/api/amazon/listing/preSalesApi.js";
	import { ElMessage, ElMessageBox } from 'element-plus'
	const state=reactive({
		  dialog:{visible:false},myfile:undefined,uploading:false,downloading:false
	})
	const {
	   dialog ,myfile,loading,downloading,uploading
	} = toRefs(state);
	function handleDownloadTemp(){
		state.downloading=true;
		downExcelTemp(null,item=>{
			state.downloading=false;
		});
	}
    function show(){
		state.dialog.visible=true;
		state.uploading=false;
		state.downloading=false;
	}
	defineExpose({
	  show
	})
	//文件上传之前
	function beforeUpload(file){
		if (file.type != "" || file.type != null || file.type != undefined){
		    //截取文件的后缀，判断文件类型
			const FileExt = file.name.replace(/.+\./, "").toLowerCase();
			//计算文件的大小
			const isLt5M = file.size / 1024  < 500; //这里做文件大小限制
			//如果大于50M
			if (!isLt5M) {
				ElMessage.error('上传文件大小不能超过 500KB!!');
				return false;
			}
			else {
			   return true;
			}
		}
	}
	
	function uploadFiles(item){
		//上传文件的需要formdata类型;所以要转
		state.myfile=item.file;
	}
	function uploadExcel(){
		let FormDatas = new FormData()
		FormDatas.append('file',state.myfile);
		state.uploading=true;
		uploadSalesExcel(FormDatas).then(function(res){
		    state.uploading=false;
			 ElMessage.success('上传成功');
			 state.dialog.visible=false;
		}).catch(res=>{
			state.uploading=false;
		})
	}
</script>

<style>
</style>