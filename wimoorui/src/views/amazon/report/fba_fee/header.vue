<template>
	 <div class="con-header" >
	  <el-row>
	    <el-space >
	  <Group @change="groupChange" defaultValue="only"/>
	  <el-select v-model="fbasize" @change="changeFbasize" placeholder="自测尺寸对比FBA尺寸" clearable >
	        <el-option  v-for="item in fbasizeList"   :key="item.id"  :label="item.name" :value="item.id"   >
	        </el-option>
	  </el-select>
	   <el-input  v-model="searchKeywords" clearable @input="searchConfirm" placeholder="请输入" class="input-with-select" >
	      <template #prepend>
	        <el-select v-model="searchtype" @change='searchTypeChange' placeholder="SKU" style="width: 110px">
	          <el-option label="SKU" value="sku"></el-option>
			  <el-option label="ASIN" value="asin"></el-option>
			   <el-option label="商品名称" value="name"></el-option>
	        </el-select>
	      </template>
	      <template #append>
	        <el-button @click="searchConfirm">
	           <el-icon style="font-size: 16px;align-itmes:center">
	            <search />
	         </el-icon>
	        </el-button>
	      </template>
	    </el-input>
		<el-checkbox v-model="oversize" size="large" @change="changeOversize" label="超收费用的SKU" />
	    <!-- <el-button>重置</el-button> -->
	  </el-space>
		 
	  </el-row>
	   <!--功能区域 -->
	 </div>
</template>

<script setup>
	import {Search,ArrowDown,} from '@element-plus/icons-vue'
	import {MenuUnfold,Plus,SettingTwo,Help,Copy,MoreOne,ChartPie} from '@icon-park/vue-next';
	import {h, ref ,watch,reactive,onMounted,toRefs} from 'vue';
	import Group from '@/components/header/group.vue';
	import {getCountryCode} from '@/utils/index.js';
	 const emit = defineEmits(['getdata']);
	 let state=reactive({
		queryParams:{},
		fbasize:"",
		fbasizeList:[{"id":"","name":"全部尺寸对比"},{"id":"ltself","name":"自测>FBA"},{"id":"etself","name":"自测=FBA"},{"id":"gtself","name":"自测<FBA"}],
		oversize:false,
		searchtype:"sku",
		searchKeywords:"",
	 });
	
	 const {
		 fbasize,
		 fbasizeList,
		 oversize,
		 queryParams,
		 searchKeywords,
		 searchtype,
	 } = toRefs(state);
     function groupChange(obj){
		state.queryParams.groupid=obj.groupid;
		state.queryParams.marketplaceid=obj.marketplaceid;
		state.queryParams.country=getCountryCode(obj.marketplaceid);
		state.queryParams.search="";
		state.queryParams.sizetype=state.fbasize;
		state.queryParams.isgtself="false";
		changeData();
	 }
	 function changeFbasize(val){
		 state.queryParams.sizetype=val;
		 changeData();
	 }
	 function changeOversize(val){
		 var strs="false";
		 if(val==true){
			 strs="true";
		 }
		 state.queryParams.isgtself=strs;
		 changeData();
	 }
	  
	 function submitForm(){
		 changeData();
	 }
	 function searchConfirm(){
		 changeData();
	 }
	 function searchTypeChange(val){
		 state.queryParams.searchtype=val;
		 changeData();
	 }
	 function changeData(){
		 state.queryParams.search=state.searchKeywords;
		 state.queryParams.searchtype=state.searchtype;
		 emit('getdata',state.queryParams);
	 }
</script>

<style>
	.pad2{
		margin-left: 5px;
	}
</style>
