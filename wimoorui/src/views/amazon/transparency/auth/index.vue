<template>
	<div class="main-sty">
		 <div class="con-header" style="padding-bottom:20px">
			 <el-space>
			  <el-button type="primary" @click="handleAdd()">新增</el-button>
			 <el-input v-model="name" clearable>
				   <template #append>
				         <el-button :icon="Search" @click="handleQuery"  />
				       </template>
			 </el-input>
			 </el-space>
		 </div>
		  <div class="con-body" >
		 <el-table :data="tableData">
			 <el-table-column label="名称" prop="clientName"></el-table-column>
			 <el-table-column label="Client Id" prop="clientId"></el-table-column>
			 <el-table-column label="Client Secret" prop="clientSecret" show-overflow-tooltip=""></el-table-column>
			 <el-table-column label="创建时间" prop="createtime">
				 <template #default="scope">
				 		 {{dateFormat(scope.row.createtime)}} 
				 </template>
			 </el-table-column>
			 <el-table-column label="操作" prop="name">
				 <template #default="scope">
					<el-button link type="primary" @click="handleEdit(scope.row)">编辑</el-button>
				 	<el-button link type="danger"  @click="handleDelete(scope.row)">删除</el-button>
				 </template>
			 </el-table-column>
		 </el-table> 
		  </div>
	</div>
	
	<el-dialog
	    v-model="dialogVisible"
	    title="授权信息"
	    width="500"
	    :before-close="handleClose"
	  >
	      <el-form :model="formData" label-width="auto" style="max-width: 600px">
	        <el-form-item label="名称">
	          <el-input v-model="formData.clientName" />
	        </el-form-item>
			<el-form-item label="Client Id">
			  <el-input v-model="formData.clientId" />
			</el-form-item>
			<el-form-item label="Client Secret">
			  <el-input v-model="formData.clientSecret" />
			</el-form-item>
		  </el-form>
	    <template #footer>
	      <div class="dialog-footer">
	        <el-button @click="dialogVisible = false">取消</el-button>
	        <el-button type="primary" @click="handleSave">
	          确认
	        </el-button>
	      </div>
	    </template>
	  </el-dialog>
</template>

<script setup>
	import { ref,reactive,onMounted,watch,toRefs } from 'vue';
	import { Search } from '@element-plus/icons-vue'
	import { ElMessage, ElMessageBox } from 'element-plus'
	import authApi from '@/api/amazon/transparency/authApi.js';
	import {dateFormat,formatFloat} from '@/utils/index.js';
	const emit = defineEmits(['change']);
	const state = reactive({
	 	 tableData:"",
		 name:"",
		 dialogVisible:false,
		 formData:{},
	 })
	const { tableData,name,formData,dialogVisible}=toRefs(state);
	function handleQuery(){
		authApi.listAuthority({"name":state.name}).then(res=>{
			state.tableData=res.data;
		})
	}
	function handleAdd(){
		state.formData={disabled:false};
		state.dialogVisible=true;
	}
	function handleEdit(row){
		state.formData=JSON.parse(JSON.stringify(row));
		state.formData.disabled=false;
		state.dialogVisible=true;
	}
	function handleSave(){
		 authApi.saveAuthority(state.formData).then(res=>{
		 	  ElMessage.success('保存成功！');
			  state.dialogVisible=false;
		 	  handleQuery();
		 })
		
	}
	function handleDelete(item){
		ElMessageBox.confirm(
		   '确定要删除该条信息吗',
		   '删除',
		   {
		     confirmButtonText: '确认',
		     cancelButtonText: '取消',
		     type: 'warning',
		   }
		 )
		   .then(() => {
			   item.disabled=true;
			   authApi.saveAuthority(item).then(res=>{
			  	  ElMessage.success('删除成功！');
			  	  handleQuery();
			  })
		   })
		   .catch(() => {
		     ElMessage.info('取消删除');
		   })
		
	}
	onMounted(()=>{
		handleQuery();
	})
	
	function handleClick(){
		
	}
</script>

<style>
</style>