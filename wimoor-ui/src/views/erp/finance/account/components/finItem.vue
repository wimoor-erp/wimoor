<template>
	<el-dialog v-model="feeVisable" title="费用项管理">
			<el-button @click="handleAdd"   >添加费用类型</el-button>
		    <el-table :data="feeTableData" class="m-t-8">
		      <el-table-column  label="费用名称"  >
			  <template #default="scope">
			  		<el-input v-if="scope.row.edit" clearable placeholder="请输入费用项" v-model="scope.row.name" ></el-input>
					<div v-else>{{scope.row.showname}}</div>
			  </template>
			  </el-table-column>
		      <el-table-column  label="操作"  >
			     <template #default="scope">
					 <el-button v-if="!scope.row.edit" type="primary"
					  @click="scope.row.edit=true"
					  link>编辑</el-button>
					 <el-button v-if="!scope.row.edit" type="primary"
					  @click="handleDelete(scope.row)"
					  link>删除</el-button>
					  
					  <el-button  v-if="scope.row.edit" type="primary"
					   @click="handleSave(scope.row)"
					   link>保存</el-button>
					   <el-button v-if="scope.row.edit"  type="primary"
					    @click="handleCancel(scope.row)"
					    link>取消</el-button>
				 </template>
			  </el-table-column>
		    </el-table>
			 <template #footer>
			        <el-button @click="feeVisable = false">关闭</el-button>
			    </template>
		  </el-dialog>
</template>

<script setup>
	import {Plus,} from '@icon-park/vue-next';
	import {Search} from '@element-plus/icons-vue'
	import { ref,reactive,onMounted,watch,toRefs,computed} from 'vue'
	import projectApi from '@/api/erp/finances/projectApi.js';
	import { ElMessage, ElMessageBox } from 'element-plus';
	const emit = defineEmits(['change']);
	const globalTableRef=ref();
	const state = reactive({
		feeTableData:[ ],
		feeVisable:false,
		feeTypeList:[],
	})
	const {
		feeTypeList,
		feeTableData,
		feeVisable,
	} = toRefs(state)
	
	function handleAdd(){
		if(state.feeTableData&&state.feeTableData.length>0){
			var hasNewItem=false;
			state.feeTableData.forEach(item=>{
				if(item.id==""){
					hasNewItem=true;
				}
			})
			if(hasNewItem){
				ElMessage.error('存在未保存的记录！');
				return ;
			}
		}
		state.feeTableData.push(  {  name:'', id:"",edit:true} );
	}
	
	
	function handleCancel(item){
		if(item.id==""){
			state.feeTableData = state.feeTableData.filter(item => item.id != "");
		}else{
			item.edit=false;
		}
	}
	
	
	function show(){
		state.feeVisable=true;
		state.feeTableData=[];
		projectApi.getProject().then((res)=>{
			 res.data.forEach(item=>{
				 if(item.issys==false){
					 item.showname=item.name;
				 	 state.feeTableData.push(item);
				 }
			 });
		});
	}
	function handleDelete(row){
		projectApi.delProject({"id":row.id}).then((res)=>{
			 ElMessage.success( '删除成功');
			 state.feeTableData = state.feeTableData.filter(item => item.id != row.id);
			 emit("change");
		});
	}
	function handleSave(row){
		if(row.id){
			projectApi.updateProject({"id":row.id,"name":row.name}).then((res)=>{
				 ElMessage.success( '修改成功');
				 row.showname=row.name;
				 row.edit=false;
				 emit("change");
			});
		}else{
			projectApi.saveProject({"name":row.name}).then((res)=>{
				ElMessage.success('添加成功');
				row.showname=row.name;
				row.id=res.data.id;
				row.edit=false;
				emit("change");
			});
		}
		
	}
	defineExpose({ show })
</script>

<style>
</style>