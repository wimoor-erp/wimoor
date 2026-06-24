<template>
  <div class="main-sty ">
    <el-tabs v-model="activeName" class="demo-tabs" @tab-click="handleClick">
      <el-tab-pane label="店铺利润" name="first"></el-tab-pane>
      <el-tab-pane label="月度结算" name="second"></el-tab-pane>
      <el-tab-pane label="月度结算(简洁版)" name="third"></el-tab-pane>
    </el-tabs>
    <div v-if="activeName === 'first'">
      <ProfitGroup />
    </div>
    <div v-if="activeName === 'second'" >
      <MonthReport />
    </div>
    <div v-if="activeName === 'third'">
      <Month />
    </div>
  </div>
</template>
<script setup>
import { ref,reactive,onMounted,toRefs,computed,nextTick} from 'vue'
import ProfitGroup from './components/profitGroup.vue';
import MonthReport from '@/views/amazon/payment/monthReport/index.vue';
import Month from '@/views/amazon/payment/finance/index.vue';

const activeName = ref('first');
</script>
<style scoped>

</style>