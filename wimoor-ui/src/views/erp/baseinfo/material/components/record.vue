<template>
		<el-popover
		    placement="top"
		    :width="520"
			trigger="click"
			@show="getRecordList()"
		  >
		    <template #reference>
		      <div class="font-extraSmall pointer" v-if="last!=''"  v-html="row.last"></div>
			   <span v-else>-</span>
		    </template>
		      <div class="card-header" >
				  <el-space>
					  <div><span class="font-extraSmall">最近7天消耗：</span>
					  <span v-if="summary" class="font-bold" >{{summary.LAST7}}</span>
					  <span v-else class="font-bold" >0</span>
					  </div>
					  <div style="padding-left:20px;">
						  <span class="font-extraSmall">最近30天消耗：</span>
						  <span v-if="summary" class="font-bold" >{{summary.LAST30}}</span>
					    <span v-else class="font-bold" >0</span>
					  </div>
				  </el-space>
		      </div>
		    <el-table :data="recordlist" >
		    	                <el-table-column label="日期" prop="createdate" width="120">
		    						<template #default="scope">
		    						   {{dateFormat(scope.row.createdate)}}
		    						</template>
		    					</el-table-column>
		    	                <el-table-column label="数量" prop="amount"></el-table-column>
		    					<el-table-column label="状态"  prop="auditstatusname">
		    					</el-table-column>
		    					<el-table-column label="已入库"    prop="totalin"></el-table-column>
		    					<el-table-column label="已付款"   prop="totalpay"></el-table-column>
		    </el-table>
		</el-popover>
</template>

<script setup>
	import { ref ,watch,reactive,onMounted,toRefs,h} from 'vue';
	import {getLast3,getLast} from '@/api/erp/purchase/plan/planApi.js';
	import outApi from '@/api/erp/inventory/outApi.js';
	import {dateFormat,getCurrencyMark} from '@/utils/index.js';
	const state = reactive({
		   loading:true,
		   recordlist:[],
		   summary:{},
		   last:null,
	});
	const {
		 loading,
		 recordlist,
		 summary,
		 last
	} = toRefs(state);
	let props = defineProps({ row:{}});
	const { row} = toRefs(props);
	var isRun=false;
	function getRecordList(){
		getLast3({"id":props.row.id}).then((res)=>{
			state.recordlist=res.data.list;
		});
		outApi.getlast({warehouseid:props.row.warehouseid,materialid:props.row.id}).then(res=>{
			state.summary=res.data;
		})
	}
	 
 
</script>

<style scoped>
	.card-header{
		padding:10px;
	}
</style>