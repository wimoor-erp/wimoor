<template>
	<el-button v-if="type" @click="show(type)">上传报告</el-button>
<el-dialog v-model="visible" title="报表上传"  width="400px" >
	<div class="text-red">请将您在卖家中心下载的报表转换成UTF-8格式的文本文件(制表符分隔)</div>
	 <el-upload
	     :drag="true"
	      action
		 :http-request="uploadFiles"
		 :limit="1"
		 :before-upload="beforeUpload" 
		 :show-file-list="true" 
		 :headers="headers" 
		 accept=".txt"
	     multiple
	   >
	     <el-icon class="font-large"><upload-filled /></el-icon>
	     <div class="el-upload__text">
	      拖拽文件到此处或 <em>点击上传</em>
	     </div>
	   </el-upload>
	   <el-descriptions column="1">
		   <el-descriptions-item label="店铺">
	         <GroupSelect @change="handleGroup" defaultValue='only'></GroupSelect>
		   </el-descriptions-item>
		   <el-descriptions-item label="区域">
	         <Region ref="regionRef" @change="handleRegion"></Region>
		   </el-descriptions-item>
	   </el-descriptions>
	 <template #footer>
	   <span class="dialog-footer">
			 <el-button @click="visible = false">取消</el-button>
			 <el-button type="primary" :loading="btnloading" @click.stop="uploadExcel">上传文件</el-button>
	   </span>
	 </template>
	  </el-dialog>
</template>
	
<script setup>
	    import {UploadFilled} from '@element-plus/icons-vue';
		import { ref,reactive,onMounted,toRefs} from 'vue';
		  import { ElMessage,ElLoading } from 'element-plus';
		import GroupSelect from "@/components/header/group_select.vue";
		import Region from "@/components/header/region.vue";
		import authApi from '@/api/amazon/auth/authApi.js';
		let state=reactive({visible:false,myfile:null,ftype:"",amazonauthid:"",templateFile:"",title:"",callback:null,btnloading:false,action:undefined});
		const{visible,myfile,title,btnloading,amazonauthid }=toRefs(state);
		let props = defineProps({ type:"",  });
		const {type} = toRefs(props);
		 const emit = defineEmits(['upload']);
		 const regionRef=ref();
		function show(ftype){
			state.visible=true;
			state.btnloading=false;
			state.ftype=ftype;
		}
		function hide(){
				state.visible=false;
				state.btnloading=false;
		}
		//文件上传之前
		function beforeUpload(file){
			if (file.type != "" || file.type != null || file.type != undefined){
			    //截取文件的后缀，判断文件类型
				const FileExt = file.name.replace(/.+\./, "").toLowerCase();
				//计算文件的大小
				const isLt5M = file.size / 1024  < 100000; //这里做文件大小限制
				//如果大于50M
				if (!isLt5M) {
					ElMessage.error(  '上传文件大小不能超过 100MB!!' )
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
		function handleGroup(groupid){
			regionRef.value.getData(groupid);
		}
		function handleRegion(amazonauthid){
			state.amazonauthid=amazonauthid;
		}
		function uploadExcel(){
			let FormDatas = new FormData()
			FormDatas.append('file',state.myfile);
			FormDatas.append('requesttype',state.ftype);
			FormDatas.append('amazonauthid',state.amazonauthid);
			state.btnloading=true;
			authApi.uploadReportFile(FormDatas).then(res=>{
				state.btnloading=false;
				ElMessage.success(  '上传成功' )
				emit("upload",FormDatas);
			}).catch(e=>{
				state.btnloading=false;
			});
			
		}
		function loading(){
			state.btnloading=true;
		}
		defineExpose({
			show,hide,loading
		})
</script>
	
<style scoped="scoped">
	.text-orange{
		color:var(--el-color-primary);
		font-size:12px;
		font-weight: 700;
	}
</style>