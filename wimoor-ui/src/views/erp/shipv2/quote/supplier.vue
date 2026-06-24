<template>
	
	<el-dialog   v-model="visiable" title="报价供应商信息" width="600px">
			 <el-table  :data="tableData"  @selection-change="handleSelectionChange">
				 <el-table-column fixed type="selection" width="38" />
				  <el-table-column prop="name" label="名称"   />
			 	 <el-table-column prop="createtime" label="创建时间"   >
					 <template #default="scope">
					 	  {{dateTimesFormat(scope.row.createtime)}} 
					 </template>
				</el-table-column>
			 </el-table>
		  <template #footer>
		  		<el-button @click="sendOrder"   type="primary">确认发送</el-button>
		  	<el-button @click="visiable=false"  >关闭</el-button>
		  </template>
	</el-dialog>
 
</template>

<script setup>
	import {Search,ArrowDown,} from '@element-plus/icons-vue'
	import {MenuUnfold,SettingTwo,Help,Copy,MoreOne} from '@icon-park/vue-next';
	import { ref,reactive,onMounted,watch,toRefs,nextTick } from 'vue'
	import thirdpartyApi from "@/api/erp/thirdparty/thirdpartyApi.js";
	import shipmentplanApi from '@/api/erp/shipv2/shipmentplanApi.js';
	import shipmentPlacementApi from '@/api/erp/shipv2/shipmentPlacementApi.js';
	import CopyText from"@/utils/copy_text.js";
	import orderApi from '@/api/quote/orderApi.js';
	import supplierApi from '@/api/quote/supplierApi.js';
	import Datepicker from '@/components/header/datepicker.vue';
	import {dateFormat,dateTimesFormat,CheckInputFloat,formatFloat} from '@/utils/index';
	import {ElMessage,ElMessageBox } from 'element-plus';
	const emit = defineEmits(['change']);
	const state = reactive({
		visiable:false,
		 token:null,
		 type:"list",
		 tableData:[],
		 selectRows:null,
		 orders:null,
	
		 title:'新增',
	})
	const{
		visiable,tableData,token,selectRows,orders,type,supplier,title,
		}=toRefs(state)
	  
	 function loadSupplier(){
		 var datas={};
		 datas.token=state.token;
		 supplierApi.listsupplier(datas).then((res)=>{
			 state.tableData=res.data;
		 });
	 }
	 function handleSelectionChange(selects){
		 state.selectRows=selects;
	 }
	 
	 function sendOrder(){
		var datas={};
		if(state.selectRows && state.selectRows.length>0){
			var orderList=[];
			var supplierList=[];
			state.orders.forEach(item=>{
				orderList.push(item.id);
			});
			state.selectRows.forEach(items=>{
				supplierList.push(items.id);
			});
			
			datas.orderids=orderList;
			datas.supplierid=supplierList;
			supplierApi.sendOrderToSupplier(datas).then((res)=>{
				 ElMessage.success("发送成功!");		
				 emit("change");
				  state.visiable=false;
			});
		}else{
			 ElMessage.error("至少选择一行数据!");
		}
		 
	 }
	
	 function show(token,orders){
			state.orders=orders;
	 		state.token=token;
	 		state.visiable=true;
	 		loadSupplier();
	 }
	
	defineExpose({show});
</script>

<style>
</style>