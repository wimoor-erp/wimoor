<template>
    <div style="padding-bottom:20px" class="flex-between">
		<el-space>
			<el-button type="primary" @click="handleAddShow">新增</el-button>
			<el-select v-model="queryParam.groupid"     placeholder="全部店铺"  >
				  <el-option key="" label="全部店铺" value=""></el-option>
			      <el-option  v-for="item in groupOption"   :key="item.id"  :label="item.name" :value="item.id"   >
			      </el-option>
			</el-select>
			<el-select v-model="queryParam.authid"     placeholder="全部授权"  >
			  <el-option key="" label="全部授权" value=""></el-option>
			  <el-option  v-for="item in authOption"   :key="item.clientId"  :label="item.clientName" :value="item.clientId"   >
			  </el-option>
			</el-select>
			<el-input v-model="queryParam.sku" clearable placeholder="请输入sku" >
				<template #append>
				      <el-button :icon="Search" @click="handleQuery"  />
				</template>
			</el-input>
		</el-space>
		<el-space>
			<el-button type="primary" @click="handleShowUpload">上传</el-button>
		</el-space>
	</div>
	<GlobalTable 
		ref="globalTable" 
		:tableData="tableData" 
		:defaultSort="{ prop: 'opttime', order: 'descending' }"  
		@loadTable="loadTableData" 
		:stripe="true"
		height="calc(100vh - 258px)"
	>
		<template #field>
	    <el-table-column prop="name"  label="图片" width="70" >
			<template #default="scope">
			        <el-image style="width:60px;height:60px" :src="scope.row.image"></el-image>
			    </template>
			</el-table-column>
		<el-table-column prop="sku"  label="名称/SKU"  show-overflow-tooltip="" >
		<template #default="scope">
		        <div class="name">{{scope.row.name}}</div>
				<div class="font-bold">{{scope.row.sku}}</div>
		    </template>
		</el-table-column>
		<el-table-column prop="groupname"  label="店铺" width="140"  />
		<el-table-column prop="client_name"  label="授权" width="140"  />
		<el-table-column prop="asin"  label="ASIN" width="120"   />
		<el-table-column prop="gtin"  label="GTIN" width="140"   >
		<template #default="scope">
					<div class="font-bold">{{scope.row.gtin}}</div>
			</template>
		</el-table-column>
		<el-table-column prop="codenum"  label="T-Code数(已使用)" width="140"   >
			<template #default="scope">
					<div >{{scope.row.codenum}}</div>
			</template>
		</el-table-column>
	    <el-table-column prop="opttime"  label="操作时间"  width="180" />
	    <el-table-column   label="操作" width="200"   >
	        <template #default="scope">
				<el-button  link type="primary" @click="handleApplyTcode(scope.row)">申请T-Code</el-button>
	            <el-button  link type="primary" @click="handleEdit(scope.row)">编辑</el-button>
				  <el-button  link type="danger" @click="handleDelete(scope.row)">删除</el-button>
	        </template>
	    </el-table-column>
		</template>
	</GlobalTable>
	
	<el-dialog
	    v-model="dialogVisible"
	    title="授权信息"
	    width="500"
	    :before-close="handleClose"
	  >
	      <el-form :model="formData" label-width="auto" style="max-width: 600px">
	        <el-form-item label="店铺">
	           <el-select v-model="formData.groupid"     placeholder="全部店铺"  >
	                 <el-option  v-for="item in groupOption"   :key="item.id"  :label="item.name" :value="item.id"   >
	                 </el-option>
	           </el-select>
	        </el-form-item>
			<el-form-item label="透明计划授权">
			  <el-select v-model="formData.authid"     placeholder="全部透明计划"  >
			        <el-option  v-for="item in authOption"   :key="item.clientId"  :label="item.clientName" :value="item.clientId"   >
			        </el-option>
			  </el-select>
			</el-form-item>
			<el-form-item label="SKU">
			  <el-input v-model="formData.sku" />
			</el-form-item>
			<el-form-item label="ASIN">
			  <el-input v-model="formData.asin" />
			</el-form-item>
			<el-form-item label="GTIN">
			  <el-input v-model="formData.gtin" />
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
	  
	  <el-dialog
	      v-model="dialogTaskVisible"
	      title="T-Code申请"
	      width="500"
	    >
	        <el-form :model="taskForm" label-width="auto" style="max-width: 600px">
	          <el-form-item label="店铺">
	             <el-select v-model="taskForm.groupid"  disabled   placeholder="全部店铺"  >
	                   <el-option  v-for="item in groupOption"   :key="item.id"  :label="item.name" :value="item.id"   >
	                   </el-option>
	             </el-select>
	          </el-form-item>
	  		<el-form-item label="透明计划授权">
	  		  <el-select v-model="taskForm.authid"   disabled  placeholder="全部透明计划"  >
	  		        <el-option  v-for="item in authOption"   :key="item.clientId"  :label="item.clientName" :value="item.clientId"   >
	  		        </el-option>
	  		  </el-select>
	  		</el-form-item>
	  		<el-form-item label="SKU">
	  		  <el-input v-model="taskForm.sku" disabled/>
	  		</el-form-item>
	  		<el-form-item label="ASIN">
	  		  <el-input v-model="taskForm.asin" disabled />
	  		</el-form-item>
	  		<el-form-item label="GTIN">
	  		  <el-input v-model="taskForm.gtin" disabled/>
	  		</el-form-item>
			<el-form-item label="数量">
			  <el-input-number v-model="taskForm.count" :min="1" :max="1000"   />
			</el-form-item>
	  	  </el-form>
	      <template #footer>
	        <div class="dialog-footer">
	          <el-button @click="dialogTaskVisible = false">取消</el-button>
	          <el-button type="primary" :loading="taskloading" @click="handleSaveTask">
	            确认
	          </el-button>
	        </div>
	      </template>
	    </el-dialog>
		<UploadDialog ref="uploadDialogRef"  @upload="handleUpload"></UploadDialog>
</template>

<script setup>
	import { ref,reactive,onMounted,watch,toRefs } from 'vue';
	import { Search } from '@element-plus/icons-vue';
	import UploadDialog from './uploadDialog.vue'
	import authApi from '@/api/amazon/transparency/authApi.js';
	import groupApi from '@/api/amazon/group/groupApi.js';
	import { ElMessage, ElMessageBox } from 'element-plus';
	import {dateFormat,formatFloat} from '@/utils/index.js';
	
	const emit = defineEmits(['change']);
	const uploadDialogRef=ref();
	const globalTable=ref();
	
	const state = reactive({
	 	 tableData:{records:[],total:0},
		 name:"",
		 queryParam:{},
		 taskForm:{},
		 taskloading:false,
		 dialogVisible:false,
		 dialogTaskVisible:false,
		 formData:{},
		 authOption:[],
		 groupOption:[],
	 });
	 
	const { tableData,name,queryParam,formData,dialogVisible,taskloading,
	dialogTaskVisible,authOption,groupOption,taskForm}=toRefs(state);
	
	function handleShowUpload(){
		uploadDialogRef.value.show({"title":"上传GITN","action":"/amazon/api/v1/summary/downExcelTemp","template":"transparency"});
	}
	function handleUpload(formData){
		authApi.upload(formData).then(res=>{
			ElMessage.success("上传成功");
			uploadDialogRef.value.hide();
			handleQuery();
		})
	}
	function handleAddShow(){
		state.dialogVisible=true;
		state.formData.groupid=state.groupOption&&state.groupOption.length>0?state.groupOption[0].id:'';
		state.formData.authid=state.authOption&&state.authOption.length>0?state.authOption[0].clientId:'';
	}
	
	function handleQuery(){
		globalTable.value.loadTable(state.queryParam);
	}
	function handleEdit(row){
			state.formData=JSON.parse(JSON.stringify(row));
			state.dialogVisible=true;
	}
	 
	function loadTableData(param){
		authApi.listSkuinfo(param).then(res=>{
			state.tableData.records=res.data.records;
			state.tableData.total=res.data.total;
		});
	}
	function handleApplyTcode(row){
		state.taskForm=JSON.parse(JSON.stringify(row));
		state.dialogTaskVisible=true;
	}
	function handleSaveTask(){
		state.taskloading=true;
		var data={"skuinfoid":state.taskForm.id,"gtin":state.taskForm.gtin,"count":state.taskForm.count};
		authApi.saveTask(data).then(res=>{
			state.taskloading=false;
			ElMessage.success('申请成功！');
			state.dialogTaskVisible=false;
		})
	}
	function handleSave(){
		state.formData.createtime=null;
		state.formData.opttime=null;
		authApi.saveSkuinfo(state.formData).then(res=>{
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
			   item.createtime=null;
			   item.opttime=null;
			   authApi.deleteSkuinfo(item).then(res=>{
			  	  ElMessage.success('删除成功！');
			  	  handleQuery();
			  })
		   })
		   .catch(() => {
		     ElMessage.info('取消删除');
		   })
		
	}
	function initLoadData(){
		groupApi.getAmazonGroup().then((res)=>{
			state.groupOption=res.data;
			handleQuery();
		});
		authApi.listAuthority({"name":""}).then(res=>{
			state.authOption=res.data;
		})
	}
	
	onMounted(()=>{
		initLoadData();
	})
</script>

<style>
</style>