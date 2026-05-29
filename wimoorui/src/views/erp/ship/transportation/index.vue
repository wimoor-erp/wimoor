<template>
	    <div class="main-sty">
		<el-tabs v-model="activeName" @tab-click="handleClick">
			<el-tab-pane label="物流维护" name="first"><TransList></TransList></el-tab-pane>
			<el-tab-pane label="物流比价" name="second"><CompareList></CompareList></el-tab-pane>
		  </el-tabs>
 </div>
</template>
 
<script>
   import TransList from './components/list.vue';
   import CompareList from './components/compare.vue';
   import { ref } from 'vue'
	export default{
	    name: '物流公司',
	    components:{TransList,CompareList },
	    setup(){
			let activeName=ref("first");
	        return{activeName }
	    }
	}
</script>

<style>
</style>