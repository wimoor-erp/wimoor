<template>
	 <el-dialog v-model="uploadVisiable"  top="3vh"  title="导入物流跟踪号">
		  <el-row>
			  <el-col :span="16">
	 		  <el-upload
	 		  		class="upload" 
	 		  		action   
	 		  		:http-request="uploadFile" 
	 		  		:limit="1" 
	 		  		:before-upload="beforeUpload" 
	 		  		:show-file-list="true" 
	 		  		:headers="headers" 
	 		  		accept=".xls,.xlsx"
	 		  		>
	 		  		<!-- action="/api/file/fileUpload" -->
	 		  		<el-button class="btn"><i class="el-icon-paperclip"></i>选择文件</el-button>
	 		  		<template #tip>
	 					   <el-link   style="margin-left:30px;" type="success" @click="downloadTemp"> 点击下载导入模板 </el-link>
	 		  		</template>
	 		  	</el-upload>
				</el-col>
				<el-col :span="7" >
				 <div style="float: right;"><el-button  type="primary" @click="uploadTransFile"  >上传</el-button ></div>
				 </el-col>
				 </el-row>
			
			<div style=" margin-bottom: 10px;margin-top:20px;">
				<el-space>
				 <h3 style="padding-right:20px;">导入记录</h3>
				<Datepicker ref="datepickers" @changedate="changedate" />
				<el-input  @change="handleQuery" 
				 v-model="queryParams.shipmentid"
				 placeholder="请输入货件编码查询"></el-input>
			</el-space>
			</div>
			<GlobalTable ref="globalTableRef" :tableData="tableData"  
			@loadTable="loadtableData" 
			:defaultSort="{ prop: 'opttime', order: 'descending' }" 
			:stripe="true" 
			:inDialog="true"
			 height="calc(45vh)" 
			 style="width: 100%;margin-bottom:16px;">
			  <template #field>
				<el-table-column prop="shipmentid" label="货件编码" width="200" />
				<el-table-column prop="status" label="提交状态" width="180" >
					<template #default="scope">
						<el-tag v-if="scope.row.status==0"  type="warning">待处理</el-tag>
						<el-tag v-if="scope.row.status==1"  type="success">已处理</el-tag>
						<el-tag v-if="scope.row.status==2"  type="danger">处理失败</el-tag>
					 </template>
					 </el-table-column>
				<el-table-column prop="opttime" label="上传时间"  >
						<template #default="scope">
							{{dateTimesFormat(scope.row.opttime)}}
						 </template>
				</el-table-column>
				<el-table-column prop="creator" label="创建人"   />
		</template>
		</GlobalTable>
	 	<template #footer>
	 	      <span class="dialog-footer">
	 	        <el-button @click="uploadVisiable = false">关闭</el-button>
	 	       
	 	      </span>
	 	 </template>
	 </el-dialog>
</template>
<script setup>
	import {h, ref ,watch,reactive,onMounted,toRefs} from 'vue'
	import {Search,ArrowDown,} from '@element-plus/icons-vue'
	import {MenuUnfold,Plus,SettingTwo,Help,Copy,MoreOne,ChartPie} from '@icon-park/vue-next';
	import GlobalTable from "@/components/Table/GlobalTable/index.vue";
	import inboundboxApi from '@/api/amazon/inbound/inboundboxApi.js';
	import {dateFormat,getCurrencyMark,dateTimesFormat} from '@/utils/index.js';
	import {ElMessage } from 'element-plus';
	import Datepicker from '@/components/header/datepicker.vue';
	 let headers=ref({"Content-Type": "multipart/form-data" }) ;
	 const globalTableRef =ref();
	let state=reactive({
		 uploadVisiable:false,
		 tableData:{records:[],total:0},
		 queryParams:{shipmentid:""},
		 myfile:null,
	});
	let {
	   queryParams,
	   uploadVisiable,
	   tableData,
	   myfile,
	} =toRefs(state);
	 
	 function downloadTemp(){
		inboundboxApi.downloadTraceuploadTemp();
	 }
	 
 function changedate(dates){
 	state.queryParams.fromDate=dates.start;
 	state.queryParams.toDate=dates.end;
	handleQuery();
 }
	//文件上传之前
	function beforeUpload(file){
		if (file.type != "" || file.type != null || file.type != undefined){
		    //截取文件的后缀，判断文件类型
			const FileExt = file.name.replace(/.+\./, "").toLowerCase();
			//计算文件的大小
			const isLt5M = file.size / 1024  < 50; //这里做文件大小限制
			//如果大于50M
			if (!isLt5M) {
				ElMessage.error( '上传文件大小不能超过 50KB!!');
				return false;
			}
			else {
			   return true;
			}
		}
	}
	function uploadFile(item){
		//上传文件的需要formdata类型;所以要转
		state.myfile=item.file;
	}
	function uploadTransFile(){
		let FormDatas = new FormData()
		FormDatas.append('file',state.myfile);
		inboundboxApi.uploadTraceuploadFile(FormDatas).then(function(res){
			  ElMessage.success('上传成功');
			  handleQuery();
		})
	}
	function handleQuery(){
		if(globalTableRef.value&& globalTableRef.value.loadTable){
	       globalTableRef.value.loadTable(state.queryParams);
		}else{
			var timer=setTimeout(function(){
			   globalTableRef.value.loadTable(state.queryParams);	
			clearTimeout(timer);
			},400);
		}
	}
	function loadtableData(params){
		inboundboxApi.recordList(params).then((res)=>{
			state.tableData.records=res.data.records;
			state.tableData.total=res.data.total;
		});
	}
	
	function show(){
		state.uploadVisiable=true;
	}
	
	defineExpose({
		show,
	})
	 
</script>

<style>
</style>