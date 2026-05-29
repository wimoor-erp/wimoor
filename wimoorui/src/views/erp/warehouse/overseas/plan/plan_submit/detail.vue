<template>
	<div class="height-screen">
		<div class="con-header ">
		<el-button type="primary" @click="submitForm">采购</el-button>
		</div>
		<el-row :gutter="16" class="m-t-16">
			<el-col :span="16">
				<el-card shadow="never">
					<el-space >
						<data-sheet class="ic-cen" theme="filled" size="20" fill="#FF6700"/>
						 <h5> 采购合计
						 <span v-if="planData.groupname">-{{planData.groupname}}</span>
						 </h5>
					</el-space>
				<div class="con-body">
					<el-row >
						<el-col :span="6">
							<div class="flex-v-bet">
								<span class="title">成品SKU数量</span>
								<span class="data">{{summary.sumNum}}</span>
							</div>
						</el-col>	
						<el-col :span="6">
							<div class="flex-v-bet">
								<span class="title">采购成品数量</span>
								<span class="data">{{summary.sumAmount}}</span>
							</div>
						</el-col>
						<el-col :span="6">
							<div class="flex-v-bet">
								<span class="title">预估成本</span>
								<span class="data" v-if="summary.sumPrice">￥{{formatFloat(summary.sumPrice)}}</span>
								<span class="data" v-else>￥0</span>
							</div>
						</el-col>
						<el-col :span="6">
							<div class="flex-v-bet">
								<span class="title">预估总成本</span>
								<span class="data text-green" v-if="summary.sumPrice">￥{{formatFloat(summary.sumPrice)+formatFloat(summary.sumAssPrice)}}</span>
								<span class="data" v-else>￥0</span>
							</div>
						</el-col>
					</el-row>
					<el-row class="m-t-16">
						 <el-col :span="6">
						 	<div class="flex-v-bet">
						 		<span class="title">组装SKU数量</span>
						 		<span class="data">{{summary.sumAssNum}}</span>
						 	</div>
						 </el-col>	
						 <el-col :span="6">
						 	<div class="flex-v-bet">
						 		<span class="title">采购组装SKU数量</span>
						 		<span class="data">{{summary.sumAssAmount}}</span>
						 	</div>
						 </el-col>
						 <el-col :span="6">
						 	<div class="flex-v-bet">
						 		<span class="title">预估组装SKU成本</span>
						 		<span class="data" v-if="summary.sumAssPrice">￥{{formatFloat(summary.sumAssPrice)}}</span>
						 		<span class="data" v-else>￥0</span>
						 	</div>
						 </el-col>
						 
					</el-row>
				</div>
				</el-card>
			</el-col>
		<el-col :span="8">
				<el-card shadow="never">
					<el-space >
					<bookmark-one class="ic-cen"  theme="filled" size="18" fill="#e6a23c"/>
				 <h5>历史采购</h5>
				 </el-space>
				 <div class="">
				 	<ul class="font-small ul-list" v-if="hislist.length>0">
				 		<li v-for="his in hislist"><span>{{his.opttime}} </span><span>  
						 <span style="margin-left:10px;"  class="font-extraSmall"> 采购数量：</span>
						 {{his.amount}} </span>
						   <el-tag  style="margin-left:10px;" :type="primary" > {{his.batchnumber}}</el-tag>
						 </li>
				 	</ul>
					<p v-else class="text-center font-extraSmall">暂无记录！</p>
				 </div>
				</el-card>
			</el-col>
		</el-row>
		<p class="font-extraSmall m-t-16 m-b-8">去掉勾选的产品，可分批次或部分采购</p>
		<el-table :data="planData.list" height="calc(100vh - 410px)" @selection-change="handleSummary"  border ref="tableRef">
			<el-table-column type="selection"></el-table-column>
			<el-table-column label="图片"  width="65">
				<template #default="scope">
					<el-image :src="scope.row.image"></el-image>
				</template>
			</el-table-column>
			<el-table-column label="名称/msku" prop="sku" show-overflow-tooltip>
				<template #default="scope">
					<div class="name">{{scope.row.name}}</div>
					<div class="sku">{{scope.row.sku}} 
					<span v-if="scope.row.issfg=='1'" @click.stop="handleEmtpy" >
					<AssemblyDialog @change="getAssembliyList(scope.row)" :loading="scope.row.assloading" :assemblyList="scope.row.assemblyList">
											  <template #field>
												  <el-tag class="pointer" type="warning" size="small" v-if="scope.row.issfg=='1'"
												  >组合</el-tag>
											  </template>
					</AssemblyDialog>
					</span>
					</div>
				</template>
			</el-table-column>
			<el-table-column label="采购成本" prop="price" width="180" sortable >
				<template #default="scope">
				   <div>￥{{scope.row.price}}</div>
				</template>
			</el-table-column>
			<el-table-column label="供应商" prop="supplier"  sortable />
			<el-table-column label="供货周期" prop="delivery_cycle" width="100" sortable >
				<template #default="scope">
					{{scope.row.delivery_cycle}}天
				</template>
			</el-table-column>
			<el-table-column label="待入库" prop="inbound" width="100" sortable ></el-table-column>
			<el-table-column label="待出库" prop="outbound" width="100" sortable ></el-table-column>
			<el-table-column label="可用库存" prop="fulfillable" width="120" sortable ></el-table-column>
			<el-table-column label="采购总量"  prop="amount" width="100" sortable ></el-table-column>
			<el-table-column label="采购金额"  prop="amount" width="100" sortable >
				<template #default="scope">
				   <div>￥{{formatFloat(scope.row.price*scope.row.amount)}}</div>
				</template>
			</el-table-column>
		</el-table>
	</div>
</template>

<script setup>
import { ref ,nextTick ,reactive,onMounted,toRefs,watch,inject} from 'vue'
import shipmentApi from "@/api/amazon/inbound/shipmentApi.js";
import AssemblyDialog from "@/views/erp/components/assembly_dialog.vue";
import { useRouter } from 'vue-router'
import {formatFloat} from '@/utils/index.js';
import {sublit} from "@/api/erp/assembly/assemblyApi.js";
import planApi from '@/api/erp/ship/planApi.js';
import groupApi from '@/api/amazon/group/groupApi.js';
import warehouseApi from '@/api/erp/warehouse/warehouseApi.js';
import {batchItem,listHisItem} from '@/api/erp/purchase/plan/planApi.js';
const emitter = inject("emitter");
let tableRef = ref()
let props = defineProps({
  	 	                       planData:{list:[]},
		                       planid:"",
							   warehouseid:"",
  	                       });
	const { planData,planid,warehouseid} = toRefs(props);
	let state = reactive({     transtype:"",
		                       summary:{sumNum:0,sumAmount:0,sumPrice:0,
							            sumAssNum:0,sumAssAmount:0,sumAssPrice:0
							            },
	 	                       hislist:[],
							   groupid:""
	                       });
						    
						 
	const { hislist,summary} = toRefs(state);
let router = useRouter() ;
	watch(props.planData,()=>{
		handleSummary(props.planData.list);
	})
	async function handleSummary(rows){
		state.summary.sumNum=0;
		state.summary.sumAmount=0;
		state.summary.sumPrice=0;
		state.summary.sumAssNum=0;
		state.summary.sumAssAmount=0;
		state.summary.sumAssPrice=0;
		rows.forEach(row=>{
			var amount=parseInt(row.amount);
			if(row.groupid){
				state.groupid=row.groupid;
			}
			if(row.issfg=="1"){
				state.summary.sumAssNum+=1 ;
				state.summary.sumAssAmount+=amount;
				if(row.price){
					state.summary.sumAssPrice+=parseFloat(row.price)*amount;
				}
			}else{
				state.summary.sumNum+=1 ;
				state.summary.sumAmount+=amount;
				if(row.price){
					state.summary.sumPrice+=parseFloat(row.price)*amount;
				}
			}
		});
	}
	function getAssembliyList(row){
		if(!row["assemblyList"]){
			row.assloading=true;
			sublit({materialid:row.materialid,warehouseid:row.warehouseid}).then(res=>{
				row.assemblyList=res.data;
				row.assloading=false;
			});
		}
	}
	function submitForm(){
		var param={};
	    var rows=tableRef.value.getSelectionRows();
		batchItem(rows).then(res=>{
			emitter.emit("removeCache", "添加采购单");
			router.push({
				path:"/e/p/o",
				query:{
					title:'添加采购单',
					path:"/e/p/o",
					batchnumber:res.data,
					"planid":props.planid,
					"warehouseid":props.warehouseid,
					"groupid":state.groupid
				},
			}) 
		})
	}
	onMounted(async()=>{
		setTimeout(function(){
		         tableRef.value.toggleAllSelection();
		},500);
		await listHisItem(props.warehouseid).then(res=>{
			state.hislist=res.data;
		});
	})
  
</script>

<style scoped="scoped">
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
		margin-bottom: 16px;
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