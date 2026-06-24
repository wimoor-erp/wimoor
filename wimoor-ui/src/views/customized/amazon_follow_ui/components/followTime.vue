<template>
	<el-dialog
	title="跟卖时间设置"
	v-model="visable"
	width="800px"
	>
	<el-button type="primary" @click="addItem">添加跟卖时间段</el-button>
	<el-tag style="margin-left: 10px">开始时间：到达时间自动上架，结束时间：到达时间自动下架，不设置结束时间为不下架，即为全天跟卖</el-tag>
	<el-table :data="tableData" class="m-t-8">
		<el-table-column label="时段名称">
			<template #default="scope">
			<el-input v-if="scope.row.isEdit" v-model="scope.row.name"></el-input>
			<span v-else>{{scope.row.name}}</span>
			</template>
		</el-table-column>
		<el-table-column label="跟卖时间" width="400">
			<template #default="scope">
				<el-space>
						<el-time-picker  :disabled="!scope.row.isEdit"
						     v-model="scope.row.starttimes"
							  format="HH:mm"
							  style="width:100px;"
						      placeholder="上架时间"
						   />
						    <div>-</div>
			   <el-time-picker 
			        v-model="scope.row.endtimes"
					:disabled="!scope.row.isEdit"
			   	    format="HH:mm"
					  style="width:100px;"
			         placeholder="下架时间"
			      />
				  </el-space>
			   </template>
		</el-table-column>
		<el-table-column label="操作" width="120">
			<template #default="scope">
				<el-button type="primary" v-if="scope.row.isEdit==false" @click="editItem(scope.row)" link>编辑</el-button>
				<el-button type="primary" v-else @click="saveItem(scope.row)" link>保存</el-button>
				<el-button type="info" @click="delItem(scope.$index,scope.row)" link>删除</el-button>
			 </template>
		</el-table-column>
	</el-table>
	<template #footer>
		<el-button @click="visable=false">关闭</el-button>
	</template>
	</el-dialog>
</template>

<script setup>
	import {ref,reactive,toRefs,onMounted,}from "vue";
	import {ElMessageBox ,ElMessage } from 'element-plus';
	import followTimeApi from '@/views/customized/amazon_follow_ui/js/followTimeApi.js';
	const state=reactive({
		visable:false,
		tableData:[
			{name:'',time:[],}
		],
	})
	const {
		visable,
		tableData,
	}=toRefs(state)
	
	function loadTableData(){
		followTimeApi.list().then((res)=>{
			 if(res.data && res.data.length>0){
				res.data.forEach(item=>{
					if(item.starttime){
						item.starttimes=new Date("2020-01-01 "+item.starttime);
					}
					if(item.endtime){
							item.endtimes=new Date("2020-01-01 "+item.endtime);
					}
					item.isEdit=false;
				}); 
			 }
			  state.tableData=res.data;
		});
	}
	function editItem(row){
		row.isEdit=true;
	}
	function saveItem(row){
		if(row.name=="" || row.name==undefined || row.name==null || row.name.trim()==""){
			ElMessage({
			  type: 'error',
			  message: '请正确填写名称!',
			})
			return;
		}
		if(row.starttimes){
			row.starttime=row.starttimes.format("hh:mm:ss") ;
		}else{
			row.starttime=null;
		}
		if(row.endtimes){
			row.endtime=row.endtimes.format("hh:mm:ss") ;
		}else{
			row.endtime=null;
		}
		if(row.id){
			//更新
			followTimeApi.update(row).then((res)=>{
				if(res.data==true){
					ElMessage({
					  type: 'success',
					  message: '更新成功!',
					})
					row.isEdit=false;
				}
			})
			
		}else{
			//保存
			followTimeApi.add(row).then((res)=>{
				if(res.data==true){
					ElMessage({
					  type: 'success',
					  message: '保存成功!',
					})
					row.isEdit=false;
				}
			})
		}
	}
	function delItem(index,row){
		if(row.id){
			followTimeApi.deleteItem({"id":row.id}).then((res)=>{
				if(res.data==true){
					ElMessage({
					  type: 'success',
					  message: '删除成功!',
					})
					state.tableData.splice(index,1);
				}
			})
		}else{
			state.tableData.splice(index,1);
		}
	}
	function addItem(){
		var timearr=[];
		timearr[0]=new Date("2020-01-01 00:00:00");
		timearr[1]=new Date("2020-01-01 23:59:00");
		state.tableData.push({"name":"","isEdit":true,"times":timearr});
	}
 
	
	function show(){
		state.visable = true;
		loadTableData();
	}
	defineExpose({
		show,
	})
</script>

<style>
</style>