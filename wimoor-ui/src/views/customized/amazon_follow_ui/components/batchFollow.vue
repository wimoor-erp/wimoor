<template>
	<el-dialog
	title="导入asin"
	v-model="visable"
	width="600px"
	>
	 <el-form :model="form" label-width="100px">
	    <el-form-item label="店铺">
	    <el-select   v-model="groupid"  >
	    	<el-option v-for="item in grouplist" :key="item.value"  :label="item.label" :value="item.value"></el-option>
	    </el-select>
	    </el-form-item>
	    <el-form-item label="跟卖时间">
	      <el-select v-model="timeid"  >
	      	<el-option v-for="item in timelist" :key="item.id" :label="item.fullname"   :value="item.id">
	      	</el-option>
	      </el-select>
	    </el-form-item>
	    <el-form-item label="文件">
	      <el-upload
	      		class="upload-demo" 
	      		action   
	      		:http-request="uploadFile" 
	      		ref="upload"
	      		:limit="1" 
	      		:show-file-list="true" 
	      		:headers="headers" 
	      		accept=".xlsx,.xls,.XLSX,.XLS"
	      		>
	      		<!-- action="/api/file/fileUpload" -->
	      		<el-button class="btn"><i class="el-icon-paperclip"></i>上传文件</el-button>
	      		<template #tip>
	      		  <div class="el-upload__tip">
	      		    仅支持XLSX,XLS文件
	      		  </div>
	      		</template>
	      	</el-upload>
		  
		  
		  
	    </el-form-item>
		<el-form-item class="font-extraSmall">
		<p>1,系统会自动去掉重复，包括：同一个站点，同一个国家不同的站点</p>
		<p>2,比上架时间最少提早一小时导入ASIN</p>
		<p>3,ASIN几分钟之内同步在亚马逊库存管理中等待上下架</p>
		<p>4,没有模板，<el-link type="primary" @click="downloadTemp" :underline="false">点击此处</el-link>下载模板文件</p>
		</el-form-item>
	  </el-form>
	<template #footer>
		<el-button @click="visable=false">取消</el-button>
		<el-button type="primary" @click="submitForm">确认</el-button>
	</template>
	</el-dialog>
</template>

<script setup>
	import {ref,reactive,toRefs,onMounted,}from"vue";
	import {ElMessageBox ,ElMessage } from 'element-plus';
	import followListApi from '@/views/customized/amazon_follow_ui/js/followListApi.js';
	import authApi from '@/api/amazon/auth/authApi.js';
	import followTimeApi from '@/views/customized/amazon_follow_ui/js/followTimeApi.js';
	let headers=ref({"Content-Type": "multipart/form-data" }) 
	const state=reactive({
		visable:false,
		groupid:null,
		timeid:null,
		grouplist:[],
		timelist:[],
		myfile:null,
	})
	const {
		visable,
		groupid,
		timeid,
		grouplist,
		timelist,
		myfile,
	}=toRefs(state)
	
	function show(){
		state.visable = true;
		authApi.getAuthMaketplace().then((res)=>{
			state.grouplist=res.data;
			if(res.data && res.data.length>0){
				state.groupid=res.data[0].value;
			}
		});
		followTimeApi.list().then((res)=>{
			if(res.data){
				res.data.forEach(item=>{
					item.fullname=item.name+"("+item.starttime+"-"+item.endtime+")"
				});
				if(res.data && res.data.length>0){
					state.timeid=res.data[0].id;
				}
			}
			state.timelist=res.data;
		});
	}
	function downloadTemp(){
		followListApi.downExcelTemp();
	}
	function submitForm(){
		let FormDatas = new FormData()
		if(state.myfile){
		    FormDatas.append('file',state.myfile);
		}else{
			FormDatas.append('file',{});
		}
		var marketplaceid=state.groupid.split("-")[0];
		var amazonauthid=state.groupid.split("-")[1];
		FormDatas.append("authid",amazonauthid);
		FormDatas.append('marketplaceid',marketplaceid);
		FormDatas.append('timeid',state.timeid);
		followListApi.uploadExcel(FormDatas).then((res)=>{
			ElMessage({
			  type: 'success',
			  message: '上传成功!',
			})
			state.visable=false;
		});
	}
	//上传文件的事件
	function uploadFile(item){
		//上传文件的需要formdata类型;所以要转
		state.myfile=item.file;
	}
	defineExpose({
		show,
	})
</script>

<style>
</style>