<template>
	    <div  style="margin:15px;">
		<el-tabs v-model="activeName" type="border-card"  @tab-click="handleClick">
					<el-tab-pane label="成品包装" name="first"> <ConsumableMaterial  v-if="activeName=='first'" ></ConsumableMaterial></el-tab-pane>
					<el-tab-pane label="批量组装" name="second"><AssemblyMaterial v-if="activeName=='second'" isConsumable="ok" ></AssemblyMaterial></el-tab-pane>
		</el-tabs>
  </div>
</template>
<script   setup>
import { ref } from 'vue'
import ConsumableMaterial from "./components/consumable_material.vue";
import AssemblyMaterial from "@/views/erp/purchase/process/index.vue";
const activeName = ref('first')
const handleClick = (tab, event) => {
  console.log(tab, event)
}
</script>

<style>
 
</style>