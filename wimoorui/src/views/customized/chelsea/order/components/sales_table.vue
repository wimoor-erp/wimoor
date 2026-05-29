<template>
	    <SalesHeader ref="headerRef" @getdata="changedata"></SalesHeader>
		<div class="con-body">
		<GlobalTable v-if="queryParams.displayType=='product'" 
						ref="globalTable1" 
						:defaultSort="defaultSort"  
						@selectionChange='selectChange' 
						:tableData="tableData" 
						height="calc(100vh - 300px)" 
						:stripe="true"  
						@loadTable="loadTableData" 
						style="width: 100%;margin-bottom:16px;">
				<template #field>
				<el-table-column fixed type="selection" width="38" />
				<el-table-column label="平台" prop="platformname" />
				<el-table-column label="国家" prop="country" />
				<el-table-column label="图片" prop="image" >
					<template #default="scope">
							<el-image :src="scope.row.image"  style="width:45px;height:45px"></el-image>
						</template>
				</el-table-column>
				<el-table-column label="名称/SKU" prop="name" show-overflow-tooltip>
				<template #default="scope">
							<div>{{scope.row.name}}</div>
							<div>{{scope.row.sku}}</div>
						</template>
				</el-table-column>
				<el-table-column label="销量" prop="quantity" width="120">
					 
				</el-table-column>
				<el-table-column label="销售额" prop="price"/>
				<el-table-column label="邮费" prop="ship_fee"/>
				<el-table-column label="平台佣金" prop="referral_fee"/>
				<el-table-column label="平台佣金百分比" prop="referral_rate">
					<template #default="scope">
						<span>{{scope.row.referral_rate}}</span>
					</template>
				</el-table-column>
				<el-table-column label="毛利润" prop="profit"/>
				<el-table-column label="操作" prop="price">
					<template #default="scope">
						<el-button type="primary" link @click="showDetail(scope.row)" >详情</el-button>
					</template>
				</el-table-column>
				</template>
			</GlobalTable>
			<GlobalTable v-if="queryParams.displayType=='order'" 
							ref="globalTable2" 
							:defaultSort="defaultSort"  
							@selectionChange='selectChange' 
							:tableData="tableData" 
							height="calc(100vh - 300px)" 
							:stripe="true"  
							@loadTable="loadTableData" 
							style="width: 100%;margin-bottom:16px;">
					<template #field>
					<el-table-column fixed type="selection" width="38" />
					<el-table-column label="平台" prop="platformname"  >
						<template #default="scope">
							<div>{{scope.row.platformname}}</div>
							<div v-if="scope.row.thirdparty_warehousename">{{scope.row.thirdparty_warehousename}}</div>
							</template>
							</el-table-column>
					<el-table-column label="国家" prop="country" />		
					<el-table-column label="订单" prop="order_id" />
					<el-table-column label="图片" prop="image" >
						<template #default="scope">
								<el-image :src="scope.row.image" style="width:45px;height:45px"></el-image>
							</template>
					</el-table-column>
					<el-table-column label="名称/SKU" prop="name" show-overflow-tooltip>
					<template #default="scope">
								<div>{{scope.row.name}}</div>
								<div>{{scope.row.sku}}</div>
							</template>
					</el-table-column>
					<el-table-column label="销量" prop="quantity" width="120">
					</el-table-column>
					<el-table-column label="是否出库" prop="isout" show-overflow-tooltip>
					<template #default="scope">
								<div>{{scope.row.warehousename}}</div>
								<el-tag v-if="scope.row.isout">是</el-tag>
								<el-tag v-else>否</el-tag>
							</template>
					</el-table-column>
					<el-table-column label="销售额" prop="price"/>
					<el-table-column label="邮费" prop="ship_fee"/>
					<el-table-column label="平台佣金" prop="referral_fee"/>
					<el-table-column label="平台佣金百分比" prop="referral_rate">
						<template #default="scope">
							<span>{{scope.row.referral_rate}}</span>
						</template>
					</el-table-column>
					<el-table-column label="毛利润" prop="profit"/>
					<el-table-column label="操作" prop="price">
						<template #default="scope">
							  <el-button type="primary" link @click="deletes(scope.row)" >删除</el-button>
						</template>
					</el-table-column>
					</template>
				</GlobalTable>
		</div>
		<el-dialog v-model="visible" class="invheader" title="订单详情" top="8vh" width="80%">
			     <GlobalTable  
			     				ref="globalTable3" 
			     				:defaultSort="defaultSort"  
			     				:tableData="tableData2" 
			     				height="calc(100vh - 300px)" 
			     				:stripe="true"  
			     				@loadTable="loadTableData2" 
			     				style="width: 100%;margin-bottom:16px;">
			     		<template #field>
			     		<el-table-column label="平台" prop="platformname" />
						<el-table-column label="国家" prop="country" />		
			     		<el-table-column label="订单" prop="order_id" />
			     		<el-table-column label="图片" prop="image" >
			     			<template #default="scope">
			     					<el-image :src="scope.row.image" style="width:45px;height:45px"></el-image>
			     				</template>
			     		</el-table-column>
			     		<el-table-column label="名称/SKU" prop="name" show-overflow-tooltip>
			     		<template #default="scope">
			     					<div>{{scope.row.name}}</div>
			     					<div>{{scope.row.sku}}</div>
			     				</template>
			     		</el-table-column>
			     		<el-table-column label="销量" prop="quantity" width="120">
			     		</el-table-column>
			     		<el-table-column label="是否出库" prop="isout" show-overflow-tooltip>
			     		<template #default="scope">
			     					<div>{{scope.row.warehousename}}</div>
			     					<el-tag v-if="scope.row.isout">是</el-tag>
			     					<el-tag v-else>否</el-tag>
			     				</template>
			     		</el-table-column>
			     		<el-table-column label="销售额" prop="price"/>
			     		<el-table-column label="邮费" prop="ship_fee"/>
			     		<el-table-column label="平台佣金" prop="referral_fee"/>
			     		<el-table-column label="平台佣金百分比" prop="referral_rate">
			     			<template #default="scope">
			     				<span>{{scope.row.referral_rate}}</span>
			     			</template>
			     		</el-table-column>
			     		<el-table-column label="毛利润" prop="profit"/>
			     		<el-table-column label="操作" prop="price">
			     			<template #default="scope">
			     				  <el-button type="primary" link @click="deletes(scope.row)" >删除</el-button>
			     			</template>
			     		</el-table-column>
			     		</template>
			     	</GlobalTable>
		</el-dialog>
</template>
<script>
    export default{ name:"调库单" };
</script>
<script setup>
	import SalesHeader from"./sales_header.vue";
	import {ref,reactive,toRefs,onMounted,nextTick}from"vue"
	import {MoreOne,} from '@icon-park/vue-next';
	import {Histogram} from '@element-plus/icons-vue'
	import { ElMessage, ElMessageBox, ElDivider } from 'element-plus';
	import {useRouter } from 'vue-router';
	import {dateFormat,dateTimesFormat} from '@/utils/index.js';
	import orderApi from "@/api/erp/order/orderApi.js";
	const router = useRouter();
	let globalTable1=ref();
	let globalTable2=ref();
	let globalTable3=ref();
	let headerRef=ref();
	const state = reactive({
		visible:false,
		tableData:{records:[],total:0},
		tableData2:{records:[],total:0},
		queryParams:{displayType:"product"},
		defaultSort:{"prop": 'opttime', "order": 'desc' },
		selectrows:[],
		detailParam:{},
	})
	const {
		visible,
		tableData,
		tableData2,
		queryParams,
		defaultSort,
		selectrows,
		detailParam,
	}=toRefs(state)
	 
	function changedata(params){
		state.queryParams.paramother=params;
		state.queryParams.displayType=params.displayType;
		state.queryParams.sort=state.defaultSort.prop;
		state.queryParams.order=state.defaultSort.order;
		nextTick(()=>{
			handleQuery();
		})
	}
	function handleQuery(){
		if(state.queryParams.displayType=="product"){
			globalTable1.value.loadTable(state.queryParams);
		}else{
			globalTable2.value.loadTable(state.queryParams);
		}
	}
	function showDetail(row){
		state.visible=true;
		state.detailParam=JSON.parse(JSON.stringify(state.queryParams));
		state.detailParam.paramother.sku=row.sku;
		state.detailParam.paramother.platformid=row.platform_id;
		 nextTick(()=>{
			globalTable3.value.loadTable(state.detailParam);
		});
	}
	function deletes(row){
		orderApi.remove({"id":row.id}).then((res)=>{
			ElMessage.success("删除成功");
			handleQuery();
			globalTable3.value.loadTable(state.detailParam);
		})
	}
	function selectChange(selection) {
		state.selectrows=selection;
	}
	function loadTableData2(params){
		orderApi.findOrderByCondition(params).then((res)=>{
			state.tableData2.records = res.data.records;
			state.tableData2.total =res.data.total;
		})
	}
	function loadTableData(params){
		if(state.queryParams.displayType=="product"){
			orderApi.findByCondition(params).then((res)=>{
				state.tableData.records = res.data.records;
				state.tableData.total =res.data.total;
			})
		}else{
			orderApi.findOrderByCondition(params).then((res)=>{
				state.tableData.records = res.data.records;
				state.tableData.total =res.data.total;
			})
		}
	}
	
	onMounted(() => {
	   
	});
</script>

<style>
</style>
