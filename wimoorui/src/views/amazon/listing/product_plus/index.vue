	<template>
		<el-dialog  class="fllow" v-model="visable" title="A+" destroy-on-close='true' width="1000px"  >
			<div class="con-header">
			</div>	 
		  <template #footer>
		  	<span class="dialog-footer">
		  		<el-button @click="visable = false"> 关闭</el-button>
		  	</span>
		  </template>
		</el-dialog>
	</template>
	
<script setup>
	import {ref,reactive,onMounted,toRefs} from "vue";
	import aplusApi from '@/api/amazon/listing/aplusApi.js';
	import {ElMessage,ElDivider} from 'element-plus';
	import {Select} from '@element-plus/icons-vue';
	let state = reactive({ visable:false,
						   href:"",
						   row:{},
						   tableData:[]});
	const {visable,href,row,tableData} = toRefs(state);
	function show(row){
			state.row=row;
			var param={"amazonAuthId":row.amazonAuthId,"marketplaceid":row.marketplaceid,"asin":row.asin,"token":""};
			aplusApi.searchContentPublishRecords(param).then(res=>{
				console.log(res);
			})
			state.visable=true;
		}
	defineExpose({ show });
</script>
	
<style scoped>
 
</style>
	
