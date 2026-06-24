<template>
	<div class="main-sty">
	  <div class="con-header" >
		  <el-tabs v-model="activeName" class="demo-tabs" @tab-click="handleClick">
			<el-tab-pane label="优惠券" name="Copuon"></el-tab-pane>
			<el-tab-pane label="促销" name="Promotions"></el-tab-pane>
		  </el-tabs>
	  </div>
	   <Coupone v-if="activeName=='Copuon'"></Coupone>
	   <Promotions v-if="activeName=='Promotions'"></Promotions>
	   <Seller v-if="activeName=='Seller'"></Seller>
	   <OrderFeedback  v-if="activeName=='feedback'"></OrderFeedback>
	  </div>
</template>

<script>
    export default{ name:"促销报告" };
</script>
<script setup>
	import {h, ref ,watch,reactive,onMounted,toRefs} from 'vue';
		import Coupone from "./coupon/index.vue";
		import Promotions from "./promotions/index.vue";
		import Seller from "./seller/index.vue";
		import OrderFeedback from "./orderfeedback/index.vue";
		let activeName=ref("Copuon");
</script>

<style>
</style>