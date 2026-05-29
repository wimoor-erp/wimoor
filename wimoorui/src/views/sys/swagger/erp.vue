<template>
 <div style="height:calc(100vh)">
	<iframe id="myIframeDiv" style="width: 100%;height:calc(100vh - 40px);"></iframe>
</div>
</template>

<script setup>
	import {ref,reactive,toRefs,onMounted,nextTick}from"vue";
	onMounted(()=>{
		nextTick(()=>{
			let iframe= document.querySelector('#myIframeDiv');
			iframe.src = window.location.origin+"/api/erp/swagger-ui/index.html"; // 就是这一行代码，一定要注意哦
			myIframeDiv.appendChild(iframe)
		})
	})
	 
</script>
<style scoped="scoped">
	 
</style>

	
 