<template>
	<el-select v-model="warehouse" style="width: 100px"  placeholder="FBA仓库" clearable>
		<el-option :label="item.label" :value="item.value" v-for="item in Data"></el-option>
	</el-select>
</template>

<script setup>
	import { ref,reactive,onMounted,watch ,toRefs} from 'vue'
	const state = reactive({
		warehouse:'',
		Data:[
			{
			  label:'ABnnc-美国仓',
			  value:'us',
			},
			{
			  label:'ABnnc-日本仓',
			  value:'us',
			},
		]
	})
	const {
		warehouse,
		Data,
	}=toRefs(state)
</script>

<style>
</style> 
