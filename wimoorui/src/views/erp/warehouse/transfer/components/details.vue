<template>
	<div class="el-white-bg">
					 <el-scrollbar class="he-scr-car" @scroll="scroll">
						 <div class="gird-line-head">
						 <h3>调库单详情</h3>
						 <div>
						 <el-button   class='ic-btn' title='帮助文档'>
						   <help theme="outline" size="16" :strokeWidth="3"/>
						 </el-button>
						 </div>
						 </div>
						 <div class="fill-from-body" >
							 <el-steps :active="handlerStep(warehouseform.auditstatus)" finish-status="success" align-center class="m-b-t-24">
							   <el-step title="创建" />
							   <el-step title="审核"  />
							   <el-step title="配货中"  />
							   <el-step title="已发货"  />
							   <el-step title="完成"  />
							 </el-steps>
							<el-descriptions :column="2">
							    <el-descriptions-item label="单据编码">{{warehouseform.number}}</el-descriptions-item>
							    <el-descriptions-item label="调出仓库">{{warehouseform.fromwarehouse}}</el-descriptions-item>
							    <el-descriptions-item label="预计到货日期">
									<span v-if="warehouseform.arrivalTime">{{dateFormat(warehouseform.arrivalTime)}}</span>
									<span v-else>无</span>
								</el-descriptions-item>
								<el-descriptions-item label="调入仓库">{{warehouseform.towarehouse}}</el-descriptions-item>
								<el-descriptions-item label="调库申请人">{{warehouseform.creator}}</el-descriptions-item>
								<el-descriptions-item label="操作类型">{{warehouseform.ftypeName}}</el-descriptions-item>
								<el-descriptions-item label="状态">
									<el-tag v-if="warehouseform.auditstatus==0" type="warning" >未提交</el-tag>
									<el-tag v-if="warehouseform.auditstatus==1" type="info" >待审核</el-tag>
									<el-tag v-if="warehouseform.auditstatus==2" >配货中</el-tag>
									<el-tag v-if="warehouseform.auditstatus==3" >已发货</el-tag>
									<el-tag v-if="warehouseform.auditstatus==4" type="success" >已完成</el-tag>
									<el-tag v-if="warehouseform.auditstatus==5" type="danger" >已驳回</el-tag>
								</el-descriptions-item>
								<el-descriptions-item label="调库时间">{{dateFormat(warehouseform.createdate)}}</el-descriptions-item>
								<el-descriptions-item label="备注">
								 {{warehouseform.remark}}
								</el-descriptions-item>
							  </el-descriptions>
						 <el-divider></el-divider>
						 <div class="mark-re">
							<div>
						   <h5 >调库商品 </h5>
						    <span class="font-extraSmall text-red"  v-if="needApply">注意仓库地址不同时，建议由调入仓库负责人审核对应调库单，已确认正常收货</span>
						   </div>
						 </div>
						 <el-table border :data="dispatchFormEntryList">
							 <el-table-column label="序号" type="index" width="80"/> 
							 <el-table-column  prop="image" label="图片"  >
							    <template #default="scope" >
							     <el-image v-if="scope.row.image" :src="scope.row.image"   class="product-img"  ></el-image>
							 	<el-image v-else :src="$require('empty/noimage40.png')"   class="product-img"   ></el-image>
							   </template>
							 </el-table-column>
							 <el-table-column  prop="product" label="名称/SKU"  show-overflow-tooltip>
							    <template #default="scope">
							       <div class='name'>{{scope.row.name}}</div>
							       <div class='sku'>{{scope.row.sku}}
							       </div>
							   </template>
							 </el-table-column>
							 <el-table-column prop="amount" width="400" label="调库数量">
							 <template #default="scope">
											  <el-space>
												  <div style="padding-right:20px"> {{scope.row.amount}}</div>
												  <div>
													  <table>
														  <tr>
															  <td>
																 {{warehouseform.towarehouse}} 
															  </td>
															  <td>
																 <shelfInvOpt
																  :materialid="scope.row.materialid"
																  :formid="scope.row.formid"
																  :warehouseid="scope.row.to_warehouseid"
																  :amount="scope.row.amount"
																  opt="1"
																  formtype="dispatch"></shelfInvOpt> 
															  </td>
														  </tr>
														  <tr>
															  <td>
																  {{warehouseform.fromwarehouse}} 
															  </td>
															  <td>
																<shelfInvOpt
																 :materialid="scope.row.materialid"
																 :formid="scope.row.formid"
																 :warehouseid="scope.row.from_warehouseid"
																 :amount="scope.row.amount"
																 opt="0"
																 formtype="dispatch"></shelfInvOpt>  
															  </td>
														  </tr>
													  </table>
												  </div>
											  </el-space>
										   </template>
							 </el-table-column>
						    <el-table-column   label="调出仓库">
						  	     <el-table-column prop="from_warehouse_inbound" label="待入库"/>
							     <el-table-column prop="from_warehouse_fulfillable" label="可用库存"/>
							     <el-table-column prop="from_warehouse_outbound" label="待出库"/>
							 </el-table-column>
							 <el-table-column   label="调入仓库">
							      <el-table-column prop="to_warehouse_inbound" label="待入库"/>
							      <el-table-column prop="to_warehouse_fulfillable" label="可用库存"/>
							      <el-table-column prop="to_warehouse_outbound" label="待出库"/>
							  </el-table-column>
						 </el-table>
						</div>
					</el-scrollbar>
						<div class='text-center mar-top-16' >
							       <el-button type="" @click="closePage">关闭</el-button>
							       <el-button v-if="!needApply&&warehouseform.auditstatus<=3" type="primary" plain @click="reSubmitForm(4,'done')">直接完成</el-button> 
										<span style="padding-left:30px;"></span>
									<el-button v-if="warehouseform.auditstatus!=5 && warehouseform.auditstatus!=0"  @click.stop="reSubmitForm(warehouseform.auditstatus,'back')">
										<span v-if="warehouseform.auditstatus==1">驳回</span>
										<span v-if="warehouseform.auditstatus==2||warehouseform.auditstatus==3">取消</span>
										<span v-if="warehouseform.auditstatus==4">撤销</span>
									</el-button>
								
									<el-button v-if="warehouseform.auditstatus==0" type="" @click="gotoeditForm">编辑</el-button>
									<el-button v-if="warehouseform.auditstatus<=3" type="primary" @click="reSubmitForm(warehouseform.auditstatus,'submit')">
										<span v-if="warehouseform.auditstatus==0">提交</span>
										<span v-if="warehouseform.auditstatus==1">通过</span>
										<span v-if="warehouseform.auditstatus==2">发货</span>
										<span v-if="warehouseform.auditstatus==3">收货</span>
									</el-button>
						</div>
						</div>
</template>

<script setup>
	import {ArrowDown,Edit} from '@element-plus/icons-vue'
	import {Plus,Minus,Help} from '@icon-park/vue-next';
	import { ref,reactive,onMounted,watch,inject,toRefs } from 'vue'
	import {ElMessage,ElMessageBox } from 'element-plus'
	import { useRoute,useRouter } from 'vue-router';
	import {dateFormat,dateTimesFormat} from '@/utils/index.js';
	import dispatchApi from '@/api/erp/inventory/dispatchApi.js';
	import shelfInvOpt from "@/views/erp/components/shelfInvOpt.vue";
	import warehouseApi from '@/api/erp/warehouse/warehouseApi.js';
	const MaterialRef = ref()
	const router = useRouter();
	const id = router.currentRoute.value.query.id;
	const emitter = inject("emitter"); // Inject `emitter`
	function closeTab(){
		emitter.emit("removeTab", 100);
	};
	const state = reactive({
		warehouseform:{},
		dispatchFormEntryList:[],
			warehouseList:[],
			needApply:false,
	})
	const {
		warehouseform,
		dispatchFormEntryList,
		warehouseList,
		needApply
	}=toRefs(state)
	
	function load(){
		dispatchApi.getData({"id":id}).then((res)=>{
			if(res.data){
				state.warehouseform=res.data.warehouseform;
				state.dispatchFormEntryList=res.data.dispatchFormEntryList;
				state.warehouseList=[];
				warehouseApi.getWarehouseList().then(function(res){
					if(res.data && res.data.length>0){
						res.data.forEach(warehouses=>{
							 if(warehouses&&warehouses.children&&warehouses.children.length>0){
							     warehouses.children.forEach(item=>{  state.warehouseList.push(item);  })
							 }
						})
					}
					if(checkDifferentWarehouse()){
						state.needApply=true;
					}else{
						state.needApply=false;
					}
				})
				
			}
		});
	}
	function handlerStep(status){
		if(status==5){
			status=-1;
		}
		if(status==4){
			status=5;
		}
		return status;
	}
	function closePage(){
			var refreshvalue="";
			if(router.currentRoute.value.query.replace){
				refreshvalue=true;
			}
			router.push({
				path:"/erp/warehouse/transfer",
				query:{
					title:'调库单',
					path:"/erp/warehouse/transfer",
					replace:true,
					refresh:refreshvalue,
				},
			})
	}
	function reSubmitForm(status,ftype){
		//状态的回滚和提交操作
		var titleStr="";
		var title="";
		if(ftype=="back"){
			if(status==1){
				titleStr="您确认要驳回该调库单吗？";
				title="驳回调库单";
				status=5;
			}else if(status==2){
				titleStr="您确认要取消该调库单吗？";
				title="取消调库单";
				status=5;
			}else if(status==3){
				titleStr="您确认要取消该调库单吗？";
				title="取消调库单";
				status=5;
			}else if(status==4){
				titleStr="您确认要撤销该调库单吗？";
				title="撤销调库单";
				status=5;
			} 
		}
		if(ftype=="submit"){
			if(status==0){
				titleStr="您确认要提交该调库单吗？";
				title="提交调库单";
				status=1;
			}else if(status==1){
				titleStr="您确认要通过该调库单吗？";
				title="通过调库单";
				status=2;
			}else if(status==2){
				titleStr="您确认要发货出库吗？";
				title="发货调库单";
				status=3;
			}else if(status==3){
				titleStr="请确认您已收到所有货物？";
				title="收货调库单";
				status=4;
			} 
		}
		if(ftype=="done"){
			titleStr="您确定要直接完成发货单吗？";
			title="完成调库单";
			status=4;
		}
		 ElMessageBox.confirm(
			titleStr,
			title,
			{
			  confirmButtonText: '确定',
			  cancelButtonText: '取消',
			  type: 'warning',
			}
		  )
			.then(() => {
					dispatchApi.examine({"ids":id,"status":status}).then((res)=>{
						  if(res.data){
							  ElMessage.success(res.data);
							  load();
						  }else{
							  ElMessage.error("操作失败");
						  }
					  });
			})
			.catch(() => {
			  ElMessage.info('取消操作');
			})
		
	}
	function gotoeditForm(){
			router.push({
				path:"/e/w/t",
				query:{
					title:'编辑调库单',
					path:"/e/w/t",
					id:id,
					fwid:state.warehouseform.from_warehouseid,
					twid:state.warehouseform.to_warehouseid,
				    replace:true
				},
			})
	}
	function checkDifferentWarehouse(){
		var fromwarehouse={};
		var towarehouse={};
		state.warehouseList.forEach(item=>{
			if(item.id==state.warehouseform.from_warehouseid){
				fromwarehouse=item;
			}
			if(item.id==state.warehouseform.to_warehouseid){
				towarehouse=item;
			}
		})
		if(fromwarehouse.addressid!=towarehouse.addressid){
			return true;
		}else{
			return false;
		}
	}
	onMounted(()=>{
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
	.m-b-t-24{
		margin-bottom:24px;
		margin-top:24px;
	}
</style>
