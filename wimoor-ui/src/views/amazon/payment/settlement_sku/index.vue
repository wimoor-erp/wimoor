<template>
  <div class="main-sty ">
  <el-tabs v-model="activeName" class="demo-tabs" @tab-click="handleClick">
    <el-tab-pane label="SKU详情" name="first"></el-tab-pane>
    <el-tab-pane label="账期SKU" name="second"></el-tab-pane>
  </el-tabs>
  <div v-if="activeName === 'first'">
    <Skutable />
  </div>
  <div v-if="activeName === 'second'">
    <Settlement />
  </div>
  </div>
</template>
<script setup>
import { ref,reactive,onMounted,toRefs,computed,nextTick} from 'vue'
import Settlement from './settlement.vue';
import Skutable from './skutable.vue';
const activeName = ref('first');
</script>
<style scoped>

</style>