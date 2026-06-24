<template>
  <div class="app-container">
    <el-tabs v-model="activeName" class="demo-tabs" @tab-click="handleClick">
      <el-tab-pane label="期末处理" name="first"></el-tab-pane>
      <el-tab-pane label="反结账" name="third"></el-tab-pane>
    </el-tabs>
    <Periods v-if="activeName==='third'"></Periods>
    <Vouchers v-if="activeName==='first'" @switch-tab="handleSwitchTab"></Vouchers>
  </div>
</template>
<script setup>
import { ref } from 'vue'
import Periods from './components/periods.vue';
import Vouchers from './components/vouchers.vue';
const activeName = ref('first')
const handleClick = (tab, event) => {
    console.log(tab, event)
}
const handleSwitchTab = (tabName) => {
    activeName.value = tabName
}
</script>
<style scoped>

</style>