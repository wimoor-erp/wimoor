<template>
  <div class="main-sty ">
    <el-tabs v-model="activeName" class="demo-tabs" @tab-click="handleClick">
      <el-tab-pane label="账期" name="first"></el-tab-pane>
      <el-tab-pane label="汇总" name="second"></el-tab-pane>
      <el-tab-pane label="报表" name="third"></el-tab-pane>
    </el-tabs>
    <div v-if="activeName === 'first'">
      <Records />
    </div>
    <div v-if="activeName === 'second'">
      <MonthSummary />
    </div>
    <div v-if="activeName === 'third'">
      <Report />
    </div>
  </div>
</template>
<script setup>
import { ref,reactive,onMounted,toRefs,computed,nextTick} from 'vue'
import MonthSummary from '@/views/amazon/payment/summaryMonth/index.vue';
import Records from "./records.vue";
import Report from "./report.vue";


const activeName = ref('first');
</script>
<style scoped>

</style>