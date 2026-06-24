<template>
<el-dialog v-model="visible" :title="title"  width="400px" >
	<el-form  label-width="auto">
	<el-form-item label="店铺">
	   <el-select v-model="formData.groupid"     placeholder="全部店铺"  >
	         <el-option  v-for="item in groupOption"   :key="item.id"  :label="item.name" :value="item.id"   >
	         </el-option>
	   </el-select>
	</el-form-item>
	<el-form-item label="透明计划授权">
	  <el-select v-model="formData.authid"     placeholder="全部透明计划"  >
	        <el-option  v-for="item in authOption"   :key="item.clientId"  :label="item.clientName" :value="item.clientId"   >
	        </el-option>
	  </el-select>
	</el-form-item>
	</el-form>
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
		import authApi from '@/api/amazon/transparency/authApi.js';
		import groupApi from '@/api/amazon/group/groupApi.js';
		import downloadTemplateFileApi from '@/api/erp/common/downloadTemplateFileApi.js';
		let state=reactive({visible:false,
		                    myfile:null,
							templateFile:"",
							title:"",
							callback:null,
		                    btnloading:false,
							formData:{},
							groupOption:[],
							authOption:[],
							action:undefined});
		const{visible,myfile,title,btnloading,formData,groupOption,authOption }=toRefs(state);
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
			initLoadData()
		}
		function initLoadData(){
			groupApi.getAmazonGroup().then((res)=>{
				state.groupOption=res.data;
				state.formData.groupid=state.groupOption&&state.groupOption.length>0?state.groupOption[0].id:'';
			});
			authApi.listAuthority({"name":""}).then(res=>{
				state.authOption=res.data;
				state.formData.authid=state.authOption&&state.authOption.length>0?state.authOption[0].clientId:'';
			})
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
			FormDatas.append('groupid',state.formData.groupid);
			FormDatas.append('authid',state.formData.authid);
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