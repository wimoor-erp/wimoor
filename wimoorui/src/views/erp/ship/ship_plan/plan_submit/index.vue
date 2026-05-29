<template>
	<div class="el-white-bg">
	<div class="gird-line-head">
		<el-space class="font-small" :size="24">
			<span><span class="text-gray">发货店铺:  </span>{{groupname}}</span>
			<span ><span class="text-gray">发货仓库:  </span>{{warehousename}}</span>
		</el-space>
		<el-button   class='ic-btn' title='帮助文档'>
			 <help theme="outline" size="16" :strokeWidth="3"/>
		</el-button>
	</div>
	<div >
		<el-tabs tab-position="left" class="demo-tabs">
		    <el-tab-pane :label="item.key" v-for="item in tableData">
				   <template #label>
				       <div style="width:150px;margin-top:20px;">
					    <el-space><local class="ic-cen" theme="filled" size="16" />
				        <div> {{item.marketname}}</div></el-space></div>
				      </template>
				     <MarketPlan :planData="item" 
					             :transtypeOptions="transtypeOptions"  
					             :warehouseid="warehouseid" 
								 :groupid="groupid"
								 :warehousename="warehousename"
								 >
					</MarketPlan>
				 </el-tab-pane>
		  </el-tabs>
	</div>
	</div>
</template>

<script setup>
	import {} from '@element-plus/icons-vue';
	import {Local,Help,DataSheet,BookmarkOne} from '@icon-park/vue-next';
	import { ref ,nextTick ,reactive,onMounted,toRefs} from 'vue'
	import warehouseApi from '@/api/erp/warehouse/warehouseApi.js';
	import groupApi from '@/api/amazon/group/groupApi.js';
	import planApi from '@/api/erp/ship/planApi.js';
	import MarketPlan from './market_plan.vue';
	import { useRouter } from 'vue-router';
	import transportationApi from '@/api/erp/ship/transportationApi.js';
	let state=reactive({
		eu:false,
		tableData:[],
		warehousename:"",
		transtypeOptions:[],
		groupname:"",
	})
	let{eu,tableData,warehousename,groupname,transtypeOptions}=toRefs(state)
	let router = useRouter() ;
	const groupid=router.currentRoute.value.query.groupid;
	const warehouseid=router.currentRoute.value.query.warehouseid;
	onMounted(async ()=>{
		await warehouseApi.detail(warehouseid).then(res=>{
			state.warehousename=res.data.name;
		});
		await groupApi.getAmazonGroupById(groupid).then(res=>{
			state.groupname=res.data.name;
		});
		await transportationApi.getTransTypeAll().then((res)=>{
			state.transtypeOptions=res.data;
		});
		await planApi.list({warehouseid:warehouseid,groupid:groupid}).then(res=>{
			state.tableData=res.data;
		});
	})
	
</script>
<style>
	.el-tabs--left .el-tabs__header.is-left,
	.el-tabs--left .el-tabs__header.is-right, 
	.el-tabs--left .el-tabs__nav-scroll, 
	.el-tabs--left .el-tabs__nav-wrap.is-left
	  {
	    height: calc(100vh - 100px) !important;
	 }
</style>
<style scoped="scoped">
	.demo-tabs > .el-tabs__content {
	  padding: 32px;
	  color: #6b778c;
	  font-size: 32px;
	  font-weight: 600;
	}
	.text-gray{
		color:var(--el-text-color-secondary)
	}
	.height-screen{
		height: calc(100vh - 100px);
		width: 100%;
		padding:16px ;
	}
	.flex-warp{
		display: flex;
	}
	.m-t-16{
		margin-top:16px;
	}
	.border-left{
		border-left: 1px solid var(--el-border-color-base);
	}
	.el-menu{border-right: 0;}
	.flex-v-bet{
		display: flex;
		flex-direction: column;
	}
	.flex-v-bet .title{
		font-size:12px;
		color: var(--el-text-color-secondary);
	}
	.flex-v-bet .data{
		font-size:16px;
		font-weight: 600;
	}
	.con-body{
		margin-top:16px;
	}
	.ul-list{
		margin-left: 16px;
		margin-top:10px;
	}
	.ul-list li::marker {
	    color: #FF6700;
	    line-height: 32px;
	}
	.m-b-8{
		margin-bottom:8px;
	}
</style>
