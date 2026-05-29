<template>
	
			<el-row>
				<el-space>
					<el-button type="primary" class="im-but-one" @click="handleAdd">
					  <plus theme="outline" size="18" fill="#fff" :strokeWidth="4"/>
					  <span>添加代料单</span>
					</el-button>
					<el-button @click="upload">导入</el-button>
					<el-button @click="handleDelete">删除</el-button>
					<Datepicker ref="datepickers" @changedate="changedate" />
					<el-input  v-model="searchKeywords" placeholder="输入代料编码或SKU" @input="handleQuery" class="input-with-select" >
					   <template #append>
					     <el-button @click="handleQuery">
					        <el-icon style="font-size: 16px;align-itmes:center">
					         <search />
					      </el-icon>
					     </el-button>
					   </template>
					 </el-input>
					<!-- <el-button>重置</el-button> -->
				</el-space>
				<div class='rt-btn-group'>
					<el-button class='ic-btn' title='帮助文档'>
						<help theme="outline" size="16" :strokeWidth="3" />
					</el-button>
				</div>
			</el-row>
			<el-dialog
			   v-model="uploadVisible"
			   title="导入产品代料单"
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
	import {ref,reactive,onMounted,watch,h,toRefs} from 'vue'
	import {Help,Plus,MenuUnfold,SettingTwo} from '@icon-park/vue-next';
	import {ElMessage,ElDivider} from 'element-plus';
	import {Search,ArrowDown,UploadFilled} from '@element-plus/icons-vue';
	import Datepicker from '@/components/header/datepicker.vue';
	import { useRouter } from 'vue-router';
	import changeApi from '@/api/erp/inventory/changeApi.js';
	const emit = defineEmits(['getdata','deleterows']);
	const router = useRouter()
    const state =reactive({
		uploadVisible:false,
		queryParam:{
			search:'',
			auditstatus:''
		},
		activeStatus:'',
		myfile:null,
		searchKeywords:'',
	})
	const {
		uploadVisible,
		queryParam,
		activeStatus,
		myfile,
		searchKeywords
	}=toRefs(state)
	function handleAdd(){
		router.push({
			path:"/e/w/e/c",
			query:{
				title:'添加代料单',
				path:"/e/w/e/c",
			},
		})
	}
	//导入
	function upload(){
		state.uploadVisible = true
	}
	function handleDelete(){
		emit('deleterows');
	}
	
	//日期改变
	function changedate(dates){
		state.queryParam.fromDate=dates.start;
		state.queryParam.toDate=dates.end;
		handleQuery();
	}
	function handleQuery(){
		state.queryParam.search=state.searchKeywords;
		emit('getdata',state.queryParam);
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
		changeApi.uploadExcel(FormDatas).then(function(res){
			if(res.data!="上传成功"){
				ElMessage.error( res.data);
			}else{
				ElMessage.success('上传成功');
				state.uploadVisible = false;
				handleQuery();
			}
		})
	}
	function downloadTemp(){
		changeApi.downExcelTemp();
	}
	onMounted(()=>{
		//handleQuery();
	});
	defineExpose({
		handleQuery,
	})
	
</script>

<style scoped="scoped">
	.font-48{
		font-size: 48px;
		    color: #999;
	}
</style>
