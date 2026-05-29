<template>
	<el-input v-model="search" @input="handleQuery" clearable :placeholder="placeholder">
			 <template #suffix>
			  <el-icon @click.stop="handleQuery"><Search /></el-icon>
			 </template>
	</el-input>	
</template>

<script setup>
	import {reactive,toRefs,onMounted} from 'vue';
	import {Search} from '@element-plus/icons-vue';
	const emit = defineEmits(['change']);
	const state = reactive({
		search:"",
	})
	const{
		search,
	}=toRefs(state)
	const props=defineProps({
		placeholder:"",
		ckey:"",
	});
	const {
	    placeholder,ckey
	} = toRefs(props);
	onMounted(()=>{
		
		 if(props.ckey){
			state.search=sessionStorage.getItem(props.ckey);
			if(state.search=="null"){
				state.search=null;
			}
		    if(state.search ){
			   emit("change",state.search,"load");
			}
		 }
	})
	function handleQuery(){
		if(props.ckey){
			sessionStorage.setItem(props.ckey,state.search);
		 }
		 emit("change",state.search);
	}
</script>
   
<style>
</style>