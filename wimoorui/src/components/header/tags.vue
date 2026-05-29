<template>
		   <el-cascader 
		   v-model="tagsValue" 
		   @change="changeTags" 
		   :options="tagsList"  
		   :teleported="false" 
		   placeholder="请选择标签"  
		   :props="markprops" 
		   collapse-tags
		   class="inputHeight"
		   collapse-tags-tooltip
		   clearable />
</template>

<script setup>
	import { ref,reactive,onMounted,toRefs } from 'vue'
	import {getAllTags} from '@/api/sys/admin/tag.js';
	const emit = defineEmits(['change']);
	let state =reactive({
		tagsList:[],
		markprops:{ multiple: true },
		tagsValue:'',
	})
	let{ tagsList,tagsValue,markprops}=toRefs(state);
	onMounted(()=>{
		getAllTags().then(res => {
		    state.tagsList=res.data;
		});
	});
	function reset(){
		state.tagsValue='';
	}
	function changeTags(tags){
		 var arrs=[];
		 if(tags){
		 	 tags.forEach(function(item){
		 		 arrs.push(item[1].toString())
		 	 });
		 }
		 emit("change",arrs);
	}
	defineExpose({
	  state,reset
	})
</script>

<style>
	.inputHeight .el-input__wrapper{
		height:32px!important;
	}
</style>