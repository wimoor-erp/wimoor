<template>
	<div class="el-white-bg">
						 <div class="gird-line-head">
						 <h3>请款单详情</h3>
						 <div>
						 <el-button   class='ic-btn' title='帮助文档'>
						   <help theme="outline" size="16" :strokeWidth="3"/>
						 </el-button>
						 </div>
						 </div>
						 <div class="fill-from-body"  >
							 <el-steps :active="step" finish-status="success" class="m-b-t-24">
							   <el-step title="提交"    >
								   <template #description>
										  <el-space class="font-extraSmall">
											  <span >{{formData.creatorname}}</span>
											  <span >{{dateTimesFormat(formData.createtime)}}</span>
										  </el-space>
								   </template>
							   </el-step>
							   <el-step title="审核"  >
								   <template #description>
										  <el-space class="font-extraSmall">
											  <span >{{NullTransform(formData.auditorname)}}</span>
											  <span >{{dateTimesFormat(formData.audittime)}}</span>
										  </el-space>
								   </template>
							   </el-step>
							   <el-step title="付款"   />
							   <el-step title="完成"  />
							 </el-steps>
							<el-descriptions :column="3">
							    <el-descriptions-item label="请款单号">
									{{formData.number}}
								</el-descriptions-item>
							    <el-descriptions-item label="关联单号">
									{{formData.purnumber}}
								</el-descriptions-item>
								<el-descriptions-item label="付款方式">
									{{formData.paymethod}}
								</el-descriptions-item>
								<el-descriptions-item label="状态">
									<el-tag  type="warning" v-if="formData.auditstatus==0">待审核</el-tag>
									<el-tag   v-if="formData.auditstatus==1">已审核待付款</el-tag>
									<el-tag  type="success" v-if="formData.auditstatus==2">已完成</el-tag>
									<el-tag  type="danger" v-if="formData.auditstatus==3">已驳回</el-tag>
								</el-descriptions-item>
								<el-descriptions-item label="备注">
									{{formData.remark}}
								</el-descriptions-item>
								</el-descriptions>
								<el-descriptions :column="1">
								<el-descriptions-item label="产品信息">
									<div class="flex-center">
									   <el-image v-if="formData.image" :src="formData.image" class="img-40"  width="40" height="40"  ></el-image>
									   <el-image v-else :src="$require('empty/noimage40.png')"  class="img-40"  width="40" height="40"  ></el-image>
									   <div >
										   <div>{{formData.mname}}</div>
										   <p class="sku">{{formData.sku}}</p>
									   </div>
									</div>
								</el-descriptions-item>
								</el-descriptions>
						<!-- <el-divider></el-divider> -->
						<div class="m-b-t-24">
						 <el-tabs v-model="activeName" type="card"  @tab-change="handleClick">
						     <el-tab-pane label="请款详情 " name="0">
								 <el-table :data="paylist">
									 <el-table-column label="费用项目" prop="projectname"/>
									 <el-table-column label="请款金额(￥)" prop="payprice"></el-table-column>
									 <el-table-column label="请款时间" prop="createdate">
										 <template #default="scope">
										 	 {{dateTimesFormat(scope.row.createdate)}}
										 </template>
									 </el-table-column>
								 </el-table>
							 </el-tab-pane>
						     <el-tab-pane label="付款记录" name="1">
								  <PayRecord ref="payrecordRef" />
							 </el-tab-pane>
						   </el-tabs>
							
						 </div>
						 <div>
							 
						 </div>
						</div>
						<div class='text-center mar-top-16' >
							<el-button type="" @click="closeTab">关闭</el-button>
						</div>
						</div>
</template>
<script setup>
	import {Plus,Minus,Help,Printer,AddPrint,InboxOut} from '@icon-park/vue-next';
	import { ref,reactive,onMounted,watch,inject,toRefs } from 'vue'
	import {ElMessage,ElMessageBox } from 'element-plus'
	import { useRoute,useRouter } from 'vue-router';
	import NullTransform from"@/utils/null-transform";
	import PayRecord from "@/views/erp/purchase/orders/components/pay_record.vue";
	import {dateFormat,dateTimesFormat,formatFloat} from '@/utils/index.js';
	import {CopyDocument,Crop,Edit,Warning,ArrowDown} from '@element-plus/icons-vue';
	import purchaseFinlistApi from '@/api/erp/purchase/finance/listApi.js';
	const router = useRouter();
	const formid = router.currentRoute.value.query.id;
	const payrecordRef=ref();
	const emitter = inject("emitter"); // Inject `emitter`
	function closeTab(){
		emitter.emit("removeTab", 100);
	};
	const state = reactive({
		activeName:'0',
		step:'1',
		formData:{},
		paylist:[],
	})
	const {
		step,
		activeName,
		formData,
		paylist,
	}=toRefs(state)

	function handleClick(val){
		if(val==1){
			payrecordRef.value.show(state.paylist);
		}
	}
	function load(){
		purchaseFinlistApi.getdetail({"id":formid}).then((res)=>{
			if(res.data){
				state.formData=res.data.form;
				state.paylist=res.data.paylist;
				if(state.paylist){
					state.paylist.forEach(item=>{
						item.opttime=dateTimesFormat(item.opttime);
						item.methodname=state.formData.paymethod;
						item.operator=state.formData.operatorname;
					})
				}
				if(state.formData.auditstatus>=2){
					state.step=(parseInt(state.formData.auditstatus)+2).toString();
				}else{
					state.step=(parseInt(state.formData.auditstatus)+1).toString();
				}
			}
		});
	}
	onMounted(() => {
	   load();
	});
	
</script>

<style scoped>
	.font-12{font-size: 12px;margin-right:4px;}
		.he-scr-car{
			height:calc(100vh - 150px);
			margin-bottom: 20px;
		}
	.in-wi-24{
			width: 400px;
		}
	.fill-from-body{
		margin:16px 24px;
	}
	.m-b-t-24{
		margin-bottom:24px;
		margin-top:24px;
	}
	.img-40{width: 40px;
	height: 40px;flex: none;
	margin-right: 8px;}
</style>
