<template>
	<div class="main-sty">
		<Header  @getdata="getdata" @download="downloadlist" />
		<Table ref="tableRef" />
	</div>
</template>
<script>
    export default{ name:"移除订单" };
</script>
<script setup>
	import { ref ,watch,reactive,onMounted} from 'vue'
	import Header from "./components/header.vue";
	import Table from "./components/table.vue";
	let tableRef = ref(Table); 
	function getdata(data){
		tableRef.value.loadData(data);
	}
	function downloadlist(){
		tableRef.value.downloadList();
	}
 
</script>

<style>
</style>
