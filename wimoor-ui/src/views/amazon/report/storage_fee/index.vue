<template>
	<div class="main-sty" >
		
	<el-tabs v-model="activeName"   >
	  <el-tab-pane  label="仓储费" name="fee" key="longfee">
	  </el-tab-pane>
	  <el-tab-pane  label="超龄库存附加费" name="longfee" key="longfee">
	  </el-tab-pane>
	</el-tabs>
	<div class="con-header">
	<LongStorageFee v-if="activeName=='longfee'"></LongStorageFee>
	<StorageFee v-if="activeName=='fee'"></StorageFee>
	</div>
	</div>
</template>
<script>
    export default{ name:"FBA仓储费" };
</script>
<script setup>
	import {ref} from 'vue';
	import LongStorageFee from './index_long.vue';
	import StorageFee from './index_fee.vue';
	let activeName=ref("fee");
</script>

<style>
</style>