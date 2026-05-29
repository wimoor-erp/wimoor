<template>
	<h3 id="assemble" class="tab-scroll">父SKU信息</h3>
	<el-form-item label="父产品列表" class="grid-row">
		<el-table border :data="dataForms">
			<el-table-column width="62" label="图片">
				 <template #default="scope">
					<el-image v-if="scope.row.image" :src="scope.row.image" width="40" height="40"></el-image>
					<el-image v-else :src="$require('empty/noimage40.png')" width="40" height="40"></el-image>
				 </template>
			</el-table-column>
			<el-table-column label="名称/父SKU">
				<template #default="scope">
					<div class='name'>{{scope.row.name}}</div>
					<div class='sku'>{{scope.row.sku}} </div>
				</template>
			</el-table-column>
			<el-table-column label="单位数量" prop="moq" width="120" >
			</el-table-column>
		</el-table>
	</el-form-item>
</template>

<script setup>
	import { ref,reactive,onMounted,watch,toRefs } from 'vue'
	let props = defineProps({
	  dataForms:Object,
	}) 
	let {dataForms} =toRefs(props);
</script>

<style>
</style>
