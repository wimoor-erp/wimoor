<template>
  <el-dialog
    v-model="dialogVisible"
     top="1vh"
    width="80%"
    :before-close="handleClose"
  >
  <template #header>
	  <el-tabs v-model="activeTabs" class="demo-tabs" @tab-change="handleClick">
	     <el-tab-pane v-for="item in form.shiplist" :label="item.warehousename" :name="item.warehouseid"></el-tab-pane>
	  </el-tabs>
  </template>
  <div class="flex-between" style="padding-bottom:10px">
	    <h1 style="width:100px;">发货单</h1>  
		<el-steps  style="width:800px;"
		    :active="activeName"
			simple
		    finish-status="success"
		  >
		<!--  <el-steps  v-model="activeName"  :active="1" finish-status="success" simple> -->
		     <el-step title="确认数量" @click="resetQuery" />
		     <el-step title="提交表单" />
		   </el-steps> 
  </div>
 
	<div v-if="activeName==1">
		<el-col :span="24">
			<el-card shadow="never">
				<div class="flex-between">
				<el-space >
					<data-sheet class="ic-cen" theme="filled" size="20" fill="#FF6700"/>
					 <h5> 发货合计  </h5>
				</el-space>
					<el-button style="float: right;" type="primary" @click="nextStep">下一步</el-button>
				 </div>
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
		<el-table  :data="nowList.list" height="calc(100vh - 465px)" @selection-change="handleSummary" style="margin-top: 5px;"  border ref="tableRef">
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
			<el-table-column label="采购总量"  prop="quantity" width="100" sortable ></el-table-column>
			<el-table-column label="采购金额"  prop="amount" width="100" sortable >
				<template #default="scope">
				   <div>￥{{formatFloat(scope.row.price*scope.row.quantity)}}</div>
				</template>
			</el-table-column>
		</el-table>
	</div>
    <StockCreate v-if="activeName==2" ref="stockCreateRef" :isdialog="true" @close="handleClose()"  @confirm="handleconfirm()"  ></StockCreate>
  </el-dialog>
</template>

<script setup>
	import { ref ,reactive,onMounted,toRefs,inject,nextTick} from 'vue';
	import StockCreate from "@/views/erp/warehouse/overseas/stock/components/create.vue";
	import orderPlanApi from '@/api/erp/order/orderPlanApi.js';
	import {CheckInputFloat,CheckInputInt,formatFloat,outputmoney} from '@/utils/index.js';
	const stockCreateRef=ref();
	const emit = defineEmits(['change']);
	const tableRef=ref();
	const state = reactive({
	 dialogVisible:false,
	 activeName:1,
	 form:{},
	 summary:{sumNum:0,sumAmount:0,sumPrice:0,
	          sumAssNum:0,sumAssAmount:0,sumAssPrice:0
	          },
	 selectRows:null,	
	 activeTabs:null,
	 nowList:null,
	});
	const {
	  dialogVisible,form,activeName,summary,selectRows,activeTabs,nowList,
	} = toRefs(state);
	function show(form){
		state.form=form;
		state.dialogVisible=true;
	    if(form.shiplist && form.shiplist.length>0){
			state.activeTabs=form.shiplist[0].warehouseid;
			state.nowList=form.shiplist[0];
		}
		handleSummary(form.shiplist);
		nextTick(()=>{
			 tableRef.value.toggleAllSelection();
		})
	}
	function resetQuery(){
		state.activeName=1;
		handleSummary(state.nowList);
		nextTick(()=>{
			 tableRef.value.toggleAllSelection();
		})
	}
	
	function nextStep(){
		state.activeName=2;
		nextTick(()=>{
			var param=JSON.parse(JSON.stringify(state.nowList));
			param.list=state.selectRows;
			param.fromwarehouseid=state.form.warehouseid;
			param.towarehouseid=state.activeTabs;
			if(param.list&&param.list.length>0){
				param.list.forEach(item=>{
					item.psku=item.sku;
					item.sku=item.msku?item.msku:item.sku;
					item.amount=item.quantity;
					item.name=item.mname;
					item.id=item.materialid;
				})
			stockCreateRef.value.initData(param);
			}
		});
	}
	 
	function handleClose(){
		state.dialogVisible=false;
	}
	function handleconfirm(){
		if(state.selectRows && state.selectRows.length>0){
			state.selectRows.forEach(item=>{
				item.opttime=null;
			});
			orderPlanApi.removeWarehouseItem(state.selectRows).then((res)=>{
				state.dialogVisible=false;
				emit("change");
			});
		}
	}
	async function handleSummary(rows){
		state.selectRows=rows;
		state.summary.sumNum=0;
		state.summary.sumAmount=0;
		state.summary.sumPrice=0;
		state.summary.sumAssNum=0;
		state.summary.sumAssAmount=0;
		state.summary.sumAssPrice=0;
		if(rows && rows.length>0){
			rows.forEach(row=>{
				var amount=parseInt(row.quantity);
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
	}
	function handleClick(val){
		state.activeName=1;
		if(val){
			state.form.shiplist.forEach(item=>{
				if(item.warehouseid==val){
					state.nowList=item;
					handleSummary(state.nowList);
					nextTick(()=>{
						 tableRef.value.toggleAllSelection();
					})
				}
			});
		}
	}
	defineExpose({ show });
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