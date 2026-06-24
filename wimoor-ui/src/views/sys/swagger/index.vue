<template>
  <div style="padding:10px">
	  文档类型：
	  <el-select v-model="doctype"  @change="handleChange">
		  <el-option label="Admin" value="admin" key="admin"></el-option>
		  <el-option label="ERP" value="erp" key="erp"></el-option>
		  <el-option label="Amazon" value="amazon" key="amazon"></el-option>
	  </el-select>
	<span class="font-extraSmall" style="padding-left:20px;">授权操作：点击“Authoriza”  按F12 ->应用->本地存储空间->jsessionid 复制 ，将值粘贴</span>  
  </div>
 <div style="height:calc(100vh - 87px)">
	<iframe id="myIframeDiv" style="width: 100%;height:calc(100vh - 95px);"></iframe>
</div>
</template>
<script>
    export default{ name:"文档API" };
</script>
<script setup>
	import {ref,reactive,toRefs,onMounted,nextTick}from"vue";
	var doctype=ref("admin");
	function handleChange(value){
		nextTick(()=>{
			let iframe= document.querySelector('#myIframeDiv');
			iframe.src = window.location.origin+"/api/"+doctype.value+"/swagger-ui/index.html"; // 就是这一行代码，一定要注意哦
		})
	}
	onMounted(()=>{
		nextTick(()=>{
			let iframe= document.querySelector('#myIframeDiv');
			iframe.src = window.location.origin+"/api/admin/swagger-ui/index.html"; // 就是这一行代码，一定要注意哦
		})
	})
	 
</script>
<style scoped="scoped">
	 
</style>

	
 