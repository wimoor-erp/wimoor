<template>
<el-dialog v-model="visible" title="导入盘点产品"  width="400px" >
	 <el-upload
	     :drag="true"
	      action
		 :http-request="uploadFiles"
		 :limit="1"
		 :before-upload="beforeUpload" 
		 :show-file-list="true" 
		 :headers="headers" 
		 accept=".xls,.xlsx"
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
		    <el-button   type="success" @click.stop="downloadTemp" plain>下载模板</el-button>
			 <div>
			 <el-button @click="visible = false">取消</el-button>
			 <el-button type="primary" @click.stop="uploadExcel">上传文件</el-button>
			 </div>
		 </div>
	   </span>
	 </template>
	  </el-dialog>
</template>
	
<script setup>
	   import {UploadFilled} from '@element-plus/icons-vue';
		import { ref,reactive,onMounted,toRefs} from 'vue';
		import stocktakingApi from '@/api/erp/inventory/stocktakingApi.js';
		let props = defineProps({ indialog:""})
		const { indialog} = toRefs(props);
		let state=reactive({visible:false,myfile:null,id:null,});
		const{visible,myfile,id }=toRefs(state);
		 const emit = defineEmits(['upload']);
		function show(id){
			state.visible=true;
			state.id=id;
		}
		function hide(){
			state.visible=false;
		}
		function downloadTemp(){
			if(props.indialog){
				stocktakingApi.downloadWarehouseList({"warehouseid":state.id});
			}else{
			    stocktakingApi.downloadStockingList({"stockid":state.id});
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
					ElMessage.error( '上传文件大小不能超过 50MB!!');
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
			FormDatas.append('stockid',state.id);
			emit("upload",FormDatas);
		}
		
		defineExpose({
			show,hide
		})
</script>
	
<style scoped="scoped">
	.text-orange{
		color:var(--el-color-primary);
		font-size:12px;
		font-weight: 700;
	}
</style>