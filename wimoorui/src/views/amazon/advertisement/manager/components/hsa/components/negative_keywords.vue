<template>
	<el-form-item>
		<div class="flex-center">
			<div class="p-l">
		 <el-tabs type="border-card"  v-model="activeName" >
		    <el-tab-pane label="精确匹配" name="NEGATIVE_EXACT">
			</el-tab-pane>
		    <el-tab-pane label="词组匹配" name="NEGATIVE_PHRASE">
			</el-tab-pane>
			<el-tab-pane label="广泛匹配" name="NEGATIVE_BROAD">
			</el-tab-pane>
			<el-input
			    v-model="keywords"
			    type="textarea"
				:autosize="{ minRows:8, maxRows:8}"
			    placeholder="输入关键词,用逗号隔开"
			  />
			  <el-space class="m-t-8">
			<el-button  @click="addKeywords" >添加关键词</el-button> 
			 <span class="font-extraSmall ">最多只能添加1000个关键词!</span>
			 </el-space>
		  </el-tabs>
		 
		   </div>
		  <div class="p-r">
			  <span class="font-extraSmall m-l-8">已添加 <span> {{tableData?.length}} </span> 个关键词</span>
			 <el-table :data="tableData" height="288px">
					 <el-table-column label="否定关键词" prop="name"/>
					 <el-table-column label="匹配类型" prop="typename"/>
					 <el-table-column label="操作" width="60" >
						 <template #header>
							 <el-link type="primary" @click.stop="clear" class="font-small" :underline="false">清空</el-link>
						 </template>
			 			 <template #default="scope">
			 						 <el-button link type="primary" @click.stop="deleteRow(scope.$index)">删除</el-button>
			 			 </template>
			 	 </el-table-column>
			 </el-table>
		  </div>
		  </div>
	</el-form-item>
</template>

<script setup>
	import { ref ,reactive,onMounted,toRefs,watch} from 'vue'
	import {Search,} from '@element-plus/icons-vue';
	import {splitStr} from '@/views/amazon/advertisement/manager/components/javascript/common.js';
	const emit = defineEmits(['change',]);
	const state = reactive({
		queryParams:{},
		tableData:[],
		keywords:"",
		activeName:"NEGATIVE_EXACT",
		addList:[],
	})
	const{
		tableData,
		keywords,
		activeName,
		queryParams,
		addList,
	}=toRefs(state)
	
	function show(params){
		state.queryParams=params;
	}
	defineExpose({
		show,getData,
	})
	function clear(){
		state.addList=[];
		state.tableData=[];
		emit("change",getData());
	}
	 
	function deleteRow(index){
		 state.tableData.splice(index,1);
	     state.addList.splice(index,1);
		  emit("change",getData());
	}
	function addKeywords(){
		var list=splitStr(state.keywords);
		list.forEach(item=>{
			if(state.addList.includes(item)==false){
			   var typename="";
				if(state.activeName=="NEGATIVE_EXACT"){
					typename="精确匹配";
				}
				if(state.activeName=="NEGATIVE_PHRASE"){
					typename="词组匹配";
				}
				if(state.activeName=="NEGATIVE_BROAD"){
					typename="广泛匹配";
				}
				var row={"type":state.activeName,"typename":typename,"value":item,"name":item};
					state.tableData.push(row);
					state.addList.push(item);
				}
		})
		 emit("change",getData());
	}
	function getData(){
		var list=[];
		state.tableData.forEach(item=>{
			var row={"matchtype":item.type,"keywordtext":item.value,"state": "ENABLED",
			         "adgroupid": state.queryParams.adgroupid,"campaignid": state.queryParams.campaignid};
			list.push(row);
		});
		return list;
	}
	 
</script>

<style scoped>
	.m-l-8{
		margin-left:8px;
	}
	.p-l{
		width:450px;
		height:320px;
	}
	.p-r{
		width:450px;
		height:320px;
		background-color: var(--el-color-info-light-7);
	}
</style>