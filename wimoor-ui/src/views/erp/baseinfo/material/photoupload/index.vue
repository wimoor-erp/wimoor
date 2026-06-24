<template>
	<div style="padding: 20px;">
	<div style="margin-bottom: 10px;">
		当前SKU：{{sku}}
	</div>
	<el-descriptions style="width: 15%;">
		<el-descriptions-item label="产品图片上传" >
		<el-upload
				class="upload-demo" 
				action   
				:http-request="uploadFile" 
				 ref="upload"
				:limit="1" 
				:on-remove="handleRemove"  
				:file-list="fileList" 
				:on-exceed="handleExceed" 
				:before-upload="beforeUpload" 
				:show-file-list="true" 
				:headers="headers" 
				accept=".jpeg,.jpg,.png"
				>
				<!-- action="/api/file/fileUpload" -->
				<el-button class="btn"><i class="el-icon-paperclip"></i>选择图片</el-button>
				<template #tip>
				  <div class="el-upload__tip">
				    图片格式JPG/JPEG/PNG,小于5MB.
				  </div>
				</template>
			</el-upload>
			</el-descriptions-item>
	</el-descriptions >
	<div style="margin-top: 10px;">
		<el-button type="success" @click="uploadImage">提交</el-button>
		
	</div>
    </div>
</template>

<script>
	import { ref,reactive,onMounted } from 'vue'
	import {dateFormat} from '@/utils/index.js';
	import { useRoute } from "vue-router"
	import { ElMessage } from 'element-plus';
	import materialApi from '@/api/erp/material/materialApi.js';
	
	export default {
		name: 'photoUpload',
		components: {},
		setup(){
			onMounted(()=>{
				
			})
	        const route = useRoute()
	        const materialid=route.query.materialid;
			let sku=route.query.sku;
		    let headers=ref({"Content-Type": "multipart/form-data" })
			 //上传后的文件列表
			let fileList=ref([])
			let photofile=ref()
			const successMsg = () => {
			    ElMessage.success('操作成功！');
			}
			const errorMsg = () => {
				ElMessage.error('操作失败！')
			}
			
			//超出文件个数的回调
			function handleExceed(){
				 ElMessage.error('超出最大上传文件数量的限制！');
				 return
			}
			//上传文件的事件
			function uploadFile(item){
				//上传文件的需要formdata类型;所以要转
				photofile.value=item.file;
			}
			//文件上传之前
			function beforeUpload(file){
				if (file.type != "" || file.type != null || file.type != undefined){
				    //截取文件的后缀，判断文件类型
					const FileExt = file.name.replace(/.+\./, "").toLowerCase();
					//计算文件的大小
					const isLt5M = file.size / 1024  < 5120; //这里做文件大小限制
					//如果大于50M
					if (!isLt5M) {
						ElMessage.error('上传文件大小不能超过 5MB!!');
						return false;
					}
					else {
					   return true;
					}
				}
			}
			//上传了的文件给移除的事件，由于我没有用到默认的展示，所以没有用到
			function handleRemove(){
			}
			function uploadImage(){
				let FormDatas = new FormData()
				FormDatas.append('file',photofile.value);
				FormDatas.append('sku','B8180FPJJB');
				FormDatas.append('materialid','11988530289945005099');
				materialApi.uploadImage(FormDatas).then(function(res){
					if(res.data){
						if(res.data.isSuccess=="true"){
							successMsg();
						}else{
							errorMsg();
						}
					}
				})
			}
			return{
				 headers,fileList,photofile,handleRemove,beforeUpload,uploadFile,handleExceed,uploadImage,sku
			}
		}
	}
</script>

<style>
</style>
