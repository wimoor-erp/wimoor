<template>
	<GlobalTable ref="globalTable" :defaultSort="defaultSort"   :tableData="tableData" height="calc(100vh - 200px)" :stripe="true"  @loadTable="loadTableData" style="width: 100%;margin-bottom:16px;">
		<template #field>
		<el-table-column label="图片" prop="image" width="80">
			<template #default="scope">
				<el-image v-if="scope.row.image" :src="scope.row.image"  class="product-img"   ></el-image>
				<el-image v-else :src="$require('empty/noimage40.png')"  class="product-img"  ></el-image>
			</template>
		</el-table-column>
		<el-table-column  prop="sku" label="名称/SKU" sortable="custom" width="240" show-overflow-tooltip>
		   <template #default="scope">
		      <div class='name'>{{scope.row.name}}</div>
		      <div class='sku'>{{scope.row.sku}}
		        <copy class="" @click="CopyText(scope.row.name)" title='复制名称' theme="outline" size="14" fill="#666" :strokeWidth="3"/>
		      </div>
		  </template>
		</el-table-column>
		<el-table-column label="单据类型" prop="typename">
			<template #default="scope">
				<el-tag v-if="scope.row.typename=='组装'"  type="warning">加工单</el-tag>
				<el-tag v-else-if="scope.row.typename=='调库'"  type="success">调库单</el-tag>
				<el-tag v-else-if="scope.row.typename=='库存转移'"  type="success">代料单</el-tag>
				<el-tag v-else-if="scope.row.typename=='其它入库'"  type="success">入库单</el-tag>
				<el-tag v-else-if="scope.row.typename=='其它出库'"  type="danger">出库单</el-tag>
				<el-tag v-else-if="scope.row.typename=='发货出库'"  type="danger">发货单</el-tag>
				<el-tag v-else-if="scope.row.typename=='采购'"  type="warning">采购单</el-tag>
				<el-tag v-else-if="scope.row.typename=='盘点'"  type="warning">盘点单</el-tag>
				<el-tag v-else >{{scope.row.typename}}单</el-tag>
			</template>
		</el-table-column>
		<el-table-column label="单据编码" prop="number" width="150"></el-table-column>
		<el-table-column label="操作行为" prop="operatename">
			<template #default="scope">
				<div v-if="scope.row.outbound>0 && !scope.row.fulfillable">出库准备</div>
				<div v-else-if="scope.row.inbound>0 && !scope.row.fulfillable">入库准备</div>
				<div v-else-if="scope.row.outbound<0">出库</div>
				<div v-else-if="scope.row.fulfillable>0">入库</div>
				<div v-else-if="scope.row.fulfillable<0">出库</div>
			</template>
		</el-table-column>
		<el-table-column label="初始库存" prop="startfulfillable">
			<template #default="scope">
				 <p>可用库存:{{NullTransform(scope.row.startfulfillable)}}</p>
				 <p>待入库:{{NullTransform(scope.row.startinbound)}}</p>
				 <p>待出库:{{NullTransform(scope.row.startoutbound)}}</p>
			</template>
		</el-table-column>
		<el-table-column label="库存变动" prop="fulfillable">
			<template #default="scope">
				 <p v-if="scope.row.endfulfillable && scope.row.endfulfillable>=scope.row.startfulfillable">可用库存:
				 <span style="color:#33a610">+</span>{{getNumValue(scope.row.endfulfillable-scope.row.startfulfillable)}}</p>
				 <p v-else>可用库存:
				 <span style="color:#fa0000">-</span>{{getNumValue(scope.row.startfulfillable-scope.row.endfulfillable)}}</p>
				 <p v-if="scope.row.endinbound>=scope.row.startinbound">待入库:
				 <span style="color:#33a610">+</span>{{getNumValue(scope.row.endinbound-scope.row.startinbound)}}</p>
				 <p v-else>待入库:
				 <span style="color:#fa0000">-</span>{{getNumValue(scope.row.startinbound-scope.row.endinbound)}}</p>
				 <p v-if="scope.row.endoutbound>=scope.row.startoutbound">待出库:
				 <span style="color:#33a610">+</span>{{getNumValue(scope.row.endoutbound-scope.row.startoutbound)}}</p>
				 <p v-else>待出库:
				 <span style="color:#fa0000">-</span>{{getNumValue(scope.row.startoutbound-scope.row.endoutbound)}}</p>
			</template>
		</el-table-column>
		<el-table-column label="结余库存" prop="endfulfillable">
			<template #default="scope">
				 <p>可用库存:{{NullTransform(scope.row.endfulfillable)}}</p>
				 <p>待入库:{{NullTransform(scope.row.endinbound)}}</p>
				 <p>待出库:{{NullTransform(scope.row.endoutbound)}}</p>
			</template>
		</el-table-column>
		<el-table-column label="操作人" prop="operator"></el-table-column>
		<el-table-column label="操作仓库" prop="warehousename"></el-table-column>
		<el-table-column label="操作时间" prop="opttime">
			 <template #default="scope">
				 {{dateTimesFormat(scope.row.opttime)}}
			 </template>
		</el-table-column>
		</template>
	</GlobalTable>
</template>

<script setup>
	import {h,ref,reactive,toRefs} from 'vue';
	import {Download,Edit} from '@element-plus/icons-vue';
	import {MoreOne} from '@icon-park/vue-next';
	import inventoryRecordApi from '@/api/erp/inventory/inventoryRecordApi.js';
	import {dateFormat,dateTimesFormat} from '@/utils/index.js';
	import {useRouter } from 'vue-router';
	import CopyText from"@/utils/copy_text.js";
	import NullTransform from"@/utils/null-transform";
	const emit = defineEmits(['selectrow']);
	const router = useRouter()
	let globalTable=ref();
	let state = reactive({
		tableData:{records:[],total:0},
		queryParams:{},
		defaultSort:{"prop": 'opttime', "order": 'descending' }
	})
	let {
		tableData,
		queryParams,
		defaultSort,
	} = toRefs(state)
	function load(params){
		state.queryParams=params;
		globalTable.value.loadTable(params);
	}
	function loadTableData(params){
		inventoryRecordApi.list(params).then((res)=>{
			state.tableData.records = res.data.records;
			state.tableData.total =res.data.total;
		})
	}
	function getNumValue(value){
		if(isNaN(value)){
			return '-';
		}else{
			return value;
		}
	}
	 
	defineExpose({
	  load,
	})
</script>

<style>

</style>
