<template>
	<el-dialog   v-model="dialog.visible" title="设置FBA站点配货优先级" width='600px'>
	<div class="rank-list-title">
	<span>FBA仓库</span>
	<span>优先级</span>
	</div>	
  <draggable
      class="draggable"
     :list="countryOptions"
	  animation="300"
	 @start="onStart"
	 @end="dragEnd"
    >
	 <template #item="{ element }">
        <div class="item" >
          <el-card shadow="hover">
			 <el-space class="list-title">
			<drag class="ic-cen" theme="outline" size="16" fill="#9a9a9a" :strokeWidth="2"/>
			<span>{{element.name}}
			<p class="font-extraSmall">FBA-{{element.market}}</p>
			</span> 
			</el-space>
				<span>{{element.findex}}</span>
		  </el-card>
        </div>
		 </template>
    </draggable>
			<template #footer>
				<span class="dialog-footer">
					<el-button @click="dialog.visible = false">取消</el-button>
					<el-button type="primary" @click="submitFunc">保存</el-button>
				</span>
			</template>
	</el-dialog>
</template>

<script setup>
	import { ref ,reactive,toRefs} from 'vue'
	import {Drag} from '@icon-park/vue-next';
	//需要安装依赖 npm i -S vuedraggable@next
	//使用文档地址https://www.itxst.com/vue-draggable-next/tutorial.html
	import draggable from "vuedraggable";
	import '@/assets/css/draggable.css';
	import priorityApi from "@/api/amazon/market/priorityApi.js";
	import { ElMessage, ElMessageBox } from 'element-plus'
   let rankVisible =ref(false)
   const state=reactive({
   	      queryParams:{transtype:""},
   		  dialog:{visible:false},
   		  formData:[],
		  groupid:"",
   		  countryOptions:[],
   		  transtypeOptions:[],
       });
   	const {
   	  queryParams,dialog,formData,countryOptions,transtypeOptions
   	} = toRefs(state);
	function show(groupid){
		state.dialog.visible=true;
		state.groupid=groupid;
		priorityApi.list({'groupid':groupid}).then(res=>{
			state.countryOptions=res.data;
		})
	}
	function dragEnd(){
		state.countryOptions.forEach((item,index)=>{
			item.findex = index+1
		})
	}
	function submitFunc(){
		var formData=[];
		state.countryOptions.forEach(market=>{
			formData.push({groupid:state.groupid,marketplaceid:market.marketplaceid,priority:market.findex})
		})
		priorityApi.save(formData).then(res=>{
		    ElMessage.success("保存成功");
			show(state.groupid);
		})
	}
    defineExpose({ show });
</script>

<style>
	.rank-list-title{
		display: flex;
		justify-content: space-between;
		margin:0 16px;
		font-size: 12px;
		color: #999;
	}
</style>
