<template>
 <div class="main-sty">
	<el-tabs v-model="activeName" class="demo-tabs" @tab-click="handleClick">
	    <el-tab-pane label="备份数据库" name="backup"></el-tab-pane>
	    <el-tab-pane label="迁移表" name="move"></el-tab-pane>
	    <el-tab-pane label="迁移账期数据" name="movesett"></el-tab-pane>
	  </el-tabs>
	  <Backup v-if="activeName=='backup'"></Backup>
	  <MoveTable v-if="activeName=='move'"></MoveTable>
	  <Settlement v-if="activeName=='movesett'"></Settlement>
</div>
</template>
<script setup>
import {h, ref ,watch,reactive,onMounted,toRefs} from 'vue'
import {Search,ArrowDown,} from '@element-plus/icons-vue';
import { ElMessage ,ElMessageBox} from 'element-plus';
import dataMoveApi from "@/api/sys/tool/dataMoveApi.js";
import Backup from "./components/backup.vue";
import MoveTable from "./components/movetable.vue";
import Settlement from "./components/settlement.vue";
const state = reactive({  activeName:"backup"});
const { activeName } = toRefs(state);

 
</script>

<style>
</style>