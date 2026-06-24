<template>
	<el-dialog
	    v-model="dialogVisible"
	    title="进度"
	    width="30%" 
	  >
	     <el-progress
	          :percentage="100"
	          status="success"
	          :indeterminate="true"
	          :duration="5"
	        />
	    <template #footer>
	      <span class="dialog-footer">
	        <el-button type="primary" @click="dialogVisible = false"  >关闭</el-button>
	      </span>
	    </template>
	  </el-dialog>
</template>

<script setup>
	  import {reactive,ref,watch,onMounted} from "vue";
	   defineProps({dialogVisible:false});
</script>

<style>
</style>
