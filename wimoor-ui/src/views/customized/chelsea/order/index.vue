<template>
	<div class="main-sty">
		<div class="con-header">
			<el-tabs v-model="activeStatus"  @tab-change="handleClick">
			  <el-tab-pane v-for="(item,index) in orderStateList"  :name="item.name" :key="item.name">
				  <template #label>
					  <span >{{item.label}}</span>
					  <span  v-if="item.length">( <span class="text-orange">{{item.length}}</span> )</span>
				   </template>
			  </el-tab-pane> 
			</el-tabs>
		</div>
		<div class="con-body">
		 	<SalesTable v-if="activeStatus=='0'" @getdata="changedata" ref="headerRef"  />
			<InvTable   v-if="activeStatus=='1'" @getdata="changedata" ref="headerRef"  />
			<PlanTable  v-if="activeStatus=='2'" @getdata="changedata" ref="headerRef"  />
		</div>
	</div>
</template>
<script>
    export default{ name:"订单" };
</script>
<script setup>
	import SalesTable from"./components/sales_table.vue";
	import InvTable from"./components/inv_table.vue";
	import PlanTable from"./components/plan_table.vue";
	import {ref,reactive,toRefs,onMounted} from"vue"
	import {MoreOne,} from '@icon-park/vue-next';
	import {Histogram} from '@element-plus/icons-vue'
	import { ElMessage, ElMessageBox, ElDivider } from 'element-plus'
	import {useRouter } from 'vue-router';
	import {dateFormat,dateTimesFormat} from '@/utils/index.js';
	import orderApi from "@/api/erp/order/orderApi.js";
	const router = useRouter();
	let globalTable=ref();
	let headerRef=ref();
	const state = reactive({
		activeStatus:'0',
		orderStateList:[
			{label:'订单销量',name:'0'},
			{label:'海外仓库存',name:'1'},
			{label:'备货计划',name:'2'},
		],
	})
	const {
		activeStatus,orderStateList
	}=toRefs(state)
	 
	onMounted(() => {
	   
	});
</script>

<style>
</style>
