<template>
	<div class="main-sty">
		<div class="con-header">
	    <SalesHeader ref="headerRef" @getdata="changedata"></SalesHeader>
		</div>
		<div class="con-body">
		<GlobalTable v-if="queryParams.displayType=='product'" 
						ref="globalTable1" 
						:defaultSort="defaultSort"  
						@selectionChange='selectChange' 
						:tableData="tableData" 
						height="calc(100vh - 250px)" 
						:stripe="true"  
						@loadTable="loadTableData" 
						style="width: 100%;margin-bottom:16px;">
				<template #field>
				<el-table-column fixed type="selection" width="38" />
				<el-table-column label="平台" prop="platformname" width="120"/>
				<el-table-column label="国家" prop="country" width="60"/>	
				<el-table-column label="图片" prop="image" width="70px">
					<template #default="scope">
							<el-image :src="scope.row.image"  style="width:45px;height:45px"></el-image>
						</template>
				</el-table-column>
				<el-table-column label="名称/SKU" sortable="custom" prop="sku" width="250px" show-overflow-tooltip>
				<template #default="scope">
							<div>{{scope.row.name}}</div>
							<div>{{scope.row.sku}}
							<span style="padding-left:10px" 
							      class="font-extraSmall"
								  v-if="scope.row.sku!=scope.row.msku">
							 ({{scope.row.msku}})</span>
							</div>
						</template>
				</el-table-column>
	
				<el-table-column label="销量" prop="quantity" sortable="custom" width="120">
					 
				</el-table-column>
				<el-table-column label="销售额"   sortable="custom" prop="price"/>
				<el-table-column label="邮费"   sortable="custom" prop="ship_fee"/>
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
						<el-button type="primary"   link @click="handleBind(scope.row)">绑定</el-button>
					</template>
				</el-table-column>
				</template>
			</GlobalTable>
			<GlobalTable v-if="queryParams.displayType=='order'" 
				ref="globalTable2" 
				:defaultSort="defaultSort"  
				@selectionChange='selectChange' 
				:tableData="tableData" 
				height="calc(100vh - 250px)" 
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
					<el-table-column label="国家" prop="country"  width="60"/>		
					<el-table-column label="订单" prop="order_id" width="180px"/>
					<el-table-column label="购买日期"  sortable="custom" prop="purchase_date" >
						<template #default="scope">
							{{dateFormat(scope.row.purchase_date)}}
						</template>
					</el-table-column>
					<el-table-column label="图片" prop="image" width="70px">
						<template #default="scope">
								<el-image :src="scope.row.image" style="width:45px;height:45px"></el-image>
							</template>
					</el-table-column>
					<el-table-column label="名称/SKU"  sortable="custom" prop="sku" width="250" show-overflow-tooltip>
					<template #default="scope">
								<div>{{scope.row.name}}</div>
								<div>{{scope.row.sku}} </div>
							</template>
					</el-table-column>
					<el-table-column label="销量" prop="quantity"   sortable="custom" width="100">
					</el-table-column>
					<el-table-column label="是否出库" prop="isout" width="250" show-overflow-tooltip>
					<template #default="scope">
								<div>{{scope.row.warehousename}}</div>
								<el-tag v-if="scope.row.isout">是</el-tag>
								<el-tag v-else>否</el-tag>
							</template>
					</el-table-column>
					<el-table-column label="销售额" prop="price"   sortable="custom" width="100"/>
					<el-table-column label="邮费" prop="ship_fee" width="100"/>
					<el-table-column label="平台佣金" prop="referral_fee"/>
					<el-table-column label="平台佣金百分比" prop="referral_rate">
						<template #default="scope">
							<span>{{scope.row.referral_rate}}</span>
						</template>
					</el-table-column>
					<el-table-column label="毛利润" prop="profit"/>
					<el-table-column label="操作" prop="price" width="80">
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
		</div>
		   <MaterialDialog ref="materialDailogRef" @getdata="getRows" type="product" ></MaterialDialog>
</template>
<script>
    export default{ name:"调库单" };
</script>
<script setup>
	import SalesHeader from"./components/sales_header.vue";
	import {ref,reactive,toRefs,onMounted,nextTick}from"vue"
	import {MoreOne,} from '@icon-park/vue-next';
	import {Histogram} from '@element-plus/icons-vue'
	import { ElMessage, ElMessageBox, ElDivider } from 'element-plus';
	import {useRouter } from 'vue-router';
	import MaterialDialog from "@/views/erp/baseinfo/material/materialDialog.vue";
	import {dateFormat,dateTimesFormat} from '@/utils/index.js';
	import orderApi from "@/api/erp/order/orderApi.js";
	import orderPlanApi from "@/api/erp/order/orderPlanApi.js";
	
	const router = useRouter();
	const materialDailogRef=ref();
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
		nowrow:{},
	})
	const {
		visible,
		tableData,
		tableData2,
		queryParams,
		nowrow,
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
	function getRows(rows){
			  if(rows&&rows.length==1){
				  var sku=rows[0].sku;
				  orderPlanApi.bindMsku({"sku":state.nowrow.sku,
				  "msku":sku,
				  "name":state.nowrow.name,
				  "warehouseid":state.nowrow.warehouseid,
				  "country":state.nowrow.country}).then((res)=>{
					  state.nowrow.sku=rows[0].sku;
					  state.nowrow.issfg=rows[0].issfg;
					  state.nowrow.pkgweight=rows[0].weight;
					  state.nowrow.image=rows[0].image;
					  state.nowrow.name=rows[0].name;
					  state.nowrow.fulfillable=rows[0].fulfillable;
					      ElMessage.success("绑定成功！");
				  }).catch(e=>{
					      ElMessage.success("绑定失败！");
				  });
			  }else{
				  ElMessage.error("只能选择一个SKU进行绑定！");
			  }
	}
	function handleBind(row){
			  state.nowrow=row;
			  materialDailogRef.value.show({"search":row.msku,"title":"绑定本地SKU"});
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
