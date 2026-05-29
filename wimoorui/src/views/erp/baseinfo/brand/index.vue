<template>
    <div class="main-sty">
        <div class="con-header">
            <el-row>
                <el-space >
                    <el-button @click="Add" type="primary" class="im-but-one">
                        <plus theme="outline" size="18" fill="#fff" :strokeWidth="4"/>
                        <span>添加品牌</span>
                    </el-button>
                    <!-- <el-dropdown trigger="click">
                        <el-button>
                            更多操作<el-icon class="el-icon--right"><arrow-down /></el-icon>
                        </el-button>
                        <template #dropdown>
                            <el-dropdown-menu >
                                <el-dropdown-item @click="uploadFile">导入</el-dropdown-item>
                                <el-dropdown-item>导出</el-dropdown-item>
                                <el-dropdown-item @click="Deletion">删除</el-dropdown-item>
                            </el-dropdown-menu>
                        </template>
                    </el-dropdown> -->
                    <el-input  v-model="searchKeywords" @input="loadData" placeholder="请输入品牌名称" clearable="true" class="input-with-select" >
                        <!-- <template #prepend>
                            <el-select v-model="selectlabel" placeholder="品牌名称" style="width: 110px">
                                <el-option label="品牌名称" value="1"></el-option>
                                <el-option label="创建人" value="2"></el-option>
                            </el-select>
                        </template> -->
                        <template #append>
                            <el-button @click.stop="loadData">
                                <el-icon style="font-size: 16px;align-itmes:center">
                                    <search />
                                </el-icon>
                            </el-button>
                        </template>
                    </el-input>
                </el-space>
                <div class='rt-btn-group'>
                    <el-button   class='ic-btn' title='帮助文档'>
                        <help theme="outline" size="16" :strokeWidth="3"/>
                    </el-button>
                </div>
            </el-row>
        </div>
        <!--表单-->
        <el-row>
            <GlobalTable ref="globalTable" :tableData="tableData" @selectionChange='handleSelect' :defaultSort="{ prop: 'opttime', order: 'descending' }"  @loadTable="loadTableData" :stripe="true" 
			height="calc(100vh - 198px)"
			>
            	<template #field>
               <!-- <el-table-column type="selection" width="38" /> -->
                <el-table-column prop="name"  label="品牌名称" sortable />
				<el-table-column prop="remark"  label="备注"   />
                <el-table-column prop="opttime"  label="操作时间" sortable />
                <el-table-column prop="operate"  label="操作" width="140" sortable >
                    <template #default="scope">
                        <el-button class='el-button--blue' @click="Edit(scope.row)">编辑</el-button>
                        <el-button class='el-button--blue' @click="Remove(scope.row)">删除</el-button>
                    </template>
                </el-table-column>
				</template>
            </GlobalTable>
        </el-row>

    </div>
	<el-dialog
	   v-model="dialogVisible"
	   title="编辑品牌"
	   width="400px"
	 >
	<el-form
	    ref="dataFormRef"
	    :model="formData"
	    :rules="rules"
	  >
	    <el-form-item label="品牌名称" prop="name">
	      <el-input v-model="formData.name" placeholder="请输入品牌名称" />
	    </el-form-item>
		<el-form-item label="品牌备注" prop="remark">
		  <el-input v-model="formData.remark" :rows="5" placeholder="请输入备注..." type="textarea" />
		</el-form-item>
	  </el-form>
	   <template #footer>
	     <span class="dialog-footer">
	       <el-button @click.stop="cancel">取消</el-button>
	       <el-button type="primary" @click.stop="submitForm">
	         提交
	       </el-button>
	     </span>
	   </template>
	 </el-dialog>
	 <!-- <el-dialog
	    v-model="uploadVisible"
	    title="导入品牌"
	    width="400px"
	  >
	  <el-upload
	      drag
	      action="https://run.mocky.io/v3/9d059bf9-4660-45f2-925d-ce80ad6c4d15"
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
	 	 <el-button type="success" plain>下载模板</el-button>
	 	 <div>
	      <el-button @click="uploadVisible = false">取消</el-button>
	      <el-button type="primary" @click="uploadVisible = false">
	        上传文件
	      </el-button></div></div>
	    </span>
	  </template>
	   </el-dialog> -->
	 
</template>
<script>
    export default{ name:"品牌管理" };
</script>
<script setup>
    import {MenuUnfold,Plus,SettingTwo,Help,Copy,MoreOne} from '@icon-park/vue-next';
    import {Search,ArrowDown,} from '@element-plus/icons-vue'
	import { ElMessage, ElMessageBox,ElForm } from 'element-plus'
	import {ref,reactive,toRefs,onMounted}from"vue";
	import brandApi from '@/api/erp/material/brandApi.js';
			// let uploadVisible =ref(false)
			let globalTable=ref();
			const dataFormRef = ref(ElForm);
			onMounted(()=>{
				loadData();
			})
			let state=reactive({
				tableData: {records:[],total:0},
				selectRows:[],
				searchKeywords:"",
				dialogVisible:false,
				formData: {
					name: '',
				}, 
				rules: {
					name: [{ required: true, message: '请输入供应商名称', trigger: 'blur' }],
				},
			})
			let{tableData,selectRows,searchKeywords,dialogVisible,
			formData,rules,}=toRefs(state)
			function Add(){
				state.dialogVisible = true;
				state.formData={};
			}
			function Edit(rows){
				state.dialogVisible = true;
				state.formData=rows;
			}
			function Remove(rows){
				ElMessageBox.confirm(
				   '确定要删除该条品牌信息吗',
				   '删除品牌',
				   {
				     confirmButtonText: '确认',
				     cancelButtonText: '取消',
				     type: 'warning',
				   }
				 )
				   .then(() => {
					   brandApi.delBrand({"id":rows.id.toString()}).then((res)=>{
					     ElMessage.success('删除成功');
					     loadData();
					   });
				   })
				   .catch(() => {
				     ElMessage.info('取消删除');
				   })
			}
			function uploadFile(){
				uploadVisible.value =true
			}
			function handleSelect(row){
				state.selectRows = row
			}
		 
			function loadData(){
				var data={};
				var searchs=state.searchKeywords;
				var search="";
				if(searchs=="" || searchs==undefined || searchs==null){
					search="";
				}else{
					search=searchs;
				}
				data.search=search;
				globalTable.value.loadTable(data);
			}
			function loadTableData(data){
				brandApi.list(data).then((res)=>{
					state.tableData.records = res.data.records
					state.tableData.total =res.data.total
				});
			}
			function cancel() {
			  state.dialogVisible = false;
			  state.formData.id = undefined;
			  dataFormRef.value.resetFields();
			}
			function submitForm(){
				  dataFormRef.value.validate((isValid) => {
					if (isValid) {
					  if (state.formData.id) {
						 brandApi.saveData(state.formData).then((res)=>{
						  ElMessage.success('修改成功');
						  cancel();
						  loadData();
						});
					  } else {
						brandApi.saveData(state.formData).then((res)=>{
						  ElMessage.success('新增成功');
						  cancel();
						  loadData();
						});
					  }
					}
				  });
			}
</script>
<style>
</style>