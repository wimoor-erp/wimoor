<template>
	<div class="main-sty">
		<div class="con-header">
			<el-row>
		<el-space>
			<el-button type="primary" class="im-but-one" @click.stop="addSupplier">
			  <plus theme="outline" size="18" fill="#fff" :strokeWidth="4"/>
			  <span>添加供应商</span>
			</el-button>
			<el-button @click.stop="uploadFile">导入</el-button>
			<el-button @click.stop="download" v-hasPerm="'erp:customer:download'">导出</el-button>
			<el-button @click.stop="removes">删除</el-button>
		<el-input  v-model="searchKeywords" @input="loadTable" clearable placeholder="请输入名称,联系人,电话或编码" style="width: 250px;" class="input-with-select" >
		   <template #append>
		     <el-button @click="loadTable" >
		        <el-icon class="ic-cen font-medium">
		         <search/>
		      </el-icon>
		     </el-button>
		   </template>
		 </el-input>
		 </el-space>
		 </el-row>
		 </div>
		 <div class="con-body">
			 <Table ref="tableRef" @handleEdit="handleEdit"/>
		 </div>
	</div>
	<el-dialog
	   v-model="uploadVisible"
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
		 <el-button type="success" @click.stop="downloadTemp"  plain>下载模板</el-button>
		 <div>
	     <el-button @click="uploadVisible = false">取消</el-button>
	     <el-button type="primary" :loading="loading" @click.stop="uploadExcel">
	       上传文件
	     </el-button></div></div>
	   </span>
	 </template>
	  </el-dialog>
</template>
<script>
    export default{ name:"供应商" };
</script>
<script setup>
	import {Search,ArrowDown,UploadFilled} from '@element-plus/icons-vue'
	import {Plus,} from '@icon-park/vue-next';
	import { ref,reactive,onMounted,toRefs} from 'vue'
	import Table from "./components/table.vue"
	import { ElMessage, ElMessageBox } from 'element-plus'
	import customerApi from '@/api/erp/material/customerApi.js';
	import downloadTemplateFileApi from '@/api/erp/common/downloadTemplateFileApi.js';
	let tableRef =ref()
	let headers=ref({"Content-Type": "multipart/form-data" });
	let state=reactive({
		uploadVisible:false,
		searchKeywords:'',
		loading:false,
		myfile:null,
	})
	let{uploadVisible,searchKeywords,myfile,loading}=toRefs(state)
	onMounted(()=>{
		loadTable();
	})
	function addSupplier(){
		tableRef.value.state.formData={};
		tableRef.value.state.formData.id = undefined;
		tableRef.value.state.dialogVisible=true;
	}
	function handleEdit(row){
		addSupplier()
	}
	function uploadFile(){
		state.uploadVisible = true
	}
	function loadTable(){
		tableRef.value.loadData(state.searchKeywords)
	}
	function download(){
		customerApi.downloadCustomerList();
	}
	//批量删除
	function removes(){
		if(tableRef.value.state.selectRows.length>0){
			var ids="";
			tableRef.value.state.selectRows.forEach(function(item){
				ids+=(item.id+",");
			});
			ElMessageBox.confirm(
			   '确定要批量删除勾选的供应商信息吗',
			   '删除供应商',
			   {
			     confirmButtonText: '确认',
			     cancelButtonText: '取消',
			     type: 'warning',
			   }
			 )
			   .then(() => {
						customerApi.deletecust({"ids":ids}).then((res)=>{
							 if(res.data=="OK"){
								 ElMessage.success( '批量删除成功');
								 tableRef.value.loadData(state.searchKeywords);
							 }else{
								 ElMessage.error('批量删除失败');
							 }
						});	
						 
			     
			   })
		}else{
			ElMessage.error('请选择供应商');
		}
	}
	function downloadTemp(){
		downloadTemplateFileApi.downExcelTemp({"ftype":'CustomerInfo'});
	}
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
		state.loading=true;
		customerApi.uploadCustomerFile(FormDatas).then(function(res){
			  ElMessage.success('上传成功');
			  state.uploadVisible = false;
			  state.loading=false;
			  tableRef.value.loadData(state.searchKeywords);
		})
		
	}
</script>

<style scoped="scoped">
	.font-large{
		font-size:48px;
		color: var(--el-color-info);
	}
</style>
