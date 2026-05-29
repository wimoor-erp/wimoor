<template>
	<div class="el-white-bg">
					 <el-scrollbar class="he-scr-car" @scroll="scroll">
						 <div class="gird-line-head">
						 <h3>产品代料单详情</h3>
						 <el-button   class='ic-btn' title='帮助文档'>
						   <help theme="outline" size="16" :strokeWidth="3"/>
						 </el-button>
						 </div>
						 <div class="fill-from-body">
							<el-descriptions :column="2">
							    <el-descriptions-item label="单据编码">{{warehouseform.number}}</el-descriptions-item>
							    <el-descriptions-item label="操作仓库">{{warehouseform.warehouse}}</el-descriptions-item>
								<el-descriptions-item label="申请人">{{warehouseform.creator}}</el-descriptions-item>
								<el-descriptions-item label="申请时间">{{dateTimesFormat(warehouseform.createdate)}}</el-descriptions-item>
								<el-descriptions-item label="备注">
								 {{warehouseform.remark}}
								</el-descriptions-item>
							  </el-descriptions>
						 <el-divider></el-divider>
						 <div class="mark-re">
							<div>
						   <h5 >商品列表</h5>
						   </div>
						 </div>
					<el-table border :data="formEntryList">
												 <el-table-column label="序号" type="index" width="60"/> 
												 <el-table-column label="调入产品信息"  show-overflow-tooltip>
												    <template #default="scope">
														<el-space>
															<div>
																<el-image v-if="scope.row.image" :src="scope.row.image" class="product-img" ></el-image>
																<el-image v-else :src="$require('empty/noimage40.png')"  class="product-img"  ></el-image>
															</div>
														<div>
												         <div class='name'>{{scope.row.name}}</div>
												         <div class='sku'>{{scope.row.sku}} </div>
													   </div>
													   </el-space>
												   </template>
												 </el-table-column>
												 <el-table-column label="调入产品库存" prop="fulfillable">
													 <template #default="scope">
														  {{scope.row.fulfillable}}
													  </template>
												</el-table-column>
												 <el-table-column label="调出产品信息"  show-overflow-tooltip>
												    <template #default="scope">
														<el-space>
															<div>
																<el-image v-if="scope.row.location" :src="scope.row.location" class="product-img" ></el-image>
																<el-image v-else :src="$require('empty/noimage40.png')"  class="product-img"  ></el-image>
															</div>
														<div>
												         <div class='name'>{{scope.row.fromname}}</div>
												         <div class='sku'>{{scope.row.fromsku}} </div>
													   </div>
													   </el-space>
												   </template>
												 </el-table-column>
												 <el-table-column label="调出产品库存" prop="fromfulfillable">
													 <template #default="scope">
														 {{scope.row.fromfulfillable}}
													  </template>
												</el-table-column>  
												 <el-table-column label="调库数量" prop="amount">
													  <template #default="scope">
														  <el-space>
															  <div style="padding-right:20px"> {{scope.row.amount}}</div>
															  <div>
																  <table>
																	  <tr>
																		  <td>{{scope.row.sku}}</td>
																		  <td>
																			  <shelfInvOpt
																			   :materialid="scope.row.materialid"
																			   :formid="scope.row.formid"
																			   :warehouseid="scope.row.warehouseid"
																			   :amount="scope.row.amount"
																			   opt="1"
																			   formtype="dispatch-inner"></shelfInvOpt>
																		  </td>
																	  </tr>
																	  <tr>
																		  <td>
																			  {{scope.row.fromsku}}
																		  </td>
																		  <td>
																			  <shelfInvOpt
																			   :materialid="scope.row.frommaterialid"
																			   :formid="scope.row.formid"
																			   :warehouseid="scope.row.warehouseid"
																			   :amount="scope.row.amount"
																			   opt="0"
																			   formtype="dispatch-inner"></shelfInvOpt>
																		  </td>
																	  </tr>
																  </table>
															  </div>
														  </el-space>
													   </template>
												 </el-table-column>
					</el-table>
						</div>
					</el-scrollbar>
						<div class='text-center mar-top-16'>
							 <div style="padding-top:10px;">
								<el-space>
									<el-button type="" @click="closePage">关闭</el-button>
								</el-space>
							</div>
						</div>
	</div>
</template>

<script setup>
	import {ArrowDown,Edit,View,Upload,Download} from '@element-plus/icons-vue';
	import { ref,reactive,onMounted,watch,toRefs,inject } from 'vue';
	import {Help,} from '@icon-park/vue-next';
	import { ElMessage, ElMessageBox } from 'element-plus';
	import {dateFormat,dateTimesFormat} from '@/utils/index.js';
	import changeApi from '@/api/erp/inventory/changeApi.js';
	import {useRouter } from 'vue-router';
	import {redirectToList} from '@/utils/page_helper.js';
	import shelfInvOpt from "@/views/erp/components/shelfInvOpt.vue";
	const emitter = inject("emitter"); // Inject `emitter`
	
	const router = useRouter();
	const id = router.currentRoute.value.query.id;
	const state = reactive({
		warehouseform:{},
		formEntryList:[],
	})
	const{
		warehouseform,
		formEntryList,
	}=toRefs(state)
	function load(){
		changeApi.getData({"id":id}).then((res)=>{
			if(res.data){
				state.warehouseform=res.data.warehouseform;
				state.formEntryList=res.data.formEntryList;
			}
		});
	}
	function closePage(){
		redirectToList(router,"/erp/warehouse/exchange",'代料单');
	}
	
	defineExpose({
		
	})
	onMounted(()=>{
		load();
	});
</script>

<style>
	.font-12{font-size: 12px;margin-right:4px;}
		.he-scr-car{
			height:calc(100vh - 176px);
			margin-bottom: 20px;
		}
	.in-wi-24{
			width: 400px;
		}
		.mark-re{
			margin-top: 16px;
			margin-bottom:16px;
			display: flex;
			align-items: center;
			justify-content: space-between;
		}
	.mark-re h5::before{
		content: "";
		display: inline-block;
		height: 13px;
		width: 4px;
		margin-right:8px;
		background-color: #FF6700;
	}
	.fill-from-body{
		margin:16px 24px;
	}
</style>
