<template>
	 <div class="con-header">
	 <el-row>
			 <el-space >
				<el-button @click="addTran" type="primary" class="im-but-one">
				    <plus theme="outline" size="18" fill="#fff" :strokeWidth="4"/>
				    <span>添加物流渠道</span>
				</el-button>
				 <el-dropdown split-button type="default" @click="uploadVisiable = true;uploadtype='add';title='导入'">
				      导入
				      <template #dropdown>
				        <el-dropdown-menu>
				          <el-dropdown-item @click="uploadVisiable = true;uploadtype='replace';title='覆盖导入'">覆盖导入</el-dropdown-item>
				        </el-dropdown-menu>
				      </template>
				    </el-dropdown>
				
				<el-button @click="downloadVisiable = true">导出</el-button>
				<el-button @click="removeTran">删除</el-button>
				<el-select v-model="isdelete"  @change='refreshTable' placeholder="显示状态" >
				    <el-option label="启用" :value="false"></el-option>
				    <el-option label="已删除" :value="true"></el-option>
				</el-select>
				<el-input  v-model="searchKeywords" @change='refreshTable' clearable placeholder="请输入" class="input-with-select" >
				    <template #prepend>
				        <el-select v-model="selectlabel"  placeholder="物流公司" style="width: 110px">
				            <el-option label="物流公司" value="1"></el-option>
				            <el-option label="公司简称" value="2"></el-option>
				        </el-select>
				    </template>
				    <template #append>
				        <el-button >
				            <el-icon  @click='refreshTable' style="font-size: 16px;align-itmes:center">
				                <search />
				            </el-icon>
				        </el-button>
				    </template>
				</el-input>
			 </el-space >
			<div class='rt-btn-group'>
			    <el-button   class='ic-btn' title='帮助文档'>
			        <help theme="outline" size="16" :strokeWidth="3"/>
			    </el-button>
			</div>	 
	 </el-row>
	 </div>
	 <el-row>
	 <el-col :span="24">
	 	<GlobalTable ref="globalTable" :tableData="tableData"  height="calc(100vh - 252px)"   @loadTable="loadTableData" :stripe="true" 
		 @selectionChange = "selectionChange">
	 	  <template #field>
	 	    <el-table-column type="selection" width="38" />
	 	    <el-table-column prop="name"  label="物流公司" sortable='custom' >
	 			<template #default="scope">
	 							<span>{{scope.row.name}}</span>
	 			</template>
	 		</el-table-column>
	 	    <el-table-column prop="api"  label="物流接口"  sortable='custom' >
			  <template #default="scope">
				<span v-if="scope.row.api">  <el-tag class="ml-2" type="success">已绑定</el-tag> </span>
				<span v-else><el-tag class="ml-2" type="info">未绑定</el-tag></span>
			  </template>
			</el-table-column>
	 		<el-table-column prop="uploadpath" label="报价单" sortable='custom' >
	 		   <template #default="scope">
	 			<span v-if="scope.row.uploadpath">  <el-tag class="ml-2" type="warning">已上传报价</el-tag> </span>
				<span v-else><el-tag class="ml-2" type="info">未上传</el-tag></span>
	 		  </template>
	 		</el-table-column>
	 	    <el-table-column prop="simplename"  label="公司简称"  sortable='custom' />
	 		<el-table-column prop="operator"  label="操作人"  sortable='custom' />
			<el-table-column prop="opttime"  label="操作时间" width="170"  />
	 	    <el-table-column prop="operate"  label="操作" width="140"  >
	 	        <template #default="scope">
	 	            <el-button class='el-button--blue' @click="showTranInfo(scope.$index, scope.row)">详情</el-button>
	 	            <el-button class='el-button--blue' @click="editTranInfo(scope.$index, scope.row)">编辑</el-button>
	 	        </template>
	 	    </el-table-column>
	     </template>
	 </GlobalTable>
  </el-col>
 </el-row>
 <el-dialog v-model="downloadVisiable"   title="下载">
		   <el-form-item label="最后修改日期">
		       <el-date-picker
		               v-model="mydate"
		               type="date"
		               placeholder="选择日期"
		             />
		   </el-form-item>
 	<template #footer>
 	      <span class="dialog-footer">
 	        <el-button @click="downloadVisiable = false">取消</el-button>
 	        <el-button type="primary" @click="downloadExcel"  >下载</el-button >
 	      </span>
 	 </template>
 </el-dialog>
 <el-dialog v-model="uploadVisiable"  width="400px" :title="title">
 		  <el-upload
 		  		class="upload" 
				:drag="true"
				 action
 		  		:http-request="uploadFile" 
 		  		:limit="1" 
 		  		:before-upload="beforeUpload" 
 		  		:show-file-list="true" 
 		  		:headers="headers" 
 		  		accept=".xls,.xlsx"
 		  		>
 		  		<!-- action="/api/file/fileUpload" -->
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
						 <el-button @click="uploadVisiable = false">取消</el-button>
						 <el-button type="primary" :loading="uploadloading"  @click="uploadTransFile">上传文件</el-button>
						 </div>
					 </div>
			  </span>
			</template>
 </el-dialog>
</template>

<script>
	import {MenuUnfold,Plus,SettingTwo,Help,Copy,MoreOne} from '@icon-park/vue-next';
	import {Edit,Delete,Search,UploadFilled} from '@element-plus/icons-vue'
	import { ref,reactive,onMounted } from 'vue'
	import {useRouter } from 'vue-router'
	import GlobalTable from "@/components/Table/GlobalTable/index.vue";
	import { ElMessage, ElMessageBox } from 'element-plus'
	import transportationApi from '@/api/erp/ship/transportationApi.js';
	import downloadTemplateFileApi from '@/api/erp/common/downloadTemplateFileApi.js';
	
	export default{
	    name: 'translist',
	    components:{ Plus,Edit,Delete,GlobalTable,Search,Help,UploadFilled,},
	    setup(){
			let globalTable=ref(GlobalTable);
			let tableData=reactive({records:[],total:0})
			let mydate=ref();
			let myfile=ref();
			let downloadVisiable=ref(false);
			let uploadVisiable=ref(false);
			let uploadloading=ref(false);
			let uploadtype=ref('add');
			let title=ref('导入');
			let myparams={'isdelete':false,search:""};
			let searchKeywords=ref("");
			let isdelete=ref(false);
			let router = useRouter();
		    let headers=ref({"Content-Type": "multipart/form-data" }) 
			onMounted(()=>{
				refreshTable();
			})
			function addTran(){
				router.push({
					path:'/transportation/edit',
					query:{
					  title:"添加物流",
					  path:'/transportation/edit',
					}
				})
			}
		 
			let selectRows = ref([])
			function selectionChange(val){
				selectRows.value =val
			}
			function removeTran(){
			 if(selectRows.value==''){
				ElMessage.error('请勾选要删除的信息!');
				return;
			}
				 ElMessageBox.confirm(
				    '您确定要删除该物流公司信息吗?',
				    '删除物流',
				    {
				      confirmButtonText: '确定',
				      cancelButtonText: '取消',
				      type: 'warning',
				    }
				  )
				    .then(() => {
						let ids="";
						selectRows.value.forEach((item)=>{
							if(ids==""){
							   ids=item.id;
							}else{
							   ids=ids+","+item.id;
							}
						})
					 let param={"ids":ids};
					 transportationApi.deleteCompany(param).then(()=>{
						 ElMessage.success( '删除成功');
						 refreshTable();
					 })  	
				  
				    })
				    .catch(() => {
				      ElMessage.info( '取消操作');
				    })
			}
			function refreshTable(){
				myparams.isdelete=isdelete.value;
				myparams.search=searchKeywords.value;
				globalTable.value.loadTable();
			}
			
			function loadTableData(param){
				param.isdelete =myparams.isdelete;
				param.search =myparams.search;
				transportationApi.getlist(param).then((res)=>{
						tableData.records = res.data.records
						tableData.total =res.data.total
					})
			}
			function editTranInfo(index,row){
				router.push({
						path:'/transportation/edit',
						query:{
						  id: row.id,
						  title:"编辑物流",
						  path:'/transportation/edit',
						}
					})
				}
			function downloadExcel(){
				transportationApi.downloadList({'search':myparams.search,'mydate':mydate.value});
				downloadVisiable.value = false
			}
		 
			function showTranInfo(index,row){
				router.push({
					path:'/transportation/details',
					query:{
					  id: row.id,
					  title:"物流详情",
					  path:'/transportation/details',
					}
				})
			}
			function uploadTransFile(){
				let FormDatas = new FormData()
				FormDatas.append('file',myfile.value);
				FormDatas.append('type',uploadtype.value);
				uploadloading.value=true;
				transportationApi.uploadTransFile(FormDatas).then(function(res){
					  ElMessage.success('上传成功');
					  uploadloading.value=false;
					  uploadVisiable.value = false;
					  refreshTable();
				}).catch(e=>{
					uploadloading.value=false;
				})
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
						ElMessage.error('上传文件大小不能超过 50KB!!');
						return false;
					}
					else {
					   return true;
					}
				}
			}
			function uploadFile(item){
				//上传文件的需要formdata类型;所以要转
				myfile.value=item.file;
			}
			function downloadTemp(){
				downloadTemplateFileApi.downExcelTemp({"ftype":'transdetail'});
			}
	        return{loadTableData,globalTable,tableData,refreshTable,searchKeywords ,isdelete,
			addTran,showTranInfo,selectRows,removeTran,
			selectionChange,editTranInfo,mydate,uploadloading,
			downloadVisiable,uploadVisiable,uploadtype,title,
			downloadExcel,uploadTransFile,downloadTemp,beforeUpload,uploadFile
			}
	    }
	
	}
</script>

<style>
</style>