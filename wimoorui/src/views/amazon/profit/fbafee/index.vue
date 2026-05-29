<template>
		<div class="main-sty">
	 <el-tabs v-model="activeName" class="demo-tabs" @tab-click="handleClick">
	     <el-tab-pane label="FBA费用公式" name="fbafee"></el-tab-pane>
	     <el-tab-pane label="尺寸分段公式" name="tierformat"></el-tab-pane>
		 <el-tab-pane label="分段管理" name="tier"></el-tab-pane>
		 <el-tab-pane label="分段重量" name="weight"></el-tab-pane>
		 <el-tab-pane label="佣金" name="referralfee"></el-tab-pane>
		 <el-tab-pane label="仓储费" name="storagefee"></el-tab-pane>
		 <el-tab-pane label="货件目标" name="inplace"></el-tab-pane>
		 <el-tab-pane label="货件目标费" name="inplacefee"></el-tab-pane>
		 <el-tab-pane label="FBA SIPP" name="fbasipp"></el-tab-pane>
		 
	   </el-tabs>
	   <FBAFee v-if="activeName=='fbafee'"></FBAFee>
	   <TierFormat v-if="activeName=='tierformat'"></TierFormat>
	   <Tier v-if="activeName=='tier'"></Tier>
	   <TierWeight v-if="activeName=='weight'"></TierWeight>
	   <ReferralFee v-if="activeName=='referralfee'"></ReferralFee>
	   <StorageFee v-if="activeName=='storagefee'"></StorageFee>
	   <Inplace v-if="activeName=='inplace'"></Inplace>
	   <InplaceFee v-if="activeName=='inplacefee'"></InplaceFee>
	   <FbaSipp v-if="activeName=='fbasipp'"></FbaSipp>
	   </div>
</template>
<script>
    export default{ name:"FBA费设置" };
</script>
<script setup>
	import {h, ref ,watch,reactive,onMounted,toRefs} from 'vue'
	import FBAFee from "./component/fba.vue";
	import TierFormat from "./component/tierformat.vue";
	import Tier from "./component/tier.vue";
	import TierWeight from "./component/tierweight.vue";
	import ReferralFee from "./component/referralfee.vue";
	import StorageFee from "./component/storagefee.vue";
	import Inplace from "./component/inplace.vue";
	import InplaceFee from "./component/inplacefee.vue";
	import FbaSipp from "./component/fbasipp.vue";
	
	 let state=reactive({
	 	 activeName:"fbafee",
	 });
	 let {
	 activeName
	 } =toRefs(state);
</script>

<style>
</style>
