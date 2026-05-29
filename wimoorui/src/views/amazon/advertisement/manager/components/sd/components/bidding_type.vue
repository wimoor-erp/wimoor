<template>
	<el-space>
		<el-select v-model="bidType.type" style="width:100px; " size="small">
			<el-option value="suggest" key="suggest" label="建议竞价"></el-option>
			<el-option value="my" key="my" label="我的竞价"></el-option>
			<el-option value="default" key="default" label="默认竞价"></el-option>
		</el-select>
		<el-input v-if="bidType.type=='my'" v-model="bidType.bid" style="width:70px"  clearable size="small" placeholder="我的竞价"></el-input>
	</el-space>
</template>

<script setup>
	import {
		reactive,
		toRefs,
	} from 'vue'
   let props = defineProps({ bidType:undefined});
   const { bidType} = toRefs(props);
</script>

<style>
</style>