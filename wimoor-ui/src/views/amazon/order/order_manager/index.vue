<template>
	<div class="main-sty">
		<Table ref="tableRef" />
	</div>
</template>
<script>
    export default{ name:"订单列表" };
</script>
<script setup>
	import { ref ,watch,reactive,onMounted} from 'vue'
	import Table from "./components/table.vue";
</script>

<style>
</style>
