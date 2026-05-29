<template>
	<div class="el-white-bg">
					 <el-scrollbar class="he-scr-car" @scroll="scroll">
						 <div class="gird-line-head">
						 <h3>加工单详情</h3>
						 <el-button   class='ic-btn' title='帮助文档'>
						   <help theme="outline" size="16" :strokeWidth="3"/>
						 </el-button>
						 </div>
						 <div class="fill-from-body">
							  <el-steps :active="active" finish-status="success" align-center class="m-b-t-24">
							    <el-step title="创建" />
							 <!--   <el-step title="待处理"  /> -->
							    <el-step title="处理中"  />
							    <el-step title="完成"  />
							  </el-steps>
						 <el-row  :gutter="32">
							 <el-col :span="8">
						<el-descriptions :column="1">
						    <el-descriptions-item label="单据编码">{{warehouseform.number}}</el-descriptions-item>
						    <el-descriptions-item label="操作类型">
								<el-tag effect="dark" v-if="warehouseform.ftype=='ass'" type="warning">组装</el-tag>
								<el-tag effect="dark" v-else type="danger">拆分</el-tag>
							</el-descriptions-item>
						    <el-descriptions-item label="单据状态">
								<!-- <el-tag v-if="active==1" effect="light" type="warning">待处理</el-tag> -->
								<el-tag v-if="active==2" effect="light" type="success">处理中</el-tag>
								<el-tag v-if="active==3" effect="light" type="info">已完成</el-tag>
								<el-tag v-if="active==4" effect="light" type="info">已终止</el-tag>
								<el-tag v-if="active==5" effect="light" type="info">已作废</el-tag>
							</el-descriptions-item>
							<el-descriptions-item label="操作仓库">{{warehouseform.warehouse}}
								<el-button size="small" @click="showWarehouseModal" type="primary">更换仓库</el-button>
							</el-descriptions-item>
							<el-descriptions-item label="备注">{{warehouseform.remark}}</el-descriptions-item>
						 </el-descriptions>
						 </el-col>
						<el-col :span="8">
							<el-space class="pro-content m-b-16">
							<el-image v-if="warehouseform.image" :src="warehouseform.image" class="product-img" ></el-image>
							<el-image v-else :src="$require('empty/noimage40.png')"  class="product-img"  ></el-image>
							 <div>
								 <p class="name font-12">{{warehouseform.mname}}</p>
								 <p class="sku ">{{warehouseform.skuname}}</p>
							 </div>
							 </el-space>
							 <div >
								 <p class="font-bold">
									 <el-space>
								 <span>{{handlecount}}</span>
								  <el-popover
								     placement="top-start"
								     title="修改加工数量"
								     :width="200"
									  v-hasPerm="'erp:po:ass:update'"
								   >
								    <el-input-number v-model="edit_handlecount" :precision="0" :step="1" :min="1" placeholder="请输入" />
									<el-button size="small" @click="changeAmount"
									 class="m-t-8"
									 type="primary">确认</el-button>
								     <template #reference>
								      <el-icon class="ic-cen" v-if="active==2"><Edit /></el-icon>
								     </template>
								   </el-popover>
								 </el-space>
								 </p>
								 <span class="font-extraSmall">加工数量</span>
							 </div>
						</el-col>
						<el-col :span="8" v-if="active>1">
						<el-row class="m-b-24">
						<el-col :span="12" class="text-center">
							<div >
							<p class="font-bold">{{warehouseform.inAmount}}</p>
							<span class="font-extraSmall">已入库</span>
							</div>
						</el-col>
						<el-col :span="12" class="text-center">
							<el-popover
							   placement="bottom"
							   title="产品入库"
							   :width="200"
							   trigger="click"
							   v-hasPerm="'erp:po:ass:in'"
							 >
							  <el-input-number v-model="stockAmount" :min="1" :step="1" :precision="0" placeholder="数量" />
								<el-button  @click="handleInstock"
								 class="m-t-8"
								 type="primary">确认</el-button>
							   <template #reference>
							  <el-button v-if="active==2" size="large" type="primary" class="m-t-8" >入库</el-button>
							   </template>
							 </el-popover>
							  <el-button @click.stop="showConsDialog" v-if="active==2" size="large"   class="m-t-8" >耗材出库</el-button>
							    
						</el-col>
						</el-row> 
						   <el-progress
							 :text-inside="true"
							      :stroke-width="8"
							      :percentage="(warehouseform.inAmount/warehouseform.amount)*100"
							     >
						       <span></span>
						    </el-progress>
							<div class="flex-center-between m-t-8">
								<span class="font-extraSmall">处理进度</span>
								<span class="font-extraSmall">{{warehouseform.inAmount+"/"+warehouseform.amount}}</span>
							</div>
						</el-col>
						 </el-row>
						 <el-divider></el-divider>
						 <div class="mark-re">
							<div>
						   <h5 >子SKU列表</h5>
						   </div>
						 </div>
						 <el-table border :data="tableData">
							 <el-table-column label="序号" type="index" width="80"/> 
							 <el-table-column  prop="image" label="图片" width="80" >
							    <template #default="scope">
							    <el-image v-if="scope.row.image" :src="scope.row.image" class="product-img" ></el-image>
							 	<el-image v-else :src="$require('empty/noimage40.png')"  class="product-img"  ></el-image>
							   </template>
							 </el-table-column>
							 <el-table-column label="名称/SKU"  show-overflow-tooltip>
							    <template #default="scope">
							       <div class='name'>{{scope.row.mname}}</div>
							       <div class='sku'>{{scope.row.sku}}
							       </div>
							   </template>
							 </el-table-column>
							 <el-table-column label="单位数量" prop="num"/>
							 <el-table-column label="需求数量" prop="amount"/>
							 <el-table-column label="仓库" prop="warename"/>
							 <el-table-column label="可用库存" prop="invcount"/>
							 <el-table-column label="待入库" prop="inbound">
							 <template #default="scope">
								 <el-tooltip :content="scope.row.inbounddetail">
									 {{scope.row.inbound}}
								 </el-tooltip>
							  </template>
							 </el-table-column>
							 
							 <el-table-column label="操作" width="150">
								  <template #default="scope">
								    <el-button @click.stop="gotoPurchase(scope.row)"
									 link type="primary" >采购</el-button>
					        	  </template>
							 </el-table-column>
						 </el-table>
						 <el-row v-if="active>=2">
						 	<div class="mark-re">
						 	   <div>
						 	  <h5 >入库记录</h5>
						 	  </div>
						 	</div>
							<el-table :data="recordList" border>
							  <el-table-column label="操作时间" prop="opttime" >
								   <template #default="scope">
									{{dateTimesFormat(scope.row.opttime)}}
									</template>
							  </el-table-column>
							  <el-table-column label="操作人"  prop="name"/>
							  <el-table-column label="入库数量" prop="amount" />
							  <el-table-column label="备注" prop="remark" />
							  <el-table-column label="操作" v-if="warehouseform.auditstatus==2">
								  <template #default="scope" >
									<el-button  v-hasPerm="'erp:po:ass:in'"
									 link type="primary"  @click.stop="cancelStock(scope.row)">撤销入库</el-button>
								  </template>
							  </el-table-column>
							</el-table>
						 </el-row>
						</div>
					</el-scrollbar>
						<div class='text-center mar-top-16'>
							 <div style="padding-top:10px;">
								<el-space>
									<el-button @click="closeTab">关闭</el-button>
									<el-button  v-hasPerm="'erp:po:ass:stop'" v-if="active==2" @click="stopAssembly()">终止单据</el-button>
									<el-button v-if="warehouseform.auditstatus==4||warehouseform.auditstatus==3" type="primary" @click="restartAssembly()">重启单据</el-button>
									<el-button v-if="active==1" type="primary" @click="handleStart">开始加工</el-button>
								</el-space>
							</div>
						</div>
	</div>
	
	<el-dialog v-model="warehouseVisible" title="更换入库仓库" :destroy-on-close='true' width="500px"  >
			<Warehouse defaultValue="only" @changeware="getWarehouse" />
	  <template #footer>
	  	<span class="dialog-footer">
	  		<el-button @click="warehouseVisible = false"> 关闭</el-button>
			<el-button type="primary" @click="submitWarehouse"> 确认</el-button>
	  	</span>
	  </template>
	</el-dialog>
	 <Consumable ref="consRef" :isassembly="true"></Consumable>
</template>

<script setup>
	import {ArrowDown,Edit} from '@element-plus/icons-vue'
	import {Plus,Minus,Help} from '@icon-park/vue-next';
	import { ref,reactive,onMounted,watch,inject,toRefs } from 'vue'
	import {ElMessage,ElMessageBox } from 'element-plus'
	import Warehouse from '@/components/header/warehouse.vue';
	import Consumable from '@/views/erp/shipv2/shipment_add/shipstep/components/consumable.vue';
	import { useRoute,useRouter } from 'vue-router';
	import {getData,changeAssAmount,startAssemblyEvent,
	saveRecord,stopAssemblyEvent,assemblyCancelInstock,resetAssForm,
	assemblyCompareToSku,changeAssemblyWarehouse } from '@/api/erp/assembly/assemblyApi.js';
	import {CheckInputFloat,CheckInputInt,dateTimesFormat} from '@/utils/index.js';
	const router = useRouter()
	const MaterialRef = ref()
	const consRef=ref();
	const id = router.currentRoute.value.query.id;
	const emitter = inject("emitter"); // Inject `emitter`
	function closeTab(){
		emitter.emit("removeTab", 100);
	};
	const state = reactive({
		tableData:[],
		active:1,
		handlecount:0,
		edit_handlecount:0,
		warehouseform:{},
		recordList:[],
		stockAmount:0,
		warehouseVisible:false,
		changeWarehouseid:null,
	})
	const {
		tableData,
		active,
		handlecount,
		edit_handlecount,
		warehouseform,
		recordList,
		stockAmount,
		warehouseVisible,
		changeWarehouseid,
	}=toRefs(state);
	function submitWarehouse(){
		var number= state.warehouseform.number;
		var warehouseid=state.changeWarehouseid;
		changeAssemblyWarehouse({"number":number,"warehouseid":warehouseid}).then((res)=>{
			 ElMessage.success('操作成功！');
			 state.warehouseVisible=false;
		});
	}
	
	function showWarehouseModal(){
		state.warehouseVisible=true;
	}
	function getWarehouse(value,load){
		state.changeWarehouseid=value;
	}
	
	function gotoPurchase(row){
		router.push({
			path:"/e/p/o",
			query:{
				title:'添加采购单',
				path:"/e/p/o",
				warehouseid:state.warehouseform.warehouseid,
				materialid:row.materialid
			},
		})
	}		
	 
	//开始加工
	function handleStart(){
		ElMessageBox.confirm('确认开始加工吗?', '提示', {
		 				confirmButtonText: '确定',
		 				cancelButtonText: '取消',
		 				type: 'warning',
		 })
		.then(() => {
		  startAssemblyEvent({"formid":id,"amounthandle":0}).then((res)=>{
		  	if(res.data){
		  		ElMessage.success(res.data.msg);
		  		load();
		  	}
		  });
		})
		.catch(() => ElMessage.info('已取消操作'));
		
	}
	function handleInstock(){
		if(state.stockAmount>0){
			saveRecord({"remark":"","formid":id,"amount":state.stockAmount}).then((res)=>{
				if(res.data){
					ElMessage.success("入库成功!");
					load();
				}
			});
		}
	}
	function stopAssembly(){
		ElMessageBox.confirm('确认终止单据吗?', '提示', {
		 				confirmButtonText: '确定',
		 				cancelButtonText: '取消',
		 				type: 'warning',
		 })
		.then(() => {
		  stopAssemblyEvent({"formid":id}).then((res)=>{
		  	if(res.data){
		  		ElMessage.success( "终止成功！");
		  		load();
		  	}
		  });
		})
		.catch(() => ElMessage.info('已取消操作'));
	}
	
	function changeAmount(){
		var amount=parseInt(state.edit_handlecount);
		if(amount>0){
			changeAssAmount({"formid":id,"value":amount.toString()}).then((res)=>{
				if(res.data){
					ElMessage.success( res.data.message);
					state.handlecount=res.data.value;
					load();
				}
			});
		}
	}
	function restartAssembly(){
		resetAssForm({"id":id}).then(res=>{
			load();
		})
	}
	function cancelStock(row){
		assemblyCancelInstock({"instockid":row.id}).then((res)=>{
			if(res.data){
				ElMessage.success( "撤销成功!");
				load();
			}
		});
	}
	
	function showConsDialog(){
		var data=state.warehouseform;
		data.itemlist=[];
		state.tableData.forEach(item=>{
			
			data.itemlist.push({"msku":item.sku,"confirmQuantity":item.amount});
		})
		data.itemlist.push({"msku":state.warehouseform.skuname,"confirmQuantity":state.warehouseform.amount});
		consRef.value.show(null,data);
	}
	
	function load(){
		getData({"formid":id}).then((res)=>{
			if(res.data.asFormEntryList){
				state.tableData=res.data.asFormEntryList;
				state.handlecount=res.data.count;
				state.edit_handlecount=parseInt(res.data.count);
				state.active=parseInt(res.data.status);
				state.recordList=res.data.recordList;
				state.warehouseform=res.data.warehouseform;
				assemblyCompareToSku(state.warehouseform.skuname,state.tableData).then(res=>{
					if(res.data){
						ElMessage.error(res.data);
					}
				})
			}
		});
	}
	
	
	onMounted(()=>{
		load();
	});
</script>

<style>
	.m-b-24{
		margin-bottom: 24px;
	}
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
	.m-b-t-24{
		margin-bottom:24px;
		margin-top:24px;
	}
	.m-b-16{
		margin-bottom: 16px;
	}
	.pro-content{
		font-size: 12px;
	}
	.pro-content .sku{
		margin-top:4px;
		color: var(--el-color-blue);
	}
</style>
