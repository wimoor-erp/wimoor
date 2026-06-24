<template>
	<div class="main-sty">
		<div class="con-header">
			<el-row>
		<el-space>
			<el-button type="primary" class="im-but-one" @click.stop="AddInboundOrder">
			  <plus theme="outline" size="18" fill="#fff" :strokeWidth="4"/>
			  <span>添加入库单</span>
			</el-button>
			<el-button @click.stop="handleDelete">删除</el-button>
			<el-button @click.stop="showUploadFile">导入</el-button>
			<Warehouse @changeware="getWarehouse" />
			<Datepicker ref="datepickers" @changedate="changedate" />
		<el-input  v-model="searchKeywords" placeholder="请输入入库编码或SKU" @input="handleQuery" class="input-with-select" >
		   <template #append>
		     <el-button @click="handleQuery">
		        <el-icon style="font-size: 16px;align-itmes:center">
		         <search />
		      </el-icon>
		     </el-button>
		   </template>
		 </el-input>
		 </el-space>
		  <div class='rt-btn-group'>
			<el-button @click.stop="download" >导出</el-button>
		 </div>
		 </el-row>
		 </div>
		 <div class="con-body">
			 <Table ref="tableRef" @selectrow="selectrow"/>
		 </div>
	</div>
	<el-dialog
	   v-model="uploadVisible"
	   title="导入入库单"
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
		 <el-button type="success" @click.stop="downloadTemp" plain>下载模板</el-button>
		 <div>
	     <el-button @click="uploadVisible = false">取消</el-button>
	     <el-button type="primary" @click.stop="uploadExcel">
	       上传文件
	     </el-button></div></div>
	   </span>
	 </template>
	  </el-dialog>
</template>

<script setup>
    import Warehouse from '@/components/header/warehouse.vue';
	import Datepicker from '@/components/header/datepicker.vue';
	import {Search,ArrowDown,UploadFilled} from '@element-plus/icons-vue';
	import {Plus,Drag} from '@icon-park/vue-next';
	import { ref,reactive,onMounted,toRefs} from 'vue';
	import Table from"./components/table.vue";
	import {useRouter } from 'vue-router';
	import { ElMessage, ElMessageBox } from 'element-plus';
	import inApi from '@/api/erp/inventory/inApi.js';
	const router = useRouter();
	let tableRef=ref();
    const state = reactive({
		uploadVisible:false,
		queryParam:{},
		searchKeywords:"",
		isload:true,
		selectrows:[],
		myfile:null,
    })
    const {
    	uploadVisible,queryParam,searchKeywords,isload,selectrows,myfile
    } = toRefs(state)
	
	function showUploadFile(){
		state.uploadVisible = true; 
	}
	
	function AddInboundOrder(){
		router.push({
			path:"/e/w/i",
			query:{
				title:'添加入库单',
				path:"/e/w/i",
			},
		})
	}
	function handleDelete(){
		 if(state.selectrows && state.selectrows.length>0){
			 ElMessageBox.confirm(
			    '删除后库存将会撤销，确定删除吗？',
			    '删除入库单',
			    {
			      confirmButtonText: '确定',
			      cancelButtonText: '取消',
			      type: 'warning',
			    }
			  )
			    .then(() => {
				  var ids="";
			 	  state.selectrows.forEach(function(item){
					  ids+=(item.id+",");
				  });
			      inApi.deleteData({"ids":ids}).then((res)=>{
			 				  if(res.data){
			 					  ElMessage.success(res.data);
								  handleQuery();
			 				  }else{
			 					  ElMessage.error( "操作失败");
			 				  }
			 			  });
			    })
			    .catch(() => {
			      ElMessage.info( '取消删除');
			    })
		 }else{
			 ElMessage.error("请选择至少一行数据！");
		 }
		 
		}
		function handleQuery(){
			state.queryParam.search=state.searchKeywords;
			if(!state.queryParam.warehouseid){
				state.queryParam.warehouseid="";
			}
			tableRef.value.load(state.queryParam);
			if(state.isload==true){
				state.isload=false;
			}
		}
		function getWarehouse(wid){
			state.queryParam.warehouseid=wid;
			handleQuery();
		}
		//日期改变
		function changedate(dates){
			state.queryParam.fromDate=dates.start;
			state.queryParam.toDate=dates.end;
			if(state.isload==false){
				handleQuery();
			}
		}
		 function selectrow(rows){
			 state.selectrows=rows;
		 }
	    function downloadTemp(){
			inApi.downExcelTemp();
		}
		//文件上传之前
		function beforeUpload(file){
			if (file.type != "" || file.type != null || file.type != undefined){
			    //截取文件的后缀，判断文件类型
				const FileExt = file.name.replace(/.+\./, "").toLowerCase();
				//计算文件的大小
				const isLt5M = file.size / 1024  < 5000; //这里做文件大小限制
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
		function uploadFiles(item){
			//上传文件的需要formdata类型;所以要转
			state.myfile=item.file;
		}
		function uploadExcel(){
			let FormDatas = new FormData();
			FormDatas.append('file',state.myfile);
			inApi.uploadExcel(FormDatas).then(function(res){
				if(res.data!="上传成功"){
					ElMessage.error( res.data);
				}else{
					ElMessage.success( '上传成功');
					state.uploadVisible = false;
					handleQuery();
				}
			})
		}
		
		function download(){
			inApi.getListDetailExport({"search":state.queryParam.search,"warehouseid":state.queryParam.warehouseid,
			"fromDate":state.queryParam.fromDate,"toDate":state.queryParam.toDate});
		}
</script>

<style scoped="scoped">
 .font-large{
	 font-size:32px;
	 color:#999;
 }
</style>
