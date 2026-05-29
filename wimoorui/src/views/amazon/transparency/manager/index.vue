<template>
	<div class="main-sty">
		 <div class="header">
			<el-tabs v-model="activeName" class="demo-tabs" @tab-click="handleClick">
				<el-tab-pane label="产品列表" name="skuinfo"></el-tab-pane>
				<el-tab-pane label="任务" name="task"></el-tab-pane>
				<el-tab-pane label="T-code" name="tcode"></el-tab-pane>
			  </el-tabs>
		 </div> 
		 <SkuInfo v-if="activeName=='skuinfo'"></SkuInfo>
		 <Task v-if="activeName=='task'"></Task>
		 <TCode v-if="activeName=='tcode'"></TCode>
	</div>
</template>

<script setup>
	import { ref,reactive,onMounted,watch,toRefs } from 'vue';
	import { Search } from '@element-plus/icons-vue'
	import authApi from '@/api/amazon/transparency/authApi.js';
	import {dateFormat,formatFloat} from '@/utils/index.js';
	import SkuInfo from "./components/skuinfo.vue";
	import Task from "./components/task.vue"; 
	import TCode from "./components/tcode.vue"; 
	const emit = defineEmits(['change']);
	const state = reactive({
	 	 tableData:"",
		 activeName:"skuinfo",
		 name:""
	 })
	const { tableData,activeName,name}=toRefs(state);
</script>

<style>
</style>