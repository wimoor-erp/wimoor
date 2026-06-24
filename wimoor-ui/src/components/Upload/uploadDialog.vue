<template>
<el-dialog v-model="visible" :title="title"  width="400px" >
	 <el-upload
	     :drag="true"
	      action
		 :http-request="uploadFiles"
		 :limit="1"
		 :before-upload="beforeUpload" 
		 :show-file-list="true" 
		 :headers="headers" 
		 :on-remove="removeloading"
		 :on-change="removeloading"
		 accept=".xls,.xlsx,.zip"
	     multiple
	   >
	     <el-icon class="font-large"><upload-filled /></el-icon>
	     <div class="el-upload__text">
	      拖拽文件到此处或 <em>点击上传</em>
	     </div>
	   </el-upload>
	 <template #footer>
	   <span class="dialog-footer">
		   <div class="flex-center-between">
		    <el-button v-if="state.title!='导入产品图片'" type="success" @click.stop="downloadTemp" plain>下载模板</el-button>
		    <span class="font-extraSmall" v-else>图片需对应SKU命名(sku.jpg/png),<br/>然后压缩文件为zip上传</span>
			 <div>
			 <el-button @click="visible = false">取消</el-button>
			 <el-button type="primary" :loading="btnloading" @click.stop="uploadExcel">上传文件</el-button>
			 </div>
		 </div>
	   </span>
	 </template>
	  </el-dialog>
</template>
	
<script setup>
	   import {UploadFilled} from '@element-plus/icons-vue';
		import { ref,reactive,onMounted,toRefs} from 'vue';
		import downloadTemplateFileApi from '@/api/erp/common/downloadTemplateFileApi.js';
		let state=reactive({visible:false,myfile:null,templateFile:"",title:"",callback:null,btnloading:false,action:undefined});
		const{visible,myfile,title,btnloading }=toRefs(state);
		 const emit = defineEmits(['upload']);
		function show(param){
			state.templateFile=param.template;
			state.title=param.title;
			state.visible=true;
			state.btnloading=false;
			state.callback=param.templateCallback;
			if(param.action){
				state.action=param.action;
			}
		}
		function hide(){
				state.visible=false;
				state.btnloading=false;
		}
		function downloadTemp(){
			   if(state.callback){
				   state.callback();
			   }
			   if(state.templateFile){
				   downloadTemplateFileApi.downExcelTemp({"ftype":state.templateFile},state.action);
			   }
				
		}
		//文件上传之前
		function beforeUpload(file){
			if (file.type != "" || file.type != null || file.type != undefined){
			    //截取文件的后缀，判断文件类型
				const FileExt = file.name.replace(/.+\./, "").toLowerCase();
				//计算文件的大小
				const isLt5M = file.size / 1024  < 50000; //这里做文件大小限制
				//如果大于50M
				if (!isLt5M) {
					ElMessage.error(  '上传文件大小不能超过 50MB!!' )
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
			state.btnloading=true;
			emit("upload",FormDatas);
		}
		function loading(){
			state.btnloading=true;
		}
		function removeloading(){
			state.btnloading=false;
		}
		defineExpose({
			show,hide,loading,removeloading
		})
</script>
	
<style scoped="scoped">
	.text-orange{
		color:var(--el-color-primary);
		font-size:12px;
		font-weight: 700;
	}
</style>