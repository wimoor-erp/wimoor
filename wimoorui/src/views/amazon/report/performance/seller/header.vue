<template>
	 <div class="con-header" >
	  <el-row>
	    <el-space >
	  <Group @change="groupChange" defaultValue="only"/>
	  </el-space>
		    <div class='rt-btn-group'>
		 				   <el-button class='ic-btn'  title='列配置'>
		 					  <setting-two theme="outline" size="16"  :strokeWidth="3"/>
		 				   </el-button>
		 					<el-button   class='ic-btn' title='帮助文档'>
		 			      <help theme="outline" size="16" :strokeWidth="3"/>
						   </el-button>
		 	  </div>
	  </el-row>
	   <!--功能区域 -->
	 </div>
</template>

<script setup>
	import {Search,ArrowDown,} from '@element-plus/icons-vue'
	import {h, ref ,watch,reactive,onMounted,toRefs} from 'vue';
	import {MenuUnfold,Plus,SettingTwo,Help,Copy,MoreOne,ChartPie} from '@icon-park/vue-next';
	import Group from '@/components/header/group.vue';
	import {getCountryCode} from '@/utils/index.js';
	 const emit = defineEmits(['getdata']);
	 let state=reactive({
		queryParams:{},
		searchKeywords:"",
	 });
	
	 const {
		 queryParams,
		 searchKeywords,
	 } = toRefs(state);
     function groupChange(obj){
		state.queryParams.groupid=obj.groupid;
		state.queryParams.marketplaceid=obj.marketplaceid;
		state.queryParams.country=getCountryCode(obj.marketplaceid);
		state.queryParams.search="";
		changeData();
	 }
	 function submitForm(){
		 changeData();
	 }
	 function searchConfirm(){
		 changeData();
	 }
	 function searchTypeChange(val){
		 changeData();
	 }
	 function changeData(){
		 state.queryParams.search=state.searchKeywords;
		 emit('getdata',state.queryParams);
	 }
</script>

<style>
	.pad2{
		margin-left: 5px;
	}
</style>
