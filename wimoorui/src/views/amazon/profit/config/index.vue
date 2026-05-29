<template>
	<el-row>
		<el-col :span="16" class="gird-line-left el-white-bg">
			<div class="con-header">
				<el-row>
					<el-col :span="20">
					<el-button type="primary" class="im-but-one" @click="addPlan">
					  <plus theme="outline" size="18" fill="#fff" :strokeWidth="4"/>
					  <span>添加计算方案</span>
					</el-button>
					</el-col>
					<el-col :span="4">
					<el-button   style="float:right" @click="loadConfig">
					  <span>刷新</span>
					</el-button>
					</el-col>
				</el-row>
			</div>
			<div class="con-body">
				<el-table :data="tableData" border @select="" height="calc(100vh - 120px)">
					<el-table-column type="selection"></el-table-column>
					<el-table-column  label="计算方案" prop="name">
						<template #default="scope">
							<el-space>
								<span>{{scope.row.name}}</span>
								<el-tag v-if="scope.row.isDefault" size="small">默认方案</el-tag>
								<el-tag @click="setDefaultPlan(scope.row)" class="pointer tag-hover-show" v-else type="info">设为默认方案</el-tag>
							</el-space>
						</template>
					</el-table-column>
					<el-table-column  label="操作时间" prop="opttime" width="160"></el-table-column>
					<el-table-column  label="操作人" prop="operatorName" width="120"></el-table-column>
					<el-table-column  label="操作" width="150">
						<template #default="scope">
							<el-button @click="editPlan(scope.row)" link type="primary">编辑</el-button>
							<el-button @click="deletePlan(scope.row)" link type="info">删除</el-button>
						</template>
					</el-table-column>
				</el-table>
			</div>
		</el-col>
		<el-col :span="8" class="el-white-bg">
		
				<h3 class="gird-line-right ">店铺默认利润计算方案设置</h3>
				
				<div class="gird-line-right flex-center-between font-small ">
					<span>店铺</span>
					<span>利润计算方案</span>
				</div>
			  <div class="scrollbarDiv">
			   <el-scrollbar class="height-screen gird-line-right" >
				<el-card  shadow="never" class="s-p-card">
					<div class="flex-center-between font-base" v-for="sp in storeData">
						<span>{{sp.name}}</span>
						<el-select v-model="sp.profitcfgid" style="width:240px" :disabled="btnshow">
							<el-option v-for=" item in  tableData" :value="item.id" :label="item.name"></el-option>
						</el-select>
					</div>
				</el-card>
				<el-button v-if="btnshow"  type="primary" @click="editStorePlan">编辑</el-button>
				<el-button v-else type="primary" @click="saveStorePlan">保存</el-button>
			</el-scrollbar>
			</div>
		</el-col>
	</el-row>
</template>
<script>
    export default{ name:"计算方案" };
</script>
<script setup>
	import configApi from "@/api/amazon/profit/config.js"
    import groupApi from "@/api/amazon/group/groupApi.js"
	import {ref,reactive,onMounted,toRefs,} from"vue"
	import {Plus} from '@icon-park/vue-next'
	import {} from '@element-plus/icons-vue'
	import {  ElMessage } from 'element-plus';
	import { useRoute,useRouter } from 'vue-router'
	let router = useRouter()
	const state=reactive({
		btnshow:true,
		storeData:[],
		tableData:[],
	})
    const { btnshow, storeData ,tableData} = toRefs(state);
	
	function editStorePlan(){
		state.btnshow = false
	}
	
	function saveStorePlan(){
		state.btnshow = true
		groupApi.updateBatch(state.storeData).then(res=>{
			ElMessage.success('修改成功');
			loadStoreInfo();
		})
	}
	function loadStoreInfo(){
		groupApi.getAmazongroupList().then(res=>{
			state.storeData=res.data;
		})
	}
 
	function addPlan(){
		router.push({
			path:'/a/p/c',
			query:{
				title:'添加利润计算方案',
				path:'/a/p/c',
			}
		})
	}
	/* 设置为默认方案 */
	function setDefaultPlan(row){
		configApi.setDefaultPlan({'id':row.id}).then(res=>{
			loadConfig();
			ElMessage.success('修改成功');
		})
	}
	/* 编辑方案*/
	function editPlan(row){
		router.push({
			path:'/a/p/c',
			query:{
				title:'编辑利润计算方案',
				path:'/a/p/c',
				id:row.id,
			}
		})
	}
	function deletePlan(row){
		configApi.deleteConfig({id:row.id}).then(res=>{
			ElMessage.success('删除成功');
			loadConfig();
		})
	}
	function loadConfig(){
		configApi.showProfitDetial().then(res=>{
			state.tableData=res.data;
		})
	}
	
	onMounted(()=>{
	    loadConfig();
		loadStoreInfo();
	});
</script>

<style>
	.height-screen{
		height:calc(100vh - 125px)
	}
	.m-t-16{
		margin-top: 16px;
	}
	.s-p-card{margin-top:16px;margin-bottom:16px;}
	.s-p-card .el-card__body{
		padding:0;
	}
	.s-p-card .el-card__body .flex-center-between{
		padding:16px;
		border-bottom: 1px solid var(--el-border-color-lighter);
	}
	tr .tag-hover-show{
		opacity: 0;
		transition: opacity .25s ease
	}
	.gird-line-right{
		padding-bottom:0px;
	}
	.scrollbarDiv .gird-line-right{
		padding-top:0px;
	}
	tr:hover .tag-hover-show{
		opacity: 1;
		transition: ;
	}
</style>
