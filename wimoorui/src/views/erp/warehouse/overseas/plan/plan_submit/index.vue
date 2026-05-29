<template>
	<div class="el-white-bg">
	<div class="gird-line-head">
		<div>
		<span class="font-small">采购计划：</span>
		<el-space :size="4">
			 <span v-for="(planware,planindex) in plan.warehouseList" class="flex-center">
								   <el-icon v-if="planindex>0"><Plus /></el-icon>
			 						  <el-tag round
			 								:type="types[planindex]"
			 								effect="plain"
			 								 >
			 							{{ planware.name }}
			 					    </el-tag>
			 </span>
		</el-space>
		</div>
		<el-button   class='ic-btn' title='帮助文档'>
			 <help theme="outline" size="16" :strokeWidth="3"/>
		</el-button>
	</div>
	<div >
		<el-tabs tab-position="left" class="demo-tabs">
		    <el-tab-pane :label="item.id" v-for="item in tableData">
				   <template #label>
				       <div style="width:180px;margin-top:20px;">
					    <el-space>
						<home class="ic-cen" theme="filled" size="16" />
				        <div> {{item.name}}</div></el-space></div>
				      </template>
				     <Detail     :planData="item" 
					             :transtypeOptions="transtypeOptions"  
					             :planid="plan.id" 
								 :warehouseid="item.id"
							>
					</Detail>
				 </el-tab-pane>
		  </el-tabs>
	</div>
	</div>
</template>

<script setup>
	import {Plus} from '@element-plus/icons-vue';
	import {Home,Help,DataSheet,BookmarkOne,} from '@icon-park/vue-next';
	import { ref ,nextTick ,reactive,onMounted,toRefs} from 'vue'
	import warehouseApi from '@/api/erp/warehouse/warehouseApi.js';
	import planApi from '@/api/erp/ship/planApi.js';
	import Detail from './detail.vue';
	import { useRouter } from 'vue-router';
	import transportationApi from '@/api/erp/ship/transportationApi.js';
	import {listItem,getPlanList} from '@/api/erp/purchase/plan/planApi.js';
	let state=reactive({
		eu:false,
		tableData:[],
		transtypeOptions:[],
		types:['primary','success','info','danger','warning',
		       'primary','success','info','danger','warning'],
		plan:{},
	})
	let{eu,tableData,plan,types,transtypeOptions}=toRefs(state);
	
	// let props = defineProps({itemlist:[]});
	// const {itemlist} = toRefs(props);
	let router = useRouter() ;
	const planid=router.currentRoute.value.query.planid;
    const list=router.currentRoute.value.query.list?router.currentRoute.value.query.list:[];
 
	onMounted(async ()=>{
		 getPlanList().then(res=>{
			if(res.data&&res.data.length>0){
				 res.data.forEach(item=>{
					 if(item.id==planid){
						 state.plan=item;
					 }
				 })
			}
			listItem(planid,list).then(res=>{
				state.tableData=res.data;
			});
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
