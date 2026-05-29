<template>
	<el-dialog v-model="Visable" title="添加指标">
		
		 <el-checkbox-group v-model="checkList">
		    <el-checkbox v-for="item in queryList" :label="item.ffield" :key="item.ffield" >{{item.title}}</el-checkbox> 
		  </el-checkbox-group>
			 <div style="float:right;margin-bottom: 10px;">
				 <el-input style="width:150px;margin-right: 5px;" type="text" placeholder="勾选后请输入分组名" v-model="versionName"></el-input>
				 <el-button type="success" @click="saveItem">保存指标分组</el-button>
			 </div>
			 <h3 style="margin-top: 5px;">已有分组名</h3>
			<el-table :data="tableData" style="width: 100%">
				 <el-table-column prop="name" label="名称" width="180" />
				 <el-table-column prop="fquery" label="包含指标项"  />
				  <el-table-column prop="fquery" label="操作"  >
					  <template #default="scope">
						  <el-button link @click.stop="deleteItem(scope.row.id)">删除</el-button>
					  </template>
				</el-table-column>
			</el-table>
			 <template #footer>
				 <el-button @click="Visable=false">关闭</el-button>
			 </template>
	</el-dialog>
	
</template>

<script setup>
	import {ref,reactive,toRefs,onMounted}from"vue";
	import {Help} from '@icon-park/vue-next';
	import {Search,ArrowDown,} from '@element-plus/icons-vue';
	import { ElMessage, ElMessageBox } from 'element-plus';
	import queryFieldApi from '@/api/sys/tool/queryFieldApi.js';
	const emit = defineEmits(['change']);
	let state=reactive({
		tableData:[],
		checkList:[],
		Visable:false,
		queryList:[],
		versionName:null,
	})
	let {
		tableData,
		checkList,Visable,queryList,versionName
	} =toRefs(state);
	 
	function handleQuery(ftype){
		//加载分组记录
		queryFieldApi.getMyVersionFieldByUser({"queryname":"analysistarget"}).then((res)=>{
			state.tableData=res.data;
			if(ftype=="change"){
				emit("change",res.data);
			}
		});
	}
	function loadQueryData(){ 
		queryFieldApi.loadfield({"queryname":"analysistarget"}).then((res)=>{
			state.queryList=res.data;
		});
	}
	function show(){
		state.Visable=true;
		loadQueryData();
		handleQuery();
	}
	 function saveItem(){
		 var data={};
		 data.queryname="analysistarget";
		 data.versionname=state.versionName;
		 var list=[];
		 state.checkList.forEach(item=>{
			 list.push({"ffield":item})
		 })
		 data.fieldlist=list;
		 if(state.versionName!="" && state.versionName!=null && state.checkList.length>0){
			 queryFieldApi.saveMyVersionFieldWithName(data).then((res)=>{
					ElMessage.success("添加成功");
					state.versionName="";
					state.checkList=[];
					handleQuery("change");
					
			 });
		 }else{
			  ElMessage.error("请确认是否勾选指标项或填写名称");
		 }
	 }
	 function deleteItem(id){
		 queryFieldApi.deleteMyVersionField({"versionid":id}).then((res)=>{
			 ElMessage.success("删除成功");
			 handleQuery("change");
		 });
	 }
	defineExpose({
		show,
	})
</script>

<style scoped="scoped">
</style>