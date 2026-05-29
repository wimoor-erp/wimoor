<template>
	<el-space v-if="isspace"  wrap >
		<slot></slot>
	</el-space>
	<el-row v-else :gutter="16"> 
	   <slot></slot>
	</el-row>
</template>

<script setup>
    import {toRefs,provide} from "vue";
	let props = defineProps({modelValue:"",isspace:""});
	const {modelValue,isspace} = toRefs(props);
	provide("groupValue",props);
	const emit=defineEmits(['update:modelValue',"change"]);
	const handleChangeEvent=function(value){
	 	  emit('update:modelValue', value);
		  emit('change', value);
	 }  
	 provide("changeEvent",handleChangeEvent);
</script>