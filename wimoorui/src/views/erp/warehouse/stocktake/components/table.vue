<template>
	<GlobalTable ref="globalTable" :defaultSort="defaultSort"   :tableData="tableData" height="calc(100vh - 252px)" :stripe="true"  @loadTable="loadTableData" style="width: 100%;margin-bottom:16px;">
		<template #field>
		<el-table-column type="selection"></el-table-column>
		<el-table-column label="盘点单编码" prop="number" sortable="custom" width="150" ></el-table-column>
		<el-table-column    width="170">
			<template #header>
				名称(<el-tag title="仓库对应的仓位,修改实际库存" size="small"> 仓库 </el-tag> / <el-tag  size="small" title="货架对应,修改货架库存" type="warning"> 库位</el-tag>)
			</template>
			<template #default="scope">
				<div v-if="scope.row.warehouselist" v-for="warehouse in scope.row.warehouselist"><el-tag  style="margin:3px;">{{warehouse.name}}</el-tag ></div>
				<div v-if="scope.row.shelflist" v-for="shelf in scope.row.shelflist"><el-tag type="warning" style="margin:3px;">{{shelf.name}}</el-tag ></div>
			</template>
		</el-table-column>
		<el-table-column label="SKU数量" prop="amount" sortable="custom">
			<template #default="scope">
				<div v-if="scope.row.skunum"><span class="font-extraSmall">仓库:</span>{{scope.row.skunum}}</div>
				<div v-if="scope.row.shelfskunum"><span class="font-extraSmall">库位:</span>{{scope.row.shelfskunum}}</div>
			</template>
		</el-table-column>
		<el-table-column label="盘点数" prop="amount" sortable="custom">
			<template #default="scope">
				<div v-if="scope.row.amount"><span class="font-extraSmall">仓库:</span>{{scope.row.amount}}</div>
				<div v-if="scope.row.amountshelf"><span class="font-extraSmall">库位:</span>{{scope.row.amountshelf}}</div>
			</template>
		</el-table-column>
		<el-table-column label="盘存金额(¥)" align="right" prop="amountprice" sortable="custom">
			<template #default="scope">
				<div v-if="scope.row.amountprice"><span class="font-extraSmall">仓库:</span>{{scope.row.amountprice}} </div>
				<div v-if="scope.row.amountshelfprice"><span class="font-extraSmall">库位:</span>{{scope.row.amountshelfprice}} </div>
			</template>
		</el-table-column>
		<el-table-column label="盘盈金额(¥)" align="right" prop="overamountprice" sortable="custom">
			<template #default="scope">
				    <div v-if="scope.row.overamountprice"> <span class="font-extraSmall">仓库:</span>{{scope.row.overamountprice}}</div>
					<div v-if="scope.row.overamountshelfprice"><span class="font-extraSmall">库位:</span> {{scope.row.overamountshelfprice}}</div>
			</template>
		</el-table-column>
		<el-table-column label="盘亏金额(¥)" align="right" prop="lossamountprice" sortable="custom">
			<template #default="scope">
				<div v-if="scope.row.lossamountprice"> <span class="font-extraSmall">仓库:</span>{{scope.row.lossamountprice}}</div>
				<div v-if="scope.row.lossamountshelfprice"> <span class="font-extraSmall">库位:</span>{{scope.row.lossamountshelfprice}}</div>
			</template>
		</el-table-column>
		 
<!-- 		<el-table-column label="库位"  sortable="custom">
		<el-table-column label="名称" width="160" >
			
		</el-table-column>
		<el-table-column label="盘点数" prop="amountshelf" sortable="custom"></el-table-column>
		<el-table-column label="盘存金额(¥)" align="right" prop="amountshelfprice" sortable="custom"></el-table-column>
		<el-table-column label="盘盈金额(¥)" align="right" prop="overamountshelfprice" sortable="custom"></el-table-column>
		<el-table-column label="盘亏金额(¥)" align="right" prop="lossamountshelfprice" sortable="custom"></el-table-column>
		</el-table-column> -->
		<el-table-column label="盘点状态" prop="isworking">
			<template #default="scope">
				<el-tag v-if="scope.row.isworking==false" type="success">盘点完成</el-tag>
				<el-tag v-else type="warning">正在盘点</el-tag>
			</template>
		</el-table-column>
		<el-table-column label="创建时间" prop="createdate">
			<template #default="scope">
				{{dateFormat(scope.row.createdate)}}
			</template>
		</el-table-column>
		<el-table-column label="备注" prop="remark"></el-table-column>
		<el-table-column label="操作" fixed="right">
			<template #default='scope'>
			<el-space>
				<el-button @click="handleDetails(scope.row)" type="primary" link>详情</el-button>
			  </el-space>	
			</template>
		</el-table-column>
	</template>
	</GlobalTable>
</template>

<script setup>
	import {h,ref,reactive,toRefs} from 'vue'
	import {Download,Edit} from '@element-plus/icons-vue';
	import {MoreOne} from '@icon-park/vue-next';
	import {dateFormat,dateTimesFormat} from '@/utils/index.js';
	import { useRoute,useRouter } from 'vue-router';
	import stocktakingApi from '@/api/erp/inventory/stocktakingApi.js';
	const router = useRouter()
	let detailsRef = ref()
	let globalTable=ref();
	let state = reactive({
		tableData:{records:[],total:0},
		queryParams:{},
		defaultSort:{"prop": 'opttime', "order": 'descending' }
	})
	let {
		tableData,queryParams,defaultSort
	} = toRefs(state)
	function handleDetails(row){
		router.push({
			path:"/e/w/s/d",
			query:{
				title:'盘点单详情',
				path:"/e/w/s/d",
				id:row.id
			},
		})
	}
	function load(params){
		state.queryParams=params;
		globalTable.value.loadTable(params);
	}
	function loadTableData(params){
		stocktakingApi.list(params).then((res)=>{
			state.tableData.records = res.data.records;
			state.tableData.total =res.data.total;
		})
	}
	defineExpose({
	  load,
	})
</script>

<style>

</style>
