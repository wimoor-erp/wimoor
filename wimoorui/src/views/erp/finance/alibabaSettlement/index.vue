<template>
	    <div class="main-sty">
		<el-row>
		<el-space>
		<el-button @click="openUpload">
		    <span title="导入后可以搜索买家账单确认收货时间 已保证系统付款与1688账单对应">1688账单明细导入</span>
		</el-button>
		<Datepicker 
		    longtime="ok"
			ref="datepickers"
			:shortIndex="1"
			@changedate="changedate" 
		/>
		</el-space>
		</el-row>
			<div class="card-div alibabasettlement">
			    <el-scrollbar>
			      <div class="scrollbar-flex-content">
			        <el-col :span="8" v-for="item in settlementList" style="margin-right:10px;">
			          <el-card :shadow="item.id==activeSettlementId?'always':'hover'"
					   :class="item.id==activeSettlementId?'active':'normal'"
						@click="handleSettlementChange(item)"> 
			        	    <el-descriptions
			        	       :title="'账单日:'+dateFormat(item.postdate)"
			        	       direction="vertical"
							   body-style="background-color: unset"
			        	       :column="4"
			        	     >
							  <template #extra>
							       <el-button @click.stop="deleteSettlement(item.id)" link :icon="Delete" type="danger"></el-button>
							     </template>
			        	       <el-descriptions-item label="买家账号">{{item.alibabaAccount}}</el-descriptions-item>
			        	       <el-descriptions-item label="入账金额（元）">{{item.amount}}</el-descriptions-item>
			        	       <el-descriptions-item label="订单笔数" :span="2">	{{item.quantity}}</el-descriptions-item>
			        	       <el-descriptions-item label="还款日"  :span="2">
			        	         <el-tag size="small">{{dateFormat(item.paydate)}}</el-tag>
			        	       </el-descriptions-item>
			        		   <el-descriptions-item label="导出时间">{{dateTimesFormat(item.loaddate)}}</el-descriptions-item>
			        	     </el-descriptions>
			           </el-card>
			        </el-col> 
			      </div>
			    </el-scrollbar>
		    
		 
	 
		  </div>
		 
		<div class="card-div">
		<el-tabs
		  v-model="activeName"
		  class="demo-tabs"
		  @tab-change="handleClick"
		>
		  <el-tab-pane label="账单明细" name="order">
			    <OrderRecord ref="orderRecordRef" :inForm="true" @change="orderChange"></OrderRecord>
		  </el-tab-pane>
		  <el-tab-pane label="还款记录" name="pay">
		  		 <PayRecord ref="payRecordRef" :inForm="true"></PayRecord>
		  </el-tab-pane>
		  <el-tab-pane label="退款记录" name="returnOrder">
		  		<ReturnOrderRecord ref="returnOrderRecordRef" :inForm="true"></ReturnOrderRecord>
		  </el-tab-pane>
		  <el-tab-pane label="充退记录" name="returnPay">
		  		 <ReturnPayRecord ref="returnPayRecordRef" :inForm="true"></ReturnPayRecord>
		  </el-tab-pane>
		  <el-tab-pane label="付款明细" name="record">
			   <PaymentRecord ref="paymentRecordRef" :inForm="true"></PaymentRecord>
		  </el-tab-pane>
		</el-tabs>
		</div>
		 
		</div>
<UploadDialog ref="uploadDialogRef" @upload="handleUpload" ></UploadDialog>
</template>

<script setup>
	import {Plus,} from '@icon-park/vue-next';
	import {Search,Delete} from '@element-plus/icons-vue'
	import { ref,reactive,onMounted,watch,toRefs,nextTick} from 'vue'
	import Datepicker from '@/components/header/datepicker.vue';
	import PaymentRecord from '@/views/erp/purchase/report/payment/index.vue';
	import UploadDialog from '@/components/Upload/uploadDialog.vue';
	import OrderRecord from './components/order.vue';
	import PayRecord from './components/pay.vue';
	import ReturnOrderRecord from './components/returnorder.vue';
	import ReturnPayRecord from './components/returnpay.vue';
	import journalApi from '@/api/erp/finances/journalApi.js';
	import faccountApi from '@/api/erp/finances/faccountApi.js';
	import purchaseAlibabaSettlementApi from '@/api/erp/finances/purchaseAlibabaSettlementApi.js';
	import projectApi from '@/api/erp/finances/projectApi.js';
	import { ElMessage, ElMessageBox } from 'element-plus';
	import {dateFormat,dateTimesFormat} from '@/utils/index.js';
	import { useRouter } from 'vue-router';
	const router = useRouter()
	const acct=router.currentRoute.value.query.acct;
	const emit = defineEmits(['changeData']);
	const globalTableRef=ref();
	const uploadDialogRef=ref();
	const finItemRef =ref();
	const paymentRecordRef=ref();
	const finRecordRef =ref();
	const orderRecordRef=ref();
	const payRecordRef=ref();
	const returnOrderRecordRef=ref();
	const returnPayRecordRef=ref();
	const state = reactive({
		feeTypeList:[],
		selectLabel:'0',
		expendRows:[],
		activeSettlementId:"",
		activeName:"order",
		searchWord:'',
		search:"",
		settlementList:[],
		queryParams:{search:'','acct':acct},
		tableData:{records:[],total:0}
	})
	const {
		settlementList,
		feeTypeList,
		selectLabel,
		searchWord,
		search,
		activeName,
		activeSettlementId,
		queryParams,
		tableData,
	} = toRefs(state)
 
	function openUpload(){
		uploadDialogRef.value.show({'template':"PurchaseAlibabaPayDate",'title':"阿里巴巴订单实际付款时间"});
	}
	function handleUpload(formDatas){
			  uploadDialogRef.value.loading();
			  formDatas.append('acct',acct);
			  purchaseAlibabaSettlementApi.uploadPayDateFile(formDatas,()=>{
				  	  		loadSettlementList();
				  	  		uploadDialogRef.value.hide();
			  })
	}
	function handleSettlementChange(item){
		state.activeSettlementId=item.id;
		handleQuery();
	}
	function orderChange(value){
		state.queryParams.settlementid=state.activeSettlementId;
		state.queryParams.search=value;
		state.queryParams.searchtype="orderid";
		state.activeName="record";
	}
	function handleClick(){
		if(state.activeName=="record"){
			nextTick(()=>{
				paymentRecordRef.value.show(state.queryParams);
			});
		}
		if(state.activeName=="order"){
			nextTick(()=>{
				state.queryParams.search="";
				state.queryParams.searchtype="";
				orderRecordRef.value.show(state.queryParams);
			});
		}
		if(state.activeName=="pay"){
			nextTick(()=>{
				payRecordRef.value.show(state.queryParams);
			});
		}
		if(state.activeName=="returnOrder"){
			nextTick(()=>{
				returnOrderRecordRef.value.show(state.queryParams);
			});
		}
		if(state.activeName=="returnPay"){
			nextTick(()=>{
				returnPayRecordRef.value.show(state.queryParams);
			});
		}
	}
	function showAccDialog(){
		if(state.queryParams.acc){
		   finRecordRef.value.show(state.queryParams.acc);
		}else{
			ElMessage.error('请选择一个具体的账户！');
		}
	}
	function handleFeeItem(){
		finItemRef.value.show();
	}
	//日期改变
	function changedate(dates){
		state.queryParams.fromDate=dates.start;
		state.queryParams.toDate=dates.end;
		loadSettlementList();
	}
	 
	function handleDownload(){
		journalApi.downExcelDate(state.queryParams);
	}
	function handleExpandChange(row,expandedRows){
		//查询detail
		state.expendRows=expandedRows;
		if(row.expandTable&&row.expandTable.length==0){
			var data={};
			data.createdate=row.createtime;
			data.project=state.queryParams.project;
			data.search=state.queryParams.search;
			data.acc=state.queryParams.acc;
			journalApi.findDetialByCondition(data).then((res)=>{
				res.data.forEach(item=>{
					row.expandTable.push(item);
				})
			});
		}
		
	}
	function tableRowClick(row){
		   globalTableRef.value.toggleRowExpansion(row);
	}
	function handleQuery(){
	    if(state.activeSettlementId){
			handleClick();
		}
	}
	function loadTableData(param){
		journalApi.list(param).then(res=>{
			state.tableData.records=res.data.records;
			state.tableData.total=res.data.total;
			if(res.data.total>0){
				state.tableData.records.forEach(item=>{
					item.expandTable=[];
				});
			}
		});
	}
	function deleteSettlement(value){
		ElMessageBox.confirm(
			'你确定要删除此账期导入的全部数据吗?',
			{
			  confirmButtonText: '确认',
			  cancelButtonText: '取消',
			  type: 'warning',
			  callback:(action)=>{
				 if(action=="confirm"){
					 purchaseAlibabaSettlementApi.deleteSettlement({settlementid:value}).then(res=>{
					 	ElMessage.success('删除成功');
						 loadSettlementList();
					 })
				 }
			  }
			}
		  )
		
	}
	function loadSettlementList(){
		purchaseAlibabaSettlementApi.list(state.queryParams).then(res=>{
			state.settlementList=res.data;
			if(state.settlementList&&state.settlementList.length>0){
			   state.activeSettlementId=state.settlementList[0].id;
			   state.queryParams.settlementid=state.activeSettlementId;
			}else{
				 state.activeSettlementId="#";
				 state.queryParams.settlementid="#";
			}
			handleQuery();
		});
	}
	onMounted(()=>{
		loadSettlementList();
		
	})
	
	</script>

<style scoped="scoped">
	.with-select{
		width: 120px;
	}
  
   .scrollbar-flex-content {
     display: flex;
	 margin-top:20px;
	 margin-bottom:20px;
   }
   .alibabasettlement .active{
    --el-tag-bg-color: var(--el-color-primary-light-9);
    --el-tag-border-color: var(--el-color-primary-light-8);
    --el-tag-hover-color: var(--el-color-primary);
    --el-tag-text-color: var(--el-color-primary);
    background-color: var(--el-tag-bg-color);
    border-color: var(--el-tag-border-color);
    color: var(--el-tag-text-color);
	    
   }
</style>
<style>
	.alibabasettlement .el-descriptions__body {
    background-color:unset !important;
}
</style>
