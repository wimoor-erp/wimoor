<template>
	<el-dialog v-model="visible" title="列配置">
		<draggable
		    class="draggable el-row "
			:gutter="20"
		   :list="dataList"
			animation="300"
			 @start="onStart"
			 @end="dragEnd"
		  >
			 <template #item="{ element }" >
				  <el-col :span="8" class="is-gutter">
		        <el-card shadow="hover">
					<div class="flex-center-between">
					 <el-space class="list-title">
					<drag class="ic-cen" theme="outline" size="16" fill="#9a9a9a" :strokeWidth="2"/>
					<span>{{element.name}}
					</span> 
					</el-space>
					 <el-switch
					    v-model="element.isshow"
					    size="small"
					  />
					  </div>
				  </el-card>
				  </el-col>
				 </template>
		  </draggable>
			<template #footer>
				<span class="dialog-footer">
					<el-button @click="visible = false">取消</el-button>
					<el-button type="primary" @click="submitFunc">保存</el-button>
				</span>
			</template>
	</el-dialog>
</template>

<script setup>
	import {Drag} from '@icon-park/vue-next';
	//使用文档地址https://www.itxst.com/vue-draggable-next/tutorial.html
	import draggable from "vuedraggable";
	import '@/assets/css/draggable.css';
	import {reactive,ref,toRefs}from"vue";
	import {ElMessage,ElDivider} from 'element-plus';
	import queryFieldApi from '@/api/sys/tool/queryFieldApi.js';
	const emit = defineEmits(['change']);
	const state = reactive({
		visible:false,
		ftype:"advcampaigns",
		dataList:[],
	});
	const {
		visible,
		dataList,
		ftype,
	}=toRefs(state);
	
	function show(ftype){
		var allList=[];
		state.ftype=ftype;
		state.dataList=[];
		state.visible = true;
		queryFieldApi.loadfield({"queryname":state.ftype}).then((res)=>{
			if(res.data && res.data.length>0){
				var allcheck=true;
				res.data.forEach(item=>{
					if(item.isShow){
						allcheck=false;;
					}
				});
				
				res.data.forEach(item=>{
					var mycheck=item.isShow;
					if(allcheck){
						mycheck=true;
					}
					allList.push({"name":item.title,"isshow":mycheck,"rank":item.findex,"ffield":item.ffield});
				
					
				});
				
				allList.sort(function(a,b){
								if(a.rank<b.rank){  return -1; }
								if(a.rank>b.rank){  return 1;  }
								return 0;
							});
				state.dataList=allList;
					
			}
		});
		
	}
	function submitFunc(){
		state.dataList.forEach((item,index)=>{
			item.findex=index;
		})
		queryFieldApi.saveMyVersionField(state.ftype,state.dataList).then((res)=>{
			ElMessage.success("操作成功!");
			emit('change',res.data);
			state.visible=false;
		});
	}
	
	defineExpose({
		show,
	})
</script>

<style>
	.is-gutter{
		padding-right:8px;
		    padding-left:8px;
			padding-bottom:12px;
	}
	.is-gutter .el-card__body{
		padding:12px 8px;
		cursor: move;
	}
</style>