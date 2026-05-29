<template>
	<el-dialog v-model="visable" title="费用类型管理">
		<el-button @click="handleAdd"   class="m-b-8">添加费用类型</el-button>
	   <GlobalTable
	   ref="globalTable"
       :tableData="tableData"
	    height="calc(100vh - 470px)"  @loadTable="loadTableData" >
	   	<template #field>
	      <el-table-column  label="费用名称" prop="name" >
		  <template #default="scope">
		  		<el-input v-if="scope.row.isEdit==true" clearable placeholder="请输入" v-model="scope.row.name" ></el-input>
				<span v-else>{{scope.row.name}} </span>
		  </template>
		  </el-table-column>
	      <el-table-column  label="操作"  >
		     <template #default="scope">
				 <el-button  type="primary"
				  @click="handleDelete(scope.row,scope.$index)"
				  link>删除</el-button>
				  <el-button v-if="scope.row.isEdit==true" type="primary" link @click="handleSave(scope.row)">
				    保存
				  </el-button>
				  <el-button v-else type="primary" link @click="scope.row.isEdit=true">
				    编辑
				  </el-button>
			 </template>
		  </el-table-column>
		  </template>
	    </GlobalTable>
		 <template #footer>
		        <el-button @click="visable = false">关闭</el-button>
		    </template>
	  </el-dialog>
</template>

<script setup>
	import { ref,reactive,onMounted,toRefs} from 'vue';
	import { ElMessage, ElMessageBox} from 'element-plus';
	import financesItemApi from '@/api/amazon/finances/financesItemApi.js';
	let globalTable=ref();
	const state=reactive({
		tableData:{records:[],total:0},
	  visable:false,
	})
	const {
		visable,
		tableData,
	}=toRefs(state)
	var hasload=false;
	function show(){
		state.visable = true;
		setTimeout(function(){
			handleQuery();
		},300)
	}
	function handleAdd(){
		state.tableData.records.push({
			name:'',
			isEdit:true,
			id:'',
		})
	}
	function handleDelete(row,index){
		 financesItemApi.deleteFinItem({"id":row.id}).then((res)=>{
			 ElMessage.success('删除成功');
			 state.tableData.records.splice(index);
		 });
	}
	
	function handleSave(row){
		row.createdate=null;
		if(row.name==''){
			ElMessage.error('请输入正确的名称');
			return;
		}
		financesItemApi.saveFinItemName(row).then((res)=>{
			ElMessage.success('操作成功');
			row.isEdit=false;
		});
	}
	function handleQuery(){
			globalTable.value.loadTable();
	}
	function loadTableData(params){
		financesItemApi.getFinList(params).then((res)=>{
			state.tableData.records = res.data.records;
			state.tableData.total =res.data.total;
			if(res.data.total>0){
				state.tableData.records.forEach(item=>{
					item.isEdit=false;
				});
			}
		});
	}
 
	defineExpose({
		show,
	})
</script>

<style>
	.m-b-8{
		margin-bottom: 8px;
	}
</style>
