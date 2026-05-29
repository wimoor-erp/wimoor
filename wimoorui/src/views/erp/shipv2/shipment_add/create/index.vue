<template>
	<div class="add-from el-white-bg">
	<div class="gird-line-head">
		<h3>添加发货单</h3>
		<el-tooltip content="亚马逊错误信息帮助文档">
			<el-button @click="openDoc"  class='ic-btn' >
			  <help theme="outline"  size="16" :strokeWidth="3"/>
			</el-button>
		</el-tooltip>
	</div>
	<Fromlist/>
	
	</div>
</template>
<script>
	export default{ name:'添加货件' }
</script>
<script setup>
	import {Help}from"@icon-park/vue-next";
	import Fromlist from"./components/form.vue"
	function openDoc(){
		window.open("https://wimoor.com/rule-c-534.html","_blank");
	}
</script>

<style>
	.add-from .divider{
		margin-left:24px;
		margin-right:24px;
	}
</style>
