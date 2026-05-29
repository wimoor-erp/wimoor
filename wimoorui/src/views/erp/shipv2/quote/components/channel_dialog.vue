<template>
	<el-dialog
	   v-model="visible"
	   title="渠道信息管理"
			
	   width="900"
	   :before-close="handleClose"
	 >
	<el-form>
		 <el-form-item   >
			 <el-space>
			 <el-input v-model="form.name" type="text" placeholder="请输入渠道名称"></el-input> 
			 <el-button type="primary"  @click="saveChannel">保存</el-button>
			 </el-space>
		 </el-form-item>
	 </el-form>
	 <el-table :data="tableData">
		 <el-table-column label="名称" prop="name">
			 <template #default="scope">
			 	 <el-input v-if="scope.row.isEdit==true" type="text" v-model="scope.row.name"></el-input>
				 <span v-else>{{scope.row.name}}</span>
			 </template>	
		 </el-table-column>
		 <el-table-column label="操作" prop="name">
		 	<template #default="scope">
				<el-button link v-if="scope.row.isEdit==true" type="info" @click="cancelChannel(scope.row)">取消</el-button>
				<el-button link v-else type="primary" @click="editChannel(scope.row)">编辑</el-button>
				<el-button link v-if="scope.row.isEdit==true" type="primary" @click="updateChannel(scope.row)">保存</el-button>
				<el-button link type="danger" @click="delChannel(scope.row)">删除</el-button>
			</template>		 
		 </el-table-column>
	 </el-table>
	   <template #footer>
	     <div class="dialog-footer">
				   <el-button @click="visible=false"  >关闭</el-button>
	     </div>
	   </template>
	 </el-dialog>
</template>

<script setup>
	import {ref,reactive,toRefs,onMounted,computed} from "vue";
	import {ElMessage,ElMessageBox } from 'element-plus'
	import transchannelApi from '@/api/quote/transchannelApi.js';
	const emit = defineEmits(['change']);
	const state = reactive({
	  // 弹窗属性
	  visible: false,
	   token:null,
	   channelid:null,
		tableData:[],
		form:{
			name:"",
		}
	});
	const {
	  visible,token,channelid,tableData,form,
	} = toRefs(state);
	
	function show(token,channelid){
		console.log(token);
		state.visible=true;
		state.token=token;
		state.channelid=channelid;
		loadChannelList();
		
		
	}
	function loadChannelList(){
		var data={"channelid":state.channelid};
		transchannelApi.supplierTransList(state.token,data).then((res)=>{
			if(res.data && res.data.length>0){
				state.tableData=res.data;
			}else{
				state.tableData=[];
			}
			emit("change",state.tableData);
		});
	}
	
	function editChannel(row){
		row.isEdit=true;
	}
	function cancelChannel(row){
		row.isEdit=false;
	}
	function saveChannel(){
		if(state.form.name!="" && state.form.name!=null && state.form.name!=undefined){
			var data={};
			data.name=state.form.name;
			data.disable=false;
			data.id=null;
			data.channelid=state.channelid;
			transchannelApi.supplierTransSave(state.token,data).then((res)=>{
				ElMessage.success("操作成功!");
				state.form.name="";
				loadChannelList();
			});
		}else{
			ElMessage.error("请正确输入名称!");
		}
		
	}
	function updateChannel(row){
		if(row.name!="" && row.name!=null && row.name!=undefined){
			row.disable=false;
			transchannelApi.supplierTransSave(state.token,row).then((res)=>{
				ElMessage.success("操作成功!");
				row.isEdit=false;
			});
		}else{
			ElMessage.error("请正确输入名称!");
		}
		
	}
	function delChannel(row){
		 ElMessageBox.confirm(
		 	'请确认是否删除？',
		 	{
		 	  confirmButtonText: '确认',
		 	  cancelButtonText: '取消',
		 	  type: 'warning',
		 	  callback:(action)=>{
		 		 if(action=="confirm"){
					 row.disable=true;
		 			transchannelApi.supplierTransSave(state.token,row).then((res)=>{
						ElMessage.success("操作成功!");
						loadChannelList();
					});
		 		 }
		 	  }
		 	}
		   )
	}
	
	
	
	
	
	defineExpose({show});
</script>

<style>
</style>