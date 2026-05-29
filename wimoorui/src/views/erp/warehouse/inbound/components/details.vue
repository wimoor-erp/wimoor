<template>
	<div class="el-white-bg">
		 <el-scrollbar class="he-scr-car" @scroll="scroll">
		 						 <div class="gird-line-head">
		 						 <h3>入库单详情</h3>
		 						 <el-button   class='ic-btn' title='帮助文档'>
		 						   <help theme="outline" size="16" :strokeWidth="3"/>
		 						 </el-button>
		 						 </div>
		 						 <div class="fill-from-body">
			<el-descriptions :column="2">
			    <el-descriptions-item label="单据编码">{{warehouseform.number}}</el-descriptions-item>
			    <el-descriptions-item label="入库仓库">{{warehouseform.warehouse}}</el-descriptions-item>
				<el-descriptions-item label="入库申请人">{{warehouseform.creator}}</el-descriptions-item>
				<el-descriptions-item label="入库时间">{{dateTimesFormat(warehouseform.opttime)}}</el-descriptions-item>
				<el-descriptions-item label="备注">
				 {{warehouseform.remark}}
				</el-descriptions-item>
			  </el-descriptions>
			  <el-divider></el-divider>
			  	 <div class="mark-re">
			  	   <h5 >入库商品 </h5>
				   </div>
			  	 <el-table border :data="inFormEntryList">
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
			  		 <el-table-column label="入库数量" prop="amount">
			  			 <template #default="scope">
			  				 {{scope.row.amount}}
			  				  <shelfInvOpt
			  				   :materialid="scope.row.materialid"
			  				   :formid="scope.row.id"
			  				   :warehouseid="warehouseform.warehouseid"
			  				   :amount="scope.row.amount"
			  				   opt="1"
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
				 				<el-button  @click.stop="closePage">关闭</el-button>
				 			</el-space>
				 		</div>
				 	</div>
					</div>
</template>
<script setup>
	import {Plus,Minus,Help} from '@icon-park/vue-next';
	import {ArrowDown,Edit,View,Upload,Download} from '@element-plus/icons-vue';
	import { ref,reactive,onMounted,watch,toRefs,inject } from 'vue';
	import { ElMessage, ElMessageBox } from 'element-plus';
	import {dateFormat,dateTimesFormat} from '@/utils/index.js';
	import {redirectToList} from '@/utils/page_helper.js';
	import inApi from '@/api/erp/inventory/inApi.js';
	import shelfInvOpt from "@/views/erp/components/shelfInvOpt.vue"
	import {useRouter } from 'vue-router';
	const emitter = inject("emitter"); // Inject `emitter`
	const router = useRouter();
	const id = router.currentRoute.value.query.id;
	const state = reactive({
		warehouseform:{},
		inFormEntryList:[],
	})
	const{
		warehouseform,
		inFormEntryList,
	}=toRefs(state)
	function closeTab(){
		emitter.emit("removeTab", 100);
	};
    function load(){
		inApi.getData({"id":id}).then((res)=>{
			if(res.data){
				state.warehouseform=res.data.warehouseform;
				state.inFormEntryList=res.data.inFormEntryList;
			}
		});
	}
	function closePage(){
		redirectToList(router,"/erp/warehouse/inbound",'入库单');
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
