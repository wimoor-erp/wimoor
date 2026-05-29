<template>
	<div class="main-sty">
		<el-tabs v-model="activeName"  @change="handleClick">
		    <el-tab-pane label="本地仓" name="1"></el-tab-pane>
		    <el-tab-pane label="FBA仓" name="2"></el-tab-pane>
		</el-tabs>
	  <div class="con-body">
		  <Self ref="selfTable" v-if="activeName=='1'"/>
		  <Fba ref="fbaTable" v-else/>
	  </div>
 </div>
</template>
<script>
    export default{ name:"库存货值" };
</script>
<script setup>
	import { ref,reactive,onMounted,toRefs} from 'vue'
	import Self from"./components/self.vue"
	import Fba from"./components/fba.vue";
	const selfTable=ref();
	const fbaTable=ref();
	const state = reactive({
		activeName:'1', 
	})
	const {
		activeName
	}=toRefs(state)
</script>

<style>
</style>
