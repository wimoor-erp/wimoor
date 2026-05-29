<template>
	<div class="el-white-bg">
	<el-scrollbar class="he-scr-car" @scroll="scroll">
							 <div class="gird-line-head">
							 <h3>出库单详情</h3>
							 <el-button   class='ic-btn' title='帮助文档'>
							   <help theme="outline" size="16" :strokeWidth="3"/>
							 </el-button>
							 </div>
							 <div class="fill-from-body">
			<el-descriptions :column="2">
			    <el-descriptions-item label="单据编码">{{warehouseform.number}}</el-descriptions-item>
				<el-descriptions-item label="收件人">{{warehouseform.customers}}</el-descriptions-item>
			    <el-descriptions-item label="出库仓库">{{warehouseform.warehouse}}</el-descriptions-item>
			    <el-descriptions-item label="快递物流">{{warehouseform.express}}</el-descriptions-item>
				<el-descriptions-item label="出库申请人">{{warehouseform.creator}}</el-descriptions-item>
			    <el-descriptions-item label="物流单号">{{warehouseform.expressno}}</el-descriptions-item>
				<el-descriptions-item label="出库时间">{{dateTimesFormat(warehouseform.opttime)}}</el-descriptions-item>
			    <el-descriptions-item label="发货地址">{{warehouseform.toaddress}}</el-descriptions-item>
				<el-descriptions-item label="备注">
				 {{warehouseform.remark}}
				</el-descriptions-item>
			  </el-descriptions>
			  <el-divider></el-divider>
			  	 <div class="mark-re">
			  	   <h5 >出库商品 </h5>
				   </div>
			  	 <el-table border :data="outFormEntryList">
			  		 <el-table-column label="序号" type="index" width="50"/> 
			  		 <el-table-column  prop="image" label="图片"  width="65">
			  		    <template #default="scope">
			  		     <el-image v-if="scope.row.image" :src="scope.row.image"   width="40" height="40"  ></el-image>
			  		 	<el-image v-else :src="$require('empty/noimage40.png')"   width="40" height="40"  ></el-image>
			  		   </template>
			  		 </el-table-column>
			  		 <el-table-column  prop="name" label="名称/SKU" width="350"  show-overflow-tooltip>
			  		    <template #default="scope">
			  		       <div class='name'>{{scope.row.name}}</div>
			  		       <div class='sku'>{{scope.row.sku}}
			  		       </div>
			  		   </template>
			  		 </el-table-column>
			  		 <el-table-column label="出库数量" prop="amount">
			  			   <template #default="scope">
			  				  {{scope.row.amount}}
							  <shelfInvOpt
							   :materialid="scope.row.materialid"
							   :formid="scope.row.id"
							   :warehouseid="warehouseform.warehouseid"
							   :amount="scope.row.amount"
							   opt="0"
							   formtype="otherout"></shelfInvOpt>
			  			  </template>   
						
			  		 </el-table-column>
			  		 <el-table-column label="可用库存" prop="fulfillable">
			  		 </el-table-column>
			  	 </el-table>
				 </div>
			</el-scrollbar>
				 					<div class='text-center mar-top-16'>
				 						 <div style="padding-top:10px;">
				 							<el-space>
												<el-button type="primary" @click.stop="showConsDialog">辅料出库</el-button>
				 								<el-button  @click.stop="closePage">关闭</el-button>
				 							</el-space>
				 						</div>
				 					</div>
	  <Consumable ref="consRef"></Consumable>
	  </div>
</template>
<script setup>
	import {Plus,Minus,Help} from '@icon-park/vue-next';
	import {ArrowDown,Edit,View,Upload,Download} from '@element-plus/icons-vue';
	import { ref,reactive,onMounted,watch,toRefs,inject } from 'vue';
	import { ElMessage, ElMessageBox } from 'element-plus';
	import shelfInvOpt from "@/views/erp/components/shelfInvOpt.vue";
	import {dateFormat,dateTimesFormat} from '@/utils/index.js';
	import outApi from '@/api/erp/inventory/outApi.js';
	import {useRouter } from 'vue-router';
	import {redirectToList} from '@/utils/page_helper.js';
	import Consumable from '@/views/erp/baseinfo/material/consumableDialog.vue';
	const emitter = inject("emitter"); // Inject `emitter`
	const router = useRouter();
	const consRef=ref();
	const id = router.currentRoute.value.query.id;
	const state = reactive({
		warehouseform:{},
		outFormEntryList:[],
	})
	const{
		warehouseform,
		outFormEntryList,
	}=toRefs(state)
	function closeTab(){
		emitter.emit("removeTab", 100);
	};
    function load(){
		outApi.getData({"id":id}).then((res)=>{
			if(res.data){
				state.warehouseform=res.data.warehouseform;
				state.outFormEntryList=res.data.outFormEntryList;
			}
		});
	}
	function closePage(){
              redirectToList(router,"/erp/warehouse/outbound",'出库单详情');
	}
	function showConsDialog(){
		   var data={};
		   var skulist=[];
		   data.warehouseid=state.warehouseform.warehouseid;
		   data.number=state.warehouseform.number+"-cons";
		   state.outFormEntryList.forEach(item=>{
			   if(item.sku){
				   var row={};
				   row.qty=item.amount;
				   row.sku=item.sku;
				   skulist.push(row);
			   }
		   });
		   data.skulist=skulist;
		   consRef.value.show(data);
	}
	defineExpose({
		 
	})
	onMounted(()=>{
		load();
	});
</script>

<style scoped>
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
